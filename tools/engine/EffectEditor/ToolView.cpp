

#include "stdafx.h"
#include "EffectEditor.h"
#include "EffectEditorDoc.h"
#include "ToolView.h"

#include "AddEditClipDlg.h"
#include "..\engine\effect\xapeffect.h"
#include "..\engine\effect\particleeffect.h"
#include "..\engine\effect\geffect.h"
#include "..\engine\effect\effectmanager.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNCREATE(CToolView, CView)


BEGIN_MESSAGE_MAP(CToolView, CView)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_COMMAND(ID_ADD_A_CLIP, &CToolView::OnAddAClip)
END_MESSAGE_MAP()




void CToolView::OnAddAClip()
{
	CAddEditClipDlg dlg;
	if (dlg.DoModal() == IDOK)
	{
		Nuclear::Effect* pAni = theApp.GetEngine()->GetEffectManager()->CreateEffect(dlg.m_strFilepath.GetBuffer(), false);
		if (!pAni)
		{
			AfxMessageBox(L"特效打开错误！");
			return;
		}
		pAni->AssureResource(false);
		dlg.m_strFilepath.ReleaseBuffer();
		if (pAni != NULL)
		{			
			switch (pAni->GetType())
			{
			case Nuclear::XPET_ANI:
				if (dlg.m_bUseDefColor)
					pAni->SetVertexColor(dlg.GetColor());
				break;
			case Nuclear::XPET_PARTICLE:
				if (dlg.m_bUseDefColor)
				{
					pAni->SetVertexColor(dlg.GetColor());
				} else {
					pAni->SetVertexColor(0xff000001);
				}
				break;
			case Nuclear::XPET_GEFFECT:
			case Nuclear::XPET_AUDIO:
				break;
			}
			switch (dlg.m_nPartType)
			{
			case Nuclear::XPPM_LOOP:
				pAni->SetLoopNum(-1);
				break;
			case Nuclear::XPPM_LOOPNUM:
				pAni->SetLoopNum(dlg.m_nPlayTime);
				break;
			case Nuclear::XPPM_TIME:
				pAni->SetPlayTime(dlg.m_nPlayTime);
				break;
			}
			switch (GetDocument()->GetEffectFileType())
			{
			case EFT_EFFECT:
				{
					Nuclear::AbstractEffectClip *pClip = NULL;
					pClip = GetDocument()->AddEffectClip(dlg.m_layer, dlg.m_nStartFrame, -1);
					if (pClip)
					{
						pClip->SetAnimation(pAni);
						GetDocument()->UpdateAllViews(NULL);
					} else {
						delete pAni;
					}
				}
				break;
			case EFT_SEBIND:
				{
					pAni->SetLoopNum(-1);
					GetDocument()->SetEffectForBind(pAni);
					GetDocument()->UpdateAllViews(NULL);
				}
				break;
			}

		}

	}
}

void CToolView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);
	if (m_bInited)
	{
		CRect rect;
		GetClientRect(&rect);
		m_Panel.MoveWindow(&rect);
	}
}

void CToolView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);
	if (m_bInited)
	{
		CRect rect;
		GetClientRect(&rect);
		m_Panel.MoveWindow(&rect);
	}
}

#ifdef _DEBUG
void CToolView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CToolView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif

CEffectEditorDoc* CToolView::GetDocument() const 
{
	XPASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CEffectEditorDoc)));
	return (CEffectEditorDoc*)m_pDocument;
}
#endif 




void CToolView::OnDraw(CDC* pDC)
{
	CDocument* pDoc = GetDocument();

	if (!m_bInited)
	{
		m_Panel.Create(m_Panel.IDD, this);
		CRect rect;
		GetClientRect(&rect);
		m_Panel.MoveWindow(&rect);
		m_Panel.ShowWindow(SW_SHOW);
		m_bInited = true;
		m_Panel.SetToolsNotify(this);
	}
}




CToolView::~CToolView()
{
}

CToolView::CToolView() : m_bInited(false)
{

}

