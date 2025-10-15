#include "StdAfx.h"
#include "Action.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CAction::~CAction(void)
{
	if (m_pActionAtomData != NULL)
	{
		delete(m_pActionAtomData);
	}
}
CAction::CAction(eActionType operatorType, int layerID)
{
	m_pActionAtomData = NULL;
	switch(operatorType)
	{
	case AT_CHANGE_CANVAS:
		m_pActionAtomData = new CCanvasActionAtom();
		break;
	case AT_TILE:
		m_pActionAtomData = new CTileActionAtom();
		break;
	case AT_SUPER_TILE:
		m_pActionAtomData = new CSuperTileActionAtom();
		break;
	case AT_TILE_COLOR:
		m_pActionAtomData = new CTileColorActionAtom();
		break;
	case AT_BIGGER_TILE:
		m_pActionAtomData = new CBiggerTileActionAtom();
		break;
	case AT_WATER:
		m_pActionAtomData = new CWaterActionAtom();
		break;
	case AT_OBJECTS:
		m_pActionAtomData = new CObjectsActionAtom();
		break;
	case AT_SINGLE_GROUND_LAYER_INFO:
		m_pActionAtomData = new CGroundLayerInfoActionAtom();
		break;
	case AT_ALL_GROUND_LAYER_INFOS:
		m_pActionAtomData = new CGroundLayerInfosActionAtom();
		break;
	case AT_NEW_DELETE_GROUND_LAYER:
		m_pActionAtomData = new CNewDeleteGroundLayerActionAtom();
		break;
	case AT_OBJECT_NAMES:
		m_pActionAtomData = new CObjectNameActionAtom();
		break;
	case AT_ELEMENT_BASE:
		m_pActionAtomData = new CElementBaseActionAtom();
		break;
	case AT_DYNAMIC_BASE:
		m_pActionAtomData = new CLinkedObjectBaseActionAtom();
		break;
	case AT_SINGLE_OBJECT_NAMES:
		m_pActionAtomData = new CSingleObjectNameActionAtom();
		break;
	case AT_TRANSPARENT_OBJECT:
		m_pActionAtomData = new CTransparentObjectActionAtom();
		break;
	case AT_TRIGGER_OBJECT:
		m_pActionAtomData = new CTriggerObjectActionAtom();
		break;
	case AT_WATER_AREA:
		m_pActionAtomData = new CWaterAreaActionAtom();
		break;
	case AT_TIME_EFFECT:
		m_pActionAtomData = new CTimeEffectActionAtom();
		break;
	case AT_MASK_PARAM:
		m_pActionAtomData = new CMaskParamActionAtom();
		break;
	case AT_BACKGROUND_INFO:
		m_pActionAtomData = new CBackgroundInfoActionAtom();
		break;
	case AT_BACKGROUND_DATA:
		m_pActionAtomData = new CBackgroundDataActionAtom();
		break;
	case AT_BACKGROUND_ORDER:
		m_pActionAtomData = new CBackgroundOrderActionAtom();
		break;
	}
	m_pActionAtomData->m_LayerID = layerID;
}

