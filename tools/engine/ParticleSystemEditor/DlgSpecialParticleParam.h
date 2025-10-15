#pragma once
#include "afxcmn.h"


// CDlgSpecialParticleParam 对话框

class CDlgSpecialParticleParam : public CDialog
{
	DECLARE_DYNAMIC(CDlgSpecialParticleParam)

public:
	CDlgSpecialParticleParam(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CDlgSpecialParticleParam();

// 对话框数据
	enum { IDD = IDD_DIALOG_SpecialParticleParam };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();

public:
	CDocument* GetEditorDoc();
	void SetCtrlPos();
	void InitCtrl();
	void SetParaListToParticleSys();
	
	void UpdateCtrlStateParam(int nVertexRow, int nVertexCol, int nVertexType, BOOL bSetShapeTexture, BOOL bAbsrtactPsl);

	virtual BOOL OnInitDialog();
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnEnKillFocus(UINT nID);

	afx_msg void OnCbnSelchangeComboApslvertextype();
	afx_msg void OnBnClickedCheckAbstractpsl();
	afx_msg void OnBnClickedCheckDirmotion();
	afx_msg void OnBnClickedCheckCreatepologyok();

private:
	CSliderCtrl m_sliderVertexRow;
	CSliderCtrl m_sliderVertexCol;

	CComboBox m_comboPslVertexType;


private:
	BOOL m_bAbsrtactPsl;
	BOOL m_bDirMotion;
	BOOL m_bPologyOk;
	BOOL m_bSetShapeTexture;

private:
	//以下三个参数是不写进粒子的
	int m_nVertexRow; //0-----40 //转场特效的前景拆分（四边形和三角形）
	int m_nVertexCol; //0-----40
	int m_nVertexType; //自定义、四边形、三角形、2pic--把图分成两块各自沿着分割线相向运动 
	
	int m_nLineAngle;
	//以上

public:
	CSliderCtrl m_sliderLineAngle;
};

