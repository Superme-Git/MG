#pragma once
#include "afxwin.h"
#include "..\engine\iengine.h"
#include "soundapp.h"
// CMainDlg dialog

class CSoundEditorDoc;
class CMainDlg : public CDialog
{
	DECLARE_DYNAMIC(CMainDlg)
//�Զ���ö�١�����
private:
	enum TOOLS_STATE
	{
		TS_MOVE,
		TS_SELECT,
		TS_POLYGON,
		TS_CIRCLE
	};

public:
	// Dialog Data
	enum { IDD = IDD_MAIN_DLG };

//��Ա��
private:
	CSoundEditorDoc* m_pDoc;
	Nuclear::IEngine *m_pEngine;
	Nuclear::IWorld *m_pWorld;
	bool m_bIsLoadedMap;	//�Ƿ��Ѿ������˵�ͼ
	SoundApp m_soundApp;
	int m_mouseState;
	TOOLS_STATE m_toolState;
	CPoint m_oldMousePoint;			//��������
	CPoint m_oldViewportPos;		//��������

//������
private:
	void SetOldViewportPoint();
	void MoveViewPort(int dx, int dy);
	void GetStepSoundMouseRect(const POINT &pt, RECT &rect);	//������������
	static void Client2World(const POINT &clientpt, const RECT& vp, POINT &worldpt)
	{
		worldpt.x = clientpt.x + vp.left;
		worldpt.y = clientpt.y + vp.top;
	}
	static void World2Client(const POINT &worldpt, const RECT& vp, POINT &clientpt)
	{
		clientpt.x = worldpt.x - vp.left;
		clientpt.y = worldpt.y - vp.top;
	}

public:
	CMainDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CMainDlg();

	bool LoadMap(const CString& name);
	bool UnloadMap();
	void OnUpdate();
	void ShowMaze(bool b);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
private:
	
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnTimer(UINT_PTR nIDEvent);
protected:
	virtual LRESULT WindowProc(UINT message, WPARAM wParam, LPARAM lParam);
public:
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnRButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnTool(UINT nID);
	afx_msg void OnUpdateTool(CCmdUI* pCmdUI);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnKeyUp(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnDrawGird();
	afx_msg void OnUpdateDrawGird(CCmdUI *pCmdUI);
	afx_msg void OnMazeInfo();
};
