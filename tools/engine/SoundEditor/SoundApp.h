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
//自定义常量、类型
private:
	typedef std::map<Nuclear::ISprite*, Sprite*> SpriteMap;

//成员属性们
	SpriteMap m_spriteMap;
	Nuclear::IEngine *m_pEngine;
	EDIT_STATE m_editState;
	Sprite* m_pHero;
	CSoundEditorDoc* m_pDoc;
	Nuclear::PictureHandle m_AreaHandle;
	bool m_bDrawGird;
	bool m_bInited;
public:
	RECT m_mouseRect;				//世界坐标
//方法们
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

//实现
	virtual bool OnInit(int step);
	virtual bool OnExit();

	virtual void OnTick(int now, int delta);	// 主线程主循环中每帧调用，可处理客户端中与渲染无关的日常工作，如处理协议等
	virtual bool OnBeforeRender(int now/*ms*/); // 引擎每帧渲染之前调用，用于客户端更新渲染相关数据，返回false则不渲染，可用此函数内wait来控制帧率
	virtual void OnRenderUI(int now);
	virtual void OnRenderInit(int now, int step, int totalstep) {}
	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type); // 当精灵状态发生改变时，回调这个函数更新动作。
	afx_msg void OnDrawGird();
	afx_msg void OnUpdateDrawGird(CCmdUI *pCmdUI);

};
