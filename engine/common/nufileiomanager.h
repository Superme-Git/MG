#ifndef __Nuclear_FILEIOMANAGER_H__
#define __Nuclear_FILEIOMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuifileio.h"
#include "nuexecutor.h"
#include "nuxptypes.h"
#include <list>
#include <platform/thread.h>
#include <platform/mutex.h>

namespace Nuclear
{

	class NuclearObject;

	class NuclearFileIOManager : public INuclearFileIO
	{
	public:
		// 使用者应该定义AsyncReadTask子类并实现OnReady(),OnDiscard接口
		// 创建子类对象并调用Submit()添加读取任务
		// 一旦调用了Submit()，在OnReady()或者OnDiscard()接口被回调后才可以并需要删除之
		// Discard() 接口可以取消此读取任务
		class AsyncReadTask : public ExecThread::CallbackTask
		{
		protected:
			NuclearFileIOManager *m_pFileIOMan;
			std::wstring m_filename;
			NuclearBuffer m_data;
			bool m_bLookupSegmPak;

		public:
			AsyncReadTask(NuclearFileIOManager* pFileIOMan, const std::wstring &fn, int iPrior, bool bLookupSegmPak) 
				: ExecThread::CallbackTask(iPrior), m_pFileIOMan(pFileIOMan), m_filename(fn), m_bLookupSegmPak(bLookupSegmPak) { }

			virtual void OnReady() = 0;
			virtual void OnDiscard() = 0;

			NuclearFileIOManager* GetFileIOManager() { return m_pFileIOMan; }
			std::wstring GetFileName() const { return m_filename; }
			NuclearBuffer& GetData() { return m_data; }
			const NuclearBuffer& GetData() const { return m_data; }

			bool Submit() { return m_pFileIOMan->AsyncReadFile(this); }
			bool Discard() { Cancel(); return true; }
			bool IsDiscarded() const { return m_bCanceled; }

			virtual void Execute() { m_pFileIOMan->GetFileImage(m_filename, m_data, m_bLookupSegmPak); }
			virtual void Callback() { m_pFileIOMan->OnAsyncReadFileNotify(this); }
		};
	public:
		NuclearFileIOManager();
		virtual ~NuclearFileIOManager() { }

		bool Init(bool bWritableDefault, bool bApplictionInBuild);
		bool Destroy();

		void OnUpdate(int64 v);

		const std::wstring &GetCurrentDirectory() const { return m_currentDirectory; }

		virtual bool GetFileImage(const std::wstring &filename, NuclearBuffer &data, bool lookupSegmPakPath = true);

		// 简化版本
		bool LoadPObject(const std::wstring &filename, NuclearObject &po);
        
        bool AddTask(Nuclear::ExecThread::Task *pTask);

		bool AsyncReadFile(AsyncReadTask *pTask);

		void OnAsyncReadFileNotify(AsyncReadTask* pTask)
		{
			if( pTask->IsDiscarded() )
			{
				pTask->OnDiscard();
                core::CMutex::Scoped autoMutex(m_MutexReadingTask);
				m_setReadingTask.erase(pTask);
			}
			else
            {
                core::CMutex::Scoped autoMutex(m_MutexResultQueue);
				m_lstResultQueue.push_back(pTask);
            }
		}

		int GetFileIOTaskCount() const {  return m_iIOTaskCount;  }
		int GetFileIOTaskBytes() const {  return m_iIOTaskBytes; }

		size_t GetQueueOfIOTaskSize() const { return m_lstResultQueue.size(); }

		void SetUpdateTimePerFrame(int time) { m_nTimePF = time; }
		int GetUpdateTimePerFrame() const { return m_nTimePF; }

	private:
		bool Init3DFileInput();
		bool Destroy3DFileInput();

		/////////////////////////////////////////////
	private:
		int m_iIOTaskCount;
		int m_iIOTaskBytes;
		int m_nTimePF;//一次Update最多多少时间
	private:
		mutable core::CMutex mMutexForTimeSize;
		int m_iTime;	// 毫秒
		int m_iSize;	// 字节
	public:
		void OnTimer(int iPeriod)
		{
			static int lch = 0;
			if( ++lch % 4 ) return;

			core::CMutex::Scoped autoMutex(mMutexForTimeSize);
			m_iTime = 0;
			m_iSize = 0;
		}
		void OnReady(int iTime/*ms*/, int iSize/*bytes*/)
		{
			core::CMutex::Scoped autoMutex(mMutexForTimeSize);
			m_iTime += iTime;
			m_iSize += iSize;
		}
		float GetAverageReadSpeed() const // 平均速度
		{
			core::CMutex::Scoped autoMutex(mMutexForTimeSize);
			if( m_iTime == 0 ) return 0;
			return m_iSize/1024.f/1024.f;
		}
		/////////////////////////////////////////////

	private:
		NuclearFileIOManager(const NuclearFileIOManager&);
		NuclearFileIOManager& operator=(const NuclearFileIOManager&);

	protected:
        core::CMutex        m_MutexResultQueue;
        core::CMutex        m_MutexReadingTask;
		core::CMutex        m_MutexGetFile;
		std::list<AsyncReadTask*> m_lstResultQueue;
		std::set<AsyncReadTask*> m_setReadingTask;

	private:
		ExecThread m_executor;//这个以后可能要扔出fileioman，作为一个任务线程“池”
		std::wstring m_currentDirectory;
	};

}

#endif