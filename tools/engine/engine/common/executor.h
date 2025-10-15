#ifndef __Nuclear_EXECUTOR_H
#define __Nuclear_EXECUTOR_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <queue>

namespace Nuclear
{

// ���̵߳ġ��̳߳ء�
class Executor
{
	enum { ePriorityCount = 4 }; // [1, 32]
public:

	class Task
	{
		friend class Executor;
	public:
		Task(int iPriority) : m_iPriority(iPriority), m_bCanceled(false)
		{ 
			if( m_iPriority < 0 || m_iPriority >= ePriorityCount ) 
				m_iPriority = 0 ;
		}
		int GetPriority() const { return m_iPriority; }
		virtual void Execute() = 0;
		virtual void UnexecuteAndDestroy() = 0;	//��û��ִ�о���Ϊ�����˳�������������
		virtual void ExecuteEndAndDestroy() = 0;//Ӧ����������ɾ������Ķ���
		virtual ~Task() { }
	protected:
		int m_iPriority;
		bool m_bCanceled;
		Executor *m_pExecutor;
	};

	/*
	   ��ͨ����
	   �û���Ҫ�̳д��ಢʵ�� Execute() �� Destroy()
	   ���û��̴߳�������ִ���߳�ִ��(Execute)������(Destroy)
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
	   �ص�����
	   �û���Ҫ�̳д��ಢʵ�� Execute() �� Callback()�����ܼ̳�DestroyInExecutor�����������Զ�ò���ִ��
	   ���û��̴߳�������ִ���߳�ִ��(Execute)���ٷ��ص��û��߳�ִ�лص�(Callback), ���û��̸߳�������
	   Callback һ���ᱻ���ã� ���ٱ��뷢���ڴ�֮��
	   �û�����ͨ�� Cancel �ӿ�ȡ��������
	*/
	class CallbackTask : public Task
	{
	private:
		static void __stdcall CallbackFunc(void*);
	public:
		CallbackTask(int iPriority) : Task(iPriority) { }
		
		void Cancel() { m_bCanceled = true; } // ȡ������
		void ExecuteEndAndDestroy();
		void UnexecuteAndDestroy();

		virtual void Execute() = 0;
		virtual void Callback() = 0;

	};


public:
	Executor();
	virtual ~Executor() { }


	bool Init();					// ��ʼ��
	bool Destroy();					// ����
	bool AddTask(Task *pTask);		// �������
	void UpdateExecuteResult();		// ȡ��CallbackTask�Ļص�����


private: 
	bool m_bRunning;
	HANDLE m_hOwnerThread;
	HANDLE m_hExecutorThread;
	HANDLE m_hExitEvent;
	unsigned int m_uThreadID;
	unsigned int m_maskTask;
	std::queue<Task*> m_queTask[ePriorityCount];

	static unsigned int __stdcall ExecutorThreadFunc(void*);
	static void __stdcall AddTaskFunc(void*);
	static void __stdcall DummyFunc(void*) { }

	// ռ��
private:
	Executor(const Executor&);
	Executor& operator=(const Executor&);

};

};

#endif