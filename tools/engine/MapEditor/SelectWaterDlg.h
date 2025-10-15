#pragma once
#include "afxwin.h"
#include "..\engine\engine\enginebase.h "
#include "..\engine\engine\configmanager.h"
#include "..\engine\effect\effectmanager.h"


#include "ToolsMap.h"
class CMapEditorView;
enum EDIT_MODE_TYPE;

class CDirectXTabCtrl : public CTabCtrl, public Nuclear::EngineBase
{
	DECLARE_DYNAMIC(CDirectXTabCtrl)
private:
	RECT m_DestRect;
	std::vector<Nuclear::Effect*> m_WaterEffects;	
	Nuclear::EffectManager *m_pEffectMgr;
	Nuclear::AniManager *m_pAniMgr;
	Nuclear::ConfigManager *m_pConfigMgr;
	Renderer *m_pRenderer;

public:
	~CDirectXTabCtrl();
	CDirectXTabCtrl();
	int m_nBrushType;

	bool m_bDrawWater;

private:
	void checkRenderer(void) {
		if( m_pRenderer == NULL ) 
		{
			CreateRenderer(&m_pRenderer, m_hWnd, 
				Nuclear::XDisplayMode(Nuclear::SubMap::PGround::WATER_WIDTH, 
				Nuclear::SubMap::PGround::WATER_HEIGHT), theApp.GetFileIOManager());
			m_pConfigMgr = new Nuclear::ConfigManager(this);
			m_pConfigMgr->Init();
			m_pEffectMgr = new Nuclear::EffectManager(this);
			Nuclear::CRECT sourceRect(0, 0, Nuclear::SubMap::PGround::WATER_WIDTH, 
				Nuclear::SubMap::PGround::WATER_HEIGHT);
			m_DestRect.left = 23;
			m_DestRect.top = 30;
			m_DestRect.right = Nuclear::SubMap::PGround::WATER_WIDTH + m_DestRect.left;
			m_DestRect.bottom = Nuclear::SubMap::PGround::WATER_HEIGHT + m_DestRect.top;
			Nuclear::CRECT desRect(m_DestRect);
			m_pRenderer->SetDestRect(&desRect);
			m_pRenderer->SetSourceRect(&sourceRect);
		} 
	}
public:
	virtual Nuclear::IApp* GetApp() { return NULL; }
	virtual int GetTick() const { return GetTickCount(); }
	virtual Nuclear::CRECT GetViewport() const { return Nuclear::CRECT(0, 0, 128, 128); }
	virtual Nuclear::ConfigManager* GetConfigManager() { return m_pConfigMgr; }
	virtual Nuclear::Renderer* GetRenderer(HWND hwnd = NULL) { return m_pRenderer; }
	virtual Nuclear::EffectManager* GetEffectManager() { return m_pEffectMgr; }
	virtual Nuclear::AniManager* GetAniManager() { return m_pAniMgr; }
	virtual Nuclear::SpriteManager* GetSpriteManager() { return NULL; }
	virtual Nuclear::CFileIOManager* GetFileIOManager() { return theApp.GetFileIOManager(); }
	virtual Nuclear::StepSoundType* GetStepSoundType() const { return NULL; }
	void PaintEffect();

	void SetWater(int i, const std::wstring &name);
	void InvalidateDXRect() { InvalidateRect(&m_DestRect, FALSE); }

	DECLARE_MESSAGE_MAP()
};

class CSelectWaterDlg : public CDialog
{
	DECLARE_DYNAMIC(CSelectWaterDlg)
private:
	std::map<unsigned int, HTREEITEM> m_TreeItemMap;
	EDIT_MODE_TYPE m_EditMode;

	Nuclear::SubMap::WaterTileType	m_nTileType;

	CButton m_btnWaterTypeCheck[4];

	CButton m_btnNewArea;
	CButton m_btnEdit;
	CButton m_btnNew;
	CButton m_btnDelete;
	CStatic m_btnBrushSizeText;
	CComboBox m_comboBrushSize;
	CTreeCtrl m_treeWaterArea;
	CTreeCtrl m_treeWaterList;
	CDirectXTabCtrl m_dxTab;
	CMapEditorView *m_pMapEditorView;

public:
	enum { IDD = IDD_DLG_SELECT_WATER };
	virtual ~CSelectWaterDlg();

	CSelectWaterDlg(CWnd* pParent = NULL);   

private:
	bool SetBrushSize(CString &val);
	void RenewCombo();
	void RenewEditMode();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	void UnselectAllItems();
	void TryChangeSelectWaterArea(Nuclear::SubMap::WaterAreaID id);
	EDIT_MODE_TYPE GetCurEditMode() { return m_EditMode; }
	bool HasSelectWaterArea() const;
	Nuclear::SubMap::WaterAreaInfoID GetCurInfoID() const;
	void RenewList();

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedWaterTypeCheck(UINT nID);
	afx_msg void OnBnClickedButton1();
	afx_msg void OnBnClickedButtonDeleteLayer();
	afx_msg void OnBnClickedButtonNewLayer();
	afx_msg void OnBnClickedButton2();
	afx_msg void OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult);	
	afx_msg void OnTimer(UINT_PTR nIDEvent);
	afx_msg void OnTvnSelchangedTree1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnCbnSelchangeBrushSize();	
	afx_msg void OnCbnEditchangeBrushSize();
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	virtual BOOL OnInitDialog();
};
