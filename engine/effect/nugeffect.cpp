#include "../common/nulog.h"
#include "utils/Utils.h"
#include "nugeffect.h"
#include "nueffectclip.h"

#include "../../dependencies/LJXML//Include/LJXML.hpp"


namespace Nuclear
{
	GEffect::GEffect(EngineBase *pEB) : Effect(pEB), m_totoalFrames(100), m_fFps(8.0f), m_bLoadSucc(true),
		m_frontBackDivide(0), m_type(0), m_priority(0), m_bindType(XPEBT_ORIGIN), m_loadingCount(0), m_bIndependentSound(true)
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
			for ( CLIPS_LIST::iterator j = clipsList.begin(), ej = clipsList.end(); j != ej; ++j)
			{
				delete (*j);
			}
			clipsList.clear();
		}
	}

	//如果GEffect的XML文件发生变化，记得ToolsPMap导出检查那里也要改
	int GEffect::LoadEffect(const std::wstring &name)
	{
		DeleteAllClip();
		std::wstring uri = L"/effect/" + name + Effect_ExtName;
		char* fileBuf = NULL;
		std::streamsize ss = 0;
		if (GetBuffFromFile(uri, fileBuf, ss) != 0)
		{
			return EC_PARSE_XML_ERROR;
		}

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, (size_t)ss, fileData, codeType))
		{
			delete[] fileBuf;
			return EC_PARSE_XML_ERROR;
		}

		delete[] fileBuf;

		LJXML::LJXML_Doc<LJXML::Char> doc;
		LJXML::Char* charData = doc.LoadFromString(fileData);
		if (!doc.first_node())
		{
			return EC_PARSE_XML_ERROR;
		}

		m_name = name;

		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		int result = LoadEffect(root, EFFECT_IO_NORMAL);
		if (m_bIndependentSound)
		{
			std::wstring audiouri = L"/effect/" + name + L".audio.xml";
			char* fileBuf = NULL;
			std::streamsize ss = 0;
			if (GetBuffFromFile(audiouri, fileBuf, ss) == 0)
			{
				delete[] fileBuf;
				std::wstring aFileData;
				LJXMLStringHelper::EncodeLJ codeType;
				if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, (size_t)ss, aFileData, codeType))
				{
					delete[] fileBuf;

					LJXML::LJXML_Doc<LJXML::Char> aDoc;
					LJXML::Char* aCharData = aDoc.LoadFromString(aFileData);

					LJXML::LJXML_Node<LJXML::Char>* aroot = (LJXML::LJXML_Node<LJXML::Char>*)aDoc.first_node();
					LoadEffectClips(aroot, EFFECT_IO_NORMAL);
				}
			}
		}

		return result;
	}

	int GEffect::LoadEffectClips(LJXML::LJXML_Node<LJXML::Char>* pRoot, int flag)
	{
		if (!pRoot)
			return EC_LOAD_XML_ERROR;

		int result, clipResult;
		result = EC_S_OK;

		LJXML::LJXML_NodeList typenl;
		pRoot->GetSubNodeList(typenl);
		for (size_t i = 0; i < typenl.size(); i++)
		{
			if (typenl[i]->type() != rapidxml::node_element)
				continue;

			EffectClip* clips = new EffectClip();
			if ( !clips) continue;

			clipResult = clips->Load(typenl[i], m_pEB, flag);
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
						std::wstring errorstr = typenl[i]->first_attribute(L"r_f")->value();
						//errorstr = L"特效文件 %s 导入clip错误，加载资源 " + errorstr + L" 失败\n";
						errorstr = L"geffect load" + errorstr;
                        XPLOG_ERROR(errorstr.c_str(), m_name.c_str());
						result |= EC_LOAD_NO_RES;
					}
					break;
				case EffectClip::ET_LOAD_RES_FAILD:
					{
						std::wstring errorstr = typenl[i]->first_attribute(L"r_f")->value();
						//errorstr = L"特效文件 %s 导入clip错误，加载 " + errorstr + L" 的参数失败\n";
						errorstr = L"geffect load" + errorstr;
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
		return result;
	}

	int GEffect::LoadEffect(LJXML::LJXML_Node<LJXML::Char>* pRoot, int flag)
	{
		if (!pRoot) return EC_LOAD_RES_FAILD;

		m_totoalFrames = _wtoi(pRoot->first_attribute(L"t_f")->value());
		m_frontBackDivide = _wtoi(pRoot->first_attribute(L"d_l")->value());
		m_relBBox.left = _wtoi(pRoot->first_attribute(L"b_l")->value());
		m_relBBox.top = _wtoi(pRoot->first_attribute(L"b_t")->value());
		m_relBBox.right = _wtoi(pRoot->first_attribute(L"b_r")->value());
		m_relBBox.bottom = _wtoi(pRoot->first_attribute(L"b_b")->value());
		m_bindType = pRoot->first_attribute(L"bT") ? static_cast<Nuclear_EffectBindType>(_wtoi(pRoot->first_attribute(L"bT")->value())) : XPEBT_NULL;
		float fFps = pRoot->first_attribute(L"fps") ? (float)_wtof(pRoot->first_attribute(L"fps")->value()) : 8.0f;
		SetFps(fFps);
		int iTmp = pRoot->first_attribute(L"hasA") ? _wtoi(pRoot->first_attribute(L"hasA")->value()) : 1;
		m_bIndependentSound = (iTmp != 0);
		m_state = XPES_EMPTY;
		return LoadEffectClips(pRoot, flag);
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
			for ( CLIPS_LIST::iterator j = clipsList.begin(), ej = clipsList.end(); j != ej; ++j)
			{
				(*j)->ResetAni();
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
		if (m_priority != priority)
		{
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
	}

	void GEffect::SetScreenCrood(bool b)
	{
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

	bool GEffect::IsScreenCrood() const
	{
		for ( LAYER_LIST::const_iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			const CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::const_iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				const Effect *pEffect = (*i)->GetAnimation();
				if (pEffect && pEffect->GetType() != XPET_AUDIO)
				{
					return pEffect->IsScreenCrood();//直接return，应该是一样的
				}
			}
		}
		return false;//如果是空的就默认false好了
	}

	void GEffect::SetScaleType(Nuclear_EffectScaleType t)
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
	}

	Nuclear_EffectScaleType GEffect::GetScaleType() const
	{
		for ( LAYER_LIST::const_iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			const CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::const_iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				const Effect *pEffect = (*i)->GetAnimation();
				if (pEffect && pEffect->GetType() != XPET_AUDIO)
				{
					return pEffect->GetScaleType();//直接return，应该是一样的
				}
			}
		}
		return XPEST_NOSCALE;//空的就return这个吧
	}

	bool GEffect::Update(DWORD tickTime)
	{
		if (!m_bVisiblity) return true;
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
				if (m_totoalFrames > 0)
				{
					curFrame = float((int)curFrame % m_totoalFrames);
				}
				if (m_fFps > 0)
				{
					m_nCurTime = int(curFrame * 1000 / m_fFps);
				}
				ResetAllClip();
				break;
			default:
				Stop();
				return false;

			}
		}
		NuclearFPoint pos = m_pos;
		pos += m_translate;
		NuclearVector4 color = m_vertexColor.ToXPVECTOR4();

		for ( LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
		{
			CLIPS_LIST& clipsList = *lyIter;
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->Update( curFrame, tickTime, pos, m_scale, m_rotationRadian, color, m_dir);
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

	Nuclear_EffectAssureResResult GEffect::AssureResource(bool async)
	{
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		m_bLoadSucc = !async;
		Nuclear_EffectAssureResResult result = XPEARR_RESREADY;
		Nuclear_EffectAssureResResult tmp;
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
	}

	bool GEffect::Render(Canvas* canvas, bool realRender)
	{
		if (!m_bVisiblity) return true;
		if (m_state == XPES_STOP || m_state == XPES_EMPTY)
			return false;
		RenderBack(canvas, realRender);
		RenderFront(canvas, realRender);
		return true;
	}

	//! 渲染前端（前端后渲染，也就是说能挡住人）
	void GEffect::RenderFront(Canvas* canvas, bool realRender)
	{
		for ( unsigned int i = m_frontBackDivide; i < m_clipLayers.size(); ++i)
		{
			CLIPS_LIST& clipsList = m_clipLayers[i];
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->Render(canvas, realRender);
			}
		}
	}

	//! 渲染后端（后端先渲染，也就是说能被人挡住）
	void GEffect::RenderBack(Canvas* canvas, bool realRender)
	{
		XPASSERT(m_frontBackDivide <= (int)m_clipLayers.size());
		for ( int i = 0; i < m_frontBackDivide; ++i)
		{
			CLIPS_LIST& clipsList = m_clipLayers[i];
			for ( CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
			{
				(*i)->Render(canvas, realRender);
			}
		}
	}

	int GEffect::LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode)
	{
		SetFps((float)_wtof(pNode->first_attribute(L"fps")->value()));
		m_pos.x = _wtoi(pNode->first_attribute(L"rel_x")->value());
		m_pos.y = _wtoi(pNode->first_attribute(L"rel_y")->value());
		m_vertexColor.data = pNode->first_attribute(L"v_c") ? _wtou(pNode->first_attribute(L"v_c")->value()) : 0xFFFFFFFF;

		// 播放控制
		m_playMode = (NuclearPlayMode)_wtoi(pNode->first_attribute(L"p_m")->value());
		if ( IsLoopNum())
			m_nLoopNum = _wtoi(pNode->first_attribute(L"l_n")->value());
		else if ( IsPlayTime())
			m_nPlayTime = _wtoi(pNode->first_attribute(L"p_t")->value());

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

	bool GEffect::GetBase(NuclearBase &base)
	{
		base.left.x = -10;
		base.left.y = 0;
		base.right.x = 10;
		base.right.y = 0;
		return true;
	}

	void GEffect::AddEffectClip(EffectClip* clip)
	{
		CLIPS_LIST& clipsList = m_clipLayers[clip->GetLayer()];
		clipsList.push_back(clip);
	}

	void GEffect::Clone(Effect* p)
	{
		Effect::Clone(static_cast<Effect*>(p));
		GEffect* pDst = static_cast<GEffect*>(p);
		pDst->DeleteAllClip();
		pDst->SetTotalFrames(m_totoalFrames);
		pDst->SetDivideLayer(m_frontBackDivide);
		pDst->SetEffectBindType(m_bindType);
		pDst->SetFps(GetFPS());
		pDst->SetLocation(m_pos);
		pDst->SetIndependentSound(m_bIndependentSound);
		pDst->SetVertexColor(m_vertexColor);
		pDst->SetPlayMode(m_playMode);
		pDst->SetCurLoopNum(m_curLoopNum);
		pDst->SetPlayTime(m_nPlayTime);

		for (LAYER_LIST::iterator i = m_clipLayers.begin(), e = m_clipLayers.end(); i != e; ++i)
		{
			CLIPS_LIST& clipsList = *i;
			for (CLIPS_LIST::iterator j = clipsList.begin(), ej = clipsList.end(); j != ej; ++j)
			{
				EffectClip* srcClip = *j;
				EffectClip* dstClip = new EffectClip();
				srcClip->Clone(dstClip, m_pEB, EFFECT_IO_NORMAL);
				dstClip->GetAnimation()->SetSoundType(m_type);
				dstClip->GetAnimation()->SetSoundPriority(m_priority);
				pDst->AddEffectClip(dstClip);
			}
		}
	}
}