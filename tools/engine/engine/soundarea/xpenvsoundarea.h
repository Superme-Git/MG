#ifndef __Nuclear_XPENVSOUNDAREA_H
#define __Nuclear_XPENVSOUNDAREA_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <set>
#include "..\engine\enginebase.h"
#include "..\sound\directsoundmanager.h"
#include "xpsoundarea.h"
class SoundApp;
class CSoundEditorDoc;
class CAllResDlg;

namespace Nuclear 
{
	//1 普通的，没有开始、结束时间。
	//2 有开始结束时间，一旦开播了就得等它播完或者离开区域。
	//  （这里的“结束”时间和现在白天黑夜类似，就是说过了这个时间就不会随机到它）
	//3 有淡入、正常、淡出、结束4个时间点，不管环境音播放到哪里，只要时间点到了就会淡出结束

	//每种环境音都可以设置是否循环。设置了循环的声音视为这个音效无限长。
	//一个音效“播放完”有如下可能：
	//1 就是纯粹播完了
	//2 当前音效是第3种音效，并且当前时间已经到达结束点了
	//3 主角已经离开了区域。
	enum XPENVSTIME
	{
		XPEST_BEGIN_TIME = 0,	//第二种声音只看BEGIN_TIME和END_TIME
		XPEST_BEGIN_NORMAL_TIME,
		XPEST_FADE_OUT_TIME,
		XPEST_END_TIME,
	};
	class XPEnvSoundArea : public XPSoundArea, public ISoundNotify
	{
	//自定义类型们
	public:
		struct SoundParam
		{
			std::wstring soundName;
			unsigned char minVol;
			unsigned char maxVol;
			unsigned char minFreq;
			unsigned char maxFreq;
			int theTime[4];
			unsigned char stype;	//可能的值是1 2 3
			bool isLooping;
		};
		typedef std::vector<SoundParam> SoundParamVet;
		struct SoundTypeParam
		{
			int nTmpInterval;
			int nMinInterval;
			int nMaxInterval;
			int nLastPlayEndTick;
			SoundHandle handle;
			SoundParamVet sounds;
			short priority;
			unsigned char currentInitVolume;
			unsigned char currentID;
			SoundTypeParam() : nTmpInterval(0), nMinInterval(0), nMaxInterval(0), nLastPlayEndTick(0), 
				handle(INVALID_SOUND_HANDLE), priority(0), currentInitVolume(0), currentID(0) {}
		};
	private:
		friend class ::SoundApp;
		friend class ::CSoundEditorDoc;
		friend class ::CAllResDlg;
		typedef std::vector<unsigned char> SoundParamIDVet;
		typedef std::vector<SoundTypeParam> SoundVet;
		typedef std::map<SoundHandle, int> SoundHandleMap;
		
	//属性们
	private:
		EngineBase *m_pEB;
		int m_lastSetVolumeTick;
		SoundVet m_sound;
		SoundHandleMap m_handleMap;

	protected:
		int m_nTransition;		//扩展区（过渡区）宽度


	public:
		XPEnvSoundArea(EngineBase *pEB);
		virtual ~XPEnvSoundArea(void);

	protected:
		void LoadSound(XMLIO::CINode& node);
		void SaveSound(XMLIO::CONode& node);
		void SetMaxVol(int typeID, int soundID, unsigned char v) { m_sound[typeID].sounds[soundID].maxVol = v; }
		void SetMinVol(int typeID, int soundID, unsigned char v) { m_sound[typeID].sounds[soundID].minVol = v; }
		void SetMaxFreq(int typeID, int soundID, unsigned char v) { m_sound[typeID].sounds[soundID].maxFreq = v; }
		void SetMinFreq(int typeID, int soundID, unsigned char v) { m_sound[typeID].sounds[soundID].minFreq = v; }
		void SetTime(int typeID, int soundID, int type, int time) { m_sound[typeID].sounds[soundID].theTime[type] = time; }
		void SetLooping(int typeID, int soundID, bool loop) { m_sound[typeID].sounds[soundID].isLooping = loop; }
		void SetSoundType(int typeID, int soundID, unsigned char type) { m_sound[typeID].sounds[soundID].stype = type; }
		void SetMinInterval(int typeID, int nInterval) { m_sound[typeID].nMinInterval = nInterval; }
		void SetMaxInterval(int typeID, int nInterval) { m_sound[typeID].nMaxInterval = nInterval; }
		void SetPriority(int typeID, short priority) { m_sound[typeID].priority = priority; }
		int AddSound(int typeID, const std::wstring &name);
		void InsertSound(int typeID, int soundID, const SoundParam &param);
		void DeleteSound(int typeID, int soundID) { m_sound[typeID].sounds.erase(m_sound[typeID].sounds.begin()+soundID); }
		int AddSoundType() { m_sound.push_back(SoundTypeParam()); return static_cast<int>(m_sound.size() - 1); }
		void InsertSoundType(int typeID, const SoundTypeParam &param);
		void DeleteSoundType(int typeID) { m_sound.erase(m_sound.begin()+typeID); }
		
	public:
		virtual void OnSoundPlayEnd(SoundHandle handle) override;
		//每帧都要调用，但是不一定每帧都设置音量，如果返回值为true，代表这个区域有声音在播放，如果是false，代表这个区域没有声音在播放
		virtual bool Update(const CPOINT& pos);
		virtual float GetAreaVolume(const CPOINT& pos) const = 0;
		void GetSoundList(int typeID, std::vector<std::wstring> &slist) const;
		int GetTypeCount() const { return static_cast<int>(m_sound.size()); }
		unsigned char GetMaxVol(int typeID, int soundID) const { return m_sound[typeID].sounds[soundID].maxVol; }
		unsigned char GetMinVol(int typeID, int soundID) const { return m_sound[typeID].sounds[soundID].minVol; }
		unsigned char GetMaxFreq(int typeID, int soundID) const { return m_sound[typeID].sounds[soundID].maxFreq; }
		unsigned char GetMinFreq(int typeID, int soundID) const { return m_sound[typeID].sounds[soundID].minFreq; }
		int GetTime(int typeID, int soundID, int type) const { return m_sound[typeID].sounds[soundID].theTime[type]; }
		bool IsLooping(int typeID, int soundID) const { return m_sound[typeID].sounds[soundID].isLooping; }
		unsigned char GetSoundType(int typeID, int soundID) const { return m_sound[typeID].sounds[soundID].stype; }
		int GetMinInterval(int typeID) const { return m_sound[typeID].nMinInterval; }
		int GetMaxInterval(int typeID) const { return m_sound[typeID].nMaxInterval; }
		short GetPriority(int typeID) const { return m_sound[typeID].priority; }
		const SoundParam& GetSoundParam(int typeID, int soundID) const { return m_sound[typeID].sounds[soundID]; }
		const SoundTypeParam& GetSoundTypeParam(int typeID) const { return m_sound[typeID]; }
	};
}


#endif	//__Nuclear_XPIAREA_H
