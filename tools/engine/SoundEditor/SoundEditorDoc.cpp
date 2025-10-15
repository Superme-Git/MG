// SoundEditorDoc.cpp : CSoundEditorDoc 类的实现
//

#include "stdafx.h"
#include "SoundEditor.h"

#include "SoundEditorDoc.h"
#include "Operators.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\soundarea\xpenvsoundcirclearea.h"
#include "..\engine\soundarea\xpenvsoundpolygonarea.h"
#include "..\engine\map\pstepsoundmap.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CSoundEditorDoc

IMPLEMENT_DYNCREATE(CSoundEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CSoundEditorDoc, CDocument)
	ON_COMMAND(ID_EDIT_REDO, &CSoundEditorDoc::OnEditRedo)
	ON_UPDATE_COMMAND_UI(ID_EDIT_REDO, &CSoundEditorDoc::OnUpdateEditRedo)
	ON_COMMAND(ID_EDIT_UNDO, &CSoundEditorDoc::OnEditUndo)
	ON_UPDATE_COMMAND_UI(ID_EDIT_UNDO, &CSoundEditorDoc::OnUpdateEditUndo)
	ON_COMMAND(ID_EDIT_COPY, &CSoundEditorDoc::OnEditCopy)
	ON_UPDATE_COMMAND_UI(ID_EDIT_COPY, &CSoundEditorDoc::OnUpdateEditCopy)
	ON_COMMAND(ID_EDIT_CUT, &CSoundEditorDoc::OnEditCut)
	ON_UPDATE_COMMAND_UI(ID_EDIT_CUT, &CSoundEditorDoc::OnUpdateEditCut)
	ON_COMMAND(ID_EDIT_PASTE, &CSoundEditorDoc::OnEditPaste)
	ON_UPDATE_COMMAND_UI(ID_EDIT_PASTE, &CSoundEditorDoc::OnUpdateEditPaste)
END_MESSAGE_MAP()


// CSoundEditorDoc 构造/析构

CSoundEditorDoc::CSoundEditorDoc() : m_pSelectedShape(NULL), m_nPolygonEditState(0), m_pEB(NULL),
m_pSelectedEnvArea(NULL), m_pSelectedBkgArea(NULL), m_nSelectedPt(-1), m_editState(EDIT_STATE_ENV_SOUND), 
m_bTestingEnableDark(false), m_bTestingDrawEnvArea(false), m_bEnableEnv(true), m_bEnableBkg(true),
m_bTestingDrawBkgArea(false), m_nUpdateViewSign(UVS_ALL), m_strMiniMapURL(L""), m_nEnvVolume(100),
m_us(US_DEFAULT), m_nGameTimeCycle(Nuclear::XP_A_DAY_TIME / 10), m_fGameTime(0.0f), m_nBkgVolume(100),
m_pSSM(NULL), m_bSSMModified(false), m_strSSMURL(L""), m_nStepBrush(-1), m_nBrushSize(1)
{
	m_bTestingDrawStep = FALSE;
	m_bTestingDrawBkgVolArea = FALSE;
	m_bDrawMaze = FALSE;
	m_SpriteMazeMask = GetPrivateProfileInt(L"MazeData", L"SpriteMask", 3, theApp.GetCfgFilePath());
	m_movingSelectArea.x = 0;
	m_movingSelectArea.y = 0;
	m_nHandleSeed = 0;
	m_nEnvOrdersSeed = 0;
	m_nEnvSoundCount = 0;
	m_nBkgOrdersSeed = 0;
	m_nBkgSoundCount = 0;
	m_nCurOperatorType = -1;
	m_pCurOperator = NULL;
	m_pOperatorList = new CActionList(this);
	m_lastEnvOpenUrl = L"";
	m_lastBkgOpenUrl = L"";
	m_pCopiedEnvArea = NULL;
	m_pCopiedBkgArea = NULL;
	m_pCopiedBkgVolArea = NULL;
	m_pSelectedBkgVolArea = NULL;
}

CSoundEditorDoc::~CSoundEditorDoc()
{
	FreeAll();
	delete m_pOperatorList;
}

BOOL CSoundEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	FreeAll();

	return TRUE;
}

void CSoundEditorDoc::SetEnvVolume(unsigned char v, UPDATE_SOURCE s)
{
	if (v != m_nEnvVolume)
	{
		m_nEnvVolume = v;
		m_nUpdateViewSign |= UVS_TEST_SET_VOLUME;
		UpdateAllViews(NULL);
		m_nUpdateViewSign &= ~UVS_TEST_SET_VOLUME;
	}
}

void CSoundEditorDoc::EnableEnv(bool b, UPDATE_SOURCE s)
{
	if (m_bEnableEnv != b)
	{
		m_bEnableEnv = b;
		m_nUpdateViewSign |= UVS_TEST_SET_VOLUME;
		UpdateAllViews(NULL);
		m_nUpdateViewSign &= ~UVS_TEST_SET_VOLUME;
	}
}

void CSoundEditorDoc::SetBkgVolume(unsigned char v, UPDATE_SOURCE s)
{
	if (v != m_nBkgVolume)
	{
		m_nBkgVolume = v;
		m_nUpdateViewSign |= UVS_TEST_SET_VOLUME;
		UpdateAllViews(NULL);
		m_nUpdateViewSign &= ~UVS_TEST_SET_VOLUME;
	}
}

void CSoundEditorDoc::EnableBkg(bool b, UPDATE_SOURCE s)
{
	if (m_bEnableBkg != b)
	{
		m_bEnableBkg = b;
		m_nUpdateViewSign |= UVS_TEST_SET_VOLUME;
		UpdateAllViews(NULL);
		m_nUpdateViewSign &= ~UVS_TEST_SET_VOLUME;
	}
}

void CSoundEditorDoc::SetGameTimeCycle(int cycle)
{
	if (cycle != m_nGameTimeCycle)
	{
		m_nGameTimeCycle = cycle;
		m_nUpdateViewSign |= UVS_TEST_SET_GAMETIME_CYCLE;
		UpdateAllViews(NULL);
		m_nUpdateViewSign &= ~UVS_TEST_SET_GAMETIME_CYCLE;
	}
}

void CSoundEditorDoc::SetGameTime(float time, UPDATE_SOURCE s)
{
	m_us = s;
	m_fGameTime = time;
	m_nUpdateViewSign |= UVS_TEST_SET_GAMETIME;
	UpdateAllViews(NULL);
	m_nUpdateViewSign &= ~UVS_TEST_SET_GAMETIME;
}


// CSoundEditorDoc 序列化

void CSoundEditorDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: 在此添加存储代码
	}
	else
	{
		// TODO: 在此添加加载代码
	}
}


// CSoundEditorDoc 诊断

#ifdef _DEBUG
void CSoundEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CSoundEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

// CSoundEditorDoc 命令
BOOL CSoundEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	FreeAll();
	CString fullPath = lpszPathName;
	fullPath = fullPath.Left(fullPath.GetLength()-9);
	m_strSSMURL = fullPath + L"ssm.ssm";
	m_strMiniMapURL = fullPath + L"mini_map.jpg";
	LoadSSM();
	XMLIO::CFileReader fr;
	int retcode = fr.OpenNativeFile(lpszPathName);
	if (retcode != XMLIO::EC_SUCCESS)
	{
		if (retcode == ERROR_FILE_NOT_FOUND)
		{
			XMLIO::CFileWriter fw;
			XMLIO::CONode root;
			fw.GetRootNode(root);
			SaveAsXML(root, Nuclear::XPSOUND_IO_EDIT);
			return XMLIO::EC_SUCCESS == fw.SaveNative(lpszPathName);
		}
		return FALSE;
	}
	XMLIO::CINode root;
	fr.GetRootNode(root);
	XMLIO::CNodeList rootnl;
	root.GetChildren(rootnl);
	XMLIO::CNodeList::iterator rnlIt = rootnl.begin();
	for (;rnlIt!=rootnl.end();++rnlIt)
	{
		if (rnlIt->GetType() != XMLIO::NT_ELEMENT)
			continue;
		if (rnlIt->GetName() == L"env")	//环境音
		{
			XMLIO::CNodeList envnl;
			CToolsEnvSoundArea *pEsa;
			rnlIt->GetChildren(envnl);
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
				pEsa->nOrders = m_nEnvOrdersSeed++;
				pEsa->nSoundListBeginID = m_nEnvSoundCount;
				int typeCount = pEsa->GetTypeCount();
				pEsa->soundHandleVet.resize(typeCount);
				for (int i = 0;i<typeCount;++i)
				{
					std::vector<std::wstring> slist;
					pEsa->GetSoundList(i, slist);
					m_nEnvSoundCount += (int)slist.size();
					pEsa->soundHandleVet[i].resize(slist.size());
					std::vector<std::wstring>::iterator sit = slist.begin(), sie = slist.end();
					int soundid = 0;
					for (;sit!=sie;++sit,++soundid)
					{
						pEsa->soundHandleVet[i][soundid] = ++m_nHandleSeed;
						m_envSoundMap[m_nHandleSeed] = pEsa;
					}
				}
				pEsa->nSoundListEndID = m_nEnvSoundCount;
				m_envAreas.insert(pEsa);
			}
		} else if (rnlIt->GetName() == L"bgs")	//背景音
		{
			XMLIO::CNodeList bkgnl;
			CToolsBkgSoundArea *pBsa;
			rnlIt->GetChildren(bkgnl);
			XMLIO::CNodeList::iterator bIt = bkgnl.begin(), bIe = bkgnl.end();
			bool bIsDefault = false;
			for (;bIt!=bIe;++bIt)
			{
				if (bIt->GetType() != XMLIO::NT_ELEMENT)
					continue;
				bIsDefault = false;
				if (bIt->GetName() == L"circle")
					pBsa = new CToolsBkgSoundCircleArea();
				else if (bIt->GetName() == L"polygon")
					pBsa = new CToolsBkgSoundPolygonArea();
				else if (bIt->GetName() == L"default")
				{
					bIsDefault = true;
					pBsa = &m_defaultBkgArea;
				}
				else
					XPASSERT(false);
				pBsa->Load(*bIt, Nuclear::XPSOUND_IO_EDIT);
				pBsa->nOrders = m_nBkgOrdersSeed++;
				pBsa->nSoundListBeginID = m_nBkgSoundCount;
				std::vector<std::wstring> slist;
				pBsa->GetSoundList(slist);
				m_nBkgSoundCount += (int)slist.size();
				pBsa->nSoundListEndID = m_nBkgSoundCount;
				std::vector<std::wstring>::iterator sit = slist.begin(), sie = slist.end();
				pBsa->soundHandleVet.resize(slist.size());
				int soundid = 0;
				for (;sit!=sie;++sit, ++soundid)
				{
					pBsa->soundHandleVet[soundid] = ++m_nHandleSeed;
					m_bkgSoundMap[m_nHandleSeed] = pBsa;
				}
				if (!bIsDefault) m_bkgAreas.insert(pBsa);
			}
		} else if (rnlIt->GetName() == L"bgv")	//背景音音量
		{
			XMLIO::CNodeList bkgnl;
			CToolsBkgVolumeArea *pBsv = NULL;
			rnlIt->GetChildren(bkgnl);
			XMLIO::CNodeList::iterator bIt = bkgnl.begin(), bIe = bkgnl.end();
			for (;bIt!=bIe;++bIt)
			{
				if (bIt->GetType() != XMLIO::NT_ELEMENT)
					continue;
				if (bIt->GetName() == L"circle")
					pBsv = new CToolsBkgVolCircleArea();
				else if (bIt->GetName() == L"polygon")
					pBsv = new CToolsBkgVolPolygonArea();
				else
					XPASSERT(false);
				pBsv->Load(*bIt, Nuclear::XPSOUND_IO_EDIT);
				m_bkgVolAreas.insert(pBsv);
			}
		} else {
			XPASSERT(false);
		}
	}
	fr.CloseFile();
	return TRUE;
}

BOOL CSoundEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	SaveSSM();
	XMLIO::CFileWriter fw;
	XMLIO::CONode root;
	fw.GetRootNode(root);
	SaveAsXML(root, Nuclear::XPSOUND_IO_EDIT);
	int retcode = fw.SaveNative(lpszPathName);
	if (retcode == XMLIO::EC_SUCCESS)
	{
		SetModifiedFlag(FALSE);
		return TRUE;
	}
	return FALSE;
}

void CSoundEditorDoc::FreeAll()
{
	m_bStepHiddent.clear();
	m_pOperatorList->CleanAll();
	if (m_pSelectedShape)
		delete m_pSelectedShape;
	if (m_pSSM)
		delete m_pSSM;
	m_pSSM = NULL;
	if (m_pCopiedBkgArea)
		delete m_pCopiedBkgArea;
	m_pCopiedBkgArea = NULL;
	if (m_pCopiedEnvArea)
		delete m_pCopiedEnvArea;
	m_pCopiedEnvArea = NULL;
	m_pCopiedBkgVolArea = NULL;
	m_pSelectedShape = NULL;
	m_pSelectedEnvArea = NULL;
	m_pSelectedBkgArea = NULL;
	m_pSelectedBkgVolArea = NULL;
	m_movingSelectArea.x = 0;
	m_movingSelectArea.y = 0;
	m_nEnvOrdersSeed = 0;
	m_nEnvSoundCount = 0;
	m_nBkgOrdersSeed = 0;
	m_nBkgSoundCount = 0;
	m_fGameTime = 0.0f;
	m_nSelectedPt = -1;
	m_nPolygonEditState = 0;
	m_nHandleSeed = 0;
	m_nCurOperatorType = -1;
	m_strSSMURL = L"";
	m_strMiniMapURL = L"";
	m_mazeName = L"";
	m_pCurOperator = NULL;
	m_envSoundMap.clear();
	m_bkgSoundMap.clear();
	m_handleVet.clear();

	EnvAreaCont::iterator eIt = m_envAreas.begin(), eIe = m_envAreas.end();
	for (;eIt!=eIe;++eIt)
	{
		delete (*eIt);
	}
	m_envAreas.clear();
	BkgAreaCont::iterator bIt = m_bkgAreas.begin(), bIe = m_bkgAreas.end();
	for (;bIt!=bIe;++bIt)
	{
		delete (*bIt);
	}
	m_bkgAreas.clear();
	BkgVolAreaCont::iterator bvIt = m_bkgVolAreas.begin(), bvIe = m_bkgVolAreas.end();
	for (;bvIt!=bvIe;++bvIt)
	{
		delete (*bvIt);
	}
	m_bkgVolAreas.clear();
	m_defaultBkgArea.ClearSound();
	m_defaultBkgArea.ClearSoundHandle();
}

void CSoundEditorDoc::SaveAsXML(XMLIO::CONode &root, int flag)
{
	XMLIO::CONode env;
	root.CreateSubElement(L"env", env);
	EnvAreaCont::iterator envIt = m_envAreas.begin(), envIe = m_envAreas.end();
	CToolsEnvSoundArea *pEsa = NULL;
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
		pEsa->Save(envAreaNode, flag);
	}
	XMLIO::CONode bgs;
	root.CreateSubElement(L"bgs", bgs);
	BkgAreaCont::iterator bkgIt = m_bkgAreas.begin(), bkgIe = m_bkgAreas.end();
	CToolsBkgSoundArea *pBkg = NULL;
	XMLIO::CONode bkgDefAreaNode;
	bgs.CreateSubElement(L"default", bkgDefAreaNode);
	m_defaultBkgArea.Save(bkgDefAreaNode, flag);
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
		pBkg->Save(bkgAreaNode, flag);
	}
	XMLIO::CONode bgv;
	root.CreateSubElement(L"bgv", bgv);
	BkgVolAreaCont::iterator bkvIt = m_bkgVolAreas.begin(), bkvIe = m_bkgVolAreas.end();
	CToolsBkgVolumeArea *pBkgVol = NULL;
	for (;bkvIt!=bkvIe;++bkvIt)
	{
		pBkgVol = *bkvIt;
		XMLIO::CONode bkvAreaNode;
		switch (pBkgVol->GetType())
		{
		case Nuclear::XPAREA_TYPE_CIRCLE:
			bgv.CreateSubElement(L"circle", bkvAreaNode);
			break;
		case Nuclear::XPAREA_TYPE_POLYGON:
			bgv.CreateSubElement(L"polygon", bkvAreaNode);
			break;
		default:
			XPASSERT(false);
		}
		pBkgVol->Save(bkvAreaNode, flag);
	}
}

void CSoundEditorDoc::SetShape(CToolsSoundArea* pArea, const Nuclear::XPIShape* pShape)
{
	pArea->SetShape(pShape);
}

void CSoundEditorDoc::BeginSetStepSound()
{
	if (m_editState != EDIT_STATE_STEP)
		return;
	XPASSERT(m_pCurOperator == NULL);
	m_pCurOperator = new StepSoundArea();
}
//像素坐标
void CSoundEditorDoc::SetStepSound(RECT rect, int gid)
{
	if (m_editState != EDIT_STATE_STEP)
		return;
	if (!m_pSSM)
		return;
	if (rect.left < 0)
		rect.left = 0;
	if (rect.top < 0)
		rect.top = 0;
	if (rect.right > m_mapSize.x)
		rect.right = m_mapSize.x;
	if (rect.bottom > m_mapSize.y)
		rect.bottom = m_mapSize.y;
	if (rect.left > rect.right || rect.top > rect.bottom)
		return;
	rect.left /= Nuclear::PStepSoundMap::GRID_WIDTH;
	rect.right /= Nuclear::PStepSoundMap::GRID_WIDTH;
	rect.top /= Nuclear::PStepSoundMap::GRID_HEIGHT;
	rect.bottom /= Nuclear::PStepSoundMap::GRID_HEIGHT;
	int width = m_mapSize.x / Nuclear::PStepSoundMap::GRID_WIDTH;
	int i, j, pos;
	StepSoundArea* pOper = dynamic_cast<StepSoundArea*>(m_pCurOperator);
	XPASSERT(pOper != NULL);
	if (pOper->olddata.size() == 0)
	{
		pOper->newdata = gid;
	} else {
		XPASSERT(pOper->newdata == (Nuclear::StepSoundGT)gid);
	}
	Nuclear::StepSoundGT* pData = m_pSSM->GetData(), *pTmpData = NULL;
	for (j=rect.top;j<rect.bottom;++j)
	{
		pos = j * width + rect.left;
		pTmpData = pData + pos;
		for (i=rect.left;i<rect.right;++i,++pTmpData, ++pos)
		{
			if (*pTmpData != gid)
			{
				std::pair<int, Nuclear::StepSoundGT> pairva = std::pair<int, Nuclear::StepSoundGT>(pos, *pTmpData);
				pOper->olddata.push_back(pairva);
				*pTmpData = gid;
			}
		}
	}
}
void CSoundEditorDoc::EndSetStepSound()
{
	StepSoundArea* pOper = dynamic_cast<StepSoundArea*>(m_pCurOperator);
	XPASSERT(pOper != NULL);
	if (m_editState != EDIT_STATE_STEP)
	{
		delete m_pCurOperator;
		m_pCurOperator = NULL;
		return;
	}
	if (pOper->olddata.size() > 0)
	{
		m_bSSMModified = true;
		m_pOperatorList->DoOneAction(m_pCurOperator);
	} else {
		delete pOper;
	}
	m_pCurOperator = NULL;	
}

bool CSoundEditorDoc::ClearPolygonCuts(CToolsSoundArea* pArea)
{
	switch(m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			CToolsEnvSoundPolygonArea* pPolyArea = dynamic_cast<CToolsEnvSoundPolygonArea*>(pArea);
			if (!pPolyArea)
				return false;

			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pPolyArea->m_innerPolygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pPolyArea->m_ToolsCutPairs;
			pOper->state = EDIT_STATE_ENV_SOUND;
			pPolyArea->ClearSPolygons();
			pPolyArea->ResetToolsPolygon();
			pOper->polygon[CAction::AFTER_ACTION] = pPolyArea->m_innerPolygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pPolyArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
			return true;
			break;
		}
	case EDIT_STATE_BKG_MUSIC:
		{
			CToolsBkgSoundPolygonArea* pPolyArea = dynamic_cast<CToolsBkgSoundPolygonArea*>(pArea);
			if (!pPolyArea)
				return false;
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pPolyArea->m_polygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pPolyArea->m_ToolsCutPairs;
			pOper->state = EDIT_STATE_BKG_MUSIC;
			pPolyArea->ClearSPolygons();
			pPolyArea->ResetToolsPolygon();
			pOper->polygon[CAction::AFTER_ACTION] = pPolyArea->m_polygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pPolyArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
			return true;
			break;
		}
	case EDIT_STATE_BKG_VOL:
		{
			CToolsBkgVolPolygonArea* pPolyArea = dynamic_cast<CToolsBkgVolPolygonArea*>(pArea);
			if (!pPolyArea)
				return false;
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pPolyArea->m_innerPolygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pPolyArea->m_ToolsCutPairs;
			pOper->state = EDIT_STATE_BKG_VOL;
			pPolyArea->ClearSPolygons();
			pPolyArea->ResetToolsPolygon();
			pOper->polygon[CAction::AFTER_ACTION] = pPolyArea->m_innerPolygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pPolyArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
			return true;
			break;
		}
	default:
		break;
	}
	return false;
}

void CSoundEditorDoc::SetSelectedAreaMaxVol(int typeID, int soundID, unsigned char v)
{
	if (!m_pSelectedEnvArea)
		return;
	unsigned char oldV = m_pSelectedEnvArea->GetMaxVol(typeID, soundID);
	if (oldV != v)
	{
		EnvSoundProp* pOper = new EnvSoundProp(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->soundID = soundID;
		pOper->type = EnvSoundProp::MAX_VOL;
		pOper->data[CAction::BEFORE_ACTION] = oldV;
		pOper->data[CAction::AFTER_ACTION] = v;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetMaxVol(typeID, soundID, v);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaMinVol(int typeID, int soundID, unsigned char v)
{
	if (!m_pSelectedEnvArea)
		return;
	unsigned char oldV = m_pSelectedEnvArea->GetMinVol(typeID, soundID);
	if (oldV != v)
	{
		EnvSoundProp* pOper = new EnvSoundProp(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->soundID = soundID;
		pOper->type = EnvSoundProp::MIN_VOL;
		pOper->data[CAction::BEFORE_ACTION] = oldV;
		pOper->data[CAction::AFTER_ACTION] = v;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetMinVol(typeID, soundID, v);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaMaxFreq(int typeID, int soundID, unsigned char f)
{
	if (!m_pSelectedEnvArea)
		return;
	unsigned char oldF = m_pSelectedEnvArea->GetMaxFreq(typeID, soundID);
	if (oldF != f)
	{
		EnvSoundProp* pOper = new EnvSoundProp(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->soundID = soundID;
		pOper->type = EnvSoundProp::MAX_FREQ;
		pOper->data[CAction::BEFORE_ACTION] = oldF;
		pOper->data[CAction::AFTER_ACTION] = f;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetMaxFreq(typeID, soundID, f);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaMinFreq(int typeID, int soundID, unsigned char f)
{
	if (!m_pSelectedEnvArea)
		return;
	unsigned char oldF = m_pSelectedEnvArea->GetMinFreq(typeID, soundID);
	if (oldF != f)
	{
		EnvSoundProp* pOper = new EnvSoundProp(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->soundID = soundID;
		pOper->type = EnvSoundProp::MIN_FREQ;
		pOper->data[CAction::BEFORE_ACTION] = oldF;
		pOper->data[CAction::AFTER_ACTION] = f;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetMinFreq(typeID, soundID, f);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaTime(int typeID, int soundID, int type, int time)
{
	if (!m_pSelectedEnvArea)
		return;
	if (type < 0)
		return;
	if (type > 3)
		return;
	int oldTime = m_pSelectedEnvArea->GetTime(typeID, soundID, type);
	if (oldTime != time)
	{
		EnvSoundTime* pOper = new EnvSoundTime(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->soundID = soundID;
		pOper->type = type;
		pOper->time[CAction::BEFORE_ACTION] = oldTime;
		pOper->time[CAction::AFTER_ACTION] = time;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetTime(typeID, soundID, type, time);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaLooping(int typeID, int soundID, bool loop)
{
	if (!m_pSelectedEnvArea)
		return;
	bool oldLoop = m_pSelectedEnvArea->IsLooping(typeID, soundID);
	if (oldLoop != loop)
	{
		EnvSoundLoop* pOper = new EnvSoundLoop(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->soundID = soundID;
		pOper->loop[CAction::BEFORE_ACTION] = oldLoop;
		pOper->loop[CAction::AFTER_ACTION] = loop;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetLooping(typeID, soundID, loop);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaSoundType(int typeID, int soundID, unsigned char type)
{
	if (!m_pSelectedEnvArea)
		return;
	unsigned char oldType = m_pSelectedEnvArea->GetSoundType(typeID, soundID);
	if (oldType != type)
	{
		EnvSoundProp* pOper = new EnvSoundProp(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->soundID = soundID;
		pOper->type = EnvSoundProp::SOUND_TYPE;
		pOper->data[CAction::BEFORE_ACTION] = oldType;
		pOper->data[CAction::AFTER_ACTION] = type;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetSoundType(typeID, soundID, type);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaMinInterval(int typeID, int interval)
{
	if (!m_pSelectedEnvArea)
		return;
	int oldInterval = m_pSelectedEnvArea->GetMinInterval(typeID);
	if (oldInterval != interval)
	{
		EnvSoundInterval* pOper = new EnvSoundInterval(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->type = EnvSoundInterval::MIN;
		pOper->interval[CAction::BEFORE_ACTION] = oldInterval;
		pOper->interval[CAction::AFTER_ACTION] = interval;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetMinInterval(typeID, interval);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaMaxInterval(int typeID, int interval)
{
	if (!m_pSelectedEnvArea)
		return;
	int oldInterval = m_pSelectedEnvArea->GetMaxInterval(typeID);
	if (oldInterval != interval)
	{
		EnvSoundInterval* pOper = new EnvSoundInterval(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->type = EnvSoundInterval::MAX;
		pOper->interval[CAction::BEFORE_ACTION] = oldInterval;
		pOper->interval[CAction::AFTER_ACTION] = interval;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetMaxInterval(typeID, interval);
		SetModifiedFlag();
	}
}


void CSoundEditorDoc::SetSelectedAreaPriority(int typeID, short priority)
{
	if (!m_pSelectedEnvArea)
		return;
	short oldPri = m_pSelectedEnvArea->GetPriority(typeID);
	if (oldPri != priority)
	{
		EnvSoundPriority* pOper = new EnvSoundPriority(m_pSelectedEnvArea);
		pOper->typeID = typeID;
		pOper->priority[CAction::BEFORE_ACTION] = oldPri;
		pOper->priority[CAction::AFTER_ACTION] = priority;
		m_pOperatorList->DoOneAction(pOper);
		m_pSelectedEnvArea->SetPriority(typeID, priority);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaVol(int soundID, int vol)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	int oldVol = pArea->GetVol(soundID);
	if (oldVol != vol)
	{
		BkgSoundVolume* pOper = new BkgSoundVolume(pArea);
		pOper->soundID = soundID;
		pOper->vol[CAction::BEFORE_ACTION] = oldVol;
		pOper->vol[CAction::AFTER_ACTION] = vol;
		m_pOperatorList->DoOneAction(pOper);
		pArea->SetVol(soundID, vol);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaSoundType(int soundID,  Nuclear::XPSA_SOUND_TYPE type)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	 Nuclear::XPSA_SOUND_TYPE oldtype = pArea->GetSoundType(soundID);
	if (oldtype != type)
	{
		BkgSoundType* pOper = new BkgSoundType(pArea);
		pOper->soundID = soundID;
		pOper->type[CAction::BEFORE_ACTION] = oldtype;
		pOper->type[CAction::AFTER_ACTION] = type;
		m_pOperatorList->DoOneAction(pOper);
		pArea->SetSoundType(soundID, type);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::ModifyEnvAreaSoundOrders(int order, int value)
{
	EnvAreaCont::iterator it = m_envAreas.begin(), ie = m_envAreas.end();
	for (;it!=ie;++it)
	{
		if ((*it)->nOrders > order)
		{
			((*it)->nSoundListEndID) += value;
			((*it)->nSoundListBeginID) += value;
		}
	}
}

int CSoundEditorDoc::AddSound2SelectedArea(int typeID, const std::wstring &name, __out SoundInfo &info)
{
	if (!m_pSelectedEnvArea)
		return -1;
	SetModifiedFlag();
	int result = m_pSelectedEnvArea->AddSound(typeID, name);
	info.nID = (m_pSelectedEnvArea->nSoundListEndID)++;
	info.handle = ++m_nHandleSeed;
	m_envSoundMap[info.handle] = m_pSelectedEnvArea;
	m_pSelectedEnvArea->soundHandleVet[typeID].push_back(info.handle);

	ModifyEnvAreaSoundOrders(m_pSelectedEnvArea->nOrders, 1);

	AddDelEnvSound* pOper = new AddDelEnvSound(m_pSelectedEnvArea);
	pOper->isAdd = true;
	pOper->typeID = typeID;
	pOper->param = m_pSelectedEnvArea->GetSoundParam(typeID, result);
	pOper->soundID = result;
	pOper->handle = info.handle;
	pOper->nAllSoundListID = info.nID;
	m_pOperatorList->DoOneAction(pOper);
	return result;
}

void CSoundEditorDoc::DeleteSelectedAreaSound(int typeID, int soundID, UINT nAllSoundListID)
{
	if (!m_pSelectedEnvArea)
		return;
	DWORD_PTR handle = m_pSelectedEnvArea->GetHandle(typeID, soundID);


	AddDelEnvSound* pOper = new AddDelEnvSound(m_pSelectedEnvArea);
	pOper->isAdd = false;
	pOper->typeID = typeID;
	pOper->param = m_pSelectedEnvArea->GetSoundParam(typeID, soundID);
	pOper->soundID = soundID;
	pOper->handle = handle;
	pOper->nAllSoundListID = nAllSoundListID;
	m_pOperatorList->DoOneAction(pOper);

	m_pSelectedEnvArea->DeleteSound(typeID, soundID);
	
	m_envSoundMap.erase(handle);
	m_pSelectedEnvArea->soundHandleVet[typeID].erase(m_pSelectedEnvArea->soundHandleVet[typeID].begin() + soundID);
	--(m_pSelectedEnvArea->nSoundListEndID);

	ModifyEnvAreaSoundOrders(m_pSelectedEnvArea->nOrders, -1);

	SetModifiedFlag();
}

void CSoundEditorDoc::ModifyBkgAreaSoundOrders(int order, int value)
{
	BkgAreaCont::iterator it = m_bkgAreas.begin(), ie = m_bkgAreas.end();
	for (;it!=ie;++it)
	{
		if ((*it)->nOrders > order)
		{
			((*it)->nSoundListEndID) += value;
			((*it)->nSoundListBeginID) += value;
		}
	}
	if (m_defaultBkgArea.nOrders > order)
	{
		m_defaultBkgArea.nSoundListEndID += value;
		m_defaultBkgArea.nSoundListBeginID += value;
	}
}

int CSoundEditorDoc::AddSound2SelectedArea(const std::wstring &name, __out SoundInfo &info)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	int result = pArea->AddSound(name);
	info.nID = (pArea->nSoundListEndID)++;
	info.handle = ++m_nHandleSeed;
	m_bkgSoundMap[m_nHandleSeed] = pArea;
	pArea->soundHandleVet.push_back(info.handle);
	
	ModifyBkgAreaSoundOrders(pArea->nOrders, 1);

	AddDelBkgSound* pOper = new AddDelBkgSound(pArea);
	pOper->isAdd = true;
	pOper->param = pArea->GetSoundParam(result);
	pOper->soundID = result;
	pOper->handle = info.handle;
	pOper->nAllSoundListID = info.nID;
	m_pOperatorList->DoOneAction(pOper);

	SetModifiedFlag();
	return result;
}

void CSoundEditorDoc::DeleteSelectedAreaSound(int soundID, UINT nAllSoundListID)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);

	DWORD_PTR handle = pArea->GetHandle(soundID);

	AddDelBkgSound* pOper = new AddDelBkgSound(pArea);
	pOper->isAdd = false;
	pOper->param = pArea->GetSoundParam(soundID);
	pOper->soundID = soundID;
	pOper->handle = handle;
	pOper->nAllSoundListID = nAllSoundListID;
	m_pOperatorList->DoOneAction(pOper);

	pArea->DeleteSound(soundID);
	m_bkgSoundMap.erase(handle);
	pArea->soundHandleVet.erase(pArea->soundHandleVet.begin() + soundID);
	--(pArea->nSoundListEndID);
	ModifyBkgAreaSoundOrders(pArea->nOrders, -1);
	SetModifiedFlag();
}

void CSoundEditorDoc::SetSelectedAreaMinInterval(int interval)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	if (!pArea)
		return;
	int oldInterval = pArea->GetMinInterval();
	if (oldInterval != interval)
	{
		BkgSoundInterval* pOper = new BkgSoundInterval(pArea);
		pOper->type = BkgSoundInterval::MIN;
		pOper->interval[CAction::BEFORE_ACTION] = oldInterval;
		pOper->interval[CAction::AFTER_ACTION] = interval;
		m_pOperatorList->DoOneAction(pOper);
		pArea->SetMinInterval(interval);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaMaxInterval(int interval)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	if (!pArea)
		return;;
	int oldInterval = pArea->GetMaxInterval();
	if (oldInterval != interval)
	{
		BkgSoundInterval* pOper = new BkgSoundInterval(pArea);
		pOper->type = BkgSoundInterval::MAX;
		pOper->interval[CAction::BEFORE_ACTION] = oldInterval;
		pOper->interval[CAction::AFTER_ACTION] = interval;
		m_pOperatorList->DoOneAction(pOper);
		pArea->SetMaxInterval(interval);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaFadeIn(int f)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	if (!pArea)
		return;;
	int oldF = pArea->GetFadeInTime();
	if (oldF != f)
	{
		BkgSoundFadeTime* pOper = new BkgSoundFadeTime(pArea);
		pOper->type = BkgSoundFadeTime::T_IN;
		pOper->fadeTime[CAction::BEFORE_ACTION] = oldF;
		pOper->fadeTime[CAction::AFTER_ACTION] = f;
		m_pOperatorList->DoOneAction(pOper);
		pArea->SetFadeInTime(f);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaFadeOut(int f)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	if (!pArea)
		return;;
	int oldF = pArea->GetFadeOutTime();
	if (oldF != f)
	{
		BkgSoundFadeTime* pOper = new BkgSoundFadeTime(pArea);
		pOper->type = BkgSoundFadeTime::T_OUT;
		pOper->fadeTime[CAction::BEFORE_ACTION] = oldF;
		pOper->fadeTime[CAction::AFTER_ACTION] = f;
		m_pOperatorList->DoOneAction(pOper);
		pArea->SetFadeOutTime(f);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaWaitPrevArea(bool b)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	if (!pArea)
		return;;
	bool oldB = pArea->IsWaitPrevAreaPlayEnd();
	if (oldB != b)
	{
		BkgSoundWaitPrevArea* pOper = new BkgSoundWaitPrevArea(pArea);
		pOper->wait[CAction::BEFORE_ACTION] = oldB;
		pOper->wait[CAction::AFTER_ACTION] = b;
		m_pOperatorList->DoOneAction(pOper);
		pArea->SetWaitPrevAreaPlayEnd(b);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::SetSelectedAreaPlayMode(Nuclear::XPBGM_PLAY_TYPE mode)
{
	CToolsBkgSoundArea* pArea = m_pSelectedBkgArea?m_pSelectedBkgArea:(&m_defaultBkgArea);
	if (!pArea)
		return;;
	Nuclear::XPBGM_PLAY_TYPE oldM = pArea->GetPlayMode();
	if (oldM != mode)
	{
		BkgSoundPlayMode* pOper = new BkgSoundPlayMode(pArea);
		pOper->type[CAction::BEFORE_ACTION] = oldM;
		pOper->type[CAction::AFTER_ACTION] = mode;
		m_pOperatorList->DoOneAction(pOper);
		pArea->SetPlayMode(mode);
		SetModifiedFlag();
	}
}

void CSoundEditorDoc::DeleteSelectedArea()
{
	CToolsSoundArea::HandleVet &handles = m_handleVet;
	handles.clear();
	switch(m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			if (!m_pSelectedEnvArea)
				return;
			AddDelEnvArea *pOper = new AddDelEnvArea(m_pSelectedEnvArea);
			pOper->isAdd = false;
			m_pCurOperator = pOper;
			m_pOperatorList->DoOneAction(pOper);

			CToolsSoundArea::HandleVet::iterator hIt, hIe;
			CToolsSoundArea::HandleVetVet::iterator hvIt = m_pSelectedEnvArea->soundHandleVet.begin(), 
				hvIe = m_pSelectedEnvArea->soundHandleVet.end();
			for (;hvIt!=hvIe;++hvIt)
			{
				hIt = hvIt->begin();
				hIe = hvIt->end();
				handles.insert(handles.end(), hIt, hIe);
				for (;hIt!=hIe;++hIt)
					m_envSoundMap.erase(*hIt);
			}
			m_envAreas.erase(m_pSelectedEnvArea);
			int count = (int)handles.size();
			ModifyEnvAreaSoundOrders(m_pSelectedEnvArea->nOrders, -count);
			
			m_pSelectedEnvArea = NULL;
			m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED | UVS_ENV_SOUND_AREA_SHAPE | UVS_ENV_SOUND_AREA_ADD_DELETED;
			break;
		}
	case EDIT_STATE_BKG_MUSIC:
		{
			if (!m_pSelectedBkgArea)
				return;

			AddDelBkgArea *pOper = new AddDelBkgArea(m_pSelectedBkgArea);
			pOper->isAdd = false;
			m_pCurOperator = pOper;
			m_pOperatorList->DoOneAction(pOper);

			CToolsSoundArea::HandleVet::iterator hIt = m_pSelectedBkgArea->soundHandleVet.begin(), 
				hIe = m_pSelectedBkgArea->soundHandleVet.end();
			handles.insert(handles.end(), hIt, hIe);
			for (;hIt!=hIe;++hIt)
				m_bkgSoundMap.erase(*hIt);
			m_bkgAreas.erase(m_pSelectedBkgArea);
			int count = (int)handles.size();
			ModifyBkgAreaSoundOrders(m_pSelectedBkgArea->nOrders, -count);

			m_pSelectedBkgArea = NULL;
			m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SELECTED | UVS_BKG_SOUND_AREA_SHAPE | UVS_BKG_SOUND_AREA_ADD_DELETED;
			break;
		}
	case EDIT_STATE_BKG_VOL:
		{
			if (!m_pSelectedBkgVolArea)
				return;

			AddDelBkgVolArea *pOper = new AddDelBkgVolArea(m_pSelectedBkgVolArea);
			pOper->isAdd = false;
			m_pCurOperator = pOper;
			m_pOperatorList->DoOneAction(pOper);

			m_bkgVolAreas.erase(m_pSelectedBkgVolArea);
			
			m_pSelectedBkgVolArea = NULL;
			m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SELECTED | UVS_BKV_SOUND_AREA_SHAPE | UVS_BKV_SOUND_AREA_ADD_DELETED;
			break;
		}
	default:
		return;
	}
	UpdateAllViews(NULL);
	m_pCurOperator = NULL;
	m_nUpdateViewSign = 0;
	handles.clear();
	SetModifiedFlag();

}

int CSoundEditorDoc::AddSoundType2SelectedArea()
{
	if (!m_pSelectedEnvArea)
		return -1;
	SetModifiedFlag();
	m_pSelectedEnvArea->soundHandleVet.push_back(CToolsSoundArea::HandleVet());
	AddDelEnvType* pOper = new AddDelEnvType(m_pSelectedEnvArea);
	pOper->isAdd = true;
	pOper->typeID = m_pSelectedEnvArea->AddSoundType();
	pOper->param = m_pSelectedEnvArea->GetSoundTypeParam(pOper->typeID);
	m_pOperatorList->DoOneAction(pOper);
	return pOper->typeID;
}

void CSoundEditorDoc::DeleteSoundTypeFromSelectedArea(int typeID, AddDelEnvType* &pOper)
{
	if (!m_pSelectedEnvArea)
		return;
	pOper = new AddDelEnvType(m_pSelectedEnvArea);
	pOper->isAdd = false;
	pOper->typeID = typeID;
	pOper->param = m_pSelectedEnvArea->GetSoundTypeParam(typeID);
	m_pSelectedEnvArea->DeleteSoundType(typeID);
	CToolsSoundArea::HandleVet::iterator hIt = m_pSelectedEnvArea->soundHandleVet[typeID].begin(),
		hIe = m_pSelectedEnvArea->soundHandleVet[typeID].end();
	pOper->soundHandles.clear();
	pOper->soundHandles.insert(pOper->soundHandles.begin(), hIt, hIe);
	m_pOperatorList->DoOneAction(pOper);
	for (;hIt!=hIe;++hIt)
	{
		m_envSoundMap.erase(*hIt);
	}
	int count = (int)pOper->soundHandles.size();
	m_pSelectedEnvArea->soundHandleVet.erase(m_pSelectedEnvArea->soundHandleVet.begin() + typeID);
	m_pSelectedEnvArea->nSoundListEndID -= count;
	ModifyEnvAreaSoundOrders(m_pSelectedEnvArea->nOrders, -count);
	SetModifiedFlag();
}

SELECT_STATE CSoundEditorDoc::CheckSelectEnvArea(const CPoint &pt)//世界坐标
{
	Nuclear::CPOINT point;
	if (m_pSelectedEnvArea)
	{
		m_nSelectedPt = m_pSelectedEnvArea->CheckClickPoint(pt);
		if (m_nSelectedPt >= 0)
			return SS_SELECTED_AREA_POINT;
		point = m_pSelectedEnvArea->GetPos();
		CRect rect(point.x - 19, point.y - 64, point.x + 19, point.y);
		if (rect.PtInRect(pt))
			return SS_SELECTED;
		m_nSelectedPt = m_pSelectedEnvArea->CheckClickLinePoint(pt);
		if (m_nSelectedPt >= 0)
			return SS_SELECTED_AREA_LINE;		
	} else {
		m_nSelectedPt = -1;
	}
	EnvAreaCont::iterator eIt = m_envAreas.begin(), eIe = m_envAreas.end();
	for (;eIt!=eIe;++eIt)
	{
		point = (*eIt)->GetPos();
		CRect rect(point.x - 19, point.y - 64, point.x + 19, point.y);
		if (rect.PtInRect(pt))
		{
			m_pSelectedEnvArea = *eIt;
			return SS_NEWSELECTED;
		}
	}
	m_pSelectedEnvArea = NULL;
	return SS_ENPTY;
}

SELECT_STATE CSoundEditorDoc::CheckSelectBkgArea(const CPoint &pt)//世界坐标
{
	Nuclear::CPOINT point;
	if (m_pSelectedBkgArea)
	{
		m_nSelectedPt = m_pSelectedBkgArea->CheckClickPoint(pt);
		if (m_nSelectedPt >= 0)
			return SS_SELECTED_AREA_POINT;
		point = m_pSelectedBkgArea->GetPos();
		CRect rect(point.x - 19, point.y - 64, point.x + 19, point.y);
		if (rect.PtInRect(pt))
			return SS_SELECTED;
		m_nSelectedPt = m_pSelectedBkgArea->CheckClickLinePoint(pt);
		if (m_nSelectedPt >= 0)
			return SS_SELECTED_AREA_LINE;
	} else {
		m_nSelectedPt = -1;
	}
	BkgAreaCont::iterator eIt = m_bkgAreas.begin(), eIe = m_bkgAreas.end();
	for (;eIt!=eIe;++eIt)
	{
		point = (*eIt)->GetPos();
		CRect rect(point.x - 19, point.y - 64, point.x + 19, point.y);
		if (rect.PtInRect(pt))
		{
			m_pSelectedBkgArea = *eIt;
			return SS_NEWSELECTED;
		}
	}
	m_pSelectedBkgArea = NULL;
	return SS_ENPTY;
}

SELECT_STATE CSoundEditorDoc::CheckSelectBkgVolArea(const CPoint &pt)//世界坐标
{
	Nuclear::CPOINT point;
	if (m_pSelectedBkgVolArea)
	{
		m_nSelectedPt = m_pSelectedBkgVolArea->CheckClickPoint(pt);
		if (m_nSelectedPt >= 0)
			return SS_SELECTED_AREA_POINT;
		point = m_pSelectedBkgVolArea->GetPos();
		CRect rect(point.x - 19, point.y - 64, point.x + 19, point.y);
		if (rect.PtInRect(pt))
			return SS_SELECTED;
		m_nSelectedPt = m_pSelectedBkgVolArea->CheckClickLinePoint(pt);
		if (m_nSelectedPt >= 0)
			return SS_SELECTED_AREA_LINE;
	} else {
		m_nSelectedPt = -1;
	}
	BkgVolAreaCont::iterator eIt = m_bkgVolAreas.begin(), eIe = m_bkgVolAreas.end();
	for (;eIt!=eIe;++eIt)
	{
		point = (*eIt)->GetPos();
		CRect rect(point.x - 19, point.y - 64, point.x + 19, point.y);
		if (rect.PtInRect(pt))
		{
			m_pSelectedBkgVolArea = *eIt;
			return SS_NEWSELECTED;
		}
	}
	m_pSelectedBkgVolArea = NULL;
	return SS_ENPTY;
}

void CSoundEditorDoc::MoveSelectedArea()
{
	CToolsSoundArea* pArea = NULL;
	switch(m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		pArea = m_pSelectedEnvArea;
		break;
	case EDIT_STATE_BKG_MUSIC:
		pArea = m_pSelectedBkgArea;
		break;
	case EDIT_STATE_BKG_VOL:
		pArea = m_pSelectedBkgVolArea;
		break;
	default:
		break;
	}
	if (!pArea)
		return;
	if ((m_movingSelectArea.x == 0) && (m_movingSelectArea.y == 0))
		return;
	MovArea* pOper = new MovArea(pArea);
	pOper->vet = m_movingSelectArea;
	pOper->state = m_editState;
	m_pOperatorList->DoOneAction(pOper);

	pArea->Move(m_movingSelectArea);
	m_movingSelectArea.x = 0;
	m_movingSelectArea.y = 0;
	pArea->ResetAreaRegion(m_mapSize);
	SetModifiedFlag();
}

void CSoundEditorDoc::SetSelectedAreaTransition(int nTransition)
{
	switch(m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			if (!m_pSelectedEnvArea)
				return;
			int oldTrans = m_pSelectedEnvArea->GetTransition();
			if (oldTrans != nTransition)
			{
				EnvAreaTransition *pOper = new EnvAreaTransition(m_pSelectedEnvArea);
				pOper->trans[CAction::BEFORE_ACTION] = oldTrans;
				pOper->trans[CAction::AFTER_ACTION] = nTransition;
				m_pOperatorList->DoOneAction(pOper);

				m_pSelectedEnvArea->SetTransition(nTransition);
				ResetAreaRegion(m_pSelectedEnvArea);
				SetModifiedFlag();
				m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SHAPE;
				UpdateAllViews(NULL);
				m_nUpdateViewSign = 0;
			}
		}
		break;
	case EDIT_STATE_BKG_VOL:
		{
			if (!m_pSelectedBkgVolArea)
				return;
			int oldTrans = m_pSelectedBkgVolArea->GetTransition();
			if (oldTrans != nTransition)
			{
				BkvAreaTransition *pOper = new BkvAreaTransition(m_pSelectedBkgVolArea);
				pOper->trans[CAction::BEFORE_ACTION] = oldTrans;
				pOper->trans[CAction::AFTER_ACTION] = nTransition;
				m_pOperatorList->DoOneAction(pOper);
				m_pSelectedBkgVolArea->SetTransition(nTransition);
				ResetAreaRegion(m_pSelectedBkgVolArea);
				SetModifiedFlag();
				m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SHAPE;
				UpdateAllViews(NULL);
				m_nUpdateViewSign = 0;
			}
		}
		break;
	default:
		break;
	}
	
}

void CSoundEditorDoc::DeleteSelectedPolygonAreaPoint(int pt)
{
	switch(m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			CToolsEnvSoundPolygonArea* pArea = dynamic_cast<CToolsEnvSoundPolygonArea*>(m_pSelectedEnvArea);
			if (!pArea)
				return;
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = m_editState;
			XPASSERT(((int)pArea->m_innerPolygon.m_points.size()) > pt);
			Nuclear::CPOINT point = pArea->m_innerPolygon.m_points[pt];
			pArea->m_innerPolygon.m_points.erase(pArea->m_innerPolygon.m_points.begin() + pt);
			//m_ToolsCutPairs里面比pt大的点-1，如果等于pt，那么删除这个节点
			CToolsSoundArea::CutPairCont::iterator it = pArea->m_ToolsCutPairs.begin(), ie = pArea->m_ToolsCutPairs.end();
			for (;it!=ie;)
			{
				if (it->x == pt || it->y == pt)
					it = pArea->m_ToolsCutPairs.erase(it);
				else
				{
					if (it->x > pt)
						--(it->x);
					if (it->y > pt)
						--(it->y);
					++it;
				}
			}
			pArea->ResetOutter();
			pArea->ResetToolsPolygon(pArea->m_innerPolygon, pArea->m_ToolsInnerPolygons);
			pArea->ResetAreaRegion(m_mapSize);

			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	case EDIT_STATE_BKG_MUSIC:
		{
			CToolsBkgSoundPolygonArea* pArea = dynamic_cast<CToolsBkgSoundPolygonArea*>(m_pSelectedBkgArea);
			if (!pArea)
				return;
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_polygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = m_editState;
			XPASSERT(((int)pArea->m_polygon.m_points.size()) > pt);
			Nuclear::CPOINT point = pArea->m_polygon.m_points[pt];
			pArea->m_polygon.m_points.erase(pArea->m_polygon.m_points.begin() + pt);
			//m_ToolsCutPairs里面比pt大的点-1，如果等于pt，那么删除这个节点
			CToolsSoundArea::CutPairCont::iterator it = pArea->m_ToolsCutPairs.begin(), ie = pArea->m_ToolsCutPairs.end();
			for (;it!=ie;)
			{
				if (it->x == pt || it->y == pt)
					it = pArea->m_ToolsCutPairs.erase(it);
				else
				{
					if (it->x > pt)
						--(it->x);
					if (it->y > pt)
						--(it->y);
					++it;
				}
			}
			pArea->ResetToolsPolygon();
			pArea->ResetAreaRegion(m_mapSize);

			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_polygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	case EDIT_STATE_BKG_VOL:
		{
			CToolsBkgVolPolygonArea* pArea = dynamic_cast<CToolsBkgVolPolygonArea*>(m_pSelectedBkgVolArea);
			if (!pArea)
				return;
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = m_editState;
			XPASSERT(((int)pArea->m_innerPolygon.m_points.size()) > pt);
			Nuclear::CPOINT point = pArea->m_innerPolygon.m_points[pt];
			pArea->m_innerPolygon.m_points.erase(pArea->m_innerPolygon.m_points.begin() + pt);
			//m_ToolsCutPairs里面比pt大的点-1，如果等于pt，那么删除这个节点
			CToolsSoundArea::CutPairCont::iterator it = pArea->m_ToolsCutPairs.begin(), ie = pArea->m_ToolsCutPairs.end();
			for (;it!=ie;)
			{
				if (it->x == pt || it->y == pt)
					it = pArea->m_ToolsCutPairs.erase(it);
				else
				{
					if (it->x > pt)
						--(it->x);
					if (it->y > pt)
						--(it->y);
					++it;
				}
			}
			pArea->ResetOutter();
			pArea->ResetToolsPolygon(pArea->m_innerPolygon, pArea->m_ToolsInnerPolygons);
			pArea->ResetAreaRegion(m_mapSize);

			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	default:
		return;
		break;
	}
	SetModifiedFlag();
}

void CSoundEditorDoc::MoveSelectedPolygonAreaPointTo(const POINT &pt)
{
	CToolsSoundArea* pToolsArea = NULL;
	switch (m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			pToolsArea = m_pSelectedEnvArea;
			CToolsEnvSoundPolygonArea* pArea = dynamic_cast<CToolsEnvSoundPolygonArea*>(pToolsArea);
			XPASSERT(pArea);
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = m_editState;
			pArea->m_innerPolygon.m_points[m_nSelectedPt] = pt;
			pArea->ResetOutter();
			pArea->ResetToolsPolygon(pArea->m_innerPolygon, pArea->m_ToolsInnerPolygons);
			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	case EDIT_STATE_BKG_MUSIC:
		{
			pToolsArea = m_pSelectedBkgArea;
			CToolsBkgSoundPolygonArea* pArea = dynamic_cast<CToolsBkgSoundPolygonArea*>(pToolsArea);
			XPASSERT(pArea);
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_polygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = m_editState;
			pArea->m_polygon.m_points[m_nSelectedPt] = pt;
			pArea->ResetToolsPolygon(pArea->m_polygon, pArea->m_ToolsPolygons);
			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_polygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	case EDIT_STATE_BKG_VOL:
		{
			pToolsArea = m_pSelectedBkgVolArea;
			CToolsBkgVolPolygonArea* pArea = dynamic_cast<CToolsBkgVolPolygonArea*>(pToolsArea);
			XPASSERT(pArea);
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = m_editState;
			pArea->m_innerPolygon.m_points[m_nSelectedPt] = pt;
			pArea->ResetOutter();
			pArea->ResetToolsPolygon(pArea->m_innerPolygon, pArea->m_ToolsInnerPolygons);
			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	default:
		XPASSERT(false);
		break;
	}
	
	pToolsArea->ResetAreaRegion(m_mapSize);
	SetModifiedFlag();
}

void CSoundEditorDoc::AddSelectedEnvPolygonPoint(const POINT &pt)
{
	CToolsEnvSoundPolygonArea* pArea = dynamic_cast<CToolsEnvSoundPolygonArea*>(m_pSelectedEnvArea);
	XPASSERT(pArea);
	PolygonModify* pOper = new PolygonModify(pArea);
	pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_innerPolygon;
	pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
	pOper->state = EDIT_STATE_ENV_SOUND;
	if (m_nSelectedPt >= 0)
	{
		if (m_nSelectedPt == 0)
			m_nSelectedPt = static_cast<int>(pArea->m_innerPolygon.m_points.size());
		std::vector<Nuclear::CPOINT>::iterator iter = pArea->m_innerPolygon.m_points.begin() + m_nSelectedPt;
		pArea->m_innerPolygon.m_points.insert(iter, pt);
	} else {
		pArea->m_innerPolygon.m_points.push_back(pt);
		m_nSelectedPt = static_cast<int>(pArea->m_innerPolygon.m_points.size() - 1);
	}
	pArea->ResetOutter();
	pArea->ResetToolsPolygon(pArea->m_innerPolygon, pArea->m_ToolsInnerPolygons);
	pArea->ResetAreaRegion(m_mapSize);
	pOper->polygon[CAction::AFTER_ACTION] = pArea->m_innerPolygon;
	pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
	m_pOperatorList->DoOneAction(pOper);
	SetModifiedFlag();
}

void CSoundEditorDoc::AddSelectedBkgPolygonPoint(const POINT &pt)
{
	CToolsBkgSoundPolygonArea* pArea = dynamic_cast<CToolsBkgSoundPolygonArea*>(m_pSelectedBkgArea);
	XPASSERT(pArea);
	PolygonModify* pOper = new PolygonModify(pArea);
	pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_polygon;
	pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
	pOper->state = EDIT_STATE_BKG_MUSIC;
	if (m_nSelectedPt >= 0)
	{
		if (m_nSelectedPt == 0)
			m_nSelectedPt = static_cast<int>(pArea->m_polygon.m_points.size());
		std::vector<Nuclear::CPOINT>::iterator iter = pArea->m_polygon.m_points.begin() + m_nSelectedPt;
		pArea->m_polygon.m_points.insert(iter, pt);
	} else {
		pArea->m_polygon.m_points.push_back(pt);
		m_nSelectedPt = static_cast<int>(pArea->m_polygon.m_points.size() - 1);
	}
	pArea->ResetToolsPolygon(pArea->m_polygon, pArea->m_ToolsPolygons);
	pArea->ResetAreaRegion(m_mapSize);
	pOper->polygon[CAction::AFTER_ACTION] = pArea->m_polygon;
	pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
	m_pOperatorList->DoOneAction(pOper);
	SetModifiedFlag();
}

void CSoundEditorDoc::AddSelectedBkgVolPolygonPoint(const POINT &pt)
{
	CToolsBkgVolPolygonArea* pArea = dynamic_cast<CToolsBkgVolPolygonArea*>(m_pSelectedBkgVolArea);
	XPASSERT(pArea);
	PolygonModify* pOper = new PolygonModify(pArea);
	pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_innerPolygon;
	pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
	pOper->state = EDIT_STATE_BKG_VOL;
	if (m_nSelectedPt >= 0)
	{
		if (m_nSelectedPt == 0)
			m_nSelectedPt = static_cast<int>(pArea->m_innerPolygon.m_points.size());
		std::vector<Nuclear::CPOINT>::iterator iter = pArea->m_innerPolygon.m_points.begin() + m_nSelectedPt;
		pArea->m_innerPolygon.m_points.insert(iter, pt);
	} else {
		pArea->m_innerPolygon.m_points.push_back(pt);
		m_nSelectedPt = static_cast<int>(pArea->m_innerPolygon.m_points.size() - 1);
	}
	pArea->ResetOutter();
	pArea->ResetToolsPolygon(pArea->m_innerPolygon, pArea->m_ToolsInnerPolygons);
	pArea->ResetAreaRegion(m_mapSize);
	pOper->polygon[CAction::AFTER_ACTION] = pArea->m_innerPolygon;
	pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
	m_pOperatorList->DoOneAction(pOper);
	SetModifiedFlag();
}


void CSoundEditorDoc::CutSelectedPolygon(int pt1, int pt2)
{
	switch(m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			CToolsEnvSoundPolygonArea* pArea = dynamic_cast<CToolsEnvSoundPolygonArea*>(m_pSelectedEnvArea);
			XPASSERT(pArea);
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = EDIT_STATE_ENV_SOUND;
			pArea->m_ToolsCutPairs.push_back(Nuclear::CPOINT(pt1, pt2));
			pArea->ResetToolsPolygon();
			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	case EDIT_STATE_BKG_MUSIC:
		{
			CToolsBkgSoundPolygonArea* pArea = dynamic_cast<CToolsBkgSoundPolygonArea*>(m_pSelectedBkgArea);
			XPASSERT(pArea);
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_polygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = EDIT_STATE_BKG_MUSIC;
			pArea->m_ToolsCutPairs.push_back(Nuclear::CPOINT(pt1, pt2));
			pArea->ResetToolsPolygon();
			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_polygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	case EDIT_STATE_BKG_VOL:
		{
			CToolsBkgVolPolygonArea* pArea = dynamic_cast<CToolsBkgVolPolygonArea*>(m_pSelectedBkgVolArea);
			XPASSERT(pArea);
			PolygonModify* pOper = new PolygonModify(pArea);
			pOper->polygon[CAction::BEFORE_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::BEFORE_ACTION] = pArea->m_ToolsCutPairs;
			pOper->state = EDIT_STATE_ENV_SOUND;
			pArea->m_ToolsCutPairs.push_back(Nuclear::CPOINT(pt1, pt2));
			pArea->ResetToolsPolygon();
			pOper->polygon[CAction::AFTER_ACTION] = pArea->m_innerPolygon;
			pOper->cutpair[CAction::AFTER_ACTION] = pArea->m_ToolsCutPairs;
			m_pOperatorList->DoOneAction(pOper);
		}
		break;
	default:
		XPASSERT(false);
	}
	
	SetModifiedFlag();
}

void CSoundEditorDoc::SetSelectedCircleAreaRadius(int r)
{
	switch(m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			CToolsEnvSoundCircleArea* pArea = dynamic_cast<CToolsEnvSoundCircleArea*>(m_pSelectedEnvArea);
			XPASSERT(pArea);
			CircleModify* pOper = new CircleModify(pArea);
			pOper->radius[CAction::BEFORE_ACTION] = pArea->m_circle.m_nRadius;
			pOper->state = EDIT_STATE_ENV_SOUND;
			pOper->radius[CAction::AFTER_ACTION] = r;
			m_pOperatorList->DoOneAction(pOper);
			pArea->m_circle.m_nRadius = r;
			pArea->ResetShape();
			m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SHAPE;
			ResetAreaRegion(pArea);
		}
		break;
	case EDIT_STATE_BKG_MUSIC:
		{
			CToolsBkgSoundCircleArea* pArea = dynamic_cast<CToolsBkgSoundCircleArea*>(m_pSelectedBkgArea);
			XPASSERT(pArea);
			CircleModify* pOper = new CircleModify(pArea);
			pOper->radius[CAction::BEFORE_ACTION] = pArea->m_circle.m_nRadius;
			pOper->state = EDIT_STATE_BKG_MUSIC;
			pOper->radius[CAction::AFTER_ACTION] = r;
			m_pOperatorList->DoOneAction(pOper);
			pArea->m_circle.m_nRadius = r;
			m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SHAPE;
			ResetAreaRegion(pArea);
		}
		break;
	case EDIT_STATE_BKG_VOL:
		{
			CToolsBkgVolCircleArea* pArea = dynamic_cast<CToolsBkgVolCircleArea*>(m_pSelectedBkgVolArea);
			XPASSERT(pArea);
			CircleModify* pOper = new CircleModify(pArea);
			pOper->radius[CAction::BEFORE_ACTION] = pArea->m_circle.m_nRadius;
			pOper->state = EDIT_STATE_BKG_VOL;
			pOper->radius[CAction::AFTER_ACTION] = r;
			m_pOperatorList->DoOneAction(pOper);
			pArea->m_circle.m_nRadius = r;
			pArea->ResetShape();
			m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SHAPE;
			ResetAreaRegion(pArea);
		}
		break;
	default:
		XPASSERT(false);
		return;
	}
	UpdateAllViews(NULL);
	m_nUpdateViewSign = 0;
	SetModifiedFlag();
}

void CSoundEditorDoc::SetSelectedBkgVolAreaVol(float v)
{
	if (!m_pSelectedBkgVolArea)
		return;
	float oldVol = m_pSelectedBkgVolArea->GetVol();
	if (oldVol == v)
		return;
	BkvAreaVolume *pOper = new BkvAreaVolume(m_pSelectedBkgVolArea);
	pOper->vol[CAction::BEFORE_ACTION] = oldVol;
	pOper->vol[CAction::AFTER_ACTION] = v;
	m_pOperatorList->DoOneAction(pOper);
	m_pSelectedBkgVolArea->SetVol(v);	
}

void CSoundEditorDoc::ResetAreaRegion(CToolsSoundArea* pArea)
{
	pArea->ResetAreaRegion(m_mapSize);
	SetModifiedFlag();
}

void CSoundEditorDoc::SelectAndMoveToAnEnvArea(DWORD_PTR handle)
{
	CToolsEnvSoundArea* pArea = m_envSoundMap[handle];
	if (!pArea)
		return;
	SelectAndMoveToAnArea(pArea);
}

void CSoundEditorDoc::AddAnEnvArea(CToolsEnvSoundArea* area)
{
	area->nSoundListBeginID = m_nEnvSoundCount;
	area->nSoundListEndID = m_nEnvSoundCount;
	area->nOrders = m_nEnvOrdersSeed++; 
	ResetAreaRegion(area);
	m_envAreas.insert(area); 
	SetModifiedFlag(); 
	AddDelEnvArea* pOper = new AddDelEnvArea(area);
	pOper->isAdd = true;
	m_pOperatorList->DoOneAction(pOper);
}

void CSoundEditorDoc::AddAnBkgArea(CToolsBkgSoundArea* area) 
{ 
	area->nSoundListBeginID = m_nBkgSoundCount;
	area->nSoundListEndID = m_nBkgSoundCount;
	area->nOrders = m_nBkgOrdersSeed++;
	ResetAreaRegion(area);
	m_bkgAreas.insert(area); 
	SetModifiedFlag(); 
	AddDelBkgArea* pOper = new AddDelBkgArea(area);
	pOper->isAdd = true;
	m_pOperatorList->DoOneAction(pOper);
}

void CSoundEditorDoc::AddAnBkgVolArea(CToolsBkgVolumeArea* area) 
{ 
	ResetAreaRegion(area);
	m_bkgVolAreas.insert(area); 
	SetModifiedFlag(); 
	AddDelBkgVolArea* pOper = new AddDelBkgVolArea(area);
	pOper->isAdd = true;
	m_pOperatorList->DoOneAction(pOper);
}

void CSoundEditorDoc::SelectAndMoveToAnArea(CToolsEnvSoundArea* pArea)
{
	assert(pArea);
	if (m_pSelectedEnvArea != pArea)
	{
		m_pSelectedEnvArea = pArea;
		m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED;	
		Nuclear::CPOINT pt = pArea->GetPos();
		pt.x -= 512;
		pt.y -= 384;
		LegitimateViewportLt(pt);
		if (m_viewportlt != pt)
		{
			m_viewportlt = pt;
			m_us = US_PANEAL;
			m_nUpdateViewSign |= UVS_VIEWPORT;
		}
		UpdateAllViews(NULL);
		m_nUpdateViewSign = 0;
	}
}

void CSoundEditorDoc::SelectAndMoveToAnBkgArea(DWORD_PTR handle)
{
	CToolsBkgSoundArea* pArea = m_bkgSoundMap[handle];
	if (!pArea)
		return;
	SelectAndMoveToAnArea(pArea);
}

void CSoundEditorDoc::SelectAndMoveToAnArea(CToolsBkgSoundArea* pArea)
{
	assert(pArea);
	if (m_pSelectedBkgArea != pArea)
	{
		m_pSelectedBkgArea = pArea;
		m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SELECTED;	
		if (pArea->GetType() != Nuclear::XPAREA_TYPE_NULL)
		{
			Nuclear::CPOINT pt = pArea->GetPos();
			pt.x -= 512;
			pt.y -= 384;
			LegitimateViewportLt(pt);
			if (m_viewportlt != pt)
			{
				m_viewportlt = pt;
				m_us = US_PANEAL;
				m_nUpdateViewSign |= UVS_VIEWPORT;
			}
		}
		UpdateAllViews(NULL);
		m_nUpdateViewSign = 0;
	}
}

void CSoundEditorDoc::LegitimateViewportLt(POINT &pt)
{
	if (1024 < m_mapSize.x)
	{
		if (pt.x + 1024 > m_mapSize.x)
		{
			pt.x = m_mapSize.x - 1024;
		}
		if (pt.x < 0)
			pt.x = 0;
	} else {
		pt.x = 0;
	}

	if (768 < m_mapSize.y)
	{
		if (pt.y + 768 > m_mapSize.y)
		{
			pt.y = m_mapSize.y - 768;
		}
		if (pt.y < 0)
			pt.y = 0;
	} else {
		pt.y = 0;
	}
}

void CSoundEditorDoc::UpdateViewport(POINT pt, UPDATE_SOURCE us)
{
	if ((m_editState != EDIT_STATE_TESTING) || (us == US_ENGINE))
	{
		LegitimateViewportLt(pt);
		if (m_viewportlt == pt)
			return;
		m_viewportlt = pt;
		m_us = us;
		m_nUpdateViewSign = UVS_VIEWPORT;
		UpdateAllViews(NULL);
		m_nUpdateViewSign = 0;
	} else {
		m_viewportlt = pt;
		m_us = us;
		m_nUpdateViewSign = UVS_TEST_SET_POS;
		UpdateAllViews(NULL);
		m_nUpdateViewSign = 0;
	}
}
void CSoundEditorDoc::OnEditRedo()
{
	m_pOperatorList->RedoOneAction();
}

void CSoundEditorDoc::OnUpdateEditRedo(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(m_pOperatorList->CanRedo());
}

void CSoundEditorDoc::OnEditUndo()
{
	m_pOperatorList->UndoOneAction();
}

void CSoundEditorDoc::OnUpdateEditUndo(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(m_pOperatorList->CanUndo());
}

void CSoundEditorDoc::SetModel(const std::wstring &name)
{
	m_modelname = name;
	Nuclear::SpriteManager* pSprMan = m_pEB->GetSpriteManager();
	int layerCount = pSprMan->GetLayerCount(name);
	XPASSERT(layerCount<8);
	for(int i=0;i<layerCount;++i)
	{
		std::vector<std::wstring> compos;
		pSprMan->DumpComponent(name, i, compos);
		m_component[i] = compos.size()>0?compos[0]:L"";
	}
	std::vector<std::wstring> actlist;
	pSprMan->DumpAction(name, actlist);
	if (actlist.size() > 0)
	{
		m_actRunLeft = actlist[0];
		m_actRunRight = actlist[0];
		m_actStand = actlist[0];
	}
	m_rideName = L"";
	m_nUpdateViewSign = UVS_TEST_SET_SPRITE;
	UpdateAllViews(NULL);
	m_nUpdateViewSign = 0;
}

const std::wstring& CSoundEditorDoc::GetComponent(int ec) const
{
	XPASSERT(ec < 8);
	XPASSERT(ec >= 0);
	return m_component[ec];
}

void CSoundEditorDoc::SetComponent(int ec, const std::wstring &name)
{
	XPASSERT(ec < 8);
	XPASSERT(ec >= 0);
	m_component[ec] = name;
	m_nUpdateViewSign = UVS_TEST_SET_SPRITE;
	UpdateAllViews(NULL);
	m_nUpdateViewSign = 0;
}

void CSoundEditorDoc::SetRunLeftAct(const std::wstring &name) 
{ 
	m_actRunLeft = name; 
	m_nUpdateViewSign = UVS_TEST_SET_SPRITE;
	UpdateAllViews(NULL);
	m_nUpdateViewSign = 0;
}

void CSoundEditorDoc::SetRunRightAct(const std::wstring &name) 
{ 
	m_actRunRight = name; 
	m_nUpdateViewSign = UVS_TEST_SET_SPRITE;
	UpdateAllViews(NULL);
	m_nUpdateViewSign = 0;
}

void CSoundEditorDoc::SetStandAct(const std::wstring &name) 
{
	m_actStand = name; 
	m_nUpdateViewSign = UVS_TEST_SET_SPRITE;
	UpdateAllViews(NULL);
	m_nUpdateViewSign = 0;
}

void CSoundEditorDoc::SetRidename(const std::wstring &name)
{
	m_rideName = name; 
	m_nUpdateViewSign = UVS_TEST_SET_SPRITE;
	UpdateAllViews(NULL);
	m_nUpdateViewSign = 0;
}

int CSoundEditorDoc::GetCurSpriteLayerCount() const
{
	return m_pEB->GetSpriteManager()->GetLayerCount(m_modelname);
}

void CSoundEditorDoc::ResetSetMazeName()
{
	wchar_t tempStr[200];
	GetPrivateProfileString(L"MazeData", L"Format", L"", tempStr, 200, theApp.GetCfgFilePath());
	CString mazename = tempStr;
	mazename.Replace(L"%MAP_NAME%", m_mapName.c_str());
	GetPrivateProfileString(L"MazeMaps", m_mapName.c_str(), L"", tempStr, 200, theApp.GetCfgFilePath());
	mazename.Replace(L"%MAP_ID%", tempStr);
	m_mazeName = mazename.IsEmpty()?L"errormaze":mazename;
}

void CSoundEditorDoc::SetPEB(Nuclear::EngineBase* pEB) 
{
	m_pEB = pEB;
	LoadSpriteCfg();
}

void CSoundEditorDoc::LoadSpriteCfg()
{
	if (!m_pEB)
		return;
	wchar_t tmpProStr[500];
	GetPrivateProfileString(L"Sprite", L"Model", L"male", tmpProStr, 500, theApp.GetCfgFilePath());
	m_modelname = tmpProStr;

	std::map<std::wstring, std::wstring> compMap;
	GetPrivateProfileString(L"Sprite", L"Component", L"body:10jnanbupao,head:bangpaidizinantou1,weapon:10jdnandao", tmpProStr, 500, theApp.GetCfgFilePath());
	std::wstring componet = tmpProStr;
	std::wstring::size_type layer = 0, layer2 = 0, comp = 0;
	while (1)
	{
		comp = componet.find_first_of(L':', layer);
		if (comp == std::wstring::npos)
			break;
		layer2 = componet.find_first_of(L',', comp);
		if (layer2 == std::wstring::npos)
			layer2 = componet.size();
		compMap[componet.substr(layer, comp-layer)] = componet.substr(comp+1, layer2-comp-1);
		layer = layer2 + 1;
	}

	Nuclear::SpriteManager *pSM = m_pEB->GetSpriteManager();
	int layerCount = pSM->GetLayerCount(m_modelname);
	std::map<std::wstring, std::wstring>::iterator comit;
	for (int i=0;i<layerCount;++i)
	{
		comit = compMap.find(pSM->GetLayerName(m_modelname, i));
		if (comit != compMap.end())
		{
			m_component[i] = comit->second;
		} else {
			m_component[i] = L"";
		}
	}
	GetPrivateProfileString(L"Sprite", L"RideName", L"", tmpProStr, 500, theApp.GetCfgFilePath());
	m_rideName = tmpProStr;
	GetPrivateProfileString(L"Sprite", L"RunLeft", L"runleft", tmpProStr, 500, theApp.GetCfgFilePath());
	m_actRunLeft = tmpProStr;
	GetPrivateProfileString(L"Sprite", L"RunRight", L"runright", tmpProStr, 500, theApp.GetCfgFilePath());
	m_actRunRight = tmpProStr;
	GetPrivateProfileString(L"Sprite", L"Stand", L"stand1", tmpProStr, 500, theApp.GetCfgFilePath());
	m_actStand = tmpProStr;
}

void CSoundEditorDoc::SaveSpriteCfg()
{
	WritePrivateProfileString(L"Sprite", L"Model", m_modelname.c_str(), theApp.GetCfgFilePath());
	std::wstring component;
	component.reserve(500);
	Nuclear::SpriteManager *pSM = m_pEB->GetSpriteManager();
	int layerCount = pSM->GetLayerCount(m_modelname);
	for (int i=0;i<layerCount;++i)
	{
		if (m_component[i] != L"")
		{
			if (component.size() > 0)
				component.append(L",");
			component.append(pSM->GetLayerName(m_modelname, i));
			component.append(L":");
			component.append(m_component[i]);
		}
	}
	WritePrivateProfileString(L"Sprite", L"RideName", m_rideName.c_str(), theApp.GetCfgFilePath());
	WritePrivateProfileString(L"Sprite", L"Component", component.c_str(), theApp.GetCfgFilePath());
	WritePrivateProfileString(L"Sprite", L"RunLeft", m_actRunLeft.c_str(), theApp.GetCfgFilePath());
	WritePrivateProfileString(L"Sprite", L"RunRight", m_actRunRight.c_str(), theApp.GetCfgFilePath());
	WritePrivateProfileString(L"Sprite", L"Stand", m_actStand.c_str(), theApp.GetCfgFilePath());
}

bool CSoundEditorDoc::SaveSSM()
{
	if (m_bSSMModified && m_pSSM)
	{
		if (!m_pSSM->SaveToNativePath(m_strSSMURL))
			return false;
		m_bSSMModified = false;
	}
	return true;
}

bool CSoundEditorDoc::LoadSSM()
{
	m_bSSMModified = false;
	if (!m_pSSM)
		m_pSSM = new Nuclear::PStepSoundMap();
	m_pSSM->LoadFromNativePath(m_strSSMURL);
	return true;
}

void CSoundEditorDoc::Reload()
{
	if (m_strPathName != L"")
	{
		m_nUpdateViewSign = 0;
		CString fullname = m_strPathName;
		OnOpenDocument(fullname);
		m_nUpdateViewSign = UVS_ALL;
		UpdateAllViews(NULL);
		m_nUpdateViewSign = 0;
		SetModifiedFlag(FALSE);
	}
}

void CSoundEditorDoc::SetMapSize(const Nuclear::CPOINT &size)
{ 
	m_mapSize = size; 
	if (m_pSSM->GetData() == NULL)
	{
		//地图大小肯定分别是GRID_WIDTH和GRID_HEIGHT的整数倍，这个由小地表决定了
		m_pSSM->Init(m_mapSize.x / Nuclear::PStepSoundMap::GRID_WIDTH, m_mapSize.y / Nuclear::PStepSoundMap::GRID_HEIGHT);
	}
}

Nuclear::PStepSoundMap *CSoundEditorDoc::CloneSSM() const
{
	if (!m_pSSM)
		return NULL;
	return m_pSSM->Clone();
}

BOOL CSoundEditorDoc::SaveModified()
{
	// TODO: Add your specialized code here and/or call the base class
	if (m_bSSMModified)
	{
		CString prompt;
		AfxFormatString1(prompt, AFX_IDP_ASK_TO_SAVE, L"ssm.ssm");
		switch (AfxMessageBox(prompt, MB_YESNOCANCEL, AFX_IDP_ASK_TO_SAVE))
		{
		case IDCANCEL:
			return FALSE;       // don't continue

		case IDYES:
			// If so, either Save or Update, as appropriate
			if (!SaveSSM())
				return FALSE;       // don't continue
			break;

		case IDNO:
			// If not saving changes, revert the document
			m_bSSMModified = false;
			break;

		default:
			XPASSERT(FALSE);
			break;
		}
	}
	
	return CDocument::SaveModified();
}

bool CSoundEditorDoc::CanCopy()
{
	switch (m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		if (m_pSelectedEnvArea)
			return true;
		break;
	case EDIT_STATE_BKG_MUSIC:
		/*if (m_pSelectedBkgArea)
			return true;*/
		break;
	default:
		return false;
	}
	return false;
}

void CSoundEditorDoc::OnEditCopy()
{
	if (!CanCopy())
		return;
	switch (m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		if (m_pCopiedEnvArea)
			delete m_pCopiedEnvArea;
		m_pCopiedEnvArea = m_pSelectedEnvArea->Clone();
		break;
	case EDIT_STATE_BKG_MUSIC:
		if (m_pCopiedBkgArea)
			delete m_pCopiedBkgArea;
		//m_pCopiedBkgArea = m_pSelectedBkgArea->Clone();
		break;
	}
}

void CSoundEditorDoc::OnUpdateEditCopy(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(CanCopy());
}

void CSoundEditorDoc::OnEditCut()
{
	OnEditCopy();
	DeleteSelectedArea();
}

void CSoundEditorDoc::OnUpdateEditCut(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(CanCopy());
}

void CSoundEditorDoc::OnEditPaste()
{
	switch (m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		if (m_pCopiedEnvArea)
		{
			CToolsEnvSoundArea* pEsa = m_pCopiedEnvArea->Clone();
			pEsa->nOrders = m_nEnvOrdersSeed++;
			pEsa->nSoundListBeginID = m_nEnvSoundCount;
			int typeCount = pEsa->GetTypeCount();
			pEsa->soundHandleVet.resize(typeCount);
			for (int i = 0;i<typeCount;++i)
			{
				std::vector<std::wstring> slist;
				pEsa->GetSoundList(i, slist);
				m_nEnvSoundCount += (int)slist.size();
				pEsa->soundHandleVet[i].resize(slist.size());
				std::vector<std::wstring>::iterator sit = slist.begin(), sie = slist.end();
				int soundid = 0;
				for (;sit!=sie;++sit,++soundid)
				{
					pEsa->soundHandleVet[i][soundid] = ++m_nHandleSeed;
					m_envSoundMap[m_nHandleSeed] = pEsa;
				}
			}
			pEsa->nSoundListEndID = m_nEnvSoundCount;
			POINT pt;
			pt.x = m_viewportlt.x - pEsa->GetPos().x + 400;
			pt.y = m_viewportlt.y - pEsa->GetPos().y + 300;
			pEsa->Move(pt);
			m_envAreas.insert(pEsa);
			m_pSelectedEnvArea = pEsa;
			AddDelEnvArea *pOper = new AddDelEnvArea(pEsa);
			pOper->isAdd = true;
			m_pCurOperator = pOper;
			m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED | UVS_ENV_SOUND_AREA_ADD_DELETED;
			UpdateAllViews(NULL);
			m_nUpdateViewSign = 0;
			m_pOperatorList->DoOneAction(pOper);
			m_pCurOperator = NULL;
			SetModifiedFlag();
			
		}
		break;
	case EDIT_STATE_BKG_MUSIC:
		if (m_pCopiedBkgArea)
		{
			
		}
		break;
	default:
		break;
	}
}

void CSoundEditorDoc::OnUpdateEditPaste(CCmdUI *pCmdUI)
{
	switch (m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
		if (m_pCopiedEnvArea)
		{
			pCmdUI->Enable(TRUE);
			return;
		}
		break;
	case EDIT_STATE_BKG_MUSIC:
		if (m_pCopiedBkgArea)
		{
			pCmdUI->Enable(TRUE);
			return;
		}
		break;
	default:
		break;
	}
	pCmdUI->Enable(FALSE);
}
