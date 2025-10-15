#include "StdAfx.h"
#include "LinkedObjectInfoView.h"
#include "LinkedObjectEditorDoc.h"
#include "LinkedObjectEditor.h"

#include "..\engine\common\util.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\common\xml.h"

IMPLEMENT_DYNCREATE(CLinkedObjectInfoView, CFormView)

CLinkedObjectInfoView::CLinkedObjectInfoView(void)
: CFormView(CLinkedObjectInfoView::IDD)
, m_bSetLinkedObjectPosCenter(FALSE)
, m_bStop(FALSE)
, m_bDragFlag(TRUE) //是否允许拖动--右侧编辑区的物件
, m_bPreviewLKO(FALSE)
{
	m_pRender = NULL;
//	m_hpicHandle = 0;
	m_vectorPicID.clear();
	m_dwBKcolor = 0xffa0a0a0;
	m_nCurSelItem = 0;

	m_pDragImage = NULL;	//拖曳图像
	m_bDragging = false;	//是否正在拖曳
	m_nDragIndex = -1;	//开始拖曳的位置
	m_nDropIndex = -1;	//拖曳到哪里
	m_bInitRenameRule = false;

	m_vectorRenameFiles.clear();
	m_vectorRenameDirs.clear();

	m_nLightAlpha = 255;
	m_nFreshFreq = 0;

	m_nSortColIndex = 0;
	m_nSortFlag = 0;

}

CLinkedObjectInfoView::~CLinkedObjectInfoView(void)
{
	int cnt = (int)m_vectorPicID.size();
	for(int i=0; i<cnt; i++)
	{
		m_pRender->FreePicture(m_vectorPicID.at(i));
	}
	m_vectorPicID.clear();
	DestroyRenderer(m_pRender);

	if(m_pDragImage != NULL)
	{
		delete m_pDragImage;
		m_pDragImage = NULL;
	}
}

void CLinkedObjectInfoView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST_FILEINFO, m_ListCtrl);
	DDX_Control(pDX, IDC_COMBO_DRIVERLIST, m_comboxDriver);
	DDX_Control(pDX, IDC_LIST_SHOWPLANTLEVEL, m_listCtrlLinkedObjectLevel);
	DDX_Check(pDX, IDC_CHECK_SETPLANTPOSCENTER, m_bSetLinkedObjectPosCenter);
	DDX_Check(pDX, IDC_CHECK_Stop, m_bStop);
	DDX_Control(pDX, IDC_SLIDER_LightAlpha, m_sliderLightAlpha);
	DDX_Check(pDX, IDC_CHECK_DragFlag, m_bDragFlag);
	DDX_Check(pDX, IDC_CHECK_previewLKO, m_bPreviewLKO);
}

BEGIN_MESSAGE_MAP(CLinkedObjectInfoView, CFormView)
	ON_CBN_SELCHANGE(IDC_COMBO_DRIVERLIST, &CLinkedObjectInfoView::OnCbnSelchangeComboDriverlist)
	ON_NOTIFY(NM_CLICK, IDC_LIST_FILEINFO, &CLinkedObjectInfoView::OnNMClickListFileinfo)
	ON_NOTIFY(NM_DBLCLK, IDC_LIST_FILEINFO, &CLinkedObjectInfoView::OnNMDblclkListFileinfo)
	ON_BN_CLICKED(IDC_BUTTON_SETBKCOLOR, &CLinkedObjectInfoView::OnBnClickedButtonSetbkcolor)
	ON_WM_HSCROLL()
	ON_WM_VSCROLL()
	ON_NOTIFY(NM_RCLICK, IDC_LIST_SHOWPLANTLEVEL, &CLinkedObjectInfoView::OnNMRclickListShowplantlevel)
	ON_COMMAND(ID_MENU_PRELEVEL, &CLinkedObjectInfoView::OnMenuPrelevel)
	ON_COMMAND(ID_MENU_NEXTLEVEL, &CLinkedObjectInfoView::OnMenuNextlevel)
	ON_COMMAND(ID_MENU_FIRSTLEVEL, &CLinkedObjectInfoView::OnMenuFirstlevel)
	ON_COMMAND(ID_MENU_LASTLEVEL, &CLinkedObjectInfoView::OnMenuLastlevel)
	ON_NOTIFY(HDN_ITEMCHANGED, 0, &CLinkedObjectInfoView::OnHdnItemchangedListShowplantlevel)
	ON_NOTIFY(NM_CLICK, IDC_LIST_SHOWPLANTLEVEL, &CLinkedObjectInfoView::OnNMClickListShowplantlevel)
	ON_COMMAND(ID_MENU_SetMainNode, &CLinkedObjectInfoView::OnMenuSetmainnode)
	ON_COMMAND(ID_MENU_DelCurSelNode, &CLinkedObjectInfoView::OnMenuDelcurselnode)
	ON_BN_CLICKED(IDC_CHECK_SETPLANTPOSCENTER, &CLinkedObjectInfoView::OnBnClickedCheckSetplantposcenter)
	ON_NOTIFY(LVN_BEGINDRAG, IDC_LIST_SHOWPLANTLEVEL, &CLinkedObjectInfoView::OnLvnBegindragListShowplantlevel)
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_BN_CLICKED(IDC_CHECK_Stop, &CLinkedObjectInfoView::OnBnClickedCheckStop)
	ON_BN_CLICKED(IDC_BUTTON_CONVERSION, &CLinkedObjectInfoView::OnBnClickedButtonConversion)
	ON_BN_CLICKED(IDC_BUTTON_IMGFileRename, &CLinkedObjectInfoView::OnBnClickedButtonImgfilerename)
	ON_BN_CLICKED(IDC_BUTTON_InitRenameRule, &CLinkedObjectInfoView::OnBnClickedButtonInitrenamerule)
	ON_BN_CLICKED(IDC_BUTTON_RenameFilesAndDir, &CLinkedObjectInfoView::OnBnClickedButtonRenamefilesanddir)
	ON_BN_CLICKED(IDC_BUTTON_RenameMapFile, &CLinkedObjectInfoView::OnBnClickedButtonRenamemapfile)
	ON_BN_CLICKED(IDC_BUTTON_RefreshMaxBox, &CLinkedObjectInfoView::OnBnClickedButtonRefreshmaxbox)
	ON_BN_CLICKED(IDC_CHECK_DragFlag, &CLinkedObjectInfoView::OnBnClickedCheckDragflag)
	
	ON_COMMAND(ID_MENU_copyfilename, &CLinkedObjectInfoView::OnMenucopyfilename)
	ON_NOTIFY(LVN_COLUMNCLICK, IDC_LIST_FILEINFO, &CLinkedObjectInfoView::OnLvnColumnclickListFileinfo)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST_FILEINFO, &CLinkedObjectInfoView::OnLvnItemchangedListFileinfo)
	ON_BN_CLICKED(IDC_CHECK_previewLKO, &CLinkedObjectInfoView::OnBnClickedCheckpreviewlko)
	ON_BN_CLICKED(IDC_BUTTON_RefreshCentPos, &CLinkedObjectInfoView::OnBnClickedButtonRefreshcentpos)
END_MESSAGE_MAP()


#ifdef _DEBUG
void CLinkedObjectInfoView::AssertValid() const
{
	CFormView::AssertValid();
}

#ifndef _WIN32_WCE
void CLinkedObjectInfoView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}
#endif
#endif //_DEBUG


// CRegionInfoView message handlers

void CLinkedObjectInfoView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	// TODO: Add your specialized code here and/or call the base class
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	// TODO: add draw code here

	UpdateData(FALSE);
}

void CLinkedObjectInfoView::OnDraw(CDC* pDC)
{
	if(m_pRender == NULL)
	{
		HRESULT hr = CreateRenderer(&m_pRender, m_hWnd, Nuclear::XDisplayMode(m_rctDesPicShow.Width(), m_rctDesPicShow.Height()), theApp.m_pFileIOManager, Nuclear::XPCRF_VERTICALSYNC);
		if( FAILED(hr) )
			return;
	}

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

// 	if(rct_temp.bottom <=0 || rct_temp.right <=0)
// 	{
// 		return;
// 	}

	m_pRender->SetDestRect(&rct_temp);
	m_pRender->SetSourceRect(&rct_temp);

	m_pRender->Begin(m_dwBKcolor); //

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

		m_pRender->DrawPicture(m_vectorPicID.at(i), rct, 0xFFFFFFFF);
	}
	m_pRender->End();
}

void CLinkedObjectInfoView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();

	m_sliderLightAlpha.SetRange(0, 255);
	m_sliderLightAlpha.SetPos(255);

	GetDlgItem(IDC_BUTTON_IMGFileRename)->EnableWindow(m_bInitRenameRule);
	GetDlgItem(IDC_BUTTON_RenameFilesAndDir)->EnableWindow(m_bInitRenameRule);
	GetDlgItem(IDC_BUTTON_RenameMapFile)->EnableWindow(m_bInitRenameRule);

	// TODO: Add your specialized code here and/or call the base class	
	GetDlgItem(IDC_STATIC_SHOWPIC)->GetWindowRect(&m_rctDesPicShow);
	ScreenToClient(&m_rctDesPicShow); 

	int nPos = 0;
	int nDrivesAdded = 0;
	CString string = _T ("?:\\");
	DWORD dwDriveList = ::GetLogicalDrives();
	while (dwDriveList) {
		if (dwDriveList & 1) {
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
	m_ListCtrl.InsertColumn(3, L"文件类型", LVCFMT_LEFT, 60);

	//	m_comboDriverList.GetLBText(0, m_strPath);

	m_listCtrlLinkedObjectLevel.SetFont(&m_font);

	m_listCtrlLinkedObjectLevel.SetExtendedStyle(LVS_EX_FULLROWSELECT | LVS_EX_GRIDLINES); // | LVS_EX_CHECKBOXES

	m_listCtrlLinkedObjectLevel.SetBkColor(RGB(200,200,200));
	m_listCtrlLinkedObjectLevel.SetTextColor(RGB(0,0,0));
	m_listCtrlLinkedObjectLevel.SetTextBkColor(RGB(200,200,200));
	m_listCtrlLinkedObjectLevel.InsertColumn(0, L"物件ID", LVCFMT_LEFT, 60);
	m_listCtrlLinkedObjectLevel.InsertColumn(1, L"物件名称", LVCFMT_LEFT, 200);
	m_listCtrlLinkedObjectLevel.InsertColumn(2, L"主节点", LVCFMT_LEFT, 70);
	m_listCtrlLinkedObjectLevel.InsertColumn(3, L"运动跟随", LVCFMT_LEFT, 70);
	m_listCtrlLinkedObjectLevel.InsertColumn(4, L"灯光", LVCFMT_LEFT, 50);
	m_listCtrlLinkedObjectLevel.InsertColumn(5, L"img格式", LVCFMT_LEFT, 60);
	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	std::wstring wdir = workdir;
	int tmppos = (int)wdir.rfind('\\');
	wdir = std::wstring(wdir, 0, tmppos);
	std::wstring sysFilePath = _T("\\res");
	std::wstring strTemp = wdir + sysFilePath + L"\\map\\elements\\";

	m_strPath = strTemp.c_str();
	m_strSysWorkPath = m_strPath;
   
	CString strLeft = m_strPath.Left(3);
	int CurIndex = m_comboxDriver.FindString(0, strLeft);
	m_comboxDriver.SetCurSel(CurIndex);

	Refresh(m_strPath); //m_strPath
}

void CLinkedObjectInfoView::OnCbnSelchangeComboDriverlist()
{
	// TODO: Add your control notification handler code here
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);

	int nIndex = m_comboxDriver.GetCurSel();
	CString str;
	m_comboxDriver.GetLBText(nIndex, str);
	m_strPath = str;
	Refresh (m_strPath);
	AfxGetMainWnd()->SetWindowText(m_strPath);
}

int CLinkedObjectInfoView::Refresh (LPCTSTR pszPath)
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
		if (!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
			SetFileListInfo(&fd);
		else
		{
			CString strComp = (LPCTSTR) &fd.cFileName;
			if ((strComp != _T(".")) && (strComp != _T("..")))
				SetFileListInfo(&fd, 1);
		}

		while (::FindNextFile (hFind, &fd)) 
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

BOOL CLinkedObjectInfoView::SetFileListInfo(WIN32_FIND_DATA* pfd, int fileType)
{
	CString strFileName = pfd->cFileName;
	CString strFileSize;
	CString strFileTime;

	if(fileType == 0)
	{//文件 bmp tga img
		CString strFileNameEX = strFileName.Right(3);
		bool isBMP = (strFileNameEX == _T("bmp")) || (strFileNameEX == _T("BMP"));
		bool isTGA = (strFileNameEX == _T("tga")) || (strFileNameEX == _T("TGA"));
		bool isIMG = (strFileNameEX == _T("set")) || (strFileNameEX == _T("SET"));
		bool isPNG = (strFileNameEX == _T("png")) || (strFileNameEX == _T("PNG"));
		bool isDDS = (strFileNameEX == _T("dds")) || (strFileNameEX == _T("DDS"));
		bool isJPG = (strFileNameEX == _T("jpg")) || (strFileNameEX == _T("JPG"));
		bool isLKO = strFileNameEX.CompareNoCase(L"lko")==0 ? true:false;
		if( !(isIMG || isLKO) ) //||isPNG||isDDS||isJPG
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

BOOL CLinkedObjectInfoView::ShowFileList()
{
	for(std::list<FileInfo>::iterator it = m_FileInfoList.begin(), ie = m_FileInfoList.end(); it != ie; ++it)
	{
		FileInfo& fileInfo = *it;
		AddItem(fileInfo);
	}

	return true;
}

BOOL CLinkedObjectInfoView::AddItem(FileInfo fileInfo)
{
	m_ListCtrl.InsertItem(0, fileInfo.strFileName);
	m_ListCtrl.SetItemText(0,1, fileInfo.strFileSize);
	m_ListCtrl.SetItemText(0,2, fileInfo.strFileLastWriteTime);

	int nPos = fileInfo.strFileName.ReverseFind('.');
	int cnt = fileInfo.strFileName.GetLength() -nPos-1;
	CString strFileNameEX;
	if(cnt!=0 && nPos!=-1)
	{
		strFileNameEX = fileInfo.strFileName.Right(cnt);
	}
	else
		strFileNameEX = L"文件夹";

	m_ListCtrl.SetItemText(0,3, strFileNameEX);

	return true;
}

void CLinkedObjectInfoView::OnLvnItemchangedListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码
	OnNMClickListFileinfo(pNMHDR, pResult);
	*pResult = 0;
}

void CLinkedObjectInfoView::OnNMClickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
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
	//读文件显示...
	CString strFileNameEX = strFileName.Right(3);	
	bool isIMG = strFileNameEX.CompareNoCase(L"set")==0 ? true:false;
	bool isLKO = strFileNameEX.CompareNoCase(L"lko")==0 ? true:false;

	if( !(isIMG || isLKO) )
	{
		return; //逻辑错误...
	}

	if(isIMG)
	{
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
	}
	else
	{
		if(m_bPreviewLKO)
		{
			pDoc->GetLinkedObjectEditorView()->OpenPreviewFile(strFileName);
		}
	}

	*pResult = 0;
}

void CLinkedObjectInfoView::OnNMDblclkListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: 在此添加控件通知处理程序代码
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;
// 
// 	if(pDoc->IsModified())
// 	{
// 		CString strInfo = _T("是否保存当前文件的修改？");
// 		int res = MessageBox(strInfo, _T("提示"), MB_ICONQUESTION|MB_YESNOCANCEL);
// 		if(res == IDYES) //IDYES
// 		{
// 			pDoc->OnSaveDocument(m_strFileName_old);
// 		}
// 		else if(res == IDCANCEL)
// 		{
// 			return;
// 		}
// 		else
// 		{// res == IDNO 继续后面的处理...
// 		}
// 	}

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
		if(m_bPreviewLKO)
		{
			return; //预览状态...
		}
		CString filePath = m_strPath+strFileName;

		CString strFileNameEX = strFileName.Right(3);	
		bool isIMG = strFileNameEX.CompareNoCase(L"set")==0 ? true:false;
		if(!isIMG)
		{
			return; //不是img文件不能通过这种方式来添加编辑...
		}

		//检查文件路径是否符合编辑规范，不符合的给出提示
		int npos = filePath.Find(L"\\elements\\");

		CString resonablePath = filePath;
		if(npos != -1)
			resonablePath = filePath.Left(npos+10);

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

		int bres = pDoc->GetLinkedObjectEditorView()->AddPicMap(filePath);
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

void CLinkedObjectInfoView::OnBnClickedButtonSetbkcolor()
{
	CColorDialog dlg;
	if( IDOK != dlg.DoModal() )
		return;

	COLORREF color = dlg.GetColor(); //BGR --> ARGB (alpha值为255, 画纯色背景用)
	m_dwBKcolor = (0xffffffff & 0xff000000) | (color & 0x000000ff)<<16 | (color & 0x0000ff00) | (color & 0x00ff0000)>>16;

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	pDoc->GetLinkedObjectEditorView()->SetBKColor(m_dwBKcolor);
	pDoc->UpdateAllViews(NULL);
}

void CLinkedObjectInfoView::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
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
	if( pScrollBar != NULL )
	{ //点击的不是滚动条..	
		int nCurSliderID = pScrollBar->GetDlgCtrlID();
		if(IDC_SLIDER_LightAlpha == nCurSliderID)
		{
			m_nLightAlpha = m_sliderLightAlpha.GetPos();

			CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
			if (pDoc == NULL)
				return CFormView::OnHScroll(nSBCode, nPos, pScrollBar);;

			pDoc->GetLinkedObjectEditorView()->SetLightAlpha(m_nLightAlpha);
		}
	}
	else
	{
		m_nFreshFreq++;
		if(m_nFreshFreq%3==0)
		{
			RECT rctTemp;
			rctTemp.left = m_rctDesPicShow.left;
			rctTemp.right = m_rctDesPicShow.right;
			rctTemp.top = m_rctDesPicShow.top;
			rctTemp.bottom = m_rctDesPicShow.bottom;

			InvalidateRect(&rctTemp, false);

			if(m_nFreshFreq>10)
			{
				Invalidate();
				m_nFreshFreq = 0;
			}
		}
	}

	CFormView::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CLinkedObjectInfoView::OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CPoint size = GetScrollPosition();
	RECT rct;
	rct.left = m_rctDesPicShow.left -size.x;
	rct.top = m_rctDesPicShow.top -size.y;
	rct.right = m_rctDesPicShow.right -size.x;
	rct.bottom = m_rctDesPicShow.bottom -size.y;

//	if(m_pRender != NULL)
//	{
//		m_pRender->SetDestRect(&rct);
//	}

	m_nFreshFreq++;
	if(m_nFreshFreq%3==0)
	{
		RECT rctTemp;
		rctTemp.left = m_rctDesPicShow.left;
		rctTemp.right = m_rctDesPicShow.right;
		rctTemp.top = m_rctDesPicShow.top;
		rctTemp.bottom = m_rctDesPicShow.bottom;

		InvalidateRect(&rctTemp, false);

		if(m_nFreshFreq>10)
		{
			Invalidate();
			m_nFreshFreq = 0;
		}
	}

	CFormView::OnVScroll(nSBCode, nPos, pScrollBar);
}

void CLinkedObjectInfoView::OnNMRclickListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: 在此添加控件通知处理程序代码

	if(m_bSetLinkedObjectPosCenter)
	{
		MessageBox(L"目前处于修改重心底座状态...不响应右键菜单..");
	}
	else
	{
		CPoint pt;
		::GetCursorPos(&pt);

		CMenu menu;
		menu.LoadMenu(IDR_MENU2);
		menu.EnableMenuItem(ID_MENU_PRELEVEL, MF_ENABLED | MF_BYCOMMAND);
		menu.EnableMenuItem(ID_MENU_NEXTLEVEL, MF_ENABLED | MF_BYCOMMAND);

		CMenu* pMenu = menu.GetSubMenu(0);
		pMenu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pt.x, pt.y, this, NULL);

		POSITION pos = m_listCtrlLinkedObjectLevel.GetFirstSelectedItemPosition();
		m_nCurSelItem = m_listCtrlLinkedObjectLevel.GetNextSelectedItem(pos);  // 得到项目索引

		if(m_nCurSelItem != -1)
		{
			CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
			if (pDoc == NULL)
				return;

			pDoc->GetLinkedObjectEditorView()->m_curSelFileID = _wtoi( m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0) );


			//调整了顺序要修改pplant对象。顺序是绘制层次关系..
			pDoc->SetModifiedFlag(true);
		}
	}

	*pResult = 0;
}

void CLinkedObjectInfoView::OnMenuPrelevel()
{
	m_listCtrlLinkedObjectLevel.SetFocus();
	// 上移一层
	if(m_nCurSelItem == -1)	  //如果没有选择就返回
	{
		MessageBox(	L"请选择一项再上移!", L"提示", MB_ICONINFORMATION);
		return;  
	}

	if(m_nCurSelItem == 0)  // 判断所选项是否位于行首
	{
		MessageBox(L"已经位于最上层!", L"提示", MB_ICONINFORMATION);
		return;
	}

	int nDragIndex = m_nCurSelItem;
	int nDropIndex = m_nCurSelItem-2;
	if(m_nCurSelItem == 1)
	{
		nDragIndex = 0;
		nDropIndex = 1;
	}

	ModifyDTList(nDragIndex, nDropIndex);
	// 提取所选列表项各列类容
	CString str1, str2, str3, str4, str5, str6;
	str1 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	str2 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);
	str3 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 2);
	str4 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 3);
	str5 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 4);
	str6 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 5);

	// 删除所选列表项
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	// 在m_nCurSelItem-1位置处插入上面所删列表项的各列类容	
	m_listCtrlLinkedObjectLevel.InsertItem(m_nCurSelItem-1, str1);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 5, str6);
	m_nCurSelItem--;

	// 使得m_nCurSelItem-1位置处项目高亮显示并获得焦点
	UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
	m_listCtrlLinkedObjectLevel.SetItemState(m_nCurSelItem, flag, flag);
}

void CLinkedObjectInfoView::OnMenuNextlevel()
{
	// 下移一层
	m_listCtrlLinkedObjectLevel.SetFocus();
	if(m_nCurSelItem == -1)	  //如果没有选择就返回
	{
		MessageBox(L"请选择一项再下移!", L"提示", MB_ICONINFORMATION);
		return;  
	}

	if(m_nCurSelItem == m_listCtrlLinkedObjectLevel.GetItemCount()-1)
	{
		MessageBox(L"已经位于最下层!", L"提示", MB_ICONINFORMATION);
		return;
	}

	int nDragIndex = m_nCurSelItem;
	int nDropIndex = m_nCurSelItem-1;
	ModifyDTList(nDragIndex, nDropIndex);


	// 提取所选列表项各列类容
	CString str1, str2, str3, str4, str5, str6;
	str1 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	str2 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);
	str3 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 2);
	str4 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 3);
	str5 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 4);
	str6 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 5);

	// 删除所选列表项
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	// 在m_nCurSelItem+1位置处插入上面所删列表项的各列类容	
	m_listCtrlLinkedObjectLevel.InsertItem(m_nCurSelItem+1, str1);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 5, str6);

	m_nCurSelItem++;

	// 使得m_nCurSelItem+1位置处项目高亮显示并获得焦点
	UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
	m_listCtrlLinkedObjectLevel.SetItemState(m_nCurSelItem, flag, flag);
}

void CLinkedObjectInfoView::OnMenuFirstlevel()
{
	// 移到第一层
	m_listCtrlLinkedObjectLevel.SetFocus();
	if(m_nCurSelItem == -1)	  //如果没有选择就返回
	{
		MessageBox(	L"请选择一项再移!", L"提示", MB_ICONINFORMATION);
		return;  
	}

	if(m_nCurSelItem == 0)  // 判断所选项是否位于行首
	{
		MessageBox(L"已经位于第一层!", L"提示", MB_ICONINFORMATION);
		return;
	}

	int nDragIndex = m_nCurSelItem;
	int nDropIndex = 0;
	ModifyDTList(nDragIndex, nDropIndex); //有问题..这样只移到了第二个位置...

	// 提取所选列表项各列类容
	CString str1, str2, str3, str4, str5, str6;
	str1 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	str2 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);
	str3 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 2);
	str4 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 3);
	str5 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 4);
	str6 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 5);

	// 删除所选列表项
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	// 在0位置处插入上面所删列表项的各列类容	
	m_listCtrlLinkedObjectLevel.InsertItem(0, str1);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 5, str6);

	m_nCurSelItem = 0;

	// 使得0位置处项目高亮显示并获得焦点
	UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
	m_listCtrlLinkedObjectLevel.SetItemState(m_nCurSelItem, flag, flag);
}

void CLinkedObjectInfoView::OnMenuLastlevel()
{
	// 移到最后一层
	m_listCtrlLinkedObjectLevel.SetFocus();
	if(m_nCurSelItem == -1)	  //如果没有选择就返回
	{
		MessageBox(L"请选择一项再移!", L"提示", MB_ICONINFORMATION);
		return;  
	}

	if(m_nCurSelItem == m_listCtrlLinkedObjectLevel.GetItemCount()-1)
	{
		MessageBox(L"已经位于最后一层!", L"提示", MB_ICONINFORMATION);
		return;
	}

	int cnt = m_listCtrlLinkedObjectLevel.GetItemCount();
	int nDragIndex = m_nCurSelItem;
	int nDropIndex = cnt;
	ModifyDTList(nDragIndex, nDropIndex);

	// 提取所选列表项各列类容
	CString str1, str2, str3, str4, str5, str6;
	str1 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	str2 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);
	str3 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 2);
	str4 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 3);
	str5 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 4);
	str6 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 5);

	// 在m_listCtrlLinkedObjectLevel.GetItemCount()-1位置处插入上面所删列表项的各列类容	
	m_listCtrlLinkedObjectLevel.InsertItem(m_listCtrlLinkedObjectLevel.GetItemCount(), str1);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 5, str6);

	// 删除所选列表项
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	m_nCurSelItem = m_listCtrlLinkedObjectLevel.GetItemCount()-1;

	m_listCtrlLinkedObjectLevel.InsertItem(m_nCurSelItem, str1);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 5, str6);

	// 使得m_listCtrlLinkedObjectLevel.GetItemCount()-1位置处项目高亮显示并获得焦点
	UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
	m_listCtrlLinkedObjectLevel.SetItemState(m_nCurSelItem, flag, flag);
}

void CLinkedObjectInfoView::OnHdnItemchangedListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMHEADER phdr = reinterpret_cast<LPNMHEADER>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码
	*pResult = 0;
}

void CLinkedObjectInfoView::OnNMClickListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: 在此添加控件通知处理程序代码
	LPNMHEADER phdr = reinterpret_cast<LPNMHEADER>(pNMHDR);

	if(m_bSetLinkedObjectPosCenter)
	{
		MessageBox(L"设置底座、重心状态处于选中状态...");
	}
	else
	{
		m_nCurSelItem = phdr->iItem;
		CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
		if(pDoc == NULL)
			return;
		pDoc->GetLinkedObjectEditorView()->m_curSelFileID = _wtoi( m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0) );
	}

	*pResult = 0;
}

void CLinkedObjectInfoView::OnMenuSetmainnode()
{
	int cnt = m_listCtrlLinkedObjectLevel.GetItemCount();
	for(int i=0; i<cnt; i++)
	{
		m_listCtrlLinkedObjectLevel.SetItemText(i, 2, _T("0"));
		if(i == m_nCurSelItem)
			m_listCtrlLinkedObjectLevel.SetItemText(i, 2, _T("1"));
	}

	CString curSelFileName = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if(pDoc == NULL)
		return;
	DWORD curSelFileID = _wtoi(curSelFileName);
	pDoc->GetLinkedObjectEditorView()->SetMainNode(curSelFileID);
}

void CLinkedObjectInfoView::OnMenuDelcurselnode()
{
	DWORD curSelFileID = _wtoi( m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0) );
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if(pDoc == NULL)
		return;

	pDoc->GetLinkedObjectEditorView()->DelNode(curSelFileID);
}

void CLinkedObjectInfoView::OnBnClickedCheckSetplantposcenter()
{
	UpdateData(true);

	int cnt = m_listCtrlLinkedObjectLevel.GetItemCount();
	if( cnt==0 )
	{
		m_bSetLinkedObjectPosCenter = false;
		MessageBox(L"当前还没有可编辑的动态物件呢\n");
		UpdateData(false);
	}

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	pDoc->GetLinkedObjectEditorView()->SetLinkedObjectPosCenterFlag( (m_bSetLinkedObjectPosCenter != 0) );
}

void CLinkedObjectInfoView::OnLvnBegindragListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码

	m_nDragIndex = pNMLV->iItem;
	if(m_nDragIndex != -1)
	{
		//拖曳图片
		POINT point;
		int offset = 62; //图片的偏移
		point.y = offset;

		m_pDragImage = m_listCtrlLinkedObjectLevel.CreateDragImage(m_nDragIndex, &point);
		m_pDragImage->BeginDrag(0, CPoint(offset+60, offset -4));
		m_pDragImage->DragEnter(this, pNMLV->ptAction); //GetDesktopWindow()

		m_bDragging = true;
		m_nDropIndex = -1;

		SetCapture ();
	}

	*pResult = 0;
}

void CLinkedObjectInfoView::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(m_bDragging)
	{
		// 移动图片～
		CPoint pt(point);
		ClientToScreen(&pt);
		m_pDragImage->DragMove(pt);
		m_pDragImage->DragShowNolock(false);

		SetCursor(LoadCursor(NULL, IDC_ARROW));
		UINT uFlags;
		// Get the item that is below cursor
		m_listCtrlLinkedObjectLevel.ScreenToClient(&pt);
		m_nDropIndex = m_listCtrlLinkedObjectLevel.HitTest(pt, &uFlags);
		if(m_nDropIndex != -1)
		{
			m_pDragImage->DragShowNolock(false);
			UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
			m_listCtrlLinkedObjectLevel.SetItemState(m_nDropIndex, flag, flag);
			m_nCurSelItem = m_nDropIndex;
			m_pDragImage->DragShowNolock(true);
		}

		CRect rct;
		m_listCtrlLinkedObjectLevel.GetClientRect(&rct);
		m_listCtrlLinkedObjectLevel.ClientToScreen(&rct);
		InvalidateRect(&rct, false);	
	}

	CFormView::OnMouseMove(nFlags, point);
}

void CLinkedObjectInfoView::ModifyDTList(int nDragIndex, int nDropIndex)
{//移动绘制层的时候调整list数据..
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	CString strTemp = m_listCtrlLinkedObjectLevel.GetItemText(nDragIndex, 2);
	BOOL bMainNode = _wtoi(strTemp);

	if( !bMainNode )
	{//调整顺序...
		Nuclear::DTList& dtlist = pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.GetLinkedObjectList();

		CString strID = m_listCtrlLinkedObjectLevel.GetItemText(nDragIndex, 0);
		DWORD dwDragFileID = _wtoi(strID);
		int nDraglist = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDragFileID);

		strID = m_listCtrlLinkedObjectLevel.GetItemText(nDropIndex, 0);
		DWORD dwDropFileID = _wtoi(strID);
		int nDroplist = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDropFileID);

		//在这里不能释放节点数据..
		if(nDroplist == 0)
		{//插到主节点后..
			if(nDropIndex == 0)//主节点位于列表框的第一个节点..
			{
				nDropIndex = 0;
			}
			else
			{
				strID = m_listCtrlLinkedObjectLevel.GetItemText(nDropIndex-1, 0);
				dwDropFileID = _wtoi(strID);
				nDroplist = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDropFileID);
			}
		}
		else
		{//不用重新计算nDroplist
		}

		pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.InsertLinkedObjectNode(dtlist[nDraglist], nDroplist+1);//后插..
		if(nDroplist > nDraglist)
		{
			dtlist.erase( dtlist.begin() +nDraglist );
			//	pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.DelLinkedObjectNode(nDraglist);
		}
		else
		{
			dtlist.erase( dtlist.begin() +nDraglist +1 );
			//	pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.DelLinkedObjectNode(nDraglist+1);
		}
	}
	else
	{//拖动的是主节点..不用理会,因为主节点总是存在第一个位置..
	}
}

void CLinkedObjectInfoView::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(m_bDragging)
	{
		m_bDragging = false;
		ReleaseCapture();
		m_pDragImage->DragLeave(this);
		m_pDragImage->EndDrag();

		if(m_pDragImage != NULL)
		{
			delete m_pDragImage;
			m_pDragImage = NULL;
		}
		if( (m_nDropIndex != -1) && (m_nDropIndex != m_nDragIndex) )
		{// 后插
			CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
			if (pDoc == NULL)
				return;
			pDoc->SetModifiedFlag(true);

			ModifyDTList(m_nDragIndex, m_nDropIndex);

/*
			CString strTemp = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 2);
			BOOL bMainNode = _wtoi(strTemp);

			if( !bMainNode )
			{//调整顺序...
				Nuclear::DTList& dtlist = pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.GetLinkedObjectList();

				CString strID = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 0);
				DWORD dwDragFileID = _wtoi(strID);
				int nDragIndex = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDragFileID);
			//	Nuclear::LkoTreeNode* pNode = pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.GetLinkedObjectNode(nDragIndex);

				strID = m_listCtrlLinkedObjectLevel.GetItemText(m_nDropIndex, 0);
				DWORD dwDropFileID = _wtoi(strID);
				int nDropIndex = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDropFileID);

				//在这里不能释放节点数据..
				if(nDropIndex == 0)
				{//插到主节点后..
					if(m_nDropIndex == 0)//主节点位于列表框的第一个节点..
					{
						nDropIndex = 0;
					}
					else
					{
						strID = m_listCtrlLinkedObjectLevel.GetItemText(m_nDropIndex-1, 0);
						dwDropFileID = _wtoi(strID);
						nDropIndex = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDropFileID);
					}
				}
				else
				{//不用重新计算nDropIndex
				}

				pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.InsertLinkedObjectNode(dtlist[nDragIndex], nDropIndex+1);//后插..
				if(nDropIndex > nDragIndex)
				{
					dtlist.erase( dtlist.begin() +nDragIndex );
					//	pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.DelLinkedObjectNode(nDragIndex);
				}
				else
				{
					dtlist.erase( dtlist.begin() +nDragIndex +1 );
					//	pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.DelLinkedObjectNode(nDragIndex+1);
				}
			}
			else
			{//拖动的是主节点..不用理会,因为主节点总是存在第一个位置..
			}
*/
			CString curSelFileName = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 0);
			DWORD curSelFileID = _wtoi(curSelFileName);

			CString fileName = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 1);
			CString strTemp = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 2);
			BOOL bMainNode = _wtoi(strTemp);
			strTemp = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 3);
			BOOL bMoveFollow = _wtoi(strTemp);

			strTemp = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 4);
			DWORD dwFlag = _wtoi(strTemp);

			CString strfmt = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 5);

			CString str;
			str.Format(L"%d", curSelFileID);
			m_listCtrlLinkedObjectLevel.InsertItem(m_nDropIndex+1, str);

			m_listCtrlLinkedObjectLevel.SetItemText(m_nDropIndex+1, 1, fileName);
			str.Format(_T("%d"),bMainNode);
			m_listCtrlLinkedObjectLevel.SetItemText(m_nDropIndex+1, 2, str);
			str.Format(_T("%d"), bMoveFollow);
			m_listCtrlLinkedObjectLevel.SetItemText(m_nDropIndex+1, 3, str);
			str.Format(_T("%d"), dwFlag);
			m_listCtrlLinkedObjectLevel.SetItemText(m_nDropIndex+1, 4, str);
			m_listCtrlLinkedObjectLevel.SetItemText(m_nDropIndex+1, 5, strfmt);

			if(m_nDropIndex > m_nDragIndex)
				m_listCtrlLinkedObjectLevel.DeleteItem(m_nDragIndex);
			else
			{
				m_listCtrlLinkedObjectLevel.DeleteItem(m_nDragIndex +1);
			}
		}
	}

	CFormView::OnLButtonUp(nFlags, point);
}

void CLinkedObjectInfoView::OnBnClickedCheckStop()
{
	UpdateData(true);

	int cnt = m_listCtrlLinkedObjectLevel.GetItemCount();
	if( cnt==0 )
	{
		m_bStop = false;
		MessageBox(L"当前编辑区还没有动态物件呢\n");
		UpdateData(false);
	}

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	pDoc->GetLinkedObjectEditorView()->SetLinkedObjectStopFlag( (m_bStop != 0) );
}

void CLinkedObjectInfoView::OnBnClickedButtonConversion()
{
	//这次转换是因为部分素材(img文件)改变了位置...从一个文件夹挪到了别的文件夹...
	
	CString strDefExt = _T("plt");
	CString strFilter = _T("(*.plt)|*.plt||");

	std::wstring strInitFolder = _T("");

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, strDefExt, NULL, dwFlags, strFilter, NULL);
	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
	filePath = dlg.GetPathName();
	int nPos = filePath.ReverseFind('\\');
	CString strPath = filePath.Left(nPos); //指定文件所在目录...

	std::vector<CString> VectorPLTFileName; //指定目录下的所有plt文件

	if (strPath.Right (1) != _T ("\\"))
		strPath += _T ("\\");
	CString filePathNoName = strPath;
	strPath += _T ("*.plt");

	HANDLE hFind;
	WIN32_FIND_DATA fd;

	if((hFind = ::FindFirstFile(strPath, &fd)) != INVALID_HANDLE_VALUE)
	{
		VectorPLTFileName.clear();

		if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
		{
			CString str = filePathNoName + fd.cFileName;
			VectorPLTFileName.push_back(str);
		}

		while (::FindNextFile (hFind, &fd)) 
		{
			if (!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
			{
				CString str = filePathNoName + fd.cFileName;
				VectorPLTFileName.push_back(str);
			}
		}
	}
	::FindClose(hFind);

	ConversionPLTFile(VectorPLTFileName);
}

void CLinkedObjectInfoView::ConversionPLTFile(const std::vector<CString>& VectorPLTFileName)
{
	TCHAR workdir[MAX_PATH];
	GetModuleFileName(NULL, workdir, MAX_PATH);
	std::wstring strWorkDir = workdir;
	int npos = (int)strWorkDir.rfind('\\');
	strWorkDir = std::wstring(strWorkDir, 0, npos);
	std::wstring sysFilePath = _T("\\res\\map\\elements\\");

	std::wstring strTemp = strWorkDir + sysFilePath;
	CString strRootPath = strTemp.c_str();

	int nCount = 0; //统计转换了多少个文件...

	int cnt = (int)VectorPLTFileName.size();
	for(int i=0; i<cnt; i++)
	{
		CString filePath = VectorPLTFileName.at(i);

		FILE *f = NULL;
		_tfopen_s(&f, filePath, _T("rb"));
		if( f == NULL )
		{
			MessageBox(filePath);
			continue;
		}

		Nuclear::PLinkedObject linkedobject;
		BOOL ret = linkedobject.Load(f) ? TRUE : FALSE;
		fclose(f);
		if(!ret)
		{
			MessageBox(filePath);
			continue;
		}

		int num = (int)linkedobject.m_vectorLinkedObjectInfo.size();
		bool bFlag = true;
		for(int j=0; j<num; j++)
		{
			std::wstring imgName = linkedobject.m_vectorLinkedObjectInfo.at(j).m_strPicName;
			
			std::vector<CString> fileFullPathVector; //存放完整路径
			int nres = FindFilesInSpecialPath(strRootPath, imgName.c_str(), fileFullPathVector);
			if(nres != 1)
			{
				CString str = L"\n文件存放在了多个位置...请清理\n";
				if(nres <1)
					str = L"\n文件不存在...请复制进来\n";
				str = strRootPath+imgName.c_str() +str;
				MessageBox(str);
				bFlag = false;
				break;
			}

			int len = fileFullPathVector.at(0).GetLength();
			int npos = fileFullPathVector.at(0).Find(L"\\elements\\");
			CString strKeyTemp = fileFullPathVector.at(0).Right(len-npos);
			std::wstring strKey = strKeyTemp;
			std::wstring TempKey = strKey;
			bool bres = PFS::CEnv::NativeFileNameToPFSFileName(TempKey, strKey);
			linkedobject.m_vectorLinkedObjectInfo.at(j).m_strPicName = strKey;
		}

		if(bFlag)
		{
			DeleteFile(filePath);//plt文件

			int nPos = filePath.ReverseFind('.');
			filePath = filePath.Left(nPos);
			filePath += L".lko";

			FILE *f = NULL; //lko文件
			_tfopen_s(&f, filePath, _T("wb"));
			if( f == NULL )
			{
				MessageBox(filePath);
				continue;
			}

			BOOL ret = linkedobject.Save(f) ? TRUE : FALSE;
			fclose(f);
			if(!ret)
			{
				MessageBox(filePath);
				continue;
			}

			nCount++;
		}
	}

	CString str;
	str.Format(_T("%d"), nCount);
	str += _T(" 个旧版本plt文件被转为新版本lko文件并存盘，老版本文件被删除");
	MessageBox(str, _T("提示"), MB_ICONINFORMATION);
}

int CLinkedObjectInfoView::FindFilesInSpecialPath(const CString& strSysPath, const CString& strFileName, std::vector<CString>& fileFullPathVector)
{//返回找到了几个
	CString strRootPath = strSysPath;
	if (strRootPath.Right(1) != _T("\\"))
		strRootPath += _T("\\");
	CString strPath = strRootPath + _T("*.*");

	HANDLE hFind;
	WIN32_FIND_DATA fd;
	int nCount = 0;

	if((hFind = ::FindFirstFile(strPath, &fd)) != INVALID_HANDLE_VALUE)
	{
		if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_HIDDEN))
		{
			if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
			{
				CString FileName = fd.cFileName;
				if(FileName.CompareNoCase(strFileName) == 0)
					fileFullPathVector.push_back(strRootPath +strFileName);
			}
			else
			{//目录
				CString strDir = (LPCTSTR) &fd.cFileName;
				if((strDir != _T(".")) && (strDir != _T("..")))
					FindFilesInSpecialPath(strRootPath +strDir, strFileName, fileFullPathVector);
			}
		}

		while(::FindNextFile(hFind, &fd)) 
		{
			if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_HIDDEN))
			{
				if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
				{
					CString FileName = fd.cFileName;
					if(FileName.CompareNoCase(strFileName) == 0)
						fileFullPathVector.push_back(strRootPath +strFileName);
				}
				else
				{
					//目录
					CString strDir = (LPCTSTR) &fd.cFileName;
					if((strDir != _T(".")) && (strDir != _T("..")))
						FindFilesInSpecialPath(strRootPath +strDir, strFileName, fileFullPathVector);
				}
			}
		}
	}
	::FindClose(hFind);

	return (int)fileFullPathVector.size();
}


#include <Shlwapi.h>
using namespace Nuclear; 

#include "..\engine\particlesystem\commonfuncs.h"

void CLinkedObjectInfoView::OnBnClickedButtonImgfilerename()
{
	//这次转换是因为一些素材(img文件)改名了...名字中不再有中文字符
	//2010-10-13 因为灯光层物件所在目录改变了而修改... /2/=/ /3/=/ 等等移到上层目录之类的改变
	//1. 改名规则初始化..
	//2. 字符串替换...
	if(!m_bInitRenameRule)
	{
		MessageBox(L"请先初始化命名规则..\n");
		return;
	}

	TCHAR workdir[MAX_PATH];
	GetModuleFileName(NULL, workdir, MAX_PATH);
	std::wstring strWorkDir = workdir;
	int npos = (int)strWorkDir.rfind('\\');
	strWorkDir = std::wstring(strWorkDir, 0, npos);
	std::wstring sysFilePath = _T("\\res\\map\\");
	std::wstring strSysPath = strWorkDir + sysFilePath;

	CString strDefExt = _T("lko");
	CString strFilter = _T("(*.lko)|*.lko||");
	std::wstring strInitFolder = strSysPath + _T("linkedobjects\\*.lko");

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, strDefExt, strInitFolder.c_str(), dwFlags, strFilter, NULL);
	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
	filePath = dlg.GetPathName();
	int nPos = filePath.ReverseFind('\\');
	CString strPath = filePath.Left(nPos); //指定文件所在目录...

	std::vector<CString> VectorLKOFileName; //指定目录下的所有lko文件

	if (strPath.Right (1) != _T ("\\"))
		strPath += _T ("\\");
	CString filePathNoName = strPath;
	strPath += _T("*.lko");

	HANDLE hFind;
	WIN32_FIND_DATA fd;

	if((hFind = ::FindFirstFile(strPath, &fd)) != INVALID_HANDLE_VALUE)
	{
		VectorLKOFileName.clear();

		if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
		{
			CString str = filePathNoName + fd.cFileName;
			VectorLKOFileName.push_back(str);
		}

		while (::FindNextFile (hFind, &fd)) 
		{
			if (!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
			{
				CString str = filePathNoName + fd.cFileName;
				VectorLKOFileName.push_back(str);
			}
		}
	}
	::FindClose(hFind);

	ConversionLKOFile(VectorLKOFileName);
}

void CLinkedObjectInfoView::ConversionLKOFile(const std::vector<CString>& VectorLKOFileName)
{ 
	//把lko文件中的img文件名按照规则做替换....

	int nCount = 0; //统计转换了多少个文件...

	int cnt = (int)VectorLKOFileName.size();
	for(int i=0; i<cnt; i++)
	{
		CString filePath = VectorLKOFileName.at(i);

		FILE *f = NULL;
		_tfopen_s(&f, filePath, _T("rb"));
		if(f==NULL)
		{
			MessageBox(filePath);
			continue;
		}

		Nuclear::PLinkedObject linkedobject;
		BOOL ret = linkedobject.Load(f) ? TRUE : FALSE;
		fclose(f);
		if(!ret)
		{
			MessageBox(filePath);
			continue;
		}

		int num = (int)linkedobject.m_vectorLinkedObjectInfo.size();
		bool bFlag = true;
		for(int j=0; j<num; j++)
		{
			std::wstring imgName = linkedobject.m_vectorLinkedObjectInfo.at(j).m_strPicName;

			//替换...把m_valuemap中的值从头到尾替换一遍
			std::map<std::wstring, std::wstring>::iterator itbegin = m_valuemap.begin();
			std::map<std::wstring, std::wstring>::iterator itend = m_valuemap.end();
			for(; itbegin!=itend; ++itbegin)
			{
				imgName = Nuclear::replace(imgName, itbegin->first, itbegin->second);
			}
			linkedobject.m_vectorLinkedObjectInfo.at(j).m_strPicName = imgName;
		}

		_tfopen_s(&f, filePath, _T("wb"));
		if( f == NULL )
		{
			MessageBox(filePath);
			continue;
		}
		ret = linkedobject.Save(f) ? TRUE : FALSE;
		fclose(f);
		if(!ret)
		{
			MessageBox(filePath);
			continue;
		}

		nCount++;
	}

	CString str;
	str.Format(_T("%d"), nCount);
	str += _T(" 个旧版本lko文件被转为新版本lko文件");
	MessageBox(str, _T("提示"), MB_ICONINFORMATION);
}

void CLinkedObjectInfoView::OnBnClickedButtonInitrenamerule()
{
	CString strDefExt = _T("txt");
	CString strFilter = _T("(*.txt)|*.txt||");

	std::wstring strInitFolder = _T("");

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, strDefExt, NULL, dwFlags, strFilter, NULL);
	dlg.DoModal();
	CString strFilePath = dlg.GetFileName();
	if( strFilePath.IsEmpty() )
		return;
	strFilePath = dlg.GetPathName();

//	CString strFilePath = L"E:\\命名规则.txt";

	HANDLE hFile = CreateFile( strFilePath.GetBuffer(strFilePath.GetLength()), GENERIC_READ, FILE_SHARE_READ, NULL,
		OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, 0);
	if ( INVALID_HANDLE_VALUE == hFile )
		return;

	DWORD dwFileSize = GetFileSize(hFile, NULL);
	TCHAR* pFileBuffer = new TCHAR[dwFileSize];
	DWORD dwNumber;
	ReadFile(hFile, pFileBuffer, dwFileSize, &dwNumber, NULL);
	CloseHandle(hFile);

	TCHAR buf[256];
	TCHAR leftside[256];
	TCHAR rightside[256];

	m_valuemap.clear();

	int nFirstRow = 0;
	memset(buf, 0, sizeof(TCHAR)*256);
	int j = 0;
	int count = 0; //超过1000行的文件是不合法的..
	for(int i=0; ( i<(int)(dwFileSize/sizeof(TCHAR)) ) && (count<1000); i++)
	{
		TCHAR ch = *(pFileBuffer+i);
		if(ch != '\n')
			buf[j++] = ch;
		else
		{
			//一行读完了..
			if( wcslen(buf) >= 2 )
			{//丢弃空行..
				nFirstRow++;
				if(nFirstRow==1) //第一个非空行确定版本号
				{
					int nVer = 1;
				}

				memset(leftside, 0, sizeof(leftside));
				memset(rightside, 0, sizeof(rightside));

				PluckFirstField(buf, leftside, sizeof(leftside), L"=");
				PluckFirstField(buf, rightside, sizeof(rightside), L"\n");

				//是否重复...
				if(m_valuemap.find(leftside) != m_valuemap.end())
				{
					delete[] pFileBuffer;
					MessageBox(_T("初始化规则失败....相同的汉字取了不同的字母名\n"));
					return;
				}

				//空格...
				LPCTSTR pszTrimChars = L" ";
				BOOL bRes = StrTrim(leftside, pszTrimChars);
				bRes = StrTrim(rightside, pszTrimChars);
				CString tempStr = rightside;
				int len = tempStr.GetLength();
				CString tcharTemp = tempStr.Left(len-1);
				m_valuemap.insert(std::make_pair(std::wstring(leftside), std::wstring(tcharTemp)));

				tcharTemp = tcharTemp.Right(1);
				int a=0;
			}

			j = 0;
			memset(buf, 0, sizeof(TCHAR)*256);
			count++;
		}
	}
	if(count>1000) //命名规则文件不可能那么大..
	{
		delete[] pFileBuffer;
		MessageBox(_T("不可识别的文件\n"));
		return;
	}

	delete[] pFileBuffer;

	m_bInitRenameRule = true;
	GetDlgItem(IDC_BUTTON_IMGFileRename)->EnableWindow(m_bInitRenameRule);
	GetDlgItem(IDC_BUTTON_RenameFilesAndDir)->EnableWindow(m_bInitRenameRule);
	GetDlgItem(IDC_BUTTON_RenameMapFile)->EnableWindow(m_bInitRenameRule);
}



int s_cnt = 0; //统计转换了多少个文件

void CLinkedObjectInfoView::OnBnClickedButtonRenamefilesanddir()
{
	//指定文件所在文件夹以及子目录的所有文件和文件夹改名...
	if(!m_bInitRenameRule)
	{
		MessageBox(L"请先初始化命名规则..\n");
		return;
	}

	TCHAR workdir[MAX_PATH];
	GetModuleFileName(NULL, workdir, MAX_PATH);
	std::wstring strWorkDir = workdir;
	int npos = (int)strWorkDir.rfind('\\');
	strWorkDir = std::wstring(strWorkDir, 0, npos);
	std::wstring sysFilePath = _T("\\res\\");
	std::wstring strSysPath = strWorkDir + sysFilePath;

	CString strDefExt = _T("");
	CString strFilter = _T("(*.*)|*.*||");
	std::wstring strInitFolder = strSysPath + _T("*.*");

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST; //strInitFolder.c_str() //NULL
	CFileDialog dlg(true, strDefExt, NULL, dwFlags, strFilter, NULL);
	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
	filePath = dlg.GetPathName();
	int nPos = filePath.ReverseFind('\\');
	CString strPath = filePath.Left(nPos); //指定文件所在目录...

	m_vectorRenameDirs.clear();
	m_vectorRenameFiles.clear();

	s_cnt = 0;
	RenameAllFilesAndDir(strPath); //指定目录下的所有文件及其文件夹改名

	CString str;
	str.Format(_T("%d"), s_cnt);
	str += _T(" 个文件和文件夹被改名");
	MessageBox(str, _T("提示"), MB_ICONINFORMATION);

/*
	int cnt = (int)m_vectorRenameFiles.size();
	for(int i=0; i<cnt; i++)
	{
		CString oldName = m_vectorRenameFiles.at(i).oldName;
		CString newName = m_vectorRenameFiles.at(i).newName;
		BOOL bres = MoveFile(oldName, newName);
//		CFile::Rename(oldName, newName);
	//	int nres = _wrename(oldName, newName);
		int a=0;
		//EACCES  ENOENT  EINVAL
	}
*/
}

void CLinkedObjectInfoView::RenameAllFilesAndDir(const CString& strPath)
{
	CFileFind filefinder;
	CString szDir = strPath;

	if(szDir.Right(1) != "\\")
		szDir += "\\";

	szDir += "*.*";

	BOOL res = filefinder.FindFile(szDir);
	while(res)
	{
		res = filefinder.FindNextFile();
		if(filefinder.IsDirectory() && !filefinder.IsDots())
		{
			//如果是一个子目录，并且子目录文件也改名
			//用递归继续往深一层找
			bool bModify = false;
			CString strPath = filefinder.GetFilePath();

			CString oldPath = strPath;
			CString newPath = strPath;

			int nPos = newPath.ReverseFind('\\');
			int len = newPath.GetLength();
			CString newNameTemp = newPath.Right(len-nPos-1); //文件名
			std::wstring newName = newNameTemp;

			std::map<std::wstring, std::wstring>::iterator itbegin = m_valuemap.begin();
			std::map<std::wstring, std::wstring>::iterator itend = m_valuemap.end();
			for(; itbegin!=itend; ++itbegin)
			{
			//	newName = Nuclear::replace(newName, itbegin->first, itbegin->second); //第一次转换
				//第二次只改文件夹的名字...
				if(newName == itbegin->first)
				{
					newName = itbegin->second;
					bModify = true;
					break;
				}
			}

			//空格...
			LPCTSTR pszTrimChars = L" ";
			CString strtemp = newName.c_str();
			BOOL bRes = StrTrim(strtemp.GetBuffer(strtemp.GetLength()), pszTrimChars);
			newName = strtemp;

			RENAMESTR renamestr;
			renamestr.oldName = oldPath;
			newPath = newPath.Left(nPos);
			if(newPath.Right(1) != "\\")
				newPath += "\\";
			newPath += newName.c_str();

			renamestr.newName = newPath;
			m_vectorRenameDirs.push_back(renamestr);

			if(bModify)
			{
				BOOL bres = MoveFile(renamestr.oldName, renamestr.newName);
				if(!bres)
					MessageBox(newPath);

				s_cnt++;
			}

			RenameAllFilesAndDir(newPath); //strPath
		}
		else if(!filefinder.IsDirectory() && !filefinder.IsDots())
		{
			/*
			CString strPath = filefinder.GetFilePath();

			CString oldPath = strPath;
			CString newPath = strPath;

			int nPos = newPath.ReverseFind('\\');
			int len = newPath.GetLength();
			CString newNameTemp = newPath.Right(len-nPos-1); //文件名
			std::wstring newName = newNameTemp;

			std::map<std::wstring, std::wstring>::iterator itbegin = m_valuemap.begin();
			std::map<std::wstring, std::wstring>::iterator itend = m_valuemap.end();
			for(; itbegin!=itend; ++itbegin)
			{
				newName = Nuclear::replace(newName, itbegin->first, itbegin->second);
			}
			//空格.. 类似“复件 ”这样的文件名处理
			LPCTSTR pszTrimChars = L" ";
			CString strtemp = newName.c_str();
			BOOL bRes = StrTrim(strtemp.GetBuffer(strtemp.GetLength()), pszTrimChars);
			newName = strtemp;

			RENAMESTR renamestr;
			renamestr.oldName = oldPath;
			newPath = newPath.Left(nPos);
			if(newPath.Right(1) != "\\")
				newPath += "\\";
			newPath += newName.c_str();

			renamestr.newName = newPath;
			m_vectorRenameFiles.push_back(renamestr);

			BOOL bres = MoveFile(renamestr.oldName, renamestr.newName);
			if(!bres)
			{
				MessageBox(newPath);
			}
			s_cnt++;
			*/
		}
	}
	filefinder.Close();//关闭
}


#include "..\engine\map\pmap.h"

void CLinkedObjectInfoView::OnBnClickedButtonRenamemapfile()
{
	s_cnt = 0; //统计修改了几个地方

	if(!m_bInitRenameRule)
	{
		MessageBox(L"请先初始化命名规则..\n");
		return;
	}

	TCHAR workdir[MAX_PATH];
	GetModuleFileName(NULL, workdir, MAX_PATH);
	std::wstring strWorkDir = workdir;
	int npos = (int)strWorkDir.rfind('\\');
	strWorkDir = std::wstring(strWorkDir, 0, npos);
	std::wstring sysFilePath = _T("\\res\\map\\");
	std::wstring strSysPath = strWorkDir + sysFilePath;

	CString strDefExt = _T("mrmp");
	CString strFilter = _T("(*.mrmp)|*.mrmp||");
	std::wstring strInitFolder = strSysPath + _T("*.mrmp");

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, strDefExt, strInitFolder.c_str(), dwFlags, strFilter, NULL);
	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
	filePath = dlg.GetPathName();


	FILE *f = NULL;
	_tfopen_s(&f, filePath, _T("rb"));
	if( f == NULL )
	{
		MessageBox(filePath);
		return;
	}
	Nuclear::PMap map;
	fseek(f, 0, SEEK_END);
	int len = ftell(f);
	fseek(f, 0, 0);
	void *buf = Nuclear::XAlloc(len);
	Nuclear::XOStream outos;
	if( buf && 0 != Nuclear::FRead(f, buf, len) )
	{
		Nuclear::XIStream os(Nuclear::XBuffer(buf, len));
		map.unmarshal(os);
		char a;
		while (!os.eos())
		{
			os >> a;
			outos << a;
		}
		Nuclear::XFree(buf);
	} else {
		Nuclear::XFree(buf);
		MessageBox(filePath);
		return;
	}
	fclose(f);

	Nuclear::SubMap::ObjNameMap::iterator it_obj;
	Nuclear::SubMap::ObjNameMap::iterator itend_obj;
	for (int type=0;type<PMap::MAP_OBJ_COUNT;++type)
	{
		Nuclear::SubMap::ObjNameMap& tmpNameMap = map.m_objs[type].GetObjFileNameMap();
		it_obj = tmpNameMap.begin();
		itend_obj = tmpNameMap.end();
		for(; it_obj != itend_obj; ++it_obj)
		{
			std::wstring imgName = it_obj->second;
			std::map<std::wstring, std::wstring>::iterator itbegin = m_valuemap.begin();
			std::map<std::wstring, std::wstring>::iterator itend = m_valuemap.end();
			for(; itbegin!=itend; ++itbegin)
			{
				imgName = Nuclear::replace(imgName, itbegin->first, itbegin->second);
			}
			it_obj->second = imgName;
			s_cnt++;
		}
	}
	
	_tfopen_s(&f, filePath, _T("wb"));
	if( f == NULL ) 
	{
		MessageBox(filePath);
		return;
	}

	BOOL ret = map.Save(f) ? TRUE : FALSE;
	if (ret)
	{
		int size = (int)outos.size();
		buf = Nuclear::XAlloc(size);
		if( buf == NULL ) 
		{
			MessageBox(L"申请空间失败");
			return;
		}
		memcpy(buf, outos.begin(), size);
		Nuclear::FWrite(f, (const void*)buf, size);
		Nuclear::XFree(buf);
		fclose(f);
	} else {
		fclose(f);
		MessageBox(filePath);
		return;
	}


	//
	//if( f == NULL )
	//{
	//	MessageBox(filePath);
	//	return;
	//}
	//ret = map.Save(f) ? TRUE : FALSE;
	//fclose(f);
	//if(!ret)
	//{
	//	MessageBox(filePath);
	//	return;
	//}

	CString str;
	str.Format(_T("%d"), s_cnt);
	str += _T(" 个地方被替换");
	MessageBox(str, _T("提示"), MB_ICONINFORMATION);
}

void CLinkedObjectInfoView::OnBnClickedButtonRefreshmaxbox()
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	pDoc->GetLinkedObjectEditorView()->RefreshLinkedObject();
}

void CLinkedObjectInfoView::OnBnClickedCheckDragflag()
{
	UpdateData(true);

	int cnt = m_listCtrlLinkedObjectLevel.GetItemCount();
	if( cnt==0 )
	{
		m_bDragFlag = false;
		MessageBox(L"当前还没有可编辑的动态物件呢\n");
		UpdateData(false);
	}

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	pDoc->GetLinkedObjectEditorView()->SetLinkedObjectDragFlag( (m_bDragFlag != 0) );
}

void CLinkedObjectInfoView::SetDragFlag(BOOL bDragFlag)
{
	m_bDragFlag = bDragFlag;
	UpdateData(false);
}

void CLinkedObjectInfoView::OnMenucopyfilename()
{
	// 提取所选列表项各列类容
	CString strfilename;
	strfilename = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);

	//复制物件名称到剪贴板
	LPTSTR pText,pGlobal;  
	HGLOBAL hGlobal;  
	pText=(LPTSTR)(LPCTSTR)strfilename;  
	if(OpenClipboard())  
	{  
		EmptyClipboard();  
		hGlobal=GlobalAlloc(GMEM_DDESHARE, 2*strfilename.GetLength()+sizeof(WCHAR));  
		pGlobal = (TCHAR*)GlobalLock(hGlobal);  
		lstrcpy(pGlobal, pText);  
		GlobalUnlock(hGlobal);  
		SetClipboardData(CF_UNICODETEXT, hGlobal);  
		CloseClipboard();
	}
}

int CALLBACK CLinkedObjectInfoView::CompareFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort)
{
	CListCtrl* pListCtrl = &(((CLinkedObjectInfoView*)lParamSort)->m_ListCtrl);  
	CString strItem1 = pListCtrl->GetItemText((int)lParam1, ((CLinkedObjectInfoView*)lParamSort)->m_nSortColIndex);
	CString strItem2 = pListCtrl->GetItemText((int)lParam2, ((CLinkedObjectInfoView*)lParamSort)->m_nSortColIndex);

	TRACE(L"lParam1 = %d %s compare with lParam2 = %d %s \n", lParam1, strItem1, lParam2, strItem2);  
	return _tcscmp(strItem2, strItem1) *((CLinkedObjectInfoView*)lParamSort)->m_nSortFlag;   
}   

void CLinkedObjectInfoView::OnLvnColumnclickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码

	//m_nSortColIndex 成员变量，要排序的列号
	m_nSortColIndex = pNMLV->iSubItem;
	m_nSortFlag>0 ? m_nSortFlag=-1 : m_nSortFlag=1; //正序、逆序

	m_ListCtrl.SortItems((PFNLVCOMPARE)(CLinkedObjectInfoView::CompareFunc), (LPARAM)this);

	for(int i=0; i<m_ListCtrl.GetItemCount(); i++)  
		m_ListCtrl.SetItemData(i, i);

	*pResult = 0;
}

void CLinkedObjectInfoView::OnBnClickedCheckpreviewlko()
{
	UpdateData(true);

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	pDoc->GetLinkedObjectEditorView()->SetLinkedObjectPreviewFlag( (m_bPreviewLKO != 0) );
}

#include "..\engine\common\pfsutil.h"
//#include <algorithm>

void CLinkedObjectInfoView::OnBnClickedButtonRefreshcentpos()
{
	//把指定文件列表中lko底座中心重置
	//全部从主节点中获取覆盖现有数据...

// 	TCHAR workdir[MAX_PATH];
// 	GetModuleFileName(NULL, workdir, MAX_PATH);
// 	std::wstring strWorkDir = workdir;
// 	int npos = (int)strWorkDir.rfind('\\');
// 	strWorkDir = std::wstring(strWorkDir, 0, npos);
// 	std::wstring sysFilePath = _T("\\res");
// 	std::wstring strSysPath = strWorkDir + sysFilePath;


	CString strDefExt = _T("xml");
	CString strFilter = _T("(*.xml)|*.xml||");

	std::wstring strInitFolder = _T("");

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, strDefExt, NULL, dwFlags, strFilter, NULL);
	dlg.DoModal();
	CString strFilePath = dlg.GetFileName();
	if( strFilePath.IsEmpty() )
		return;
	strFilePath = dlg.GetPathName();

	XMLIO::CFileReader fr;
	int nError = fr.OpenNativeFile(strFilePath.GetBuffer());
	if( XMLIO::EC_SUCCESS != nError)
		return;

	XMLIO::CINode root;
	if( ! fr.GetRootNode(root))
		return;

	XMLIO::CNodeList nl;
	root.GetChildren(nl);

	std::wstring strVal;
	std::wstring lkoFilePath;

	for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
	{
		XMLIO::CINode& node = *it;
		if( L"img2lko" == node.GetName() )
		{
			if( node.GetAttribute(L"dst", strVal) )
			{
				std::wstring fileEX = strVal.substr(strVal.length()-4);
				if(fileEX != L".lko")
				{
					continue;
				}
				//读主节点底座重心来更新lko文件
				std::transform(strVal.begin(), strVal.end(), strVal.begin(), ::towlower);

				bool bres = PFS::CEnv::PFSFileNameToNativeFileName(strVal, lkoFilePath);
			//	lkoFilePath = strSysPath + strVal;

				ResetLKOFileCentPos(lkoFilePath);
			}
		}
	}
	fr.CloseFile();
}

bool CLinkedObjectInfoView::ResetLKOFileCentPos(std::wstring filePath)
{
	FILE *lkofile = NULL;
	_tfopen_s(&lkofile, filePath.c_str(), _T("rb"));
	if(lkofile == NULL)
	{
		MessageBox(filePath.c_str());
		return false;
	}

	Nuclear::PLinkedObject linkedobject;
	BOOL ret = linkedobject.Load(lkofile) ? TRUE : FALSE;
	fclose(lkofile);
	if(!ret)
	{
		MessageBox(filePath.c_str());
		return false;
	}

	bool bLightLKO = true;
	int nIndex = linkedobject.GetMainNodeID();
	std::wstring strPicName = linkedobject.m_vectorLinkedObjectInfo[nIndex].m_strPicName;

	std::transform(strPicName.begin(), strPicName.end(), strPicName.begin(), ::towlower);

	linkedobject.m_vectorLinkedObjectInfo[nIndex].m_strPicName = strPicName;

	std::wstring strImgFileName; //主节点的完整本地路径
	bool bres = PFS::CEnv::PFSFileNameToNativeFileName(strPicName, strImgFileName);

	FILE *imgfile = NULL;
	_tfopen_s(&imgfile, strImgFileName.c_str(), _T("rb"));
	if( imgfile == NULL )
	{
		MessageBox(strImgFileName.c_str());
		return FALSE;
	}

	Nuclear::PImg imgObject;
	imgObject.ResetImgObject(); //打开老版本文件..提示保存成新版本文件时候的bug..
	//比如：某些参数如mask信息在老版本中没有..那么就会导致打开老版本保存新版本时候一直重复覆盖以前的参数..
	ret = imgObject.Load(imgfile);
	fclose(imgfile);
	if(!ret)
		return false;

	linkedobject.m_ptCentPos = imgObject.CenterPoint;
	for(int i=0; i<4; i++)
		linkedobject.m_ptBasePos[i] = imgObject.pos[i];

	_tfopen_s(&lkofile, filePath.c_str(), _T("wb"));
	ret = linkedobject.Save(lkofile);
	fclose(lkofile);
	if(!ret)
	{
		MessageBox(filePath.c_str());
		return false;
	}
	return true;
}
