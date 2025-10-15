// AllResDlg.cpp : implementation file
//

#include "stdafx.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\soundarea\xpenvsoundcirclearea.h"
#include "..\engine\soundarea\xpenvsoundpolygonarea.h"
#include "..\engine\soundarea\xpbkgsoundarea.h" 
#include "SoundEditor.h"
#include "AllResDlg.h"
#include "SoundEditorDoc.h"

// CAllResDlg dialog

IMPLEMENT_DYNAMIC(CAllResDlg, CDialog)

CAllResDlg::CAllResDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CAllResDlg::IDD, pParent), m_bIsInited(false), m_bHasCountRef(false), 
	m_nMinWidth(0), m_nMinHeight(0), pDoc(NULL)
{
	m_sortInfo.id = -1;
	m_sortInfo.isup = 0;
}

CAllResDlg::~CAllResDlg()
{
	ReleaseMaps();
}

void CAllResDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST_RES, m_resListCtrl);
}


BEGIN_MESSAGE_MAP(CAllResDlg, CDialog)
	ON_BN_CLICKED(IDC_EXPORT_TO_EXCEL, &CAllResDlg::OnBnClickedExportToExcel)
	ON_BN_CLICKED(IDC_DELETE, &CAllResDlg::OnBnClickedDelete)
	ON_BN_CLICKED(IDC_REFRESH, &CAllResDlg::OnBnClickedRefresh)
	ON_BN_CLICKED(IDC_COUNT_REF, &CAllResDlg::OnBnClickedCountRef)
	ON_WM_SIZING()
	ON_WM_SIZE()
	ON_NOTIFY(HDN_ITEMCLICK, 0, &CAllResDlg::OnHdnItemclickListRes)
END_MESSAGE_MAP()

void CAllResDlg::RenewSize()
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		rect.DeflateRect(10, 10);
		rect.bottom -= 35;
		m_resListCtrl.MoveWindow(&rect);
		rect.top = rect.bottom + 10;
		rect.bottom = rect.top + 25;
		rect.left = rect.right - 70;
		GetDlgItem(IDC_COUNT_REF)->MoveWindow(&rect);
		rect.MoveToX(rect.left - 80);
		GetDlgItem(IDC_REFRESH)->MoveWindow(&rect);
		rect.MoveToX(rect.left - 80);
		GetDlgItem(IDC_DELETE)->MoveWindow(&rect);
		rect.MoveToX(rect.left - 80);
		GetDlgItem(IDC_EXPORT_TO_EXCEL)->MoveWindow(&rect);
		Invalidate();
	}
}


void CAllResDlg::ResetLists()
{
	ReleaseMaps();
	m_bHasCountRef = false;
	m_resListCtrl.DeleteAllItems();
	m_soundInfos.clear();
	m_soundInfoMap.clear();

	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CStringList FileList;
	BOOL working;
	SoundInfo tmpinfo;
	unsigned int i = 0;
	LV_ITEM lvItem, lvItemSize;
	ZeroMemory(&lvItem, sizeof(LV_ITEM));
	ZeroMemory(&lvItemSize, sizeof(LV_ITEM));
	lvItemSize.iSubItem = 2;
	lvItem.mask = LVIF_TEXT;
	lvItemSize.mask = LVIF_TEXT;
	lvItem.pszText = L"未知";
	FileName=theApp.GetExePath() + _T(".\\res\\sound");
	FileList.AddTail(FileName);

	while (!FileList.IsEmpty())  
	{  
		FileName=FileList.GetHead()+_T("\\*.*");
		FileList.RemoveHead();
		working = myFinder.FindFile(FileName);
		while (working)
		{
			working = myFinder.FindNextFile();
			if (myFinder.IsDots())   
				continue;
			if (myFinder.IsHidden())
				continue;
			tmpName = myFinder.GetFilePath();
			if (myFinder.IsDirectory()) 
			{
				FileList.AddTail(tmpName);
			}
			else
			{
				if ((tmpName.Right(4).MakeLower() != L".mp3") && (tmpName.Right(4).MakeLower() != L".ogg") && (tmpName.Right(4).MakeLower() != L".wav"))
					continue;
				const std::wstring oldfilename = tmpName;
				std::wstring newname;
				PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
				m_soundInfoMap[newname] = i;
				m_soundInfos.push_back(tmpinfo);
				SoundInfo &info = m_soundInfos[i];
				info.ref = 0;
				info.uri = newname;
				m_resListCtrl.InsertItem((int)i, newname.c_str());
				m_resListCtrl.SetItemData((int)i, i);	//itemData是m_soundInfos的下标
				lvItem.iItem = (int)i;
				lvItem.iSubItem = 1;
				m_resListCtrl.SetItem(&lvItem);
				PFS::CFile file;
				if (file.Open(newname, PFS::FM_EXCL, PFS::FA_RDONLY ))
				{
					info.size = file.GetSize() / 1024.0f;
				}
				CString sizeStr;
				sizeStr.Format(L"%0.2f", info.size);
				lvItemSize.iItem = lvItem.iItem;
				lvItemSize.pszText = sizeStr.GetBuffer();
				m_resListCtrl.SetItem(&lvItemSize);
				sizeStr.ReleaseBuffer();
				lvItem.iSubItem = 3;
				m_resListCtrl.SetItem(&lvItem);
				++i;
			}
		}
	}
}

void CAllResDlg::ReleaseMaps()
{
	PMaps::iterator mapIt = m_maps.begin(), mapIe = m_maps.end();
	EnvAreaVet::iterator envIt, envIe;
	BkgAreaVet::iterator bkgIt, bkgIe;
	for (;mapIt!=mapIe;++mapIt)
	{
		for(envIt=mapIt->envSoundAreas.begin(),envIe=mapIt->envSoundAreas.end();envIt!=envIe;++envIt)
		{
			delete *envIt;
		}
		for(bkgIt=mapIt->bkgSoundAreas.begin(),bkgIe=mapIt->bkgSoundAreas.end();bkgIt!=bkgIe;++bkgIt)
		{
			delete *bkgIt;
		}
		if (mapIt->pDefBkgSoundArea)
			delete mapIt->pDefBkgSoundArea;
	}
	m_maps.clear();
}

void CAllResDlg::ResetMaps()
{
	ReleaseMaps();
	CFileFind myFinder;
	CString FileName;
	CString FPath;
	BOOL working;
	MapInfo tmpMapInfo;
	tmpMapInfo.isModify = false;
	tmpMapInfo.pDefBkgSoundArea = NULL;
	FPath=theApp.GetExePath() + _T("\\res\\map\\");
	working = myFinder.FindFile(FPath + L"*.*");
	size_t mapID = 0;
	SoundNameInfoMap::iterator soundIter, soundIe = m_soundInfoMap.end();
	while (working)
	{
		working = myFinder.FindNextFile();
		if (myFinder.IsDots())   
			continue;
		if (myFinder.IsHidden())
			continue;
		FileName = myFinder.GetFileName().MakeLower();
		if (myFinder.IsDirectory()) 
		{
			if (FileName == L"maze")
				continue;
			if (FileName == L"tiles")
				continue;
			if (FileName == L"elements")
				continue;
			if (FileName == L"linkedobjects")
				continue;
			if (FileName == L"distortionobjects")
				continue;
			XMLIO::CFileReader fr;
			std::wstring fullname = FPath + FileName + L"\\sound.mps";
			int retcode = fr.OpenNativeFile(fullname);
			if (retcode != XMLIO::EC_SUCCESS)
				continue;
			XMLIO::CINode root;
			if (!fr.GetRootNode(root))
			{
				fr.CloseFile();
				continue;
			}
			m_maps.push_back(tmpMapInfo);
			MapInfo& mapInfo = *m_maps.rbegin();
			mapInfo.mapname = FileName;
			XMLIO::CNodeList nl;
			root.GetChildren(nl);
			XMLIO::CNodeList::iterator it = nl.begin(), ie = nl.end();
			for (;it!=ie;++it)
			{
				if (it->GetType() != XMLIO::NT_ELEMENT)
					continue;
				if (it->GetName() == L"env")
				{//环境音
					XMLIO::CNodeList envnl;
					Nuclear::XPEnvSoundArea *pEsa;
					it->GetChildren(envnl);
					XMLIO::CNodeList::iterator eIt = envnl.begin(), eIe = envnl.end();
					for (;eIt!=eIe;++eIt)
					{
						if (eIt->GetType() != XMLIO::NT_ELEMENT)
							continue;
						if (eIt->GetName() == L"circle")
							pEsa = new CToolsEnvSoundCircleArea();
						else if (eIt->GetName() == L"polygon")
							pEsa = new CToolsEnvSoundPolygonArea();
						else
							XPASSERT(false);
						pEsa->Load(*eIt, Nuclear::XPSOUND_IO_EDIT);
						mapInfo.envSoundAreas.push_back(pEsa);
						Nuclear::XPEnvSoundArea::SoundVet::iterator it, ie;
						Nuclear::XPEnvSoundArea::SoundParamVet::iterator tit, tie;
						for (it = pEsa->m_sound.begin(), ie = pEsa->m_sound.end();it!=ie;++it)
						{
							for (tit=it->sounds.begin(), tie = it->sounds.end(); tit!=tie;++tit)
							{
								soundIter = m_soundInfoMap.find(tit->soundName);
								if (soundIter == soundIe)
								{
									if (m_notfoundset.find(tit->soundName) == m_notfoundset.end())
									{
										CString errMsg;
										errMsg.Format(L"找不到声音文件: %s", tit->soundName.c_str());
										AfxMessageBox(errMsg);
										m_notfoundset.insert(tit->soundName);
									}
									continue;
								}
								SoundInfo &sinfo = m_soundInfos[soundIter->second];
								sinfo.maps.insert(mapID);
								++sinfo.ref;
								mapInfo.soundInMap[soundIter->second].envSoundAreas.insert(pEsa);
							}
						}
					}
				} else if (it->GetName() == L"bgs")
				{//背景音乐
					XMLIO::CNodeList bkgnl;
					Nuclear::XPBkgSoundArea *pBsa;
					it->GetChildren(bkgnl);
					bool bIsBkgSoundArea;
					XMLIO::CNodeList::iterator bIt = bkgnl.begin(), bIe = bkgnl.end();
					for (;bIt!=bIe;++bIt)
					{
						bIsBkgSoundArea = false;
						if (bIt->GetType() != XMLIO::NT_ELEMENT)
							continue;
						if (bIt->GetName() == L"circle")
							pBsa = new CToolsBkgSoundCircleArea();
						else if (bIt->GetName() == L"polygon")
							pBsa = new CToolsBkgSoundPolygonArea();
						else if (bIt->GetName() == L"default")
						{
							mapInfo.pDefBkgSoundArea = new CToolsBkgDefaultSoundArea();
							bIsBkgSoundArea = true;
							pBsa = mapInfo.pDefBkgSoundArea;
						}
						else
							XPASSERT(false);
						pBsa->Load(*bIt, Nuclear::XPSOUND_IO_EDIT);
						if (!bIsBkgSoundArea)
							mapInfo.bkgSoundAreas.push_back(pBsa);
						Nuclear::XPBkgSoundArea::SoundParamVet::iterator it, ie;
						for (it = pBsa->m_sounds.begin(), ie = pBsa->m_sounds.end();it!=ie;++it)
						{
							soundIter = m_soundInfoMap.find(it->soundName);
							if (soundIter == soundIe)
							{
								if (m_notfoundset.find(it->soundName) == m_notfoundset.end())
								{
									CString errMsg;
									errMsg.Format(L"找不到声音文件: %s", it->soundName.c_str());
									AfxMessageBox(errMsg);
									m_notfoundset.insert(it->soundName);
								}
								continue;
							}
							SoundInfo &sinfo = m_soundInfos[soundIter->second];
							sinfo.maps.insert(mapID);
							++sinfo.ref;
							if (bIsBkgSoundArea)
								mapInfo.soundInMap[soundIter->second].isInDefault = true;
							else
								mapInfo.soundInMap[soundIter->second].bkgSoundAreas.insert(pBsa);
						}
					}
				} else {
					XPASSERT(false);
				}
			}
			++mapID;
		}
	}
}
// CAllResDlg message handlers

void CAllResDlg::OnBnClickedExportToExcel()
{
	CFileDialog dlg(false, L".csv", L"output.csv", OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, L"CSV (逗号分割) (*.csv)|*.csv||");
	if (dlg.DoModal() == IDOK)
	{
		setlocale(LC_CTYPE, "chinese_china");;
		FILE *f = Nuclear::FOpen(dlg.GetPathName(), L"w");
		if (f)
		{
			//m_resListCtrl.InsertColumn(0, _T("资源路径"), LVCFMT_LEFT, 250);
			//m_resListCtrl.InsertColumn(1, _T("引用数"), LVCFMT_LEFT, 70);
			//m_resListCtrl.InsertColumn(2, _T("大小(k)"), LVCFMT_LEFT, 70);
			//m_resListCtrl.InsertColumn(3, _T("引用地图"), LVCFMT_LEFT, 80);
			fwprintf(f, L"%s", L"资源路径,引用数,大小(KB),引用地图\n");
			SoundInfoVector::iterator it = m_soundInfos.begin(), ie = m_soundInfos.end();
			for (;it!=ie;++it)
			{
				SoundInfo &info = *it;
				if (info.deleted)
					continue;
				fwprintf(f, L"\"%s\",%d,%0.2f,\"%s\"\n", info.uri.c_str(), info.ref, info.size, info.mapnames.c_str());
			}
			fclose(f);
		} else {
			AfxMessageBox(L"打开文件失败");
		}

	}
	
}

void CAllResDlg::OnBnClickedDelete()
{
	if (!m_bHasCountRef)
	{
		AfxMessageBox(L"请先计算引用！\n");
		return;
	}
	POSITION pos = m_resListCtrl.GetFirstSelectedItemPosition();
	if (pos == NULL)
	{
		AfxMessageBox(L"请先选中1项物件");
		return;
	}
	Nuclear::XPEnvSoundArea* pEnv;
	Nuclear::XPBkgSoundArea* pBkg;
	CString FPath=theApp.GetExePath() + _T("\\res\\map\\");
	while (pos)
	{
		int nItem = m_resListCtrl.GetNextSelectedItem(pos);
		unsigned int i = (unsigned int)m_resListCtrl.GetItemData(nItem);
		SoundInfo &info = m_soundInfos[i];
		bool ok = true;
		info.deleted = true;
		PMapIDSet::iterator mIt = info.maps.begin(), mIe = info.maps.end();
		for (;mIt!=mIe;++mIt)
		{
			bool hasDel = false;
			MapInfo &mapInfo = m_maps[*mIt];
			SoundInMapInfoMap::iterator sIt = mapInfo.soundInMap.find(i);
			if (sIt != mapInfo.soundInMap.end())
			{
				SoundInMapInfo& simi = sIt->second;
				//EnvSet::iterator eIt = simi.envSoundAreas.begin(), eIe = simi.envSoundAreas.end();
				EnvAreaVet::iterator eIt = mapInfo.envSoundAreas.begin();
				EnvSet::iterator esIe = simi.envSoundAreas.end();
				for (;eIt!=mapInfo.envSoundAreas.end();)
				{
					pEnv = *eIt;
					if (simi.envSoundAreas.find(pEnv) == esIe)
					{
						++eIt;
						continue;
					}
					Nuclear::XPEnvSoundArea::SoundVet::iterator it;
					Nuclear::XPEnvSoundArea::SoundParamVet::iterator tit;
					for (it = pEnv->m_sound.begin();it!=pEnv->m_sound.end();)
					{
						for (tit=it->sounds.begin(); tit!=it->sounds.end();)
						{
							if (tit->soundName == info.uri)
							{
								tit = it->sounds.erase(tit);
								hasDel = true;
							} else {
								++tit;
							}
						}
						if (it->sounds.size() == 0)
						{
							it = pEnv->m_sound.erase(it);
						} else {
							++it;
						}
					}
					if (pEnv->m_sound.size() == 0)
					{
						eIt = mapInfo.envSoundAreas.erase(eIt);
						delete pEnv;
					} else {
						++eIt;
					}
				}
				BkgAreaVet::iterator bIt = mapInfo.bkgSoundAreas.begin();
				BkgSet::iterator bsIe = simi.bkgSoundAreas.end();
				for (;bIt!=mapInfo.bkgSoundAreas.end();)
				{
					pBkg = *bIt;
					if (simi.bkgSoundAreas.find(pBkg) == bsIe)
					{
						++bIt;
						continue;
					}
					Nuclear::XPBkgSoundArea::SoundParamVet::iterator it = pBkg->m_sounds.begin();
					for (;it!=pBkg->m_sounds.end();)
					{
						if (it->soundName == info.uri)
						{
							it = pBkg->m_sounds.erase(it);
							hasDel = true;
						} else {
							++it;
						}
					}
					if (pBkg->m_sounds.size() == 0)
					{
						bIt = mapInfo.bkgSoundAreas.erase(bIt);
						delete pBkg;
					} else {
						++bIt;
					}
				}
				if (simi.isInDefault && mapInfo.pDefBkgSoundArea)
				{
					Nuclear::XPBkgSoundArea::SoundParamVet::iterator it = mapInfo.pDefBkgSoundArea->m_sounds.begin();
					for (;it!=mapInfo.pDefBkgSoundArea->m_sounds.end();)
					{
						if (it->soundName == info.uri)
						{
							it = mapInfo.pDefBkgSoundArea->m_sounds.erase(it);
							hasDel = true;
						} else {
							++it;
						}
					}
				}
				mapInfo.soundInMap.erase(sIt);
				if (hasDel)
				{
					XMLIO::CFileWriter fw;
					XMLIO::CONode root;
					fw.GetRootNode(root);
					XMLIO::CONode env;
					root.CreateSubElement(L"env", env);
					EnvAreaVet::iterator envIt = mapInfo.envSoundAreas.begin(), envIe = mapInfo.envSoundAreas.end();
					Nuclear::XPEnvSoundArea *pEsa = NULL;
					for (;envIt!=envIe;++envIt)
					{
						pEsa = *envIt;
						XMLIO::CONode envAreaNode;
						switch (pEsa->GetType())
						{
						case Nuclear::XPAREA_TYPE_CIRCLE:
							env.CreateSubElement(L"circle", envAreaNode);
							break;
						case Nuclear::XPAREA_TYPE_POLYGON:
							env.CreateSubElement(L"polygon", envAreaNode);
							break;
						default:
							XPASSERT(false);
						}
						pEsa->Save(envAreaNode, Nuclear::XPSOUND_IO_EDIT);
					}
					XMLIO::CONode bgs;
					root.CreateSubElement(L"bgs", bgs);
					BkgAreaVet::iterator bkgIt =  mapInfo.bkgSoundAreas.begin(), bkgIe = mapInfo.bkgSoundAreas.end();
					Nuclear::XPBkgSoundArea *pBkg = NULL;
					if (mapInfo.pDefBkgSoundArea)
					{
						XMLIO::CONode bkgDefAreaNode;
						bgs.CreateSubElement(L"default", bkgDefAreaNode);
						mapInfo.pDefBkgSoundArea->Save(bkgDefAreaNode, Nuclear::XPSOUND_IO_EDIT);
					}
					
					for (;bkgIt!=bkgIe;++bkgIt)
					{
						pBkg = *bkgIt;
						XMLIO::CONode bkgAreaNode;
						switch (pBkg->GetType())
						{
						case Nuclear::XPAREA_TYPE_CIRCLE:
							bgs.CreateSubElement(L"circle", bkgAreaNode);
							break;
						case Nuclear::XPAREA_TYPE_POLYGON:
							bgs.CreateSubElement(L"polygon", bkgAreaNode);
							break;
						default:
							XPASSERT(false);
						}
					pBkg->Save(bkgAreaNode, Nuclear::XPSOUND_IO_EDIT);
					}
					std::wstring nName = FPath + mapInfo.mapname.c_str() + L"\\sound.mps";
					fw.SaveNative(nName);
				}
			}
			
		}
		m_resListCtrl.DeleteItem(nItem);
		std::wstring navname;
		PFS::CEnv::PFSFileNameToNativeFileName( info.uri, navname);
		DeleteFile(navname.c_str());
	}
}

void CAllResDlg::OnBnClickedRefresh()
{
	ResetLists();
}

void CAllResDlg::OnBnClickedCountRef()
{
	ResetMaps();
	int i, count;
	unsigned int id;
	count = m_resListCtrl.GetItemCount();
	LV_ITEM lvItem;
	ZeroMemory(&lvItem, sizeof(LV_ITEM));
	lvItem.mask = LVIF_TEXT;
	PMapIDSet::iterator it, ie;
	for (i=0;i<count;++i)
	{
		id = static_cast<unsigned int>(m_resListCtrl.GetItemData(i));
		SoundInfo& info = m_soundInfos[id];
		CString cont;
		cont.Format(L"%d", info.ref);
		lvItem.iItem = i;
		lvItem.iSubItem = 1;
		lvItem.pszText = cont.GetBuffer();
		m_resListCtrl.SetItem(&lvItem);
		cont.ReleaseBuffer();
		info.mapnames = L"";
		it = info.maps.begin();
		ie = info.maps.end();
		for (;it!=ie;++it)
		{
			if (info.mapnames.size() > 0)
				info.mapnames += L",";
			info.mapnames += m_maps[*it].mapname;
		}
		lvItem.iSubItem = 3;
		cont = info.mapnames.c_str();
		lvItem.pszText = cont.GetBuffer();
		m_resListCtrl.SetItem(&lvItem);
		cont.ReleaseBuffer();
	}
	m_bHasCountRef = true;
}

BOOL CAllResDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_resListCtrl.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	m_resListCtrl.InsertColumn(0, _T("资源路径"), LVCFMT_LEFT, 250);
	m_resListCtrl.InsertColumn(1, _T("引用数"), LVCFMT_LEFT, 70);
	m_resListCtrl.InsertColumn(2, _T("大小(k)"), LVCFMT_LEFT, 70);
	m_resListCtrl.InsertColumn(3, _T("引用地图"), LVCFMT_LEFT, 80);
	ResetLists();
	SetCurrentDirectory(theApp.GetExePath());
	CRect rect;
	GetWindowRect(&rect);
	m_nMinWidth = rect.Width();
	m_nMinHeight = rect.Height();
	m_bIsInited = true;
	RenewSize();
	return TRUE;
}

void CAllResDlg::OnSizing(UINT fwSide, LPRECT pRect)
{
	if (pRect->right - pRect->left < m_nMinWidth)
	{
		pRect->right = pRect->left + m_nMinWidth;
	}

	if (pRect->bottom - pRect->top < m_nMinHeight)
	{
		pRect->bottom = pRect->top + m_nMinHeight;
	}
	CDialog::OnSizing(fwSide, pRect);
	RenewSize();
}

void CAllResDlg::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	RenewSize();
}

int CALLBACK SortFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort);
typedef struct _SortParam{
	CAllResDlg::SortInfo sortInfo;
	CAllResDlg::SoundInfoVector* pVector;
} SortParam;

void CAllResDlg::OnHdnItemclickListRes(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMHEADER phdr = reinterpret_cast<LPNMHEADER>(pNMHDR);
	SortParam param;
	if (phdr->iItem == m_sortInfo.id)
	{
		m_sortInfo.isup=m_sortInfo.isup==0?1:0;
	} else {
		m_sortInfo.id = phdr->iItem;
		m_sortInfo.isup = 0;
	}
	param.sortInfo = m_sortInfo;
	param.pVector = &m_soundInfos;
	m_resListCtrl.SortItems(SortFunc, reinterpret_cast<DWORD_PTR>(&param));
	*pResult = 0;
}

int CALLBACK SortFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort)
{
	/*m_resListCtrl.InsertColumn(0, _T("资源路径"), LVCFMT_LEFT, 250);
	m_resListCtrl.InsertColumn(1, _T("引用数"), LVCFMT_LEFT, 70);
	m_resListCtrl.InsertColumn(2, _T("大小(k)"), LVCFMT_LEFT, 70);
	m_resListCtrl.InsertColumn(3, _T("引用地图"), LVCFMT_LEFT, 80);*/
	SortParam *pParam = reinterpret_cast<SortParam*>(lParamSort);
	CAllResDlg::SoundInfo& info1 = (*pParam->pVector)[lParam1];
	CAllResDlg::SoundInfo& info2 = (*pParam->pVector)[lParam2];
	bool result = true;
	switch (pParam->sortInfo.id)
	{
	case 0:
		result = info1.uri > info2.uri;
		break;
	case 1:
		result = info1.ref > info2.ref;
		break;
	case 2:
		result = info1.size > info2.size;
		break;
	case 3:
		result = info1.mapnames > info2.mapnames;
		break;
	}
	if (pParam->sortInfo.isup)
		return !result;
	return result;
}