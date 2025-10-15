// LinkedObjectEditorView.h : CLinkedObjectEditorView ��Ľӿ�
//

#pragma once

#include <vector>
#include <string>
#include <map>

#include "..\engine\renderer\renderer.h"
#include "..\engine\map\plinkedobject.h"

#include <hash_map>
#include "LkoTreeNodeListEX.h"


class CLinkedObjectEditorView : public CView
{
protected: // �������л�����
	CLinkedObjectEditorView();
	DECLARE_DYNCREATE(CLinkedObjectEditorView)

	virtual void OnInitialUpdate(); 
// ����
public:
	int AddPicMap(CString strFilePath);
	void SetBKColor(DWORD cr) { m_dwBKcolor = cr; }
	
	void Render();
	void RenderPreviewLKO();

	BOOL DelNode(DWORD curSelFileID);
	BOOL SetMainNode(DWORD dwFileID);
	void SetLinkedObjectPosCenterFlag(bool bSetLinkedObjectPosCenter);
	void SetLinkedObjectStopFlag(bool bStop);
	void SetLinkedObjectStopState(bool bFlag);
	void SetLinkedObjectData(const Nuclear::PLinkedObject& plantObj);
	Nuclear::CRECT ComputeLinkedObjectMaxBox(const Nuclear::PLinkedObject& plantObject);
	Nuclear::CRECT ComputeLinkedObjectMaxBox();
	void RefreshLinkedObject();//�ⲿ�޸���img�ز�֮��ˢ�°�Χ�е�..

	bool CreateDTList(const Nuclear::PLinkedObject& plantObject); //�����ļ�ʱ����
	bool ResetDTList();//�������ڵ�ʱ����...

	int GetDTListIndex(DWORD dwFileID);//��key�ҵ�����˳��---Ȼ�����ҵ�����dtlist�еĴ��λ��

	void SetLightAlpha(int nLightAlpha);

	void SetLinkedObjectDragFlag(bool bDragFlag);
	void SetLinkedObjectPreviewFlag(bool bPreviewFlag);

public:
	LkoTreeNodeListEX m_DynamicLinkedObject; //������view�޸�..

// ��д
public:
	virtual void OnDraw(CDC* pDC);  // ��д�Ի��Ƹ���ͼ
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

public: //DOC�б����ļ�ʱ�õ�..��public��..
	Nuclear::CPOINT m_ptCentPos; //plant������,(��������ڵ�����Ͻ�..)
	Nuclear::CPOINT m_ptBasePos[4];//plant�ĵ���,----λ�ù�ϵͬ��	
	bool m_bLKOEnableZoom; //�Ƿ���������
	float m_fLKOzoom;//������ҳ̶�
	float m_fLKOTime;//�ָ�����ʱ��

	typedef stdext::hash_map<DWORD, Nuclear::PLANTPICINFO> HashMapLinkedObjectInfo;
	HashMapLinkedObjectInfo m_mapLinkedObjectInfo; //ID--���ļ�ʱ����...��д����..

private:
	Nuclear::Renderer* m_pRender;
	Nuclear::FRECT m_FrctMainNodPos; //�������Ǿ������� ---

	struct IMGPICHANDLEINFO 
	{//��Ӧimg�е�һС��..
		Nuclear::PictureHandle m_handlePic;
		Nuclear::CRECT m_rct; //����ڸ�ͼƬ���Ͻǵ�����..
		IMGPICHANDLEINFO()
		{
			m_handlePic = 0;
			m_rct = Nuclear::CRECT();
		}
	};

	typedef std::vector<IMGPICHANDLEINFO> PicHandleInfoVetor;
	std::map<std::wstring, PicHandleInfoVetor> m_mapPicHandle; //�������������Ӧ��handle--..

	struct IMGPICMOVEPARAM// update(time)ʵʱ�˶�����..
	{
		float m_fAngle;
		float m_fSkewx;
		Nuclear::CPOINT m_ptOffset; //PLANTPICINFO �д������������ֵ...
		//������update()ʱ����ýڵ��˶��������ڵ�ͻ�ı��
		//���Ͻ�������ڵ�����Ͻǵ�����..�����ڵ����Ͻ�Ϊ0,0��--ˮƽ��x+ ��ֱ��y+
		IMGPICMOVEPARAM()
		{
			m_fAngle = 0.0f;
			m_fSkewx = 0.0f;
			m_ptOffset = Nuclear::CPOINT();
		}
	};
	std::map<DWORD, IMGPICMOVEPARAM> m_mapMoveParam; 

	DWORD m_dwBKcolor;
	bool m_bSetLinkedObjectPosCenter;
	bool m_bStop;
	bool m_bDragFlag; //���lko�ĸ�����Ƿ������϶�

	bool m_bPreviewLKO; //Ԥ����༭����״̬

	float m_fLastTime;

//	std::wstring m_curMouseFile;
//	std::wstring m_strMainNodeName; //���ڵ���-- ����б�򡢻���view���Ҽ�ɾ�����ڵ�ʱ���޸�Ϊ��

	DWORD m_curMouseFileID;
	DWORD m_dwMainNodeID; //���ڵ���-- ����б�򡢻���view���Ҽ�ɾ�����ڵ�ʱ���޸�Ϊ0
	DWORD m_dwMainNodeID_last;

	DWORD m_dwPicIDMax;//ID���ֵ..

	int m_nLightAlpha;

public:
//	std::wstring m_curSelFileName; //��ǰѡ����.. ����б���ѡ���޸�
	DWORD m_curSelFileID;

	//����----Ԥ�����
private:
	Nuclear::PLinkedObject m_plantObjectPreview;
	LkoTreeNodeListEX m_DynamicLinkedObjectPreview;
	std::map<std::wstring, PicHandleInfoVetor> m_mapPreviewPicHandle; //�������������Ӧ��handles--..

public:
	bool OpenPreviewFile(CString strLkoFileName);
private:
	bool CreatePreviewDTList(const Nuclear::PLinkedObject& previewObj);
	//����


public://redo undo����
	struct MOVENODEPARAM 
	{//�ƶ��ڵ����
		DWORD dwFileID;
		int dx;
		int dy;
	};
	std::vector<MOVENODEPARAM> m_VectorUndoOperations;
	std::vector<MOVENODEPARAM> m_VectorRedoOperations;
	
private: //���ĵ���...
//	CPoint m_ptCentPos; //����...������ڵ�����Ͻǵ�����..�����ڵ����Ͻ�Ϊ0,0��--ˮƽ��x+ ��ֱ��y+
//	CPoint m_pointBorderVertex[4]; //����...���λ�ù�ϵͬ��

protected:
	std::map<Nuclear::XPTEXTURE_FORMAT, std::wstring> m_fmtMap; //img�ļ���ʽ

private: //�������ĵ������...
	bool m_bDrawBorder;
	CPoint m_initialPoint;
	CPoint m_ptFrom[2];
	CPoint m_ptTo[2];
	CPoint m_ptPos[4]; //����ԭʼ����--���ڻ�����Ƥ������

	bool m_bMoveBorder;
	int m_nMovePointIndex;

	bool m_bLoadPic;//��psl�ļ�ʱ����Ϊfalse��renderʱ�ȼ��..

private:
	float Computer2PointDis(CPoint pt1, CPoint pt2);
	void Update(float timeDelta);
	void InvertLine(CDC* pDC, CPoint ptFrom, CPoint ptTo);
	void ComputerPltFilePos(const CPoint& initialPoint, const CPoint& point);
	void SetPltFilePos();
	void InitData();
	bool MoveSelectedElements(DWORD dwFileID, int dx, int dy);
	bool MovePreviewLKO(int dx, int dy);

// ʵ��
public:
	virtual ~CLinkedObjectEditorView();
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
	afx_msg void OnMenuDelcurselobject();
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);

public:
	afx_msg void OnMenuSetplantmoveparam();
	afx_msg void OnTimer(UINT_PTR nIDEvent);
protected:
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);

public:
	afx_msg void OnMenuLightobject();
	afx_msg void OnMenuSetzoomparam();
	afx_msg void OnEditUndo();
	afx_msg void OnUpdateEditUndo(CCmdUI *pCmdUI);
	afx_msg void OnEditRedo();
	afx_msg void OnUpdateEditRedo(CCmdUI *pCmdUI);
};


