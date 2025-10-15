#pragma once
#include "..\engine\sprite\componentsprite.h"

class ToolsSoundSprite :
	public Nuclear::ComponentSprite
{
private:
	bool m_bIsPause;
public:
	ToolsSoundSprite(Nuclear::EngineBase *pEB, const std::wstring &modelname);
	virtual ~ToolsSoundSprite(void);

	void PlayAction(const std::wstring& action_name, const Nuclear::SpriteSoundParam& param);//����
	bool PlayActionSilence(const std::wstring& action_name);//����
	void Update();
	void Pause();
	void Play() { m_bIsPause = false; }
	bool IsPause() const { return m_bIsPause; }
};
