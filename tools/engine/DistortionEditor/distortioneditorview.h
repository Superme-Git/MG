// DistortionEditorView.h : CDistortionEditorView ��Ľӿ�
//

#pragma once

#include <vector>
#include <string>
#include <map>

#include "..\engine\renderer\renderer.h"
#include "..\engine\map\pdistortionobject.h"

#include "ToolsDistort.h"

using namespace Nuclear;

class CDistortionEditorView : public CView , public Nuclear::EngineBase
{
protected: // �������л�����
	CDistortionEditorView();
	DECLARE_DYNCREATE(CDistortionEditorView)
public:
	virtual void OnInitialUpdate();
// ����
public:
	void SetBKColor(DWORD cr) { m_dwBKcolor = cr; }
	void Render();

public:
// ��д
public:
	virtual void OnDraw(CDC* pDC);  // ��д�Ի��Ƹ���ͼ
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

public:
	void AddPicToDis(CString strFilePath, bool bLightFlag);
	CToolsDistort* GetDistortionObjEffect() { return m_distortionObjEffect; }

private:
// 	struct IMGPICHANDLEINFO 
// 	{//��Ӧimg�е�һС��..
// 		Nuclear::PictureHandle m_handlePic;
// 		Nuclear::CRECT m_rct; //����ڸ�ͼƬ���Ͻǵ�����..
// 		IMGPICHANDLEINFO()
// 		{
// 			m_handlePic = 0;
// 			m_rct = Nuclear::CRECT();
// 		}
// 	};

	Nuclear::FRECT m_frctImgPos; //��ͼλ��..Ϊ����������϶��ӵ�...
	CPoint m_initialPoint;

private:
	SystemResourceManager* m_SystemResoureManager;
	Nuclear::Renderer* m_pRender;
	CToolsDistort* m_distortionObjEffect;

	DWORD m_dwBKcolor;
	float m_fLastTime;
	float m_fDistortionPos;

public:
//	std::wstring m_curSelFileName; //��ǰѡ����.. ����б���ѡ���޸�
	DWORD m_curSelFileID;
	bool m_bLoadFile;//���ļ�ʱ����Ϊtrue��renderʱ�ȼ��..

	FPOINT m_CenterPoint; //���� ---���ͼƬ���Ͻǵ�----����������...
	FPOINT m_pos[4]; //����... �� �� �� �� 0 1 2 3

private:

private:

private:
	float Computer2PointDis(CPoint pt1, CPoint pt2);
	float Update(const Nuclear::PDistortionObject& distortionObj, float timeDelta); //����fPos
	void InvertLine(CDC* pDC, CPoint ptFrom, CPoint ptTo);

// ʵ��
public:
	virtual ~CDistortionEditorView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()

private:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDblClk(UINT nFlags, CPoint point);

public:
	afx_msg void OnTimer(UINT_PTR nIDEvent);
protected:
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);

public:
	//from engineBase
	virtual SystemResourceManager* GetSystemResourceManager() { return m_SystemResoureManager; }
	virtual CFileIOManager* GetFileIOManager() { return NULL; }
	virtual SpriteManager* GetSpriteManager() { return NULL; }
	virtual AniManager* GetAniManager() { return NULL; }
	virtual EffectManager* GetEffectManager() { return NULL; }
	virtual Renderer* GetRenderer(HWND hwnd = NULL) { return m_pRender; }
	virtual ConfigManager* GetConfigManager() { return NULL; }
	virtual StepSoundType* GetStepSoundType() const { return NULL; }
	virtual CRECT GetViewport() const
	{
		CRect rectClient;
		GetClientRect(&rectClient);
		Nuclear::CRECT rect;
		rect.top = rectClient.top;//m_DrawScrollPos.y;
		rect.left = rectClient.left;//m_DrawScrollPos.x;
		rect.right = rectClient.right;//rect.left + m_nViewportWidth;
		rect.bottom = rectClient.bottom;//rect.top + m_nViewportHeight;
		return rect;
	}

	virtual int GetTick() const { return 0; }
	virtual Nuclear::IApp* GetApp()  { return NULL; }
	afx_msg void OnMenuDellight();
};


