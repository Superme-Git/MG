#pragma once
#include "..\engine\effect\sebind.h"
#include "sprite.h"

class CToolsSebind :
	public Nuclear::Sebind
{
private:
	Nuclear::Effect* m_pEffect;

private:
	const CToolsSebind& operator=( const CToolsSebind& src);
	CToolsSebind( const CToolsSebind& );

public:
	Nuclear::Effect* GetEffect() const { return m_pEffect; }
	void SetEffect(Nuclear::Effect* pEffect)
	{
		if (m_pEffect)
			delete m_pEffect;
		m_pEffect = pEffect;
	}

	bool Tick(Sprite* pSprite, DWORD tickTime)
	{
		if (m_pEffect)
		{
			XPASSERT(pSprite);
			return Sebind::Tick(pSprite->GetDirection(), pSprite->GetActionName(), static_cast<float>(pSprite->GetCurFrame()), 
				pSprite->GetPosition(), m_pEffect, tickTime);
		}
		return false;
	}
	void ClearAll();
	bool Save(const wchar_t* lpszPathName, int flag = 0);
	void Save(XMLIO::CONode& root, int flag = 0);
	bool Load( XMLIO::CINode& root, int flag = 0) { return Sebind::Load(root, flag); }
	void ReleaseAll();
	Nuclear::EffectClip* AddClip(Nuclear::XPDIRECTION dir, const std::wstring& action, int frameCount);
	virtual ~CToolsSebind(void);

	CToolsSebind(void);
};
