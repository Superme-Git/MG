#include "win32/share.hpp"

namespace FireNet
{

	PollIO::IOMap PollIO::iomap;
	PollIO::FDSet PollIO::fdset;
	PollIO::IOMap PollIO::ionew;
	PollIO::EventSet PollIO::eventset;
	Mutex PollIO::locker_poll;
	Mutex PollIO::locker_event;

	bool PollIO::wakeup_flag = false;

#if defined USE_EPOLL
	int PollIO::ep;
#elif defined USE_SELECT
	int PollIO::maxfd;
	fd_set PollIO::rfds;
	fd_set PollIO::wfds;
	fd_set PollIO::all_rfds;
	fd_set PollIO::all_wfds;
#endif

	int PollControl::writer;
};


