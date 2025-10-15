#include "StdAfx.h"
#include "ParticleSystemEditor.h"
#include "ParticleSysInfoView.h"
#include "ParticleSystemEditorDoc.h"


IMPLEMENT_DYNCREATE(CParticleSysInfoView, CView)

CParticleSysInfoView::CParticleSysInfoView(void)
{
	m_bStop = false;
	XL_PSLINFO::s_nProcessId = GetCurrentProcessId();
}

CParticleSysInfoView::~CParticleSysInfoView(void)
{
}

BEGIN_MESSAGE_MAP(CParticleSysInfoView, CView)
	ON_WM_KEYDOWN()

	ON_COMMAND(ID_BUTTON_OPENPSL, &CParticleSysInfoView::OnOpenPsl)
	ON_COMMAND(ID_BUTTON_SAVEPSL, &CParticleSysInfoView::OnSavePsl)
	ON_COMMAND(ID_BUTTON_SAVEASPSL, &CParticleSysInfoView::OnSaveasPsl)
END_MESSAGE_MAP()


void CParticleSysInfoView::OnOpenPsl()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleParameterView()->OnBnClickedButtonOpenparticlesys();
}

void CParticleSysInfoView::OnSavePsl()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleParameterView()->OnBnClickedButtonSavemodifypsl();
}

void CParticleSysInfoView::OnSaveasPsl()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleParameterView()->OnBnClickedButtonSaveparticlesys();
}

void CParticleSysInfoView::OnDraw(CDC* pDC)
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	int nActiveParticle = pParticleSys->GetNumActiveParticles();
	CString str;
	str.Format(L"当前更新粒子数:  %d ", nActiveParticle);
	pDC->SetBkMode(TRANSPARENT);
	pDC->TextOut(0, 0, str);

	float fps = pDoc->GetParticleSystemEditorView()->GetFPS();
	str.Format(L"FPS: %.1f", fps);
	pDC->TextOut(180, 0, str);

	int cnt = pParticleSys->GetPSL()->m_vectorTexturePath.size();
//	if( pDoc->GetParticleParameterView()->GetParticleSys()->IsEmitterSingleTexture() )
//		cnt = 1;
	str.Format(L"%d ", cnt);
	str = L"当前使用的纹理数：" + str;

	pDC->TextOut(0, 20, str);
	for(int i=0; i<cnt; i++)
	{
		int a = i%2;
		int b = i/2;
		pDC->TextOut(250*a, 40+20*b, pParticleSys->GetPSL()->m_vectorTexturePath[i].c_str());
	}
}
void CParticleSysInfoView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	switch(nChar)
	{
	case 'P':
		{
			CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
			ASSERT_VALID(pDoc);
			if (!pDoc)
				return CView::OnKeyDown(nChar, nRepCnt, nFlags);
			pDoc->GetParticleParameterView()->OnBnClickedButtonReplay();
			m_bStop = false;
			pDoc->GetParticleParameterView()->OnSetStopFlag(m_bStop);
		}
		break;
	case 'O':
		{
			m_bStop = !m_bStop;

			CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
			ASSERT_VALID(pDoc);
			if (!pDoc)
				return CView::OnKeyDown(nChar, nRepCnt, nFlags);
			pDoc->GetParticleParameterView()->OnSetStopFlag(m_bStop);
		}
		break;
	default:
		break;
	}

	CView::OnKeyDown(nChar, nRepCnt, nFlags);
}
