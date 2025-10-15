#pragma once
#include "..\engine\iapp.h"
#include "..\engine\iengine.h"
#include "sprite.h"
#include <map>
#include "SoundEditorDoc.h"

namespace Nuclear
{
	class XPEnvSoundCircleArea;
	class XPEnvSoundPolygonArea;
	class XPEnvSoundArea;
	class Renderer;
};

class SoundApp :
	public Nuclear::IApp
{
//�Զ��峣��������
private:
	typedef std::map<Nuclear::ISprite*, Sprite*> SpriteMap;

//��Ա������
	SpriteMap m_spriteMap;
	Nuclear::IEngine *m_pEngine;
	EDIT_STATE m_editState;
	Sprite* m_pHero;
	CSoundEditorDoc* m_pDoc;
	Nuclear::PictureHandle m_AreaHandle;
	bool m_bDrawGird;
	bool m_bInited;
public:
	RECT m_mouseRect;				//��������
//������
private:
	void DrawArea(const CToolsSoundArea *pArea, const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected);
	void DrawGrid(const POINT& vplt, const POINT& gridsize);
public:
	SoundApp(void);
	virtual ~SoundApp(void);

	void RemoveAllSprite();
	void SetState(EDIT_STATE state);
	Sprite* GetHero() { return m_pHero; }
	void Reset();
	void SetDoc(CSoundEditorDoc* pDoc) { m_pDoc = pDoc; }
	void UpdateViewport(CPoint pt);
	void EnableNightEffect(bool b);
	void SetNightCycle(int cycle);
	void SetGameTime(float t);
	void SetEnvVolume(unsigned char v);
	void EnableEnv(bool b);
	void SetBkgVolume(unsigned char v);
	void EnableBkg(bool b);

	bool IsInited() const { return m_bInited; }

//ʵ��
	virtual bool OnInit(int step);
	virtual bool OnExit();

	virtual void OnTick(int now, int delta);	// ���߳���ѭ����ÿ֡���ã��ɴ���ͻ���������Ⱦ�޹ص��ճ��������紦��Э���
	virtual bool OnBeforeRender(int now/*ms*/); // ����ÿ֡��Ⱦ֮ǰ���ã����ڿͻ��˸�����Ⱦ������ݣ�����false����Ⱦ�����ô˺�����wait������֡��
	virtual void OnRenderUI(int now);
	virtual void OnRenderInit(int now, int step, int totalstep) {}
	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type); // ������״̬�����ı�ʱ���ص�����������¶�����
	afx_msg void OnDrawGird();
	afx_msg void OnUpdateDrawGird(CCmdUI *pCmdUI);

};
