#ifndef __Nuclear_ENGINEBASE_H__
#define __Nuclear_ENGINEBASE_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxptypes.h"
#include "../astar/nuastar.hpp"
#include "../common/nufileiomanager.h"

namespace LJXML
{
	typedef wchar_t Char;
	template<class Ch> class LJXML_Node;
}

namespace Nuclear
{
	class IEntitativeTitleManager;
	class SystemResourceManager;
	class NuclearFileIOManager;
	class SpriteManager;
	class AniManager;
	class SpineManager;
	class EffectManager;
	class Renderer;
	class ConfigManager;
	class StepSoundType;
	class IApp;
	class World;
	class XP3DEngine;

	/*
		������������������Ҫ�Ĺ������ܣ����������滷��
		�����еĺܶ������������ָ���������ָ�����õ���Ҫ�Ĺ���
		��ʱû�뵽�õ�����
		��������ع�����Engine��ص���Ϣ��������ص���Ϣ�ֿ������ܿ����ж������
	*/
	class EngineBase
	{
	public:
		virtual IEntitativeTitleManager *GetEntitativeTitleManager() { return NULL; }
		virtual SystemResourceManager* GetSystemResourceManager() { return NULL; }
		virtual NuclearFileIOManager* GetFileIOManager() = 0;
		virtual SpriteManager* GetSpriteManager() = 0;
		virtual AniManager* GetAniManager() = 0;
		virtual SpineManager* GetSpineManager() = 0;
		virtual World* GetXPWorld() { return NULL; }
		virtual float GetWorldScale() const { return 1.0f; }
		virtual EffectManager* GetEffectManager() = 0;
		virtual Renderer* GetRenderer() = 0;
		virtual PictureHandle GetSpritePictureHandle() const { return INVALID_PICTURE_HANDLE; }
		virtual ConfigManager* GetConfigManager()= 0;
		virtual NuclearRect GetViewport() const = 0;
		virtual int GetTick() const = 0;//ע���ڿ�����24�쵽��49����Ǹ���
		virtual void Render(bool controlFPS) { }
		virtual int GetGameTime() { return 0; }
		virtual bool IsDaytime() const { return true; }
		virtual bool GetWaterDepth(const NuclearLocation& pt, unsigned int &nDepth) const { nDepth = 0; return true; }//����ˮ�����
		virtual IApp* GetApp() = 0;
		virtual bool IsAsyncRead() const { return false; }
		virtual bool ScheduleTimer(INuclearTimer * timer, int period/* ms */) { return false; }
		virtual bool CancelTimer(INuclearTimer * timer) { return false; }

		virtual astar::PathFinder * GetPathFinder() { return NULL; }
		virtual ~EngineBase() { }

	};

}

#endif