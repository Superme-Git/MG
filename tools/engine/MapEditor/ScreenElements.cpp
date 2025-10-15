#include "StdAfx.h"
#include "..\engine\common\recttopologylist.h"
#include "..\engine\common\util.h"
#include "..\engine\common\log.h"
#include "..\engine\world\lkotreenodelist.h"
#include "ScreenElements.h"
#include "MapEditor.h"
#include "MapEditorView.h"


using namespace Nuclear;

int CScreenElements::RenewLayers()
{
	if (!m_pView)
		return RENEW_FAILD;
	bool sort = false;
	unsigned char layerCount = Nuclear::MAP_LAYER_COUNT - 1;
	if (layerCount != m_ElementLayerArray.size())
	{
		m_ElementLayerArray.clear();
		ElementLayers tmpLayers;
		unsigned char i;
		for (i=0;i<layerCount;i++)
		{
			tmpLayers.m_LayerID = i;
			m_ElementLayerArray.push_back(tmpLayers);
		}
		m_bMustRenew = true;
	}

	CRECT viewport = m_pView->GetViewport();
	if ((!m_bMustRenew) && viewport == m_LastViewportRect)
		return NEED_NOT_RENEW;
	PMap &pmap = m_pDocument->GetToolsMap();
	ElementLayersArray::iterator layer = m_ElementLayerArray.begin();
	TopologyBaseVector::iterator elementInfoIter;
	SubMap::ObjInfoVector::const_iterator objInfoIter, objInfoIterEnd;
	SubMap::sObjInfo *ptrObjInfo;
	TopologySortBase topologySortBase;
	TopologyBaseVector tmpVector;
	unsigned int tmpSortBaseID;
	CPOINT tmpCPoint;
	Location location;
	PMap::MAP_OBJ_TYPE objType;
	CLayerView::LAYER_OBJ_TYPE objLayerViewType;
	int layerID = CMapEditorView::FIRST_ELEMENT_LAYER;
	for(;layer!=m_ElementLayerArray.end();++layer,++layerID)
	{
		if (!m_pView->IsLayerShow(static_cast<CLayerView::LAYER_EDIT_ID>(layerID)))
			continue;

		tmpVector.clear();
		TopologyBaseVector& elementInfoInScreen = layer->m_TopologyArray;
		elementInfoIter = elementInfoInScreen.begin();
		for (;elementInfoIter!=elementInfoInScreen.end();++elementInfoIter)
		{

			objLayerViewType = m_pView->GetObjectType(elementInfoIter->sortBaseID);
			if (objLayerViewType == CLayerView::LOT_SPRITE || objLayerViewType == CLayerView::LOT_TRIGGER_EFFECT)
			{
				tmpVector.push_back(*elementInfoIter);
			} else {
				ptrObjInfo = static_cast<SubMap::sObjInfo*>(elementInfoIter->data);
				if (!IsRectCross(ptrObjInfo->m_Rect, viewport))
				{
					XFree(ptrObjInfo);
				} else {
					tmpVector.push_back(*elementInfoIter);
				}
			}
		}


		const std::set<unsigned int>& setElementIdInScreen = layer->m_RectTopologyList.GetSortBaseIDSet();
		elementInfoInScreen = tmpVector;
		for (int i=0;i<PMap::MAP_OBJ_COUNT;++i)
		{
			objType = static_cast<PMap::MAP_OBJ_TYPE>(i);
			objLayerViewType = CLayerView::ConvertObjectType(objType);			
			objInfoIter = m_pDocument->GetConstObjectInfo(objType, layer->m_LayerID).begin();
			objInfoIterEnd = m_pDocument->GetConstObjectInfo(objType, layer->m_LayerID).end();
			for(;objInfoIter!=objInfoIterEnd;++objInfoIter)
			{
				if ((!objInfoIter->m_bDeleted) && setElementIdInScreen.find(CLayerView::GetSortBaseId(objInfoIter->m_ObjectId, objLayerViewType)) == setElementIdInScreen.end())
				{
					if (IsRectCross(objInfoIter->m_Rect, viewport))
					{
						ptrObjInfo = static_cast<SubMap::sObjInfo*>(XAlloc(sizeof(SubMap::sObjInfo)));
						*ptrObjInfo = *objInfoIter;
						topologySortBase.data = ptrObjInfo;
						topologySortBase.sortBaseID = CLayerView::GetSortBaseId(ptrObjInfo->m_ObjectId, objLayerViewType);
						if (GetBaseRect(objType, ptrObjInfo, topologySortBase.rect))
						{
							if (objType == PMap::MAP_OBJ_LINKEDOBJ)
							{
								m_pView->CheckLinkedObjectUpdateList(static_cast<char>(layer->m_LayerID), 
									objInfoIter->m_ObjectId, 
									m_pView->GetLinkedObjectInfo(pmap.GetObjFileName(objType, ptrObjInfo->m_MapKey)),
									objInfoIter->m_Position);
							} else if (objType == PMap::MAP_OBJ_DISTORT)
							{
								const std::wstring &fn = pmap.GetObjFileName(objType, ptrObjInfo->m_MapKey);
								m_pView->CheckDistortObjectUpdateList(static_cast<char>(layer->m_LayerID), 
									objInfoIter->m_ObjectId, m_pView->GetDistortInfo(fn),
									objInfoIter->m_Position, fn);
							}
							elementInfoInScreen.push_back(topologySortBase);
						} else {
							XFree(ptrObjInfo);
						}

					}
				}
			}
		}

		if (layer->m_LayerID == CMapEditorView::LEI_MID - CMapEditorView::FIRST_ELEMENT_LAYER)
		{

			std::vector<::Sprite*>* sprites = m_pView->GetSprites();
			if (sprites != NULL)
			{
				std::vector<::Sprite*>::iterator spriteIter = sprites->begin();
				for (;spriteIter!=sprites->end();++spriteIter)
				{
					tmpSortBaseID = ((*spriteIter)->GetGUID() | CLayerView::LOT_SPRITE);
					if (setElementIdInScreen.find(tmpSortBaseID) == setElementIdInScreen.end())
					{
						topologySortBase.data = *spriteIter;	


						topologySortBase.sortBaseID = tmpSortBaseID;				
						location = (*spriteIter)->GetLocation();
						TransToDiamondRadix(location.x-10, location.y, tmpCPoint);
						topologySortBase.rect.left = tmpCPoint.x;
						topologySortBase.rect.bottom = tmpCPoint.y;
						TransToDiamondRadix(location.x+10, location.y, tmpCPoint);
						topologySortBase.rect.right = tmpCPoint.x;
						topologySortBase.rect.top = tmpCPoint.y;
						elementInfoInScreen.push_back(topologySortBase);
					}
				}
			}

			std::list<Effect*>* teffects = m_pView->GetMiddleTriggerEffect();
			if (teffects)
			{
				std::list<Effect*>::iterator tEffIter = teffects->begin();
				Effect* pEffect;
				XPBase base;
				for (;tEffIter!=teffects->end();++tEffIter)
				{
					pEffect = *tEffIter;
					tmpSortBaseID = (pEffect->GetGUID() | CLayerView::LOT_TRIGGER_EFFECT);
					if (setElementIdInScreen.find(tmpSortBaseID) == setElementIdInScreen.end())
					{
						if (pEffect->GetBase(base))
						{
							topologySortBase.data = pEffect;	
							topologySortBase.sortBaseID = tmpSortBaseID;				
							location = pEffect->GetLocation();

							TransToDiamondRadix((base.left.x+ location.x),(base.left.y+ location.y),tmpCPoint);
							topologySortBase.rect.left = tmpCPoint.x;
							topologySortBase.rect.bottom = tmpCPoint.y;
							TransToDiamondRadix((base.right.x+ location.x),(base.right.y+ location.y),tmpCPoint);
							topologySortBase.rect.right = tmpCPoint.x;
							topologySortBase.rect.top = tmpCPoint.y;
							elementInfoInScreen.push_back(topologySortBase);
						}
					}
				}
			}
			sort = true;
		} else
		{
			sort = false;
		}
		layer->m_RectTopologyList.Dump(elementInfoInScreen, sort);
	}

	m_bMustRenew = false;
	m_LastViewportRect = viewport;
	return RENEW_SUCCEEDED;
}

void CScreenElements::RemoveObjects(unsigned char layerID, const Nuclear::SortBaseIDSet& sortBaseID)
{
	if (layerID >= m_ElementLayerArray.size())
		return;
	TopologyBaseVector::iterator objInfoIter;
	TopologyBaseVector &objInfoInScreen = m_ElementLayerArray.at(layerID).m_TopologyArray;
	RectTopologyList &rectTopologyList = m_ElementLayerArray.at(layerID).m_RectTopologyList;
	SubMap::sObjInfo *ptrObjInfo;
	objInfoIter = objInfoInScreen.begin();
	TopologyBaseVector tmpVector;
	tmpVector.clear();

	for (;objInfoIter!=objInfoInScreen.end();objInfoIter++)
	{
		if (sortBaseID.find(objInfoIter->sortBaseID) != sortBaseID.end())
		{	
			ptrObjInfo = static_cast<SubMap::sObjInfo*>(objInfoIter->data);
			XFree(ptrObjInfo);
		} else {
			tmpVector.push_back(*objInfoIter);
		}
	}
	objInfoInScreen = tmpVector;
	rectTopologyList.Dump(objInfoInScreen, layerID == CMapEditorView::LEI_MID - CMapEditorView::FIRST_ELEMENT_LAYER);
}

void CScreenElements::RemoveTriggerEffect(Effect* effect)
{
	unsigned int tmpSortBaseID = effect->GetGUID() | CLayerView::LOT_TRIGGER_EFFECT;



	int layerID = CMapEditorView::LEI_MID - CMapEditorView::FIRST_ELEMENT_LAYER;
	TopologyBaseVector::iterator elementInfoIter;
	TopologyBaseVector &elementInfoInScreen = m_ElementLayerArray.at(layerID).m_TopologyArray;
	RectTopologyList &rectTopologyList = m_ElementLayerArray.at(layerID).m_RectTopologyList;
	elementInfoIter = elementInfoInScreen.begin();
	TopologyBaseVector tmpVector;
	tmpVector.clear();

	for (;elementInfoIter!=elementInfoInScreen.end();elementInfoIter++)
	{
		if (elementInfoIter->sortBaseID != tmpSortBaseID)
		{
			tmpVector.push_back(*elementInfoIter);
		}
	}
	elementInfoInScreen = tmpVector;
	rectTopologyList.Dump(elementInfoInScreen);
}

void CScreenElements::RemoveSprite(::Sprite* sprite)
{
	unsigned int tmpSortBaseID = sprite->GetGUID() | CLayerView::LOT_SPRITE;
	int layerID = CMapEditorView::LEI_MID - CMapEditorView::FIRST_ELEMENT_LAYER;
	TopologyBaseVector::iterator elementInfoIter;
	TopologyBaseVector &elementInfoInScreen = m_ElementLayerArray.at(layerID).m_TopologyArray;
	RectTopologyList &rectTopologyList = m_ElementLayerArray.at(layerID).m_RectTopologyList;
	elementInfoIter = elementInfoInScreen.begin();
	TopologyBaseVector tmpVector;
	tmpVector.clear();

	for (;elementInfoIter!=elementInfoInScreen.end();elementInfoIter++)
	{
		if (elementInfoIter->sortBaseID != tmpSortBaseID)
		{
			tmpVector.push_back(*elementInfoIter);
		}
	}
	elementInfoInScreen = tmpVector;
	rectTopologyList.Dump(elementInfoInScreen);
}

bool CScreenElements::CheckBaseRect(unsigned char layerID, const SortBaseRectType &baseRect, const CRECT &picRect) const
{
	if (layerID >= m_ElementLayerArray.size())
		return false;
	TopologyBaseVector::const_iterator elementInfoIter;
	const TopologyBaseVector &elementInfoInScreen = m_ElementLayerArray.at(layerID).m_TopologyArray;
	elementInfoIter = elementInfoInScreen.begin();

	for (;elementInfoIter!=elementInfoInScreen.end();elementInfoIter++)
	{
		if (IsRectIncision(elementInfoIter->rect, baseRect))
		{
			return false;
		}
	}


	PMap &pmap = m_pDocument->GetToolsMap();
	PMap::MAP_OBJ_TYPE type;
	CLayerView::LAYER_OBJ_TYPE layerObjType;
	SubMap::ObjInfoVector::const_iterator iter;
	SortBaseRectType tmpBaseRect;
	const SortBaseIDSet& setObjIdInScreen = m_ElementLayerArray[layerID].m_RectTopologyList.GetSortBaseIDSet();
	for (int i=0;i<PMap::MAP_OBJ_COUNT;++i)
	{
		type = static_cast<PMap::MAP_OBJ_TYPE>(i);
		layerObjType = CLayerView::ConvertObjectType(type);
		const SubMap::ObjInfoVector &objs = m_pDocument->GetConstObjectInfo(type, layerID);
		iter = objs.begin();
		for (;iter!=objs.end();++iter)
		{
			if (iter->m_bDeleted)
				continue;
			if (IsRectCross(picRect, iter->m_Rect) && setObjIdInScreen.find(CLayerView::GetSortBaseId(iter->m_ObjectId, layerObjType)) == setObjIdInScreen.end())
			{
				if (GetBaseRect(type, &(*iter), tmpBaseRect))
				{
					if (IsRectIncision(tmpBaseRect, baseRect))
					{
						return false;
					}
				}
			}
		}

	}
	return true;
}

bool CScreenElements::CheckBaseRect(const SubMap::ObjInfoVector& objInfos) const
{
	PMap &pmap = m_pDocument->GetToolsMap();
	SubMap::ObjInfoVector::const_iterator iter1, iter2;
	SortBaseRectType tmpBaseRect1, tmpBaseRect2;

	PMap::MAP_OBJ_TYPE objType;
	for (iter1=objInfos.begin();(iter1 + 1)!=objInfos.end();iter1++)
	{
		if (!CLayerView::GetObjectType(iter1->m_ObjectId, objType))
			continue;
		GetBaseRect(objType, &(*iter1), tmpBaseRect1);
		for (iter2=iter1+1;iter2!=objInfos.end();iter2++)
		{
			if (!CLayerView::GetObjectType(iter2->m_ObjectId, objType))
				continue;
			GetBaseRect(objType, &(*iter2), tmpBaseRect2);
			if (IsRectIncision(tmpBaseRect1, tmpBaseRect2))
			{
				return false;
			}

		}
	}
	return true;
}

bool CScreenElements::GetBaseRect(PMap::MAP_OBJ_TYPE objType, const SubMap::sObjInfo* pInfo, SortBaseRectType &baseRect) const
{
	static bool (CScreenElements::*pGetBaseRect[PMap::MAP_OBJ_COUNT])(const std::wstring& name, const SubMap::sObjInfo* pInfo, SortBaseRectType &rect) const =
	{
		&CScreenElements::GetImgBaseRect, &CScreenElements::GetEffectBaseRect, &CScreenElements::GetLinkedObjectBaseRect, &CScreenElements::GetDistortBaseRect
	};
	return (this->*pGetBaseRect[objType])(m_pDocument->GetToolsMap().GetObjFileName(objType, pInfo->m_MapKey), pInfo, baseRect);
}

bool CScreenElements::GetDistortBaseRect(const std::wstring& name, const SubMap::sObjInfo* pInfo, SortBaseRectType &rect) const
{
	if (!pInfo)
		return false;
	CPOINT tmpCPoint;
	PDistortionObject *pTmpInfo = m_pView->GetDistortInfo(name);
	if (!pTmpInfo)
		return false;
	std::wstring imgFilename = pTmpInfo->m_distortionObj.strImgFileName;
	imgFilename.erase(imgFilename.size()-4, 4);
	return GetImgBaseRect(imgFilename, pInfo, rect);
}

bool CScreenElements::GetLinkedObjectBaseRect(const std::wstring& name, const SubMap::sObjInfo* pInfo, SortBaseRectType &rect) const
{
	if (!pInfo)
		return false;
	CPOINT tmpCPoint;
	PLinkedObject *pTmpInfo = m_pView->GetLinkedObjectInfo(name);
	if (!pTmpInfo)
		return false;
	int adjX = pInfo->m_Position.x - pTmpInfo->m_ptCentPos.x;
	int adjY = pInfo->m_Position.y - pTmpInfo->m_ptCentPos.y;

	TransToDiamondRadix(pTmpInfo->m_ptBasePos[enum_left].x + adjX, pTmpInfo->m_ptBasePos[enum_left].y + adjY, tmpCPoint);
	rect.left = tmpCPoint.x;
	rect.bottom = tmpCPoint.y;
	TransToDiamondRadix(pTmpInfo->m_ptBasePos[enum_right].x + adjX, pTmpInfo->m_ptBasePos[enum_right].y + adjY, tmpCPoint);
	rect.right = tmpCPoint.x;
	rect.top = tmpCPoint.y;
	return true;
}

bool CScreenElements::GetEffectBaseRect(const std::wstring& name, const SubMap::sObjInfo* pInfo, SortBaseRectType &rect) const
{
	if (!pInfo)
		return false;
	Nuclear::XPBase base;
	m_pView->GetEffectBase(name, base);

	Nuclear::CPOINT tmpCPoint;	
	TransToDiamondRadix(pInfo->m_Position.x + base.left.x, pInfo->m_Position.y + base.left.y, tmpCPoint);
	rect.left = tmpCPoint.x;
	rect.bottom = tmpCPoint.y;
	TransToDiamondRadix(pInfo->m_Position.x + base.right.x, pInfo->m_Position.y + base.right.y, tmpCPoint);
	rect.right = tmpCPoint.x;
	rect.top = tmpCPoint.y;
	return true;
}

bool CScreenElements::GetImgBaseRect(const std::wstring& name, const SubMap::sObjInfo* pInfo, SortBaseRectType &rect) const
{
	if (!pInfo)
		return false;
	CPOINT tmpCPoint;

	LPImgInfo pTmpInfo = m_pView->GetImgInfo(name);
	if (!pTmpInfo)
		return false;
	TransToDiamondRadix(pTmpInfo->m_Positions[enum_left].x + pInfo->m_Rect.left, pTmpInfo->m_Positions[enum_left].y + pInfo->m_Rect.top, tmpCPoint);
	rect.left = tmpCPoint.x;
	rect.bottom = tmpCPoint.y;
	TransToDiamondRadix(pTmpInfo->m_Positions[enum_right].x + pInfo->m_Rect.left, pTmpInfo->m_Positions[enum_right].y + pInfo->m_Rect.top, tmpCPoint);
	rect.right = tmpCPoint.x;
	rect.top = tmpCPoint.y;
	return true;
}

void CScreenElements::ClearLayers()
{
	ElementLayersArray::iterator layer = m_ElementLayerArray.begin();
	TopologyBaseVector::iterator elementInfoIter;
	for(;layer!=m_ElementLayerArray.end();layer++)
	{
		elementInfoIter = layer->m_TopologyArray.begin();
		for (;elementInfoIter!=layer->m_TopologyArray.end();elementInfoIter++)
		{
			if (m_pView->GetObjectType(elementInfoIter->sortBaseID) == CLayerView::LOT_SPRITE)	
				continue;
			if (m_pView->GetObjectType(elementInfoIter->sortBaseID) == CLayerView::LOT_TRIGGER_EFFECT)	
				continue;
			XFree(elementInfoIter->data);
		}
	}
	m_ElementLayerArray.clear();
}

CScreenElements::~CScreenElements(void)
{
	ClearLayers();
}

CScreenElements::CScreenElements(void) : m_pDocument(NULL)
{
}

