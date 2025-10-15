

#include "stdafx.h"
#include <d3d9types.h>
#include "SpriteEditor.h"
#include "SpriteEditorView.h"
#include "SpriteEditorDoc.h"
#include "SelectActionDlg.h"
#include "mainfrm.h"
#include "..\engine\common\log.h"
#include "..\engine\common\util.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\common\fileiomanager.h"
#include "..\engine\renderer\renderer.h"
#include "..\engine\engine\configmanager.h"
#include "..\engine\sound\directsoundmanager.h"



const float XP_EFFECT_MAX_ALPHA = 0.8f;
const float XP_EFFECT_MIN_ALPHA = 0.2f;
const float XP_EFFECT_ALPHA_DELTA = (XP_EFFECT_MAX_ALPHA - XP_EFFECT_MIN_ALPHA) / Nuclear::XP_EFFECT_HALF_CYCLE;


IMPLEMENT_DYNCREATE(CSpriteEditorView, CView)

IMPLEMENT_DYNCREATE(CAnimationOpView, CFormView)

IMPLEMENT_DYNCREATE(CEquipSelectView, CFormView)

IMPLEMENT_DYNCREATE(CActionControlView, CFormView)

void CEquipSelectView::OnBnClickedChkFenKuai()
{
	int enable = ((CButton*)GetDlgItem(IDC_CHK_ENABLERS))->GetCheck();
	int fenkuai = ((CButton*)GetDlgItem(IDC_CHK_FENKUAI))->GetCheck();
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->ChangeDye(enable,fenkuai);
}
void CEquipSelectView::OnBnClickedChkEnableRS()
{
	int enable = ((CButton*)GetDlgItem(IDC_CHK_ENABLERS))->GetCheck();
	int fenkuai = ((CButton*)GetDlgItem(IDC_CHK_FENKUAI))->GetCheck();
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->ChangeDye(enable,fenkuai);
}

void CEquipSelectView::OnBnClickedBtnOpenRs()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);
	pDoc->OpenHSLDlg();
}

void CAnimationOpView::OnLbnSelchangeListBoxAnimation()
{
}
void CSpriteEditorView::OnUpdateTestSelectObject(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bTestObjSel);
}

void CSpriteEditorView::OnTestSelectObject()
{
	m_bTestObjSel = !m_bTestObjSel;
}

int CSpriteEditorView::GetAniDir(int dirMode, int ndir)
{
	switch (dirMode)
	{
	case Nuclear::XPANIDM_8:
		break;
	case Nuclear::XPANIDM_8USE1:
	case Nuclear::XPANIDM_8FROM1:
	case Nuclear::XPANIDM_B2FROM1:
		ndir = 0;
		break;
	case Nuclear::XPANIDM_8FROM5:
		if( ndir == Nuclear::XPDIR_BOTTOMLEFT )
			ndir = 3;
		else if( ndir == Nuclear::XPDIR_LEFT )
			ndir = 2;
		else if( ndir == Nuclear::XPDIR_TOPLEFT )
			ndir = 1;
		break;
	case Nuclear::XPANIDM_4:
		if( (ndir==Nuclear::XPDIR_TOP) || (ndir==Nuclear::XPDIR_TOPRIGHT) )
			ndir = 0;
		else if( (ndir==Nuclear::XPDIR_RIGHT) || (ndir==Nuclear::XPDIR_BOTTOMRIGHT) )
			ndir = 1;
		else if( (ndir==Nuclear::XPDIR_BOTTOM) || (ndir==Nuclear::XPDIR_BOTTOMLEFT) )
			ndir = 2;
		else
			ndir = 3;
		break;
	case Nuclear::XPANIDM_3:
		if( ndir==Nuclear::XPDIR_BOTTOMRIGHT )
			ndir = 0;
		else if( ndir==Nuclear::XPDIR_BOTTOM )
			ndir = 1;
		else if( ndir==Nuclear::XPDIR_BOTTOMLEFT )
			ndir = 2;
		else
			ndir = 0;
		break;
	case Nuclear::XPANIDM_2:
		if( ndir==Nuclear::XPDIR_BOTTOMRIGHT )
			ndir = 0;
		else if( ndir==Nuclear::XPDIR_TOPLEFT )
			ndir = 1;
		else
			ndir = 0;
		break;;

	case Nuclear::XPANIDM_4FROM2:
		if( ndir == Nuclear::XPDIR_BOTTOMLEFT )
			ndir = 0;
		else if( ndir == Nuclear::XPDIR_TOPRIGHT )
			ndir = 1;
		break;
	}

	return ndir;
}



float CSpriteEditorView::CalPointsDistance(CPoint pt1, CPoint pt2)
{
	float dis = powf((float)(pt1.x - pt2.x), 2.0f) + powf((float)(pt1.y - pt2.y), 2.0f);
	dis = sqrt(dis);
	return dis;
}

void CActionControlView::OnBnClickedSettingBase()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->SetAdMode(8);
	pDoc->m_bBaseOn = true;
	GetDlgItem(IDC_BTN_COPY)->EnableWindow(FALSE);
	GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->EnableWindow(FALSE);
}

BOOL CSpriteEditorView::OnMouseWheel(UINT nFlags, short zDelta, CPoint pt)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	if (pDoc->m_State == 1 && pDoc->m_nChunkSettingState == 1 && pDoc->m_pSpriteMaze) 
	{
		if (zDelta < 0)
		{
			--(pDoc->m_nBrushSize);
			if (pDoc->m_nBrushSize < 1)
				pDoc->m_nBrushSize = 1;
		} else {
			++(pDoc->m_nBrushSize);
			if (pDoc->m_nBrushSize > 20)
				pDoc->m_nBrushSize = 20;
		}
	}
	return CView::OnMouseWheel(nFlags, zDelta, pt);
}

void CActionControlView::OnBnClickedBtnCopy()
{
	CSelectActionDlg dlg;
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->GetSpriteManager()->DumpAction(pDoc->m_CurModelName, dlg.m_Actions);
	if (dlg.DoModal() == IDOK)
	{
		std::vector<size_t>::iterator it = dlg.m_SelectedActions.begin(), ie = dlg.m_SelectedActions.end();
		Nuclear::SpriteManager *pSprMan = pDoc->GetSpriteManager();
		for (;it!=ie;++it)
		{
			std::wstring &actName = dlg.m_Actions[*it];
			if (actName == pDoc->m_CurActionName)
				continue;
			const Nuclear::Action* pAction = pSprMan->GetAction(pDoc->m_CurModelName, actName);
			if (pAction)
			{
				Nuclear::Action act = *pAction;
				for (int i=0;i<8;++i)
				{
					act.SetBoundingBox(i, pDoc->m_CurAction.GetBoundingBox(i));
				}
				pSprMan->SetAction(pDoc->m_CurModelName, actName, act);
				std::wstring fpath = L"/model/"+pDoc->m_CurModelName+L"/action/" + actName + L".act";
				act.Save(fpath.c_str());
			}
		}
	}
}

void CActionControlView::OnBnClickedBtnApplyAllDir()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	Nuclear::CRECT rect = pDoc->m_CurAction.GetBoundingBox(pDoc->m_PlayState.m_iCurDir);
	for (int i=0;i<8;++i)
	{
		if (i != pDoc->m_PlayState.m_iCurDir)
		{
			pDoc->m_CurAction.SetBoundingBox(i, rect);
			pDoc->m_bDirty = true;
		}
	}
}

void CActionControlView::OnBnClickedSettingBoundingBox()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->SetAdMode(7);
	pDoc->m_bBorderOn = true;
	pDoc->m_PlayState.m_fCycle = false;
	GetDlgItem(IDC_BTN_COPY)->EnableWindow(TRUE);
	GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->EnableWindow(TRUE);
}

void CActionControlView::OnBnClickedButtonResetScale()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->m_nScale = 100;
	m_SilderScale.SetPos(100);
}

void CAnimationOpView::OnDraw(CDC* /*pDC*/)
{
	if (!m_bInited)
	{
		CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
		m_bInited = true;
		m_SliderVol.SetRange(0, 255);
		m_SliderFreq.SetRange(0, 255);
		m_SliderVol.SetPos(pDoc->m_nVol);
		m_SliderFreq.SetPos(pDoc->m_nFreq);
	}
}

void CAnimationOpView::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	switch (pScrollBar->GetDlgCtrlID())
	{
	case IDC_SLIDER_VOL:
		pDoc->m_nVol = m_SliderVol.GetPos();
		break;
	case IDC_SLIDER_FREQ:
		pDoc->m_nFreq = m_SliderFreq.GetPos();
		break;
	default:
		assert(false && L"(pScrollBar->GetDlgCtrlID())");
	}

	CFormView::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CAnimationOpView::OnBnClickedButton2()
{
	wchar_t olddir[MAX_PATH];
	GetCurrentDirectory(MAX_PATH, olddir);
	CFileDialog dlg(TRUE, NULL, NULL, OFN_FILEMUSTEXIST, L"声音文件 (*.*|*.*||)");
	std::wstring indir;

	PFS::CEnv::PFSFileNameToNativeFileName(L"/sound/a", indir);
	CString dir = indir.c_str();
	dir.Delete(dir.GetLength() - 1);
	dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
	if (dlg.DoModal() == IDOK)
	{
		std::wstring name;
		if (PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name))
		{
			CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
			pDoc->m_CurSoundName = name;
			GetDlgItem(IDC_SOUND_NAME)->SetWindowText(name.c_str());
		} else {
			AfxMessageBox(L"请选择正确路径（res\\sound）下面的声音文件！");
		}
	}
	SetCurrentDirectory(olddir);
}

void CSpriteEditorView::RenewSize()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);
	if( pDoc->m_pRenderer == NULL ) return;
	CRect rect;
	GetClientRect(&rect);
	Nuclear::CRECT sourceRect(0, 0, rect.right-rect.left, rect.bottom-rect.top);
	pDoc->m_pRenderer->SetSourceRect(&sourceRect);
}
void CSpriteEditorView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);
	RenewSize();
}

void CSpriteEditorView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);
	RenewSize();
}

void CActionControlView::OnBnClickedSettingEffectPosition()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->SetAdMode(5);
	GetDlgItem(IDC_BTN_COPY)->EnableWindow(FALSE);
	GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->EnableWindow(FALSE);
}

void CActionControlView::OnBnClickedSettingRide()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->SetAdMode(3);
	GetDlgItem(IDC_BTN_COPY)->EnableWindow(FALSE);
	GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->EnableWindow(FALSE);
}

void CActionControlView::OnBnClickedSettingNone()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->SetAdMode(0);
	GetDlgItem(IDC_BTN_COPY)->EnableWindow(FALSE);
	GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->EnableWindow(FALSE);
}

void CActionControlView::OnCbnSelchangeComboWaterType()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->GetSpriteManager()->SetWaterSurfaceType(pDoc->m_CurModelName, static_cast<Nuclear::WATER_SURFACE_TYPE>(m_ComboBoxWaterType.GetCurSel()));
}

void CActionControlView::OnBnClickedButtonTitlePos()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	pDoc->SetAdMode(1);
	GetDlgItem(IDC_BTN_COPY)->EnableWindow(FALSE);
	GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->EnableWindow(FALSE);
}

void CSpriteEditorView::OnLButtonUp(UINT nFlags, CPoint point)
{
	int oldBaseMode = m_adBaseMode;
	m_adBorderMode = 0;
	m_adBaseMode = 0;
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	ReleaseCapture();
	assert(pDoc);
	int adMode = pDoc->GetAdMode();
	float fscale = pDoc->m_nScale / 100.0f;

	CRect rect;
	GetClientRect(&rect);
	CPoint pt;
	pt.x = (LONG)((point.x - rect.Width() / 2) / fscale);
	pt.y = (LONG)((point.y - rect.Height() / 2) / fscale);
	switch (adMode)
	{
	case 9:
		{
			m_nCurSelMaskpt = -1;
			m_bModifyMask = false;
		}
		break;

	case 0:
	case 1:
	case 3:
	case 5:
	case 7:
		break;
	case 8:
		if( pDoc->m_State == 1 && !(nFlags&MK_CONTROL) )
		{
			if( oldBaseMode != 0 )
			{
				const Nuclear::XPBase &base = pDoc->GetSpriteManager()->GetBase(pDoc->m_CurModelName);
				pDoc->GetSpriteManager()->SetBase(pDoc->m_CurModelName, base, true);
			}
		}
		break;
	case 2:
		pDoc->SetAdMode(1);
		pDoc->AddTitleLocation(m_MouseDownPos.y - pt.y);
		break;
	case 4:
		pDoc->SetAdMode(3);
		pDoc->AddHorseHeight(m_MouseDownPos.y - pt.y);
		break;
	case 6:
		{
			CSize adj = pt - m_MouseDownPos;
			pDoc->SetAdMode(5);
			pDoc->AddEffectPosition(adj);
		}
	default:
		break;
	}

	if (pDoc->m_nChunkSettingState == 2)
	{
		CRect rect;
		if (GetAdjRect(pDoc->m_pSpriteMaze, rect))
		{
			CSpriteEditorDoc::ModifySpriteMazeRect(pDoc->m_pSpriteMaze, rect);
			pDoc->m_bMazeSizeDirty = true;
			pDoc->m_bMazeDirty = true;
		}
		pDoc->m_nChunkSettingState = 1;
	}
	CView::OnLButtonUp(nFlags, point);
}

bool GetMazeRect(const Nuclear::PSpriteMaze *pSpriteMaze, RECT &rect)
{
	if (pSpriteMaze == NULL)
		return false;
	int gridwidth = 24;
	int gridheight = 16;
	const Nuclear::CPOINT &ltpt = pSpriteMaze->GetLeftTopPos();
	rect.left = -ltpt.x;
	rect.right = rect.left + gridwidth * pSpriteMaze->GetWidth();
	rect.top = -ltpt.y;
	rect.bottom = rect.top + gridheight * pSpriteMaze->GetHeight();
	return true;
}

enum ADJ_RECT
{
	AR_TOP = 1,		
	AR_BOTTOM = 2,	
	AR_LEFT = 4,	
	AR_RIGHT = 8,	
};
const int MOUSE_NEAR = 3;

int CheckRectAdjPos(const CPoint &pt, const CRect &rect, float scale)
{
	int result = 0;
	int mousenear = (int)(MOUSE_NEAR / scale);
	if (pt.y >= rect.top - mousenear && pt.y <= rect.bottom + mousenear)
	{
		if (abs(pt.x - rect.left) < mousenear)
		{
			result |= AR_LEFT;
		} else if (abs(pt.x - rect.right) < mousenear)
		{
			result |= AR_RIGHT;
		}
	}
	if (pt.x >= rect.left - mousenear && pt.x <= rect.right + mousenear)
	{
		if (abs(pt.y - rect.top) < mousenear)
		{
			result |= AR_TOP;
		} else if (abs(pt.y - rect.bottom) < mousenear)
		{
			result |= AR_BOTTOM;
		}
	}
	return result;
}

void CSpriteEditorView::OnLButtonDown(UINT nFlags, CPoint point)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	SetCapture();
	assert(pDoc);
	int adMode = pDoc->GetAdMode();
	CRect rect;
	GetClientRect(&rect);
	float fscale = pDoc->m_nScale / 100.0f;

	m_MouseDownPos.x = (LONG)((point.x - rect.Width() / 2) / fscale);
	m_MouseDownPos.y = (LONG)((point.y - rect.Height() / 2) / fscale);
	switch (adMode)
	{
	case 9: 
		{
			m_bModifyMask = false;
			m_nCurSelMaskpt = -1;
			m_nCurRegion = 0;
			for(int k=0; k<pDoc->m_CurAni.GetRegionCount(); ++k) 
			{
				Nuclear::XAni::XPic pic;
				Nuclear::FPOINT mousePos;
				if( pDoc->m_CurAni.GetPic(pDoc->m_PlayState.m_iCurDir, 
					pDoc->m_PlayState.m_iCurFrame, k,pic) )
				{
					Nuclear::FRECT frect = pic.offset.ToFRECT();
					frect *= fscale;
					frect.left += rect.Width()/2.0f;
					frect.top += rect.Height()/2.0f;
					mousePos.x = point.x - frect.left;
					mousePos.y = point.y - frect.top;
				}

				std::vector<Nuclear::CPOINT> outLinePoints = pic.outLine.m_points;
				int cnt = (int)( outLinePoints.size() );
				for(int i=0; i<cnt; i++)
				{
					outLinePoints[i] *= fscale;
					float fdis2 = (float)( outLinePoints[i].Dis2( Nuclear::CPOINT(mousePos) ) ); 
					if(fdis2 < 10)
					{
						m_bModifyMask = true;
						m_nCurSelMaskpt = i;

						m_nCurRegion = k;

						pDoc->m_bAniDirty = true;
						pDoc->m_bModifyOutLine = true;

						break;
					}
				}

				if(m_nCurSelMaskpt != -1)
				{
					Invalidate(true);
					break;
				}
			}
		}
		break;

	case 0:
	case 7:
	case 8:
		if (pDoc->m_State == 1 && pDoc->m_nChunkSettingState != 0) {
			if (pDoc->m_pSpriteMaze)
			{
				switch (pDoc->m_nChunkSettingState)
				{
				case 1:
					{
						CRect mazerect;
						GetMazeRect(pDoc->m_pSpriteMaze, mazerect);
						m_nAdjPosition = CheckRectAdjPos(m_MouseDownPos, mazerect, fscale);
						if (ResetMouseCurData())
						{
							SetCursor(m_hMouseCursor);
							pDoc->m_nChunkSettingState = 2;
						} else {
							int gridwidth = 24;
							int gridheight = 16;

							int dx = (m_MouseDownPos.x - mazerect.left) / gridwidth;
							int dy = (m_MouseDownPos.y - mazerect.top) / gridheight;
							if (mazerect.PtInRect(m_MouseDownPos))
							{
								char &maze = pDoc->m_pSpriteMaze->GetMaze()[dy * pDoc->m_pSpriteMaze->GetWidth() + dx];
								switch (maze)
								{
								case 0:
								case 1:
									maze = 3;
									pDoc->m_ChunkState = 3;
									pDoc->m_bMazeDirty = true;
									break;
								case 3:
									maze = 1;
									pDoc->m_ChunkState = 1;
									pDoc->m_bMazeDirty = true;
									break;
								default:
									XPASSERT(FALSE);
									break;
								}
							} else {
								pDoc->m_ChunkState = 3;
							}
							if (pDoc->m_nBrushSize > 1)
							{
								char *pMaze;
								int bx, x, y, r, b;
								bx = dx - pDoc->m_nBrushSize / 2;
								y = dy - pDoc->m_nBrushSize / 2;
								r = bx + pDoc->m_nBrushSize;
								b = y + pDoc->m_nBrushSize;
								if (bx < 0)
									bx = 0;
								if (y < 0)
									y = 0;
								if (r > pDoc->m_pSpriteMaze->GetWidth())
									r = pDoc->m_pSpriteMaze->GetWidth();
								if (b > pDoc->m_pSpriteMaze->GetHeight())
									b = pDoc->m_pSpriteMaze->GetHeight();
								for (;y<b;++y)
								{
									pMaze = pDoc->m_pSpriteMaze->GetMaze() + y * pDoc->m_pSpriteMaze->GetWidth() + bx;
									for (x=bx;x<r;++x,++pMaze)
									{
										if (*pMaze != pDoc->m_ChunkState)
										{
											*pMaze = pDoc->m_ChunkState;
											pDoc->m_bMazeDirty = true;
										}
									}
								}
							}
						}
					}
					break;
				case 2:
					XPASSERT(false && L"LButtonDown m_nBlockSettingState Error!");
					break;
				}

			}
		} else {
			if( !(nFlags&MK_CONTROL) )
			{
				if (pDoc->m_bBaseOn)
				{
					CRect rect0, rect2;
					rect0.SetRect(m_adBase0.x-4,m_adBase0.y-4,m_adBase0.x+4,m_adBase0.y+4);
					rect2.SetRect(m_adBase2.x-4,m_adBase2.y-4,m_adBase2.x+4,m_adBase2.y+4);


					if( rect0.PtInRect(m_MouseDownPos) )
					{
						m_adBaseMode = 1;
					}
					else if( rect2.PtInRect(m_MouseDownPos) )
					{
						m_adBaseMode = 2;
					}
				}
				if (pDoc->m_bBorderOn)
				{
					CRect rectlt, rectrb;
					rectlt.SetRect(m_adBorderLT.x-4,m_adBorderLT.y-4,m_adBorderLT.x+4,m_adBorderLT.y+4);
					rectrb.SetRect(m_adBorderRB.x-4,m_adBorderRB.y-4,m_adBorderRB.x+4,m_adBorderRB.y+4);

					if( rectlt.PtInRect(m_MouseDownPos) )
					{
						m_adBorderMode = 1;
						m_adBaseMode = 0;
					}
					else if( rectrb.PtInRect(m_MouseDownPos) )
					{
						m_adBorderMode = 2;
						m_adBaseMode = 0;
					}
				}
			}
			if (nFlags & MK_SHIFT)
			{
				pDoc->SpriteRunTo(point.x, point.y);
			} else {
				pDoc->SelectSprite(static_cast<float>(point.x), static_cast<float>(point.y));
			}

		}
		break;
	case 1:
	case 2:
	case 3:
	case 4:
	case 5:
	case 6:
		assert(adMode==1 || adMode == 3 || adMode == 5);
		pDoc->SetAdMode(adMode+1);
		break;
	}
	CView::OnLButtonDown(nFlags, point);
}

void CSpriteEditorView::OnRButtonDown(UINT nFlags, CPoint point)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);
	if (pDoc->m_State == 1 && pDoc->m_nChunkSettingState == 1) 
	{
		if (pDoc->m_pSpriteMaze)
		{
			CRect clientrect;
			GetClientRect(&clientrect);
			float fscale = pDoc->m_nScale / 100.0f;
			POINT pot;
			pot.x = (LONG)((point.x - clientrect.Width() / 2) / fscale);
			pot.y = (LONG)((point.y - clientrect.Height() / 2) / fscale);
			int gridwidth = 24;
			int gridheight = 16;
			Nuclear::CPOINT pt = pDoc->m_pSpriteMaze->GetLeftTopPos();
			char *pMaze;
			int bx, x, y, r, b;
			bx = (pot.x + pt.x) / gridwidth - pDoc->m_nBrushSize / 2;
			y = (pot.y + pt.y) / gridheight - pDoc->m_nBrushSize / 2;
			r = bx + pDoc->m_nBrushSize;
			b = y + pDoc->m_nBrushSize;
			if (bx < 0)
				bx = 0;
			if (y < 0)
				y = 0;
			if (r > pDoc->m_pSpriteMaze->GetWidth())
				r = pDoc->m_pSpriteMaze->GetWidth();
			if (b > pDoc->m_pSpriteMaze->GetHeight())
				b = pDoc->m_pSpriteMaze->GetHeight();
			for (;y<b;++y)
			{
				pMaze = pDoc->m_pSpriteMaze->GetMaze() + y * pDoc->m_pSpriteMaze->GetWidth() + bx;
				for (x=bx;x<r;++x,++pMaze)
				{
					if (*pMaze != 0)
					{
						*pMaze = 0;
						pDoc->m_bMazeDirty = true;
					}
				}
			}
		}
	} else {
		pDoc->RotateSprite();
	}
	CView::OnRButtonDown(nFlags, point);
}

void CAnimationOpView::OnCbnSelChangeComboBoxCurAniGroup()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);

	int cursel = m_ComboBoxCurAnimationGroup.GetCurSel();
	if( cursel < 0 ) return;
	CString str;
	m_ComboBoxCurAnimationGroup.GetLBText(cursel,str);
	std::wstring newmodelname = str;
	if( newmodelname == pDoc->m_CurModelName ) return;

	pDoc->ChangeModel(newmodelname);
}

void CAnimationOpView::OnBnClickedBtnRenameAnimation()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);

	if( -1 != m_ListBoxAnimation.GetCurSel())
	{
		wchar_t buf[256];
		m_ListBoxAnimation.GetText(m_ListBoxAnimation.GetCurSel(),buf);
		if( pDoc->m_CurActionName != buf )
		{
			std::wstring str = std::wstring(L"确认要修改动作 ") + buf + L" 的名称吗?";
			if( IDNO == ::MessageBox(NULL, str.c_str(), L"提示", MB_YESNO))
				return;
			pDoc->RenameAnimation(buf);
		}
		else
			::MessageBox(NULL,L"不可以修改当前动作的名称",NULL,0);
	}
}

bool CSpriteEditorView::GetAdjRect(const Nuclear::PSpriteMaze *pSpriteMaze, RECT& rect)
{
	if (!GetMazeRect(pSpriteMaze, rect))
		return false;
	int gridwidth = 24;
	int gridheight = 16;
	int deltax, deltay;
	deltax = (m_MouseNowPos.x - m_MouseDownPos.x) / gridwidth * gridwidth;
	deltay = (m_MouseNowPos.y - m_MouseDownPos.y) / gridheight * gridheight;
	if (m_nAdjPosition & AR_TOP)
	{
		rect.top += deltay;
	} else if (m_nAdjPosition & AR_BOTTOM)
	{
		rect.bottom += deltay;
	}
	if (m_nAdjPosition & AR_LEFT)
	{
		rect.left += deltax;
	} else if (m_nAdjPosition & AR_RIGHT)
	{
		rect.right += deltax;
	}

	return true;
}

void CSpriteEditorView::OnMouseMove(UINT nFlags, CPoint point)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	CRect rect;
	GetClientRect(&rect);
	float fscale = pDoc->m_nScale / 100.0f;

	m_MouseNowPos.x = (LONG)((point.x - rect.Width() / 2) / fscale);
	m_MouseNowPos.y = (LONG)((point.y - rect.Height() / 2) / fscale);
	assert(pDoc);
	if( (nFlags&MK_LBUTTON) && (nFlags&MK_CONTROL))
	{
		if( pDoc->m_CurSprite != -1 )
		{
			pDoc->SpriteMoveTo(static_cast<float>(point.x), static_cast<float>(point.y));
		}
	}
	if (pDoc->m_State == 1 && pDoc->m_pSpriteMaze)
	{
		switch (pDoc->m_nChunkSettingState)
		{
		case 0:
			break;
		case 1:
			if (nFlags&MK_LBUTTON || nFlags&MK_RBUTTON)
			{
				char brush = (nFlags&MK_LBUTTON)?pDoc->m_ChunkState:0;
				int gridwidth = 24;
				int gridheight = 16;
				Nuclear::CPOINT ltpt = pDoc->m_pSpriteMaze->GetLeftTopPos();
				int dx = (m_MouseNowPos.x + ltpt.x) / gridwidth;
				int dy = (m_MouseNowPos.y + ltpt.y) / gridheight;
				char *pMaze;
				int bx, x, y, r, b;
				bx = dx - pDoc->m_nBrushSize / 2;
				y = dy - pDoc->m_nBrushSize / 2;
				r = bx + pDoc->m_nBrushSize;
				b = y + pDoc->m_nBrushSize;
				if (bx < 0)
					bx = 0;
				if (y < 0)
					y = 0;
				if (r > pDoc->m_pSpriteMaze->GetWidth())
					r = pDoc->m_pSpriteMaze->GetWidth();
				if (b > pDoc->m_pSpriteMaze->GetHeight())
					b = pDoc->m_pSpriteMaze->GetHeight();
				for (;y<b;++y)
				{
					pMaze = pDoc->m_pSpriteMaze->GetMaze() + y * pDoc->m_pSpriteMaze->GetWidth() + bx;
					for (x=bx;x<r;++x,++pMaze)
					{
						if (*pMaze != brush)
						{
							*pMaze = brush;
							pDoc->m_bMazeDirty = true;
						}
					}
				}
			} else {
				CRect rect;
				if (GetMazeRect(pDoc->m_pSpriteMaze, rect))
				{
					m_nAdjPosition = CheckRectAdjPos(m_MouseNowPos, rect, fscale);
					if (ResetMouseCurData())
					{
						SetCursor(m_hMouseCursor);
					}
				}
			}
			break;
		case 2:
			if (nFlags&MK_LBUTTON)
			{
				if (m_nAdjPosition != 0)
				{
					SetCursor(m_hMouseCursor);
				}
			}
			break;
		default:
			XPASSERT(false && L"pDoc->m_nBlockSettingState Error!");
			break;
		}
	}
	if( !(nFlags&MK_CONTROL) )
	{
		if( m_adBorderMode != 0 )
		{
			if( m_adBorderMode == 1 )
			{
				pDoc->AdjustBorder(m_MouseNowPos.x - m_adBorderLT.x, m_MouseNowPos.y - m_adBorderLT.y, 0, 0);
			}
			else if( m_adBorderMode == 2 )
			{
				pDoc->AdjustBorder(0, 0, m_MouseNowPos.x - m_adBorderRB.x, m_MouseNowPos.y - m_adBorderRB.y);
			}
		}

		if( m_adBaseMode != 0 )
		{
			if( m_adBaseMode == 1 )
			{
				pDoc->AdjustBase(m_MouseNowPos.x - m_adBase0.x, m_MouseNowPos.y - m_adBase0.y, 0, 0);
			}
			else if( m_adBaseMode == 2 )
			{
				pDoc->AdjustBase(0, 0, m_MouseNowPos.x - m_adBase2.x, m_MouseNowPos.y - m_adBase2.y);
			}
		}
	}


	int nAdMode = pDoc->GetAdMode();

	if( (nAdMode==9) && (nFlags&MK_LBUTTON) )
	{
		if( (m_nCurSelMaskpt!=-1) && m_bModifyMask && (GetCapture()==this) )
		{
			::SetCursor(AfxGetApp()->LoadStandardCursor(IDC_SIZEALL));
			{
				Nuclear::XAni::XPic pic;
				Nuclear::FPOINT mousePos;
				Nuclear::FRECT frect;
				if( pDoc->m_CurAni.GetPic(pDoc->m_PlayState.m_iCurDir, 
					pDoc->m_PlayState.m_iCurFrame, m_nCurRegion, pic) ) 
				{
					frect = pic.offset.ToFRECT();

					frect *= fscale;
					frect.left += rect.Width()/2.0f;
					frect.top += rect.Height()/2.0f;
					mousePos.x = point.x - frect.left;
					mousePos.y = point.y - frect.left;
				}

				std::vector<Nuclear::CPOINT>& outLinePoints = pic.outLine.m_points;


				int dx = (int)( point.x - outLinePoints[m_nCurSelMaskpt].x *fscale - frect.left );
				int dy = (int)( point.y - outLinePoints[m_nCurSelMaskpt].y *fscale - frect.top );

				outLinePoints[m_nCurSelMaskpt].x += dx;
				outLinePoints[m_nCurSelMaskpt].y += dy;

				pDoc->m_CurAni.SetPic(pDoc->m_PlayState.m_iCurDir,
					pDoc->m_PlayState.m_iCurFrame, m_nCurRegion, pic);
			}
		}
		else if(m_bTestObjSel)
		{
			m_nCurRegion = 0;
			for(int k=0; k<pDoc->m_CurAni.GetRegionCount(); ++k) 
			{
				Nuclear::XAni::XPic pic;
				Nuclear::FPOINT mousePos;
				if( pDoc->m_CurAni.GetPic(pDoc->m_PlayState.m_iCurDir, 
					pDoc->m_PlayState.m_iCurFrame, k,pic) )
				{
					Nuclear::FRECT frect = pic.offset.ToFRECT();
					frect *= fscale;
					frect.left += rect.Width()/2.0f;
					frect.top += rect.Height()/2.0f;
					mousePos.x = point.x - frect.left;
					mousePos.y = point.y - frect.top;
				}

				std::vector<Nuclear::CPOINT> outLinePoints = pic.outLine.m_points;
				Nuclear::XPPolygon polygon;
				int npts = (int)( outLinePoints.size() );
				for(int i=0; i<npts; i++)
				{
					outLinePoints[i] *= fscale;
					polygon.m_points.push_back( Nuclear::CPOINT(outLinePoints[i]) );
				}

				m_bSelObj = polygon.IsPointIn((int)(mousePos.x), (int)(mousePos.y));
				if(m_bSelObj)
				{
					m_nCurRegion = k;
					break;
				}
			}
		}
		else
		{
		}
	}
}

void CAnimationOpView::OnContextMenu(CWnd* /*pWnd*/, CPoint /*point*/)
{
	CMenu menu;
	menu.LoadMenu(IDR_MENU_CONTEXT);
	CMenu *m1 = menu.GetSubMenu(0);
	POINT p;
	GetCursorPos(&p);
	m1->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, p.x, p.y, this);
}
void CEquipSelectView::OnBnClickedBtnCcAni()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);
	CColorDialog   dlg;  
	dlg.DoModal();  
	DWORD bgr = dlg.GetColor();
	pDoc->m_AniColor = D3DCOLOR_ARGB(0xff,bgr&0xff,(bgr>>8)&0xff,(bgr>>16)&0xff);
}


void CAnimationOpView::OnBnClickedBtnDeleteAnimation()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);

	if( -1 != m_ListBoxAnimation.GetCurSel())
	{
		wchar_t buf[256];
		m_ListBoxAnimation.GetText(m_ListBoxAnimation.GetCurSel(),buf);
		if( pDoc->m_CurActionName != buf )
		{
			std::wstring str = std::wstring(L"确认要删除动作 ") + buf + L" 吗?";
			if( IDNO == ::MessageBox(NULL, str.c_str(), L"提示", MB_YESNO))
				return;
			pDoc->DeleteAnimation(buf);
		}
		else
			::MessageBox(NULL,L"不可以删除当前动作",NULL,0);
	}
}

void CAnimationOpView::OnBnClickedBtnOpenAnimation()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);
	pDoc->OpenAnimationFile();
}

void CAnimationOpView::OnBnClickedBtnNewAnimation()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	pDoc->OnAnimationNew();
}

void CSpriteEditorView::OnClose()
{
	CView::OnClose();
}

void CSpriteEditorView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);

	if( (GetKeyState(VK_SPACE)&0x8000))
	{
		switch( nChar )
		{
		case VK_UP:
			pDoc->AdjustBaseCenter(0,-1);
			break;
		case VK_DOWN:
			pDoc->AdjustBaseCenter(0,1);
			break;
		case VK_LEFT:
			pDoc->AdjustBaseCenter(-1,0);
			break;
		case VK_RIGHT:
			pDoc->AdjustBaseCenter(1,0);
			break;
		default:
			break;
		}
	}

	else if( (GetKeyState(VK_CONTROL) &0x8000))
	{
		switch( nChar )
		{
		case VK_UP:
			pDoc->AdjustOffset(0,1);
			break;
		case VK_DOWN:
			pDoc->AdjustOffset(0,-1);
			break;
		case VK_LEFT:
			pDoc->AdjustOffset(1,0);
			break;
		case VK_RIGHT:
			pDoc->AdjustOffset(-1,0);
			break;
		default:
			break;
		}
	}
	else
	{
		switch (pDoc->GetAdMode())
		{
		case 1:
			switch (nChar)
			{
			case VK_UP:
				pDoc->AddTitleLocation(1);
				break;
			case VK_DOWN:
				pDoc->AddTitleLocation(-1);
				break;
			}
			break;
		case 3:
			switch (nChar)
			{
			case VK_UP:
				pDoc->AddHorseHeight(1);
				break;
			case VK_DOWN:
				pDoc->AddHorseHeight(-1);
				break;
			}
			break;
		case 5:
			switch( nChar )
			{
			case VK_UP:
				pDoc->AddEffectPosition(CPoint(0, -1));
				break;
			case VK_DOWN:
				pDoc->AddEffectPosition(CPoint(0, 1));
				break;
			case VK_LEFT:
				pDoc->AddEffectPosition(CPoint(-1, 0));
				break;
			case VK_RIGHT:
				pDoc->AddEffectPosition(CPoint(1, 0));
				break;
			default:
				break;
			}
			break;
		default:
			switch( nChar )
			{
			case VK_UP:
				pDoc->SpriteMove(0,-1.f);
				break;
			case VK_DOWN:
				pDoc->SpriteMove(0,1.f);
				break;
			case VK_LEFT:
				pDoc->SpriteMove(-1.f,0);
				break;
			case VK_RIGHT:
				pDoc->SpriteMove(1.f,0);
				break;
			default:
				break;
			}
		}

	}


	switch( nChar )
	{
	case VK_INSERT:
		pDoc->AddSprite();
		break;
	case VK_DELETE:
		pDoc->DeleteSprites();
		break;
	case VK_PRIOR:
		pDoc->UpSprite();
		break;
	case VK_NEXT:
		pDoc->DownSprite();
		break;
	case VK_TAB:
		pDoc->SwitchSprite();
		break;
	default:
		break;
	}

	CView::OnKeyDown(nChar, nRepCnt, nFlags);
}

void CEquipSelectView::OnBnClickedBtnNewAnimation()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	pDoc->OnAnimationNew();
}

#define M_ACV_IMP_1(i) void CEquipSelectView::OnBnClickedChkShowL##i() \
{ \
	UpdateData(TRUE); \
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument(); \
	assert(pDoc); \
	pDoc->ShowEquipment(i-1, m_bShowEcArr[i-1] == TRUE); \
} \
	void CEquipSelectView::OnBnClickedBtnNewL##i() \
{ \
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument(); \
	assert(pDoc); \
	pDoc->NewEquipment(i-1); \
} \
	void CEquipSelectView::OnBnClickedBtnReloadL##i() \
{ \
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument(); \
	assert(pDoc); \
	pDoc->ReloadEquipment(i-1);\
} \
	void CEquipSelectView::OnBnClickedBtnDelL##i() \
{ \
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument(); \
	assert(pDoc); \
	pDoc->DeleteEquipment(i-1); \
} \
	void CEquipSelectView::OnBnClickedBtnCcL##i() \
{ \
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument(); \
	assert(pDoc); \
	pDoc->ChangeEquipmentColor(i-1); \
} \
	void CEquipSelectView::OnCbnSelchangeCmbCurL##i() \
{ \
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument(); \
	assert(pDoc); \
	if( m_cmbCurL##i.GetCount() <= 0 ) return; \
	int j = m_cmbCurL##i.GetCurSel(); \
	if( j< 0 ) return; \
	CString str; \
	m_cmbCurL##i.GetLBText(j,str); \
	pDoc->SelectEquipment(i-1, std::wstring(str)); \
}

M_ACV_IMP_1(1);
M_ACV_IMP_1(2);
M_ACV_IMP_1(3);
M_ACV_IMP_1(4);
M_ACV_IMP_1(5);
M_ACV_IMP_1(6);
M_ACV_IMP_1(7);
M_ACV_IMP_1(8);
M_ACV_IMP_1(9);
M_ACV_IMP_1(10);
M_ACV_IMP_1(11);
M_ACV_IMP_1(12);

void CActionControlView::OnBnClickedBtnEvTime()
{
	UpdateData(TRUE);
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	if( pDoc->m_State ==1 )
	{
		int n = pDoc->m_CurAction.GetFrameCount();
		for(int i=0; i<n; ++i)
		{
			pDoc->m_CurAction.SetFrameTime(i, 1);
			pDoc->m_CurAction.SetFrameStartTime(i, 1.f/n * i);
		}

		pDoc->m_bDirty = true;
		m_iTime = 1;
		UpdateData(FALSE);
	}
}

void CActionControlView::OnBnClickedBtnSetTime()
{
	UpdateData(TRUE);
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	if( pDoc->m_State ==1 )
	{
		if( m_iTime >0 && m_iTime <= 64 )
		{
			pDoc->m_CurAction.SetFrameTime(pDoc->m_PlayState.m_iCurFrame, m_iTime);

			int n = pDoc->m_CurAction.GetFrameCount();
			int total = 0, i;
			for(i=0; i<n; ++i)
			{
				total += pDoc->m_CurAction.GetFrameTime(i);
			}
			int tmp = 0;
			for(i=0; i<n; ++i)
			{
				pDoc->m_CurAction.SetFrameStartTime(i, tmp/(float)total);
				tmp += pDoc->m_CurAction.GetFrameTime(i);
			}

			pDoc->m_bDirty = true;
		}
	}
}

void CActionControlView::OnBnClickedBtnApplyAll()
{
	UpdateData(TRUE);
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	if( pDoc->m_CurModelName == L"" ) return;
	Nuclear::SpriteManager *pSprMan = pDoc->GetSpriteManager();
	if( pSprMan == NULL ) return;
	int nec = pSprMan->GetLayerCount(pDoc->m_CurModelName);
	if( pDoc->m_State ==1 )
	{
		if( m_ListBoxEcOrder.GetCount()>0)
		{
			int sel = m_ListBoxEcOrder.GetCurSel();
			if( sel >= 0 )
			{
				int n = pDoc->m_CurAction.GetFrameCount();
				for(int i=0; i<n; ++i)
					pDoc->m_CurAction.SetFrameOrder(i, pDoc->m_PlayState.m_iCurDir, m_ecOrders, nec);
				pSprMan->SetAction(pDoc->m_CurModelName, pDoc->m_CurActionName, pDoc->m_CurAction);
				pDoc->m_bDirty = true;

			}
		}
	}
}

void CActionControlView::OnBnClickedBtnSaveAni()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	pDoc->Save(false,true,-1);
}

void CActionControlView::OnBnClickedBtnMoveDown()
{
	UpdateData(TRUE);
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	if( pDoc->m_CurModelName == L"" ) return;
	Nuclear::SpriteManager *pSprMan = pDoc->GetSpriteManager();
	if( pSprMan == NULL ) return;
	int nec = pSprMan->GetLayerCount(pDoc->m_CurModelName);
	if( pDoc->m_State == 1 )
	{
		if( m_ListBoxEcOrder.GetCount()>0)
		{
			int sel = m_ListBoxEcOrder.GetCurSel();
			if( sel >= 0 )
			{
				if( sel < nec-1 )
				{
					std::swap(m_ecOrders[sel], m_ecOrders[sel+1]);
					pDoc->m_CurAction.SetFrameOrder(pDoc->m_PlayState.m_iCurFrame, pDoc->m_PlayState.m_iCurDir, m_ecOrders, nec);
					pSprMan->SetAction(pDoc->m_CurModelName, pDoc->m_CurActionName, pDoc->m_CurAction);
					pDoc->m_bDirty = true;

					m_ListBoxEcOrder.ResetContent();
					for(int i=0; i<nec;++i)
						m_ListBoxEcOrder.AddString(pDoc->m_pSpriteMgr->GetLayerDes(pDoc->m_CurModelName, m_ecOrders[i]).c_str());
					m_ListBoxEcOrder.SetCurSel(sel+1);
					m_ListBoxEcOrder.SetFocus();
					m_ListBoxEcOrder.UpdateData(FALSE);

				}
			}
		}
	}
}

void CActionControlView::OnBnClickedBtnMoveUp()
{
	UpdateData(TRUE);
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	if( pDoc->m_CurModelName == L"" ) return;
	Nuclear::SpriteManager *pSprMan = pDoc->GetSpriteManager();
	if( pSprMan == NULL ) return;
	int nec = pSprMan->GetLayerCount(pDoc->m_CurModelName);

	if( pDoc->m_State ==1 )
	{
		if( m_ListBoxEcOrder.GetCount()>0)
		{
			int sel = m_ListBoxEcOrder.GetCurSel();
			if( sel >= 0 )
			{
				if( sel > 0 )
				{

					std::swap(m_ecOrders[sel], m_ecOrders[sel-1]);

					pDoc->m_CurAction.SetFrameOrder(pDoc->m_PlayState.m_iCurFrame, pDoc->m_PlayState.m_iCurDir, m_ecOrders, nec);
					pDoc->m_bDirty = true;
					pSprMan->SetAction(pDoc->m_CurModelName, pDoc->m_CurActionName, pDoc->m_CurAction);

					m_ListBoxEcOrder.ResetContent();
					for(int i=0; i<nec;++i)
						m_ListBoxEcOrder.AddString(pSprMan->GetLayerDes(pDoc->m_CurModelName, m_ecOrders[i]).c_str());
					m_ListBoxEcOrder.SetCurSel(sel-1);
					m_ListBoxEcOrder.SetFocus();
					m_ListBoxEcOrder.UpdateData(FALSE);
				}
			}
		}
	}
}

void CActionControlView::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	switch (pScrollBar->GetDlgCtrlID())
	{
	case IDC_SBR_FRAME:
		UpdateData(TRUE);
		assert(pDoc);

		if( pDoc->m_State )
		{
			int n = pDoc->m_nFrame;
			int iFrame = m_ScrollBarFrame.GetScrollPos();
			switch( nSBCode )
			{
			case 0:
				if( --iFrame < 0 )
					iFrame = 0;
				break;
			case 1:
				if( ++iFrame >= n )
					iFrame = n-1;
				break;
			case 2:
				iFrame -= 4;
				if( iFrame < 0 )
					iFrame = 0;
				break;
			case 3:
				iFrame += 4;
				if( iFrame >= n )
					iFrame = n-1;
				break;
			default:
				break;

			}
			if( iFrame != pDoc->m_PlayState.m_iCurFrame )
			{
				pDoc->PlayFrame(iFrame);
			}
		}
		break;
	case IDC_SLIDER_SCALE:
		pDoc->m_nScale = m_SilderScale.GetPos();
		break;
	}
	CFormView::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CActionControlView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();

	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	m_ComboBoxWaterType.Clear();
	m_ComboBoxWaterType.AddString(L"永远水平");
	m_ComboBoxWaterType.AddString(L"顺精灵方向");
	m_ComboBoxWaterType.AddString(L"逆精灵方向");
	m_SilderScale.SetRange(1, 200);
	m_SilderScale.SetPos(100);
}

void CActionControlView::OnBnClickedRadio()
{
	UpdateData(TRUE);
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	if( m_Dirs1 != pDoc->m_PlayState.m_iCurDir )
	{
		pDoc->PlayDir(m_Dirs1);
	}
}

void CActionControlView::Update()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	Nuclear::SpriteManager *pSprMan = pDoc->GetSpriteManager();
	if( pSprMan == NULL ) return;
	int nec = pSprMan->GetLayerCount(pDoc->m_CurModelName);

	m_ListBoxEcOrder.ResetContent();
	for(int i=0; i<nec;++i)
		m_ListBoxEcOrder.AddString(pSprMan->GetLayerName(pDoc->m_CurModelName, i).c_str());

	m_Dirs1 =pDoc->m_PlayState.m_iCurDir;

	if( pDoc->m_State == 2 )
	{
		GetDlgItem(IDC_STATIC_FRAME_CENTER)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_STATIC_C1)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_STATIC_OFFY)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_STATIC_OFFX)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_EDT_OFFX)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_EDT_OFFY)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_OTHER_GROUP)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BUTTON_TITLE_POS)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_TITLE_Y_POS)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_SETTING_NONE)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_SETTING_RIDE)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_SETTING_EFFECT_POS)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_SETTING_BOUNDING_BOX)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_MODEL_WATER_TYPE_LAB)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_SETTING_BASE)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_COPY)->ShowWindow(SW_HIDE);
		m_ComboBoxWaterType.ShowWindow(SW_HIDE);
	}
	else
	{
		GetDlgItem(IDC_STATIC_FRAME_CENTER)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_STATIC_C1)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_STATIC_OFFY)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_STATIC_OFFX)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_EDT_OFFX)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_EDT_OFFY)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_OTHER_GROUP)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BUTTON_TITLE_POS)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_TITLE_Y_POS)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_SETTING_NONE)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_SETTING_RIDE)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_SETTING_EFFECT_POS)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_SETTING_BOUNDING_BOX)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_MODEL_WATER_TYPE_LAB)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_COPY)->ShowWindow(SW_SHOW);
		if (pDoc->GetAdMode() == 7)
		{
			GetDlgItem(IDC_BTN_COPY)->EnableWindow(TRUE);
			GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->EnableWindow(TRUE);
		} else {
			GetDlgItem(IDC_BTN_COPY)->EnableWindow(FALSE);
			GetDlgItem(IDC_BTN_APPLY_ALL_DIR)->EnableWindow(FALSE);
		}
		m_ComboBoxWaterType.ShowWindow(SW_SHOW);
		m_ComboBoxWaterType.SetCurSel(pDoc->GetSpriteManager()->GetWaterSurfaceType(pDoc->m_CurModelName));
	}

	if( pDoc->m_State == 1 )
	{
		GetDlgItem(IDC_STATIC_FRAME_ORDER)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_LST_ECORDER)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_MOVEUP)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_MOVEDOWN)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_APPLYALL)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_STATIC_FRAME_TIME)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_EDT_FRAMETIME)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_SETTIME)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_EVTIME)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_SETTING_BASE)->ShowWindow(SW_SHOW);
	}
	else
	{
		GetDlgItem(IDC_STATIC_FRAME_ORDER)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_LST_ECORDER)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_MOVEUP)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_MOVEDOWN)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_APPLYALL)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_STATIC_FRAME_TIME)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_EDT_FRAMETIME)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_SETTIME)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_EVTIME)->ShowWindow(SW_HIDE);
	}

	if( pDoc->m_State )
	{

		int n = pDoc->m_nFrame;
		m_ScrollBarFrame.SetScrollRange(0, n);
		SCROLLINFO si;
		si.cbSize = sizeof(si);
		si.nMin =0;
		si.nMax =n;
		si.nPage= n/10;
		m_ScrollBarFrame.SetScrollInfo(&si);
		m_ScrollBarFrame.SetScrollPos(pDoc->m_PlayState.m_iCurFrame);

		m_CurFrame.Format(L"当前帧： %d/%d", 1+pDoc->m_PlayState.m_iCurFrame, n);

		if( pDoc->m_State == 1 )
		{
			pDoc->m_CurAction.GetFrameOrder(pDoc->m_PlayState.m_iCurFrame, pDoc->m_PlayState.m_iCurDir, m_ecOrders, nec);
			m_iTime = pDoc->m_CurAction.GetFrameTime(pDoc->m_PlayState.m_iCurFrame);

			m_ListBoxEcOrder.ResetContent();
			for(int i=0; i< nec;++i)
				m_ListBoxEcOrder.AddString(pSprMan->GetLayerDes(pDoc->m_CurModelName, m_ecOrders[i]).c_str());
		}
		if( pDoc->m_State == 2 )
		{
			m_iOffX = pDoc->m_PackCenter.x;
			m_iOffY = pDoc->m_PackCenter.y;
		}

	}

	m_SilderScale.SetPos(pDoc->m_nScale);

	UpdateData(FALSE);
}

void CActionControlView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	Update();
}

#ifdef _DEBUG
void CActionControlView::AssertValid() const
{
	CFormView::AssertValid();
}

#ifndef _WIN32_WCE
void CActionControlView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}
#endif
#endif 

BEGIN_MESSAGE_MAP(CActionControlView, CFormView)
	ON_BN_CLICKED(IDC_RADIO1, &CActionControlView::OnBnClickedRadio)
	ON_BN_CLICKED(IDC_RADIO2, &CActionControlView::OnBnClickedRadio)
	ON_BN_CLICKED(IDC_RADIO3, &CActionControlView::OnBnClickedRadio)
	ON_BN_CLICKED(IDC_RADIO4, &CActionControlView::OnBnClickedRadio)
	ON_BN_CLICKED(IDC_RADIO5, &CActionControlView::OnBnClickedRadio)
	ON_BN_CLICKED(IDC_RADIO6, &CActionControlView::OnBnClickedRadio)
	ON_BN_CLICKED(IDC_RADIO7, &CActionControlView::OnBnClickedRadio)
	ON_BN_CLICKED(IDC_RADIO8, &CActionControlView::OnBnClickedRadio)
	ON_WM_HSCROLL()
	ON_BN_CLICKED(IDC_BTN_MOVEUP, &CActionControlView::OnBnClickedBtnMoveUp)
	ON_BN_CLICKED(IDC_BTN_MOVEDOWN, &CActionControlView::OnBnClickedBtnMoveDown)
	ON_BN_CLICKED(IDC_BTN_SAVEACT, &CActionControlView::OnBnClickedBtnSaveAni)
	ON_BN_CLICKED(IDC_BTN_APPLYALL, &CActionControlView::OnBnClickedBtnApplyAll)
	ON_BN_CLICKED(IDC_BTN_SETTIME, &CActionControlView::OnBnClickedBtnSetTime)
	ON_BN_CLICKED(IDC_BTN_EVTIME, &CActionControlView::OnBnClickedBtnEvTime)
	ON_BN_CLICKED(IDC_BUTTON_TITLE_POS, &CActionControlView::OnBnClickedButtonTitlePos)
	ON_CBN_SELCHANGE(IDC_COMBO_WATER_TYPE, &CActionControlView::OnCbnSelchangeComboWaterType)
	ON_BN_CLICKED(IDC_SETTING_NONE, &CActionControlView::OnBnClickedSettingNone)
	ON_BN_CLICKED(IDC_SETTING_RIDE, &CActionControlView::OnBnClickedSettingRide)
	ON_BN_CLICKED(IDC_SETTING_EFFECT_POS, &CActionControlView::OnBnClickedSettingEffectPosition)
	ON_BN_CLICKED(IDC_BUTTON_RESET_SCALE, &CActionControlView::OnBnClickedButtonResetScale)
	ON_BN_CLICKED(IDC_SETTING_BOUNDING_BOX, &CActionControlView::OnBnClickedSettingBoundingBox)
	ON_BN_CLICKED(IDC_BTN_APPLY_ALL_DIR, &CActionControlView::OnBnClickedBtnApplyAllDir)
	ON_BN_CLICKED(IDC_BTN_COPY, &CActionControlView::OnBnClickedBtnCopy)
	ON_BN_CLICKED(IDC_SETTING_BASE, &CActionControlView::OnBnClickedSettingBase)

	ON_BN_CLICKED(IDC_BUTTON_SAVE_SCALE, &CActionControlView::OnBnClickedButtonSaveScale)
END_MESSAGE_MAP()

void CActionControlView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);

	DDX_Radio(pDX, IDC_RADIO1, m_Dirs1);
	DDX_Control(pDX, IDC_SBR_FRAME, m_ScrollBarFrame);
	DDX_Control(pDX, IDC_SLIDER_SCALE, m_SilderScale);
	DDX_Text(pDX, IDC_EDT_CURFRAME, m_CurFrame);
	DDX_Control(pDX, IDC_LST_ECORDER, m_ListBoxEcOrder);
	DDX_Text(pDX, IDC_EDT_FRAMETIME, m_iTime);
	DDV_MinMaxInt(pDX, m_iTime, 1, 64);
	DDX_Text(pDX, IDC_EDT_OFFX, m_iOffX);
	DDX_Text(pDX, IDC_EDT_OFFY, m_iOffY);
	DDX_Control(pDX, IDC_COMBO_WATER_TYPE, m_ComboBoxWaterType);
}

CActionControlView::~CActionControlView()
{
}

CActionControlView::CActionControlView()
: CFormView(CActionControlView::IDD)

, m_Dirs1(0)
, m_CurFrame(_T(""))
, m_iTime(1)
, m_iOffX(0)
, m_iOffY(0)
{

}

void CSpriteEditorView::OnTimer(UINT_PTR nIDEvent)
{
	if( nIDEvent == 1 )
		Render();
	CView::OnTimer(nIDEvent);

	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
}

void CSpriteEditorView::OnInitialUpdate()
{
	CView::OnInitialUpdate();
	SetTimer(1,30,NULL);
}

BOOL CSpriteEditorView::OnEraseBkgnd(CDC* pDC)
{
	return TRUE;

}


BOOL CSpriteEditorView::DestroyWindow()
{


	return CView::DestroyWindow();
}

void CEquipSelectView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);

	if( pDoc->GetRenderer() == NULL ) return;
	Nuclear::SpriteManager *pSprMan = pDoc->GetSpriteManager();
	int nec = pSprMan->GetLayerCount(pDoc->m_CurModelName);

	if( pDoc->m_State == 2 )
	{
		GetDlgItem(IDC_STATIC_CUR_ANI)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_EDT_CUR_ANI)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_CC_ANI)->ShowWindow(SW_SHOW);

#define M_ACV_IMP_2(i) GetDlgItem(IDC_BTN_CC_L##i)->ShowWindow(SW_HIDE); \
	GetDlgItem(IDC_BTN_NEW_L##i)->ShowWindow(SW_HIDE); \
	GetDlgItem(IDC_BTN_DEL_L##i)->ShowWindow(SW_HIDE); \
	GetDlgItem(IDC_BTN_RELOAD_L##i)->ShowWindow(SW_HIDE); \
	GetDlgItem(IDC_CHK_SHOW_L##i)->ShowWindow(SW_HIDE); \
	GetDlgItem(IDC_CMB_CUR_L##i)->ShowWindow(SW_HIDE); \
	GetDlgItem(IDC_STATIC_CUR_L##i)->ShowWindow(SW_HIDE)

		M_ACV_IMP_2(1);
		M_ACV_IMP_2(2);
		M_ACV_IMP_2(3);
		M_ACV_IMP_2(4);
		M_ACV_IMP_2(5);
		M_ACV_IMP_2(6);
		M_ACV_IMP_2(7);
		M_ACV_IMP_2(8);
		M_ACV_IMP_2(9);
		M_ACV_IMP_2(10);
		M_ACV_IMP_2(11);
		M_ACV_IMP_2(12);

		GetDlgItem(IDC_EDT_CUR_ACTION)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_STATIC_CUR_ACTION)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_NEWACTION)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_STATIC_HORSE_NAME)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_HORSE_NAME)->ShowWindow(SW_HIDE);

		m_CurAniName = pDoc->m_CurAniName.c_str();
		GetDlgItem(IDC_EDIT_PIC_PATH_PREFIX)->SetWindowText(pDoc->m_PicPathPrefix.c_str());
	}
	else
	{
		GetDlgItem(IDC_STATIC_CUR_ANI)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_EDT_CUR_ANI)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_BTN_CC_ANI)->ShowWindow(SW_HIDE);

		GetDlgItem(IDC_EDT_CUR_ACTION)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_STATIC_CUR_ACTION)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_BTN_NEWACTION)->ShowWindow(SW_SHOW);

		m_CurActionName = pDoc->m_CurActionName.c_str();
		for(int i=0; i<nec;++i)
			m_bShowEcArr[i] = pDoc->m_CurEquips[i].m_bShowEc;

#define M_ACV_IMP_3(i) GetDlgItem(IDC_BTN_CC_L##i)->ShowWindow( nec>=i ? SW_SHOW : SW_HIDE); \
	GetDlgItem(IDC_BTN_NEW_L##i)->ShowWindow( nec>=i ? SW_SHOW : SW_HIDE); \
	GetDlgItem(IDC_BTN_DEL_L##i)->ShowWindow( nec>=i ? SW_SHOW : SW_HIDE); \
	GetDlgItem(IDC_BTN_RELOAD_L##i)->ShowWindow( nec>=i ? SW_SHOW : SW_HIDE); \
	GetDlgItem(IDC_CHK_SHOW_L##i)->ShowWindow( nec>=i ? SW_SHOW : SW_HIDE); \
	GetDlgItem(IDC_CMB_CUR_L##i)->ShowWindow( nec>=i ? SW_SHOW : SW_HIDE); \
	GetDlgItem(IDC_STATIC_CUR_L##i)->ShowWindow( nec>=i ? SW_SHOW : SW_HIDE); \
	if( nec >=i )  \
		{ \
		GetDlgItem(IDC_STATIC_CUR_L##i)->SetWindowText(std::wstring(L"当前" + pSprMan->GetLayerDes(pDoc->m_CurModelName, i-1)).c_str()); \
		while( m_cmbCurL##i.GetCount() > 0 ) \
		m_cmbCurL##i.DeleteString(0); \
		std::vector<std::wstring> tbodys; \
		pSprMan->DumpComponent(pDoc->m_CurModelName, i-1, tbodys); \
		int n = (int)tbodys.size(); \
		for(int j=0; j<n; ++j) \
		{ \
		m_cmbCurL##i.AddString(tbodys[j].c_str()); \
		if( tbodys[j] == pDoc->m_CurEquips[i-1].m_Equip.GetName() ) \
		m_cmbCurL##i.SetCurSel(j); \
		} \
		}

		M_ACV_IMP_3(1);
		M_ACV_IMP_3(2);
		M_ACV_IMP_3(3);
		M_ACV_IMP_3(4);
		M_ACV_IMP_3(5);
		M_ACV_IMP_3(6);
		M_ACV_IMP_3(7);
		M_ACV_IMP_3(8);
		M_ACV_IMP_3(9);
		M_ACV_IMP_3(10);
		M_ACV_IMP_3(11);
		M_ACV_IMP_3(12);

		GetDlgItem(IDC_STATIC_HORSE_NAME)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_HORSE_NAME)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_HORSE_NAME)->SetWindowText(pDoc->m_CurHorse.c_str());
	}

	GetDlgItem(IDC_STATIC_PIC_PATH_PREFIX)->ShowWindow(pDoc->m_State == 2? SW_SHOW: SW_HIDE);
	GetDlgItem(IDC_EDIT_PIC_PATH_PREFIX)->ShowWindow(pDoc->m_State == 2? SW_SHOW: SW_HIDE);

	int dyeable = pSprMan->GetDyeEnable(pDoc->m_CurModelName);
	((CButton*)GetDlgItem(IDC_CHK_ENABLERS))->SetCheck(dyeable);
	if (dyeable > 1)
		((CButton*)GetDlgItem(IDC_CHK_FENKUAI))->SetCheck(1);
	else
		((CButton*)GetDlgItem(IDC_CHK_FENKUAI))->SetCheck(0);
	if(dyeable > 0)
		((CButton*)GetDlgItem(IDC_BTN_OPENRS))->EnableWindow(TRUE);
	else
		((CButton*)GetDlgItem(IDC_BTN_OPENRS))->EnableWindow(FALSE);

	UpdateData(FALSE);
}

#ifdef _DEBUG
void CEquipSelectView::AssertValid() const
{
	CFormView::AssertValid();
}

#ifndef _WIN32_WCE
void CEquipSelectView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}
#endif
#endif 

BEGIN_MESSAGE_MAP(CEquipSelectView, CFormView)
	ON_BN_CLICKED(IDC_BTN_NEW_L1, &CEquipSelectView::OnBnClickedBtnNewL1)
	ON_BN_CLICKED(IDC_BTN_NEW_L2, &CEquipSelectView::OnBnClickedBtnNewL2)
	ON_BN_CLICKED(IDC_BTN_NEW_L3, &CEquipSelectView::OnBnClickedBtnNewL3)
	ON_BN_CLICKED(IDC_BTN_NEW_L4, &CEquipSelectView::OnBnClickedBtnNewL4)
	ON_BN_CLICKED(IDC_BTN_NEW_L5, &CEquipSelectView::OnBnClickedBtnNewL5)
	ON_BN_CLICKED(IDC_BTN_NEW_L6, &CEquipSelectView::OnBnClickedBtnNewL6)
	ON_BN_CLICKED(IDC_BTN_NEW_L7, &CEquipSelectView::OnBnClickedBtnNewL7)
	ON_BN_CLICKED(IDC_BTN_NEW_L8, &CEquipSelectView::OnBnClickedBtnNewL8)
	ON_BN_CLICKED(IDC_BTN_NEW_L9, &CEquipSelectView::OnBnClickedBtnNewL9)
	ON_BN_CLICKED(IDC_BTN_NEW_L10, &CEquipSelectView::OnBnClickedBtnNewL10)
	ON_BN_CLICKED(IDC_BTN_NEW_L11, &CEquipSelectView::OnBnClickedBtnNewL11)
	ON_BN_CLICKED(IDC_BTN_NEW_L12, &CEquipSelectView::OnBnClickedBtnNewL12)

	ON_BN_CLICKED(IDC_BTN_RELOAD_L1, &CEquipSelectView::OnBnClickedBtnReloadL1)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L2, &CEquipSelectView::OnBnClickedBtnReloadL2)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L3, &CEquipSelectView::OnBnClickedBtnReloadL3)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L4, &CEquipSelectView::OnBnClickedBtnReloadL4)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L5, &CEquipSelectView::OnBnClickedBtnReloadL5)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L6, &CEquipSelectView::OnBnClickedBtnReloadL6)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L7, &CEquipSelectView::OnBnClickedBtnReloadL7)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L8, &CEquipSelectView::OnBnClickedBtnReloadL8)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L9, &CEquipSelectView::OnBnClickedBtnReloadL9)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L10, &CEquipSelectView::OnBnClickedBtnReloadL10)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L11, &CEquipSelectView::OnBnClickedBtnReloadL11)
	ON_BN_CLICKED(IDC_BTN_RELOAD_L12, &CEquipSelectView::OnBnClickedBtnReloadL12)

	ON_BN_CLICKED(IDC_CHK_SHOW_L1, &CEquipSelectView::OnBnClickedChkShowL1)
	ON_BN_CLICKED(IDC_CHK_SHOW_L2, &CEquipSelectView::OnBnClickedChkShowL2)
	ON_BN_CLICKED(IDC_CHK_SHOW_L3, &CEquipSelectView::OnBnClickedChkShowL3)
	ON_BN_CLICKED(IDC_CHK_SHOW_L4, &CEquipSelectView::OnBnClickedChkShowL4)
	ON_BN_CLICKED(IDC_CHK_SHOW_L5, &CEquipSelectView::OnBnClickedChkShowL5)
	ON_BN_CLICKED(IDC_CHK_SHOW_L6, &CEquipSelectView::OnBnClickedChkShowL6)
	ON_BN_CLICKED(IDC_CHK_SHOW_L7, &CEquipSelectView::OnBnClickedChkShowL7)
	ON_BN_CLICKED(IDC_CHK_SHOW_L8, &CEquipSelectView::OnBnClickedChkShowL8)
	ON_BN_CLICKED(IDC_CHK_SHOW_L9, &CEquipSelectView::OnBnClickedChkShowL9)
	ON_BN_CLICKED(IDC_CHK_SHOW_L10, &CEquipSelectView::OnBnClickedChkShowL10)
	ON_BN_CLICKED(IDC_CHK_SHOW_L11, &CEquipSelectView::OnBnClickedChkShowL11)
	ON_BN_CLICKED(IDC_CHK_SHOW_L12, &CEquipSelectView::OnBnClickedChkShowL12)

	ON_BN_CLICKED(IDC_BTN_NEWACTION, &CEquipSelectView::OnBnClickedBtnNewAnimation)

	ON_CBN_SELCHANGE(IDC_CMB_CUR_L1, &CEquipSelectView::OnCbnSelchangeCmbCurL1)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L2, &CEquipSelectView::OnCbnSelchangeCmbCurL2)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L3, &CEquipSelectView::OnCbnSelchangeCmbCurL3)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L4, &CEquipSelectView::OnCbnSelchangeCmbCurL4)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L5, &CEquipSelectView::OnCbnSelchangeCmbCurL5)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L6, &CEquipSelectView::OnCbnSelchangeCmbCurL6)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L7, &CEquipSelectView::OnCbnSelchangeCmbCurL7)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L8, &CEquipSelectView::OnCbnSelchangeCmbCurL8)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L9, &CEquipSelectView::OnCbnSelchangeCmbCurL9)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L10, &CEquipSelectView::OnCbnSelchangeCmbCurL10)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L11, &CEquipSelectView::OnCbnSelchangeCmbCurL11)
	ON_CBN_SELCHANGE(IDC_CMB_CUR_L12, &CEquipSelectView::OnCbnSelchangeCmbCurL12)

	ON_BN_CLICKED(IDC_BTN_DEL_L1, &CEquipSelectView::OnBnClickedBtnDelL1)
	ON_BN_CLICKED(IDC_BTN_DEL_L2, &CEquipSelectView::OnBnClickedBtnDelL2)
	ON_BN_CLICKED(IDC_BTN_DEL_L3, &CEquipSelectView::OnBnClickedBtnDelL3)
	ON_BN_CLICKED(IDC_BTN_DEL_L4, &CEquipSelectView::OnBnClickedBtnDelL4)
	ON_BN_CLICKED(IDC_BTN_DEL_L5, &CEquipSelectView::OnBnClickedBtnDelL5)
	ON_BN_CLICKED(IDC_BTN_DEL_L6, &CEquipSelectView::OnBnClickedBtnDelL6)
	ON_BN_CLICKED(IDC_BTN_DEL_L7, &CEquipSelectView::OnBnClickedBtnDelL7)
	ON_BN_CLICKED(IDC_BTN_DEL_L8, &CEquipSelectView::OnBnClickedBtnDelL8)
	ON_BN_CLICKED(IDC_BTN_DEL_L9, &CEquipSelectView::OnBnClickedBtnDelL9)
	ON_BN_CLICKED(IDC_BTN_DEL_L10, &CEquipSelectView::OnBnClickedBtnDelL10)
	ON_BN_CLICKED(IDC_BTN_DEL_L11, &CEquipSelectView::OnBnClickedBtnDelL11)
	ON_BN_CLICKED(IDC_BTN_DEL_L12, &CEquipSelectView::OnBnClickedBtnDelL12)

	ON_BN_CLICKED(IDC_BTN_CC_ANI, &CEquipSelectView::OnBnClickedBtnCcAni)

	ON_BN_CLICKED(IDC_BTN_CC_L1, &CEquipSelectView::OnBnClickedBtnCcL1)
	ON_BN_CLICKED(IDC_BTN_CC_L2, &CEquipSelectView::OnBnClickedBtnCcL2)
	ON_BN_CLICKED(IDC_BTN_CC_L3, &CEquipSelectView::OnBnClickedBtnCcL3)
	ON_BN_CLICKED(IDC_BTN_CC_L4, &CEquipSelectView::OnBnClickedBtnCcL4)
	ON_BN_CLICKED(IDC_BTN_CC_L5, &CEquipSelectView::OnBnClickedBtnCcL5)
	ON_BN_CLICKED(IDC_BTN_CC_L6, &CEquipSelectView::OnBnClickedBtnCcL6)
	ON_BN_CLICKED(IDC_BTN_CC_L7, &CEquipSelectView::OnBnClickedBtnCcL7)
	ON_BN_CLICKED(IDC_BTN_CC_L8, &CEquipSelectView::OnBnClickedBtnCcL8)
	ON_BN_CLICKED(IDC_BTN_CC_L9, &CEquipSelectView::OnBnClickedBtnCcL9)
	ON_BN_CLICKED(IDC_BTN_CC_L10, &CEquipSelectView::OnBnClickedBtnCcL10)
	ON_BN_CLICKED(IDC_BTN_CC_L11, &CEquipSelectView::OnBnClickedBtnCcL11)
	ON_BN_CLICKED(IDC_BTN_CC_L12, &CEquipSelectView::OnBnClickedBtnCcL12)

	ON_BN_CLICKED(IDC_BTN_OPENRS, &CEquipSelectView::OnBnClickedBtnOpenRs)
	ON_BN_CLICKED(IDC_CHK_ENABLERS, &CEquipSelectView::OnBnClickedChkEnableRS)
	ON_BN_CLICKED(IDC_CHK_FENKUAI, &CEquipSelectView::OnBnClickedChkFenKuai)
	ON_EN_CHANGE(IDC_EDIT_PIC_PATH_PREFIX, &CEquipSelectView::OnEnChangeEditPicPathPrefix)
END_MESSAGE_MAP()

void CEquipSelectView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDT_CUR_ACTION, m_CurActionName);
	DDX_Check(pDX, IDC_CHK_SHOW_L1, m_bShowEcArr[0]);
	DDX_Check(pDX, IDC_CHK_SHOW_L2, m_bShowEcArr[1]);
	DDX_Check(pDX, IDC_CHK_SHOW_L3, m_bShowEcArr[2]);
	DDX_Check(pDX, IDC_CHK_SHOW_L4, m_bShowEcArr[3]);
	DDX_Check(pDX, IDC_CHK_SHOW_L5, m_bShowEcArr[4]);
	DDX_Check(pDX, IDC_CHK_SHOW_L6, m_bShowEcArr[5]);
	DDX_Check(pDX, IDC_CHK_SHOW_L7, m_bShowEcArr[6]);
	DDX_Check(pDX, IDC_CHK_SHOW_L8, m_bShowEcArr[7]);
	DDX_Check(pDX, IDC_CHK_SHOW_L9, m_bShowEcArr[8]);
	DDX_Check(pDX, IDC_CHK_SHOW_L10, m_bShowEcArr[9]);
	DDX_Check(pDX, IDC_CHK_SHOW_L11, m_bShowEcArr[10]);
	DDX_Check(pDX, IDC_CHK_SHOW_L12, m_bShowEcArr[11]);
	DDX_Text(pDX, IDC_EDT_CUR_ANI, m_CurAniName);

	DDX_Control(pDX, IDC_CMB_CUR_L1, m_cmbCurL1);
	DDX_Control(pDX, IDC_CMB_CUR_L2, m_cmbCurL2);
	DDX_Control(pDX, IDC_CMB_CUR_L3, m_cmbCurL3);
	DDX_Control(pDX, IDC_CMB_CUR_L4, m_cmbCurL4);
	DDX_Control(pDX, IDC_CMB_CUR_L5, m_cmbCurL5);
	DDX_Control(pDX, IDC_CMB_CUR_L6, m_cmbCurL6);
	DDX_Control(pDX, IDC_CMB_CUR_L7, m_cmbCurL7);
	DDX_Control(pDX, IDC_CMB_CUR_L8, m_cmbCurL8);
	DDX_Control(pDX, IDC_CMB_CUR_L9, m_cmbCurL9);
	DDX_Control(pDX, IDC_CMB_CUR_L10, m_cmbCurL10);
	DDX_Control(pDX, IDC_CMB_CUR_L11, m_cmbCurL11);
	DDX_Control(pDX, IDC_CMB_CUR_L12, m_cmbCurL12);
}

CEquipSelectView::~CEquipSelectView()
{

}

CEquipSelectView::CEquipSelectView()
: CFormView(CEquipSelectView::IDD)
, m_CurActionName(_T(""))
, m_CurAniName(_T(""))
{
	gEquipSelectView = this;

	for(int i=0;i<Nuclear::XPSC_MAXCOUNT;++i)
		m_bShowEcArr[i] = FALSE;
}

void CAnimationOpView::OnLbnDblclkListBoxAnimation()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);

	if( -1 != m_ListBoxAnimation.GetCurSel())
	{
		wchar_t buf[256];
		m_ListBoxAnimation.GetText(m_ListBoxAnimation.GetCurSel(),buf);
		if( pDoc->m_CurActionName != buf )
		{
			pDoc->OpenAction(buf);
		}
	}
}

void CAnimationOpView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{

	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();

	assert(pDoc);
	if( pDoc->m_pSpriteMgr == NULL ) return;
	m_ComboBoxCurAnimationGroup.ResetContent();
	m_ComboBoxCurAnimationGroup.SetCurSel(0);
	std::vector<std::wstring> v;

	pDoc->m_pSpriteMgr->DumpModelNames(v);
	for(int i=0; i<(int)v.size();++i)
	{
		m_ComboBoxCurAnimationGroup.AddString(v[i].c_str());

		if( v[i] == pDoc->m_CurModelName )
			m_ComboBoxCurAnimationGroup.SetCurSel(i);
	}

	m_ListBoxAnimation.ResetContent();

	const Nuclear::SpriteManager *pSprMan = pDoc->m_pSpriteMgr;
	if( pSprMan == NULL ) return;

	std::vector<std::wstring> actlist;
	pSprMan->DumpAction(pDoc->m_CurModelName, actlist);
	for(int i=0; i<(int)actlist.size(); ++i)
	{
		std::wstring name = actlist[i];

		m_ListBoxAnimation.AddString(name.c_str());
	}

	for(int i=0; i<(int)m_ListBoxAnimation.GetCount(); ++i)
	{
		wchar_t buf[256];
		m_ListBoxAnimation.GetText(i,buf);
		if( pDoc->m_CurActionName == buf )
		{
			m_ListBoxAnimation.SetCurSel(i);
			break;
		}
	}


}

#ifdef _DEBUG
void CAnimationOpView::AssertValid() const
{
	CFormView::AssertValid();
}

#ifndef _WIN32_WCE
void CAnimationOpView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}
#endif
#endif 

BEGIN_MESSAGE_MAP(CAnimationOpView, CFormView)
	ON_LBN_DBLCLK(IDC_LST_ACTION, &CAnimationOpView::OnLbnDblclkListBoxAnimation)
	ON_BN_CLICKED(IDC_BTN_NEWACTION, &CAnimationOpView::OnBnClickedBtnNewAnimation)
	ON_BN_CLICKED(IDC_BTN_OPENANI, &CAnimationOpView::OnBnClickedBtnOpenAnimation)
	ON_BN_CLICKED(IDC_BTN_DELACTION, &CAnimationOpView::OnBnClickedBtnDeleteAnimation)
	ON_WM_CONTEXTMENU()
	ON_BN_CLICKED(IDC_BTN_RENAMEACTION, &CAnimationOpView::OnBnClickedBtnRenameAnimation)
	ON_CBN_SELCHANGE(IDC_CMB_CURACTGROUP, &CAnimationOpView::OnCbnSelChangeComboBoxCurAniGroup)
	ON_BN_CLICKED(IDC_BUTTON2, &CAnimationOpView::OnBnClickedButton2)
	ON_WM_HSCROLL()
	ON_LBN_SELCHANGE(IDC_LST_ACTION, &CAnimationOpView::OnLbnSelchangeListBoxAnimation)
END_MESSAGE_MAP()

void CAnimationOpView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LST_ACTION, m_ListBoxAnimation);
	DDX_Control(pDX, IDC_CMB_CURACTGROUP, m_ComboBoxCurAnimationGroup);
	DDX_Control(pDX, IDC_SLIDER_VOL, m_SliderVol);
	DDX_Control(pDX, IDC_SLIDER_FREQ, m_SliderFreq);
}

CAnimationOpView::~CAnimationOpView()
{
}

CAnimationOpView::CAnimationOpView()
: CFormView(CAnimationOpView::IDD), m_bInited(false)
{
}

#ifdef _DEBUG
void CSpriteEditorView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CSpriteEditorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif
#endif 

void CSpriteEditorView::Render()
{
	if (m_nStartTime == 0)
		m_nStartTime = Nuclear::GetMilliSeconds();
	RECT rect;
	GetClientRect(&rect);
	Nuclear::FRECT clientrect((float)rect.left,(float)rect.top,(float)rect.right,(float)rect.bottom);
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);
	if( pDoc->m_pRenderer == NULL ) return;
	float fscale = pDoc->m_nScale / 100.0f;

	if (pDoc->m_pSoundManager)
		pDoc->m_pSoundManager->OnTick();


	pDoc->m_pRenderer->Begin(pDoc->m_BgColor);
	if( pDoc->m_bBgPicOn && pDoc->m_BgPicHandle != Nuclear::INVALID_PICTURE_HANDLE )
	{
		if( pDoc->m_bBgPicSt )
		{
			Nuclear::FRECT frect(0.f,0.f,(float)m_d3dWidth,(float)m_d3dHeight);
			pDoc->m_pRenderer->DrawPicture(pDoc->m_BgPicHandle, frect, 0xFFFFFFFF);
		}
		else
		{
			Nuclear::PictureInfo picinfo;
			pDoc->m_pRenderer->GetPictureInfo(pDoc->m_BgPicHandle, picinfo);
			Nuclear::FRECT frect;
			frect.left = (clientrect.Width()/2- picinfo.m_nPicWidth/2);
			frect.right = (clientrect.Width()/2+ picinfo.m_nPicWidth/2);
			frect.top = (clientrect.Height()/2- picinfo.m_nPicHeight/2);
			frect.bottom = (clientrect.Height()/2+ picinfo.m_nPicHeight/2);
			pDoc->m_pRenderer->DrawPicture(pDoc->m_BgPicHandle, frect, 0xFFFFFFFF);
		}
	}

	if( pDoc->m_BirdType )
	{
		int gridwidth = 10;
		int gridheight = 10;
		int addX;
		int addY;
		if ( pDoc->m_BirdType == 2)	
		{
			gridwidth = 24;
			gridheight = 16;

			addX = static_cast<int>((clientrect.Width() / fscale + gridwidth) / 2) % gridwidth;
			addY = static_cast<int>((clientrect.Height() / fscale + gridheight) / 2) % gridheight;
		} else {	
			addX = static_cast<int>(clientrect.Width() / fscale / 2) % gridwidth;
			addY = static_cast<int>(clientrect.Height() / fscale / 2) % gridheight;
		}

		int cols = static_cast<int>(clientrect.Width() / fscale /gridwidth) + 1;
		int rows = static_cast<int>(clientrect.Height()/ fscale / gridheight) + 1;

		Nuclear::FRECT *lines = new Nuclear::FRECT[cols+rows];
		for(int i=0; i<cols;++i)
		{
			lines[i].top = 0.f;
			lines[i].bottom = (float)m_d3dHeight;
			lines[i].right = lines[i].left = (i*gridwidth+addX) * fscale;
		}
		for(int i=0; i<rows;++i)
		{
			lines[i+cols].left = 0.f;
			lines[i+cols].right = (float)m_d3dWidth;
			lines[i+cols].bottom = lines[i+cols].top = (i*gridheight+addY) * fscale;
		}
		pDoc->m_pRenderer->DrawLine(lines,rows+cols,0x80808080);
		delete[] lines;
	}

	int ti = 0;
	static DWORD beginTick = Nuclear::GetMilliSeconds();
	DWORD now = Nuclear::GetMilliSeconds();
	for(std::vector<Nuclear::XHardRef<Nuclear::Sprite> >::iterator it = pDoc->m_Sprites.begin(), ie = pDoc->m_Sprites.end(); it != ie; ++it)
	{
		Nuclear::XHardRef<Nuclear::Sprite> &p = *it;

		if( ti == pDoc->m_CurSprite)
		{
			pDoc->m_pRenderer->DrawEllipse((float)p->GetLocation().x, (float)p->GetLocation().y, 20.f,10.f,0xff00ff00,Nuclear::XPFM_WIREFRAME);
		}
		p->tick(now, now - beginTick);
		p->Render(NULL);
		++ti;
	}
	beginTick = now;


	int nCurEC = 0;
	for(int i=0; i<(int)pDoc->m_CurEquips.size();++i)
		if( pDoc->m_CurEquips[i].m_Equip.GetName() != L"")
			++nCurEC;
	if( pDoc->m_State ==0 || pDoc->m_State == 1 && 0 == nCurEC)
		;
	else if( pDoc->m_PlayState.m_fAuto || pDoc->m_PlayState.m_fPlayOnce)
	{
		int oldframe = pDoc->m_PlayState.m_iCurFrame;
		int d = (int)Nuclear::GetMilliSeconds() - pDoc->m_PlayState.m_iStartTick;
		if( d > pDoc->m_Time )
		{
			if (pDoc->m_PlayState.m_fPlayOnce)
				pDoc->m_PlayState.m_fPlayOnce = false;
			pDoc->PlayReset();
			d = 0;

			if( pDoc->m_PlayState.m_fCycle )
			{
				pDoc->PlayRR();
			}
		}

		if( d != -1 )
		{
			float r = d / (float)pDoc->m_Time;
			int i;
			if( pDoc->m_State == 1)
			{
				for(i=pDoc->m_nFrame-1; i>=0;--i)
				{
					if( r > pDoc->m_CurAction.GetFrameStartTime(i) )
					{
						break;
					}
				}
				if (d == 0 && pDoc->m_CurSoundName.length() > 0)
				{
					pDoc->PlayStaticSound();
				}
			}
			else if( pDoc->m_State == 2 )
			{
				i = (int)(r*pDoc->m_nFrame);
				if( i >= pDoc->m_nFrame ) i=pDoc->m_nFrame-1;
			}
			pDoc->m_PlayState.m_iCurFrame = i >0?i:0;
			if( oldframe != pDoc->m_PlayState.m_iCurFrame )
				pDoc->UpdateControlView();
		}
	}

	if( pDoc->m_State == 2 ) 
	{
		if( !pDoc->m_bUnwrap )
		{
			Nuclear::XAni::XPic pic;
			for(int k=0; k<pDoc->m_CurAni.GetRegionCount();++k)
			{
				if( pDoc->m_CurAni.GetPic(pDoc->m_PlayState.m_iCurDir, 
					pDoc->m_PlayState.m_iCurFrame, k,pic))
				{
					if( pic.pBigPic && pic.pBigPic->handle != Nuclear::INVALID_PICTURE_HANDLE )
					{	
						if( pDoc->m_bPixList )
						{
							Nuclear::PictureInfo picinfo;
							pDoc->m_pRenderer->GetPictureInfo(pic.pBigPic->handle, picinfo);
							Nuclear::XBuffer buf;
							buf.resize((picinfo.m_texfmt==Nuclear::XPTEXFMT_A4R4G4B4?2:4)*picinfo.m_nPicWidth*picinfo.m_nPicHeight);
							if( pDoc->m_pRenderer->GetPictureData(pic.pBigPic->handle, buf.normalbegin(), (int)buf.size()))
							{
								for(int row=0; row<picinfo.m_nPicHeight;++row)
								{
									for(int col=0; col<picinfo.m_nPicWidth;++col)
									{
										int color;
										if( picinfo.m_texfmt == Nuclear::XPTEXFMT_A4R4G4B4 )
										{
											color = ((int)((short*)buf.constbegin())[col+row*picinfo.m_nPicWidth])*16;
										}
										else
										{
											color = ((int*)buf.constbegin())[col+row*picinfo.m_nPicWidth];
										}

										float sr = 6 * fscale;
										Nuclear::FRECT frect;
										frect.left = col*sr;
										frect.right = (col+1)*sr;
										frect.top = row*sr;
										frect.bottom = (row+1)*sr;

										pDoc->m_pRenderer->DrawBox(frect, color);
									}
								}
							}
						}
						else
						{
							Nuclear::DrawPictureParam param;
							Nuclear::XPMATRIX3 tm;

							Nuclear::FRECT frect = pic.offset.ToFRECT();
							frect *= fscale;
							frect.left += clientrect.Width()/2;
							frect.right += clientrect.Width()/2;
							frect.top += clientrect.Height()/2;
							frect.bottom += clientrect.Height()/2;
							if ((pDoc->m_CurAni.GetDirMode() == Nuclear::XPANIDM_8FROM5 && pDoc->m_PlayState.m_iCurDir > Nuclear::XPDIR_BOTTOM) ||
								(pDoc->m_CurAni.GetDirMode() == Nuclear::XPANIDM_B2FROM1 && pDoc->m_PlayState.m_iCurDir == Nuclear::XPDIR_BOTTOMLEFT) ||
								(pDoc->m_CurAni.GetDirMode() == Nuclear::XPANIDM_4FROM2 && pDoc->m_PlayState.m_iCurDir == Nuclear::XPDIR_BOTTOMLEFT) ||
								(pDoc->m_CurAni.GetDirMode() == Nuclear::XPANIDM_4FROM2 && pDoc->m_PlayState.m_iCurDir == Nuclear::XPDIR_TOPRIGHT)
								)
							{
								frect.left = clientrect.Width() - frect.left;
								frect.right = clientrect.Width() - frect.right;
							}
							else if( pDoc->m_CurAni.GetDirMode() == Nuclear::XPANIDM_8FROM1 && pDoc->m_PlayState.m_iCurDir != Nuclear::XPDIR_RIGHT )
							{
								Nuclear::FPOINT rp(clientrect.Width()/2,clientrect.Height()/2);			
								Nuclear::GetRotationScalingMartrixByDirection(&tm, &rp, (Nuclear::XPDIRECTION)pDoc->m_PlayState.m_iCurDir);														
								param.pMatrix = &tm;
							}

							param.handle = pic.pBigPic->handle;
							param.pRect = &frect;
							param.pSrcrect = &pic.srcrect;
							Nuclear::XPCOLOR color = (k == pDoc->m_CurAni.GetRegionCount()-1)?pDoc->m_AniColor:0xffffffff;
							param.pColor = &color;
							param.blend = pDoc->m_CurAni.GetBlend();
							pDoc->m_pRenderer->DrawPicture(param);

							if (pDoc->m_bDrawEdge)
							{
								pDoc->m_pRenderer->DrawBox(frect, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);



								std::vector<Nuclear::CPOINT> outLinePoints = pic.outLine.m_points;
								int nx = (int)(frect.left);
								int ny = (int)(frect.top);
								int cnt = (int)( outLinePoints.size() );

								Nuclear::XPPolygon outLine;

								for(int i=0; i<cnt; i++)
								{
									outLinePoints[i] *= fscale;
									outLinePoints[i] += Nuclear::CPOINT(nx, ny);

									outLine.m_points.push_back(outLinePoints[i]);

									float x0 = (float)( outLinePoints[i].x );
									float y0 = (float)( outLinePoints[i].y );
									Nuclear::XPCOLOR cr = 0xff00ff00;
									if( (i == m_nCurSelMaskpt) && (k == m_nCurRegion) )
										cr = 0xffff0000;

									pDoc->m_pRenderer->DrawEllipse(x0, y0, 3, 3, cr, Nuclear::XPFM_WIREFRAME, 0.0f);
								}
								Nuclear::XPCOLOR cr = 0xFFFFFF00;
								if(k==1)
								{
									cr = 0xffff00ff;
								}
								if(m_bSelObj && (k == m_nCurRegion))
									cr = 0xffff0000;

								Nuclear::CRECT viewport((int)(clientrect.left), (int)(clientrect.top), (int)(clientrect.right), (int)(clientrect.bottom));
								outLine.Render(pDoc->m_pRenderer, viewport, cr, Nuclear::XPFM_WIREFRAME, 0);


							} 

							if( k == pDoc->m_CurAni.GetRegionCount()-1 && pDoc->m_bBaseOn ) 
							{
								Nuclear::XPBase base;
								if (pDoc->m_CurAni.GetBase(base))
								{									
									Nuclear::FRECT frect;
									Nuclear::CPOINT tmpBase[4];
									tmpBase[0] = base.left;
									tmpBase[2] = base.right;
									const float tga = 2;
									tmpBase[1].x = (tmpBase[0].x+tmpBase[2].x)/2 + (LONG)(tga * (tmpBase[0].y-tmpBase[2].y)/2);
									tmpBase[1].y = (tmpBase[0].y+tmpBase[2].y)/2 + (LONG)((tmpBase[0].x-tmpBase[2].x)/(2*tga));
									tmpBase[3].x = (tmpBase[0].x+tmpBase[2].x)/2 + (LONG)(-tga * (tmpBase[0].y-tmpBase[2].y)/2);
									tmpBase[3].y = (tmpBase[0].y+tmpBase[2].y)/2 + (LONG)((tmpBase[0].x-tmpBase[2].x)/(2*-tga));

									for(int i=0; i< 4; ++i)
									{
										int j = i + 1;
										if( j == 4 ) 
											j = 0;
										frect.left = (clientrect.Width()/2+tmpBase[i].x * fscale);
										frect.top = (clientrect.Height()/2+tmpBase[i].y * fscale);
										frect.right = (clientrect.Width()/2+tmpBase[j].x * fscale);
										frect.bottom = (clientrect.Height()/2+tmpBase[j].y * fscale);
										pDoc->m_pRenderer->DrawLine(frect, 0xff00ffff);
									}

									int slr = 2, srb = 2;
									if( m_adBaseMode == 1 )
										slr = 3;
									else if( m_adBaseMode == 2 )
										srb = 3;

									m_adBase0.x = base.left.x;
									m_adBase0.y = base.left.y;

									frect.left = (clientrect.Width()/2+base.left.x * fscale-2*slr);
									frect.top = (clientrect.Height()/2+base.left.y * fscale-2*slr);
									frect.right = (clientrect.Width()/2+base.left.x * fscale+2*slr);
									frect.bottom = (clientrect.Height()/2+base.left.y * fscale+2*slr);
									pDoc->m_pRenderer->DrawBox(frect,0xff00ff00, Nuclear::XPFM_WIREFRAME);

									m_adBase2.x = base.right.x;
									m_adBase2.y = base.right.y;

									frect.left = (clientrect.Width()/2+base.right.x * fscale-2*srb);
									frect.top = (clientrect.Height()/2+base.right.y * fscale-2*srb);
									frect.right = (clientrect.Width()/2+base.right.x * fscale+2*srb);
									frect.bottom = (clientrect.Height()/2+base.right.y * fscale+2*srb);
									pDoc->m_pRenderer->DrawBox(frect,0xff0000ff, Nuclear::XPFM_WIREFRAME);
								}
							}
							if( k == pDoc->m_CurAni.GetRegionCount()-1 && pDoc->m_bBorderOn ) 
							{
								Nuclear::CRECT border;
								if (pDoc->m_CurAni.GetBorder(border))
								{
									Nuclear::FRECT frect;
									frect.left = (clientrect.Width()/2+border.left * fscale);
									frect.top = (clientrect.Height()/2+border.top * fscale);
									frect.right = (clientrect.Width()/2+border.right * fscale);
									frect.bottom = (clientrect.Height()/2+border.bottom * fscale);
									pDoc->m_pRenderer->DrawBox(frect,0xffff0000, Nuclear::XPFM_WIREFRAME);

									int slr = 2, srb = 2;
									if( m_adBorderMode == 1 )
										slr = 3;
									else if( m_adBorderMode == 2 )
										srb = 3;

									m_adBorderLT.x = border.left;
									m_adBorderLT.y = border.top;

									frect.left = (clientrect.Width()/2+border.left * fscale-2*slr);
									frect.top = (clientrect.Height()/2+border.top * fscale-2*slr);
									frect.right = (clientrect.Width()/2+border.left * fscale+2*slr);
									frect.bottom = (clientrect.Height()/2+border.top * fscale+2*slr);
									pDoc->m_pRenderer->DrawBox(frect,0xff00ff00, Nuclear::XPFM_WIREFRAME);

									m_adBorderRB.x = border.right;
									m_adBorderRB.y = border.bottom;

									frect.left = (clientrect.Width()/2+border.right * fscale-2*srb);
									frect.top = (clientrect.Height()/2+border.bottom * fscale-2*srb);
									frect.right = (clientrect.Width()/2+border.right * fscale+2*srb);
									frect.bottom = (clientrect.Height()/2+border.bottom * fscale+2*srb);
									pDoc->m_pRenderer->DrawBox(frect,0xff0000ff, Nuclear::XPFM_WIREFRAME);

								}
							}
						}
					}
				}
			}

		}
		else 
		{

			int n = (int)pDoc->m_CurAniGroup.size();
			int ltop = 0;
			int dir = pDoc->m_PlayState.m_iCurDir;
			for(int d = 0; d < n; ++d) 
			{
				Nuclear::XAni &ani = pDoc->m_CurAniGroup[d];

				if( !ani.TestDir(dir) )
					continue;


				int fn = ani.GetFrameCount();

				int width = 0;
				int height = 0;
				for(int i=0; i<fn; ++i)
				{
					Nuclear::XAni::XPic pic;
					if( ani.GetPic(dir, i, 0, pic))
					{
						if (pic.offset.Width() > width)
						{
							width = pic.offset.Width();
						}
						if (pic.offset.Height() > height)
						{
							height = pic.offset.Height();
						}
					}
				}
				width +=8;
				height +=8;
				for(int i=0; i<fn; ++i)
				{
					Nuclear::XAni::XPic pic;
					if( ani.GetPic(dir, i, 0, pic))
					{
						if( pic.pBigPic && pic.pBigPic->handle != Nuclear::INVALID_PICTURE_HANDLE )
						{
							Nuclear::FRECT frect;
							frect.left = static_cast<float>(i*width*fscale);
							frect.right = static_cast<float>(frect.left+pic.offset.Width()*fscale);
							frect.top = static_cast<float>(ltop*fscale);
							frect.bottom = static_cast<float>(frect.top+pic.offset.Height()*fscale);
							Nuclear::DrawPictureParam param;
							param.handle = pic.pBigPic->handle;
							param.pRect = &frect;
							param.pSrcrect = &pic.srcrect;
							param.blend = pDoc->m_CurAni.GetBlend();
							pDoc->m_pRenderer->DrawPicture(param);
						}
					}
				}
				ltop += height;
			}

		}
	}
	else if( pDoc->m_State == 1 ) 
	{
		{
			int d = (Nuclear::GetMilliSeconds() - m_nStartTime) % Nuclear::XP_EFFECT_CYCLE;
			d = d > Nuclear::XP_EFFECT_HALF_CYCLE?Nuclear::XP_EFFECT_CYCLE - d : d;
			m_fEffectAlpha = XP_EFFECT_MIN_ALPHA + XP_EFFECT_ALPHA_DELTA * d;
		}

		int horseHeight = pDoc->GetHorseHeight();
		bool bIsRiding = false;
		bool bIsEffectLayer = false;
		if (pDoc->GetAdMode() == 4)
		{
			horseHeight += m_MouseDownPos.y - m_MouseNowPos.y;
		}
		std::wstring actionName = pDoc->m_CurAction.GetLinkName(pDoc->m_PlayState.m_iCurFrame) == L"" ? pDoc->m_CurActionName : pDoc->m_CurAction.GetLinkName(pDoc->m_PlayState.m_iCurFrame);


		if (actionName == L"riding_run" || actionName == L"riding_stand1")
		{
			bIsRiding = true;
		}
		int ne = (int)pDoc->m_CurEquips.size();
		if(ne <= 0)
		{
			return;
		}

		std::vector<int> ecorder(ne);
		if( pDoc->m_PlayState.m_iCurFrame < pDoc->m_nFrame )
		{
			bool hasRenderEffect = false;
			bool firstEffect = false;
			pDoc->m_CurAction.GetFrameOrder(pDoc->m_PlayState.m_iCurFrame, pDoc->m_PlayState.m_iCurDir, &ecorder[0], ne);
			int effectFrameI = -1;
			for(int i=0; i < ne; ++i)
			{
				int ec = ecorder[i];
				if( ec < 0 || ec >= ne ) continue;
				if( !pDoc->m_CurAction.TestEquip(ec)) continue;
				if( !pDoc->m_CurEquips[ec].m_bShowEc ) continue;
				if (pDoc->m_pSpriteMgr->GetLayerEffectEnable(pDoc->m_CurModelName, ec))
					effectFrameI = i;
			}
			switch(pDoc->m_PlayState.m_iCurDir)
			{
			case 0:
			case 5:
			case 6:
			case 7:
				firstEffect = true;				
				break;
			default:
				break;
			}

			int   iRelatedPicPosIndex  = 0;
			float pRelatedPicPosWidth  = 0.0f;
			float pRelatedPicPosHeight = 0.0f;

			for(int i=0; (i < ne || effectFrameI > -1); ++i)
			{
				int ec = i<ne?ecorder[i]:0;
				if (effectFrameI > -1)
				{
					if (firstEffect)
					{
						ec = ecorder[effectFrameI];
						effectFrameI = -1;
						i = -1;
					} else {
						if (i == ne)
						{
							ec = ecorder[effectFrameI];
							effectFrameI = -1;
						}
					}
				}

				if( ec < 0 || ec >= ne ) continue;
				if( !pDoc->m_CurAction.TestEquip(ec)) continue;
				if( !pDoc->m_CurEquips[ec].m_bShowEc ) continue;
				if( pDoc->m_CurEquips[ec].m_Equip.GetName() != L"" && pDoc->IsNewAnimation())
				{
					Nuclear::XAni::XPic pic;
					Nuclear::XWeakRef<Nuclear::XAni> weakani = pDoc->m_CurEquips[ec].m_Equip.GetAni(pDoc->m_CurModelName, actionName, false);
					Nuclear::XHardRef<Nuclear::XAni> ani = weakani;
					bIsEffectLayer = pDoc->m_pSpriteMgr->GetLayerEffectEnable(pDoc->m_CurModelName, ec);
					if (bIsEffectLayer && i != -1 && i != ne)
						continue;
					if( ani.IsValid() )
					{
						pDoc->LoadAnimationRes(&(*ani));

						for(int k=0; k<ani->GetRegionCount();++k)
						{
							if( ani->GetPic(pDoc->m_PlayState.m_iCurDir, pDoc->m_CurAction.GetLineFrame(pDoc->m_PlayState.m_iCurFrame), k, pic))
							{
								if( pic.pBigPic && pic.pBigPic->handle != Nuclear::INVALID_PICTURE_HANDLE )
								{
									Nuclear::DrawPictureParam param;
									Nuclear::XPMATRIX3 tm;
									Nuclear::XPCOLOR color = (k == ani->GetRegionCount()-1)?pDoc->m_CurEquips[ec].m_Color:0xffffffff;
									Nuclear::FRECT frect(pic.offset.ToFRECT());



									if (bIsEffectLayer)
									{
										color.a = static_cast<unsigned char>(color.a * m_fEffectAlpha);
										param.blend = Nuclear::XPTEXBM_ADDITIVE_ALPHA;
										frect *= Nuclear::XP_EFFECT_SCALE * fscale;
									} else {
										param.blend = Nuclear::XPTEXBM_DEFAULT;
										frect *= fscale;
									}

									frect.left		+= clientrect.Width()/2;
									frect.right		+= clientrect.Width()/2;
									frect.top		+= clientrect.Height()/2;
									frect.bottom	+= clientrect.Height()/2;

									if ((horseHeight != 0) && bIsRiding)
									{
										if (!pDoc->m_pSpriteMgr->GetLayerRideEnable(pDoc->m_CurModelName, ec))
										{
											frect.top		-= horseHeight * fscale;
											frect.bottom	-= horseHeight * fscale;
										}
									}
									if ((ani->GetDirMode() == Nuclear::XPANIDM_8FROM5 && pDoc->m_PlayState.m_iCurDir > Nuclear::XPDIR_BOTTOM) ||
										(ani->GetDirMode() == Nuclear::XPANIDM_B2FROM1 && pDoc->m_PlayState.m_iCurDir == Nuclear::XPDIR_BOTTOMLEFT) ||
										(ani->GetDirMode() == Nuclear::XPANIDM_4FROM2 && pDoc->m_PlayState.m_iCurDir == Nuclear::XPDIR_BOTTOMLEFT) ||
										(ani->GetDirMode() == Nuclear::XPANIDM_4FROM2 && pDoc->m_PlayState.m_iCurDir == Nuclear::XPDIR_TOPRIGHT)
										)
									{
										frect.left = clientrect.Width() - frect.left;
										frect.right = clientrect.Width() - frect.right;
									}
									else if( ani->GetDirMode() == Nuclear::XPANIDM_8FROM1 && pDoc->m_PlayState.m_iCurDir != Nuclear::XPDIR_RIGHT )
									{
										Nuclear::FPOINT rp(clientrect.Width()/2,clientrect.Height()/2);
										Nuclear::GetRotationScalingMartrixByDirection(&tm, &rp, (Nuclear::XPDIRECTION)pDoc->m_PlayState.m_iCurDir);														
										param.pMatrix = &tm;
									}

									param.pRect = &frect;
									param.pSrcrect = &pic.srcrect;
									param.pColor = &color;
									param.handle = pic.pBigPic->handle;

									int eDye = pDoc->GetDyeEnable();
									if(eDye == 0)
									{
										pDoc->m_pRenderer->DrawPicture(param);
									}
									else
									{
										Nuclear::RendererStateAutoRecover stateAutoRecover(pDoc->m_pRenderer);

										pDoc->m_pRenderer->SetRenderEffect(Nuclear::XPRE_DYE);

										pDoc->m_pRenderer->SetShaderTexture(Nuclear::XPRE_DYE,"g_renderTex",pic.pBigPic->handle);
										if(pic.pBigPicPart != NULL)
										{
											pDoc->m_pRenderer->SetShaderTexture(Nuclear::XPRE_DYE,"g_renderTex2",pic.pBigPicPart->handle);
										}
										pDoc->m_pRenderer->SetShaderParam(Nuclear::XPRE_DYE,(const void*)(pDoc->m_pDyeShaderParam));
										pDoc->m_pRenderer->DrawPicture(param);
										pDoc->m_pRenderer->SetShaderTexture(Nuclear::XPRE_DYE,"g_renderTex",NULL);
										if(pic.pBigPicPart != NULL)
										{
											pDoc->m_pRenderer->SetShaderTexture(Nuclear::XPRE_DYE,"g_renderTex2",NULL);
										}
									}

									if (pDoc->m_bDrawEdge)
									{
										pDoc->m_pRenderer->DrawBox(frect, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);
									}
								}
							}
						}
					}
				}
			}
		}

		Nuclear::IFontManager* pFontMan = pDoc->m_pRenderer->GetFontManager();
		float titlePos = static_cast<float>(pDoc->GetTitlePosition());
		if (pDoc->GetAdMode() == 2)
		{
			titlePos += m_MouseDownPos.y - m_MouseNowPos.y;
		}
		if (bIsRiding)
			titlePos += 25 + horseHeight;
		for(int i=0; i<3; ++i)
		{
			if (!pDoc->m_TitleArr[i].IsValid())
				continue;
			pFontMan->DrawText(pDoc->m_TitleArr[i]->text, (clientrect.Width() - pDoc->m_TitleArr[i]->width) / 2, clientrect.Height()/2 - (titlePos * fscale + (1+i)*20), NULL);
		}
		if (pDoc->GetAdMode() == 5 || pDoc->GetAdMode() == 6)
		{
			Nuclear::CPOINT effectPos = pDoc->GetEffectPosition();
			if (pDoc->GetAdMode() == 6)
			{
				effectPos += Nuclear::CPOINT(m_MouseNowPos.x - m_MouseDownPos.x, 
					m_MouseNowPos.y - m_MouseDownPos.y);
			}
			if (bIsRiding)
				effectPos.y = (LONG)(effectPos.y - 25 - horseHeight);
			effectPos.x = (LONG)(fscale * effectPos.x);
			effectPos.y = (LONG)(fscale * effectPos.y);
			effectPos.x += (int)clientrect.Width()/2;
			effectPos.y += (int)clientrect.Height()/2;
			pDoc->m_pRenderer->DrawLine(effectPos.x - 10.0f, effectPos.y - 0.0f, effectPos.x + 10.0f, effectPos.y + 0.0f, 0xFFFF0000);
			pDoc->m_pRenderer->DrawLine(effectPos.x - 0.0f, effectPos.y - 10.0f, effectPos.x + 0.0f, effectPos.y + 10.0f, 0xFFFF0000);
		}
		if( pDoc->m_bBaseOn && pDoc->GetAdMode() == 8) 
		{
			const Nuclear::XPBase &base = pDoc->GetSpriteManager()->GetBase(pDoc->m_CurModelName);
			Nuclear::FRECT frect;
			Nuclear::CPOINT basept[4];
			const float tga = 1.60033f; 
			basept[0] = base.left;
			basept[2] = base.right;
			basept[1].x = (base.left.x+base.right.x)/2 + (LONG)(tga * (base.left.y-base.right.y)/2);
			basept[1].y = (base.left.y+base.right.y)/2 + (LONG)((base.left.x-base.right.x)/(2*tga));
			basept[3].x = (base.left.x+base.right.x)/2 + (LONG)(-tga * (base.left.y-base.right.y)/2);
			basept[3].y = (base.left.y+base.right.y)/2 + (LONG)((base.left.x-base.right.x)/(2*-tga));
			for(int i=0; i< 4; ++i)
			{
				int j = i + 1;
				if( j == 4 ) 
					j = 0;
				frect.left = (clientrect.Width()/2+basept[i].x * fscale);
				frect.top = (clientrect.Height()/2+basept[i].y * fscale);
				frect.right = (clientrect.Width()/2+basept[j].x * fscale);
				frect.bottom = (clientrect.Height()/2+basept[j].y * fscale);
				pDoc->m_pRenderer->DrawLine(frect, 0xff00ffff);
			}

			int slr = 2, srb = 2;
			if( m_adBaseMode == 1 )
				slr = 3;
			else if( m_adBaseMode == 2 )
				srb = 3;

			m_adBase0 = base.left;

			frect.left = (clientrect.Width()/2+m_adBase0.x * fscale-2*slr);
			frect.top = (clientrect.Height()/2+m_adBase0.y * fscale-2*slr);
			frect.right = (clientrect.Width()/2+m_adBase0.x * fscale+2*slr);
			frect.bottom = (clientrect.Height()/2+m_adBase0.y * fscale+2*slr);
			pDoc->m_pRenderer->DrawBox(frect,0xff00ff00, Nuclear::XPFM_WIREFRAME);

			m_adBase2 = base.right;

			frect.left = (clientrect.Width()/2+m_adBase2.x * fscale-2*srb);
			frect.top = (clientrect.Height()/2+m_adBase2.y * fscale-2*srb);
			frect.right = (clientrect.Width()/2+m_adBase2.x * fscale+2*srb);
			frect.bottom = (clientrect.Height()/2+m_adBase2.y * fscale+2*srb);
			pDoc->m_pRenderer->DrawBox(frect,0xff0000ff, Nuclear::XPFM_WIREFRAME);
		}


		if( pDoc->m_bBorderOn && pDoc->GetAdMode() == 7) 
		{
			Nuclear::CRECT border = pDoc->m_CurAction.GetBoundingBox(pDoc->m_PlayState.m_iCurDir);
			Nuclear::FRECT frect;
			frect.left = (clientrect.Width()/2+border.left * fscale);
			frect.top = (clientrect.Height()/2+border.top * fscale);
			frect.right = (clientrect.Width()/2+border.right * fscale);
			frect.bottom = (clientrect.Height()/2+border.bottom * fscale);
			pDoc->m_pRenderer->DrawBox(frect,0xffff0000, Nuclear::XPFM_WIREFRAME);

			int slr = 2, srb = 2;
			if( m_adBorderMode == 1 )
				slr = 3;
			else if( m_adBorderMode == 2 )
				srb = 3;

			m_adBorderLT.x = border.left;
			m_adBorderLT.y = border.top;

			frect.left = (clientrect.Width()/2+border.left * fscale-2*slr);
			frect.top = (clientrect.Height()/2+border.top * fscale-2*slr);
			frect.right = (clientrect.Width()/2+border.left * fscale+2*slr);
			frect.bottom = (clientrect.Height()/2+border.top * fscale+2*slr);
			pDoc->m_pRenderer->DrawBox(frect,0xff00ff00, Nuclear::XPFM_WIREFRAME);

			m_adBorderRB.x = border.right;
			m_adBorderRB.y = border.bottom;

			frect.left = (clientrect.Width()/2+border.right * fscale-2*srb);
			frect.top = (clientrect.Height()/2+border.bottom * fscale-2*srb);
			frect.right = (clientrect.Width()/2+border.right * fscale+2*srb);
			frect.bottom = (clientrect.Height()/2+border.bottom * fscale+2*srb);
			pDoc->m_pRenderer->DrawBox(frect,0xff0000ff, Nuclear::XPFM_WIREFRAME);

		}
	}
	if ( pDoc->m_State == 1 && pDoc->m_nChunkSettingState != 0)	
	{
		if (pDoc->m_pSpriteMaze)
		{
			int gridwidth = 24;
			int gridheight = 16;
			Nuclear::FRECT lines[4];
			int x = static_cast<int>(clientrect.Width() / 2);
			int y = static_cast<int>(clientrect.Height() / 2);
			int w = pDoc->m_pSpriteMaze->GetWidth();
			int h = pDoc->m_pSpriteMaze->GetHeight();
			Nuclear::CPOINT pt = pDoc->m_pSpriteMaze->GetLeftTopPos();
			lines[0].top = lines[1].top = lines[1].bottom = lines[2].top = static_cast<float>(y - pt.y * fscale);
			lines[0].bottom = lines[2].bottom = lines[3].top = lines[3].bottom = lines[0].top + h * gridheight * fscale;
			lines[0].left = lines[0].right = lines[1].left = lines[3].left = static_cast<float>(x - pt.x * fscale);
			lines[1].right = lines[2].left = lines[2].right = lines[3].right = lines[0].left + w * gridwidth * fscale;
			pDoc->m_pRenderer->DrawLine(lines,4,0xFFFF8080);
			Nuclear::FRECT rect;
			rect.top = lines[0].top;
			rect.bottom = rect.top + gridheight * fscale;
			Nuclear::XPCOLOR color[4];
			color[0] = 0x00000000;
			color[1] = 0x6004FD16;
			color[3] = 0x60FF0000;
			int k = 0;
			char* pMaze = pDoc->m_pSpriteMaze->GetMaze();
			for (int j=0;j<h;++j)
			{
				rect.left = lines[0].left;
				rect.right = rect.left + gridwidth * fscale;
				for (int i=0;i<w;++i,++k, ++pMaze)
				{
					pDoc->m_pRenderer->DrawBox(rect, color[*pMaze]);
					rect.left = rect.right;
					rect.right += gridwidth * fscale;
				}
				rect.top = rect.bottom;
				rect.bottom += gridheight * fscale;
			}
			if (pDoc->m_nChunkSettingState != 2)
			{

				pt.x = m_MouseNowPos.x - gridwidth / 2;
				pt.y = m_MouseNowPos.y - gridheight / 2;
				pt.x %= gridwidth;
				if (pt.x < 0)
					pt.x += gridwidth;
				pt.y %= gridheight;
				if (pt.y < 0)
					pt.y += gridheight;

				Nuclear::FRECT mouseBox;
				mouseBox.left = (m_MouseNowPos.x - pt.x - pDoc->m_nBrushSize / 2 * gridwidth) * fscale + clientrect.Width() / 2;
				mouseBox.right = mouseBox.left + pDoc->m_nBrushSize * gridwidth * fscale;
				mouseBox.top = (m_MouseNowPos.y - pt.y - pDoc->m_nBrushSize / 2 * gridheight) * fscale + clientrect.Height() / 2;
				mouseBox.bottom = mouseBox.top + pDoc->m_nBrushSize * gridheight * fscale;
				pDoc->m_pRenderer->DrawBox(mouseBox, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);
			}
		}


		if (pDoc->m_nChunkSettingState == 2)
		{
			CRect rect;
			if (GetAdjRect(pDoc->m_pSpriteMaze, rect))
			{
				Nuclear::FRECT MazeBox;
				MazeBox.left = rect.left * fscale + clientrect.Width() / 2; 
				MazeBox.right = rect.right * fscale + clientrect.Width() / 2;
				MazeBox.top = rect.top * fscale + clientrect.Height() / 2;
				MazeBox.bottom = rect.bottom * fscale + clientrect.Height() / 2;
				pDoc->m_pRenderer->DrawBox(MazeBox, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);

			}
		}

	}

	if( pDoc->m_bCrossOn && !pDoc->m_bUnwrap)
	{
		pDoc->m_pRenderer->DrawLine(0.f,clientrect.Height()/2,clientrect.Width(),clientrect.Height()/2,0xff00ff00);
		pDoc->m_pRenderer->DrawLine(clientrect.Width()/2,0.f,clientrect.Width()/2,clientrect.Height(),0xff0000ff);
	}
	pDoc->m_pRenderer->End();

}

void CSpriteEditorView::OnDraw(CDC* pDC)
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	assert(pDoc);
	if( pDoc->m_pRenderer == NULL )
	{
		pDoc->m_pFileIOMgr = new Nuclear::CFileIOManager();
		if( !pDoc->m_pFileIOMgr->Init(true, true) )
		{
			AfxMessageBox(L"PFS路径初始化失败");
		}

		Nuclear::CreateRenderer(&pDoc->m_pRenderer, m_hWnd, Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)), pDoc->m_pFileIOMgr);
		assert(pDoc->m_pRenderer);

		pDoc->m_pConfigMgr = new Nuclear::ConfigManager(pDoc);
		int nScale = GetPrivateProfileInt(L"SPRITE", L"ThousandOfScale", 1500, theApp.GetExePath() + L"\\SpriteEditorCfg.ini");
		pDoc->m_pConfigMgr->Init();
		pDoc->m_pConfigMgr->SetLogicToWorldScaleX(nScale / 1000.0f);

		pDoc->m_pRenderer->GetFontManager()->AddFontType(1, L"/ui/fonts/DFYuanW7-GB2312.ttf" , 14);
		pDoc->m_pTitleMgr = new Nuclear::TitleManager(pDoc->m_pRenderer->GetFontManager());

		pDoc->m_pSpriteMgr = new Nuclear::SpriteManager(pDoc);
		if (!pDoc->m_pSpriteMgr->PreInit())
		{
			AfxMessageBox(L"精灵管理器初始化整体失败");
			return;
		}
		size_t SprCount = pDoc->m_pSpriteMgr->GetInitCount();
		for (size_t i=0;i<SprCount;++i)
		{
			if( pDoc->m_pSpriteMgr->Init(i, 1) )
			{	
				wchar_t tmpStr[255];
				swprintf_s(tmpStr, 255, L"精灵管理器初始化部分失败，第 %d 号精灵有问题！", i);
				AfxMessageBox(tmpStr);
			}
		}
		if (!pDoc->m_pSpriteMgr->PostInit())
		{
			AfxMessageBox(L"精灵管理器初始化失败");
			return;
		}
		std::vector<std::wstring> v;
		pDoc->m_pSpriteMgr->DumpModelNames(v);
		if( !v.empty() ) pDoc->m_CurModelName = v[0];
		pDoc->m_pAniMgr = new Nuclear::AniManager(pDoc);
		pDoc->m_pSoundManager = new Nuclear::DirectSoundManager(pDoc);
		if( !pDoc->m_pSoundManager->Init(m_hWnd, DSSCL_PRIORITY) ||
			! pDoc->m_pSoundManager->SetPrimaryBufferFormat( 2, 22050, 16 ) )
		{
			AfxMessageBox(L"DirectSound 初始化失败");
		}

		pDoc->ResetLayer();
		m_d3dWidth = pDoc->m_pRenderer->GetDisplayMode().width;
		m_d3dHeight = pDoc->m_pRenderer->GetDisplayMode().height;
		((CSpriteEditorApp*)AfxGetApp())->m_pView = this;

		pDoc->m_BgPicHandle = pDoc->m_pRenderer->LoadPicture(L"./bkground/yueguanglindi.png");

		pDoc->m_TitleArr[0] = pDoc->GetTitleManager()->GetTitle(L"名字0", 1, 0xFFFFFFFF);
		pDoc->m_TitleArr[1] = pDoc->GetTitleManager()->GetTitle(L"名字1", 1, 0xFFFF0000);
		pDoc->m_TitleArr[2] = pDoc->GetTitleManager()->GetTitle(L"名字2", 1, 0xFFFFFFFF);

		RenewSize();
		pDoc->UpdateAllViews(NULL);
	}

	Render();
}

BEGIN_MESSAGE_MAP(CSpriteEditorView, CView)
	ON_WM_ERASEBKGND()
	ON_WM_TIMER()
	ON_WM_KEYDOWN()
	ON_WM_CLOSE()
	ON_WM_MOUSEMOVE()
	ON_WM_RBUTTONDOWN()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_WM_MOUSEWHEEL()
	ON_COMMAND(ID_TEST_SelObj, &CSpriteEditorView::OnTestSelectObject)
	ON_UPDATE_COMMAND_UI(ID_TEST_SelObj, &CSpriteEditorView::OnUpdateTestSelectObject)
END_MESSAGE_MAP()

CSpriteEditorView::~CSpriteEditorView()
{
}

CSpriteEditorView::CSpriteEditorView()
{
	m_adBorderMode = 0;
	m_adBaseMode = 0;
	m_MouseDownPos.x = 0;
	m_MouseDownPos.y = 0;
	m_nStartTime = 0;
	m_bTestObjSel = false;
	m_bSelObj = false;
	m_nCurRegion = 0;
}

bool CSpriteEditorView::ResetMouseCurData()
{
	switch (m_nAdjPosition)
	{
	case AR_TOP:
	case AR_BOTTOM:
		m_hMouseCursor = LoadCursor(NULL, IDC_SIZENS);
		return true;
		break;
	case AR_LEFT:
	case AR_RIGHT:
		m_hMouseCursor = LoadCursor(NULL, IDC_SIZEWE);
		return true;
		break;
	case AR_LEFT | AR_TOP:
	case AR_RIGHT | AR_BOTTOM:
		m_hMouseCursor = LoadCursor(NULL, IDC_SIZENWSE);
		return true;
		break;
	case AR_LEFT | AR_BOTTOM:
	case AR_RIGHT | AR_TOP:
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

void CEquipSelectView::OnEnChangeEditPicPathPrefix()
{
	CString cs;
	GetDlgItem(IDC_EDIT_PIC_PATH_PREFIX)->GetWindowText(cs);
	
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	if(pDoc->m_PicPathPrefix != cs.GetString())
	{
		pDoc->m_PicPathPrefix = cs;
		pDoc->m_bAniDirty = true;
	}
}

void CActionControlView::OnBnClickedButtonSaveScale()
{
	CSpriteEditorDoc* pDoc = (CSpriteEditorDoc*)GetDocument();
	if(pDoc && !pDoc->m_CurModelName.empty())
	{
		Nuclear::SpriteManager::XModel* pModel = pDoc->m_pSpriteMgr->GetXModel(pDoc->m_CurModelName);
		if(pModel)
		{
			pModel->m_fScale = pDoc->m_nScale / 100.f;
			pDoc->m_pSpriteMgr->SaveModels();
		}
	}
}
