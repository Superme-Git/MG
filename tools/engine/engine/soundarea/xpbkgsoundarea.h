#ifndef __Nuclear_XPBKGSOUNDAREA_H
#define __Nuclear_XPBKGSOUNDAREA_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "xpsoundarea.h"


class SoundApp;
class CSoundEditorDoc;
class CAllResDlg;
namespace Nuclear
{
	class DirectSoundManager;
	class IStreamSoundNotify;
	class EngineBase;


	enum XPBGM_PLAY_TYPE
	{
		XPBGMPT_LOOP_REST = 0,
		XPBGMPT_RANDOM,
		XPBGMPT_RANDOM_UNIQUE,
		XPBGMPT_QUEUE1,
		XPBGMPT_QUEUE2,
		XPBGMPT_LIST,
	};
	class IXPBGMControl;
	class XPBGMListControl;

	class XPBkgSoundArea : public XPSoundArea
	{
	//自定义类型们
	public:
		struct SoundParam
		{
			std::wstring soundName;
			int vol;
			XPSA_SOUND_TYPE type;
			SoundParam() : type(XPSA_BOTH) {}
		};
		typedef std::vector<SoundParam> SoundParamVet;
	private:
		friend class ::SoundApp;
		friend class ::CSoundEditorDoc;
		friend class ::CAllResDlg;
		friend class XPBGMListControl;

	//成员们
	private:
		EngineBase *m_pEB;
		SoundParamVet m_sounds;
		IXPBGMControl *m_pBGMControl;
		int m_nMinInterval;
		int m_nMaxInterval;
		unsigned short m_nFadeInTime;
		unsigned short m_nFadeOutTime;
		bool m_bWaitPrevAreaPlayEnd;//是否等上个区域FadeOut完毕

	//方法们
	public:
		XPBkgSoundArea(EngineBase *pEB);
		virtual ~XPBkgSoundArea(void);
	private:
		//bool SetNextSound(bool isDay);

	protected:
		void LoadSound(XMLIO::CINode& node);
		void SaveSound(XMLIO::CONode& node);
		void SetVol(int soundID, int v) { m_sounds[soundID].vol = v; }
		void SetSoundType(int soundID, XPSA_SOUND_TYPE type) { m_sounds[soundID].type = type; }
		int AddSound(const std::wstring &name);
		void InsertSound(int soundID, const SoundParam &param);
		void DeleteSound(int soundID) { m_sounds.erase(m_sounds.begin()+soundID); }
		void MoveSound(int soundID, bool bUp);
		void SetMinInterval(int v) { m_nMinInterval = v; }
		void SetMaxInterval(int v) { m_nMaxInterval = v; }
		void SetFadeInTime(unsigned short t) { m_nFadeInTime = t; }
		void SetFadeOutTime(unsigned short t) { m_nFadeOutTime = t; }
		void SetWaitPrevAreaPlayEnd(bool b) { m_bWaitPrevAreaPlayEnd = b; }
		void SetPlayMode(XPBGM_PLAY_TYPE mode);

	public:
		//以下，给IXPBGMControl用
		void GetCurTimeSoundIDs(std::vector<int> &vet);
		const SoundParamVet& GetSounds() const { return m_sounds; }
		EngineBase *GetEngineBase() const { return m_pEB; }
		IXPBGMControl* GetBGMControl() const { return m_pBGMControl; }

		//方法
		int GetMinInterval() const { return m_nMinInterval; }
		int GetMaxInterval() const { return m_nMaxInterval; }
		unsigned short GetFadeInTime() const { return m_nFadeInTime; }
		unsigned short GetFadeOutTime() const { return m_nFadeOutTime; }
		bool IsWaitPrevAreaPlayEnd() const { return m_bWaitPrevAreaPlayEnd; }
		void GetSoundList(std::vector<std::wstring> &slist) const;
		int GetVol(int soundID) const { return m_sounds[soundID].vol; }
		XPBGM_PLAY_TYPE GetPlayMode() const;
		XPSA_SOUND_TYPE GetSoundType(int soundID) const { return m_sounds[soundID].type; }
		const SoundParam& GetSoundParam(int soundID) const { return m_sounds[soundID]; }
		void ClearSound() { m_sounds.clear(); }

		//下面的接口是给World用的
		//如果pArea就是自己，代表刚刚跳地图进来。。。
		void LeaveMeAndEnterNew(XPBkgSoundArea *pArea);
		void FadeOut(unsigned short time);
		virtual bool TestArea(const CPOINT& pos) const = 0;
		void SetVolume(float fV);
		float GetVolume() const;
	};

	class XPBkgDefaultSoundArea :
		public XPBkgSoundArea
	{
		//自定义类型
	public:
		friend class ::CSoundEditorDoc;
		friend class ::SoundApp;

		//属性们
	protected:

		//方法们
	public:
		//构造析构
		XPBkgDefaultSoundArea(EngineBase *pEB) : XPBkgSoundArea(pEB) {};
		virtual ~XPBkgDefaultSoundArea(void) {};

		//实现
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_NULL; }
		virtual int Load(XMLIO::CINode& node, int flag);
		virtual int Save(XMLIO::CONode& node, int flag);
		virtual bool TestArea(const CPOINT& pos) const { return true; }
	};

	class XPBkgSoundPolygonArea :
		public XPBkgSoundArea
	{
	//自定义类型
	public:
		friend class ::CSoundEditorDoc;
		friend class ::SoundApp;

	//属性们
	protected:
		XPPolygon m_polygon;

	//方法们
	public:
		//构造析构
		XPBkgSoundPolygonArea(EngineBase *pEB) : XPBkgSoundArea(pEB) {};
		virtual ~XPBkgSoundPolygonArea(void) {};

		//实现
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_POLYGON; }
		virtual int Load(XMLIO::CINode& node, int flag);
		virtual int Save(XMLIO::CONode& node, int flag);
		virtual int LoadToolsData(XMLIO::CINode& node) { return 0; }
		virtual int SaveToolsData(XMLIO::CONode& node) { return 0; }
		virtual bool TestArea(const CPOINT& pos) const { return m_polygon.IsPointIn(pos.x, pos.y); }
	};

	class XPBkgSoundCircleArea :
		public XPBkgSoundArea
	{
		//自定义类型
	public:
		friend class ::CSoundEditorDoc;
		friend class ::SoundApp;

		//属性们
	protected:
		XPCircle m_circle;

		//方法们
	public:
		//构造析构
		XPBkgSoundCircleArea(EngineBase *pEB) : XPBkgSoundArea(pEB) {};
		virtual ~XPBkgSoundCircleArea(void) {};

		//实现
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_CIRCLE; }
		virtual int Load(XMLIO::CINode& node, int flag);
		virtual int Save(XMLIO::CONode& node, int flag);
		virtual bool TestArea(const CPOINT& pos) const { return m_circle.IsPointIn(pos.x, pos.y); }
	};


	class IXPBGMControl : public ISoundNotify, public ISoundLoadNotify, public ITimer
	{
	protected:
		XPBkgSoundArea *m_pArea;
		std::set<std::wstring> m_loadingFiles;
		int m_nCurSound;
		int m_nNextSoundID;
		SoundHandle m_curSoundHandle;
		bool m_bLoadSuccNeedPlay;	//加载完之后是否马上play
		bool m_bLeft;				//是否离开了区域
		bool m_bIsFirst;			//是否进入区域的第一首歌
		bool m_bHasSetTimer;		//是否设了Timer
		float m_fVolume;

	public:
		static IXPBGMControl* GetBGMControl(XPBGM_PLAY_TYPE type, XPBkgSoundArea *pArea);

	protected:
		IXPBGMControl(XPBkgSoundArea *pArea) : m_pArea(pArea), m_nCurSound(-1), 
			m_nNextSoundID(-1), m_curSoundHandle(INVALID_SOUND_HANDLE), 
			m_bLoadSuccNeedPlay(false), m_bLeft(true), m_bIsFirst(true), m_bHasSetTimer(false) {}

		CHECK_SOUND_STATE CheckSound(int soundID);
		bool CheckAndCreateSound(int soundID, SoundHandle& handle);

	public:
		virtual ~IXPBGMControl();

		virtual int GetNextSoundID() = 0;
		virtual XPBGM_PLAY_TYPE GetType() const = 0;
		virtual void LeaveMeAndEnterNew(XPBkgSoundArea *pControl);
		virtual void FadeOut(unsigned short time);
		virtual void EnterMe(int delayTime);//delayTime是上一个区域的曲子FadeOut的时间
		virtual void Reset();
		bool PlayCurSoundHandle(unsigned short nFadeInTime);
		bool AppendSound(const std::wstring &fn);
		bool AppendSound(int soundID);
		void SetVolume(float fV);
		float GetVolume() const { return m_fVolume; }

		//实现接口
		virtual void OnSoundGoingToEnd(SoundHandle handle) override;
		virtual void OnSoundPlayEnd(SoundHandle handle) override;
		virtual void OnSoundLoaded(const std::wstring &fn, bool success);
		virtual void OnTimer();
	};

};
#endif //#ifndef __Nuclear_XPBKGSOUNDAREA_H
