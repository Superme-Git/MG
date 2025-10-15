#pragma once
#include "engine\isprite.h"

class Sprite
{
private:
	
	int m_actState;	//0 stand1 1 runleft 2 runright 3 ������
	bool m_isTel;

public:
	Nuclear::ISprite *m_pSprite;
	Sprite(Nuclear::ISprite *pSprite);
	~Sprite(void);

	void UpdateAction(Nuclear::XPUSA_TYPE type);
	void GoToRun();
	int GetState() const { return m_actState; }
	
	//ISprite�Ľӿڵİ�װ
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

	// ͷ�����֣�slot [0,1,2] ���ϵ���
	//void SetTitle(int slot, const std::wstring &title, int fontType, Nuclear::XPCOLOR color) { m_pSprite->SetTitle(slot, title, fontType, color); }
	//void UnsetTitle(int slot) { m_pSprite->UnsetTitle(slot); }

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
		m_pSprite->SetDefaultAction(action_name, Nuclear::XPSALT_SYNC, freq,fHoldLastFrame);
	}
	std::wstring GetDefaultAction() { return m_pSprite->GetDefaultAction(); }
	// ��ʱ������ÿ��������0~1���� ��ʱ�����������֮���Զ��л�Ĭ�϶���, action_name �����ַ���ȡ����ʱ����
	void PlayAction(const std::wstring& action_name, float freq=1.0f) { 
		m_pSprite->PlayAction(action_name, Nuclear::XPSALT_SYNC, freq); 
	}
	//std::wstring PlayAction() { return m_pSprite->PlayAction(); }
	// ��ǰ�����Ĳ�����֡����������Render��ʱ����£���RenderǰΪ��һ֡�����ݣ�Render��Ϊ��֡�����ݣ�
	int GetCurrentFrame() const { return m_pSprite->GetCurrentFrame(); }
	// ��ǰ��������֡��
	int GetTotalFrame() const { return m_pSprite->GetTotalFrame(); }

	// ���ھ����ϲ��ŵ�Ч��

	//���ð��ļ��������ó�L""��ʱ��Ϊȡ��
	bool SetBindFile(const std::wstring &bindFile) { return m_pSprite->SetBindFile(0,bindFile); }
	std::wstring GetBindFile() { return m_pSprite->GetBindFile(0); }
	// ���ú�ȡ������Ч������Ҫ�ֹ�ȡ��������������
	// bindType��������󶨵�
	// 0 �̶��󶨵㣺dx��dy����Ծ���ԭ�㡣
	// 1 �����󶨵㣺���涯���仯���ı䣨ÿһ֡������ʱ����dx��dy��
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

	// ��ʱЧ������������Զ�ȡ��������������, times�ǲ��Ŵ���
	void PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, bool selfRef)
	{
//		m_pSprite->PlayEffect(effect_name, dx, dy, times, selfRef);
	}

	//
	void EnableShadow(bool b) { m_pSprite->EnableShadow(b); }
	bool IsEnableShadow() { return m_pSprite->IsEnableShadow();	}

	// ����˲���ϲ�Ӱ
//	void TeleportWithBlur(const Nuclear::Location &point) { m_pSprite->TeleportWithBlur(point); m_isTel = true;}

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
	void SetDirection(int dir) { m_pSprite->SetDirection((Nuclear::XPDIRECTION)dir); }

};
