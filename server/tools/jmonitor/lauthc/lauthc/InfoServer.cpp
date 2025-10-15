
#include "InfoServer.hpp"
#include "infoserver/CGetServerInfo.hpp"

namespace lauthc { 


	InfoServer InfoServer::instance;

	const GNET::Protocol::Manager::Session::State* InfoServer::GetInitState() const
	{
		return &lauthc::state::InfoServer;
	}

	void InfoServer::OnAddSession(Session::ID sid)
	{
		//TODO
	}

	void InfoServer::OnDelSession(Session::ID sid)
	{
		//TODO
	}


	void InfoServer::InsertLinkClient( LinkClient* lc)
	{
		int id = NextID();
		m_linkclients.insert( std::make_pair( id, lc));
		lc->GetForwards().SetLinkClientID( id);
	}

	LinkClient* InfoServer::GetLinkClient( int id)
	{
		LinkClientMap::const_iterator it = m_linkclients.find( id);
		if( it == m_linkclients.end())
			return NULL;
		else
			return it->second;
	}

	void InfoServer::MakeServerInfosList( std::list<infoserver::ServerInfos>& list) const
	{
		for( LinkClientMap::const_iterator it = m_linkclients.begin(), ite = m_linkclients.end(); it != ite; ++ it)
		{
			LinkClient* client = it->second;
			list.push_back( infoserver::ServerInfos( client->GetNameAsOctets(), client->GetListenPort1(), client->GetListenPort2()));
		}
	}

} // namespace lauthc {


namespace infoserver {

	void CGetServerInfo::Process( GNET::Protocol::Manager* manager, GNET::Protocol::Manager::Session::ID sid)
	{
		SGetServerInfo	protocol;

		lauthc::InfoServer::GetInstance()->MakeServerInfosList( protocol.infos);
		manager->Send( sid, protocol);
	}

} // namespace infoserver {
