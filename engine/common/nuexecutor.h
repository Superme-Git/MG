﻿#ifndef __Nuclear_EXECUTOR_H
#define __Nuclear_EXECUTOR_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <queue>
#include <platform/thread.h>
#include <platform/mutex.h>
#include <platform/ksemaphore.h>

namespace Nuclear
{

	// 单线程的线程池
	class ExecThread : public core::Thread
	{
	private:
		enum { ePriorityCount = 4 }; // [1, 32]

	public:

		class Task
		{
			friend class ExecThread;
		public:
			Task(int iPriority) : m_iPriority(iPriority), m_bCanceled(false)
			{ 
				if( m_iPriority < 0 || m_iPriority >= ePriorityCount ) 
					m_iPriority = 0 ;
			}

			int GetPriority() const { return m_iPriority; }
			virtual void Execute() = 0;
			virtual void UnexecuteAndDestroy() = 0;	//还没有执行就因为程序退出会调用这个方法
			virtual void ExecuteEndAndDestroy() = 0;//应该在这里面删除本类的对象
			virtual ~Task() { }
		protected:
			int m_iPriority;
			bool m_bCanceled;
			ExecThread *m_pExecutor;
		};

		/*
		   普通任务
		   用户需要继承此类并实现 Execute() 和 Destroy()
		   在用户线程创建，在执行线程执行(Execute)和销毁(Destroy)
		*/
		class SimpleTask : public Task
		{
		public:
			SimpleTask(int iPriority) : Task(iPriority) { }
				
			void ExecuteEndAndDestroy();
			void UnexecuteAndDestroy();

			virtual void Execute() = 0;
			virtual void Destroy() = 0;
		};

		/*
		   回调任务
		   用户需要继承此类并实现 Execute() 和 Callback()，不能继承DestroyInExecutor，这个方法永远得不到执行
		   在用户线程创建，在执行线程执行(Execute)，再返回到用户线程执行回调(Callback), 由用户线程负责销毁
		   Callback 一定会被调用， 销毁必须发生在此之后
		   用户可以通过 Cancel 接口取消此任务
		*/
		class CallbackTask : public Task
		{
		private:
			static void CallbackFunc(void*);
		public:
			CallbackTask(int iPriority) : Task(iPriority) { }
		
			void Cancel() { m_bCanceled = true; } // 取消任务
			void ExecuteEndAndDestroy();
			void UnexecuteAndDestroy();

			virtual void Execute() = 0;
			virtual void Callback() = 0;
		};


	public:
		ExecThread();
		virtual ~ExecThread() { }

		bool Init();					// 初始化
		bool Destroy();					// 销毁
		void Close();					// yeqing 2015-10-20
		bool AddTask(Task *pTask);		// 添加任务
		void UpdateExecuteResult();		// 取得CallbackTask的回调机会
		virtual void Update();

	private:
		bool addTask(Task *pTask);		// 添加任务

	private:
		unsigned int m_uThreadID;
		unsigned int m_maskTask;
		std::queue<Task*> m_queTask[ePriorityCount];
		std::queue<Task*> m_cacheQueTask;
		static core::CMutex    m_QueueMutex;
		static core::CSemaphore* m_QueueSem;
		virtual void Run();

	private:
		ExecThread(const ExecThread&);
		ExecThread& operator=(const ExecThread&);

	};

}

#endif