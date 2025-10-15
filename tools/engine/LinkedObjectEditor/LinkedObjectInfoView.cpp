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
, m_bDragFlag(TRUE) //�Ƿ������϶�--�Ҳ�༭�������
, m_bPreviewLKO(FALSE)
{
	m_pRender = NULL;
//	m_hpicHandle = 0;
	m_vectorPicID.clear();
	m_dwBKcolor = 0xffa0a0a0;
	m_nCurSelItem = 0;

	m_pDragImage = NULL;	//��ҷͼ��
	m_bDragging = false;	//�Ƿ�������ҷ
	m_nDragIndex = -1;	//��ʼ��ҷ��λ��
	m_nDropIndex = -1;	//��ҷ������
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
	m_ListCtrl.InsertColumn(0, L"�ļ�����", LVCFMT_LEFT, 160);
	m_ListCtrl.InsertColumn(1, L"�ļ���С", LVCFMT_LEFT, 80);
	m_ListCtrl.InsertColumn(2, L"����ʱ��", LVCFMT_LEFT, 80);
	m_ListCtrl.InsertColumn(3, L"�ļ�����", LVCFMT_LEFT, 60);

	//	m_comboDriverList.GetLBText(0, m_strPath);

	m_listCtrlLinkedObjectLevel.SetFont(&m_font);

	m_listCtrlLinkedObjectLevel.SetExtendedStyle(LVS_EX_FULLROWSELECT | LVS_EX_GRIDLINES); // | LVS_EX_CHECKBOXES

	m_listCtrlLinkedObjectLevel.SetBkColor(RGB(200,200,200));
	m_listCtrlLinkedObjectLevel.SetTextColor(RGB(0,0,0));
	m_listCtrlLinkedObjectLevel.SetTextBkColor(RGB(200,200,200));
	m_listCtrlLinkedObjectLevel.InsertColumn(0, L"���ID", LVCFMT_LEFT, 60);
	m_listCtrlLinkedObjectLevel.InsertColumn(1, L"�������", LVCFMT_LEFT, 200);
	m_listCtrlLinkedObjectLevel.InsertColumn(2, L"���ڵ�", LVCFMT_LEFT, 70);
	m_listCtrlLinkedObjectLevel.InsertColumn(3, L"�˶�����", LVCFMT_LEFT, 70);
	m_listCtrlLinkedObjectLevel.InsertColumn(4, L"�ƹ�", LVCFMT_LEFT, 50);
	m_listCtrlLinkedObjectLevel.InsertColumn(5, L"img��ʽ", LVCFMT_LEFT, 60);
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

		m_ListTypeMap.clear(); //0�ļ� 1�ļ��� 2��һ��Ŀ¼
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

	FileInfo fileInfo;//���һ��ָ���ϲ�Ŀ¼��...
	fileInfo.strFileName = _T("..");
	fileInfo.strFileSize = _T("<DIR>");
	fileInfo.strFileLastWriteTime = _T("�ϲ�Ŀ¼");
	m_FileInfoList.push_back(fileInfo);

	m_ListTypeMap.insert(std::make_pair(_T(".."), 1));//0�ļ� 1�ļ���

	//��ʾ�б�
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
	{//�ļ� bmp tga img
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
	// else //1�ļ���

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


	m_ListTypeMap.insert(std::make_pair(strFileName, fileType));//0�ļ� 1�ļ���

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
		strFileNameEX = L"�ļ���";

	m_ListCtrl.SetItemText(0,3, strFileNameEX);

	return true;
}

void CLinkedObjectInfoView::OnLvnItemchangedListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnNMClickListFileinfo(pNMHDR, pResult);
	*pResult = 0;
}

void CLinkedObjectInfoView::OnNMClickListFileinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);

	// TODO: �ڴ���ӿؼ�֪ͨ����������
	POSITION pos = m_ListCtrl.GetFirstSelectedItemPosition();
	int nCurSelListIndex = m_ListCtrl.GetNextSelectedItem(pos);  // �õ���Ŀ����
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
	//���ļ���ʾ...
	CString strFileNameEX = strFileName.Right(3);	
	bool isIMG = strFileNameEX.CompareNoCase(L"set")==0 ? true:false;
	bool isLKO = strFileNameEX.CompareNoCase(L"lko")==0 ? true:false;

	if( !(isIMG || isLKO) )
	{
		return; //�߼�����...
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

		//��img�ļ���ʼ��
		//�����ļ�����������ʾ...
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
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;
// 
// 	if(pDoc->IsModified())
// 	{
// 		CString strInfo = _T("�Ƿ񱣴浱ǰ�ļ����޸ģ�");
// 		int res = MessageBox(strInfo, _T("��ʾ"), MB_ICONQUESTION|MB_YESNOCANCEL);
// 		if(res == IDYES) //IDYES
// 		{
// 			pDoc->OnSaveDocument(m_strFileName_old);
// 		}
// 		else if(res == IDCANCEL)
// 		{
// 			return;
// 		}
// 		else
// 		{// res == IDNO ��������Ĵ���...
// 		}
// 	}

	POSITION pos = m_ListCtrl.GetFirstSelectedItemPosition();
	int nCurSelListIndex = m_ListCtrl.GetNextSelectedItem(pos);  // �õ���Ŀ����
	CString strFileName = m_ListCtrl.GetItemText(nCurSelListIndex, 0);

	int fileType;
	std::map<CString, int>::const_iterator it = m_ListTypeMap.find(strFileName);
	if( it == m_ListTypeMap.end() )
		return;
	else
		fileType = it->second;

	if(fileType==0)
	{//�ļ�
		if(m_bPreviewLKO)
		{
			return; //Ԥ��״̬...
		}
		CString filePath = m_strPath+strFileName;

		CString strFileNameEX = strFileName.Right(3);	
		bool isIMG = strFileNameEX.CompareNoCase(L"set")==0 ? true:false;
		if(!isIMG)
		{
			return; //����img�ļ�����ͨ�����ַ�ʽ����ӱ༭...
		}

		//����ļ�·���Ƿ���ϱ༭�淶�������ϵĸ�����ʾ
		int npos = filePath.Find(L"\\elements\\");

		CString resonablePath = filePath;
		if(npos != -1)
			resonablePath = filePath.Left(npos+10);

		//�ж�·���Ƿ�Ϸ������������ֻ�е�ǰelementsĿ¼�µ�..���Խ���Ŀ¼:)
		//ָ���˱��Ŀ¼�µ��ļ�Ҳ�ǿ��Ե�...���Ǹ������ѣ��༭֮��Ҫ�Ѷ�Ӧ��img�Ŷ�λ�ã�
		if(resonablePath.CompareNoCase(m_strSysWorkPath) != 0)
		{
			CString strInfo;
			strInfo = L"��ǰ�ز�����·��: ";
			strInfo += resonablePath;
			strInfo += L"      \n";
			strInfo += L"�����ϱ༭ϰ��, �Ƿ�����༭?";
			if(AfxMessageBox(strInfo, MB_YESNO) == IDNO)
				return;
		}

		int bres = pDoc->GetLinkedObjectEditorView()->AddPicMap(filePath);
	}
	else
	{//�ļ���
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

	COLORREF color = dlg.GetColor(); //BGR --> ARGB (alphaֵΪ255, ����ɫ������)
	m_dwBKcolor = (0xffffffff & 0xff000000) | (color & 0x000000ff)<<16 | (color & 0x0000ff00) | (color & 0x00ff0000)>>16;

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	pDoc->GetLinkedObjectEditorView()->SetBKColor(m_dwBKcolor);
	pDoc->UpdateAllViews(NULL);
}

void CLinkedObjectInfoView::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: �ڴ������Ϣ�����������/�����Ĭ��ֵ

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
	{ //����Ĳ��ǹ�����..	
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
	// TODO: �ڴ������Ϣ�����������/�����Ĭ��ֵ

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
	// TODO: �ڴ���ӿؼ�֪ͨ����������

	if(m_bSetLinkedObjectPosCenter)
	{
		MessageBox(L"Ŀǰ�����޸����ĵ���״̬...����Ӧ�Ҽ��˵�..");
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
		m_nCurSelItem = m_listCtrlLinkedObjectLevel.GetNextSelectedItem(pos);  // �õ���Ŀ����

		if(m_nCurSelItem != -1)
		{
			CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
			if (pDoc == NULL)
				return;

			pDoc->GetLinkedObjectEditorView()->m_curSelFileID = _wtoi( m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0) );


			//������˳��Ҫ�޸�pplant����˳���ǻ��Ʋ�ι�ϵ..
			pDoc->SetModifiedFlag(true);
		}
	}

	*pResult = 0;
}

void CLinkedObjectInfoView::OnMenuPrelevel()
{
	m_listCtrlLinkedObjectLevel.SetFocus();
	// ����һ��
	if(m_nCurSelItem == -1)	  //���û��ѡ��ͷ���
	{
		MessageBox(	L"��ѡ��һ��������!", L"��ʾ", MB_ICONINFORMATION);
		return;  
	}

	if(m_nCurSelItem == 0)  // �ж���ѡ���Ƿ�λ������
	{
		MessageBox(L"�Ѿ�λ�����ϲ�!", L"��ʾ", MB_ICONINFORMATION);
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
	// ��ȡ��ѡ�б����������
	CString str1, str2, str3, str4, str5, str6;
	str1 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	str2 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);
	str3 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 2);
	str4 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 3);
	str5 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 4);
	str6 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 5);

	// ɾ����ѡ�б���
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	// ��m_nCurSelItem-1λ�ô�����������ɾ�б���ĸ�������	
	m_listCtrlLinkedObjectLevel.InsertItem(m_nCurSelItem-1, str1);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem-1, 5, str6);
	m_nCurSelItem--;

	// ʹ��m_nCurSelItem-1λ�ô���Ŀ������ʾ����ý���
	UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
	m_listCtrlLinkedObjectLevel.SetItemState(m_nCurSelItem, flag, flag);
}

void CLinkedObjectInfoView::OnMenuNextlevel()
{
	// ����һ��
	m_listCtrlLinkedObjectLevel.SetFocus();
	if(m_nCurSelItem == -1)	  //���û��ѡ��ͷ���
	{
		MessageBox(L"��ѡ��һ��������!", L"��ʾ", MB_ICONINFORMATION);
		return;  
	}

	if(m_nCurSelItem == m_listCtrlLinkedObjectLevel.GetItemCount()-1)
	{
		MessageBox(L"�Ѿ�λ�����²�!", L"��ʾ", MB_ICONINFORMATION);
		return;
	}

	int nDragIndex = m_nCurSelItem;
	int nDropIndex = m_nCurSelItem-1;
	ModifyDTList(nDragIndex, nDropIndex);


	// ��ȡ��ѡ�б����������
	CString str1, str2, str3, str4, str5, str6;
	str1 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	str2 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);
	str3 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 2);
	str4 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 3);
	str5 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 4);
	str6 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 5);

	// ɾ����ѡ�б���
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	// ��m_nCurSelItem+1λ�ô�����������ɾ�б���ĸ�������	
	m_listCtrlLinkedObjectLevel.InsertItem(m_nCurSelItem+1, str1);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem+1, 5, str6);

	m_nCurSelItem++;

	// ʹ��m_nCurSelItem+1λ�ô���Ŀ������ʾ����ý���
	UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
	m_listCtrlLinkedObjectLevel.SetItemState(m_nCurSelItem, flag, flag);
}

void CLinkedObjectInfoView::OnMenuFirstlevel()
{
	// �Ƶ���һ��
	m_listCtrlLinkedObjectLevel.SetFocus();
	if(m_nCurSelItem == -1)	  //���û��ѡ��ͷ���
	{
		MessageBox(	L"��ѡ��һ������!", L"��ʾ", MB_ICONINFORMATION);
		return;  
	}

	if(m_nCurSelItem == 0)  // �ж���ѡ���Ƿ�λ������
	{
		MessageBox(L"�Ѿ�λ�ڵ�һ��!", L"��ʾ", MB_ICONINFORMATION);
		return;
	}

	int nDragIndex = m_nCurSelItem;
	int nDropIndex = 0;
	ModifyDTList(nDragIndex, nDropIndex); //������..����ֻ�Ƶ��˵ڶ���λ��...

	// ��ȡ��ѡ�б����������
	CString str1, str2, str3, str4, str5, str6;
	str1 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	str2 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);
	str3 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 2);
	str4 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 3);
	str5 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 4);
	str6 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 5);

	// ɾ����ѡ�б���
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	// ��0λ�ô�����������ɾ�б���ĸ�������	
	m_listCtrlLinkedObjectLevel.InsertItem(0, str1);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(0, 5, str6);

	m_nCurSelItem = 0;

	// ʹ��0λ�ô���Ŀ������ʾ����ý���
	UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
	m_listCtrlLinkedObjectLevel.SetItemState(m_nCurSelItem, flag, flag);
}

void CLinkedObjectInfoView::OnMenuLastlevel()
{
	// �Ƶ����һ��
	m_listCtrlLinkedObjectLevel.SetFocus();
	if(m_nCurSelItem == -1)	  //���û��ѡ��ͷ���
	{
		MessageBox(L"��ѡ��һ������!", L"��ʾ", MB_ICONINFORMATION);
		return;  
	}

	if(m_nCurSelItem == m_listCtrlLinkedObjectLevel.GetItemCount()-1)
	{
		MessageBox(L"�Ѿ�λ�����һ��!", L"��ʾ", MB_ICONINFORMATION);
		return;
	}

	int cnt = m_listCtrlLinkedObjectLevel.GetItemCount();
	int nDragIndex = m_nCurSelItem;
	int nDropIndex = cnt;
	ModifyDTList(nDragIndex, nDropIndex);

	// ��ȡ��ѡ�б����������
	CString str1, str2, str3, str4, str5, str6;
	str1 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 0);
	str2 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);
	str3 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 2);
	str4 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 3);
	str5 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 4);
	str6 = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 5);

	// ��m_listCtrlLinkedObjectLevel.GetItemCount()-1λ�ô�����������ɾ�б���ĸ�������	
	m_listCtrlLinkedObjectLevel.InsertItem(m_listCtrlLinkedObjectLevel.GetItemCount(), str1);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(m_listCtrlLinkedObjectLevel.GetItemCount(), 5, str6);

	// ɾ����ѡ�б���
	m_listCtrlLinkedObjectLevel.DeleteItem(m_nCurSelItem);

	m_nCurSelItem = m_listCtrlLinkedObjectLevel.GetItemCount()-1;

	m_listCtrlLinkedObjectLevel.InsertItem(m_nCurSelItem, str1);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 1, str2);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 2, str3);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 3, str4);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 4, str5);
	m_listCtrlLinkedObjectLevel.SetItemText(m_nCurSelItem, 5, str6);

	// ʹ��m_listCtrlLinkedObjectLevel.GetItemCount()-1λ�ô���Ŀ������ʾ����ý���
	UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
	m_listCtrlLinkedObjectLevel.SetItemState(m_nCurSelItem, flag, flag);
}

void CLinkedObjectInfoView::OnHdnItemchangedListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMHEADER phdr = reinterpret_cast<LPNMHEADER>(pNMHDR);
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	*pResult = 0;
}

void CLinkedObjectInfoView::OnNMClickListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	LPNMHEADER phdr = reinterpret_cast<LPNMHEADER>(pNMHDR);

	if(m_bSetLinkedObjectPosCenter)
	{
		MessageBox(L"���õ���������״̬����ѡ��״̬...");
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
		MessageBox(L"��ǰ��û�пɱ༭�Ķ�̬�����\n");
		UpdateData(false);
	}

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	pDoc->GetLinkedObjectEditorView()->SetLinkedObjectPosCenterFlag( (m_bSetLinkedObjectPosCenter != 0) );
}

void CLinkedObjectInfoView::OnLvnBegindragListShowplantlevel(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	// TODO: �ڴ���ӿؼ�֪ͨ����������

	m_nDragIndex = pNMLV->iItem;
	if(m_nDragIndex != -1)
	{
		//��ҷͼƬ
		POINT point;
		int offset = 62; //ͼƬ��ƫ��
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
	// TODO: �ڴ������Ϣ�����������/�����Ĭ��ֵ
	if(m_bDragging)
	{
		// �ƶ�ͼƬ��
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
{//�ƶ����Ʋ��ʱ�����list����..
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	CString strTemp = m_listCtrlLinkedObjectLevel.GetItemText(nDragIndex, 2);
	BOOL bMainNode = _wtoi(strTemp);

	if( !bMainNode )
	{//����˳��...
		Nuclear::DTList& dtlist = pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.GetLinkedObjectList();

		CString strID = m_listCtrlLinkedObjectLevel.GetItemText(nDragIndex, 0);
		DWORD dwDragFileID = _wtoi(strID);
		int nDraglist = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDragFileID);

		strID = m_listCtrlLinkedObjectLevel.GetItemText(nDropIndex, 0);
		DWORD dwDropFileID = _wtoi(strID);
		int nDroplist = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDropFileID);

		//�����ﲻ���ͷŽڵ�����..
		if(nDroplist == 0)
		{//�嵽���ڵ��..
			if(nDropIndex == 0)//���ڵ�λ���б��ĵ�һ���ڵ�..
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
		{//�������¼���nDroplist
		}

		pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.InsertLinkedObjectNode(dtlist[nDraglist], nDroplist+1);//���..
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
	{//�϶��������ڵ�..�������,��Ϊ���ڵ����Ǵ��ڵ�һ��λ��..
	}
}

void CLinkedObjectInfoView::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: �ڴ������Ϣ�����������/�����Ĭ��ֵ
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
		{// ���
			CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
			if (pDoc == NULL)
				return;
			pDoc->SetModifiedFlag(true);

			ModifyDTList(m_nDragIndex, m_nDropIndex);

/*
			CString strTemp = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 2);
			BOOL bMainNode = _wtoi(strTemp);

			if( !bMainNode )
			{//����˳��...
				Nuclear::DTList& dtlist = pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.GetLinkedObjectList();

				CString strID = m_listCtrlLinkedObjectLevel.GetItemText(m_nDragIndex, 0);
				DWORD dwDragFileID = _wtoi(strID);
				int nDragIndex = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDragFileID);
			//	Nuclear::LkoTreeNode* pNode = pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.GetLinkedObjectNode(nDragIndex);

				strID = m_listCtrlLinkedObjectLevel.GetItemText(m_nDropIndex, 0);
				DWORD dwDropFileID = _wtoi(strID);
				int nDropIndex = pDoc->GetLinkedObjectEditorView()->GetDTListIndex(dwDropFileID);

				//�����ﲻ���ͷŽڵ�����..
				if(nDropIndex == 0)
				{//�嵽���ڵ��..
					if(m_nDropIndex == 0)//���ڵ�λ���б��ĵ�һ���ڵ�..
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
				{//�������¼���nDropIndex
				}

				pDoc->GetLinkedObjectEditorView()->m_DynamicLinkedObject.InsertLinkedObjectNode(dtlist[nDragIndex], nDropIndex+1);//���..
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
			{//�϶��������ڵ�..�������,��Ϊ���ڵ����Ǵ��ڵ�һ��λ��..
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
		MessageBox(L"��ǰ�༭����û�ж�̬�����\n");
		UpdateData(false);
	}

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	XPASSERT(pDoc);
	pDoc->GetLinkedObjectEditorView()->SetLinkedObjectStopFlag( (m_bStop != 0) );
}

void CLinkedObjectInfoView::OnBnClickedButtonConversion()
{
	//���ת������Ϊ�����ز�(img�ļ�)�ı���λ��...��һ���ļ���Ų���˱���ļ���...
	
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
	CString strPath = filePath.Left(nPos); //ָ���ļ�����Ŀ¼...

	std::vector<CString> VectorPLTFileName; //ָ��Ŀ¼�µ�����plt�ļ�

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

	int nCount = 0; //ͳ��ת���˶��ٸ��ļ�...

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
			
			std::vector<CString> fileFullPathVector; //�������·��
			int nres = FindFilesInSpecialPath(strRootPath, imgName.c_str(), fileFullPathVector);
			if(nres != 1)
			{
				CString str = L"\n�ļ�������˶��λ��...������\n";
				if(nres <1)
					str = L"\n�ļ�������...�븴�ƽ���\n";
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
			DeleteFile(filePath);//plt�ļ�

			int nPos = filePath.ReverseFind('.');
			filePath = filePath.Left(nPos);
			filePath += L".lko";

			FILE *f = NULL; //lko�ļ�
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
	str += _T(" ���ɰ汾plt�ļ���תΪ�°汾lko�ļ������̣��ϰ汾�ļ���ɾ��");
	MessageBox(str, _T("��ʾ"), MB_ICONINFORMATION);
}

int CLinkedObjectInfoView::FindFilesInSpecialPath(const CString& strSysPath, const CString& strFileName, std::vector<CString>& fileFullPathVector)
{//�����ҵ��˼���
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
			{//Ŀ¼
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
					//Ŀ¼
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
	//���ת������ΪһЩ�ز�(img�ļ�)������...�����в����������ַ�
	//2010-10-13 ��Ϊ�ƹ���������Ŀ¼�ı��˶��޸�... /2/=/ /3/=/ �ȵ��Ƶ��ϲ�Ŀ¼֮��ĸı�
	//1. ���������ʼ��..
	//2. �ַ����滻...
	if(!m_bInitRenameRule)
	{
		MessageBox(L"���ȳ�ʼ����������..\n");
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
	CString strPath = filePath.Left(nPos); //ָ���ļ�����Ŀ¼...

	std::vector<CString> VectorLKOFileName; //ָ��Ŀ¼�µ�����lko�ļ�

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
	//��lko�ļ��е�img�ļ������չ������滻....

	int nCount = 0; //ͳ��ת���˶��ٸ��ļ�...

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

			//�滻...��m_valuemap�е�ֵ��ͷ��β�滻һ��
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
	str += _T(" ���ɰ汾lko�ļ���תΪ�°汾lko�ļ�");
	MessageBox(str, _T("��ʾ"), MB_ICONINFORMATION);
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

//	CString strFilePath = L"E:\\��������.txt";

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
	int count = 0; //����1000�е��ļ��ǲ��Ϸ���..
	for(int i=0; ( i<(int)(dwFileSize/sizeof(TCHAR)) ) && (count<1000); i++)
	{
		TCHAR ch = *(pFileBuffer+i);
		if(ch != '\n')
			buf[j++] = ch;
		else
		{
			//һ�ж�����..
			if( wcslen(buf) >= 2 )
			{//��������..
				nFirstRow++;
				if(nFirstRow==1) //��һ���ǿ���ȷ���汾��
				{
					int nVer = 1;
				}

				memset(leftside, 0, sizeof(leftside));
				memset(rightside, 0, sizeof(rightside));

				PluckFirstField(buf, leftside, sizeof(leftside), L"=");
				PluckFirstField(buf, rightside, sizeof(rightside), L"\n");

				//�Ƿ��ظ�...
				if(m_valuemap.find(leftside) != m_valuemap.end())
				{
					delete[] pFileBuffer;
					MessageBox(_T("��ʼ������ʧ��....��ͬ�ĺ���ȡ�˲�ͬ����ĸ��\n"));
					return;
				}

				//�ո�...
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
	if(count>1000) //���������ļ���������ô��..
	{
		delete[] pFileBuffer;
		MessageBox(_T("����ʶ����ļ�\n"));
		return;
	}

	delete[] pFileBuffer;

	m_bInitRenameRule = true;
	GetDlgItem(IDC_BUTTON_IMGFileRename)->EnableWindow(m_bInitRenameRule);
	GetDlgItem(IDC_BUTTON_RenameFilesAndDir)->EnableWindow(m_bInitRenameRule);
	GetDlgItem(IDC_BUTTON_RenameMapFile)->EnableWindow(m_bInitRenameRule);
}



int s_cnt = 0; //ͳ��ת���˶��ٸ��ļ�

void CLinkedObjectInfoView::OnBnClickedButtonRenamefilesanddir()
{
	//ָ���ļ������ļ����Լ���Ŀ¼�������ļ����ļ��и���...
	if(!m_bInitRenameRule)
	{
		MessageBox(L"���ȳ�ʼ����������..\n");
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
	CString strPath = filePath.Left(nPos); //ָ���ļ�����Ŀ¼...

	m_vectorRenameDirs.clear();
	m_vectorRenameFiles.clear();

	s_cnt = 0;
	RenameAllFilesAndDir(strPath); //ָ��Ŀ¼�µ������ļ������ļ��и���

	CString str;
	str.Format(_T("%d"), s_cnt);
	str += _T(" ���ļ����ļ��б�����");
	MessageBox(str, _T("��ʾ"), MB_ICONINFORMATION);

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
			//�����һ����Ŀ¼��������Ŀ¼�ļ�Ҳ����
			//�õݹ��������һ����
			bool bModify = false;
			CString strPath = filefinder.GetFilePath();

			CString oldPath = strPath;
			CString newPath = strPath;

			int nPos = newPath.ReverseFind('\\');
			int len = newPath.GetLength();
			CString newNameTemp = newPath.Right(len-nPos-1); //�ļ���
			std::wstring newName = newNameTemp;

			std::map<std::wstring, std::wstring>::iterator itbegin = m_valuemap.begin();
			std::map<std::wstring, std::wstring>::iterator itend = m_valuemap.end();
			for(; itbegin!=itend; ++itbegin)
			{
			//	newName = Nuclear::replace(newName, itbegin->first, itbegin->second); //��һ��ת��
				//�ڶ���ֻ���ļ��е�����...
				if(newName == itbegin->first)
				{
					newName = itbegin->second;
					bModify = true;
					break;
				}
			}

			//�ո�...
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
			CString newNameTemp = newPath.Right(len-nPos-1); //�ļ���
			std::wstring newName = newNameTemp;

			std::map<std::wstring, std::wstring>::iterator itbegin = m_valuemap.begin();
			std::map<std::wstring, std::wstring>::iterator itend = m_valuemap.end();
			for(; itbegin!=itend; ++itbegin)
			{
				newName = Nuclear::replace(newName, itbegin->first, itbegin->second);
			}
			//�ո�.. ���ơ����� ���������ļ�������
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
	filefinder.Close();//�ر�
}


#include "..\engine\map\pmap.h"

void CLinkedObjectInfoView::OnBnClickedButtonRenamemapfile()
{
	s_cnt = 0; //ͳ���޸��˼����ط�

	if(!m_bInitRenameRule)
	{
		MessageBox(L"���ȳ�ʼ����������..\n");
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
			MessageBox(L"����ռ�ʧ��");
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
	str += _T(" ���ط����滻");
	MessageBox(str, _T("��ʾ"), MB_ICONINFORMATION);
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
		MessageBox(L"��ǰ��û�пɱ༭�Ķ�̬�����\n");
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
	// ��ȡ��ѡ�б����������
	CString strfilename;
	strfilename = m_listCtrlLinkedObjectLevel.GetItemText(m_nCurSelItem, 1);

	//����������Ƶ�������
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
	// TODO: �ڴ���ӿؼ�֪ͨ����������

	//m_nSortColIndex ��Ա������Ҫ������к�
	m_nSortColIndex = pNMLV->iSubItem;
	m_nSortFlag>0 ? m_nSortFlag=-1 : m_nSortFlag=1; //��������

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
	//��ָ���ļ��б���lko������������
	//ȫ�������ڵ��л�ȡ������������...

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
				//�����ڵ��������������lko�ļ�
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

	std::wstring strImgFileName; //���ڵ����������·��
	bool bres = PFS::CEnv::PFSFileNameToNativeFileName(strPicName, strImgFileName);

	FILE *imgfile = NULL;
	_tfopen_s(&imgfile, strImgFileName.c_str(), _T("rb"));
	if( imgfile == NULL )
	{
		MessageBox(strImgFileName.c_str());
		return FALSE;
	}

	Nuclear::PImg imgObject;
	imgObject.ResetImgObject(); //���ϰ汾�ļ�..��ʾ������°汾�ļ�ʱ���bug..
	//���磺ĳЩ������mask��Ϣ���ϰ汾��û��..��ô�ͻᵼ�´��ϰ汾�����°汾ʱ��һֱ�ظ�������ǰ�Ĳ���..
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
