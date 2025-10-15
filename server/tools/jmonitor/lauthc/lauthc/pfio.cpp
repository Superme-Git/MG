#include "pfio.hpp"
#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "pf.hpp"
#include "LinkClient.hpp"
#include "InfoServer.hpp"
#include "gnet/PortForward.hpp"
#include "../../../gnet/glinkd/pfp.hpp"

namespace portforward {

	bool gDebugLevel = false;

	void setDebugLevel( bool n)
	{
		gDebugLevel = n;
	}
	bool isDebugLevel()
	{
		return gDebugLevel;
	}

	void OutLog( const char* info, ...)
	{
		if( gDebugLevel)
		{
			va_list	vl;

			va_start( vl, info);
//			vprintf( info, vl);

			if( FILE* file = fopen( "/tmp/lauthc.log", "a+"))
			{
				time_t t = time( NULL);
				fprintf( file, "%s ", ctime( &t));
				vfprintf( file, info, vl);
				fclose( file);
			}
			va_end( vl);
		}
	}

	static std::string SockAddrToString( const struct sockaddr_in& addr)
	{
		const int 		bsize = 256;
		char				buffer[bsize];

		sprintf( buffer, "%s:%d", inet_ntoa( addr.sin_addr), ntohs( addr.sin_port));
		return buffer;
	}

} // namespace portforward {

////////////////////////////////////////////////////////////////////////////////

namespace portforward {


	PortForwardIO::PortForwardIO( int x, int lid, int accid, Forward* f)
		: PollIO( x), linkid( lid), acceptorid( accid), forward( f)
	{
		forward->io = this;
	}

	PortForwardIO::~PortForwardIO()
	{
		if( forward)
		{
			OutLog( "PortForwardIO::~PortForwardIO forward = %s\n", forward->str().c_str());
			forward->io = NULL;
			try { forward->OnIODestroy(); } catch ( ... ) { /*skip*/ }
		}
	}

	void PortForwardIO::PollIn()
	{
		if( NULL == forward)
			return;

		int recv_bytes;
		GNET::Octets 	ibuf( 16384);
		do
		{
			if ((recv_bytes = read(fd, ibuf.begin(), ibuf.capacity())) > 0)
			{
				ibuf.resize( recv_bytes);
				forward->OnIORecv( ibuf);
				return;
			}
		} while (recv_bytes == -1 && errno == EINTR);

		if (recv_bytes != -1 || errno != EAGAIN)
		{
			if( 0 == recv_bytes)
				OutLog( "PortForwardIO::PollIn close peer Closed\n");
			else
				OutLog( "PortForwardIO::PollIn close reset %d Closed\n", errno);
			Close();
		}
	}

	void PortForwardIO::PollOut()
	{
		if( NULL == forward)
			return;
		forward->OnIOSend();
	}

	int PortForwardIO::Send( const GNET::Octets& data)
	{
		int send_bytes;
		do
		{
			if ((send_bytes = write(fd, data.begin(), data.size())) >= 0)
				return send_bytes;
		} while (send_bytes == -1 && errno == EINTR);

		if (send_bytes != -1 || errno != EAGAIN)
		{
			OutLog( "PortForwardIO::Send error = %d Closed\n ", errno);
			Close();
		}
		return 0;
	}

	std::string PortForwardIO::str() const
	{
		struct sockaddr_in local, peer;
		socklen_t local_len = sizeof( local);
		socklen_t peer_len  = sizeof( peer);

		getsockname( fd, (struct sockaddr *)&local, &local_len);
		getpeername( fd, (struct sockaddr *)&peer, &peer_len);

		return SockAddrToString( local) + " - " + SockAddrToString( peer);
	}

} // namespace portforward {

////////////////////////////////////////////////////////////////////////////////

namespace portforward {

	LocalAcceptor::LocalAcceptor( int x, int lid, int accid)
		: PollIO(x), linkid( lid), acceptorid( accid)
	{}

	LocalAcceptor::~LocalAcceptor()
	{}

	void LocalAcceptor::PollIn()
	{
		int s = accept(fd, 0, 0);
		if (s != -1)
		{
			lauthc::InfoServer*	infoserver = lauthc::InfoServer::GetInstance();
			lauthc::LinkClient* lc = infoserver->GetLinkClient( linkid);
			if( NULL == lc)
			{
				OutLog( "LocalAcceptor::PollIn linkid = %d lost LinkClient\n", linkid);
				close( s);
				return;
			}

			int 			portsid = infoserver->NextID();
			Forward * 	forward = lc->GetForwards().newForward( portsid, acceptorid);

			if( NULL == forward)
			{
				OutLog( "LocalAcceptor::PollIn %d %d failed!\n", portsid, acceptorid);
				close( s);
				return;
			}

			PortForwardIO* pfio = new PortForwardIO(s, linkid, acceptorid, forward);
			GNET::PollIO::Register( pfio, true, false);
			portsids.insert( portsid);

			gnet::PortForward pf;
			pf.command = eConnect;
			pf.portsid = portsid;
			pf.code    = eConnectV0;

			ConnectParameterV0 cp;
			cp.address = this->rhost;
			cp.port    = this->rport;
			cp.timeout = 2;
			pf.data = GNET::Marshal::OctetsStream() << cp;

			lc->SendProtocol(pf);

			OutLog( "LocalAcceptor::PollIn linkid = %d acceptorid = %d portsid = %d rhost = %s rport = %s forward = (%s)\n",
					linkid, acceptorid, portsid, this->rhost.c_str(), this->rport.c_str(), forward->str().c_str());
		}
	}

	void LocalAcceptor::SetRemote( const std::string& host, const std::string& port)
	{
		this->rhost = host;
		this->rport = port;
	}

	std::string LocalAcceptor::getLocalInfo() const
	{
		struct sockaddr_in local;
		socklen_t local_len = sizeof( local);
		getsockname( fd, (struct sockaddr *)&local, &local_len);
		return SockAddrToString( local);
	}

	int LocalAcceptor::GetAcceptorID() const
	{
		return acceptorid;
	}

	std::string LocalAcceptor::str() const
	{
		char buffer[1024];
		sprintf( buffer, "linkid = %d acceptorid = %d LocalInfo = %s rhost = %s rport = %s", linkid, acceptorid, getLocalInfo().c_str(), rhost.c_str(), rport.c_str());
		return buffer;
	}

	LocalAcceptor* LocalAcceptor::Open( const std::string& servername, const std::string& host, int lport, int linkid, int accid)
	{
		try
		{
			GNET::Conf *conf = GNET::Conf::GetInstance();
			GNET::Conf::section_type section = "LocalAcceptor";

			int s = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
			if( -1 == s)
				throw errno;

			GNET::SockAddr sa;
			struct sockaddr_in *addr = sa;
			memset(addr, 0, sizeof(*addr));
			addr->sin_family = AF_INET;
			if ( (addr->sin_addr.s_addr = inet_addr( host.c_str())) == INADDR_NONE)
				addr->sin_addr.s_addr = INADDR_ANY;
			addr->sin_port = htons( lport);

			int optval = 1;
			setsockopt(s, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));
			setsockopt(s, SOL_SOCKET, SO_KEEPALIVE, &optval, sizeof(optval));

			optval = atoi(conf->find(section, "so_sndbuf").c_str());
			if (optval) setsockopt(s, SOL_SOCKET, SO_SNDBUF, &optval, sizeof(optval));
			optval = atoi(conf->find(section, "so_rcvbuf").c_str());
			if (optval) setsockopt(s, SOL_SOCKET, SO_RCVBUF, &optval, sizeof(optval));
			optval = atoi(conf->find(section, "tcp_nodelay").c_str());
			if (optval) setsockopt(s, IPPROTO_TCP, TCP_NODELAY, &optval, sizeof(optval));

			int result = bind (s, sa, sa.GetLen());
			if( -1 == result)
				throw errno;

			optval = atoi(conf->find(section, "listen_backlog").c_str());
			result = listen (s, optval ? optval : SOMAXCONN);
			if( -1 == result)
				throw errno;
			LocalAcceptor* acceptor = new LocalAcceptor( s, linkid, accid);
			PollIO::Register( acceptor, true, false);
			OutLog( "LocalAcceptor::Open %s %s %d %d %d success!\n", servername.c_str(), host.c_str(), lport, linkid, accid);
			return acceptor;
		}
		catch( int errnum)
		{
			OutLog( "LocalAcceptor::Open %s %s %d %d %d failed, %d( %s)!\n", servername.c_str(), host.c_str(), lport, linkid, accid, errnum, strerror( errnum));
			return NULL;
		}

	}


} // namespace portforward {
