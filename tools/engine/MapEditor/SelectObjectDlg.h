
#ifndef SELECT_OBJ_DLG_H
#define SELECT_OBJ_DLG_H

#pragma once


#include "afxcmn.h"
#include <vector>
#include <map>

#include "..\engine\common\xptypes.h"
#include "..\engine\map\pmap.h"

#include "LayerView.h"

class CMapEditorView;

using Nuclear::Renderer;
using Nuclear::XDisplayMode;

class CSelectObjectDlg : public CDialog
{
	DECLARE_DYNAMIC(CSelectObjectDlg)
private:
	typedef std::map<unsigned int, LPImgInfo> ImgInfoMap;

private:
	unsigned int m_FirstObjectIds[PMap::MAP_OBJ_COUNT];
	std::vector<CString> m_ObjectFilenameArr;
	CTreeCtrl m_Tree;
	HTREEITEM m_ItemSelect;	
	CRect m_ColorRect;
	bool m_bSizing;
	unsigned int m_Color;
	int m_nMinHeight;
	int m_nMinWidth;
	int m_nPictureHeight;
	int m_nPictureWidth;
	Nuclear::CRECT m_DestRect;
	unsigned int m_nBrushType;
	int m_nPathLength;
	Nuclear::FRECT m_PictureRect;
	ImgInfoMap m_ImgInfoMap;
	int m_nPictureId;
	Renderer *m_pRenderer;
	XDisplayMode m_DisplayMode;
	CMapEditorView* m_pMapEditorView;

public:
	
	
	


	enum { IDD = IDD_DLG_SELECT_OBJ };

private:
	afx_msg void OnBnClickedSelectBackgroundColor();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnTvnSelchangedTree1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnPaint();
	afx_msg void OnShowWindow(BOOL bShow, UINT nStatus);
	void ResetDisplay();
	void ResetDistortDisplay();
	void ResetLinkedObjectDisplay();
	void ResetEffectDisplay();
	void ResetElementDisplay();
	void checkRenderer(void) {
		if( m_pRenderer == NULL ) 
		{
			CreateRenderer(&m_pRenderer, m_hWnd, Nuclear::XDisplayMode(m_nPictureWidth, m_nPictureHeight), theApp.GetFileIOManager());
			m_DisplayMode = m_pRenderer->GetDisplayMode();
			ResizeRect(m_nPictureWidth, m_nPictureHeight);
		} 
	}
	void ResizePicture();
	void ResizeRect(int width, int height);
	bool DrawElement(void);
	void RenewSize();
	bool RenewDistortTrees();
	bool RenewLinkedObjectTrees();
	bool RenewEffectTrees();
	bool RenewElementTrees();
	bool RenewTrees(void);

protected:
	DECLARE_MESSAGE_MAP()
	virtual void OnOK();

	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	const CString& GetFilenameAndType(unsigned int brushType, PMap::MAP_OBJ_TYPE &type)
	{
		for (int i=PMap::MAP_OBJ_COUNT-1;i>=0;--i)
		{
			if (m_nBrushType >= m_FirstObjectIds[i])
			{
				type = static_cast<PMap::MAP_OBJ_TYPE>(i);
				return m_ObjectFilenameArr[brushType];
			}
		}
		static CString nullStr = L"";
		return nullStr;
	}
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	void ResetBrushType();
	void SelectSelection();
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	virtual BOOL OnInitDialog();
	virtual ~CSelectObjectDlg();
	CSelectObjectDlg(CWnd* pParent = NULL);   
};
#endif