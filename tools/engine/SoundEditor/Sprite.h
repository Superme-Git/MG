#pragma once
#include "..\engine\isprite.h"

class Sprite
{
private:
	Nuclear::ISprite *m_pSprite;
	int m_actState;	//0 stand1 1 runleft 2 runright
	std::wstring m_runleft;
	std::wstring m_runright;
	std::wstring m_stand;

public:
	Sprite(Nuclear::ISprite *pSprite);
	~Sprite(void);

	void UpdateAction(Nuclear::XPUSA_TYPE type);
	void GoToRun();
	int GetState() const { return m_actState; }
	void SetRunLeft(const std::wstring &name) { m_runleft = name; }
	void SetRunRight(const std::wstring &name) { m_runright = name; }
	void SetStand(const std::wstring &name) { m_stand = name; }
	const std::wstring& GetRunLeft() const { return m_runleft; }
	const std::wstring& GetRunRight() const { return m_runright; }
	const std::wstring& GetStand() const { return m_stand; }
	
	//ISprite的接口的包装
	void EnableStepSound(bool b) { m_pSprite->EnableStepSound(b); }
	bool IsStepSoundEnable() const { return m_pSprite->IsStepSoundEnable(); }
	// 设置位置。location 世界像素坐标。
	void SetLocation(const Nuclear::Location& location) { m_pSprite->SetLocation(location); }
	// 得到位置.
	Nuclear::Location GetLocation() const { return m_pSprite->GetLocation(); }

	// 设置和得到方向。direction 世界坐标方向
	void SetDirection( Nuclear::XPDIRECTION direction ) { m_pSprite->SetDirection(direction); }
	Nuclear::XPDIRECTION GetDirection() const { return m_pSprite->GetDirection(); }

	// 设置是否可见
	void SetVisible(bool v) { m_pSprite->SetVisible(v); }
	bool IsVisiable() const { return m_pSprite->IsVisiable(); }

	// 设置精灵模型
	void SetModel(const std::wstring &modelname) { m_pSprite->SetModel(modelname); }

	// 设置精灵的装备组件
	void SetComponent(int scid, const std::wstring& resource, Nuclear::XPCOLOR color=0xffffffff/*XRGB*/)
	{
		m_pSprite->SetComponent(scid, resource, color);
	}
	// 设置坐骑的名字（当骑马的时候调用，下马的时候设上L""）
	void SetRideName(const std::wstring& name);
	
	// 默认动作，每个精灵必须有一个默认动作
	void SetDefaultAction( const std::wstring& action_name, bool fHoldLastFrame, float freq=1.0f)
	{
		m_pSprite->SetDefaultAction(action_name, Nuclear::XPSALT_SYNC, freq, fHoldLastFrame);
	}
	std::wstring GetDefaultAction() { return m_pSprite->GetDefaultAction(); }
	// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
	void PlayAction(const std::wstring& action_name, float freq=1.0f) { m_pSprite->PlayAction(action_name, Nuclear::XPSALT_SYNC, freq); }
	std::wstring GetCurActionName() { return m_pSprite->GetCurActionName(); }
	// 当前动作的播到哪帧（此数据在Render的时候更新，在Render前为上一帧的数据，Render后为本帧的数据）
	int GetCurrentFrame() const { return m_pSprite->GetCurrentFrame(); }
	// 当前动作的总帧数
	int GetTotalFrame() const { return m_pSprite->GetTotalFrame(); }

	// 绑在精灵上播放的效果

	//设置绑定文件，当设置成L""的时候为取消
	bool SetBindFile(const std::wstring &bindFile) { return m_pSprite->SetBindFile(0, bindFile); }
	std::wstring GetBindFile() { return m_pSprite->GetBindFile(0); }
	// 设置和取消持续效果，需要手工取消，个数无上限
	// bindType精灵特殊绑定点
	// 0 固定绑定点：dx，dy是相对精灵原点。
	// 1 武器绑定点：会随动作变化而改变（每一帧）。此时忽略dx，dy。
	Nuclear::IEffect* SetDurativeEffect(const std::wstring &effect_name, const Nuclear::CPOINT& relpos, unsigned int flag)
	{
		return m_pSprite->SetDurativeEffect(effect_name, relpos, flag);
	}
	Nuclear::IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const Nuclear::CPOINT& relpos, unsigned int flag)
	{
		return m_pSprite->SetContinueEffect(headEffect, midEffect, endEffect, continueTime, relpos, flag);
	}
	void RemoveDurativeEffect(Nuclear::IEffect* pEffect) { m_pSprite->RemoveDurativeEffect(pEffect); }

	//
	void EnableShadow(bool b) { m_pSprite->EnableShadow(b); }
	bool IsEnableShadow() { return m_pSprite->IsEnableShadow();	}

	// 精灵瞬移拖残影
	void TeleportWithBlur(const Nuclear::Location &point) { m_pSprite->TeleportWithBlur(point, 500, 300); }

	// 精灵移动速度，单位：像素/毫秒。初始为 0.160
	void  SetMoveSpeed(float speed) { m_pSprite->SetMoveSpeed(speed); }
	float GetMoveSpeed() const { return m_pSprite->GetMoveSpeed(); }

	// 停止移动。清除未走完的路径。
	void StopMove() { return m_pSprite->StopMove(); }
	// 是否移动中。还有路需要走，没有到达目的地。
	bool IsMoving() const { return m_pSprite->IsMoving(); }
	// 设置暂停状态。这个个完全独立的控制状态，与是否真的在移动中( IsMoving )无关。
	//       true  精灵将不再移动，但保持所有现有的移动状态，如，当前路径和尾迹。
	//             新的移动命令（尾迹）全部在这种状态下，只会被记录。
	//       false 精灵恢复到移动状态。
	void SetMoveSuspended(bool moveSuspended) { m_pSprite->SetMoveSuspended(moveSuspended); }
	// 是否暂停状态。
	bool IsMoveSuspended() const { return m_pSprite->IsMoveSuspended(); }

	void MoveTo(int targetX, int targetY, int range, const Nuclear::CSIZE * size) { m_pSprite->MoveTo(targetX, targetY, range, size) ;}
	void MoveTo(const Nuclear::Location & target, int range, const Nuclear::CSIZE * size) { this->MoveTo(target.x, target.y, range, size); }
	
	// 转向目标点
	void SetDirection(int targetx, int targety) { m_pSprite->SetDirection(targetx, targety); }
	void SetDirection(const Nuclear::CPOINT & target) { SetDirection(target.x, target.y); }

	// 设置精灵可达图掩码。为0，忽略障碍。
	virtual void SetMoveMask(unsigned int mask) { m_pSprite->SetMoveMask(mask); };
};
