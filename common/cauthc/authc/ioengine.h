#ifndef __FIRENET_IOENGINE_H__
#define __FIRENET_IOENGINE_H__
#pragma once

namespace aio
{
	class Runnable
	{
	public:
		virtual void run()     { }
		virtual void destroy() { delete this; }

		inline void runAndDestroy()
		{
			run();
			destroy();
		}
	protected:
		virtual ~Runnable()    { };
	};

	class TaskQueue
	{
		TaskQueue( const TaskQueue&) {}
		void operator=( const TaskQueue&) {}

	private:
		std::list<Runnable*> tasks;
		FireNet::Mutex mutex;

		bool exit;

	public:
		TaskQueue();
		~TaskQueue();
	public:
		inline void addTask(Runnable* task)
		{
			FireNet::Mutex::Scoped scoped( mutex);
			tasks.push_back( task);
		}
		inline void addTask(Runnable& task)
		{
			addTask( &task);
		}

		void runTasks();
		void clear();
	};

	class Engine : public Thread
	{
		TaskQueue		taskqueue;

		Engine();
		~Engine();
	public:
		static Engine & getInstance();

		bool Startup();
		void Cleanup();

		void Connect(const FireNet::Connector & c);

		virtual void Run();

	private:

		bool	exit;
	};

} // namespace aio {

#endif