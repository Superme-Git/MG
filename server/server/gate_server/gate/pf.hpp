
#pragma once

#include <assert.h>
#include "gnet/PortForward.hpp"
#include "pfp.hpp"

namespace portforward
{

typedef GNET::Protocol::Manager::Session::ID link_sid_t;
typedef int serv_sid_t;
typedef int port_sid_t;
typedef GNET::Octets octets_t;

class Session;

/*
数据转发；
流量控制；
管理连接；
缓存管理；
*/
// 维护转发连接，这里只定义结构，主要的处理都写在 Forwards 和 Session 中。
struct Forward
{
	// 流量控制
	long windowSize;   // 未确认的数据大小。

	//long forwardSize;  // :recv 并转发的数据总量。see OnRecv
	//long sendSize;     // ::send 出去的数据总量。

	octets_t   buffer;  // 转发连接没有建立成功时，缓存收到的数据。
	Session *  session;

	Forward();
	~Forward();
private:
	Forward(const Forward&);
	void operator=(const Forward&);
};

class Authority
{
	bool str2int(const std::string & chunk, int & out, int base = 10)
	{
		const char * nptr = chunk.c_str();
		char * endptr = 0;
		out = strtol(nptr, &endptr, base);
		if (*nptr && !*endptr)
			return true;
		return false;
	}

	// 把chunk转换成数字并和az比较。chunk格式有误时返回false。
	bool azcompare(const std::string & chunk, int az)
	{
		int out = 0;
		if (str2int(chunk, out))
			return out == az;
		return false;
	}

	// 把 ‘q’ 段中的端口解析出来并添加到端口集合中去
	bool qports(const std::string & chunk);

	AuthorityParameter data;

	friend class gnet::PortForward;
	void swap(AuthorityParameter & param)
	{
		const int tmp = data.allow;
		data.allow = param.allow;
		param.allow = tmp;

		data.ports.swap(param.ports);
	}
public:
	bool allowGrant() const        { return data.allowGrant(); }
	bool allowForward() const      { return data.allowForward(); }
	bool allowPort(int port) const { return data.allowPort(port); }

	const AuthorityParameter & getData() const { return data; }

	void setupAllowInOnlineAnnounce(int algorithm, int aid, int zoneid, const GNET::Octets & account);
};

/*
	建立的映射。
*/
class Forwards
{
	typedef std::map<port_sid_t, Forward*> forwards_t;
	forwards_t forwards;

	Forwards(const Forwards&);
	void operator=(const Forwards&);

	Authority auth;
public:
	Forwards() { }
	~Forwards();

	Authority & authority() { return auth; }

	void dump();

	// command
	void connect(link_sid_t linksid, port_sid_t portsid, const ConnectParameterV0 & param);
	void forward(link_sid_t linksid, port_sid_t portsid, octets_t & data /* not const */);
	void forwardAck(link_sid_t linksid, port_sid_t portsid, int code);

	Forward * find(port_sid_t portsid)
	{
		forwards_t::iterator it = forwards.find(portsid);
		if (it != this->forwards.end())
			return it->second;
		return NULL;
	}

	Forward * remove(port_sid_t portsid)
	{
		Forward * tmp = NULL;
		forwards_t::iterator it = forwards.find(portsid);
		if (it != this->forwards.end())
		{
			tmp = it->second;
			forwards.erase(it);
		}
		return tmp;
	}
};

} // namespace portforward

