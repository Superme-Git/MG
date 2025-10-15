// ParticleSystemEditorView.h : CParticleSystemEditorView 类的接口
//

#pragma once

#include <D3D9.h>
#include <vector>
#include <string>

#include "..\engine\renderer\renderer.h"
#include "..\engine\particlesystem\particlemanager.h"
#include "..\engine\common\fileiomanager.h"
#include "..\engine\common\xptypes.h"
#include "..\engine\particlesystem\iparticlesyscommon.h"

#include "..\engine\particlesystem\PSVertexsObject.h"
#include "..\Voronoi\include\voronoi.h"

class CParticleSystemEditorView : public CView
{
protected: // 仅从序列化创建
	CParticleSystemEditorView();
	DECLARE_DYNCREATE(CParticleSystemEditorView)

public:
	virtual ~CParticleSystemEditorView();

	static CParticleSystemEditorView* getInstance();

public:
	HRESULT CreateVectorD3DTexture(std::vector<std::wstring> vectorTextureName);
//	HRESULT GenPSD3DTexture(std::vector<std::wstring> vectorTextureName); //若干小纹理拼接成一张大纹理..
// 重写
public:
	virtual void OnDraw(CDC* pDC);  // 重写以绘制该视图
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

public:
	Nuclear::ParticleManager* m_pParSysManager;

private:
	Nuclear::Renderer *m_pRenderer;

	//抽象粒子---以下
	Nuclear::SHAPE_LIST m_shapeList;
	Nuclear::PictureHandle m_pD3dTextureShapes; //抽象粒子特效用的纹理... ----从particleManager里面传过来的..这里面不做管理！
	
	Nuclear::PSVertexsObject m_vertexsObject;
	//以上

public:
	bool m_bPologyModify; //标记分块多边形是否有修改
	CString m_vtxsFilePath; //vtxs文件完整路径

private:
	std::vector<Nuclear::PictureHandle> m_vectorD3DTexture; //小纹理..

	bool m_bDeviceLost;
	BOOL m_bTrackFx;
	BOOL m_bShowBKpic;
	BOOL m_bMoveMap;
	BOOL m_bSetParticleHoleFlag;
	BOOL m_bSetMaxBox;
	BOOL m_bBlindArea;
	BOOL m_bBlockLine;
	BOOL m_bReflectLine;

	bool m_bStop;
	float m_fLastTime;
	BOOL m_bPologyOk; //控制鼠标消息--是否拆分前景图..
	bool m_bSetShapeTexture; //设置了纹理之后才能做拆分--保存--加载等工作

	BOOL m_bSetEmitterSpinCentFlag; //左键单击设置发射器旋转中心

	Nuclear::FRECT m_FrctMapPos;
	CPoint m_initialPoint; //拖动地图..
	Nuclear::CRECT m_ViewPort;
	UINT m_nMouseMoveCnt;

	Nuclear::PictureHandle m_hBKpicHandle; //背景图片纹理句柄..
	Nuclear::PictureHandle m_hShapePicHandle; //抽象粒子特效纹理句柄..用来编辑拆分用的..

	int m_nShapeTextureWidth;
	int m_nShapeTextureHeight;

private:
	CPoint m_InitBoxPoint; //画包围盒、盲区、反射线、阻挡线等的时候用的..
	CPoint m_oldBoxPoint;

public:
	CRect m_rctMaxBox; //最大包围盒...这里存的是屏幕坐标..

private:
	voronoi::Voronoi m_voronoi; //拆分前景地图用的..
	CPoint m_ptLastPos; //移动块..
	int m_nMouseDownIndex;

	int m_nVertexRow; //0-----40 //转场特效的前景拆分（四边形和三角形）
	int m_nVertexCol; //0-----40
	int m_nVertexType;

	int m_nLineAngle;

private: //同一个psl里用到的纹理都是一个大小
//	int m_nTexWidth; //当前psl中一个小纹理的宽、高
//	int m_nTexHeight;
//	int m_nPslTexWidth; //小纹理合并之后的大纹理宽、高
//	int m_nPslTexHeight;
//	int m_nTexNum; //小纹理个数

//	Nuclear::PSTEXTUREINFO m_psTextureInfo;	
	//小纹理在大纹理中的摆放顺序按行优先...

private:
	virtual void OnInitialUpdate();
	void SetParticleSysPos(CPoint point);
	void ReleaseVectorD3Dtexture(std::vector<Nuclear::PictureHandle> &vectorD3DTexture);

	bool GenPSTextureInfo(const std::vector<Nuclear::PictureHandle>& vectorD3DTexture, Nuclear::PSTEXTUREINFO* ppslTexture);

public:
	bool InitD3D(void);
	Nuclear::Renderer* GetRenderer() const { return m_pRenderer; }
	float GetFPS() { return m_pRenderer->GetFPS(); }
	void Render(void);
	void SetObjTrackFlag(BOOL bTrackFx);
	// 释放以前的PictureHandle..创建新的..m_hBKpicHandle
	void CreateBKTextureHandle(const std::wstring& strBKpicFilePath);
	void CreateShapeTexture(const CString& filePath);
	void ComputeShapeVertex();

	void SetShowBKPicFlag(BOOL bShowBKpic) { m_bShowBKpic = bShowBKpic; }
	void SetMoveBKPicFlag(BOOL bMoveMap) { m_bMoveMap = bMoveMap; }
	void SetEmitterSpinCentFlag(BOOL bSetEmitterSpinCentFlag) { m_bSetEmitterSpinCentFlag = bSetEmitterSpinCentFlag; }
	void SetParticleHoleFlag(BOOL bSetParticleHoleFlag) { m_bSetParticleHoleFlag = bSetParticleHoleFlag; }
	void SetMaxBoxFlag(BOOL bSetMaxBox) { m_bSetMaxBox = bSetMaxBox; }
	void SetBlindAreaFlag(BOOL bBlindArea) { m_bBlindArea = bBlindArea; }

	void SetBlockLineFlag(BOOL bBlockLine) { m_bBlockLine = bBlockLine; }
	void SetReflectLineFlag(BOOL bReflectLine) { m_bReflectLine = bReflectLine; }

	void SetStopFlag(bool bStop) { m_bStop = bStop; }
	void SetPologyFlag(bool bPologyOk) { m_bPologyOk = bPologyOk; }
	void SaveShapeList(const CString& filePath);
	void LoadShapeList(const CString& filePath);

	void SetShapeListData(const std::wstring& shapeListName, const Nuclear::SHAPE_LIST& shapeList); //打开抽象psl时初始化预览数据...

	void SetVertexType(int nVertexType);
	void SetVertexRow(int nVertexRow);
	void SetVertexCol(int nVertexCol);
	void SetLineAngle(int nLineAngle);

	int GetVertexType() { return m_nVertexType; }
	int GetVertexRow() { return m_nVertexRow; }
	int GetVertexCol() { return m_nVertexCol; }
	int GetLineAngle() { return m_nLineAngle; }

	const Nuclear::FRECT& GetFrctMapPos() { return m_FrctMapPos; }

	bool CutBigPicToSmallPic(int nRow, int nCol, const CString& strPath);//美术切割资源用
	void PrintScreenToFile();
	void StrtAni(); //开始保存序列帧
	void EndAni();  //结束保存序列帧

	void SetPrintScreenParam(CString filePath, CString pslName, Nuclear::XPIMAGE_FILEFORMAT fileformat, Nuclear::CRECT srcRct, int nFramePerSecond);
	CString m_strFilePath;
	CString m_strPslName;
	Nuclear::XPIMAGE_FILEFORMAT m_nFileFormat;
	Nuclear::CRECT m_RctScreen;
	int m_nFramePerSecond;

	int m_nFrameIndex;//当前帧序号---用来生成名字...

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnTimer(UINT_PTR nIDEvent);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMenuProperties();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnRButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDblClk(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);

	afx_msg void OnOpenPsl();
	afx_msg void OnSavePsl();
	afx_msg void OnSaveasPsl();
	afx_msg void OnBtnTga2dds();
};

