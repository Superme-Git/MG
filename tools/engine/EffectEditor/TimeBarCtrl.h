#pragma once

#include <map>
#include <set>
#include <string>


typedef struct ClipObj
{
	std::wstring name;
	DWORD objID;
	int layerID;
	int startFrame;
	int endFrame;
	bool isSpecObj;	
} ClipObj;

class TimeBarNotify 
{
public:
	virtual bool OnUnSelectedAllItem() { return true; }
	virtual bool OnDragingPlayFrame(int frame) { return true; }
	virtual bool OnDragingPlayPos(float pos) { return true; }
	virtual bool OnUnSelectedItem(DWORD objID) { return true; }
	virtual bool OnSelectedItem(DWORD objID) { return true; }
	virtual bool OnModifyMaxFrame(int nMaxFrame) { return true; }
	virtual bool OnModifySpare(int pos) { return true; }	
	virtual bool OnModify(const ClipObj& obj) { return true; }
};

typedef std::map<DWORD, ClipObj> ClipObjMap;

class CTimeBarCtrl : public CDialog
{
	DECLARE_DYNAMIC(CTimeBarCtrl)

private:
	static int const HEAD_HEIGHT = 20;
	static int const LINE_HEIGHT = 17;
	static int const SPAR_HEIGHT = 8;
	static int const LAYER_HEAD_WIDTH = 20;
	static int const FRAME_WIDTH = 5;

	enum MOUSE_STATE
	{
		MOUSE_NORMAL,
		MOUSE_ON_MAX_FRAME_LINE,
		MOUSE_ON_SELECTED_OBJ_END,
		MOUSE_ON_PLAYPOS,
		MOUSE_DOWN,
		MOUSE_DRAGING_OBJ,
		MOUSE_DRAGING_SPARE,
		MOUSE_DRAGING_MAX_FRAME,
		MOUSE_DRAGING_SELECTED_OBJ_END,
		MOUSE_DRAGING_PLAYPOS
	};

	enum CLICK_STATE
	{
		CLICK_EMPTY_AREA,
		CLICK_A_SELECTED_CLIP,
		CLICK_A_COMMON_CLIP,
	};

public:

	enum { IDD = IDD_TIME_BAR_CTRL };




private:
	bool m_bInited;
	std::set<DWORD> m_nSelectIDs;
	MOUSE_STATE m_mouseState;
	ClipObjMap m_mapClipObjs;
	CPoint m_ptMouseDownPos;
	CPoint m_ptMousePos;
	CPoint m_ptSelectObjPos;	
	TimeBarNotify* m_pNotify;
	int m_nLayerCount;
	int m_nLayerSeparatorPos;
	int m_nMaxFrame;
	int m_nPlayPos;
	CRect m_rectClientRect;

public:
	COLORREF bgColor;
	COLORREF lineColor;
	COLORREF frontColor;
	COLORREF textColor;
	COLORREF selectColor;
	COLORREF spareColor;
	COLORREF specObjColor;
	COLORREF playLineColor;
	int mousePlayPosRange;
	bool canCross;
	bool playPosMustInScreen;


private:
	CLICK_STATE CheckSelection(const CPoint &point, DWORD &selectedID);
	void UpdateScrollPos();
	void TransScrollPos(int nBar, UINT nSBCode, UINT nPos, int step);
	void DrawAnObj(const ClipObj &obj, CDC* pDC, int scroll);

	void DrawPlayPos(CDC* pDC);
	void DrawObjs(CDC* pDC);
	void DrawGird(CDC* pDC);
	void DrawCtrl(CDC* pDC);

public:
	float GetPlayPos()
	{
		return static_cast<float>(m_nPlayPos) / FRAME_WIDTH;
	}
	void SetPlayPos(float pos)
	{
		m_nPlayPos = static_cast<int>(pos * FRAME_WIDTH);
	}

	void SetMaxFrame(int maxFrame)
	{
		m_nMaxFrame = maxFrame;
		ResetScrollBar();
	}

	void SetLayerSeparatorPos(int layerSeparatorPos)
	{
		m_nLayerSeparatorPos = layerSeparatorPos;
		ResetScrollBar();
	}
	void SetLayerCount(int layerCount)
	{
		m_nLayerCount = layerCount;
		ResetScrollBar();
	}
	void ResetScrollBar();
	void SetNotify(TimeBarNotify* notify)
	{
		m_pNotify = notify;
	}

	bool RemoveAll();

	bool DeleteAnObj(DWORD objId);
	bool ModifyAnObj(const ClipObj& obj);
	bool AddAnObj(const ClipObj& obj);
	virtual ~CTimeBarCtrl();

	CTimeBarCtrl(CWnd* pParent = NULL);   

protected:
	virtual void OnCancel() {};
	virtual void OnOK() {};
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnPaint();
};
