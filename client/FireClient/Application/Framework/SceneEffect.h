/***********************************************************************
filename: 	SceneEffect.h
author:		eagle
purpose:	一种场景效果
*************************************************************************/
#pragma once

class SceneEffect 
{
public:
	SceneEffect();
	virtual ~SceneEffect();

	virtual void Start();
	virtual void End(bool bRemoveSelf=true);
	virtual bool Update(int elapse); //return true说明结束了
	virtual void Draw();

	bool IsEnd();
	void SetIsRenderTarget(bool b);
	int GetPicturHandle();
	void SetAlpha(float alpha);
	float GetAlpha();
	void SetPictureHandle(int handle);
	int GetDurationTime();
	int GetTotalTime();
	void SetTotalTime(int time);

protected:
    void FreePicture();
	void RemoveSelf();  //从动画管理器中删除

private:
	int m_iDurateTime; //效果已经持续时间
	int m_iTotalTime; //总时间
	float m_fAlpha;
	int m_iPictureHandle;
	bool m_bIsRenderTarget; //是否是场景截图
	bool m_bEnd;
};

class DreamFadeEffect : public SceneEffect  //进出“梦-境”的水波纹效果
{
public:
	DreamFadeEffect();
	~DreamFadeEffect();

	void Draw();
	bool Update(int elapse);
	void Start();

private:
	int m_iCaptureWorldHandle;
};

class EnterMoiveBlackEffect : public SceneEffect  //跳场景动画时的黑遮罩的效果
{
public:
	EnterMoiveBlackEffect();
	~EnterMoiveBlackEffect();

	bool Update(int elapse);
};

class ShowSceneEffect : public SceneEffect  //渐亮当前场景效果
{
public:
	ShowSceneEffect();
	~ShowSceneEffect();

	bool Update(int elapse);
};

class EnterMoiveBlendEffect : public SceneEffect  //跳场景动画时的半透明遮罩的效果
{
public:
	EnterMoiveBlendEffect();
	~EnterMoiveBlendEffect();

	bool Update(int elapse);
};

class HideSceneEffect : public SceneEffect  //渐隐当前场景效果
{
public:
	HideSceneEffect();
	~HideSceneEffect();

	bool Update(int elapse);
};



