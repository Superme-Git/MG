#include   "stdafx.h"
#include "Project.h"
#include <sstream>
#include "Shlwapi.h"
typedef const wchar_t XKEY[];
static XKEY PATHS = L"Paths";
static XKEY FILEATTRIBUTE = L"FileAttribute";
static XKEY FILEIGNORE = L"FileIgnore";
static XKEY OPERATORPATH = L"OperatorPath";
static XKEY OPERATORTYPE = L"OperatorType";
static XKEY OPERATIONNAME = L"Operation";
static XKEY NAME = L"Name";
static XKEY ISPATH = L"IsPath";
static XKEY APPLYCHILD = L"ApplyChild";
static XKEY ZIPLEVEL = L"ZipLevel";
static XKEY SETUPLEVEL = L"SetupLevel";
static XKEY MOUNTMANAGER = L"MountManager";
static XKEY VERSIONMETA = L"VersionMeta";
static XKEY ISMAKEMD5 = L"IsMakeMD5";

bool CFileIgnore::IsFileIgnore( const std::wstring& file)
{
	std::wstring tempfile = file;
	tempfile.erase( tempfile.find_last_not_of( L'/') + 1);
	bool bFirstLevel = ( tempfile.find( L'/') == std::wstring::npos);

	for( CFileIgnoreMap::iterator itor = m_ignores.begin(); itor != m_ignores.end(); ++itor)
	{
		const std::wstring& ignore = itor->first;
		if( itor->second.bIsPath)
		{
			std::wstring temp( ignore);
			temp += L'/';
			std::wstring::size_type pos = file.find( temp);
			if( pos == 0)
				return true;
			if( (pos != std::wstring::npos) && (file[pos-1] == L'/'))		// 在子目录下出现
			{
				if( itor->second.bApplyChild)
					return true;
			}
		}
		else
		{
			// 判断 file 是否以 ignore 结尾
			int pos = file.size() - ignore.size();
			if( pos >= 0 && ( 0 == _tcscmp( file.c_str()+pos, ignore.c_str())))
			//std::wstring sExt = GetFileExt( file);
			//if( sExt == itor->first)
			{
				if( bFirstLevel ? true : itor->second.bApplyChild)
					return true;
			}
		}
	}
	return false;
}

bool CFileIgnore::Read( const XMLIO::CINode& rn)
{
	size_t count = rn.GetChildrenCount();
	XMLIO::CINode child;
	std::wstring strName;
	for( size_t i=0; i<count; i++)
	{
		rn.GetChildAt( i, child);
		CIgnoreItem item;
		item.bIsPath = !!child.GetAttributeInteger( ISPATH);
		item.bApplyChild = !! child.GetAttributeInteger( APPLYCHILD);
		child.GetAttribute( NAME, strName);
		m_ignores.insert( std::make_pair( strName, item));
		
	}
	return true;
}

bool CFileIgnore::Write( XMLIO::CONode& wn)
{
	XMLIO::CONode child;
	for( CFileIgnoreMap::iterator itor = m_ignores.begin(); itor!=m_ignores.end(); ++itor)
	{
		wn.CreateSubElement( L"child", child);
		child.SetAttribute( NAME, itor->first);
		child.SetAttribute( ISPATH, itor->second.bIsPath ? 1 : 0);
		child.SetAttribute( APPLYCHILD, itor->second.bApplyChild ? 1 : 0);
	}
	return true;
}

// 得到压缩图标索引
int CFileAttribute::GetZipLevel(const std::wstring& sFile)
{
	std::wstring sExt = GetFileExt( sFile);
	CAttributeMap::const_iterator itor = m_attributes.find(sExt);
	if (itor != m_attributes.end())
	{
		return itor->second.nZipLevel;
	}
	return RADIO1;
}

int CFileAttribute::GetSetupLevel( const std::wstring& sFile)
{
	std::wstring sExt = GetFileExt( sFile);
	CAttributeMap::const_iterator itor = m_attributes.find(sExt);
	if (itor != m_attributes.end())
	{
		return itor->second.nSetupLevel;
	}
	return ZipID;
}

bool CFileAttribute::Read( const XMLIO::CINode& rn)
{
	size_t count = rn.GetChildrenCount();
	XMLIO::CINode child;
	std::wstring strName;
	for( size_t i=0; i<count; i++)
	{
		rn.GetChildAt( i, child);
		CAttributeItem item;
		item.nZipLevel = child.GetAttributeInteger( ZIPLEVEL);
		item.nSetupLevel = child.GetAttributeInteger( SETUPLEVEL);
		child.GetAttribute( NAME, strName);
		m_attributes.insert( std::make_pair( strName, item));
	}
	return true;
}

bool CFileAttribute::Write( XMLIO::CONode& wn)
{
	XMLIO::CONode child;
	for( CAttributeMap::iterator itor = m_attributes.begin(); itor!=m_attributes.end(); ++itor)
	{
		wn.CreateSubElement( L"child", child);
		child.SetAttribute( NAME, itor->first);
		child.SetAttribute( ZIPLEVEL, itor->second.nZipLevel);
		child.SetAttribute( SETUPLEVEL, itor->second.nSetupLevel);
	}
	return true;
}

std::wstring CMountManager::GetMaxMountStr()
{
	std::wostringstream os;
	os << L"/src" << m_mounts.size();
	return os.str();
}

std::wstring CMountManager::GetMountStr( const std::wstring& path)
{
	std::wstring temp = path;
	if( GetRelatePath( temp))
	{
		std::replace( temp.begin(), temp.end(), L'\\', L'/');
		return temp;
	}
	//CMountMap::iterator itor = m_mounts.find( temp);
	//if( itor != m_mounts.end())
	//	return itor->second;
	std::wstring mount = GetMaxMountStr();
	PFS::CEnv::Mount( mount, temp, PFS::FST_NATIVE, PFS::MT_RUNTIME);
	m_mounts.insert( std::make_pair( mount, temp));
	return mount;
}

void CMountManager::CloseAllFiles()
{
	//for( CMountMap::iterator itor = m_mounts.begin(); itor != m_mounts.end(); ++itor)
	//	PFS::CEnv::CloseAllFiles( itor->second);
}

void CMountManager::MountAll()
{
	for( CMountMap::iterator itor = m_mounts.begin(); itor!=m_mounts.end(); ++itor)
		PFS::CEnv::Mount( itor->first, itor->second, PFS::FST_NATIVE, PFS::MT_RUNTIME);
}

void CMountManager::Close()
{
	for( CMountMap::iterator itor = m_mounts.begin(); itor!=m_mounts.end(); ++itor)
		PFS::CEnv::Unmount( itor->first);
	m_mounts.clear();
}

bool CMountManager::ChangeMountPath( const std::wstring& mount, const std::wstring& path)
{
	CMountMap::iterator itor = m_mounts.find( mount);
	if( itor != m_mounts.end())
	{
		if( itor->second != path)
		{
			PFS::CEnv::Unmount( mount);
			PFS::CEnv::Mount( mount, path, PFS::FST_NATIVE, PFS::MT_RUNTIME);
			itor->second = path;
			return true;
		}
	}
	return false;
}

bool CMountManager::Read( const XMLIO::CINode& rn)
{
	size_t count = rn.GetChildrenCount();
	XMLIO::CINode child;
	std::wstring strPath, strName;
	for( size_t i=0; i<count; i++)
	{
		rn.GetChildAt( i, child);
		child.GetAttribute( OPERATORPATH, strPath );
		child.GetAttribute( NAME, strName);

		
		if (PathIsRelativeW(strName.c_str())) //如果是相对路径，就转换成绝对路径
		{
			strName = m_mountRelativePath +  strName;
		}
		

		m_mounts.insert( std::make_pair( strPath, strName ) );
	}
	return true;
}

bool CMountManager::Write( XMLIO::CONode& wn)
{
	XMLIO::CONode child;
	for( CMountMap::iterator itor = m_mounts.begin(); itor!=m_mounts.end(); ++itor)
	{
		wn.CreateSubElement( L"child", child);
		child.SetAttribute( OPERATORPATH, itor->first);
		child.SetAttribute( NAME, itor->second);
	}
	return true;
}

bool CProject::Open( const std::wstring& filename)
{
	Close();


	//获取绝对Project的路径，用于mount相对路径

	size_t pos = filename.find_last_of(L"\\");
	wstring fpath;
	if ( pos != wstring::npos)
	{
		fpath = filename.substr(0, pos);
	}

	if (PathIsRelativeW(filename.c_str()))
	{
		WCHAR rpath[MAX_PATH];
		wmemset(rpath, 0, MAX_PATH);
		GetCurrentDirectoryW(MAX_PATH, rpath);				
		m_relativepath = rpath + fpath;

	}else
	{
		m_relativepath = fpath;
	}

	m_relativepath += L"\\";

	m_mountmanager.setMountRelativePath(m_relativepath);


	XMLIO::CFileReader reader;
	if( PFS::EC_SUCCESS != reader.OpenNativeFile( filename))
		return false;
	m_filename = filename;
	GetProjectName( filename, m_name);

	XMLIO::CINode root;
	reader.GetRootNode(root);
	m_bMakeMD5 = !!root.GetAttributeInteger( ISMAKEMD5);

	size_t count = root.GetChildrenCount();
	XMLIO::CINode child;
	for( size_t i=0; i<count; i++)
	{
		root.GetChildAt( i, child);
		std::wstring name = child.GetName();
		if( name == FILEATTRIBUTE)
			m_attribute.Read( child);
		else if( name == FILEIGNORE)
			m_ignore.Read( child);
		else if( name == PATHS)
			m_operatormanager.Read( child);
		else if( name == MOUNTMANAGER)
			m_mountmanager.Read( child);
		else if( name == VERSIONMETA)
			m_versionmeta.ReadNode( child);
	}
	m_mountmanager.MountAll();
	return true;
}

bool CProject::Save( const std::wstring& filename)
{
	XMLIO::CFileWriter write;
	XMLIO::CONode root,child;
	write.GetRootNode(root);
	// 写 project 属性
	root.SetAttribute( ISMAKEMD5, m_bMakeMD5);

	root.CreateSubElement( MOUNTMANAGER, child);
	m_mountmanager.Write( child );
	root.CreateSubElement( PATHS, child); 
	m_operatormanager.Write( child);
	root.CreateSubElement( FILEATTRIBUTE, child);
	m_attribute.Write( child);
	root.CreateSubElement( FILEIGNORE, child);
	m_ignore.Write( child);
	root.CreateSubElement( VERSIONMETA, child);
	m_versionmeta.WriteNode( child);
	
	if( PFS::EC_SUCCESS == write.SaveNative( filename))
	{
		m_filename = filename;
		GetProjectName( filename, m_name);
	}
	return true;
}

bool COperatorManager::Read( const XMLIO::CINode& rn)
{
	size_t count = rn.GetChildrenCount();
	XMLIO::CINode child;
	for( size_t i=0; i<count; i++)
	{
		rn.GetChildAt( i, child);
		COperatorItem item;
		child.GetAttribute( OPERATORPATH, item.m_path);
		child.GetAttribute( NAME, item.m_windowpath );
		item.m_ispath = !! child.GetAttributeInteger( ISPATH);
		item.m_type = ( COperatorManager::OPERATOR_TYPE)( child.GetAttributeInteger( OPERATORTYPE) );
		m_operators.push_back( item);
	}
	return true;
}

bool COperatorManager::COperatorItem::operator < ( const COperatorItem& right) const
{
	int icomp = m_path.compare(right.m_path);
	if( icomp < 0 )
	{
		return true;
	}

	if( icomp == 0 )
	{
		icomp = m_windowpath.compare(right.m_windowpath );
		if( icomp < 0 )
		{
			return true;
		}

		if( icomp == 0 )
		{
			if(	
			( 
				m_type  == OT_SETCHECKED ||
				m_type == OT_SETZIPLEVEL ||
				m_type == OT_SETURL ||
				m_type == OT_SETPACK ||
				m_type == OT_SETCHILDPACKAGE ) )
			{
				if( right.m_type == OT_SETCHECKED ||
					right.m_type == OT_SETZIPLEVEL ||
					right.m_type == OT_SETURL ||
					right.m_type == OT_SETPACK ||
					right.m_type == OT_SETCHILDPACKAGE )
				{
					return m_type < right.m_type;
				}
			}

			
			if( m_ispath == right.m_ispath )
			{
				return false;
			}
			else
			{
				return( m_ispath );
			}
		}
	}

	return false;
}

void CProject::SortOperators()
{
	m_operatormanager.SortOperators();
}

// 对操作符进行优化排序，可以发现一些重复项。待完整测试。
void COperatorManager::SortOperators()
{
	std::stable_sort( m_operators.begin(), m_operators.end() );
}	

bool COperatorManager::Write( XMLIO::CONode& wn)
{
	const size_t size = 16;
	wchar_t buf[size] = {0};
	XMLIO::CONode child; 

	for( COperatorItemVector::iterator itor = m_operators.begin(); itor != m_operators.end(); ++itor)
	{
		wn.CreateSubElement( L"child", child);
		child.SetAttribute( OPERATORPATH, itor->m_path);
		child.SetAttribute( NAME, itor->m_windowpath);
		child.SetAttribute( ISPATH, itor->m_ispath ? 1 : 0);
		child.SetAttribute( OPERATORTYPE, itor->m_type);
		child.SetAttribute( OPERATIONNAME, itor->GetOperationName());
	}
	return true;
}

bool CProject::GetProjectName( const std::wstring& filename, std::wstring& projectName)
{
	std::wstring::size_type posStart = filename.find_last_of( L"/\\");
	if( posStart == std::wstring::npos)
	{
		return false;
	}
	std::wstring::size_type posEnd = filename.find_last_of( L".");
	if( posEnd > posStart+1)
	{
		posEnd -= posStart+1;
	}
	else
	{
		posEnd = std::wstring::npos;
	}
	projectName = filename.substr( posStart+1, posEnd);
	return true;
}