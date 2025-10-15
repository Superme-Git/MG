#include "rpcgen.hpp"
#include <iostream>
#include <unistd.h>
#include <netdb.h>
#include "LinkClient.hpp"
#include "InfoServer.hpp"

using namespace GNET;

namespace gnet { void InitUsbKeyAuthc(); }

namespace SERVERINFO {

	typedef std::vector<lauthc::GameServerInfo> ServerInfoList;

	class CServers
	{
		ServerInfoList		silist;
	public:
		CServers( const char* serversconf)
		{
			Load( serversconf);
		}
	private:
		void Load( const char* serversconf)
		{
			const int 	bsize = 128;
			char 			buffer[bsize];

			Conf					conf( serversconf);

			int count = atoi( conf.find( "main", "count").c_str());
			silist.resize( count);
			for( int i = 0; i < count; i ++)
			{
				sprintf( buffer, "server%d", i);
				lauthc::GameServerInfo&	info = silist[i];
				info.m_name = conf.find( buffer, "name");
				info.host = conf.find( buffer, "ip");
				info.port = atoi( conf.find( buffer, "port").c_str());
				info.eport = atoi( conf.find( buffer, "eport").c_str());
				info.m_listenport1 = atoi( conf.find( buffer, "listenjmxport1").c_str());
				info.m_listenport2 = atoi( conf.find( buffer, "listenjmxport2").c_str());
				info.m_gsip = conf.find( buffer, "gsip");
				info.m_gsjmxport1 = conf.find( buffer, "gsjmxport1");
				info.m_gsjmxport2 = conf.find( buffer, "gsjmxport2");
			}
		}
	public:
		const ServerInfoList& GetServerInfoList() const {
			return silist;
		}
		ServerInfoList& GetServerInfoList(){
			return silist;
		}
	};

	class MakeHostIP
	{
		class CHostNameException : public std::exception
		{
			std::string		m_info;
		public:
			CHostNameException( const std::string& info) throw ()
				: m_info( info)
			 {}
			virtual ~CHostNameException() throw() {}
		public:
		    virtual const char* what() const throw()
		    {
		    	return m_info.c_str();
		    }
		};

		std::map<std::string,std::string>	host2ipold;
		std::map<std::string,std::string>	host2ipnew;
	public:
		MakeHostIP()
		{
			Load();
		}
		~MakeHostIP()
		{
			Save();
		}
	private:
		static std::string GetHostIP( const std::string& hostname)
		{
			struct hostent* hostent = gethostbyname( hostname.c_str());
			if( NULL == hostent)
				return std::string();
			else if( 0 == hostent->h_length)
				return std::string();
			else
				return inet_ntoa( *((struct in_addr*)hostent->h_addr_list[0]));
		}
		void Save() const
		{
			FILE*		file = fopen( "lasthost.conf", "w+");
			if( NULL == file)
				throw CHostNameException( "open lasthost.conf failed!");
			fprintf( file, "[main]\n");
			fprintf( file, "count=%d\n", (int)host2ipnew.size());
			fprintf( file, "\n");

			int index = 0;
			for( std::map<std::string,std::string>::const_iterator it = host2ipnew.begin(), ite = host2ipnew.end(); it != ite; ++ it)
			{
				const std::string&	host = it->first;
				const std::string&	ip = it->second;

				fprintf( file, "[server%d]\n", index ++);
				fprintf( file, "host=%s\n", host.c_str());
				fprintf( file, "ip=%s\n", ip.c_str());
				fprintf( file, "\n");
			}
			fclose( file);
		}
		void Load()
		{
			const int 	bsize = 128;
			char 			buffer[bsize];

			Conf					conf( "lasthost.conf");

			int count = atoi( conf.find( "main", "count").c_str());

			for( int i = 0; i < count; i ++)
			{
				sprintf( buffer, "server%d", i);
				std::string host = conf.find( buffer, "host");
				std::string ip = conf.find( buffer, "ip");

				host2ipold.insert( std::make_pair( host, ip));

			}
		}
		std::string TryGetHostIP( const std::string& hostname) const
		{
			std::string ipaddr = GetHostIP( hostname);
			if( !ipaddr.empty())
				return ipaddr;

			std::map<std::string,std::string>::const_iterator it = host2ipold.find( hostname);
			if( it == host2ipold.end())
				return ipaddr;
			ipaddr = it->second;
			return ipaddr;
		}
		std::string GetHostIPAndStore( const std::string& hostname)
		{
			std::string ipaddr = TryGetHostIP( hostname);
			if( !ipaddr.empty())
				host2ipnew.insert( std::make_pair( hostname, ipaddr));
			return ipaddr;
		}
	public:
		static void TransServerInfoList( ServerInfoList& silist) {
			MakeHostIP		makehostip;
			for( ServerInfoList::iterator it = silist.begin(), ite = silist.end(); it != ite; ++ it)
			{
				lauthc::GameServerInfo& 	sinfo = *it;
				sinfo.ip = makehostip.GetHostIPAndStore( sinfo.host);

				if( sinfo.ip.empty())
					throw CHostNameException( sinfo.m_name + " : " + sinfo.host + " to ip failed!");
			}
		}
	};

} // namespace SERVERINFO {

std::string RandPort( const lauthc::GameServerInfo& sinfo)
{
	char	buffer[32];

	int count = sinfo.eport - sinfo.port + 1;
	int port = rand() % count + sinfo.port;
	sprintf( buffer, "%d", port);
	return buffer;
}

void RunServer()
{
	Conf *conf = Conf::GetInstance( NULL);
	lauthc::InfoServer* manager = lauthc::InfoServer::GetInstance();
	manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
	Protocol::Server(manager);
}

void RunClients( const char* serversconf)
{
	srand( time( NULL));

	Conf *conf = Conf::GetInstance( NULL);

	SERVERINFO::CServers		silist( serversconf);

	SERVERINFO::MakeHostIP::TransServerInfoList( silist.GetServerInfoList());

	lauthc::InfoServer* infoserver = lauthc::InfoServer::GetInstance();

	for( SERVERINFO::ServerInfoList::const_iterator it = silist.GetServerInfoList().begin(), ite = silist.GetServerInfoList().end(); it != ite; ++ it)
	{
		const lauthc::GameServerInfo& sinfo = *it;
		std::string 	port = RandPort( sinfo);

		lauthc::LinkClient *manager = new lauthc::LinkClient( sinfo);
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		conf->put( manager->Identification(), "address", sinfo.ip);
		conf->put( manager->Identification(), "port", port);
		Protocol::Client(manager);

		portforward::OutLog( "RunClients %s %s(%s):%s\n", sinfo.m_name.c_str(), sinfo.host.c_str(), sinfo.ip.c_str(), port.c_str());
		infoserver->InsertLinkClient( manager);
	}
}

int main(int argc, char *argv[])
{
	if (argc != 3 || access(argv[1], R_OK) == -1 || access(argv[2], R_OK) == -1)
	{
		std::cerr << "Usage: " << argv[0] << " configurefile serversconf" << std::endl;
		exit(-1);
	}

	portforward::setDebugLevel( true);

	Conf::GetInstance(argv[1]);
	Log::setprogname("lauthc");

	gnet::InitUsbKeyAuthc();

	RunServer();
	RunClients( argv[2]);

	Thread::Pool::AddTask(PollIO::Task::GetInstance(), true);
	Thread::Pool::Run();
	return 0;
}

