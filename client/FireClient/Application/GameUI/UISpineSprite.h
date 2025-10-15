#ifndef __Nuclear_UISpineSprite_H
#define __Nuclear_UISpineSprite_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <spine/spine-cocos2dx.h>
#include "ComponentsObject.h"

////UI上的SpineSprite精灵
class UISpineSprite
{
public:

	UISpineSprite(const std::wstring& modelName);
	virtual ~UISpineSprite();

	Nuclear::EngineSpriteHandle GetSprite(){ return m_SpriteHandle; }

	void PlayAction(eActionType actionType);
	void SetDefaultAction(eActionType actionType);

	void SetSpineModel(const std::wstring &modelname, bool async);

	void RenderUISprite();

	std::wstring GetModelName() const { return m_modelName; }
	int GetCurActDuration() { return (int)(m_CurActTime * 1000); }

	virtual void SetUILocation(const Nuclear::NuclearLocation& loc);
	virtual void SetUIScale(const float scale);
	virtual void SetUIAlpha(unsigned char alpha);

private:

	std::wstring GetActionName(eActionType type);

private:

	std::wstring					m_modelName;
	Nuclear::EngineSpriteHandle		m_SpriteHandle;

	float m_CurActTime;	//当前动画的播放时间

};

#endif
