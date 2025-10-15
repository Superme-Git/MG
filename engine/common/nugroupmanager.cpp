#include "stdafx.h"
#include "groupmanager.h"
#include "xml.h"

namespace Nuclear
{

	bool GroupManager::Load()
	{
		XMLIO::CFileReader		fr;
		XBuffer xmlfiledata;
		if( !m_pIFileIO->GetFileImage(m_store, xmlfiledata))
			return false;

		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
			return false;

		XMLIO::CINode	root;
		if( !fr.GetRootNode(root) )
		{
			fr.CloseFile();
			return false;
		}

		m_groups.clear();

		XMLIO::CNodeList nl;
		root.GetChildren(nl);
		for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if ( L"group" != node.GetName()) continue;
			std::wstring groupname;
			if(!node.GetAttribute(L"name", groupname) || groupname.empty() ) continue;

			Group &g= m_groups[groupname];

			XMLIO::CNodeList memnodes;
			node.GetChildren(memnodes);
			for( XMLIO::CNodeList::iterator it2 = memnodes.begin(); it2 != memnodes.end(); ++ it2)
			{	
				XMLIO::CINode& memnode = *it2;
				if( L"member" != memnode.GetName()) continue;
				std::wstring memname;
				if( (!memnode.GetAttribute(L"name", memname)) || memname.empty() ) continue;

				g.insert(memname);
			}
		}
		fr.CloseFile();

		return true;
	}

	bool GroupManager::Save() const
	{

		XMLIO::CFileWriter fw;
		XMLIO::CONode node;
		fw.GetRootNode(node);

		for(Groups::const_iterator it = m_groups.begin(), ie = m_groups.end(); it != ie; ++it)
		{			
			XMLIO::CONode gnode;
			fw.CreateElement( node, L"group", gnode);
			gnode.SetAttribute( L"name", it->first);

			const Group &g = it->second;
			for(Group::const_iterator it2 = g.begin(), ie2 = g.end(); it2 != ie2; ++it2)
			{
				XMLIO::CONode memnode;
				fw.CreateElement( gnode, L"member", memnode);
				memnode.SetAttribute( L"name", *it2);
			}
		}

		if( XMLIO::EC_SUCCESS != fw.Save(m_store) )
			return false;

		return true;
	}

	bool GroupManager::AddGroup(const std::wstring &groupname) 
	{ 
		return m_groups[groupname].empty();
	}

	bool GroupManager::DelGroup(const std::wstring &groupname) 
	{ 
		Groups::iterator it = m_groups.find(groupname);
		if( it == m_groups.end() ) return false;
		m_groups.erase(it);
		return true; 
	}

	bool GroupManager::AddElement(const std::wstring &groupname, const std::wstring &membername) 
	{ 
		Groups::iterator it = m_groups.find(groupname);
		if( it == m_groups.end() ) return false;
		Group &g = it->second;
		Group::iterator it2 = g.find(membername);
		if( it2 != g.end() ) return false;
		g.insert(membername);
		return true; 
	}

	bool GroupManager::DelElement(const std::wstring &groupname, const std::wstring &membername) 
	{ 
		Groups::iterator it = m_groups.find(groupname);
		if( it == m_groups.end() ) return false;
		Group &g = it->second;
		Group::iterator it2 = g.find(membername);
		if( it2 == g.end() ) return false;
		g.erase(membername);
		return true;
	}

	bool GroupManager::FindElement(const std::wstring &groupname, const std::wstring &membername) const
	{
		Groups::const_iterator it = m_groups.find(groupname);
		if( it == m_groups.end() ) return false;
		const Group &g = it->second;
		Group::const_iterator it2 = g.find(membername);
		if( it2 == g.end() ) return false;
		return true;
	}

	bool GroupManager::DelElement(const std::wstring &membername) // from all groups
	{
		bool r = false;
		for(Groups::iterator it = m_groups.begin(), ie = m_groups.end(); it != ie; ++it)
		{
			Group &g = it->second;
			Group::iterator it2 = g.find(membername);
			if( it2 == g.end() ) continue;
			g.erase(membername);
			r = true;
		}
		return r;
	}

	void GroupManager::DumpGroup(const std::wstring &groupname, std::vector<std::wstring> &members) const
	{ 
		Groups::const_iterator it = m_groups.find(groupname);
		if( it == m_groups.end() ) return;
		for(Group::const_iterator it2 = it->second.begin(), ie2 = it->second.end(); it2 != ie2; ++it2)
			members.push_back(*it2);
	}

	void GroupManager::DumpGroups(std::vector<std::wstring> &groups) const
	{ 
		for(Groups::const_iterator it = m_groups.begin(), ie = m_groups.end(); it != ie; ++it)
			groups.push_back(it->first);
	}

};