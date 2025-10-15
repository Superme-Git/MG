#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\fileiomanager.h"
#include "..\renderer\renderer.h"
#include "animanager.h"
#include "enginebase.h"

namespace Nuclear
{

	class AniPicReadTask : public CFileIOManager::AsyncReadTask
	{
		AniManager* m_pAniManager;
		XAni::XBigPic* m_pPic;
		std::wstring m_xapuri;
		XPTEXTURE_FORMAT m_texfmt;
		int m_tick;
	public:
		AniPicReadTask(const std::wstring &filename, const std::wstring &xapuri, CFileIOManager *pFileIOMan, AniManager *pAniMan, XAni::XBigPic* pBigPic, XPTEXTURE_FORMAT texfmt)
			: CFileIOManager::AsyncReadTask(pFileIOMan, filename, 0), m_xapuri(xapuri), m_pAniManager(pAniMan), m_pPic(pBigPic), m_texfmt(texfmt)
		{
			m_tick = GetMilliSeconds();
		}

		virtual void OnReady()
		{
			int tick = GetMilliSeconds();
			if( m_data.size() == 0 )
			{
				XPLOG_ERROR(L"load xappicfile failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load xappicfile failed!, path is %s\n", m_filename.c_str());
			} else {
				m_pFileIOMan->OnReady(tick-m_tick, m_data.size());
				XPTRACE(L"load xappicfile ok!, path is %s\n", m_filename.c_str());
				if (m_pPic->handle == INVALID_PICTURE_HANDLE)
				{
					m_pPic->handle = m_pAniManager->m_pEB->GetRenderer()->LoadPictureFromMem(m_data.constbegin(), 
						m_data.size(), NULL, m_texfmt, XPPOOL_MANAGED);//可以考虑用XPPOOL_VIDEOMEM
				}
			}
			AniManager::LoadingMap::iterator it = m_pAniManager->m_LoadingUrls.find(m_xapuri);
			if (it != m_pAniManager->m_LoadingUrls.end())
			{
				if( m_pPic->handle == INVALID_PICTURE_HANDLE )
				{
					it->second.ani->m_state = XAni::XPANISTATE_NULL;
				}
				it->second.remainPics.erase(m_filename);
				if (it->second.remainPics.size() == 0)//全部加载完了
				{
					if (it->second.ani->m_state == XAni::XPANISTATE_NULL)
					{
						m_pAniManager->FreeAni(*it->second.ani);
					} else { 
						it->second.ani->m_state = XAni::XPANISTATE_PICTURE;
					}
					AniManager::LoadingNotifySet::iterator sIt = it->second.notifys.begin(), sIe = it->second.notifys.end();
					for (;sIt!=sIe;++sIt)
					{
						(*sIt)->OnLoadAniReady(m_xapuri);
					}
					m_pAniManager->m_LoadingUrls.erase(it);
				} else {
					if (*it->second.ani->m_buffers.begin() == m_pPic && it->second.ani->m_state != XAni::XPANISTATE_NULL)
					{
						it->second.ani->m_state = XAni::XPANISTATE_BASE_PIC;
						AniManager::LoadingNotifySet::iterator sIt = it->second.notifys.begin(), sIe = it->second.notifys.end();
						for (;sIt!=sIe;++sIt)
						{
							(*sIt)->OnLoadAniBaseReady(m_xapuri);
						}
					}
				}
			} else {
				XPLOG_ERROR(L"AniReadTask On Ready error, path is %s\n", m_filename.c_str());
			}
			delete this;
		}

		virtual void OnDiscard() 
		{
			AniManager::LoadingMap::iterator it = m_pAniManager->m_LoadingUrls.find(m_xapuri);
			if (it != m_pAniManager->m_LoadingUrls.end())
			{
				it->second.remainPics.erase(m_filename);
			} else {
				XPLOG_ERROR(L"AniReadTask On Discard error, path is %s\n", m_filename.c_str());
			}
			delete this;
		}
		virtual ~AniPicReadTask() { }
	};

	class XapReadTask : public CFileIOManager::AsyncReadTask
	{
		AniManager* m_pAniManager;
		XHardRef<XAni> m_ani;
		int m_tick;
	public:
		XapReadTask(const std::wstring &filename, CFileIOManager *pFileIOMan, AniManager *pAniMan, XHardRef<XAni> ani)
			: CFileIOManager::AsyncReadTask(pFileIOMan, filename, 0), m_pAniManager(pAniMan), m_ani(ani)
		{
			m_tick = GetMilliSeconds();
		}

		virtual void OnReady()
		{
			int tick = GetMilliSeconds();
			if( m_data.size() == 0 )
			{
				XPLOG_ERROR(L"load ani failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load ani failed!, path is %s\n", m_filename.c_str());
				m_ani->m_state = XAni::XPANISTATE_NULL;
			} else {
				m_pFileIOMan->OnReady(tick-m_tick, m_data.size());
				XPTRACE(L"load ani ok!, path is %s\n", m_filename.c_str());
			}
			AniManager::LoadingXapMap::iterator it = m_pAniManager->m_LoadingXaps.find(m_filename);
			if (it != m_pAniManager->m_LoadingXaps.end())
			{
				if (m_ani->m_state == XAni::XPANISTATE_NO_XAP || m_ani->m_state == XAni::XPANISTATE_NULL)
				{
					PAniPack pack;
					if (!pack.LoadFromMem(m_data))
					{
						XPTRACE(L"  解析ani 资源失败, uri= %s\n", m_filename.c_str());
						XPLOG_ERROR(L"  解析ani 资源失败, uri= %s\n", m_filename.c_str());
						if (!(m_pFileIOMan->GetFileImage(std::wstring(L"/error/error") + SFI_ExtName, m_data) && pack.LoadFromMem(m_data)))
						{
							m_ani->m_state = XAni::XPANISTATE_NULL;
							return;
						}
					}
					std::wstring baseuri = m_filename.substr(0, m_filename.size() - 4);
					if( !m_pAniManager->LoadAniFromPack(*m_ani, pack, baseuri) )
					{
						XPTRACE(L"  解析ani 资源pack失败, uri= %s\n", m_filename.c_str());
						XPLOG_ERROR(L"  解析ani 资源pack失败, uri= %s\n", m_filename.c_str());
						m_ani->m_state = XAni::XPANISTATE_NULL;
						return;
					}
				}

				if (it->second.notifys.size() > 0)
				{
					AniManager::LoadingPicParam &param = m_pAniManager->m_LoadingUrls[m_filename];
					param.ani = m_ani;
					XPASSERT(param.notifys.size() == 0);
					param.notifys.swap(it->second.notifys);
					XAni::Buffers::iterator bit = m_ani->m_buffers.begin(), bie = m_ani->m_buffers.end();
					bool loading = false;
					for (;bit!=bie;++bit)
					{
						XAni::XBigPic *pPic = *bit;
						if (pPic->handle != INVALID_PICTURE_HANDLE)
							continue;
						loading = true;
						param.remainPics.insert(pPic->filename);
						AniPicReadTask *pTask = new AniPicReadTask(pPic->filename, m_filename, m_pFileIOMan, m_pAniManager, pPic, m_ani->GetTexFmt());
						pTask->Submit();
					}
					if (!loading)
					{
						//居然全部加载完了。。。
						AniManager::LoadingNotifySet::iterator sIt = param.notifys.begin(), sIe = param.notifys.end();
						for (;sIt!=sIe;++sIt)
						{
							(*sIt)->OnLoadAniReady(m_filename);
						}
						m_pAniManager->m_LoadingUrls.erase(m_filename);
					}
				}
				m_pAniManager->m_LoadingXaps.erase(it);
			} else {
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
	bool XAni::TestPoint(int dir, int frame, const CPOINT &pt, XPMOUSE_TEST_MODE mode, Renderer *pRenderer, int r, float testval)
	{
		//从逻辑上来说，如果mode是XPTM_PIC_RECT，那么不会调用进来，在Sprite或者Immovableobj就已经return了
		XPASSERT(mode != XPTM_PIC_RECT);
		XPic pic;
		XPBase base;
		if( GetBase(base) )
		{
			for(int k=0;k<m_nRegion;++k)
			{
				if( GetPic(dir, frame, k, pic) )
				{
					Location realtestloc = pt;
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
						Nuclear::PictureInfo picinfo;
						pRenderer->GetPictureInfo(pic.pBigPic->handle, picinfo);
						CRECT rect(pic.offset);
						rect.Widen(r);
						if( rect.PtInRect(realtestloc) )
						{
							CRECT getrect;
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
		case XPANIDM_4FROM2://add by eagle(还不太清楚TestMask中这个方向是什么，什么原理)
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
		case XPANIDM_4FROM2://add by eagle(还不太清楚DirMask是干吗用的，什么原理)
			return 0x28;
		default:
			break;
		}
		return 0;
	}
	bool XAni::GetPic(int dir, int seq, int reg, XPic &pic) const
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
			if( dir == XPDIR_BOTTOMLEFT )
				dir = XPDIR_BOTTOMRIGHT;
			else if( dir == XPDIR_TOPRIGHT )
				dir = XPDIR_TOPLEFT;
			break;
		default:
			break;
		}
		const XPicMap::const_iterator it = m_pics.find(dir);
		if( it == m_pics.end() ) return false;
		const VPic &v = it->second;
		if( seq < 0 || seq >= (int)v.size() ) return false;
		pic = v[seq][reg];


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
			if( dir == XPDIR_BOTTOMLEFT )
				dir = XPDIR_BOTTOMRIGHT;
			else if( dir == XPDIR_TOPRIGHT )
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

	XOStream& PAniPack::marshal(XOStream &os) const
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

	const XIStream& PAniPack::unmarshal(const XIStream &os)
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
		m_bindType = static_cast<XPEFFECT_BIND_TYPE>(bindtype);
		CPOINT oldcenter(0, 0);
		if (dummy > VERSION_OLD_BASE)
		{
			os >> m_base.left;
			os >> m_base.right;
			if (dummy > VERSION_NO_CENTER)
			{
				os >> m_center;
			}
		} else {
			CPOINT unuse;
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
						CRECT offset = m_data[i][j][k].offset;
						m_data[i][j][k].outLinePoints.push_back( CPOINT(0, 0) );
						m_data[i][j][k].outLinePoints.push_back( CPOINT(offset.Width()-1, 0) );
						m_data[i][j][k].outLinePoints.push_back( CPOINT(offset.Width()-1, offset.Height()-1) );
						m_data[i][j][k].outLinePoints.push_back( CPOINT(0, offset.Height()-1) );
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
		//XPLOG_INFO(L"GarbageCollection %d\n", (int)m_rsrcmap.size());
		for(ResourceMap::iterator it = m_rsrcmap.begin(), ie = m_rsrcmap.end(); it != ie;)
		{
			if (it->second->GetHardRefCount() == 1)
			{
				int tick = nowtick - it->second->GetTick();
				if (tick > m_nXapGCTime)
				{
					FreeAni(*(*(it->second)));
					delete it->second;
					it = m_rsrcmap.erase(it);
					continue;
				}
				if (tick > m_nPicGCTime && (*(it->second))->GetState() > XAni::XPANISTATE_NOPICTURE)
				{
					FreeAniPic(*(*(it->second)));
				}
			}
			++it;
		}
	}

	void AniManager::Destroy()
	{
		m_LoadingUrls.clear();
		m_LoadingXaps.clear();
		for(ResourceMap::iterator it = m_rsrcmap.begin(), ie = m_rsrcmap.end(); it != ie; ++it)
		{
			assert( it->second->GetHardRefCount() ==1 );
			FreeAni(*(*(it->second)));
			delete it->second;
		}
		m_rsrcmap.clear();
	}

	void AniManager::RemoveLoadingNotify(const std::wstring &uri, AniLoadingNotify* pNotify)
	{
		LoadingMap::iterator it = m_LoadingUrls.find(uri);
		if (it != m_LoadingUrls.end())
		{
			it->second.notifys.erase(pNotify);
		}
		LoadingXapMap::iterator xIt = m_LoadingXaps.find(uri);
		if (xIt != m_LoadingXaps.end())
		{
			xIt->second.notifys.erase(pNotify);
		}
	}

	XPEffectAssureResResult AniManager::AsyncLoadAni(const std::wstring &uri, AniLoadingNotify* pNotify)
	{
		XHardRef<XAni>ani = GetAni(uri, true);
		if (!ani.IsValid())
			return XPEARR_LOAD_ERROR;
		switch (ani->GetState())
		{
		case XAni::XPANISTATE_NO_XAP:
			m_LoadingXaps[uri].notifys.insert(pNotify);
			return XPEARR_LOADING;
			break;
		case XAni::XPANISTATE_NOPICTURE:
		case XAni::XPANISTATE_BASE_PIC:
			{
				LoadingMap::iterator it = m_LoadingUrls.find(uri);
				if (it == m_LoadingUrls.end())
				{
					LoadingPicParam &param = m_LoadingUrls[uri];
					param.ani = ani;
					param.notifys.insert(pNotify);
					XAni::Buffers::iterator bit = ani->m_buffers.begin(), bie = ani->m_buffers.end();
					CFileIOManager *pFIOMan = m_pEB->GetFileIOManager();
					for (;bit!=bie;++bit)
					{
						XAni::XBigPic *pPic = *bit;
						param.remainPics.insert(pPic->filename);
						AniPicReadTask *pTask = new AniPicReadTask(pPic->filename, uri, pFIOMan, this, pPic, ani->GetTexFmt());
						pTask->Submit();
					}
				} else {
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

	bool AniManager::SyncLoadAni(XHardRef<XAni> ani)
	{
		XPASSERT(ani.IsValid());
		if (ani->GetState() == XAni::XPANISTATE_NOPICTURE || ani->GetState() == XAni::XPANISTATE_BASE_PIC)
		{
			bool result = true;
			XAni::Buffers::iterator bit = ani->m_buffers.begin(), bie = ani->m_buffers.end();
			CFileIOManager *pFIOMan = m_pEB->GetFileIOManager();
			Renderer *pRenderer = m_pEB->GetRenderer();
			for (;bit!=bie;++bit)
			{
				XAni::XBigPic *pPic = *bit;
				if (pPic->handle != INVALID_PICTURE_HANDLE)
					continue;
				XBuffer buffer;
				if (!pFIOMan->GetFileImage(pPic->filename, buffer))
				{
					XPLOG_ERROR(L"load xappicfile failed!, path is %s\n", pPic->filename.c_str());
					XPTRACE(L"load xappicfile failed!, path is %s\n", pPic->filename.c_str());
					result = false;
				}
				if (buffer.size() > 0)
				{
					pPic->handle = pRenderer->LoadPictureFromMem(buffer.constbegin(), buffer.size(), 
						NULL, ani->GetTexFmt(), XPPOOL_MANAGED);
				}
			}
			if (result)
			{
				ani->m_state = XAni::XPANISTATE_PICTURE;
			} else {
				bit = ani->m_buffers.begin(), bie = ani->m_buffers.end();
				for (;bit!=bie;++bit)
				{
					XAni::XBigPic *pBigPic = *bit;
					if (pBigPic)
					{
						if (pBigPic->handle != INVALID_PICTURE_HANDLE)
							m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
					}
				}
				ani->m_state = XAni::XPANISTATE_NULL;
				return false;
			}
		}
		return true;
	}

	bool AniManager::SyncLoadXap(const std::wstring &uri, XAni &ani)
	{
		PAniPack pack;
		bool fIsPFSPath = !uri.empty() && uri[0] == L'/';
		bool ok = false;
		if( fIsPFSPath )
		{
			XBuffer filedata;
			if( !m_pEB->GetFileIOManager()->GetFileImage(uri,filedata))
			{
				XPTRACE(L"  ani 资源加载失败, uri= %s\n", uri.c_str());
				XPLOG_ERROR(L"  ani 资源加载失败, uri= %s\n", uri.c_str());
				if(!m_pEB->GetFileIOManager()->GetFileImage(std::wstring(L"/error/error") + SFI_ExtName,filedata))
				{
					ani.m_state = XAni::XPANISTATE_NULL;
					return false;
				}
			}
			if (pack.LoadFromMem(filedata))
				ok = true;
		}
		else if( pack.LoadFromNativePath(uri.c_str()) )
		{
			//用NativePath的，只有编辑器了吧
			ok = true;
		}

		if (!ok)
		{
			XBuffer filedata;
			XPTRACE(L"  解析ani 资源失败, uri= %s\n", uri.c_str());
			XPLOG_ERROR(L"  解析ani 资源失败, uri= %s\n", uri.c_str());
			if(m_pEB->GetFileIOManager()->GetFileImage(std::wstring(L"/error/error") + SFI_ExtName, filedata) && pack.LoadFromMem(filedata))
				ok = true;	
		}

		if( ok )
		{
			std::wstring baseuri = uri.substr(0, uri.size() - 4);
			if( !LoadAniFromPack(ani, pack, baseuri) )
			{
				XPTRACE(L"  解析ani 资源pack失败, uri= %s\n", uri.c_str());
				XPLOG_ERROR(L"  解析ani 资源pack失败, uri= %s\n", uri.c_str());
				ani.m_state = XAni::XPANISTATE_NULL;
				return false;
			}
			ani.m_state = XAni::XPANISTATE_NOPICTURE;
			return true;
		}
		ani.m_state = XAni::XPANISTATE_NULL;
		return false;
	}

	XWeakRef<XAni> AniManager::GetAni(const std::wstring &uri, bool async)
	{
		ResourceMap::iterator it = m_rsrcmap.find(uri);
		if( it != m_rsrcmap.end() )
		{
			XHardRef<XAni> &hardref = *it->second;
			if (!async && hardref->m_state == XAni::XPANISTATE_NO_XAP)
			{
				SyncLoadXap(uri, *hardref);
			}
			return hardref;
		}
		XAni* pXAni = new XAni();
		XHardRef<XAni> *pHardRef = new XHardRef<XAni>(pXAni);
		pHardRef->SetTick(m_pEB->GetTick());

		if (async)
		{
			pXAni->m_state = XAni::XPANISTATE_NO_XAP;
			XPASSERT(m_LoadingXaps[uri].notifys.size() == 0);
			m_LoadingXaps[uri].notifys.clear();
			XapReadTask* pTask = new XapReadTask(uri, m_pEB->GetFileIOManager(), this, *pHardRef);
			pTask->Submit();
		} else {
			SyncLoadXap(uri, *pXAni);
		}
		m_rsrcmap[uri] = pHardRef;
		return *pHardRef;
	}

	bool AniManager::LoadAniFromPack(XAni &ani, const PAniPack &pack, const std::wstring &packbaseuri)
	{
		assert( ani.m_state == XAni::XPANISTATE_NULL || ani.m_state == XAni::XPANISTATE_NO_XAP);
		ani.m_filefmt = (XPIMAGE_FILEFORMAT)pack.m_filefmt;
		ani.m_texfmt = (XPTEXTURE_FORMAT)pack.m_texfmt;
		ani.m_blend	= (XPTEXTURE_BLENDMODE)pack.m_blend;
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
		
		for(int i= 0; i< (int)pack.m_dirs.size(); ++i)
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

			for(int k=0; k<ani.m_nRegion;++k)
			{
				if (ani.m_nFrame > 1000)
				{
					XPLOG_ERROR(L"LoadAni %s ani.m_nFrame is %d\n", packbaseuri.c_str(), ani.m_nFrame);
				}
				for(int j=0; j< ani.m_nFrame;++j)
				{
					XAni::XPic &xpic = v[j][k];
					const PAniPack::FileSec &file = pack.m_data[i][j][k];
					bigPicIt = bigPicMap.find(file.strPicPath);
					if (bigPicIt == bigPicMap.end())
					{
						xpic.pBigPic = new XAni::XBigPic();
						bigPicMap[file.strPicPath] = xpic.pBigPic;
						xpic.pBigPic->filename = strPicPathPrefix + file.strPicPath;

						{//加载染色分块图
							std::wstring wsPicPathPath;
							wsPicPathPath = strPicPathPrefix + file.strPicPath + L"part";
							XBuffer filedata;
							if( m_pEB->GetFileIOManager()->GetFileImage(wsPicPathPath,filedata))
							{
								xpic.pBigPicPart = new XAni::XBigPic();
								xpic.pBigPicPart->filename = wsPicPathPath;
								bigPicMapPart[file.strPicPath] = xpic.pBigPicPart;
							}
						}
					} else {
						xpic.pBigPic = bigPicIt->second;
						bigPicIt = bigPicMapPart.find(file.strPicPath);
						if (bigPicIt != bigPicMapPart.end())
						{
							xpic.pBigPicPart = bigPicIt->second;
						}
					}
					xpic.srcrect = file.rctData;
					xpic.offset = file.offset;

				//	//版本14以前的ani里没有记录outLinePoints信息--在读文件的时候初始化为小图片矩形的4个点.
				//	//
					if (pack.m_data[i][j][k].outLinePoints.size() > 500)
					{
						XPLOG_ERROR(L"LoadAni %s pack.m_data[%d][%d][%d].outLinePoints.size is %d\n", packbaseuri.c_str(), i, j, k, (int)pack.m_data[i][j][k].outLinePoints.size());
					}
					xpic.outLine.m_points = pack.m_data[i][j][k].outLinePoints;
				}
			}
		}

		//让ani.m_buffers的顺序是各方向的第一帧排前一点

		for(int i= 0; i< (int)pack.m_dirs.size(); ++i)
		{	
			for(int k=0; k<ani.m_nRegion;++k)
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
		for (bigPicIt = bigPicMap.begin(), bigPicEnd = bigPicMap.end(); bigPicIt != bigPicEnd; ++bigPicIt)
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
		return true;
	}

	AniManager::AniManager(EngineBase *pEB) : m_pEB(pEB), m_nPicGCTime(20000), m_nXapGCTime(1200000)
	{ 
		
	}

	void AniManager::FreeAni(XAni &ani)
	{
		for (XAni::Buffers::iterator it = ani.m_buffers.begin(), ie = ani.m_buffers.end() ; it!=ie ; ++it)
		{
			XAni::XBigPic *pBigPic = *it;
			if (pBigPic)
			{
				if (pBigPic->handle != INVALID_PICTURE_HANDLE)
				{
					m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
				}
				delete pBigPic;
			}
		}
		for (XAni::Buffers::iterator it = ani.m_buffersPart.begin(), ie = ani.m_buffersPart.end() ; it!=ie ; ++it)
		{
			XAni::XBigPic *pBigPic = *it;
			if (pBigPic)
			{
				if (pBigPic->handle != INVALID_PICTURE_HANDLE)
				{
					m_pEB->GetRenderer()->FreePicture(pBigPic->handle);
				}
				delete pBigPic;
			}
		}
		ani.m_buffers.clear();
		ani.m_buffersPart.clear();
		ani.m_pics.clear();
		ani.m_nRegion = 0;
		ani.m_nFrame = 0;
		ani.m_dirmode = XPANIDM_NULL;
		ani.m_state = XAni::XPANISTATE_NULL;
	}

	void AniManager::FreeAniPic(XAni &ani)
	{
		if (ani.m_state < XAni::XPANISTATE_NOPICTURE)
			return;
		for (XAni::Buffers::iterator it = ani.m_buffers.begin(), ie = ani.m_buffers.end() ; it!=ie ; ++it)
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
};