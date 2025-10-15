
#include <stdio.h>
#include "pf.hpp"
#include "../../../gnet/glinkd/pfp.hpp"
#include "LinkClient.hpp"
#include "InfoServer.hpp"

namespace portforward
{

	static void sendCloseToServer( int linkid, int portsid, int code, const wchar_t * reason = NULL)
	{
		lauthc::InfoServer*	infoserver = lauthc::InfoServer::GetInstance();
		if( lauthc::LinkClient* lc = infoserver->GetLinkClient( linkid))
		{
			gnet::PortForward p;
			p.command = portforward::eClose;
			p.portsid = portsid;
			p.code = code;
			// p.data = reason; // utf-16le

			lc->SendProtocol( p);
		}
	}

	///////////////////////////////////////////////////////////////////////////////
	// Forwards
	void Forwards::cmdPrintLocal()
	{
		for( acceptor_t::const_iterator it = acceptor.begin(), ie = acceptor.end(); it != ie; ++ it)
		{
			LocalAcceptor * la = it->second;

			printf( "Local %d %s %s:%s\n", la->GetAcceptorID(), la->getLocalInfo().c_str(), la->rhost.c_str(), la->rport.c_str());

			for( std::set<int>::const_iterator it = la ->portsids.begin(), ie = la ->portsids.end(); it != ie; ++it)
			{
				if (Forward * fw = this->findForward(*it))
					printf( "\tForward %s\n", fw->str().c_str());
			}
		}
	}

	bool Forwards::cmdNewLocal( const std::string& servername, const std::string& host, int port, const std::string& rhost, const std::string& rport)
	{
		int accid = lauthc::InfoServer::GetInstance()->NextID();
		if( LocalAcceptor * la = LocalAcceptor::Open( servername, host, port, m_linkclientid, accid))
		{
			la->SetRemote( rhost, rport);
			acceptor.insert( std::make_pair( accid, la));

			OutLog( "Forwards::cmdNewLocal %s as %s\n", servername.c_str(), la->str().c_str());
			return true;
		}
		return false;
	}

	void Forwards::cmdCloseLocal( int acceptorid, bool closePorts)
	{
		LocalAcceptor * la = RemoveLocalAcceptor( acceptorid);
		if( NULL == la)
			return;

		if (closePorts)
		{
			std::set<int> tmp;
			la->portsids.swap(tmp);
			for( std::set<int>::iterator it = tmp.begin(), ie = tmp.end(); it != ie; ++it)
				this->cmdCloseForward(*it, eCloseActiveFromConsole); // close forward
		}
		la->Close();
	}

	void Forwards::cmdCloseForward( int portsid, int reason)
	{
		if(Forward * forward = removeForward(portsid))
		{
			delete forward;
			sendCloseToServer( m_linkclientid, portsid, reason);
		}
	}

	void Forwards::cmdCloseAll()
	{
		acceptor_t		temp( acceptor);

		for( acceptor_t::iterator it = temp.begin(), ite = temp.end(); it != ite; ++ it)
			cmdCloseLocal( it->first, true);

		OutLog( "Forwards::cmdCloseAll acceptor.size() =q %d forwards.size() = %d\n", acceptor.size(), forwards.size());
	}

	void Forwards::process(const gnet::PortForward & pf)
	{
		switch (pf.command)
		{
		case portforward::eAnsiText:
			OutLog( "portforward::eAnsiText TextFromServer: %s\n", std::string((const char *)pf.data.begin(), pf.data.size()).c_str());
			break;
		case portforward::eAuthority:
			OutLog( "portforward::eAuthority\n");
			switch (pf.code)
			{
			case eAuthoritySet:
				OutLog( "eAuthoritySet? 我不是服务器啊.\n");
				break;
			case eAuthorityGet:
				{
					portforward::AuthorityParameter a;
					GNET::Marshal::OctetsStream(pf.data) >> a;
					OutLog( "Authority: userid=%d allow=%s%s ports={", pf.portsid, (a.allowGrant() ? "g" : ""), (a.allowForward() ? "f" : ""));
					bool first = true;
					for (std::set<int>::iterator it = a.ports.begin(), ie = a.ports.end(); it != ie; ++it)
						OutLog( "%s %d", (first ? "" : "," ), *it);
					OutLog( " }\n");
				}
				break;
			}
			break;

		case portforward::eConnect:
			OutLog( "portforward::eConnect\n");
			sendCloseToServer( m_linkclientid, pf.portsid, portforward::eCloseConnectUnsupported);
			// 暂不支持反向映射。这里需要的实现是：先建立 Forward；开始连接；连接成功以后把IO绑定到Forward上。
			break;

		case portforward::eClose:
			OutLog( "portforward::eClose Forward Peer Close. code=%d\n", pf.code);
			if (Forward * forward = removeForward( pf.portsid))
				delete forward;
			// else 忽略：关闭协议不报错。
			break;

		case portforward::eForward:
			OutLog( "portforward::eForward portsid = %d ", pf.portsid);
			if (portforward::eForwardRaw == pf.code)
			{
				OutLog( "pf.code = eForwardRaw ");
				if (Forward * forward = findForward( pf.portsid))
				{
					forward->ForwardData( pf.data);
					OutLog( "ForwardData size = %d\n", pf.data.size());
				}
				else
				{
					sendCloseToServer( m_linkclientid, pf.portsid, portforward::eCloseForwardPortNotFound);
					OutLog( "lost Forward sendCloseToServer.\n");
				}
			}
			else
			{
				sendCloseToServer( m_linkclientid, pf.portsid, portforward::eCloseUnknownForwardType);
				OutLog( "pf.code = %d sendCloseToServer.\n", pf.code);
			}
			break;

		case portforward::eForwardAck:
			OutLog( "portforward::eForwardAck portsid = %d ", pf.portsid);
			if (Forward * forward = findForward( pf.portsid))
			{
				forward->ForwardAck( pf.code);
				OutLog( "ForwardAck pf.code = %d\n", pf.code);
			}
			else
			{
				sendCloseToServer( m_linkclientid, pf.portsid, portforward::eCloseForwardAckPortNotFound);
				OutLog( "lost Forward pf.code = %d, sendCloseToServer\n", pf.code);
			}
			break;
		default:
			OutLog( "Forwards::process command = %d portsid = %d\n", pf.command, pf.portsid);
			break;
		}
	}

	Forwards::~Forwards()
	{
		for( acceptor_t::const_iterator it = acceptor.begin(), ie = acceptor.end(); it != ie; ++ it)
		{
			LocalAcceptor * la = it->second;
			la->Close();
		}

		forwards_t tmp;
		tmp.swap(this->forwards);
		for (forwards_t::iterator it = tmp.begin(), ie = tmp.end(); it != ie; ++it)
			delete it->second;
	}

} // namespace portforward


namespace portforward {

	Forward::Forward( int sid, int accid, int lid)
		: linkid( lid), portsid( sid), acceptorid( accid)
	{
		this->forwardSize = 0;
		this->windowSize = 0;
		this->sendSize = 0;

		this->io = NULL;

		OutLog( "Forward() %s\n", str().c_str());
	}

	Forward::~Forward()
	{
		if( lauthc::LinkClient* lc = lauthc::InfoServer::GetInstance()->GetLinkClient( linkid))
			lc->GetForwards().RemoveAcceptorSID( this->acceptorid, portsid);

		if (io)
		{
			io->forward = NULL; // detach
			io->Close();
			io = NULL;
		}
	}

	std::string Forward::str() const
	{
		char buffer[1024];
		sprintf( buffer, "linkid = %d acceptorid = %d portsid = %d winsize = %d forward = %d sendlocal = %d IO(%s)", linkid, acceptorid, getPortsid(), windowSize, forwardSize, sendSize, ( io ? io->str().c_str() : "") );
		return buffer;
	}

	void Forward::OnIODestroy()
	{
		if( lauthc::LinkClient* lc = lauthc::InfoServer::GetInstance()->GetLinkClient( linkid))
			lc->GetForwards().cmdCloseForward( this->portsid, eCloseSessionClose);
	}

	void Forward::ForwardData(const GNET::Octets & data)
	{
		buffer.insert(buffer.end(), data.begin(), data.end());
		if( buffer.size() > 0)
			this->io->PermitSend();
	}

	void Forward::ForwardAck(long ackedSize)
	{
		bool isLoad = this->windowSize >= eForwardWindowLowSize;
		this->windowSize -= ackedSize;
		if (isLoad && this->windowSize < eForwardWindowLowSize && this->io)
		{
			this->io->PermitRecv(); // start read
		}
	}

	void Forward::OnIORecv( GNET::Octets& data)
	{
		int rc = data.size();

		this->forwardSize += rc;
		this->windowSize += rc;

		if( lauthc::LinkClient* lc = lauthc::InfoServer::GetInstance()->GetLinkClient( linkid))
		{
			gnet::PortForward pf;
			pf.command = portforward::eForward;
			pf.portsid = this->portsid;
			pf.code = portforward::eForwardRaw;
			pf.data.swap( data);

			lc->SendProtocol(pf);
			OutLog( "Forward::OnIORecv %s\n", str().c_str());
		}
		else
		{
			OutLog( "Forward::OnIORecv lauthc::InfoServer::GetInstance().GetLinkClient %d lost LinkClient\n", linkid);
		}


		if (this->windowSize >= eForwardWindowHighSize) // 64k
		{
			this->io->ForbidRecv(); // stop read
		}
	}

	int Forward::send(const GNET::Octets & data)
	{
		int rc = this->io->Send( data);
		if( rc > 0)
		{
			this->sendSize += rc;

			if( lauthc::LinkClient* lc = lauthc::InfoServer::GetInstance()->GetLinkClient( linkid))
			{
				gnet::PortForward pf;
				pf.command = portforward::eForwardAck;
				pf.portsid = this->portsid;
				pf.code = rc;

				lc->SendProtocol(pf);
			}
			else
			{
				OutLog( "Forward::send lauthc::InfoServer::GetInstance().GetLinkClient %d lost LinkClient\n", linkid);
			}
		}
		return rc;
	}

	void Forward::OnIOSend()
	{
		if (buffer.size() > 0)
		{
			int rc = send(buffer);
			buffer.erase(0, rc);
		}
		if( buffer.size() > 0)
			this->io->PermitSend();
		else
			this->io->ForbidSend();

		OutLog( "Forward::OnIOSend %s\n", str().c_str());
	}

} // namespace portforward
