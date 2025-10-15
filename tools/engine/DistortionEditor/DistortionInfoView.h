#pragma once
#include "afxext.h"
#include "Resource.h"

#include "afxwin.h"
#include "afxcmn.h"

#include <map>
#include <list>

#include "..\engine\renderer\renderer.h"
#include "..\engine\map\pimg.h"


#include "BezierControl.h"

class CDistortionInfoView : public CFormView
{
	DECLARE_DYNCREATE(CDistortionInfoView)

protected:
	CDistortionInfoView();           // protected constructor used by dynamic creation
	virtual ~CDistortionInfoView();

public:
	enum { IDD = IDD_DIALOG_DISTORTIONINFOVIEW };

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
	void Updatectrl();

public:
	CListCtrl m_listCtrlDistortionLevel; //������Ʋ�ι�ϵ�༭��չʾ
	bezier::CBezierControl m_bezier;

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
	CString m_strPath; //��ǰ����ļ���
	CString m_strFileName_old;

	CString m_strSysWorkPath; //�����������ԭʼ��Դ·���Ƿ���\\res\\map\\elements\\��

	std::map<CString, int> m_ListTypeMap; //0�ļ� 1�ļ��� 2��һ��Ŀ¼
	std::list<FileInfo> m_FileInfoList;

	Nuclear::Renderer* m_pRender;
	CRect m_rctDesPicShow;
	Nuclear::PImg m_imgObject;

	std::vector<Nuclear::PictureHandle> m_vectorPicID; //...����굥���ļ��б��ʱ���ʼ��..

	DWORD m_dwBKcolor;

	BOOL m_bLightFlag;

private:
	CListCtrl m_ListCtrl; //ԭʼ�ļ��б�
	CComboBox m_comboxDriver;
	CComboBox m_comboDistortionType;
	CSliderCtrl m_sliderGridx;
	CSliderCtrl m_sliderSpeed;
	CSliderCtrl m_sliderCycNum;
	CSliderCtrl m_sliderSwing;
	
	int m_nDistortionType;
	int m_nGridx; //�������Ŀǰû����..
	float m_fSpeed; //�ƶ�2pi��Ҫ������ --- 1.0f--100.0f
	float m_fCycNum;
	float m_fSwing;
	bezier::SamplePointContanters m_SPCSamplePoints;
	bezier::TangentPointContanters m_TPCTangentPoints;

private:
	int Refresh (LPCTSTR pszPath);
	BOOL SetFileListInfo(WIN32_FIND_DATA* pfd, int fileType = 0); //1�ļ���
	BOOL ShowFileList();
	BOOL AddItem(FileInfo fileInfo);
	int FindFilesInSpecialPath(const CString& strSysPath, const CString& strFileName, std::vector<CString>& fileFullPathVector);

private:
	afx_msg void OnCbnSelchangeComboDriverlist();
	afx_msg void OnLvnItemchangedListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMClickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclkListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonSetbkcolor();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);

	afx_msg void OnNMRclickListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnCbnSelchangeComboDistortiontype();
	afx_msg void OnKillFocus(CWnd* pNewWnd);
public:
	afx_msg void OnBnClickedButtonCurveadjust();
	afx_msg void OnBnClickedCheckAddlight();
};
