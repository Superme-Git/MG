// LinkedObjectEditorView.h : CLinkedObjectEditorView 类的接口
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
protected: // 仅从序列化创建
	CLinkedObjectEditorView();
	DECLARE_DYNCREATE(CLinkedObjectEditorView)

	virtual void OnInitialUpdate(); 
// 操作
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
	void RefreshLinkedObject();//外部修改了img素材之后刷新包围盒等..

	bool CreateDTList(const Nuclear::PLinkedObject& plantObject); //打开新文件时调用
	bool ResetDTList();//重设主节点时调用...

	int GetDTListIndex(DWORD dwFileID);//从key找到绘制顺序---然后再找到其在dtlist中的存放位置

	void SetLightAlpha(int nLightAlpha);

	void SetLinkedObjectDragFlag(bool bDragFlag);
	void SetLinkedObjectPreviewFlag(bool bPreviewFlag);

public:
	LkoTreeNodeListEX m_DynamicLinkedObject; //允许别的view修改..

// 重写
public:
	virtual void OnDraw(CDC* pDC);  // 重写以绘制该视图
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

public: //DOC中保存文件时用到..先public吧..
	Nuclear::CPOINT m_ptCentPos; //plant的重心,(相对于主节点的左上角..)
	Nuclear::CPOINT m_ptBasePos[4];//plant的底座,----位置关系同上	
	bool m_bLKOEnableZoom; //是否允许被干扰
	float m_fLKOzoom;//干扰最激烈程度
	float m_fLKOTime;//恢复所需时间

	typedef stdext::hash_map<DWORD, Nuclear::PLANTPICINFO> HashMapLinkedObjectInfo;
	HashMapLinkedObjectInfo m_mapLinkedObjectInfo; //ID--读文件时建立...不写磁盘..

private:
	Nuclear::Renderer* m_pRender;
	Nuclear::FRECT m_FrctMainNodPos; //这里存的是绝对坐标 ---

	struct IMGPICHANDLEINFO 
	{//对应img中的一小块..
		Nuclear::PictureHandle m_handlePic;
		Nuclear::CRECT m_rct; //相对于该图片左上角的坐标..
		IMGPICHANDLEINFO()
		{
			m_handlePic = 0;
			m_rct = Nuclear::CRECT();
		}
	};

	typedef std::vector<IMGPICHANDLEINFO> PicHandleInfoVetor;
	std::map<std::wstring, PicHandleInfoVetor> m_mapPicHandle; //这里面存各组件对应的handle--..

	struct IMGPICMOVEPARAM// update(time)实时运动参数..
	{
		float m_fAngle;
		float m_fSkewx;
		Nuclear::CPOINT m_ptOffset; //PLANTPICINFO 中存的是美术设置值...
		//这里是update()时如果该节点运动跟随主节点就会改变的
		//左上角相对主节点的左上角的坐标..（主节点左上角为0,0）--水平右x+ 竖直下y+
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
	bool m_bDragFlag; //组成lko的各物件是否允许拖动

	bool m_bPreviewLKO; //预览与编辑两种状态

	float m_fLastTime;

//	std::wstring m_curMouseFile;
//	std::wstring m_strMainNodeName; //主节点名-- 左侧列表框、或者view中右键删除主节点时候修改为空

	DWORD m_curMouseFileID;
	DWORD m_dwMainNodeID; //主节点名-- 左侧列表框、或者view中右键删除主节点时候修改为0
	DWORD m_dwMainNodeID_last;

	DWORD m_dwPicIDMax;//ID最大值..

	int m_nLightAlpha;

public:
//	std::wstring m_curSelFileName; //当前选定项.. 左侧列表框可选择修改
	DWORD m_curSelFileID;

	//以下----预览相关
private:
	Nuclear::PLinkedObject m_plantObjectPreview;
	LkoTreeNodeListEX m_DynamicLinkedObjectPreview;
	std::map<std::wstring, PicHandleInfoVetor> m_mapPreviewPicHandle; //这里面存各组件对应的handles--..

public:
	bool OpenPreviewFile(CString strLkoFileName);
private:
	bool CreatePreviewDTList(const Nuclear::PLinkedObject& previewObj);
	//以上


public://redo undo功能
	struct MOVENODEPARAM 
	{//移动节点操作
		DWORD dwFileID;
		int dx;
		int dy;
	};
	std::vector<MOVENODEPARAM> m_VectorUndoOperations;
	std::vector<MOVENODEPARAM> m_VectorRedoOperations;
	
private: //重心底座...
//	CPoint m_ptCentPos; //重心...相对主节点的左上角的坐标..（主节点左上角为0,0）--水平右x+ 竖直下y+
//	CPoint m_pointBorderVertex[4]; //底座...相对位置关系同上

protected:
	std::map<Nuclear::XPTEXTURE_FORMAT, std::wstring> m_fmtMap; //img文件格式

private: //设置重心底座相关...
	bool m_bDrawBorder;
	CPoint m_initialPoint;
	CPoint m_ptFrom[2];
	CPoint m_ptTo[2];
	CPoint m_ptPos[4]; //底座原始坐标--用于绘制橡皮筋线用

	bool m_bMoveBorder;
	int m_nMovePointIndex;

	bool m_bLoadPic;//打开psl文件时设置为false，render时先检查..

private:
	float Computer2PointDis(CPoint pt1, CPoint pt2);
	void Update(float timeDelta);
	void InvertLine(CDC* pDC, CPoint ptFrom, CPoint ptTo);
	void ComputerPltFilePos(const CPoint& initialPoint, const CPoint& point);
	void SetPltFilePos();
	void InitData();
	bool MoveSelectedElements(DWORD dwFileID, int dx, int dy);
	bool MovePreviewLKO(int dx, int dy);

// 实现
public:
	virtual ~CLinkedObjectEditorView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
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


