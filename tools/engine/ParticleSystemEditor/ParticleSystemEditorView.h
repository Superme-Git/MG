// ParticleSystemEditorView.h : CParticleSystemEditorView ��Ľӿ�
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
protected: // �������л�����
	CParticleSystemEditorView();
	DECLARE_DYNCREATE(CParticleSystemEditorView)

public:
	virtual ~CParticleSystemEditorView();

	static CParticleSystemEditorView* getInstance();

public:
	HRESULT CreateVectorD3DTexture(std::vector<std::wstring> vectorTextureName);
//	HRESULT GenPSD3DTexture(std::vector<std::wstring> vectorTextureName); //����С����ƴ�ӳ�һ�Ŵ�����..
// ��д
public:
	virtual void OnDraw(CDC* pDC);  // ��д�Ի��Ƹ���ͼ
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

public:
	Nuclear::ParticleManager* m_pParSysManager;

private:
	Nuclear::Renderer *m_pRenderer;

	//��������---����
	Nuclear::SHAPE_LIST m_shapeList;
	Nuclear::PictureHandle m_pD3dTextureShapes; //����������Ч�õ�����... ----��particleManager���洫������..�����治������
	
	Nuclear::PSVertexsObject m_vertexsObject;
	//����

public:
	bool m_bPologyModify; //��Ƿֿ������Ƿ����޸�
	CString m_vtxsFilePath; //vtxs�ļ�����·��

private:
	std::vector<Nuclear::PictureHandle> m_vectorD3DTexture; //С����..

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
	BOOL m_bPologyOk; //���������Ϣ--�Ƿ���ǰ��ͼ..
	bool m_bSetShapeTexture; //����������֮����������--����--���صȹ���

	BOOL m_bSetEmitterSpinCentFlag; //����������÷�������ת����

	Nuclear::FRECT m_FrctMapPos;
	CPoint m_initialPoint; //�϶���ͼ..
	Nuclear::CRECT m_ViewPort;
	UINT m_nMouseMoveCnt;

	Nuclear::PictureHandle m_hBKpicHandle; //����ͼƬ������..
	Nuclear::PictureHandle m_hShapePicHandle; //����������Ч������..�����༭����õ�..

	int m_nShapeTextureWidth;
	int m_nShapeTextureHeight;

private:
	CPoint m_InitBoxPoint; //����Χ�С�ä���������ߡ��赲�ߵȵ�ʱ���õ�..
	CPoint m_oldBoxPoint;

public:
	CRect m_rctMaxBox; //����Χ��...����������Ļ����..

private:
	voronoi::Voronoi m_voronoi; //���ǰ����ͼ�õ�..
	CPoint m_ptLastPos; //�ƶ���..
	int m_nMouseDownIndex;

	int m_nVertexRow; //0-----40 //ת����Ч��ǰ����֣��ı��κ������Σ�
	int m_nVertexCol; //0-----40
	int m_nVertexType;

	int m_nLineAngle;

private: //ͬһ��psl���õ���������һ����С
//	int m_nTexWidth; //��ǰpsl��һ��С����Ŀ���
//	int m_nTexHeight;
//	int m_nPslTexWidth; //С����ϲ�֮��Ĵ��������
//	int m_nPslTexHeight;
//	int m_nTexNum; //С�������

//	Nuclear::PSTEXTUREINFO m_psTextureInfo;	
	//С�����ڴ������еİڷ�˳��������...

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
	// �ͷ���ǰ��PictureHandle..�����µ�..m_hBKpicHandle
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

	void SetShapeListData(const std::wstring& shapeListName, const Nuclear::SHAPE_LIST& shapeList); //�򿪳���pslʱ��ʼ��Ԥ������...

	void SetVertexType(int nVertexType);
	void SetVertexRow(int nVertexRow);
	void SetVertexCol(int nVertexCol);
	void SetLineAngle(int nLineAngle);

	int GetVertexType() { return m_nVertexType; }
	int GetVertexRow() { return m_nVertexRow; }
	int GetVertexCol() { return m_nVertexCol; }
	int GetLineAngle() { return m_nLineAngle; }

	const Nuclear::FRECT& GetFrctMapPos() { return m_FrctMapPos; }

	bool CutBigPicToSmallPic(int nRow, int nCol, const CString& strPath);//�����и���Դ��
	void PrintScreenToFile();
	void StrtAni(); //��ʼ��������֡
	void EndAni();  //������������֡

	void SetPrintScreenParam(CString filePath, CString pslName, Nuclear::XPIMAGE_FILEFORMAT fileformat, Nuclear::CRECT srcRct, int nFramePerSecond);
	CString m_strFilePath;
	CString m_strPslName;
	Nuclear::XPIMAGE_FILEFORMAT m_nFileFormat;
	Nuclear::CRECT m_RctScreen;
	int m_nFramePerSecond;

	int m_nFrameIndex;//��ǰ֡���---������������...

protected:

// ���ɵ���Ϣӳ�亯��
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

