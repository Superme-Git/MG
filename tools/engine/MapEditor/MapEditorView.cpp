
#include "stdafx.h"
#include <afxole.h>

#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "RegionMapInfoDlg.h"
#include "ViewPropDlg.h"
#include "MainFrm.h"
#include "MapClipboardContentMaker.h"
#include "EditorRender.h"


#include "..\engine\common\pfsutil.h"
#include "..\engine\common\util.h"
#include "..\engine\common\log.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\sprite\componentsprite.h"
#include "..\engine\particlesystem\iparticlemanager.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

using Nuclear::SubMap::PtrTileTypeList;
using Nuclear::SubMap::PtrTileType;
using Nuclear::Renderer;
using Nuclear::XDisplayMode;


IMPLEMENT_DYNCREATE(CMapEditorView, CScrollView)

void CMapEditorView::OnRButtonDown(UINT nFlags, CPoint point)
{

	if (m_nLayerEditID == LEI_GAMING && m_pSelectedSprite)
	{
		CMapEditorDoc* pDoc = (GetDocument());
		m_pSelectedSprite->RunTo(static_cast<LONG>(point.x / m_fRatio) + m_DrawScrollPosition.x, static_cast<LONG>(point.y / m_fRatio) + m_DrawScrollPosition.y);
	}

	__super::OnRButtonDown(nFlags, point);
}
void CMapEditorView::OnMaskEdit()
{
	if (m_MaskEditorDlg.m_hWnd != NULL)
	{
		if (m_MaskEditorDlg.IsWindowVisible())
		{
			m_MaskEditorDlg.ShowWindow(SW_HIDE);
		} else {
			m_MaskEditorDlg.ShowWindow(SW_SHOW);
		}
	} else {
		m_MaskEditorDlg.Create(this);
		m_MaskEditorDlg.ShowWindow(SW_SHOW);
	}
}

void CMapEditorView::OnUpdateUpdateLinkedObjectEveryFrame(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bUpdateLinkedObjectsEveryFrame);
}

void CMapEditorView::OnUpdateLinkedObjectEveryFrame()
{
	m_bUpdateLinkedObjectsEveryFrame = !m_bUpdateLinkedObjectsEveryFrame;
}

void CMapEditorView::OnShowPredlg()
{
	ChangeEditLayer(LEI_GAMING);
	if (!m_pPerformanceDlg)
	{
		m_pPerformanceDlg = new CPerformanceDlg();
		m_pPerformanceDlg->Create(this);
		m_pPerformanceDlg->ShowWindow(SW_SHOW);
		m_nEditMode = EMT_PERFORMANCE_DLG;
	} else {
		if (m_pPerformanceDlg->m_hWnd)
		{
			if (m_pPerformanceDlg->IsWindowVisible())
			{
				m_nEditMode = EMT_GAMING_NORMAL;
				m_pPerformanceDlg->ShowWindow(SW_HIDE);
			} else {
				m_nEditMode = EMT_PERFORMANCE_DLG;
				m_pPerformanceDlg->ShowWindow(SW_SHOW);
			}
		}
	}
}

void CMapEditorView::OnUpdateTestLayerChunk(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(dynamic_cast<CEditorRender*>(m_pRenderer)->GetLayerChunkState());
}

void CMapEditorView::OnTestLayerChunk()
{
	CEditorRender* renderer = dynamic_cast<CEditorRender*>(m_pRenderer);
	if (renderer->GetLayerChunkState())
	{
		renderer->EnableLayerChunk(false);
	} else {
		if (!renderer->EnableLayerChunk(true))
		{
			renderer->LoadChunkPicture(L"/elements/layerblock.png");
			renderer->EnableLayerChunk(true);
		}
	}
}

void CMapEditorView::OnUpdateShowEffect(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bShowEffect);
}

void CMapEditorView::OnShowEffect()
{
	m_bShowEffect = !m_bShowEffect;
}

void CMapEditorView::OnEditResetAllObjs()
{
	GetDocument()->ResetAllObjectRects();
	GetDocument()->SetModifiedFlag();
	m_ScreenElements.ClearLayers();
}
void CMapEditorView::GetSelectedItemRect(Nuclear::CRECT &rect) const
{
	Nuclear::PMap &pmap = GetDocument()->GetToolsMap();
	rect.left = rect.right = rect.bottom = rect.top = 0;
	switch (m_nEditMode)
	{
	case EMT_OBJECT_NORMAL:
	case EMT_OBJECT_DRAG:
	case EMT_OBJECT_DRAG_RECT:
		{
			Nuclear::SortBaseIDSet::const_iterator iter = m_SelectedObjects.begin();
			if (iter == m_SelectedObjects.end())
				return;
			PMap::MAP_OBJ_TYPE type;
			if (!GetObjectType(*iter, type))
				return;
			unsigned char layerID = static_cast<unsigned char>(m_nLayerEditID - FIRST_ELEMENT_LAYER);
			rect = pmap.GetConstObjInfo(type, layerID, GetObjectId(*iter)).m_Rect;
			++iter;
			for (;iter!=m_SelectedObjects.end();++iter)
			{
				if (!GetObjectType(*iter, type))
					continue;
				const Nuclear::CRECT& tmpRect = pmap.GetConstObjInfo(type, layerID, GetObjectId(*iter)).m_Rect;
				rect.left = min(rect.left, tmpRect.left);
				rect.top = min(rect.top, tmpRect.top);
				rect.bottom = max(rect.bottom, tmpRect.bottom);
				rect.right = max(rect.right, tmpRect.right);
			}
			break;
		}
	default:
		break;
	}
}

unsigned int CMapEditorView::GetSelectedItemColor() const
{
	Nuclear::PMap &pmap = GetDocument()->GetToolsMap();
	unsigned int result = 0xFFFFFFFF;
	switch (m_nEditMode)
	{
	case EMT_OBJECT_NORMAL:
	case EMT_OBJECT_DRAG:
	case EMT_OBJECT_DRAG_RECT:
		{
			Nuclear::SortBaseIDSet::const_iterator iter = m_SelectedObjects.begin();
			if (iter == m_SelectedObjects.end())
				return result;
			PMap::MAP_OBJ_TYPE type;
			if (!GetObjectType(*iter, type))
				return result;
			unsigned char layerID = static_cast<unsigned char>(m_nLayerEditID - FIRST_ELEMENT_LAYER);
			result = pmap.GetConstObjInfo(type, layerID, GetObjectId(*iter)).m_AttachColor.data;
			++iter;
			for (;iter!=m_SelectedObjects.end();++iter)
			{
				if (!GetObjectType(*iter, type))
					continue;
				if (pmap.GetConstObjInfo(type, layerID, GetObjectId(*iter)).m_AttachColor.data != result)
				{
					return 0xFFFFFFFF;
				}
			}
			break;
		}
	default:
		break;
	}
	return result;
}

void CMapEditorView::SetPlayingTime(bool bIsPlayingTime) 
{ 
	m_bPlayingTime = bIsPlayingTime; 
	m_MaskEditorDlg.SetPlay(bIsPlayingTime);
	m_TimeEffectEditorDlg.SetPlay(bIsPlayingTime);
}

void CMapEditorView::SetSelectedItemColor(unsigned int nColor)
{
	Nuclear::PMap &pmap = GetDocument()->GetToolsMap();
	if (m_nEditMode == m_nEditMode)
	{
		Nuclear::SortBaseIDSet::iterator iter = m_SelectedObjects.begin();
		if (m_pNowOper)
			delete m_pNowOper;
		m_pNowOper = new CAction(CAction::AT_OBJECTS, m_nLayerEditID);
		CAction::CObjectsActionAtom *oper = m_pNowOper->GetObjectsActionData();
		PMap::MAP_OBJ_TYPE type;
		unsigned char layerID = static_cast<unsigned char>(m_nLayerEditID - FIRST_ELEMENT_LAYER);
		for (;iter!=m_SelectedObjects.end();++iter)
		{
			if (!GetObjectType(*iter, type))
				continue;
			Nuclear::SubMap::sObjInfo& objInfo = pmap.GetObjInfo(type, layerID, GetObjectId(*iter));
			objInfo.m_ObjectId = *iter;
			oper->m_AtomObjInfo[CAction::CActionAtom::BEFORE_OPERATOR].push_back(objInfo);
			objInfo.m_AttachColor = nColor;
			oper->m_AtomObjInfo[CAction::CActionAtom::AFTER_OPERATOR].push_back(objInfo);
			objInfo.m_ObjectId = CLayerView::GetObjectId(objInfo.m_ObjectId);
		}
		m_pOperatorList->DoOneAction(m_pNowOper);
		m_pNowOper = NULL;
		m_ScreenElements.RemoveObjects(layerID, m_SelectedObjects);
		m_ScreenElements.SetMustRenew();
	}
}

void CMapEditorView::OnUpdateItemProperty(CCmdUI *pCmdUI)
{
	
	if (m_PropertiesDlg.m_hWnd == NULL)
	{
		pCmdUI->SetCheck(FALSE);
		return;
	}
	if (m_PropertiesDlg.IsWindowVisible())
	{
		pCmdUI->SetCheck();
	} else {
		pCmdUI->SetCheck(FALSE);
	}
}

void CMapEditorView::OnItemProperty()
{
	
	if (m_PropertiesDlg.m_hWnd == NULL)
	{
		m_PropertiesDlg.Create(this);
		CRect rect;
		CRect clientWindowRect = m_ClientRect;
		m_PropertiesDlg.GetWindowRect(&rect);
		ClientToScreen(&clientWindowRect);
		rect.MoveToXY(clientWindowRect.right - rect.Width(), clientWindowRect.top);
		m_PropertiesDlg.MoveWindow(&rect);
		m_PropertiesDlg.ShowWindow(SW_SHOW);
	} else {
		if (m_PropertiesDlg.IsWindowVisible())
		{
			m_PropertiesDlg.ShowWindow(SW_HIDE);
		} else {
			m_PropertiesDlg.ShowWindow(SW_SHOW);
		}
	}
}

void CMapEditorView::OnTimeEffect()
{
	
	if (m_TimeEffectEditorDlg.m_hWnd != NULL)
	{
		if (m_TimeEffectEditorDlg.IsWindowVisible())
		{
			m_TimeEffectEditorDlg.ShowWindow(SW_HIDE);
		} else {
			m_TimeEffectEditorDlg.ShowWindow(SW_SHOW);
		}
	} else {
		m_TimeEffectEditorDlg.Create(this);
		m_TimeEffectEditorDlg.ShowWindow(SW_SHOW);
	}
}

void CMapEditorView::CheckScalRect(const RECT &standardRect, int oldWidth, int oldHeight, int &newWidth, int &newHeight)
{
	double asp = fabs(static_cast<double>(standardRect.right - standardRect.left) / (standardRect.bottom - standardRect.top));
	newWidth = abs(oldWidth);
	newHeight = abs(oldHeight);
	if (asp < static_cast<double>(newWidth) / newHeight)
	{
		newWidth = static_cast<int>(newHeight * asp);
	} else {
		newHeight = static_cast<int>(newWidth / asp);
	}
}

void CMapEditorView::OnUpdateEditPaste(CCmdUI *pCmdUI)
{
	BOOL result = TRUE;
	if (m_nLayerEditID < FIRST_ELEMENT_LAYER)
		result = FALSE;
	else if (m_nLayerEditID > LAST_ELEMENT_LAYER)
		result = FALSE;
	else {
		COleDataObject odo;
		odo.AttachClipboard();
		if (!odo.IsDataAvailable(CMapClipboardContentMaker::ms_ClipboardFormatID))
		{
			result = FALSE;
		}
	}
	pCmdUI->Enable(result);
}

void CMapEditorView::OnEditPaste()
{
	


	COleDataObject odo;
	odo.AttachClipboard();
	if (odo.IsDataAvailable(CMapClipboardContentMaker::ms_ClipboardFormatID))
	{
		HANDLE hData = odo.GetGlobalData(CMapClipboardContentMaker::ms_ClipboardFormatID);
		if (hData)
		{
			CMapClipboardContentMaker eeccm;
			if (eeccm.GetDataFromMapClipboardContent(hData))
			{
				LONG middleX, middleY;
				middleX = static_cast<LONG>(m_nViewportWidth / 2);
				middleY = static_cast<LONG>(m_nViewportHeight / 2);
				const VectorMapCBContainer &vet = eeccm.GetContainer();
				VectorMapCBContainer::const_iterator iter = vet.begin();
				CMapEditorDoc *pDoc = GetDocument();
				Nuclear::PMap &map = pDoc->GetToolsMap();
				RECT rect;
				CPoint point;
				if (m_pNowOper)
					delete (m_pNowOper);
				m_pNowOper = new CAction(CAction::AT_OBJECTS, m_nLayerEditID);
				CAction::CObjectsActionAtom* operObjs = m_pNowOper->GetObjectsActionData();
				unsigned int objID;
				unsigned int attachColor;
				bool ok = true;
				ClearAllSelection();
				m_ObjsListDlg.UnselectAllItems();
				for (;iter!=vet.end();iter++)
				{
					rect.left = iter->rect.left + m_DrawScrollPosition.x + middleX;
					rect.top = iter->rect.top + m_DrawScrollPosition.y + middleY;
					rect.right = iter->rect.right + m_DrawScrollPosition.x + middleX;
					rect.bottom = iter->rect.bottom + m_DrawScrollPosition.y + middleY;
					point = iter->point + m_DrawScrollPosition;
					point.x += middleX;
					point.y += middleY;
					std::wstring tmpName(iter->name);
					attachColor = iter->attachColor;
					if ((m_nLayerEditID == LEI_WATER_1 || m_nLayerEditID == LEI_WATER_2) && iter->type != PMap::MAP_OBJ_ELEMENT)
					{
						ok = false;
						continue;
					}
					objID = pDoc->AddObject(iter->type, m_nLayerEditID - FIRST_ELEMENT_LAYER, 
						pDoc->GetObjectMapKey(iter->type, tmpName, m_pOperatorList), rect, point, attachColor);
					Nuclear::SubMap::sObjInfo tmpInfo = map.GetObjInfo(iter->type, m_nLayerEditID - FIRST_ELEMENT_LAYER, objID);
					tmpInfo.m_ObjectId = CLayerView::GetSortBaseId(tmpInfo.m_ObjectId, CLayerView::ConvertObjectType(iter->type));
					operObjs->m_AtomObjInfo[CAction::CActionAtom::AFTER_OPERATOR].push_back(tmpInfo);
					m_ObjsListDlg.TryAddObject(tmpInfo.m_ObjectId);
					AddSelection(tmpInfo.m_ObjectId, rect);
					m_ObjsListDlg.TryChangeSelectedObject(tmpInfo.m_ObjectId);
				
				}
				if (!ok)
				{
					AfxMessageBox(L"天1层不能放特效、植物等动态物件！");
				}
				m_ScreenElements.SetMustRenew();
				pDoc->SetModifiedFlag();
				m_SmallMapDlg.TryInvalidate();
				m_pOperatorList->DoOneAction(m_pNowOper);
				m_pNowOper = NULL;
			}
		}
	}
}

void CMapEditorView::OnUpdateEditCut(CCmdUI *pCmdUI)
{
	BOOL result = TRUE;
	if (m_nLayerEditID < FIRST_ELEMENT_LAYER)
		result = FALSE;
	else if (m_nLayerEditID > LAST_ELEMENT_LAYER)
		result = FALSE;
	else if (m_SelectedObjects.size() == 0)
		result = FALSE;
	else if (m_nBrushType != 0)
		result = FALSE;
	else if (m_nEditMode != EMT_OBJECT_NORMAL)
		result = FALSE;
	pCmdUI->Enable(result);
}

void CMapEditorView::OnEditCut()
{
	
	CMapClipboardContentMaker* pEeccm;
	if (SetSelectItemToClipboardContentMaker(pEeccm))
	{
		HANDLE hData = pEeccm->MakeMapClipboardContent();
		if (hData)
		{
			if (m_nEditMode == EMT_OBJECT_NORMAL)
			{
				DeleteSelectedObjects();
			}
			COleDataSource *pods = new COleDataSource;
			pods->CacheGlobalData(CMapClipboardContentMaker::ms_ClipboardFormatID, hData);
			pods->SetClipboard();
		}
		delete pEeccm;
	}
}

void CMapEditorView::OnUpdateEditCopy(CCmdUI *pCmdUI)
{
	BOOL result = TRUE;
	if (m_nLayerEditID < FIRST_ELEMENT_LAYER)
		result = FALSE;
	else if (m_nLayerEditID > LAST_ELEMENT_LAYER)
		result = FALSE;
	else if (m_SelectedObjects.size() == 0)
		result = FALSE;
	else if (m_nBrushType != 0)
		result = FALSE;
	else if (m_nEditMode != EMT_OBJECT_NORMAL)
		result = FALSE;
	pCmdUI->Enable(result);
}

void CMapEditorView::OnEditCopy()
{
	CMapClipboardContentMaker* pEeccm;
	if (SetSelectItemToClipboardContentMaker(pEeccm))
	{
		HANDLE hData = pEeccm->MakeMapClipboardContent();
		if (hData)
		{
			COleDataSource *pods = new COleDataSource;
			pods->CacheGlobalData(CMapClipboardContentMaker::ms_ClipboardFormatID, hData);
			pods->SetClipboard();
		}
		delete pEeccm;
	}
}

bool CMapEditorView::SetSelectItemToClipboardContentMaker(CMapClipboardContentMaker* &pEeccm)
{
	if (m_nLayerEditID < FIRST_ELEMENT_LAYER)
		return false;
	if (m_nLayerEditID > LAST_ELEMENT_LAYER)
		return false;
	if (m_SelectedObjects.size() == 0)
		return false;
	if (m_nBrushType != 0)
		return false;
	PMap::MAP_OBJ_TYPE objType;
	switch (m_nEditMode)
	{
	case EMT_OBJECT_NORMAL:
		pEeccm = new CMapClipboardContentMaker(MCCT_OBJ);
		break;
	default:
		return false;
		break;
	}
	int layerID = m_nLayerEditID - FIRST_ELEMENT_LAYER;
	Nuclear::PMap &pmap = GetDocument()->GetToolsMap();
	CRect rect, maxRect;
	CPoint point;
	point.x = 0;
	point.y = 0;
	Nuclear::SortBaseIDSet::iterator iter = m_SelectedObjects.begin();


	maxRect.right = MINLONG;
	maxRect.bottom = MINLONG;
	maxRect.left = MAXLONG;
	maxRect.top = MAXLONG;
	Nuclear::CRECT *pRect = NULL;
	
	
	for (;iter!=m_SelectedObjects.end();iter++)
	{
		if (!GetObjectType(*iter, objType))
			continue;
		Nuclear::SubMap::sObjInfo &objInfo = pmap.GetObjInfo(objType, layerID, GetObjectId(*iter));
		pRect = &(objInfo.m_Rect);
		if (pRect->left < maxRect.left)
			maxRect.left = pRect->left;
		if (pRect->top < maxRect.top)
			maxRect.top = pRect->top;
		if (pRect->right > maxRect.right)
			maxRect.right = pRect->right;
		if (pRect->bottom > maxRect.bottom)
			maxRect.bottom = pRect->bottom;
	}
	LONG middleX, middleY;
	middleX = static_cast<LONG>((maxRect.left + maxRect.right) / 2);
	middleY = static_cast<LONG>((maxRect.top + maxRect.bottom) / 2);
	unsigned int attachColor = 0xFFFFFFFF;
	for (iter=m_SelectedObjects.begin();iter!=m_SelectedObjects.end();iter++)
	{
		if (!CLayerView::GetObjectType(*iter, objType))
			continue;
		Nuclear::SubMap::sObjInfo &objInfo = pmap.GetObjInfo(objType, layerID, GetObjectId(*iter));
		rect.left = objInfo.m_Rect.left - middleX;
		rect.right = objInfo.m_Rect.right - middleX;
		rect.top = objInfo.m_Rect.top - middleY;
		rect.bottom = objInfo.m_Rect.bottom - middleY;
		point.x = objInfo.m_Position.x - middleX;
		point.y = objInfo.m_Position.y - middleY;
		attachColor = objInfo.m_AttachColor.data;
		pEeccm->AddObject(pmap.GetObjFileName(objType, objInfo.m_MapKey), objType, rect, point, attachColor);
	}
	return true;
}

void CMapEditorView::SaveNewBase()
{
	int IndexMAX_x = 0;
	int IndexMAX_y = 0;
	int IndexMIN_x = 0;
	int IndexMIN_y = 0;

	int i=0;

	float fmin_x = 1000000.0f;
	float fmax_x = -1000000.0f;
	float fmin_y = 1000000.0f;
	float fmax_y = -1000000.0f;

	for(i=0; i<4; i++)
	{
		if(m_fPosX[i]>fmax_x)
		{
			IndexMAX_x = i;
			fmax_x = m_fPosX[i];
		}
		if(m_fPosX[i]<fmin_x)
		{
			IndexMIN_x = i;
			fmin_x = m_fPosX[i];
		}

		if(m_fPosY[i]>fmax_y)
		{
			IndexMAX_y = i;
			fmax_y = m_fPosY[i];
		}
		if(m_fPosY[i]<fmin_y)
		{
			IndexMIN_y = i;
			fmin_y = m_fPosY[i];
		}
	}

	Nuclear::SortBaseIDSet::iterator objIter = m_SelectedObjects.begin();
	if (objIter != m_SelectedObjects.end())
	{
		CMapEditorDoc* pDoc = (GetDocument());
		XPASSERT(pDoc);
		PMap &pmap = pDoc->GetToolsMap();
		CLayerView::LAYER_OBJ_TYPE type = GetObjectType(*objIter);
		PMap::MAP_OBJ_TYPE objType;
		if (!ConvertObjectType(type, objType))
			return;
		const Nuclear::SubMap::sObjInfo &objInfo = pmap.GetObjInfo(objType, LEI_MID - FIRST_ELEMENT_LAYER, GetObjectId(*objIter));
		const std::wstring &name = pmap.GetObjFileName(objType, objInfo.m_MapKey);
		switch (type)
		{
		case LOT_ELEMENT:
			{
				LPImgInfo pImgInfo = m_ImgInfoMap[name];
				if (pImgInfo)
				{
					if (m_pNowOper)
						delete m_pNowOper;
					m_pNowOper = new CAction(CAction::AT_ELEMENT_BASE, m_nLayerEditID);
					CAction::CElementBaseActionAtom* oper = m_pNowOper->GetElementBaseActionData();
					oper->m_ObjectMapKey = objInfo.m_MapKey;
					for (i=0;i<4;i++)
					{
						oper->oper[i][CAction::CActionAtom::BEFORE_OPERATOR] = pImgInfo->m_Positions[i];
					}
					pImgInfo->m_Positions[Nuclear::enum_left].x = static_cast<LONG>(m_fPosX[IndexMIN_x] - objInfo.m_Rect.left);
					pImgInfo->m_Positions[Nuclear::enum_left].y = static_cast<LONG>(m_fPosY[IndexMIN_x] - objInfo.m_Rect.top);

					pImgInfo->m_Positions[Nuclear::enum_right].x = static_cast<LONG>(m_fPosX[IndexMAX_x] - objInfo.m_Rect.left);
					pImgInfo->m_Positions[Nuclear::enum_right].y = static_cast<LONG>(m_fPosY[IndexMAX_x] - objInfo.m_Rect.top);

					pImgInfo->m_Positions[Nuclear::enum_up].x = static_cast<LONG>(m_fPosX[IndexMIN_y] - objInfo.m_Rect.left);
					pImgInfo->m_Positions[Nuclear::enum_up].y = static_cast<LONG>(m_fPosY[IndexMIN_y] - objInfo.m_Rect.top);

					pImgInfo->m_Positions[Nuclear::enum_bottom].x = static_cast<LONG>(m_fPosX[IndexMAX_y] - objInfo.m_Rect.left);
					pImgInfo->m_Positions[Nuclear::enum_bottom].y = static_cast<LONG>(m_fPosY[IndexMAX_y] - objInfo.m_Rect.top);

					std::map<Nuclear::SubMap::ObjMapKey, int>::iterator iter = m_ChangedImages.find(objInfo.m_MapKey);
					if (iter == m_ChangedImages.end())
					{
						m_ChangedImages[objInfo.m_MapKey] = 1;
					} else {
						(iter->second)++;
					}
					for (i=0;i<4;i++)
					{
						oper->oper[i][CAction::CActionAtom::AFTER_OPERATOR] = pImgInfo->m_Positions[i];
					}
				} else {
					return ;
				}
			}
			break;
		case LOT_LINKEDOBJECT:
			{
				Nuclear::PLinkedObject *pLinkObj = m_LinkedObjectMap[name];
				if (pLinkObj)
				{
					if (m_pNowOper)
						delete m_pNowOper;
					m_pNowOper = new CAction(CAction::AT_DYNAMIC_BASE, m_nLayerEditID);
					CAction::CLinkedObjectBaseActionAtom* oper = m_pNowOper->GetLinkedObjectBaseActionData();
					oper->m_ObjectMapKey = objInfo.m_MapKey;
					for (i=0;i<4;i++)
					{
						oper->oper[i][CAction::CActionAtom::BEFORE_OPERATOR] = pLinkObj->m_ptBasePos[i];
					}
					Nuclear::CPOINT transPoint = objInfo.m_Position - pLinkObj->m_ptCentPos;
					pLinkObj->m_ptBasePos[Nuclear::enum_left].x = static_cast<LONG>(m_fPosX[IndexMIN_x] - transPoint.x);
					pLinkObj->m_ptBasePos[Nuclear::enum_left].y = static_cast<LONG>(m_fPosY[IndexMIN_x] - transPoint.y);

					pLinkObj->m_ptBasePos[Nuclear::enum_right].x = static_cast<LONG>(m_fPosX[IndexMAX_x] - transPoint.x);
					pLinkObj->m_ptBasePos[Nuclear::enum_right].y = static_cast<LONG>(m_fPosY[IndexMAX_x] - transPoint.y);

					pLinkObj->m_ptBasePos[Nuclear::enum_up].x = static_cast<LONG>(m_fPosX[IndexMIN_y] - transPoint.x);
					pLinkObj->m_ptBasePos[Nuclear::enum_up].y = static_cast<LONG>(m_fPosY[IndexMIN_y] - transPoint.y);

					pLinkObj->m_ptBasePos[Nuclear::enum_bottom].x = static_cast<LONG>(m_fPosX[IndexMAX_y] - transPoint.x);
					pLinkObj->m_ptBasePos[Nuclear::enum_bottom].y = static_cast<LONG>(m_fPosY[IndexMAX_y] - transPoint.y);

					std::map<Nuclear::SubMap::ObjMapKey, int>::iterator iter = m_ChangedLinkedObjects.find(objInfo.m_MapKey);
					if (iter == m_ChangedLinkedObjects.end())
					{
						m_ChangedLinkedObjects[objInfo.m_MapKey] = 1;
					} else {
						(iter->second)++;
					}
					for (i=0;i<4;i++)
					{
						oper->oper[i][CAction::CActionAtom::AFTER_OPERATOR] = pLinkObj->m_ptBasePos[i];
					}
				} else {
					return ;
				}
			}
			break;
		default:
			return;
		}
		m_pOperatorList->DoOneAction(m_pNowOper);
		m_pNowOper = NULL;
		m_bIsSelectedBasePoint = false;
		m_ScreenElements.ClearLayers();
		pDoc->SetModifiedFlag();
	}
}

bool CMapEditorView::CheckDragObj(int layer, Nuclear::SortBaseId objID) const
{
	if (m_nLayerEditID - FIRST_ELEMENT_LAYER == layer && m_nEditMode == EMT_OBJECT_DRAG)
	{
		if (m_SelectedObjects.find(objID) != m_SelectedObjects.end())
		{
			return true;
		}		
	}
	return false;	
}

void CMapEditorView::CalculateImgFilePosition(const CPoint& initialPoint, const CPoint& point)
{
	m_fPosX[0] = static_cast<float>(initialPoint.x);
	m_fPosY[0] = static_cast<float>(initialPoint.y);
	m_fPosX[2] = static_cast<float>(point.x);
	m_fPosY[2] = static_cast<float>(point.y);
	float dx = static_cast<float>(point.x - initialPoint.x);
	float dy = static_cast<float>((point.y - initialPoint.y)/Nuclear::COS_58);
	float a = (dx+dy)/2.0f;
	float b = (dy-dx)/2.0f;
	m_fPosX[3] = a +initialPoint.x;
	m_fPosY[3] = static_cast<float>(a*Nuclear::COS_58+initialPoint.y);
	m_fPosX[1] = -b +initialPoint.x;
	m_fPosY[1] = static_cast<float>(b*Nuclear::COS_58+initialPoint.y);
}

void CMapEditorView::OnFileExportJpg()
{
	CString path, name;
	if (!CMapEditorApp::SelectExportFolder(path, name, GetSafeHwnd()))
		return ;
	path = path + _T("jpgs\\");
	SHCreateDirectoryEx(NULL, path, NULL);
	path = path + name + _T("_%04d_%04d.jpg");
	SaveAsJpg(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN), path);
}

void CMapEditorView::OnShowElementResource()
{
	if (m_ElementResourceDlg.m_hWnd == NULL) 
	{
		m_ElementResourceDlg.Create(this);
		m_ElementResourceDlg.ShowWindow(SW_SHOW);
		m_ElementResourceDlg.ResetLists();
	} else {
		if (m_ElementResourceDlg.IsWindowVisible())
		{
			m_ElementResourceDlg.ShowWindow(SW_HIDE);
		} else {
			m_ElementResourceDlg.ShowWindow(SW_SHOW);
			m_ElementResourceDlg.ResetLists();
		}
	}
}

void CMapEditorView::OnZoomReset()
{
	SetRatio(1.0f);
	std::vector<Sprite*>::iterator it = m_Sprites.begin(), ie = m_Sprites.end();
	for (;it!=ie;++it)
	{
		(*it)->SetScale(m_fRatio);
	}
}

void CMapEditorView::OnZoomOut()
{
	SetRatio(m_fRatio / 1.5f);
	std::vector<Sprite*>::iterator it = m_Sprites.begin(), ie = m_Sprites.end();
	for (;it!=ie;++it)
	{
		(*it)->SetScale(m_fRatio);
	}
}

void CMapEditorView::OnZoomIn()
{
	SetRatio(m_fRatio * 1.5f);
	std::vector<Sprite*>::iterator it = m_Sprites.begin(), ie = m_Sprites.end();
	for (;it!=ie;++it)
	{
		(*it)->SetScale(m_fRatio);
	}
}

void CMapEditorView::ResetScrollBar()
{
	SIZE size = GetDocument()->GetDocSize();
	size.cx = static_cast<LONG>(size.cx * m_fRatio);
	size.cy = static_cast<LONG>(size.cy * m_fRatio);
	SetScrollSizes(MM_TEXT, size);
	SetScrollPos(SB_HORZ, static_cast<int>(m_DrawScrollPosition.x * m_fRatio));
	SetScrollPos(SB_VERT, static_cast<int>(m_DrawScrollPosition.y * m_fRatio));
	m_SmallMapDlg.TryInvalidate();
}

void CMapEditorView::OnContextMenu(CWnd* /*pWnd*/, CPoint point)
{
	CPoint realScalPoint = point;
	ScreenToClient(&realScalPoint);
	realScalPoint.x = static_cast<LONG>(realScalPoint.x / m_fRatio);
	realScalPoint.y = static_cast<LONG>(realScalPoint.y / m_fRatio);
	switch(m_nLayerEditID)
	{
	case LEI_GROUND:
		break;
	case LEI_WATER_1:
	case LEI_WATER_2:
	case LEI_LOW_1:
	case LEI_LOW_2:
	case LEI_LOW_3:
	case LEI_LOW_4:
	case LEI_LOW_5:
	case LEI_LOW_6:
	case LEI_MID:
	case LEI_HIGH_1:
	case LEI_HIGH_2:
		{
			if (m_nBrushType == 0) 
			{
				CMenu popup;  
				popup.CreatePopupMenu();
				if (CheckSelectLayerObject(m_nLayerEditID - FIRST_ELEMENT_LAYER, realScalPoint, popup))
				{
					Nuclear::SortBaseId nSelectID = popup.TrackPopupMenu(TPM_LEFTALIGN|TPM_RIGHTBUTTON|TPM_VERTICAL|TPM_NONOTIFY|TPM_RETURNCMD, point.x, point.y, this);
					
					if (nSelectID != 0)
					{
						nSelectID--;
						if (!IsCTRLPressed())
						{
							ClearAllSelection();
							m_ObjsListDlg.UnselectAllItems();
						}
						PMap::MAP_OBJ_TYPE objType;
						CLayerView::GetObjectType(nSelectID, objType);
						Nuclear::CRECT rect = GetDocument()->GetToolsMap().GetObjInfo(objType, m_nLayerEditID - FIRST_ELEMENT_LAYER, GetObjectId(nSelectID)).m_Rect;
						if (m_SelectedObjects.find(nSelectID) == m_SelectedObjects.end())
						{
							AddSelection(nSelectID, rect);
							m_ObjsListDlg.TryChangeSelectedObject(nSelectID);
						} else {
							RemoveSelection(nSelectID, rect);
							m_ObjsListDlg.TryChangeSelectedObject(nSelectID, FALSE);
						}
					}
				}

			}
		}
		
		break;
	default:
		break;

	}
}

void CMapEditorView::OnTimer(UINT_PTR nIDEvent)
{
	switch (nIDEvent)
	{
	case 1000:
		theApp.GetFileIOManager()->OnUpdate(9999999);
		UpdateSprite();
		drawMap();
		m_SmallMapDlg.TryInvalidate(false);
		break;
	case 1001:
		GetDocument()->AutoSave();
		break;
	}
	__super::OnTimer(nIDEvent);
}

void CMapEditorView::OnExportToRmp()
{
	CMapEditorDoc* pDoc = GetDocument();
	XPASSERT(pDoc);
	CString fullPath, name;
	if (pDoc->IsModified())
	{
		AfxMessageBox(_T("请先保存地图～"));
	} else {
		CString title = pDoc->GetTitle();
		CString path = pDoc->GetPathName();
		int pos = path.ReverseFind(L'\\');
		title = title.Left(title.GetLength() - 5);
		path = path.Left(pos);
		if (title != path.Right(title.GetLength()))
		{
			if (!CMapEditorApp::SelectExportFolder(fullPath, name, GetSafeHwnd(), L"mrmp文件名和文件夹名不一致，请手动选择导出路径……"))
				return ;
			name = fullPath + L"map.rmp";
		} else {
			name = path + L"\\map.rmp";
		}
		pDoc->ExportToFile(name);
	}
}

BOOL CMapEditorView::Create(LPCTSTR lpszClassName, LPCTSTR lpszWindowName, DWORD dwStyle, const RECT& rect, CWnd* pParentWnd, UINT nID, CCreateContext* pContext)
{
	BOOL result = CScrollView::Create(lpszClassName, lpszWindowName, dwStyle, rect, pParentWnd, nID, pContext);
	if (result)
	{
		m_ObjectsCtrl.Init(GetDocument(), this);
		m_ScreenElements.Init(GetDocument(), this);
		m_GroundCtrl.Init(GetDocument(), this);
	}
	return result;
}

void CMapEditorView::EndDragging()
{
	CMapEditorDoc *pDoc = GetDocument();
	CAction::CGroundLayerInfosActionAtom* oper = m_pNowOper->GetAllGroundLayerInfosActionData();
#ifdef _DEBUG
	const CToolsMap::TileLayerInfoList layers = pDoc->GetGroundLayerInfos();
	oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = layers;
#else
	oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = pDoc->GetGroundLayerInfos();
#endif
	pDoc->SetModifiedFlag();
	m_pOperatorList->DoOneAction(m_pNowOper);
	m_pNowOper = NULL;
}

void CMapEditorView::BeginDragging()
{
	CMapEditorDoc *pDoc = GetDocument();
	if (m_pNowOper)
		delete m_pNowOper;
	m_pNowOper = new CAction(CAction::AT_ALL_GROUND_LAYER_INFOS, m_nLayerEditID);
	CAction::CGroundLayerInfosActionAtom* oper = m_pNowOper->GetAllGroundLayerInfosActionData();
#ifdef _DEBUG
	const CToolsMap::TileLayerInfoList layers = pDoc->GetGroundLayerInfos();
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = layers;
#else
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pDoc->GetGroundLayerInfos();
#endif

}
void CMapEditorView::SetGroundLayerShow(int layer, bool bShow) 
{
	m_GroundCtrl.SetLayerShow(layer, bShow, m_pNowOper, m_pOperatorList);
}

void CMapEditorView::SetGroundLayerName(int layer, CString name, bool needNoticeLayerListDlg) 
{
	if (needNoticeLayerListDlg)	
	{
		m_LayerListDlg.SetLayerName(layer, name);
	} else {
		
		m_GroundCtrl.SetGroundLayerName(layer, name, m_pNowOper, m_pOperatorList);
	}
}

bool CMapEditorView::DeleteGroundLayer(int layer) 
{
	UINT nOldState = m_LayerListDlg.GetLayerState(layer);
	m_LayerListDlg.SetLayerState(layer, nOldState & ~(LVIS_SELECTED | LVIS_FOCUSED));
	if (m_GroundCtrl.DeleteGroundLayer(layer, m_pNowOper, m_pOperatorList))
	{
		m_LayerListDlg.DeleteLayer(layer);
		return true;
	} else {
		m_LayerListDlg.SetLayerState(layer, nOldState);
		return false;
	}
}

bool CMapEditorView::NewGroundLayer()
{
	if (m_GroundCtrl.NewGroundLayer(m_pNowOper, m_pOperatorList))
	{
		m_LayerListDlg.NewLayer(GetDocument()->GetGroundLayerInfo(0).name);
		return true;
	}
	return false;
}

int CMapEditorView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CScrollView::OnCreate(lpCreateStruct) == -1)
		return -1;
	CMainFrame* pMainWnd = reinterpret_cast<CMainFrame*>(theApp.GetMainWnd());
	pMainWnd->ShowToolBars();
	return 0;
}

void CMapEditorView::OnFileSaveAsJpg()
{
	CString path, name;
	if (!CMapEditorApp::SelectExportFolder(path, name, GetSafeHwnd()))
		return ;
	path = path + _T("editor_picture_blocks\\");
	SHCreateDirectoryEx(NULL, path, NULL);
	path = path + name + _T("_%04d_%04d.jpg");
	SaveAsJpg(JPG_WIDTH, JPG_HEIGHT, path, true);
}


void CMapEditorView::SaveAsJpg(int jpgWidth, int jpgHeight, const CString &nameFormat, bool showBase)
{
	XDisplayMode d3dMode = m_pRenderer->GetDisplayMode();
	CPoint oldDrawScrollPos = m_DrawScrollPosition;
	bool oldShowBase = m_bShowBase;
	unsigned int oldBrushType = m_nBrushType;
	m_bShowBase = showBase;
	m_nBrushType = 0;
	
	jpgWidth = (jpgWidth / 4) * 4;
	jpgHeight = (jpgHeight / 4) * 4;
	
	int printWidth, printHeight;		
	int srcPrintWidth, srcPrintHeight;	
	printWidth = (d3dMode.width / jpgWidth) * jpgWidth;
	printHeight = (d3dMode.height / jpgHeight) * jpgHeight;
	srcPrintWidth = static_cast<int>(printWidth / m_fRatio);
	srcPrintHeight = static_cast<int>(printHeight / m_fRatio);

	PrintScreenParam param;
	param.m_Width = jpgWidth;
	param.m_Height = jpgHeight;
	param.m_NameTample = nameFormat;
	param.m_SrcWidth = srcPrintWidth;
	param.m_SrcHeight = srcPrintHeight;
	param.m_ColBegin = 0;
	param.m_RowBegin = 0;

	
	int colCount, rowCount;		
	int srcWidth, srcHeight;	
	int w, h;					
	int row, col;				
	int remainderW, remainderH;	
	PMap &map = GetDocument()->GetToolsMap();
	srcWidth = map.GetWidth();
	srcHeight = map.GetHeight();

	remainderW = srcWidth % srcPrintWidth;
	remainderH = srcHeight % srcPrintHeight;
	w = srcWidth / srcPrintWidth + (remainderW>0?1:0);
	h = srcHeight / srcPrintHeight + (remainderH>0?1:0);

	m_DrawScrollPosition.y = 0;
	for (row=1;row<=h;++row)
	{
		if (row == h && remainderH>0)
		{
			param.m_SrcHeight = remainderH;
		}
		m_DrawScrollPosition.x = 0;
		param.m_ColBegin = 0;
		param.m_SrcWidth = srcPrintWidth;
		for (col=1;col<=w;++col)
		{
			if (col == w && remainderW>0)
			{
				param.m_SrcWidth = remainderW;
			}
			ScreenshotToFile(param, rowCount, colCount);
			m_DrawScrollPosition.x += srcPrintWidth;
			param.m_ColBegin += colCount;
		}
		param.m_RowBegin += rowCount;
		m_DrawScrollPosition.y += srcPrintHeight;
	}

	m_bShowBase = oldShowBase;
	m_nBrushType = oldBrushType;
	m_DrawScrollPosition = oldDrawScrollPos;
}

void CMapEditorView::ScreenshotToFile(const CMapEditorView::PrintScreenParam &param, int& rowCount, int& colCount)
{
	m_pRenderer->Clear(m_BackgroundColor);
	void* buf;
	buf = NULL;
	int size;
	int old_width = m_nViewportWidth;
	int old_height = m_nViewportHeight;
	m_nViewportWidth = param.m_SrcWidth;
	m_nViewportHeight = param.m_SrcHeight;
	m_ClientRect.left = 0;
	m_ClientRect.top = 0;
	m_ClientRect.right = static_cast<int>(m_nViewportWidth * m_fRatio);
	m_ClientRect.bottom = static_cast<int>(m_nViewportHeight * m_fRatio);
	UpdateLinkedObjs(0);
	UpdateEffects(0);
	m_ScreenElements.RenewLayers();
	int row, col;
	Nuclear::CRECT outRect = m_ClientRect;
	drawBackground();
	drawElementLayers(0);
	GetClientRect(&m_ClientRect);
	m_nViewportWidth = old_width;
	m_nViewportHeight = old_height;
	CString name;
	std::vector<std::pair<Nuclear::CRECT, Nuclear::XBuffer>> dstpics;
	if (m_pRenderer->PrintScreen(&buf, &size, Nuclear::XPIFF_DDS, &outRect))
	{
		if (m_pRenderer->SplitTexture(buf, size, Nuclear::XPTEXFMT_A8R8G8B8, Nuclear::XPIFF_JPG, outRect, dstpics, 
			rowCount, colCount, param.m_Width, param.m_Height))
		{
			FILE *f = NULL;
			std::vector<std::pair<Nuclear::CRECT, Nuclear::XBuffer>>::iterator iter;
			for (iter=dstpics.begin();iter!=dstpics.end();iter++)
			{
				col = iter->first.left / param.m_Width + param.m_ColBegin;
				row = iter->first.top / param.m_Height + param.m_RowBegin;
				name.Format(param.m_NameTample, row, col);
				_tfopen_s(&f, name, _T("wb"));
				if (f)
				{
					Nuclear::FWrite(f, iter->second.constbegin(), iter->second.size());
					fclose(f);
				}

			}
			Nuclear::XFree(buf);
		}
	}
}

void CMapEditorView::OnViewProperty()
{
	CViewPropDlg dlg;
	dlg.SetBaseColor(m_BaseColor);
	dlg.SetGridColor(m_GridColor);
	dlg.SetBgColor(m_BackgroundColor);
	dlg.SetRadius(m_nMagnetRadius);
	if( IDOK != dlg.DoModal() )
		return;
	m_GridColor = dlg.GetGridColor();
	m_BaseColor = dlg.GetBaseColor();
	m_BackgroundColor = dlg.GetBgColor();
	m_nMagnetRadius = dlg.GetRadius();
}

void CMapEditorView::OnUpdateEditRedo(CCmdUI *pCmdUI)
{
	if (m_pOperatorList->CanRedo())
	{
		pCmdUI->Enable();
	} else {
		pCmdUI->Enable(FALSE);
	}
}

void CMapEditorView::OnEditRedo()
{
	ReDo();
}

void CMapEditorView::OnUpdateEditUndo(CCmdUI *pCmdUI)
{
	if (m_pOperatorList->CanUndo())
	{
		pCmdUI->Enable();
	} else {
		pCmdUI->Enable(FALSE);
	}
}

void CMapEditorView::OnEditUndo()
{
	UnDo();
}

void CMapEditorView::OnKeyUp(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	bool hasChanged = false;
	switch(nChar)
	{
	case VK_SHIFT:
		m_MagnetAdjust.x = 0;
		m_MagnetAdjust.y = 0;
		break;
	case 'A':
		{
			CMenu popup;  
			popup.CreatePopupMenu();
			PopMenuDataArray data;
			CPoint realScalPoint;
			CPoint point = m_PointMouse;
			ClientToScreen(&point);
			realScalPoint.x = static_cast<LONG>(m_PointMouse.x / m_fRatio);
			realScalPoint.y = static_cast<LONG>(m_PointMouse.y / m_fRatio);
			ShowAllPictures(realScalPoint, popup, data);
			int nSelectID = popup.TrackPopupMenu(TPM_LEFTALIGN|TPM_RIGHTBUTTON|TPM_VERTICAL|TPM_NONOTIFY|TPM_RETURNCMD, point.x, point.y, this);
			if (nSelectID > 0)
			{
				PopupMenuItemData tmpData = data[nSelectID - 1];
				ChangeEditLayer(tmpData.m_LayerId);
				if (m_nLayerEditID == LEI_GROUND)
				{
					SetSmallLayerEdit(tmpData.m_SmallId);
				} else {
					Nuclear::CRECT rect;
					if (CheckSelectLayerObject(m_nLayerEditID - FIRST_ELEMENT_LAYER, tmpData.m_SmallId, rect))
					{
						m_SelectObjectDlg.SelectSelection();
						ClearAllSelection();
						AddSelection(tmpData.m_SmallId, rect);
					}
				}
			}
		}
		break;
	}
	CScrollView::OnKeyUp(nChar, nRepCnt, nFlags);
}

void CMapEditorView::OnUpdateShowElementList(CCmdUI *pCmdUI)
{
	if (m_ObjsListDlg.m_hWnd != NULL && m_ObjsListDlg.IsWindowVisible())
	{
		pCmdUI->SetCheck(1);
	} else {
		pCmdUI->SetCheck(0);
	}
}

void CMapEditorView::OnShowElementList()
{
	switch(m_nLayerEditID)
	{
	case LEI_GROUND:
		
	case LEI_GAMING:	
		break;
	case LEI_WATER_1:
	case LEI_WATER_2:
	case LEI_LOW_1:
	case LEI_LOW_2:
	case LEI_LOW_3:
	case LEI_LOW_4:
	case LEI_LOW_5:
	case LEI_LOW_6:
	case LEI_MID:
	case LEI_HIGH_1:
	case LEI_HIGH_2:
		if (m_ObjsListDlg.m_hWnd == NULL) 
		{
			m_ObjsListDlg.Create(this);
			m_ObjsListDlg.ShowWindow(SW_SHOW);
			m_ObjsListDlg.ResetLists();
		} else {
			if (m_ObjsListDlg.IsWindowVisible())
			{
				m_ObjsListDlg.ShowWindow(SW_HIDE);
			} else {
				m_ObjsListDlg.ShowWindow(SW_SHOW);
				m_ObjsListDlg.ResetLists();
			}
		}
		break;
	}
}

BOOL CMapEditorView::OnScroll(UINT nScrollCode, UINT nPos, BOOL bDoScroll)
{
	BOOL result = CScrollView::OnScroll(nScrollCode, nPos, bDoScroll);
	m_SmallMapDlg.TryInvalidate();
	return result;
}

void CMapEditorView::OnUpdateShowSmallMap(CCmdUI *pCmdUI)
{
	if (m_SmallMapDlg.m_hWnd != NULL && m_SmallMapDlg.IsWindowVisible())
	{
		pCmdUI->SetCheck(1);
	} else {
		pCmdUI->SetCheck(0);
	}

}

void CMapEditorView::OnShowSmallMap()
{
	if (m_SmallMapDlg.m_hWnd == NULL) 
	{
		m_SmallMapDlg.Create(this);
		m_SmallMapDlg.ShowWindow(SW_SHOW);
	} else {
		if (m_SmallMapDlg.IsWindowVisible())
		{
			m_SmallMapDlg.ShowWindow(SW_HIDE);
		} else {
			m_SmallMapDlg.ShowWindow(SW_SHOW);
		}
	}
}

void CMapEditorView::OnUpdateShowSelectDlg(CCmdUI *pCmdUI)
{
	int result = 0;
	switch(m_nLayerEditID)
	{
	case LEI_GROUND:
		if (m_SelectGroundDlg.m_hWnd != NULL) 
		{
			if (m_SelectGroundDlg.IsWindowVisible())
			{
				result = 1;
			}
		}
		break;
	case LEI_WATER_1:
	case LEI_WATER_2:
	case LEI_LOW_1:
	case LEI_LOW_2:
	case LEI_LOW_3:
	case LEI_LOW_4:
	case LEI_LOW_5:
	case LEI_LOW_6:
	case LEI_MID:
	case LEI_HIGH_1:
	case LEI_HIGH_2:
		if (m_SelectObjectDlg.m_hWnd != NULL) 
		{
			if (m_SelectObjectDlg.IsWindowVisible())
			{
				result = 1;
			}
		}
		break;
	case LEI_TRANS:
		if (m_TransparentObjectListDlg.m_hWnd != NULL)
		{
			if (m_TransparentObjectListDlg.IsWindowVisible())
			{
				result = 1;
			}
		}
	case LEI_TRIGGER:
		if (m_TriggerListDlg.m_hWnd != NULL)
		{
			if (m_TriggerListDlg.IsWindowVisible())
			{
				result = 1;
			}
		}
		break;
	case LEI_WATER:
		if (m_SelectWaterDlg.m_hWnd != NULL)
		{
			if (m_SelectWaterDlg.IsWindowVisible())
			{
				result = 1;
			}
		}
		break;
	case LEI_BACKGROUND:
		if (m_BackgroundListDlg.m_hWnd != NULL)
		{
			if (m_BackgroundListDlg.IsWindowVisible())
			{
				result = 1;
			}
		}
		break;
	case LEI_GAMING:
		break;
	}

	pCmdUI->SetCheck(result);
}

void CMapEditorView::OnShowSelectDlg()
{
	switch(m_nLayerEditID)
	{
	case LEI_GROUND:
		if (m_SelectGroundDlg.m_hWnd == NULL) 
		{
			m_SelectGroundDlg.Create(this);
			m_SelectGroundDlg.ShowWindow(SW_SHOW);
		} else {
			if (m_SelectGroundDlg.IsWindowVisible())
			{
				m_SelectGroundDlg.ShowWindow(SW_HIDE);
			} else {
				m_SelectGroundDlg.ShowWindow(SW_SHOW);
			}

		}
		break;
	case LEI_WATER_1:
	case LEI_WATER_2:
	case LEI_LOW_1:
	case LEI_LOW_2:
	case LEI_LOW_3:
	case LEI_LOW_4:
	case LEI_LOW_5:
	case LEI_LOW_6:
	case LEI_MID:
	case LEI_HIGH_1:
	case LEI_HIGH_2:
		if (m_SelectObjectDlg.m_hWnd == NULL) 
		{
			m_SelectObjectDlg.Create(this);
			m_SelectObjectDlg.ShowWindow(SW_SHOW);
		} else {
			if (m_SelectObjectDlg.IsWindowVisible())
			{
				m_SelectObjectDlg.ShowWindow(SW_HIDE);
			} else {
				m_SelectObjectDlg.ShowWindow(SW_SHOW);
			}
		}
		break;
	case LEI_TRANS:
		if (m_TransparentObjectListDlg.m_hWnd == NULL)
		{
			m_TransparentObjectListDlg.Create(this);
			m_TransparentObjectListDlg.ShowWindow(SW_SHOW);
		} else {
			if (m_TransparentObjectListDlg.IsWindowVisible())
			{
				m_TransparentObjectListDlg.ShowWindow(SW_HIDE);
			} else {
				m_TransparentObjectListDlg.ShowWindow(SW_SHOW);
			}
		}
		break;
	case LEI_TRIGGER:
		if (m_TriggerListDlg.m_hWnd == NULL)
		{
			m_TriggerListDlg.Create(this);
			m_TriggerListDlg.ShowWindow(SW_SHOW);
		} else {
			if (m_TriggerListDlg.IsWindowVisible())
			{
				m_TriggerListDlg.ShowWindow(SW_HIDE);
			} else {
				m_TriggerListDlg.ShowWindow(SW_SHOW);
			}
		}
		break;
	case LEI_WATER:
		if (m_SelectWaterDlg.m_hWnd == NULL)
		{
			m_SelectWaterDlg.Create(this);
			m_SelectWaterDlg.ShowWindow(SW_SHOW);
		} else {
			if (m_SelectWaterDlg.IsWindowVisible())
			{
				m_SelectWaterDlg.ShowWindow(SW_HIDE);
			} else {
				m_SelectWaterDlg.ShowWindow(SW_SHOW);
			}
		}
		break;
	case LEI_BACKGROUND:
		if (m_BackgroundListDlg.m_hWnd == NULL) 
		{
			m_BackgroundListDlg.Create(this);
			m_BackgroundListDlg.ShowWindow(SW_SHOW);
		} else {
			if (m_BackgroundListDlg.IsWindowVisible())
			{
				m_BackgroundListDlg.ShowWindow(SW_HIDE);
			} else {
				m_BackgroundListDlg.ShowWindow(SW_SHOW);
			}
		}
		break;
	case LEI_GAMING:
		break;
	}
}

void CMapEditorView::ToPlaySelectedTransparentObjects()
{
	if (m_nLayerEditID == LEI_TRANS && fabs(m_fRatio - 1.0f) < 0.0001f)
	{
		if (m_SelectedObjects.size() != 1)
		{
			AfxMessageBox(_T("只能选中一个传送对象来播放"));
		} else {
			m_bTransparent = true;
		}
	}
}

void CMapEditorView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	switch (nChar)
	{
	case VK_SHIFT:
		m_MagnetAdjust.x = 0;
		m_MagnetAdjust.y = 0;
		break;
	case VK_DOWN:
	case VK_UP:
	case VK_LEFT:
	case VK_RIGHT:
		if (m_nLayerEditID>= FIRST_ELEMENT_LAYER && m_nLayerEditID <= LAST_ELEMENT_LAYER)
		{
			if (m_nBrushType == 0 && m_SelectedObjects.size() > 0 && m_nEditMode == EMT_OBJECT_NORMAL)
			{
				int dx = 0;
				int dy = 0;
				switch (nChar)
				{
				case VK_DOWN:
					dy = 1;
					break;
				case VK_UP:
					dy = -1;
					break;
				case VK_LEFT:
					dx = -1;
					break;
				case VK_RIGHT:
					dx = 1;
					break;
				}
				MoveSelectedObjects(dx, dy);
			}
		}
		break;
	case VK_DELETE:
		if (m_nLayerEditID>= FIRST_ELEMENT_LAYER && m_nLayerEditID <= LAST_ELEMENT_LAYER && m_nBrushType == 0 
			&& m_SelectedObjects.size() > 0 && m_nEditMode == EMT_OBJECT_NORMAL)
		{
			DeleteSelectedObjects();
		}
		break;
	case VK_ESCAPE:
		if (m_nLayerEditID>= FIRST_ELEMENT_LAYER && m_nLayerEditID <= LAST_ELEMENT_LAYER)
		{
			m_SelectObjectDlg.SelectSelection();
			ClearAllSelection();
			m_ObjsListDlg.UnselectAllItems();
		} else if (m_nLayerEditID == LEI_TRANS)
		{
			ClearAllSelection();
			m_TransparentObjectListDlg.UnselectAllItems();
		}
		m_bIsSelectedBase = false;
		m_bIsSelectedBasePoint = false;
		break;
	case 'D':
		if (m_nLayerEditID == LEI_GAMING && m_bReadyForSprite && m_Sprites.size() > 0 ) {
			Sprite* sprite = m_Sprites.back();
			m_Sprites.pop_back();
			m_ScreenElements.RemoveSprite(sprite);
			delete sprite;
			m_ScreenElements.SetMustRenew();
			if (m_Sprites.size() > 0)
			{
				m_pSelectedSprite = m_Sprites.back();
			} else {
				m_pSelectedSprite = NULL;
			}

		}
		break;
	case 'N':
		if (m_nLayerEditID == LEI_GAMING && m_bReadyForSprite && (fabs(m_fRatio - 1.0f) < 0.0001f)) {
			Sprite* pSprite = AddSprite();
			pSprite->SetLocation(Nuclear::Location(m_DrawScrollPosition.x + 280, m_DrawScrollPosition.y + 280));
			m_ScreenElements.SetMustRenew();
			m_pSelectedSprite = pSprite;
		}
		break;
	case 'P':
		ToPlaySelectedTransparentObjects();
		break;
	case 'C':
		{
			CColorDialog dlg;
			if (dlg.DoModal() == IDOK)
			{
				COLORREF ccc = dlg.GetColor();
				m_GroundColor.a = 0xFF;
				m_GroundColor.r = GetRValue(ccc);
				m_GroundColor.g = GetGValue(ccc);
				m_GroundColor.b = GetBValue(ccc);
			}
			

		}
		break;
	}
	CScrollView::OnKeyDown(nChar, nRepCnt, nFlags);
}

bool CMapEditorView::MoveSelectedWaterAreas(long addX, long addY)
{
	bool result = true;
	m_nEditMode = EMT_WATER_IMMERSION_NORMAL;
	Nuclear::PMap &map = GetDocument()->GetToolsMap();
	std::set<unsigned int>::iterator iter = m_SelectedObjects.begin();
	const Nuclear::SubMap::WaterAreaInfoMap &waterArea = map.GetWaterAreaInfos();
	Nuclear::SubMap::WaterAreaInfoMap::const_iterator infoIter;
	Nuclear::SubMap::WaterAreaMap::const_iterator rectIter;
	Nuclear::SubMap::WaterAreaID id;
	CAction* pOper = new CAction(CAction::AT_WATER_AREA, GetLayerEditId());
	CAction::CWaterAreaActionAtom* pWaterData = pOper->GetWaterAreaActionData();
	pWaterData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = map.GetWaterAreaInfos();
	Nuclear::CRECT rect;
	for (;iter!=m_SelectedObjects.end();++iter)
	{
		id.id = *iter;
		infoIter = waterArea.find(id.infoID);
		if (infoIter != waterArea.end())
		{
			rectIter = infoIter->second.areas.find(id.areaID);
			if (rectIter != infoIter->second.areas.end())
			{
				rect = rectIter->second;
				rect.left += addX;
				rect.right += addX;
				rect.top += addY;
				rect.bottom += addY;
				map.ModifyAnWaterArea(id, rect);
			}
			
		}
		
	}
	pWaterData->oper[CAction::CActionAtom::AFTER_OPERATOR] = map.GetWaterAreaInfos();
	DoAction(pOper);
	if (result)
	{
		std::vector<Nuclear::CRECT>::iterator rectIter = m_SelectedRects.begin();
		for (;rectIter!=m_SelectedRects.end();rectIter++)
		{
			rectIter->left += addX;
			rectIter->right += addX;
			rectIter->top += addY;
			rectIter->bottom += addY;
		}
	}
	return result;
}

bool CMapEditorView::MoveSelectedTriggers(long addX, long addY)
{

	bool result = true;
	m_nEditMode = EMT_TRIGGER_NORMAL;
	Nuclear::PMap &pmap = GetDocument()->GetToolsMap();
	std::set<unsigned int>::iterator iter = m_SelectedObjects.begin();
	const Nuclear::SubMap::TriggerInfoMap &triggers = pmap.GetTriggerInfos();
	Nuclear::SubMap::TriggerInfoMap::const_iterator infoIter;
	Nuclear::SubMap::TriggerAreaMap::const_iterator rectIter;
	Nuclear::SubMap::TriggerID id;
	CAction* pOper = new CAction(CAction::AT_TRIGGER_OBJECT, GetLayerEditId());
	CAction::CTriggerObjectActionAtom* pTriggerData = pOper->GetTriggerObjectActionData();
	pTriggerData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetTriggerInfos();
	Nuclear::CRECT rect;
	for (;iter!=m_SelectedObjects.end();++iter)
	{
		id.id = *iter;
		infoIter = triggers.find(id.infoID);
		if (infoIter != triggers.end())
		{
			rectIter = infoIter->second.areas.find(id.areaID);
			if (rectIter != infoIter->second.areas.end())
			{
				rect = rectIter->second;
				rect.left += addX;
				rect.right += addX;
				rect.top += addY;
				rect.bottom += addY;
				pmap.ModifyAnTriggerArea(id, rect);
			}

		}

	}
	pTriggerData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetTriggerInfos();
	DoAction(pOper);
	if (result)
	{
		std::vector<Nuclear::CRECT>::iterator rectIter = m_SelectedRects.begin();
		for (;rectIter!=m_SelectedRects.end();rectIter++)
		{
			rectIter->left += addX;
			rectIter->right += addX;
			rectIter->top += addY;
			rectIter->bottom += addY;
		}
	}
	return result;
}

bool CMapEditorView::MoveSelectedTransparentObjects(long addX, long addY)
{
	bool result = true;
	m_nEditMode = EMT_TRANSPARENT_OBJECT_NORMAL;
	std::set<unsigned int>::iterator iter = m_SelectedObjects.begin();
	Nuclear::PMap &map = GetDocument()->GetToolsMap();
	const Nuclear::SubMap::TransObjInfoMap transObjs = map.GetTransInfos();
	Nuclear::SubMap::TransObjInfoMap::const_iterator transIter;
	Nuclear::SubMap::TransObjInfo tmpInfo;
	CAction *pNowOper = new CAction(CAction::AT_TRANSPARENT_OBJECT, m_nLayerEditID);
	CAction::CTransparentObjectActionAtom *oper = pNowOper->GetTransparentObjectsActionData();
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = map.GetTransInfos();
	for (;iter!=m_SelectedObjects.end();iter++)
	{
		transIter = transObjs.find(*iter);
		if (transIter != transObjs.end())
		{
			tmpInfo = transIter->second;
			tmpInfo.picRect.left += addX;
			tmpInfo.picRect.right += addX;
			tmpInfo.picRect.top += addY;
			tmpInfo.picRect.bottom += addY;
			map.ModifyAnTransObj(transIter->first, tmpInfo);
		}
	}
	oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = map.GetTransInfos();
	DoAction(pNowOper);
	GetDocument()->SetModifiedFlag();
	if (result)
	{
		std::vector<Nuclear::CRECT>::iterator rectIter = m_SelectedRects.begin();
		for (;rectIter!=m_SelectedRects.end();rectIter++)
		{
			rectIter->left += addX;
			rectIter->right += addX;
			rectIter->top += addY;
			rectIter->bottom += addY;
		}
	}
	return result;
}

bool CMapEditorView::MoveSelectedObjects(long addX, long addY)
{
	m_nEditMode = EMT_OBJECT_NORMAL;
	m_ScreenElements.SetMustRenew();
	if (m_ObjectsCtrl.MoveObjects(addX, addY, m_SelectedObjects, m_pNowOper, m_pOperatorList))
	{
		m_SmallMapDlg.TryInvalidate();
		std::vector<Nuclear::CRECT>::iterator rectIter = m_SelectedRects.begin();
		for (;rectIter!=m_SelectedRects.end();rectIter++)
		{
			rectIter->left += addX;
			rectIter->right += addX;
			rectIter->top += addY;
			rectIter->bottom += addY;
		}
		m_PropertiesDlg.TryInvalidata();
		return true;
	} else {
		return false;
	}
}

void CMapEditorView::DeleteSelectedObjects()
{
	if (m_ObjectsCtrl.DeleteObjects(m_SelectedObjects, m_pNowOper, m_pOperatorList))
	{
		unsigned char layer = m_nLayerEditID - FIRST_ELEMENT_LAYER;
		m_ScreenElements.RemoveObjects(layer, m_SelectedObjects);
		EffectMap::iterator effIter;
		Nuclear::SubMap::ObjectId objID;
		Nuclear::SortBaseIDSet::iterator iter = m_SelectedObjects.begin();
		for(;iter!=m_SelectedObjects.end();++iter)
		{
			if (CLayerView::GetObjectType(*iter) == CLayerView::LOT_EFFECT)
			{
				objID = CLayerView::GetObjectId(*iter);
				effIter = m_EffectArray[layer].find(objID);
				if (effIter != m_EffectArray[layer].end())
				{
					delete effIter->second;
					m_EffectArray[layer].erase(effIter);
				}
			}
		}
		ClearAllSelection();
		m_ScreenElements.SetMustRenew();
		m_SmallMapDlg.TryInvalidate();
		m_PropertiesDlg.TryInvalidata();
	}
}

char CMapEditorView::CheckSelectedRectPoint(const CPoint &point)
{
	std::vector<Nuclear::CRECT>::iterator iter = m_SelectedRects.begin();
	char pos = TOSD_DIRECT_EMPTY;
	if (abs(iter->left - point.x) * m_fRatio < 5)
	{
		pos |= TOSD_LEFT_MASK;
	} else if (abs(iter->right - point.x) * m_fRatio < 5)
	{
		pos |= TOSD_RIGHT_MASK;
	}

	if (abs(iter->top -point.y) * m_fRatio < 5)
	{
		pos |= TOSD_TOP_MASK;
	} else if (abs(iter->bottom - point.y) * m_fRatio < 5)
	{
		pos |= TOSD_BOTTOM_MASK;
	}
	return pos;
}

void CMapEditorView::MouseMove(const CPoint & point)
{
	CMapEditorDoc* pDoc = (GetDocument());
	CPoint tmpPoint;
	CPoint realScalPoint;
	realScalPoint.x = static_cast<LONG>(point.x / m_fRatio);
	realScalPoint.y = static_cast<LONG>(point.y / m_fRatio);
	Nuclear::CRECT rect;

	if (IsSPACEPressed())
	{
		if (m_bIsMouseDown)
		{
			tmpPoint = m_PointMouse - point;
			Nuclear::PMap& map = pDoc->GetToolsMap();
			tmpPoint.x += GetScrollPos(SB_HORZ);
			tmpPoint.y += GetScrollPos(SB_VERT);
			if (tmpPoint.x / m_fRatio + m_nViewportWidth > map.GetWidth())
			{
				tmpPoint.x = static_cast<LONG>((map.GetWidth() - m_nViewportWidth) * m_fRatio);
			}
			if (tmpPoint.y / m_fRatio + m_nViewportHeight > map.GetHeight())
			{
				tmpPoint.y = static_cast<LONG>((map.GetHeight() - m_nViewportHeight) * m_fRatio);
			}

			if (tmpPoint.x < 0)
				tmpPoint.x = 0;
			if (tmpPoint.y < 0)
				tmpPoint.y = 0;
			m_MouseDownPoint.x -= tmpPoint.x - GetScrollPos(SB_HORZ);
			m_MouseDownPoint.y -= tmpPoint.y - GetScrollPos(SB_VERT);
			SetScrollPos(SB_HORZ, static_cast<int>(tmpPoint.x));
			SetScrollPos(SB_VERT, static_cast<int>(tmpPoint.y));
		}
	} else {
		switch(m_nLayerEditID)
		{
		case LEI_GROUND:
			if (!((m_nEditMode == EMT_SMALL_GROUND_TYPE || m_nEditMode == EMT_SMALL_GROUND_TILE)
				&& (m_bIsMutiSelectedLayer || (m_nSmallLayerEditID == -1))))
			{
				switch (m_nEditMode)
				{
				case EMT_SMALL_GROUND_TYPE:
					m_bIsShowMouseRect = GetMouseRectForGroundTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_SMALL);
					if (m_bIsMouseDown)
					{
						if (ClientRectToGroundTypeWorldRect(m_MouseRect, rect, Nuclear::TST_SMALL))
						{
							if (m_pNowOper == NULL)
								m_pNowOper = new CAction(CAction::AT_TILE, m_nLayerEditID);
							if (m_GroundCtrl.SetGroundType(m_nBrushType, rect, m_pNowOper))
							{
								pDoc->SetModifiedFlag();
								m_SmallMapDlg.TryInvalidate();
							}
						}
					}
					break;
				case EMT_SMALL_GROUND_TILE:
					m_bIsShowMouseRect = true;
					GetMouseRectForTileTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_SMALL);
					if (m_bIsMouseDown)
					{
						ClientRectToTileWorldRect(m_MouseRect, rect, Nuclear::TST_SMALL);
						if (m_pNowOper == NULL)
							m_pNowOper = new CAction(CAction::AT_TILE, m_nLayerEditID);
						if (m_GroundCtrl.SetTileType(m_nBrushType, rect, m_pNowOper))
						{
							pDoc->SetModifiedFlag();
							m_SmallMapDlg.TryInvalidate();
						}
					}
					break;
				case EMT_SMALL_GROUND_COLOR:

					m_bIsShowMouseRect = GetMouseRectForGroundTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_SMALL);
					if (m_bIsMouseDown)
					{
						if (ClientRectToGroundTypeWorldRect(m_MouseRect, rect, Nuclear::TST_SMALL))
						{
							if (m_pNowOper == NULL)
								m_pNowOper = new CAction(CAction::AT_TILE_COLOR, m_nLayerEditID);
							if (m_GroundCtrl.SetTileColorType(m_nBrushType, rect, m_pNowOper))
							{
								pDoc->SetModifiedFlag();
								m_SmallMapDlg.TryInvalidate();
							}
						}
					}
					m_bIsShowMouseRect = true;
					break;
				case EMT_SMALL_SUPER_DELETE:
					m_bIsShowMouseRect = true;
					GetMouseRectForTileTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_SMALL);
					if (m_bIsMouseDown)
					{
						ClientRectToTileWorldRect(m_MouseRect, rect, Nuclear::TST_SMALL);
						if (m_pNowOper == NULL)
							m_pNowOper = new CAction(CAction::AT_SUPER_TILE, m_nLayerEditID);
						if (m_GroundCtrl.SuperDeleteTile(rect, m_pNowOper))
						{
							pDoc->SetModifiedFlag();
							m_SmallMapDlg.TryInvalidate();
						}
					}
					break;
				case EMT_MIDDLE_GROUND:
					m_bIsShowMouseRect = GetMouseRectForGroundTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_MIDDLE);
					if (m_bIsMouseDown)
					{
						if (ClientRectToGroundTypeWorldRect(m_MouseRect, rect, Nuclear::TST_MIDDLE))
						{
							if (m_pNowOper == NULL)
								m_pNowOper = new CAction(CAction::AT_BIGGER_TILE, m_nLayerEditID);
							if (m_GroundCtrl.SetGroundType(m_nBrushType, rect, m_pNowOper))
							{
								m_SmallMapDlg.TryInvalidate();
							}
						}
					}
					break;
				case EMT_MIDDLE_GROUND_TILE:
					m_bIsShowMouseRect = true;
					GetMouseRectForTileTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_MIDDLE);
					if (m_bIsMouseDown)
					{
						ClientRectToTileWorldRect(m_MouseRect, rect, Nuclear::TST_MIDDLE);
						if (m_pNowOper == NULL)
							m_pNowOper = new CAction(CAction::AT_BIGGER_TILE, m_nLayerEditID);
						if (m_GroundCtrl.SetTileType(m_nBrushType, rect, m_pNowOper))
						{
							pDoc->SetModifiedFlag();
							m_SmallMapDlg.TryInvalidate();
						}
					}
					break;
				case EMT_BIG_GROUND:
					m_bIsShowMouseRect = GetMouseRectForGroundTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_BIG);
					if (m_bIsMouseDown)
					{
						if (ClientRectToGroundTypeWorldRect(m_MouseRect, rect, Nuclear::TST_BIG))
						{
							if (m_pNowOper == NULL)
								m_pNowOper = new CAction(CAction::AT_BIGGER_TILE, m_nLayerEditID);
							if (m_GroundCtrl.SetGroundType(m_nBrushType, rect, m_pNowOper))
							{
								pDoc->SetModifiedFlag();
								m_SmallMapDlg.TryInvalidate();
							}

						}
					}
					break;
				case EMT_BIG_GROUND_TILE:
					m_bIsShowMouseRect = true;
					GetMouseRectForTileTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_BIG);
					if (m_bIsMouseDown)
					{
						ClientRectToTileWorldRect(m_MouseRect, rect, Nuclear::TST_BIG);
						if (m_pNowOper == NULL)
							m_pNowOper = new CAction(CAction::AT_BIGGER_TILE, m_nLayerEditID);
						if (m_GroundCtrl.SetTileType(m_nBrushType, rect, m_pNowOper))
						{
							pDoc->SetModifiedFlag();
							m_SmallMapDlg.TryInvalidate();
						}
					}
					break;
				}
				AdjustMousePoints();
			}
			break;
		case LEI_WATER:
		case LEI_WATER_1:
		case LEI_WATER_2:
		case LEI_LOW_1:
		case LEI_LOW_2:
		case LEI_LOW_3:
		case LEI_LOW_4:
		case LEI_LOW_5:
		case LEI_LOW_6:
		case LEI_MID:
		case LEI_HIGH_1:
		case LEI_HIGH_2:
		case LEI_TRANS:
		case LEI_TRIGGER:
			switch (m_nEditMode)
			{
			
			case EMT_WATER_TILE:
				m_bIsShowMouseRect = true;
				GetMouseRectForWaterMode(realScalPoint, m_MouseRect);
				if (m_bIsMouseDown)
				{
					ClientRectToWaterWorldRect(m_MouseRect, rect);
					if (m_pNowOper == NULL)
						m_pNowOper = new CAction(CAction::AT_WATER, m_nLayerEditID);
					Nuclear::SubMap::WaterTileType type;
					type.data = static_cast<unsigned char>(m_nBrushType);
					if (m_GroundCtrl.SetWaterType(type, rect, m_pNowOper))
					{
						pDoc->SetModifiedFlag();
						m_SmallMapDlg.TryInvalidate();
					}
				}
				AdjustMousePoints();
				break;
			case EMT_OBJECT_NORMAL:
				if (m_bIsMouseDown)
				{
					if (m_nBrushType == 0 && m_SelectedObjects.size() > 0)
					{
						m_nEditMode = EMT_OBJECT_DRAG;
						m_bIsShowSelectElementRect = false;
					}
				}
				break;
			case EMT_WATER_IMMERSION_NORMAL:
				m_bIsShowMouseRect = false;
			case EMT_TRANSPARENT_OBJECT_NORMAL:
			case EMT_TRIGGER_NORMAL:
				if (m_bIsMouseDown)
				{
					if (m_nBrushType == 0 && m_SelectedObjects.size() > 0)
					{
						switch (m_nEditMode)
						{
						case EMT_TRANSPARENT_OBJECT_NORMAL:
							m_nEditMode = EMT_TRANSPARENT_OBJECT_DRAG;
							break;
						case EMT_TRIGGER_NORMAL:
							m_nEditMode = EMT_TRIGGER_DRAG;
							break;
						case EMT_WATER_IMMERSION_NORMAL:
							m_nEditMode = EMT_WATER_IMMERSION_DRAG;
							break;
						}
						m_bIsShowSelectElementRect = false;
					}
				} else if (m_SelectedObjects.size() == 1)
				{
					HCURSOR hCurs;
					switch (CheckSelectedRectPoint(realScalPoint + m_DrawScrollPosition))
					{
					case TOSD_LEFT_TOP:
					case TOSD_RIGHT_BOTTOM:
						hCurs = LoadCursor(NULL, IDC_SIZENWSE);
						break;
					case TOSD_RIGHT_TOP:
					case TOSD_LEFT_BOTTOM:
						hCurs = LoadCursor(NULL, IDC_SIZENESW);
						break;
					default:
						
						hCurs = LoadCursor(NULL, IDC_ARROW);
						break;
					}
					SetCursor(hCurs);
					
				}
				break;
			case EMT_OBJECT_DRAG:
			case EMT_TRANSPARENT_OBJECT_DRAG:
			case EMT_TRIGGER_DRAG:
			case EMT_WATER_IMMERSION_DRAG:
				break;
			case EMT_OBJECT_DRAG_RECT:
			case EMT_TRANSPARENT_OBJECT_DRAG_RECT:
			case EMT_TRIGGER_DRAG_RECT:
			case EMT_WATER_IMMERSION_DRAG_RECT:
				m_MouseRect.left = static_cast<int>(m_MouseDownPoint.x / m_fRatio) + m_DrawScrollPosition.x;
				m_MouseRect.top = static_cast<int>(m_MouseDownPoint.y / m_fRatio) + m_DrawScrollPosition.y;
				m_MouseRect.right = realScalPoint.x + m_DrawScrollPosition.x;
				m_MouseRect.bottom = realScalPoint.y + m_DrawScrollPosition.y;
				int tmpSwap;
				if (m_MouseRect.left > m_MouseRect.right)
				{
					tmpSwap = m_MouseRect.left;
					m_MouseRect.left = m_MouseRect.right;
					m_MouseRect.right = tmpSwap;
				}
				if (m_MouseRect.top > m_MouseRect.bottom)
				{
					tmpSwap = m_MouseRect.top;
					m_MouseRect.top = m_MouseRect.bottom;
					m_MouseRect.bottom = tmpSwap;
				}
				break;
			case EMT_TRANSPARENT_OBJECT_CHANGE_SIZE:
			case EMT_TRIGGER_CHANGE_SIZE:
			case EMT_WATER_IMMERSION_CHANGE_SIZE:
				break;
			}
			break;
		case LEI_GAMING:
			if (m_bIsMouseDown && m_bIsSelectedBase && m_bIsSelectedBasePoint)
			{
				CalculateImgFilePosition(m_InitialPoint, realScalPoint + m_DrawScrollPosition);
			}
			break;
		}
	}
	m_PointMouse = point;
	realScalPoint += m_DrawScrollPosition;
	reinterpret_cast<CMainFrame*>(theApp.m_pMainWnd)->SetStateBarMousePosition(realScalPoint.x, realScalPoint.y);
}

void CMapEditorView::AdjustMousePoints(void)
{
	m_MouseRect.left += m_DrawScrollPosition.x;
	m_MouseRect.right += m_DrawScrollPosition.x;
	m_MouseRect.top += m_DrawScrollPosition.y;
	m_MouseRect.bottom += m_DrawScrollPosition.y;
}
void CMapEditorView::SelectLayerObject(unsigned char LayerId, const Nuclear::CRECT& rect)
{
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	const CScreenElements::ElementLayersArray &elementLayers = m_ScreenElements.GetElements();
	const Nuclear::TopologyBaseVector &elements = elementLayers[LayerId].m_TopologyArray;
	Nuclear::TopologyBaseVector::const_iterator iter = elements.begin();
	Nuclear::SubMap::sObjInfo *ptrInfo;
	LAYER_OBJ_TYPE type;
	if (!IsCTRLPressed())
	{
		ClearAllSelection();
	}
	for (;iter!=elements.end();iter++)
	{
		type = GetObjectType(iter->sortBaseID);
		if (CLayerView::CheckIsObject(type))
		{
			ptrInfo = static_cast<Nuclear::SubMap::sObjInfo*>(iter->data);
			if (ptrInfo->m_bDeleted)
				continue;
			if (Nuclear::IsRectCross(ptrInfo->m_Rect, rect))
			{
				Nuclear::SortBaseId sortBaseID = CLayerView::GetSortBaseId(ptrInfo->m_ObjectId, type);
				AddSelection(sortBaseID, ptrInfo->m_Rect);
				m_ObjsListDlg.TryChangeSelectedObject(sortBaseID);
			}
		}
	}

}

void CMapEditorView::SelectTransparentObject( const Nuclear::CRECT& rect)
{
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	const Nuclear::SubMap::TransObjInfoMap &tranObjs = map.GetTransInfos();
	Nuclear::SubMap::TransObjInfoMap::const_iterator iter = tranObjs.begin();
	if (!IsCTRLPressed())
	{
		ClearAllSelection();
	}
	for (;iter!=tranObjs.end();iter++)
	{

		if (Nuclear::IsRectCross(iter->second.picRect, rect))
		{
			AddSelection(iter->first, iter->second.picRect);
			m_TransparentObjectListDlg.TryChangeSelectedTransparentObject(iter->first);
		}
	}
}

void CMapEditorView::SelectWaterArea( const Nuclear::CRECT& rect)
{
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	const Nuclear::SubMap::WaterAreaInfoMap &waterArea = map.GetWaterAreaInfos();
	Nuclear::SubMap::WaterAreaInfoMap::const_iterator iter = waterArea.begin();
	Nuclear::SubMap::WaterAreaMap::const_iterator rectIter, rectIterEnd;
	Nuclear::SubMap::WaterAreaID id;
	if (!IsCTRLPressed())
	{
		ClearAllSelection();
	}
	for (;iter!=waterArea.end();iter++)
	{
		rectIter = iter->second.areas.begin();
		rectIterEnd = iter->second.areas.end();
		for (;rectIter!=rectIterEnd;++rectIter)
		{
			if (Nuclear::IsRectCross(rectIter->second, rect))
			{
				id.infoID = iter->first;
				id.areaID = rectIter->first;
				AddSelection(id.id, rectIter->second);
				m_SelectWaterDlg.TryChangeSelectWaterArea(id);
			}
		}
	}
}

unsigned int CMapEditorView::GetWaterDepth(const Nuclear::Location& pt) const
{
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	const Nuclear::SubMap::WaterAreaInfoMap &waterArea = map.GetWaterAreaInfos();
	Nuclear::SubMap::WaterAreaInfoMap::const_iterator iter = waterArea.begin();
	Nuclear::SubMap::WaterAreaMap::const_iterator rectIter, rectIterEnd;
	Nuclear::SubMap::WaterAreaID id;
	unsigned int result = 0;
	int border;
	for (;iter!=waterArea.end();iter++)
	{
		rectIter = iter->second.areas.begin();
		rectIterEnd = iter->second.areas.end();
		for (;rectIter!=rectIterEnd;++rectIter)
		{
			if (rectIter->second.PtInRect(pt))
			{
				if (result < iter->second.depth)
				{
					border = iter->second.border;
					if (pt.x - rectIter->second.left < border)
						border = pt.x - rectIter->second.left;
					if (rectIter->second.right - pt.x < border)
						border = rectIter->second.right - pt.x;
					if (pt.y - rectIter->second.top < border)
						border = pt.y - rectIter->second.top;
					if (rectIter->second.bottom - pt.y < border)
						border = rectIter->second.bottom - pt.y;
					if (border == iter->second.border)
					{
						result = iter->second.depth;
					} else {
						unsigned int tmp = static_cast<unsigned int>(Nuclear::Lerpf(0.0f, 
							static_cast<float>(iter->second.depth), 
							static_cast<float>(border) / iter->second.border));
						if (result < tmp)
							result = tmp;
					}
					
				}
			}
		}
	}
	return result;
}

void CMapEditorView::SelectTrigger( const Nuclear::CRECT& rect)
{
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	const Nuclear::SubMap::TriggerInfoMap &triggers = pmap.GetTriggerInfos();
	Nuclear::SubMap::TriggerInfoMap::const_iterator iter = triggers.begin();
	Nuclear::SubMap::TriggerAreaMap::const_iterator rectIter, rectIterEnd;
	Nuclear::SubMap::TriggerID id;
	if (!IsCTRLPressed())
	{
		ClearAllSelection();
	}
	for (;iter!=triggers.end();iter++)
	{
		rectIter = iter->second.areas.begin();
		rectIterEnd = iter->second.areas.end();
		if (m_HiddenTriggerArea.find(iter->first) != m_HiddenTriggerArea.end())
			continue;
		for (;rectIter!=rectIterEnd;++rectIter)
		{
			if (Nuclear::IsRectCross(rectIter->second, rect))
			{
				id.infoID = iter->first;
				id.areaID = rectIter->first;
				AddSelection(id.id, rectIter->second);
				m_TriggerListDlg.TryChangeSelectedTrigger(id);
			}
		}
	}
}

bool CMapEditorView::CheckSelectElementBasePoint(const CPoint& point, Nuclear::XPIMG_BORDERPOS& selPoint)
{
	Nuclear::SortBaseIDSet::iterator objIter = m_SelectedObjects.begin();
	if (objIter != m_SelectedObjects.end())
	{
		PMap::MAP_OBJ_TYPE type;
		if (!GetObjectType(*objIter, type))
			return false;
		Nuclear::CPOINT pos[4];
		Nuclear::CPOINT transPoint;	
		CMapEditorDoc* pDoc = GetDocument();
		XPASSERT(pDoc);
		PMap &pmap = pDoc->GetToolsMap();
		const Nuclear::SubMap::sObjInfo &objInfo = pmap.GetObjInfo(type, LEI_MID - FIRST_ELEMENT_LAYER, GetObjectId(*objIter));
		const std::wstring &name = pmap.GetObjFileName(type, objInfo.m_MapKey);
		switch (type)
		{
		case PMap::MAP_OBJ_ELEMENT:
			{
				LPImgInfo pImgInfo;
				pImgInfo = m_ImgInfoMap[name];
				if (pImgInfo)
				{
					pos[0] = pImgInfo->m_Positions[0];pos[1] = pImgInfo->m_Positions[1];pos[2] = pImgInfo->m_Positions[2];pos[3] = pImgInfo->m_Positions[3];
					transPoint.x = objInfo.m_Rect.left;
					transPoint.y = objInfo.m_Rect.top;
				} else {
					return false;
				}
			}
			break;
		case PMap::MAP_OBJ_LINKEDOBJ:
			{
				Nuclear::PLinkedObject *pLinkObj;
				pLinkObj = m_LinkedObjectMap[name];
				if (pLinkObj)
				{
					pos[0] = pLinkObj->m_ptBasePos[0];pos[1] = pLinkObj->m_ptBasePos[1];
					pos[2] = pLinkObj->m_ptBasePos[2];pos[3] = pLinkObj->m_ptBasePos[3];
					transPoint = objInfo.m_Position - pLinkObj->m_ptCentPos;
				} else {
					return false;
				}
			}
			break;
		default:
			return false;
		}
		int i, j, x, y;
		for (i=0;i<4;i++)
		{
			x = pos[i].x + transPoint.x - m_DrawScrollPosition.x;
			y = pos[i].y + transPoint.y - m_DrawScrollPosition.y;
			if (point.x >= x-3 && point.x <= x+4 && point.y >= y-3 && point.y <= y+4)
			{
				selPoint = static_cast<Nuclear::XPIMG_BORDERPOS>(i);
				for (j=0;j<4;j++)
				{
					m_fPosX[j] = static_cast<float>(pos[j].x + transPoint.x);
					m_fPosY[j] = static_cast<float>(pos[j].y + transPoint.y);
				}
				return true;			
			}
		}
	}
	return false;
}

bool CMapEditorView::CheckSelectObjectBase(const CPoint& point, Nuclear::SortBaseId& sortBaseID)
{
	bool result = false;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	Nuclear::CPOINT mousePoint = point + m_DrawScrollPosition;
	Nuclear::CPOINT tmpPoint;	
	Nuclear::CPOINT transPoint;	
	Nuclear::CRECT baseRect;
	const CScreenElements::ElementLayersArray &elementLayers = m_ScreenElements.GetElements();
	const Nuclear::TopologyBaseVector &elements = elementLayers[LEI_MID - FIRST_ELEMENT_LAYER].m_TopologyArray;
	Nuclear::TopologyBaseVector::const_iterator iter = elements.begin();
	Nuclear::SubMap::sObjInfo *ptrObjInfo;
	LPImgInfo pImgInfo;
	Nuclear::PLinkedObject* pLinkObj;
	float x, y, x1, y1;
	POINT basePos[4];
	CLayerView::LAYER_OBJ_TYPE type;
	for (;iter!=elements.end();iter++)
	{
		type = GetObjectType(iter->sortBaseID);
		switch (type)
		{
		case LOT_EFFECT:
		case LOT_SPRITE:
			continue;
			break;
		case LOT_ELEMENT:
			ptrObjInfo = static_cast<Nuclear::SubMap::sObjInfo*>(iter->data);
			if (ptrObjInfo->m_bDeleted)
				continue;

			pImgInfo = m_ImgInfoMap[pmap.GetObjFileName(PMap::MAP_OBJ_ELEMENT, ptrObjInfo->m_MapKey)];

			if (!pImgInfo)
				continue;

			transPoint.x = ptrObjInfo->m_Rect.left;
			transPoint.y = ptrObjInfo->m_Rect.top;

			baseRect.left = pImgInfo->m_Positions[Nuclear::enum_left].x;
			baseRect.top = pImgInfo->m_Positions[Nuclear::enum_up].y;
			baseRect.right = pImgInfo->m_Positions[Nuclear::enum_right].x;
			baseRect.bottom = pImgInfo->m_Positions[Nuclear::enum_bottom].y;
			basePos[0] = pImgInfo->m_Positions[0];
			basePos[1] = pImgInfo->m_Positions[1];
			basePos[2] = pImgInfo->m_Positions[2];
			basePos[3] = pImgInfo->m_Positions[3];
			sortBaseID = CLayerView::GetSortBaseId(ptrObjInfo->m_ObjectId, type);
			break;
		case LOT_LINKEDOBJECT:
			ptrObjInfo = static_cast<Nuclear::SubMap::sObjInfo*>(iter->data);
			if (ptrObjInfo->m_bDeleted)
				continue;

			pLinkObj = m_LinkedObjectMap[pmap.GetObjFileName(PMap::MAP_OBJ_LINKEDOBJ, ptrObjInfo->m_MapKey)];
			if (!pLinkObj)
				continue;

			transPoint = ptrObjInfo->m_Position - pLinkObj->m_ptCentPos;
			baseRect.left = pLinkObj->m_ptBasePos[Nuclear::enum_left].x;
			baseRect.top = pLinkObj->m_ptBasePos[Nuclear::enum_up].y;
			baseRect.right = pLinkObj->m_ptBasePos[Nuclear::enum_right].x;
			baseRect.bottom = pLinkObj->m_ptBasePos[Nuclear::enum_bottom].y;
			basePos[0] = pLinkObj->m_ptBasePos[0];
			basePos[1] = pLinkObj->m_ptBasePos[1];
			basePos[2] = pLinkObj->m_ptBasePos[2];
			basePos[3] = pLinkObj->m_ptBasePos[3];
			sortBaseID = CLayerView::GetSortBaseId(ptrObjInfo->m_ObjectId, type);
			break;
		case LOT_DISTORT:
			int todo;
			break;
		}
		tmpPoint = mousePoint - transPoint;


		if (baseRect.PtInRect(tmpPoint))
		{
			x = static_cast<float>(tmpPoint.x -basePos[Nuclear::enum_left].x);
			y = static_cast<float>(tmpPoint.y - basePos[Nuclear::enum_left].y);
			x1 = static_cast<float>(basePos[Nuclear::enum_up].x - basePos[Nuclear::enum_left].x);
			y1 = static_cast<float>(basePos[Nuclear::enum_up].y - basePos[Nuclear::enum_left].y);
			if (x * y1 / x1 > y)  
				continue;

			x = static_cast<float>(tmpPoint.x - basePos[Nuclear::enum_up].x);
			y = static_cast<float>(tmpPoint.y - basePos[Nuclear::enum_up].y);
			x1 = static_cast<float>(basePos[Nuclear::enum_right].x - basePos[Nuclear::enum_up].x);
			y1 = static_cast<float>(basePos[Nuclear::enum_right].y - basePos[Nuclear::enum_up].y);
			if (x * y1 / x1 > y)  
				continue;

			x = static_cast<float>(tmpPoint.x - basePos[Nuclear::enum_right].x);
			y = static_cast<float>(tmpPoint.y - basePos[Nuclear::enum_right].y);
			x1 = static_cast<float>(basePos[Nuclear::enum_bottom].x - basePos[Nuclear::enum_right].x);
			y1 = static_cast<float>(basePos[Nuclear::enum_bottom].y - basePos[Nuclear::enum_right].y);
			if (x * y1 / x1 < y)  
				continue;

			x = static_cast<float>(tmpPoint.x - basePos[Nuclear::enum_bottom].x);
			y = static_cast<float>(tmpPoint.y - basePos[Nuclear::enum_bottom].y);
			x1 = static_cast<float>(basePos[Nuclear::enum_left].x - basePos[Nuclear::enum_bottom].x);
			y1 = static_cast<float>(basePos[Nuclear::enum_left].y - basePos[Nuclear::enum_bottom].y);
			if (x * y1 / x1 < y)  
				continue;

			for (int i=0;i<4;i++)
			{
				m_fPosX[i] = static_cast<float>(basePos[i].x + transPoint.x);
				m_fPosY[i] = static_cast<float>(basePos[i].y + transPoint.y);
			}
			result = true;
			break;
		}
	}
	return result;
}

bool CMapEditorView::CheckSelectTransparentObject(unsigned int transID, Nuclear::CRECT& rect)
{
	Nuclear::PMap &map = GetDocument()->GetToolsMap();
	const Nuclear::SubMap::TransObjInfoMap &tranObjs = map.GetTransInfos();
	Nuclear::SubMap::TransObjInfoMap::const_iterator iter = tranObjs.find(transID);
	if (iter == tranObjs.end())
		return false;
	rect = iter->second.picRect;
	return true;
}

CMapEditorView::CLICK_STATE CMapEditorView::CheckSelectTrigger(const CPoint& point, Nuclear::SubMap::TriggerID& result, Nuclear::CRECT& rect)
{
	Nuclear::CPOINT tmpPoint = point + m_DrawScrollPosition;
	const Nuclear::SubMap::TriggerInfoMap &triggers = GetDocument()->GetTriggerInfos();
	Nuclear::SubMap::TriggerInfoMap::const_iterator iter;
	Nuclear::SubMap::TriggerAreaMap::const_iterator rectIter, rectIterEnd;

	std::set<unsigned int>::iterator ids = m_SelectedObjects.begin();
	for(;ids!=m_SelectedObjects.end();ids++)
	{
		result.id = *ids;
		if (m_HiddenTriggerArea.find(result.infoID) != m_HiddenTriggerArea.end())
			continue;
		iter = triggers.find(result.infoID);
		if (iter!=triggers.end())
		{
			rectIter = iter->second.areas.find(result.areaID);
			if (rectIter != iter->second.areas.end())
			{
				if (rectIter->second.PtInRect(tmpPoint))
				{
					rect = rectIter->second;
					return CS_SELECTED_ELEMENT;
				}	
			}
		}
	}

	for (iter=triggers.begin();iter!=triggers.end();iter++)
	{
		if (m_HiddenTriggerArea.find(iter->first) != m_HiddenTriggerArea.end())
			continue;
		rectIter = iter->second.areas.begin();
		rectIterEnd = iter->second.areas.end();
		for (;rectIter!=rectIterEnd;++rectIter)
		{
			if (rectIter->second.PtInRect(tmpPoint))
			{
				rect = rectIter->second;
				result.infoID = iter->first;
				result.areaID = rectIter->first;
				return CS_COMMON_ELEMENT;
			}
		}

	}
	return CS_EMPTY_AREA;
}

CMapEditorView::CLICK_STATE CMapEditorView::CheckSelectWaterArea(const CPoint& point, Nuclear::SubMap::WaterAreaID& result, Nuclear::CRECT& rect)
{
	Nuclear::PMap &pmap = GetDocument()->GetToolsMap();
	Nuclear::CPOINT tmpPoint = point + m_DrawScrollPosition;
	const Nuclear::SubMap::WaterAreaInfoMap &waterArea = pmap.GetWaterAreaInfos();
	Nuclear::SubMap::WaterAreaInfoMap::const_iterator iter;
	Nuclear::SubMap::WaterAreaMap::const_iterator rectIter, rectIterEnd;
	
	std::set<unsigned int>::iterator ids = m_SelectedObjects.begin();
	for(;ids!=m_SelectedObjects.end();ids++)
	{
		result.id = *ids;
		iter = waterArea.find(result.infoID);
		if (iter!=waterArea.end())
		{
			rectIter = iter->second.areas.find(result.areaID);
			if (rectIter != iter->second.areas.end())
			{
				if (rectIter->second.PtInRect(tmpPoint))
				{
					rect = rectIter->second;
					return CS_SELECTED_ELEMENT;
				}	
			}
		}
	}

	for (iter=waterArea.begin();iter!=waterArea.end();iter++)
	{
		rectIter = iter->second.areas.begin();
		rectIterEnd = iter->second.areas.end();
		for (;rectIter!=rectIterEnd;++rectIter)
		{
			if (rectIter->second.PtInRect(tmpPoint))
			{
				rect = rectIter->second;
				result.infoID = iter->first;
				result.areaID = rectIter->first;
				return CS_COMMON_ELEMENT;
			}
		}
		
	}
	return CS_EMPTY_AREA;
}

CMapEditorView::CLICK_STATE CMapEditorView::CheckSelectTransparentObject(const CPoint& point, unsigned int& result, Nuclear::CRECT& rect)
{
	Nuclear::PMap &map = GetDocument()->GetToolsMap();
	Nuclear::CPOINT tmpPoint = point + m_DrawScrollPosition;
	const Nuclear::SubMap::TransObjInfoMap &tranObjs = map.GetTransInfos();
	Nuclear::SubMap::TransObjInfoMap::const_iterator iter;
	std::set<unsigned int>::iterator transIDs = m_SelectedObjects.begin();

	for(;transIDs!=m_SelectedObjects.end();transIDs++)
	{
		iter = tranObjs.find(*transIDs);
		if (iter!=tranObjs.end())
		{
			if (iter->second.picRect.PtInRect(tmpPoint))
			{
				rect = iter->second.picRect;
				result = *transIDs;
				return CS_SELECTED_ELEMENT;
			}
		}
	}

	for (iter=tranObjs.begin();iter!=tranObjs.end();iter++)
	{
		if (iter->second.picRect.PtInRect(tmpPoint))
		{
			rect = iter->second.picRect;
			result = iter->first;
			return CS_COMMON_ELEMENT;
		}
	}
	return CS_EMPTY_AREA;
}

CMapEditorView::CLICK_STATE CMapEditorView::CheckSelectLayerObject(unsigned char LayerId, const CPoint& point, Nuclear::SortBaseId& result, Nuclear::CRECT& rect)
{
	result = 0;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	Nuclear::CPOINT tmpPoint = point + m_DrawScrollPosition;
	Nuclear::SortBaseIDSet::iterator baseIDiter = m_SelectedObjects.begin();
	PMap::MAP_OBJ_TYPE objType;
	LAYER_OBJ_TYPE type;
	for(;baseIDiter!=m_SelectedObjects.end();baseIDiter++)
	{
		type = GetObjectType(*baseIDiter);
		if (!ConvertObjectType(type, objType))
			continue;
		rect = pmap.GetObjInfo(objType, LayerId, CLayerView::GetObjectId(*baseIDiter)).m_Rect;
		if (rect.PtInRect(tmpPoint))
		{
			result = *baseIDiter;
			return CS_SELECTED_ELEMENT;
		}
	}

	const CScreenElements::ElementLayersArray &elementLayers = m_ScreenElements.GetElements();
	const Nuclear::TopologyBaseVector &objs = elementLayers[LayerId].m_TopologyArray;
	Nuclear::TopologyBaseVector::const_iterator iter = objs.begin();
	Nuclear::SubMap::sObjInfo *ptrInfo;
	for (;iter!=objs.end();iter++)
	{
		type = GetObjectType(iter->sortBaseID);
		if (CheckIsObject(type))
		{
			ptrInfo = static_cast<Nuclear::SubMap::sObjInfo*>(iter->data);
			if (ptrInfo->m_bDeleted)
				continue;
			rect = ptrInfo->m_Rect;
			if (rect.PtInRect(tmpPoint))
			{
				result = GetSortBaseId(ptrInfo->m_ObjectId, type);
				return CS_COMMON_ELEMENT;
			}
		}
	}
	return CS_EMPTY_AREA;
}

bool CMapEditorView::CheckSelectLayerObject(unsigned char LayerId, const CPoint& point, CMenu& popup)
{
	bool result = false;
	CMapEditorDoc* pDoc = GetDocument();
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	Nuclear::CPOINT tmpPoint = point + m_DrawScrollPosition;
	const CScreenElements::ElementLayersArray &elementLayers = m_ScreenElements.GetElements();
	const Nuclear::TopologyBaseVector &elements = elementLayers[LayerId].m_TopologyArray;
	Nuclear::TopologyBaseVector::const_iterator iter = elements.begin();
	Nuclear::SubMap::sObjInfo *ptrInfo;
	PMap::MAP_OBJ_TYPE objType;
	CLayerView::LAYER_OBJ_TYPE type;
	for (;iter!=elements.end();iter++)
	{
		type = GetObjectType(iter->sortBaseID);
		if (!ConvertObjectType(type, objType))
			continue;
		ptrInfo = static_cast<Nuclear::SubMap::sObjInfo*>(iter->data);
		if (ptrInfo->m_bDeleted)
			continue;
		if (ptrInfo->m_Rect.PtInRect(tmpPoint))
		{
			const std::wstring &name = pmap.GetObjFileName(objType, ptrInfo->m_MapKey);
			popup.AppendMenu(MF_STRING|MF_ENABLED, iter->sortBaseID + 1, name.data());
			result = true;
		}
	}
	return result;
}

void CMapEditorView::ShowAllPictures(const CPoint& point, CMenu& popup, PopMenuDataArray& data)
{
	PopupMenuItemData tmpData;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	Nuclear::CPOINT tmpPoint = point + m_DrawScrollPosition;
	data.clear();
	tmpData.m_LayerId = LEI_GROUND;
	
	if (tmpPoint.x <= map.GetWidth() && tmpPoint.x > 0
		&& tmpPoint.y <= map.GetHeight() && tmpPoint.y > 0)
	{
		TileType *ptrTile;
		unsigned char nowTile, tmpTileType;
		CString FileName;
		const Nuclear::SubMap::PtrTileTypeList &layers = pDoc->GetGroundLayers();
		int layer, layerID, x, y, width, pos;
		width = map.GetWidthForSmlTiles();
		x = tmpPoint.x / Nuclear::PMap::TILE_WIDTH;
		y = tmpPoint.y / Nuclear::PMap::TILE_HEIGHT;
		pos = y * width + x;
		nowTile = Nuclear::SubMap::PGround::TILE_EMPTY;
		for (layer=0;layer<static_cast<int>(layers.size());layer++)
		{
			layerID = GetGroundLayerID(layer);
			if (layerID == -1)	
				continue;
			tmpData.m_SmallId = layerID;
			ptrTile = layers[layerID] + pos;
			if (*ptrTile != Nuclear::SubMap::PGround::TILE_NULL)
			{
				tmpTileType = Nuclear::SubMap::PGround::GetTileType(*ptrTile);
				tmpTileType -= tmpTileType & nowTile;	
				if (tmpTileType)						
				{	
					nowTile |= tmpTileType;				
					FileName = theApp.GetPathMap()->GetTileFileName(*ptrTile).data();
					if (FileName != _T(""))
					{
						data.push_back(tmpData);
						popup.AppendMenu(MF_STRING|MF_ENABLED, data.size(), FileName);
					}
				}
			}
			if (nowTile == Nuclear::SubMap::PGround::TILE_SOLID)	
			{
				break;
			}
		}
	}

	
	const CScreenElements::ElementLayersArray &elementLayers = m_ScreenElements.GetElements();
	CScreenElements::ElementLayersArray::const_iterator elementLayersIter = elementLayers.begin();
	Nuclear::TopologyBaseVector::const_iterator iter;
	Nuclear::SubMap::sObjInfo *ptrInfo;
	Nuclear::PMap::MAP_OBJ_TYPE objType;
	for (;elementLayersIter!=elementLayers.end();elementLayersIter++)
	{
		tmpData.m_LayerId = static_cast<LAYER_EDIT_ID>(tmpData.m_LayerId + 1);
		iter=elementLayersIter->m_TopologyArray.begin();
		for(;iter!=elementLayersIter->m_TopologyArray.end();iter++)
		{
			if (CLayerView::GetObjectType(iter->sortBaseID, objType))
			{
				ptrInfo = static_cast<Nuclear::SubMap::sObjInfo*>(iter->data);
				if (ptrInfo->m_bDeleted)
					continue;
				if (ptrInfo->m_Rect.PtInRect(tmpPoint))
				{
					tmpData.m_SmallId = iter->sortBaseID;
					data.push_back(tmpData);
					const std::wstring &name = map.GetObjFileName(objType, ptrInfo->m_MapKey);
					popup.AppendMenu(MF_STRING|MF_ENABLED, data.size(), name.data());
				}
			}
		}
	}
}


bool CMapEditorView::CalculateAdjustMouseObjectV(const Nuclear::CRECT &rect, const Nuclear::CRECT &mouseRect)
{
	int minDis = m_nMagnetRadius + 1;
	int tmpDis = rect.left - mouseRect.left;
	if (abs(tmpDis) < abs(minDis))
		minDis = tmpDis;
	tmpDis = rect.left - mouseRect.right;
	if (abs(tmpDis) < abs(minDis))
		minDis = tmpDis;
	tmpDis = rect.right - mouseRect.left;
	if (abs(tmpDis) < abs(minDis))
		minDis = tmpDis;
	tmpDis = rect.right - mouseRect.right;
	if (abs(tmpDis) < abs(minDis))
		minDis = tmpDis;
	if (minDis != m_nMagnetRadius + 1)
	{
		m_MagnetAdjust.x = minDis;
		return true;
	}
	return false;
}

bool CMapEditorView::CalculateAdjustMouseObjectH(const Nuclear::CRECT &rect, const Nuclear::CRECT &mouseRect)
{
	int minDis = m_nMagnetRadius + 1;
	int tmpDis = rect.top - mouseRect.top;
	if (abs(tmpDis) < abs(minDis))
		minDis = tmpDis;
	tmpDis = rect.top - mouseRect.bottom;
	if (abs(tmpDis) < abs(minDis))
		minDis = tmpDis;
	tmpDis = rect.bottom - mouseRect.top;
	if (abs(tmpDis) < abs(minDis))
		minDis = tmpDis;
	tmpDis = rect.bottom - mouseRect.bottom;
	if (abs(tmpDis) < abs(minDis))
		minDis = tmpDis;
	if (minDis != m_nMagnetRadius + 1)
	{
		m_MagnetAdjust.y = minDis;
		return true;
	}
	return false;
}

void CMapEditorView::CalculateAdjustMouseObject(const Nuclear::CRECT &rect, bool isSelected)
{
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();

	const CScreenElements::ElementLayersArray &elementLayers = m_ScreenElements.GetElements();
	const Nuclear::TopologyBaseVector &elements = elementLayers[m_nLayerEditID - FIRST_ELEMENT_LAYER].m_TopologyArray;
	Nuclear::TopologyBaseVector::const_iterator iter = elements.begin();
	Nuclear::SubMap::sObjInfo *ptrObjInfo;
	int minVDis = m_nMagnetRadius + 1;
	int minHDis = m_nMagnetRadius + 1;
	int tmpDis;
	Nuclear::SortBaseId hObjID, vObjID;
	Nuclear::SubMap::ObjectId selID = 0;
	Nuclear::CRECT *pRect;
	LAYER_OBJ_TYPE type;
	PMap::MAP_OBJ_TYPE objType;
	if (isSelected)
	{
		selID = GetObjectId(*(m_SelectedObjects.begin()));
	}
	for (;iter!=elements.end();iter++)
	{
		type = GetObjectType(iter->sortBaseID);
		if (CheckIsObject(type))
		{
			ptrObjInfo = static_cast<Nuclear::SubMap::sObjInfo*>(iter->data);
			if (ptrObjInfo->m_bDeleted)
				continue;
			if (isSelected && ptrObjInfo->m_ObjectId == selID)
				continue;
			pRect = &ptrObjInfo->m_Rect;
			
			
			if ((pRect->top >= rect.top-m_nMagnetRadius && pRect->top <= rect.bottom+m_nMagnetRadius) ||
				(pRect->bottom >= rect.top-m_nMagnetRadius && pRect->bottom <= rect.bottom+m_nMagnetRadius) ||
				(pRect->top <= rect.top && pRect->bottom >= rect.bottom))
			{
				tmpDis = pRect->left-rect.right;
				if (abs(tmpDis) < abs(minHDis))
				{
					hObjID = GetSortBaseId(ptrObjInfo->m_ObjectId, type);
					minHDis = tmpDis;
				}
				tmpDis = pRect->right-rect.left;
				if (abs(tmpDis) < abs(minHDis))
				{
					hObjID = GetSortBaseId(ptrObjInfo->m_ObjectId, type);
					minHDis = tmpDis;
				}
			}

			
			if ((pRect->left >= rect.left-m_nMagnetRadius && pRect->left <= rect.right+m_nMagnetRadius) ||
				(pRect->right >= rect.left-m_nMagnetRadius && pRect->right <= rect.right+m_nMagnetRadius) ||
				(pRect->left <= rect.left && pRect->right >= rect.right))
			{
				tmpDis = pRect->top-rect.bottom;
				if (abs(tmpDis) < abs(minVDis))
				{
					vObjID = GetSortBaseId(ptrObjInfo->m_ObjectId, type);
					minVDis = tmpDis;
				}
				tmpDis = pRect->bottom-rect.top;
				if (abs(tmpDis) < abs(minVDis))
				{
					vObjID = GetSortBaseId(ptrObjInfo->m_ObjectId, type);
					minVDis = tmpDis;
				}
			}
		}
	}
	m_MagnetAdjust.x = 0;
	m_MagnetAdjust.y = 0;
	
	if (abs(minVDis) < abs(minHDis))
	{
		m_MagnetAdjust.y = minVDis;
		GetObjectType(vObjID, objType);
		Nuclear::SubMap::sObjInfo &objInfo = map.GetObjInfo(objType, m_nLayerEditID - FIRST_ELEMENT_LAYER, GetObjectId(vObjID));
		if (!CalculateAdjustMouseObjectV(objInfo.m_Rect, rect))
		{
			if (minHDis < m_nMagnetRadius + 1)
			{
				m_MagnetAdjust.x = minHDis;
			}
		}
	} else {
		if (minHDis < m_nMagnetRadius + 1)
		{
			m_MagnetAdjust.x = minHDis;
			GetObjectType(hObjID, objType);
			Nuclear::SubMap::sObjInfo &objInfo = map.GetObjInfo(objType, m_nLayerEditID - FIRST_ELEMENT_LAYER, GetObjectId(hObjID));
			if (!CalculateAdjustMouseObjectH(objInfo.m_Rect, rect))
			{
				if (minVDis < m_nMagnetRadius + 1)
				{
					m_MagnetAdjust.y = minVDis;
				}
			}
		}
	}
}

bool CMapEditorView::DrawStaticLinkedObjectTree(Nuclear::PLinkedObject *pLko, float x, float y, bool drawBase, unsigned int attachColor)
{
	Nuclear::PLinkedObject::VectorLinkedObjectInfo::iterator iter = pLko->m_vectorLinkedObjectInfo.begin() + pLko->GetMainNodeID();
	std::wstring imgFilename;
	float mainCenterX, mainCenterY;
	mainCenterX = x - (pLko->m_ptCentPos.x - iter->m_nWidth / 2) * m_fRatio;
	mainCenterY = y - (pLko->m_ptCentPos.y - iter->m_nHeight / 2) * m_fRatio;
	iter = pLko->m_vectorLinkedObjectInfo.begin();
	for (;iter!=pLko->m_vectorLinkedObjectInfo.end();iter++)
	{
		imgFilename = iter->m_strPicName;
		imgFilename.erase(imgFilename.size()-4, 4);
		CheckPictureLoad(imgFilename);
		LPImgInfo pImgInfo = m_ImgInfoMap[imgFilename];
		if (iter->m_bMainNode)
		{
			drawElement(pImgInfo, mainCenterX - (iter->m_nWidth / 2) * m_fRatio,
				mainCenterY - (iter->m_nHeight / 2) * m_fRatio, false, attachColor);
		} else {
			drawElement(pImgInfo, mainCenterX + (iter->m_rotateCent_2.x - iter->m_rotateCent.x - iter->m_nWidth / 2) * m_fRatio,
				mainCenterY + (iter->m_rotateCent_2.y - iter->m_rotateCent.y - iter->m_nHeight / 2) * m_fRatio, false, attachColor);
		}
	}
	if (m_bShowBase && drawBase)
	{
		float x1, x2, y1, y2, left, top;
		left = x - static_cast<float>(pLko->m_ptCentPos.x) * m_fRatio;
		top = y - static_cast<float>(pLko->m_ptCentPos.y) * m_fRatio;
		int i;
		for (i=0;i<3;i++)
		{
			x1 = left + pLko->m_ptBasePos[i].x * m_fRatio;
			x2 = left + pLko->m_ptBasePos[i+1].x * m_fRatio;
			y1 = top + pLko->m_ptBasePos[i].y * m_fRatio;
			y2 = top + pLko->m_ptBasePos[i+1].y * m_fRatio;
			m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
		}
		x1 = left + pLko->m_ptBasePos[3].x * m_fRatio;
		x2 = left + pLko->m_ptBasePos[0].x * m_fRatio;
		y1 = top + pLko->m_ptBasePos[3].y * m_fRatio;
		y2 = top + pLko->m_ptBasePos[0].y * m_fRatio;
		m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
		
		x1 = left + pLko->m_ptBasePos[2].x * m_fRatio;
		y1 = top + pLko->m_ptBasePos[2].y * m_fRatio;
		m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
	}
	return true;
}

bool CMapEditorView::DrawDistortObjectInMouse(const std::wstring &name)
{
	if (m_nBrushType > 0)
		CheckDistortObjectLoad(name);
	

	Nuclear::PDistortionObject* pObj = m_DistortObjectMap[name];
	if (!pObj)
		return false;
	std::wstring imgFilename = pObj->m_distortionObj.strImgFileName;
	imgFilename.erase(imgFilename.size()-4, 4);
	CheckPictureLoad(imgFilename);
	
	LPImgInfo pImgInfo = m_ImgInfoMap[imgFilename];
	m_MouseObjectPosition.x = static_cast<int>(m_PointMouse.x / m_fRatio);
	m_MouseObjectPosition.y = static_cast<int>(m_PointMouse.y / m_fRatio);
	if (IsSHIFTPressed())
	{
		Nuclear::CRECT crect;
		crect.left = m_MouseObjectPosition.x + m_DrawScrollPosition.x - pImgInfo->m_CenterPoint.x;
		crect.top = m_MouseObjectPosition.y + m_DrawScrollPosition.y - pImgInfo->m_CenterPoint.y;
		crect.right = crect.left + pImgInfo->m_nFileWidth;
		crect.bottom = crect.top + pImgInfo->m_nFileHeight;
		CalculateAdjustMouseObject(crect, false);
	}
	m_MouseObjectPosition += m_MagnetAdjust;
	m_MouseObjectPosition.x -= pImgInfo->m_CenterPoint.x;
	m_MouseObjectPosition.y -= pImgInfo->m_CenterPoint.y;
	return drawElement(pImgInfo, m_fRatio * m_MouseObjectPosition.x, m_fRatio * m_MouseObjectPosition.y, 
		m_nLayerEditID==LEI_MID, m_nAttachColor);
}


bool CMapEditorView::DrawLinkedObjectInMouse(const std::wstring &name)
{
	if (m_nBrushType > 0)
	
	CheckLinkedObjectLoad(name);

	
	Nuclear::PLinkedObject *pLko = m_LinkedObjectMap[name];
	m_MouseObjectPosition.x = static_cast<int>(m_PointMouse.x / m_fRatio);
	m_MouseObjectPosition.y = static_cast<int>(m_PointMouse.y / m_fRatio);
	if (IsSHIFTPressed())
	{
		Nuclear::CRECT crect = pLko->m_rctMaxBox;
		crect += m_MouseObjectPosition + m_DrawScrollPosition;
		crect.Widen(DYNAMIC_MAX_RECT_WIDEN);
		CalculateAdjustMouseObject(crect, false);
	}
	m_MouseObjectPosition += m_MagnetAdjust;
	return DrawStaticLinkedObjectTree(pLko, m_fRatio * m_MouseObjectPosition.x, m_fRatio * m_MouseObjectPosition.y, 
		m_nLayerEditID==LEI_MID, m_nAttachColor);
	return false;
}

bool CMapEditorView::DrawEffectInMouse(const std::wstring &name)
{
	static DWORD tickCount = 0;
	if (fabs(m_fRatio - 1.0f) < 0.0001f)
	{
		if (m_MouseEffectName != name || m_pMouseEffect == NULL)
		{
			if (m_pMouseEffect)
				delete m_pMouseEffect;
			m_pMouseEffect = GetEffectManager()->CreateEffect(name, true);
			if (m_pMouseEffect == NULL)
				return false;
			m_pMouseEffect->AssureResource(false);
			if (m_EffectInfoMap.find(name) == m_EffectInfoMap.end())
			{
				sEffInfo &info = m_EffectInfoMap[name];
				info.m_Rect = m_pMouseEffect->GetRelBouningBox();
				m_pMouseEffect->GetBase(info.m_Base);
			}
			m_MouseEffectName = name;
			m_pMouseEffect->SetDirection(Nuclear::XPDIR_TOP);
			m_pMouseEffect->Play();
		}

		m_MouseObjectPosition.x = static_cast<int>(m_PointMouse.x / m_fRatio);
		m_MouseObjectPosition.y = static_cast<int>(m_PointMouse.y / m_fRatio);
		if (IsSHIFTPressed())
		{
			Nuclear::CRECT crect = m_pMouseEffect->GetRelBouningBox();
			crect += m_MouseObjectPosition + m_DrawScrollPosition;
			CalculateAdjustMouseObject(crect, false);
		}
		m_MouseObjectPosition += m_MagnetAdjust;
		

		m_pMouseEffect->SetLocation(m_PointMouse + m_DrawScrollPosition);
		
		DWORD newTC = GetTick();
		if (newTC-tickCount > 1000)
			tickCount = newTC;
		m_pMouseEffect->Update(newTC-tickCount);
		tickCount = newTC;
		m_pMouseEffect->Render(NULL);
		return true;
	}
	return false;
}

bool CMapEditorView::DrawElementInMouse(const std::wstring &name)
{
	CheckPictureLoad(name);
	
	LPImgInfo pImgInfo = m_ImgInfoMap[name];
	m_MouseObjectPosition.x = static_cast<int>(m_PointMouse.x / m_fRatio);
	m_MouseObjectPosition.y = static_cast<int>(m_PointMouse.y / m_fRatio);
	if (IsSHIFTPressed())
	{
		Nuclear::CRECT crect;
		crect.left = m_MouseObjectPosition.x + m_DrawScrollPosition.x - pImgInfo->m_CenterPoint.x;
		crect.top = m_MouseObjectPosition.y + m_DrawScrollPosition.y - pImgInfo->m_CenterPoint.y;
		crect.right = crect.left + pImgInfo->m_nFileWidth;
		crect.bottom = crect.top + pImgInfo->m_nFileHeight;
		CalculateAdjustMouseObject(crect, false);
	}
	m_MouseObjectPosition += m_MagnetAdjust;
	m_MouseObjectPosition.x -= pImgInfo->m_CenterPoint.x;
	m_MouseObjectPosition.y -= pImgInfo->m_CenterPoint.y;
	return drawElement(pImgInfo, m_fRatio * m_MouseObjectPosition.x, m_fRatio * m_MouseObjectPosition.y, 
		m_nLayerEditID==LEI_MID, m_nAttachColor);
}

bool CMapEditorView::DrawSelectLinkedObjectTreeInMouse(void)
{
	int layerID;
	Nuclear::PLinkedObject *pLko;
	layerID = m_nLayerEditID - FIRST_ELEMENT_LAYER;
	if (m_nLayerEditID>LAST_ELEMENT_LAYER || m_nLayerEditID<FIRST_ELEMENT_LAYER)
		return false;

	CPoint adj = m_PointMouse-m_MouseDownPoint;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	std::set<unsigned int>::iterator iter = m_SelectedObjects.begin();
	bool drawBase = m_nLayerEditID == LEI_MID;
	if (m_SelectedObjects.size() == 1 && IsSHIFTPressed())
	{
		if (CLayerView::GetObjectType(*iter) != LOT_LINKEDOBJECT)
			return false;
		Nuclear::SubMap::sObjInfo &dynamicInfo = map.GetObjInfo(PMap::MAP_OBJ_LINKEDOBJ, layerID, GetObjectId(*iter));
		const std::wstring &name = map.GetObjFileName(PMap::MAP_OBJ_LINKEDOBJ, dynamicInfo.m_MapKey);
		
		pLko = m_LinkedObjectMap[name];
		if (pLko == NULL)
			return false;
		m_MouseObjectPosition.x = static_cast<int>(m_PointMouse.x / m_fRatio);
		m_MouseObjectPosition.y = static_cast<int>(m_PointMouse.y / m_fRatio);
		Nuclear::CRECT crect = pLko->m_rctMaxBox;
		crect += m_MouseObjectPosition + m_DrawScrollPosition;
		crect.Widen(DYNAMIC_MAX_RECT_WIDEN);
		CalculateAdjustMouseObject(crect, false);
		m_MouseObjectPosition += m_MagnetAdjust;
		return DrawStaticLinkedObjectTree(pLko, (m_MouseObjectPosition.x - m_DrawScrollPosition.x) * m_fRatio, 
			(m_MouseObjectPosition.y - m_DrawScrollPosition.y) * m_fRatio, drawBase, dynamicInfo.m_AttachColor.data);
	} else {
		for (;iter!=m_SelectedObjects.end();iter++)
		{
			if (CLayerView::GetObjectType(*iter) != LOT_LINKEDOBJECT)
				continue;
			Nuclear::SubMap::sObjInfo &dynamicInfo = map.GetObjInfo(PMap::MAP_OBJ_LINKEDOBJ, layerID, CLayerView::GetObjectId(*iter));
			const std::wstring &name = map.GetObjFileName(PMap::MAP_OBJ_LINKEDOBJ, dynamicInfo.m_MapKey);
			pLko = m_LinkedObjectMap[name];
			DrawStaticLinkedObjectTree(pLko, (dynamicInfo.m_Position.x - m_DrawScrollPosition.x) * m_fRatio + adj.x,
				(dynamicInfo.m_Position.y - m_DrawScrollPosition.y) * m_fRatio + adj.y, drawBase, dynamicInfo.m_AttachColor.data);
		}
	}
	return true;
}

bool CMapEditorView::DrawSelectElementInMouse(void)
{
	int layerID;
	LPImgInfo pImgInfo;
	layerID = m_nLayerEditID - FIRST_ELEMENT_LAYER;
	if (m_nLayerEditID>LAST_ELEMENT_LAYER || m_nLayerEditID<FIRST_ELEMENT_LAYER)
		return false;
	CPoint adj = m_PointMouse-m_MouseDownPoint;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	Nuclear::SortBaseIDSet::iterator iter = m_SelectedObjects.begin();
	bool drawBase = m_nLayerEditID == LEI_MID;
	if (m_SelectedObjects.size() == 1 && IsSHIFTPressed())
	{
		if (CLayerView::GetObjectType(*iter) != LOT_ELEMENT)
			return false;
		Nuclear::SubMap::sObjInfo &picInfo = map.GetObjInfo(PMap::MAP_OBJ_ELEMENT, layerID, GetObjectId(*iter));
		const std::wstring &name = map.GetObjFileName(PMap::MAP_OBJ_ELEMENT, picInfo.m_MapKey);
		pImgInfo = m_ImgInfoMap[name];
		if (pImgInfo == NULL)
			return false;
		m_MouseObjectPosition.x = picInfo.m_Rect.left + static_cast<int>(adj.x / m_fRatio);
		m_MouseObjectPosition.y = picInfo.m_Rect.top + static_cast<int>(adj.y / m_fRatio);
		Nuclear::CRECT crect;
		crect.left = m_MouseObjectPosition.x;
		crect.top = m_MouseObjectPosition.y;
		crect.right = crect.left + picInfo.m_Rect.right - picInfo.m_Rect.left;
		crect.bottom = crect.top + picInfo.m_Rect.bottom - picInfo.m_Rect.top;
		CalculateAdjustMouseObject(crect, true);
		m_MouseObjectPosition += m_MagnetAdjust;
		return drawElement(pImgInfo, (m_MouseObjectPosition.x - m_DrawScrollPosition.x) * m_fRatio, 
			(m_MouseObjectPosition.y - m_DrawScrollPosition.y) * m_fRatio, drawBase, picInfo.m_AttachColor.data);
	} else {
		Nuclear::FRECT rect;
		for (;iter!=m_SelectedObjects.end();iter++)
		{
			if (CLayerView::GetObjectType(*iter) != LOT_ELEMENT)
				continue;
			Nuclear::SubMap::sObjInfo &picInfo = map.GetObjInfo(PMap::MAP_OBJ_ELEMENT, layerID, GetObjectId(*iter));
			const std::wstring &name = map.GetObjFileName(PMap::MAP_OBJ_ELEMENT, picInfo.m_MapKey);
			pImgInfo = m_ImgInfoMap[name];
			rect.left = (picInfo.m_Rect.left - m_DrawScrollPosition.x) * m_fRatio + adj.x;
			rect.right = (picInfo.m_Rect.right - m_DrawScrollPosition.x) * m_fRatio + adj.x;
			rect.top = (picInfo.m_Rect.top - m_DrawScrollPosition.y) * m_fRatio + adj.y;
			rect.bottom = (picInfo.m_Rect.bottom - m_DrawScrollPosition.y) * m_fRatio + adj.y;
			drawElement(pImgInfo, rect, drawBase, picInfo.m_AttachColor.data);
		}
	}
	return true;
}

bool CMapEditorView::DrawSelectDistortInMouse(void)
{
	int layerID;
	LPImgInfo pImgInfo;
	layerID = m_nLayerEditID - FIRST_ELEMENT_LAYER;
	if (m_nLayerEditID>LAST_ELEMENT_LAYER || m_nLayerEditID<FIRST_ELEMENT_LAYER)
		return false;
	CPoint adj = m_PointMouse-m_MouseDownPoint;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	Nuclear::SortBaseIDSet::iterator iter = m_SelectedObjects.begin();
	bool drawBase = m_nLayerEditID == LEI_MID;
	if (m_SelectedObjects.size() == 1 && IsSHIFTPressed())
	{
		if (CLayerView::GetObjectType(*iter) != LOT_DISTORT)
			return false;
		Nuclear::SubMap::sObjInfo &objInfo = map.GetObjInfo(PMap::MAP_OBJ_DISTORT, layerID, GetObjectId(*iter));
		const std::wstring &name = map.GetObjFileName(PMap::MAP_OBJ_DISTORT, objInfo.m_MapKey);
		Nuclear::PDistortionObject* pObj = m_DistortObjectMap[name];
		if (!pObj)
			return false;
		std::wstring imgFilename = pObj->m_distortionObj.strImgFileName;
		imgFilename.erase(imgFilename.size()-4, 4);
		
		LPImgInfo pImgInfo = m_ImgInfoMap[imgFilename];

		if (pImgInfo == NULL)
			return false;
		m_MouseObjectPosition.x = objInfo.m_Rect.left + static_cast<int>(adj.x / m_fRatio);
		m_MouseObjectPosition.y = objInfo.m_Rect.top + static_cast<int>(adj.y / m_fRatio);
		Nuclear::CRECT crect;
		crect.left = m_MouseObjectPosition.x;
		crect.top = m_MouseObjectPosition.y;
		crect.right = crect.left + objInfo.m_Rect.right - objInfo.m_Rect.left;
		crect.bottom = crect.top + objInfo.m_Rect.bottom - objInfo.m_Rect.top;
		CalculateAdjustMouseObject(crect, true);
		m_MouseObjectPosition += m_MagnetAdjust;
		return drawElement(pImgInfo, (m_MouseObjectPosition.x - m_DrawScrollPosition.x) * m_fRatio, 
			(m_MouseObjectPosition.y - m_DrawScrollPosition.y) * m_fRatio, drawBase, objInfo.m_AttachColor.data);
	} else {
		Nuclear::FRECT rect;
		for (;iter!=m_SelectedObjects.end();iter++)
		{
			if (CLayerView::GetObjectType(*iter) != LOT_DISTORT)
				continue;
			Nuclear::SubMap::sObjInfo &objInfo = map.GetObjInfo(PMap::MAP_OBJ_DISTORT, layerID, GetObjectId(*iter));
			const std::wstring &name = map.GetObjFileName(PMap::MAP_OBJ_DISTORT, objInfo.m_MapKey);

			Nuclear::PDistortionObject* pObj = m_DistortObjectMap[name];
			if (!pObj)
				continue;
			std::wstring imgFilename = pObj->m_distortionObj.strImgFileName;
			imgFilename.erase(imgFilename.size()-4, 4);
			pImgInfo = m_ImgInfoMap[imgFilename];
			rect.left = (objInfo.m_Rect.left - m_DrawScrollPosition.x) * m_fRatio + adj.x;
			rect.right = (objInfo.m_Rect.right - m_DrawScrollPosition.x) * m_fRatio + adj.x;
			rect.top = (objInfo.m_Rect.top - m_DrawScrollPosition.y) * m_fRatio + adj.y;
			rect.bottom = (objInfo.m_Rect.bottom - m_DrawScrollPosition.y) * m_fRatio + adj.y;
			drawElement(pImgInfo, rect, drawBase, objInfo.m_AttachColor.data);
		}
	}
	return true;
}

bool CMapEditorView::DrawSelectEffectInMouse(void)
{
	
	if ((fabs(m_fRatio - 1.0f) > 0.0001f))
		return false;
	int layerID = m_nLayerEditID - FIRST_ELEMENT_LAYER;
	if (m_nLayerEditID>LAST_ELEMENT_LAYER || m_nLayerEditID<FIRST_ELEMENT_LAYER)
		return false;
	CPoint adj = m_PointMouse-m_MouseDownPoint;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	Nuclear::SortBaseIDSet::iterator iter = m_SelectedObjects.begin();
	Nuclear::Location loc;
	EffectMap::iterator effIter;
	Nuclear::Effect* pEffect;
	for (;iter!=m_SelectedObjects.end();iter++)
	{
		if (CLayerView::GetObjectType(*iter) != LOT_EFFECT)
			continue;
		Nuclear::SubMap::sObjInfo &effectInfo = map.GetObjInfo(PMap::MAP_OBJ_EFFECT, layerID, GetObjectId(*iter));
		effIter = m_EffectArray[layerID].find(effectInfo.m_ObjectId);
		if (effIter == m_EffectArray[layerID].end())
		{
			const std::wstring &effectName = map.GetObjFileName(PMap::MAP_OBJ_EFFECT, effectInfo.m_MapKey);
			pEffect = GetEffectManager()->CreateEffect(effectName, true);
			if (pEffect == NULL)
				continue;
			pEffect->AssureResource(false);
			if (m_EffectInfoMap.find(effectName) == m_EffectInfoMap.end())
			{
				sEffInfo &info = m_EffectInfoMap[effectName];
				info.m_Rect = pEffect->GetRelBouningBox();
				pEffect->GetBase(info.m_Base);
			}
			pEffect->SetDirection(Nuclear::XPDIR_TOP);
			m_EffectArray[layerID][effectInfo.m_ObjectId] = pEffect;
		} else {
			pEffect = effIter->second;
			if( pEffect == NULL )
				continue;
		}
		loc = effectInfo.m_Position + adj;
		pEffect->SetLocation(loc);
		/*DWORD newTC = GetTick();
		if (newTC-tickCount > 1000)
			tickCount = newTC;
		pEffect->Update(newTC-tickCount);*/
		pEffect->Render(NULL);


	}
	return true;
}

bool CMapEditorView::drawSelectedRects(void)
{
	
	if (m_bIsShowMouseRect)
	{
		
		m_pRenderer->DrawLine((m_MouseRect.left - m_DrawScrollPosition.x)  * m_fRatio, 
			(m_MouseRect.top - m_DrawScrollPosition.y) * m_fRatio, (m_MouseRect.right - m_DrawScrollPosition.x) * m_fRatio, 
			(m_MouseRect.top - m_DrawScrollPosition.y) * m_fRatio, 0xFFFFFFFF);
		m_pRenderer->DrawLine((m_MouseRect.left - m_DrawScrollPosition.x) * m_fRatio + 1,
			(m_MouseRect.top - m_DrawScrollPosition.y) * m_fRatio + 1, (m_MouseRect.right - m_DrawScrollPosition.x) * m_fRatio + 1,
			(m_MouseRect.top - m_DrawScrollPosition.y) * m_fRatio + 1, 0xFF000000);
		
		m_pRenderer->DrawLine((m_MouseRect.right - m_DrawScrollPosition.x) * m_fRatio,
			(m_MouseRect.top - m_DrawScrollPosition.y) * m_fRatio, (m_MouseRect.right - m_DrawScrollPosition.x) * m_fRatio,
			(m_MouseRect.bottom - m_DrawScrollPosition.y) * m_fRatio, 0xFFFFFFFF);
		m_pRenderer->DrawLine((m_MouseRect.right - m_DrawScrollPosition.x) * m_fRatio + 1,
			(m_MouseRect.top - m_DrawScrollPosition.y) * m_fRatio + 1, (m_MouseRect.right - m_DrawScrollPosition.x) * m_fRatio + 1,
			(m_MouseRect.bottom - m_DrawScrollPosition.y) * m_fRatio + 1, 0xFF000000);
		
		m_pRenderer->DrawLine((m_MouseRect.left - m_DrawScrollPosition.x) * m_fRatio,
			(m_MouseRect.bottom - m_DrawScrollPosition.y) * m_fRatio, (m_MouseRect.right - m_DrawScrollPosition.x) * m_fRatio,
			(m_MouseRect.bottom - m_DrawScrollPosition.y) * m_fRatio, 0xFFFFFFFF);
		m_pRenderer->DrawLine((m_MouseRect.left - m_DrawScrollPosition.x) * m_fRatio + 1,
			(m_MouseRect.bottom - m_DrawScrollPosition.y) * m_fRatio + 1, (m_MouseRect.right - m_DrawScrollPosition.x) * m_fRatio + 1,
			(m_MouseRect.bottom - m_DrawScrollPosition.y) * m_fRatio + 1, 0xFF000000);
		
		m_pRenderer->DrawLine((m_MouseRect.left - m_DrawScrollPosition.x) * m_fRatio,
			(m_MouseRect.top - m_DrawScrollPosition.y) * m_fRatio, (m_MouseRect.left - m_DrawScrollPosition.x) * m_fRatio,
			(m_MouseRect.bottom - m_DrawScrollPosition.y) * m_fRatio, 0xFFFFFFFF);
		m_pRenderer->DrawLine((m_MouseRect.left - m_DrawScrollPosition.x) * m_fRatio + 1,
			(m_MouseRect.top - m_DrawScrollPosition.y) * m_fRatio + 1, (m_MouseRect.left - m_DrawScrollPosition.x) * m_fRatio + 1,
			(m_MouseRect.bottom - m_DrawScrollPosition.y) * m_fRatio + 1, 0xFF000000);

	} else {
		
	}
	
	if (m_bIsShowSelectElementRect)
	{
		std::vector<Nuclear::CRECT>::iterator iter = m_SelectedRects.begin();
		for(;iter!=m_SelectedRects.end();iter++)
		{
			m_pRenderer->DrawBox((iter->left - m_DrawScrollPosition.x) * m_fRatio, (iter->top - m_DrawScrollPosition.y) * m_fRatio,
				(iter->right - m_DrawScrollPosition.x) * m_fRatio, (iter->bottom - m_DrawScrollPosition.y) * m_fRatio, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);
			m_pRenderer->DrawBox((iter->left - m_DrawScrollPosition.x) * m_fRatio + 1, (iter->top - m_DrawScrollPosition.y) * m_fRatio + 1,
				(iter->right - m_DrawScrollPosition.x) * m_fRatio + 1, (iter->bottom - m_DrawScrollPosition.y) * m_fRatio + 1, 0xFF000000, Nuclear::XPFM_WIREFRAME);
		}
	}


	
	if (m_bIsSelectedBase && fabs(m_fRatio - 1.0f) < 0.0001f)
	{
		int i, j, x, y;
		float x1, y1;
		for (i=0;i<4;i++)
		{
			x = static_cast<LONG>(m_fPosX[i]) - m_DrawScrollPosition.x;
			y = static_cast<LONG>(m_fPosY[i]) - m_DrawScrollPosition.y;
			m_pRenderer->DrawBox(static_cast<float>(x-3), static_cast<float>(y-3), static_cast<float>(x+4), 
				static_cast<float>(y+4), 0xFF00FF00);
			m_pRenderer->DrawBox(static_cast<float>(x-4), static_cast<float>(y-4), 
				static_cast<float>(x+4), static_cast<float>(y+4), 0xFF000000, Nuclear::XPFM_WIREFRAME);
		}
		for (i=0;i<4;i++)
		{
			j = i+1>3?0:i+1;
			x = static_cast<LONG>(m_fPosX[i]) - m_DrawScrollPosition.x;
			y = static_cast<LONG>(m_fPosY[i]) - m_DrawScrollPosition.y;
			x1 = static_cast<float>(m_fPosX[j]) - m_DrawScrollPosition.x;
			y1 = static_cast<float>(m_fPosY[j]) - m_DrawScrollPosition.y;
			m_pRenderer->DrawLine(static_cast<float>(x), static_cast<float>(y), x1, y1, 0xFFFF0000);
		}
	}

	
	if (m_nLayerEditID == LEI_GAMING && m_nEditMode == EMT_PERFORMANCE_DLG)
	{
		if (m_pPerformanceDlg)
		{
			Nuclear::CRECT rect = m_pPerformanceDlg->GetViewportRect();
			m_pRenderer->DrawBox((rect.left - m_DrawScrollPosition.x) * m_fRatio, (rect.top - m_DrawScrollPosition.y) * m_fRatio,
				(rect.right - m_DrawScrollPosition.x) * m_fRatio, (rect.bottom - m_DrawScrollPosition.y) * m_fRatio, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);
			m_pRenderer->DrawBox((rect.left - m_DrawScrollPosition.x) * m_fRatio + 1, (rect.top - m_DrawScrollPosition.y) * m_fRatio + 1,
				(rect.right - m_DrawScrollPosition.x) * m_fRatio + 1, (rect.bottom - m_DrawScrollPosition.y) * m_fRatio + 1, 0xFFFF0000, Nuclear::XPFM_WIREFRAME);
		}
	}
	return true;
}

void CMapEditorView::DrawMouseObj(int layer)
{
	static bool (CMapEditorView::*pDrawInMouse[PMap::MAP_OBJ_COUNT])(const std::wstring &name) =
	{
		&CMapEditorView::DrawElementInMouse, &CMapEditorView::DrawEffectInMouse, &CMapEditorView::DrawLinkedObjectInMouse, &CMapEditorView::DrawDistortObjectInMouse
	};
	if (m_nLayerEditID - FIRST_ELEMENT_LAYER == layer)
	{
		
		switch (m_nEditMode)
		{
		case EMT_OBJECT_NORMAL:
			{
				if (m_nBrushType > 0)
				{
					PMap::MAP_OBJ_TYPE type;
					std::wstring name = m_SelectObjectDlg.GetFilenameAndType(m_nBrushType, type);
					(this->*pDrawInMouse[type])(name);
				}
			}
			break;
		case EMT_OBJECT_DRAG:
			DrawSelectElementInMouse();
			DrawSelectEffectInMouse();
			DrawSelectLinkedObjectTreeInMouse();
			DrawSelectDistortInMouse();
			break;
		default:
			break;
		}
	}
}

bool CMapEditorView::drawWaterAreaObjs(void)
{
	if (m_nLayerEditID != LEI_WATER)
		return false;
	if (m_nEditMode == EMT_WATER_TILE)
		return false;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	const Nuclear::SubMap::WaterAreaInfoMap &waterArea = pmap.GetWaterAreaInfos();
	Nuclear::SubMap::WaterAreaInfoMap::const_iterator iter = waterArea.begin();
	Nuclear::SubMap::WaterAreaMap::const_iterator rectIter, rectIterEnd;
	Nuclear::SubMap::WaterAreaID id;
	Nuclear::CRECT viewport = GetViewport();
	Nuclear::FRECT rect, srect;
	CPoint adj = m_PointMouse-m_MouseDownPoint;
	for (;iter!=waterArea.end();iter++)
	{
		rectIter = iter->second.areas.begin();
		rectIterEnd = iter->second.areas.end();
		for (;rectIter!=rectIterEnd;++rectIter)
		{
			id.infoID = iter->first;
			id.areaID = rectIter->first;
			if (m_nLayerEditID == LEI_WATER && m_nEditMode == EMT_WATER_IMMERSION_CHANGE_SIZE && 
				m_SelectedObjects.find(id.id) != m_SelectedObjects.end())
				continue;
			if (Nuclear::IsRectCross(rectIter->second, viewport))
			{
				rect = rectIter->second.ToFRECT();
				rect.top -= m_DrawScrollPosition.y;
				rect.bottom -= m_DrawScrollPosition.y;
				rect.left -= m_DrawScrollPosition.x;
				rect.right -= m_DrawScrollPosition.x;
				srect = rect;
				srect.top += iter->second.border;
				srect.bottom -= iter->second.border;
				srect.left += iter->second.border;
				srect.right -= iter->second.border;
				rect.left *= m_fRatio;
				rect.top *= m_fRatio;
				rect.bottom *= m_fRatio;
				rect.right *= m_fRatio;
				srect.left *= m_fRatio;
				srect.top *= m_fRatio;
				srect.bottom *= m_fRatio;
				srect.right *= m_fRatio;
				if (m_nEditMode == EMT_WATER_IMMERSION_DRAG && m_nLayerEditID == LEI_WATER && 
					m_SelectedObjects.find(id.id) != m_SelectedObjects.end())
				{
					rect.top += adj.y;
					rect.bottom += adj.y;
					rect.left += adj.x;
					rect.right += adj.x;
					srect.top += adj.y;
					srect.bottom += adj.y;
					srect.left += adj.x;
					srect.right += adj.x;
				}
				m_pRenderer->DrawBox(rect, 0x4000FF00);
				if (srect.left < srect.right && srect.top < srect.bottom)
					m_pRenderer->DrawBox(srect, 0x400000FF);
			}
		}
	}
	
	if (m_nLayerEditID == LEI_WATER && m_nEditMode == EMT_WATER_IMMERSION_CHANGE_SIZE)
	{
		id.id = *m_SelectedObjects.begin();
		iter = waterArea.find(id.infoID);
		if (iter != waterArea.end())
		{
			rectIter = iter->second.areas.find(id.areaID);
			if (rectIter != iter->second.areas.end())
			{
				
				float adjx, adjy;
				adjx = static_cast<float>(adj.x) / m_fRatio;
				adjy = static_cast<float>(adj.y) / m_fRatio;
				rect.left = static_cast<float>(m_InitialPoint.x);
				rect.top = static_cast<float>(m_InitialPoint.y);
				rect.right = static_cast<float>(m_ToChangePoint.x) + adjx;
				rect.bottom = static_cast<float>(m_ToChangePoint.y) + adjy;
				int WGreater0, HGreater0;
				WGreater0 = rect.Width() > 0 ? 1 : -1;
				HGreater0 = rect.Height() > 0 ? 1 : -1;
				HCURSOR hCurs;
				if (WGreater0 * HGreater0 > 0)
				{
					hCurs = LoadCursor(NULL, IDC_SIZENWSE);
				} else {
					hCurs = LoadCursor(NULL, IDC_SIZENESW);
				}
				SetCursor(hCurs);
				Nuclear::NormalizeRect(rect);
				rect.top -= m_DrawScrollPosition.y;
				rect.bottom -= m_DrawScrollPosition.y;
				rect.left -= m_DrawScrollPosition.x;
				rect.right -= m_DrawScrollPosition.x;
				srect = rect;
				srect.top += iter->second.border;
				srect.bottom -= iter->second.border;
				srect.left += iter->second.border;
				srect.right -= iter->second.border;
				rect.left *= m_fRatio;
				rect.top *= m_fRatio;
				rect.bottom *= m_fRatio;
				rect.right *= m_fRatio;
				srect.left *= m_fRatio;
				srect.top *= m_fRatio;
				srect.bottom *= m_fRatio;
				srect.right *= m_fRatio;
				m_pRenderer->DrawBox(rect, 0x4000FF00);
				if (srect.left < srect.right && srect.top < srect.bottom)
					m_pRenderer->DrawBox(srect, 0x400000FF);
			}
		}
	}
	return true;
}


bool CMapEditorView::drawTransObjs(void)
{
	if (!m_bIsShowLayers[LEI_TRANS] && m_nLayerEditID != LEI_TRANS)
		return false;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &map = pDoc->GetToolsMap();
	const Nuclear::SubMap::TransObjInfoMap &transObjs = map.GetTransInfos();
	Nuclear::SubMap::TransObjInfoMap::const_iterator iter = transObjs.begin();
	Nuclear::CRECT viewport = GetViewport();
	Nuclear::FRECT rect;
	CPoint adj = m_PointMouse-m_MouseDownPoint;
	for (;iter!=transObjs.end();iter++)
	{
		if (m_nLayerEditID == LEI_TRANS && m_nEditMode == EMT_TRANSPARENT_OBJECT_CHANGE_SIZE && 
			m_SelectedObjects.find(iter->first) != m_SelectedObjects.end())
			continue;
		if (Nuclear::IsRectCross(iter->second.picRect, viewport))
		{
			rect = iter->second.picRect.ToFRECT();
			rect.top -= m_DrawScrollPosition.y;
			rect.bottom -= m_DrawScrollPosition.y;
			rect.left -= m_DrawScrollPosition.x;
			rect.right -= m_DrawScrollPosition.x;
			rect.left *= m_fRatio;
			rect.top *= m_fRatio;
			rect.bottom *= m_fRatio;
			rect.right *= m_fRatio;
			if (m_nEditMode == EMT_TRANSPARENT_OBJECT_DRAG && m_nLayerEditID == LEI_TRANS && 
				m_SelectedObjects.find(iter->first) != m_SelectedObjects.end())
			{

				rect.top += adj.y;
				rect.bottom += adj.y;
				rect.left += adj.x;
				rect.right += adj.x;
			}
			m_pRenderer->DrawBox(rect, 0x80FFFF00);
			
			
		}
	}

	if (m_nLayerEditID == LEI_TRANS && m_nEditMode == EMT_TRANSPARENT_OBJECT_CHANGE_SIZE)
	{
		iter = transObjs.find(*m_SelectedObjects.begin());
		if (iter != transObjs.end())
		{
			RECT standardRect;
			standardRect.left = 0;
			standardRect.top = 0;
			standardRect.right = iter->second.width;
			standardRect.bottom = iter->second.height;
			
			float adjx, adjy;
			adjx = static_cast<float>(adj.x) / m_fRatio;
			adjy = static_cast<float>(adj.y) / m_fRatio;
			rect.left = static_cast<float>(m_InitialPoint.x);
			rect.top = static_cast<float>(m_InitialPoint.y);
			rect.right = static_cast<float>(m_ToChangePoint.x) + adjx;
			rect.bottom = static_cast<float>(m_ToChangePoint.y) + adjy;
			int width, height;
			int WGreater0, HGreater0;
			CheckScalRect(standardRect, static_cast<int>(rect.Width()), static_cast<int>(rect.Height()), width, height);
			WGreater0 = rect.Width() > 0 ? 1 : -1;
			HGreater0 = rect.Height() > 0 ? 1 : -1;
			rect.right = rect.left + width * WGreater0;
			rect.bottom = rect.top + height * HGreater0;
			HCURSOR hCurs;
			if (WGreater0 * HGreater0 > 0)
			{
				hCurs = LoadCursor(NULL, IDC_SIZENWSE);
			} else {
				hCurs = LoadCursor(NULL, IDC_SIZENESW);
			}
			SetCursor(hCurs);
			Nuclear::NormalizeRect(rect);
			rect.top -= m_DrawScrollPosition.y;
			rect.bottom -= m_DrawScrollPosition.y;
			rect.left -= m_DrawScrollPosition.x;
			rect.right -= m_DrawScrollPosition.x;
			rect.left *= m_fRatio;
			rect.top *= m_fRatio;
			rect.bottom *= m_fRatio;
			rect.right *= m_fRatio;
			m_pRenderer->DrawBox(rect, 0x80FFFF00);
			
		}
	}
	return true;
}

bool CMapEditorView::drawTriggerObjs()
{
	if (!m_bIsShowLayers[LEI_TRIGGER] && m_nLayerEditID != LEI_TRIGGER)
		return false;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	const Nuclear::SubMap::TriggerInfoMap &triggers = pmap.GetTriggerInfos();
	Nuclear::SubMap::TriggerInfoMap::const_iterator iter = triggers.begin();
	Nuclear::SubMap::TriggerAreaMap::const_iterator rectIter, rectIterEnd;
	Nuclear::SubMap::TriggerID id;
	Nuclear::CRECT viewport = GetViewport();
	Nuclear::FRECT rect;
	CPoint adj = m_PointMouse-m_MouseDownPoint;
	for (;iter!=triggers.end();++iter)
	{
		id.infoID = iter->first;
		if (m_HiddenTriggerArea.find(id.infoID) != m_HiddenTriggerArea.end())
			continue;
		rectIter = iter->second.areas.begin();
		rectIterEnd = iter->second.areas.end();
		for (;rectIter!=rectIterEnd;++rectIter)
		{
			id.areaID = rectIter->first;
			if (m_nLayerEditID == LEI_TRIGGER && m_nEditMode == EMT_TRIGGER_CHANGE_SIZE && 
				m_SelectedObjects.find(id.id) != m_SelectedObjects.end())
				continue;
			if (Nuclear::IsRectCross(rectIter->second, viewport))
			{
				rect = rectIter->second.ToFRECT();
				rect.top -= m_DrawScrollPosition.y;
				rect.bottom -= m_DrawScrollPosition.y;
				rect.left -= m_DrawScrollPosition.x;
				rect.right -= m_DrawScrollPosition.x;
				rect.left *= m_fRatio;
				rect.top *= m_fRatio;
				rect.bottom *= m_fRatio;
				rect.right *= m_fRatio;
				if (m_nEditMode == EMT_TRIGGER_DRAG && m_nLayerEditID == LEI_TRIGGER && 
					m_SelectedObjects.find(id.id) != m_SelectedObjects.end())
				{
					rect.top += adj.y;
					rect.bottom += adj.y;
					rect.left += adj.x;
					rect.right += adj.x;
				}
				m_pRenderer->DrawBox(rect, 0x80FFFF00);
			}
		}
	}

	if (m_nLayerEditID == LEI_TRIGGER && m_nEditMode == EMT_TRIGGER_CHANGE_SIZE)
	{
		id.id = *m_SelectedObjects.begin();
		iter = triggers.find(id.infoID);
		if (iter != triggers.end())
		{
			rectIter = iter->second.areas.find(id.areaID);
			if (rectIter != iter->second.areas.end())
			{
				
				float adjx, adjy;
				adjx = static_cast<float>(adj.x) / m_fRatio;
				adjy = static_cast<float>(adj.y) / m_fRatio;
				rect.left = static_cast<float>(m_InitialPoint.x);
				rect.top = static_cast<float>(m_InitialPoint.y);
				rect.right = static_cast<float>(m_ToChangePoint.x) + adjx;
				rect.bottom = static_cast<float>(m_ToChangePoint.y) + adjy;
				int WGreater0, HGreater0;
				WGreater0 = rect.Width() > 0 ? 1 : -1;
				HGreater0 = rect.Height() > 0 ? 1 : -1;
				HCURSOR hCurs;
				if (WGreater0 * HGreater0 > 0)
				{
					hCurs = LoadCursor(NULL, IDC_SIZENWSE);
				} else {
					hCurs = LoadCursor(NULL, IDC_SIZENESW);
				}
				SetCursor(hCurs);
				Nuclear::NormalizeRect(rect);
				rect.top -= m_DrawScrollPosition.y;
				rect.bottom -= m_DrawScrollPosition.y;
				rect.left -= m_DrawScrollPosition.x;
				rect.right -= m_DrawScrollPosition.x;
				rect.left *= m_fRatio;
				rect.top *= m_fRatio;
				rect.bottom *= m_fRatio;
				rect.right *= m_fRatio;
				m_pRenderer->DrawBox(rect, 0x80FFFF00);
			}
		}
	}
	return true;
}

void CMapEditorView::UpdateSprite()
{
	static DWORD last = GetTick();
	DWORD now = GetTick();
	bool renew = false;
	for (std::vector<Sprite*>::iterator it = m_Sprites.begin(); it != m_Sprites.end(); ++it)
	{
		(*it)->tick(now, now - last);
		if ((*it)->IsMoving())
		{
			m_ScreenElements.RemoveSprite((*it));
			renew = true;
		}
	}
	last = now;
	if (renew)
	{
		m_ScreenElements.SetMustRenew();
	}

	/*if (m_pSelectedSprite != NULL)
	{
		Nuclear::Location loc = m_pSelectedSprite->GetLocation();
		loc.x += m_nSpriteVx;
		loc.y += m_nSpriteVy;
		m_pSelectedSprite->SetLocation(loc);
		if (m_nSpriteVx!=0 || m_nSpriteVy !=0)
		{
			m_ScreenElements.RemoveSprite(m_pSelectedSprite);
			m_ScreenElements.SetMustRenew();
		}
	}*/
}

void CMapEditorView::OnEditMapInfo()
{
	CRegionMapInfoDlg dlg;
	CMapEditorDoc* pDoc = GetDocument();
	CToolsMap& pmap = pDoc->GetToolsMap();
	int width, height;
	width = pmap.GetWidth();
	height = pmap.GetHeight();
	dlg.m_nWidth = width;
	dlg.m_nHeight = height;
	dlg.m_bIsUndergroundPalace = pmap.GetSign() & Nuclear::PMap::MAP_SIGN_UNDERGROUND_PALACE;
	for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
	{
		dlg.m_EffectName[i] = pmap.GetWaterName(i).c_str();
	}
	
	dlg.HideGroundType();
	if (IDOK == dlg.DoModal())
	{
		CAction* oper = new CAction(CAction::AT_CHANGE_CANVAS, m_nLayerEditID);
		oper->GetCanvasActionData()->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap;
		CMapEditorDoc::sModifyCanvasParam param;
		param.m_NewWidth = dlg.m_nWidth;
		param.m_NewHeight = dlg.m_nHeight;
		param.m_ModifyMode = dlg.m_nScaleDirect;
		bool changeEffectName = false;
		
		for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
		{
			param.m_EffectName[i] = dlg.m_EffectName[i].GetBuffer();
			dlg.m_EffectName[i].ReleaseBuffer();
			if ((!changeEffectName) && (pmap.GetWaterName(i) != param.m_EffectName[i]))
			{
				changeEffectName = true;
			}
		}
		if (pDoc->ModifyCanvas(param))
		{
			if (changeEffectName)
			{
				ResetWaterEffect();
				if (m_SmallMapDlg.m_hWnd)
				{
					m_SmallMapDlg.ResetWaterEffect();
				}
				if (m_SelectWaterDlg.m_hWnd)
				{
					m_SelectWaterDlg.RenewList();
				}
			}
			if (dlg.m_bIsUndergroundPalace)
			{
				pmap.AddSign(Nuclear::PMap::MAP_SIGN_UNDERGROUND_PALACE);
			} else {
				pmap.RemoveSign(Nuclear::PMap::MAP_SIGN_UNDERGROUND_PALACE);
			}
			oper->GetCanvasActionData()->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap;
			m_pOperatorList->DoOneAction(oper);
			SetRatio(m_fRatio);
			pDoc->SetModifiedFlag();
			m_SmallMapDlg.TryInvalidate(true);
			m_ScreenElements.ClearLayers();
			m_TreeMap.clear();
		} else {
			delete (oper);
		}
	}
}

void CMapEditorView::GetMouseRectForWaterMode(CPoint point, Nuclear::CRECT &mouseRect)
{
	if (m_nBrushSize % 2 == 0)
	{
		point.x -= Nuclear::SubMap::PGround::WATER_WIDTH / 2;
		point.y -= Nuclear::SubMap::PGround::WATER_HEIGHT / 2;
	}
	CPoint tmpPoint = m_DrawScrollPosition + point;
	tmpPoint.x %= Nuclear::SubMap::PGround::WATER_WIDTH;
	tmpPoint.y %= Nuclear::SubMap::PGround::WATER_HEIGHT;
	point -= tmpPoint;
	mouseRect.left = point.x;
	mouseRect.top = point.y;
	mouseRect.right = point.x + Nuclear::SubMap::PGround::WATER_WIDTH;
	mouseRect.bottom = point.y + Nuclear::SubMap::PGround::WATER_HEIGHT;
	mouseRect.right += Nuclear::SubMap::PGround::WATER_WIDTH * (m_nBrushSize / 2);
	mouseRect.bottom += Nuclear::SubMap::PGround::WATER_HEIGHT * (m_nBrushSize / 2);
	mouseRect.left -= Nuclear::SubMap::PGround::WATER_WIDTH * ((m_nBrushSize - 1) / 2);
	mouseRect.top -= Nuclear::SubMap::PGround::WATER_HEIGHT * ((m_nBrushSize - 1) / 2);
}

void CMapEditorView::GetMouseRectForTileTypeMode(CPoint point, Nuclear::CRECT &mouseRect, Nuclear::TILE_SIZE_TYPE sizeType)
{
	int tileWidth, tileHeight;
	GetTileWidthHeight(sizeType, tileWidth, tileHeight);
	if (m_nBrushSize % 2 == 0)
	{
		point.x -= tileWidth / 2;
		point.y -= tileHeight / 2;
	}
	CPoint tmpPoint = m_DrawScrollPosition + point;
	tmpPoint.x %= tileWidth;
	tmpPoint.y %= tileHeight;
	point -= tmpPoint;
	mouseRect.left = point.x;
	mouseRect.top = point.y;
	mouseRect.right = point.x + tileWidth;
	mouseRect.bottom = point.y + tileHeight;
	mouseRect.right += tileWidth * (m_nBrushSize / 2);
	mouseRect.bottom += tileHeight * (m_nBrushSize / 2);
	mouseRect.left -= tileWidth * ((m_nBrushSize - 1) / 2);
	mouseRect.top -= tileHeight * ((m_nBrushSize - 1) / 2);
}

void CMapEditorView::OnUpdateShowBase(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bShowBase?1:0);
}


void CMapEditorView::OnShowBase()
{
	m_bShowBase = !m_bShowBase;
}

void CMapEditorView::OnUpdateShowGrid(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio((pCmdUI->m_nID - ID_SHOW_GIRD_CLOSE) == m_ShowGridType);
}

void CMapEditorView::OnShowGrid(UINT nID)
{
	m_ShowGridType = static_cast<eSHOW_GIRD_STATE>(nID - ID_SHOW_GIRD_CLOSE);
}

BOOL CMapEditorView::OnMouseWheel(UINT nFlags, short zDelta, CPoint pt)
{
	CPoint adj = GetScrollPosition();
	MouseMove(m_PointMouse);
	BOOL result = CScrollView::OnMouseWheel(nFlags, zDelta, pt);
	adj -= GetScrollPosition();
	m_MouseDownPoint += adj;
	m_SmallMapDlg.TryInvalidate();
	return result;
}


void CMapEditorView::OnLButtonUp(UINT nFlags, CPoint point)
{
	CPoint realScalPoint;
	realScalPoint.x = static_cast<LONG>(point.x / m_fRatio);
	realScalPoint.y = static_cast<LONG>(point.y / m_fRatio);
	m_bIsMouseDown = false;
	ReleaseCapture();
	CMapEditorDoc* pDoc = (GetDocument());

	switch(m_nLayerEditID)
	{
	case LEI_GROUND:
		switch (m_nEditMode)
		{
		case EMT_SMALL_GROUND_TILE:
		case EMT_SMALL_GROUND_TYPE:
			if (m_pNowOper && m_pNowOper->GetTileActionData())
			{
				if (m_pNowOper->GetTileActionData()->m_TileData.size()>0)
				{
					m_pNowOper->GetTileActionData()->m_SmallLayerID = m_nSmallLayerEditID;
					m_pOperatorList->DoOneAction(m_pNowOper);
				} else {
					delete(m_pNowOper);
				}
				m_pNowOper = NULL;
			}
			break;
		case EMT_SMALL_SUPER_DELETE:
			if (m_pNowOper && m_pNowOper->GetSuperTileActionData())
			{
				if (m_pNowOper->GetSuperTileActionData()->m_TileData.size()>0)
				{
					m_pOperatorList->DoOneAction(m_pNowOper);
				} else {
					delete(m_pNowOper);
				}
				m_pNowOper = NULL;
			}
			break;
		case EMT_SMALL_GROUND_COLOR:
			if (m_pNowOper && m_pNowOper->GetTileColorActionData())
			{
				if (m_pNowOper->GetTileColorActionData()->m_TileColorData.size()>0)
				{
					m_pOperatorList->DoOneAction(m_pNowOper);
				} else {
					delete(m_pNowOper);
				}
				m_pNowOper = NULL;
			}
			break;
		case EMT_MIDDLE_GROUND_TILE:
		case EMT_MIDDLE_GROUND:
		case EMT_BIG_GROUND_TILE:
		case EMT_BIG_GROUND:
			if (m_pNowOper && m_pNowOper->GetBiggerTileActionData())
			{
				if (m_pNowOper->GetBiggerTileActionData()->m_TileData.size()>0)
				{
					m_pNowOper->GetBiggerTileActionData()->m_GroundLayers = m_GroundLayerEditID;
					m_pOperatorList->DoOneAction(m_pNowOper);
				} else {
					delete(m_pNowOper);
				}
				m_pNowOper = NULL;
			}
			break;
		}
		break;
	case LEI_WATER_1:
	case LEI_WATER_2:
	case LEI_LOW_1:
	case LEI_LOW_2:
	case LEI_LOW_3:
	case LEI_LOW_4:
	case LEI_LOW_5:
	case LEI_LOW_6:
	case LEI_MID:
	case LEI_HIGH_1:
	case LEI_HIGH_2:
		switch (m_nEditMode)
		{
		case EMT_OBJECT_NORMAL:
		case EMT_OBJECT_DRAG:
			if (m_nBrushType == 0)
			{
				if (m_SelectedObjects.size() > 0 && m_nEditMode == EMT_OBJECT_DRAG)
				{
					if (m_SelectedObjects.size() == 1 && IsSHIFTPressed())
					{
						PMap::MAP_OBJ_TYPE type;
						if (CLayerView::GetObjectType(*(m_SelectedObjects.begin()), type))
						{
							Nuclear::SubMap::sObjInfo &objInfo = pDoc->GetToolsMap().GetObjInfo(type,
								m_nLayerEditID - FIRST_ELEMENT_LAYER, GetObjectId(*(m_SelectedObjects.begin())));
							MoveSelectedObjects(m_MouseObjectPosition.x - objInfo.m_Rect.left, m_MouseObjectPosition.y - objInfo.m_Rect.top);
						}
					} else {
						MoveSelectedObjects(static_cast<LONG>((point.x - m_MouseDownPoint.x) / m_fRatio), 
							static_cast<LONG>((point.y - m_MouseDownPoint.y) / m_fRatio));
					}
					m_bIsShowSelectElementRect = true;
				} else {
					if (IsCTRLPressed())
					{
						Nuclear::SortBaseId sortBaseID;
						Nuclear::CRECT rect;
						switch (CheckSelectLayerObject(m_nLayerEditID - FIRST_ELEMENT_LAYER, realScalPoint, sortBaseID, rect))
						{
						case CS_EMPTY_AREA:
							break;
						case CS_SELECTED_ELEMENT:
							RemoveSelection(sortBaseID, rect);
							m_ObjsListDlg.TryChangeSelectedObject(sortBaseID, FALSE);
							break;
						case CS_COMMON_ELEMENT:
							AddSelection(sortBaseID, rect);
							m_ObjsListDlg.TryChangeSelectedObject(sortBaseID);
							break;
						}
					}
				}
			}
			break;
		case EMT_OBJECT_DRAG_RECT:
			m_nEditMode = EMT_OBJECT_NORMAL;
			SelectLayerObject(m_nLayerEditID - FIRST_ELEMENT_LAYER, m_MouseRect);
			m_bIsShowMouseRect = false;
			break;
		}
		break;
	case LEI_TRANS:
	case LEI_TRIGGER:
	case LEI_WATER:
		switch (m_nEditMode)
		{
		case EMT_WATER_TILE:
			if (m_pNowOper && m_pNowOper->GetWaterActionData())
			{
				if (m_pNowOper->GetWaterActionData()->m_WaterData.size()>0)
				{
					m_pOperatorList->DoOneAction(m_pNowOper);
				} else {
					delete(m_pNowOper);
				}
				m_pNowOper = NULL;
			}
			break;
		case EMT_TRANSPARENT_OBJECT_NORMAL:
		case EMT_TRANSPARENT_OBJECT_DRAG:
			if (m_SelectedObjects.size() > 0 && m_nEditMode == EMT_TRANSPARENT_OBJECT_DRAG)
			{
				MoveSelectedTransparentObjects(static_cast<LONG>((point.x - m_MouseDownPoint.x) / m_fRatio), static_cast<LONG>((point.y - m_MouseDownPoint.y) / m_fRatio));
				m_bIsShowSelectElementRect = true;
			} else {
				if (IsCTRLPressed())
				{
					unsigned int transID;
					Nuclear::CRECT rect;
					switch (CheckSelectTransparentObject(realScalPoint, transID, rect))
					{
					case CS_EMPTY_AREA:
						break;
					case CS_SELECTED_ELEMENT:
						RemoveSelection(transID, rect);
						m_TransparentObjectListDlg.TryChangeSelectedTransparentObject(transID, FALSE);
						break;
					case CS_COMMON_ELEMENT:
						AddSelection(transID, rect);
						m_TransparentObjectListDlg.TryChangeSelectedTransparentObject(transID);
						break;
					}
				}
			}
			break;
		case EMT_TRIGGER_NORMAL:
		case EMT_TRIGGER_DRAG:
			if (m_SelectedObjects.size() > 0 && m_nEditMode == EMT_TRIGGER_DRAG)
			{
				MoveSelectedTriggers(static_cast<LONG>((point.x - m_MouseDownPoint.x) / m_fRatio), static_cast<LONG>((point.y - m_MouseDownPoint.y) / m_fRatio));
				m_bIsShowSelectElementRect = true;
			} else {
				if (IsCTRLPressed())
				{
					Nuclear::SubMap::TriggerID tID;
					Nuclear::CRECT rect;
					switch (CheckSelectTrigger(realScalPoint, tID, rect))
					{
					case CS_EMPTY_AREA:
						break;
					case CS_SELECTED_ELEMENT:
						RemoveSelection(tID.id, rect);
						
						break;
					case CS_COMMON_ELEMENT:
						AddSelection(tID.id, rect);
						
						break;
					}
				}
			}
			break;
		case EMT_WATER_IMMERSION_NORMAL:
		case EMT_WATER_IMMERSION_DRAG:
			if (m_SelectedObjects.size() > 0 && m_nEditMode == EMT_WATER_IMMERSION_DRAG)
			{
				MoveSelectedWaterAreas(static_cast<LONG>((point.x - m_MouseDownPoint.x) / m_fRatio), static_cast<LONG>((point.y - m_MouseDownPoint.y) / m_fRatio));
				m_bIsShowSelectElementRect = true;
			} else {
				if (IsCTRLPressed())
				{
					Nuclear::SubMap::WaterAreaID id;
					Nuclear::CRECT rect;
					switch (CheckSelectWaterArea(realScalPoint, id, rect))
					{
					case CS_EMPTY_AREA:
						break;
					case CS_SELECTED_ELEMENT:
						RemoveSelection(id.id, rect);
						
						break;
					case CS_COMMON_ELEMENT:
						AddSelection(id.id, rect);
						
						break;
					}
				}
			}
			break;
		case EMT_WATER_IMMERSION_DRAG_RECT:
			m_nEditMode = EMT_WATER_IMMERSION_NORMAL;
			SelectWaterArea(m_MouseRect);
			m_bIsShowMouseRect = false;
			break;
		case EMT_TRANSPARENT_OBJECT_DRAG_RECT:
			m_nEditMode = EMT_TRANSPARENT_OBJECT_NORMAL;
			SelectTransparentObject(m_MouseRect);
			m_bIsShowMouseRect = false;
			break;
		case EMT_TRIGGER_DRAG_RECT:
			m_nEditMode = EMT_TRIGGER_NORMAL;
			SelectTrigger(m_MouseRect);
			m_bIsShowMouseRect = false;
			break;
		case EMT_WATER_IMMERSION_CHANGE_SIZE:
			{
				m_bIsShowSelectElementRect = true;
				m_nEditMode = EMT_WATER_IMMERSION_NORMAL;
				PMap &pmap = pDoc->GetToolsMap();
				const Nuclear::SubMap::WaterAreaInfoMap &waters = pmap.GetWaterAreaInfos();
				Nuclear::SubMap::WaterAreaInfoMap::const_iterator iter;
				Nuclear::CRECT rect;
				CPoint adj = m_PointMouse-m_MouseDownPoint;
				Nuclear::SubMap::WaterAreaID id(*m_SelectedObjects.begin());
				iter = waters.find(id.infoID);
				if (iter != waters.end())
				{
					
					unsigned int adjx, adjy;
					adjx = static_cast<unsigned int>(adj.x / m_fRatio);
					adjy = static_cast<unsigned int>(adj.y / m_fRatio);
					rect.left = m_InitialPoint.x;
					rect.top = m_InitialPoint.y;
					rect.right = m_ToChangePoint.x + adjx;
					rect.bottom = m_ToChangePoint.y + adjy;
					Nuclear::NormalizeRect(rect);
					CAction* pOper = new CAction(CAction::AT_WATER_AREA, GetLayerEditId());
					CAction::CWaterAreaActionAtom* pWaterData = pOper->GetWaterAreaActionData();
					pWaterData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetWaterAreaInfos();
					pmap.ModifyAnWaterArea(id, rect);
					pWaterData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetWaterAreaInfos();
					DoAction(pOper);
					m_SelectedRects.clear();
					m_SelectedRects.push_back(rect);
					m_SelectWaterDlg.RenewList();
				}
				break;
			}
		case EMT_TRIGGER_CHANGE_SIZE:
			{
				m_nEditMode = EMT_TRIGGER_NORMAL;
				PMap &pmap = pDoc->GetToolsMap();
				const Nuclear::SubMap::TriggerInfoMap &triggers = pmap.GetTriggerInfos();
				Nuclear::SubMap::TriggerInfoMap::const_iterator iter;
				Nuclear::CRECT rect;
				CPoint adj = m_PointMouse-m_MouseDownPoint;
				Nuclear::SubMap::TriggerID id(*m_SelectedObjects.begin());
				iter = triggers.find(id.infoID);
				if (iter != triggers.end())
				{
					
					unsigned int adjx, adjy;
					adjx = static_cast<unsigned int>(adj.x / m_fRatio);
					adjy = static_cast<unsigned int>(adj.y / m_fRatio);
					rect.left = m_InitialPoint.x;
					rect.top = m_InitialPoint.y;
					rect.right = m_ToChangePoint.x + adjx;
					rect.bottom = m_ToChangePoint.y + adjy;
					Nuclear::NormalizeRect(rect);
					CAction* pOper = new CAction(CAction::AT_TRIGGER_OBJECT, GetLayerEditId());
					CAction::CTriggerObjectActionAtom* pTriggerData = pOper->GetTriggerObjectActionData();
					pTriggerData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetTriggerInfos();
					pmap.ModifyAnTriggerArea(id, rect);
					pTriggerData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetTriggerInfos();
					DoAction(pOper);
					m_SelectedRects.clear();
					m_SelectedRects.push_back(rect);
					m_TriggerListDlg.ReNewTree();
				}
				break;
			}
		case EMT_TRANSPARENT_OBJECT_CHANGE_SIZE:
			m_nEditMode = EMT_TRANSPARENT_OBJECT_NORMAL;
			PMap &map = pDoc->GetToolsMap();
			const Nuclear::SubMap::TransObjInfoMap &transObjs = map.GetTransInfos();
			Nuclear::SubMap::TransObjInfoMap::const_iterator iter;
			std::map<std::wstring, Nuclear::PictureHandle>::iterator picIter;
			Nuclear::FRECT rect;
			CPoint adj = m_PointMouse-m_MouseDownPoint;
			iter = transObjs.find(*m_SelectedObjects.begin());
			if (iter != transObjs.end())
			{
				RECT standardRect;
				standardRect.left = 0;
				standardRect.top = 0;
				standardRect.right = iter->second.width;
				standardRect.bottom = iter->second.height;
				
				float adjx, adjy;
				adjx = static_cast<float>(adj.x) / m_fRatio;
				adjy = static_cast<float>(adj.y) / m_fRatio;
				rect.left = static_cast<float>(m_InitialPoint.x);
				rect.top = static_cast<float>(m_InitialPoint.y);
				rect.right = static_cast<float>(m_ToChangePoint.x) + adjx;
				rect.bottom = static_cast<float>(m_ToChangePoint.y) + adjy;
				int width, height;
				int WGreater0, HGreater0;
				CheckScalRect(standardRect, static_cast<int>(rect.Width()), static_cast<int>(rect.Height()), width, height);
				WGreater0 = rect.Width() > 0 ? 1 : -1;
				HGreater0 = rect.Height() > 0 ? 1 : -1;
				rect.right = rect.left + width * WGreater0;
				rect.bottom = rect.top + height * HGreater0;
				Nuclear::NormalizeRect(rect);
				Nuclear::SubMap::TransObjInfo tmpInfo = iter->second;
				tmpInfo.picRect.left = static_cast<LONG>(rect.left);
				tmpInfo.picRect.top = static_cast<LONG>(rect.top);
				tmpInfo.picRect.bottom = static_cast<LONG>(rect.bottom);
				tmpInfo.picRect.right = static_cast<LONG>(rect.right);

				CAction *pNowOper = new CAction(CAction::AT_TRANSPARENT_OBJECT, m_nLayerEditID);
				CAction::CTransparentObjectActionAtom *oper = pNowOper->GetTransparentObjectsActionData();
				oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = map.GetTransInfos();
				map.ModifyAnTransObj(iter->first, tmpInfo);
				oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = map.GetTransInfos();
				DoAction(pNowOper);
				GetDocument()->SetModifiedFlag();
				ClearAllSelection();
				m_TransparentObjectListDlg.UnselectAllItems();
				AddSelection(iter->first, tmpInfo.picRect);
				m_TransparentObjectListDlg.TryChangeSelectedTransparentObject(iter->first);
			}
			break;
		}
		break;
	case LEI_BACKGROUND:
		switch (m_nEditMode)
		{
		case EMT_BACKGROUND_NORMAL:
			break;
		case EMT_BACKGROUND_DRAG:
			{
				CToolsMap &pMap = pDoc->GetToolsMap();
				CAction* pNowOper = new CAction(CAction::AT_BACKGROUND_INFO, GetLayerEditId());
				CAction::CBackgroundInfoActionAtom* oper = pNowOper->GetBackgroundInfoActionData();
				oper->m_InfoID[CAction::CActionAtom::BEFORE_OPERATOR] = m_SelectedMbg;
				oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pMap.GetBackGroundInfoVet()[m_SelectedMbg];
				oper->m_InfoID[CAction::CActionAtom::AFTER_OPERATOR] = m_SelectedMbg;
				oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = oper->oper[CAction::CActionAtom::BEFORE_OPERATOR];
				Nuclear::SubMap::PBackGoundInfo &info = oper->oper[CAction::CActionAtom::AFTER_OPERATOR];
				info.pos.x += realScalPoint.x + m_DrawScrollPosition.x - m_MouseObjectPosition.x;
				info.pos.y += realScalPoint.y + m_DrawScrollPosition.y - m_MouseObjectPosition.y;
				pMap.ModifyBackgroundInfo(m_SelectedMbg, info);
				m_pOperatorList->DoOneAction(pNowOper);
				pDoc->SetModifiedFlag();
			}
			m_nEditMode = EMT_BACKGROUND_NORMAL;

			break;
		}
		break;
	case LEI_GAMING:
		if (m_bIsSelectedBase && m_bIsSelectedBasePoint)
		{
			SaveNewBase();
		}
		break;
	}
	CScrollView::OnLButtonUp(nFlags, point);
}

bool CMapEditorView::ClientRectToWaterWorldRect(const Nuclear::CRECT &mouseRect, Nuclear::CRECT &rect)
{
	rect.left = (mouseRect.left + m_DrawScrollPosition.x) / Nuclear::SubMap::PGround::WATER_WIDTH;
	rect.right = (mouseRect.right + m_DrawScrollPosition.x) / Nuclear::SubMap::PGround::WATER_WIDTH - 1;
	rect.top = (mouseRect.top + m_DrawScrollPosition.y) / Nuclear::SubMap::PGround::WATER_HEIGHT;
	rect.bottom = (mouseRect.bottom + m_DrawScrollPosition.y) / Nuclear::SubMap::PGround::WATER_HEIGHT - 1;
	return true;
}

bool CMapEditorView::ClientRectToTileWorldRect(const Nuclear::CRECT &mouseRect, Nuclear::CRECT &rect, Nuclear::TILE_SIZE_TYPE sizeType)
{
	int tileWidth, tileHeight;
	GetTileWidthHeight(sizeType, tileWidth, tileHeight);
	rect.left = (mouseRect.left + m_DrawScrollPosition.x) / tileWidth;
	rect.right = (mouseRect.right + m_DrawScrollPosition.x) / tileWidth - 1;
	rect.top = (mouseRect.top + m_DrawScrollPosition.y) / tileHeight;
	rect.bottom = (mouseRect.bottom + m_DrawScrollPosition.y) / tileHeight - 1;
	return true;
}

bool CMapEditorView::ClientRectToGroundTypeWorldRect(const Nuclear::CRECT &mouseRect, Nuclear::CRECT &rect, Nuclear::TILE_SIZE_TYPE sizeType)
{
	int tileWidth, tileHeight;
	GetTileWidthHeight(sizeType, tileWidth, tileHeight);
	rect.left = (mouseRect.left + m_DrawScrollPosition.x) / tileWidth;
	if (mouseRect.left + m_DrawScrollPosition.x < 0)
		rect.left--;
	rect.right = (mouseRect.right + m_DrawScrollPosition.x) / tileWidth;
	rect.top = (mouseRect.top + m_DrawScrollPosition.y) / tileHeight;
	if (mouseRect.top + m_DrawScrollPosition.y < 0)
		rect.top--;
	rect.bottom = (mouseRect.bottom + m_DrawScrollPosition.y) / tileHeight;
	return true;
}

bool CMapEditorView::GetMouseRectForGroundTypeMode(CPoint point, Nuclear::CRECT &mouseRect, Nuclear::TILE_SIZE_TYPE sizeType)
{
	int tileWidth, tileHeight;
	GetTileWidthHeight(sizeType, tileWidth, tileHeight);
	CPoint tmpPoint = point + m_DrawScrollPosition;
	tmpPoint.x %= tileWidth;
	tmpPoint.y %= tileHeight;
	
	mouseRect.left = -tileWidth / 2 + point.x - tmpPoint.x;
	mouseRect.top = -tileHeight / 2 + point.y - tmpPoint.y;
	mouseRect.right = tileWidth / 2 + point.x - tmpPoint.x;
	mouseRect.bottom = tileHeight / 2 + point.y - tmpPoint.y;
	if (tmpPoint.x > (tileWidth >> 1) && m_nBrushSize % 2 == 1)	
	{
		mouseRect.left += tileWidth;
		mouseRect.right += tileWidth;
	}

	if (tmpPoint.y > (tileHeight >> 1) && m_nBrushSize % 2 == 1)	
	{
		mouseRect.top += tileHeight;
		mouseRect.bottom += tileHeight;
	}
	mouseRect.right += tileWidth * (m_nBrushSize / 2);
	mouseRect.bottom += tileHeight * (m_nBrushSize / 2);
	mouseRect.left -= tileWidth * ((m_nBrushSize - 1) / 2);
	mouseRect.top -= tileHeight * ((m_nBrushSize - 1) / 2);
	return true;
}

void CMapEditorView::GetTileWidthHeight(Nuclear::TILE_SIZE_TYPE sizeType, int &tileWidth, int &tileHeight)
{
	switch (sizeType)
	{
	case Nuclear::TST_SMALL:
		tileWidth = PMap::TILE_WIDTH;
		tileHeight = PMap::TILE_HEIGHT;
		break;
	case Nuclear::TST_MIDDLE:
		tileWidth = Nuclear::SubMap::PGround::TILE_MID_WIDTH;
		tileHeight = Nuclear::SubMap::PGround::TILE_MID_HEIGHT;
		break;
	case Nuclear::TST_BIG:
		tileWidth = Nuclear::SubMap::PGround::TILE_BIG_WIDTH;
		tileHeight = Nuclear::SubMap::PGround::TILE_BIG_HEIGHT;
		break;
	}
}

bool CMapEditorView::TryUnselectTransparentObject(unsigned int transID)
{
	Nuclear::CRECT rect;
	if (!CheckSelectTransparentObject(transID, rect))
		return false;
	RemoveSelection(transID, rect);
	return true;
}

bool CMapEditorView::TrySelectTransparentObject(unsigned int transID)
{
	Nuclear::CRECT rect;
	if (!CheckSelectTransparentObject(transID, rect))
		return false;
	AddSelection(transID, rect);
	return true;

}

bool CMapEditorView::TryUnselectObject(unsigned char layerID, Nuclear::SortBaseId sortBaseID)
{
	Nuclear::CRECT rect;
	if (!CheckSelectLayerObject(layerID, sortBaseID, rect))
		return false;
	RemoveSelection(sortBaseID, rect);
	return true;
}

bool CMapEditorView::TrySelectObject(unsigned char layerID, Nuclear::SortBaseId sortBaseID)
{
	Nuclear::CRECT rect;
	if (!CheckSelectLayerObject(layerID, sortBaseID, rect))
		return false;
	AddSelection(sortBaseID, rect);
	m_nBrushType = 0;
	return true;

}

bool CMapEditorView::AddTransparentObject(unsigned int time, int pX, int pY, int width, int height)
{
	Nuclear::SubMap::TransObjInfo tmpInfo;
	tmpInfo.time = time;
	tmpInfo.width = width;
	tmpInfo.height = height;
	tmpInfo.scalPoint.x = pX;
	tmpInfo.scalPoint.y = pY;
	tmpInfo.picRect = GetViewport();
	RECT standardRect;
	standardRect.left = 0;
	standardRect.top = 0;
	standardRect.right = width;
	standardRect.bottom = height;
	int newWidth, newHeight;
	CheckScalRect(standardRect, tmpInfo.picRect.Width(), tmpInfo.picRect.Height(), newWidth, newHeight);
	tmpInfo.picRect.left += (tmpInfo.picRect.Width() - newWidth) / 2;
	tmpInfo.picRect.right = tmpInfo.picRect.left + newWidth;
	tmpInfo.picRect.top += (tmpInfo.picRect.Height() - newHeight) / 2;
	tmpInfo.picRect.bottom = tmpInfo.picRect.top + newHeight;
	CAction *pNowOper = new CAction(CAction::AT_TRANSPARENT_OBJECT, m_nLayerEditID);
	CAction::CTransparentObjectActionAtom *oper = pNowOper->GetTransparentObjectsActionData();
	Nuclear::PMap &map = GetDocument()->GetToolsMap();
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = map.GetTransInfos();
	map.AddAnTransObj(tmpInfo);
	oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = map.GetTransInfos();
	DoAction(pNowOper);
	GetDocument()->SetModifiedFlag();
	return true;
}

bool CMapEditorView::CheckSelectLayerObject(unsigned char LayerId, Nuclear::SortBaseId sortBaseID, Nuclear::CRECT& rect)
{
	if (LayerId != m_nLayerEditID - FIRST_ELEMENT_LAYER)
		return false;
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	PMap::MAP_OBJ_TYPE objType;
	Nuclear::SubMap::ObjectId objID;
	if (CLayerView::GetObjectType(sortBaseID, objType))
	{
		objID = CLayerView::GetObjectId(sortBaseID);
		const Nuclear::SubMap::ObjInfoVector &objs = pmap.GetConstObjInfo(objType, LayerId);
		if (objs.size() <= objID)
			return false;
		if (objs[objID].m_bDeleted)
			return false;
		rect = objs[objID].m_Rect;
	}
	return true;
}

void CMapEditorView::RemoveSelection(const unsigned int& objID, const Nuclear::CRECT& rect) 
{
	if (m_SelectedObjects.find(objID) != m_SelectedObjects.end())
	{
		m_SelectedObjects.erase(objID);
		m_SelectedRects.erase(remove(m_SelectedRects.begin(),m_SelectedRects.end(), rect));
		if (m_SelectedRects.size()==0)
		{
			m_bIsShowSelectElementRect = false;
		} else {
			m_bIsShowSelectElementRect = true;
		}
		m_PropertiesDlg.TryInvalidata();
	}
}
void CMapEditorView::AddSelection(const unsigned int& objID, const Nuclear::CRECT& rect) 
{
	if (m_SelectedObjects.find(objID) == m_SelectedObjects.end())
	{
		m_SelectedObjects.insert(objID);
		m_SelectedRects.push_back(rect);
		m_bIsShowSelectElementRect = true;
		m_PropertiesDlg.TryInvalidata();
	}
}

void CMapEditorView::ClearAllSelection()
{
	m_SelectedObjects.clear();
	m_SelectedRects.clear();
	m_bIsShowSelectElementRect = false;
	m_PropertiesDlg.TryInvalidata();
}

void CMapEditorView::OnLButtonDown(UINT nFlags, CPoint point)
{
	CMapEditorDoc* pDoc = (GetDocument());
	Nuclear::CRECT rect;
	std::wstring name;
	bool error = false;
	m_bIsMouseDown = true;
	m_MouseDownPoint = point;
	CPoint realScalPoint;
	realScalPoint.x = static_cast<LONG>(point.x / m_fRatio);
	realScalPoint.y = static_cast<LONG>(point.y / m_fRatio);
	SetCapture();
	if (!IsSPACEPressed())
	{
		switch(m_nLayerEditID)
		{
		case LEI_GROUND:
			switch (m_nEditMode)
			{
			case EMT_SMALL_GROUND_TYPE:
				if (m_bIsMutiSelectedLayer)
				{
					AfxMessageBox(_T("请只选择一个地表图层～"));
					error = true;
				} else if (m_nSmallLayerEditID == -1) {
					AfxMessageBox(_T("请选择一个地表图层～"));
					error = true;
				} else {
					if (m_GroundLayerEditID != Nuclear::SubMap::GL_SMALL)
						SetGroundLayerEdit(Nuclear::SubMap::GL_SMALL);
					m_bIsShowMouseRect = GetMouseRectForGroundTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_SMALL);
					if (ClientRectToGroundTypeWorldRect(m_MouseRect, rect, Nuclear::TST_SMALL))
					{
						if (m_pNowOper)
							delete (m_pNowOper);
						m_pNowOper = new CAction(CAction::AT_TILE, m_nLayerEditID);
						if (m_GroundCtrl.SetGroundType(m_nBrushType, rect, m_pNowOper))
						{
							m_SmallMapDlg.TryInvalidate();
						} else {
							delete (m_pNowOper);
							m_pNowOper = NULL;
						}
					}
				}
				break;
			case EMT_SMALL_GROUND_TILE:
				if (m_bIsMutiSelectedLayer)
				{
					AfxMessageBox(_T("请只选择一个地表图层～"));
					error = true;
				} else if (m_nSmallLayerEditID == -1) {
					AfxMessageBox(_T("请选择一个地表图层～"));
					error = true;
				} else {
					if (m_GroundLayerEditID != Nuclear::SubMap::GL_SMALL)
						SetGroundLayerEdit(Nuclear::SubMap::GL_SMALL);
					m_bIsShowMouseRect = true;
					GetMouseRectForTileTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_SMALL);
					ClientRectToTileWorldRect(m_MouseRect, rect, Nuclear::TST_SMALL);
					if (m_pNowOper)
						delete (m_pNowOper);
					m_pNowOper = new CAction(CAction::AT_TILE, m_nLayerEditID);
					if (m_GroundCtrl.SetTileType(m_nBrushType, rect, m_pNowOper))
					{
						pDoc->SetModifiedFlag();
						m_SmallMapDlg.TryInvalidate();
					} else {
						delete (m_pNowOper);
						m_pNowOper = NULL;
					}
				}
				break;
			case EMT_SMALL_SUPER_DELETE:
				if (m_GroundLayerEditID != Nuclear::SubMap::GL_SMALL)
					SetGroundLayerEdit(Nuclear::SubMap::GL_SMALL);
				m_bIsShowMouseRect = true;
				GetMouseRectForTileTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_SMALL);
				ClientRectToTileWorldRect(m_MouseRect, rect, Nuclear::TST_SMALL);
				if (m_pNowOper)
					delete (m_pNowOper);
				m_pNowOper = new CAction(CAction::AT_SUPER_TILE, m_nLayerEditID);
				if (m_GroundCtrl.SuperDeleteTile(rect, m_pNowOper))
				{
					pDoc->SetModifiedFlag();
					m_SmallMapDlg.TryInvalidate();
				} else {
					delete (m_pNowOper);
					m_pNowOper = NULL;
				}
				break;
			case EMT_SMALL_GROUND_COLOR:
				if (m_GroundLayerEditID != Nuclear::SubMap::GL_SMALL)
					SetGroundLayerEdit(Nuclear::SubMap::GL_SMALL);

				m_bIsShowMouseRect = true;
				GetMouseRectForGroundTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_SMALL);
				ClientRectToGroundTypeWorldRect(m_MouseRect, rect, Nuclear::TST_SMALL);
				if (m_pNowOper)
					delete (m_pNowOper);
				m_pNowOper = new CAction(CAction::AT_TILE_COLOR, m_nLayerEditID);
				if (m_GroundCtrl.SetTileColorType(m_nBrushType, rect, m_pNowOper))
				{
					m_SmallMapDlg.TryInvalidate();
				} else {
					delete (m_pNowOper);
					m_pNowOper = NULL;
				}
				break;
			case EMT_MIDDLE_GROUND:
				if ((m_GroundLayerEditID != Nuclear::SubMap::GL_MID1) && (m_GroundLayerEditID != Nuclear::SubMap::GL_MID2))
				{
					AfxMessageBox(_T("请选择一个地表中层～"));
					error = true;
				} else {
					m_bIsShowMouseRect = GetMouseRectForGroundTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_MIDDLE);
					if (ClientRectToGroundTypeWorldRect(m_MouseRect, rect, Nuclear::TST_MIDDLE))
					{
						if (m_pNowOper)
							delete (m_pNowOper);
						m_pNowOper = new CAction(CAction::AT_BIGGER_TILE, m_nLayerEditID);
						if (m_GroundCtrl.SetGroundType(m_nBrushType, rect, m_pNowOper))
						{
							m_SmallMapDlg.TryInvalidate();
						} else {
							delete (m_pNowOper);
							m_pNowOper = NULL;
						}
					}
				}
				break;
			case EMT_MIDDLE_GROUND_TILE:
				if ((m_GroundLayerEditID != Nuclear::SubMap::GL_MID1) && (m_GroundLayerEditID != Nuclear::SubMap::GL_MID2))
				{
					AfxMessageBox(_T("请选择一个地表中层～"));
					error = true;
				} else {
					m_bIsShowMouseRect = true;
					GetMouseRectForTileTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_MIDDLE);
					ClientRectToTileWorldRect(m_MouseRect, rect, Nuclear::TST_MIDDLE);
					if (m_pNowOper)
						delete (m_pNowOper);
					m_pNowOper = new CAction(CAction::AT_BIGGER_TILE, m_nLayerEditID);
					if (m_GroundCtrl.SetTileType(m_nBrushType, rect, m_pNowOper))
					{
						pDoc->SetModifiedFlag();
						m_SmallMapDlg.TryInvalidate();
					} else {
						delete (m_pNowOper);
						m_pNowOper = NULL;
					}
				}
				break;
			case EMT_BIG_GROUND:
				if (m_GroundLayerEditID != Nuclear::SubMap::GL_BIG)
					SetGroundLayerEdit(Nuclear::SubMap::GL_BIG);
				m_bIsShowMouseRect = GetMouseRectForGroundTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_BIG);
				if (ClientRectToGroundTypeWorldRect(m_MouseRect, rect, Nuclear::TST_BIG))
				{
					if (m_pNowOper)
						delete (m_pNowOper);
					m_pNowOper = new CAction(CAction::AT_BIGGER_TILE, m_nLayerEditID);
					if (m_GroundCtrl.SetGroundType(m_nBrushType, rect, m_pNowOper))
					{
						m_SmallMapDlg.TryInvalidate();
					} else {
						delete (m_pNowOper);
						m_pNowOper = NULL;
					}
				}
				break;
			case EMT_BIG_GROUND_TILE:
				if (m_GroundLayerEditID != Nuclear::SubMap::GL_BIG)
					SetGroundLayerEdit(Nuclear::SubMap::GL_BIG);
				m_bIsShowMouseRect = true;
				GetMouseRectForTileTypeMode(realScalPoint, m_MouseRect, Nuclear::TST_BIG);
				ClientRectToTileWorldRect(m_MouseRect, rect, Nuclear::TST_BIG);
				if (m_pNowOper)
					delete (m_pNowOper);
				m_pNowOper = new CAction(CAction::AT_BIGGER_TILE, m_nLayerEditID);
				if (m_GroundCtrl.SetTileType(m_nBrushType, rect, m_pNowOper))
				{
					pDoc->SetModifiedFlag();
					m_SmallMapDlg.TryInvalidate();
				} else {
					delete (m_pNowOper);
					m_pNowOper = NULL;
				}
				break;
			}
			AdjustMousePoints();
			break;
		case LEI_WATER_1:
		case LEI_WATER_2:
		case LEI_LOW_1:
		case LEI_LOW_2:
		case LEI_LOW_3:
		case LEI_LOW_4:
		case LEI_LOW_5:
		case LEI_LOW_6:
		case LEI_MID:
		case LEI_HIGH_1:
		case LEI_HIGH_2:
			m_bIsShowMouseRect = false;
			switch (m_nEditMode)
			{
			case EMT_OBJECT_NORMAL:
				if (m_nBrushType != 0)
				{
					PMap::MAP_OBJ_TYPE type;
					name = m_SelectObjectDlg.GetFilenameAndType(m_nBrushType, type);

					bool ok = true;
					switch (type)
					{
					case PMap::MAP_OBJ_ELEMENT:

						CheckPictureLoad(name);
						if (GetImgInfo(name) == NULL)
						{
							ok = false;
						}
						break;
					default:
						if (m_nLayerEditID == LEI_WATER_1 || m_nLayerEditID == LEI_WATER_2)
						{
							AfxMessageBox(L"水下的层不能放特效、植物等动态物件！");
							ok = false;
						}
						break;
					}
					if (ok)
						m_ObjectsCtrl.AddObject(type, name, m_MouseObjectPosition, m_pNowOper, m_pOperatorList);
				} else {
					Nuclear::SortBaseId sortBaseID;
					switch (CheckSelectLayerObject(m_nLayerEditID - FIRST_ELEMENT_LAYER, realScalPoint, sortBaseID, rect))
					{
					case CS_EMPTY_AREA:
						if (!IsCTRLPressed())
						{
							ClearAllSelection();
							m_ObjsListDlg.UnselectAllItems();
						}
						m_bIsShowMouseRect = true;
						m_nEditMode = EMT_OBJECT_DRAG_RECT;
						m_MouseRect.left = realScalPoint.x + m_DrawScrollPosition.x;
						m_MouseRect.top = realScalPoint.y + m_DrawScrollPosition.y;
						m_MouseRect.right = m_MouseRect.left;
						m_MouseRect.bottom = m_MouseRect.top;
						break;
					case CS_SELECTED_ELEMENT:
						break;
					case CS_COMMON_ELEMENT:
						if (!IsCTRLPressed())
						{
							ClearAllSelection();
							m_ObjsListDlg.UnselectAllItems();
							AddSelection(sortBaseID, rect);
							m_ObjsListDlg.TryChangeSelectedObject(sortBaseID);
						}
						break;
					}
				}
				break;
			case EMT_OBJECT_DRAG:
			case EMT_OBJECT_DRAG_RECT:
				break;
			}
			break;
		case LEI_TRANS:
		case LEI_TRIGGER:
		case LEI_WATER:
			{
				bool isTrigger = true;
				bool isTransOrTrigger = true;
				switch (m_nEditMode)
				{
				case EMT_WATER_TILE:
					m_bIsShowMouseRect = true;
					GetMouseRectForWaterMode(realScalPoint, m_MouseRect);
					ClientRectToWaterWorldRect(m_MouseRect, rect);
					if (m_pNowOper)
						delete (m_pNowOper);
					m_pNowOper = new CAction(CAction::AT_WATER, m_nLayerEditID);
					Nuclear::SubMap::WaterTileType type;
					type.data = static_cast<unsigned char>(m_nBrushType);
					if (m_GroundCtrl.SetWaterType(type, rect, m_pNowOper))
					{
						pDoc->SetModifiedFlag();
						m_SmallMapDlg.TryInvalidate();
					} else {
						delete (m_pNowOper);
						m_pNowOper = NULL;
					}
					AdjustMousePoints();
					break;
				case EMT_WATER_IMMERSION_NORMAL:
					isTransOrTrigger = false;
				case EMT_TRANSPARENT_OBJECT_NORMAL:
					isTrigger = false;
				case EMT_TRIGGER_NORMAL:
					{
						bool isChangeSize = false;
						if (m_SelectedObjects.size() == 1)
						{
							std::vector<Nuclear::CRECT>::iterator iter = m_SelectedRects.begin();
							switch (CheckSelectedRectPoint(realScalPoint + m_DrawScrollPosition))
							{
							case TOSD_LEFT_TOP:
								isChangeSize = true;
								m_InitialPoint.x = iter->right;
								m_InitialPoint.y = iter->bottom;
								m_ToChangePoint.x = iter->left;
								m_ToChangePoint.y = iter->top;
								break;
							case TOSD_RIGHT_TOP:
								isChangeSize = true;
								m_InitialPoint.x = iter->left;
								m_InitialPoint.y = iter->bottom;
								m_ToChangePoint.x = iter->right;
								m_ToChangePoint.y = iter->top;
								break;
							case TOSD_RIGHT_BOTTOM:
								isChangeSize = true;
								m_InitialPoint.x = iter->left;
								m_InitialPoint.y = iter->top;
								m_ToChangePoint.x = iter->right;
								m_ToChangePoint.y = iter->bottom;
								break;
							case TOSD_LEFT_BOTTOM:
								isChangeSize = true;
								m_InitialPoint.x = iter->right;
								m_InitialPoint.y = iter->top;
								m_ToChangePoint.x = iter->left;
								m_ToChangePoint.y = iter->bottom;
								break;
							default:
								break;
							}
						}
						if (isChangeSize)
						{
							if (isTransOrTrigger)
							{
								m_nEditMode = isTrigger?EMT_TRIGGER_CHANGE_SIZE:EMT_TRANSPARENT_OBJECT_CHANGE_SIZE;
							} else {
								m_nEditMode = EMT_WATER_IMMERSION_CHANGE_SIZE;
								m_bIsShowSelectElementRect = false;
							}
							
						} else {
							unsigned int transID;
							Nuclear::SubMap::WaterAreaID wID;
							Nuclear::SubMap::TriggerID tID;
							CLICK_STATE result;
							if (isTransOrTrigger)
							{
								if (isTrigger)
								{
									result = CheckSelectTrigger(realScalPoint, tID, rect);
									transID = tID.id;
								} else {
									result = CheckSelectTransparentObject(realScalPoint, transID, rect);
								}
							} else {
								result = CheckSelectWaterArea(realScalPoint, wID, rect);
								transID = wID.id;
							}
							switch (result)
							{
							case CS_EMPTY_AREA:
								if (!IsCTRLPressed())
								{
									ClearAllSelection();
									if (isTransOrTrigger)
									{
										if (isTrigger)
										{
											m_TriggerListDlg.UnselectAllItems();
										} else {
											m_TransparentObjectListDlg.UnselectAllItems();
										}
									} else {
										m_SelectWaterDlg.UnselectAllItems();
									}
								}
								m_bIsShowMouseRect = true;
								if (isTransOrTrigger)
								{
									m_nEditMode = isTrigger?EMT_TRIGGER_DRAG_RECT:EMT_TRANSPARENT_OBJECT_DRAG_RECT;
								} else {
									m_nEditMode = EMT_WATER_IMMERSION_DRAG_RECT;
								}
								m_MouseRect.left = realScalPoint.x + m_DrawScrollPosition.x;
								m_MouseRect.top = realScalPoint.y + m_DrawScrollPosition.y;
								m_MouseRect.right = m_MouseRect.left;
								m_MouseRect.bottom = m_MouseRect.top;
								break;
							case CS_SELECTED_ELEMENT:
								break;
							case CS_COMMON_ELEMENT:
								if (!IsCTRLPressed())
								{
									ClearAllSelection();
									AddSelection(transID, rect);
									if (isTransOrTrigger)
									{
										if (isTrigger)
										{
											m_TriggerListDlg.UnselectAllItems();
											m_TriggerListDlg.TryChangeSelectedTrigger(tID);
										} else {
											m_TransparentObjectListDlg.UnselectAllItems();
											m_TransparentObjectListDlg.TryChangeSelectedTransparentObject(transID);
										}
									} else {
										m_SelectWaterDlg.UnselectAllItems();
										m_SelectWaterDlg.TryChangeSelectWaterArea(wID);
									}
								}
								break;
							}
						}
					}
					break;
				case EMT_TRANSPARENT_OBJECT_DRAG:
				case EMT_TRANSPARENT_OBJECT_DRAG_RECT:
				case EMT_TRANSPARENT_OBJECT_CHANGE_SIZE:
				case EMT_TRIGGER_DRAG:
				case EMT_TRIGGER_DRAG_RECT:
				case EMT_TRIGGER_CHANGE_SIZE:
				case EMT_WATER_IMMERSION_DRAG:
				case EMT_WATER_IMMERSION_DRAG_RECT:
				case EMT_WATER_IMMERSION_CHANGE_SIZE:
					break;
				case EMT_WATER_IMMERSION_ADD_RECT:
					{
						m_bIsShowSelectElementRect = false;
						m_nEditMode = EMT_WATER_IMMERSION_CHANGE_SIZE;
						m_InitialPoint = realScalPoint + m_DrawScrollPosition;
						m_ToChangePoint = m_InitialPoint;
						Nuclear::SubMap::WaterAreaID id = pDoc->GetToolsMap().AddAnWaterArea(
							m_SelectWaterDlg.GetCurInfoID(), Nuclear::CRECT(m_InitialPoint, 0, 0));
						m_SelectedObjects.clear();
						m_SelectedObjects.insert(id.id);
						
					}
					break;
				case EMT_TRIGGER_ADD_RECT:
					{
						m_nEditMode = EMT_TRIGGER_CHANGE_SIZE;
						m_InitialPoint = realScalPoint + m_DrawScrollPosition;
						m_ToChangePoint = m_InitialPoint;
						Nuclear::SubMap::TriggerID id = pDoc->AddTriggerArea(
							m_TriggerListDlg.GetCurInfoID(), Nuclear::CRECT(m_InitialPoint, 0, 0));
						m_SelectedObjects.clear();
						m_SelectedObjects.insert(id.id);

					}
					break;
				}
			}

			break;
		case LEI_BACKGROUND:
			m_nEditMode = EMT_BACKGROUND_NORMAL;
			if (m_SelectedMbg >= 0)
			{
				if (m_nBrushType)
				{
					Nuclear::CPOINT pt = realScalPoint + m_DrawScrollPosition;
					if (m_SelectedMovBackgroundPosition.PtInRect(pt))
					{
						pt -= m_SmbPosition;
						const Nuclear::SubMap::PBackGoundInfo &info = GetDocument()->GetToolsMap().GetBackGroundInfoVet()[m_SelectedMbg];
						int bgwidth = info.girdWidth * info.width;
						int bgheight = info.girdHeight * info.height;
						if (pt.x < 0)
						{
							XPASSERT(info.wrapHorz);	
							int n = -(pt.x + 1) / bgwidth + 1;
							pt.x += n*bgwidth;
						}
						if (pt.x >= bgwidth)
						{
							int n = pt.x / bgwidth;
							pt.x -= n*bgwidth;
						}
						if (pt.y < 0)
						{
							XPASSERT(info.wrapVert);	
							int n = -(rect.top + 1) / bgheight + 1;
							pt.y += n*bgheight;
						}
						if (pt.y >= bgheight)
						{
							int n = pt.y / bgheight;
							pt.y -= n*bgheight;
						}
						int x = pt.x / info.girdWidth;
						int y = pt.y / info.girdHeight;
						Nuclear::SubMap::ObjMapKey *pKey = info.pPics + y * info.width + x;
						PMap::MAP_OBJ_TYPE type;
						name = m_SelectObjectDlg.GetFilenameAndType(m_nBrushType, type);
						
						bool ok = true;
						switch (type)
						{
						case PMap::MAP_OBJ_ELEMENT:
							CheckPictureLoad(name);
							if (GetImgInfo(name) != NULL)
							{
								CAction* pOper = new CAction(CAction::AT_BACKGROUND_DATA, m_nLayerEditID);
								CAction::CBackgroundDataActionAtom* oper = pOper->GetBackgroundDataActionData();
								oper->m_InfoID = m_SelectedMbg;
								oper->m_Position = y * info.width + x;
								oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = (*pKey);
								(*pKey) = GetDocument()->GetObjectMapKey(type, name, NULL);							
								oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = (*pKey);
								m_pOperatorList->DoOneAction(pOper);
							}
							break;
						default:
							AfxMessageBox(L"移动背景只能放普通物件!");
							break;
						}
					}
				} else {
					m_nEditMode = EMT_BACKGROUND_DRAG;
					m_MouseObjectPosition.x = realScalPoint.x + m_DrawScrollPosition.x;
					m_MouseObjectPosition.y = realScalPoint.y + m_DrawScrollPosition.y;
				}
			}
			break;
		case LEI_GAMING:
			switch (m_nEditMode)
			{
			case EMT_PERFORMANCE_DLG:
				if (m_pPerformanceDlg)
				{
					m_pPerformanceDlg->SetViewportCenter(realScalPoint + m_DrawScrollPosition);
				}
				break;
			case EMT_GAMING_NORMAL:
				if (fabs(m_fRatio - 1.0f) < 0.0001f)
				{
					if (m_bIsSelectedBase && CheckSelectElementBasePoint(realScalPoint, m_SelectedTheBasePoint))
					{
						m_bIsSelectedBasePoint = true;
						int ia = static_cast<int>(m_SelectedTheBasePoint);
						int initIndex = (ia+2)>3?(ia-2):(ia+2);	
						m_InitialPoint.x = static_cast<LONG>(m_fPosX[initIndex]);
						m_InitialPoint.y = static_cast<LONG>(m_fPosY[initIndex]);
					} else {
						m_bIsSelectedBase = false;
						m_bIsSelectedBasePoint = false;
						Nuclear::SortBaseId sortBaseID;
						if (CheckSelectObjectBase(realScalPoint, sortBaseID))
						{
							m_SelectedObjects.clear();
							m_SelectedObjects.insert(sortBaseID);
							m_bIsSelectedBase = true;
						}
					}
					

				} else {
					m_bIsSelectedBase = false;
				}
				break;
			}
			break;
		}
	}
	if (error)
	{
		ReleaseCapture();
		m_bIsMouseDown = false;
	}
	CScrollView::OnLButtonDown(nFlags, point);
}
void CMapEditorView::OnMouseMove(UINT nFlags, CPoint point)
{
	MouseMove(point);
	CScrollView::OnMouseMove(nFlags, point);
}

void CMapEditorView::OnUpdateShowLayer(CCmdUI* pCmdUI)
{
	pCmdUI->SetCheck(m_bIsShowLayers[pCmdUI->m_nID - ID_SHOW_LAYER_GROUND]);
}

void CMapEditorView::OnSelectShowLayer(UINT nID)
{
	m_bIsShowLayers[nID - ID_SHOW_LAYER_GROUND] = !m_bIsShowLayers[nID - ID_SHOW_LAYER_GROUND];
	m_ScreenElements.SetMustRenew();
}

void CMapEditorView::OnUpdateEditLayer(CCmdUI* pCmdUI)
{
	pCmdUI->SetRadio((pCmdUI->m_nID - ID_LAYER_GROUND) == m_nLayerEditID);
}

void CMapEditorView::OnSelectEditLayer(UINT nID)
{
	ChangeEditLayer(nID - ID_LAYER_GROUND);
}

void CMapEditorView::ChangeEditLayer(int layerID)
{
	if (layerID != LEI_GAMING)
	{
		ReleasePerformanceDlg();
	}
	m_bIsSelectedBase = false;
	m_bIsShowMouseRect = false;
	m_nBrushType = 0;
	ClearAllSelection();
	if (m_TransparentObjectListDlg.m_hWnd != NULL)
	{
		m_TransparentObjectListDlg.ShowWindow(SW_HIDE);
	}
	if (m_LayerListDlg.m_hWnd != NULL)
	{
		m_LayerListDlg.ShowWindow(SW_HIDE);
	}
	if (m_SelectGroundDlg.m_hWnd != NULL) 
	{
		m_SelectGroundDlg.ShowWindow(SW_HIDE);
	}
	if (m_SelectObjectDlg.m_hWnd != NULL) 
	{
		m_SelectObjectDlg.ShowWindow(SW_HIDE);
	}
	if (m_SelectWaterDlg.m_hWnd != NULL)
	{
		m_SelectWaterDlg.ShowWindow(SW_HIDE);	
	}
	if (m_ObjsListDlg.m_hWnd != NULL) 
	{
		if (m_ObjsListDlg.IsWindowVisible() && m_nLayerEditID != layerID)
		{
			m_ObjsListDlg.ShowWindow(SW_HIDE);
		}
	}
	if (m_BackgroundListDlg.m_hWnd != NULL) 
	{
		m_BackgroundListDlg.ShowWindow(SW_HIDE);
	}
	if (m_TriggerListDlg.m_hWnd != NULL)
	{
		m_TriggerListDlg.ShowWindow(SW_HIDE);
	}
	if (m_nLayerEditID == LEI_BACKGROUND && layerID != m_nLayerEditID)
	{
		m_SelectedMbg = -1;
	}
	m_nLayerEditID = layerID;
	switch(m_nLayerEditID)
	{
	case LEI_GROUND:
		if (m_SelectGroundDlg.m_hWnd == NULL) 
		{
			m_SelectGroundDlg.Create(this);
		}
		if (m_LayerListDlg.m_hWnd == NULL)
		{
			m_LayerListDlg.Create(this);
		}
		m_LayerListDlg.ShowWindow(SW_SHOW);
		m_SelectGroundDlg.ShowWindow(SW_SHOW);
		m_nEditMode = m_SelectGroundDlg.GetCurrentEditMode();
		break;
	case LEI_WATER_1:
	case LEI_WATER_2:
	case LEI_LOW_1:
	case LEI_LOW_2:
	case LEI_LOW_3:
	case LEI_LOW_4:
	case LEI_LOW_5:
	case LEI_LOW_6:
	case LEI_MID:
	case LEI_HIGH_1:
	case LEI_HIGH_2:
		if (m_SelectObjectDlg.m_hWnd == NULL) 
		{
			m_SelectObjectDlg.Create(this);
		}
		m_SelectObjectDlg.ShowWindow(SW_SHOW);
		break;
	case LEI_TRANS:
		m_nEditMode = EMT_TRANSPARENT_OBJECT_NORMAL;
		if (m_TransparentObjectListDlg.m_hWnd == NULL)
		{
			m_TransparentObjectListDlg.Create(this);
		}
		m_TransparentObjectListDlg.ShowWindow(SW_SHOW);
		break;
	case LEI_TRIGGER:
		m_nEditMode = EMT_TRIGGER_NORMAL;
		if (m_TriggerListDlg.m_hWnd == NULL)
		{
			m_TriggerListDlg.Create(this);
		}
		m_TriggerListDlg.ShowWindow(SW_SHOW);
		break;
	case LEI_WATER:
		if (m_SelectWaterDlg.m_hWnd == NULL)
		{
			m_SelectWaterDlg.Create(this);
		}
		m_SelectWaterDlg.ShowWindow(SW_SHOW);
		m_nEditMode = m_SelectWaterDlg.GetCurEditMode();
		break;
	case LEI_BACKGROUND:
		if (m_BackgroundListDlg.m_hWnd == NULL) 
		{
			m_BackgroundListDlg.Create(this);
		}
		m_BackgroundListDlg.ShowWindow(SW_SHOW);
		if (m_SelectObjectDlg.m_hWnd == NULL) 
		{
			m_SelectObjectDlg.Create(this);
		}
		m_SelectObjectDlg.ShowWindow(SW_SHOW);
		m_nEditMode = EMT_BACKGROUND_NORMAL;
	case LEI_GAMING:
		m_nEditMode = EMT_GAMING_NORMAL;
		break;
	}
	SetFocus();
}

BOOL CMapEditorView::OnEraseBkgnd(CDC* pDC)
{
	return TRUE;
}

void CMapEditorView::OnSize(UINT nType, int cx, int cy)
{
	CScrollView::OnSize(nType, cx, cy);
	GetClientRect(&m_ClientRect);
	m_TimeEffectEditorDlg.m_ColorBalanceParam.fCentx = (m_ClientRect.right - m_ClientRect.left) / 2.0f;
	m_TimeEffectEditorDlg.m_ColorBalanceParam.fCenty = (m_ClientRect.bottom - m_ClientRect.top) / 2.0f;
	ChangeScale();
}


void CMapEditorView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CScrollView::OnSizing(fwSide, pRect);
	ChangeScale();
}

Sprite* CMapEditorView::AddSprite()
{
	Sprite *pSprite = new Sprite(this, m_ModelName);
	m_Sprites.push_back(pSprite);
	int layerCount = m_pSpriteMgr->GetLayerCount(m_ModelName);
	for (int i=0;i<layerCount;++i)
	{
		pSprite->SetComponent(i, m_Components[i]);
	}
	pSprite->SetRunLeft(m_AnimationRunLeft);
	pSprite->SetRunRight(m_AnimationRunRight);
	pSprite->SetStand(m_AnimationStand);
	pSprite->SetDefaultAction(m_AnimationStand, Nuclear::XPSALT_SYNC, 1.0f, false);
	pSprite->SetDirection(Nuclear::XPDIR_BOTTOM);
	return pSprite;
}

#ifdef _DEBUG
void CMapEditorView::AssertValid() const
{
	CScrollView::AssertValid();
}

void CMapEditorView::Dump(CDumpContext& dc) const
{
	CScrollView::Dump(dc);
}

CMapEditorDoc* CMapEditorView::GetDocument() const 
{
	XPASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CMapEditorDoc)));
	return (CMapEditorDoc*)m_pDocument;
}
#endif 


void CMapEditorView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	
}

void CMapEditorView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	
}

BOOL CMapEditorView::OnPreparePrinting(CPrintInfo* pInfo)
{
	
	return DoPreparePrinting(pInfo);
}

void CMapEditorView::OnInitialUpdate()
{
	CScrollView::OnInitialUpdate();


	
	CMapEditorDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
	
	SIZE size = pDoc->GetDocSize();
	SetScrollSizes(MM_TEXT, size);
	SetScrollPos(SB_HORZ, static_cast<int>(m_DrawScrollPosition.x));
	SetScrollPos(SB_VERT, static_cast<int>(m_DrawScrollPosition.y));
	SetTimer(1000, 40, NULL);
}

void CMapEditorView::OnDraw(CDC* /*pDC*/)
{
	CMapEditorDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
	
	if (m_LayerListDlg.m_hWnd == NULL)
	{
		m_LayerListDlg.Create(this);
	}
	if (m_TimeEffectEditorDlg.m_hWnd == NULL)
	{
		m_TimeEffectEditorDlg.Create(this);
	}
	if (m_MaskEditorDlg.m_hWnd == NULL)
	{
		m_MaskEditorDlg.Create(this);
	}
}

void CMapEditorView::UpdateGameTime(DWORD delta)
{
	m_nGameTime = m_nGameTime + static_cast<DWORD>(delta * m_fGameTimeDeltaScaleFactor);
	m_nGameTime %= Nuclear::XP_A_DAY_TIME;
	m_TimeEffectEditorDlg.UpdateParam();
	m_MaskEditorDlg.UpdateParams();
}

void CMapEditorView::ResetScrollPos()
{
	m_DrawScrollPosition = GetScrollPosition();
	m_DrawScrollPosition.x = static_cast<LONG>(m_DrawScrollPosition.x / m_fRatio);
	m_DrawScrollPosition.y = static_cast<LONG>(m_DrawScrollPosition.y / m_fRatio);
}

void CMapEditorView::GetAdjMovingBackground(Nuclear::CPOINT &adj)
{
	if (m_nEditMode == EMT_BACKGROUND_DRAG)
	{
		adj.x = static_cast<LONG>(m_PointMouse.x / m_fRatio) + m_DrawScrollPosition.x - m_MouseObjectPosition.x;
		adj.y = static_cast<LONG>(m_PointMouse.y / m_fRatio) + m_DrawScrollPosition.y - m_MouseObjectPosition.y;
	}
}

bool CMapEditorView::GetSpritePosAndActionFlag(Nuclear::Location &pos, Nuclear::ActionTypeFlag &atf)
{
	if (m_pSelectedSprite == NULL)
		return false;
	pos = m_pSelectedSprite->GetLocation();
	atf = m_pSelectedSprite->GetCurrentActionType();
	return true;
}

CMapEditorView::~CMapEditorView()
{
	if (m_pMouseEffect)
		delete m_pMouseEffect;
	ReleasePerformanceDlg();
	if (m_TimeEffectEditorDlg.m_hWnd != NULL)
	{
		m_TimeEffectEditorDlg.DestroyWindow();
	}
	if (m_SelectGroundDlg.m_hWnd != NULL) 
	{
		m_SelectGroundDlg.DestroyWindow();
	}
	if (m_LayerListDlg.m_hWnd != NULL) 
	{
		m_LayerListDlg.DestroyWindow();
	}
	if (m_TransparentObjectListDlg.m_hWnd != NULL)
	{
		m_TransparentObjectListDlg.DestroyWindow();
	}
	if (m_TriggerListDlg.m_hWnd != NULL)
	{
		m_TriggerListDlg.DestroyWindow();
	}
	if (m_SelectObjectDlg.m_hWnd != NULL) 
	{
		m_SelectObjectDlg.DestroyWindow();
	}
	if (m_SelectWaterDlg.m_hWnd != NULL)
	{
		m_SelectWaterDlg.DestroyWindow();
	}
	if (m_SmallMapDlg.m_hWnd != NULL) 
	{
		m_SmallMapDlg.DestroyWindow();
	}
	if (m_ObjsListDlg.m_hWnd != NULL) 
	{
		m_ObjsListDlg.DestroyWindow();
	}
	if (m_ElementResourceDlg.m_hWnd != NULL)
	{
		m_ElementResourceDlg.DestroyWindow();
	}
	if (m_BackgroundListDlg.m_hWnd != NULL) 
	{
		m_BackgroundListDlg.DestroyWindow();
	}
	if (m_PropertiesDlg.m_hWnd != NULL)
	{
		m_PropertiesDlg.DestroyWindow();
	}
	if (m_MaskEditorDlg.m_hWnd != NULL)
	{
		m_MaskEditorDlg.DestroyWindow();
	}
	if (m_pOperatorList != NULL)
	{
		delete (m_pOperatorList);
	}
	if (m_pNowOper != NULL)
	{
		delete (m_pNowOper);
	}
	std::vector<Sprite*>::iterator iter;
	for(iter=m_Sprites.begin();iter!=m_Sprites.end();iter++)
	{
		delete(*iter);
	}

	CMainFrame* pMainWnd = dynamic_cast<CMainFrame*>(theApp.GetMainWnd());
	pMainWnd->HideToolBars();
}

CMapEditorView::CMapEditorView() : m_nLayerEditID(LEI_GAMING), m_bIsShowMouseRect(false), 
m_bIsMouseDown(false), m_nEditMode(EMT_SMALL_GROUND_TYPE), m_pPerformanceDlg(NULL), 
m_pNowOper(NULL), m_nSmallLayerEditID(0), m_nBrushSize(0), m_nGameTime(0), m_fGameTimeDeltaScaleFactor(0.5),
m_pSelectedSprite(NULL), m_nAttachColor(0xFFFFFFFF), 
m_pMouseEffect(NULL), m_nBrushType(0), m_nMagnetRadius(20), m_bIsSelectedBase(false),
m_pOperatorList(NULL), m_GroundLayerEditID(Nuclear::SubMap::GL_SMALL)
{
	m_GroundColor.data = 0xFFFFFFFF;
	
	
	m_MagnetAdjust.x = 0;
	m_MagnetAdjust.y = 0;
	m_DrawScrollPosition.x = 0;
	m_DrawScrollPosition.y = 0;
	CString FilePath = theApp.GetResPath() + _T("\\MapEditorCfg.ini");
	m_nMagnetRadius = GetPrivateProfileInt(L"VIEW", L"MagnetRadio", 20, FilePath);


	wchar_t tmpProStr[256];
	GetPrivateProfileString(L"Sprite", L"Model", L"male", tmpProStr, 500, FilePath);
	m_ModelName = tmpProStr;

	std::map<std::wstring, std::wstring> compMap;
	GetPrivateProfileString(L"Sprite", L"Component", L"body:10jnanbupao,head:xuanrennantou1,weapon:10jdnandao", tmpProStr, 500, FilePath);
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

	int layerCount = m_pSpriteMgr->GetLayerCount(m_ModelName);
	std::map<std::wstring, std::wstring>::iterator comit;
	for (int i=0;i<layerCount;++i)
	{
		comit = compMap.find(m_pSpriteMgr->GetLayerName(m_ModelName, i));
		if (comit != compMap.end())
		{
			m_Components[i] = comit->second;
		} else {
			m_Components[i] = L"";
		}
	}

	GetPrivateProfileString(L"Sprite", L"RideName", L"", tmpProStr, 500, FilePath);
	m_RideName = tmpProStr;
	GetPrivateProfileString(L"Sprite", L"RunLeft", L"runleft", tmpProStr, 500, FilePath);
	m_AnimationRunLeft = tmpProStr;
	GetPrivateProfileString(L"Sprite", L"RunRight", L"runright", tmpProStr, 500, FilePath);
	m_AnimationRunRight = tmpProStr;
	GetPrivateProfileString(L"Sprite", L"Stand", L"stand1", tmpProStr, 500, FilePath);
	m_AnimationStand = tmpProStr;
	m_pOperatorList = new CActionList(this);
}

BEGIN_MESSAGE_MAP(CMapEditorView, CScrollView)
	
	ON_COMMAND(ID_FILE_PRINT, &CScrollView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CScrollView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CScrollView::OnFilePrintPreview)
	ON_COMMAND_RANGE(ID_LAYER_GROUND, ID_LAYER_GAMING, &CMapEditorView::OnSelectEditLayer)
	ON_UPDATE_COMMAND_UI_RANGE(ID_LAYER_GROUND, ID_LAYER_GAMING, &CMapEditorView::OnUpdateEditLayer)
	ON_COMMAND_RANGE(ID_SHOW_LAYER_GROUND, ID_SHOW_LAYER_WATER, &CMapEditorView::OnSelectShowLayer)
	ON_UPDATE_COMMAND_UI_RANGE(ID_SHOW_LAYER_GROUND, ID_SHOW_LAYER_WATER, &CMapEditorView::OnUpdateShowLayer)
	ON_WM_SIZING()
	ON_WM_SIZE()
	ON_WM_ERASEBKGND()
	ON_WM_MOUSEMOVE()
	ON_WM_SETFOCUS()
	ON_WM_DESTROY()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEWHEEL()
	ON_COMMAND_RANGE(ID_SHOW_GIRD_CLOSE, ID_SHOW_CITY_GIRD, &CMapEditorView::OnShowGrid)
	ON_UPDATE_COMMAND_UI_RANGE(ID_SHOW_GIRD_CLOSE, ID_SHOW_CITY_GIRD, &CMapEditorView::OnUpdateShowGrid)
	ON_COMMAND(ID_SHOW_BASE, &CMapEditorView::OnShowBase)
	ON_UPDATE_COMMAND_UI(ID_SHOW_BASE, &CMapEditorView::OnUpdateShowBase)
	ON_COMMAND(ID_Edit_MapInfo, &CMapEditorView::OnEditMapInfo)
	ON_WM_KEYDOWN()
	ON_COMMAND(ID_SHOW_SELECT_DLG, &CMapEditorView::OnShowSelectDlg)
	ON_UPDATE_COMMAND_UI(ID_SHOW_SELECT_DLG, &CMapEditorView::OnUpdateShowSelectDlg)
	ON_COMMAND(ID_SHOW_SMALL_MAP, &CMapEditorView::OnShowSmallMap)
	ON_UPDATE_COMMAND_UI(ID_SHOW_SMALL_MAP, &CMapEditorView::OnUpdateShowSmallMap)
	ON_COMMAND(ID_SHOW_ELEMENT_LIST, &CMapEditorView::OnShowElementList)
	ON_UPDATE_COMMAND_UI(ID_SHOW_ELEMENT_LIST, &CMapEditorView::OnUpdateShowElementList)
	ON_WM_KEYUP()
	ON_COMMAND(ID_EDIT_UNDO, &CMapEditorView::OnEditUndo)
	ON_UPDATE_COMMAND_UI(ID_EDIT_UNDO, &CMapEditorView::OnUpdateEditUndo)
	ON_COMMAND(ID_EDIT_REDO, &CMapEditorView::OnEditRedo)
	ON_UPDATE_COMMAND_UI(ID_EDIT_REDO, &CMapEditorView::OnUpdateEditRedo)
	ON_COMMAND(ID_VIEW_PROPERT, &CMapEditorView::OnViewProperty)
	ON_COMMAND(ID_FILE_SAVE_AS_JPG, &CMapEditorView::OnFileSaveAsJpg)
	ON_WM_CREATE()
	ON_COMMAND(ID_EDIT_COPY, &CMapEditorView::OnEditCopy)
	ON_COMMAND(ID_EXPORT_TO_RMP, &CMapEditorView::OnExportToRmp)
	ON_WM_TIMER()
	ON_WM_CONTEXTMENU()
	ON_COMMAND(ID_ZOOM_IN, &CMapEditorView::OnZoomIn)
	ON_COMMAND(ID_ZOOM_OUT, &CMapEditorView::OnZoomOut)
	ON_COMMAND(ID_NO_ZOOM, &CMapEditorView::OnZoomReset)
	ON_COMMAND(ID_SHOW_OBJ_RES, &CMapEditorView::OnShowElementResource)
	ON_COMMAND(ID_FILE_EXPORT_JPG, &CMapEditorView::OnFileExportJpg)
	ON_UPDATE_COMMAND_UI(ID_EDIT_COPY, &CMapEditorView::OnUpdateEditCopy)
	ON_COMMAND(ID_EDIT_CUT, &CMapEditorView::OnEditCut)
	ON_UPDATE_COMMAND_UI(ID_EDIT_CUT, &CMapEditorView::OnUpdateEditCut)
	ON_COMMAND(ID_EDIT_PASTE, &CMapEditorView::OnEditPaste)
	ON_UPDATE_COMMAND_UI(ID_EDIT_PASTE, &CMapEditorView::OnUpdateEditPaste)
	ON_COMMAND(ID_TIME_EFFECT, &CMapEditorView::OnTimeEffect)
	ON_COMMAND(ID_ITEM_PROP, &CMapEditorView::OnItemProperty)
	ON_UPDATE_COMMAND_UI(ID_ITEM_PROP, &CMapEditorView::OnUpdateItemProperty)
	ON_COMMAND(ID_EDIT_RESET_ALL_OBJS, &CMapEditorView::OnEditResetAllObjs)
	ON_COMMAND(ID_SHOW_EFFECT, &CMapEditorView::OnShowEffect)
	ON_UPDATE_COMMAND_UI(ID_SHOW_EFFECT, &CMapEditorView::OnUpdateShowEffect)
	ON_COMMAND(ID_TEST_LAYER_BLOCK, &CMapEditorView::OnTestLayerChunk)
	ON_UPDATE_COMMAND_UI(ID_TEST_LAYER_BLOCK, &CMapEditorView::OnUpdateTestLayerChunk)
	ON_COMMAND(ID_SHOW_PREDLG, &CMapEditorView::OnShowPredlg)
	ON_COMMAND(ID_UPDATE_LKO_EVERY_FRAME, &CMapEditorView::OnUpdateLinkedObjectEveryFrame)
	ON_UPDATE_COMMAND_UI(ID_UPDATE_LKO_EVERY_FRAME, &CMapEditorView::OnUpdateUpdateLinkedObjectEveryFrame)
	ON_COMMAND(ID_MASK_EDIT, &CMapEditorView::OnMaskEdit)
	ON_WM_RBUTTONDOWN()
END_MESSAGE_MAP()

