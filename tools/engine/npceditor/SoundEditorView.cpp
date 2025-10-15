// SoundEditorView.cpp : CSoundEditorView 类的实现
//

#include "stdafx.h"
#include "SoundEditor.h"

#include "SoundEditorDoc.h"
#include "SoundEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CSoundEditorView

IMPLEMENT_DYNCREATE(CSoundEditorView, CView)

BEGIN_MESSAGE_MAP(CSoundEditorView, CView)
	ON_COMMAND_RANGE(ID_TOOL_MOVE, ID_GAOJISHUAGUAI, &CSoundEditorView::OnTool)
	ON_UPDATE_COMMAND_UI_RANGE(ID_TOOL_MOVE, ID_GAOJISHUAGUAI, &CSoundEditorView::OnUpdateTool)
	ON_COMMAND(ID_FILE_OPEN, &CSoundEditorView::OnFileOpen)
	ON_WM_KEYUP()
	ON_COMMAND(ID_DRAW_GIRD, &CSoundEditorView::OnDrawGird)
	ON_UPDATE_COMMAND_UI(ID_DRAW_GIRD, &CSoundEditorView::OnUpdateDrawGird)
	ON_COMMAND(ID_DRAW_BLOCK, &CSoundEditorView::OnDrawBlock)
	ON_UPDATE_COMMAND_UI(ID_DRAW_BLOCK, &CSoundEditorView::OnUpdateDrawBlock)
	ON_COMMAND(ID_SAVEMAZE, &CSoundEditorView::OnSavemaze)
	ON_COMMAND(ID_ALLSCREEN_BRUSH, &CSoundEditorView::OnAllScreenBrush)
	ON_UPDATE_COMMAND_UI(ID_ALLSCREEN_BRUSH, &CSoundEditorView::OnUpdateAllScreenBrush)
END_MESSAGE_MAP()

// CSoundEditorView 构造/析构

CSoundEditorView::CSoundEditorView()
{
	m_bIsInited = false;
	


}

CSoundEditorView::~CSoundEditorView()
{
}

BOOL CSoundEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	//  CREATESTRUCT cs 来修改窗口类或样式

	return CView::PreCreateWindow(cs);
}

// CSoundEditorView 绘制

void CSoundEditorView::OnDraw(CDC* /*pDC*/)
{
	CSoundEditorDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
	if (!m_bIsInited)
	{
		m_bIsInited = true;
		m_mainDlg.Create(m_mainDlg.IDD, this);
		m_mainDlg.BringWindowToTop();
		m_mainDlg.ShowWindow(SW_SHOW);
	}


	// TODO: 在此处为本机数据添加绘制代码
}


// CSoundEditorView 打印

BOOL CSoundEditorView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// 默认准备
	return DoPreparePrinting(pInfo);
}

void CSoundEditorView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: 添加额外的打印前进行的初始化过程
}

void CSoundEditorView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: 添加打印后进行的清除过程
}


// CSoundEditorView 诊断

#ifdef _DEBUG
void CSoundEditorView::AssertValid() const
{
	CView::AssertValid();
}

void CSoundEditorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CSoundEditorDoc* CSoundEditorView::GetDocument() const // 非调试版本是内联的
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CSoundEditorDoc)));
	return (CSoundEditorDoc*)m_pDocument;
}
#endif //_DEBUG


// CSoundEditorView 消息处理程序

void CSoundEditorView::OnFileOpen()
{
	BROWSEINFO bi;
	ZeroMemory(&bi,sizeof(BROWSEINFO));
	bi.hwndOwner = m_hWnd;
	CString folderName, fullPath;
	bi.pszDisplayName = folderName.GetBuffer(MAX_PATH);
	bi.lpszTitle = _T("请选择要打开的地图文件夹");
	bi.ulFlags = BIF_RETURNONLYFSDIRS;

	CString path = theApp.GetExePath() + L"\\map";
	bi.pidlRoot = GetIDListFromPath(path);
	LPITEMIDLIST idl = SHBrowseForFolder(&bi);
	folderName.ReleaseBuffer();
	if(idl == NULL)
		return;
	SHGetPathFromIDList(idl, fullPath.GetBuffer(MAX_PATH));
	fullPath.ReleaseBuffer();
	GlobalFree(idl);
	CString mapName = fullPath.Mid(fullPath.ReverseFind(L'\\') + 1);

	m_mainDlg.SetMazeFileName(fullPath + L"\\maze.dat");

	m_mainDlg.SetRegionTypeInfoFileName(fullPath + L"\\regiontypeinfo.dat");

	m_mainDlg.SetJumpBlockFileName(fullPath+L"\\JumpBlock.dat");

	m_mainDlg.SetIslandInfFileName(fullPath+L"\\Island.dat");

	m_mainDlg.SetIslandInf2FileName(fullPath+L"\\Island2.dat");

	m_mainDlg.SetIsMonsterFileName(fullPath+L"\\monster.dat");

	if (m_mainDlg.LoadMap(mapName))
	{
		/*CString filename = fullPath + L"\\sound.mps";
		theApp.OpenDocumentFile(filename);*/
	}
}

void CSoundEditorView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	m_mainDlg.OnUpdate();
}

void CSoundEditorView::OnTool(UINT nID)
{
	m_mainDlg.OnTool(nID);
}

void CSoundEditorView::OnUpdateTool(CCmdUI* pCmdUI)
{
	m_mainDlg.OnUpdateTool(pCmdUI);
}
void CSoundEditorView::OnKeyUp(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	m_mainDlg.OnKeyUp(nChar, nRepCnt, nFlags);
	CView::OnKeyUp(nChar, nRepCnt, nFlags);
}

void CSoundEditorView::OnDrawGird()
{
	m_mainDlg.OnDrawGird();
}

void CSoundEditorView::OnUpdateDrawGird(CCmdUI *pCmdUI)
{
	m_mainDlg.OnUpdateDrawGird(pCmdUI);
}

void CSoundEditorView::OnDrawBlock()
{
	m_mainDlg.OnDrawBlock();
}

void CSoundEditorView::OnUpdateDrawBlock(CCmdUI *pCmdUI)
{
	m_mainDlg.OnUpdateDrawBlock(pCmdUI);
}

void CSoundEditorView::OnSavemaze()
{
	m_mainDlg.OnSavemaze();
}

void CSoundEditorView::OnAllScreenBrush()
{
	m_mainDlg.OnAllScreenBrush();
}

void CSoundEditorView::OnUpdateAllScreenBrush(CCmdUI *pCmdUI)
{
	m_mainDlg.OnUpdateAllScreenBrush(pCmdUI);
}
