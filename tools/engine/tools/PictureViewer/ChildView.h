// ChildView.h : CChildView ��Ľӿ�
//


#pragma once


// CChildView ����

class CChildView : public CWnd
{
// ����
public:
	CChildView();

// ����
public:

// �ڲ�����
protected:
	CDC			m_memDC;
	CDC			m_memDC2;
	CRect		m_rcPicture;
	CString		m_strFilePath;
	HBRUSH		m_hbrBackground;
	BITMAPINFO	m_BitmapInfo;
	BOOL		m_bAlphaBlendMode;
	CStringList m_fileNameList;
	POSITION	m_posInFileList;
	DWORD		m_dwSizeScale;
	int			m_maxClipWidth;
	int			m_maxClipHeight;
	int			m_stretchBltMode;
	HBITMAP		m_hBITMAP; // Current Bitmap

	void InitFileNameList( CString strPathName );

	BOOL InnerShowPicture( LPCTSTR lpszFilePath );

// ����
public:
	BOOL ShowPicture( LPCTSTR lpszFilePath );

// ��д
protected:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

	void DrawPicture(CDC* pDC);
	void Refresh();

// ʵ��
public:
	virtual ~CChildView();

	// ���ɵ���Ϣӳ�亯��
protected:
	afx_msg void OnPaint();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg void OnFileOpen();
	afx_msg void OnPicAlphaBlend();
	afx_msg BOOL OnPicStretchBltMode(UINT nID);
	afx_msg void OnUpdatePicAlphaBlend(CCmdUI *pCmdUI);
	afx_msg void OnUpdatePicStretchBltMode(CCmdUI *pCmdUI);
	afx_msg BOOL OnOpenRecentFile(UINT nID);
	afx_msg void OnPrev();
	afx_msg void OnNext();
	afx_msg void OnZoomIn();
	afx_msg void OnZoomOut();
	afx_msg void OnFeatSize();
	afx_msg void OnActualSize();
	afx_msg void OnUpdatePictureOpened(CCmdUI *pCmdUI);
	afx_msg void OnUpdateFeatSize(CCmdUI *pCmdUI);
	afx_msg void OnUpdateActualSize(CCmdUI *pCmdUI);
	afx_msg void OnUpdateFileSaveAs(CCmdUI *pCmdUI);
	afx_msg void OnFileSaveAs();
};

