#ifndef __CONFIGMANAGER_H
#define __CONFIGMANAGER_H

#include <map>
#include <string>
#include "Singleton.hpp"
class GameConfigManager : public CSingleton<GameConfigManager>
{
public:
	GameConfigManager();
	~GameConfigManager();

	static void sSetPlayBackMusicBootState(bool flag) { s_PlayBackMusicBootState = flag; }
	static bool sGetPlayBackMusicBootState() { return s_PlayBackMusicBootState; }
	static bool sIsPlayBackMusicBoot();

	void sdkStopMusic();

	bool isPlayEffect();
	bool isPlayBackMusic();

	void SaveConfig();
	void SetConfigValue(std::wstring& key, int value);
	int GetConfigValue(const std::wstring& key);

	void SetFromDefaultConfig();
	void ApplyConfig();

	bool GetStepLoadTextureInMovie() const { return m_bStepLoadTextureInMovie; }
	void SetStepLoadTextureInMovie(bool b) { m_bStepLoadTextureInMovie = b; }
	bool GetStepLoadTextureAlways() const { return m_bStepLoadTextureAlways; }
	void SetStepLoadTextureAlways(bool b) { m_bStepLoadTextureAlways = b; }

	const std::map<std::wstring, int>& GetMapConfig() { return m_MapConfig; }

private:
	void LoadConfig();
	void LoadDefaultConfig();
    
	static bool s_PlayBackMusicBootState;
	std::map<std::wstring, int> m_MapConfig;
    std::map<std::wstring,int> m_MapDefaultConfig;

	bool  m_bStepLoadTextureInMovie;
	bool  m_bStepLoadTextureAlways;
};

inline GameConfigManager* gGetGameConfigManager()
{
	return GameConfigManager::GetInstance();
}

#endif
