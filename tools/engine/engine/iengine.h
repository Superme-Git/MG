#ifndef __Nuclear_IENGINE_H
#define __Nuclear_IENGINE_H

#if _MSC_VER > 1000
#pragma once
#endif



#include "iquery.h"
#include "iworld.h"
#include "ienv.h"
#include "iapp.h"
#include "ientitativetitlemanager.h"
#include "renderer\renderer.h"

namespace Nuclear
{

struct EngineParameter
{
	wchar_t* szWindowTitle;
	wchar_t* szClassName;
	HWND hWnd;	// �ǿձ�ʾ�û��Լ������Ĵ���
	XDisplayMode dmode;
	bool bAsyncRead;
	bool bApplictionInBuild;	//exe�ļ���build������true����build\bin������false
	bool bHasMaximizbox;		//�Ƿ�����󻯰�ť
	bool bSizeBox;				//�Ƿ��ܵ�����С
	DWORD dwRenderFlags;
	int nAppInitStepCount;		//����IApp::OnInit�Ĵ���
	IApp *pApp; // ��ֹΪ�գ�����Ҫ��֤��ָ����Engine::Run()�����ڼ���Ч
	EngineParameter() 
		: szWindowTitle(NULL), szClassName(NULL), hWnd(NULL), dmode(1024, 768, 32), bAsyncRead(false), bApplictionInBuild(false), 
		bHasMaximizbox(true), bSizeBox(true), dwRenderFlags(XPCRF_VERTICALSYNC), nAppInitStepCount(1), pApp(NULL) { }
};

class StepSoundType;
class IFileIO;

/* 
	���汣��ʹ�õ�ϵͳ��Դ
	WM_TIMER��WM_SIZING��WM_PAINT��WM_SIZE��WM_DESTROY��WM_ACTIVATE��WM_KILLFOCUS��WM_SETFOCUS��(WM_SYSCOMMAND����wParam==SC_KEYMENU����Ϣ)��Ϣ
	�û��Զ�����Ϣ��ID�����汣��ʹ��WM_USER+1 ~ WM_USER+10

	�ڱ���engine��ʱ��������OVERRIDE_NEW����꣬
	��ô���������new������Nuclear::XAlloc()��Nuclear::XFree()
	������SELF_MALLOC����꣬��
	Nuclear::XAlloc()��Nuclear::XFree()���棬����NedMalloc�ڴ��

	�����ڿͻ��������Ҫ��malloc��free��Ӧ����Nuclear::XAlloc()��Nuclear::XFree()
*/

// ͼ������
class IEngine
{
	// ��������
public:
	IEngine() {}
	virtual ~IEngine() {  }

	// �ӿ�
public:

	virtual bool Run(const EngineParameter &ep) = 0;						// ����
	virtual bool Exit() = 0;												// �˳�

	// ������û��Լ������Ĵ��ڣ���ô��Ҫ�ڴ��ں����е��ô˺�����Ҫע����ǣ������Ҫ����������
	// ������������ȫ�ֲ��ţ����ڴ��ڷǼ���״̬Ҳ���ţ�����ôWM_SHOWWINDOW��WM_ACTIVATE��WM_SIZE��Ϣ����ת��������
	virtual bool OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam ) { return false; }
	// ������û��Լ������Ĵ��ڣ���ô��Ҫ�ڳ������(OnIdle)ʱ�е��ô˺���
	virtual void OnIdle() { }

	// �û����������߳�ִ�е�������֮֡ǰִ��һ���֣�����Ҫ��IEnv::SetTaskExecuteCount��ֵ
	virtual void PutTask(IRunnable *task) = 0; 
	virtual size_t GetTaskListCount() const = 0; //���Task�����������������̵߳��á�PutTask֮�󲻻�����������

	virtual size_t GetQueueOfIOTaskSize() const = 0; //���IO���еĳ��ȡ������첽IO���֮���ӻ����̴߳�����Ķ��У����������̵߳��á�

	// �����ṩ�� timer
	virtual bool ScheduleTimer(ITimer * timer, int period/* ms */) = 0;
	virtual bool CancelTimer(ITimer * timer) = 0;

	// ע�ᡢ���ʵ���TitleManager
	virtual void SetEntitativeTitleManager(IEntitativeTitleManager* pETM) = 0;
	virtual IEntitativeTitleManager *GetEntitativeTitleManager() = 0;

	// ע�⣺������Щָ���� Run ֮ǰ�� Exit֮�� ����Ч
	virtual IWorld* GetWorld() = 0;				// �õ�����
	virtual const IWorld* GetWorld() const = 0;	// �õ�����
	virtual IEnv* GetEnv() = 0;					// �õ�����
	virtual const IEnv* GetEnv() const = 0;		// �õ�����
	virtual IApp* GetApp() = 0;					//
	virtual IQuery* GetQuery() = 0;
	virtual Renderer* GetRenderer( HWND hwnd=NULL ) = 0; // �õ�Renderer, �˽ӿ�ΪGXWindows��¶�������ⲿӦ�ò�Ӧʹ��
	virtual IFileIO* GetFileIO() = 0;			//��ȡ�ļ��Ľӿ�

	//�������, ���У�volumeΪ0~255��255���0��С, freqΪ0~255��128������
	//�л�������ģʽ�����Ҫ��Customs�л������ģʽ�������Լ�STOP�������֣���AUTO�л������ģʽ����������ı������ֻ��Լ�ֹͣ
	//Ĭ����XPBGSM_AUTO
	virtual void SetBGSoundMode(XPBGSoundMode mode) = 0;
	virtual XPBGSoundMode GetBGSoundMode() const = 0;
	//ֻ������ģʽ����Custom��ʱ�����漸����������Ч
	virtual CHECK_SOUND_STATE CheckBGSound(const std::wstring &fn, bool async) = 0;
	__declspec(deprecated) SoundHandle PlayBGSound(LPCTSTR fn, bool looping = false, bool bGlobalFocus = false)
	{
		CheckBGSound(fn, false);
		return PlayBGSound(fn, 0, GetEnv()->GetBGMType(), 255, looping, bGlobalFocus);
	}
	//����Ҫ��CheckBGSound����OK�����߻ص��˼��سɹ�֮�󣬵���PlayBGSound, ����PushNextStreamSound���ܳɹ�
	virtual SoundHandle PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, unsigned char type, unsigned char volume, bool looping, bool bGlobalFocus) = 0;
	__declspec(deprecated) SoundHandle PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, bool looping, bool bGlobalFocus)
	{
		return PlayBGSound(fn, fadeInTime, GetEnv()->GetBGMType(), 255, looping, bGlobalFocus);
	}
	__declspec(deprecated) virtual SoundHandle PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, unsigned char type, bool looping, bool bGlobalFocus)
	{
		return PlayBGSound(fn, fadeInTime, type, 255, looping, bGlobalFocus);
	}
	virtual bool PushNextStreamSound(SoundHandle handle, const std::wstring &fn) = 0;
	virtual bool FadeOutToStop(SoundHandle handle, unsigned short time) = 0;

	//priority�����ȼ���0Ϊ��ߣ�Ŀǰ���ϵ����ȼ������������������10��������
	//type�����ͣ�0�̶�Ϊ������Ч�������ɿͻ��˶��壬���������غ������������ڣ�Ҳ����ȫ����0
	virtual bool PlayEffectSound(const std::wstring &fn, short priority, unsigned char type, unsigned char volume = 255, unsigned char freq = 128) = 0;

	//ֹͣ����ĳ�����͵�����������������������������͵��������Ქ�ţ����Ҫ��ֹ���ţ�Ӧ����EnableSound��
	virtual void StopTypeSound(unsigned char type) = 0;

	//ȫ������������Ϊ[0,1]
	virtual void SetSoundVolume(unsigned char type, float fVolume) = 0;
	virtual float GetSoundVolume(unsigned char type) const = 0;
	__declspec(deprecated) void SetBGSoundVolume(float fVolume) { SetSoundVolume(GetEnv()->GetBGMType(), fVolume); }
	__declspec(deprecated) float GetBGSoundVolume() const {	return GetSoundVolume(GetEnv()->GetBGMType()); }
	__declspec(deprecated) void SetEffectSoundVolume(unsigned char type, float fVolume) { SetSoundVolume(type, fVolume); }
	__declspec(deprecated) float GetEffectSoundVolume(unsigned char type) const {	return GetSoundVolume(type); }


	//��������
	//�����ǰ������ģʽ��Custom�����´򿪱������ֵ����͵�ʱ��Ҫ���µ���PlayBGSound()�������ű�������
	virtual void EnableSound(unsigned char type, bool b) = 0;
	virtual bool IsSoundEnable(unsigned char type) const = 0;

	__declspec(deprecated) void EnableBGSound(bool b) { EnableSound(GetEnv()->GetBGMType(), b); }
	__declspec(deprecated) bool IsBGSoundEnable() const { return IsSoundEnable(GetEnv()->GetBGMType()); }
	__declspec(deprecated) void EnableEffectSound(unsigned char type, bool b) { EnableSound(type, b); }
	__declspec(deprecated) bool IsEffectSoundEnable(unsigned char type) const { return IsSoundEnable(type); }
	
	// �����羫�����
	virtual EngineSpriteHandle CreateEngineSprite(const std::wstring &modelname) = 0;
	virtual void ReleaseEngineSprite(EngineSpriteHandle handle) = 0;
	virtual void RendererEngineSprite(EngineSpriteHandle handle) = 0;
	virtual void SetEngineSpriteLoc(EngineSpriteHandle handle, const Location& loc) = 0;
	virtual void SetEngineSpriteDirection(EngineSpriteHandle handle, XPDIRECTION dir) = 0;
	virtual void SetEngineSpriteDefaultAction(EngineSpriteHandle handle, const std::wstring &actname) = 0;
	virtual void SetEngineSpriteAction(EngineSpriteHandle handle, const std::wstring &actname) = 0;
	virtual void SetEngineSpriteComponent(EngineSpriteHandle handle, int scid, const std::wstring& resource, Nuclear::XPCOLOR color=0xffffffff/*XRGB*/) = 0;
	virtual void EnableEngineSpriteShadow(EngineSpriteHandle handle, bool b) = 0;
	virtual void SetEngineSpriteScale(EngineSpriteHandle handle, float scale) = 0;


	//һЩ���ݡ�����

	virtual int GetAllSpriteCount() const = 0;
	virtual void GetAllSpriteDetail(std::map<std::wstring, int> &detail) const = 0;

	virtual int GetAllEffectCount() const = 0;
	virtual void GetAllEffectDetail(std::map<std::wstring, int> &detail) const = 0;

	virtual int GetAllSoundCount() const = 0;
	virtual void GetAllSoundDetail(std::vector<std::wstring> &detail) const = 0;


	//������Ч
	virtual IEffect* CreateFontEffect(TextBlockHandle textHandle, const XPFontEffectParam& structFontParam) = 0;

	// ������Ч���
	virtual IEffect* CreateEffect(const std::wstring &effectname, bool async = false) = 0;
	virtual void DrawEffect(IEffect* pEffect) = 0;
	virtual void ReleaseEffect(IEffect* pEffect) = 0;

	//CacheEffect��ʲô��Ч�����ԣ���һ���ǽ����
	virtual void HoldEffect(const std::wstring &name) = 0;
	virtual void ReleaseEffect(const std::wstring &name) = 0;

	//��ͼ
	virtual PictureHandle CaptureWorld() = 0;

	virtual void SetCleanColor(XPCOLOR color) = 0;
	virtual XPCOLOR GetCleanColor() const = 0;

	//����Ӵ���ʧȥ���߻��Active״̬����ô���ϵ�������ӿڣ�
	virtual void OnActive(bool active) = 0;
	virtual XPWINDOW_STATE GetWindowState() const = 0;

	// ռ��
private:
	IEngine(const IEngine&);
	IEngine& operator=(const IEngine&);
};

IEngine* GetEngine();	// �õ�����ʵ�����Ÿ��㺯��

}


#endif