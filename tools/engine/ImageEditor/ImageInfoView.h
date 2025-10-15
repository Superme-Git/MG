#pragma once
#include "afxext.h"
#include "Resource.h"

#include <map>
#include <list>
#include "afxcmn.h"

#include "..\engine/renderer\renderer.h"
#include "..\engine/common\xptypes.h"
#include "afxwin.h"

using namespace Nuclear;

class CImageInfoView : public CFormView
{
	DECLARE_DYNCREATE(CImageInfoView)

protected:
	CImageInfoView();           // protected constructor used by dynamic creation
	virtual ~CImageInfoView();

public:
	enum { IDD = IDD_VIEW_IMAGEINFO };

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

public:
	std::map<std::wstring, Nuclear::XPTEXTURE_FORMAT> m_texfmtmap; //statusView���õ�.
	CComboBox m_comboImgFileFormat;

private:
	CComboBox m_comboDriverList;

	Nuclear::XPTEXTURE_FORMAT m_texfmt;

public: //����
	CListCtrl m_ListCtrl;
	int m_nSortColIndex;
	int m_nSortFlag;
	static int CALLBACK CompareFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort); //��̬���Ա�������ص�����..

private:
	Renderer *m_pRenderer;

	struct ImageFile
	{
		PictureHandle pic;	 // D3DRenderer�����ͼ��handle
		int pwidth;	 // ԭʼͼƬ���
		int pheight; // ԭʼͼƬ�߶�
		void *data;	 // ԭʼͼ���ļ�����
		int size;	 // ԭʼͼ���ļ���С
		TCHAR postfix[4]; // ���趼��3����ĸ�ĺ�׺
		ImageFile() : pic(INVALID_PICTURE_HANDLE), pwidth(0), pheight(0), data(0), size(0) { }
	};

	struct OLDIMGFILE : public ImageFile
	{
		POINT CenterPoint; //����
		POINT pos[4]; //�� �� �� �� 0 1 2 3

		OLDIMGFILE() :ImageFile()
		{
			CenterPoint.x = 0;
			CenterPoint.y = 0;
			for(int i=0; i<4; i++)
			{
				pos[i].x = 0;
				pos[i].y = 0;
			}
		}
	};

	struct FileInfo 
	{
		CString strFileName;
		CString strFileSize;
		CString strFileLastWriteTime;
	};

	CFont m_font;
	CString m_strPath;
	CString m_strFileName_old;
	CPoint m_centerPoint; //������ʱ�����Ļ�׼��
	CPoint m_BorderPoint[4]; //������ʱ�ı߿��׼��
	BOOL m_bSetMaskpt;
	BOOL m_bAdjustMaskpt;

	std::map<CString, int> m_ListTypeMap; //0�ļ� 1�ļ��� 2��һ��Ŀ¼
	std::list<FileInfo> m_FileInfoList;

	CString m_curFileNameNoPath;//��ѯ��ǰѡ����ʱ�� ---picTOimg  picTOani
	CString m_strFindFileName;

	CSliderCtrl m_sliderZoomRate;
	float m_fZoomRate;
	CString m_strZoomRate;
	CPoint m_pointStrZoomRate;

public:
	float GetCurrentZoomRate();
	CString GetImgFileName() { return m_strFileName_old; }

private:
	virtual void OnInitialUpdate();
	afx_msg void OnCbnSelchangeCmbDriver();

	afx_msg void OnCbnSelchangeComboImgfileformat();

public:
	void SetControlRange(int minFrame, int maxFrame);
	void UpdateControlState(bool isEnable);

private:
	int Refresh (LPCTSTR pszPath);
	BOOL SetFileListInfo(WIN32_FIND_DATA* pfd, int fileType = 0); //1�ļ���
	BOOL ShowFileList();
	BOOL AddItem(FileInfo fileInfo);
	BOOL LoadPicFile(const CString strFilePath, BYTE** ppFileDataBuffer, DWORD& dwFileSize);

	void ConversionImgFile(std::vector<CString> &VectorImgFileName);
	BOOL ReadOldImgFile(const CString& strFilePath, OLDIMGFILE** ppOldImgFile);

	//��ʱ����ͳ���ж���set�ļ���a8r8g8b8��ʽ��
	void FindSetFileInfo(const CString& pslPath);


private:
	afx_msg void OnLvnItemchangedListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMClickList(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclickList(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclkListFileInfo(NMHDR *pNMHDR, LRESULT *pResult);

	afx_msg void OnMenuPICtoIMG();
	afx_msg void OnMenuSetCenterPointStandard();
	afx_msg void OnMenuSetCenterPoints();

	afx_msg void OnMenuSetborderstandard();
	afx_msg void OnMenuSetborders();
	afx_msg void OnMenuDeletefiles();
	afx_msg void OnMenuFilesaveas();

	afx_msg void OnBnClickedButtonFindfile();
	afx_msg void OnEnChangeEditStrfilename();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);

	afx_msg void OnBnClickedButtonImgfileverconversion();
	afx_msg void OnBnClickedCheckSetmaskrct();
	afx_msg void OnBnClickedCheckAdjustmaskrct();
public:
	afx_msg void OnLvnColumnclickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonimginfo();
};
