
#pragma once

#include "rpcgen.hpp"
#include "gnet/PortForward.hpp"
#include "pfio.hpp"

namespace portforward
{

	/*
	数据转发；
	流量控制；
	管理连接；
	缓存管理；
	*/
	class Forward
	{
	public:
		Forward( int sid, int accid, int lid);
		~Forward();

		int  getPortsid() const          { return this->portsid; }
		long getAcceptorId() const       { return this->acceptorid; }

		///////////////////////////////////////////////////////////
		// PortForward 数据相关命令
		void ForwardData(const GNET::Octets & data);
		void ForwardAck(long ackedSize);

		std::string str() const;

	private:
		int 	linkid;
		int  	portsid;    // 端口转发编号
		int 	acceptorid; // 本地监听编号

		// 流量控制
		int windowSize;   // 未被确认的转发数据
		int forwardSize;  // :recv 并转发的数据总量。see OnRecv
		int sendSize;     // ::send 出去的数据总量。

		GNET::Octets buffer;
		PortForwardIO * io;

		friend class PortForwardIO;

		// 发生错误时抛出异常
		int send(const GNET::Octets & data);

		// IO events
		void OnIORecv( GNET::Octets& data);
		void OnIOSend();
		void OnIODestroy();

		// 禁止拷贝
		Forward(const Forward&);
		void operator=(const Forward&);
	};

	class Forwards //: public aio::ASocketFactory
	{
		typedef std::map<int, Forward *> forwards_t;
		forwards_t forwards; // portsid -> Forward

		typedef std::map<int, LocalAcceptor *> acceptor_t;
		acceptor_t acceptor; // acid -> LocalAcceptor

		int 		m_linkclientid;
	public:
		~Forwards();

		void SetLinkClientID( int id)
		{
			m_linkclientid = id;
		}
		int GetLinkClientID() const
		{
			return m_linkclientid;
		}

		// 命令
		// 新建本地转发端口
		bool cmdNewLocal( const std::string& servername, const std::string& host, int port, const std::string& rhost, const std::string& rport);
		// 关闭本地监听。closePorts 是否一起关闭它创建的映射。
		void cmdCloseLocal( int acceptorid, bool closePorts);
		void cmdCloseForward(int portsid, int reason); // 关闭某个映射并且发送协议给远端。
		void cmdPrintLocal();              // 打印所有端口映射信息。

		void cmdCloseAll();

		// 协议
		void process(const gnet::PortForward & pf);

		LocalAcceptor* GetLocalAcceptor( int accid)
		{
			acceptor_t::const_iterator it = acceptor.find( accid);
			if( it == acceptor.end())
				return NULL;
			else
				return it->second;
		}

		LocalAcceptor* RemoveLocalAcceptor( int accid)
		{
			acceptor_t::iterator it = acceptor.find( accid);
			if( it == acceptor.end())
				return NULL;
			LocalAcceptor* la = it->second;
			acceptor.erase( it);
			return la;
		}


		// Forward 管理
		Forward * newForward(int portsid, int acceptorid)
		{
			std::pair<forwards_t::iterator, bool> ii = forwards.insert(std::make_pair(portsid, (Forward*)NULL));
			if (ii.second)
				ii.first->second = new Forward( portsid, acceptorid, m_linkclientid);
			return ii.first->second;
		}

		Forward * findForward(int portsid)
		{
			forwards_t::iterator it = forwards.find(portsid);
			if (it != forwards.end())
				return it->second;
			return NULL;
		}

		Forward * removeForward(int portsid)
		{
			Forward * tmp = NULL;
			forwards_t::iterator it = forwards.find(portsid);
			if (it != forwards.end())
			{
				tmp = it->second;
				forwards.erase(it);
			}
			return tmp;
		}

		void RemoveAcceptorSID( int accid, int sid)
		{
			if( LocalAcceptor* la = GetLocalAcceptor( accid))
				la->portsids.erase( sid);
		}
	};


} // namespace portforward

