
// getAttributeCodeDlg.h : 头文件
//

#pragma once
#include <list>
#include <set>
#include "afxwin.h"
#include "afxcmn.h"

typedef std::list<MEMORY_BASIC_INFORMATION> MemoryInfoList;

class ProgressSetter
{
public:
	virtual void setProgress(unsigned long curStep, unsigned long stepCount) = 0;
};

static const int CODE_RAW_LENGTH = 16;

class CodeRaw
{
public:
	BYTE data[CODE_RAW_LENGTH];

public:

	bool operator < (const CodeRaw& oth) const
	{
		return memcmp(data, oth.data, CODE_RAW_LENGTH) < 0;
	}

	bool operator > (const CodeRaw& oth) const
	{
		return memcmp(data, oth.data, CODE_RAW_LENGTH) > 0;
	}

	bool operator == (const CodeRaw& oth) const
	{
		return memcmp(data, oth.data, CODE_RAW_LENGTH) == 0;
	}
};

typedef std::set<CodeRaw> CodeRawSet;

class CodeRawFilter
{
public:
	CodeRawSet* mpCodeRawSet;

public:
	bool isCodeRawSetEmpty() const
	{
		return mpCodeRawSet ? mpCodeRawSet->empty() : true;
	}

	void onGetCodeRaw(const CodeRaw& oth)
	{
		if (mpCodeRawSet)
		{
			CodeRawSet::iterator it = mpCodeRawSet->find(oth);
			if (it != mpCodeRawSet->end())
			{
				mpCodeRawSet->erase(it);
			}
		}
	}
};

// CgetAttributeCodeDlg 对话框
class CgetAttributeCodeDlg : public CDialogEx
{
public:
	bool mbScanning;
	HANDLE mhThread;

public:
	CgetAttributeCodeDlg(CWnd* pParent = NULL);	// 标准构造函数

// 对话框数据
	enum { IDD = IDD_GETATTRIBUTECODE_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV 支持

public:
	CString getProcessFilename(DWORD processId);
	bool getCodeFromFile(const CString& filename, CodeRawSet& codeRawSet, ProgressSetter* pProgressSetter = NULL);
	bool filterCodeFromFile(const CString& filename, CodeRawFilter& filter, ProgressSetter* pProgressSetter = NULL);

	bool getProcessMemoryInfoList(HANDLE hProcess, MemoryInfoList& memInfoList, ProgressSetter* pProgressSetter = NULL);
	bool getCodeFromMemoryInfo(HANDLE hProcess, MEMORY_BASIC_INFORMATION& mbi, CodeRawSet& codeRawSet, ProgressSetter* pProgressSetter = NULL);
	bool getCodeFromMemoryInfo(HANDLE hProcess, MEMORY_BASIC_INFORMATION& mbi, CodeRawFilter& filter, ProgressSetter* pProgressSetter = NULL);

	CString codeRawToCString(const BYTE codeRaw[]);
	int getZeroCount(const CodeRaw& codeRaw);

	void setProgress(float fPercent);

	void startScan();
	void stopScan();
	void onFinishScan();

// 实现
protected:
	HICON m_hIcon;

	// 生成的消息映射函数
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
	virtual void OnOK();
public:
	afx_msg void OnDeltaposSpinCodeMaxCount(NMHDR *pNMHDR, LRESULT *pResult);
	DWORD mTargetProcessId;
	int mMaxZeroCount;
	CString mcsOutput;
	CButton mBtnScan;
	CString mBtnScanLabel;
	CProgressCtrl mProgressCtrl;
	CEdit mEditProcessId;

	afx_msg void OnBnClickedButtonScan();
	CEdit mEditOutput;
	CEdit mEditStatus;
	CEdit mEditMaxZeroCount;
	CSpinButtonCtrl mSpinMaxZeroCount;
	virtual void OnFinalRelease();
};
