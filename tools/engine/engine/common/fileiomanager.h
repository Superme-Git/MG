#ifndef __Nuclear_FILEIOMANAGER_H
#define __Nuclear_FILEIOMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "ifileio.h"
#include "executor.h"

namespace Nuclear
{

class PObject;

class CFileIOManager : 
	public IFileIO
{
public:
	// 使用者应该定义AsyncReadTask子类并实现OnReady(),OnDiscard接口
	// 创建子类对象并调用Submit()添加读取任务
	// 一旦调用了Submit()，在OnReady()或者OnDiscard()接口被回调后才可以并需要删除之
	// Discard() 接口可以取消此读取任务
	class AsyncReadTask : public Executor::CallbackTask
	{
	protected:
		CFileIOManager *m_pFileIOMan;
		std::wstring m_filename;
		XBuffer m_data;
	public:
		AsyncReadTask(CFileIOManager* pFileIOMan, const std::wstring &fn, int iPrior) 
			: Executor::CallbackTask(iPrior), m_pFileIOMan(pFileIOMan), m_filename(fn) { }
		CFileIOManager* GetFileIOManager() { return m_pFileIOMan; }
		std::wstring GetFileName() const { return m_filename; }
		XBuffer& GetData() { return m_data; }
		const XBuffer& GetData() const { return m_data; }

		bool Submit() { return m_pFileIOMan->AsyncReadFile(this); }
		bool Discard() { Cancel(); return true; }
		bool IsDiscarded() const { return m_bCanceled; }

		virtual void OnReady() = 0;
		virtual void OnDiscard() = 0;

		virtual void Execute()
		{
			m_pFileIOMan->GetFileImage(m_filename, m_data);
		}
		virtual void Callback()
		{
			m_pFileIOMan->OnAsyncReadFileNotify(this);
		}
	};
public:
	CFileIOManager();
	virtual ~CFileIOManager() { }

	bool Init(bool bWritableDefault, bool bApplictionInBuild);
	bool Destroy();
	void OnUpdate(__int64 v);

	virtual bool OnMount(bool bWritableDefault, bool bApplictionInBuild);
	virtual bool OnUnmount();

	// 目前引擎中同步读取PFS文件必须使用此方法，然后使用PObject::LoadFromMem
	virtual bool GetFileImage(const std::wstring &filename, XBuffer &data);

	// 楼上的简化版
	bool LoadPObject(const std::wstring &filename, PObject &po);

	bool AsyncReadFile(AsyncReadTask *pTask);

	void OnAsyncReadFileNotify(AsyncReadTask* pTask)
	{
		if( pTask->IsDiscarded() )
		{
			pTask->OnDiscard();
			m_setReadingTask.erase(pTask);
		}
		else
			m_lstResultQueue.push_back(pTask);
	}

	int GetFileIOTaskCount() const { 
		return m_iIOTaskCount; 
	}
	int GetFileIOTaskBytes() const { 
		return m_iIOTaskBytes;
	}

	size_t GetQueueOfIOTaskSize() const { return m_lstResultQueue.size(); }

	void SetFrame(int f) { m_nFrame = f; }

	void SetUpdateTimePerFrame(int time) { m_nTimePF = time; }
	int GetUpdateTimePerFrame() const { return m_nTimePF; }

	/////////////////////////////////////////////
	// 以下成员和方法临时用于磁盘读取速度的调试统计
private:
	int m_iIOTaskCount;
	int m_iIOTaskBytes;
	int m_nFrame;//多少帧创建一次纹理
	int m_nTimePF;//一次Update最多多少时间
private:
	int m_iTime;	// 毫秒
	int m_iSize;	// 字节
public:
	void OnTimer(int iPeriod)
	{
		static int lch = 0;
		if( ++lch % 4 ) return;
		m_iTime = 0;
		m_iSize = 0;
	}
	void OnReady(int iTime/*ms*/, int iSize/*bytes*/)
	{
		m_iTime += iTime;
		m_iSize += iSize;
	}
	float GetAverageReadSpeed() const // 平均速度
	{
		if( m_iTime == 0 ) return 0;
		//return m_iSize/1024.f/1024.f*1000/m_iTime;
		return m_iSize/1024.f/1024.f;
	}
	/////////////////////////////////////////////

protected:
	std::list<AsyncReadTask*> m_lstResultQueue;
	std::set<AsyncReadTask*> m_setReadingTask;
private:
	Executor m_executor;//这个以后可能要扔出fileioman，作为一个任务线程“池”

	std::vector<std::wstring> m_MountRoots;

	// 占坑
private:
	CFileIOManager(const CFileIOManager&);
	CFileIOManager& operator=(const CFileIOManager&);

};

};

#endif