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
	CListCtrl m_listCtrlLinkedObjectLevel; //������Ʋ�ι�ϵ�༭��չʾ

private:
	int m_nCurSelItem;
	CImageList* m_pDragImage;	//��ҷͼ��
	bool		m_bDragging;	//�Ƿ�������ҷ
	int			m_nDragIndex;	//��ʼ��ҷ��λ��
	int			m_nDropIndex;	//��ҷ������

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

	CString m_strSysWorkPath; //�����������ԭʼ��Դ·���Ƿ���\\res\\map\\elements\\��

	std::map<CString, int> m_ListTypeMap; //0�ļ� 1�ļ��� 2��һ��Ŀ¼
	std::list<FileInfo> m_FileInfoList;

	Nuclear::Renderer* m_pRender;
	CRect m_rctDesPicShow;
//	Nuclear::PictureHandle m_hpicHandle; //��ǰѡ����ͼƬ������..
	std::vector<Nuclear::PictureHandle> m_vectorPicID;
	Nuclear::PImg m_imgObject;

	DWORD m_dwBKcolor;

	BOOL m_bSetLinkedObjectPosCenter;
	BOOL m_bStop;

	CSliderCtrl m_sliderLightAlpha;
	int m_nLightAlpha; //�ƹ��alpha�����������������롣

	BOOL m_bDragFlag;
	BOOL m_bPreviewLKO;

private: //����
	CListCtrl m_ListCtrl; //ԭʼ�ļ��б�
	int m_nSortColIndex;
	int m_nSortFlag;
	static int CALLBACK CompareFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort); //��̬���Ա�������ص�����..

	CComboBox m_comboxDriver;

	bool m_bInitRenameRule; //�ڳ�ʼ������������֮����Ϊtrue
	std::map<std::wstring, std::wstring> m_valuemap;

private:
	bool ResetLKOFileCentPos(std::wstring filePath);

	int Refresh (LPCTSTR pszPath);
	BOOL SetFileListInfo(WIN32_FIND_DATA* pfd, int fileType = 0); //1�ļ���
	BOOL ShowFileList();
	BOOL AddItem(FileInfo fileInfo);
	void ModifyDTList(int nDragIndex, int nDropIndex);

	void ConversionPLTFile(const std::vector<CString>& VectorPLTFileName); //ɾ����dynamicelementsĿ¼
	void ConversionLKOFile(const std::vector<CString>& VectorLKOFileName); 
	//������Դ�ļ����Լ��ļ������е������ַ���������ĸ  --------2009--4--29

	int FindFilesInSpecialPath(const CString& strSysPath, const CString& strFileName, std::vector<CString>& fileFullPathVector);

	void RenameAllFilesAndDir(const CString& strPath); //ָ��Ŀ¼�µ������ļ������ļ��и���

	struct RENAMESTR 
	{
		CString oldName;
		CString newName;
	};
	std::vector<RENAMESTR> m_vectorRenameFiles;
	std::vector<RENAMESTR> m_vectorRenameDirs;

	int m_nFreshFreq; //OnVScroll��ˢ�����Ͻ�����...

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

	void SetDragFlag(BOOL bDragFlag); //�´򿪵��ļ�Ĭ����Ϊ�����϶�
	afx_msg void OnMenucopyfilename();
	afx_msg void OnLvnColumnclickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonRefreshcentpos();
};
