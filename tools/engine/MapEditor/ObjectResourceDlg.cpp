

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "ObjectResourceDlg.h"
#include <set>



IMPLEMENT_DYNAMIC(CObjectResourceDlg, CDialog)

void CObjectResourceDlg::OnLvnEndlabeleditEleResList(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVDISPINFO *pDispInfo = reinterpret_cast<NMLVDISPINFO*>(pNMHDR);

	if (pDispInfo->item.pszText)
	{
		CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
		PMap &pmap = pDoc->GetToolsMap();
		sNameKey &tmpKey = m_NameKeys[m_EleResList.GetItemData(pDispInfo->item.iItem)];
		Nuclear::SubMap::ObjNameMap &mapElementNames = pmap.m_objs[tmpKey.m_Type].m_mapObjNames;
		std::wstring name(m_EleResList.GetItemText(pDispInfo->item.iItem, 0));
		Nuclear::SubMap::ObjMapKey key, newKey;

		if (pDoc->CheckObjectMapKey(tmpKey.m_Type, name, key))
		{
			XPASSERT(key == tmpKey.m_MapKey);
			pDoc->DeleteObjectKey(tmpKey.m_Type, name);
			CAction *pNowOper = new CAction(CAction::AT_SINGLE_OBJECT_NAMES, m_pMapEditorView->GetLayerEditId());
			CAction::CSingleObjectNameActionAtom* oper = pNowOper->GetSingleObjectNameActionData();
			oper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_ObjMapKey = key;
			oper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_Name = name.c_str();
			oper->m_MapObjectType = tmpKey.m_Type;
			if (pDoc->CheckObjectMapKey(tmpKey.m_Type, pDispInfo->item.pszText, newKey))
			{
				mapElementNames.erase(key);
				m_EleResList.DeleteItem(pDispInfo->item.iItem);
				int layerID = 0;
				Nuclear::SubMap::ObjLayerVector &layerVector = pmap.m_objs[tmpKey.m_Type].m_vectorLayers;
				Nuclear::SubMap::ObjLayerVector::iterator layerIter = layerVector.begin();
				Nuclear::SubMap::ObjInfoVector::iterator eleIter;
				std::pair<int, unsigned int> tmpInfo;
				for (;layerIter!=layerVector.end();layerIter++,layerID++)
				{
					eleIter = layerIter->vectorObjs.begin();
					for (;eleIter!=layerIter->vectorObjs.end();eleIter++)
					{
						if (eleIter->m_MapKey == key)
						{
							tmpInfo.first = layerID;
							tmpInfo.second = eleIter->m_ObjectId;
							oper->m_Pictures.push_back(tmpInfo);
							eleIter->m_MapKey = newKey;
						}
					}
				}
				oper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_ObjMapKey = newKey;
				oper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_Name = _T("deleted");
			} else {
				mapElementNames[key] = pDispInfo->item.pszText;
				pDoc->SetObjectKey(tmpKey.m_Type, pDispInfo->item.pszText, key);
				m_EleResList.SetItemText(pDispInfo->item.iItem, 0, pDispInfo->item.pszText);
				oper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_ObjMapKey = key;
				oper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_Name = pDispInfo->item.pszText;
			}
			m_pMapEditorView->DoAction(pNowOper);
			m_pMapEditorView->ResetElementList();
			m_pMapEditorView->GetDocument()->SetModifiedFlag();
			m_pMapEditorView->ClearScreenElementLayers();
		}
	}
	*pResult = 0;
}

void CObjectResourceDlg::ShowCounts()
{
	int itemCount = m_EleResList.GetItemCount();
	int i;
	wchar_t strCount[10];
	LV_ITEM lvItem;
	for (i=0;i<itemCount;i++)
	{
		ZeroMemory(&lvItem, sizeof(LV_ITEM));
		ZeroMemory(strCount, 10 * sizeof(wchar_t));
		_itow_s(m_Counts[m_NameKeys[m_EleResList.GetItemData(i)]], strCount, 10, 10);
		lvItem.mask = LVIF_TEXT;
		lvItem.pszText = strCount;
		lvItem.iItem = i;
		lvItem.iSubItem = 2;
		m_EleResList.SetItem(&lvItem);
	}
}

void CObjectResourceDlg::ResetCounts()
{
	m_Counts.clear();
	CMapEditorDoc* pDoc = m_pMapEditorView->GetDocument();
	Nuclear::PMap &pmap = pDoc->GetToolsMap();
	int t=0;
	Nuclear::PMap::MAP_OBJ_TYPE type;
	Nuclear::SubMap::ObjLayerVector::iterator layerIter;
	Nuclear::SubMap::ObjInfoVector::iterator objInfoIter, objInfoEnd;
	CountMap::iterator countIter;
	sNameKey tmpKey;
	for (t=0;t<Nuclear::PMap::MAP_OBJ_COUNT;++t)
	{
		type = static_cast<Nuclear::PMap::MAP_OBJ_TYPE>(t);
		Nuclear::SubMap::ObjLayerVector &layerVector = pmap.m_objs[type].m_vectorLayers;
		layerIter = layerVector.begin();
		tmpKey.m_Type = type;
		for (;layerIter!=layerVector.end();++layerIter)
		{
			objInfoEnd = layerIter->vectorObjs.end();
			for (objInfoIter=layerIter->vectorObjs.begin();objInfoIter!=objInfoEnd;++objInfoIter)
			{
				if (objInfoIter->m_bDeleted)
					continue;
				tmpKey.m_MapKey = objInfoIter->m_MapKey;
				countIter = m_Counts.find(tmpKey);
				if (countIter == m_Counts.end())
				{
					m_Counts[tmpKey] = 1;
				} else {
					++(countIter->second);
				}
			}

		}
	}
	const Nuclear::SubMap::TriggerInfoMap &triggers = pmap.m_triggerObjs.GetTriggerInfos();
	Nuclear::SubMap::TriggerInfoMap::const_iterator triIt, triIe;
	Nuclear::SubMap::TriggerInfoVet::const_iterator vetIt, vetIe;
	triIt = triggers.begin();
	triIe = triggers.end();
	tmpKey.m_Type = Nuclear::PMap::MAP_OBJ_EFFECT;
	for (;triIt!=triIe;++triIt)
	{
		vetIt=triIt->second.triggerInfos.begin();
		vetIe=triIt->second.triggerInfos.end();
		for (;vetIt!=vetIe;++vetIt)
		{
			if (!(vetIt->triggerTypeFlag & Nuclear::SubMap::TRIGGER_DELETE))
			{
				tmpKey.m_MapKey = vetIt->effectMapKey;
				countIter = m_Counts.find(tmpKey);
				if (countIter == m_Counts.end())
				{
					m_Counts[tmpKey] = 1;
				} else {
					++(countIter->second);
				}
			}
		}
	}
}

void CObjectResourceDlg::OnBnClickedButtonClear()
{
	ResetCounts();
	CMapEditorDoc* pDoc = m_pMapEditorView->GetDocument();
	Nuclear::PMap &pmap = pDoc->GetToolsMap();
	Nuclear::SubMap::ObjNameMap::iterator iter, iterEnd;
	int t=0;
	Nuclear::PMap::MAP_OBJ_TYPE type;
	sNameKey tmpKey;
	CAction *pNowOper = new CAction(CAction::AT_OBJECT_NAMES, m_pMapEditorView->GetLayerEditId());
	CAction::CObjectNameActionAtom* oper = pNowOper->GetObjectNameActionData();
	CountMap::iterator mapEnd = m_Counts.end();
	for (t=0;t<Nuclear::PMap::MAP_OBJ_COUNT;++t)
	{
		type = static_cast<Nuclear::PMap::MAP_OBJ_TYPE>(t);
		Nuclear::SubMap::ObjNameMap &mapObjNames = pmap.m_objs[type].m_mapObjNames;
		oper->oper[type][CAction::CActionAtom::BEFORE_OPERATOR] = mapObjNames;
		iter = mapObjNames.begin();
		iterEnd = mapObjNames.end();
		tmpKey.m_Type = type;
		for (;iter!=iterEnd;)
		{
			tmpKey.m_MapKey = iter->first;
			if (m_Counts.find(tmpKey) == mapEnd)
			{
				pDoc->DeleteObjectKey(type, iter->second);
				iter = mapObjNames.erase(iter);
			} else {
				++iter;
			}
		}
		oper->oper[type][CAction::CActionAtom::AFTER_OPERATOR] = mapObjNames;
	}
	m_pMapEditorView->DoAction(pNowOper);
	ResetLists();
	ShowCounts();
	m_pMapEditorView->GetDocument()->SetModifiedFlag();
}

void CObjectResourceDlg::OnBnClickedButtonCalRef()
{
	ResetCounts();
	ShowCounts();
}

void CObjectResourceDlg::OnOK()
{

}

void CObjectResourceDlg::OnBnClickedRefresh()
{
	ResetLists();
}

BOOL CObjectResourceDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	SetWindowText(_T("物件资源表"));
	m_EleResList.InsertColumn(0, _T("资源路径"), LVCFMT_LEFT, 545);
	m_EleResList.InsertColumn(1, _T("物件类型"), LVCFMT_LEFT, 100);
	m_EleResList.InsertColumn(2, _T("引用次数"), LVCFMT_LEFT, 100);
	GetDlgItem(IDC_BUTTON2)->ShowWindow(SW_HIDE);
	GetDlgItem(IDC_BUTTON1)->ShowWindow(SW_HIDE);

	return TRUE;  

}

void CObjectResourceDlg::ResetLists()
{
	static wchar_t* OBJ_TYPE_NAMES[Nuclear::PMap::MAP_OBJ_COUNT] = {L"物件", L"特效", L"动态物件", L"飘动物件"};
	m_EleResList.DeleteAllItems();

	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	Nuclear::PMap &pmap = pDoc->GetToolsMap();
	Nuclear::SubMap::ObjNameMap::iterator iter, iterEnd;
	int i=0, t=0;
	Nuclear::PMap::MAP_OBJ_TYPE type;
	sNameKey tmpKey;
	LV_ITEM lvItem;
	ZeroMemory(&lvItem, sizeof(LV_ITEM));
	lvItem.mask = LVIF_TEXT;
	m_NameKeys.clear();
	for (t=0;t<Nuclear::PMap::MAP_OBJ_COUNT;++t)
	{
		type = static_cast<Nuclear::PMap::MAP_OBJ_TYPE>(t);
		Nuclear::SubMap::ObjNameMap &mapObjNames = pmap.m_objs[type].m_mapObjNames;
		iter = mapObjNames.begin();
		iterEnd = mapObjNames.end();
		tmpKey.m_Type = type;
		for (;iter!=iterEnd;++iter,++i)
		{
			tmpKey.m_MapKey = iter->first;
			m_NameKeys.push_back(tmpKey);
			m_EleResList.InsertItem(i, iter->second.data());
			m_EleResList.SetItemData(i, i);	
			lvItem.pszText = OBJ_TYPE_NAMES[t];
			lvItem.iItem = i;
			lvItem.iSubItem = 1;
			m_EleResList.SetItem(&lvItem);
			lvItem.pszText = _T("未知");
			lvItem.iSubItem = 2;
			m_EleResList.SetItem(&lvItem);
		}
	}

}

BOOL CObjectResourceDlg::Create(CMapEditorView* pMapEditorView/* = NULL*/)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

BEGIN_MESSAGE_MAP(CObjectResourceDlg, CDialog)
	ON_BN_CLICKED(IDC_REFRESH, &CObjectResourceDlg::OnBnClickedRefresh)
	ON_BN_CLICKED(IDC_BUTTON_COUNT_REF, &CObjectResourceDlg::OnBnClickedButtonCalRef)
	ON_BN_CLICKED(IDC_BUTTON_CLEAN, &CObjectResourceDlg::OnBnClickedButtonClear)
	ON_NOTIFY(LVN_ENDLABELEDIT, IDC_ELE_RES_LIST, &CObjectResourceDlg::OnLvnEndlabeleditEleResList)
END_MESSAGE_MAP()

void CObjectResourceDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_ELE_RES_LIST, m_EleResList);
}

CObjectResourceDlg::~CObjectResourceDlg()
{
}

CObjectResourceDlg::CObjectResourceDlg(CWnd* pParent /*=NULL*/)
: CDialog(CObjectResourceDlg::IDD, pParent)
{

}

