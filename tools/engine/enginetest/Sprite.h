#pragma once
#include "..\engine\isprite.h"

class Sprite
{
private:
	Nuclear::ISprite *m_pSprite;
	//�ֲ�����Ŀͻ��ˣ����������ϵĹ�����
	int m_actState;	//0 stand1 1 runleft 2 runright 3 ������ 4 �ڹ��� 5 ���ڲ����������� 6 �����վ��stand1 7 ������� run2
	bool m_isTel;

public:
	Sprite(Nuclear::ISprite *pSprite);
	~Sprite(void);

	void SetPeople(bool b) { m_actState = b?0:6; }
	//�������false�ʹ�������������ˣ����Ըɵ���
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

	
	//ISprite�Ľӿڵİ�װ
	// ����λ�á�location �����������ꡣ
	void SetLocation(const Nuclear::Location& location) { m_pSprite->SetLocation(location); }
	// �õ�λ��.
	Nuclear::Location GetLocation() const { return m_pSprite->GetLocation(); }
	int GetBubbleItemOffset() const { return m_pSprite->GetBubbleItemOffset(); }

	// ���ú͵õ�����direction �������귽��
	void SetDirection( Nuclear::XPDIRECTION direction ) { m_pSprite->SetDirection(direction); }
	Nuclear::XPDIRECTION GetDirection() const { return m_pSprite->GetDirection(); }

	// �����Ƿ�ɼ�
	void SetVisible(bool v) { m_pSprite->SetVisible(v); }
	bool IsVisiable() const { return m_pSprite->IsVisiable(); }

	// ����ܷ�ѡ��ani��ʱ���ģʽ
	void SetMouseTestMode(Nuclear::XPMOUSE_TEST_MODE m) { m_pSprite->SetMouseTestMode(m); }
	Nuclear::XPMOUSE_TEST_MODE GetMouseTestMode() const { return m_pSprite->GetMouseTestMode(); }

	// �°�ͷ�����֣�����UI����
	void SetTitle(Nuclear::EntitativeTitleHandle handle, Nuclear::XPTITLE_ALIGN align = Nuclear::XPTA_DEFAULT) { m_pSprite->SetTitle(handle, align); }
	Nuclear::EntitativeTitleHandle UnsetTitle() { return m_pSprite->UnsetTitle(); }
	void SetTitleSize(int width, int height) { m_pSprite->SetTitleSize(width, height); }
	int GetTitleWidth() const { return m_pSprite->GetTitleWidth(); }
	int GetTitleHeight() const { return m_pSprite->GetTitleHeight(); }

	// ���þ���ģ��
	void SetModel(const std::wstring &modelname) { m_pSprite->SetModel(modelname); }

	void SetEffectMinMax(float min, float max) { m_pSprite->SetMinMaxEffectAlpha(min, max); }


	// ���þ����װ�����
	void SetComponent(int scid, const std::wstring& resource, Nuclear::XPCOLOR color=0xffffffff/*XRGB*/)
	{
		m_pSprite->SetComponent(scid, resource, color);
	}
	// ������������֣��������ʱ����ã������ʱ������L""��
	void SetRideName(const std::wstring& name);
	
	// Ĭ�϶�����ÿ�����������һ��Ĭ�϶���
	void SetDefaultAction( const std::wstring& action_name, bool fHoldLastFrame, float freq=1.0f)
	{
		m_pSprite->SetDefaultAction(action_name, fHoldLastFrame, freq);
	}
	std::wstring GetDefaultAction() { return m_pSprite->GetDefaultAction(); }
	// ��ʱ������ÿ��������0~1���� ��ʱ�����������֮���Զ��л�Ĭ�϶���, action_name �����ַ���ȡ����ʱ����
	void PlayAction(const std::wstring& action_name, float freq=1.0f) { m_pSprite->PlayAction(action_name, freq, false); }
	std::wstring GetCurActionName() { return m_pSprite->GetCurActionName(); }
	// ��ǰ�����Ĳ�����֡����������Render��ʱ����£���RenderǰΪ��һ֡�����ݣ�Render��Ϊ��֡�����ݣ�
	int GetCurrentFrame() const { return m_pSprite->GetCurrentFrame(); }
	// ��ǰ��������֡��
	int GetTotalFrame() const { return m_pSprite->GetTotalFrame(); }


	// Ԥȡ�þ���Ķ�����Դ���������Դ��GCʱ�䷶Χ��120�룩��û�б�ʹ�û��߱����£����ľ��飩Ԥȡ����ô���Զ��ͷ�
	bool PrefetchAction(const std::wstring &action_name) { return m_pSprite->PrefetchAction(action_name); }

	// ��ס�þ���Ķ�����Դ�����������ReleaseAction����ô����Զ���ͷţ�ֱ�����黻ģ�ͻ��߱�����
	void HoldAction(const std::wstring &action_name) { m_pSprite->HoldAction(action_name); }
	// ֻ��Holdס��Action����ҪRelease
	void ReleaseAction(const std::wstring &action_name) { m_pSprite->ReleaseAction(action_name); }

	// ���ھ����ϲ��ŵ�Ч��

	//���ð��ļ��������ó�L""��ʱ��Ϊȡ��
	bool SetBindFile(const std::wstring &bindFile) { return m_pSprite->SetBindFile(bindFile); }
	std::wstring GetBindFile() { return m_pSprite->GetBindFile(); }
	// ���ú�ȡ������Ч������Ҫ�ֹ�ȡ��������������
	Nuclear::IEffect* SetDurativeEffect(const std::wstring &effect_name, const Nuclear::CPOINT& relpos, unsigned int flag)
	{
		return m_pSprite->SetDurativeEffect(effect_name, relpos, flag);
	}
	Nuclear::IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const Nuclear::CPOINT& relpos, unsigned int flag)
	{
		return m_pSprite->SetContinueEffect(headEffect, midEffect, endEffect, continueTime, relpos, flag);
	}
	void RemoveDurativeEffect(Nuclear::IEffect* pEffect) { m_pSprite->RemoveDurativeEffect(pEffect); }

	// ��ʱЧ������������Զ�ȡ��������������, times�ǲ��Ŵ���
	void PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, bool selfRef)
	{
		m_pSprite->PlayEffect(effect_name, dx, dy, times, selfRef, true);
	}

	//
	void EnableShadow(bool b) { m_pSprite->EnableShadow(b); }
	bool IsEnableShadow() { return m_pSprite->IsEnableShadow();	}

	// ���þ���ɴ�ͼ���롣Ϊ0�������ϰ���
	void SetMoveMask(unsigned int mask) { m_pSprite->SetMoveMask(mask); }

	// ����˲���ϲ�Ӱ
	void TeleportWithBlur(const Nuclear::Location &point, int telTime, int blurTime) { m_pSprite->TeleportWithBlur(point, telTime, blurTime); m_isTel = true;}

	// �����ƶ��ٶȣ���λ������/���롣��ʼΪ 0.160
	void  SetMoveSpeed(float speed) { m_pSprite->SetMoveSpeed(speed); }
	float GetMoveSpeed() const { return m_pSprite->GetMoveSpeed(); }

	// ֹͣ�ƶ������δ�����·����
	void StopMove() { return m_pSprite->StopMove(); }
	// �Ƿ��ƶ��С�����·��Ҫ�ߣ�û�е���Ŀ�ĵء�
	bool IsMoving() const { return m_pSprite->IsMoving(); }
	// ������ͣ״̬���������ȫ�����Ŀ���״̬�����Ƿ�������ƶ���( IsMoving )�޹ء�
	//       true  ���齫�����ƶ����������������е��ƶ�״̬���磬��ǰ·����β����
	//             �µ��ƶ����β����ȫ��������״̬�£�ֻ�ᱻ��¼��
	//       false ����ָ����ƶ�״̬��
	void SetMoveSuspended(bool moveSuspended) { m_pSprite->SetMoveSuspended(moveSuspended); }
	// �Ƿ���ͣ״̬��
	bool IsMoveSuspended() const { return m_pSprite->IsMoveSuspended(); }

	void MoveTo(int targetX, int targetY, int range, const Nuclear::CSIZE * size) { m_pSprite->MoveTo(targetX, targetY, range, size) ;}
	void MoveTo(const Nuclear::Location & target, int range, const Nuclear::CSIZE * size) { this->MoveTo(target.x, target.y, range, size); }
	
	// ת��Ŀ���
	void SetDirection(int targetx, int targety) { m_pSprite->SetDirection(targetx, targety); }
	void SetDirection(const Nuclear::CPOINT & target) { SetDirection(target.x, target.y); }
};
