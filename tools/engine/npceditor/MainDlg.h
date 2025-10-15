#pragma once
#include "afxwin.h"
#include "engine\iengine.h"
#include "soundapp.h"
//#include "NpcPropertyDlg.h"
// CMainDlg dialog

class CSoundEditorDoc;


inline CPoint PixelPointToGridPoint(CPoint pt)
{
	return CPoint(pt.x/GRID_WIDTH,pt.y/GRID_HEIGHT);
}
class CMainDlg : public CDialog
{
	DECLARE_DYNAMIC(CMainDlg)
//�Զ���ö�١�����
private:
	enum MOUSESTATE
	{
		NORMAL				= 0,
		LEFT_BUTTON_DOWN	= 1,
		RIGHT_BUTTON_DOWN	= 2,
	};

	enum TOOLS_STATE
	{
		TS_MOVE = 0,
		TS_SELECT,
		TS_PUTNPC,
		TS_SETBLOCK, //�赲
		TS_QIEZUO,//�д�
		TS_BOSA,//����
		TS_BAITAN,//��̯
		TS_DUST,
		TS_GRASS,
		TS_WATER,
		TS_KITE,	//
		TS_SHUAGUAI = 11, //ˢ��
		TS_MINGSHENG,
		TS_QINHGONG1,
		TS_QINHGONG2,
		TS_QINHGONG3,
		TS_QINHGONG4,
		TS_GUDAO,
		TS_AREA11,
		TS_AREA12,
		TS_AREA13,
		TS_AREA14,
		TS_AREA15,
		TS_AREA16,
		TS_SETBLOCK2,  //2���赲
		TS_GUDAO2,     //2��µ�
		TS_GAOJISHUGUAI,//�߼�ˢ��
	};

public:
	// Dialog Data
	enum { IDD = IDD_MAIN_DLG };
	SoundApp m_soundApp;

//��Ա��
private:
	CSoundEditorDoc* m_pDoc;
	//CNpcPropertyDlg m_dlgNpcDlg;
	
	Nuclear::IEngine *m_pEngine;
	Nuclear::IWorld *m_pWorld;
	bool m_bIsLoadedMap;	//�Ƿ��Ѿ������˵�ͼ
	
	int m_mouseState;
	TOOLS_STATE m_toolState;
	CPoint m_oldMousePoint;			//��������
	CPoint m_oldViewportPos;		//��������

	CString m_mazefilename;		//��Ӧ�ĵ�ͼmaze�ļ�
	CString m_NpcXmlFileName;   //NPC XML�ļ�
	std::wstring m_RegionTypeInfoFileName;
	std::wstring m_JumpBlockFileName;
	std::wstring m_IslandInfFileName;
	std::wstring m_IslandInf2FileName;

	//2015 Yao
	std::wstring	m_MonsterFileName;

	bool			m_bAllScreenBrush;
	int				m_EdgeSize;

//������
private:
	void SetOldViewportPoint();
	void MoveViewPort(int dx, int dy);
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

	void SetMazeFileName(const CString name){m_mazefilename = name;}
	void SetRegionTypeInfoFileName(const CString name)
	{
		m_RegionTypeInfoFileName = name;
	}
	void SetNPcXmlFileName(const CString name){m_NpcXmlFileName = name;}

	void SetJumpBlockFileName(const CString& name) { m_JumpBlockFileName=name; }
	void SetIslandInfFileName(const CString& name) { m_IslandInfFileName=name; }

	void SetIslandInf2FileName(const CString& name) { m_IslandInf2FileName=name; }

    //2015 Monster Dat
	void SetIsMonsterFileName(const CString& name){ m_MonsterFileName=name; }
	//-

	bool LoadMap(const CString& name);
	bool UnloadMap();
	void OnUpdate();
	
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
	afx_msg void OnDrawBlock();
	afx_msg void OnUpdateDrawBlock(CCmdUI *pCmdUI);
	afx_msg void OnSavemaze();
	afx_msg void On32786();
	afx_msg void OnAllScreenBrush();
	afx_msg void OnUpdateAllScreenBrush(CCmdUI *pCmdUI);
};
