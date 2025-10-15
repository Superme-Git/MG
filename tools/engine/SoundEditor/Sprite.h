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
	
	//ISprite�Ľӿڵİ�װ
	void EnableStepSound(bool b) { m_pSprite->EnableStepSound(b); }
	bool IsStepSoundEnable() const { return m_pSprite->IsStepSoundEnable(); }
	// ����λ�á�location �����������ꡣ
	void SetLocation(const Nuclear::Location& location) { m_pSprite->SetLocation(location); }
	// �õ�λ��.
	Nuclear::Location GetLocation() const { return m_pSprite->GetLocation(); }

	// ���ú͵õ�����direction �������귽��
	void SetDirection( Nuclear::XPDIRECTION direction ) { m_pSprite->SetDirection(direction); }
	Nuclear::XPDIRECTION GetDirection() const { return m_pSprite->GetDirection(); }

	// �����Ƿ�ɼ�
	void SetVisible(bool v) { m_pSprite->SetVisible(v); }
	bool IsVisiable() const { return m_pSprite->IsVisiable(); }

	// ���þ���ģ��
	void SetModel(const std::wstring &modelname) { m_pSprite->SetModel(modelname); }

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
		m_pSprite->SetDefaultAction(action_name, Nuclear::XPSALT_SYNC, freq, fHoldLastFrame);
	}
	std::wstring GetDefaultAction() { return m_pSprite->GetDefaultAction(); }
	// ��ʱ������ÿ��������0~1���� ��ʱ�����������֮���Զ��л�Ĭ�϶���, action_name �����ַ���ȡ����ʱ����
	void PlayAction(const std::wstring& action_name, float freq=1.0f) { m_pSprite->PlayAction(action_name, Nuclear::XPSALT_SYNC, freq); }
	std::wstring GetCurActionName() { return m_pSprite->GetCurActionName(); }
	// ��ǰ�����Ĳ�����֡����������Render��ʱ����£���RenderǰΪ��һ֡�����ݣ�Render��Ϊ��֡�����ݣ�
	int GetCurrentFrame() const { return m_pSprite->GetCurrentFrame(); }
	// ��ǰ��������֡��
	int GetTotalFrame() const { return m_pSprite->GetTotalFrame(); }

	// ���ھ����ϲ��ŵ�Ч��

	//���ð��ļ��������ó�L""��ʱ��Ϊȡ��
	bool SetBindFile(const std::wstring &bindFile) { return m_pSprite->SetBindFile(0, bindFile); }
	std::wstring GetBindFile() { return m_pSprite->GetBindFile(0); }
	// ���ú�ȡ������Ч������Ҫ�ֹ�ȡ��������������
	// bindType��������󶨵�
	// 0 �̶��󶨵㣺dx��dy����Ծ���ԭ�㡣
	// 1 �����󶨵㣺���涯���仯���ı䣨ÿһ֡������ʱ����dx��dy��
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

	// ����˲���ϲ�Ӱ
	void TeleportWithBlur(const Nuclear::Location &point) { m_pSprite->TeleportWithBlur(point, 500, 300); }

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

	// ���þ���ɴ�ͼ���롣Ϊ0�������ϰ���
	virtual void SetMoveMask(unsigned int mask) { m_pSprite->SetMoveMask(mask); };
};
