#pragma once

#include "..\engine\renderer\renderer.h"
#include "..\engine\particlesystem\particlemanager.h"
#include "..\engine\particlesystem\pslgroupobject.h"

#include "PSParameterDlg.h"

// CTestManagerDlg 对话框

#include <D3D9.h>
#include "afxcmn.h"
#include "afxwin.h"

class CTestManagerDlg : public CDialog
{
	DECLARE_DYNAMIC(CTestManagerDlg)

public:
	CTestManagerDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CTestManagerDlg();

//	CImageList m_ImageList;

// 对话框数据
	enum { IDD = IDD_DIALOG_TestManager };

private:
	Nuclear::Renderer *m_pRenderer;
	Nuclear::ParticleManager* m_pParSysManager;

	CRect m_rectDrawParticle;
	CString m_strSysPath;


	HTREEITEM m_curSelItem;
	HTREEITEM m_curGroupItem;

public:
	bool m_bBKchange;

public:
//	std::vector<Nuclear::pslgroupobject> m_vectorPSLgroup;
//	std::vector<PSLGROUP> m_vectorPSLs;	
	Nuclear::pslgroupobject m_PSLgroup;

private:
	bool InitD3D();
	void Render();

private:
	int m_nComputerConfig;
	float m_fFPS;
	int m_nFrameCnt;
	float m_fTimeElapsed;
	float m_fLastTime;

	Nuclear::PictureHandle m_hBKpicHandle; //背景图片纹理句柄..

	CPSParameterDlg* m_pPSParameterDlg;

	BOOL m_bLineEmiterDemo;
	BOOL m_bMoveBKMap;
	BOOL m_bMoveEffect;
	Nuclear::FRECT m_FrctMapPos;
	CPoint m_initialPoint; //拖动地图..
	Nuclear::CRECT m_ViewPort;

	std::vector<Nuclear::FPOINT> m_fpoints; //特效链的路径...每亮点连成一条线作为发射器
	std::vector<Nuclear::ParticleSystemHandle> m_vectorPS; //按路径段顺序播放的粒子特效序列...

	int m_nCurSelPt;//当前选中的点索引

private:
	CTreeCtrl m_treeParticleSys;
	CImageList* m_pDragImage;	//拖曳图像
	bool		m_bDragging;	//是否正在拖曳
	HTREEITEM	m_hItemDrag;	//开始拖曳的位置
	HTREEITEM	m_hItemDrop;	//拖曳到哪里

private:
	CImageList* CreateDragImageEx(HTREEITEM hItem);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()

private:
	bool ModifyParticleSysPosInfo(CPoint pt);
	HTREEITEM DelChildItems(HTREEITEM hItem);
	bool SaveGroup(LPCTSTR filePath, const std::vector<std::wstring>& EffectGroup); //老版本文件读写
	bool LoadGroup(LPCTSTR filePath, std::vector<std::wstring>& EffectGroup);
	bool SaveGroup(LPCTSTR filePath); //新版本文件读写
	bool LoadGroup(LPCTSTR filePath); //

	int FindFilesInSpecialPath(const CString& strSysPath, const CString& strFileName, std::vector<CString>& fileFullPathVector);

	float ComputerDistance(Nuclear::FPOINT pt1, Nuclear::FPOINT pt2);

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();

	afx_msg void OnTimer(UINT_PTR nIDEvent);

	afx_msg void OnNMClickTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclickTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult);

	afx_msg void OnMenuAddparticlesys();
	afx_msg void OnMenuDelparticlesys();
	afx_msg void OnMenuModifyparticlesys();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);

	afx_msg void OnMenuAddeffectgroup();
	afx_msg void OnMenuModifygroupname();
	afx_msg void OnMenuDeleffectgroup();
	afx_msg void OnMenuSaveeffectgroup();
	afx_msg void OnMenuLaodeffectgroup();
	afx_msg void OnBnClickedCheckMovebkmap();
	afx_msg void OnBnClickedCheckMoveeffect();

	afx_msg void OnBnClickedButtonSetbkmap();
	afx_msg void OnTvnBegindragTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult);

	afx_msg void OnMenupslproperty();
	afx_msg void OnMenuPsreplay();
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnTvnKeydownTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnClose();
	afx_msg void OnBnClickedButtonPslgfileverconversion();
	void ConversionPSLGFile(std::vector<CString> &VectorPSLGFileName);

	afx_msg void OnBnClickedRadioLow();
	afx_msg void OnBnClickedRadioMiddle();
	afx_msg void OnBnClickedRadioHigh();

	void InitCtrlPriorityParam();
	void SetParticleSysPriorityParam();

public:
	CComboBox m_comboComputerConfig;
	afx_msg void OnCbnSelchangeComboComputerconfig();

public:
	BOOL m_nLowMiddleHigh;
public:
	afx_msg void OnTvnSelchangedTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult);
public:
	afx_msg void OnBnClickedSaveConfigparam();
	afx_msg void OnBnClickedCheckLineemiterdemo();
	afx_msg void OnLButtonDblClk(UINT nFlags, CPoint point);
};
