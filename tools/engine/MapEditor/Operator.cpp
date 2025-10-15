#include "StdAfx.h"
#include "Operator.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CAction::~CAction(void)
{
	if (data != NULL)
	{
		delete(data);
	}
}
CAction::CAction(eActionType operatorType, int layerID)
{
	data = NULL;
	switch(operatorType)
	{
	case AT_CHANGE_CANVAS:
		data = new CanvasOperator();
		break;
	case AT_TILE:
		data = new TileOperator();
		break;
	case AT_SUPER_TILE:
		data = new SuperTileOperator();
		break;
	case AT_TILE_COLOR:
		data = new TileColorOperator();
		break;
	case AT_BIGGER_TILE:
		data = new BiggerTileOperator();
		break;
	case AT_WATER:
		data = new WaterOperator();
		break;
	case AT_OBJECTS:
		data = new CObjectsActionAtom();
		break;
	case AT_SINGLE_GROUND_LAYER_INFO:
		data = new CGroundLayerInfoActionAtom();
		break;
	case AT_ALL_GROUND_LAYER_INFOS:
		data = new CGroundLayerInfosActionAtom();
		break;
	case AT_NEW_DELETE_GROUND_LAYER:
		data = new CNewDeleteGroundLayerActionAtom();
		break;
	case AT_OBJECT_NAMES:
		data = new CObjectNameActionAtom();
		break;
	case AT_ELEMENT_BASE:
		data = new CElementBaseActionAtom();
		break;
	case AT_DYNAMIC_BASE:
		data = new CLinkedObjectBaseActionAtom();
		break;
	case AT_SINGLE_OBJECT_NAMES:
		data = new CSingleObjectNameActionAtom();
		break;
	case AT_TRANSPARENT_OBJECT:
		data = new CTransparentObjectActionAtom();
		break;
	case AT_TRIGGER_OBJECT:
		data = new CTriggerObjectActionAtom();
		break;
	case AT_WATER_AREA:
		data = new CWaterAreaActionAtom();
		break;
	case AT_TIME_EFFECT:
		data = new CTimeEffectActionAtom();
		break;
	case AT_MASK_PARAM:
		data = new CMaskParamActionAtom();
		break;
	case AT_BACKGROUND_INFO:
		data = new CBackgroundInfoActionAtom();
		break;
	case AT_BACKGROUND_DATA:
		data = new CBackgroundDataActionAtom();
		break;
	case AT_BACKGROUND_ORDER:
		data = new CBackgroundOrderActionAtom();
		break;
	}
	data->m_LayerID = layerID;
}

