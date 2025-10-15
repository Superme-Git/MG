#include "StdAfx.h"
#include "DistortionInfoView.h"
#include "DistortionEditorDoc.h"
#include "DistortionEditor.h"
#include "..\engine\common\pfsutil.h"

IMPLEMENT_DYNCREATE(CDistortionInfoView, CFormView)

CDistortionInfoView::CDistortionInfoView(void)
: CFormView(CDistortionInfoView::IDD)
, m_bLightFlag(FALSE)
{
	m_pRender = NULL;
	m_vectorPicID.clear();
	m_dwBKcolor = 0xffa0a0a0;
	m_nCurSelItem = 0;

	m_nDistortionType = 0;
	m_nGridx = 10;

	m_fSpeed = 2.0f;
	m_fCycNum = 0.2f;
	m_fSwing = 1.0f;

	m_TPCTangentPoints.clear();//再次编辑的时候需要
	m_SPCSamplePoints.clear();

	m_strSysWorkPath = L"";
}

CDistortionInfoView::~CDistortionInfoView(void)
{
	int cnt = (int)m_vectorPicID.size();
	for(int i=0; i<cnt; i++)
	{
		m_pRender->FreePicture(m_vectorPicID.at(i));
	}
	m_vectorPicID.clear();
	DestroyRenderer(m_pRender);
}

void CDistortionInfoView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST_FILEINFO, m_ListCtrl);
	DDX_Control(pDX, IDC_COMBO_DRIVERLIST, m_comboxDriver);
	DDX_Control(pDX, IDC_COMBO_DistortionType, m_comboDistortionType);
	DDX_Control(pDX, IDC_SLIDER_Gridx, m_sliderGridx);
	DDX_Control(pDX, IDC_SLIDER_Gridy, m_sliderSpeed);
	DDX_Control(pDX, IDC_SLIDER_CycNum, m_sliderCycNum);
	DDX_Control(pDX, IDC_SLIDER_Swing, m_sliderSwing);
	DDX_Text(pDX, IDC_EDIT_Gridx, m_nGridx);
	DDX_Text(pDX, IDC_EDIT_Gridy, m_fSpeed);
	DDX_Text(pDX, IDC_EDIT_CycNum, m_fCycNum);
	DDX_Text(pDX, IDC_EDIT_Swing, m_fSwing);

	//	DDV_MinMaxFloat(pDX, m_nGridx, 0.0f, 10.0f);
	DDV_MinMaxFloat(pDX, m_fSpeed, 1.0f, 100.0f);
	DDV_MinMaxFloat(pDX, m_fCycNum, 0.0f, 2.0f);
	DDV_MinMaxFloat(pDX, m_fSwing, 0.0f, 5.0f);
	DDX_Check(pDX, IDC_CHECK_ADDLIGHT, m_bLightFlag);
}

BEGIN_MESSAGE_MAP(CDistortionInfoView, CFormView)
	ON_CBN_SELCHANGE(IDC_COMBO_DRIVERLIST, &CDistortionInfoView::OnCbnSelchangeComboDriverlist)
	ON_NOTIFY(NM_CLICK, IDC_LIST_FILEINFO, &CDistortionInfoView::OnNMClickListFileinfo)
	ON_NOTIFY(NM_DBLCLK, IDC_LIST_FILEINFO, &CDistortionInfoView::OnNMDblclkListFileinfo)
	ON_BN_CLICKED(IDC_BUTTON_SETBKCOLOR, &CDistortionInfoView::OnBnClickedButtonSetbkcolor)
	ON_WM_HSCROLL()
	ON_WM_VSCROLL()

	ON_CBN_SELCHANGE(IDC_COMBO_DistortionType, &CDistortionInfoView::OnCbnSelchangeComboDistortiontype)
	ON_WM_KILLFOCUS()
	ON_BN_CLICKED(IDC_BUTTON_CurveAdjust, &CDistortionInfoView::OnBnClickedButtonCurveadjust)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST_FILEINFO, &CDistortionInfoView::OnLvnItemchangedListFileinfo)
	ON_BN_CLICKED(IDC_CHECK_ADDLIGHT, &CDistortionInfoView::OnBnClickedCheckAddlight)
END_MESSAGE_MAP()


#ifdef _DEBUG
void CDistortionInfoView::AssertValid() const
{
	CFormView::AssertValid();
}

#ifndef _WIN32_WCE
void CDistortionInfoView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}
#endif
#endif //_DEBUG


// CRegionInfoView message handlers

void CDistortionInfoView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	// TODO: Add your specialized code here and/or call the base class
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	// TODO: add draw code here

	UpdateData(FALSE);
}

void CDistortionInfoView::OnDraw(CDC* pDC)
{
	if(m_pRender == NULL)
	{
		HRESULT hr = CreateRenderer(&m_pRender, m_hWnd, Nuclear::XDisplayMode(m_rctDesPicShow.Width(), m_rctDesPicShow.Height()), theApp.m_pFileIOManager, Nuclear::XPCRF_VERTICALSYNC);
		if( FAILED(hr) )
			return;
	}

	m_pRender->Begin(m_dwBKcolor); //
	CPoint size = GetScrollPosition();
	int dx = m_rctDesPicShow.left -size.x;
	int dy = m_rctDesPicShow.top -size.y;
	int nWidth = m_imgObject.m_nFileWidth;
	int nHeight = m_imgObject.m_nFileHeight;
	int nShowWidth = m_rctDesPicShow.Width();
	int nShowHeight = m_rctDesPicShow.Height();
	float coefx = nShowWidth/(float)(nWidth);
	float coefy = nShowHeight/(float)(nHeight);

	Nuclear::CRECT rct_temp;
	rct_temp.left = dx;
	rct_temp.top = dy;
	rct_temp.right = rct_temp.left + m_rctDesPicShow.Width();
	rct_temp.bottom = rct_temp.top + m_rctDesPicShow.Height();

	m_pRender->SetDestRect(&rct_temp);
	m_pRender->SetSourceRect(&rct_temp);

	int cnt = (int)m_vectorPicID.size();
	for(int i=0; i<cnt; i++)
	{
		Nuclear::FRECT rct;
		rct = m_imgObject.m_vectorRct.at(i).ToFRECT();
		float cx = rct.Width() *coefx;
		float cy = rct.Height() *coefy;
		rct.left *= coefx;
		rct.top *= coefy;
		rct.right = rct.left +cx;
		rct.bottom = rct.top +cy;
		rct += Nuclear::FPOINT((float)dx, (float)dy);

		m_pRender->DrawPicture(m_vectorPicID.at(i), rct, 0xffffffff);
	}
	m_pRender->End();
}

void CDistortionInfoView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();

	// TODO: Add your specialized code here and/or call the base class	
	GetDlgItem(IDC_STATIC_SHOWPIC)->GetWindowRect(&m_rctDesPicShow);
	ScreenToClient(&m_rctDesPicShow); 

	int nPos = 0;
	int nDrivesAdded = 0;
	CString string = _T ("?:\\");
	DWORD dwDriveList = ::GetLogicalDrives();
	while(dwDriveList)
	{
		if (dwDriveList & 1)
		{
			string.SetAt (0, _T ('A') + nPos);
			m_comboxDriver.InsertString(-1, string);
			nDrivesAdded++;
		}
		dwDriveList >>= 1;
		nPos++;
	}

	m_font.CreateFont(16, 0,0,0,FW_NORMAL, 0,0,0,
		DEFAULT_CHARSET, OUT_CHARACTER_PRECIS, CLIP_CHARACTER_PRECIS,
		DEFAULT_QUALITY, DEFAULT_PITCH | FF_DONTCARE, L"Arial");
	m_ListCtrl.SetFont(&m_font);

	m_ListCtrl.SetExtendedStyle(LVS_EX_FULLROWSELECT | LVS_EX_GRIDLINES); // | LVS_EX_CHECKBOXES

	m_ListCtrl.SetBkColor(RGB(200,200,200));
	m_ListCtrl.SetTextColor(RGB(0,0,0));
	m_ListCtrl.SetTextBkColor(RGB(200,200,200));
	m_ListCtrl.InsertColumn(0, L"文件名称", LVCFMT_LEFT, 160);
	m_ListCtrl.InsertColumn(1, L"文件大小", LVCFMT_LEFT, 80);
	m_ListCtrl.InsertColumn(2, L"日期时间", LVCFMT_LEFT, 80);

#ifndef _DEBUG
	TCHAR workdir[MAX_PATH];
	GetModuleFileName(NULL, workdir, MAX_PATH);
#endif

	TCHAR workPath[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workPath);
	std::wstring strWorkDir = workPath;
	int npos = (int)strWorkDir.rfind('\\');
	strWorkDir = std::wstring(strWorkDir, 0, npos);
	std::wstring sysFilePath = _T("\\res\\map\\elements\\");

	std::wstring strTemp = strWorkDir + sysFilePath;
	m_strPath = strTemp.c_str();
	m_strSysWorkPath = m_strPath;

	CString strLeft = m_strPath.Left(3);
	int CurIndex = m_comboxDriver.FindString(0, strLeft);
	m_comboxDriver.SetCurSel(CurIndex);

	Refresh(m_strPath); //m_strPath

	m_comboDistortionType.ResetContent();
	m_comboDistortionType.InsertString(0, _T("水平方向固定_1"));
	m_comboDistortionType.InsertString(1, _T("水平方向固定_2"));
	m_comboDistortionType.InsertString(2, _T("垂直方向固定_1"));
	m_comboDistortionType.InsertString(3, _T("垂直方向固定_2"));
	m_comboDistortionType.InsertString(4, _T("斜对角方向固定"));
	m_comboDistortionType.SetCurSel(m_nDistortionType);

	//	m_sliderSwing.SetPos((int)(m_fSwing*100));


//	m_sliderGridx.SetRange(0, 50, true);
//	m_sliderGridx.SetPos(10);
	m_sliderSpeed.SetRange(100, 10000, true); //1.0f--100.0f
	m_sliderSpeed.SetPos((int)(m_fSpeed*100));
//	m_sliderSpeed.SetPos(200); //-------default 2.0f
	m_sliderCycNum.SetRange(0,200, true); //0.0f~~~~2.0f -----0.2f
	m_sliderCycNum.SetPos((int)(m_fCycNum*100));
//	m_sliderCycNum.SetPos(20);
//	m_sliderSwing.SetRange(0, 500, true); //0.0f~~~~~1.0f
//	m_sliderSwing.SetPos(100);

	CRect rect;
	((CStatic*)(GetDlgItem(IDC_STATIC_CurveAdjustText)))->GetWindowRect(&rect);
	ScreenToClient(&rect);

	rect.top = rect.bottom;
	rect.right = rect.left +500;
	rect.bottom = rect.top +200;
// 
// 	m_bezier.Create(m_bezier.IDD, this);
// 	int height = rect.Height();
// 	int widht = rect.Width();
// 	m_bezier.MoveWindow(&rect);
// 	m_bezier.ShowWindow(SW_SHOW);
// 	m_bezier.SetMinX(0);
// 	m_bezier.SetMaxX(1);
// 	m_bezier.SetMinY(0);
// 	m_bezier.SetMaxY(5);
// 
// 	m_bezier.unitX = 0.01f;
// 	m_bezier.unitY = 0.1f;
// 	m_bezier.SetLineCount(1);
// 	m_bezier.vectorCurveColor.push_back(RGB(0,255,0));
}

void CDistortionInfoView::OnCbnSelchangeComboDriverlist()
{
	// TODO: Add your control notification handler code here
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	XPASSERT(pDoc);

	int nIndex = m_comboxDriver.GetCurSel();
	CString str;
	m_comboxDriver.GetLBText(nIndex, str);
	m_strPath = str;
	Refresh (m_strPath);
	AfxGetMainWnd()->SetWindowText(m_strPath);
}

int CDistortionInfoView::Refresh (LPCTSTR pszPath)
{
	CString strPath = pszPath;
	if (strPath.Right (1) != _T ("\\"))
		strPath += _T ("\\");
	strPath += _T ("*.*");

	HANDLE hFind;
	WIN32_FIND_DATA fd;
	int nCount = 0;

	if ((hFind = ::FindFirstFile(strPath, &fd)) != INVALID_HANDLE_VALUE)
	{
		m_ListCtrl.DeleteAllItems();

		m_ListTypeMap.clear(); //0文件 1文件夹 2上一级目录
		m_FileInfoList.clear();

		TCHAR szFullPath[MAX_PATH];
		::GetFullPathName (pszPath, sizeof (szFullPath) / sizeof (TCHAR), szFullPath, NULL);
		m_strPath = szFullPath;

		CString strTitle = _T("IMGEDIT - ");
		strTitle += szFullPath;
		SetWindowText (strTitle);

		if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_HIDDEN))
		{
			if (!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
				SetFileListInfo(&fd);
			else
			{
				CString strComp = (LPCTSTR) &fd.cFileName;
				if ((strComp != _T(".")) && (strComp != _T("..")))
					SetFileListInfo(&fd, 1);
			}
		}

		while (::FindNextFile (hFind, &fd)) 
		{
			if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_HIDDEN))
			{
				if (!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
					SetFileListInfo(&fd);
				else
				{
					CString strComp = (LPCTSTR) &fd.cFileName;
					if ((strComp != _T(".")) && (strComp != _T("..")))
						SetFileListInfo(&fd, 1);
				}
			}
		}
	}
	::FindClose(hFind);

	FileInfo fileInfo;//添加一项指向上层目录的...
	fileInfo.strFileName = _T("..");
	fileInfo.strFileSize = _T("<DIR>");
	fileInfo.strFileLastWriteTime = _T("上层目录");
	m_FileInfoList.push_back(fileInfo);

	m_ListTypeMap.insert(std::make_pair(_T(".."), 1));//0文件 1文件夹

	//显示列表
	ShowFileList();

	m_comboxDriver.SetWindowText(m_strPath);
	//	AfxGetMainWnd()->SetWindowText(m_strPath);
	return nCount;
}

BOOL CDistortionInfoView::SetFileListInfo(WIN32_FIND_DATA* pfd, int fileType)
{
	CString strFileName = pfd->cFileName;
	CString strFileSize;
	CString strFileTime;

	if(fileType == 0)
	{//文件img
		CString strFileNameEX = strFileName.Right(3);
		bool isIMG = (strFileNameEX == _T("set")) || (strFileNameEX == _T("SET"));
		bool isDIS = (strFileNameEX == _T("dis")) || (strFileNameEX == _T("DIS"));
		if( !(isIMG || isDIS) )
			return false;
	}
	// else //1文件夹

	DWORD nFileSizeLow = (DWORD)(pfd->nFileSizeLow/1024.0f) +1;
	FILETIME ftLastWriteTime = pfd->ftLastWriteTime;

	strFileSize.Format (_T ("%u"), nFileSizeLow);
	strFileSize += _T(" KB");
	if(fileType==1)
		strFileSize = _T("<DIR>");
	// Date and time
	CTime time(ftLastWriteTime);

	BOOL pm = FALSE;
	int nHour = time.GetHour();
	if (nHour == 0)
		nHour = 12;
	else if(nHour == 12)
		pm = TRUE;
	else if(nHour > 12) {
		nHour -= 12;
		pm = TRUE;
	}

	strFileTime.Format( _T ("%d/%0.2d/%0.2d (%d:%0.2d%c)"),
		time.GetMonth(), time.GetDay(), time.GetYear() % 100,
		nHour, time.GetMinute(), pm ? _T('p') : _T('a') );


	m_ListTypeMap.insert(std::make_pair(strFileName, fileType));//0文件 1文件夹

	FileInfo fileInfo;
	fileInfo.strFileName = strFileName;
	fileInfo.strFileSize = strFileSize;
	fileInfo.strFileLastWriteTime = strFileTime;
	if(fileType==1)
		m_FileInfoList.push_back(fileInfo);
	else
		m_FileInfoList.push_front(fileInfo);

	return true;
}

BOOL CDistortionInfoView::ShowFileList()
{
	for(std::list<FileInfo>::iterator it = m_FileInfoList.begin(), ie = m_FileInfoList.end(); it != ie; ++it)
	{
		FileInfo& fileInfo = *it;
		AddItem(fileInfo);
	}

	return true;
}

BOOL CDistortionInfoView::AddItem(FileInfo fileInfo)
{
	m_ListCtrl.InsertItem(0, fileInfo.strFileName);
	m_ListCtrl.SetItemText(0,1, fileInfo.strFileSize);
	m_ListCtrl.SetItemText(0,2, fileInfo.strFileLastWriteTime);

	return true;
}

void CDistortionInfoView::OnNMClickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	XPASSERT(pDoc);

	// TODO: 在此添加控件通知处理程序代码
	POSITION pos = m_ListCtrl.GetFirstSelectedItemPosition();
	int nCurSelListIndex = m_ListCtrl.GetNextSelectedItem(pos);  // 得到项目索引
	CString strFileName = m_ListCtrl.GetItemText(nCurSelListIndex, 0);

	int fileType;
	std::map<CString, int>::const_iterator it = m_ListTypeMap.find(strFileName);
	if( it == m_ListTypeMap.end() )
		return;
	else
		fileType = it->second;

	if(fileType==1)
		return;

	if(m_strPath.Right(1) != _T ("\\"))
		m_strPath += _T("\\");
	strFileName = m_strPath + strFileName;
	
	CString strFileNameEX = strFileName.Right(3);	
	bool isIMG = strFileNameEX.CompareNoCase(L"set")==0 ? true:false;
	bool isDIS = strFileNameEX.CompareNoCase(L"dis")==0 ? true:false;

	if( !(isIMG || isDIS) )
	{
		return; //逻辑错误...
	}


	if(isIMG)
	{
		//检查文件路径是否符合编辑规范，不符合的给出提示
		int npos = strFileName.Find(L"\\elements\\");

		CString resonablePath = strFileName;
		if(npos != -1)
			resonablePath = strFileName.Left(npos+10);

		//判断路径是否合法。。合理的是只有当前elements目录下的..可以建子目录:)
		//指定了别的目录下的文件也是可以的...但是给出提醒（编辑之后要把对应的img放对位置）
		if(resonablePath.CompareNoCase(m_strSysWorkPath) != 0)
		{
			CString strInfo;
			strInfo = L"当前素材所在路径: ";
			strInfo += resonablePath;
			strInfo += L"      \n";
			strInfo += L"不符合编辑习惯, 是否继续编辑?";
			if(AfxMessageBox(strInfo, MB_YESNO) == IDNO)
				return;
		}

		//

		//读文件显示...
		int cnt = (int)m_vectorPicID.size();
		for(int i=0; i<cnt; i++)
		{
			m_pRender->FreePicture( m_vectorPicID.at(i) );
		}
		m_vectorPicID.clear();

		FILE *f = NULL;
		_tfopen_s(&f, strFileName, _T("rb"));
		if( f == NULL )
			return;

		BOOL ret = m_imgObject.Load(f) ? TRUE:FALSE;
		fclose(f);
		if(!ret)
			return;

		//读img文件初始化
		//根据文件名来加载显示...
		int nPos = strFileName.ReverseFind('.');
		CString filePathNoEX = strFileName.Left(nPos);

		cnt = (int)m_imgObject.m_vectorRct.size();
		for(int i=0; i<cnt; i++)
		{
			CString str;
			str.Format(_T("_%d"), i);
			CString fileNameNoEx = filePathNoEX + str;
			Nuclear::PictureHandle handle = m_pRender->LoadPictureFromNativePath(std::wstring(fileNameNoEx));
			m_vectorPicID.push_back(handle);
		}

		Nuclear::CRECT rct;
		rct.left = m_rctDesPicShow.left;
		rct.top = m_rctDesPicShow.top;
		rct.right = m_rctDesPicShow.right;
		rct.bottom = m_rctDesPicShow.bottom;
		m_pRender->SetSourceRect(&rct);

		InvalidateRect(&m_rctDesPicShow);

//		pDoc->GetDistortionEditorView()->AddPicToDis(strFileName, m_bLightFlag);
	}
	else
	{ //预览并编辑dis文件

		int cnt = (int)m_vectorPicID.size();
		for(int i=0; i<cnt; i++)
		{
			m_pRender->FreePicture( m_vectorPicID.at(i) );
		}
		m_vectorPicID.clear();


		Nuclear::PDistortionObject& distortionObjs = pDoc->GetDistortionObject();
		pDoc->SetDisFileName(strFileName);

		FILE *f = NULL;
		_tfopen_s(&f, strFileName, _T("rb"));
		if( f == NULL ) return;

		BOOL ret = false;
		ret = distortionObjs.Load(f) ? TRUE : FALSE;
		fclose(f);
		if(!ret)
			return;

		pDoc->UpdateAllViews(NULL);
		pDoc->SetTitle(strFileName);

		pDoc->GetDistortionEditorView()->m_bLoadFile = true;
		pDoc->GetDistortionInfoView()->Updatectrl();
	}

	*pResult = 0;
}

void CDistortionInfoView::OnBnClickedButtonSetbkcolor()
{
	CColorDialog dlg;
	if( IDOK != dlg.DoModal() )
		return;

	COLORREF color = dlg.GetColor(); //BGR --> ARGB (alpha值为255, 画纯色背景用)
	m_dwBKcolor = (0xffffffff & 0xff000000) | (color & 0x000000ff)<<16 | (color & 0x0000ff00) | (color & 0x00ff0000)>>16;

	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	pDoc->GetDistortionEditorView()->SetBKColor(m_dwBKcolor);
	pDoc->UpdateAllViews(NULL);
}

void CDistortionInfoView::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if( pScrollBar==NULL )
	{ //点击的是滚动条..
		if(m_pRender != NULL)
		{
			CPoint size = GetScrollPosition();
			RECT rct;
			rct.left = m_rctDesPicShow.left -size.x;
			rct.top = m_rctDesPicShow.top -size.y;
			rct.right = m_rctDesPicShow.right -size.x;
			rct.bottom = m_rctDesPicShow.bottom -size.y;
			//		m_pRender->SetDestRect(&rct);
		}
	}
	else
	{ //滑动条...
		m_nGridx = m_sliderGridx.GetPos(); //暂时没用
		m_fSpeed = m_sliderSpeed.GetPos() *0.01f;
		m_fCycNum = m_sliderCycNum.GetPos() *0.01f;
		m_fSwing = m_sliderSwing.GetPos() *0.01f;
		UpdateData(false); //刷新控件

		CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
		XPASSERT(pDoc);
		Nuclear::PDistortionObject& distortionObjs = pDoc->GetDistortionObject();
		distortionObjs.m_distortionObj.fSpeed = m_fSpeed;
		distortionObjs.m_distortionObj.fCycNum = m_fCycNum;
//		distortionObjs.m_distortionObj.fAmplitude = m_fSwing;

		CToolsDistort* pObj = pDoc->GetDistortionEditorView()->GetDistortionObjEffect();
		if(NULL != pObj)
		{
			Nuclear::DistortionEffectParam& param = pObj->GetParam();
			param.fSpeed = m_fSpeed;
			param.fCycNum = m_fCycNum;
		}
	}

	CFormView::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CDistortionInfoView::OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	if(m_pRender != NULL)
	{
		CPoint size = GetScrollPosition();
		RECT rct;
		rct.left = m_rctDesPicShow.left -size.x;
		rct.top = m_rctDesPicShow.top -size.y;
		rct.right = m_rctDesPicShow.right -size.x;
		rct.bottom = m_rctDesPicShow.bottom -size.y;
//		m_pRender->SetDestRect(&rct);
	}

	CFormView::OnVScroll(nSBCode, nPos, pScrollBar);
}

void CDistortionInfoView::OnNMDblclkListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	XPASSERT(pDoc);

	POSITION pos = m_ListCtrl.GetFirstSelectedItemPosition();
	int nCurSelListIndex = m_ListCtrl.GetNextSelectedItem(pos);  // 得到项目索引
	CString strFileName = m_ListCtrl.GetItemText(nCurSelListIndex, 0);

	int fileType;
	std::map<CString, int>::const_iterator it = m_ListTypeMap.find(strFileName);
	if( it == m_ListTypeMap.end() )
		return;
	else
		fileType = it->second;

	if(fileType==0)
	{//文件
		if(m_strPath.Right(1) != _T ("\\"))
			m_strPath += _T("\\");
		strFileName = m_strPath + strFileName;

		CString strFileNameEX = strFileName.Right(3);	
		bool isIMG = strFileNameEX.CompareNoCase(L"set")==0 ? true:false;

		if( !isIMG )
		{
			return; //只有img图片可以添加到右侧编辑
		}

		//检查文件路径是否符合编辑规范，不符合的给出提示
		int npos = strFileName.Find(L"\\elements\\");

		CString resonablePath = strFileName;
		if(npos != -1)
			resonablePath = strFileName.Left(npos+10);

		//判断路径是否合法。。合理的是只有当前elements目录下的..可以建子目录:)
		//指定了别的目录下的文件也是可以的...但是给出提醒（编辑之后要把对应的img放对位置）
		if(resonablePath.CompareNoCase(m_strSysWorkPath) != 0)
		{
			CString strInfo;
			strInfo = L"当前素材所在路径: ";
			strInfo += resonablePath;
			strInfo += L"      \n";
			strInfo += L"不符合编辑习惯, 是否继续编辑?";
			if(AfxMessageBox(strInfo, MB_YESNO) == IDNO)
				return;
		}
		//

		std::wstring strKey;//elments下包含子目录名的文件名
		int len = strFileName.GetLength();
		npos = strFileName.Find(L"\\elements\\");
		strKey = strFileName.Right(len-npos);
		std::wstring TempKey = strKey;
		bool bres = PFS::CEnv::NativeFileNameToPFSFileName(TempKey, strKey);

		Nuclear::PDistortionObject& distortionObjs = pDoc->GetDistortionObject();
		CString strMainFile = distortionObjs.m_distortionObj.strImgFileName.c_str();
		if(m_bLightFlag)
		{
			if(strMainFile == L"")
			{
				MessageBox(L"先添加主节点再添加灯光\n");
				return;
			}
			else
			{
			//	distortionObjs.m_distortionObj.strLightFile = strKey;
			}
		}
		else
		{
		//	distortionObjs.m_distortionObj.strImgFileName = strKey;
		}

		pDoc->GetDistortionEditorView()->AddPicToDis(strFileName, m_bLightFlag!=0);
	}
	else
	{//文件夹
		if (m_strPath.Right(1) != _T("\\"))
			m_strPath += _T("\\");
		m_strPath += strFileName;
		Refresh(m_strPath);
	}

	*pResult = 0;
}
void CDistortionInfoView::OnCbnSelchangeComboDistortiontype()
{
	// TODO: 在此添加控件通知处理程序代码
	m_nDistortionType = m_comboDistortionType.GetCurSel();

	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	Nuclear::PDistortionObject& distortionObjs = pDoc->GetDistortionObject();
	distortionObjs.m_distortionObj.nType = (XPDISTORTIONMESHTYPE)m_nDistortionType;

	CToolsDistort* pObj = pDoc->GetDistortionEditorView()->GetDistortionObjEffect();
	if(NULL != pObj)
	{
		Nuclear::DistortionEffectParam& param = pObj->GetParam();
		param.nType = (XPDISTORTIONMESHTYPE)m_nDistortionType;
		//	pObj->SetParam(param);
	}

}

void CDistortionInfoView::OnKillFocus(CWnd* pNewWnd)
{
	CFormView::OnKillFocus(pNewWnd);

	// TODO: 在此处添加消息处理程序代码
	UpdateData(true);

	m_fCycNum = m_fCycNum>2.0f ? 2.0f:m_fCycNum;
	m_fCycNum = m_fCycNum<0.0f ? 0.0f:m_fCycNum;
	m_fSwing = m_fSwing>5.0f ? 5.0f:m_fSwing;
	m_fSwing = m_fSwing<0.0f ? 0.0f:m_fSwing;
	m_fSpeed = m_fSpeed>100.0f ? 100.0f:m_fSpeed;
	m_fSpeed = m_fSpeed<1.0f ? 1.0f:m_fSpeed;
	
	UpdateData(false);

	m_sliderCycNum.SetPos((int)(m_fCycNum*100));
	m_sliderSwing.SetPos((int)(m_fSwing*100));
	m_sliderSpeed.SetPos((int)(m_fSpeed*100));

	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	Nuclear::PDistortionObject& distortionObjs = pDoc->GetDistortionObject();
	distortionObjs.m_distortionObj.fSpeed = m_fSpeed;
	distortionObjs.m_distortionObj.fCycNum = m_fCycNum;
//	distortionObjs.m_distortionObj.fAmplitude = m_fSwing;


	CToolsDistort* pObj = pDoc->GetDistortionEditorView()->GetDistortionObjEffect();
	if(NULL != pObj)
	{
		Nuclear::DistortionEffectParam& param = pObj->GetParam();
		param.fSpeed = m_fSpeed;
		param.fCycNum = m_fCycNum;
	}
}

void CDistortionInfoView::Updatectrl()
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	Nuclear::PDistortionObject& distortionObjs = pDoc->GetDistortionObject();

	m_fSpeed = distortionObjs.m_distortionObj.fSpeed;
	m_fCycNum = distortionObjs.m_distortionObj.fCycNum;
//	m_fSwing = distortionObjs.m_distortionObj.fAmplitude;
	m_nDistortionType = distortionObjs.m_distortionObj.nType;

	m_sliderCycNum.SetPos((int)(m_fCycNum*100));
//	m_sliderSwing.SetPos((int)(m_fSwing*100));
	m_sliderSpeed.SetPos((int)(m_fSpeed*100));
	m_comboDistortionType.SetCurSel(m_nDistortionType);
	UpdateData(false);

	std::vector<FPOINT> famplitudes = distortionObjs.m_distortionObj.fAmplitudes;
	int cnt = (int)famplitudes.size();

	bezier::SamplePointContanter samplePtList;
	bezier::SamplePoint samplePt;

	bezier::TangentPointContanter TangentPtList;
	bezier::TangentPoint TangentPt;

	for(int i=0; i<cnt; i++)
	{
		samplePt.point = bezier::FPoint(famplitudes[i].x, famplitudes[i].y);
		samplePt.id = i;
		samplePt.u = 0.0f;

		if(i == cnt-1)
		{
			samplePt.id = i-1;
			samplePt.u = 1.0f;
		}

		samplePtList.push_back(samplePt);

		TangentPt.point = bezier::FPoint(famplitudes[i].x, famplitudes[i].y);
		TangentPt.controlPt[0] = TangentPt.point;
		TangentPt.controlPt[1] = TangentPt.point;
		TangentPtList.push_back(TangentPt);
	}
	m_SPCSamplePoints.push_back(samplePtList);
	m_TPCTangentPoints.push_back(TangentPtList);
}

#include "DlgSwingCurveAdjust.h"
void CDistortionInfoView::OnBnClickedButtonCurveadjust()
{
	CDlgSwingCurveAdjust dlg;

	int cnt1 = (int)m_TPCTangentPoints.size(); //再次编辑的时候需要
	int cnt2 = (int)m_SPCSamplePoints.size();
	if(cnt1+cnt2 != 0)
		dlg.setCurveParamData(m_SPCSamplePoints, m_TPCTangentPoints);

	if( IDOK != dlg.DoModal() )
		return;

	m_TPCTangentPoints = dlg.m_TPCTangentPoints; //再次编辑的时候需要
	m_SPCSamplePoints = dlg.m_SPCSamplePoints;
	int nSize = (int)m_SPCSamplePoints.size();

	//std::list<SamplePoint>
	bezier::SamplePointContanter sampleList = m_SPCSamplePoints.at(0);

	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	Nuclear::PDistortionObject& distortionObjs = pDoc->GetDistortionObject();

	distortionObjs.m_distortionObj.fAmplitudes.clear();

	bezier::SamplePointContanter::iterator spIter = sampleList.begin();
	bezier::SamplePointContanter::iterator spLast = sampleList.end();
	for( ; spIter != spLast; spIter++)
	{
		distortionObjs.m_distortionObj.fAmplitudes.push_back(Nuclear::FPOINT(spIter->point.x, spIter->point.y));
	}

	CToolsDistort* pObj = pDoc->GetDistortionEditorView()->GetDistortionObjEffect();
	if(NULL != pObj)
	{
		Nuclear::DistortionEffectParam& param = pObj->GetParam();
		param.fAmplitudes.clear();
		param.fAmplitudes = distortionObjs.m_distortionObj.fAmplitudes;
	}
}

void CDistortionInfoView::OnLvnItemchangedListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码
	OnNMClickListFileinfo(pNMHDR, pResult);
	*pResult = 0;
}

void CDistortionInfoView::OnBnClickedCheckAddlight()
{
	UpdateData(true);
}
