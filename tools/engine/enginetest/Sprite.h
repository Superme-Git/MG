#pragma once
#include "..\engine\isprite.h"

class Sprite
{
private:
	Nuclear::ISprite *m_pSprite;
	//又不是真的客户端，不管在马上的攻击了
	int m_actState;	//0 stand1 1 runleft 2 runright 3 在马上 4 在攻击 5 正在播放死亡动作 6 怪物的站立stand1 7 怪物的走 run2
	bool m_isTel;

public:
	Sprite(Nuclear::ISprite *pSprite);
	~Sprite(void);

	void SetPeople(bool b) { m_actState = b?0:6; }
	//如果返回false就代表这个东西挂了，可以干掉了
	bool UpdateAction(Nuclear::XPUSA_TYPE type);
	void GoToRun();
	void Attack();
	void Die();
	void StopAttack();
	int GetState() const { return m_actState; }

	void SetScale(float scale) { m_pSprite->SetScale(scale); }
	float GetScale () const { return m_pSprite->GetScale(); }
	void UseRenderTarget(bool b) { m_pSprite->UseRenderTarget(b); }
	bool IsUseRenderTarget() const { return m_pSprite->IsUseRenderTarget(); }

	
	//ISprite的接口的包装
	// 设置位置。location 世界像素坐标。
	void SetLocation(const Nuclear::Location& location) { m_pSprite->SetLocation(location); }
	// 得到位置.
	Nuclear::Location GetLocation() const { return m_pSprite->GetLocation(); }
	int GetBubbleItemOffset() const { return m_pSprite->GetBubbleItemOffset(); }

	// 设置和得到方向。direction 世界坐标方向
	void SetDirection( Nuclear::XPDIRECTION direction ) { m_pSprite->SetDirection(direction); }
	Nuclear::XPDIRECTION GetDirection() const { return m_pSprite->GetDirection(); }

	// 设置是否可见
	void SetVisible(bool v) { m_pSprite->SetVisible(v); }
	bool IsVisiable() const { return m_pSprite->IsVisiable(); }

	// 检查能否选择ani的时候的模式
	void SetMouseTestMode(Nuclear::XPMOUSE_TEST_MODE m) { m_pSprite->SetMouseTestMode(m); }
	Nuclear::XPMOUSE_TEST_MODE GetMouseTestMode() const { return m_pSprite->GetMouseTestMode(); }

	// 新版头顶文字，将用UI来画
	void SetTitle(Nuclear::EntitativeTitleHandle handle, Nuclear::XPTITLE_ALIGN align = Nuclear::XPTA_DEFAULT) { m_pSprite->SetTitle(handle, align); }
	Nuclear::EntitativeTitleHandle UnsetTitle() { return m_pSprite->UnsetTitle(); }
	void SetTitleSize(int width, int height) { m_pSprite->SetTitleSize(width, height); }
	int GetTitleWidth() const { return m_pSprite->GetTitleWidth(); }
	int GetTitleHeight() const { return m_pSprite->GetTitleHeight(); }

	// 设置精灵模型
	void SetModel(const std::wstring &modelname) { m_pSprite->SetModel(modelname); }

	void SetEffectMinMax(float min, float max) { m_pSprite->SetMinMaxEffectAlpha(min, max); }


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
		m_pSprite->SetDefaultAction(action_name, fHoldLastFrame, freq);
	}
	std::wstring GetDefaultAction() { return m_pSprite->GetDefaultAction(); }
	// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
	void PlayAction(const std::wstring& action_name, float freq=1.0f) { m_pSprite->PlayAction(action_name, freq, false); }
	std::wstring GetCurActionName() { return m_pSprite->GetCurActionName(); }
	// 当前动作的播到哪帧（此数据在Render的时候更新，在Render前为上一帧的数据，Render后为本帧的数据）
	int GetCurrentFrame() const { return m_pSprite->GetCurrentFrame(); }
	// 当前动作的总帧数
	int GetTotalFrame() const { return m_pSprite->GetTotalFrame(); }


	// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
	bool PrefetchAction(const std::wstring &action_name) { return m_pSprite->PrefetchAction(action_name); }

	// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
	void HoldAction(const std::wstring &action_name) { m_pSprite->HoldAction(action_name); }
	// 只有Hold住的Action才需要Release
	void ReleaseAction(const std::wstring &action_name) { m_pSprite->ReleaseAction(action_name); }

	// 绑在精灵上播放的效果

	//设置绑定文件，当设置成L""的时候为取消
	bool SetBindFile(const std::wstring &bindFile) { return m_pSprite->SetBindFile(bindFile); }
	std::wstring GetBindFile() { return m_pSprite->GetBindFile(); }
	// 设置和取消持续效果，需要手工取消，个数无上限
	Nuclear::IEffect* SetDurativeEffect(const std::wstring &effect_name, const Nuclear::CPOINT& relpos, unsigned int flag)
	{
		return m_pSprite->SetDurativeEffect(effect_name, relpos, flag);
	}
	Nuclear::IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const Nuclear::CPOINT& relpos, unsigned int flag)
	{
		return m_pSprite->SetContinueEffect(headEffect, midEffect, endEffect, continueTime, relpos, flag);
	}
	void RemoveDurativeEffect(Nuclear::IEffect* pEffect) { m_pSprite->RemoveDurativeEffect(pEffect); }

	// 临时效果，播放完毕自动取消，个数无上限, times是播放次数
	void PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, bool selfRef)
	{
		m_pSprite->PlayEffect(effect_name, dx, dy, times, selfRef, true);
	}

	//
	void EnableShadow(bool b) { m_pSprite->EnableShadow(b); }
	bool IsEnableShadow() { return m_pSprite->IsEnableShadow();	}

	// 设置精灵可达图掩码。为0，忽略障碍。
	void SetMoveMask(unsigned int mask) { m_pSprite->SetMoveMask(mask); }

	// 精灵瞬移拖残影
	void TeleportWithBlur(const Nuclear::Location &point, int telTime, int blurTime) { m_pSprite->TeleportWithBlur(point, telTime, blurTime); m_isTel = true;}

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
};
