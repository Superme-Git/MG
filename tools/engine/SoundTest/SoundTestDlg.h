// SoundTestDlg.h : ͷ�ļ�
//

#pragma once
#include "afxcmn.h"
#include <mmsystem.h>
#include <dsound.h>
#include "..\engine\renderer\renderer.h"

class IDisposer;
// CSoundTestDlg �Ի���
class CSoundTestDlg : public CDialog
{
// ����
public:
	CSoundTestDlg(CWnd* pParent = NULL);	// ��׼���캯��
	virtual ~CSoundTestDlg();

// �Ի�������
	enum { IDD = IDD_SOUNDTEST_DIALOG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV ֧��
private:
	Nuclear::Renderer *m_pRenderer;
	Nuclear::FileIOManager *m_pFileIOMan;

	void Scan(const CString &path, IDisposer *pDisposer);
// ʵ��
protected:
	HICON m_hIcon;

	// ���ɵ���Ϣӳ�亯��
	DECLARE_MESSAGE_MAP()
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnBnClickedButton1();
	
public:
	afx_msg void OnBnClickedButton2();
};
