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
#include "astar/astar.hpp" // in share. �������ﲻ��include������Ԥ���� tyedef �� PathFinder��

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
	������������������Ҫ�Ĺ������ܣ����������滷��
	�����еĺܶ������������ָ���������ָ�����õ���Ҫ�Ĺ���
	��ʱû�뵽�õ�����
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
	virtual int GetTick() const = 0;//ע���ڿ�����24�쵽��49����Ǹ���
	virtual void Render(bool controlFPS) { }
	virtual int GetGameTime() { return 0; }
	virtual bool IsDaytime() const { return true; }
	virtual unsigned int GetWaterDepth(const Location& pt) const { return 0; }//����ˮ�����
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