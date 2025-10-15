#ifndef __FIRENET_ACTIVEIO_H__
#define __FIRENET_ACTIVEIO_H__

#if /*(defined WIN7_32) ||*/ (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <winsock2.h>
#endif
namespace FireNet
 {
	class NetActiveIO : PollIO
	{
		NetSession *mAssocSession;
		SockAddr 	mSockAddr;
		Connector	mConnector;
        int	mai_family;	/* PF_xxx */
        int	mai_socktype;	/* SOCK_xxx */
        int	mai_protocol;	/* 0 or IPPROTO_xxx for IPv4 and IPv6 */
        socklen_t mai_addrlen;	/* length of ai_addr */
        sockaddr mai_addr;

		void PollIn()  { Close(); }
		void PollOut() { Close(); }

		NetActiveIO(int x, const SockAddr &sockAddr, const NetSession &s, const Connector& mConnector) : PollIO(x), mAssocSession( s.Clone()), mSockAddr(sockAddr)
		{
			this->mConnector = mConnector;
			int error = connect(fd, mSockAddr, mSockAddr.GetLen());
		}
#ifdef OS_IOS
		NetActiveIO(int x, const SockAddr &sockAddr, addrinfo* pAI, const NetSession &s, const Connector& mConnector) : PollIO(x), mAssocSession(s.Clone()), mSockAddr(sockAddr)
        {
            this->mConnector = mConnector;
            this->mai_family = pAI->ai_family;
            this->mai_socktype = pAI->ai_socktype;
            this->mai_protocol = pAI->ai_protocol;
            this->mai_addrlen = pAI->ai_addrlen;
            this->mai_addr.sa_family = pAI->ai_addr->sa_family;
            this->mai_addr.sa_len = pAI->ai_addr->sa_len;
            for (int i = 0; i < sizeof(this->mai_addr.sa_data); i++)
            {
                this->mai_addr.sa_data[i] = pAI->ai_addr->sa_data[i];
            }
            
            int ss = socket(this->mai_family, this->mai_socktype, this->mai_protocol);
            fd = ss;
            int optValue = 1;
            setsockopt(ss, SOL_SOCKET, SO_KEEPALIVE, &optValue, sizeof(optValue));
            fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK);
            int error = connect(fd, pAI->ai_addr, pAI->ai_addrlen);
            int Result = error;
        }
#endif        
	public:
		~NetActiveIO()
		{
			int optValue = -1;
			socklen_t optLength = sizeof(optValue);
			int optReturn = getsockopt(fd, SOCK_STREAM, SO_ERROR, &optValue, &optLength);
			if ( optReturn == 0 && optValue == 0 )
			{
				PollIO::Register(new StreamIO( fd, mAssocSession, mConnector), true, false);
				fd = 0;
			}
			else
			{
#ifdef OS_IOS
				int socketReturn = connect(fd, &mai_addr, mai_addrlen);
#else
                int socketReturn = connect(fd, mSockAddr, mSockAddr.GetLen());
#endif
#ifdef OS_WINDOWS
				if (socketReturn == 0 || ( socketReturn == -1 && WSAEISCONN == WSAGetLastError()) )
#else
				if (socketReturn == 0 || ( socketReturn == -1 && errno == EISCONN) )
#endif
				{
					PollIO::Register( new StreamIO( fd, mAssocSession, mConnector), true, false);
					fd = 0;
				}
				else
				{
					mAssocSession->OnAbort(mSockAddr, mConnector);
					mAssocSession->Destroy();
				}
			}
		}

		static NetActiveIO *Open(const NetSession &mAssocSession, const FireNet::SockAddr& mSockAddr, const Connector& mConnector)
		{
			int s = -1, optValue = 1;
			s = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
			setsockopt(s, SOL_SOCKET, SO_KEEPALIVE, &optValue, sizeof(optValue));
			return s != -1 ? (NetActiveIO*)PollIO::Register(new NetActiveIO(s, mSockAddr, mAssocSession, mConnector), true, true) : NULL;
		}
        
#ifdef OS_IOS
		static NetActiveIO *Open2(const NetSession &mAssocSession, const FireNet::SockAddr& mSockAddr, const Connector& mConnector, addrinfo* pAI)
        {
            int s = -1, optValue = 1;
            //s = socket(AF_INET6, SOCK_STREAM, IPPROTO_TCP);
            //setsockopt(s, SOL_SOCKET, SO_KEEPALIVE, &optValue, sizeof(optValue));
            //if (s != -1)
            {
                return (NetActiveIO*)PollIO::Register(new NetActiveIO(s, mSockAddr, pAI, mAssocSession, mConnector), true, true);
            }
            //else
            {
                //return NULL;
            }
        }
#endif
	};

} // namespace FireNet {

#endif
