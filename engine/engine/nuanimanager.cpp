#include "nuanimanager.h"
#include "../common/nulog.h"
#include "../common/nufileiomanager.h"
#include "../renderer/nurenderer.h"
#include "engine/nuengine.h"
#include "nuenginebase.h"
#include "nuiengine.h"
#include "ljfmfex.h"

#if (defined WIN32) && (defined XP_PERFORMANCE)
#include "utils/StringCover.h"
#endif

namespace Nuclear
{
	#define LoadPicTimeLimit 60
	class AniPicReadTask : public NuclearFileIOManager::AsyncReadTask
	{
		AniManager* m_pAniManager;
		XAni::XBigPic* m_pPic;
		std::wstring m_xapuri;
		NuclearHardRef<XAni> m_ani;
		int64_t m_tick;
		int m_iPixelConvert;
		cocos2d::CCImage* m_pImage;
	public:
		AniPicReadTask(const std::wstring &filename, const std::wstring &xapuri, 
			NuclearFileIOManager *pFileIOMan, AniManager *pAniMan, XAni::XBigPic* pBigPic, 
			NuclearHardRef<XAni> ani, bool bLookupSegmPak, bool iPixelConvert = 0)
			: NuclearFileIOManager::AsyncReadTask(pFileIOMan, filename, 0, bLookupSegmPak), 
			m_xapuri(xapuri), m_pAniManager(pAniMan), m_pPic(pBigPic), m_ani(ani), m_iPixelConvert(iPixelConvert), m_pImage(NULL)
		{
			m_pPic->asyncLoad = true;
			m_tick = GetMilliSeconds();
		}

		virtual void Execute()
		{
			NuclearFileIOManager::AsyncReadTask::Execute();

			loadCCImage();
		}

		void loadCCImage()
		{
			if (m_data.size() > 0 || m_ani->m_state != XAni::XPANISTATE_NULL)
			{
				Renderer* pRenderer = m_pAniManager->m_pEB->GetRenderer();

				CC_SAFE_RELEASE_NULL(m_pImage);
				m_pImage = new cocos2d::CCImage;
				if (!pRenderer->LoadCCImageFromMem(m_pImage, m_ani->GetTexFmt(), m_data.constbegin(), m_data.size()))
				{
					CC_SAFE_RELEASE_NULL(m_pImage);
				}
			}
		}

		virtual void OnReady()
		{
			int64_t tick = GetMilliSeconds();
			if (m_data.size() == 0 || m_ani->m_state == XAni::XPANISTATE_NULL)
			{
				XPLOG_ERROR(L"load xappicfile failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load xappicfile failed!, path is %s\n", m_filename.c_str());
			}
			else
			{
				m_pFileIOMan->OnReady(int(tick-m_tick), m_data.size());
				Renderer* pRenderer = m_pAniManager->m_pEB->GetRenderer();
				if (m_pPic->handle == INVALID_PICTURE_HANDLE)
				{
                    Nuclear::NuclearPictureInfo picinfo;
                    picinfo.fileuri = m_pPic->filename;
					if (m_pImage)
					{
						m_pPic->handle = pRenderer->LoadPictureFromCCImage(m_pImage, m_ani->GetTexFmt(), &picinfo, XPPOOL_MANAGED, pRenderer->CacheInMemory(), 0);
						CC_SAFE_RELEASE_NULL(m_pImage);
					}
					else
					{
						m_pPic->handle = pRenderer->LoadPictureFromMem(m_data.constbegin(), m_data.size(), &picinfo, m_ani->GetTexFmt(), XPPOOL_MANAGED, pRenderer->CacheInMemory(), 0, m_iPixelConvert);
					}

					m_pPic->loaded = true;
					if (!pRenderer->CacheInMemory())
						pRenderer->CachePicture(m_pPic->handle, m_xapuri);
				}
				else
				{
                    Nuclear::NuclearPictureInfo picinfo;
					 picinfo.fileuri = m_pPic->filename;
					 if (!pRenderer->GetPictureInfo(m_pPic->handle, picinfo))
                    {
						if (m_pImage)
						{
							m_pPic->handle = pRenderer->LoadPictureFromCCImage(m_pImage, m_ani->GetTexFmt(), &picinfo, XPPOOL_MANAGED, pRenderer->CacheInMemory(), 0);
							CC_SAFE_RELEASE_NULL(m_pImage);
						}
						else
						{
							m_pPic->handle = pRenderer->LoadPictureFromMem(m_data.constbegin(), m_data.size(), &picinfo, m_ani->GetTexFmt(), XPPOOL_MANAGED, pRenderer->CacheInMemory(), 0, m_iPixelConvert);
						}
						m_pPic->loaded = true;
					}
                }
			}
			AniManager::LoadingMap::iterator it = m_pAniManager->m_LoadingUrls.find(m_xapuri);
			if (it != m_pAniManager->m_LoadingUrls.end())
			{
				NuclearHardRef<XAni>& hardAni = it->second.ani;
				if (m_ani->m_state != XAni::XPANISTATE_NULL && m_pPic->handle == INVALID_PICTURE_HANDLE)
				{
					if (!hardAni->m_perpetualmem)
						hardAni->m_state = XAni::XPANISTATE_NULL;
				}
				it->second.remainPics.erase(m_filename);
				if (it->second.remainPics.size() == 0)//全部加载完了
				{
					if (hardAni->m_state == XAni::XPANISTATE_NULL)
					{
						m_pAniManager->FreeAni(*hardAni);
					}
					else
					{ 
						hardAni->m_state = XAni::XPANISTATE_PICTURE;
					}
					AniManager::LoadingNotifySet::iterator sIt = it->second.notifys.begin(), sIe = it->second.notifys.end();
					for (;sIt!=sIe;++sIt)
					{
						(*sIt)->OnLoadAniReady(m_xapuri);
					}
					m_pAniManager->m_LoadingUrls.erase(it);
				}
				else
				{
					if (m_iPixelConvert)
					{
						//core::CMutex::Scoped autoLock(hardAni->mMutexForBuffers);
						if (*hardAni->m_buffers.begin() == m_pPic && hardAni->m_state != XAni::XPANISTATE_NULL)
						{
							hardAni->m_state = XAni::XPANISTATE_BASE_PIC;
							AniManager::LoadingNotifySet::iterator sIt = it->second.notifys.begin(), sIe = it->second.notifys.end();
							for (; sIt != sIe; ++sIt)
							{
								(*sIt)->OnLoadAniBaseReady(m_xapuri);
							}
						}
					}
				}
			}
			else
			{
				XPLOG_ERROR(L"AniReadTask On Ready error, path is %s\n", m_filename.c_str());
			}
			delete this;
		}

		virtual void OnDiscard() 
		{
            //logfilefail
			AniManager::LoadingMap::iterator it = m_pAniManager->m_LoadingUrls.find(m_xapuri);
			if (it != m_pAniManager->m_LoadingUrls.end())
			{
				it->second.remainPics.erase(m_filename);
			}
			else
			{
				XPLOG_ERROR(L"AniReadTask On Discard error, path is %s\n", m_filename.c_str());
			}
			delete this;
		}
		virtual ~AniPicReadTask()
		{
			CC_SAFE_RELEASE_NULL(m_pImage);
		}
	};

	class XapReadTask : public NuclearFileIOManager::AsyncReadTask
	{
		AniManager* m_pAniManager;
		NuclearHardRef<XAni> m_ani;
		int64_t m_tick;
		int m_error;
	public:
		XapReadTask(const std::wstring &filename, NuclearFileIOManager *pFileIOMan, 
			AniManager *pAniMan, NuclearHardRef<XAni> ani, bool bLookupSegmpak)
			: NuclearFileIOManager::AsyncReadTask(pFileIOMan, filename, 0, bLookupSegmpak), 
			m_pAniManager(pAniMan), m_ani(ani), m_error(0)
		{
			m_tick = GetMilliSeconds();
		}

		virtual void Execute()
		{
			NuclearFileIOManager::AsyncReadTask::Execute();

			loadPackAndAni();
		}

		void loadPackAndAni()
		{
			m_error = 0;

			bool bIt = false;
			{
				core::CMutex::Scoped autoLock(m_pAniManager->mMutexForLoadingXaps);
				AniManager::LoadingXapMap::iterator it = m_pAniManager->m_LoadingXaps.find(m_filename);
				bIt = (it != m_pAniManager->m_LoadingXaps.end());
			}

			if (bIt)
			{
				if (m_ani->m_state == XAni::XPANISTATE_NO_XAP || m_ani->m_state == XAni::XPANISTATE_NULL)
				{
					PAniPack pack;
					if (!pack.LoadFromMem(m_data))
					{
						XPTRACE(L"  解析ani 资源失败, uri= %s\n", m_filename.c_str());
						XPLOG_ERROR(L"  解析ani 资源失败, uri= %s\n", m_filename.c_str());
						if (!(m_pFileIOMan->GetFileImage(L"/cfg/error/discard.ani", m_data, false) && pack.LoadFromMem(m_data)))
						{
							m_ani->m_state = XAni::XPANISTATE_NULL;
							m_error = 1;
							return;
						}
					}

					std::wstring baseuri = m_filename.substr(0, m_filename.size() - 4);
					if (!m_pAniManager->LoadAniFromPack(*m_ani, pack, baseuri, true))
					{
						XPTRACE(L"  解析ani 资源pack失败, uri= %s\n", m_filename.c_str());
						XPLOG_ERROR(L"  解析ani 资源pack失败, uri= %s\n", m_filename.c_str());
						m_ani->m_state = XAni::XPANISTATE_NULL;
						m_error = 2;
						return;
					}
				}
			}
		}

		virtual void OnReady()
		{
			int64_t tick = GetMilliSeconds();
			if( m_data.size() == 0 )
			{
				XPLOG_ERROR(L"load ani failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load ani failed!, path is %s\n", m_filename.c_str());
				m_ani->m_state = XAni::XPANISTATE_NULL;
			} else {
				m_pFileIOMan->OnReady(int(tick-m_tick), m_data.size());
				XPTRACE(L"load ani ok!, path is %s\n", m_filename.c_str());
			}

			AniManager::LoadingXapMap::iterator it = m_pAniManager->m_LoadingXaps.find(m_filename);
			if (it != m_pAniManager->m_LoadingXaps.end())
			{
				if (m_error)
				{
					delete this;
					return;
				}

				if (it->second.notifys.size() > 0)
				{
					Renderer *pRenderer = GetEngine()->GetRenderer();
					AniManager::LoadingPicParam &param = m_pAniManager->m_LoadingUrls[m_filename];
					param.ani = m_ani;
					//param.notifys.clear();//清除未加载完的加载队列
					param.notifys.swap(it->second.notifys);
					bool loading1 = false;
					bool loading2 = false;
					{
						//core::CMutex::Scoped autoLock(m_ani->mMutexForBuffers);
						XAni::Buffers::iterator bit = m_ani->m_buffers.begin();
						for (; bit != m_ani->m_buffers.end(); ++bit)
						{
							XAni::XBigPic *pPic = *bit;
							if (pRenderer->GetPictureHandle(pPic->handle, pPic->filename))
								continue;
							loading1 = true;
							param.remainPics.insert(pPic->filename);
							int iPixelConvert = 0;
							if (m_ani->m_buffersPart.size() > 0)
							{
								iPixelConvert = 1;
							}
							AniPicReadTask *pTask = new AniPicReadTask(pPic->filename, m_filename, m_pFileIOMan, m_pAniManager, pPic, m_ani, m_bLookupSegmPak, iPixelConvert);
							if (!pTask->Submit())
							{
								delete pTask;
							}
						}
					}

					{//分块图比较小,先加载以保证渲染HSV纹理的时候分块图已经准备好了
						if (m_ani->m_buffersPart.size() > 0)
						{
							XAni::Buffers::iterator bit = m_ani->m_buffersPart.begin(), bie = m_ani->m_buffersPart.end();
							for (; bit != bie; ++bit)
							{
								XAni::XBigPic *pPic = *bit;
								if (pRenderer->GetPictureHandle(pPic->handle, pPic->filename))
									continue;
								loading2 = true;
								param.remainPics.insert(pPic->filename);
								AniPicReadTask *pTask = new AniPicReadTask(pPic->filename, m_filename, m_pFileIOMan, m_pAniManager, pPic, m_ani, m_bLookupSegmPak, 0);
								if (!pTask->Submit())
								{
									delete pTask;
								}
							}
						}
					}

					if (!loading1 && !loading2)
					{
						//居然全部加载完了。。。
						AniManager::LoadingNotifySet::iterator sIt = param.notifys.begin(), sIe = param.notifys.end();
						for (; sIt != sIe; ++sIt)
						{
							(*sIt)->OnLoadAniReady(m_filename);
						}
						m_pAniManager->m_LoadingUrls.erase(m_filename);
					}
				}
				core::CMutex::Scoped autoLock(m_pAniManager->mMutexForLoadingXaps);
				m_pAniManager->m_LoadingXaps.erase(it);
			}
			else
			{
				XPLOG_ERROR(L"XapReadTask On Ready error, path is %s\n", m_filename.c_str());
			}
			delete this;
		}

		virtual void OnDiscard() 
		{
			delete this;
		}
		virtual ~XapReadTask() { }
	};

	//这个pt是相对ani的原点的坐标, r只对alpha模式生效
	bool XAni::TestPoint(int dir, int frame, const NuclearPoint &pt, Nuclear_Mouse_TestMode mode, Renderer *pRenderer, int r, float testval)
	{
		//从逻辑上来说，如果mode是XPTM_PIC_RECT，那么不会调用进来，在Sprite或者Immovableobj就已经return了
		XPASSERT(mode != XPTM_PIC_RECT);
		XPic pic;
		NuclearBase base;
		if( GetBase(base) )
		{
			for(int k=0;k<m_nRegion;++k)
			{
				if( GetPic(dir, frame, k, pic) )
				{
					NuclearLocation realtestloc = pt;
					if( m_dirmode == XPANIDM_8FROM5 && dir > XPDIR_BOTTOM )
					{
						realtestloc.x = -realtestloc.x;
					}
					if (mode == XPTM_POLYGON)
					{
						//转换为小图片坐标
						realtestloc.x -= pic.offset.left;
						realtestloc.y -= pic.offset.top;
						return pic.outLine.IsPointIn(realtestloc.x, realtestloc.y);
					}
					if (pic.pBigPic != NULL && pic.pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
                        assert(false && "mode == XPTM_ALPHA, need to complete GetPictureData");
						Nuclear::NuclearPictureInfo picinfo;
						pRenderer->GetPictureInfo(pic.pBigPic->handle, picinfo);
						NuclearRect rect(pic.offset);
						rect.Widen(r);
						if( rect.PtInRect(realtestloc) )
						{
							NuclearRect getrect;
							getrect.right = realtestloc.x - pic.offset.left + r + 1;
							getrect.bottom = realtestloc.y - pic.offset.top + r + 1;
							int tmpLength = r + r + 1;
							getrect.left = getrect.right - tmpLength;
							getrect.top = getrect.bottom - tmpLength;
							if (getrect.left < 0)
								getrect.left = 0;
							if (getrect.top < 0)
								getrect.top = 0;
							if (getrect.right > pic.offset.Width())
								getrect.right = pic.offset.Width();
							if (getrect.bottom > pic.offset.Height())
								getrect.bottom = pic.offset.Height();
							int offset = static_cast<int>(pic.srcrect.left * picinfo.m_nPicWidth);
							getrect.left += offset;
							getrect.right += offset;
							offset = static_cast<int>(pic.srcrect.top * picinfo.m_nPicHeight);
							getrect.top += offset;
							getrect.bottom += offset;
							int count = getrect.Width() * getrect.Height();
							if( picinfo.m_texfmt == XPTEXFMT_A8R8G8B8 || picinfo.m_texfmt == XPTEXFMT_DXT1
								|| picinfo.m_texfmt == XPTEXFMT_DXT2
								|| picinfo.m_texfmt == XPTEXFMT_DXT3
								|| picinfo.m_texfmt == XPTEXFMT_DXT4
								|| picinfo.m_texfmt == XPTEXFMT_DXT5)
							{	
								unsigned int colordata[25];
								if( pRenderer->GetPictureData(pic.pBigPic->handle, colordata, sizeof(unsigned int) * count, &getrect))
								{
									unsigned int alphalevel = static_cast<unsigned int>(256 * testval); 
									for (int i=0;i<count;++i)
									{
										if ((colordata[i]>>24) >= alphalevel)
											return true;
									}
								}
							}
							else if( picinfo.m_texfmt == XPTEXFMT_A4R4G4B4 )
							{
								unsigned short colordata[25];
								if( pRenderer->GetPictureData(pic.pBigPic->handle, colordata, sizeof(unsigned short) * count, &getrect))
								{
									unsigned short alphalevel = static_cast<unsigned short>(16 * testval); 
									for (int i=0;i<count;++i)
									{
										if ((colordata[i]>>12) >= alphalevel)
											return true;
									}
								}
							}
							else
							{
								XPTRACE(L"warning, unknown texturefmt, %d\n", picinfo.m_texfmt);
							}
						}
					}
				}
			}
		}
		return false;
	}

	bool XAni::TestDir(int dir) const 
	{ 
		switch( m_dirmode )
		{
		case XPANIDM_8USE1:
		case XPANIDM_8FROM5:
		case XPANIDM_8FROM1:
		case XPANIDM_8:
			return true;
		case XPANIDM_4:
		case XPANIDM_3:
        case XPANIDM_2:
        case XPANIDM_B2FROM1:
			{
				XPicMap::const_iterator it = m_pics.find(dir);
				return it != m_pics.end();
			}
		default:
			break;
		}
		return false;
	}

	int XAni::GetDirMask() const
	{
		switch( m_dirmode )
		{
		case XPANIDM_8USE1:
		case XPANIDM_8FROM5:
		case XPANIDM_8FROM1:
		case XPANIDM_8:
			return 0xff;
		case XPANIDM_4:
			return 0xaa;
		case XPANIDM_3:
			return 0x38;
        case XPANIDM_2:
            return 0x88;
        case XPANIDM_B2FROM1:
            return 0x28;
		default:
			break;
		}
		return 0;
	}

	bool XAni::assureLoadPic(XPic &pic) const
	{
		bool bRet1 = false, bRet2 = false;
		NuclearFileIOManager *pFIOMan = GetEngine()->GetFileIOManager();
		Renderer *pRenderer = GetEngine()->GetRenderer();

		if (pic.pBigPic->handle != INVALID_PICTURE_HANDLE)
		{
			Nuclear::NuclearPictureInfo picinfo;
			if (pRenderer->GetPictureInfo(pic.pBigPic->handle, picinfo))
			{
				bRet1 = true;
			}
		}
		if (!bRet1)
		{
			bool bResult1 = false;
			NuclearBuffer buffer;
			if (!pFIOMan->GetFileImage(pic.pBigPic->filename, buffer, false))
			{
				wprintf(L"load xappicfile failed!, path is %ls\n", pic.pBigPic->filename.c_str());
				wprintf(L"load xappicfile failed!, path is %ls\n", pic.pBigPic->filename.c_str());
			}

			if (buffer.size() > 0)
			{
				NuclearPictureInfo picinfo;
				picinfo.fileuri = pic.pBigPic->filename;
				pic.pBigPic->handle = pRenderer->LoadPictureFromMem(buffer.constbegin(), buffer.size(), &picinfo, GetTexFmt(), XPPOOL_MANAGED, pRenderer->CacheInMemory(), 0);
				if (pRenderer->CacheInMemory() == false)
					pRenderer->CachePicture(pic.pBigPic->handle, pic.pBigPic->filename);
				pic.pBigPic->loaded = true;
				bResult1 = true;
				bRet1 = true;
			}

			if (!bResult1)
			{
				if (pic.pBigPic->handle != INVALID_PICTURE_HANDLE)
				{
					pRenderer->FreePicture(pic.pBigPic->handle);
					pic.pBigPic->handle = INVALID_PICTURE_HANDLE;
				}
			}
		}

		if (!pic.pBigPicPart)
			bRet2 = true;

		if (pic.pBigPicPart && pic.pBigPicPart->handle != INVALID_PICTURE_HANDLE)
		{
			Nuclear::NuclearPictureInfo picinfo;
			if (pRenderer->GetPictureInfo(pic.pBigPicPart->handle, picinfo))
			{
				bRet2 = true;
			}
		}

		if (pic.pBigPicPart && !bRet2)
		{
			bool bResult2 = false;
			NuclearBuffer buffer;
			if (!pFIOMan->GetFileImage(pic.pBigPicPart->filename, buffer, false))
			{
				wprintf(L"load xappicfile failed!, path is %ls\n", pic.pBigPicPart->filename.c_str());
				wprintf(L"load xappicfile failed!, path is %ls\n", pic.pBigPicPart->filename.c_str());
			}

			if (buffer.size() > 0)
			{
				NuclearPictureInfo picinfo;
				picinfo.fileuri = pic.pBigPicPart->filename;
				pic.pBigPicPart->handle = pRenderer->LoadPictureFromMem(buffer.constbegin(), buffer.size(), &picinfo, GetTexFmt(), XPPOOL_MANAGED, pRenderer->CacheInMemory());
				if (pRenderer->CacheInMemory() == false)
					pRenderer->CachePicture(pic.pBigPicPart->handle, pic.pBigPicPart->filename);
				pic.pBigPicPart->loaded = true;
				bResult2 = true;
				bRet2 = true;
			}

			if (!bResult2)
			{
				if (pic.pBigPicPart->handle != INVALID_PICTURE_HANDLE)
				{
					pRenderer->FreePicture(pic.pBigPicPart->handle);
					pic.pBigPicPart->handle = INVALID_PICTURE_HANDLE;
				}
			}
		}

		return bRet1 && bRet2;
	}

	bool XAni::GetPic(int dir, int seq, int reg, XPic &pic)
	{
		if (reg < 0 || reg >= m_nRegion) return false;

		switch (m_dirmode)
		{
		case XPANIDM_8USE1:
			dir = XPDIR_TOP;
			break;
		case XPANIDM_8FROM1:
			dir = XPDIR_RIGHT;
			break;
		case XPANIDM_8FROM5:
			if (dir == XPDIR_BOTTOMLEFT)
				dir = XPDIR_BOTTOMRIGHT;
			else if (dir == XPDIR_LEFT)
				dir = XPDIR_RIGHT;
			else if (dir == XPDIR_TOPLEFT)
				dir = XPDIR_TOPRIGHT;
			break;
		case XPANIDM_B2FROM1:
			if (dir == XPDIR_BOTTOMLEFT)
				dir = XPDIR_BOTTOMRIGHT;
			break;
		case XPANIDM_4FROM2:
			if (dir == XPDIR_BOTTOMLEFT)
				dir = XPDIR_BOTTOMRIGHT;
			else if (dir == XPDIR_TOPRIGHT)
				dir = XPDIR_TOPLEFT;
			break;
		default:
			break;
		}

		XPicMap::iterator it = m_pics.find(dir);
		if (it == m_pics.end())
			return false;

		VPic &v = it->second;
		if (seq < 0 || seq >= (int)v.size())
			return false;

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
		Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());
		if (pEngine->GetStepLoadTexture())
		{
			if (v[seq][reg].pBigPic->handle == INVALID_PICTURE_HANDLE && pEngine->GetCurDeltaTime() > LoadPicTimeLimit && seq > 0)
			{
				if (v[seq-1][reg].pBigPic->handle != INVALID_PICTURE_HANDLE)
				{
					pic = v[seq-1][reg];
					m_usingdirs.insert(std::make_pair(dir, dir));
					return true;
				}
			}
		}
#endif
		pic = v[seq][reg];
		m_usingdirs.insert(std::make_pair(dir, dir));

		bool bBigPic = pic.pBigPic && !pic.pBigPic->asyncLoad && !pic.pBigPic->loaded;
		bool bBigPicPart = pic.pBigPicPart && !pic.pBigPicPart->asyncLoad && !pic.pBigPicPart->loaded;
		if (bBigPic || bBigPicPart)
		{//如果同步加载还没有加载到所需纹理资源,开始加载所需纹理
#if (defined WIN32) && (defined XP_PERFORMANCE)
			int64_t begin = Nuclear::GetMilliSeconds();
#endif
			if (assureLoadPic(v[seq][reg]))
			{
				m_state = XAni::XPANISTATE_PICTURE;
			}
#if (defined WIN32) && (defined XP_PERFORMANCE)
			int delta = int(Nuclear::GetMilliSeconds() - begin);
			std::string strOut = "[XAni::GetPic:";
			strOut += ws2s(pic.pBigPic->filename) + "] time:";
			strOut += StringCover::intToString(delta);
			strOut += '\n';
			if (delta > 0) ::OutputDebugStringA(strOut.c_str());
#endif
		}

		return true;
	}

	bool XAni::SetPic(int dir, int seq, int reg, const XPic& pic)
	{
		if( reg < 0 || reg >= m_nRegion ) return false;
		switch (m_dirmode)
		{
            case XPANIDM_8USE1:
                dir = XPDIR_TOP;
                break;
            case XPANIDM_8FROM1:
                dir = XPDIR_RIGHT;
                break;
            case XPANIDM_8FROM5:
                if( dir == XPDIR_BOTTOMLEFT )
                    dir = XPDIR_BOTTOMRIGHT;
                else if( dir == XPDIR_LEFT )
                    dir = XPDIR_RIGHT;
                else if( dir == XPDIR_TOPLEFT )
                    dir = XPDIR_TOPRIGHT;
                break;
            case XPANIDM_B2FROM1:
                if( dir == XPDIR_BOTTOMLEFT )
                    dir = XPDIR_BOTTOMRIGHT;
				break;
			case XPANIDM_4FROM2:
				if (dir == XPDIR_BOTTOMLEFT)
					dir = XPDIR_BOTTOMRIGHT;
				else if (dir == XPDIR_TOPRIGHT)
					dir = XPDIR_TOPLEFT;
				break;

            default:
                break;
		}
		XPicMap::iterator it = m_pics.find(dir);
		if( it == m_pics.end() ) return false;
		VPic &v = it->second;
		if( seq < 0 || seq >= (int)v.size() ) return false;
		v[seq][reg] = pic;
        
		return true;
    }

	bool XAni::ExistInUsingDirs(int dir)
	{
		UsingDirMap::const_iterator it = m_usingdirs.find(dir);
		if (it != m_usingdirs.end())
		{
			return true;
		}
		return false;
	}

	void XAni::Update()
	{
		m_usingdirs.clear();
	}

	NuclearOStream& PAniPack::marshal(NuclearOStream &os) const
	{//save
		int dummy = VERSION;
		int i = 0, j = 0, k = 0;

		os << dummy;
		os << m_filefmt;
		os << m_texfmt;
		os << m_blend;
		os << m_time;
		os << m_nRegion;
		os << m_nFrame;
		os << m_dirmode;
		os << m_color;
		os << m_systemLevel;
		os << (int)m_bindType;

		os << m_base.left;
		os << m_base.right;
		os << m_center;
		os << m_border;

		for(i=0; i<(int)m_dirs.size(); ++i)
		{
			for(j=0; j<m_nFrame; ++j)
			{
				for(k=0; k<m_nRegion; ++k)
				{
					os << m_data[i][j][k].offset;
					os << m_data[i][j][k].strPicPath;
					os << m_data[i][j][k].rctData;

					int npt = (int)(m_data[i][j][k].outLinePoints.size());
					os << npt;
					for(int m=0; m<npt; m++)
						os << m_data[i][j][k].outLinePoints[m];
				}
			}
		}

		os << strPicPathPrefix;

		return os;
	}

	const INuclearStream& PAniPack::unmarshal(const INuclearStream &os)
	{//load
		int dummy;
		os >> dummy;
		if (dummy < 10)
		{
			throw Exception();
		}

		m_nXapVersion = dummy;

		int version = dummy;

		int i = 0, j = 0, k=0;

		os >> m_filefmt;
		os >> m_texfmt;
		os >> m_blend;			
		os >> m_time;
		os >> m_nRegion;
		os >> m_nFrame;
		os >> m_dirmode;
		os >> m_color;
		os >> m_systemLevel;
		int bindtype;
		os >> bindtype;
		m_bindType = static_cast<Nuclear_EffectBindType>(bindtype);
		NuclearPoint oldcenter(0, 0);
		if (dummy > VERSION_OLD_BASE)
		{
			os >> m_base.left;
			os >> m_base.right;
			if (dummy > VERSION_NO_CENTER)
			{
				os >> m_center;
			}
		} else {
			NuclearPoint unuse;
			os >> oldcenter;
			m_center = oldcenter;
			os >> m_base.left;
			os >> unuse;
			os >> m_base.right;
			os >> unuse;
		}
		os >> m_border;

		switch( m_dirmode )
		{
		case XPANIDM_3:
			m_dirs.push_back(3);
			m_dirs.push_back(4);
			m_dirs.push_back(5);
			break;
		case XPANIDM_4:
			m_dirs.push_back(1);
			m_dirs.push_back(3);
			m_dirs.push_back(5);
			m_dirs.push_back(7);
			break;
		case XPANIDM_8:
			m_dirs.push_back(0);
			m_dirs.push_back(1);
			m_dirs.push_back(2);
			m_dirs.push_back(3);
			m_dirs.push_back(4);
			m_dirs.push_back(5);
			m_dirs.push_back(6);
			m_dirs.push_back(7);
			break;
		case XPANIDM_8FROM5:
			m_dirs.push_back(0);
			m_dirs.push_back(1);
			m_dirs.push_back(2);
			m_dirs.push_back(3);
			m_dirs.push_back(4);
			break;
		case XPANIDM_8FROM1:
			m_dirs.push_back(2);
			break;
		case XPANIDM_8USE1:			
			m_dirs.push_back(0);
			break;
        case XPANIDM_2:
            m_dirs.push_back(3);
            m_dirs.push_back(7);
            break;
        case XPANIDM_B2FROM1:
            m_dirs.push_back(3);
            break;
		case XPANIDM_4FROM2:
			m_dirs.push_back(3);
			m_dirs.push_back(7);
			break;
		default:
			break;
		}

		m_data.resize(m_dirs.size());
		for(i=0; i<(int)m_dirs.size(); ++i)
		{
			m_data[i].resize(m_nFrame);
			for(j=0; j<m_nFrame; ++j)
			{
				m_data[i][j].resize(m_nRegion);
				for(k=0; k<m_nRegion;++k)
				{
					os >> m_data[i][j][k].offset;
					m_data[i][j][k].offset -= oldcenter;
					os >> m_data[i][j][k].strPicPath;
					os >> m_data[i][j][k].rctData;

					if(dummy > VERSION_13)
					{
						int npt = 0;
						os >> npt;
						m_data[i][j][k].outLinePoints.resize(npt);
						for(int m=0; m<npt; m++)
							os >> m_data[i][j][k].outLinePoints[m];
					}
					else
					{
						NuclearRect offset = m_data[i][j][k].offset;
						m_data[i][j][k].outLinePoints.push_back( NuclearPoint(0, 0) );
						m_data[i][j][k].outLinePoints.push_back( NuclearPoint(offset.Width()-1, 0) );
						m_data[i][j][k].outLinePoints.push_back( NuclearPoint(offset.Width()-1, offset.Height()-1) );
						m_data[i][j][k].outLinePoints.push_back( NuclearPoint(0, offset.Height()-1) );
					}
				}
			}
		}

		if (version > VERSION_14)
		{
			os >> strPicPathPrefix;
		}

		return os;
	}

	void AniManager::GarbageCollection(int nowtick)
	{
		for(ResourceMap::iterator it = m_rsrcmap.begin(), ie = m_rsrcmap.end(); it != ie;)
		{
			NuclearHardRef<XAni>& hardref = *it->second;
			if (hardref.GetHardRefCount() == 1)
			{
				int tick = nowtick - hardref.GetTick();
				if (tick > m_nXapGCTime)
				{
					FreeAni(*(hardref));
					if (!hardref->m_perpetualmem)
					{
						delete it->second;
						m_rsrcmap.erase(it++);
						continue;
					}
				}
				if (tick > m_nPicGCTime && hardref->GetState() > XAni::XPANISTATE_NOPICTURE)
				{
					FreeAniPic(*(hardref), true);
				}
				else if (hardref->GetReleaseFlag() == XPANI_RELEASE_IMMEDIATE_GC && hardref->GetState() > XAni::XPANISTATE_NOPICTURE)
                {
					FreeAniPic(*(hardref), false);
					hardref->SetReleaseFlag(XPANI_RELEASE_WAIT_GC);
                }
			}
			++it;
		}
	}
    
	void AniManager::GarbageCollectionAll(bool bImmediately)
	{
		if (!bImmediately)
		{
			if (m_nCooldown >= 0)
				return;

			m_nCooldown = m_nGcCooldown;
		}

        ResourceMap::iterator it = m_rsrcmap.begin(), ie = m_rsrcmap.end(); 
		while(it != ie)
		{
			if (it->second->GetHardRefCount() == 1)
			{
				FreeAni(*(*(it->second)));
				if (!(*(it->second))->m_perpetualmem)
				{
					delete it->second;
					m_rsrcmap.erase(it++);
					continue;
				}
			}
			++it;
		}
	}

	void AniManager::Destroy()
	{
		{
			m_LoadingUrls.clear();
		}
		{
			core::CMutex::Scoped autoLock(mMutexForLoadingXaps);
			m_LoadingXaps.clear();
		}

		for(ResourceMap::iterator it = m_rsrcmap.begin(), ie = m_rsrcmap.end(); it != ie; ++it)
		{
			//assert( it->second->GetHardRefCount() == 1 );//shield by yyy(没有必要，后面反正也要做删除操作)
			FreeAni(*(*(it->second)), true);
			delete it->second;
		}

		m_rsrcmap.clear();
	}

	void AniManager::RemoveLoadingNotify(const std::wstring &uri, AniLoadingNotify* pNotify)
	{
		{
			LoadingMap::iterator it = m_LoadingUrls.find(uri);
			if (it != m_LoadingUrls.end())
			{
				it->second.notifys.erase(pNotify);
				if (it->second.notifys.empty())
					m_LoadingUrls.erase(it);
			}
		}
		{
			core::CMutex::Scoped autoLock(mMutexForLoadingXaps);
			LoadingXapMap::iterator xIt = m_LoadingXaps.find(uri);
			if (xIt != m_LoadingXaps.end())
			{
				xIt->second.notifys.erase(pNotify);
				if (xIt->second.notifys.empty())
					m_LoadingXaps.erase(xIt);
			}
		}
	}

	bool AniManager::SyncLoadAni(NuclearHardRef<XAni>& ani, bool bLookupSegmPak, unsigned char bDye)
	{
		if (!ani.IsValid()) return false;

		if (ani->GetState() == XAni::XPANISTATE_NOPICTURE || ani->GetState() == XAni::XPANISTATE_BASE_PIC)
		{
			bool result1 = true, result2 = true;
			NuclearFileIOManager *pFIOMan = m_pEB->GetFileIOManager();
			Renderer *pRenderer = m_pEB->GetRenderer();
			{
				XAni::Buffers::iterator bit = ani->m_buffers.begin();
				for (; bit != ani->m_buffers.end(); ++bit)
				{
					XAni::XBigPic *pPic = *bit;
					if (pPic->handle != INVALID_PICTURE_HANDLE)
					{
						Nuclear::NuclearPictureInfo picinfo;
						if (pRenderer->GetPictureInfo(pPic->handle, picinfo)){
							continue;
						}
						else{
							result1 = false;
						}
					}

					NuclearBuffer buffer;
					if (!pFIOMan->GetFileImage(pPic->filename, buffer, false))
					{
						wprintf(L"load xappicfile failed!, path is %ls\n", pPic->filename.c_str());
						wprintf(L"load xappicfile failed!, path is %ls\n", pPic->filename.c_str());
					}
					if (buffer.size() > 0)
					{
						NuclearPictureInfo picinfo;
						picinfo.fileuri = pPic->filename;
						pPic->handle = pRenderer->LoadPictureFromMem(buffer.constbegin(), buffer.size(), &picinfo, ani->GetTexFmt(), XPPOOL_MANAGED, pRenderer->CacheInMemory());
						if (pRenderer->CacheInMemory() == false)
							pRenderer->CachePicture(pPic->handle, pPic->filename);
						pPic->loaded = true;
						result1 = true;
					}
				}
			}
			{
				XAni::Buffers::iterator bit = ani->m_buffersPart.begin();
				for (; bit != ani->m_buffersPart.end(); ++bit)
				{
					XAni::XBigPic *pPic = *bit;
					if (pPic->handle != INVALID_PICTURE_HANDLE)
					{
						Nuclear::NuclearPictureInfo picinfo;
						if (pRenderer->GetPictureInfo(pPic->handle, picinfo)){
							continue;
						}
						else{
							result2 = false;
						}
					}

					NuclearBuffer buffer;
					if (!pFIOMan->GetFileImage(pPic->filename, buffer, false))
					{
						wprintf(L"load xappicfile failed!, path is %ls\n", pPic->filename.c_str());
						wprintf(L"load xappicfile failed!, path is %ls\n", pPic->filename.c_str());
					}
					if (buffer.size() > 0)
					{
						NuclearPictureInfo picinfo;
						picinfo.fileuri = pPic->filename;
						pPic->handle = pRenderer->LoadPictureFromMem(buffer.constbegin(), buffer.size(), &picinfo, ani->GetTexFmt(), XPPOOL_MANAGED, pRenderer->CacheInMemory());
						if (pRenderer->CacheInMemory() == false)
							pRenderer->CachePicture(pPic->handle, pPic->filename);
						pPic->loaded = true;
						result2 = true;
					}
				}
			}
			if (result1 && result2)
			{
				ani->m_state = XAni::XPANISTATE_PICTURE;
			}
			else
			{
				{
					XAni::Buffers::iterator bit = ani->m_buffers.begin();
					for (; bit != ani->m_buffers.end(); ++bit)
					{
						XAni::XBigPic *pBigPic = *bit;
						if (pBigPic)
						{
							if (pBigPic->handle != INVALID_PICTURE_HANDLE)
							{
								m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
								pBigPic->handle = INVALID_PICTURE_HANDLE;
							}
						}
					}
				}
				{
					XAni::Buffers::iterator bit = ani->m_buffersPart.begin();
					for (; bit != ani->m_buffersPart.end(); ++bit)
					{
						XAni::XBigPic *pBigPic = *bit;
						if (pBigPic)
						{
							if (pBigPic->handle != INVALID_PICTURE_HANDLE)
							{
								m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
								pBigPic->handle = INVALID_PICTURE_HANDLE;
							}
						}
					}
				}

				if (!ani->m_perpetualmem)
					ani->m_state = XAni::XPANISTATE_NULL;
				else
					ani->m_state = XAni::XPANISTATE_NOPICTURE;
				return false;
			}
		}
		return true;
	}

	Nuclear_EffectAssureResResult AniManager::AsyncLoadAni(const std::wstring &uri, AniLoadingNotify* pNotify, bool bLookupSegmPak, unsigned char bDye)
	{
		NuclearHardRef<XAni>ani = GetAni(uri, true, bLookupSegmPak, false, bDye, pNotify);
     
		if (!ani.IsValid())
			return XPEARR_LOAD_ERROR;

		Renderer *pRenderer = GetEngine()->GetRenderer();
		switch (ani->GetState())
		{
		case XAni::XPANISTATE_NO_XAP:
			{
				core::CMutex::Scoped autoLock(mMutexForLoadingXaps);
				m_LoadingXaps[uri].notifys.insert(pNotify);
				return XPEARR_LOADING;
				break;
			}
		case XAni::XPANISTATE_NOPICTURE:
		case XAni::XPANISTATE_BASE_PIC:
			{
				LoadingMap::iterator it = m_LoadingUrls.find(uri);
				if (it == m_LoadingUrls.end())
				{
					NuclearFileIOManager *pFIOMan = m_pEB->GetFileIOManager();
					LoadingPicParam &param = m_LoadingUrls[uri];
					bool loading1 = false;
					bool loading2 = false;
					param.ani = ani;
					param.notifys.insert(pNotify);
					{
						//core::CMutex::Scoped autoLock(ani->mMutexForBuffers);
						XAni::Buffers::iterator bit = ani->m_buffers.begin();
						for (; bit != ani->m_buffers.end(); ++bit)
						{
							XAni::XBigPic *pPic = *bit;
							if (pRenderer->GetPictureHandle(pPic->handle, pPic->filename))
								continue;
							loading1 = true;
							param.remainPics.insert(pPic->filename);
							int iPixelConvert = 0;
							if (ani->m_buffersPart.size() > 0)
							{
								iPixelConvert = 1;
							}
							AniPicReadTask *pTask = new AniPicReadTask(pPic->filename, uri, pFIOMan, this, pPic, ani, bLookupSegmPak, iPixelConvert);
							if (!pTask->Submit())
							{
								delete pTask;
							}
						}
					}
					{
						XAni::Buffers::iterator bit = ani->m_buffersPart.begin(), bie = ani->m_buffersPart.end();
						for (; bit != bie; ++bit)
						{
							XAni::XBigPic *pPic = *bit;
							if (pRenderer->GetPictureHandle(pPic->handle, pPic->filename))
								continue;
							loading2 = true;
							param.remainPics.insert(pPic->filename);
							AniPicReadTask *pTask = new AniPicReadTask(pPic->filename, uri, pFIOMan, this, pPic, ani, bLookupSegmPak, 0);
							if (!pTask->Submit())
							{
								delete pTask;
							}
						}
					}
					if (!loading1 && !loading2)
					{
						//居然全部加载完了。。。
						AniManager::LoadingNotifySet::iterator sIt = param.notifys.begin(), sIe = param.notifys.end();
						for (; sIt != sIe; ++sIt)
						{
							(*sIt)->OnLoadAniReady(uri);
						}
						m_LoadingUrls.erase(uri);
						return XPEARR_RESREADY;
					}
				}
				else
				{
					it->second.notifys.insert(pNotify);
				}
				return XPEARR_LOADING;
				break;
			}
		case XAni::XPANISTATE_NULL:
			return XPEARR_LOAD_ERROR;
			break;
		case XAni::XPANISTATE_PICTURE:
			return XPEARR_RESREADY;
			break;
		default:
			XPASSERT(false && L"AsyncLoadAni");
			return XPEARR_LOAD_ERROR;
			break;
		}
		return XPEARR_RESREADY;
	}

	bool AniManager::syncLoadAni(NuclearHardRef<XAni>& ani, bool bLookupSegmPak)
	{
		PAniPack pack;
		const std::wstring uri = ani->GetAniUri();
		bool fIsLJFMPath = !uri.empty() && uri[0] == L'/';
		bool ok = false;
		if( fIsLJFMPath )
		{
			NuclearBuffer filedata;
			if( !m_pEB->GetFileIOManager()->GetFileImage(uri, filedata, bLookupSegmPak))
			{
				XPTRACE(L"  ani 资源加载失败, uri= %s\n", uri.c_str());
				XPLOG_ERROR(L"  ani 资源加载失败, uri= %s\n", uri.c_str());
				if(!m_pEB->GetFileIOManager()->GetFileImage(L"/cfg/error/discard.ani",filedata, false))
				{
					ani->m_state = XAni::XPANISTATE_NULL;
					return false;
				}
			}
			if (pack.LoadFromMem(filedata))
				ok = true;
		}
		else if (pack.LoadFromNativePath(uri.c_str()))
		{
			//用NativePath的，只有编辑器了吧
			ok = true;
		}

		if (!ok)
		{
			NuclearBuffer filedata;
			XPTRACE(L"  解析ani 资源失败, uri= %s\n", uri.c_str());
			XPLOG_ERROR(L"  解析ani 资源失败, uri= %s\n", uri.c_str());
			if(m_pEB->GetFileIOManager()->GetFileImage(L"/cfg/error/discard.ani", filedata, false) && pack.LoadFromMem(filedata))
				ok = true;	
		}

		if( ok )
		{
			std::wstring baseuri = uri.substr(0, uri.size() - 4);
			if( !LoadAniFromPack(*ani, pack, baseuri, false) )
			{
				XPTRACE(L"  解析ani 资源pack失败, uri= %s\n", uri.c_str());
				XPLOG_ERROR(L"  解析ani 资源pack失败, uri= %s\n", uri.c_str());
				ani->m_state = XAni::XPANISTATE_NULL;
				return false;
			}
			ani->m_state = XAni::XPANISTATE_NOPICTURE;
			return true;
		}

		ani->m_state = XAni::XPANISTATE_NULL;

		return false;
	}

	NuclearWeakRef<XAni> AniManager::GetAni(const std::wstring &uri, bool async, bool bLookupSegmPak, bool bPerpetualMem, unsigned char bDye, AniLoadingNotify* pNotify)
	{
#if (defined WIN32) && (defined XP_PERFORMANCE)
		int64_t begin = Nuclear::GetMilliSeconds();
#endif
		ResourceMap::iterator it = m_rsrcmap.find(uri);
		if( it != m_rsrcmap.end() )
		{
			NuclearHardRef<XAni> &hardref = *it->second;
			if (!bPerpetualMem && hardref->m_perpetualmem && hardref->m_state == XAni::XPANISTATE_NOPICTURE)
			{
				SyncLoadAni(hardref, bLookupSegmPak);
				it->second->SetTick(m_pEB->GetTick());
			}
			if (!async && hardref->m_state == XAni::XPANISTATE_NO_XAP)//不管同步还是异步，都转为同步加载
			{
				syncLoadAni(hardref, bLookupSegmPak);
			}
			if (async && pNotify != NULL)		//异步加载的情况下
			{
				switch (hardref->m_state)
				{
					case XAni::XPANISTATE_NO_XAP:
					case XAni::XPANISTATE_NOPICTURE:
					{
						core::CMutex::Scoped autoLock(mMutexForLoadingXaps);
						m_LoadingXaps[uri].notifys.insert(pNotify);
						break;
					}
					case XAni::XPANISTATE_BASE_PIC:
					{
						LoadingMap::iterator it = m_LoadingUrls.find(uri);
						if (it != m_LoadingUrls.end())
						{
							it->second.notifys.insert(pNotify);
						}
						break;
					}
				}
			}
			return hardref;
		}

		XAni* pXAni = new XAni(uri, bPerpetualMem);
		pXAni->m_bDyeEnable = bDye;
		NuclearHardRef<XAni> *pHardRef = new NuclearHardRef<XAni>(pXAni);
		pHardRef->SetTick(m_pEB->GetTick());

		if (async)
		{
			pXAni->m_state = XAni::XPANISTATE_NO_XAP;
			core::CMutex::Scoped autoLock(mMutexForLoadingXaps);
			m_LoadingXaps[uri].notifys.clear();//清除未加载完的加载队列
			XapReadTask* pTask = new XapReadTask(uri, m_pEB->GetFileIOManager(), this, *pHardRef, bLookupSegmPak);
			if (!pTask->Submit())
			{
				delete pTask;
			}
		}
		else
		{
			if (!syncLoadAni(*pHardRef, bLookupSegmPak))
			{
				delete pHardRef;
				return NuclearHardRef<XAni>();
			}
		}

		m_rsrcmap[uri] = pHardRef;

#if (defined WIN32) && (defined XP_PERFORMANCE)
		int delta = int(Nuclear::GetMilliSeconds() - begin);
		std::string strOut = "[AniManager::GetAni:";
		strOut += ws2s(uri) + "] time:";
		strOut += StringCover::intToString(delta);
		strOut += '\n';
		if (delta > 0) ::OutputDebugStringA(strOut.c_str());
#endif
		return *pHardRef;
	}

	bool AniManager::LoadAniFromPack(XAni &ani, const PAniPack &pack, const std::wstring &packbaseuri, bool async)
	{
#if (defined WIN32) && (defined XP_PERFORMANCE)
		int64_t begin = Nuclear::GetMilliSeconds();
#endif
		core::CMutex::Scoped autoLock(mMutexForLoadingXaps);
		ani.m_filefmt = (NuclearImageFileFormat)pack.m_filefmt;
		ani.m_texfmt = (NuclearTextureFormat)pack.m_texfmt;
		ani.m_blend	= (NuclearTextureBlendMode)pack.m_blend;
		ani.m_time = pack.m_time;
		ani.m_nRegion = pack.m_nRegion;
		ani.m_nFrame = pack.m_nFrame;
		ani.m_base = pack.m_base;
		ani.m_border = pack.m_border;
		ani.m_bindType = pack.m_bindType;
		ani.m_sysLevel = pack.m_systemLevel;

		ani.m_dirmode = (XPANI_DIRECTIONMODE)pack.m_dirmode;
		std::map<std::wstring, XAni::XBigPic*> bigPicMap, bigPicMapPart;
		std::map<std::wstring, XAni::XBigPic*>::iterator bigPicIt, bigPicEnd;
		if ((int)pack.m_dirs.size() > 8)
		{
			XPLOG_ERROR(L"LoadAni %s dir.size is %d\n", packbaseuri.c_str(), (int)pack.m_dirs.size());
		}

		std::wstring strPicPathPrefix;
		if (pack.strPicPathPrefix.empty())
		{
			strPicPathPrefix = packbaseuri;
		}
		else
		{
			size_t lastSplashDot = packbaseuri.find_last_of(L"/");
			strPicPathPrefix = packbaseuri.substr(0, lastSplashDot + 1) + pack.strPicPathPrefix;
		}
		
		int i = 0;
		for(i= 0; i< (int)pack.m_dirs.size(); ++i)
		{
			int dir = pack.m_dirs[i];
			XAni::VPic &v = ani.m_pics[dir];
			v.resize(ani.m_nFrame);

			for(int j=0; j< ani.m_nFrame;++j)
			{
				v[j].resize(ani.m_nRegion);
			}
			
			if (ani.m_nRegion > 2)
			{
				XPLOG_ERROR(L"LoadAni %s ani.m_nRegion is %d\n", packbaseuri.c_str(), ani.m_nRegion);
			}

			for(int k=0; k<ani.m_nRegion; ++k)
			{
				if (ani.m_nFrame > 1000)
				{
					XPLOG_ERROR(L"LoadAni %s ani.m_nFrame is %d\n", packbaseuri.c_str(), ani.m_nFrame);
				}
				for(int j=0; j< ani.m_nFrame; ++j)
				{
					XAni::XPic &xpic = v[j][k];
					const PAniPack::FileSec &file = pack.m_data[i][j][k];
					bigPicIt = bigPicMap.find(file.strPicPath);
					if (bigPicIt == bigPicMap.end())
					{
						xpic.pBigPic = new XAni::XBigPic();
						xpic.pBigPic->dir = dir;
						xpic.pBigPic->filename = strPicPathPrefix + file.strPicPath;
						xpic.pBigPic->asyncLoad = async;
						bigPicMap[file.strPicPath] = xpic.pBigPic;

						if (ani.m_bDyeEnable > 1)
						{//加载染色分块图
							std::wstring wsPicPathPath;
							wsPicPathPath = strPicPathPrefix + file.strPicPath + L"part";
							LJFM::LJFMF cfile;
							if (cfile.Open(wsPicPathPath, LJFM::FM_EXCL, LJFM::FA_RDONLY))
							{
								xpic.pBigPicPart = new XAni::XBigPic();
								xpic.pBigPicPart->dir = dir;
								xpic.pBigPicPart->filename = wsPicPathPath;
								xpic.pBigPicPart->asyncLoad = async;
								bigPicMapPart[file.strPicPath] = xpic.pBigPicPart;
							}
						}
					}
					else
					{
						xpic.pBigPic = bigPicIt->second;
						if (!bigPicMapPart.empty())
						{
							bigPicIt = bigPicMapPart.find(file.strPicPath);
							if (bigPicIt != bigPicMapPart.end())
							{
								xpic.pBigPicPart = bigPicIt->second;
							}
						}
					}
					xpic.srcrect = file.rctData;
					xpic.offset = file.offset;

					//版本14以前的ani里没有记录outLinePoints信息--在读文件的时候初始化为小图片矩形的4个点.
					if (pack.m_data[i][j][k].outLinePoints.size() > 500)
					{
						XPLOG_ERROR(L"LoadAni %s pack.m_data[%d][%d][%d].outLinePoints.size is %d\n", packbaseuri.c_str(), i, j, k, (int)pack.m_data[i][j][k].outLinePoints.size());
					}
					xpic.outLine.m_points = pack.m_data[i][j][k].outLinePoints;
				}
			}
		}

		//让ani.m_buffers的顺序是各方向的第一帧排前一点
		//core::CMutex::Scoped autoLock(ani.mMutexForBuffers);
		for(i= 0; i< (int)pack.m_dirs.size(); ++i)
		{	
			for (int k = 0; k<ani.m_nRegion; ++k)
			{
				if (ani.m_nFrame > 0)
				{
					const PAniPack::FileSec &file = pack.m_data[i][0][k];
					bigPicIt = bigPicMap.find(file.strPicPath);
					if (bigPicIt != bigPicMap.end())
					{
						ani.m_buffers.push_back(bigPicIt->second);
						bigPicMap.erase(bigPicIt);
					}
				}
			}
		}
		bigPicIt = bigPicMap.begin();
		bigPicEnd = bigPicMap.end();
		for (bigPicIt, bigPicEnd; bigPicIt != bigPicEnd; ++bigPicIt)
		{
			ani.m_buffers.push_back(bigPicIt->second);
		}

		//让ani.m_buffersPart的顺序是各方向的第一帧排前一点
		for (int i = 0; i < (int)pack.m_dirs.size(); ++i)
		{
			for (int k = 0; k < ani.m_nRegion; ++k)
			{
				if (ani.m_nFrame > 0)
				{
					const PAniPack::FileSec &file = pack.m_data[i][0][k];
					bigPicIt = bigPicMapPart.find(file.strPicPath);
					if (bigPicIt != bigPicMapPart.end())
					{
						ani.m_buffersPart.push_back(bigPicIt->second);
						bigPicMapPart.erase(bigPicIt);
					}
				}
			}
		}
		bigPicIt = bigPicMapPart.begin();
		bigPicEnd = bigPicMapPart.end();
		for (bigPicIt, bigPicEnd; bigPicIt != bigPicEnd; ++bigPicIt)
		{
			ani.m_buffersPart.push_back(bigPicIt->second);
		}

		ani.m_state = XAni::XPANISTATE_NOPICTURE;
#if (defined WIN32) && (defined XP_PERFORMANCE)
		int delta = int(Nuclear::GetMilliSeconds() - begin);
		std::string strOut = "[AniManager::LoadAniFromPack:";
		strOut += ws2s(packbaseuri) + "] time:";
		strOut += StringCover::intToString(delta);
		strOut += '\n';
		if (delta > 0) ::OutputDebugStringA(strOut.c_str());
#endif
		return true;
	}

	AniManager::AniManager(EngineBase *pEB) : m_pEB(pEB), m_nPicGCTime(60000), m_nXapGCTime(180000), m_nGcCooldown(10000), m_nCooldown(0)
	{
		m_bStepLoadTexture = false;
	}

	void AniManager::FreeAni(XAni &ani, bool straight)
	{
		if (straight || !ani.m_perpetualmem)
		{
			for (XAni::Buffers::iterator it = ani.m_buffers.begin(), ie = ani.m_buffers.end(); it != ie; ++it)
			{
				XAni::XBigPic *pBigPic = *it;
				if (pBigPic)
				{
					if (pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
						pBigPic->handle = INVALID_PICTURE_HANDLE;
					}
					delete pBigPic;
				}
			}
			ani.m_buffers.clear();

			for (XAni::Buffers::iterator it = ani.m_buffersPart.begin(), ie = ani.m_buffersPart.end(); it != ie; ++it)
			{
				XAni::XBigPic *pBigPic = *it;
				if (pBigPic)
				{
					if (pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
						pBigPic->handle = INVALID_PICTURE_HANDLE;
					}
					delete pBigPic;
				}
			}
			ani.m_buffersPart.clear();

			ani.m_pics.clear();
			ani.m_nRegion = 0;
			ani.m_nFrame = 0;
			ani.m_dirmode = XPANIDM_NULL;
			ani.m_state = XAni::XPANISTATE_NULL;
		}
		else
		{
			for (XAni::Buffers::iterator it = ani.m_buffers.begin(), ie = ani.m_buffers.end(); it != ie; ++it)
			{
				XAni::XBigPic *pBigPic = *it;
				if (pBigPic)
				{
					if (pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
						pBigPic->handle = INVALID_PICTURE_HANDLE;
					}
				}
			}

			for (XAni::Buffers::iterator it = ani.m_buffersPart.begin(), ie = ani.m_buffersPart.end(); it != ie; ++it)
			{
				XAni::XBigPic *pBigPic = *it;
				if (pBigPic)
				{
					if (pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
						pBigPic->handle = INVALID_PICTURE_HANDLE;
					}
				}
			}
			ani.m_state = XAni::XPANISTATE_NOPICTURE;
		}
	}

	void AniManager::FreeAniPic(XAni &ani, bool bCullUsingDir)
	{
		if (ani.m_state < XAni::XPANISTATE_NOPICTURE)
			return;

		if (!bCullUsingDir)
		{
			//core::CMutex::Scoped autoLock(ani.mMutexForBuffers);
			for (XAni::Buffers::iterator it = ani.m_buffers.begin(), ie = ani.m_buffers.end(); it != ie; ++it)
			{
				XAni::XBigPic *pBigPic = *it;
				if (pBigPic)
				{
					if (pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
						pBigPic->handle = INVALID_PICTURE_HANDLE;
						pBigPic->loaded = false;
						pBigPic->asyncLoad = false;
					}
				}
			}

			for (XAni::Buffers::iterator it = ani.m_buffersPart.begin(), ie = ani.m_buffersPart.end(); it != ie; ++it)
			{
				XAni::XBigPic *pBigPic = *it;
				if (pBigPic)
				{
					if (pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
						pBigPic->handle = INVALID_PICTURE_HANDLE;
						pBigPic->loaded = false;
						pBigPic->asyncLoad = false;
					}
				}
			}
		}
		else
		{
			//core::CMutex::Scoped autoLock(ani.mMutexForBuffers);
			for (XAni::Buffers::iterator it = ani.m_buffers.begin(), ie = ani.m_buffers.end(); it != ie; ++it)
			{
				XAni::XBigPic *pBigPic = *it;
				if (pBigPic)
				{
					if (pBigPic->handle != INVALID_PICTURE_HANDLE && !ani.ExistInUsingDirs(pBigPic->dir))
					{
						m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
						pBigPic->handle = INVALID_PICTURE_HANDLE;
						pBigPic->loaded = false;
						pBigPic->asyncLoad = false;
					}
				}
			}

			for (XAni::Buffers::iterator it = ani.m_buffersPart.begin(), ie = ani.m_buffersPart.end(); it != ie; ++it)
			{
				XAni::XBigPic *pBigPic = *it;
				if (pBigPic)
				{
					if (pBigPic->handle != INVALID_PICTURE_HANDLE && !ani.ExistInUsingDirs(pBigPic->dir))
					{
						m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
						pBigPic->handle = INVALID_PICTURE_HANDLE;
						pBigPic->loaded = false;
						pBigPic->asyncLoad = false;
					}
				}
			}
		}
		ani.m_state = XAni::XPANISTATE_NOPICTURE;
	}
    
    void AniManager::Run(int delta)
    {
        if (m_nCooldown >= 0)
		{
            m_nCooldown -= delta;
        }
    }

	void AniManager::Update()
	{
		ResourceMap::iterator it = m_rsrcmap.begin();
		for (; it != m_rsrcmap.end(); ++it)
		{
			(*it->second)->Update();
		}
	}

}