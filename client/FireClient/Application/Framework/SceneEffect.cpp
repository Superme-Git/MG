#include "stdafx.h"
#include "SceneEffect.h"
#include "GameScene.h"
#include "GameUIManager.h"

const int s_iTotalDreamFadeEffect = 2500; //进出“梦-境”水波纹效果总时间

SceneEffect::SceneEffect():
m_iDurateTime(0),
m_iTotalTime(0),
m_fAlpha(1.0f),
m_iPictureHandle(Nuclear::INVALID_PICTURE_HANDLE),
m_bIsRenderTarget(false),
m_bEnd(false)
{
}

SceneEffect::~SceneEffect()
{
	FreePicture();
}

void SceneEffect::Start()
{
	m_bEnd = false;
	m_iDurateTime = 0;
}

bool SceneEffect::Update(int elapse)
{
	m_iDurateTime+=elapse;
	if (m_iDurateTime>=m_iTotalTime)
	{
		End(false);
		m_bEnd=true;
		return true;
	}
	return false;
}

void SceneEffect::Draw()
{
	if (m_iPictureHandle!=Nuclear::INVALID_PICTURE_HANDLE)
	{
		unsigned char alpha= 255;
		alpha=(unsigned char)(m_fAlpha*255.0f);
		Nuclear::NuclearColor color(alpha,255,255,255);
		Nuclear::NuclearDisplayMode mode=Nuclear::GetEngine()->GetRenderer()->GetDisplayMode();
		Nuclear::NuclearFRectt rect(0.0f,0.0f,(float)mode.width,(float)mode.height);
		Nuclear::GetEngine()->GetRenderer()->DrawPicture(m_iPictureHandle,rect,color);
	}
}

bool SceneEffect::IsEnd()
{
	return m_bEnd;
}

void SceneEffect::SetIsRenderTarget(bool b)
{
	m_bIsRenderTarget = b;
}

int SceneEffect::GetPicturHandle()
{
	return m_iPictureHandle;
}

void SceneEffect::SetAlpha(float alpha)
{
	m_fAlpha = alpha;
}

float SceneEffect::GetAlpha()
{
	return m_fAlpha;
}

void SceneEffect::SetPictureHandle(int handle)
{
	m_iPictureHandle = handle;
}

int SceneEffect::GetDurationTime()
{
	return m_iDurateTime;
}

int SceneEffect::GetTotalTime()
{
	return m_iTotalTime;
}

void SceneEffect::SetTotalTime(int time)
{
	m_iTotalTime = time;
}

void SceneEffect::RemoveSelf()  //从动画管理器中删除
{
	if (gGetScene())
	{
		gGetScene()->RemoveSceneEffect(this);
	}
}

void SceneEffect::FreePicture()
{
	if (m_iPictureHandle != Nuclear::INVALID_PICTURE_HANDLE)
	{
		if (m_bIsRenderTarget)
		{
			Nuclear::GetEngine()->GetRenderer()->FreeRenderTarget(m_iPictureHandle);
		}
		else
		{
			Nuclear::GetEngine()->GetRenderer()->FreePicture(m_iPictureHandle);
		}
	}
	m_iPictureHandle = Nuclear::INVALID_PICTURE_HANDLE;
}

void SceneEffect::End(bool bRemoveSelf)
{
	FreePicture();
	m_iDurateTime = 0;
	if (bRemoveSelf)
	{
		RemoveSelf();
	}
}

EnterMoiveBlackEffect::EnterMoiveBlackEffect()
{ }

EnterMoiveBlackEffect::~EnterMoiveBlackEffect()
{ }

bool EnterMoiveBlackEffect::Update(int elapse)
{
	bool bEnd=SceneEffect::Update(elapse);
	if (!bEnd)
	{
		int elapseTime = GetDurationTime();
		int totalTime=GetTotalTime();
		float alpha= 1.0f;
		if (elapseTime<totalTime / 3)
		{
			alpha=0.0f;
		}
		else if (elapseTime < totalTime * 2 / 3)
		{
			alpha = 1.0f - (float)(totalTime * 2 / 3 - elapseTime) / (float)(totalTime / 3);
		}
		else if (elapseTime < totalTime)
		{
			alpha = (float)(totalTime - elapseTime) / (totalTime / 3);
		}

		SetAlpha(alpha);
	}
	return bEnd;
}

EnterMoiveBlendEffect::EnterMoiveBlendEffect()
{ }

EnterMoiveBlendEffect::~EnterMoiveBlendEffect()
{ }

bool EnterMoiveBlendEffect::Update(int elapse)
{
	bool bEnd = SceneEffect::Update(elapse);
	if (!bEnd)
	{
		int elapseTime = GetDurationTime();
		int totalTime = GetTotalTime();
		float alpha = 1.0f;
		if (elapseTime < totalTime / 4)
		{
			alpha = (float)elapseTime / (float)(totalTime / 4);
		}
		else if (elapseTime < totalTime * 3 / 4)
		{
			alpha = 1.0f;
		}
		else if (elapseTime < totalTime)
		{
			alpha = (float)(totalTime - elapseTime) / (float)(totalTime / 4);
		}

		SetAlpha(alpha);
	}
	else
	{
		gGetGameUIManager()->SetShowGameUI(true);
	}
	return bEnd;
}

HideSceneEffect::HideSceneEffect()
{ }

HideSceneEffect::~HideSceneEffect()
{ }

bool HideSceneEffect::Update(int elapse)
{
	bool bEnd = SceneEffect::Update(elapse);
	if (!bEnd)
	{
		int elapseTime = GetDurationTime();
		int totalTime = GetTotalTime();
		float alpha = 1.0f - (float)elapseTime / (float)totalTime;      //alpha从1-0,用一张黑图盖
		SetAlpha(alpha);

	}
	return bEnd;
}

ShowSceneEffect::ShowSceneEffect()
{ }

ShowSceneEffect::~ShowSceneEffect()
{ }

bool ShowSceneEffect::Update(int elapse)
{
	bool bEnd = SceneEffect::Update(elapse);
	if (!bEnd)
	{
		int elapseTime = GetDurationTime();
		int totalTime = GetTotalTime();
		float alpha = (float)elapseTime / (float)totalTime;      //alpha从0-1,用一张黑图盖
		SetAlpha(alpha);
	}
	return bEnd;
}


extern bool gIsInBackground();

void DreamFadeEffect::Draw()
{
	SceneEffect::Draw();
}

bool DreamFadeEffect::Update(int elapse)
{
	bool bEnd = SceneEffect::Update(elapse);
	if (!bEnd)
	{
		int elapseTime = GetDurationTime();
		int totalTime = GetTotalTime();
		float alpha = 1.0f - (float)elapseTime / (float)totalTime;      //alpha从1-0,用一张黑图盖
		SetAlpha(alpha);
	}
	return bEnd;
}

void DreamFadeEffect::Start()
{
    int worldCaptureHandle = 0;
    if(!gIsInBackground())
    {
        worldCaptureHandle = Nuclear::GetEngine()->CaptureWorld();
    }
	
	if (worldCaptureHandle!=Nuclear::INVALID_PICTURE_HANDLE)
	{
		SetPictureHandle(worldCaptureHandle);
		SetTotalTime(s_iTotalDreamFadeEffect);		
	}
	SceneEffect::Start();

}

DreamFadeEffect::DreamFadeEffect()
	: m_iCaptureWorldHandle(Nuclear::INVALID_PICTURE_HANDLE)
{

}

DreamFadeEffect::~DreamFadeEffect()
{
	if (m_iCaptureWorldHandle != Nuclear::INVALID_PICTURE_HANDLE)
	{
		Nuclear::GetEngine()->GetRenderer()->FreePicture(m_iCaptureWorldHandle);
		m_iCaptureWorldHandle = Nuclear::INVALID_PICTURE_HANDLE;
	}

}

