
#pragma once
#include <vector>
#include <list>
#include "..\engine\map\pmap.h"
#include "..\engine\common\recttopologylist.h"
#include "MapEditorDoc.h"
#include "LayerView.h"
#include "sprite.h"

class CScreenElements
{
public:
	enum
	{
		RENEW_FAILD = 0,
		NEED_NOT_RENEW = 1,
		RENEW_SUCCEEDED = 2
	};

	struct ElementLayers
	{
		unsigned char m_LayerID;
		Nuclear::TopologyBaseVector m_TopologyArray;
		Nuclear::RectTopologyList m_RectTopologyList;
	};

	typedef std::vector<ElementLayers> ElementLayersArray;

private:
	bool GetDistortBaseRect(const std::wstring& name, const Nuclear::SubMap::sObjInfo* pInfo, Nuclear::SortBaseRectType &rect) const;
	bool GetLinkedObjectBaseRect(const std::wstring& name, const Nuclear::SubMap::sObjInfo* pInfo, Nuclear::SortBaseRectType &rect) const;
	bool GetEffectBaseRect(const std::wstring& name, const Nuclear::SubMap::sObjInfo* pInfo, Nuclear::SortBaseRectType &rect) const;
	bool GetImgBaseRect(const std::wstring& name, const Nuclear::SubMap::sObjInfo* pInfo, Nuclear::SortBaseRectType &rect) const;

	ElementLayersArray m_ElementLayerArray;
	Nuclear::CRECT m_LastViewportRect;
	bool m_bMustRenew;
	CLayerView* m_pView;
	CMapEditorDoc* m_pDocument;

public:
	bool CheckBaseRect(const Nuclear::SubMap::ObjInfoVector& objInfos) const;
	bool CheckBaseRect(unsigned char layerID, const Nuclear::SortBaseRectType &baseRect, const Nuclear::CRECT &picRect) const;
	void RemoveObjects(unsigned char layerID, const Nuclear::SortBaseIDSet& sortBaseID);
	void RemoveTriggerEffect(Nuclear::Effect* effect);
	void RemoveSprite(Sprite* sprite);
	bool GetBaseRect(Nuclear::PMap::MAP_OBJ_TYPE objType, const Nuclear::SubMap::sObjInfo* pInfo, Nuclear::SortBaseRectType &baseRect) const;

	const ElementLayersArray& GetElements() const 
	{
		return m_ElementLayerArray;
	}

	void SetMustRenew(bool isMustRenew = true)
	{
		m_bMustRenew = isMustRenew;
	}

	void Init(CMapEditorDoc *pDoc, CLayerView* pView)
	{
		m_pDocument = pDoc;
		m_pView = pView;
	}
	void ClearLayers();
	int RenewLayers();
	~CScreenElements(void);
	CScreenElements(void);
};
