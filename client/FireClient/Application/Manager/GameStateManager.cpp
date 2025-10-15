#include "stdafx.h"
#include "GameStateManager.h"


GameStateManager::GameStateManager()
: m_eGameState(eGameStateNull)
{ }

GameStateManager::~GameStateManager()
{ }

void GameStateManager::setGameState(eGameState state)
{
	m_eGameState = state;
}

eGameState GameStateManager::getGameState()
{
	return m_eGameState;
}

bool GameStateManager::isGameState(eGameState state)
{
	return m_eGameState == state;
}
