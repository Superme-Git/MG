//  CEGUILoadingTaskManager.h
//  CEGUI

#ifndef CEGUI_CEGUILoadingTaskManager_h
#define CEGUI_CEGUILoadingTaskManager_h
#include <queue>
#include <list>
#include <map>
#include <platform/thread.h>
#include <platform/mutex.h>

namespace CEGUI {
	class ITask
	{
	public:
		enum enumTaskStatus{
			eTaskAdded,
			eTaskRunning,
			eTaskSuccessed,
			eTaskFailed,
		};

		enum enumTaskPri{
			eTPFont,
			eTPFile,
			eTPParse,
			eTPCount
		};

		enum enumThread{
			eMainThread,
			eWorkerThread,
		};

		virtual void Run() = 0;
		enumTaskPri GetPri(){ return m_pri; }
		void SetPriority(float priority)
		{
			m_priority = priority;
		}
		float GetPriority(){ return m_priority; }

		virtual ~ITask(){}
	protected:
		ITask(enumTaskPri aPri) :m_status(eTaskAdded), m_pri(aPri), m_priority(0.f){}
	private:
		enumTaskStatus  m_status;
		enumTaskPri     m_pri;
		float           m_priority;
	};

	class CCEGUITaskManager
	{
	public:
		bool QueueTask(ITask* aPTask);

		ITask* GetTask(ITask::enumThread eThread = ITask::eWorkerThread);

		bool    LoadingFontEmpty();
		static CCEGUITaskManager* GetInstancePtr();
		static void destroy();	// yeqing 2015-10-19

		void	Update();

	private:
		bool queueTask(ITask* aPTask);

	private:
		std::queue<ITask*> m_vCacheTasks;
		std::map<float, std::list<ITask*> >  m_mapFileTasks;  //窗口的x坐标小的先加载
		//std::list<ITask*> m_vFileTasks;
		std::list<ITask*> m_vParseTasks;
		std::list<ITask*> m_vFontTasks;

		core::CMutex      m_mutexQueue;
		static CCEGUITaskManager* s_Instance;
	};
}

#endif
