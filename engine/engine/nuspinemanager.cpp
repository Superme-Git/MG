#include "nuspinemanager.h"
#include "engine/nuenginebase.h"
#include "engine/nuengine.h"
#include "../common/nufileiomanager.h"
#include "../renderer/nucocos2d_render.h"
#include <spine/spine-cocos2dx.h>


namespace Nuclear
{
	SpineRes::SpineRes()
		: loaded(false)
	{
	}

	SpineRes::~SpineRes()
	{
	}

	class SpineReadTask : public NuclearFileIOManager::AsyncReadTask
	{
	public:
		struct DataImage
		{
			NuclearBuffer imageData;
			cocos2d::CCImage* pImage;
		};

		bool mCancel;

	private:
		std::wstring  mDir;
		std::wstring  mName;
		int64_t		  mTick;
		EngineBase*	  mpEngine;
		NuclearHardRef<SpineRes>     mpSpineRes;
		SpineManager* mpSpineMan;

		std::map<std::wstring, DataImage> mImageMap;

	public:
		SpineReadTask(const std::wstring& dir, const std::wstring& name, EngineBase *pEB, NuclearFileIOManager *pFileIOMan, NuclearHardRef<SpineRes> spineRes, SpineManager* spineMan)
			: NuclearFileIOManager::AsyncReadTask(pFileIOMan, L"", 0, false)
			, mDir(dir)
			, mName(name)
			, mpEngine(pEB)
			, mpSpineRes(spineRes)
			, mpSpineMan(spineMan)
		{
			mCancel = false;
			mTick = GetMilliSeconds();
		}

		virtual void Execute()
		{
			if (mCancel || mpSpineRes->loaded) return;

			Renderer* pRenderer = mpEngine->GetRenderer();

			// 加载 atlas 文件
			std::wstring pathAtlasFile = mDir + mName + L".atlas";
			mpSpineRes->mMutexForLoadingSpine.Lock();
			m_pFileIOMan->GetFileImage(pathAtlasFile, mpSpineRes->atlasBuffer, false);
			mpSpineRes->mMutexForLoadingSpine.UNLock();

			// 解出 atlas 引用的图片文件名
			spine::PathToTextureMap textureMap;
			std::string strDir = ws2s(mDir);
			spine::spAtlas_parseTextureMap((const char*)mpSpineRes->atlasBuffer.constbegin(), mpSpineRes->atlasBuffer.size(), strDir.c_str(), &textureMap);

			// 加载 atlas 引用的图片成 CCImage
			for (spine::PathToTextureMap::iterator it = textureMap.begin(); it != textureMap.end(); ++it)
			{
				const std::string& texturePath = it->first;

				Nuclear::NuclearPictureInfo picinfo;
				picinfo.fileuri = s2ws(texturePath);

				cocos2d::CCImage* pImage = NULL;
				NuclearBuffer imageData;
				if (m_pFileIOMan->GetFileImage(picinfo.fileuri, imageData))
				{
					pImage = new cocos2d::CCImage;
					if (!pRenderer->LoadCCImageFromMem(pImage, XPTEXFMT_DEFAULT, imageData.constbegin(), imageData.size()))
					{
						CC_SAFE_RELEASE_NULL(pImage);
					}
				}

				DataImage di = { imageData, pImage };
				mImageMap.insert(std::make_pair(picinfo.fileuri, di));
			}

			// 加载 json 文件
			std::wstring pathJsonFile = mDir + mName + L".json";
			mpSpineRes->mMutexForLoadingSpine.Lock();
			m_pFileIOMan->GetFileImage(pathJsonFile, mpSpineRes->jsonBuffer, false);
			mpSpineRes->mMutexForLoadingSpine.UNLock();
		}

		virtual void OnReady()
		{
			int64_t tick = GetMilliSeconds();

			int dataSize = getAllDataSize();
			if (mpSpineRes && dataSize > 0)
			{
				m_pFileIOMan->OnReady(int(tick - mTick), dataSize);  // 统计文件读取

				if (!mpSpineRes->loaded && !mCancel)
				{
					Renderer* pRenderer = mpEngine->GetRenderer();

					// 从 CCImage 或 imageData 创建贴图
					for (std::map<std::wstring, DataImage>::iterator it = mImageMap.begin(); it != mImageMap.end(); ++it)
					{
						DataImage& di = it->second;

						Nuclear::NuclearPictureInfo picinfo;
						picinfo.fileuri = it->first;

						PictureHandle picHandle = INVALID_PICTURE_HANDLE;
						if (di.pImage)
						{
							picHandle = pRenderer->LoadPictureFromCCImage(di.pImage, XPTEXFMT_DEFAULT, &picinfo);
							CC_SAFE_RELEASE_NULL(di.pImage);
						}
						else
						{
							picHandle = pRenderer->LoadPictureFromMem(di.imageData.constbegin(), di.imageData.size(), &picinfo);
						}

						mpSpineRes->mPicHandles.push_back(picHandle);
					}
					
					mpSpineRes->loaded = true;
				}

				for (LoadingSpineNotifySet::iterator sIt = mpSpineRes->notifys.begin(); sIt != mpSpineRes->notifys.end(); ++sIt)
				{
					SpineLoadingNotify* pItNotify = *sIt;
					if (!pItNotify->loaded)
					{
						pItNotify->OnLoaded(mDir, mpSpineRes.get());
						pItNotify->loaded = true;
					}
				}
			}
			mpSpineMan->NotifyFinish(mName);
			delete this;
		}

		virtual void OnDiscard()
		{
			delete this;
		}

		int getAllDataSize() const
		{
			int dataSize = 0;

			if (mpSpineRes)
			{
				dataSize += mpSpineRes->atlasBuffer.size();
				dataSize += mpSpineRes->jsonBuffer.size();

				for (std::map<std::wstring, DataImage>::const_iterator it = mImageMap.begin(); it != mImageMap.end(); ++it)
				{
					const DataImage& di = it->second;
					dataSize += di.imageData.size();
				}
			}
			
			return dataSize;
		}

		virtual ~SpineReadTask()
		{
			for (std::map<std::wstring, DataImage>::iterator it = mImageMap.begin(); it != mImageMap.end(); ++it)
			{
				DataImage& di = it->second;
				CC_SAFE_RELEASE_NULL(di.pImage);
			}
			mImageMap.clear();
		}
	};

	SpineResLoadingParam::SpineResLoadingParam(NuclearHardRef<SpineRes> res, SpineReadTask* task)
		: spineRes(res)
		, readTask(task)
	{

	}

	SpineResLoadingParam::~SpineResLoadingParam()
	{
		if (spineRes)
		{
			spineRes->loaded = true;
			//delete spineRes;
			spineRes = NuclearHardRef<SpineRes>();
		}
		if (readTask)
		{
			readTask->mCancel = true;
			readTask->Discard();
			readTask = NULL;
		}
	}

	SpineManager::SpineManager(EngineBase *pEB)
		: m_pEB(pEB)
	{
	}

	SpineManager::~SpineManager()
	{
		Destroy();
	}

	void SpineManager::Destroy()
	{
		for (SpineResLoadintMap::iterator it = m_SpineResLoadingMap.begin(); it != m_SpineResLoadingMap.end(); ++it)
		{
			delete it->second;
		}
		m_SpineResLoadingMap.clear();
	}
	void SpineManager::NotifyFinish(const std::wstring &name)
	{
		SpineResLoadintMap::iterator it = m_SpineResLoadingMap.find(name);
		if (it != m_SpineResLoadingMap.end())
		{
			SpineResLoadingParam* param = it->second;
			param->readTask = NULL;
		}
	}
	bool SpineManager::ASyncLoadSpineRes(const std::wstring &name, const std::wstring& dir, SpineLoadingNotify* pNotify)
	{
		if (!pNotify) return false;

		pNotify->loaded = false;

		SpineResLoadintMap::iterator it = m_SpineResLoadingMap.find(name);
		if (it != m_SpineResLoadingMap.end())
		{
			SpineResLoadingParam* param = it->second;
			param->spineRes->notifys.insert(pNotify);

			if (param->spineRes->loaded)
			{
				pNotify->OnLoaded(dir, param->spineRes.get());
			}
			return true;
		}

		NuclearHardRef<SpineRes> spineRes(new SpineRes);
		spineRes->notifys.insert(pNotify);

		SpineReadTask* pTask = new SpineReadTask(dir, name, m_pEB, m_pEB->GetFileIOManager(), spineRes, this);
		if (pTask->Submit())
		{
			SpineResLoadingParam* param = new SpineResLoadingParam(spineRes, pTask);

			m_SpineResLoadingMap[name] = param;

			return true;
		}
		else
		{
			delete pTask;
			return false;
		}

		return false;
	}

	bool SpineManager::SyncLoadSpineRes(const std::wstring &name, const std::wstring& dir, SpineLoadingNotify* pNotify)
	{
		if (name.empty() || !pNotify) return false;

		NuclearFileIOManager *pFIOMan = m_pEB->GetFileIOManager();
		Renderer* pRenderer = m_pEB->GetRenderer();

		NuclearHardRef<SpineRes> spineRes = NuclearHardRef<SpineRes>();;
		pNotify->loaded = false;

		SpineResLoadintMap::iterator it = m_SpineResLoadingMap.find(name);
		if (it != m_SpineResLoadingMap.end())
		{
			spineRes = it->second->spineRes;
			spineRes->notifys.insert(pNotify);
		}
		else
		{
			//spineRes = new SpineRes;
			spineRes = NuclearHardRef<SpineRes>(new SpineRes());
			spineRes->notifys.insert(pNotify);

			SpineResLoadingParam* param = new SpineResLoadingParam(spineRes, NULL);
			m_SpineResLoadingMap[name] = param;
		}

		if (spineRes)
		{
			if (!spineRes->loaded)
			{

				// 加载 atlas 文件
				std::wstring pathAtlasFile = dir + name + L".atlas";
				spineRes->mMutexForLoadingSpine.Lock();
				pFIOMan->GetFileImage(pathAtlasFile, spineRes->atlasBuffer, false);
				spineRes->mMutexForLoadingSpine.UNLock();

				// 解析 atlas 引用的图片文件名
				spine::PathToTextureMap textureMap;
				std::string strDir = ws2s(dir);
				spine::spAtlas_parseTextureMap((const char*)spineRes->atlasBuffer.constbegin(), spineRes->atlasBuffer.size(), strDir.c_str(), &textureMap);

				// 加载 atlas 引用的图片
				for (spine::PathToTextureMap::iterator it = textureMap.begin(); it != textureMap.end(); ++it)
				{
					const std::string& texturePath = it->first;

					Nuclear::NuclearPictureInfo picinfo;
					picinfo.fileuri = s2ws(texturePath);

					PictureHandle picHandle = INVALID_PICTURE_HANDLE;
					if (!pRenderer->GetPictureHandle(picHandle, picinfo.fileuri))
					{
						NuclearBuffer data;
						pFIOMan->GetFileImage(picinfo.fileuri, data);

						picHandle = pRenderer->LoadPictureFromMem(data.constbegin(), data.size(), &picinfo);
					}

					spineRes->mPicHandles.push_back(picHandle);
				}

				// 加载 json 文件
				std::wstring pathJsonFile = dir + name + L".json";
				spineRes->mMutexForLoadingSpine.Lock();
				pFIOMan->GetFileImage(pathJsonFile, spineRes->jsonBuffer, false);
				spineRes->mMutexForLoadingSpine.UNLock();

				spineRes->loaded = true;
			}

			// 通知每一个监听器
			for (LoadingSpineNotifySet::iterator sIt = spineRes->notifys.begin(); sIt != spineRes->notifys.end(); ++sIt)
			{
				SpineLoadingNotify* pItNotify = *sIt;
				if (!pItNotify->loaded)
				{
					pItNotify->OnLoaded(dir, spineRes.get());
					pItNotify->loaded = true;
				}
			}
		}

		return true;
	}

	void SpineManager::FreeSpineRes(const std::wstring& name, SpineLoadingNotify* pNotify)
	{
		SpineResLoadintMap::iterator it = m_SpineResLoadingMap.find(name);
		if (it != m_SpineResLoadingMap.end())
		{
			SpineResLoadingParam* param = it->second;
			for (LoadingSpineNotifySet::iterator sIt = param->spineRes->notifys.begin(); sIt != param->spineRes->notifys.end(); ++sIt)
			{
				if (*sIt == pNotify)
				{
					param->spineRes->notifys.erase(sIt);
					break;
				}
			}

			if (param->spineRes->notifys.empty())
			{
				for (SpineRes::PictureHandleArray::iterator itHFPair = param->spineRes->mPicHandles.begin(); itHFPair != param->spineRes->mPicHandles.end(); ++itHFPair)
				{
					PictureHandle& picHandle = *itHFPair;
					if (picHandle != INVALID_PICTURE_HANDLE)
					{
						m_pEB->GetRenderer()->FreePicture(picHandle);
						picHandle = INVALID_PICTURE_HANDLE;
					}
				}

				delete param;
				m_SpineResLoadingMap.erase(it);
			}
		}
	}
}
