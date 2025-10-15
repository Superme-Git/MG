
#include <stdlib.h>
#include "pf.hpp"
#include "LinkServer.hpp"
#include "io/netio.h"
#include "common/timer.h"

namespace gnet
{

void sendAnsiTextToClient(portforward::link_sid_t linksid, portforward::port_sid_t portsid, const char * text)
{
	gnet::PortForward p;
	p.command = portforward::eAnsiText;
	p.portsid = portsid;
	p.code = 0;
	if (text) p.data.insert(p.data.begin(), text, strlen(text));
	glinkd::LinkServer::GetInstance()->Send(linksid, p);
}

void sendCloseToClient(portforward::link_sid_t linksid, portforward::port_sid_t portsid, int code, const char * reason = NULL)
{
	gnet::PortForward p;
	p.command = portforward::eClose;
	p.portsid = portsid;
	p.code = code;
	if (reason) p.data.insert(p.data.begin(), reason, strlen(reason));
	glinkd::LinkServer::GetInstance()->Send(linksid, p);
}

// 协议实现
void PortForward::Process(Manager * manager, Manager::Session::ID linksid)
{
	glinkd::LinkSession * linkSession = glinkd::LinkServer::GetInstance()->GetLinkSession(linksid);
	if (NULL == linkSession)
		return;

	///////////////////////////////////////////////////////////////////////////////////////////////
	// 处理授权命令。
	if (portforward::eAuthority == this->command)
	{
		const int userid = this->portsid;

		if (!(portforward::eAuthorityGet == this->code && linkSession->getUserId() == userid) // 查询自己不需要权限。
				&& !linkSession->getForwards()->authority().allowGrant())
			return sendAnsiTextToClient(linksid, portsid, "authority: deny.");

		glinkd::LinkSession * target = glinkd::LinkServer::GetInstance()->GetLinkSessionByUserid(userid);
		if (NULL == target)
			return sendAnsiTextToClient(linksid, portsid, "authority: user not found.");

		switch (this->code)
		{
		case portforward::eAuthoritySet:
			{
				if (linkSession->getUserId() == userid)
					return sendAnsiTextToClient(linksid, portsid, "authority: can not grant to yourself.");

				portforward::AuthorityParameter param;
				GNET::Marshal::OctetsStream(this->data) >> param;
				if (0 != linkSession->getForwards()->authority().getData().grantcheck(param))
					return sendAnsiTextToClient(linksid, portsid, "authority: out of range.");

				target->getForwards()->authority().swap(param);

				this->code = portforward::eAuthorityGet;
				this->data = GNET::Marshal::OctetsStream() << param;
				manager->Send(linksid, this);
			}
			break;

		case portforward::eAuthorityGet:
			{
				//this->code = portforward::eAuthorityGet;
				this->data = GNET::Marshal::OctetsStream() << target->getForwards()->authority().getData();
				manager->Send(linksid, this);
			}
			break;
		}
		return; // process eAuthority done.
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	// 处理端口转发相关命令。
	if (false == linkSession->getForwards()->authority().allowForward())
		return sendCloseToClient(linksid, this->portsid, portforward::eCloseNoAuthority, "");

	switch (this->command)
	{
	case portforward::eConnect:
		if (portforward::eConnectV0 == this->code)
		{
			portforward::ConnectParameterV0 param;
			GNET::Marshal::OctetsStream(this->data) >> param;
			linkSession->getForwards()->connect(linksid, this->portsid, param);
			break;
		}
		sendCloseToClient(linksid, this->portsid, portforward::eCloseUnknownConnectVersion, "");
		break;

	case portforward::eClose:
		//printf("##### portforward::eClose portsid=%d code=%d\n", this->portsid, this->code);
		delete linkSession->getForwards()->remove(this->portsid);
		break;

	case portforward::eForward:
		if (portforward::eForwardRaw == this->code)
			linkSession->getForwards()->forward(linksid, this->portsid, this->data);
		else
			sendCloseToClient(linksid, this->portsid, portforward::eCloseUnknownForwardType, "");
		break;

	case portforward::eForwardAck:
		linkSession->getForwards()->forwardAck(linksid, this->portsid, this->code);
		break;

	case portforward::eDumpLinkd:
		//linkSession->getForwards()->dump();
		break;

	default:
		break;
	}
}

} // namespace gnet

namespace portforward
{

void Forwards::dump()
{
	for (forwards_t::iterator it = forwards.begin(), ie = forwards.end(); it != ie; ++it)
		printf("##### Forward portsid=%d windowSize=%ld buffSize=%ld\n",
			it->first, it->second->windowSize, it->second->buffer.size());
}

bool Authority::qports(const std::string & chunk)
{
	int wellKnownPorts[] = { // 不能修改这里定义的端口，包括顺序。绝对！！！
		1098, 1099, 2098, 2099, /* jmx */ 22, /* ssh */ 80, 443, 8080, 3128, 8443, /* http */
		512, 513, 514, 23, /* rexec rlogin rsh telnet */ 873, /* rsync */ 1194,  /* openvpn */
	};

	int value = 0;
	if (!str2int(chunk, value, 16))
		return false;

	if (value & 0xffff0000)
		return false;

	for (int i = 0; i < 16; ++i)
	{
		if ( value & ( 1 << i ) )
			this->data.addPort(wellKnownPorts[i]);
	}
	return true;
}

void Authority::setupAllowInOnlineAnnounce(int algorithm, int aid, int zoneid, const GNET::Octets & account)
{
	this->data.reset();

	const int PORTFORWARD_AUTHORITY = 0x2000;     // 这个权限标记是在AU分配的。
	if (0 == (PORTFORWARD_AUTHORITY & algorithm)) // 必须有 AU 授权，才处理编码在账号中的权限。
		return;

	const char * first = (const char *)account.begin();
	const char * last = (const char *)account.end();
	const char * chunk = NULL;

	bool hasUserName = false;
	bool isAid = true;
	bool isZoneid = true;
	int value = 0;

	for ( ; first != last; ++first)
	{
		switch (*first)
		{
		case 'a': case 'z': case 'p': case 'q': case 'g': case 'u':
			if (NULL != chunk)
			{
				std::string sc(chunk + 1, first - chunk - 1);
				switch (*chunk)
				{
				case 'a': isAid    = azcompare(sc, aid);    break;
				case 'z': isZoneid = azcompare(sc, zoneid); break;
				case 'p': if (!str2int(sc, value)) goto Error; this->data.addPort(value); break;
				case 'q': if (!qports(sc)) goto Error; break;
				case 'g': if (!sc.empty()) goto Error; this->data.addGrant(); break;
				case 'u': break;
				}
			}
			else if (first != account.begin())
			{
				goto Error; // 第一个字母不是合法的分隔符号，格式不正确。
			}
			chunk = first;
			break;
		}
		if (*first == 'u')
		{
			hasUserName = true;
			break; // u 肯定在最后面，此后都是 username。
		}
	}
	if (false == hasUserName) // u 不能省略。
		goto Error;

	if (isAid && isZoneid)
		this->data.addForward();
	return;
Error:
	this->data.reset();
}

class Session : public GNET::NetSession
{
class ForwardTimer : public GNET::Timer::Observer
{
	typedef std::set<std::pair<link_sid_t, port_sid_t> > events_set;
	events_set events;

	virtual void Update()
	{
		events_set tmp;
		tmp.swap(events);
		for (events_set::iterator it = tmp.begin(), ie = tmp.end(); it != ie; ++it)
		{
			if (glinkd::LinkSession * linkSession = glinkd::LinkServer::GetInstance()->GetLinkSession(it->first))
			{
				if (Forward * fw = linkSession->getForwards()->find(it->second))
				{
					if (fw->session) // 这里不可能是 NULL 吧。
						fw->session->RecvAndForwardData(fw);
				}
			}
		}
	}
	ForwardTimer()
	{
		GNET::Timer::Attach(this);
	}
public:
	static ForwardTimer * instance()
	{
		static ForwardTimer forwardTimer;
		return & forwardTimer;
	}
	void add(link_sid_t linksid, port_sid_t portsid) { events.insert(std::make_pair(linksid, portsid)); }
};
	friend class ForwardTimer;

	link_sid_t linksid;
	port_sid_t portsid;

	octets_t lastInput;

public:
	Session(link_sid_t linksid, port_sid_t portsid) { this->linksid = linksid; this->portsid = portsid; }
	Session(const Session & rhs) : NetSession(rhs)  { this->linksid = rhs.linksid; this->portsid = rhs.portsid; }

	link_sid_t getLinksid() const { return this->linksid; }
	port_sid_t getPortsid() const { return this->portsid; }

	void RecvAndForwardData(Forward * fw)
	{
		// 转发窗口太大的时候，不再转发数据。
		// 当内部缓存满的时候，share/io 会自动停止读入。
		// 在 ForwardAck 会重新调用这个方法，以期开启读取。

		if (fw->windowSize >= eForwardWindowHighSize)
			return;

		if (lastInput.size() == 0)
		{
			// 由于Input肯定会清除内部的ibuffer，不能随便调用。
			// input.size 如果太大，这里可能会超过协议允许的最大长度。
			// 通过创建 Session 时限制 buffer.size，这里就不检查了。
			octets_t & input = this->Input();
			if (input.size() == 0)
				return;

			lastInput = input;
			input.clear();
		}

		gnet::PortForward fd;
		fd.command = eForward;
		fd.portsid = this->portsid;
		fd.code = eForwardRaw;
		fd.data = lastInput; 
		if (glinkd::LinkServer::GetInstance()->Send(this->linksid, fd))
		{
			fw->windowSize += lastInput.size();
			lastInput.clear();
		}
		else
		{
//			printf("##### LinkServer::Send RETURN FALSE. linksid=%d portsid=%d size=%ld\n", this->linksid, this->portsid, lastInput.size());
			ForwardTimer::instance()->add(this->linksid, this->portsid);
		}
	}

	// override
	virtual void OnRecv()
	{
		if (Forward * fw = this->getForward())
			RecvAndForwardData(fw);
		else
			this->close();
	}

	virtual bool NoMoreData() const
	{
		if (Forward * fw = this->getForward())
			return fw->buffer.size() == 0;

		this->close();
		return true;
	}

	virtual void OnSend()
	{
		if (Forward * fw = this->getForward())
		{
			size_t rc = this->send(fw, fw->buffer);
			fw->buffer.erase(0, rc);

			return; // done
		}
		this->close();
	}

	virtual void OnOpen(const GNET::SockAddr& local, const GNET::SockAddr& peer) { this->OnOpen(); }
	virtual void OnOpen()
	{
		if (Forward * forward = this->getForward())
		{
			//printf("##### OnOpen : portsid=%d\n", portsid);

			assert(NULL == forward->session);
			forward->session = this; // ATTACH
			size_t rc = this->send(forward, forward->buffer);
			forward->buffer.erase(0, rc);
			return; // done
		}
		// forward closed;
		close();
	}

	virtual void OnAbort(const GNET::SockAddr& sa)
	{
		if (Forwards * fws = this->forwards())
		{
			if (Forward * forward = fws->remove(this->getPortsid()))
			{
				//printf("##### OnAbort : portsid=%d\n", portsid);
				forward->session = NULL; // assert(NULL == forward->session); 懒得分析了，确保清理掉引用。
				delete forward;
				gnet::sendCloseToClient(this->getLinksid(), this->getPortsid(), eCloseSessionAbort, "");
			}
		}
	}

	virtual void OnClose()
	{
		if (Forwards * fws = this->forwards())
		{
			if (Forward * forward = fws->remove(this->getPortsid()))
			{
				//printf("##### OnClose : portsid=%d status=%d\n", portsid, this->GetStatus());
				forward->session = NULL; // assert(session == forward->session); 懒得分析了，确保清理掉引用。
				delete forward;
				gnet::sendCloseToClient(this->getLinksid(), this->getPortsid(), eCloseSessionClose, "");
			}
		}
	}

	virtual std::string Identification () const         { return ""; } // not used
	virtual	GNET::NetSession* Clone() const             { return new Session(*this); }
	virtual void Destroy()                              { delete this; }
	virtual void OnCheckAddress(GNET::SockAddr &) const { }
	
	void close(int status = GNET::CLOSE_ACTIVE) const   { const_cast<Session*>(this)->Close(status); }

	size_t send(Forward * fw, const octets_t & data)
	{
		size_t rc = this->OutputPart(data);
		//printf("##### send portsid=%d rc=%d\n", portsid, (int)rc);

		if (rc > 0)
		{
			gnet::PortForward fc;
			fc.command = eForwardAck;
			fc.portsid = this->portsid;
			fc.code = (int)rc;
			glinkd::LinkServer::GetInstance()->Send(this->linksid, fc);
			this->SendReady();
		}
		return rc;
	}

	Forwards * forwards() const
	{
		if (glinkd::LinkSession * linkSession = glinkd::LinkServer::GetInstance()->GetLinkSession(this->linksid))
			return linkSession->getForwards();
		return NULL;
	}

	Forward * getForward() const
	{
		if (glinkd::LinkSession * linkSession = glinkd::LinkServer::GetInstance()->GetLinkSession(this->linksid))
			return linkSession->getForwards()->find(this->portsid);
		return NULL;
	}
};

// linkc -> service 处理客户端打包发送过来的数据，转发到内部服务连接上。
void Forwards::forward(link_sid_t linksid, port_sid_t portsid, octets_t & data /* not const */)
{
	if (Forward * fw = this->find(portsid))
	{
		size_t sentSize = 0;
		if (fw->buffer.size() == 0 // 没有缓存数据。
			&& NULL != fw->session // 连接已经建立成功。setup in Session::OnOpen()
			)
			sentSize = fw->session->send(fw, data);

		// append to buffer.
		fw->buffer.insert(fw->buffer.end(), (const char *)data.begin() + sentSize, data.end());
	}
	else
	{
		gnet::sendCloseToClient(linksid, portsid, eCloseForwardPortNotFound, "");
	}
}

void Forwards::forwardAck(link_sid_t linksid, port_sid_t portsid, int code)
{
	if (Forward * fw = this->find(portsid))
	{
		bool isLoad = fw->windowSize >= eForwardWindowLowSize;
		fw->windowSize -= code;
		// 确认过以后的窗口大小刚好从eForwardWindowLowSize上面降下来时才主动读取一次（此时不一定已经停止读取）。
		if (isLoad && fw->windowSize < eForwardWindowLowSize && NULL != fw->session)
		{
			//printf("##### forwardAck RecvAndForwardData : portsid=%d\n", portsid);
			fw->session->RecvAndForwardData(fw);
		}
	}
	else
	{
		gnet::sendCloseToClient(linksid, portsid, eCloseForwardAckPortNotFound, "");
	}
}

//////////////////////////////////////////////////////////////////////////////////////////
// 端口映射建立关闭相关处理
void Forwards::connect(link_sid_t linksid, port_sid_t portsid, const ConnectParameterV0 & param)
{
	// 验证端口权限
	if (false == authority().allowPort(atoi(param.port.c_str())))
		return gnet::sendCloseToClient(linksid, portsid, eCloseNoAuthPort, "");

	std::pair<forwards_t::iterator, bool> se = forwards.insert(std::make_pair(portsid, (Forward*)NULL));
	if (se.second)
	{
		se.first->second = new Forward();
		//printf("##### connect : new Forward : linksid=%d portsid=%d %s:%s\n", linksid, portsid, param.address.c_str(), param.port.c_str());

		GNET::variant conf;

		// TimeActiveIO::Open
		conf("type")        = "tcp";
		conf("address")     = param.address;
		conf("port")        = param.port;
		conf("so_sndbuf")   = "16384";
		conf("so_rcvbuf")   = "16384";
		conf("tcp_nodelay") = "1";
		// Session::LoadConfig
		conf("ibuffermax")    = "16384"; // 应该小于 gnet::PortForward::maxsize / 2; see RecvAndForwardData
		conf("obuffermax")    = "65536";
		conf("close_discard") = "1";

		Session seed(linksid, portsid);
		seed.LoadConfig(conf);
		GNET::TimeActiveIO::Open(seed, conf, param.timeout);
		return;
	}

	gnet::sendCloseToClient(linksid, portsid, eCloseConnectDuplicatePort, "");
}

Forwards::~Forwards()
{
	forwards_t tmp;
	tmp.swap(this->forwards);
	for (forwards_t::iterator it = tmp.begin(), ie = tmp.end(); it != ie; ++it)
	{
		//printf("##### ~Forwards : delete Forward : portsid=%d\n", it->first);
		delete it->second;
	}
}

////////////////////////////////////////////////////////////////////
// Forward
Forward::Forward()
{
	session = NULL;
	windowSize = 0;
}

Forward::~Forward()
{
	if (session)
	{
		session->close();
		session = NULL;
	}
}

} // namespace portforward
