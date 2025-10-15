

#include "stdafx.h"
#include "..\engine\common\log.h"
#include "..\engine\common\xptypes.h"
#include "..\engine\effect\effectmanager.h"
#include "..\engine\sound\directsoundmanager.h"
#include "..\engine\engine\configmanager.h"
#include "MainFrm.h"
#include "EffectEditor.h"
#include "EffectEditorDoc.h"
#include "EffectEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif



const int CEffectEditorView::ADJ_POS[8] = {BAP_TOP, BAP_TOP|BAP_LEFT, BAP_LEFT, BAP_BOTTOM|BAP_LEFT,
BAP_BOTTOM, BAP_BOTTOM|BAP_RIGHT, BAP_RIGHT, BAP_TOP|BAP_RIGHT};

IMPLEMENT_DYNCREATE(CEffectEditorView, CView)

BEGIN_MESSAGE_MAP(CEffectEditorView, CView)

	ON_COMMAND(ID_FILE_PRINT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CView::OnFilePrintPreview)
	ON_WM_TIMER()
	ON_WM_ERASEBKGND()
	ON_COMMAND(ID_BUTTON_PLAY, &CEffectEditorView::OnPlay)
	ON_COMMAND(ID_BUTTON_PAUSE, &CEffectEditorView::OnPause)
	ON_COMMAND(ID_BUTTON_STOP, &CEffectEditorView::OnStop)
	ON_COMMAND(ID_PREV_FRAME, &CEffectEditorView::OnPrevFrame)
	ON_COMMAND(ID_NEXT_FRAME, &CEffectEditorView::OnNextFrame)
	ON_UPDATE_COMMAND_UI_RANGE(ID_BUTTON_PLAY, ID_BUTTON_STOP, &CEffectEditorView::OnUpdateControl)
	ON_UPDATE_COMMAND_UI_RANGE(FIRST_TOOL_ID, LAST_TOOL_ID, &CEffectEditorView::OnUpdateToolsControl)
	ON_COMMAND_RANGE(FIRST_TOOL_ID, LAST_TOOL_ID, &CEffectEditorView::OnToolsControl)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_RBUTTONDOWN()
	ON_WM_RBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_COMMAND(ID_SHOW_BOX, &CEffectEditorView::OnShowBox)
	ON_UPDATE_COMMAND_UI(ID_SHOW_BOX, &CEffectEditorView::OnUpdateShowBox)
	ON_COMMAND(ID_SHOW_CENTER_CROSS, &CEffectEditorView::OnShowCenterCross)
	ON_UPDATE_COMMAND_UI(ID_SHOW_CENTER_CROSS, &CEffectEditorView::OnUpdateShowCenterCross)
	ON_COMMAND(ID_RIGHT_KEY_DOWN, &CEffectEditorView::OnRightKeyDown)
	ON_COMMAND(ID_LEFT_KEY_DOWN, &CEffectEditorView::OnLeftKeyDown)
	ON_COMMAND(ID_DOWN_KEY_DOWN, &CEffectEditorView::OnDownKeyDown)
	ON_COMMAND(ID_UP_KEY_DOWN, &CEffectEditorView::OnUpKeyDown)
	ON_COMMAND(ID_BACKGROUND_COLOR, &CEffectEditorView::OnBackgroundColor)
	ON_COMMAND(ID_BACKGROUND_PICTURE, &CEffectEditorView::OnBackgroundPicture)
	ON_UPDATE_COMMAND_UI_RANGE(ID_SHOW_BACKGROUND_COLOR, ID_SHOW_BACKGROUND_PICTURE, &CEffectEditorView::OnUpdateBackgroundMode)
	ON_COMMAND_RANGE(ID_SHOW_BACKGROUND_COLOR, ID_SHOW_BACKGROUND_PICTURE, &CEffectEditorView::OnSelectBackgroundMode)
	ON_COMMAND(ID_PLAY_STOP, &CEffectEditorView::OnPlayStop)
	ON_WM_MBUTTONDOWN()
	ON_WM_MBUTTONUP()
END_MESSAGE_MAP()


template <typename T>
void swap(T& a, T&b)
{
	T c; c = a; a = b; b = c;
}

template <class T>
void CheckRect(T &rect) 
{
	if (rect.left > rect.right)
		swap(rect.left, rect.right);
	if (rect.top > rect.bottom)
		swap(rect.top, rect.bottom);
}

bool CEffectEditorView::SetBackgroundPicture(const wchar_t * filename)
{
	m_BackgroundPicture = m_pRenderer->LoadPictureFromNativePath(filename);
	if (m_BackgroundPicture != Nuclear::INVALID_PICTURE_HANDLE)
	{
		Nuclear::PictureInfo picinfo;
		m_pRenderer->GetPictureInfo(m_BackgroundPicture, picinfo);
		m_fBgPicWidth = static_cast<float>(picinfo.m_nPicWidth);
		m_fBgPicHeight = static_cast<float>(picinfo.m_nPicHeight);
		return true;
	}
	return false;
}

void CEffectEditorView::OnBackgroundPicture()
{
	CFileDialog dlg(TRUE);
	if (dlg.DoModal() == IDOK)
	{
		CString filename = dlg.GetPathName();
		if (SetBackgroundPicture(filename.GetBuffer()))
		{
			WritePrivateProfileString(L"BackGround", L"ImgPath", filename, theApp.GetCfgFilePath());
		} else {
			AfxMessageBox(L"加载图片失败");
		}
		filename.ReleaseBuffer();
	}
}

void CEffectEditorView::OnBackgroundColor()
{
	COLORREF color = ReverseColor(m_BackgroundColor.data);
	CColorDialog dlg(color);
	if (dlg.DoModal() == IDOK)
	{
		m_BackgroundColor.data = ReverseColor(dlg.GetColor());
		m_BackgroundColor.a = 255;

		CString colorData;
		colorData.Format(L"%d", m_BackgroundColor.data);
		WritePrivateProfileString(L"BackGround", L"Color", colorData, theApp.GetCfgFilePath());
	}
}

void CEffectEditorView::OnUpdateBackgroundMode(CCmdUI* pCmdUI)
{
	switch (pCmdUI->m_nID)
	{
	case ID_SHOW_BACKGROUND_COLOR:
		pCmdUI->SetRadio(m_BgMode == BM_COLOR);
		break;
	case ID_SHOW_BACKGROUND_PICTURE:
		pCmdUI->SetRadio(m_BgMode == BM_PICTURE);
		break;
	}
}

void CEffectEditorView::OnSelectBackgroundMode(UINT nID)
{
	switch (nID)
	{
	case ID_SHOW_BACKGROUND_COLOR:
		m_BgMode = BM_COLOR;
		break;
	case ID_SHOW_BACKGROUND_PICTURE:
		if ( m_BackgroundPicture != Nuclear::INVALID_PICTURE_HANDLE)	
		{
			m_BgMode = BM_PICTURE;
		} else {
			AfxMessageBox(L"请先设置图片");
			m_BgMode = BM_COLOR;
		}

		break;
	}
	CString modeData;
	modeData.Format(L"%d", m_BgMode);
	WritePrivateProfileString(L"BackGround", L"BGmode", modeData, theApp.GetCfgFilePath());
}

void CEffectEditorView::OnUpdate(CView* , LPARAM , CObject* )
{
	if (GetDocument()->m_bNeedReplay)
	{
		if (GetDocument()->GetPlayState() == Nuclear::XPES_PLAYING)
		{
			GetDocument()->Play();
		}
		GetDocument()->m_bNeedReplay = false;
	}
}

void CEffectEditorView::OnUpKeyDown()
{
	ArrowKeyDownMove(0, -1);
}
void CEffectEditorView::OnDownKeyDown()
{
	ArrowKeyDownMove(0, 1);
}

void CEffectEditorView::OnLeftKeyDown()
{
	ArrowKeyDownMove(-1, 0);
}

void CEffectEditorView::OnRightKeyDown()
{
	ArrowKeyDownMove(1, 0);
}

void CEffectEditorView::ArrowKeyDownMove(int x, int y)
{
	switch (m_ToolsState)
	{
	case TS_MOVE:
		GetDocument()->MoveSelectedClips(x, y);
		break;
	case TS_FRAME_POS:
		if (GetDocument()->GetSelectedClips().size() > 0 && GetDocument()->GetPlayState() != Nuclear::XPES_PLAYING)
		{
			CPoint adjPoint(x, y);
			GetDocument()->ModifyAdjPosForSelectedClip(adjPoint);
		}
		break;
	default:
		break;
	}
}

void CEffectEditorView::OnUpdateShowBox(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bShowBox);
}

void CEffectEditorView::OnUpdateShowCenterCross(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bShowCenterCross);
}

void CEffectEditorView::OnShowBox()
{
	m_bShowBox = !m_bShowBox;
}

void CEffectEditorView::OnShowCenterCross()
{
	m_bShowCenterCross = !m_bShowCenterCross;
}

void CEffectEditorView::OnMouseMove(UINT nFlags, CPoint point)
{
	reinterpret_cast<CMainFrame*>(theApp.m_pMainWnd)->SetStateBarMousePosition(point.x, point.y);
	switch (m_ToolsState)
	{
	case TS_ARROW:
		if (m_bLMouseButtonDown || m_bMMouseButtonDown)
		{
			m_EffPosition = m_OldEffPosition;
			m_EffPosition.x += point.x - m_MouseDownPosition.x;
			m_EffPosition.y += point.y - m_MouseDownPosition.y;
			GetDocument()->SetSpritePosition(m_EffPosition);
		}
		if (m_bRMouseButtonDown || m_bMMouseButtonDown)
		{
			m_BgPosition = m_OldBgPosition;
			m_BgPosition.x += point.x - m_MouseDownPosition.x;
			m_BgPosition.y += point.y - m_MouseDownPosition.y;
		}
		break;
	case TS_BOUNDS_RECT:
		if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
		{
			switch (m_MouseMode)
			{
			case MM_NORMAL:
				if (!(m_bLMouseButtonDown || m_bRMouseButtonDown || m_bMMouseButtonDown))
				{
					m_nAdjPosition = CheckBoundsAdjPosition(point);
					if (ResetMouseCurData())
					{
						SetCursor(m_hMouseCursor);
					}
				}
				break;
			case MM_BOUNDS_ADJUST_RECT:
				if (m_nAdjPosition != 0)
				{
					SetCursor(m_hMouseCursor);
				}
				break;
			}
		}
		break;	
	case TS_FRAME_POS:
		if (GetDocument()->GetPlayState() == Nuclear::XPES_PLAYING)	
			break;
	case TS_MOVE:
		if (m_bLMouseButtonDown)
		{
			if (GetDocument()->GetEffectFileType() == EFT_SEBIND)
			{
				if (m_ToolsState == TS_FRAME_POS)
				{
					Nuclear::Effect *pEffect = GetDocument()->GetBindEffect();
					if (pEffect)
					{
						CPoint pt = point - m_MouseDownPosition;
						pEffect->SetLocation(pt.x, pt.y);
					}
				}
			} else {
				if (m_OldAnimationPosition.size() > 0)
				{
					SetPositionFromOldAniPosition(point - m_MouseDownPosition);
				}
			}
		}
		break;
	case TS_FRAME_SIZE:
		if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
		{
			const EffectClipSet& selClip = GetDocument()->GetSelectedClips();
			if (selClip.size() == 1)
			{
				switch (m_MouseMode)
				{
				case MM_NORMAL:
					{
						const Nuclear::Effect* pAni = (*selClip.begin())->GetAnimation();
						if (pAni->GetType() != Nuclear::XPET_AUDIO)
						{
							if (CheckAniSizeMousePosition(point, pAni) > 0)
							{
								ResetMouseCurData();
								SetCursor(m_hMouseCursor);
							}
							break;
						}
					}
					break;
				case MM_MODIFYING_SCAL:
					if (m_nAdjPosition != 0)
					{
						SetCursor(m_hMouseCursor);
					}
					break;
				}
			}
		}
		break;
	}
	m_MousePosition = point;
	__super::OnMouseMove(nFlags, point);
}


void CEffectEditorView::OnMButtonUp(UINT nFlags, CPoint point)
{
	ReleaseCapture();
	m_bMMouseButtonDown = false;
	switch (m_ToolsState)
	{
	case TS_ARROW:
		m_BgPosition = m_OldBgPosition;
		m_BgPosition.x += point.x - m_MouseDownPosition.x;
		m_BgPosition.y += point.y - m_MouseDownPosition.y;
		m_EffPosition = m_OldEffPosition;
		m_EffPosition.x += point.x - m_MouseDownPosition.x;
		m_EffPosition.y += point.y - m_MouseDownPosition.y;
		break;
	default:
		break;
	}
	__super::OnMButtonUp(nFlags, point);
}

void CEffectEditorView::OnMButtonDown(UINT nFlags, CPoint point)
{
	SetCapture();
	m_bMMouseButtonDown = true;
	m_MouseDownPosition = point;
	switch (m_ToolsState)
	{
	case TS_ARROW:
		m_OldBgPosition = m_BgPosition;
		m_OldEffPosition = m_EffPosition;
		break;
	default:		
		break;
	}
	__super::OnMButtonDown(nFlags, point);
}

void CEffectEditorView::OnRButtonUp(UINT nFlags, CPoint point)
{
	ReleaseCapture();
	m_bRMouseButtonDown = false;
	switch (m_ToolsState)
	{
	case TS_ARROW:
		m_BgPosition = m_OldBgPosition;
		m_BgPosition.x += point.x - m_MouseDownPosition.x;
		m_BgPosition.y += point.y - m_MouseDownPosition.y;
		break;
	default:		
		break;
	}
	__super::OnRButtonUp(nFlags, point);
}

void CEffectEditorView::OnRButtonDown(UINT nFlags, CPoint point)
{
	SetCapture();
	m_bRMouseButtonDown = true;
	m_MouseDownPosition = point;
	switch (m_ToolsState)
	{
	case TS_ARROW:
		m_OldBgPosition = m_BgPosition;
		break;
	default:
		break;
	}
	__super::OnRButtonDown(nFlags, point);
}

void CEffectEditorView::OnLButtonUp(UINT nFlags, CPoint point)
{
	ReleaseCapture();
	CEffectEditorDoc *pDoc = GetDocument();

	switch (m_ToolsState)
	{
	case TS_ARROW:
		if (m_bLMouseButtonDown)
		{
			m_EffPosition = m_OldEffPosition;
			m_EffPosition.x += point.x - m_MouseDownPosition.x;
			m_EffPosition.y += point.y - m_MouseDownPosition.y;
			pDoc->SetSpritePosition(m_EffPosition);
		}
		break;
	case TS_BOUNDS_RECT:
		if (pDoc->GetEffectFileType() == EFT_EFFECT)
		{
			Nuclear::CRECT rect;
			switch (m_MouseMode)
			{
			case MM_NORMAL:
				rect.left = m_MouseDownPosition.x - m_EffPosition.x;
				rect.top = m_MouseDownPosition.y - m_EffPosition.y;
				rect.right = m_MousePosition.x - m_EffPosition.x;
				rect.bottom = m_MousePosition.y - m_EffPosition.y;
				CheckRect(rect);
				break;
			case MM_BOUNDS_ADJUST_RECT:
				GetAdjBoundsRect(point, rect);
				break;	
			}
			pDoc->SetBoundsRect(rect);
			m_MouseMode = MM_NORMAL;
		}
		break;
	case TS_MOVE:
		if (m_bLMouseButtonDown && m_OldAnimationPosition.size() > 0 && (pDoc->GetEffectFileType() == EFT_EFFECT))
		{
			ClipOldPosMap::iterator iter = m_OldAnimationPosition.begin();
			int addX = point.x - m_MouseDownPosition.x;
			int addY = point.y - m_MouseDownPosition.y;

			for (;iter!=m_OldAnimationPosition.end();++iter)
			{
				iter->first->GetAnimation()->SetLocation(iter->second);
			}

			pDoc->MoveSelectedClips(addX, addY);
		}
		break;
	case TS_FRAME_POS:
		if (m_bLMouseButtonDown && pDoc->GetSelectedClips().size() > 0 && pDoc->GetPlayState() != Nuclear::XPES_PLAYING)
		{
			ClipOldPosMap::iterator iter = m_OldAnimationPosition.begin();

			if (pDoc->GetEffectFileType()==EFT_EFFECT)
			{
				for (;iter!=m_OldAnimationPosition.end();++iter)
				{
					iter->first->GetAnimation()->SetLocation(iter->second);
				}
				pDoc->ModifyAdjPosForSelectedClip(point - m_MouseDownPosition);
			} else {
				if (pDoc->GetBindEffect())
				{
					pDoc->GetBindEffect()->SetLocation(0, 0);
					pDoc->ModifyAdjPosForSelectedClip(point - m_MouseDownPosition);
				}
			}
		}
		break;
	case TS_FRAME_ROTATE:
		{
			pDoc->SetSelectClipsControl(NULL);
			float radian;
			if (GetRotationRadian(radian))
			{
				pDoc->SetRotationForSelectedClip(radian);
			}
		}
		break;
	case TS_FRAME_SIZE:
		if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
		{
			switch (m_MouseMode)
			{
			case MM_MODIFYING_SCAL:
				{
					Nuclear::FPOINT pt;
					if (GetScale(pt))
					{
						GetDocument()->SetScaleForSelectedClip(pt);
					}
					m_MouseMode = MM_NORMAL;
					GetDocument()->SetSelectClipsControl(NULL);
				}


				break;
			case MM_NORMAL:
				break;
			}
		}
	}
	m_bLMouseButtonDown = false;
	__super::OnLButtonUp(nFlags, point);
}

void CEffectEditorView::OnLButtonDown(UINT nFlags, CPoint point)
{
	SetCapture();
	m_bLMouseButtonDown = true;
	m_MouseDownPosition = point;
	switch (m_ToolsState)
	{
	case TS_ARROW:
		m_OldEffPosition = m_EffPosition;
		break;
	case TS_BOUNDS_RECT:
		if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
		{
			switch (m_MouseMode)
			{
			case MM_NORMAL:
				m_nAdjPosition = CheckBoundsAdjPosition(point);
				if (ResetMouseCurData())
				{
					m_MouseMode = MM_BOUNDS_ADJUST_RECT;
					SetCursor(m_hMouseCursor);
				}
				break;
			case MM_BOUNDS_ADJUST_RECT:

				m_MouseMode = MM_NORMAL;
				break;
			}
		}
		break;
	case TS_MOVE:
		SetOldAniPosition();
		break;
	case TS_FRAME_POS:
		SetOldAniPosition();
		break;
	case TS_FRAME_ROTATE:
		if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
		{
			const EffectClipSet& selClip = GetDocument()->GetSelectedClips();
			if (selClip.size() == 1)
			{
				const Nuclear::Effect* pAni = (*selClip.begin())->GetAnimation();
				if (pAni->GetType() != Nuclear::XPET_AUDIO)
				{
					m_OldAngle = pAni->GetRotationRadian();
					GetDocument()->SetSelectClipsControl(this);
				}
			}
		} else {
			const Nuclear::Effect* pAni = GetDocument()->GetBindEffect();
			m_OldAngle = pAni->GetRotationRadian();
			GetDocument()->SetSelectClipsControl(this);
		}
		break;
	case TS_FRAME_SIZE:
		if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
		{
			m_ScaleMousePosition = 0;
			const EffectClipSet& selClip = GetDocument()->GetSelectedClips();
			if (selClip.size() == 1)
			{
				const Nuclear::Effect* pAni = (*selClip.begin())->GetAnimation();
				if (pAni->GetType() != Nuclear::XPET_AUDIO)
				{
					m_ScaleMousePosition = CheckAniSizeMousePosition(point, pAni);
					if (m_ScaleMousePosition > 0)
					{
						ResetMouseCurData();
						m_MouseMode = MM_MODIFYING_SCAL;
						SetCursor(m_hMouseCursor);
						m_OldScal = pAni->GetScale();
						GetDocument()->SetSelectClipsControl(this);
					}
					break;
				}
			}
		}
		break;
	}
	__super::OnLButtonDown(nFlags, point);
}

void CEffectEditorView::ChangeSize()
{
	if (m_pRenderer)
	{
		GetClientRect(&m_ClientRect);
		Nuclear::CRECT sourceRect(0, 0, m_ClientRect.right-m_ClientRect.left, m_ClientRect.bottom-m_ClientRect.top);
		m_pRenderer->SetSourceRect(&sourceRect);
	}
}


void CEffectEditorView::OnSizing(UINT fwSide, LPRECT pRect)
{
	__super::OnSizing(fwSide, pRect);
	ChangeSize();
}

void CEffectEditorView::OnSize(UINT nType, int cx, int cy)
{
	__super::OnSize(nType, cx, cy);
	ChangeSize();
}

bool CEffectEditorView::ResetMouseCurData()
{
	switch (m_nAdjPosition)
	{
	case BAP_TOP:
	case BAP_BOTTOM:
		m_hMouseCursor = LoadCursor(NULL, IDC_SIZENS);
		return true;
		break;
	case BAP_LEFT:
	case BAP_RIGHT:
		m_hMouseCursor = LoadCursor(NULL, IDC_SIZEWE);
		return true;
		break;
	case BAP_LEFT | BAP_TOP:
	case BAP_RIGHT | BAP_BOTTOM:
		m_hMouseCursor = LoadCursor(NULL, IDC_SIZENWSE);
		return true;
		break;
	case BAP_LEFT | BAP_BOTTOM:
	case BAP_RIGHT | BAP_TOP:
		m_hMouseCursor = LoadCursor(NULL, IDC_SIZENESW);
		return true;
		break;
	default:
		m_hMouseCursor = LoadCursor(NULL, IDC_ARROW);
		return false;
		break;
	}
	return false;
}

int CEffectEditorView::CheckAniSizeMousePosition(const CPoint &pt, const Nuclear::Effect* pAni)
{
	if (!pAni)
		return 0;
	int i;
	m_nAdjPosition = 0;
	m_OldAngle = pAni->GetRotationRadian();
	while (m_OldAngle < 0)
	{
		m_OldAngle += Nuclear::XP_PI * 2;
	}
	while (m_OldAngle >= Nuclear::XP_PI * 2)
	{
		m_OldAngle -= Nuclear::XP_PI * 2;
	}

	int pos = 8 - Round(m_OldAngle * 4 / Nuclear::XP_PI);


	for (i=0;i<8;++i,++pos)
	{
		if (fabs(m_AdjPointArray[i].x - pt.x) + fabs(m_AdjPointArray[i].y - pt.y) <  10)
		{
			pos %= 8;
			m_nAdjPosition = ADJ_POS[pos];
			return ADJ_POS[i];
		}
	}
	return 0;
}

bool CEffectEditorView::GetRotationRadian( float& angle) const
{
	if (!m_bLMouseButtonDown)
		return false;
	if (m_ToolsState != TS_FRAME_ROTATE)
		return false;
	const EffectClipSet& selClip = GetDocument()->GetSelectedClips();
	if (selClip.size() != 1)
		return false;
	const Nuclear::Effect* pAni;
	if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
	{
		pAni = (*selClip.begin())->GetAnimation();
	} else {
		pAni = GetDocument()->GetBindEffect();
	}
	if (!pAni)
		return false;
	if (pAni->GetType() == Nuclear::XPET_AUDIO)
		return false;
	angle = CalcMouseAngle();
	return true;
}

float CEffectEditorView::CalcMouseAngle() const
{
	const Nuclear::AbstractEffectClip* pClip = *GetDocument()->GetSelectedClips().begin();
	const Nuclear::Effect* pAni;
	if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
	{
		pAni = pClip->GetAnimation();
	} else {
		pAni = GetDocument()->GetBindEffect();
	}
	Nuclear::FPOINT pos = pAni->GetLocation().ToFPOINT();
	pos += pAni->GetTranslation();
	Nuclear::XPVECTOR2 beginVet, endVet;
	beginVet.x = m_MouseDownPosition.x - pos.x;
	beginVet.y = m_MouseDownPosition.y - pos.y;
	endVet.x = m_MousePosition.x - pos.x;
	endVet.y = m_MousePosition.y - pos.y;
	float result = 0.0f;
	float beginVetMod = sqrtf(beginVet.x * beginVet.x + beginVet.y * beginVet.y);
	float endVetMod = sqrtf(endVet.x * endVet.x + endVet.y * endVet.y);


	result = static_cast<float>(acos((beginVet.x * endVet.x + beginVet.y * endVet.y) / (beginVetMod * endVetMod)));

	if (beginVet.x * endVet.y - endVet.x * beginVet.y < 0)
		result *= -1;
	result += m_OldAngle;
	return result;
}

bool CEffectEditorView::GetScale( Nuclear::FPOINT& scale) const
{
	if (!m_bLMouseButtonDown)
		return false;
	if (m_ToolsState != TS_FRAME_SIZE)
		return false;
	if (m_MouseMode != MM_MODIFYING_SCAL)
		return false;
	const EffectClipSet& selClip = GetDocument()->GetSelectedClips();
	if (selClip.size() != 1)
		return false;
	const Nuclear::Effect* pAni = (*selClip.begin())->GetAnimation();
	if (!pAni)
		return false;
	if (pAni->GetType() == Nuclear::XPET_AUDIO)
		return false;

	Nuclear::XPMATRIX3 m;
	Nuclear::XPMatrixRotationOrigin(&m, pAni->GetRotationRadian());
	Nuclear::XPVECTOR2 vet, vetOut;
	vet.x = static_cast<float>(m_MousePosition.x - m_MouseDownPosition.x);
	vet.y = static_cast<float>(m_MousePosition.y - m_MouseDownPosition.y);
	Nuclear::XPVec3TransformNormal(&vetOut, &vet, &m);
	scale = m_OldScal;
	Nuclear::CRECT rect = pAni->GetRelBouningBox();
	if (m_ScaleMousePosition & BAP_TOP)
	{
		scale.y -= vet.y * 2 / rect.Height();
	} else if (m_ScaleMousePosition & BAP_BOTTOM)
	{
		scale.y += vet.y * 2 / rect.Height();
	}
	if (m_ScaleMousePosition & BAP_LEFT)
	{
		scale.x -= vet.x * 2 / rect.Width();
	} else if (m_ScaleMousePosition & BAP_RIGHT)
	{
		scale.x += vet.x * 2 / rect.Width();
	}
	return true;
}

void CEffectEditorView::OnNextFrame()
{
	float fTmp = GetDocument()->GetPlayingFrame();
	GetDocument()->SetPlayingFrame(Round(fTmp) + 1);
}

void CEffectEditorView::OnPrevFrame()
{
	float fTmp = GetDocument()->GetPlayingFrame();
	GetDocument()->SetPlayingFrame(Round(fTmp) - 1);
}

void CEffectEditorView::OnToolsControl(UINT nID)
{
	m_ToolsState = static_cast<eToolState>(nID);
}

void CEffectEditorView::OnPlayStop()
{
	Nuclear::XPEffectState state = GetDocument()->GetPlayState();
	switch(state)
	{
	case Nuclear::XPES_PLAYING:
		GetDocument()->Pause();
		break;
	case Nuclear::XPES_PAUSE:
	case Nuclear::XPES_STOP:
		GetDocument()->Play();
		break;
	}
}

void CEffectEditorView::OnStop()
{
	GetDocument()->Stop();
}

void CEffectEditorView::OnPause()
{
	GetDocument()->Pause();
}

void CEffectEditorView::OnPlay()
{
	GetDocument()->Play();
}

void CEffectEditorView::OnUpdateToolsControl(CCmdUI* pCmdUI)
{
	pCmdUI->SetRadio(m_ToolsState == pCmdUI->m_nID);
}

void CEffectEditorView::OnUpdateControl(CCmdUI* pCmdUI)
{
	Nuclear::XPEffectState state = GetDocument()->GetPlayState();
	switch (pCmdUI->m_nID)
	{
	case ID_BUTTON_PLAY:
		pCmdUI->SetRadio(state == Nuclear::XPES_PLAYING);
		break;
	case ID_BUTTON_PAUSE:
		pCmdUI->SetRadio(state == Nuclear::XPES_PAUSE);
		break;
	case ID_BUTTON_STOP:
		pCmdUI->SetRadio(state == Nuclear::XPES_STOP);
		break;
	}
}

BOOL CEffectEditorView::OnEraseBkgnd(CDC* pDC)
{
	return true;
}

void CEffectEditorView::OnTimer(UINT_PTR nIDEvent)
{
	switch (nIDEvent)
	{
	case 1000:
		if (Tick())
		{
			RenderEffect();
			CMainFrame *pMainFrame = dynamic_cast<CMainFrame*>(theApp.m_pMainWnd);
			pMainFrame->SetStateBarBgPosition(static_cast<int>(m_BgPosition.x), static_cast<int>(m_BgPosition.y));
			pMainFrame->SetStateBarEffectPosition(m_EffPosition.x, m_EffPosition.y);
			const EffectClipSet& sel = GetDocument()->GetSelectedClips();
			if (sel.size() > 0)
			{
				const Nuclear::AbstractEffectClip* pClip = *sel.begin();
				const Nuclear::Effect *pEffect = pClip->GetAnimation();
				if (pEffect)
				{
					Nuclear::CPOINT pt = pEffect->GetLocation() + pEffect->GetTranslation() - m_EffPosition;
					pMainFrame->SetStateBarSelEffectPosition(pt.x, pt.y);
				} else {
					pMainFrame->SetStateBarSelEffectPosition(0, 0);
				}
			} else {
				pMainFrame->SetStateBarSelEffectPosition(0, 0);
			}

		}
		break;
	}
	__super::OnTimer(nIDEvent);
}

void CEffectEditorView::OnInitialUpdate()
{
	__super::OnInitialUpdate();

	if (!m_bInited)
	{
		SetTimer(1000, 16, NULL);
		GetDocument()->ResetEffect(this);
		m_bInited = true;
	}
	GetClientRect(&m_ClientRect);
	m_EffPosition.x = m_ClientRect.Width() >> 1;
	m_EffPosition.y = m_ClientRect.Height() >> 1;
	GetDocument()->SetSpritePosition(m_EffPosition);
}

void CEffectEditorView::SetPositionFromOldAniPosition(const CPoint &adjPt)
{
	ClipOldPosMap::iterator iter = m_OldAnimationPosition.begin();
	for (;iter!=m_OldAnimationPosition.end();++iter)
	{
		iter->first->GetAnimation()->SetLocation(iter->second.x + adjPt.x, iter->second.y + adjPt.y);
	}
}


void CEffectEditorView::SetOldAniPosition()
{
	if (GetDocument()->GetEffectFileType() == EFT_SEBIND)
		return;
	const EffectClipSet& selectedClips = GetDocument()->GetSelectedClips();
	m_OldAnimationPosition.clear();
	if (selectedClips.size() > 0)
	{
		EffectClipSet::const_iterator iter = selectedClips.begin();
		for (;iter!=selectedClips.end();++iter)
		{
			m_OldAnimationPosition[*iter] = (*iter)->GetAnimation()->GetLocation();
		}
	}
}

void CEffectEditorView::GetAdjBoundsRect(const CPoint &pt, Nuclear::CRECT& rect)
{
	rect = GetDocument()->GetRelativeBouningBox();
	if (m_nAdjPosition & BAP_TOP)
	{
		rect.top += pt.y - m_MouseDownPosition.y;
	} else if (m_nAdjPosition & BAP_BOTTOM)
	{
		rect.bottom += pt.y - m_MouseDownPosition.y;
	}
	if (m_nAdjPosition & BAP_LEFT)
	{
		rect.left += pt.x - m_MouseDownPosition.x;
	} else if (m_nAdjPosition & BAP_RIGHT)
	{
		rect.right += pt.x - m_MouseDownPosition.x;
	}
	CheckRect(rect);
}

int CEffectEditorView::CheckBoundsAdjPosition(const CPoint &pt)
{
	int result = 0;
	Nuclear::CRECT rect = GetDocument()->GetRelativeBouningBox();
	rect.left += m_EffPosition.x;
	rect.top += m_EffPosition.y;
	rect.right += m_EffPosition.x;
	rect.bottom += m_EffPosition.y;
	CheckRect(rect);
	if (pt.y >= rect.top - MOUSE_NEAR && pt.y <= rect.bottom + MOUSE_NEAR)
	{
		if (abs(pt.x - rect.left) < MOUSE_NEAR)
		{
			result |= BAP_LEFT;
		} else if (abs(pt.x - rect.right) < MOUSE_NEAR)
		{
			result |= BAP_RIGHT;
		}
	}
	if (pt.x >= rect.left - MOUSE_NEAR && pt.x <= rect.right + MOUSE_NEAR)
	{
		if (abs(pt.y - rect.top) < MOUSE_NEAR)
		{
			result |= BAP_TOP;
		} else if (abs(pt.y - rect.bottom) < MOUSE_NEAR)
		{
			result |= BAP_BOTTOM;
		}
	}
	return result;
}

void CEffectEditorView::RenderEffect()
{
	m_pEffectManager->OnTick();
	m_pRenderer->Begin(m_BackgroundColor);

	if ( m_BgMode == BM_PICTURE)
	{
		if (m_BackgroundPicture != Nuclear::INVALID_PICTURE_HANDLE)
		{
			Nuclear::FRECT rect(m_BgPosition, m_fBgPicWidth, m_fBgPicHeight);
			m_pRenderer->DrawPicture(m_BackgroundPicture, rect, 0xFFFFFFFF);
		}
	}
	GetDocument()->Draw(m_pRenderer);
	Nuclear::XPCOLOR color(0xFFFF0000);
	if (m_bShowCenterCross)
	{
		m_pRenderer->DrawLine(m_EffPosition.x - 10.0f, static_cast<float>(m_EffPosition.y), 
			m_EffPosition.x + 10.0f, static_cast<float>(m_EffPosition.y), color);
		m_pRenderer->DrawLine(static_cast<float>(m_EffPosition.x), m_EffPosition.y - 10.0f, 
			static_cast<float>(m_EffPosition.x), m_EffPosition.y + 10.0f, color);
	}
	if (m_ToolsState == TS_FRAME_ROTATE || m_ToolsState == TS_FRAME_SIZE)
	{
		const EffectClipSet& selClip = GetDocument()->GetSelectedClips();
		if (selClip.size() == 1)
		{
			const Nuclear::Effect* pAni = NULL;
			if (GetDocument()->GetEffectFileType() == EFT_EFFECT)
				pAni = (*selClip.begin())->GetAnimation();
			else
				pAni = GetDocument()->GetBindEffect();
			if (pAni)
			{
				Nuclear::CRECT boundRect;
				if (pAni->GetType() != Nuclear::XPET_AUDIO)	
				{
					Nuclear::Location pos;
					boundRect = pAni->GetRelBouningBox();
					m_AdjPointArray[0].x = boundRect.left + static_cast<float>(boundRect.Width()) / 2;
					m_AdjPointArray[0].y = static_cast<float>(boundRect.top);
					m_AdjPointArray[1].x = static_cast<float>(boundRect.left);
					m_AdjPointArray[1].y = m_AdjPointArray[0].y;
					m_AdjPointArray[2].x = m_AdjPointArray[1].x;
					m_AdjPointArray[2].y = boundRect.top + static_cast<float>(boundRect.Height()) / 2;
					m_AdjPointArray[3].x = m_AdjPointArray[1].x;
					m_AdjPointArray[3].y = static_cast<float>(boundRect.bottom);
					m_AdjPointArray[4].x = m_AdjPointArray[0].x;
					m_AdjPointArray[4].y = m_AdjPointArray[3].y;
					m_AdjPointArray[5].x = static_cast<float>(boundRect.right);
					m_AdjPointArray[5].y = m_AdjPointArray[3].y;
					m_AdjPointArray[6].x = m_AdjPointArray[5].x;
					m_AdjPointArray[6].y = m_AdjPointArray[2].y;
					m_AdjPointArray[7].x = m_AdjPointArray[5].x;
					m_AdjPointArray[7].y = m_AdjPointArray[0].y;
					Nuclear::XPMATRIX3 m, mTmp;
					Nuclear::FPOINT scal = pAni->GetScale();
					Nuclear::XPMatrixScaling(&m, scal.x, scal.y);
					Nuclear::XPMatrixRotationOrigin(&mTmp, pAni->GetRotationRadian());
					m *= mTmp;
					Nuclear::XPVECTOR2 vOut;
					for (int i=0;i<8;++i)
					{
						Nuclear::XPVec3TransformCoord(&vOut, m_AdjPointArray + i, &m);
						m_AdjPointArray[i] = vOut;

						pos = pAni->GetLocation();
						pos.x = pos.x + static_cast<int>(pAni->GetTranslation().x);
						pos.y = pos.y + static_cast<int>(pAni->GetTranslation().y);
						m_AdjPointArray[i].x += pos.x;
						m_AdjPointArray[i].y += pos.y;
					}
					for (int i=1;i<8;++i)
					{
						m_pRenderer->DrawLine(m_AdjPointArray[i].x, m_AdjPointArray[i].y, 
							m_AdjPointArray[i-1].x, m_AdjPointArray[i-1].y, color);
					}
					m_pRenderer->DrawLine(m_AdjPointArray[7].x, m_AdjPointArray[7].y, 
						m_AdjPointArray[0].x, m_AdjPointArray[0].y, color);

					if (m_ToolsState == TS_FRAME_SIZE)
					{
						for (int i=0;i<8;++i)
						{
							m_pRenderer->DrawCircle(m_AdjPointArray[i].x, m_AdjPointArray[i].y, 2, color);
						}
					}
				}
			}
		}
	}

	if (m_bShowBox)
	{
		Nuclear::CRECT rect;
		if (m_ToolsState == TS_BOUNDS_RECT && m_bLMouseButtonDown)
		{
			switch (m_MouseMode)
			{
			case MM_NORMAL:
				rect.left = m_MouseDownPosition.x;
				rect.top = m_MouseDownPosition.y;
				rect.right = m_MousePosition.x;
				rect.bottom = m_MousePosition.y;
				break;
			case MM_BOUNDS_ADJUST_RECT:
				GetAdjBoundsRect(m_MousePosition, rect);
				rect.left += m_EffPosition.x;
				rect.right += m_EffPosition.x;
				rect.top += m_EffPosition.y;
				rect.bottom += m_EffPosition.y;
				break;
			}
		} else {
			rect = GetDocument()->GetRelativeBouningBox();
			rect.left += m_EffPosition.x;
			rect.right += m_EffPosition.x;
			rect.top += m_EffPosition.y;
			rect.bottom += m_EffPosition.y;
		}
		CheckRect(rect);
		m_pRenderer->DrawBox(rect.ToFRECT(), color, Nuclear::XPFM_WIREFRAME);
	}
	m_pRenderer->End();
}

bool CEffectEditorView::Tick()
{
	static DWORD tickTime = GetTickCount();
	DWORD tick = GetTickCount();
	DWORD delta = tick - tickTime;
	tickTime = tick;
	if (m_pSoundManager)
		m_pSoundManager->OnTick();
	if (theApp.GetFileIOManager())
		theApp.GetFileIOManager()->OnUpdate(9999999);
	GetDocument()->Tick(delta);
	return true;
}

bool CEffectEditorView::CheckAndCreateRenderer()
{
	if( m_pRenderer == NULL ) 
	{
		Nuclear::CRECT sourceRect(0, 0, m_ClientRect.right-m_ClientRect.left, m_ClientRect.bottom-m_ClientRect.top);
		CreateRenderer(&m_pRenderer, m_hWnd, Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)), GetFileIOManager());
		m_pRenderer->SetSourceRect(&sourceRect);
		m_BackgroundColor.data = GetPrivateProfileInt(L"BackGround", L"Color", m_BackgroundColor.data, theApp.GetCfgFilePath());
		m_BgMode = static_cast<eBackgroundMode>(GetPrivateProfileInt(L"BackGround", L"BGmode", BM_COLOR, theApp.GetCfgFilePath()));
		if (m_BgMode == BM_PICTURE)
		{
			wchar_t imgpath[255];
			GetPrivateProfileString(L"BackGround", L"ImgPath", L"", imgpath, 255, theApp.GetCfgFilePath());
			if (!SetBackgroundPicture(imgpath))
			{
				m_BgMode = BM_COLOR;
			}
		}
		if (m_TryOpenFilename.size() > 0)
		{

			GetDocument()->OnOpenDocument(m_TryOpenFilename.c_str());
			GetDocument()->SetSpritePosition(m_EffPosition);
			m_TryOpenFilename = L"";
		}
		m_pSoundManager = new Nuclear::DirectSoundManager(this);
		m_pSoundManager->Init(m_hWnd, DSSCL_PRIORITY);
		m_pSoundManager->SetPrimaryBufferFormat( 2, 22050, 16 );
		return true;
	}
	return false;
}

#ifdef _DEBUG
void CEffectEditorView::AssertValid() const
{
	CView::AssertValid();
}

void CEffectEditorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CEffectEditorDoc* CEffectEditorView::GetDocument() const 
{
	XPASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CEffectEditorDoc)));
	return (CEffectEditorDoc*)m_pDocument;
}
#endif 




void CEffectEditorView::OnEndPrinting(CDC* , CPrintInfo* )
{
}




void CEffectEditorView::OnBeginPrinting(CDC* , CPrintInfo* )
{
}

BOOL CEffectEditorView::OnPreparePrinting(CPrintInfo* pInfo)
{

	return DoPreparePrinting(pInfo);
}

void CEffectEditorView::OnDraw(CDC* )
{
	CEffectEditorDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	CheckAndCreateRenderer();
}




BOOL CEffectEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	return CView::PreCreateWindow(cs);
}



CEffectEditorView::~CEffectEditorView()
{
	GetDocument()->ReleaseRes();
	delete m_pEffectManager;
	delete m_pSpriteMgr;
	if (m_pAnimationManager)
		delete m_pAnimationManager;
	delete m_pConfigMgr;
	if (m_pSoundManager)
		delete m_pSoundManager;
	DestroyRenderer(m_pRenderer);
}

CEffectEditorView::CEffectEditorView()
{
	m_TryOpenFilename = L"";
	m_bInited = false;
	m_pRenderer = NULL;
	m_pAnimationManager = NULL;
	m_ToolsState = TS_ARROW;
	m_MouseMode = MM_NORMAL;
	m_bLMouseButtonDown = false;
	m_bRMouseButtonDown = false;
	m_bMMouseButtonDown = false;
	m_bShowCenterCross = true;
	m_bShowBox = true;
	m_nAdjPosition = 0;
	m_BackgroundColor.data = 0xFFCCCCCC;
	m_BackgroundPicture = Nuclear::INVALID_PICTURE_HANDLE;
	m_pEffectManager = new Nuclear::EffectManager(this);
	m_pEffectManager->PreInit();
	m_pSpriteMgr = new Nuclear::SpriteManager(this);
	m_pConfigMgr = new Nuclear::ConfigManager(this);
	m_pSoundManager = NULL;
	m_pConfigMgr->Init();
	m_pAnimationManager = new Nuclear::AniManager(this);
	if( !m_pAnimationManager->Init() ) 
	{
		XPLOG_INFO(L"动画管理器初始化失败 Init Faild.\n");
		return;
	}
	if (!m_pSpriteMgr->PreInit())
	{
		AfxMessageBox(L"精灵管理器初始化整体失败");
		return;
	}
	size_t SprCount = m_pSpriteMgr->GetInitCount();
	bool succ = true;
	for (size_t i=0;i<SprCount;++i)
	{
		if( m_pSpriteMgr->Init(i, 1) )
		{	
			XPLOG_ERROR(L"精灵管理器初始化部分失败，第 %d 号精灵有问题！", i);
			succ = false;
		}
	}
	if (!succ)
	{
		AfxMessageBox(L"精灵管理器初始化部分失败，请查看日志并且联系美术～");
	}
	if (!m_pSpriteMgr->PostInit())
	{
		AfxMessageBox(L"精灵管理器初始化失败");
		return;
	}
	theApp.SetEngine(this);
}

