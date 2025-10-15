#include "StdAfx.h"
#include "FileViewer.h"
#include "FileManager.h"
#include "SetupMetaFile.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

CFileManager::CFileManager(void)
{
	m_root.SetIsPath(true);
}

CFileManager::~CFileManager(void)
{
	//for( CMountMap::iterator itor = m_mounts.begin(); itor!=m_mounts.end(); ++itor)
	//	PFS::CEnv::Unmount( itor->second);
}

//// 得到指定目录下子结点
//CZipTreeItem* CFileManager::GetChild( const std::wstring& path)
//{
//	if( path.empty())
//		return &m_root;
//	CZipTreeItem* pParent = &m_root;
//	std::wstring current;
//	std::wstring::size_type begin = 0;
//	while (true)
//	{
//		CZipTreeItem::ZipTreeItemMap& child = pParent->GetChild();
//		std::wstring::size_type pos = path.find_first_of( L"/", begin);
//		begin = pos+1;
//		if ((pos == std::wstring::npos) || (pos == path.size()-1))
//		{
//			CZipTreeItem::ZipTreeItemMap::iterator itor = child.find( path);
//			if( itor == child.end())
//				break;
//			return &(itor->second);
//		}
//		current = path.substr(0, begin);
//		CZipTreeItem::ZipTreeItemMap::iterator itor = child.find(current);
//		if( itor == child.end())
//			break;
//		pParent = &(itor->second);
//	}
//	return NULL;
//}

//void CFileManager::MakeFileTree( const std::wstring& filename)
//{
	//std::wstring newname( filename);
	//newname.erase( 0, newname.find_first_not_of( L'/'));

	//CZipTreeItem* pParent = &m_root;
	//std::wstring current;
	//std::wstring::size_type begin = 0;
	//while (true)
	//{
	//	CZipTreeItem::ZipTreeItemMap& child = pParent->GetChild();
	//	std::wstring::size_type pos = newname.find_first_of( L"/", begin);
	//	begin = pos+1;
	//	CZipTreeItem childItem;
	//	if( (pos == std::wstring::npos) || (pos == newname.size()-1))
	//	{
	//		childItem.SetIsPath( false);
	//		childItem.SetName( newname);
	//		childItem.SetIsCheck( !CFileIgnore::GetInstance().IsFileIgnore( newname));
	//		childItem.SetZipLevel( CFileAttribute::GetInstance().GetZipLevel( newname));
	//		childItem.SetAttributes( CFileAttribute::GetInstance().GetSetupLevel( newname));
	//		child.insert( std::make_pair( newname, childItem));
	//		break;
	//	}
	//	current = newname.substr( 0, begin);
	//	childItem.SetIsPath( true);
	//	childItem.SetName( current);
	//	childItem.SetIsCheck( !CFileIgnore::GetInstance().IsFileIgnore( current));
	//	std::pair< CZipTreeItem::ZipTreeItemMap::iterator, bool> pr = child.insert( std::make_pair( current, childItem));
	//	pParent = &(pr.first->second);
	//}
//}

bool CFileManager::IsItemDefaultLevel( CZipTreeItem& item, bool bIsResource)
{
	if( !item.GetIsPath())
	{
		if( bIsResource)
			return item.GetZipLevel() == m_project->GetAttribute().GetZipLevel( item.GetName());
		else
			return item.GetAttributes() == m_project->GetAttribute().GetSetupLevel( item.GetName());
	}
	CZipTreeItem::ZipTreeItemMap& child = item.GetChild();
	for( CZipTreeItem::ZipTreeItemMap::iterator itor = child.begin(); itor != child.end(); ++itor)
	{
		if( !IsItemDefaultLevel( itor->second, bIsResource))
			return false;
	}
	return true;
}


