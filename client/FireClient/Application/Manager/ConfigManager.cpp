#include "stdafx.h"
#include "ConfigManager.h"
#include "GameTable/SysConfig/CGameconfig.h"
#include "../Utils/IniManager.h"
#include "utils/StringCover.h"
#include "SimpleAudioEngine.h"

#include "GameTable/common/CCommon.h"
#include "BattleManager.h"
#include "../Framework/GameScene.h"
#include "SceneMovieManager.h"
#include "MusicSoundVolumeMixer.h"
#include "engine/nuengine.h"


bool GameConfigManager::s_PlayBackMusicBootState = true;

GameConfigManager::GameConfigManager()
	: m_bStepLoadTextureInMovie(false)
	, m_bStepLoadTextureAlways(false)
{
    LoadDefaultConfig();
    LoadConfig();
    ApplyConfig();
}

GameConfigManager::~GameConfigManager()
{

}


void GameConfigManager::sdkStopMusic(){
	CocosDenshion::SimpleAudioEngine::sharedEngine()->stopBackgroundMusic(true);
}

bool GameConfigManager::isPlayEffect()
{
	int value = GetConfigValue(L"soundeffect");
	return value == 0 ? false : true;
}

bool GameConfigManager::sIsPlayBackMusicBoot()
{
	std::string strIniFileName("SystemSetting.ini");
	IniManager IniMgr(strIniFileName);

	std::wstring strKey = L"sound";
	std::wstring strValue;
	if (IniMgr.GetValueByName(L"GameConfig", strKey, strValue))
	{
		return StringCover::WStrToNum<int>(strValue) > 0;
	}

	return false;
}

bool GameConfigManager::isPlayBackMusic()
{
	int value = GetConfigValue(L"sound");
	return value == 0 ? false : true;
}

void GameConfigManager::SaveConfig()
{
	std::string strIniFileName("SystemSetting.ini");
	IniManager IniMgr(strIniFileName);

	std::map<std::wstring, int>::iterator it = m_MapConfig.begin();
	for (; it != m_MapConfig.end(); ++it)
	{
		std::wstring strKey = it->first;
		std::wstring strValue = StringCover::NumTowstring<int>(it->second);

		if (strKey != L"")
		{
			IniMgr.WriteValueByName(L"GameConfig", strKey, strValue);
		}
	}
}

void GameConfigManager::SetConfigValue(std::wstring& key, int value)
{
	m_MapConfig[key] = value;
}

int GameConfigManager::GetConfigValue(const std::wstring& key)
{
	std::map<std::wstring, int>::iterator it = m_MapConfig.find(key);
	if (it != m_MapConfig.end())
	{
		return it->second;
	}

	return -1;
}

void GameConfigManager::LoadConfig()
{
	std::string strIniFileName("SystemSetting.ini");
	IniManager IniMgr(strIniFileName);

	std::map<std::wstring, int>::iterator it = m_MapDefaultConfig.begin();
	for (; it != m_MapDefaultConfig.end(); ++it)
	{
		std::wstring strKey = it->first;
		std::wstring strValue;
		if (strKey != L"" && IniMgr.GetValueByName(L"GameConfig", strKey, strValue))
		{
			int iValue = StringCover::WStrToNum<int>(strValue);
			m_MapConfig[strKey] = iValue;
		}
	}
}

void GameConfigManager::LoadDefaultConfig()
{
	std::vector<int> allIDs;
	GameTable::SysConfig::GetCGameconfigTableInstance().getAllID(allIDs);
	for (std::vector<int>::iterator it = allIDs.begin(); it != allIDs.end(); ++it)
	{
		int id = *it;
		const GameTable::SysConfig::CGameconfig& record = GameTable::SysConfig::GetCGameconfigTableInstance().getRecorder(id);
		if (record.id != -1)
		{
			std::wstring strKey = record.key;

			int value = record.value;

			m_MapDefaultConfig[strKey] = value;
			m_MapConfig[strKey] = value;
		}
	}
}

void GameConfigManager::SetFromDefaultConfig()
{
	m_MapConfig.clear();
	std::map<std::wstring, int>::iterator it = m_MapDefaultConfig.begin();
	for (; it != m_MapDefaultConfig.end(); ++it)
	{
		m_MapConfig[it->first] = it->second;
	}
}

void GameConfigManager::ApplyConfig()
{
    int BackMusic=GetConfigValue(L"sound");
    int BackMusicValue=GetConfigValue(L"soundvalue");
    int EffectMusic=GetConfigValue(L"soundeffect");
	int showFps = GetConfigValue(L"screenfresh");
//#if CC_TARGET_PLATFORM != CC_PLATFORM_WIN32
	int fpsNumber = 45;
	if (showFps == 0)
	{
		fpsNumber = GetTableNumberFromLua("SysConfig.cfpssetting", 1, "lessvalue");
	}
	else
	{
		fpsNumber = GetTableNumberFromLua("SysConfig.cfpssetting", 1, "morevalue");
	}
	gSetMaxFps(fpsNumber);
//#endif	
    
	float effectAdjust = StringCover::WStrToNum<float>(GameTable::common::GetCCommonTableInstance().getRecorder(204).value);

	if (MusicSoundVolumeMixer::GetInstance())
	{
		MusicSoundVolumeMixer::GetInstance()->setBackgroundMusicVolume((float)(BackMusicValue / 255.0f));
	}
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectsVolume((float)(BackMusicValue / 255.0f * effectAdjust));
    
    if (BackMusic==0)
	{
        CocosDenshion::SimpleAudioEngine::sharedEngine()->stopBackgroundMusic(true);
    }
	else if(!CocosDenshion::SimpleAudioEngine::sharedEngine()->isBackgroundMusicPlaying())
    {
        if (GetBattleManager()&&GetBattleManager()->IsInBattle())
		{
            GetBattleManager()->PlayBattleSound();
        }
		else
        {
			if (gGetSceneMovieManager() && gGetSceneMovieManager()->isOnSceneMovie())
			{
				CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
			}
			else
			{
				if (gGetScene())
				{
					gGetScene()->PlayMapBackGroundMusic();
				}
			}
            
        }
    }
    
    if (EffectMusic==0)
	{
        CocosDenshion::SimpleAudioEngine::sharedEngine()->stopAllEffects();
    }
	else
    {
        CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeAllEffects();
    }

	Nuclear::Engine::GetInstance().GetSystemResourceManager()->SetSoundInited(EffectMusic != 0);
}
