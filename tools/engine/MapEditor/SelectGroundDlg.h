#ifndef SELECT_GROUND_DLG_H
#define SELECT_GROUND_DLG_H

#pragma once
#include "afxcmn.h"
#include<atlimage.h>
#include "afxwin.h"



class CMapEditorView;
enum EDIT_MODE_TYPE;

class CSelectGroundDlg : public CDialog
{
	DECLARE_DYNAMIC(CSelectGroundDlg)

private:
	static const unsigned int MAP_KEY_BIG = 0x20000;
	static const unsigned int MAP_KEY_MID = 0x10000;
	static const unsigned int MAP_KEY_SMALL = 0x00000;
	static const unsigned int MAP_KEY_MASK = 0x30000;

public:
	enum { IDD = IDD_SELECT_GROUND };
	virtual ~CSelectGroundDlg();

	CSelectGroundDlg(CWnd* pParent = NULL);   

protected:
	DECLARE_MESSAGE_MAP()
	virtual void OnOK() {}

	virtual void DoDataExchange(CDataExchange* pDX);    
public:
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	virtual BOOL OnInitDialog();
	EDIT_MODE_TYPE GetCurrentEditMode();

private:
	afx_msg void OnTvnSelchangedTree6(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTvnSelchangedTree5(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTvnSelchangedTree4(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTvnSelchangedTree3(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTvnSelchangedTree2(NMHDR *pNMHDR, LRESULT *pResult);	
	afx_msg void OnTvnSelchangedTree1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTcnSelchangeTab2(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTcnSelchangeTabGroundSize(NMHDR *pNMHDR, LRESULT *pResult);

	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnBnClickedSelectBackgroundColor();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnCbnSelchangeBrushSize();
	afx_msg void OnCbnEditchangeBrushSize();
	afx_msg void OnShowWindow(BOOL bShow, UINT nStatus);
	afx_msg void OnPaint();
	bool SetBrushSize(CString &val);

	void RenewCombo();
	bool SetSelectColor();
	bool SetSelectBiggerTile(Nuclear::TILE_SIZE_TYPE tileSize);
	bool SetSelectBiggerGround(Nuclear::TILE_SIZE_TYPE tileSize);
	bool SetSelectSmallTile();
	bool SetSelectSmallGround();
	void RenewEditMode();
	bool LoadPictureByAddress(CString file);


	bool RenewTrees(Nuclear::TILE_SIZE_TYPE tileSize);
	bool RenewSmallTrees(void);
	bool RenewTrees(void);
	void RenewSize();
	int m_nMinHeight;

	int m_nMinWidth;
	EDIT_MODE_TYPE m_nEditMode;
	unsigned int m_nBrushColor;
	TileType m_nBrushType;
	std::map<unsigned int, CString> m_PictureMap;

	CRect m_BitmapRect;
	CImage m_Image;
	CMapEditorView* m_pMapEditorView;

	CComboBox m_comboBrushSize;
	CTreeCtrl m_treeTileList[3];
	CTreeCtrl m_treeGroundList[3];
	CTabCtrl m_TabGroundSize;
	CTabCtrl m_tab2;
	CTabCtrl m_tab;
	bool m_bSizing;
};

#endif