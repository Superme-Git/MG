#ifndef __FIRENET_NETIO_H__
#define __FIRENET_NETIO_H__

#include <errno.h>

#ifdef _WIN32
#include "os/windows/mutex.hpp"
#elif defined OS_IOS
#include "os/linux/mutex.hpp"
#endif


namespace FireNet
{
	class SockAddr
	{
		Octets addr;
	public:
		SockAddr() {}
		template<typename T> SockAddr(const T &mSockAddr) : addr(&mSockAddr, sizeof(mSockAddr)) { }
		SockAddr(const SockAddr &rhs) : addr(rhs.addr) { }
		socklen_t GetLen() const { return (socklen_t)addr.size(); }
		template<typename T> operator T* () { addr.resize(sizeof(T)); return (T *)addr.begin(); }
		template<typename T> operator const T* () const { return (const T *)addr.begin(); }

		template<typename T> const T* get() const { return (const T *)addr.begin(); }
	};

	class Connector
	{
	public:
		std::string host;
		std::string port;
		std::string mHostAddress;

		int syserror;
		std::string error;
		FireNet::lid_t udata;

		Connector()
		{
			syserror = 0;
			udata = 0;
		}
	};

	class NetIO : public PollIO
	{
	protected:
		NetSession *session;
		NetIO(int fd, NetSession *s) : PollIO(fd), session(s) { s->assoc_io = this; }
	};

	class StreamIO : public NetIO
	{
		void PollIn()
		{
			int recv_bytes;
			Octets& ibuf = session->GetIBuffer();
			do
			{
				if ((recv_bytes = readsocket(fd, ibuf.end(), ibuf.capacity() - ibuf.size())) > 0)
				{
					ibuf.resize(ibuf.size() + recv_bytes);
					session->OnRecv();
					if ( ibuf.size() == ibuf.capacity() )
						ForbidRecv();
					return;
				}
			} while (recv_bytes == -1 && errno == EINTR);

			int e = errno;
			if (recv_bytes != -1 || e != EAGAIN)
			{
				FireNet::Mutex::Scoped l(session->locker);
				session->OnSend();
				session->GetOBuffer().clear();

				char error[64];
				sprintf(error, "recv error (s=%d b=%d e=%d)", fd, recv_bytes, e);
				session->Close(error, true);
			}
		}

		void PollOut()
		{
			int send_bytes;
			FireNet::Mutex::Scoped l(session->locker);
			session->OnSend();
			Octets& obuf = session->GetOBuffer();
			if (obuf.size() == 0)
				return;

			do
			{
				if ((send_bytes = writesocket(fd, obuf.begin(), obuf.size())) >= 0)
				{
					obuf.erase(obuf.begin(), (char*)obuf.begin() + send_bytes);
					if ( obuf.size() == 0 )
						session->SendFinish();
					return;
				}
			} while (send_bytes == -1 && errno == EINTR);

			int e = errno;
			if (send_bytes != -1 || e != EAGAIN)
			{
				obuf.clear();

				char error[64];
				sprintf(error, "send error (s=%d b=%d e=%d)", fd, send_bytes, e);
				session->Close(error, true);
			}
		}

		void PollClose()
		{
			FireNet::Mutex::Scoped l(session->locker);
			if ( session->closing && ( session->close_discard || session->GetOBuffer().size() == 0 ))
			{
				Close();
			}
		}
	public:
		~StreamIO()
		{
			session->OnClose();
			session->Destroy();
		}

		StreamIO(int fd, NetSession *s, const Connector& mConnector) : NetIO(fd, s)
		{
#ifdef OS_IOS
			struct sockaddr_in6 local, peer;
#else
			struct sockaddr_in local, peer;
#endif
			socklen_t local_len = sizeof(local);
			socklen_t peer_len  = sizeof(peer);
			if ( getsockname(fd, (struct sockaddr *)&local, &local_len) == -1 || getpeername(fd, (struct sockaddr *)&peer, &peer_len) == -1 )
				session->OnOpen( mConnector);
			else
				session->OnOpen(SockAddr(local), SockAddr(peer), mConnector);
		}
	};

}

#endif
