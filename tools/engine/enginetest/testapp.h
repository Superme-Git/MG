#ifndef __TESTAPP_H
#define __TESTAPP_H

#if _MSC_VER > 1000
#pragma once
#endif


// �ڴ�й©���
#ifdef _DEBUG
#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>
//#define new new(_NORMAL_BLOCK, __FILE__, __LINE__)
#endif

#include "..\engine\iapp.h"
#include "..\engine\iengine.h"
#include "..\engine\engine\toolstitlemanager.h"
#include "sprite.h"
#include <map>
class CMyFrame;

class MyTask : public Nuclear::IRunnable
{
public:
	virtual ~MyTask() { }
	virtual void Run()
	{
		
	}
};

class TestApp : public Nuclear::IApp, public Nuclear::ITimer
{
private:
	typedef std::map<Nuclear::ISprite*, Sprite*> SpriteMap;

public:
	Nuclear::EngineSpriteHandle esh;
	Nuclear::IEngine* pEngine;
	Nuclear::IWorld* pWorld;
	Nuclear::IImmovableObj *pMine;
	Nuclear::Location moveTarget;
	Nuclear::Location moveStart;
	Nuclear::IEffect* pEffect;
	Nuclear::IEffect* pEffMapTarget;
	//Nuclear::ToolsTitleManager *pToolsTitleMan;
	Nuclear::XPCursorHandle hArrowCursor;
	Nuclear::XPCursorHandle hAppStartingCursor;

	std::wstring m_strInitMapName;
	std::wstring m_strInitMazeName;

	SpriteMap m_Sprites;
	Sprite* m_pHero;
	std::list<Sprite*> m_monsters;

	CMyFrame* pMyFrame;
	int speed;
	// ��������
public:
	TestApp();
	virtual ~TestApp() 
	{
		if (pEngine) pEngine->CancelTimer(this);
		SpriteMap::iterator it = m_Sprites.begin(), ie = m_Sprites.end();
		for (;it!=ie;++it)
		{
			delete it->second;
		}
		//if (pToolsTitleMan)
		//	delete pToolsTitleMan;
	}
private:
	Sprite* NewSprite(const std::wstring &model, bool attach = false);
	// �ص��ӿ�
public:
	//virtual IApp* Clone() const;
	//virtual void Destroy();

	virtual bool OnInit(int step);
	virtual bool OnExit();
	virtual void OnTick(int now, int delta) ;
	virtual bool OnBeforeRender(int now) { return true; }
	virtual void OnRenderUI(int now) ;
	virtual void OnRenderInit(int now, int step, int totalstep);

	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type); // ������״̬�����ı�ʱ���ص�����������¶�����

	virtual void OnTimer();

	// windows ��Ϣ
	// ����ֵӦ����XPWinMSGFuncSignö�ٵİ�λ��
	virtual Nuclear::XPWinMSGFuncResult OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam );

	// ���ص�ͼ�Ļص�
	virtual bool LoadingMapNotify(int pro);

};


#endif