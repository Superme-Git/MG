// SoundTestDlg.h : 头文件
//

#pragma once
#include "afxcmn.h"
#include <mmsystem.h>
#include <dsound.h>
#include "..\engine\renderer\renderer.h"

class IDisposer;
// CSoundTestDlg 对话框
class CSoundTestDlg : public CDialog
{
// 构造
public:
	CSoundTestDlg(CWnd* pParent = NULL);	// 标准构造函数
	virtual ~CSoundTestDlg();

// 对话框数据
	enum { IDD = IDD_SOUNDTEST_DIALOG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV 支持
private:
	Nuclear::Renderer *m_pRenderer;
	Nuclear::FileIOManager *m_pFileIOMan;

	void Scan(const CString &path, IDisposer *pDisposer);
// 实现
protected:
	HICON m_hIcon;

	// 生成的消息映射函数
	DECLARE_MESSAGE_MAP()
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnBnClickedButton1();
	
public:
	afx_msg void OnBnClickedButton2();
};
