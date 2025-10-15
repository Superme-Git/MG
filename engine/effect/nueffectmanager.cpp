//#include "../../../segmpak/segmpak/segmclient.h"
#include "../common/nulog.h"
#include "../common/nuljfmutil.h"
#include "../common/nufileiomanager.h"
#include "../common/nufileiomanager.h"
#include "../engine/nuanimanager.h"
#include "../particlesystem/nuiparticlemanager.h"
#include "../renderer/nurenderer.h"
#include "../sprite/nusprite.h"
#include "utils/Utils.h"
#include "nueffect.h"
#include "nueffectmanager.h"
#include "nuxapeffect.h"
#include "nulightingeffect.h"
#include "nuparticleeffect.h"
#include "nugeffect.h"
#include "nuspineeffect.h"
#include "nulisteffect.h"
#include "nusebind.h"

#define MaxGEffectLoadValue 3		//需要进入到GEffect模板中的加载GEffect时间阈值

namespace Nuclear
{
	EffectManager* EffectGC::pEffectMan = NULL;
	class ContinueEffectInfo : public IEffectNotify
	{
	public:
		Effect *pEffects[3];
		ListEffect* pListEffect;
		int durTime;
		bool cycle;
		unsigned char remainEffect;
		ContinueEffectInfo() : pListEffect(NULL), durTime(0), cycle(false), remainEffect(0)
		{
			pEffects[0] = pEffects[1] = pEffects[2] = NULL;
		}
		virtual void OnAsyncLoaded(IEffect *pEffect, bool succeeded)
		{
			remainEffect -= 1;
			unsigned char i = 0;
			for (i=0;i<3;++i)
			{
				if (pEffect == pEffects[i])
				{
					if (!succeeded)
					{
						delete pEffect;
						pEffects[i] = NULL;
                        pEffect = NULL;
					}
					break;
				}
			}
            if(pEffect)
            {
                pEffect->RemoveNotify(this);
                pEffect->Play();
            }

			switch (CheckSuccess())
			{
			case -1:
			case 0:
				delete this;
				break;
			default:
				break;
			}
		}
		int CheckSuccess()
		{
			if (remainEffect == 0)
			{
				if ((!pEffects[0]) && (!pEffects[1]) && (!pEffects[2]))//一个都没有
				{
					pListEffect->NotifyAsyncLoadResult(false);
					return -1;
				}
				if (pEffects[0])//begin
					durTime -= pEffects[0]->GetPlayOnceTime();
				if (pEffects[2])//end
					durTime -= pEffects[2]->GetPlayOnceTime();
				if (durTime <= 1)
					durTime = 1;
				if (pEffects[1])//mid
				{
					int onceTime = pEffects[1]->GetPlayOnceTime();
					if (onceTime > 0)
					{
						pEffects[1]->SetLoopNum((durTime + onceTime - 1) / onceTime);
					}
				}
				pListEffect->PushAnEffect(pEffects[0]);
				pListEffect->PushAnEffect(pEffects[1]);
				pListEffect->PushAnEffect(pEffects[2]);
				pListEffect->AssureResource(false);
				pListEffect->SetLoopNum(cycle?-1:1);
				pListEffect->Play();
				pListEffect->NotifyAsyncLoadResult(true);
				return 0;
			}
			return 1;
		}

	};

	EffectManager::EffectManager(EngineBase *pEB) : m_pEB(pEB), m_pSebindManager(new SebindManager(pEB))
	{
		EffectGC::pEffectMan = this;
	}

	EffectManager::~EffectManager()
	{
		OnTick();
		EffectGC::pEffectMan = NULL;
		delete m_pSebindManager;

		for (GEffectTempletMap::iterator it = m_GEffectTempletMap.begin(); it != m_GEffectTempletMap.end(); ++it)
		{
			GEffect* geffect = dynamic_cast<GEffect*>(it->second);
			if (geffect)
			{
				delete geffect;//it->second;
			}
		}
		m_GEffectTempletMap.clear();

		for (SEffectMap::iterator it = m_SEffects.begin(); it != m_SEffects.end(); ++it)
		{
			if (it->second)
				delete it->second;
		}
		m_SEffects.clear();
	}

	bool EffectManager::PreInit()
	{
		char* fileBuf = NULL;
		std::streamsize ss = 0;
		if (GetBuffFromFile(L"/effect/spine/spine_effect.set", fileBuf, ss) != 0)
		{
			return false;
		}

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, (size_t)ss, fileData, codeType))
		{
			delete[] fileBuf;
			return false;
		}

		delete[] fileBuf;

		LJXML::LJXML_Doc<LJXML::Char> doc;
		LJXML::Char* charData = doc.LoadFromString(fileData);
		if (!doc.first_node())
		{
			return false;
		}

		bool r = true;

		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		LJXML::LJXML_NodeList nl;
		root->GetSubNodeList(nl);
		for (size_t i = 0; i < nl.size(); i++)
		{
			if (std::wstring(nl[i]->name()).compare(L"effect") != 0) continue;

			std::wstring namestr = nl[i]->first_attribute(L"name")->value();
			std::wstring desstr = nl[i]->first_attribute(L"des")->value();

			if (namestr.empty() || desstr.empty() || m_SEffects.find(namestr) != m_SEffects.end())
			{
				r = false;
				continue;
			}

			std::wstring strKey = namestr;
			SEffect *pSEffect = new SEffect;
			pSEffect->m_name = namestr;
			pSEffect->m_des = desstr;
			m_SEffects[strKey] = pSEffect;
		}

		return r;
	}

	ParticleEffect* EffectManager::CreateLinkedEffect(std::wstring name, const NuclearPoint &pt1, const NuclearPoint &pt2, float time)
	{
		if (name == L"")
			return NULL;

		if( 0 != name.find(L"par") )
			return NULL;
		
		bool bIsSegmpak = false;

		ParticleEffect *pParticleEffect = new ParticleEffect(m_pEB);
		IParticleManager *pParMgr = m_pEB->GetRenderer()->GetParticleManager();
		pParticleEffect->m_name = name;
		pParticleEffect->m_parmeter.cycle = time < 0.0f;
		pParticleEffect->m_parmeter.autorender = false;
		pParticleEffect->m_parmeter.layer = 1;
		pParticleEffect->m_parmeter.m_bModifyLineEmitter = true;
		pParticleEffect->m_parmeter.m_ptLineEmitterStrt = pt1.ToFPOINT();
		pParticleEffect->m_parmeter.m_ptLineEmitterEnd = pt2.ToFPOINT();
		pParticleEffect->SetSegmPak(bIsSegmpak);

		std::wstring::size_type npos = name.find(L"particle/psl/");
		if( npos != std::wstring::npos )
			name = name.substr(13);

		pParticleEffect->m_handle = pParMgr->CreateParticleSystem(name, pParticleEffect->m_parmeter, false, bIsSegmpak);
		if (pParticleEffect->m_handle == INVALID_PS_HANDLE)
		{
			delete pParticleEffect;
			return NULL;
		}
		pParticleEffect->m_relBBox = pParMgr->GetParticleSystemInfo(pParticleEffect->m_handle).m_rctMaxBox;
		if (pParticleEffect->m_parmeter.cycle)
		{
			pParticleEffect->SetCycleMode(XPPCM_PULSE_EMISSION);
		} else {
			pParticleEffect->SetCycleMode(XPPCM_ALWAY_EMISSION);
			pParticleEffect->SetSysLife(time);
		}
		pParticleEffect->m_pos = (pt1 + pt2);
		pParticleEffect->m_pos /= 2;
		//这里的systemLevel没有用，粒子自己会管理
		return pParticleEffect;
	}

	FontEffect* EffectManager::CreateFontEffect(TextBlockHandle textHandle, const sNuclearFontEffectParam& structFontParam)
	{
        assert(false);
        return NULL;
//		FontEffect *pFontEffect = new FontEffect(m_pEB, textHandle, structFontParam);
//		return pFontEffect;
	}

	Effect* EffectManager::GetEffectTemplet(const std::wstring& effectname)
	{
		GEffectTempletMap::iterator it = m_GEffectTempletMap.find(effectname);
		if (it != m_GEffectTempletMap.end())
		{
			return it->second;
		}
		return NULL;
	}

	bool EffectManager::GetEffectAni(IEffect* pEffect, bool bPerpetualMem)
	{
		//return false;
		GEffect* geffect = dynamic_cast<GEffect*>(pEffect);
		if (!geffect) return false;

		bool bIsSegmpak = false;
		for (int i = 0; i < geffect->m_clipLayers.size(); i++)
		{
			GEffect::CLIPS_LIST plist = geffect->m_clipLayers[i];
			for (int j = 0; j < plist.size(); j++)
			{
				EffectClip* pClip = plist[j];
				Effect* pUnEffect = pClip->GetAnimation();
				if (pUnEffect->GetType() == XPET_ANI)
				{
					XapEffect* pXapEffect = (XapEffect*)pUnEffect;
					m_pEB->GetAniManager()->GetAni(L"/effect/" + pXapEffect->GetName() + SFI_ExtName, false, bIsSegmpak, bPerpetualMem);
				}
			}
		}
		return true;
	}

	bool EffectManager::LoadEffectAni(std::wstring name, bool bPerpetualMem)
	{
		bool bIsSegmpak = false;
		GEffectTempletMap::iterator it = m_GEffectTempletMap.find(name);
		if (it != m_GEffectTempletMap.end())
		{
			GEffect* geffect = dynamic_cast<GEffect*>(it->second);
			if (!geffect) return false;

			for (int i = 0; i < geffect->m_clipLayers.size(); i++)
			{
				GEffect::CLIPS_LIST plist = geffect->m_clipLayers[i];
				for (int j = 0; j < plist.size(); j++)
				{
					EffectClip* pClip = plist[j];
					Effect* pUnEffect = pClip->GetAnimation();
					if (pUnEffect->GetType() == XPET_ANI)
					{
						XapEffect* pXapEffect = (XapEffect*)pUnEffect;
						m_pEB->GetAniManager()->AsyncLoadAni(L"/effect/" + pXapEffect->GetName() + SFI_ExtName, pXapEffect, bIsSegmpak);
					}
				}
			}
			return true;
		}
		return false;
	}

	Effect* EffectManager::CreateEffect(std::wstring name, bool cycle, bool bPreLoad)
	{
		if (name == L"")
			return NULL;

		bool bIsSegmpak = false;		
		
		SEffectMap::iterator it = m_SEffects.find(name);
		if (it == m_SEffects.end())//系列帧特效
		{
			if( 0 == name.find(L"ani") )
			{
				// 创建 XapEffect
				XapEffect* pXapEffect = new XapEffect(m_pEB);
				pXapEffect->m_name = name;
				pXapEffect->SetLoopNum(cycle?-1:1);
				pXapEffect->SetSegmPak(bIsSegmpak);
				return pXapEffect;
			}
			else if (0 == name.find(L"ele"))
			{				
				if (!bPreLoad)
				{// 创建 lighting Effect
					lightingEffect* pXapEffect = new lightingEffect(m_pEB);
					pXapEffect->m_name = name;
					pXapEffect->SetLoopNum(cycle ? -1 : 1);
					pXapEffect->SetSegmPak(bIsSegmpak);
					return pXapEffect;
				}
			}
			else if( 0 == name.find(L"par") )
			{
				// 创建 particle effect
				ParticleEffect *pParticleEffect = new ParticleEffect(m_pEB);
				IParticleManager *pParMgr = m_pEB->GetRenderer()->GetParticleManager();
				pParticleEffect->m_name = name;
				pParticleEffect->m_parmeter.cycle = cycle;
				pParticleEffect->m_parmeter.autorender = false;
				pParticleEffect->m_parmeter.layer = 1;
				std::wstring::size_type npos = name.find(L"particle/psl/");
				if( npos != std::wstring::npos )
					name = name.substr(13);
				pParticleEffect->SetSegmPak(bIsSegmpak);
				pParticleEffect->m_handle = pParMgr->CreateParticleSystem(name, pParticleEffect->m_parmeter, true, bIsSegmpak);
				if (pParticleEffect->m_handle == INVALID_PS_HANDLE)
				{
					delete pParticleEffect;
					return NULL;
				}
				//pParticleEffect->m_systemLevel = 0;
				pParticleEffect->m_relBBox = pParMgr->GetParticleSystemInfo(pParticleEffect->m_handle).m_rctMaxBox;
				pParticleEffect->SetLoopNum(cycle?-1:1);
			
				//这里的systemLevel没有用，粒子自己会管理
				return pParticleEffect;
			}
			else if ( 0 == name.find(L"geffect") || 0 == name.find(L"character"))
			{
				//从模板队列中查找是否已经存在该模板
				GEffectTempletMap::iterator it = m_GEffectTempletMap.find(name);
				if (it != m_GEffectTempletMap.end())
				{
					if (bPreLoad)
						return it->second;

					GEffect* pGEffect = new GEffect(m_pEB);
					pGEffect->SetSegmPak(bIsSegmpak);
					pGEffect->SetLoopNum(cycle ? -1 : 1);
					GEffect* pSrcGEffect = dynamic_cast<GEffect*>(it->second);
					pSrcGEffect->Clone(pGEffect);
					return pGEffect;
				}
				else
				{
					int64_t startT = Nuclear::GetMilliSeconds();
					GEffect* pGEffect = new GEffect(m_pEB);
					pGEffect->SetSegmPak(bIsSegmpak);

					int result = pGEffect->LoadEffect(name);
					if (result != GEffect::EC_S_OK)
					{
						std::wstring errorstr;
						errorstr = L"effectmanager createeffect" +name ;//L"加载特效文件 " + name + L" 发生错误\n";
						XPLOG_ERROR(errorstr.c_str());
					}
					if (result & (GEffect::EC_LOAD_XML_ERROR | GEffect::EC_PARSE_XML_ERROR))
					{
						delete pGEffect;
						return NULL;
					}
					pGEffect->SetLoopNum(cycle?-1:1);

					if (bPreLoad)
					{
						//int64_t dT = Nuclear::GetMilliSeconds() - startT;
						//if (dT > MaxGEffectLoadValue)
						{
							m_GEffectTempletMap[name] = pGEffect;
							GetEffectAni(pGEffect, true);
						}
						//else
						//{
						//	delete pGEffect;
						//	pGEffect = NULL;
						//}
					}
					return pGEffect;
				}
			
				return NULL;
			}
			else if ( 0 == name.find(L"/sound") )
			{
				return NULL;
			}
			else if( 0 == name.find(L"3d") ) //3d/effect/xxxxxx
			{
				return NULL;
			}
			else
			{
				//XPASSERT(false && L"effect name error");
				//这个名字有问题
				XPLOG_ERROR(L"effect name error: %s\n", name.c_str());
			}
		}
		else//Spine特效
		{
			if (!bPreLoad)
			{
				SpineEffect* pSpineEffect = new SpineEffect(m_pEB, name);
				pSpineEffect->SetLoopNum(cycle ? -1 : 1);
				pSpineEffect->SetSegmPak(bIsSegmpak);
				pSpineEffect->m_isSpine = true;
				return pSpineEffect;
			}
		}

		return NULL;
	}

	Effect* EffectManager::CreateListEffect(const std::wstring& begin, const std::wstring& midcyc, const std::wstring& end, int durTime, bool cycle, bool async, bool removesound)
	{
		Effect *pBegin = NULL, *pMid = NULL, *pEnd = NULL;
		ListEffect* pResult = NULL;
		ContinueEffectInfo* pInfo = new ContinueEffectInfo();
		pInfo->cycle = cycle;
		pInfo->durTime = durTime;
		if (begin != L"")
		{
			pBegin = CreateEffect(begin, false);
			if (pBegin)
			{
				if (removesound)
					pBegin->RemoveAudio();
				switch (pBegin->AssureResource(async))
				{
				case XPEARR_RESREADY:
					pInfo->pEffects[0] = pBegin;
					pBegin->Play();
					break;
				case XPEARR_LOADING:
					pInfo->pEffects[0] = pBegin;
					pInfo->remainEffect += 1;
					pBegin->AddNotify(pInfo);
					break;
				case XPEARR_LOAD_ERROR:
					delete pBegin;
					pBegin = NULL;
					break;
				}				
			}
		}
		if (midcyc != L"")
		{
			pMid = CreateEffect(midcyc, false);
			if (pMid)
			{
				if (removesound)
					pMid->RemoveAudio();
				switch (pMid->AssureResource(async))
				{
				case XPEARR_RESREADY:
					pInfo->pEffects[1] = pMid;
					pMid->Play();
					break;
				case XPEARR_LOADING:
					pInfo->pEffects[1] = pMid;
					pInfo->remainEffect += 1;
					pMid->AddNotify(pInfo);
					break;
				case XPEARR_LOAD_ERROR:
					delete pMid;
					pMid = NULL;
					break;
				}
			}
		}
		if (end != L"")
		{
			pEnd = CreateEffect(end, false);
			if (pEnd)
			{
				if (removesound)
					pEnd->RemoveAudio();
				switch (pEnd->AssureResource(async))
				{
				case XPEARR_RESREADY:
					pInfo->pEffects[2] = pEnd;
					pEnd->Play();
					break;
				case XPEARR_LOADING:
					pInfo->pEffects[2] = pEnd;
					pInfo->remainEffect += 1;
					pEnd->AddNotify(pInfo);
					break;
				case XPEARR_LOAD_ERROR:
					delete pEnd;
					pEnd = NULL;
					break;
				}
			}
		}
		pResult = new ListEffect(m_pEB);
		if (pResult)
		{
			pInfo->pListEffect = pResult;
			switch(pInfo->CheckSuccess())
			{
			case -1:
				delete pResult;
				pResult = NULL;
				break;
			case 0:
				delete pInfo;
			case 1:
				return pResult;
			}
		}
		delete pInfo;
		if (pBegin)
			delete pBegin;
		if (pMid)
			delete pMid;
		if (pEnd)
			delete pEnd;

		return pResult;
	}

	void EffectManager::RemoveEffect(IEffect* pEffect)
	{
		m_willDelEffect.push_back(pEffect);
	}

	void EffectManager::OnTick()
	{
		std::list<IEffect*>::iterator it = m_willDelEffect.begin(), ie = m_willDelEffect.end();
		for (;it!=ie;++it)
		{
			if (!(*it)->m_isSpine) delete *it;
		}
		m_willDelEffect.clear();
	}
}
