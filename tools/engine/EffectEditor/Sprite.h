#pragma once
#include "..\engine\engine\enginebase.h"
#include "..\engine\sprite\spritemanager.h"

class Sprite
{
private:
	int	m_nBeginTick;									
	float m_fSlopeEffectAlpha;							
	float m_fMinEffectAlpha;							
	float m_fEffectAlpha;								
	unsigned char m_FlagEffectLayer;					
	float m_fCurFrame;									
	int m_nCurFrame;									
	DWORD m_nCurTime;									
	Nuclear::CPOINT m_Position;								
	Nuclear::XPDIRECTION m_Direction;						
	Nuclear::EngineBase *m_pEngine;
	std::vector<Nuclear::XPCOLOR> m_Colors;			
	std::vector<Nuclear::Component*> m_CurComponents;	
	std::wstring m_CurActionName;						
	const Nuclear::Action *m_pAction;					
	std::wstring m_ModelName;							
public:
	const Nuclear::CPOINT& GetPosition() const { return m_Position; }
	Nuclear::XPDIRECTION GetDirection() const { return m_Direction; }
	const std::wstring& GetModelName() const { return m_ModelName; }
	const std::wstring& GetActionName() const { return m_CurActionName; }
	std::wstring GetComponent(int sc);
	int GetTime() { return (m_pAction)?m_pAction->GetTime():0; }
	int GetFrameCount() { return (m_pAction)?m_pAction->GetFrameCount():0; }
	float GetFloatFrame() const { return m_fCurFrame;}
	int GetCurFrame() const { return m_nCurFrame; }
	void SetFrame(int frame);

	void Tick(DWORD deltaTime);
	void Render();

	void SetPosition(Nuclear::CPOINT pos) { m_Position = pos; }
	void SetDirection(Nuclear::XPDIRECTION dir) { m_Direction = dir; }
	void SetAction(const std::wstring& action_name);
	void SetComponent(int sc, const std::wstring& resource, Nuclear::XPCOLOR color);
	std::wstring GetModelName() { return m_ModelName; }

	bool SetModel(const std::wstring &modelname);
	virtual ~Sprite(void);

	Sprite(Nuclear::EngineBase *pEB);
};
