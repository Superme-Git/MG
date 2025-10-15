// ImageEditorView.h : CImageEditorView 类的接口
//

#pragma once

#include "ImageEditor.h"
#include "..\engine\common\xptypes.h"
#include "..\engine\common\util.h"
#include "..\engine\map\pimg.h"
#include "..\engine\renderer\renderer.h"
#include "..\engine\sprite\sprite.h"
#include "..\engine\engine\enginebase.h"




using Nuclear::Renderer;
using Nuclear::XDisplayMode;
using Nuclear::FRECT;
using Nuclear::PImg;

class CImageEditorView : public CView, public Nuclear::EngineBase
{
protected: // 仅从序列化创建
	CImageEditorView();
	DECLARE_DYNCREATE(CImageEditorView)

private:
	RECT m_rectClientInit;	// 初始D3D设备的大小
	RECT m_rectClient;		// 客户区域
	Renderer *m_pRenderer;

	std::vector<Nuclear::PictureHandle> m_vectorPicID;

	float m_fcoefU; //纹理坐标..
	float m_fcoefV;

	bool m_bDrawBorder;
	CPoint m_initialPoint;
	CPoint m_ptFrom[2];
	CPoint m_ptTo[2];
	float m_fPosx[4];
	float m_fPosy[4];

	int	m_nViewWidth;
	int	m_nViewHeight;
	int m_nScrollPosX;
	int m_nScrollPosY;
	int m_nVPageSizeX;
	int m_nVPageSizeY;
	bool m_bIsPlay;

	bool m_bMoveBorder;
	int m_nMovePointIndex;
	CPoint m_pointBorderCent[4];
	CPoint m_pointBorderVertex[4];
	int m_nScollCnt;
	float m_fZoomRate;

	int	m_nViewWidth0;
	int	m_nViewHeight0;

	BOOL m_bSetMaskpt;
	BOOL m_bAdjustMaskpt;
	BOOL m_bModifyMask;
	int m_nCurSelMaskpt; //当前选中的遮挡多边形点

	int m_nRButtonSelpt;


	bool m_bPicTransFlag;

private:
	int m_nMouseMoveCnt;//鼠标移动时界面刷新频率
	std::vector<Nuclear::CPOINT> m_imgMasks;
	//以前是FRECT//做中间运算..imgFile中的mask是int，mousemove中有缩放时候误差积累严重..

private:
	//精灵相关...
	int m_nSpriteVx;
	int m_nSpriteVy;
	//Nuclear::Sprite* m_pSprite;

	Nuclear::SpriteManager *m_pSprMan;
	Nuclear::ConfigManager *m_pConfigMan;
	Nuclear::AniManager *m_pAniMan;

private:
	//精灵
	Nuclear::Sprite* LoadSprite();
	void SetSpriteAnimAndDirect();
	void UpdateSprite();

	//Nuclear::EngineBase
	virtual Nuclear::CFileIOManager* GetFileIOManager()
	{
		return theApp.m_pFileIOManager;
	}
	virtual Nuclear::SpriteManager* GetSpriteManager()
	{
		return m_pSprMan;
	}
	virtual Nuclear::ConfigManager* GetConfigManager()
	{
		return m_pConfigMan;
	}
	virtual Nuclear::AniManager* GetAniManager()
	{
		return m_pAniMan;
	}

	virtual Nuclear::EffectManager* GetEffectManager() 
	{
		return NULL; 
	}
	virtual Nuclear::StepSoundType* GetStepSoundType() const { return NULL; }

	virtual Nuclear::Renderer* GetRenderer(HWND hwnd = NULL)
	{
		return m_pRenderer;
	}
	virtual Nuclear::CRECT GetViewport() const
	{
		Nuclear::CRECT rect;
		rect.top = m_rectClient.top;//m_DrawScrollPos.y;
		rect.left = m_rectClient.left;//m_DrawScrollPos.x;
		rect.right = m_rectClient.right;//rect.left + m_nViewportWidth;
		rect.bottom = m_rectClient.bottom;//rect.top + m_nViewportHeight;
		return rect;
	}

	virtual int GetTick() const { return Nuclear::GetMilliSeconds(); } 
	virtual Nuclear::IApp* GetApp()  { return NULL; }

public:
	Renderer* GetDX9Render() { return m_pRenderer; }
	void SetViewSize(int iViewWidth, int iViewHeight);
	void ReSetViewSize();
	void SetViewSize0();

	void InitBorderCentPos();

	void SetMaskRctFlag(BOOL bSetMaskRct);

	void SetAdjustMaskRctFlag(BOOL bAdjustMaskRct) { m_bAdjustMaskpt = bAdjustMaskRct; }
	void SetZoomRate(float fZoomRate) { m_fZoomRate = fZoomRate; }

	void Render();

private:
	void DrawPic();
	void InvertLine (CDC* pDC, CPoint ptFrom, CPoint ptTo);
	void ComputerImgFilePos(const CPoint& initialPoint, const CPoint& point);
	void SetImgFilePos(PImg& ImgFile);
	float Computer2PointDis(CPoint pt1, CPoint pt2);
	CPoint ZoomPoint(CPoint pt, float fZoomRate);

// 重写
public:
	virtual void OnDraw(CDC* pDC);  // 重写以绘制该视图
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

// 实现
public:
	virtual ~CImageEditorView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:
	DECLARE_MESSAGE_MAP()

private:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
//public:
	afx_msg void OnClose();

	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnDelmask();
public:
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	virtual void OnInitialUpdate();
	afx_msg void OnTimer(UINT_PTR nIDEvent);
};


