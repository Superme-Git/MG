
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
����ת����
�������ƣ�
�������ӣ�
�������
*/
// ά��ת�����ӣ�����ֻ����ṹ����Ҫ�Ĵ���д�� Forwards �� Session �С�
struct Forward
{
	// ��������
	long windowSize;   // δȷ�ϵ����ݴ�С��

	//long forwardSize;  // :recv ��ת��������������see OnRecv
	//long sendSize;     // ::send ��ȥ������������

	octets_t   buffer;  // ת������û�н����ɹ�ʱ�������յ������ݡ�
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

	// ��chunkת�������ֲ���az�Ƚϡ�chunk��ʽ����ʱ����false��
	bool azcompare(const std::string & chunk, int az)
	{
		int out = 0;
		if (str2int(chunk, out))
			return out == az;
		return false;
	}

	// �� ��q�� ���еĶ˿ڽ�����������ӵ��˿ڼ�����ȥ
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
	������ӳ�䡣
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

