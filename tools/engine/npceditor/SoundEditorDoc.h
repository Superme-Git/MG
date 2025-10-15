// SoundEditorDoc.h : CSoundEditorDoc ��Ľӿ�
//
#pragma once
#include "engine\common\util.h"
#include "engine\common\pfsutil.h"
#include "xmlio\xmlio.h"
#include <set>
enum EDIT_STATE 
{
	EDIT_STATE_MAZE = 0,		//�༭�赲��
	EDIT_STATE_NPC	= 1,		//�༭NPC
	EDIT_STATE_MapJump=2,       //��ͼ��ת��༭
	EDIT_STATE_TESTING
};
enum SELECT_STATE
{
	SS_SELECTED = 0,		//�Ѿ���ѡ���˵�
	SS_SELECTED_AREA_POINT,	//�Ѿ�ѡ���˵������һ����
	SS_NEWSELECTED,			//ѡ���˸��µ�
	SS_ENPTY,				//ѡ�˸��յط�
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
protected: // �������л�����
	CSoundEditorDoc();
	DECLARE_DYNCREATE(CSoundEditorDoc)
//�Զ���������
public:
	

// ����
private:
	EDIT_STATE m_editState;
	Nuclear::CPOINT m_mapSize;
public:
	Nuclear::XPIShape *m_pSelectedShape;
	int m_nSelectedPt;
	POLYGON_MOUSE_STATE m_PolygonMouseState;
	RECT m_CutingPolygonLine;
	bool m_bIsIngoreDragging;//�Ժ����State��Ҫ�ϲ������������������������switch
	BOOL m_bTestingDrawBkgArea;
	int m_nUpdateViewSign;

// ����
private:
	void FreeAll();
	
public:
	
	const Nuclear::CPOINT GetMapSize(){return m_mapSize;}
	void SetMapSize(const Nuclear::CPOINT &size);
	EDIT_STATE GetEditState() { return m_editState; }
	void SetEditState(EDIT_STATE state) { m_editState = state; }
	void SaveAsXML(XMLIO::CONode &root);
	


// ��д
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// ʵ��
public:
	virtual ~CSoundEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
};


