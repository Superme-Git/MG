#include "xpenvsoundarea.h"
#include "nulog.h"
#include "../CocosDenshion/include/SimpleAudioEngine.h"
#include "ljfmfex.h"
#include "ljfsfile.h"
#include "ljfszipfile.h"
#include "StringCover.h"

namespace Nuclear
{

	XPEnvSoundArea::XPEnvSoundArea(EngineBase *pEB) : m_pEB(pEB), m_lastSetVolumeTick(0),
		m_nTransition(0)
	{
	}

	XPEnvSoundArea::~XPEnvSoundArea(void)
	{
		if (m_pEB)
		{
			SoundVet::iterator it = m_sound.begin(), ie = m_sound.end();
			int i = 0;
			for (; it != ie; ++it, ++i)
			{
				SoundTypeParam& type = *it;
				if (type.handle != INVALID_SOUND_HANDLE)
				{
					//pDSM->SetNotify(type.handle, NULL);
					CocosDenshion::SimpleAudioEngine::sharedEngine()->stopEffect(type.handle);
					type.handle = INVALID_SOUND_HANDLE;
				}
			}
			m_sound.clear();
		}

	}

	void XPEnvSoundArea::LoadSound(LJXML::LJXML_Node<>& node)
	{
		m_sound.clear();
		LJXML::LJXML_NodeList typeNodeList;
		node.GetChildren(typeNodeList);
		SoundTypeParam tmpType;
		SoundParam tmpParam;
		LJXML::LJXML_NodeList::iterator tIt = typeNodeList.begin(), tIe = typeNodeList.end();
		int t = 0;
		unsigned char i;
		for (; tIt != tIe; ++tIt)
		{
			LJXML::LJXML_Node<>& tnode = **tIt;
			if (tnode.GetType() != rapidxml::node_element)
				continue;
			XPASSERT(tnode.GetName() == L"type");
			m_sound.push_back(tmpType);
			SoundTypeParam &type = m_sound[t];
			type.nMinInterval = tnode.GetIntAttribute(L"minInt", 0);
			type.nMaxInterval = tnode.GetIntAttribute(L"maxInt", 0);
			type.priority = static_cast<short>(tnode.GetIntAttribute(L"priority", 0));
			LJXML::LJXML_NodeList sNodeList;
			tnode.GetChildren(sNodeList);
			LJXML::LJXML_NodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
			i = 0;
			for (; nlIt != nlIe; ++nlIt)
			{
				LJXML::LJXML_Node<>& snode = **nlIt;
				if (snode.GetType() != rapidxml::node_element)
					continue;
				XPASSERT(snode.GetName() == L"sound");
				type.sounds.push_back(tmpParam);
				SoundParam &param = type.sounds[i];
				snode.GetAttribute(L"url", param.soundName);
				param.minVol = static_cast<unsigned char>(snode.GetAttributeInteger(L"minvol", 0));
				param.maxVol = static_cast<unsigned char>(snode.GetAttributeInteger(L"maxvol", 0));
				param.minFreq = static_cast<unsigned char>(snode.GetAttributeInteger(L"minfreq", 0));
				param.maxFreq = static_cast<unsigned char>(snode.GetAttributeInteger(L"maxfreq", 0));
				param.stype = snode.GetAttributeInteger(L"type", 1);
				param.theTime[XPEST_BEGIN_TIME] = snode.GetAttributeInteger(L"BT", 0);
				param.theTime[XPEST_BEGIN_NORMAL_TIME] = snode.GetAttributeInteger(L"BNT", param.theTime[XPEST_BEGIN_TIME]);
				param.theTime[XPEST_END_TIME] = snode.GetAttributeInteger(L"ET", XPSA_DAY);
				param.theTime[XPEST_FADE_OUT_TIME] = snode.GetAttributeInteger(L"FOT", param.theTime[XPEST_END_TIME]);
				param.isLooping = (snode.GetAttributeInteger(L"looping") == 1);
				++i;
			}
			++t;
		}
	}

	void XPEnvSoundArea::SaveSound(LJXML::LJXML_Node<>& node)
	{
	}

	void XPEnvSoundArea::OnSoundPlayEnd(SoundHandle handle)
	{
	}

	inline float GetFadeVol(int time, int *theTimes)
	{
		if (theTimes[XPEST_BEGIN_TIME] <= theTimes[XPEST_END_TIME])
		{
			if (time >= theTimes[XPEST_END_TIME] || time < theTimes[XPEST_BEGIN_TIME])
				return 0.0f;
			if (time < theTimes[XPEST_BEGIN_NORMAL_TIME])
				return XPNORMALIZE(theTimes[XPEST_BEGIN_TIME], theTimes[XPEST_BEGIN_NORMAL_TIME], time);
			if (time > theTimes[XPEST_FADE_OUT_TIME])
				return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME], time);
		}
		else {
			if (time < theTimes[XPEST_BEGIN_TIME] && time >= theTimes[XPEST_END_TIME])
				return 0.0f;
			if (theTimes[XPEST_BEGIN_NORMAL_TIME] < theTimes[XPEST_BEGIN_TIME])
			{
				if (time < theTimes[XPEST_BEGIN_NORMAL_TIME])
					return XPNORMALIZE(theTimes[XPEST_BEGIN_TIME], theTimes[XPEST_BEGIN_NORMAL_TIME] + XPSA_DAY, time + XPSA_DAY);
				if (time >= theTimes[XPEST_BEGIN_TIME])
					return XPNORMALIZE(theTimes[XPEST_BEGIN_TIME], theTimes[XPEST_BEGIN_NORMAL_TIME] + XPSA_DAY, time);
				if (time > theTimes[XPEST_FADE_OUT_TIME])
					return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME], time);
			}
			else {
				if (time < theTimes[XPEST_BEGIN_NORMAL_TIME] && time > theTimes[XPEST_BEGIN_TIME])
					return XPNORMALIZE(theTimes[XPEST_BEGIN_TIME], theTimes[XPEST_BEGIN_NORMAL_TIME], time);
				if (theTimes[XPEST_END_TIME] < theTimes[XPEST_FADE_OUT_TIME])
				{
					if (time < theTimes[XPEST_END_TIME])
						return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME] + XPSA_DAY, time + XPSA_DAY);
					if (time > theTimes[XPEST_FADE_OUT_TIME])
						return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME] + XPSA_DAY, time);
				}
				else {
					if (time > theTimes[XPEST_FADE_OUT_TIME] && time <= theTimes[XPEST_END_TIME])
						return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME], time);
				}
			}
		}
		return 1.0f;
	}

	bool XPEnvSoundArea::Update(const NuclearPoint& pos)
	{
		XPASSERT(m_pEB);
		float vol = 0.0f;
		bool bIsInitVol = false;
		bool setVolume = (m_pEB->GetTick() - m_lastSetVolumeTick >= 100);//100毫秒检测一次
		int playingSoundNum = 0;
		if (setVolume)
			m_lastSetVolumeTick = m_pEB->GetTick();

		int time = m_pEB->GetGameTime();

		CocosDenshion::SimpleAudioEngine* pAudioEngine = CocosDenshion::SimpleAudioEngine::sharedEngine();

		for (SoundVet::iterator it = m_sound.begin(); it != m_sound.end(); ++it)
		{
			SoundTypeParam& effectSoundInfo = *it;

			if (effectSoundInfo.handle != INVALID_SOUND_HANDLE)
			{
				if (!pAudioEngine->hasEffect(effectSoundInfo.handle) || !pAudioEngine->isEffectPlaying(effectSoundInfo.handle))
				{
					pAudioEngine->stopEffect(effectSoundInfo.handle);
					pAudioEngine->setCurEffectPriority(-1);

					effectSoundInfo.nLastPlayEndTick = m_pEB->GetTick();
					effectSoundInfo.handle = INVALID_SOUND_HANDLE;
					effectSoundInfo.nTmpInterval = RangeRand(effectSoundInfo.nMinInterval, effectSoundInfo.nMaxInterval);
				}
			}

			if (effectSoundInfo.handle == INVALID_SOUND_HANDLE)
			{
				unsigned int curTick = m_pEB->GetTick();
				if (curTick - effectSoundInfo.nLastPlayEndTick >= effectSoundInfo.nTmpInterval)
				{
					if (!pAudioEngine->testPriority(effectSoundInfo.priority))	//测试优先级
					{
						continue;
					}

					if (!bIsInitVol)
					{
						vol = GetAreaVolume(pos);
						bIsInitVol = true;
					}
					if (vol <= 0.001f)
					{
						continue;
					}
					float fadeVol = 1.0f;

					unsigned char id = 0;
					size_t size = 0;
					SoundParamIDVet tmpVet;
					SoundParamVet::iterator pIt = effectSoundInfo.sounds.begin(), pIe = effectSoundInfo.sounds.end();
					for (; pIt != pIe; ++pIt, ++id)
					{
						SoundParam &param = *pIt;
						switch (param.stype)
						{
						case 1:	//第一种，任何时间都可以
							tmpVet.push_back(id);
							break;
						case 2: //第二种
							if (param.theTime[XPEST_BEGIN_TIME] <= param.theTime[XPEST_END_TIME])
							{
								if (time >= param.theTime[XPEST_BEGIN_TIME] && time <= param.theTime[XPEST_END_TIME])
									tmpVet.push_back(id);
							}
							else {
								if (time >= param.theTime[XPEST_BEGIN_TIME] || time <= param.theTime[XPEST_END_TIME])
									tmpVet.push_back(id);
							}
							break;
						case 3:	//第三种
							if (param.theTime[XPEST_BEGIN_TIME] <= param.theTime[XPEST_END_TIME])
							{
								if (time >= param.theTime[XPEST_BEGIN_TIME] && time <= param.theTime[XPEST_END_TIME])
								{
									tmpVet.push_back(id);
									fadeVol = GetFadeVol(time, param.theTime);
								}
							}
							else {
								if (time >= param.theTime[XPEST_BEGIN_TIME] || time <= param.theTime[XPEST_END_TIME])
								{
									tmpVet.push_back(id);
									fadeVol = GetFadeVol(time, param.theTime);
								}
							}
							break;
						default:
							XPASSERT(false && L"pIt->stype wrong!");
						}
					}
					size = tmpVet.size();
					if (size == 0)
						continue;
					effectSoundInfo.currentID = tmpVet[RangeRand((size_t)0, size - 1)];
					SoundParam &param = effectSoundInfo.sounds[effectSoundInfo.currentID];
					unsigned char volume;
					effectSoundInfo.currentInitVolume = RangeRand((int)param.minVol, (int)param.maxVol);
					volume = static_cast<unsigned char>(vol * fadeVol * effectSoundInfo.currentInitVolume);
					//type.handle = pDSM->XPPlaySound(param.soundName, type.priority, 
					//	m_pEB->GetConfigManager()->GetEnvSoundType(), 
					//	volume, RangeRand(param.minFreq, param.maxFreq), param.isLooping);
					//XPTRACE(L"playStaticSound, %s, %d, %d, %d===================================================\n", param.soundName.c_str(), volume, freq, looping);

					std::string soundFilename;
					std::wstring newws;
					newws = LJFM::LJFMF::GetFullPathFileName(param.soundName);
					soundFilename = StringCover::to_string(newws);

					effectSoundInfo.handle = pAudioEngine->playEffect(soundFilename.c_str(), param.isLooping);
					pAudioEngine->setCurEffectPriority(effectSoundInfo.priority);

					if (effectSoundInfo.handle != INVALID_SOUND_HANDLE)
					{
						//pDSM->SetNotify(type.handle, this);
						float fVolume = volume / 255.f;
						pAudioEngine->setEffectVolume(effectSoundInfo.handle, fVolume);
						
						++playingSoundNum;
					}
				}
			}
			else
			{
				++playingSoundNum;
				if (setVolume)
				{
					if (!bIsInitVol)
					{
						vol = GetAreaVolume(pos);
						bIsInitVol = true;
					}
					if (vol < 0.01f)
					{
						--playingSoundNum;
						//pDSM->StopSound(type.handle);
						pAudioEngine->stopEffect(effectSoundInfo.handle);
						pAudioEngine->setCurEffectPriority(-1);
						effectSoundInfo.handle = INVALID_SOUND_HANDLE;
					}
					else {
						float fadeVol = 1.0f;
						SoundParam &param = effectSoundInfo.sounds[effectSoundInfo.currentID];
						if (param.stype == 3)
						{
							fadeVol = GetFadeVol(time, param.theTime);
							if (fadeVol < 0.001f)
							{
								--playingSoundNum;
								//pDSM->StopSound(type.handle);
								pAudioEngine->stopEffect(effectSoundInfo.handle);
								pAudioEngine->setCurEffectPriority(-1);
								effectSoundInfo.handle = INVALID_SOUND_HANDLE;
							}
						}
						//pDSM->SetSoundVolume(type.handle, 
						//	static_cast<unsigned char>(vol * fadeVol * type.currentInitVolume));
						float fVol = vol * fadeVol * effectSoundInfo.currentInitVolume / 255.f;
						pAudioEngine->setEffectVolume(effectSoundInfo.handle, fVol);
					}

				}
			}
		}
		return playingSoundNum > 0;
	}

	void XPEnvSoundArea::GetSoundList(int typeID, std::vector<std::wstring> &slist) const
	{
		slist.clear();
		const SoundTypeParam &type = m_sound[typeID];
		slist.reserve(type.sounds.size());
		SoundParamVet::const_iterator it = type.sounds.begin(), ie = type.sounds.end();
		for (; it != ie; ++it)
		{
			slist.push_back(it->soundName);
		}
	}

	int XPEnvSoundArea::AddSound(int typeID, const std::wstring &name)
	{
		SoundTypeParam &type = m_sound[typeID];
		SoundParam tmpParam;
		tmpParam.maxFreq = 128;
		tmpParam.minFreq = 128;
		tmpParam.maxVol = 255;
		tmpParam.minVol = 255;
		tmpParam.soundName = name;
		tmpParam.stype = 1;
		tmpParam.isLooping = false;
		tmpParam.theTime[0] = 0;
		tmpParam.theTime[1] = 0;
		tmpParam.theTime[2] = XPSA_DAY;
		tmpParam.theTime[3] = XPSA_DAY;
		type.sounds.push_back(tmpParam);
		return static_cast<int>(type.sounds.size() - 1);
	}

	void XPEnvSoundArea::InsertSound(int typeID, int soundID, const SoundParam &param)
	{
		SoundTypeParam &type = m_sound[typeID];
		XPASSERT(soundID <= (int)type.sounds.size());
		SoundParamVet::iterator it = type.sounds.begin() + soundID;
		type.sounds.insert(it, param);
	}

	void XPEnvSoundArea::InsertSoundType(int typeID, const SoundTypeParam &param)
	{
		XPASSERT(typeID <= (int)m_sound.size());
		SoundVet::iterator it = m_sound.begin() + typeID;
		m_sound.insert(it, param);
	}

}
