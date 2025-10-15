#ifndef __Nuclear_ISPRITE_H
#define __Nuclear_ISPRITE_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <list>
#include <string>
#include "common\xptypes.h"
#include "iselectableobj.h"
#include "ieffect.h"

namespace astar
{
	struct Point;
	typedef std::list<Point> Path;	//Ϊ�˲�include "astar\apath.hpp"��ֻ���ֶ�����һ���ˡ�����������
}

namespace Nuclear
{

	// �������õ�װ�����
	// sprite Ŀǰ��ģ��(model)����
	// ģ���б��� /model/sprites.xml ��
	// ÿ��ģ���ڷֱ����Լ���ÿ�����ã�����ģ�� male �Ĳ������� /model/male/layerdef.xml ��
	// �����/model/[modelname]/layerdef.xml �������ã�Ȼ�������ÿ���������ͨ��IQuery��ؽӿڻ��
	// ���֧�� XPEC_MAXCOUNT ��
	// ֮ǰ�ĵ��㾫����ʱ���ٶ��Ⱪ¶��������ʹ��

	// by wuweixin
	// ����ʹ����������ϵ, ���������Ӧʵ�ʵ�ͼ�ϵ���������
	// �߼������Ӧ�ɴ�ͼ���������߼�����, �ӿ���A*����Լ��ӿ�����ʽ��logic��, �����ʹ���߼�����
	// �����Ϊ��������
	// ��������ϵ����һ�������ϵ
	// ��������.x ==  �߼�����.x * N
	// ��������.y ==  �߼�����.y
	// N = [1, 2]
	// Ŀǰת��ֻ��sprite�ڲ�ʵ��ʹ��, �Ժ�����Ҫ�ٿ��Ǳ�¶����ת���ӿ�
	// PS: (�Ѿ���ȡ��¶��)

	enum SPRITE_COMPONENT
	{
		XPSC_ALL = -1, // ����ֵ��������ƴ��֮��
		XPSC_MAXCOUNT = 12,
	};

	// ���ڻص��ӿ� IApp::OnUpdateSpriteAction(ISprite * sprite, XPUSAT_TYPE type)
	enum XPUSA_TYPE
	{
		XPUSAT_MOVESTATE,			// �ƶ�״̬�������»ص�
		XPUSAT_MOVEINFLEXION,		// �ƶ�����·���յ�ʱ�Ļص� by wuweixin
		XPUSAT_PLAYACTIONFINISH,	// PlayAction  �����ص�
		XPUSAT_DEFAULTACTIONFINISH,	// DefaultAction �����ص�
		XPUSAT_TELEPORTEND			// ˲���ƶ������ص�
	};

	enum XPSPREFF_FLAG
	{
		XPSPREFFFLAG_BIND_EFF_POS = 1,		//�������Ч�ҵ�󶨵㣬����relpos���Ͼ����GetEffectPos����XPSPREFFFLAG_BIND_WEAPON��ͻ
		XPSPREFFFLAG_BIND_AUTO = 2,			//������Ч��GetEffectBindType���Զ������Ƿ����XPSPREFFFLAG_BIND_EFF_POS flag�������XPEBT_EFFECT_POS����ô���ϣ���XPSPREFFFLAG_BIND_WEAPON��XPSPREFFFLAG_BIND_EFF_POS����ͻ
		XPSPREFFFLAG_BIND_WEAPON = 4,		//Ĭ���������󶨵�0
		XPSPREFFFLAG_BIND_WEAPON_0 = 4,		//�����󶨵�1�����涯���仯���ı䣨ÿһ֡������ʱ����relpos����XPSPREFFFLAG_BIND_EFF_POS��XPSPREFFFLAG_SELF_REF��XPSPREFFFLAG_BIND_WEAPON_1��ͻ
		XPSPREFFFLAG_BIND_WEAPON_1 = 8,		//�����󶨵�2�����涯���仯���ı䣨ÿһ֡������ʱ����relpos����XPSPREFFFLAG_BIND_EFF_POS��XPSPREFFFLAG_SELF_REF��XPSPREFFFLAG_BIND_WEAPON_0��ͻ
		XPSPREFFFLAG_LINKING_FRAME = 16,	//ֻ��ani��Ч���ã�����Ч��֡�ͽ�ɫ��֡ǿ��ͬ��
		XPSPREFFFLAG_SELF_REF = 32,			//relpos����ݽ�ɫ��ǰ�������¼���λ�ã���XPSPREFFFLAG_BIND_EFF_POS��ͻ����ĳЩ��Ч��XPEBT_EFFECT_POS������£���XPSPREFFFLAG_BIND_AUTO��ͻ
		XPSPREFFFLAG_ASYNC = 64,			//�첽��ȡ��Դ
		XPSPREFFFLAG_NO_FOLLOW_SCALE = 128,	//�������ŵ�ʱ����Ч�����������
		XPSPREFFFLAG_NO_SOUND = 256,		//��������Ч���������
		XPSPREFFFLAG_DRAW_ON_TOP = 512,		//���������ǵ���Ч�����ϲ㲥��
	};

	enum XPSPRITE_ACTION_LOAD_TYPE
	{
		XPSALT_SYNC = 0,	//ͬ������
		XPSALT_ASYNC,		//�첽����
		XPSALT_BASE_ASYNC,	//�첽���أ����������һ�������һ֡��ʱ��ͻ�����
	};

	class ISprite;
	//�����Notify���ڷ����������ISprite*������ʹ�����Notify�����
	//���Notify�ڶ�ʧ�豸����С����ʱ�򶼻�������
	class SpriteNotify
	{
	public:
		virtual void OnPlayFrame(ISprite* pSprite, const std::wstring &actionName, int frame) = 0;
	};

	class ISprite : virtual public ISelectableObj
	{

	public:

		ISprite(){}
		virtual ~ISprite(){}
		// ע��/ȡ��Notify����Ҫע����ǣ����������Notify�Ļص��ڲ�ȡ��Notify��
		// ����ܿ�������������һ֡������֡�պ��и�Notify����ô���NotifyҲ�ᱻ����һ��
		virtual void RegisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame) = 0;
		virtual void DeregisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame) = 0;
		virtual void EnableStepSound(bool b) = 0;
		virtual bool IsStepSoundEnable() const = 0;
		//�༭��ʹ��
		virtual void ClearAllNotify() = 0;

		virtual const std::wstring &GetSoundActorName() const = 0;

		// ����
		virtual void SetScale(float scale) = 0;
		virtual float GetScale () const = 0;

		// ����λ�á�location �����������ꡣ
		virtual void SetLocation(const Location& location) = 0;
		// �õ�λ��.
		virtual Location GetLocation() const = 0;

		// ���ú͵õ�����direction �������귽��
		virtual void SetDirection( XPDIRECTION direction ) = 0;
		virtual XPDIRECTION GetDirection() const = 0;

		//�Ƿ���RenderTarget�����ƣ�����Ҫ��͸���ڵ�����Ҫ��Ӱ�ӡ���Ҫ����ʱ�򣬴�����ľ������
		//�õ����ܣ�����������鲻�ܳ���1024X1024�����Ҿ������ĵ����ڻ������ĵ�(512,512)��λ��
		//����ص�������Ҫ����㾫��Ӱ�ӵ�ʱ��Ӱ�ӵ��ص�������ɫ������������ǵ���Ĳ���û����ɫ����
		//�ص�RenderTarget����һ�����������
		virtual void UseRenderTarget(bool b) = 0;
		virtual bool IsUseRenderTarget() const = 0;

		// �����Ƿ�ɼ�
		virtual void SetVisible(bool v) = 0; // default true
		virtual bool IsVisiable() const = 0;

		// �����Ƿ�ɼ�ʵ�壨���SetVisible(false)�˵Ļ������������ô�趼���ɼ���
		virtual void SetEntityVisible(bool v) = 0; // default true
		virtual bool IsEntityVisible() const = 0;

		// �����Ƿ�ɼ����֣����SetVisible(false)�˵Ļ������������ô�趼���ɼ���
		virtual void SetTitleVisible(bool v) = 0; // default true
		virtual bool IsTitleVisible() const = 0;

		// ����Alpha
		virtual void SetAlpha(unsigned char a) = 0; //Ĭ����255
		virtual unsigned char GetAlpha() = 0; //Ĭ����255

		// ������鱻�ڵ���ʱ���Ƿ���Ҫ��͸��������ǣ���ô������ȫ��ס��������ľ��飬���Գ��������ľ���Ӧ����Ϊfalse��
		virtual bool IsCoveredAlpha() const = 0;
		virtual void SetCoveredAlpha(bool b) = 0;

		// �°�ͷ�����֣���UI����
		virtual void SetTitle(EntitativeTitleHandle handle, XPTITLE_ALIGN align = XPTA_DEFAULT) = 0;
		virtual EntitativeTitleHandle GetTitle() = 0;
		virtual EntitativeTitleHandle UnsetTitle() = 0;
		virtual void SetTitleSize(int width, int height) = 0;
		virtual int GetTitleWidth() const = 0;
		virtual int GetTitleHeight() const = 0;

		// ���ð������������˺����֣��ĳ�ʼYƫ�ƣ�����Ϊ��
		virtual int GetBubbleItemOffset() const = 0;

		// ��ñ�����Ч�ҵ��λ�ã�����������꣩
		virtual const CPOINT& GetEffectPos() const = 0;

		// ���þ���ģ��
		virtual void SetModel(const std::wstring &modelname) = 0;

		// ���ؾ���ģ�͵�����
		virtual std::wstring GetModelName() = 0;

		// ���þ����װ�����
		virtual bool SetComponent(int scid, const std::wstring& resource, XPCOLOR color=0xffffffff/*XRGB*/) = 0;
		virtual bool GetComponent(int scid, std::wstring& resource) = 0;
		virtual bool GetComponentColor(int scid, XPCOLOR &color) = 0;
		// ������������֣��������ʱ����ã������ʱ������L""��
		virtual void SetRideName(const std::wstring& name) = 0;
		// ������Ч�㣨����еĻ�����Alpha������[0~1]
		virtual void SetMinMaxEffectAlpha(float minalpha, float maxalpha) = 0;
		virtual float GetMinEffectAlpha() const = 0;
		virtual float GetMaxEffectAlpha() const = 0;

		// Ԥȡ�þ���Ķ�����Դ���������Դ��GCʱ�䷶Χ��120�룩��û�б�ʹ�û��߱����£����ľ��飩Ԥȡ����ô���Զ��ͷ�
		virtual XP_PREFETCH_RESULT PrefetchAction(const std::wstring &action_name) = 0;

		// ��ס�þ���Ķ�����Դ�����������ReleaseAction����ô����Զ���ͷţ�ֱ�����黻ģ�ͻ��߱�����
		virtual void HoldAction(const std::wstring &action_name) = 0;
		// ֻ��Holdס��Action����ҪRelease
		virtual void ReleaseAction(const std::wstring &action_name) = 0;

		// Ĭ�϶�����ÿ�����������һ��Ĭ�϶���
		__declspec(deprecated) bool SetDefaultAction( const std::wstring& action_name, bool bHoldLastFrame, float freq=1.0f) //Ĭ���첽
		{
			return SetDefaultAction(action_name, XPSALT_ASYNC, freq, bHoldLastFrame);
		}
		__declspec(deprecated) bool SetDefaultAction( const std::wstring& action_name, bool bHoldLastFrame, bool bAsync, float freq)
		{
			return SetDefaultAction(action_name, bAsync?XPSALT_ASYNC:XPSALT_SYNC, freq, bHoldLastFrame);
		}

		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq, bool bHoldLastFrame) = 0;

		virtual const std::wstring &GetDefaultAction() const = 0;
		// ��ʱ������ÿ��������0~1���� ��ʱ�����������֮���Զ��л�Ĭ�϶���, action_name �����ַ���ȡ����ʱ����
		// ����true������Ĳ����ˣ�����false��������ʵ����һ��������Ŀǰ�Ļ�û�����غ���
		__declspec(deprecated) bool PlayAction(const std::wstring& action_name, float freq, bool bAsync)
		{
			return PlayAction(action_name, bAsync?XPSALT_ASYNC:XPSALT_SYNC, freq);
		}
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq) = 0;

		virtual const std::wstring &GetCurActionName() const = 0;

		// ��ǰ�����Ĳ�����֡����������UpdateAction��ʱ����£���UpdateActionǰΪ��һ֡�����ݣ�UpdateAction��Ϊ��֡�����ݣ�
		virtual int GetCurrentFrame() const = 0;
		// ��ǰ��������֡��
		virtual int GetTotalFrame() const = 0;
		
		// ���ھ����ϲ��ŵ�Ч��

		//���ð��ļ��������ó�L""��ʱ��Ϊȡ��
		//idֻ����0����1����Ӧ��ͬ����
		virtual bool SetBindFile(int id, const std::wstring &bindFile) = 0;
		virtual std::wstring GetBindFile(int id) = 0;

		__declspec(deprecated) bool SetBindFile(const std::wstring &bindFile)
		{
			return SetBindFile(0, bindFile);
		}
		__declspec(deprecated) std::wstring GetBindFile()
		{
			return GetBindFile(0);
		}
		// ���ú�ȡ������Ч������Ҫ�ֹ�ȡ��������������
		// bindType��������󶨵�
		
		virtual IEffect* SetDurativeEffect(const std::wstring &effect_name, const CPOINT& relpos, unsigned int flag) = 0;
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const CPOINT& relpos, unsigned int flag) = 0;
		virtual void RemoveDurativeEffect(IEffect* pEffect) = 0;

		// ��ʱЧ������������Զ�ȡ��������������, times�ǲ��Ŵ���, �����������soundtype��������������
		virtual void PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype) = 0;
		__declspec(deprecated) void PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag)
		{
			PlayEffect(effect_name, dx, dy, times, flag, 2);//Ĭ��2�ɡ�����
		}
		__declspec(deprecated) void PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, bool selfRef, bool async)
		{
			unsigned int flag = 0;
			if (selfRef)
				flag |= XPSPREFFFLAG_SELF_REF;
			if (async)
				flag |= XPSPREFFFLAG_ASYNC;
			PlayEffect(effect_name, dx, dy, times, flag, 2);
		}
		//
		virtual void EnableShadow(bool b) = 0;
		virtual bool IsEnableShadow() = 0;
		
		// ����˲���ϲ�Ӱ
		//telTime���ƶ���ʱ�䣬blurTime�ǲ�Ӱ������ʱ�䣬�ܵ�ʱ����telTime+blurTime����telTime֮����Ѿ��ƶ���λ
		virtual void TeleportWithBlur(const Location &point, int telTime, int blurTime) = 0;

		// ����֧��4������8�������У�4���������� ���� ���� ����
		virtual void SetOnly4Dir(bool b) = 0;	// Ĭ����false��Ҳ����8����
		virtual bool IsOnly4Dir() const = 0;

		// �Ƿ����
		virtual void SetHighlight(bool b) = 0;
		virtual bool IsHighlight() const = 0;

		// �Ƿ�TestAlpha
		__declspec(deprecated) void SetTestAlpha(bool b) { SetMouseTestMode(b?XPTM_ALPHA:XPTM_POLYGON); }
		__declspec(deprecated) bool IsTestAlpha() const	{ return GetMouseTestMode() == XPTM_ALPHA; }

		// ����ܷ�ѡ��ani��ʱ���ģʽ
		virtual void SetMouseTestMode(XPMOUSE_TEST_MODE m) = 0;
		virtual XPMOUSE_TEST_MODE GetMouseTestMode() const = 0;

		//��������
		virtual void SetActionSoundPriority(short priority) = 0;
		virtual short GetActionSoundPriority() const = 0;

		// �����ƶ��ٶȣ���λ������/���롣��ʼΪ 0.160
		virtual void  SetMoveSpeed(float speed) = 0;
		virtual float GetMoveSpeed() const = 0;

		// ֹͣ�ƶ������δ�����·����
		virtual void StopMove() = 0;
		// �Ƿ��ƶ��С�����·��Ҫ�ߣ�û�е���Ŀ�ĵء�
		virtual bool IsMoving() const = 0;
		// ������ͣ״̬���������ȫ�����Ŀ���״̬�����Ƿ�������ƶ���( IsMoving )�޹ء�
		//       true  ���齫�����ƶ����������������е��ƶ�״̬���磬��ǰ·����β����
		//             �µ��ƶ����β����ȫ��������״̬�£�ֻ�ᱻ��¼��
		//       false ����ָ����ƶ�״̬��
		virtual void SetMoveSuspended(bool moveSuspended) = 0;
		// �Ƿ���ͣ״̬��
		virtual bool IsMoveSuspended() const = 0;

		// Ѱ�ҵ�target��·���������ƶ���ȥ�����Ѱ·ʧ�ܣ����ֵ�ǰ·�����䡣
		//
		// targetX,targetY
		//       Ŀ�����ꡣ�����������ꡣ
		// range
		//       [!ע��] ��ȻtargetX targetY������������, ��range�����߼�����Ϊ��׼��, �ȽϹ���
		//       ��Ŀ���ֱ�߿ɴ����С��rangeʱ��ֹͣ�ƶ���
		//       ����Ҫ��ֹͣ��ֱ�߿ɴ�Ŀ�꣬���Թ��У�ʵ��ֹͣ������Ŀ���range�١�
		//       range ��Ҫ���ڴ���ս������ʱĿ��һ��Ҳ�����ƶ�״̬�����������ӳ٣�
		//       Ŀ���λ�ñ�������׼ȷ�ġ��ӳ�������ɵ�range��׼ȷ���Թ���ɵ�����·�����Ƶ����
		//       �Ⱥ����Թ��µ��������������ʵ��Ч���ٵ�����
		// size
		//       ָ�����ҷ�Χ�����鵱ǰ�����(GetLocation)Ϊ���ģ�size��ߵ�rect��
		//       ��Χ֮�ⶼ�������ɴ����NULL��ʾ����������ͼ������Ϊ�������ꡣ
		// @return
		//       0 Ѱ·�ɹ�����������ƶ�״̬��
		//       1 Ѱ·ʧ�ܡ�����״̬���䡣
		//       2 Ѱ·�ɹ����������վ��״̬����ʱ��Ŀ���Ѿ��ڷ�Χ�ڡ�
		virtual void MoveTo(int targetX, int targetY, int range, const CSIZE * size) = 0;
		void MoveTo(const Location & target, int range, const CSIZE * size) { this->MoveTo(target.x, target.y, range, size); }
		// rect
		//       ָ�����ҷ�Χ�����Ρ���Χ֮�ⶼ�������ɴ����NULL��ʾ����������ͼ������Ϊ�����������ꡣ
		//       �����ǰ���鲻�ڲ��ҷ�Χ�ڣ���Ѱ·ʧ�ܡ�
		/* ��Ҫʱ��ʵ�֡�
		virtual void MoveTo(int targetX, int targetY, int range, CRECT * rect) = 0;
		void MoveTo(const Location & target, int range, CRECT * rect) { this->MoveTo(target.x, target.y, range, rect); }
		// */

		// ��trail�ӵ���ǰ·�����棬�þ�����β��·���еĵ��ƶ������trail�͵�ǰ·���޷��޷����ӡ�
		// ��ǰ·���ᱻ��������Ѿ������¶�λ��trail.front()��
		//	  trail β��·�����߼����ꡣ[ע��] trail�ᱻ�ڲ��޸ģ�����֮���������塣
		virtual void MoveTo(astar::Path & trail) = 0;

		// ��ȡ��ǰpath��һ������, ��ʹ��MoveTo����ÿɵõ�����Ҫ�ߵ�·��(�߼�����)
		virtual void GetPath(astar::Path & path) = 0;

		// �Ƿ��¼β����һ��ֻ��������Ҫ��Ĭ��Ϊfalse��
		virtual void KeepMoveTrail(bool keep) = 0;
		virtual bool IsKeepMoveTrail() const = 0;

		// �ѵ�ǰ·��β��ȡ�߲���ӵ�trail��ͨ������ֵ�ж��Ƿ������β����
		// һ�㶼�ô����trail��ȡβ�����������ռ����ڲ�������Ƿ�����޷�ƴ�ӡ�
		virtual bool TakeMoveTrail(astar::Path & trail /*out*/) = 0;

		// ���þ���ɴ�ͼ���롣Ϊ0�������ϰ���
		virtual void SetMoveMask(unsigned int mask) = 0;

		// �������þ���λ�÷���һ�����ڷ����������ƶ����������ͻ���λ�á����������·����֤�����ʧ�ܡ�
		//  [ע��] SetLocationDirection(const astar::Point &)ʹ���߼�����
		//         ��������������ʹ��SetLocation()��SetDirection()
		virtual void SetLocationDirection(const astar::Point &) = 0;

		// ȡ�þ�����߼�����
		virtual Location GetLogicLocation() const = 0;
		// �⼸��Ŀǰ�ò���
		//virtual void SetLogicLocation(float x, float y) = 0;    
		//virtual void SetLogicLocation(const Location & location) = 0;
		//virtual void SetLogicDirection(int tx, int ty) = 0;     


		// ת��Ŀ���
		virtual void SetDirection(int targetx, int targety) = 0;
		void SetDirection(const Nuclear::CPOINT & target) { SetDirection(target.x, target.y); }

		// [!ע��] ��Ȼtarget����������, ��range�����߼�����Ϊ��׼��, �ȽϹ���
		virtual bool InAttackRange( const Nuclear::Location & target, int range, unsigned int mask) = 0;

	};
}

#endif