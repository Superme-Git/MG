#include "stdafx.h"
#include "SaveManager.h"
#include "SaveOperator.h"
#include "../../../pfs.h"

typedef const wchar_t* XKEY;
const XKEY PROJECTFILE = L"projectfile";
const XKEY OUTPUTFILE = L"outputfile";
const XKEY VERSIONMETA = L"versionmeta";
const XKEY VERSIONSTR = L"versionstring";
const XKEY LUV = L"luv";
const XKEY RELEASEDATE = L"release-date";
const XKEY ORIGINSIZE = L"size";
const XKEY UPDATESIZE = L"updatesize";


void CSaveManager::Initialize()
{
	m_root.SetIsPath( true);
	lastpfs = L"";
}

void CSaveManager::VerifyPath()
{
	size_t pos ;
	std::replace(m_pProjectFile.begin(), m_pProjectFile.end(), L'/', L'\\');
	std::replace(m_pOutputDir.begin(), m_pOutputDir.end(), L'/', L'\\');
	wstring dir;
	wstring pfsname;
	if((pos = m_pOutputDir.find_first_of(L":")) != m_pOutputDir.npos && m_pOutputDir[pos+1]!= L'\\')
	{
		m_pOutputDir.insert(pos+1, L"\\");
	}
	
	if((pos = m_pOutputDir.find_last_of(L"\\")) != m_pOutputDir.npos)
	{
		dir = m_pOutputDir.substr(0,pos+1);
		pfsname = m_pOutputDir.substr(pos+1);
	}
	if(pfsname.empty() || pfsname == L"default")
	{
		int pos1 = m_pProjectFile.find_last_of(L"\\");
		int pos2 = m_pProjectFile.find(L".pkproj");
		pfsname = m_pProjectFile.substr(pos1+1, pos2-pos1-1) + L".pfs";
	}
	else if ( (pos = pfsname.find_last_of(L'.')) == m_pOutputDir.npos)
	{
		pfsname.append(L".pfs" );
	}
	else if( pfsname.substr(pos).compare(L".zip") || pfsname.substr(pos).compare(L".pfs"))
	{
		pfsname = pfsname.substr(0, pos).append(L".pfs");
	}
	if(!IsDirectoryExisting(dir.c_str()))
	{
		if(!CreateDirectory(dir.c_str(), NULL))
		{
			MSGLOGW(L"输出文件路径无效,使用当前工具所在目录\n");
			m_pOutputDir = pfsname;
		}
	}
	else
	{
		m_pOutputDir = dir.append(pfsname);
	}
	
}

bool CSaveManager::SaveFile()
{
	CSaveOperator* saver = new CSaveOperator( m_root, m_project, m_pOutputDir, m_bResource, lastpfs);
	COperator::COperatorVector ov;
	ov.push_back( saver);
	ResetEvent(hEvent);
	m_thread.SetOperators( ov);
	m_thread.SetListener( &m_listener);
	COperator::m_bStop = false;
	MSGLOGW(L"开始打包...\n");
	m_thread.Start();
	std::wstringstream wss;
	if(SUCCEEDED(WaitForSingleObject(hEvent, INFINITE)) && m_thread.GetThreadResult())
	{
		MSGLOGW(L"\n打包完成!\n");
	}
	else
	{
		MSGLOGW(L"\n打包操作失败!\n");
		goto Finally;
	}
	wss << std::endl;
	wss << L"类    型:  " ;
	if( m_bResource)
	{
		wss << L"资源包" << std::endl;
	}
	else
	{
		if( lastpfs.empty())
		{
			wss << L"安装包"<< std::endl;
		}
		else
		{
			wss << L"临时更新包" << std::endl;
		}
	}
	wss << L"保存路径:  " << m_pOutputDir << std::endl;
	wss << L"统计信息:  " << std::endl;

	CAnalyseFile& analyse = saver->GetAnalyseFile();
	wss << analyse;

	if( !m_bResource && m_project.GetIsMakeMD5())
	{
		wss << L"安装包md5值：" << saver->GetMD5Value() << std::endl;
	}
	wss << L"---------------------------------------" << std::endl;
	MSGLOGW( wss.str().c_str() );

Finally:
	delete saver;
	return PFS::CEnv::GetLastError() == 0;
}

bool CSaveManager::OpenProject()
{
	VerifyPath();
	m_project.AddDefault();
	if(!m_project.Open(m_pProjectFile))
		return false;
	m_project.SetVersionMeta(MergeVersionMeta(m_project.GetVersionMeta(), m_versioninfo));
	COperator::COperatorVector ov;
	COperatorManager::COperatorItemVector& ops = m_project.GetOperators();
	for( COperatorManager::COperatorItemVector::iterator itor = ops.begin(); itor != ops.end(); ++itor)
	{
		COperator* ope = NULL;
		switch( itor->m_type)
		{
		case COperatorManager::OT_ADD:
			ope = new CAddOperator_Project( m_root, m_project, itor->m_windowpath, itor->m_ispath, itor->m_path);
			break;
		case COperatorManager::OT_ADDNEW:
			ope = new CAddNewPathOperator_Project( m_root, m_project, itor->m_windowpath, itor->m_path);
			break;
		case COperatorManager::OT_DELETE:
			ope = new CDeleteOperator_Project( m_root, m_project, itor->m_path);
			break;
		case COperatorManager::OT_SETCHECKED:
			ope = new CSetCheckedOperator_Project( m_root, m_project, itor->m_ispath, itor->m_path);
			break;
		case COperatorManager::OT_SETZIPLEVEL:
			ope = new CSetZipLevelOperator_Project( m_root, m_project, itor->m_path, itor->m_ispath, _wtoi( itor->m_windowpath.c_str()));
			break;
		case COperatorManager::OT_ADDPATH:
			ope = new CAddPathOperator_Project( m_root, m_project, itor->m_windowpath, itor->m_path);
			break;
		case COperatorManager::OT_SETURL:
			ope = new CSetUrlOperator_Project( m_root, m_project, itor->m_windowpath, itor->m_path);
			break;
		case COperatorManager::OT_SETPACK:
			ope = new CSetPackOperator_Project( m_root, m_project, itor->m_ispath, itor->m_path);
			break;
		case COperatorManager::OT_SETCHILDPACKAGE:
			ope = new CSetChildPackageOperator_Project( m_root, m_project, itor->m_ispath, itor->m_path);
			break;
		}
		if( ope)
			ov.push_back( ope);
	}
	COperatorThread thread;
	if( !ov.empty())
	{
		ResetEvent(hEvent);
		thread.SetOperators( ov);
		thread.SetListener( &m_listener);
		MSGLOGW(L"正在打开工程文件...\n");
		thread.Start();
	}
	else
		return false;
	if(SUCCEEDED(WaitForSingleObject(hEvent, INFINITE)) &&  m_thread.GetThreadResult())
	{
		MSGLOGW(L"打开工程文件成功!\n");
	}
	else
	{
		MSGLOGW(L"读取工程文件失败!\n");
	}
	for( COperator::COperatorVector::iterator itor = ov.begin(); itor != ov.end(); ++itor)
		delete *itor;

	return true;
}

void CSaveManager::Clear()
{
	m_project.Close();
}

bool CSaveManager::ParseXmlCfg(const std::wstring& filename)
{
	XMLIO::CFileReader reader;
	if( PFS::EC_SUCCESS != reader.OpenNativeFile( filename))
	{
		wprintf(L"未找到配置文件或配置文件无效！\n");
		return false;
	}

	XMLIO::CINode root;
	reader.GetRootNode(root);
	m_bResource = !!root.GetAttributeInteger( L"isResource");
	m_bSaveProject = !!root.GetAttributeInteger( L"bSaveProject");
	m_bSortOperators = !!root.GetAttributeInteger( L"bSortOperators");
	root.GetAttribute(PROJECTFILE, m_pProjectFile);
	m_pOutputDir = root.GetAttribute(OUTPUTFILE).c_str();
	size_t count = root.GetChildrenCount();
	XMLIO::CINode child;
	for( size_t i=0; i<count; i++)
	{
		root.GetChildAt( i, child);
		std::wstring name = child.GetName();		
		if( name == VERSIONMETA)
		{
			m_versioninfo = m_project.GetVersionMeta();
			wstring versionstr;
			child.GetAttribute(VERSIONSTR, versionstr);
			if(versionstr == L"+1")
				m_bVersionAdd1 = true;
			else if(!versionstr.empty())
				PFSX::String2Version(versionstr, m_versioninfo.m_Version);
			wstring luv;
			child.GetAttribute( LUV, luv );		
			if(luv == L"+1")
				m_bLUVadd1 = true;
			else if(!luv.empty())
				PFSX::String2Version(luv, m_versioninfo.m_luv);
			wstring releaseDate;
			child.GetAttribute( RELEASEDATE, releaseDate);
			if(releaseDate == L"NOW" ||  releaseDate.empty())
				m_versioninfo.m_ReleaseDate = time(0);
			else
				m_versioninfo.m_ReleaseDate = ParseDate(releaseDate);
			
			size_t serversCount = child.GetChildrenCount();
			if(serversCount > 0)
			{
				XMLIO::CINode server;
				for( size_t i=0; i<serversCount; i++)
				{
					child.GetChildAt(i, server);
					if(server.GetName() == L"server")
					{
						PFSX::VersionUpdateInfo vui;
						vui.Type = server.GetAttributeInteger(L"type");
						vui.Url = server.GetAttribute(L"url");
						vui.Network = server.GetAttribute(L"network");
						PFSX::CVersionInfo::VersionUpdateInfoMap& map = m_versioninfo.GetUpdateInfoMap();
						PFSX::CVersionInfo::VersionUpdateInfoMap::iterator itor = map.find( vui.Type );
						if ( itor != map.end() )
						{
							itor->second = vui;
						}
						else
						{
							map.insert(std::make_pair(vui.Type, vui));
						}
					}
				}
			}
		}
	}
	return true;
}

void CSaveManager::SetParam(const std::wstring& input, 
							const std::wstring& output, 
							bool isResource,
							bool save,
							bool sort,
							const std::wstring& version, 
							const std::wstring& date,
							const std::wstring& luv)
{
	m_bResource = isResource;
	m_bSaveProject = save;
	m_bSortOperators = sort;
	m_pProjectFile = input;
	m_pOutputDir = output;
	m_versioninfo = m_project.GetVersionMeta();

	if(version == L"+1")
		m_bVersionAdd1 = true;
	else if(!version.empty())
		PFSX::String2Version(version, m_versioninfo.m_Version);

	if(luv == L"+1")
		m_bLUVadd1 = true;
	else if(!luv.empty())
		PFSX::String2Version(luv, m_versioninfo.m_luv);
	
	if(date.empty() || date == L"NOW")
		m_versioninfo.m_ReleaseDate = time(0);
	else
		m_versioninfo.m_ReleaseDate = ParseDate(date);
}

PFSX::CVersionInfo& CSaveManager::MergeVersionMeta(const PFSX::CVersionInfo& src,PFSX::CVersionInfo& dest)
{
	const PFSX::CVersionInfo::VersionUpdateInfoMap& mapsrc = src.GetUpdateInfoMap();
	PFSX::CVersionInfo::VersionUpdateInfoMap& mapdest = dest.GetUpdateInfoMap();
	PFSX::CVersionInfo::VersionUpdateInfoMap::const_iterator itorsrc;
	PFSX::CVersionInfo::VersionUpdateInfoMap::iterator itordest;
	for(itorsrc = mapsrc.begin();itorsrc!= mapsrc.end();itorsrc++)
	{
		itordest = mapdest.find(itorsrc->first);
		if ( itordest == mapdest.end() )
		{
			mapdest.insert(*itorsrc);
		}
	}
	if(m_bVersionAdd1)
	{
		dest.m_Version = src.m_Version + 1;
	}
	if(m_bLUVadd1)
	{
		dest.m_luv = src.m_luv + 1;
	}
	return dest;
}


bool CSaveManager::SaveProject()
{
	if(!m_bSaveProject)
		return true;
	if( m_bSortOperators )
	{
		m_project.SortOperators();
	}
	
	if( m_project.Save( m_pProjectFile ))
	{
		MSGLOGW(L"工程文件已保存！\n---------------------------------------\n");
	}
	else
	{
		MSGLOGW(L"工程文件保存失败！\n---------------------------------------\n");
		return false;
	}

	return true;
}