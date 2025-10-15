
#include "..\engine\common\util.h"
#include "..\engine\effect\geffect.h"
#include <map>
#pragma once

enum eToolState
{
	TS_ARROW = ID_TOOL_ARROW,
	TS_BOUNDS_RECT = ID_TOOL_BOUNDS_RECT,
	TS_MOVE = ID_TOOL_MOVE,
	TS_FRAME_POS = ID_TOOL_FRAME_POS,
	TS_FRAME_ROTATE = ID_TOOL_FRAME_ROTATE,
	TS_FRAME_SIZE = ID_TOOL_FRAME_SIZE
};

class CEffectEditorView : public CView, Nuclear::IEffectClipControl, Nuclear::EngineBase
{
protected: 
	CEffectEditorView();
	DECLARE_DYNCREATE(CEffectEditorView)

private: 
	typedef std::map<Nuclear::AbstractEffectClip*, Nuclear::CPOINT> ClipOldPosMap;
	enum eBoundsAdjPos
	{
		BAP_TOP = 1,		
		BAP_BOTTOM = 2,	
		BAP_LEFT = 4,	
		BAP_RIGHT = 8,	
	};
	enum eMouseMode
	{
		MM_NORMAL,
		MM_BOUNDS_ADJUST_RECT,
		MM_MODIFYING_SCAL
	};
	enum eBackgroundMode
	{
		BM_COLOR = 0,
		BM_PICTURE
	};
	
public:
	static int const ADJ_POS[8];
	static int const MODIFY_RECT_RADIUS = 100;
	static int const MOUSE_NEAR = 5;
	static int const LAST_TOOL_ID = TS_FRAME_SIZE;
	static int const FIRST_TOOL_ID = ID_TOOL_ARROW;

private:
	std::wstring m_TryOpenFilename;
	Nuclear::CRECT m_ClientRect;
	Nuclear::DirectSoundManager *m_pSoundManager;
	Nuclear::EffectManager *m_pEffectManager;
	Nuclear::AniManager *m_pAnimationManager;
	Nuclear::SpriteManager *m_pSpriteMgr;
	Nuclear::ConfigManager *m_pConfigMgr;
	Nuclear::Renderer *m_pRenderer;
	int m_ScaleMousePosition;	


	Nuclear::FPOINT m_OldScal;	
	float m_OldAngle;	
	int m_nAdjPosition;
	eMouseMode m_MouseMode;
	Nuclear::XPVECTOR2 m_AdjPointArray[8];
	// 1 0 7
	// 2   6
	// 3 4 5
	CPoint m_MousePosition;
	CPoint m_MouseDownPosition;
	ClipOldPosMap m_OldAnimationPosition;
	Nuclear::FPOINT m_OldBgPosition;
	Nuclear::FPOINT m_BgPosition;
	Nuclear::Location m_OldEffPosition;
	Nuclear::Location m_EffPosition;
	HCURSOR m_hMouseCursor;
	eToolState m_ToolsState;
	eBackgroundMode m_BgMode;
	float m_fBgPicHeight;
	float m_fBgPicWidth;
	Nuclear::PictureHandle m_BackgroundPicture;
	Nuclear::XPCOLOR m_BackgroundColor;
	bool m_bShowBox;
	bool m_bShowCenterCross;
	bool m_bRMouseButtonDown;
	bool m_bMMouseButtonDown;
	bool m_bLMouseButtonDown;
	bool m_bInited;

public:
	CEffectEditorDoc* GetDocument() const;


private:
	bool SetBackgroundPicture(const wchar_t * filename);
	bool ResetMouseCurData();
	int CheckAniSizeMousePosition(const CPoint &pt, const Nuclear::Effect* pAni);
	float CalcMouseAngle() const;
	void ArrowKeyDownMove(int x, int y);
	void SetPositionFromOldAniPosition(const CPoint &adjPt);
	void SetOldAniPosition();
	void GetAdjBoundsRect(const CPoint &pt, Nuclear::CRECT& rect);
	int CheckBoundsAdjPosition(const CPoint &pt);
	void RenderEffect();
	bool Tick();
	bool CheckAndCreateRenderer();

public:


public:
	virtual bool GetRotationRadian( float& angle) const;
	virtual bool GetScale( Nuclear::FPOINT& scale) const;
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);


	virtual void OnDraw(CDC* pDC);  

protected:
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);


public:
	virtual Nuclear::IApp* GetApp() { return NULL; }
	virtual int GetTick() const { return Nuclear::GetMilliSeconds(); }
	virtual Nuclear::CRECT GetViewport() const { return m_ClientRect; }
	virtual Nuclear::DirectSoundManager* GetSoundManager() { return m_pSoundManager; }
	virtual Nuclear::Renderer* GetRenderer(HWND hwnd = NULL) { return m_pRenderer; }
	virtual Nuclear::EffectManager* GetEffectManager() { return m_pEffectManager; }
	virtual Nuclear::AniManager* GetAniManager() { return m_pAnimationManager; }
	virtual Nuclear::ConfigManager* GetConfigManager() { return m_pConfigMgr; }
	virtual Nuclear::SpriteManager* GetSpriteManager() { return m_pSpriteMgr; }
	virtual Nuclear::CFileIOManager* GetFileIOManager() { return theApp.GetFileIOManager(); }
	virtual Nuclear::StepSoundType* GetStepSoundType() const { return NULL; }
	void SetLaterOpenFile(LPCTSTR lpszPathName) { m_TryOpenFilename = lpszPathName; }

#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif
	virtual ~CEffectEditorView();

protected:
	void ChangeSize();


protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnMButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnUpdateBackgroundMode(CCmdUI* pCmdUI);	
	afx_msg void OnSelectBackgroundMode(UINT nID);
	afx_msg void OnBackgroundPicture();
	afx_msg void OnBackgroundColor();
	afx_msg void OnUpdateShowCenterCross(CCmdUI *pCmdUI);
	afx_msg void OnShowCenterCross();
	afx_msg void OnUpdateShowBox(CCmdUI *pCmdUI);
	afx_msg void OnShowBox();
	afx_msg void OnUpKeyDown();
	afx_msg void OnDownKeyDown();
	afx_msg void OnLeftKeyDown();
	afx_msg void OnRightKeyDown();
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnRButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnNextFrame();
	afx_msg void OnPrevFrame();
	afx_msg void OnPlayStop();
	afx_msg void OnStop();
	afx_msg void OnPause();
	afx_msg void OnPlay();
	afx_msg void OnToolsControl(UINT nID);
	afx_msg void OnUpdateToolsControl(CCmdUI* pCmdUI);
	afx_msg void OnUpdateControl(CCmdUI* pCmdUI);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg void OnTimer(UINT_PTR nIDEvent);
	virtual void OnInitialUpdate();
};

#ifndef _DEBUG  
inline CEffectEditorDoc* CEffectEditorView::GetDocument() const
   { return reinterpret_cast<CEffectEditorDoc*>(m_pDocument); }
#endif

