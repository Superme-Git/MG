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
		这个类的意义是引擎需要的工具类框架，基本的引擎环境
		引擎中的很多组件都保留了指向此类对象的指针来得到想要的工具
		暂时没想到好的名字
		这个还得重构，将Engine相关的信息和世界相关的信息分开，可能可以有多个世界
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
		virtual int GetTick() const = 0;//注意在开机第24天到第49天会是负的
		virtual void Render(bool controlFPS) { }
		virtual int GetGameTime() { return 0; }
		virtual bool IsDaytime() const { return true; }
		virtual bool GetWaterDepth(const NuclearLocation& pt, unsigned int &nDepth) const { nDepth = 0; return true; }//返回水的深度
		virtual IApp* GetApp() = 0;
		virtual bool IsAsyncRead() const { return false; }
		virtual bool ScheduleTimer(INuclearTimer * timer, int period/* ms */) { return false; }
		virtual bool CancelTimer(INuclearTimer * timer) { return false; }

		virtual astar::PathFinder * GetPathFinder() { return NULL; }
		virtual ~EngineBase() { }

	};

}

#endif