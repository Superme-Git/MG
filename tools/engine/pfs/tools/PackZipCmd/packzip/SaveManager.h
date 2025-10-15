#pragma once
#include "ZipTreeItem.h"
#include "Operator.h"


class CSaveManager
{
public:
	CSaveManager():m_bResource(false),m_bSaveProject(false),m_bSortOperators(false),
		m_bVersionAdd1(false),m_bLUVadd1(false)
	{Initialize();}
	~CSaveManager()
	{
		Clear(); 
		::CloseHandle(hEvent);
	}
	
	bool ParseXmlCfg(const std::wstring& filename);
	void Initialize();
	void VerifyPath();
	bool OpenProject();
	bool SaveFile();
	bool SaveProject();
	void Clear();
	PFSX::CVersionInfo& MergeVersionMeta(const PFSX::CVersionInfo& src,PFSX::CVersionInfo& dest);
	void SetParam(const std::wstring& input, 
					const std::wstring& output, 
					bool isResource,
					bool save,
					bool sort,
					const std::wstring& version, 
					const std::wstring& date,
					const std::wstring& luv);

private:
	CProject				m_project;
	CZipTreeItem			m_root;
	COperatorThread			m_thread;
	wstring					m_pProjectFile;
	wstring					m_pOutputDir;
	wstring					lastpfs;
	bool					m_bResource;
	bool					m_bSortOperators;
	bool					m_bSaveProject;
	bool					m_bVersionAdd1;
	bool					m_bLUVadd1;
	CSaveThreadListener		m_listener;
	PFSX::CVersionInfo		m_versioninfo;
};