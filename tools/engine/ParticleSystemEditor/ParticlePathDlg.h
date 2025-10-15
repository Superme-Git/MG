#pragma once

//#include <vector>

#include "..\engine\particlesystem\ParticleSysCommon.h"
#include "..\engine\particlesystem\TTFOutline.h"
// CParticlePathDlg 对话框
#include "..\engine\particlesystem\PslPathObject.h"
#include "afxcmn.h"
#include "afxwin.h"

class CParticlePathDlg : public CDialog
{
	DECLARE_DYNAMIC(CParticlePathDlg)

public:
	CParticlePathDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CParticlePathDlg();

// 对话框数据
	enum { IDD = IDD_DIALOG_PARTICLEPATHWND };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()

public:
	void SetSysPath(CString sysPath);
	BOOL GetVectorCharPathFlag() { return m_bVectorCharPath; }
	CString GetPathFileName() { return m_strPathFileName; }
	void SetPathFileName(CString pathFileName) { m_strPathFileName = pathFileName; }
	int CreateFullVectorCharStrPath();
	const std::vector<POINT>& GetFullVectorCharStrPath() { return m_vectorFullPathPoint; }
	void Loadpath(std::wstring pathFileName);
	float GetPathZoom() { return m_fPathZoom; }
	void SetPathZoom(float fPathZoom) { m_fPathZoom = fPathZoom; }

	float GetPathSpin() { return m_fPathSpin; }
	void SetPathSpin(float fPathSpin) { m_fPathSpin = fPathSpin; }

	int GetPathMirrorType() { return m_nPathMirrorType; }
	void SetPathMirrorType(int nPathMirrorType) { m_nPathMirrorType = nPathMirrorType; }

	const std::vector<int>& GetVectorKeyPointIndex() { return m_vectorKeyPointIndex; }

public:
	bool m_bBKchange;

private:
	// HBITMAP m_hBitmap; ---
	 CBitmap m_bitmap;

	 std::vector<int> m_vectorKeyPointIndex; //编辑器N键测试专用数据

private:	
	void Draw(CDC *pDC); //
	bool m_bPslCoor; //为了处理打开时预览当前使用路径...先存相对，第一次绘制的时候转为绝对坐标，用m_bPslCoor标记
	
private:
	Nuclear::CPslPathObject m_PslPathObject;
	CRect m_rctDrawPath;
	std::vector<Nuclear::PATHPOINT> m_vectorPathPoint; //存的绝对坐标！原始值..
	typedef std::vector<POINT> CurvePoint;
	std::vector<CurvePoint> m_vectorCurvePoint; //比路径点少1（曲线段数）
	std::vector<POINT> m_vectorFullPathPoint; //完整路径---包括自定义的和矢量字符的---

	int m_curSelPointIndex;
	int m_nBezierPtIndex;
	BOOL m_bAddPoint;
	BOOL m_bMovePoint;
	BOOL m_bCurveAdjust;
	BOOL m_bVectorCharPath;
	BOOL m_bMoveMap;
	BOOL m_bShowGrid;
	BOOL m_bCurveSample; //true表示美术编辑好了完整的路径，不需要程序插值了
	BOOL m_bhasKeyPoint; //当前待保存的曲线是否设置有关键点(只在m_bCurveSample为false时有用)
						 //用来判断是否需要生成关键点索引文件

	CRect m_rctMapPos;
	CPoint m_initialPoint; //拖动地图..

	CPoint m_ptFrom;
	CPoint m_ptFrom0;
	CPoint m_ptTo;
	CPoint m_ptTo0;

	CString m_strSysPath;

	//矢量字符串路径
	typedef std::vector<Nuclear::Contour> CharContor;
	std::vector<CharContor> m_vectorStrContour;

	std::vector<POINT> m_vectorCharStartPt;
	std::vector<POINT> m_vectorCharCenterPt;
	int m_nCurSelCharIndex;
	int m_nCurRotatePtIndex;

	struct CharContourRct4Point 
	{
		CPoint pt[4];
	};
	std::vector<CharContourRct4Point> m_vectorCharContourRct;

	std::vector<float> m_fVectorFontAngle; //每个字符一个角度

	bool m_bMoveChar;
	bool m_bRotateChar;
	CPoint m_pointMouseDown;

private:
	int m_nMouseMoveCnt;//鼠标移动时界面刷新频率

private://矢量字相关
	CString m_strInputText;
	int m_nFontHeight;
	int m_nFontSpace;
	float m_fFontAngle;
	CString m_strFontName;

	bool m_bModifyPath;
	CString m_strPathFileName; // //path//下面的内容，包括子目录...和文件名

private:
	void DrawBKPicture(CDC* dc);
	void DrawPathPoint(CDC* dc, Nuclear::PATHPOINT PathPoint);
	void DrawControlPoint(CDC* dc, POINT pt);
	void DrawCharContourRct(CDC* dc, POINT pt, int rx, int ry);
	void DrawCharContourRct(CDC* dc, int nCharIndex);
	float ComputerDistance(CPoint pt1, CPoint pt2);
	float ComputerMinDis4Corner(CPoint pt0, int centPtIndex);
	void RotatePoint(CPoint& pt, CPoint centPt, float fFontAngle);
	int Computer2LineAngle(const CPoint& ptAnchor, const CPoint& ptOld, const CPoint& ptNew);
	bool Computer2LineAngle(const CPoint &ptAnchor,const CPoint &ptOld,const CPoint &ptNew, float &fValue);

	void TransCoor(CPoint& pt, float fZoom, float fSpin, int nPathMirrorType); //缩放、旋转fSpin角度、镜像

	void InvertLine(CDC* pDC, CPoint ptFrom, CPoint ptTo);

	CPoint Computer3BezierCurvePoint(float u, CPoint p0, CPoint p1, CPoint p2, CPoint p3);
	void CreateVectorCurvePoint(int index, CPoint pt0, CPoint pt1, CPoint pt2, CPoint p3);
	void CreatePathCurve(int curSelPointIndex);

	void CreateVectorCharPoint(); //2008-10-20
	Nuclear::CTTFOutline* CreateOutline(CDC* pDC, int nchar, GLYPHMETRICS& gmm);

private:
	HBITMAP LoadJPG(LPCTSTR filepath);
	void TransformPathPoint(std::vector<Nuclear::PATHPOINT>& vectorPathPoint, float fPathZoom, float fPathSpin, Nuclear::PSL_PATHMIRRORTYPE nPathMirrorType);
	void WriteKeyPointIndexFile(std::wstring strPath);

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	afx_msg void OnPaint();
	afx_msg void OnClose();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnRButtonUp(UINT nFlags, CPoint point);

private:
	virtual BOOL OnInitDialog();

	afx_msg void OnNenuDelpoint();
	afx_msg void OnNenuCurve();
	afx_msg void OnNenuLink();

	afx_msg void OnBnClickedCheckAddpoint();
	afx_msg void OnBnClickedCheckMovepoint();
	afx_msg void OnBnClickedCheckCurveadjust();
	afx_msg void OnBnClickedCheckMovemap();
	afx_msg void OnBnClickedCheckShowgrid();
	afx_msg void OnBnClickedCheckCurveSample();

	afx_msg void OnBnClickedButtonReset();
	afx_msg void OnBnClickedButtonSavepath();
	afx_msg void OnBnClickedButtonLoadpath();

	afx_msg void OnBnClickedButtonSetvectorchar();
	afx_msg void OnBnClickedCheckLoadvectorchar();
	afx_msg void OnBnClickedButtonSetbkmap();


private:		
	HCURSOR m_hAddCursor;
	HCURSOR m_hMoveCursor;
	HCURSOR m_hRotateCursor;

	CSliderCtrl m_sliderPathZoom;
	CSliderCtrl m_sliderPathSpin;
	CComboBox m_comboPathMirrorType;
	float m_fPathZoom; //0.5---2.0f --default: 1.0f
	float m_fPathSpin; //-360---360 --default: 0.0f
	int m_nPathMirrorType;
	afx_msg void OnEnKillfocusEdit(UINT nID);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnCbnSelchangeComboPathmirrortype();

public:
	afx_msg void OnMenuKeypoint();
};
