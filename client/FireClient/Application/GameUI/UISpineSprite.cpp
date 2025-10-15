#include "stdafx.h"
#include "UISpineSprite.h"
#include "engine/nuengine.h"
#include "GameUIManager.h"

UISpineSprite::UISpineSprite(const std::wstring& modelName)
	: m_modelName(modelName)
	, m_CurActTime(0.f)
{
	m_SpriteHandle = Nuclear::GetEngine()->CreateEngineSprite(modelName, false, true);
	SetDefaultAction(eActionStand);

	if (gGetGameUIManager()) {
		gGetGameUIManager()->AddUISpineSprite(this);
	}
}

UISpineSprite::~UISpineSprite()
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		Nuclear::GetEngine()->ReleaseEngineSprite(m_SpriteHandle);
		m_SpriteHandle = Nuclear::INVALID_ENGINE_SPRITE_HANDLE;
	}

	if (gGetGameUIManager()) {
		gGetGameUIManager()->RemoveUISpineSprite(this);
	}
}

void UISpineSprite::PlayAction(eActionType actionType)
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		std::wstring actionname = GetActionName(actionType);
		Nuclear::GetEngine()->SetEngineSpriteAction(m_SpriteHandle, actionname);
		Nuclear::GetEngine()->GetEngineSpriteActionTimeByName(m_SpriteHandle, actionname, m_CurActTime);
	}
}

void UISpineSprite::SetDefaultAction(eActionType actionType)
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		std::wstring actionname = GetActionName(actionType);
		Nuclear::GetEngine()->SetEngineSpriteDefaultAction(m_SpriteHandle, actionname);
	}
}

void UISpineSprite::RenderUISprite()
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		Nuclear::GetEngine()->RendererEngineSprite(m_SpriteHandle);
	}
}

void UISpineSprite::SetUILocation(const Nuclear::NuclearLocation& loc)
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		//Nuclear::NuclearRect vp = Nuclear::GetEngine()->GetWorld()->GetViewport();
		//Nuclear::NuclearLocation vpLocation(vp.left, vp.top);
		Nuclear::GetEngine()->SetEngineSpriteLoc(m_SpriteHandle,/* vpLocation +*/ loc);
	}
}

void UISpineSprite::SetUIScale(const float scale)
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		Nuclear::GetEngine()->SetEngineSpriteScale(m_SpriteHandle, scale);
	}
}

void UISpineSprite::SetUIAlpha(unsigned char alpha)
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		Nuclear::GetEngine()->SetEngineSpriteAlpha(m_SpriteHandle, alpha);
	}
}

void UISpineSprite::SetSpineModel(const std::wstring &modelname, bool async)
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		Nuclear::GetEngine()->SetEngineSpriteModel(m_SpriteHandle, modelname, async);
	}
}

std::wstring UISpineSprite::GetActionName(eActionType type)
{
	return gGetActionName(type);
}
