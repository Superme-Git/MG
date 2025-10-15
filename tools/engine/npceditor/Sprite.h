#pragma once
#include "engine\isprite.h"

class Sprite
{
private:
	
	int m_actState;	//0 stand1 1 runleft 2 runright 3 在马上
	bool m_isTel;

public:
	Nuclear::ISprite *m_pSprite;
	Sprite(Nuclear::ISprite *pSprite);
	~Sprite(void);

	void UpdateAction(Nuclear::XPUSA_TYPE type);
	void GoToRun();
	int GetState() const { return m_actState; }
	
	//ISprite的接口的包装
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

	// 头顶文字，slot [0,1,2] 从上到下
	//void SetTitle(int slot, const std::wstring &title, int fontType, Nuclear::XPCOLOR color) { m_pSprite->SetTitle(slot, title, fontType, color); }
	//void UnsetTitle(int slot) { m_pSprite->UnsetTitle(slot); }

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
		m_pSprite->SetDefaultAction(action_name, Nuclear::XPSALT_SYNC, freq,fHoldLastFrame);
	}
	std::wstring GetDefaultAction() { return m_pSprite->GetDefaultAction(); }
	// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
	void PlayAction(const std::wstring& action_name, float freq=1.0f) { 
		m_pSprite->PlayAction(action_name, Nuclear::XPSALT_SYNC, freq); 
	}
	//std::wstring PlayAction() { return m_pSprite->PlayAction(); }
	// 当前动作的播到哪帧（此数据在Render的时候更新，在Render前为上一帧的数据，Render后为本帧的数据）
	int GetCurrentFrame() const { return m_pSprite->GetCurrentFrame(); }
	// 当前动作的总帧数
	int GetTotalFrame() const { return m_pSprite->GetTotalFrame(); }

	// 绑在精灵上播放的效果

	//设置绑定文件，当设置成L""的时候为取消
	bool SetBindFile(const std::wstring &bindFile) { return m_pSprite->SetBindFile(0,bindFile); }
	std::wstring GetBindFile() { return m_pSprite->GetBindFile(0); }
	// 设置和取消持续效果，需要手工取消，个数无上限
	// bindType精灵特殊绑定点
	// 0 固定绑定点：dx，dy是相对精灵原点。
	// 1 武器绑定点：会随动作变化而改变（每一帧）。此时忽略dx，dy。
	Nuclear::IEffect* SetDurativeEffect(const std::wstring &effect_name,  int bindType, int dx, int dy, bool selfRef)
	{
		//return m_pSprite->SetDurativeEffect(effect_name, bindType, dx, dy, selfRef);
	}
	Nuclear::IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, int bindType, int dx, int dy, bool selfRef)
	{
		return NULL;
		//return m_pSprite->SetContinueEffect(headEffect, midEffect, endEffect, continueTime, bindType, dx, dy, selfRef);
	}
	void RemoveDurativeEffect(Nuclear::IEffect* pEffect) { m_pSprite->RemoveDurativeEffect(pEffect); }

	// 临时效果，播放完毕自动取消，个数无上限, times是播放次数
	void PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, bool selfRef)
	{
//		m_pSprite->PlayEffect(effect_name, dx, dy, times, selfRef);
	}

	//
	void EnableShadow(bool b) { m_pSprite->EnableShadow(b); }
	bool IsEnableShadow() { return m_pSprite->IsEnableShadow();	}

	// 精灵瞬移拖残影
//	void TeleportWithBlur(const Nuclear::Location &point) { m_pSprite->TeleportWithBlur(point); m_isTel = true;}

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
	void SetDirection(int dir) { m_pSprite->SetDirection((Nuclear::XPDIRECTION)dir); }

};
