// SoundEditorDoc.h : CSoundEditorDoc 类的接口
//
#pragma once
#include "engine\common\util.h"
#include "engine\common\pfsutil.h"
#include "xmlio\xmlio.h"
#include <set>
enum EDIT_STATE 
{
	EDIT_STATE_MAZE = 0,		//编辑阻挡点
	EDIT_STATE_NPC	= 1,		//编辑NPC
	EDIT_STATE_MapJump=2,       //地图跳转点编辑
	EDIT_STATE_TESTING
};
enum SELECT_STATE
{
	SS_SELECTED = 0,		//已经被选中了的
	SS_SELECTED_AREA_POINT,	//已经选中了的区域的一个点
	SS_NEWSELECTED,			//选中了个新的
	SS_ENPTY,				//选了个空地方
};

enum POLYGON_MOUSE_STATE
{
	PMS_NORMAL = 0,
	PMS_DRAGGING,
	PMS_MOVING_POINT,
	PMS_CUTTING
};

enum UPDATE_VIEW_SIGN
{
	UVS_TAB = 1,
	UVS_VIEWPORT = 2,
	UVS_ENV_SOUND_AREA_SELECTED = 4,
	UVS_ENV_SOUND_AREA_SHAPE = 8,
	UVS_ALL = 0xFFFFFFFF,
};

class CSoundEditorDoc : public CDocument
{
protected: // 仅从序列化创建
	CSoundEditorDoc();
	DECLARE_DYNCREATE(CSoundEditorDoc)
//自定义类型们
public:
	

// 属性
private:
	EDIT_STATE m_editState;
	Nuclear::CPOINT m_mapSize;
public:
	Nuclear::XPIShape *m_pSelectedShape;
	int m_nSelectedPt;
	POLYGON_MOUSE_STATE m_PolygonMouseState;
	RECT m_CutingPolygonLine;
	bool m_bIsIngoreDragging;//以后各种State都要合并起来，最好用类来做，消灭switch
	BOOL m_bTestingDrawBkgArea;
	int m_nUpdateViewSign;

// 操作
private:
	void FreeAll();
	
public:
	
	const Nuclear::CPOINT GetMapSize(){return m_mapSize;}
	void SetMapSize(const Nuclear::CPOINT &size);
	EDIT_STATE GetEditState() { return m_editState; }
	void SetEditState(EDIT_STATE state) { m_editState = state; }
	void SaveAsXML(XMLIO::CONode &root);
	


// 重写
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// 实现
public:
	virtual ~CSoundEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
};


