#pragma once
#include "afxwin.h"
#include "afxcmn.h"



class CMapEditorView;
class CMaskEditorDlg : public CDialog
{
	DECLARE_DYNAMIC(CMaskEditorDlg)

public:
	
	enum { IDD = IDD_DLG_MASK_EDITOR };

private:
	bool m_bNewPicture;
	bool m_bInKeyFrame;
	CEdit m_editPictureUrl;

	CEdit m_editLastAlpha;
	CSliderCtrl m_sliderTimeBar;
	CSliderCtrl m_sliderAlpha;
	CSliderCtrl m_sliderLastAlpha;
	CComboBox m_comboBlend;
	CButton m_btnAddDelFrame;
	CButton m_btnStop;
	CButton m_btnBrowse;
	CButton m_btnNewPicture;
	CListBox m_listKeyFrames;
	CMapEditorView *m_pMapEditorView;

public:
	virtual ~CMaskEditorDlg();
	CMaskEditorDlg(CWnd* pParent = NULL);   
	std::wstring m_CurPictureName;
	Nuclear::XPMaskParam m_MaskParam;

private:
	void SetPictureItemState(bool isNewPic);
	void SetItemState(bool isKeyFrame, bool isNewPic);
	void GetCurrentTimeBarParam();
	void ResetKeyFrames();
	void UpdateAllParamControls();
	void ModifyParams();

public:
	virtual BOOL Create(CMapEditorView* pMapEditorView);
	void SetPlay(bool isPlay);
	void UpdateParams();
	void Reset();

protected:
	DECLARE_MESSAGE_MAP()
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	virtual BOOL OnInitDialog();
	virtual void OnCancel();
	virtual void OnOK() {}
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	afx_msg void OnEnKillfocusEdit2();
	afx_msg void OnCbnSelchangeCombo1();
	afx_msg void OnBnClickedCheck1();
	afx_msg void OnBnClickedButton1();
	afx_msg void OnShowWindow(BOOL bShow, UINT nStatus);
	afx_msg void OnBnClickedStop();
	afx_msg void OnBnClickedPlayQuickly();
	afx_msg void OnLbnSelchangeList1();
	afx_msg void OnBnClickedAddRemoveFrame();
	afx_msg void OnEnKillfocusEditForSlider(UINT nID);
	afx_msg void OnEnKillfocusEdit1();
	afx_msg void OnSliderReleasedCapture(UINT nID, NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnBnClickedPlay();
};
