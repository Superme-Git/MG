#pragma once
#include "afxcmn.h"
#include <vector>
#include <string>
#include <map>

// CTextureDlg 对话框

const int WIDTH_SHOWIMAGE = 64;
const int HIGH_SHOWIMAGE = 64;

class CTextureDlg : public CDialog
{
	DECLARE_DYNAMIC(CTextureDlg)

public:
	CTextureDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CTextureDlg();

// 对话框数据
	enum { IDD = IDD_DIALOG_TEXTUREWND };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	virtual BOOL OnInitDialog();
	BOOL InitImageList(CString strTexturePath); //对应树控件...

	BOOL InitFilePathList(CString filePath);

	void ImageSizeAdjust(DWORD* pByte, int WIDTH_SHOWIMAGE, int HIGH_SHOWIMAGE,
							DWORD* pFileBuf, int nWidth, int nHeight);

	void FindTextureFileName(CString strRootPath, CString strDir, CStringArray& ArrayStrFile);
	void FillTextureTree(CString strTexturePath, CString strDir, HTREEITEM hParentItem);
	void FillTextureTree(); //根据m_mapStrFileArray来初始化..
	
	void InitMapStrFileArray();
	HTREEITEM FindItem(HTREEITEM rootItem, CString strItemText); //在树中找指定节点

public:
	void GetTextureFiles(std::vector<std::wstring>& ArrayTextureFileName);
	BOOL SaveBmp(HBITMAP hBitmap, CString FileName);

private:
	CTreeCtrl m_treePSLTexture; //树结构管理纹理图片...
	std::map<CString, std::vector<CString>> m_mapStrFileArray; //分目录存下文件名...
	std::map<CString, CImageList*> m_mapImageList; //分目录记录image信息...
	CString m_strCurMapKey; //根据这个值来显示左边列表框的图片..
	
	//HTREEITEM这种值每次刷新就变了...
	HTREEITEM m_hTreeItemMenu;//右键菜单项...复制节点路径时候有用
	HTREEITEM m_hTreeItemCurShow; //右边窗口目前显示的文件夹项
//代替以上变量...每次刷新都要根据m_strCurMapKey这个值遍历树来找到准确的m_hTreeItemCurShow...
	//以上 ---2009--4---17

	int m_nRButtonIndex;//复制该项的路径..右边窗口里

	CListCtrl m_listCtrlTexture;
	CImageList	m_ImageList; //左边窗口显示的数据..
	CStringArray m_ArrayStrFile; //系统提供的所有纹理文件的路径
	CString m_strTexturePath;
//	CStringArray m_ArrayTextureFileName; //用户选定的所有纹理文件的路径
	std::vector<std::wstring> m_ArrayTextureFileName;

private:
	afx_msg void OnLvnItemchangedListTexture(NMHDR *pNMHDR, LRESULT *pResult);

	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnPaint();
	afx_msg void OnCustomdrawMyList ( NMHDR* pNMHDR, LRESULT* pResult );
public:
	afx_msg void OnBnClickedButtonRefresh();
	afx_msg void OnNMClickTreePsltexturelist(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnMenuCopypath();
	afx_msg void OnMenuRefreshtexture();
	afx_msg void OnNMRclickTreePsltexturelist(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclickListTexture(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnMenuCopyfullpath();
	afx_msg void OnNMDblclkTreePsltexturelist(NMHDR *pNMHDR, LRESULT *pResult);
};
