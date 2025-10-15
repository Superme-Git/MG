


#pragma once
#include <map>
#include <vector>

#include "..\engine\renderer\renderer.h"
#include "..\engine\common\xptypes.h"
#include "..\engine\map\pimg.h"
#include "..\engine\effect\effect.h"

#include "..\engine\world\lkotreenodelist.h"

#include "ImgInfo.h"
#include "SelectGroundDlg.h"
#include "SelectObjectDlg.h"
#include "LayerListDlg.h"
#include "TransparentObjectListDlg.h"
#include "TriggerListDlg.h"
#include "SmallMapDlg.h"
#include "ObjectListDlg.h"
#include "ActionList.h"
#include "MapEditorGroundCtrl.h"
#include "ObjectResourceDlg.h"
#include "MapEditorObjsCtrl.h"
#include "TimeEffectEditorDlg.h"
#include "SelectWaterDlg.h"
#include "PropertiesDlg.h"
#include "DisplayMapBase.h"
#include "PerformanceDlg.h"
#include "BackgroundListDlg.h"
#include "MaskEditorDlg.h"
#include "sprite.h"


#define IsKeyPressed(KEY) ((GetAsyncKeyState(KEY) & (0x8000)) != 0) 
#define IsCTRLPressed() IsKeyPressed(VK_CONTROL)
#define IsSHIFTPressed() IsKeyPressed(VK_SHIFT)
#define IsSPACEPressed() IsKeyPressed(VK_SPACE)

class CMapClipboardContentMaker;


enum EDIT_MODE_TYPE
{
	EMT_SMALL_GROUND_TYPE = 0,
	EMT_SMALL_GROUND_TILE,
	EMT_SMALL_GROUND_COLOR,
	EMT_SMALL_SUPER_DELETE,
	EMT_MIDDLE_GROUND,
	EMT_MIDDLE_GROUND_TILE,
	EMT_BIG_GROUND,
	EMT_BIG_GROUND_TILE,

	EMT_OBJECT_NORMAL,
	EMT_OBJECT_DRAG,
	EMT_OBJECT_DRAG_RECT,

	EMT_TRANSPARENT_OBJECT_NORMAL,
	EMT_TRANSPARENT_OBJECT_DRAG,
	EMT_TRANSPARENT_OBJECT_DRAG_RECT,
	EMT_TRANSPARENT_OBJECT_CHANGE_SIZE,

	EMT_TRIGGER_NORMAL,
	EMT_TRIGGER_DRAG,
	EMT_TRIGGER_DRAG_RECT,
	EMT_TRIGGER_CHANGE_SIZE,
	EMT_TRIGGER_ADD_RECT,

	EMT_WATER_TILE,
	EMT_WATER_IMMERSION_NORMAL,
	EMT_WATER_IMMERSION_DRAG,
	EMT_WATER_IMMERSION_DRAG_RECT,
	EMT_WATER_IMMERSION_CHANGE_SIZE,
	EMT_WATER_IMMERSION_ADD_RECT,

	EMT_BACKGROUND_NORMAL,
	EMT_BACKGROUND_DRAG,

	EMT_GAMING_NORMAL,
	EMT_PERFORMANCE_DLG,
};


class CMapEditorView : public CScrollView, public CDisplayMapBase
{
protected: 
	DECLARE_DYNCREATE(CMapEditorView)
	CMapEditorView();

private:
	const static int SPRITE_SPEED = 8;
	const static int JPG_HEIGHT = 256;
	const static int JPG_WIDTH = 256;

	typedef struct 
	{
		LAYER_EDIT_ID m_LayerId;			
		Nuclear::SortBaseId m_SmallId;	

	} PopupMenuItemData;


	typedef std::vector<PopupMenuItemData> PopMenuDataArray;

	struct PrintScreenParam
	{
		CString m_NameTample;
		int m_SrcWidth;				
		int m_SrcHeight;				
		int m_Width;					
		int m_Height;					
		int m_RowBegin;				
		int m_ColBegin;				
	};

	enum CLICK_STATE
	{
		CS_EMPTY_AREA = 0,
		CS_SELECTED_ELEMENT = 1,
		CS_COMMON_ELEMENT = 2
	};

	enum TRANSPARENT_OBJECT_SCAL_DIRECT
	{												
		TOSD_DIRECT_EMPTY = 0,							
		TOSD_TOP_MASK = 8,								
		TOSD_BOTTOM_MASK = 4,							
		TOSD_LEFT_MASK = 2,								
		TOSD_RIGHT_MASK = 1,								
		TOSD_LEFT_TOP = TOSD_TOP_MASK + TOSD_LEFT_MASK,			
		TOSD_RIGHT_TOP = TOSD_TOP_MASK + TOSD_RIGHT_MASK,			
		TOSD_RIGHT_BOTTOM = TOSD_BOTTOM_MASK + TOSD_RIGHT_MASK,	
		TOSD_LEFT_BOTTOM = TOSD_BOTTOM_MASK + TOSD_LEFT_MASK,		

	};

public:
	friend class ::CBackgroundListDlg;
	friend class ::CMapEditorDoc;
	friend class ::CMapEditorObjsCtrl;
	friend class ::CActionList;
	CMapEditorDoc* GetDocument() const;
	enum {
		GROUND_TYPE_BRUSH_NULL = 0xFFFF
	};

	const static int DYNAMIC_MAX_RECT_WIDEN = 20; 

	int static const LAYER_COUNT = Nuclear::MAP_LAYER_COUNT;

private:
	void ReleasePerformanceDlg()
	{
		if (m_pPerformanceDlg)
		{
			if (m_pPerformanceDlg->m_hWnd != NULL)
			{
				m_pPerformanceDlg->DestroyWindow();
			}
			delete m_pPerformanceDlg;
			m_pPerformanceDlg = NULL;
		}
	}
	void SaveNewBase();

	void CalculateImgFilePosition(const CPoint& initialPoint, const CPoint& point);
	void SaveAsJpg(int jpgWidth, int jpgHeight, const CString &nameFormat, bool showBase = false);

	void ScreenshotToFile(const PrintScreenParam &param, int& rowCount, int& colCount);
	void UpdateSprite();

	Sprite* AddSprite();
	bool SetSelectItemToClipboardContentMaker(CMapClipboardContentMaker* &pEeccm);


	bool MoveSelectedWaterAreas(long addX, long addY);
	bool MoveSelectedTriggers(long addX, long addY);
	bool MoveSelectedTransparentObjects(long addX, long addY);
	bool MoveSelectedObjects(long addX, long addY);
	void DeleteSelectedObjects();
	void SelectWaterArea( const Nuclear::CRECT& rect);
	void SelectTrigger( const Nuclear::CRECT& rect);
	void SelectTransparentObject(const Nuclear::CRECT& rect);
	void SelectLayerObject(unsigned char LayerId, const Nuclear::CRECT& rect);
	bool CheckSelectTransparentObject(unsigned int transID, Nuclear::CRECT& rect);
	CLICK_STATE CheckSelectWaterArea(const CPoint& point, Nuclear::SubMap::WaterAreaID& result, Nuclear::CRECT& rect);
	CLICK_STATE CheckSelectTrigger(const CPoint& point, Nuclear::SubMap::TriggerID& result, Nuclear::CRECT& rect);
	CLICK_STATE CheckSelectTransparentObject(const CPoint& point, unsigned int& result, Nuclear::CRECT& rect);
	bool CheckSelectElementBasePoint(const CPoint& point, Nuclear::XPIMG_BORDERPOS& selPoint);
	bool CheckSelectObjectBase(const CPoint& point, Nuclear::SortBaseId& sortBaseID);
	bool CheckSelectLayerObject(unsigned char LayerId, Nuclear::SortBaseId sortBaseID, Nuclear::CRECT& rect);
	bool CheckSelectLayerObject(unsigned char LayerId, const CPoint& point, CMenu& popup);	
	CLICK_STATE CheckSelectLayerObject(unsigned char LayerId, const CPoint& point, Nuclear::SortBaseId& result, Nuclear::CRECT& rect);
	char CheckSelectedRectPoint(const CPoint &point);
	void RemoveSelection(const unsigned int& objID, const Nuclear::CRECT& rect);
	void AddSelection(const unsigned int& objID, const Nuclear::CRECT& rect);
	void ShowAllPictures(const CPoint& point, CMenu& popup, PopMenuDataArray& data);
	bool CalculateAdjustMouseObjectV(const Nuclear::CRECT &rect, const Nuclear::CRECT &mouseRect);


	bool CalculateAdjustMouseObjectH(const Nuclear::CRECT &rect, const Nuclear::CRECT &mouseRect);
	void CalculateAdjustMouseObject(const Nuclear::CRECT &rect, bool isSelected);
	void AdjustMousePoints(void);
	void MouseMove(const CPoint & point);
	void GetTileWidthHeight(Nuclear::TILE_SIZE_TYPE sizeType, int &tileWidth, int &tileHeight);


	void CheckScalRect(const RECT &standardRect, int oldWidth, int oldHeight, int &newWidth, int &newHeight);

	bool ClientRectToWaterWorldRect(const Nuclear::CRECT &mouseRect, Nuclear::CRECT &rect);
	void GetMouseRectForWaterMode(CPoint point, Nuclear::CRECT &mouseRect);
	bool ClientRectToTileWorldRect(const Nuclear::CRECT &mouseRect, Nuclear::CRECT &rect, Nuclear::TILE_SIZE_TYPE sizeType);
	void GetMouseRectForTileTypeMode(CPoint point, Nuclear::CRECT &mouseRect, Nuclear::TILE_SIZE_TYPE sizeType);
	bool ClientRectToGroundTypeWorldRect(const Nuclear::CRECT &mouseRect, Nuclear::CRECT &rect, Nuclear::TILE_SIZE_TYPE sizeType);

	bool GetMouseRectForGroundTypeMode(CPoint point, Nuclear::CRECT &mouseRect, Nuclear::TILE_SIZE_TYPE sizeType);
	bool DrawStaticLinkedObjectTree(Nuclear::PLinkedObject *pLkoe, float x, float y, bool drawBase, unsigned int attachColor);

	bool DrawSelectDistortInMouse(void);
	bool DrawSelectLinkedObjectTreeInMouse(void);
	bool DrawSelectEffectInMouse(void);
	bool DrawSelectElementInMouse(void);
	bool DrawDistortObjectInMouse(const std::wstring &name);
	bool DrawLinkedObjectInMouse(const std::wstring &name);
	bool DrawEffectInMouse(const std::wstring &name);
	bool DrawElementInMouse(const std::wstring &name);
	astar::PathFinder m_PathFinder;

	Sprite* m_pSelectedSprite;

	std::wstring m_RideName;

	std::wstring m_AnimationStand;
	std::wstring m_AnimationRunRight;
	std::wstring m_AnimationRunLeft;
	std::wstring m_Components[8];
	std::wstring m_ModelName;
	std::vector<Sprite*> m_Sprites;
	std::wstring m_MouseEffectName;


	Nuclear::Effect* m_pMouseEffect;
	CMapEditorGroundCtrl m_GroundCtrl;	


	CMapEditorObjsCtrl m_ObjectsCtrl;		
	CBackgroundListDlg m_BackgroundListDlg;		

	CPerformanceDlg* m_pPerformanceDlg;			
	CPropertiesDlg	m_PropertiesDlg;			
	CMaskEditorDlg m_MaskEditorDlg;			
	CTimeEffectEditorDlg m_TimeEffectEditorDlg;	
	CObjectResourceDlg m_ElementResourceDlg;		
	CObjectListDlg m_ObjsListDlg;					
	CSmallMapDlg m_SmallMapDlg;					
	CSelectWaterDlg m_SelectWaterDlg;			
	CSelectObjectDlg m_SelectObjectDlg;				
	CTriggerListDlg m_TriggerListDlg;				
	CTransparentObjectListDlg m_TransparentObjectListDlg;			
	CLayerListDlg m_LayerListDlg;				
	CSelectGroundDlg m_SelectGroundDlg;			
	LPTRACTION m_pNowOper;

	LPTRACTIONLIST m_pOperatorList;	
	std::set<Nuclear::SubMap::TriggerInfoID> m_HiddenTriggerArea;	

	std::vector<Nuclear::CRECT> m_SelectedRects;	
	Nuclear::CRECT m_MouseRect;
	Nuclear::SortBaseIDSet m_SelectedObjects;	

	std::map<Nuclear::SubMap::ObjMapKey, int> m_ChangedLinkedObjects;

	std::map<Nuclear::SubMap::ObjMapKey, int> m_ChangedImages;
	CPoint m_MouseDownPoint;	

	CPoint m_PointMouse;		
	CPoint m_MouseObjectPosition;		
	CPoint m_MagnetAdjust;			
	CPoint m_ToChangePoint;

	CPoint m_InitialPoint; 

	float m_fPosY[4];
	float m_fPosX[4];
	unsigned int m_nBrushType;	

	int m_nMagnetRadius;	
	float m_fGameTimeDeltaScaleFactor;

	DWORD m_nGameTime;
	unsigned int m_nAttachColor;
	int m_nBrushSize;	
	EDIT_MODE_TYPE m_nEditMode;	
	Nuclear::SubMap::GroundLayers m_GroundLayerEditID;	
	int m_nSmallLayerEditID;	
	int m_nLayerEditID;	
	Nuclear::XPIMG_BORDERPOS m_SelectedTheBasePoint;

	bool m_bIsSelectedBasePoint;

	bool m_bIsSelectedBase;

	bool m_bIsShowMouseRect;

	bool m_bIsMouseDown;

	bool m_bIsMutiSelectedLayer;

	Nuclear::XPCOLOR m_GroundColor;

public:
	virtual void OnDraw(CDC* pDC);  
	bool AddTransparentObject(unsigned int time, int pX, int pY, int width, int height);

	void EndDragging();

	void BeginDragging();
	void SetGroundLayerShow(int layer, bool bShow); 
	void SetGroundLayerName(int layer, CString name, bool needNoticeLayerListDlg = true);
	bool DeleteGroundLayer(int layer);
	bool NewGroundLayer();
	void SetGroundLayerEdit(Nuclear::SubMap::GroundLayers layer)
	{
		SetGroundLayerEditID(layer);
		m_LayerListDlg.SelectLayer(layer);
	}
	void SetSmallLayerEdit(int layer)
	{	
		m_nSmallLayerEditID = layer;
		m_LayerListDlg.SelectLayer(layer);
	}
	void SetGroundLayerEditID(Nuclear::SubMap::GroundLayers layer)
	{
		m_GroundLayerEditID = layer;
	}
	void SetSmallLayerEditID(int layer) 
	{	
		if (layer == -1)
		{
			m_nSmallLayerEditID = -1;
			m_bIsShowMouseRect = false;
		} else {
			m_nSmallLayerEditID = GetGroundLayerID(layer);
			if (m_nSmallLayerEditID == -1)
			{
				m_bIsShowMouseRect = false;
			} else {
				m_bIsShowMouseRect = true;
			}
		}
	};
	void SetMutiSelectedLayer(bool isMutiSelect = true)  { m_bIsMutiSelectedLayer = isMutiSelect; m_bIsShowMouseRect = !isMutiSelect; }
	CObjectListDlg& GetObjectListDlg() {return m_ObjsListDlg;}
	void ChangeEditLayer(int layerID);
	void ClearAllSelection();
	bool TryUnselectTransparentObject(unsigned int transID);
	bool TrySelectTransparentObject(unsigned int transID);
	bool TryUnselectObject(unsigned char layerID, Nuclear::SortBaseId sortBaseID);
	bool TrySelectObject(unsigned char layerID, Nuclear::SortBaseId sortBaseID);
	const Nuclear::SortBaseIDSet& GetSelectedObjects() const {return m_SelectedObjects;}
	Nuclear::SubMap::GroundLayers GetGroundLayerEditId() { return m_GroundLayerEditID; }
	int GetSmallLayerEditId() {return m_nSmallLayerEditID;}
	int GetLayerEditId() {return m_nLayerEditID;}
	void SetBrushType(unsigned int brushType) { m_nBrushType = brushType;}
	int GetBrushSize() { return m_nBrushSize;}
	void SetBrushSize(int brushSize) { m_nBrushSize = brushSize;}
	EDIT_MODE_TYPE GetEditMode() { return m_nEditMode;}
	void SetEditMode(EDIT_MODE_TYPE editMode) 
	{ 
		if (m_nEditMode != editMode)
		{
			m_bIsShowSelectElementRect = false;
			m_nEditMode = editMode;
		}

	}
	void DoAction(CAction* oper) {	
		m_pOperatorList->DoOneAction(oper);
		GetDocument()->SetModifiedFlag();
	}
	void ResetMagnetAdjust() {	m_MagnetAdjust.x = 0; m_MagnetAdjust.y = 0; }
	void ResetElementList() { if (m_ObjsListDlg.m_hWnd) {m_ObjsListDlg.ResetLists(); }}
	void ReDo() { m_pOperatorList->RedoOneAction(); }
	void UnDo() { m_pOperatorList->UndoOneAction(); }
	void ToPlaySelectedTransparentObjects();
	void SetSettingMask(bool bIsSettingMask) { m_bSettingMask = bIsSettingMask; }
	void SetSettingTimeEffect(bool bIsSettingTimeEffect) { m_bSettingTimeEffect = bIsSettingTimeEffect; }
	bool IsPlayingTime() const { return m_bPlayingTime; }
	void SetPlayingTime(bool bIsPlayingTime);
	unsigned int GetAttachColor() const {return m_nAttachColor; }
	void SetAttachColor(unsigned int nAttachColor) { m_nAttachColor = nAttachColor; }
	unsigned int GetSelectedItemColor() const;
	void SetSelectedItemColor(unsigned int nColor);
	void GetSelectedItemRect(Nuclear::CRECT &rect) const;
	void ShowTriggerInfoArea(Nuclear::SubMap::TriggerInfoID id, BOOL show)
	{
		if (show)
			m_HiddenTriggerArea.erase(id);
		else
			m_HiddenTriggerArea.insert(id);
	}
	void SetSelectMoveBackground(int id) { m_SelectedMbg = id; }
	virtual astar::PathFinder * GetPathFinder() { return &m_PathFinder; }

protected:
	virtual const Nuclear::XPMaskParam *GetMaskParam() const { return &m_MaskEditorDlg.m_MaskParam; }
	virtual const std::wstring& GetMaskUri() const { return m_MaskEditorDlg.m_CurPictureName; }
	virtual void GetAdjMovingBackground(Nuclear::CPOINT &adj);
	virtual bool GetSpritePosAndActionFlag(Nuclear::Location &pos, Nuclear::ActionTypeFlag &atf);
	virtual void UpdateGameTime(DWORD delta);
	virtual void ResetScrollPos();
	virtual bool drawWaterAreaObjs();
	virtual bool drawTriggerObjs();
	virtual bool drawTransObjs(void);
	virtual HWND GetHWnd() const { return m_hWnd; }
	virtual size_t GetSelectedObjSize() const { return m_SelectedObjects.size(); }
	virtual unsigned int GetSelectedTransID() const { return *(m_SelectedObjects.begin()); }
	virtual void GetWndClientRect(LPRECT lpRect) const { GetClientRect(lpRect); }
	virtual void ResetScrollBar();
	virtual bool drawSelectedRects(void);
	virtual void DrawMouseObj(int layer);
	virtual bool CheckDragObj(int layer, Nuclear::SortBaseId objID) const;
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);

	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnInitialUpdate(); 

public:
	void SetGameTimeScaleFactor(float mul) { m_fGameTimeDeltaScaleFactor = mul; }	
	float GetGameTimeScaleFactor() const { return m_fGameTimeDeltaScaleFactor; }
	void SetGameTime(DWORD time) { m_nGameTime = time; }
	virtual int GetGameTime() { return m_nGameTime; }
	virtual std::vector<Sprite*>* GetSprites() { return &m_Sprites; }

	virtual ~CMapEditorView();

	virtual unsigned int GetWaterDepth(const Nuclear::Location& pt) const;

	virtual const Nuclear::XPColorBalanceParam* GetTimeEffectParam() const { return &m_TimeEffectEditorDlg.m_ColorBalanceParam; }
	virtual bool CheckUpdateTrigger(Nuclear::SubMap::TriggerInfoID id) const
	{
		if (m_HiddenTriggerArea.find(id) == m_HiddenTriggerArea.end())
			return true;
		return false;
	}

#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
private:
	CString m_Stemp;
#endif


protected:
	afx_msg void OnKeyUp(UINT nChar, UINT nRepCnt, UINT nFlags);	
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);


	afx_msg BOOL OnMouseWheel(UINT nFlags, short zDelta, CPoint pt);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnUpdateTestLayerChunk(CCmdUI *pCmdUI);



	afx_msg void OnTestLayerChunk();
	afx_msg void OnZoomOut();


	afx_msg void OnZoomIn();
	afx_msg void OnZoomReset();
	afx_msg void OnUpdateShowSmallMap(CCmdUI *pCmdUI);

	afx_msg void OnUpdateShowSelectDlg(CCmdUI *pCmdUI);
	afx_msg void OnUpdateShowLayer(CCmdUI* pCmdUI);
	afx_msg void OnUpdateShowGrid(CCmdUI *pCmdUI);
	afx_msg void OnUpdateShowElementList(CCmdUI *pCmdUI);
	afx_msg void OnUpdateShowBase(CCmdUI *pCmdUI);
	afx_msg void OnUpdateItemProperty(CCmdUI *pCmdUI);
	afx_msg void OnViewProperty();
	afx_msg void OnUpdateShowEffect(CCmdUI *pCmdUI);
	afx_msg void OnUpdateUpdateLinkedObjectEveryFrame(CCmdUI *pCmdUI);
	afx_msg void OnUpdateLinkedObjectEveryFrame();
	afx_msg void OnShowPredlg();
	afx_msg void OnShowEffect();
	afx_msg void OnShowSmallMap();
	afx_msg void OnShowSelectDlg();
	afx_msg void OnShowGrid(UINT nID);
	afx_msg void OnShowElementResource();
	afx_msg void OnShowElementList();
	afx_msg void OnShowBase();
	afx_msg void OnSelectShowLayer(UINT nID);
	afx_msg void OnItemProperty();
	afx_msg void OnFileSaveAsJpg();

	afx_msg void OnFileExportJpg();
	afx_msg void OnExportToRmp();
	afx_msg void OnTimeEffect();


	afx_msg void OnUpdateEditUndo(CCmdUI *pCmdUI);
	afx_msg void OnUpdateEditRedo(CCmdUI *pCmdUI);
	afx_msg void OnUpdateEditLayer(CCmdUI* pCmdUI);
	afx_msg void OnSelectEditLayer(UINT nID);
	afx_msg void OnEditMapInfo();
	afx_msg void OnUpdateEditPaste(CCmdUI *pCmdUI);

	afx_msg void OnEditPaste();
	afx_msg void OnUpdateEditCut(CCmdUI *pCmdUI);
	afx_msg void OnEditCut();
	afx_msg void OnUpdateEditCopy(CCmdUI *pCmdUI);
	afx_msg void OnEditCopy();
	afx_msg void OnMaskEdit();

	afx_msg void OnEditResetAllObjs();
	afx_msg void OnEditUndo();
	afx_msg void OnEditRedo();
	afx_msg void OnContextMenu(CWnd* /*pWnd*/, CPoint /*point*/);

	afx_msg void OnSize(UINT nType, int cx, int cy);


	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	virtual BOOL OnScroll(UINT nScrollCode, UINT nPos, BOOL bDoScroll = TRUE);
	afx_msg void OnTimer(UINT_PTR nIDEvent);


	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	virtual BOOL Create(LPCTSTR lpszClassName, LPCTSTR lpszWindowName, DWORD dwStyle, const RECT& rect, CWnd* pParentWnd, UINT nID, CCreateContext* pContext = NULL);
	DECLARE_MESSAGE_MAP()
};

#ifndef _DEBUG  
inline CMapEditorDoc* CMapEditorView::GetDocument() const
{ return reinterpret_cast<CMapEditorDoc*>(m_pDocument); }
#endif