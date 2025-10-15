#ifndef __Nuclear_GROUPMANAGER_H
#define __Nuclear_GROUPMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "ifileio.h"

namespace Nuclear
{

class GroupManager
{
	typedef std::set<std::wstring> Group;
	typedef std::map<std::wstring, Group> Groups;
	// 构造析构
public:
	GroupManager(IFileIO *pIFileIO, const std::wstring &storepath ) : m_pIFileIO(pIFileIO), m_store(storepath) { }
	~GroupManager() { }
public:

	bool Load();
	bool Save() const;

	bool AddGroup(const std::wstring &groupname);
	bool DelGroup(const std::wstring &groupname);
	bool AddElement(const std::wstring &groupname, const std::wstring &membername);
	bool DelElement(const std::wstring &groupname, const std::wstring &membername);
	bool DelElement(const std::wstring &membername); // from all groups
	bool FindElement(const std::wstring &groupname, const std::wstring &membername) const;
	void DumpGroup(const std::wstring &groupname, std::vector<std::wstring> &members) const;
	void DumpGroups(std::vector<std::wstring> &groups) const;

private:
	IFileIO *m_pIFileIO;
	std::wstring m_store;

	Groups m_groups;
	// 占坑
private:
	GroupManager(const GroupManager&);
	GroupManager& operator=(const GroupManager&);

};


}


#endif