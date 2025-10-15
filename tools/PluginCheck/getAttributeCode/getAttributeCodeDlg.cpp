
// getAttributeCodeDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "getAttributeCode.h"
#include "getAttributeCodeDlg.h"
#include "afxdialogex.h"
#include <TlHelp32.h>
#include <vector>
#include <psapi.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define MulitFeatureCode 5

// ����Ӧ�ó��򡰹��ڡ��˵���� CAboutDlg �Ի���

class CAboutDlg : public CDialogEx
{
public:
	CAboutDlg();

// �Ի�������
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

// ʵ��
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialogEx(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialogEx)
END_MESSAGE_MAP()


// CgetAttributeCodeDlg �Ի���

CgetAttributeCodeDlg::CgetAttributeCodeDlg(CWnd* pParent /*=NULL*/)
	: CDialogEx(CgetAttributeCodeDlg::IDD, pParent)
	, mbScanning(false)
	, mhThread(NULL)

	, mTargetProcessId(0)
	, mMaxZeroCount(0)
	, mcsOutput(_T(""))
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CgetAttributeCodeDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_PROCESS_ID, mTargetProcessId);
	DDX_Text(pDX, IDC_EDIT_CODE_MAX_COUNT, mMaxZeroCount);
	DDV_MinMaxInt(pDX, mMaxZeroCount, 0, 32);
	DDX_Text(pDX, IDC_EDIT_OUTPUT, mcsOutput);
	DDX_Control(pDX, IDC_BUTTON_SCAN, mBtnScan);
	DDX_Control(pDX, IDC_PROGRESS_SCAN, mProgressCtrl);
	DDX_Control(pDX, IDC_EDIT_PROCESS_ID, mEditProcessId);
	DDX_Control(pDX, IDC_EDIT_OUTPUT, mEditOutput);
	DDX_Control(pDX, IDC_EDIT_STATUS, mEditStatus);
	DDX_Control(pDX, IDC_EDIT_CODE_MAX_COUNT, mEditMaxZeroCount);
	DDX_Control(pDX, IDC_SPIN_CODE_MAX_COUNT, mSpinMaxZeroCount);
}

CString CgetAttributeCodeDlg::getProcessFilename(DWORD processId)
{
	CString cs;

	HANDLE hSnap = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, processId);
	if (hSnap && hSnap != INVALID_HANDLE_VALUE)
	{
		MODULEENTRY32 me;
		me.dwSize = sizeof(me);

		if (Module32First(hSnap, &me))
		{
			cs = me.szExePath;
		}

		CloseHandle(hSnap);
	}

	return cs;
}

bool CgetAttributeCodeDlg::getCodeFromFile(const CString& filename, CodeRawSet& codeRawSet, ProgressSetter* pProgressSetter/* = NULL*/)
{
	DWORD fileAttributes = GetFileAttributes(filename);
	HANDLE hFile = CreateFile(filename, GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, fileAttributes, NULL);
	if (hFile && hFile != INVALID_HANDLE_VALUE)
	{
		int fileSize = GetFileSize(hFile, NULL);

		BYTE* fileData = (BYTE*)malloc(fileSize);
		if (fileData)
		{
			DWORD readSize = 0;
			if (ReadFile(hFile, fileData, fileSize, &readSize, NULL))
			{
				for (int i = 0; i < fileSize && mbScanning; i += CODE_RAW_LENGTH)
				{
					if (fileSize - i >= CODE_RAW_LENGTH)
					{
						CodeRaw codeRaw;
						memcpy(codeRaw.data, fileData + i, CODE_RAW_LENGTH);
						if (getZeroCount(codeRaw) <= mMaxZeroCount)
						{
							codeRawSet.insert(codeRaw);
						}
					}
					else
					{
						break;
					}

					if (pProgressSetter)
					{
						pProgressSetter->setProgress(i, fileSize);
					}
				}
			}

			free(fileData);
		}

		CloseHandle(hFile);

		return true;
	}

	return false;
}

bool CgetAttributeCodeDlg::filterCodeFromFile(const CString& filename, CodeRawFilter& filter, ProgressSetter* pProgressSetter/* = NULL*/)
{
	DWORD fileAttributes = GetFileAttributes(filename);
	HANDLE hFile = CreateFile(filename, GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, fileAttributes, NULL);
	if (hFile && hFile != INVALID_HANDLE_VALUE)
	{
		int fileSize = GetFileSize(hFile, NULL);
		BYTE* fileData = (BYTE*)malloc(fileSize);
		if (fileData)
		{
			DWORD readSize = 0;
			if (ReadFile(hFile, fileData, fileSize, &readSize, NULL))
			{
				for (int i = 0; i < fileSize && !filter.isCodeRawSetEmpty() && mbScanning; i += CODE_RAW_LENGTH)
				{
					if (fileSize - i >= CODE_RAW_LENGTH)
					{
						CodeRaw codeRaw;
						memcpy(codeRaw.data, fileData + i, CODE_RAW_LENGTH);
						if (getZeroCount(codeRaw) <= mMaxZeroCount)
						{
							filter.onGetCodeRaw(codeRaw);
						}
					}
					else
					{
						break;
					}

					if (pProgressSetter)
					{
						pProgressSetter->setProgress(i, fileSize);
					}
				}
			}

			free(fileData);
		}

		CloseHandle(hFile);

		return true;
	}

	return false;
}

bool CgetAttributeCodeDlg::getProcessMemoryInfoList(HANDLE hProcess, MemoryInfoList& memInfoList, ProgressSetter* pProgressSetter/* = NULL*/)
{
	if (hProcess)
	{
		DWORD startAddress = 0x00010000;
		DWORD endAddress = 0x7FFFFFFF;

		DWORD readAddress = startAddress;
		int curStep = 0;
		while (readAddress < endAddress)
		{
			MEMORY_BASIC_INFORMATION mbi;
			VirtualQueryEx(hProcess, (LPCVOID)readAddress, &mbi, sizeof(mbi));

			if (mbi.State == MEM_COMMIT
				&& (mbi.Protect == PAGE_READONLY/* || mbi.Protect == PAGE_EXECUTE_READ*/)
				/*&& mbi.Type == MEM_IMAGE*/
				)
			{
				memInfoList.push_back(mbi);
			}

			if (endAddress - readAddress > mbi.RegionSize)
			{
				readAddress += mbi.RegionSize;
			}
			else
			{
				break;
			}

			if (pProgressSetter)
			{
				pProgressSetter->setProgress(readAddress - startAddress, endAddress - startAddress);
			}
		}

		if (pProgressSetter)
		{
			pProgressSetter->setProgress(endAddress - startAddress, endAddress - startAddress);
		}

		return true;
	}

	return false;
}

BEGIN_MESSAGE_MAP(CgetAttributeCodeDlg, CDialogEx)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_NOTIFY(UDN_DELTAPOS, IDC_SPIN_CODE_MAX_COUNT, &CgetAttributeCodeDlg::OnDeltaposSpinCodeMaxCount)
	ON_BN_CLICKED(IDC_BUTTON_SCAN, &CgetAttributeCodeDlg::OnBnClickedButtonScan)
END_MESSAGE_MAP()


// CgetAttributeCodeDlg ��Ϣ�������

BOOL CgetAttributeCodeDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	// ��������...���˵�����ӵ�ϵͳ�˵��С�

	// IDM_ABOUTBOX ������ϵͳ���Χ�ڡ�
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		BOOL bNameValid;
		CString strAboutMenu;
		bNameValid = strAboutMenu.LoadString(IDS_ABOUTBOX);
		ASSERT(bNameValid);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// ���ô˶Ի����ͼ�ꡣ  ��Ӧ�ó��������ڲ��ǶԻ���ʱ����ܽ��Զ�
	//  ִ�д˲���
	SetIcon(m_hIcon, TRUE);			// ���ô�ͼ��
	SetIcon(m_hIcon, FALSE);		// ����Сͼ��

	// TODO:  �ڴ���Ӷ���ĳ�ʼ������

	mProgressCtrl.SetRange32(0, 100000);
	mProgressCtrl.SetPos(0);

	return TRUE;  // ���ǽ��������õ��ؼ������򷵻� TRUE
}

void CgetAttributeCodeDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialogEx::OnSysCommand(nID, lParam);
	}
}

// �����Ի��������С����ť������Ҫ����Ĵ���
//  �����Ƹ�ͼ�ꡣ  ����ʹ���ĵ�/��ͼģ�͵� MFC Ӧ�ó���
//  �⽫�ɿ���Զ���ɡ�

void CgetAttributeCodeDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // ���ڻ��Ƶ��豸������

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// ʹͼ���ڹ����������о���
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// ����ͼ��
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialogEx::OnPaint();
	}
}

//���û��϶���С������ʱϵͳ���ô˺���ȡ�ù��
//��ʾ��
HCURSOR CgetAttributeCodeDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}



void CgetAttributeCodeDlg::OnOK()
{
}


void CgetAttributeCodeDlg::OnDeltaposSpinCodeMaxCount(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMUPDOWN pNMUpDown = reinterpret_cast<LPNMUPDOWN>(pNMHDR);

	mMaxZeroCount -= pNMUpDown->iDelta;
	UpdateData(FALSE);
	UpdateData(TRUE);
	
	*pResult = 0;
}

CString CgetAttributeCodeDlg::codeRawToCString(const BYTE codeRaw[])
{
	TCHAR sz[CODE_RAW_LENGTH * 2 + 1] = { 0 };

	for (int i = 0; i < CODE_RAW_LENGTH; ++i)
	{
		BYTE b = codeRaw[i];
		TCHAR bsz[3] = { 0 };
		_stprintf(bsz, _T("%02X"), b);
		_tcscat(sz, bsz);
	}

	return CString(sz);
}

int CgetAttributeCodeDlg::getZeroCount(const CodeRaw& codeRaw)
{
	int count = 0;

	for (int i = 0; i < CODE_RAW_LENGTH; ++i)
	{
		BYTE b = codeRaw.data[i];
		if (b == 0)
		{
			++count;
		}
	}

	return count;
}

class DlgProgressSetter : public ProgressSetter
{
protected:
	DlgProgressSetter* mpParent;
	CgetAttributeCodeDlg* mpDlg;

public:
	float mStart, mEnd;

public:
	DlgProgressSetter(DlgProgressSetter* pParent, CgetAttributeCodeDlg* pDlg, float fStart, float fEnd)
		: mpParent(pParent)
		, mpDlg(pDlg)
		, mStart(fStart)
		, mEnd(fEnd)
	{}

	float calPercent(float fPercent)
	{
		fPercent = mStart + (mEnd - mStart) * fPercent;

		if (mpParent)
		{
			fPercent = mpParent->calPercent(fPercent);
		}

		return fPercent;
	}

	virtual void setProgress(unsigned long curStep, unsigned long stepCount)
	{
		float fPercent = (double)curStep / stepCount;
		fPercent = (fPercent > 1) ? 1 : fPercent;
		fPercent = calPercent(fPercent);
		mpDlg->setProgress(fPercent);
	}
};


bool CgetAttributeCodeDlg::getCodeFromMemoryInfo(HANDLE hProcess, MEMORY_BASIC_INFORMATION& mbi, CodeRawSet& codeRawSet, ProgressSetter* pProgressSetter/* = NULL*/)
{
	DWORD addrOffset = 0;
	while (true)
	{
		if (mbi.RegionSize - addrOffset >= CODE_RAW_LENGTH)
		{
			CodeRaw codeRaw;
			SIZE_T readBytes = 0;
			DWORD codeAddr = (DWORD)mbi.BaseAddress + addrOffset;

			//DWORD oldProtect, newProtect = mbi.Protect | PAGE_EXECUTE_READ;
			//VirtualProtectEx(hProcess, mbi.BaseAddress, mbi.RegionSize, newProtect, &oldProtect);

			if (ReadProcessMemory(hProcess, (LPCVOID)codeAddr, codeRaw.data, CODE_RAW_LENGTH, &readBytes))
			{
				if (getZeroCount(codeRaw) <= mMaxZeroCount)
				{
					codeRawSet.insert(codeRaw);
				}
			}

			//VirtualProtectEx(hProcess, mbi.BaseAddress, mbi.RegionSize, oldProtect, &newProtect);

			addrOffset += CODE_RAW_LENGTH;
		}
		else
		{
			break;
		}

		if (pProgressSetter)
		{
			pProgressSetter->setProgress(addrOffset, mbi.RegionSize);
		}
	}

	if (pProgressSetter)
	{
		pProgressSetter->setProgress(mbi.RegionSize, mbi.RegionSize);
	}

	return true;
}

bool CgetAttributeCodeDlg::getCodeFromMemoryInfo(HANDLE hProcess, MEMORY_BASIC_INFORMATION& mbi, CodeRawFilter& filter, ProgressSetter* pProgressSetter/* = NULL*/)
{
	DWORD addrOffset = 0;
	while (!filter.isCodeRawSetEmpty() && mbScanning)
	{
		if (mbi.RegionSize - addrOffset >= CODE_RAW_LENGTH)
		{
			CodeRaw codeRaw;
			SIZE_T readBytes = 0;
			DWORD codeAddr = (DWORD)mbi.BaseAddress + addrOffset;

			//DWORD oldProtect, newProtect = mbi.Protect | PAGE_EXECUTE_READ;
			//VirtualProtectEx(hProcess, mbi.BaseAddress, mbi.RegionSize, newProtect, &oldProtect);

			if (ReadProcessMemory(hProcess, (LPCVOID)codeAddr, codeRaw.data, CODE_RAW_LENGTH, &readBytes))
			{
				if (getZeroCount(codeRaw) <= mMaxZeroCount)
				{
					filter.onGetCodeRaw(codeRaw);
				}
			}

			//VirtualProtectEx(hProcess, mbi.BaseAddress, mbi.RegionSize, oldProtect, &newProtect);

			addrOffset += CODE_RAW_LENGTH;
		}
		else
		{
			break;
		}

		if (pProgressSetter)
		{
			pProgressSetter->setProgress(addrOffset, mbi.RegionSize);
		}
	}

	if (pProgressSetter)
	{
		pProgressSetter->setProgress(mbi.RegionSize, mbi.RegionSize);
	}

	return true;
}

void CgetAttributeCodeDlg::setProgress(float fPercent)
{
	int nLower, nUpper;
	mProgressCtrl.GetRange(nLower, nUpper);

	int pos = nLower + (nUpper - nLower) * fPercent;
	mProgressCtrl.SetPos(pos);
}

DWORD WINAPI ScanThreadProc(LPVOID lpParameter)
{
	CgetAttributeCodeDlg* pDlg = (CgetAttributeCodeDlg*)lpParameter;

	pDlg->mEditStatus.SetWindowText(_T("ɨ�迪ʼ"));

	pDlg->mcsOutput.Empty();

	pDlg->setProgress(0);

	CodeRawSet codeRawSet;

	CString targetProcessFilename = pDlg->getProcessFilename(pDlg->mTargetProcessId);

	TCHAR szStatus[1024] = { 0 };
	_stprintf(szStatus, _T("��ȡĿ�����[%u]�ļ������� %s"), pDlg->mTargetProcessId, targetProcessFilename);
	pDlg->mEditStatus.SetWindowText(szStatus);

	DlgProgressSetter dps1(NULL, pDlg, 0, 0.1);
	pDlg->getCodeFromFile(targetProcessFilename, codeRawSet, &dps1);
	if (!codeRawSet.empty())
	{
		if (pDlg->mTargetProcessId == 0)
		{
			pDlg->mEditStatus.SetWindowText(_T("ö����������"));

			const int MAX_PROCESS_COUNT = 2048;
			DWORD otherProcessIds[MAX_PROCESS_COUNT] = { 0 };
			DWORD processCount = 0;
			EnumProcesses(otherProcessIds, sizeof(otherProcessIds), &processCount);
			processCount /= sizeof(DWORD);

			DlgProgressSetter dps2(NULL, pDlg, 0.1, 0.9);
			for (DWORD i = 0; i < processCount && !codeRawSet.empty() && pDlg->mbScanning; ++i)
			{
				DWORD otherProcessId = otherProcessIds[i];
				if (otherProcessId != 0 && otherProcessId != pDlg->mTargetProcessId && otherProcessId != GetCurrentProcessId())
				{
					CString otherProcessFilename = pDlg->getProcessFilename(otherProcessId);
					if (!otherProcessFilename.IsEmpty())
					{
						TCHAR szStatus[1024] = { 0 };
						_stprintf(szStatus, _T("������������[%u]�ļ������� %s"), otherProcessId, otherProcessFilename);
						pDlg->mEditStatus.SetWindowText(szStatus);

						CodeRawFilter codeRawFilter;
						codeRawFilter.mpCodeRawSet = &codeRawSet;

						DlgProgressSetter dps3(&dps2, pDlg, (float)i / processCount, (float)(i + 1) / processCount);
						pDlg->filterCodeFromFile(otherProcessFilename, codeRawFilter, &dps3);
					}
				}
			}
		}

		pDlg->mEditStatus.SetWindowText(_T("ת���ļ�������Ϊ�ɶ��ַ���"));

		if (pDlg->mTargetProcessId != 0)
		{
			float index = 0;
			CodeRaw codeRaw;
			BYTE tempData[CODE_RAW_LENGTH];
			for (CodeRawSet::iterator it = codeRawSet.begin(); it != codeRawSet.end(); ++it, index++)
			{
				codeRaw = *it;
				if (index == 0)
				{
					memcpy(tempData, codeRaw.data, CODE_RAW_LENGTH);
				}
				else
				{
					for (int i = 0; i < CODE_RAW_LENGTH; i++)
					{
						tempData[i] ^= codeRaw.data[i];
					}
				}
			}

			CString csCode = pDlg->codeRawToCString(tempData);
			if (pDlg->mcsOutput.IsEmpty())
			{
				pDlg->mcsOutput = csCode;
			}
			else
			{
				pDlg->mcsOutput += _T("\r\n") + csCode;
			}
		}
		else
		{
			float index = 0;
			DlgProgressSetter dps4(NULL, pDlg, 0.9, 1);
			for (CodeRawSet::iterator it = codeRawSet.begin(); it != codeRawSet.end(); ++it, index++)
			{
				const CodeRaw& codeRaw = *it;

				CString csCode = pDlg->codeRawToCString(codeRaw.data);
				if (pDlg->mcsOutput.IsEmpty())
				{
					pDlg->mcsOutput = csCode;
				}
				else
				{
					pDlg->mcsOutput += _T("\r\n") + csCode;
				}

				DlgProgressSetter dps5(&dps4, pDlg, index / codeRawSet.size(), (index + 1) / codeRawSet.size());
			}
		}
	}

SCAN_FIN:
	if (codeRawSet.size() > MulitFeatureCode)
	{
		pDlg->mEditStatus.SetWindowText(_T("ɨ����ϣ�ɨ����������룬��ѡ�񼸸���"));
	}
	else
	{
		pDlg->mEditStatus.SetWindowText(_T("ɨ����ϣ�"));
	}

	pDlg->setProgress(1);

	pDlg->mEditOutput.SetWindowText(pDlg->mcsOutput);
	pDlg->onFinishScan();

	return 0;
}

void CgetAttributeCodeDlg::startScan()
{
	if (!mbScanning)
	{
		if (UpdateData(TRUE))
		{
			mbScanning = true;

			mBtnScan.GetWindowText(mBtnScanLabel);
			mBtnScan.SetWindowText(_T("ֹͣɨ��"));

			mhThread = CreateThread(NULL, 0, ScanThreadProc, this, 0, NULL);

			mEditProcessId.EnableWindow(!mbScanning);
			mEditMaxZeroCount.EnableWindow(!mbScanning);
			mSpinMaxZeroCount.EnableWindow(!mbScanning);
		}
	}
}

void CgetAttributeCodeDlg::stopScan()
{
	if (mbScanning)
	{
		mbScanning = false;
	}
}

void CgetAttributeCodeDlg::onFinishScan()
{
	mEditOutput.SetWindowText(mcsOutput);

	mBtnScan.SetWindowText(mBtnScanLabel);

	mEditProcessId.EnableWindow(true);
	mEditMaxZeroCount.EnableWindow(true);
	mSpinMaxZeroCount.EnableWindow(true);

	if (mhThread)
	{
		CloseHandle(mhThread);
		mhThread = NULL;
	}
}

void CgetAttributeCodeDlg::OnBnClickedButtonScan()
{
	if (mbScanning)
	{
		stopScan();
	}
	else
	{
		startScan();
	}
}


void CgetAttributeCodeDlg::OnFinalRelease()
{
	if (mhThread)
	{
		TerminateThread(mhThread, 1);
		CloseHandle(mhThread);
		mhThread = NULL;
	}

	CDialogEx::OnFinalRelease();
}
