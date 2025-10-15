#ifndef __GAMESTATEMANAGER_H
#define __GAMESTATEMANAGER_H

#include "GameCommon.h"
#include "Singleton.hpp"
class GameStateManager : public CSingleton<GameStateManager>
{
private:
	eGameState	m_eGameState;
public:
	GameStateManager();
	~GameStateManager();

	void setGameState(eGameState state);
	eGameState getGameState();
	bool isGameState(eGameState state);
};

inline GameStateManager* gGetStateManager()
{
	return GameStateManager::GetInstance();
}
#endif
