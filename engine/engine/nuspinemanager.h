#ifndef __Nuclear_SPINEMANAGER_H__
#define __Nuclear_SPINEMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxptypes.h"
#include "../common/nuxref.h"
#include "platform/mutex.h"

namespace Nuclear
{
	class EngineBase;
	class SpineRes;
	class SpineReadTask;

	class SpineLoadingNotify
	{
	public:
		bool loaded;
		SpineLoadingNotify() : loaded(false){ }
		virtual void OnLoaded(const std::wstring& dir, SpineRes* pSpineRes) = 0;
	};
	typedef std::set<SpineLoadingNotify*> LoadingSpineNotifySet;

	class SpineRes
	{
	public:
		typedef std::vector<PictureHandle> PictureHandleArray;

	public:
		PictureHandleArray mPicHandles;
		NuclearBuffer	atlasBuffer;
		NuclearBuffer	jsonBuffer;
		LoadingSpineNotifySet notifys;
		bool   loaded;

		core::CMutex mMutexForLoadingSpine;

		SpineRes();
		~SpineRes();
	};

	class SpineResLoadingParam
	{
	public:
		NuclearHardRef<SpineRes>		spineRes;
		SpineReadTask*	readTask;
		SpineResLoadingParam(NuclearHardRef<SpineRes> res, SpineReadTask* task);
		~SpineResLoadingParam();
	};

	class SpineManager
	{
		typedef std::map<std::wstring, SpineResLoadingParam*> SpineResLoadintMap;

	public:

		SpineManager(EngineBase *pEB);
		~SpineManager();

		void Destroy();

		bool ASyncLoadSpineRes(const std::wstring &name, const std::wstring& dir, SpineLoadingNotify* pNotify);
		bool SyncLoadSpineRes(const std::wstring &name, const std::wstring& dir, SpineLoadingNotify* pNotify);

		void FreeSpineRes(const std::wstring& name, SpineLoadingNotify* pNotify);
		void NotifyFinish(const std::wstring &name);

	private:

		EngineBase *m_pEB;

		SpineResLoadintMap	m_SpineResLoadingMap;
	};

}

#endif
