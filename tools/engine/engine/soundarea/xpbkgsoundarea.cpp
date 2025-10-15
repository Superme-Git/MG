#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\xptypes.h"
#include "..\sound\directsoundmanager.h"
#include "..\engine\configmanager.h"
#include "..\engine\enginebase.h"
#include "xpbkgsoundarea.h"


namespace Nuclear
{
	XPBkgSoundArea::XPBkgSoundArea(EngineBase *pEB) : m_pEB(pEB), m_pBGMControl(NULL), 
		m_nMinInterval(0), m_nMaxInterval(0), m_nFadeInTime(0), m_nFadeOutTime(0), 
		m_bWaitPrevAreaPlayEnd(false)
	{
	}

	XPBkgSoundArea::~XPBkgSoundArea(void)
	{
		if (m_pBGMControl)
			delete m_pBGMControl;
	}

	void XPBkgSoundArea::LoadSound(XMLIO::CINode& node)
	{
		m_nMinInterval = node.GetAttributeInteger(L"minInt", 0);
		m_nMaxInterval = node.GetAttributeInteger(L"maxInt", 0);
		m_nFadeInTime = node.GetAttributeInteger(L"fi", 0);
		m_nFadeOutTime = node.GetAttributeInteger(L"fo", 0);
		m_bWaitPrevAreaPlayEnd = (node.GetAttributeInteger(L"wait", 0) == 1);
		//既然先实现了XPBGMPT_RANDOM，那么先用这个吧
		XPBGM_PLAY_TYPE type = static_cast<XPBGM_PLAY_TYPE>(node.GetAttributeInteger(L"mode", XPBGMPT_RANDOM));//XPBGMPT_QUEUE1));
		if (m_pBGMControl)
			delete m_pBGMControl;
		m_pBGMControl = IXPBGMControl::GetBGMControl(type, this);
		m_sounds.clear();
		SoundParam tmpParam;
		XMLIO::CNodeList sNodeList;
		node.GetChildren(sNodeList);
		XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		unsigned char i = 0;
		for (;nlIt!=nlIe;++nlIt)
		{
			XMLIO::CINode& snode = *nlIt;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			XPASSERT(snode.GetName()==L"sound");
			m_sounds.push_back(tmpParam);
			SoundParam &param = m_sounds[i];
			snode.GetAttribute(L"url", param.soundName);
			param.vol = static_cast<unsigned char>(snode.GetAttributeInteger(L"vol", 0));
			param.type = static_cast<XPSA_SOUND_TYPE>(snode.GetAttributeInteger(L"type", XPSA_BOTH));
			++i;
		}
	}
	void XPBkgSoundArea::SaveSound(XMLIO::CONode& node)
	{
		if (m_nMinInterval != 0) node.SetAttribute(L"minInt", m_nMinInterval);
		if (m_nMaxInterval != 0) node.SetAttribute(L"maxInt", m_nMaxInterval);
		if (m_nFadeInTime != 0) node.SetAttribute(L"fi", m_nFadeInTime);
		if (m_nFadeOutTime != 0) node.SetAttribute(L"fo", m_nFadeOutTime);
		if (m_bWaitPrevAreaPlayEnd) node.SetAttribute(L"wait", 1);
		if (m_pBGMControl)
		{
			node.SetAttribute(L"mode", m_pBGMControl->GetType());
		}
		SoundParamVet::iterator sIt = m_sounds.begin(), sIe = m_sounds.end();
		for (;sIt!=sIe;++sIt)
		{
			XMLIO::CONode snode;
			node.CreateSubElement(L"sound", snode);
			snode.SetAttribute(L"url", sIt->soundName);
			snode.SetAttribute(L"vol", sIt->vol);
			snode.SetAttribute(L"type", static_cast<int>(sIt->type));
		}
	}

	int XPBkgSoundArea::AddSound(const std::wstring &name) 
	{
		SoundParam tmpParam;
		tmpParam.vol = 255;
		tmpParam.soundName = name;
		tmpParam.type = XPSA_BOTH;
		m_sounds.push_back(tmpParam);
		return static_cast<int>(m_sounds.size() - 1);
	}

	void XPBkgSoundArea::InsertSound(int soundID, const SoundParam &param)
	{
		SoundParamVet::iterator iter = m_sounds.begin() + soundID;
		m_sounds.insert(iter, param);
	}

	void XPBkgSoundArea::MoveSound(int soundID, bool bUp)
	{
		if (soundID == 0 && bUp)
			return;
		if (soundID == m_sounds.size() - 1 && (!bUp))
			return;
		XPASSERT(soundID >= 0);
		XPASSERT(soundID < static_cast<int>(m_sounds.size()));
		SoundParam &p1 = m_sounds[soundID];
		SoundParam &p2 = m_sounds[soundID + bUp?-1:1];
		SoundParam tmpParam = p1;
		p1 = p2;
		p2 = tmpParam;
	}

	void XPBkgSoundArea::SetPlayMode(XPBGM_PLAY_TYPE mode)
	{
		if (m_pBGMControl)
		{
			if (m_pBGMControl->GetType() == mode)
			{
				return;
			} else {
				delete m_pBGMControl;
			}
		}
		m_pBGMControl = IXPBGMControl::GetBGMControl(mode, this);
	}

	XPBGM_PLAY_TYPE XPBkgSoundArea::GetPlayMode() const
	{
		if (m_pBGMControl)
			return m_pBGMControl->GetType();
		return XPBGMPT_RANDOM;
	}

	void XPBkgSoundArea::GetSoundList(std::vector<std::wstring> &slist) const
	{
		slist.clear();
		slist.reserve(m_sounds.size());
		SoundParamVet::const_iterator it = m_sounds.begin(), ie = m_sounds.end();
		for (;it!=ie;++it)
		{
			slist.push_back(it->soundName);
		}
	}

	void XPBkgSoundArea::LeaveMeAndEnterNew(XPBkgSoundArea *pArea)
	{
		if (!m_pBGMControl)
			return;
		m_pBGMControl->LeaveMeAndEnterNew(pArea);
	}

	void XPBkgSoundArea::FadeOut(unsigned short time)
	{
		if (!m_pBGMControl)
			return;
		m_pBGMControl->FadeOut(time);
	}

	void XPBkgSoundArea::GetCurTimeSoundIDs(std::vector<int> &vet)
	{
		vet.clear();
		if (!m_pEB)
			return;
		XPBkgSoundArea::SoundParamVet::iterator it = m_sounds.begin(), ie = m_sounds.end();
		XPSA_SOUND_TYPE nowtype = m_pEB->IsDaytime()?XPSA_DAY:XPSA_NIGHT;
		int i = 0;
		for (;it!=ie;++it,++i)
		{
			if (it->type & nowtype)
			{
				vet.push_back(i);
			}
		}
	}

	void XPBkgSoundArea::SetVolume(float fV)
	{
		if (!m_pBGMControl)
			return;
		m_pBGMControl->SetVolume(fV);
	}

	float XPBkgSoundArea::GetVolume() const
	{
		if (!m_pBGMControl)
			return 1.0f;
		return m_pBGMControl->GetVolume();
	}

	int XPBkgDefaultSoundArea::Load(XMLIO::CINode& node, int flag)
	{
		XMLIO::CNodeList sNodeList;
		node.GetChildren(sNodeList);
		XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			XMLIO::CINode& snode = *nlIt;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			XPASSERT(snode.GetName()==L"sounds");
			LoadSound(snode);
		}
		return 0;
	}

	int XPBkgDefaultSoundArea::Save(XMLIO::CONode& node, int flag)
	{
		XMLIO::CONode sounds;
		node.CreateSubElement(L"sounds", sounds);
		SaveSound(sounds);
		return 0;
	}

	int XPBkgSoundPolygonArea::Load(XMLIO::CINode& node, int flag)
	{
		XMLIO::CNodeList sNodeList;
		node.GetChildren(sNodeList);
		XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			XMLIO::CINode& snode = *nlIt;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			if (snode.GetName()==L"area") 
			{
				LoadPolygon(snode, m_polygon);
			} else if (snode.GetName()==L"regions")
			{
				LoadRegion(snode, m_regionSet);
			} else if (snode.GetName()==L"sounds")
			{
				LoadSound(snode);
			} else if (snode.GetName()==L"TOOLS_DATA")
			{
				if (flag & XPSOUND_IO_EDIT)	//切割信息
				{
					LoadToolsData(snode);
				}
			} else
				XPASSERT(false);
		}
		return 0;
	}

	int XPBkgSoundPolygonArea::Save(XMLIO::CONode& node, int flag)
	{
		XMLIO::CONode sounds;
		node.CreateSubElement(L"sounds", sounds);
		SaveSound(sounds);
		XMLIO::CONode area;
		node.CreateSubElement(L"area", area);
		SavePolygon(area, m_polygon);
		XMLIO::CONode regions;
		node.CreateSubElement(L"regions", regions);
		SaveRegion(regions, m_regionSet);
		if (flag & XPSOUND_IO_EDIT)
		{
			XMLIO::CONode tools;
			node.CreateSubElement(L"TOOLS_DATA", tools);
			SaveToolsData(tools);
		}
		return 0;
	}

	int XPBkgSoundCircleArea::Load(XMLIO::CINode& node, int flag)
	{
		LoadCircle(node, m_circle);
		XMLIO::CNodeList sNodeList;
		node.GetChildren(sNodeList);
		XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			XMLIO::CINode& snode = *nlIt;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			if (snode.GetName()==L"sounds")
			{
				LoadSound(snode);
			} else if (snode.GetName()==L"regions") {
				LoadRegion(snode, m_regionSet);
			} else {
				XPASSERT(false);
			}
		}
		return 0;
	}

	int XPBkgSoundCircleArea::Save(XMLIO::CONode& node, int flag)
	{
		SaveCircle(node, m_circle);
		XMLIO::CONode soundnode;
		node.CreateSubElement(L"sounds", soundnode);
		SaveSound(soundnode);
		XMLIO::CONode regionnode;
		node.CreateSubElement(L"regions", regionnode);
		SaveRegion(regionnode, m_regionSet);
		return 0;
	}

	class XPBGMLoopRestControl : public IXPBGMControl
	{
		int m_dayFirstSound;
		int m_nightFirstSound;
	public:
		XPBGMLoopRestControl(XPBkgSoundArea *pArea) : IXPBGMControl(pArea), 
			m_dayFirstSound(-1), m_nightFirstSound(-1) {}
		virtual ~XPBGMLoopRestControl() {}

		virtual int GetNextSoundID()
		{
			if (!m_pArea)
				return -1;
			EngineBase *pEB = m_pArea->GetEngineBase();
			if (!pEB)
				return -1;

			const XPBkgSoundArea::SoundParamVet &sounds = m_pArea->GetSounds();
			int size = (int)sounds.size(), i = 0;
			if (size == 0)
				return -1;
			int curSound = m_nCurSound;
			if (curSound == -1)//第一首
			{
				if (m_dayFirstSound == -1 && m_nightFirstSound == -1)//初始化，计算第一首到底是哪个
				{
					bool day = false, night = false;
					while (i < size && !(day && night))
					{
						if (!day && sounds[i].type & XPSA_DAY)
						{
							day = true;
							m_dayFirstSound = i;
						}
						if (!night && sounds[i].type & XPSA_NIGHT)
						{
							night = true;
							m_nightFirstSound = i;
						}
						++i;
					}
				}
				return pEB->IsDaytime()?m_dayFirstSound:m_nightFirstSound;
			}
			int curfirst = pEB->IsDaytime()?m_dayFirstSound:m_nightFirstSound;
			XPSA_SOUND_TYPE nowtype = pEB->IsDaytime()?XPSA_DAY:XPSA_NIGHT;
			int count = 0;
			i = curSound + 1;
			while (count < size)
			{
				if (i >= size)
					i -= size;
				if (i != curfirst && sounds[i].type & nowtype)
				{
					return i;
				}
				++i;
				++count;
			}
			return -1;
		}

		virtual XPBGM_PLAY_TYPE GetType() const { return XPBGMPT_LOOP_REST; }
	};

	class XPBGMRandomControl : public IXPBGMControl
	{
	public:
		XPBGMRandomControl(XPBkgSoundArea *pArea) : IXPBGMControl(pArea) {}
		virtual ~XPBGMRandomControl() {}

		virtual int GetNextSoundID()
		{
			if (!m_pArea)
				return -1;
			std::vector<int> tmpVet;
			m_pArea->GetCurTimeSoundIDs(tmpVet);
			if (tmpVet.size() == 0)
				return -1;
			return tmpVet[RangeRand(0, (int)tmpVet.size() - 1)];
		}
	
		virtual XPBGM_PLAY_TYPE GetType() const { return XPBGMPT_RANDOM; }
	};

	class XPBGMRandomUniqueControl : public IXPBGMControl
	{
	public:
		XPBGMRandomUniqueControl(XPBkgSoundArea *pArea) : IXPBGMControl(pArea) {}//public吧，反正外面拿不到类
		virtual ~XPBGMRandomUniqueControl() {}

		virtual int GetNextSoundID()
		{
			if (!m_pArea)
				return -1;
			std::vector<int> tmpVet;
			m_pArea->GetCurTimeSoundIDs(tmpVet);
			int size = (int)tmpVet.size();
			switch (size)
			{
			case 0:
				return -1;
				break;
			case 1:
				return tmpVet[0];
				break;
			default:
				break;
			}
			
			if (m_nCurSound == -1)
			{
				return tmpVet[RangeRand(0, size - 1)];
			}
			int tmpId = RangeRand(0, size - 2);
			return tmpVet[tmpId + (tmpVet[tmpId] >= m_nCurSound)?1:0];
			
		}
		virtual XPBGM_PLAY_TYPE GetType() const { return XPBGMPT_RANDOM_UNIQUE; }
	};

	class XPBGMQueue1Control : public IXPBGMControl
	{
	public:
		XPBGMQueue1Control(XPBkgSoundArea *pArea) : IXPBGMControl(pArea) {}
		virtual ~XPBGMQueue1Control() {}

		virtual int GetNextSoundID()
		{
			if (!m_pArea)
				return -1;
			EngineBase *pEB = m_pArea->GetEngineBase();
			if (!pEB)
				return -1;
			const XPBkgSoundArea::SoundParamVet &sounds = m_pArea->GetSounds();
			int size = (int)sounds.size();
			if (size == 0)
				return -1;
			XPSA_SOUND_TYPE nowtype = pEB->IsDaytime()?XPSA_DAY:XPSA_NIGHT;
			int count = 0;
			int i = m_nCurSound + 1;
			while (count < size)
			{
				if (i >= size)
					i -= size;
				if (sounds[i].type & nowtype)
				{
					return i;
				}
				++i;
				++count;
			}
			return -1;
		}

		virtual XPBGM_PLAY_TYPE GetType() const { return XPBGMPT_QUEUE1; }
	};

	class XPBGMQueue2Control : public IXPBGMControl
	{
		int m_nLastSound;
	public:
		XPBGMQueue2Control(XPBkgSoundArea *pArea) : IXPBGMControl(pArea), m_nLastSound(-1) {}
		virtual ~XPBGMQueue2Control() {}

		virtual int GetNextSoundID()
		{
			if (!m_pArea)
				return -1;
			EngineBase *pEB = m_pArea->GetEngineBase();
			if (!pEB)
				return -1;
			const XPBkgSoundArea::SoundParamVet &sounds = m_pArea->GetSounds();
			int size = (int)sounds.size();
			if (size == 0)
				return -1;
			XPSA_SOUND_TYPE nowtype = pEB->IsDaytime()?XPSA_DAY:XPSA_NIGHT;
			int count = 0;
			while (count < size)
			{
				++m_nLastSound;
				if (m_nLastSound >= size)
					m_nLastSound -= size;
				if (sounds[m_nLastSound].type & nowtype)
				{
					return m_nLastSound;
				}
				++count;
			}
			return -1;
		}

		virtual XPBGM_PLAY_TYPE GetType() const { return XPBGMPT_QUEUE2; }
	};


	class XPBGMListControl : public IXPBGMControl
	{
	public:
		XPBGMListControl(XPBkgSoundArea *pArea) : IXPBGMControl(pArea) {}
		virtual ~XPBGMListControl() {}

		virtual int GetNextSoundID()
		{
			if (!m_pArea)
				return -1;
			EngineBase *pEB = m_pArea->GetEngineBase();
			if (!pEB)
				return -1;
			const XPBkgSoundArea::SoundParamVet &sounds = m_pArea->GetSounds();
			int size = (int)sounds.size();
			if (size == 0)
				return -1;
			XPSA_SOUND_TYPE nowtype = pEB->IsDaytime()?XPSA_DAY:XPSA_NIGHT;
			int count = 0;
			int i = m_nCurSound + 1;
			while (count < size)
			{
				if (i >= size)
					i -= size;
				if (sounds[i].type & nowtype)
				{
					return i;
				}
				++i;
				++count;
			}
			return -1;
		}

		void LeaveMeAndEnterNew(XPBkgSoundArea *pArea) 
		{
			if (!pArea)
			{
				Reset();
				return;
			}
			IXPBGMControl *pControl = pArea->GetBGMControl();
			if ((!pControl) || (!m_pArea))
			{
				Reset();
				return;
			}
			if (pControl->GetType() == XPBGMPT_LIST && pControl != this)
			{
				if (pArea->m_sounds.size() == m_pArea->m_sounds.size())
				{
					XPBGMListControl* pListControl = dynamic_cast<XPBGMListControl*>(pControl);
					if (pListControl)//既然GetType都是XPBGMPT_LIST，其实木有必要了吧
					{
						//交换Control指针
						m_pArea->m_pBGMControl = pControl;
						pArea->m_pBGMControl = this;
						//交换Area指针
						pListControl->m_pArea = m_pArea;
						m_pArea = pArea;
						return;
					}
				}
			}
			Reset();
			pControl->EnterMe(m_pArea->GetFadeOutTime());
		}
		virtual XPBGM_PLAY_TYPE GetType() const { return XPBGMPT_LIST; }
	};

	IXPBGMControl* IXPBGMControl::GetBGMControl(XPBGM_PLAY_TYPE type, XPBkgSoundArea *pArea)
	{
		switch (type)
		{
		case XPBGMPT_LOOP_REST:
			return new XPBGMLoopRestControl(pArea);
			break;
		case XPBGMPT_RANDOM:
			return new XPBGMRandomControl(pArea);
			break;
		case XPBGMPT_RANDOM_UNIQUE:
			return new XPBGMRandomUniqueControl(pArea);
			break;
		case XPBGMPT_QUEUE1:
			return new XPBGMQueue1Control(pArea);
			break;
		case XPBGMPT_QUEUE2:
			return new XPBGMQueue2Control(pArea);
			break;
		case XPBGMPT_LIST:
			return new XPBGMListControl(pArea);
			break;
		}
		return NULL;
	}

	IXPBGMControl::~IXPBGMControl()
	{
		if (m_pArea && m_pArea->GetEngineBase())
		{
			std::set<std::wstring>::iterator it = m_loadingFiles.begin(), ie = m_loadingFiles.end();
			DirectSoundManager *pDSM = m_pArea->GetEngineBase()->GetSoundManager();
			for (;it!=ie;++it)
			{
				pDSM->CancelSoundLoadingNotify(*it, this);
			}
			Reset();
		}
	}

	void IXPBGMControl::LeaveMeAndEnterNew(XPBkgSoundArea *pArea) 
	{
		Reset();
		if ((!pArea) || (!pArea->GetBGMControl()))
			return;
		pArea->GetBGMControl()->EnterMe(m_pArea->GetFadeOutTime());
	}

	void IXPBGMControl::FadeOut(unsigned short time)
	{
		m_bLeft = true;
		m_nCurSound = -1;
		if (!m_pArea || !m_pArea->GetEngineBase())
			return;
		EngineBase* pEB = m_pArea->GetEngineBase();
		if (m_curSoundHandle)
		{
			//有m_curSoundHandle，那么SoundMan就没有问题
			DirectSoundManager *pDSM = pEB->GetSoundManager();
			pDSM->FadeOutToStop(m_curSoundHandle, time);
			pDSM->SetNotify(m_curSoundHandle, NULL);
			m_curSoundHandle = INVALID_SOUND_HANDLE;
		}
		if (m_bHasSetTimer)
		{
			pEB->CancelTimer(this);
		}
	}

	CHECK_SOUND_STATE IXPBGMControl::CheckSound(int soundID)
	{
		if (!m_pArea)
			return CSS_FAILD;
		const XPBkgSoundArea::SoundParamVet& sounds = m_pArea->GetSounds();
		int size = static_cast<int>(sounds.size());
		if (size == 0 || soundID >= size || !m_pArea->GetEngineBase())
			return CSS_FAILD;
		DirectSoundManager *pDSM = m_pArea->GetEngineBase()->GetSoundManager();
		if (!pDSM)
			return CSS_FAILD;
		const XPBkgSoundArea::SoundParam& param = sounds[soundID];
		XPISoundType type = XPIST_STREAM_SOUND;
		CHECK_SOUND_STATE result = pDSM->CheckSound(param.soundName, true, this, type);
		if (result == CSS_LOADING)
		{
			m_loadingFiles.insert(param.soundName);
		}
		return result;
	}

	bool IXPBGMControl::CheckAndCreateSound(int soundID, SoundHandle& handle)
	{
		switch (CheckSound(soundID))
		{
		case CSS_CHECK_OK:
			{
				//因为CheckSound了，所以m_pArea、soundID和GetSoundManager指针都没有问题
				const XPBkgSoundArea::SoundParam& param = m_pArea->GetSounds()[soundID];
				DirectSoundManager *pDSM = m_pArea->GetEngineBase()->GetSoundManager();
				handle = pDSM->CreateStreamSound(param.soundName, this);
				if (handle != INVALID_SOUND_HANDLE)
				{
					pDSM->SetSoundVolume(handle, static_cast<unsigned char>(param.vol * m_fVolume));
				}
				return true;
			}
			break;
		case CSS_LOADING:
			handle = INVALID_SOUND_HANDLE;
			return true;
			break;
		default:
			//faild了
			handle = INVALID_SOUND_HANDLE;
			break;
		}
		return false;
	}
	bool IXPBGMControl::PlayCurSoundHandle(unsigned short nFadeInTime)
	{
		if (m_curSoundHandle == INVALID_SOUND_HANDLE)
		{
			m_bLoadSuccNeedPlay = true;
			return false;
		}
		m_bLoadSuccNeedPlay = false;
		//能调到这里来了，m_curSoundHandle还不是Invalid的，那么m_pEB，SoundMan应该都没有问题了吧
		XPASSERT(m_pArea);
		XPASSERT(m_pArea->GetEngineBase());
		EngineBase *pEB = m_pArea->GetEngineBase();
		XPASSERT(pEB);
		DirectSoundManager *pDSM = pEB->GetSoundManager();
		XPASSERT(pDSM);
		return pDSM->PlayAndFadeIn(m_curSoundHandle, nFadeInTime, 0, pEB->GetConfigManager()->GetBGMType(), false);
	}

	bool IXPBGMControl::AppendSound(const std::wstring &fn)
	{
		if (m_curSoundHandle == INVALID_SOUND_HANDLE)
			return false;
		//能调到这里来了，m_curSoundHandle还不是Invalid的，那么m_pEB，SoundMan应该都没有问题了吧
		XPASSERT(m_pArea);
		XPASSERT(m_pArea->GetEngineBase());
		XPASSERT(m_pArea->GetEngineBase()->GetSoundManager());
		return m_pArea->GetEngineBase()->GetSoundManager()->PushNextStreamSound(m_curSoundHandle, fn);
	}

	bool IXPBGMControl::AppendSound(int soundID)
	{
		if (!m_pArea)
			return false;
		const XPBkgSoundArea::SoundParamVet &sounds = m_pArea->GetSounds();
		if ((int)sounds.size() <= soundID)
			return false;
		return AppendSound(sounds[soundID].soundName);
	}

	void IXPBGMControl::SetVolume(float fV)
	{
		if (m_fVolume == fV)
			return;
		m_fVolume = fV;
		if (m_curSoundHandle != INVALID_SOUND_HANDLE && m_nCurSound >= 0)//m_nCurSound == -1的时候已经是在FadeOut了，就不管了吧
		{
			const XPBkgSoundArea::SoundParam& param = m_pArea->GetSounds()[m_nCurSound];
			DirectSoundManager *pDSM = m_pArea->GetEngineBase()->GetSoundManager();
			pDSM->SetSoundVolume(m_curSoundHandle, static_cast<unsigned char>(param.vol * m_fVolume));
		}
		
	}

	void IXPBGMControl::EnterMe(int delayTime)
	{
		m_bLeft = false;
		m_bIsFirst = true;
		if (!m_pArea || !m_pArea->GetEngineBase())//如果没有m_pEB，CheckAndCreateSound应该不会成功吧
			return;
		m_nCurSound = GetNextSoundID();
		if (m_nCurSound == -1)
			return;
		if (CheckAndCreateSound(m_nCurSound, m_curSoundHandle))
		{
			if (m_pArea->IsWaitPrevAreaPlayEnd() && delayTime > 0)
			{
				m_bHasSetTimer = true;
				m_pArea->GetEngineBase()->ScheduleTimer(this, delayTime);
			} else {
				if (PlayCurSoundHandle(m_pArea->GetFadeInTime()))
				{
					m_bIsFirst = false;
				}
			}
		}
	}

	void IXPBGMControl::Reset()
	{
		m_bLeft = true;
		m_nCurSound = -1;
		if (!m_pArea || !m_pArea->GetEngineBase())
			return;
		EngineBase* pEB = m_pArea->GetEngineBase();
		if (m_curSoundHandle)
		{
			//有m_curSoundHandle，那么SoundMan就没有问题
			DirectSoundManager *pDSM = pEB->GetSoundManager();
			pDSM->FadeOutToStop(m_curSoundHandle, m_pArea->GetFadeOutTime());
			pDSM->SetNotify(m_curSoundHandle, NULL);
			m_curSoundHandle = INVALID_SOUND_HANDLE;
		}
		if (m_bHasSetTimer)
		{
			pEB->CancelTimer(this);
		}
	}

	void IXPBGMControl::OnSoundGoingToEnd(SoundHandle handle)
	{
		if (m_bLeft)
			return;
		m_nNextSoundID = GetNextSoundID();
		if (m_nNextSoundID == -1)
			return;
		if (!m_pArea)
			return;
		switch (CheckSound(m_nNextSoundID))
		{
		case CSS_CHECK_OK:
			if (m_pArea->GetMinInterval() == m_pArea->GetMaxInterval() &&  m_pArea->GetMaxInterval() == 0)//连续
			{
				AppendSound(m_nNextSoundID);
				m_nCurSound = m_nNextSoundID;
				m_nNextSoundID = -1;
			}
			break;
		case CSS_LOADING:
			//留给加载完成的回调
			break;
		case CSS_FAILD:
			m_nNextSoundID = -1;
			break;
		}
	}

	void IXPBGMControl::OnSoundPlayEnd(SoundHandle handle)
	{
		if (m_bLeft)
			return;
		m_nCurSound = m_nNextSoundID;
		m_nNextSoundID = -1;
		if (m_nCurSound == -1)
			return;
		if (!m_pArea)
			return;
		if (CheckAndCreateSound(m_nCurSound, m_curSoundHandle))
		{
			int waitTime = RangeRand(m_pArea->GetMinInterval(), m_pArea->GetMaxInterval());
			if (waitTime == 0)//连续
			{
				PlayCurSoundHandle(0);
			} else {
				m_bHasSetTimer = true;
				XPASSERT(m_pArea->GetEngineBase());//如果没有m_pEB，这声音怎么播出来的呢？以至于还能回调？
				m_pArea->GetEngineBase()->ScheduleTimer(this, waitTime);
			}

		}
	}

	void IXPBGMControl::OnSoundLoaded(const std::wstring &fn, bool success)
	{
		m_loadingFiles.erase(fn);
		if (m_bLeft)
			return;
		if (!success)
			return;
		if (!m_pArea)
			return;
		const XPBkgSoundArea::SoundParamVet &sounds = m_pArea->GetSounds();
		if (m_nCurSound != -1)
		{
			if (sounds[m_nCurSound].soundName == fn)
			{
				if (CheckAndCreateSound(m_nCurSound, m_curSoundHandle))
				{
					if (m_bLoadSuccNeedPlay)
					{
						if (PlayCurSoundHandle(m_bIsFirst?m_pArea->GetFadeInTime():0))
						{
							m_bIsFirst = false;
						}
					} else {
						//等Timer吧
					}
				} else {
					XPLOG_ERROR(L"OnLoadEnd CheckAndCreate失败, fn: %s\n", fn.c_str());
				}
				return;
			}
		}
		if (m_nNextSoundID != -1)
		{
			if (sounds[m_nNextSoundID].soundName == fn)
			{
				if (m_pArea->GetMinInterval() == m_pArea->GetMaxInterval() &&  m_pArea->GetMaxInterval() == 0)//连续
				{
					AppendSound(m_nNextSoundID);
					m_nCurSound = m_nNextSoundID;
					m_nNextSoundID = -1;
				} else {
					//等PlayEnd的时候再说
				}
			}
		}
	}

	void IXPBGMControl::OnTimer()
	{
		XPASSERT(m_pArea);
		XPASSERT(m_pArea->GetEngineBase());//没有m_pEB的话，连这个Timer都Set不了
		m_pArea->GetEngineBase()->CancelTimer(this);
		m_bHasSetTimer = false;
		//到时间播放了
		if (m_bLeft)
			return;//已经离开区域了
		if (PlayCurSoundHandle(m_bIsFirst?m_pArea->GetFadeInTime():0))
		{
			m_bIsFirst = false;
		}
	}

}