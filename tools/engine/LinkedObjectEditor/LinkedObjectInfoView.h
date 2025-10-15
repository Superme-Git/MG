#pragma once
#include "afxext.h"
#include "Resource.h"

#include "afxwin.h"
#include "afxcmn.h"

#include <map>
#include <list>

#include "..\engine\renderer\renderer.h"
#include "..\engine\map\pimg.h"

class CLinkedObjectInfoView : public CFormView
{
	DECLARE_DYNCREATE(CLinkedObjectInfoView)

protected:
	CLinkedObjectInfoView();           // protected constructor used by dynamic creation
	virtual ~CLinkedObjectInfoView();

public:
	enum { IDD = IDD_DIALOG_PLANTINFOVIEW };

#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

protected:
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view

private:
	virtual void OnInitialUpdate();

public:
	CListCtrl m_listCtrlLinkedObjectLevel; //物件绘制层次关系编辑并展示

private:
	int m_nCurSelItem;
	CImageList* m_pDragImage;	//拖曳图像
	bool		m_bDragging;	//是否正在拖曳
	int			m_nDragIndex;	//开始拖曳的位置
	int			m_nDropIndex;	//拖曳到哪里

private:
	struct FileInfo 
	{
		CString strFileName;
		CString strFileSize;
		CString strFileLastWriteTime;
	};

	CFont m_font;
	CString m_strPath;
	CString m_strFileName_old;

	CString m_strSysWorkPath; //用来检查美术原始资源路径是否在\\res\\map\\elements\\下

	std::map<CString, int> m_ListTypeMap; //0文件 1文件夹 2上一级目录
	std::list<FileInfo> m_FileInfoList;

	Nuclear::Renderer* m_pRender;
	CRect m_rctDesPicShow;
//	Nuclear::PictureHandle m_hpicHandle; //当前选定的图片纹理句柄..
	std::vector<Nuclear::PictureHandle> m_vectorPicID;
	Nuclear::PImg m_imgObject;

	DWORD m_dwBKcolor;

	BOOL m_bSetLinkedObjectPosCenter;
	BOOL m_bStop;

	CSliderCtrl m_sliderLightAlpha;
	int m_nLightAlpha; //灯光层alpha，用来方便美术对齐。

	BOOL m_bDragFlag;
	BOOL m_bPreviewLKO;

private: //排序
	CListCtrl m_ListCtrl; //原始文件列表
	int m_nSortColIndex;
	int m_nSortFlag;
	static int CALLBACK CompareFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort); //静态类成员函数做回调函数..

	CComboBox m_comboxDriver;

	bool m_bInitRenameRule; //在初始化了命名规则之后置为true
	std::map<std::wstring, std::wstring> m_valuemap;

private:
	bool ResetLKOFileCentPos(std::wstring filePath);

	int Refresh (LPCTSTR pszPath);
	BOOL SetFileListInfo(WIN32_FIND_DATA* pfd, int fileType = 0); //1文件夹
	BOOL ShowFileList();
	BOOL AddItem(FileInfo fileInfo);
	void ModifyDTList(int nDragIndex, int nDropIndex);

	void ConversionPLTFile(const std::vector<CString>& VectorPLTFileName); //删除了dynamicelements目录
	void ConversionLKOFile(const std::vector<CString>& VectorLKOFileName); 
	//美术资源文件名以及文件夹名中的中文字符换成了字母  --------2009--4--29

	int FindFilesInSpecialPath(const CString& strSysPath, const CString& strFileName, std::vector<CString>& fileFullPathVector);

	void RenameAllFilesAndDir(const CString& strPath); //指定目录下的所有文件及其文件夹改名

	struct RENAMESTR 
	{
		CString oldName;
		CString newName;
	};
	std::vector<RENAMESTR> m_vectorRenameFiles;
	std::vector<RENAMESTR> m_vectorRenameDirs;

	int m_nFreshFreq; //OnVScroll里刷新左上角区域...

private:
	afx_msg void OnCbnSelchangeComboDriverlist();
	afx_msg void OnLvnItemchangedListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMClickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclkListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonSetbkcolor();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);

	afx_msg void OnNMRclickListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult);
public:
	afx_msg void OnMenuPrelevel();
	afx_msg void OnMenuNextlevel();
	afx_msg void OnMenuFirstlevel();
	afx_msg void OnMenuLastlevel();
	afx_msg void OnHdnItemchangedListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMClickListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnMenuSetmainnode();
	afx_msg void OnMenuDelcurselnode();
	afx_msg void OnBnClickedCheckSetplantposcenter();
	afx_msg void OnLvnBegindragListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnBnClickedCheckStop();
	afx_msg void OnBnClickedButtonConversion();
	afx_msg void OnBnClickedButtonImgfilerename();
	afx_msg void OnBnClickedButtonInitrenamerule();
	afx_msg void OnBnClickedButtonRenamefilesanddir();
	afx_msg void OnBnClickedButtonRenamemapfile();
	afx_msg void OnBnClickedButtonRefreshmaxbox();
	afx_msg void OnBnClickedCheckDragflag();
	afx_msg void OnBnClickedCheckpreviewlko();

	void SetDragFlag(BOOL bDragFlag); //新打开的文件默认设为不可拖动
	afx_msg void OnMenucopyfilename();
	afx_msg void OnLvnColumnclickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonRefreshcentpos();
};
