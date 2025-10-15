#ifndef __Nuclear_ENGINEBASE_H
#define __Nuclear_ENGINEBASE_H

#if _MSC_VER > 1000
#pragma once
#endif

//#include "systemresourcemanager.h"
//#include "common\fileiomanager.h"
//#include "animanager.h"
//#include "sound\directsoundmanager.h"
//#include "sprite\spritemanager.h"
//#include "effect\effectmanager.h"
//#include "renderer\renderer.h"
//#include "configmanager.h"
//#include "iapp.h"
//#include "titlemanager.h"

#include "..\common\xptypes.h"
#include "astar/astar.hpp" // in share. 本来这里不想include。不能预定义 tyedef 的 PathFinder。

namespace Nuclear
{

class IEntitativeTitleManager;
class DirectSoundManager;
class SystemResourceManager;
class CFileIOManager;
class SpriteManager;
class AniManager;
class EffectManager;
class Renderer;
class ConfigManager;
class StepSoundType;
class IApp;
class World;
/*
	这个类的意义是引擎需要的工具类框架，基本的引擎环境
	引擎中的很多组件都保留了指向此类对象的指针来得到想要的工具
	暂时没想到好的名字
*/

class EngineBase
{
public:
	virtual IEntitativeTitleManager *GetEntitativeTitleManager() { return NULL; }
	virtual DirectSoundManager* GetSoundManager() { return NULL; }
	virtual SystemResourceManager* GetSystemResourceManager() { return NULL; }
	virtual CFileIOManager* GetFileIOManager() = 0;
	virtual SpriteManager* GetSpriteManager() = 0;
	virtual AniManager* GetAniManager() = 0;
	virtual World* GetXPWorld() { return NULL; }
	virtual float GetWorldScale() const { return 1.0f; }
	virtual EffectManager* GetEffectManager() = 0;
	virtual Renderer* GetRenderer(HWND hwnd = NULL) = 0;
	virtual PictureHandle GetSpritePictureHandle() const { return INVALID_PICTURE_HANDLE; }
	virtual PictureHandle GetAlphaSpritePictureHandle() const { return INVALID_PICTURE_HANDLE; }
	virtual bool GetLightPos(CPOINT &pos) { return false; }
	virtual StepSoundType* GetStepSoundType() const = 0;
	virtual ConfigManager* GetConfigManager()= 0;
	virtual CRECT GetViewport() const = 0;
	virtual int GetTick() const = 0;//注意在开机第24天到第49天会是负的
	virtual void Render(bool controlFPS) { }
	virtual int GetGameTime() { return 0; }
	virtual bool IsDaytime() const { return true; }
	virtual unsigned int GetWaterDepth(const Location& pt) const { return 0; }//返回水的深度
	virtual IApp* GetApp() = 0;
	virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const { shearX = 0.7f; scalingY = 0.7f; }
	virtual bool IsAsyncRead() const { return false; }
	virtual bool ScheduleTimer(ITimer * timer, int period/* ms */) { return false; }
	virtual bool CancelTimer(ITimer * timer) { return false; }

	virtual astar::PathFinder * GetPathFinder() { return NULL; }
	virtual ~EngineBase() { }

};

}

#endif