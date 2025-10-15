#pragma once
#include "..\engine\engine\enginebase.h"
#include "..\engine\renderer\renderer.h"
#include "..\engine\common\xptypes.h"
#include "..\engine\common\util.h"
#include "..\engine\map\pimg.h"
#include "..\engine\world\distortbase.h"
#include "..\engine\sprite\sprite.h"
#include "..\engine\effect\effectmanager.h"
#include "..\engine\effect\effect.h"
#include "..\engine\effect\xapbatch.h"

#include "LayerView.h"
#include "ScreenElements.h"

class SingleEffectNotify;

class CDisplayMapBase : public CLayerView, public Nuclear::EngineBase
{

	friend class SingleEffectNotify;

private:
	union TriggerTimeKey
	{
		struct {
			int infoVetID;
			Nuclear::SubMap::TriggerInfoID infoID;
			Nuclear::SubMap::TriggerSmallID areaID;
		};
		unsigned __int64 id;
	};

public:
	const static int GRID_BLOCK_HEIGHT = 16;
	const static int GRID_BLOCK_WIDTH = 24;
	LAYER_EDIT_ID static const WATER_BEFORE_LAYER = LEI_LOW_1;
	LAYER_EDIT_ID static const LAST_ELEMENT_LAYER = LEI_HIGH_2;
	LAYER_EDIT_ID static const FIRST_ELEMENT_LAYER = LEI_WATER_1;
	friend class CMapEditorDoc;

	struct sTimeStruct
	{
		DWORD m_TriggerTime;	
		DWORD m_EffectTime;	
		bool m_bHasTriggered;	
		sTimeStruct() : m_TriggerTime(0), m_EffectTime(0), m_bHasTriggered(false) {}
	};

	typedef std::map<unsigned int, Nuclear::Effect*> EffectMap;
	typedef std::map<unsigned __int64, sTimeStruct> TriggerTimeMap;

	enum eSHOW_GIRD_STATE 
	{
		SGS_CLOSE = 0,
		SGS_SMALL_TILE,
		SGS_MIDDLE_TILE,
		SGS_BIG_TILE,
		SGS_WATER,
		SGS_800X600,
		SGS_1024X768,
		SGS_BLOCK,
		SGS_CITY,
	};

protected:
	CMapEditorApplicationDelegate m_AppDelegate;
	Nuclear::SystemResourceManager *m_pSystemResourceMgr;
	Nuclear::EffectManager *m_pEffectMgr;
	Nuclear::AniManager *m_pAnimationMgr;
	Nuclear::ConfigManager *m_pConfigMgr;
	Nuclear::SpriteManager *m_pSpriteMgr;
	Nuclear::Renderer *m_pRenderer;

	Nuclear::PictureHandle m_BackgroundSprite;


	Nuclear::PictureHandle m_TimeEffectHandle;

	std::vector<Nuclear::XapBatch> m_WaterEffectArray;	


	std::list<Nuclear::Effect*> m_TriggerEffectArray[4]; 
	std::vector<EffectMap> m_EffectArray;
	CScreenElements	m_ScreenElements;	


	unsigned int m_BackgroundColor;

	unsigned int m_GridColor;
	unsigned int m_BaseColor;
	CPoint m_DrawScrollPosition;		

	RECT m_ClientRect;		
	TriggerTimeMap m_TriggerTimeMap;
	std::set<Nuclear::IEffectNotify*> m_pNotifys;	
	struct sEffInfo
	{
		Nuclear::CRECT m_Rect;
		Nuclear::XPBase m_Base;
	};
	std::map<std::wstring, sEffInfo> m_EffectInfoMap;		
	std::map<std::wstring, Nuclear::PDistortionObject*> m_DistortObjectMap;				
	std::map<std::wstring, Nuclear::PLinkedObject*> m_LinkedObjectMap;				
	std::map<std::pair<char, unsigned int>, Nuclear::DistortBase*> m_DistortMap;	
	std::map<std::pair<char, unsigned int>, Nuclear::LkoTreeNodeList> m_TreeMap;	
	std::map<Nuclear::PictureHandle, Nuclear::PicResrc*> m_PictureResMap;				
	std::map<std::wstring, LPImgInfo> m_ImgInfoMap;					
	std::map<int, Nuclear::PictureHandle> m_BigTileMap;
	std::map<int, Nuclear::PictureHandle> m_MidTileMap;
	std::map<int, Nuclear::PictureHandle> m_TileMap;

	int m_nViewportHeight;

	int m_nViewportWidth;
	float m_fRatio;		
	int m_SelectedMbg;			
	Nuclear::CPOINT m_SmbPosition;	
	Nuclear::CRECT m_SelectedMovBackgroundPosition;	
	bool m_bUpdateLinkedObjectsEveryFrame;	
	bool m_bIsShowLayers[Nuclear::MAP_LAYER_COUNT + 3];
	bool m_bIsShowSelectElementRect;
	std::wstring m_MaskUri;
	Nuclear::PictureHandle m_MaskPicture;
	Nuclear::PictureHandle m_WaterAlphaMask;	
	Nuclear::PictureHandle m_TransparentObject;			
	bool m_bSettingMask;		 
	bool m_bSettingTimeEffect; 
	bool m_bPlayingTime;		
	DWORD m_dwTickCount; 
	bool m_bTransparent;	
	eSHOW_GIRD_STATE m_ShowGridType; 
	bool m_bShowBase;	
	bool m_bNewDay;	
	bool m_bReadyForSprite;
	bool m_bInited;
	bool m_bShowEffect;

public:
	virtual ~CDisplayMapBase(void);
	CDisplayMapBase(void);

protected:
	virtual const Nuclear::XPMaskParam *GetMaskParam() const { return NULL; }
	virtual const std::wstring& GetMaskUri() const 
	{
		static std::wstring tmp;
		return tmp;
	}
	virtual void GetAdjMovingBackground(Nuclear::CPOINT& adj) {}
	virtual bool IsLayerShow(LAYER_EDIT_ID layer) { return m_bIsShowLayers[layer]; }
	virtual bool GetSpritePosAndActionFlag(Nuclear::Location &pos, Nuclear::ActionTypeFlag &atf) { return false; }
	virtual const Nuclear::XPColorBalanceParam* GetTimeEffectParam() const = 0;
	virtual void UpdateGameTime(DWORD delta) = 0;
	virtual size_t GetSelectedObjSize() const { return 0; }
	virtual void ResetScrollPos() = 0;
	virtual HWND GetHWnd() const = 0;
	virtual unsigned int GetSelectedTransID() const = 0;
	virtual void GetWndClientRect(LPRECT lpRect) const = 0;
	virtual bool drawWaterAreaObjs() { return true; }
	virtual bool drawTriggerObjs() { return true; }
	virtual bool drawTransObjs(void) { return true; }
	virtual void ResetScrollBar() { }
	virtual bool drawSelectedRects(void) { return true; }
	virtual void DrawMouseObj(int layer) { }
	virtual bool CheckDragObj(int layer, Nuclear::SortBaseId objID) const { return false; }
	virtual bool CheckUpdateTrigger(Nuclear::SubMap::TriggerInfoID) const { return true; }

public:
	virtual CMapEditorDoc* GetDocument() const = 0;


protected:
	virtual void UpdateTriggers(DWORD dwDelta);	
	virtual void UpdateEffects(DWORD dwDelta);		
	virtual void UpdateDistortObjs(DWORD dwDelta);	
	virtual void UpdateLinkedObjs(DWORD dwDelta);	
	Nuclear::Effect* CreateTriggerEffect(const Nuclear::SubMap::TriggerInfo& info);
	Nuclear::Effect* TryTrigger(const Nuclear::SubMap::TriggerInfo& info);
	void SetRatio(float ratio);
	void ChangeScale();
	bool checkRenderer();
	void CheckDistortObjectLoad(const std::wstring& name);

	void CheckLinkedObjectLoad(const std::wstring& name);
	void CheckPictureLoad(const std::wstring& name);
	bool drawCityGrid(void);
	void DrawTriggerEffect(int i);
	bool drawTransparent(void);
	bool drawGrids(void);
	bool drawElementLayers(DWORD delta);
	bool drawWater();
	bool drawLayerTiles(Nuclear::SubMap::GroundLayers layers);
	bool drawSmallTiles(void);
	bool drawMovingBackground();
	bool drawBackground(void);
	bool drawElement(LPImgInfo pImgInfo, float x, float y, bool drawBase, unsigned int attachColor);
	bool drawElement(LPImgInfo pImgInfo, const Nuclear::FRECT& rect, bool drawBase, unsigned int attachColor);
	bool drawLinkedObjectTree(char layerID, Nuclear::SubMap::sObjInfo* ptrObjInfo, bool drawBase);
	bool drawLinkedObjectNode(const std::wstring& imgname, Nuclear::XPMATRIX3 const *m, unsigned int attachColor);
	void drawMap(void);

public:
	std::list<Nuclear::Effect*>* GetMiddleTriggerEffect() { return m_TriggerEffectArray + Nuclear::XPEL_SPRITE; }
	inline virtual Nuclear::PDistortionObject* GetDistortInfo(const std::wstring& name);
	inline virtual Nuclear::PLinkedObject* GetLinkedObjectInfo(const std::wstring& name);
	inline virtual LPImgInfo GetImgInfo(const std::wstring& name);
	virtual bool CheckDistortObjectUpdateList(char layerID, unsigned int objID, Nuclear::PDistortionObject* pDistObj, const Nuclear::CPOINT &pos, const std::wstring& name);
	virtual bool CheckLinkedObjectUpdateList(char layerID, unsigned int objID, Nuclear::PLinkedObject* pLinkedObj, const Nuclear::CPOINT &pos);
	virtual Nuclear::CRECT GetViewport() const { return Nuclear::CRECT(m_DrawScrollPosition, m_nViewportWidth, m_nViewportHeight);	}


	virtual float GetWorldScale() const { return m_fRatio; }


	virtual Nuclear::IApp* GetApp()  { return &m_AppDelegate; }
	virtual int GetTick() const { return Nuclear::GetMilliSeconds(); }
	virtual Nuclear::Renderer* GetRenderer(HWND hwnd = NULL) {	return m_pRenderer; }
	virtual Nuclear::EffectManager* GetEffectManager() { return m_pEffectMgr; }
	virtual Nuclear::AniManager* GetAniManager() {	return m_pAnimationMgr; }
	virtual Nuclear::ConfigManager* GetConfigManager() { return m_pConfigMgr; }
	virtual Nuclear::SpriteManager* GetSpriteManager() { return m_pSpriteMgr; }
	virtual Nuclear::CFileIOManager* GetFileIOManager() { return theApp.GetFileIOManager(); }
	virtual Nuclear::SystemResourceManager* GetSystemResourceManager() { return m_pSystemResourceMgr; }
	virtual Nuclear::StepSoundType* GetStepSoundType() const { return NULL; }
	void ResetWaterEffect();

	bool GetEffectBase(const std::wstring& name, Nuclear::XPBase &base);
	bool GetEffectRect(const std::wstring &name, Nuclear::CRECT& rect);
	float GetRatio() { return m_fRatio; }
	size_t GetGroundLayerCount() { return GetDocument()->GetGroundLayers().size(); }
	void ClearScreenElementLayers() { m_ScreenElements.ClearLayers(); }
	int GetGroundLayerID(int layer);	

};


inline bool CDisplayMapBase::GetEffectRect(const std::wstring &name, Nuclear::CRECT& rect)
{
	std::map<std::wstring, sEffInfo>::iterator iter = m_EffectInfoMap.find(name);
	if (iter != m_EffectInfoMap.end())
	{
		rect = iter->second.m_Rect;
		return true;
	} else {
		Nuclear::Effect* pEffect = GetEffectManager()->CreateEffect(name, false);
		if (!pEffect)
			return false;
		pEffect->AssureResource(false);
		rect = pEffect->GetRelBouningBox();
		sEffInfo &info = m_EffectInfoMap[name];
		info.m_Rect = rect;
		pEffect->GetBase(info.m_Base);
		delete pEffect;
		return true;
	}
}
inline bool CDisplayMapBase::GetEffectBase(const std::wstring& name, Nuclear::XPBase &base)
{
	std::map<std::wstring, sEffInfo>::iterator iter = m_EffectInfoMap.find(name);
	if (iter != m_EffectInfoMap.end())
	{
		base = iter->second.m_Base;
		return true;
	} else {
		Nuclear::Effect* pEffect = GetEffectManager()->CreateEffect(name, false);
		if (!pEffect)
			return false;
		pEffect->AssureResource(false);
		sEffInfo &info = m_EffectInfoMap[name];
		info.m_Rect = pEffect->GetRelBouningBox();;
		pEffect->GetBase(info.m_Base);
		base = info.m_Base;
		delete pEffect;
		return true;
	}
}

inline Nuclear::PDistortionObject* CDisplayMapBase::GetDistortInfo(const std::wstring& name)
{
	CheckDistortObjectLoad(name);
	return m_DistortObjectMap[name];
}

inline Nuclear::PLinkedObject* CDisplayMapBase::GetLinkedObjectInfo(const std::wstring& name)
{
	CheckLinkedObjectLoad(name);
	return m_LinkedObjectMap[name];
}

inline LPImgInfo CDisplayMapBase::GetImgInfo(const std::wstring& name)
{
	CheckPictureLoad(name);
	return m_ImgInfoMap[name];
}

