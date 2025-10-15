#pragma once

//#include <vector>

#include "..\engine\particlesystem\ParticleSysCommon.h"
#include "..\engine\particlesystem\TTFOutline.h"
// CParticlePathDlg �Ի���
#include "..\engine\particlesystem\PslPathObject.h"
#include "afxcmn.h"
#include "afxwin.h"

class CParticlePathDlg : public CDialog
{
	DECLARE_DYNAMIC(CParticlePathDlg)

public:
	CParticlePathDlg(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CParticlePathDlg();

// �Ի�������
	enum { IDD = IDD_DIALOG_PARTICLEPATHWND };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

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

	 std::vector<int> m_vectorKeyPointIndex; //�༭��N������ר������

private:	
	void Draw(CDC *pDC); //
	bool m_bPslCoor; //Ϊ�˴����ʱԤ����ǰʹ��·��...�ȴ���ԣ���һ�λ��Ƶ�ʱ��תΪ�������꣬��m_bPslCoor���
	
private:
	Nuclear::CPslPathObject m_PslPathObject;
	CRect m_rctDrawPath;
	std::vector<Nuclear::PATHPOINT> m_vectorPathPoint; //��ľ������꣡ԭʼֵ..
	typedef std::vector<POINT> CurvePoint;
	std::vector<CurvePoint> m_vectorCurvePoint; //��·������1�����߶�����
	std::vector<POINT> m_vectorFullPathPoint; //����·��---�����Զ���ĺ�ʸ���ַ���---

	int m_curSelPointIndex;
	int m_nBezierPtIndex;
	BOOL m_bAddPoint;
	BOOL m_bMovePoint;
	BOOL m_bCurveAdjust;
	BOOL m_bVectorCharPath;
	BOOL m_bMoveMap;
	BOOL m_bShowGrid;
	BOOL m_bCurveSample; //true��ʾ�����༭����������·��������Ҫ�����ֵ��
	BOOL m_bhasKeyPoint; //��ǰ������������Ƿ������йؼ���(ֻ��m_bCurveSampleΪfalseʱ����)
						 //�����ж��Ƿ���Ҫ���ɹؼ��������ļ�

	CRect m_rctMapPos;
	CPoint m_initialPoint; //�϶���ͼ..

	CPoint m_ptFrom;
	CPoint m_ptFrom0;
	CPoint m_ptTo;
	CPoint m_ptTo0;

	CString m_strSysPath;

	//ʸ���ַ���·��
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

	std::vector<float> m_fVectorFontAngle; //ÿ���ַ�һ���Ƕ�

	bool m_bMoveChar;
	bool m_bRotateChar;
	CPoint m_pointMouseDown;

private:
	int m_nMouseMoveCnt;//����ƶ�ʱ����ˢ��Ƶ��

private://ʸ�������
	CString m_strInputText;
	int m_nFontHeight;
	int m_nFontSpace;
	float m_fFontAngle;
	CString m_strFontName;

	bool m_bModifyPath;
	CString m_strPathFileName; // //path//��������ݣ�������Ŀ¼...���ļ���

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

	void TransCoor(CPoint& pt, float fZoom, float fSpin, int nPathMirrorType); //���š���תfSpin�Ƕȡ�����

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
