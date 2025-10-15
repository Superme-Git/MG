#include "stdafx.h"
#include "MovieSceneNpc.h"
#include "GameScene.h"
#include "MainCharacter.h"

MovieSceneNpc::MovieSceneNpc()
{
	m_eSceneType = eSceneObjMovie;
	SetNameColour(0xFFFFF000);
	SetNameShadowColor(0xFF200238);
}
MovieSceneNpc::~MovieSceneNpc()
{
}

void MovieSceneNpc::Draw(int now)
{
	if (IsVisiable())
	{
		if (m_data.chengwei != L"")
		{
			Nuclear::NuclearLocation loc = GetLocation();
			Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();

			if (pFontMan&&m_pTitleTextBlock&&m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
			{

				DWORD color = 0xFF00F6FF;
				m_pTitleTextBlock->SetFontColor(color, color);
				m_pTitleTextBlock->SetShadowColor(0xFF01111D);
				int height = m_pTitleTextBlock->GetTextHeight();
				int width = m_pTitleTextBlock->GetTextWidth();

				m_titleIndex++;
				int left = loc.x - gGetScene()->GetViewport().left - width / 2;
				int top = loc.y - gGetScene()->GetViewport().top;
				pFontMan->DrawText(m_TitleTextureHandle, (float)left, (float)top, NULL);
			}
		}

		SceneObject::Draw(now);
	}
}
bool MovieSceneNpc::SetAttribute(const stMovieSceneNpcData& data)
{
	m_data=data;
	
	SetID(m_data.id);

	UpdatNameTexture();

	if (m_data.chengwei != L"")
	{
		DWORD color = m_data.chengweiColor;
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			m_TitleTextureHandle = pFontMan->NewText(m_data.chengwei.c_str(), 0, color, color);
			m_pTitleTextBlock = pFontMan->GetTextBlock(m_TitleTextureHandle);
		}
	}

	
	
	if (NULL == GetSprite())
	{
		NewSpriteModel(data.model);
		
		if (data.id==0) 
		{
			UpdateSpriteComponent(GetMainCharacter()->GetComponentInfo());
		}
		SetDirection(data.dir);
		
		return true;
	}
	
	return false;
}
void MovieSceneNpc::Run(int now,int delta)
{	
	SceneObject::Run(now,delta);
}
void MovieSceneNpc::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	SceneObject::UpdateAction(type);
	if (type == Nuclear::XPUSAT_MOVEINFLEXION)
	{
		astar::Path path;
		GetPath(path);
		if (path.empty())
		{
			StopMove();
			PlayAction(eActionStand);
		}
	}
}
bool MovieSceneNpc::PlayAction(eActionType actionType,float freq)
{
	ComponentsObject::PlayAction(actionType, freq);
	return true;
}
void MovieSceneNpc::StopMove()
{
	if (!IsMoving())
		return;
	SceneObject::StopMove();
	SetMoveMask(eMoveMaskNormal);
}