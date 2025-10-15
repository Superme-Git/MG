#ifndef __Nuclear_IWORLD_H
#define __Nuclear_IWORLD_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "common\xptypes.h"
#include "ieffect.h"
#include "isprite.h"
#include "iimmovableobj.h"
namespace XMLIO
{
	class CINode;
}
namespace Nuclear
{
	class PStepSoundMap;
	enum {
		XPEFFECT_WAR_LAYER_COUNT = XPEL_TOP,
		XPEFFECT_LAYER_COUNT,
	};

	//��ͼ��תЧ��
	enum XPLOADMAP_EFFECT
	{
		XPLOADMAP_NOEFFECT,
		//XPLOADMAP_JUMP
	};

	// Load��ͼ����
	struct XPLoadmapParam
	{
		Location objLoc;	//��תĿ��λ��
		virtual XPLOADMAP_EFFECT GetType() const
		{
			return XPLOADMAP_NOEFFECT;
		}
		virtual ~XPLoadmapParam() {}
	};

	//// Load��ͼ��ԾЧ������
	//struct XPLoadmapJumpParam : public XPLoadmapParam
	//{
	//	unsigned int transID;
	//	virtual XPLOADMAP_EFFECT GetType() const
	//	{
	//		return XPLOADMAP_JUMP;
	//	}
	//	virtual ~XPLoadmapJumpParam() {}
	//};

	// ��Ϸ����

	class IWorld
	{
		// ��������
	public:
		IWorld() {}
		virtual ~IWorld() {  }

		// �ӿ�
	public:
		__declspec(deprecated) bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param)
		{
			return LoadMap(mapname, mazename, param, false);
		}
		virtual bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, bool async) = 0;	
		virtual bool UnloadMap() = 0;				// ж�ص�ͼ

		virtual bool GetMapSize(CPOINT &size) const = 0;

		/* ���� */
		virtual void SetScale(float scale) = 0;
		virtual float GetScale () const = 0;

		/* �ϰ� */
		virtual bool SetMapMaze(const void* mazeBuffer, size_t size) = 0;
		virtual bool SetMazeMask(const unsigned char* pData, const CRECT &rect) = 0;//rect����ҿ��ϱ��¿�����������
		virtual unsigned int GetMazeMask(int x, int y) = 0;//�߼�����

		/* �����ϰ���ʲô��ɫ */
		virtual void SetMazeColors(const XPMazeColors &colors) = 0;
		virtual const XPMazeColors& GetMazeColors() const = 0;

		/* ������ͼ��ֻ��SoundEditorʹ�ã� */
		virtual bool SetSoundAreas(XMLIO::CINode& root, PStepSoundMap* pSSM) = 0;

		/* �ⲿ��Ҫ�ĵ�ͼA*���� */
		virtual bool GetAStartPath(astar::Path& path, const Nuclear::Location& start, const Nuclear::Location& end) = 0; // ������ʼ����߼�����, ���ذ�����ʼ�㡢�յ��·��

		/* ս��״̬ */
		__declspec(deprecated) void SetWarMode(bool m) { SetWorldMode(m?XPWM_SIMPLE_BATTLE:XPWM_NORMAL); }
		__declspec(deprecated) bool IsInWarMode() const { return ((GetWorldMode() == XPWM_SIMPLE_BATTLE) || (GetWorldMode() == XPWM_WORLD_BATTLE)); }

		/* ����ģʽ */
		virtual void SetWorldMode(XPWorldMode m) = 0;
		virtual XPWorldMode GetWorldMode() const = 0;

		//Ӧ����pfs·��������ͼƬ��ʽ��dds��png֮��ġ����û�л�ͬ����ȡ��Դ
		virtual WarBackgroundHandle LoadWarBackgound( const std::wstring &name ) = 0;
		virtual void FreeWarBackgroundHandle (WarBackgroundHandle handle) = 0;
		virtual bool SetWarBackground(WarBackgroundHandle handle) = 0;
		virtual void FreeAllWarBackground() = 0;

		/* ��ͨ״̬�ľ��� */
		virtual ISprite* NewSprite(const std::wstring &modelname) = 0;
		//virtual ISprite* CloneSprite(const ISprite* spr) = 0;
		virtual void DeleteSprite(ISprite* sprite) = 0;
		virtual void DeleteAllSprite(bool keepAttached = false) = 0; // keepAttached �Ƿ������ǡ�

		/* ս��״̬�ľ��� */
		virtual ISprite* NewWarSprite(const std::wstring &modelname) = 0;
		virtual void DeleteWarSprite(ISprite* sprite) = 0;
		virtual void DeleteAllWarSprite() = 0;

		//�����Ӱ�ӵĲ���
		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const = 0;
		virtual void SetSpriteShadowParam(const float &shearX, const float &scalingY) = 0;

		/* ���ڵ�ͼ�ϵ�������������Ʒ������.. */
		virtual IImmovableObj* NewImmovableObj(const std::wstring &objname, int layer/*Ŀǰ����Ϊ0��ֻ�еر���һ��*/, XPCOLOR color = 0xffffffff, float freq=1.0f) = 0;
		virtual void DeleteImmovableObj(IImmovableObj* immobj) = 0;
		virtual void DeleteAllImmovableObj() = 0;

		//���������OnBeforeRender���������Ч����Ϊ���ʱ�򻺴��Ѿ�����
		virtual bool SelectObjs(const Location &loc, std::vector<ISelectableObj*> &ettobjs) = 0;

		/* ֱ���ڵ�ͼ�ϲ��ŵ� effect */
		//������Ч��
		virtual IEffect* SetLinkedEffect(const std::wstring &name, XPEFFECT_LAYER layer, const CPOINT &pt1, const CPOINT &pt2, float time, bool async) = 0;// time < 0����ѭ����time > 0������������time��֮��ֹͣ���䣬��������������֮�������¿�ʼ�����ֻ����������Ч
		virtual IEffect* SetEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async) = 0;
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async) = 0;
		virtual void RemoveEffect(IEffect* pEffect) = 0;

		//һ����Ч��, times�ǲ��Ŵ���, һ����������һ�����˺����߷�Χ�˺���
		//layer �ɲ߻�
		//a) �ر�֮�ϣ�����֮�¡�                      // �磺����ȼ�գ�ը��
		//b) ������ڵ��㣬������ݵ����Զ��ж��ڵ���  // �磺ͻ��ı���
		//c) ����֮�ϡ�                                // �磺���磬С��Χ
		//d) ���ϲ㡣                                  // �磺����ѩ����Χ
		// �����Ч��������soundtype��������������
		virtual void PlayEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype) = 0;
		
		//ս����Ч������������layerֻ��3�㣬��֧��XPEL_TOP
		//������Ч��
		virtual IEffect* SetWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async) = 0;
		virtual IEffect* SetWarContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async) = 0;
		virtual void RemoveWarEffect(IEffect* pEffect) = 0;

		//һ����Ч��, times�ǲ��Ŵ���, һ����������һ�����˺����߷�Χ�˺���
		// �����Ч��������soundtype��������������
		virtual void PlayWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype) = 0;
		//ս����Ч����������������


		

		virtual void AttachCameraTo(ISprite* sprite) = 0;
		virtual CRECT GetViewport() = 0; //����camera��left, top, right, bottom
		virtual void SetViewport(const Location &loc/* Ŀ���Ӵ����ģ�Ҳ����Ҫ�������ǵ���λ��*/, bool syncRsrc/* �Ƿ��ȼ�����Դ*/) = 0;
		virtual void SetViewportLT(int left, int top) = 0; // Ϊ�༭����ӣ��������ʱ���ô˺����ƶ��ӿڣ�


		virtual void SetGameTime(float time) = 0;	//������Ϸ�����ҹʱ�䣬[0~1)�����ȱ仯��0������ҹ12�㣬0.5��������12��
		virtual float GetTempGameTime() const = 0;		//��ȡ��Ϸ�����ҹʱ�䣬[0~1)�����ȱ仯��0������ҹ12�㣬0.5��������12��
		virtual void SetGameTimeCycle(int time) = 0;	//������Ϸ�߼�ʱ��һ�죨24Сʱ���ĳ��ȣ���λ����

		//virtual void FocusTo(Location ptCurrent,Location ptTarget) = 0;
		virtual bool SetCameraUpdateType(XPCAMERA_UPDATE_TYPE type) = 0;
		virtual XPCAMERA_UPDATE_TYPE GetCameraUpdateType() const = 0;

		//mapname�����L""����ô���Ǳ���ͼ��center��Ԥ���ص����ĵ�
		//������PrefetchMapRes����֮��ֻҪû��������ͼ��
		//����������Դ�ͻ����첽�ķ�ʽ���ؽ���������һֱ���ͷţ�
		//����PrefetchMapRes������һ�����򣬻���CancelPrefetch���������˵�ͼ
		virtual bool PrefetchMapRes(const std::wstring &mapname, const std::wstring &mazename, const CPOINT &center, bool autoSwitch) = 0;
		__declspec(deprecated) void PrefetchMapRes(const CPOINT &center)
		{
			PrefetchMapRes(L"", L"", center, false);
		}
		virtual void CancelPrefetch() = 0;

		// ռ��
	private:
		IWorld(const IWorld&);
		IWorld& operator=(const IWorld&);

	};


}


#endif