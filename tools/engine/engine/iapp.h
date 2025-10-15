#ifndef __Nuclear_IAPP_H
#define __Nuclear_IAPP_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <windows.h>
#include "isprite.h"

namespace Nuclear
{

	typedef unsigned int XPWinMSGFuncResult;

	enum XPWinMSGFuncSign
	{
		XPWINMSG_OK = 0,
		XPWINMSG_HANDLED = 1,	//������Ϣ�Ѿ����������ˣ�����Ŀǰ�������һλ��״̬��Ԥ����
		XPWINMSG_INTERCEPT = 2,	//��Ҫ���������Ϣ�����´���
	};
	// �ص��ӿڼ���

	class IApp
	{
		// ��������
	public:
		IApp() {}
		virtual ~IApp() {  }

		// �ӿ�
	public:

		virtual bool OnInit(int step) = 0;
		virtual bool OnExit() = 0;

		//ע�⣬�����ĸ�������now�ڿ�����24�쵽��49����Ǹ��ģ�deltaһ���ܱ�֤����0��������ȷ��
		virtual void OnTick(int now, int delta) = 0;	// ���߳���ѭ����ÿ֡���ã��ɴ���ͻ���������Ⱦ�޹ص��ճ��������紦��Э���
		virtual bool OnBeforeRender(int now/*ms*/) = 0; // ����ÿ֡��Ⱦ֮ǰ���ã����ڿͻ��˸�����Ⱦ������ݣ�����false����Ⱦ�����ô˺�����wait������֡��
		virtual void OnRenderUI(int now) = 0; // ��������Ⱦ֮����ã�����GXWindows��Ⱦ��֮��������ܻ��ж�����Ⱦ

		//�����ʼ���Ǹ����ڡ���step�ǵڼ�����totalstep��һ���м�����ע��ǰ���������-1����Ϊ����֪������һ���ж��ٸ����������totalsetp����ep��nAppInitStepCount��
		//step��1��totalstep, ��������ÿһ�����п��ܵ�������Ϊ�һ����֡�ʣ����������õ�̫Ƶ�������ǣ�step==1��step==totalstep�϶��ᱻ����
		virtual void OnRenderInit(int now, int step, int totalstep) = 0;

		//�е��������ӿ�ȡ������ع�������Sprite��Notify�����ˡ�������������
		virtual void OnUpdateSpriteAction(ISprite * sprite, XPUSA_TYPE type) = 0; // ������״̬�����ı�ʱ���ص�����������¶�����

		/*
		��������������������ѭ���й�ϵͼʾ�⣺
		while( true ) //mainloop
		{
			...
			int now = ::timeGetTime()
			delta = ...
			app->OnTick(now, delta)
			...
			if( ��Ҫ��Ⱦ �����н���� )
			{
				if( app->OnBeforeRender(now) )
				{
					...
					BeginRender()
					EngineRender()
					app->OnRenderUI(now)
					EndRender()
				}
			}
			...
		}
		*/

		// windows ��Ϣ��������WM_TIMER��WM_SIZING��WM_PAINT��WM_SIZE��WM_DESTROY��WM_ACTIVATE��WM_KILLFOCUS��WM_SETFOCUS��(WM_SYSCOMMAND����wParam==SC_KEYMENU����Ϣ)��Ϣ
		// ����ֵӦ����XPWinMSGFuncSignö�ٵİ�λ��
		virtual XPWinMSGFuncResult OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam ) { return false; }

		// ���ص�ͼ�Ļص����첽���ػ���ú�Ƶ�����������첽���ص�ʱ�����ֻ�ʺ����״̬
		virtual bool LoadingMapNotify(int pro) { return true; }
		virtual void LoadingMapFaildNotify() {}

		//�������Customģʽ�£��������ֲ������ˣ��п������ֶ���ֹ�����IEngine::FadeOutToStop���˳����򣩻���ô˻ص�
		virtual void OnBGMPlayEnd(SoundHandle handle) {}
		//�������Customģʽ�£��������ֲ��ſ첥���ˣ����ʱ�����ȥ�첽������һ�������ˣ�
		virtual void OnBGMGoingToEnd(SoundHandle handle) {}
		//�첽����һ������֮��Ļص���succΪ�������سɹ�
		virtual void OnBGMLoaded(const std::wstring &fn, bool succ) {}

		// ռ��
	private:
		IApp(const IApp&);
		IApp& operator=(const IApp&);

	};


}


#endif