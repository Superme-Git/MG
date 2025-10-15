
#include "threadpool.h"

using namespace GNET;

class MyTask : public Thread::Runnable
{
	const char *ident;
public:
	MyTask(const char *_ident) : ident(_ident) { }
	void Run()
	{
		printf("run %s in %08x\n", ident, pthread_self());
	}
};

class MyChooser : public ThreadChooser<RandomChooseThread>
{
public:
	void AppendObject(iterator it)
	{
		(*it)->RunTask(new MyTask("normal"));
	}
};

class AffChooser : public ThreadChooser<AffinityChooseThread<RandomChooseThread> >
{
public:
	void AppendObject(iterator it)
	{
		(*it)->RunTask(new MyTask("affinity"));
	}
};

class Aff2Chooser : public ThreadChooser<AffinityChooseThread<RandomChooseThread> >
{
	Thread::Runnable *r;
public:
	void AppendObject(iterator it)
	{
		(*it)->RunTask(r);
	}

	Aff2Chooser& operator() (Thread::Runnable *_r) { r = _r; return *this; }
};

class NormalTask :  public ThreadChooser<RandomChooseThread>, public Thread::Runnable
{
public:
	void AppendObject(iterator it)
	{
		(*it)->RunTask(this);
	}

	void Run()
	{
		printf("NormalTask in %08x\n", pthread_self());
	}
};

int main()
{
	ThreadPool pool(HardReference<CounterPolicy>(new FixedCounterPolicy(5)));
	MyChooser chooser;
	AffChooser affchooser;
	Aff2Chooser aff2chooser;
	pool.RequestObject(chooser);
	pool.RequestObject(affchooser);
	pool.RequestObject(chooser);
	pool.RequestObject(affchooser);
	pool.RequestObject(aff2chooser(new MyTask("Aff2")));
	pool.RequestObject(chooser);
	pool.RequestObject(chooser);
	pool.RequestObject(aff2chooser(new MyTask("Aff2")));
	pool.RequestObject(affchooser);
	pool.RequestObject(chooser);
	pool.RequestObject(*(new NormalTask()));
	pool.RequestObject(chooser);
	pool.RequestObject(affchooser);
	pool.RequestObject(aff2chooser(new MyTask("Aff2")));
}
