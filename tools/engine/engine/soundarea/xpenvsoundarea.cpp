#include "stdafx.h"
#include "..\common\log.h"
#include "..\engine\configmanager.h"
#include "xpenvsoundarea.h"


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
			DirectSoundManager* pDSM = m_pEB->GetSoundManager();
			SoundVet::iterator it = m_sound.begin(), ie = m_sound.end();
			int i = 0;
			for (;it!=ie;++it,++i)
			{
				SoundTypeParam& type = *it;
				if (type.handle != INVALID_SOUND_HANDLE)
				{
					pDSM->SetNotify(type.handle, NULL);
				}
			}
		}
		
	}

	void XPEnvSoundArea::LoadSound(XMLIO::CINode& node)
	{
		m_sound.clear();
		XMLIO::CNodeList typeNodeList;
		node.GetChildren(typeNodeList);
		SoundTypeParam tmpType;
		SoundParam tmpParam;
		XMLIO::CNodeList::iterator tIt = typeNodeList.begin(), tIe = typeNodeList.end();
		int t = 0;
		unsigned char i;
		for (;tIt!=tIe;++tIt)
		{
			XMLIO::CINode& tnode = *tIt;
			if (tnode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			XPASSERT(tnode.GetName()==L"type");
			m_sound.push_back(tmpType);
			SoundTypeParam &type = m_sound[t];
			type.nMinInterval = tnode.GetIntAttribute(L"minInt", 0);
			type.nMaxInterval = tnode.GetIntAttribute(L"maxInt", 0);
			type.priority = static_cast<short>(tnode.GetIntAttribute(L"priority", 0));
			XMLIO::CNodeList sNodeList;
			tnode.GetChildren(sNodeList);
			XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
			i = 0;
			for (;nlIt!=nlIe;++nlIt)
			{
				XMLIO::CINode& snode = *nlIt;
				if (snode.GetType() != XMLIO::NT_ELEMENT)
					continue;
				XPASSERT(snode.GetName()==L"sound");
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
				param.theTime[XPEST_END_TIME] = snode.GetAttributeInteger(L"ET", XP_A_DAY_TIME);
				param.theTime[XPEST_FADE_OUT_TIME] = snode.GetAttributeInteger(L"FOT", param.theTime[XPEST_END_TIME]);
				param.isLooping = (snode.GetAttributeInteger(L"looping") == 1);
				++i;
			}
			++t;
		}
	}

	void XPEnvSoundArea::SaveSound(XMLIO::CONode& node)
	{
		SoundVet::iterator tIt = m_sound.begin(), tIe = m_sound.end();
		for (;tIt!=tIe;++tIt)
		{
			XMLIO::CONode tnode;
			node.CreateSubElement(L"type", tnode);
			tnode.SetAttribute(L"minInt", tIt->nMinInterval);
			tnode.SetAttribute(L"maxInt", tIt->nMaxInterval);
			tnode.SetAttribute(L"priority", tIt->priority);
			SoundParamVet::iterator sIt = tIt->sounds.begin(), sIe = tIt->sounds.end();
			for (;sIt!=sIe;++sIt)
			{
				XMLIO::CONode snode;
				tnode.CreateSubElement(L"sound", snode);
				snode.SetAttribute(L"url", sIt->soundName);
				snode.SetAttribute(L"minvol", sIt->minVol);
				snode.SetAttribute(L"maxvol", sIt->maxVol);
				snode.SetAttribute(L"minfreq", sIt->minFreq);
				snode.SetAttribute(L"maxfreq", sIt->maxFreq);
				snode.SetAttribute(L"type", sIt->stype);
				switch (sIt->stype)
				{
				case 3:
					snode.SetAttribute(L"BNT", sIt->theTime[XPEST_BEGIN_NORMAL_TIME]);
					snode.SetAttribute(L"FOT", sIt->theTime[XPEST_FADE_OUT_TIME]);
					//没有break
				case 2:
					snode.SetAttribute(L"BT", sIt->theTime[XPEST_BEGIN_TIME]);
					snode.SetAttribute(L"ET", sIt->theTime[XPEST_END_TIME]);
					break;
				case 1:
					break;
				default:
					XPASSERT(FALSE && L"sIt->stype value wrong!!!");
					break;
				}
				snode.SetAttribute(L"looping", sIt->isLooping?1:0);
			}
		}
	}

	void XPEnvSoundArea::OnSoundPlayEnd(SoundHandle handle)
	{
		XPASSERT(m_pEB);
		SoundHandleMap::iterator it = m_handleMap.find(handle);
		if (it != m_handleMap.end())
		{
			SoundTypeParam& type = m_sound[it->second];
			type.nLastPlayEndTick = m_pEB->GetTick();
			type.handle = INVALID_SOUND_HANDLE;
			type.nTmpInterval = RangeRand(type.nMinInterval, type.nMaxInterval);
			m_handleMap.erase(it);
		} else {
			XPLOG_ERROR(L"it == m_handleMap.end()\n");
		}
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
		} else {
			if (time < theTimes[XPEST_BEGIN_TIME] && time >= theTimes[XPEST_END_TIME])
				return 0.0f;
			if (theTimes[XPEST_BEGIN_NORMAL_TIME] < theTimes[XPEST_BEGIN_TIME])
			{
				if (time < theTimes[XPEST_BEGIN_NORMAL_TIME])
					return XPNORMALIZE(theTimes[XPEST_BEGIN_TIME], theTimes[XPEST_BEGIN_NORMAL_TIME] + XP_A_DAY_TIME, time + XP_A_DAY_TIME);
				if (time >= theTimes[XPEST_BEGIN_TIME])
					return XPNORMALIZE(theTimes[XPEST_BEGIN_TIME], theTimes[XPEST_BEGIN_NORMAL_TIME] + XP_A_DAY_TIME, time);
				if (time > theTimes[XPEST_FADE_OUT_TIME])
					return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME], time);
			} else {
				if (time < theTimes[XPEST_BEGIN_NORMAL_TIME] && time > theTimes[XPEST_BEGIN_TIME])
					return XPNORMALIZE(theTimes[XPEST_BEGIN_TIME], theTimes[XPEST_BEGIN_NORMAL_TIME], time);
				if (theTimes[XPEST_END_TIME] < theTimes[XPEST_FADE_OUT_TIME])
				{
					if (time < theTimes[XPEST_END_TIME])
						return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME] + XP_A_DAY_TIME, time + XP_A_DAY_TIME);
					if (time > theTimes[XPEST_FADE_OUT_TIME])
						return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME] + XP_A_DAY_TIME, time);
				} else {
					if (time > theTimes[XPEST_FADE_OUT_TIME] && time <= theTimes[XPEST_END_TIME])
						return 1.0f - XPNORMALIZE(theTimes[XPEST_FADE_OUT_TIME], theTimes[XPEST_END_TIME], time);
				}
			}
		}
		return 1.0f;
	}

	bool XPEnvSoundArea::Update(const CPOINT& pos)
	{
		XPASSERT(m_pEB);
		float vol = 0.0f;
		bool bIsInitVol = false;
		bool setVolume = (m_pEB->GetTick() - m_lastSetVolumeTick >= 100);//100毫秒检测一次
		int playingSoundNum = 0;
		if (setVolume)
			m_lastSetVolumeTick = m_pEB->GetTick();
		DirectSoundManager* pDSM = m_pEB->GetSoundManager();
		SoundVet::iterator it = m_sound.begin(), ie = m_sound.end();
		int i = 0;
		int time = m_pEB->GetGameTime();
		for (;it!=ie;++it,++i)
		{
			SoundTypeParam& type = *it;
			if (type.handle == INVALID_SOUND_HANDLE)
			{
				if (m_pEB->GetTick() - type.nLastPlayEndTick >= type.nTmpInterval)
				{
					if (!pDSM->TestPriority(type.priority))	//测试优先级
						continue;
					if (!bIsInitVol) 
					{
						vol = GetAreaVolume(pos);
						bIsInitVol = true;
					}
					if (vol <= 0.001f)
						continue;
					float fadeVol = 1.0f;

					unsigned char id = 0;
					size_t size = 0;
					SoundParamIDVet tmpVet;
					SoundParamVet::iterator pIt = type.sounds.begin(), pIe = type.sounds.end();
					for (;pIt!=pIe;++pIt,++id)
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
							} else {
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
							} else {
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
					type.currentID = tmpVet[RangeRand((size_t)0, size - 1)];
					SoundParam &param = type.sounds[type.currentID];
					unsigned char volume;
					type.currentInitVolume = RangeRand(param.minVol, param.maxVol);
					volume = static_cast<unsigned char>(vol * fadeVol * type.currentInitVolume);
					type.handle = pDSM->XPPlaySound(param.soundName, type.priority, 
						m_pEB->GetConfigManager()->GetEnvSoundType(), 
						volume, RangeRand(param.minFreq, param.maxFreq), param.isLooping);
					//XPTRACE(L"playStaticSound, %s, %d, %d, %d===================================================\n", param.soundName.c_str(), volume, freq, looping);
					if (type.handle != INVALID_SOUND_HANDLE)
					{
						pDSM->SetNotify(type.handle, this);
						m_handleMap[type.handle] = i;
						++playingSoundNum;
					}
				}
			} else {
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
						pDSM->StopSound(type.handle);
					} else {
						float fadeVol = 1.0f;
						SoundParam &param = type.sounds[type.currentID];
						if (param.stype == 3)
						{
							fadeVol = GetFadeVol(time, param.theTime);
							if (fadeVol < 0.001f)
							{
								--playingSoundNum;
								pDSM->StopSound(type.handle);
							}
						}
						pDSM->SetSoundVolume(type.handle, 
							static_cast<unsigned char>(vol * fadeVol * type.currentInitVolume));
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
		for (;it!=ie;++it)
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
		tmpParam.theTime[2] = XP_A_DAY_TIME;
		tmpParam.theTime[3] = XP_A_DAY_TIME;
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
