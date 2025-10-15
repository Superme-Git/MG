//  CEGUIResLoadThread.cpp
//  CEGUI

#include "CEGUIResLoadThread.h"
#include "CEGUILoadingTaskManager.h"

namespace CEGUI
{
    static core::CSemaphore* pSem = new core::CSemaphore("CEGUIResLoadThread");
    core::CSemaphore &g_LoadResSem = *pSem;
    CEGUIResLoadThread* CEGUIResLoadThread::s_Thread = NULL;
    
    CEGUIResLoadThread::CEGUIResLoadThread()
		: Thread()
    {
        
    }
    
    CEGUIResLoadThread::~CEGUIResLoadThread()
    {
		delete (&g_LoadResSem);
    }
    
    CEGUIResLoadThread* CEGUIResLoadThread::GetPtr()
    {
        if(s_Thread == NULL)
        {
            s_Thread = new CEGUIResLoadThread;
        }
        return s_Thread;
    }
    
    void CEGUIResLoadThread::Run()
    {
		while (true)
		{
            g_LoadResSem.wait();

			ITask* pTask = CCEGUITaskManager::GetInstancePtr()->GetTask(ITask::eWorkerThread);            
            if (pTask) {
                pTask->Run();
            }

			// yeqing 2015-10-19
			if (!IsRunningNow())
			{
				CCEGUITaskManager::destroy();	// yeqing 暂时在这里释放
				break;
			}
		}
    }

	// yeqing 2015-10-19
	void CEGUIResLoadThread::Destroy()
	{
		if (s_Thread != NULL)
		{
			s_Thread->StopRunning();
			g_LoadResSem.fire();
			s_Thread->Join();
			delete s_Thread;
			s_Thread = NULL;
		}
	}

}
