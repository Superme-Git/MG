// SoundTestDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "SoundTest.h"
#include "SoundTestDlg.h"
#include "..\engine\common\fileiomanager.h"
#include "..\engine\common\log.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\common\util.h"
#include "..\engine\sound\isounddata.h"
#include "..\engine\map\pimg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

struct XL_TESTINFO
{ 
	static DWORD s_nProcessId;
	static const wchar_t *fn() { return L"testinfo.log"; } 
};

DWORD XL_TESTINFO::s_nProcessId = 0;

#define XPLOG_TESTINFO xwprint<XL_TESTINFO>

// 用于应用程序“关于”菜单项的 CAboutDlg 对话框

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// 对话框数据
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

// 实现
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()


// CSoundTestDlg 对话框




CSoundTestDlg::CSoundTestDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CSoundTestDlg::IDD, pParent)
	, m_pRenderer(NULL)
	, m_pFileIOMan(NULL)
{
	m_pFileIOMan = new Nuclear::FileIOManager();
	if( !m_pFileIOMan->Init(true, true) )
	{
		AfxMessageBox(L"PFS路径初始化失败");
	}
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}
CSoundTestDlg::~CSoundTestDlg()
{
	DestroyRenderer(m_pRenderer);
	m_pRenderer = NULL;
	m_pFileIOMan->Destroy();
	delete m_pFileIOMan;
}

void CSoundTestDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CSoundTestDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDC_BUTTON1, &CSoundTestDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CSoundTestDlg::OnBnClickedButton2)
END_MESSAGE_MAP()


// CSoundTestDlg 消息处理程序

BOOL CSoundTestDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// 将“关于...”菜单项添加到系统菜单中。

	// IDM_ABOUTBOX 必须在系统命令范围内。
	XPASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	XPASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// 设置此对话框的图标。当应用程序主窗口不是对话框时，框架将自动
	//  执行此操作
	SetIcon(m_hIcon, TRUE);			// 设置大图标
	SetIcon(m_hIcon, FALSE);		// 设置小图标

	return TRUE;  // 除非将焦点设置到控件，否则返回 TRUE
}

void CSoundTestDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// 如果向对话框添加最小化按钮，则需要下面的代码
//  来绘制该图标。对于使用文档/视图模型的 MFC 应用程序，
//  这将由框架自动完成。

void CSoundTestDlg::OnPaint()
{
	if( m_pRenderer == NULL ) 
	{
		Nuclear::CRECT sourceRect(0, 0, 10, 10);
		//int *a = new int[1];
		CreateRenderer(&m_pRenderer, GetSafeHwnd(), Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)), m_pFileIOMan);
		//a = new int[2];
		m_pRenderer->SetSourceRect(&sourceRect);
	}
	if (IsIconic())
	{
		CPaintDC dc(this); // 用于绘制的设备上下文

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// 使图标在工作矩形中居中
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// 绘制图标
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

//当用户拖动最小化窗口时系统调用此函数取得光标显示。
//
HCURSOR CSoundTestDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

class IDisposer
{
public:
	virtual void OnChangeFolder(const CString &path, const CString &name) = 0;
	virtual bool OnIMGFile(const CString &path, const CString &file) = 0;//返回false的话就不读取img文件，就不会调用OnDDSFile
	virtual bool OnDDSFile(const CString &path, const CString &file) = 0;
};

class CopyDisposer : public IDisposer
{
protected:
	int m_oldPathLength;
	CString m_outPath;
	CString m_currentPath;
public:
	CopyDisposer(const CString &path) : m_oldPathLength(0), m_outPath(path) {}
	virtual void OnChangeFolder(const CString &path, const CString &name) 
	{
		if (name.IsEmpty())
		{
			m_oldPathLength = path.GetLength();
		}
		m_currentPath = m_outPath + path.Right(path.GetLength() - m_oldPathLength);
		
		if (GetFileAttributes(m_currentPath) == 0xffffffff)
		{
			if (!CreateDirectory(m_currentPath, NULL))
			{
				AfxMessageBox(L"创建路径失败\n");
			}
		}
		m_currentPath += L"\\";
	}
	//返回false的话就不读取img文件，就不会调用OnDDSFile
	virtual bool OnIMGFile(const CString &path, const CString &file) 
	{
		CopyFile(path, m_currentPath + file, TRUE);
		return true;
	}
	virtual bool OnDDSFile(const CString &path, const CString &file) 
	{
		CopyFile(path, m_currentPath + file, TRUE);
		return true;
	}
};

class OnlyReSaveDisposer : public CopyDisposer
{
	Nuclear::Renderer *m_pRenderer;
	Nuclear::FileIOManager *m_pFileIOMan;
public:
	OnlyReSaveDisposer(const CString &path, Nuclear::Renderer *pRenderer, Nuclear::FileIOManager *pFileIOMan) : 
	  CopyDisposer(path), m_pRenderer(pRenderer), m_pFileIOMan(pFileIOMan) {}

	virtual bool OnDDSFile(const CString &path, const CString &file) 
	{
		CString tmpPath = path;
		CString OutFile = m_currentPath + file;
		Nuclear::XBuffer in;
		if (m_pFileIOMan->GetFileImage(tmpPath.GetBuffer(), in))
		{
			tmpPath.ReleaseBuffer();
			Nuclear::PictureHandle handle = m_pRenderer->LoadPictureFromMem(in.constbegin(), in.size(), NULL, Nuclear::XPTEXFMT_A8R8G8B8);
			if (handle == Nuclear::INVALID_PICTURE_HANDLE)
			{
				XPLOG_TESTINFO(L"%s 不是图片？\n", tmpPath.GetBuffer());
				tmpPath.ReleaseBuffer();
				return false;
			}
			Nuclear::XBuffer out;
			m_pRenderer->SaveTextureToFile(handle, Nuclear::XPTEXFMT_DXT5, Nuclear::XPIFF_DDS, out);
			m_pRenderer->FreePicture(handle);
			FILE *f = Nuclear::FOpen(OutFile.GetBuffer(), L"wb");
			OutFile.ReleaseBuffer();
			if( f == NULL )
			{
				XPLOG_TESTINFO(L"打开 %s 失败！\n", OutFile.GetBuffer());
				OutFile.ReleaseBuffer();
				return false;
			}
			Nuclear::FWrite(f, out.constbegin(), out.size());
			fclose(f);
		} else {
			tmpPath.ReleaseBuffer();
			XPLOG_TESTINFO(L"打开 %s 失败！\n", tmpPath.GetBuffer());
			tmpPath.ReleaseBuffer();
			return false;
		}
		return true;
	}
};

class ScanAlphaDisposer : public CopyDisposer
{
	Nuclear::Renderer *m_pRenderer;
	Nuclear::FileIOManager *m_pFileIOMan;
public:
	ScanAlphaDisposer(const CString &path, Nuclear::Renderer *pRenderer, Nuclear::FileIOManager *pFileIOMan) : 
	  CopyDisposer(path), m_pRenderer(pRenderer), m_pFileIOMan(pFileIOMan) {}

	  virtual bool OnDDSFile(const CString &path, const CString &file) 
	  {
		  CString tmpPath = path;
		  CString OutFile = m_currentPath + file;
		  Nuclear::XBuffer in;
		  if (m_pFileIOMan->GetFileImage(tmpPath.GetBuffer(), in))
		  {
			  tmpPath.ReleaseBuffer();
			  Nuclear::PictureHandle handle = m_pRenderer->LoadPictureFromMem(in.constbegin(), in.size(), NULL, Nuclear::XPTEXFMT_A8R8G8B8);
			  if (handle == Nuclear::INVALID_PICTURE_HANDLE)
			  {
				  XPLOG_TESTINFO(L"%s 不是图片？\n", tmpPath.GetBuffer());
				  tmpPath.ReleaseBuffer();
				  return false;
			  }
			  m_pRenderer->ClearAlpha(handle);
			  Nuclear::XBuffer out;
			  m_pRenderer->SaveTextureToFile(handle, Nuclear::XPTEXFMT_DXT5, Nuclear::XPIFF_DDS, out);
			  m_pRenderer->FreePicture(handle);
			  FILE *f = Nuclear::FOpen(OutFile.GetBuffer(), L"wb");
			  OutFile.ReleaseBuffer();
			  if( f == NULL )
			  {
				  XPLOG_TESTINFO(L"打开 %s 失败！\n", OutFile.GetBuffer());
				  OutFile.ReleaseBuffer();
				  return false;
			  }
			  Nuclear::FWrite(f, out.constbegin(), out.size());
			  fclose(f);
		  } else {
			  tmpPath.ReleaseBuffer();
			  XPLOG_TESTINFO(L"打开 %s 失败！\n", tmpPath.GetBuffer());
			  tmpPath.ReleaseBuffer();
			  return false;
		  }
		  return true;
	  }
};

void CSoundTestDlg::Scan(const CString &path, IDisposer *pDisposer)
{
	if (!pDisposer)
		return;
	XPLOG_TESTINFO(L"开始Scan\n");
	CFileFind myFinder;
	CString FileName;
	CString Path;
	CString fullPath;
	CString ddsName;
	CString ddsNameFormat;
	std::list<CString> folderList;
	std::list<CString> folderNameList;
	BOOL working;
	folderList.push_back(path);
	folderNameList.push_back(L"");
	while (!folderList.empty())  
	{  
		Path = folderList.front();
		folderList.pop_front();
		pDisposer->OnChangeFolder(Path, folderNameList.front());
		folderNameList.pop_front();
		Path += L"\\";
		working = myFinder.FindFile(Path + L"*.*");
		while (working)
		{
			working = myFinder.FindNextFile();
			if (myFinder.IsDots())   
				continue;
			if (myFinder.IsHidden())
				continue;
			FileName = myFinder.GetFileName();
			if (FileName.Right(4).MakeLower() != L".set" && !myFinder.IsDirectory())
				continue;
			if (myFinder.IsDirectory()) 
			{
				folderList.push_back(myFinder.GetFilePath());
				folderNameList.push_back(myFinder.GetFileName());
			} else {
				fullPath = myFinder.GetFilePath();
				if (pDisposer->OnIMGFile(fullPath, FileName))
				{
					Nuclear::PImg img;
					img.LoadFromNativePath(fullPath.GetBuffer());
					fullPath.ReleaseBuffer();
					int count = (int)img.m_vectorRct.size();
					if (img.m_nCol * img.m_nRow != count)
					{
						XPLOG_TESTINFO(L"%s 的 dds 数量有问题！\n", fullPath.GetBuffer());
						fullPath.ReleaseBuffer();
					}
					ddsNameFormat = myFinder.GetFileTitle() + L"_%d";
					for (int i=0;i<count;++i)
					{
						ddsName.Format(ddsNameFormat, i);
						pDisposer->OnDDSFile(Path + ddsName, ddsName);
					}
				}
			}
		}	
	}
}

class AllDisposer : public IDisposer
{
	CopyDisposer *pCopy;
	OnlyReSaveDisposer *pReSave;
	ScanAlphaDisposer *pScan;

public:
	AllDisposer(const CString &Copypath, const CString &resavepath, const CString &scanpath, Nuclear::Renderer *pRenderer, Nuclear::FileIOManager *pFileIOMan)
	{
		pCopy = new CopyDisposer(Copypath);
		pReSave = new OnlyReSaveDisposer(resavepath, pRenderer, pFileIOMan);
		pScan = new ScanAlphaDisposer(scanpath, pRenderer, pFileIOMan);
	}

	~AllDisposer() 
	{
		delete pCopy;
		delete pReSave;
		delete pScan;
	}

	virtual void OnChangeFolder(const CString &path, const CString &name)
	{
		pCopy->OnChangeFolder(path, name);
		pReSave->OnChangeFolder(path, name);
		pScan->OnChangeFolder(path, name);
	}
	
	//返回false的话就不读取img文件，就不会调用OnDDSFile
	virtual bool OnIMGFile(const CString &path, const CString &file)
	{
		pCopy->OnIMGFile(path, file);
		pReSave->OnIMGFile(path, file);
		pScan->OnIMGFile(path, file);
		return true;
	}
	virtual bool OnDDSFile(const CString &path, const CString &file)
	{
		pCopy->OnDDSFile(path, file);
		pReSave->OnDDSFile(path, file);
		pScan->OnDDSFile(path, file);
		return true;
	}
};

void CSoundTestDlg::OnBnClickedButton1()
{
	//CopyDisposer tmpDis(L"D:\\a");
	//OnlyReSaveDisposer tmpDis(L"D:\\a", m_pRenderer, m_pFileIOMan);
	AllDisposer tmpDis(L"D:\\copy", L"D:\\resave", L"d:\\scan", m_pRenderer, m_pFileIOMan);
	Scan(L"D:\\map\\elements", &tmpDis);
}

void CSoundTestDlg::OnBnClickedButton2()
{
}
