#include "stdafx.h"
#include "fileiomanager.h"
#include "pfsutil.h"
#include "xml.h"
#include "log.h"
#include "pobject.h"
#include "xptimeuselog.h"

namespace Nuclear
{
	extern XPTimeUseLog g_timeuserlog;
	CFileIOManager::CFileIOManager()
		: m_iIOTaskCount(0), m_iIOTaskBytes(0), m_iTime(0), m_iSize(0), m_nTimePF(30)
	{ 
	}

	bool CFileIOManager::OnMount(bool bWritableDefault, bool bApplictionInBuild)
	{
		wchar_t workdir[MAX_PATH];
		//-int nchar = GetModuleFileName(NULL, workdir, MAX_PATH);
		
		//2015 Yao 处理工作路径问题
		::GetCurrentDirectory(MAX_PATH, workdir);
		std::wstring wdir = workdir;

		//2015 Yao 处理工作路径问题
		int npos = (int)wdir.rfind('\\');
		wdir = std::wstring(wdir, 0, npos);
		std::wstring sysFilePath = _T("\\res");
		std::wstring strTemp = wdir + sysFilePath;

		if (!PFS::CEnv::Mount( L"/root", strTemp/*workdir*/, PFS::FST_NATIVE, bWritableDefault ? PFS::MT_WRITABLE : PFS::MT_RUNTIME))
		{
			XPLOG_ERROR(L"Mount root 失败\n");
			return false;
		}
		m_MountRoots.push_back(L"/root");

		XMLIO::CFileReader fr;

		std::wstring xmlpath = strTemp + L"\\cfg\\mount.xml";
		int nError = fr.OpenNativeFile(xmlpath);
		
		if( XMLIO::EC_SUCCESS != nError)
		{
			fr.CloseFile();
			XPLOG_ERROR(L"打开mount.xml错误\n");
			return false;
		}

		XMLIO::CINode	root;
		if (!fr.GetRootNode(root))
		{
			fr.CloseFile();
			XPLOG_ERROR(L"解释mount.xml错误\n");
			return false;
		}

		XMLIO::CNodeList mountnl;
		root.GetChildren(mountnl);
		bool r = true;

		for( XMLIO::CNodeList::iterator mountIt = mountnl.begin(); mountIt != mountnl.end(); ++mountIt)
		{
			XMLIO::CNodeList itemNl;
			XMLIO::CINode& mountNode = *mountIt;
			if (mountNode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			std::wstring rootName;
			if (!mountNode.GetAttribute(L"root", rootName))
			{
				XPLOG_ERROR(L"解释mount.xml错误: 没有找到root字段\n");
				r = false;
				continue;
			}

			std::wstring dir;
			std::wstring pfs;
			bool usepfs = false;
			bool dbg = (mountNode.GetAttributeInteger(L"dbg") == 1);
			PFS::MOUNT_TYPE mountType = bWritableDefault ? PFS::MT_WRITABLE : mountNode.GetAttributeInteger(L"mt");
			if (mountNode.GetAttribute(L"pfs", pfs))
			{
				if (PFS::CFile::IsFileExisting(pfs))
				{
					usepfs = PFS::CEnv::Mount( rootName, pfs, bWritableDefault ? PFS::FST_ZIP : PFS::FST_RUNZIP, mountType);
				}
			}
			if (!usepfs)
			{
				if (!mountNode.GetAttribute(L"dir", dir))
				{
					XPLOG_ERROR(L"解释mount.xml错误: 没有找到dir字段\n");
					r = false;
					continue;
				}
				if (!PFS::CEnv::Mount( rootName, dir, PFS::FST_NATIVE, mountType))
				{
					if (!dbg)
					{
						XPLOG_ERROR(L"Mount %s 失败\n", rootName.c_str());
						r = false;
					}
					continue;
				}
			}
			m_MountRoots.push_back(rootName);
			
			
		}
		fr.CloseFile();
		return r;
	}

	bool CFileIOManager::OnUnmount()
	{
		std::vector<std::wstring>::reverse_iterator rootIter = m_MountRoots.rbegin();
		for (;rootIter!=m_MountRoots.rend();++rootIter)
		{
			PFS::CEnv::Unmount(*rootIter);
		}
		return true;
	}

	bool CFileIOManager::Init(bool bWritableDefault, bool bApplictionInBuild)
	{
		m_executor.Init();
		return OnMount(bWritableDefault, bApplictionInBuild);
	}

	bool CFileIOManager::Destroy()
	{
		m_executor.Destroy();
		for(std::set<AsyncReadTask*>::const_iterator it = m_setReadingTask.begin(),ie=m_setReadingTask.end();
			it != ie; ++it)
			delete *it;
		return OnUnmount();
	}

	bool CFileIOManager::GetFileImage(const std::wstring &filename, XBuffer &data)
	{
		// 同步读取
		PFS::CFile file;			
		if( !file.Open(filename, PFS::FM_EXCL, PFS::FA_RDONLY ))
			return false;
		int file_size = (int)file.GetSize();
		if( file_size == 0 ) return false;
		data.SetOctets(XFileOctets(XHardRef<PFS::CImageData>(new PFS::CImageData(file.GetImage()))));
		return true;
	}

	bool CFileIOManager::LoadPObject(const std::wstring &filename, PObject &po)
	{
		XBuffer buf;
		if (GetFileImage(filename, buf) && po.LoadFromMem(buf))
			return true;
		XPLOG_ERROR(L"Load Object %s Faild\n", filename.c_str());
		return false;
	}


	bool CFileIOManager::AsyncReadFile(CFileIOManager::AsyncReadTask *pTask)
	{
		if( pTask == NULL )
			return false;

		bool r = false;

		// 只支持异步模式异步读取
		r = m_executor.AddTask(pTask);
		if( r )
			m_setReadingTask.insert(pTask);
		return r;
	}

	void CFileIOManager::OnUpdate(__int64 v)
	{
		__int64 tc = rdtsc();
		__int64 endtc = tc + v * m_nTimePF;

		m_executor.UpdateExecuteResult();
		m_iIOTaskCount = 0;
		m_iIOTaskBytes = 0;
		{
			g_timeuserlog.Push( XPTimeUseLog::TUL_EXECUTORUPDATE );
			// todo test result queue
			const int iMaxSize = 128 * 1024;
			std::list<AsyncReadTask*>::iterator it = m_lstResultQueue.begin();
			while( it != m_lstResultQueue.end() && rdtsc() < endtc)
			{
				int iResultSize = (*it)->GetData().size();
				++m_iIOTaskCount;
				m_iIOTaskBytes += iResultSize;
				g_timeuserlog.AddIOSize(iResultSize);
				if( (*it)->IsDiscarded() )
					(*it)->OnDiscard();
				else
					(*it)->OnReady();						
				m_setReadingTask.erase(*it);
				it = m_lstResultQueue.erase(it);
			}
		}
	}
};