#include "nufileiomanager.h"
#include "nuxml.h"
#include "nulog.h"
#include "nupobject.h"
#include "nuxptimeuselog.h"
#include "ljfmfex.h"
#include "nuxref.h"
#include "nuljfmutil.h"
#include "../../common/ljfm/code/include/ljfmext.h"

#include <utils/FileUtil.h>
#include <utils/StringUtil.h>
#include <utils/Utils.h>


#ifdef ANDROID
#include <fcntl.h>
#include <errno.h>
#endif

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

namespace Nuclear
{
	extern XPTimeUseLog g_timeuserlog;
	NuclearFileIOManager::NuclearFileIOManager()
		: m_iIOTaskCount(0), m_iIOTaskBytes(0), m_iTime(0), m_iSize(0), m_nTimePF(30)
	{ 
	}

	bool NuclearFileIOManager::Init(bool bWritableDefault, bool bApplictionInBuild)
	{
#ifdef ANDROID
		m_executor.Init();
		LOGD("fileio init succ.\n");
		return true;
#elif (defined WIN7_32)
		m_executor.Init();
		LOGD("fileio init succ.\n");
		return true;
#else
		m_executor.Init();
		LOGD("fileio init succ.\n");
		return true;
#endif
	}

	bool NuclearFileIOManager::Destroy()
	{
		// 停止加载线程
		// yeqing 2015-10-20
		m_executor.Destroy();
		//assert(false);

		// 清空结果队列
		{
			core::CMutex::Scoped autoLock(m_MutexResultQueue);
			m_lstResultQueue.clear();
		}

		// 删除等待队列中的每个 Task
		{
			core::CMutex::Scoped autoMutex(m_MutexReadingTask);
			for (std::set<AsyncReadTask*>::iterator it = m_setReadingTask.begin(); it != m_setReadingTask.end(); ++it)
			{
				AsyncReadTask* pTask = *it;
				if (pTask)
				{
					delete pTask;
				}
			}
			m_setReadingTask.clear();
		}

        return false;
	}

	bool NuclearFileIOManager::GetFileImage(const std::wstring &filename, NuclearBuffer &data, bool lookupSegmPakPath)
	{
		core::CMutex::Scoped autoLock(m_MutexGetFile);

		// 同步读取
		size_t nPosTga = filename.rfind(L".tga");
		size_t nPosDds = filename.rfind(L".dds");
		size_t nPosJpg = filename.rfind(L".jpg");
		size_t nPosPng = filename.rfind(L".png");

		if (nPosTga != std::wstring::npos ||
			nPosDds != std::wstring::npos ||
			nPosJpg != std::wstring::npos ||
			nPosPng != std::wstring::npos
			)
		{
			size_t nPos = filename.rfind(L".");
			if (nPos == std::wstring::npos)
			{
				return INVALID_PICTURE_HANDLE;
			}
			std::wstring strFileNameConvert = filename;
			std::wstring strFileNamePng = filename; //png
			strFileNamePng.replace(nPos, 8, L".png");


			bool bGetIamgeData = false;
			LJFM::LJFMF file;
			if (file.Open(strFileNameConvert, LJFM::FM_EXCL, LJFM::FA_RDONLY))
			{
				bGetIamgeData = true;
			}
			else
			{
				if (file.Open(strFileNamePng, LJFM::FM_EXCL, LJFM::FA_RDONLY))
				{
					bGetIamgeData = true;
				}
			}

			if (!bGetIamgeData)
				return false;

			int file_size = (int)file.GetSize();
			if (file_size == 0) return false;
			data.SetOctets(NuclearFileOctets(NuclearHardRef<LJFM::LJFMID>(new LJFM::LJFMID(file.GetImage()))));
			return true;
		}
		else
		{
			LJFM::LJFMF file;

			if (!file.Open(filename, LJFM::FM_EXCL, LJFM::FA_RDONLY))
				return false;

			int file_size = (int)file.GetSize();
			if (file_size == 0) return false;
			data.SetOctets(NuclearFileOctets(NuclearHardRef<LJFM::LJFMID>(new LJFM::LJFMID(file.GetImage()))));
			return true;
		}	
	}

	bool NuclearFileIOManager::LoadPObject(const std::wstring &filename, NuclearObject &po)
	{
		NuclearBuffer buf;
		if (GetFileImage(filename, buf) && po.LoadFromMem(buf))
			return true;
		XPLOG_ERROR(L"Load Object %s Faild\n", filename.c_str());
		return false;
	}

	bool NuclearFileIOManager::AsyncReadFile(NuclearFileIOManager::AsyncReadTask *pTask)
	{
		if( pTask == NULL )
			return false;

		bool r = false;

		// 只支持异步模式异步读取
		r = m_executor.AddTask(pTask);
		if( r )
        {
            core::CMutex::Scoped autoMutex(m_MutexReadingTask);
			m_setReadingTask.insert(pTask);
        }
		return r;
	}
    
    bool NuclearFileIOManager::AddTask(Nuclear::ExecThread::Task *pTask)
	{
		if( pTask == NULL )
			return false;
        
		// 只支持异步模式异步读取
		return m_executor.AddTask(pTask);
	}

	void NuclearFileIOManager::OnUpdate(int64 v)
	{
		m_executor.Update();
        const int MAX_FILEIO_CNT = 3;
		const int MAX_SPENT_TIME = 50;
		m_iIOTaskCount = 0;
		m_iIOTaskBytes = 0;
		{
			int64_t startTime = Nuclear::GetMilliSeconds();

			while(true)
			{
                AsyncReadTask* pTask = NULL;
                {
                    core::CMutex::Scoped autoMutex(m_MutexResultQueue);
                    std::list<AsyncReadTask*>::iterator it = m_lstResultQueue.begin();
                    if (it != m_lstResultQueue.end() && m_iIOTaskCount <= MAX_FILEIO_CNT)
                    {
                        pTask = *it;
                        m_lstResultQueue.erase(it);
                    }else{
                        break;
                    }
                }
                
				int iResultSize = pTask->GetData().size();
				++m_iIOTaskCount;
				m_iIOTaskBytes += iResultSize;
				if( pTask->IsDiscarded() )
					pTask->OnDiscard();
				else
					pTask->OnReady();
                
                {
                    core::CMutex::Scoped autoMutex(m_MutexReadingTask);
                    m_setReadingTask.erase(pTask);
                }

				int64_t nowTime = Nuclear::GetMilliSeconds();
				int64_t spentTime = nowTime - startTime;
				if (spentTime >= MAX_SPENT_TIME)
				{
					break;
				}
			}
		}
	}

};
