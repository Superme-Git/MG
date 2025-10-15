#pragma once
#include "..\engine\sprite\componentsprite.h"
#include "..\engine\iapp.h"


class CMapEditorApplicationDelegate : public Nuclear::IApp
{
public:
	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type);
	virtual void OnRenderInit(int now, int step, int totalstep) {};
	virtual void OnRenderUI(int now) {}
	virtual bool OnBeforeRender(int now/*ms*/) { return true; }
	virtual void OnTick(int now, int delta) {}
	virtual bool OnExit() { return true; }
	virtual bool OnInit(int step) { return true; }
};

class Sprite : public Nuclear::ComponentSprite
{
private:
	std::wstring m_Stand;
	std::wstring m_RunRight;
	std::wstring m_RunLeft;
	int m_AniState;	

public:
	void RunTo(int x, int y);
	void PlayAction(const std::wstring &action_name) { Nuclear::ComponentSprite::PlayAction(action_name, Nuclear::XPSALT_SYNC, 1.0f); }
	const std::wstring& GetStand() const { return m_Stand; }

	const std::wstring& GetRunRight() const { return m_RunRight; }
	const std::wstring& GetRunLeft() const { return m_RunLeft; }
	void SetStand(const std::wstring &name) { m_Stand = name; }
	void SetRunRight(const std::wstring &name) { m_RunRight = name; }
	void SetRunLeft(const std::wstring &name) { m_RunLeft = name; }
	int GetState() const { return m_AniState; }
	void SwitchToRun();
	void UpdateAnimation(Nuclear::XPUSA_TYPE type);

	~Sprite(void);
	Sprite(Nuclear::EngineBase *pEB, const std::wstring &modelname);
};


