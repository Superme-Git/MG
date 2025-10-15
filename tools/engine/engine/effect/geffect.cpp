#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\fileiomanager.h"
#include "geffect.h"
#include "effectclip.h"
#include "effectmanager.h"


namespace Nuclear
{
	bool GEffectData::Parse(const XBuffer& buffer)
	{
		XPASSERT(pRoot == NULL && pFr == NULL);
		bLoading = false;
		pFr = new XMLIO::CFileReader();
		int nError = pFr->OpenData(buffer.constbegin(), buffer.size());
		if( XMLIO::EC_SUCCESS != nError)
		{
			pFr->CloseFile();
			delete pFr;
			pFr = NULL;
			return false;
		}
		pRoot = new XMLIO::CINode();
		if (!pFr->GetRootNode(*pRoot))
		{
			delete pRoot;
			pRoot = NULL;
			pFr->CloseFile();
			delete pFr;
			pFr = NULL;
			return false;
		}
		return true;
	}

	GEffectData::~GEffectData() 
	{
		if (pFr)
		{
			pFr->CloseFile();
			delete pFr;
			pFr = NULL;
		}
		XPSAFE_DELETE(pRoot);
	}

	class GEffectReadTask : public CFileIOManager::AsyncReadTask
	{
		std::wstring m_name;
		GEffectXMLCaches* m_pCaches;
		int m_tick;
	public:
		GEffectReadTask(const std::wstring &filename, const std::wstring &name, CFileIOManager *pFileIOMan, GEffectXMLCaches* pCahces)
			: CFileIOManager::AsyncReadTask(pFileIOMan, filename, 0), m_name(name), m_pCaches(pCahces)
		{
			m_tick = GetMilliSeconds();
		}

		virtual void OnReady()
		{
			bool result = false;
			int tick = GetMilliSeconds();
			GEffectData &data = m_pCaches->m_datas[m_name];
			data.tick = tick;
			if( m_data.size() == 0 )
			{
				XPLOG_ERROR(L"load Geffect failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load Geffect failed!, path is %s\n", m_filename.c_str());
			} else {
				m_pFileIOMan->OnReady(tick-m_tick, m_data.size());
				XPTRACE(L"load Geffect ok!, path is %s\n", m_filename.c_str());
				if (data.pRoot == NULL)
				{
					if (!data.Parse(m_data))
					{
						XPLOG_ERROR(L"异步解析特效文件 %s 发生错误\n", m_name.c_str());
					}
				}
			}
			data.bLoading = false;
			GEffectXMLCaches::LoadingGEffects::iterator it = m_pCaches->m_notifys.find(m_name);
			if (it !=  m_pCaches->m_notifys.end())
			{
				GEffectXMLCaches::GEffectSet::iterator gIt = it->second.begin(), gIe = it->second.end();
				if (gIt != gIe)
				{
					if (data.pRoot)
					{
						if ((*gIt)->OnAsyncLoadedXML(data.pRoot) != GEffect::EC_S_OK)
						{
							XPLOG_ERROR(L"异步加载特效文件 %s 发生错误, %d\n", m_name.c_str(), result);//只打印一次
						}
						++gIt;
					}
					for (;gIt!=gIe;++gIt)
					{
						(*gIt)->OnAsyncLoadedXML(data.pRoot);
					}
				}
				m_pCaches->m_notifys.erase(it);
			}
			delete this;
		}

		virtual void OnDiscard() 
		{
			GEffectXMLCaches::LoadingGEffects::iterator it = m_pCaches->m_notifys.find(m_name);
			if (it !=  m_pCaches->m_notifys.end())
			{
				GEffectXMLCaches::GEffectSet::iterator gIt = it->second.begin(), gIe = it->second.end();
				for (;gIt!=gIe;++gIt)
				{
					(*gIt)->OnAsyncLoadedXML(NULL);
				}
				m_pCaches->m_notifys.erase(it);
			}
			delete this;
		}
		virtual ~GEffectReadTask() { }
	};

	const GEffectData& GEffectXMLCaches::GetGEffectData(const std::wstring &name, GEffect *pGEffect, bool async)
	{
		GEffectDataMap::iterator it = m_datas.find(name);
		if (it == m_datas.end() || (it->second.bLoading && (!async)))//没有找到，或者是正在加载，但是需要同步
		{	
			std::wstring uri = L"/effect/" + name + Effect_ExtName;
			GEffectData& data = m_datas[name];
			data.tick = m_pEngine->GetTick();
			if (async)
			{
				//异步
				m_notifys[name].insert(pGEffect);
				GEffectReadTask* pTask = new GEffectReadTask(uri, name, m_pEngine->GetFileIOManager(), this);
				pTask->Submit();
			} else {
				//同步
				data.tick = m_pEngine->GetTick();
				data.bLoading = false;
				XBuffer buffer;
				if( !m_pEngine->GetFileIOManager()->GetFileImage(uri, buffer))
				{
					XPLOG_ERROR(L"同步加载特效文件 %s 发生错误\n", name.c_str());
					return data;
				}
				if (!data.Parse(buffer))
				{
					XPLOG_ERROR(L"同步解析特效文件 %s 发生错误\n", name.c_str());
				}
			}
			return data;
		}
		it->second.tick = m_pEngine->GetTick();
		return it->second;
	}

	void GEffectXMLCaches::RemoveNotify(const std::wstring &name, GEffect *pGEffect)
	{
		LoadingGEffects::iterator it = m_notifys.find(name);
		if (it == m_notifys.end())
			return;
		it->second.erase(pGEffect);
	}

	GEffect::GEffect(EngineBase *pEB) : Effect(pEB), m_totoalFrames(100), m_fFps(-1.0f), m_bLoadSucc(true),
		m_frontBackDivide(0), m_type(0), m_priority(0), m_bindType(XPEBT_ORIGIN), m_scaleType(XPEST_NOSCALE),
		m_loadingCount(0), m_bHasXML(false), m_bIsRemoveAudio(false), m_bScreenCrood(false), m_bHoldRes(false)
	{
		m_nLoopNum = 1;
		m_playMode = XPPM_LOOPNUM;
		m_clipLayers.resize(MAX_CLIP_LAYER);
	}

	GEffect::~GEffect()
	{
		DeleteAllClip();
	}

	void GEffect::DeleteAllClip()
	{
		for ( LAYER_LIST::iterator i = m_clipLayers.begin(), e = m_clipLayers.end(); i != e; ++i)
		{
			CLIPS_LIST& clipsList = *i;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				delete (*i);
			}
			clipsList.clear();
		}
	}

	//还有如下的未处理
	int GEffect::OnAsyncLoadedXML(XMLIO::CINode *pRoot)
	{
		if (!pRoot)
		{
			NotifyAsyncLoadResult(false);
			return EC_LOAD_XML_ERROR;
		}
		int result = LoadEffect(*pRoot, EFFECT_IO_NORMAL);
		//在这里肯定是异步的
		switch(AssureResource(true))
		{
		case XPEARR_RESREADY:
			NotifyAsyncLoadResult(true);
			break;
		case XPEARR_LOADING:
			break;
		case XPEARR_LOAD_ERROR:
			NotifyAsyncLoadResult(false);
			break;
		}
		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				Effect* pEffect = (*i)->GetAnimation();
				if (pEffect)
				{
					pEffect->SetSoundType(m_type);
					pEffect->SetSoundPriority(m_priority);
					pEffect->SetScaleType(m_scaleType);
					pEffect->SetScreenCrood(m_bScreenCrood);
					if (m_bIsRemoveAudio)
						pEffect->RemoveAudio();
					if (m_bHoldRes)
						pEffect->HoldRes();
				}
			}
		}
		return result;
	}

	//如果GEffect的XML文件发生变化，记得CToolsMap导出检查那里也要改
	int GEffect::LoadEffect(const std::wstring &name)
	{
		DeleteAllClip();
		std::wstring uri = L"/effect/" + name + Effect_ExtName;
		XBuffer xmlfiledata;
		if( !m_pEngine->GetFileIOManager()->GetFileImage(uri, xmlfiledata))
		{
			return EC_LOAD_XML_ERROR;
		}
		XMLIO::CFileReader fr;
		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
		{
			fr.CloseFile();
			return EC_PARSE_XML_ERROR;
		}

		XMLIO::CINode root;
		if (!fr.GetRootNode(root))
		{
			fr.CloseFile();
			return EC_PARSE_XML_ERROR;
		}
		m_name = name;
		int result = LoadEffect(root, EFFECT_IO_NORMAL);
		fr.CloseFile();
		if (m_bIndependentSound)
		{
			std::wstring audiouri = L"/effect/" + name + L".audio.xml";
			XBuffer xmlsfiledata;
			if( m_pEngine->GetFileIOManager()->GetFileImage(audiouri, xmlsfiledata))
			{
				XMLIO::CFileReader afr;
				int nError = afr.OpenData(xmlsfiledata.constbegin(), xmlsfiledata.size());
				if( XMLIO::EC_SUCCESS == nError)
				{
					XMLIO::CINode aroot;
					if (afr.GetRootNode(aroot))
					{
						LoadEffectClips(aroot, EFFECT_IO_NORMAL);
					}
				}
				afr.CloseFile();
			}
		}
		return result;
	}

	int GEffect::LoadEffectClips(XMLIO::CINode& root, int flag)
	{
		XMLIO::CNodeList clipsNodeList;
		root.GetChildren( clipsNodeList);
		int result, clipResult;
		result = EC_S_OK;
		for ( XMLIO::CNodeList::iterator i = clipsNodeList.begin(), e = clipsNodeList.end(); i != e; ++i)
		{
			XMLIO::CINode& node = *i;
			if (node.GetType() != XMLIO::NT_ELEMENT)
				continue;

			EffectClip* clips = new EffectClip();
			if ( !clips) continue;

			clipResult = clips->Load( node, m_pEngine, flag);
			if ( clipResult != EffectClip::ET_S_OK)
			{
				switch (clipResult)
				{
				case EffectClip::ET_LOAD_ATTRIBUTE_FAILD:
					XPLOG_ERROR(L"特效文件 %s 导入clip错误，没有找到res_file属性\n", m_name.c_str());
					result |= EC_LOAD_ATTRIBUTE_FAILD;
					break;
				case EffectClip::ET_LOAD_NO_RES:
					{
						std::wstring errorstr;
						node.GetAttribute(L"r_f", errorstr);
						errorstr = L"特效文件 %s 导入clip错误，加载资源 " + errorstr + L" 失败\n";
						XPLOG_ERROR(errorstr.c_str(), m_name.c_str());
						result |= EC_LOAD_NO_RES;
					}
					break;
				case EffectClip::ET_LOAD_RES_FAILD:
					{
						std::wstring errorstr;
						node.GetAttribute(L"r_f", errorstr);
						errorstr = L"特效文件 %s 导入clip错误，加载 " + errorstr + L" 的参数失败\n";
						XPLOG_ERROR(errorstr.c_str(), m_name.c_str());
						result |= EC_LOAD_RES_FAILD;
					}
					break;
				}
				delete clips;
				continue;
			}
			clips->GetAnimation()->SetSoundType(m_type);
			clips->GetAnimation()->SetSoundPriority(m_priority);
			CLIPS_LIST& clipsList = m_clipLayers[clips->GetLayer()];
			clipsList.push_back( clips);
		}
		m_bHasXML = true;
		return result;
	}

	int GEffect::LoadEffect(XMLIO::CINode& root, int flag)
	{
		m_bIsRemoveAudio = false;
		m_totoalFrames = root.GetIntAttribute( L"t_f");
		m_frontBackDivide = root.GetIntAttribute( L"d_l");
		m_relBBox.left = root.GetIntAttribute( L"b_l");
		m_relBBox.top = root.GetIntAttribute( L"b_t");
		m_relBBox.right = root.GetIntAttribute( L"b_r");
		m_relBBox.bottom = root.GetIntAttribute( L"b_b");
		m_bindType = static_cast<XPEFFECT_BIND_TYPE>(root.GetIntAttribute(L"bT"));
		SetFps( (float)root.GetFloatAttribute( L"fps", 8.0f));
		bool bIndependentSound = (root.GetIntAttribute( L"hasA", 1) != 0);
		m_state = XPES_EMPTY;
		int result = LoadEffectClips(root, flag);
		if (bIndependentSound)
		{
			std::wstring audiouri = L"/effect/" + m_name + L".audio.xml";
			XBuffer xmlsfiledata;
			if( m_pEngine->GetFileIOManager()->GetFileImage(audiouri, xmlsfiledata))
			{
				XMLIO::CFileReader afr;
				int nError = afr.OpenData(xmlsfiledata.constbegin(), xmlsfiledata.size());
				if( XMLIO::EC_SUCCESS == nError)
				{
					XMLIO::CINode aroot;
					if (afr.GetRootNode(aroot))
					{
						LoadEffectClips(aroot, flag);
					}
				}
				afr.CloseFile();
			}
		}
		return result;
	}

	bool GEffect::SetDivideLayer( int layer)
	{
		if ( layer < 0 || layer > MAX_CLIP_LAYER)
			return false;
		m_frontBackDivide = layer;
		return true;
	}

	void GEffect::ResetAllClip()
	{
		for ( LAYER_LIST::iterator i = m_clipLayers.begin(), e = m_clipLayers.end(); i != e; ++i)
		{
			CLIPS_LIST& clipsList = *i;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->ResetAni();
			}
		}
	}

	// 如果特效里面有声音，声音的类型和优先级。注意：要在声音播放出来之前设置，否则无效
	void GEffect::SetSoundType(unsigned char type) 
	{
		if (m_type != type)
		{
			for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
			{
				CLIPS_LIST& clipsList = *lyIter;
				for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
				{
					Effect* pEffect = (*i)->GetAnimation();
					if (pEffect)
					{
						pEffect->SetSoundType(type);
					}
				}
			}
			m_type = type; 
		}
	}

	void GEffect::RemoveAudio()
	{
		m_bIsRemoveAudio = true;
		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				Effect* pEffect = (*i)->GetAnimation();
				if (pEffect)
				{
					pEffect->RemoveAudio();
				}
			}
		}
	}
	
	void GEffect::SetSoundPriority(short priority) 
	{ 
		if (m_priority == priority)
			return;
		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				Effect* pEffect = (*i)->GetAnimation();
				if (pEffect)
				{
					pEffect->SetSoundPriority(priority);
				}
			}
		}
		m_priority = priority; 
	}

	void GEffect::SetScreenCrood(bool b)
	{
		if (m_bScreenCrood == b)
			return;
		m_bScreenCrood = b;
		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				if ((*i)->GetAnimation())
				{
					(*i)->GetAnimation()->SetScreenCrood(b);
				}
			}
		}
	}

	void GEffect::SetScaleType(XPEFFECT_SCALE_TYPE t)
	{
		if (m_scaleType != t)
		{
			for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
			{
				CLIPS_LIST& clipsList = *lyIter;
				for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
				{
					if ((*i)->GetAnimation())
					{
						(*i)->GetAnimation()->SetScaleType(t);
					}
				}
			}
			m_scaleType = t;
		}
		
	}

	bool GEffect::Update(DWORD tickTime)
	{
		if (m_state != XPES_PLAYING) return true;
		if (m_nCurTime < 0)
		{
			m_nCurTime = 0;
			tickTime = 0;
		}
		m_nCurTime += tickTime;
		float curFrame = GetPlayingFrame();
		
		if (curFrame > m_totoalFrames)
		{
			NotifyEnd();
			if (m_state != XPES_PLAYING) return true;
			++m_curLoopNum;
			switch (m_playMode)
			{
			case XPPM_LOOPNUM:
				if ( m_curLoopNum >= m_nLoopNum)
				{
					Stop();
					return false;
				}
				//这里不用break;
			case XPPM_LOOP:
				curFrame = float((int)curFrame % m_totoalFrames);
				m_nCurTime = int(curFrame * 1000 / m_fFps);
				ResetAllClip();
				break;
			default:
				Stop();
				return false;

			}
		}
		FPOINT pos = m_pos;
		pos += m_translate;
		XPVECTOR4 color = m_vertexColor.ToXPVECTOR4();

		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->Update(curFrame, tickTime, pos, m_scale, m_rotationRadian, color, m_dir);
			}
		}
		return true;
	}

	void GEffect::OnAsyncLoaded(IEffect *pEffect, bool succeeded)
	{
		--m_loadingCount;
		if (succeeded)
			m_bLoadSucc = true;
		if (m_loadingCount == 0)	//已经加载完了
		{
			if (m_bLoadSucc)
			{
				m_state = XPES_STOP;
				this->Play();
			} else {
				m_state = XPES_EMPTY;
			}
			NotifyAsyncLoadResult(m_bLoadSucc);
		}
	}

	XPEffectAssureResResult GEffect::AssureResource(bool async)
	{
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		if (!m_bHasXML)
		{
			XPASSERT(m_pEngine && L"AssureResource");
			EffectManager *pEffectMan = m_pEngine->GetEffectManager();
			XPASSERT(pEffectMan && L"AssureResource");
			GEffectXMLCaches *pCaches = pEffectMan->GetGEffectCache();
			XPASSERT(pCaches && L"AssureResource");
			const GEffectData& effectData = pCaches->GetGEffectData(m_name, this, async);
			if (effectData.pRoot)
			{
				//xml文件已经加载完成了，如果是ToolsEffect，AssureResource的时候，m_bHasXML会是true了
				LoadEffect(*effectData.pRoot, EFFECT_IO_NORMAL);//在这里会将m_bHasXML设为true
			} else {
				return effectData.bLoading?XPEARR_LOADING:XPEARR_LOAD_ERROR;
			}
		}
		m_bLoadSucc = !async;
		XPEffectAssureResResult result = XPEARR_RESREADY, tmp;
		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				tmp = (*i)->GetAnimation()->AssureResource(async);
				result = max(tmp, result);
				switch (tmp)
				{
				case XPEARR_LOADING:
					++m_loadingCount;
					(*i)->GetAnimation()->AddNotify(this);
					break;
				case XPEARR_RESREADY:
					m_bLoadSucc = true;
					break;
				}
			}
		}
		if (result == XPEARR_RESREADY)
		{
			m_state = XPES_STOP;
			this->Play();
		}
		return result;
	}

	void GEffect::Release()
	{
		if (m_state != XPES_STOP) 
			return;
		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->GetAnimation()->Release();
			}
		}
		m_state = XPES_EMPTY;
	}

	void GEffect::HoldRes()
	{
		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->GetAnimation()->HoldRes();
			}
		}
		m_bHoldRes = true;
	}

	bool GEffect::Render(Canvas* canvas)
	{
		if (m_state == XPES_STOP || m_state == XPES_EMPTY) 
			return false;
		RenderBack(canvas);
		RenderFront(canvas);
		return true;
	}

	//! 渲染前端（前端后渲染，也就是说能挡住人）
	void GEffect::RenderFront(Canvas* canvas)
	{
		for ( unsigned int i = m_frontBackDivide; i < m_clipLayers.size(); ++i)
		{
			CLIPS_LIST& clipsList = m_clipLayers[i];
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->Render(canvas);
			}
		}
	}

	//! 渲染后端（后端先渲染，也就是说能被人挡住）
	void GEffect::RenderBack(Canvas* canvas)
	{
		XPASSERT(m_frontBackDivide <= (int)m_clipLayers.size());
		for ( int i = 0; i < m_frontBackDivide; ++i)
		{
			CLIPS_LIST& clipsList = m_clipLayers[i];
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->Render(canvas);
			}
		}
	}

	int GEffect::LoadAttribute(XMLIO::CINode& node)
	{
		float fps = (float)node.GetFloatAttribute( L"fps");
		SetFps(fps);
		m_pos.x = node.GetIntAttribute( L"rel_x");
		m_pos.y = node.GetIntAttribute( L"rel_y");
		m_vertexColor.data = node.GetUIntAttribute( L"v_c", 0xFFFFFFFF);

		// 播放控制
		m_playMode = (XPPLAY_MODE)node.GetIntAttribute( L"p_m");
		if ( IsLoopNum())
			m_nLoopNum = node.GetIntAttribute( L"l_n" );
		else if ( IsPlayTime())
			m_nPlayTime = node.GetIntAttribute( L"p_t" );


		return 0;
	}

	int GEffect::SaveAttribute(XMLIO::CONode& node)
	{
		node.SetAttribute( L"fps", GetFPS());
		node.SetAttribute( L"rel_x", m_pos.x);
		node.SetAttribute( L"rel_y", m_pos.y);
		node.SetAttribute( L"v_c", (unsigned int)m_vertexColor.data);

		// 播放控制
		node.SetAttribute( L"p_m", (int)m_playMode);
		if ( IsLoopNum())
			node.SetAttribute( L"l_n", m_nLoopNum);
		else if( IsPlayTime())
			node.SetAttribute( L"p_t", m_nPlayTime);	
		return 0;
	}

	bool GEffect::Stop()
	{
		if (Effect::Stop())
		{
			for ( LAYER_LIST::iterator layerIter = m_clipLayers.begin(), layerEnd = m_clipLayers.end(); 
				layerIter != layerEnd; ++layerIter)
			{
				CLIPS_LIST& clipsList = *layerIter;
				for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
				{
					(*i)->GetAnimation()->Stop();
				}
			}
			ResetAllClip();
			return true;
		}
		return false;
	}

	bool GEffect::GetBase(XPBase &base)
	{
		base.left.x = -10;
		base.left.y = 0;
		base.right.x = 10;
		base.right.y = 0;
		return true;
	}
}