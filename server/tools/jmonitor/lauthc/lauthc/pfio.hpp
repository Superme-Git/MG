#pragma once

#include <io/pollio.h>

namespace portforward {

	class Forward;

	class PortForwardIO : public GNET::PollIO
	{
		friend class Forward;
		int 						linkid;
		int 						acceptorid;
		Forward* 				forward;
	protected:
		virtual void PollIn();
		virtual void PollOut();
	public:
		PortForwardIO( int x, int lid, int accid, Forward* f);
		virtual ~PortForwardIO();

		int Send( const GNET::Octets& data);

		std::string str() const;
	};

	class LocalAcceptor : public GNET::PollIO
	{
		friend class Forwards;

		int 						linkid;
		int						acceptorid;
	private:
		LocalAcceptor( int x, int lid, int accid);
	protected:
		virtual void PollIn();
		virtual ~LocalAcceptor();
	public:
		static LocalAcceptor* Open( const std::string& servername, const std::string& host, int lport, int linkid, int accid);
	private:
		std::string rhost;
		std::string rport;

		std::set<int> portsids;
	public:
		void SetRemote( const std::string& host, const std::string& port);
		std::string getLocalInfo() const;
		int GetAcceptorID() const;

		std::string str() const;
	};

	void setDebugLevel( bool n);
	bool isDebugLevel();
	void OutLog( const char* info, ...);

} // namespace portforward {

