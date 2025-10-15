#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\pfsutil.h"
#include "..\common\xml.h"
#include "..\common\fileiomanager.h"
#include "..\engine\animanager.h"
#include "..\particlesystem\iparticlemanager.h"
#include "..\renderer\renderer.h"
#include "..\sprite\sprite.h"
#include "effect.h"
#include "effectmanager.h"
#include "xapeffect.h"
#include "particleeffect.h"
#include "geffect.h"
#include "listeffect.h"
#include "audioeffect.h"
#include "sebind.h"
#include "spineeffect.h"

#include "fonteffect.h"

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
		virtual void OnAsyncLoaded(IEffect *pEffect, bool succeeded) override
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
					}
					break;
				}
			}
			pEffect->RemoveNotify(this);
			pEffect->Play();
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
					pEffects[1]->SetLoopNum((durTime + onceTime - 1) / onceTime);
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

	EffectManager::EffectManager(EngineBase *pEB) : m_pEB(pEB), m_pSebindManager(new SebindManager(pEB)), m_pGEffectCaches(new GEffectXMLCaches(pEB))
	{
		EffectGC::pEffectMan = this;
	}

	EffectManager::~EffectManager()
	{
		OnTick();
		EffectGC::pEffectMan = NULL;
		delete m_pSebindManager;
		delete m_pGEffectCaches;

		for (SEffectMap::iterator it = m_SEffects.begin(); it != m_SEffects.end(); ++it)
		{
			if (it->second)
				delete it->second;
		}
		m_SEffects.clear();
	}

	bool EffectManager::PreInit()
	{
		XMLIO::CFileReader		fr;

		std::wstring xmlpath = std::wstring(L"/effect/spine_effect") + ImageSet_ExtName;
		XBuffer xmlfiledata;
		if( !m_pEB->GetFileIOManager()->GetFileImage(xmlpath, xmlfiledata))
			return false;

		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
			return false;

		XMLIO::CINode	root;
		if( !fr.GetRootNode(root) )
		{
			fr.CloseFile();
			return false;
		}

		bool r = true;

		XMLIO::CNodeList nl;
		root.GetChildren(nl);
		for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if ( L"effect" != node.GetName()) continue;

			std::wstring namestr;
			node.GetAttribute(L"name", namestr);
			std::wstring desstr;
			node.GetAttribute(L"des", desstr);

			if( namestr.empty() || desstr.empty() || m_SEffects.find(namestr) != m_SEffects.end() )
			{
				r = false;
				continue;
			}

			SEffect *pSEffect = new SEffect;
			pSEffect->m_name = namestr;
			pSEffect->m_des = desstr;
			m_SEffects[namestr] = pSEffect;
		}
		fr.CloseFile();
		return r;
	}


	ParticleEffect* EffectManager::CreateLinkedEffect(std::wstring name, const CPOINT &pt1, const CPOINT &pt2, float time)
	{
		if (name == L"")
			return NULL;

		if( 0 != name.find(L"par") )
			return NULL;
		
		// 创建 particle effect
		ParticleEffect *pParticleEffect = new ParticleEffect(m_pEB);
		IParticleManager *pParMgr = m_pEB->GetRenderer()->GetParticleManager();
		pParticleEffect->m_name = name;
		pParticleEffect->m_parmeter.cycle = time < 0.0f;
		pParticleEffect->m_parmeter.autorender = false;
		pParticleEffect->m_parmeter.layer = 1;
		pParticleEffect->m_parmeter.m_bModifyLineEmitter = true;
		pParticleEffect->m_parmeter.m_ptLineEmitterStrt = pt1.ToFPOINT();
		pParticleEffect->m_parmeter.m_ptLineEmitterEnd = pt2.ToFPOINT();

		std::wstring::size_type npos = name.find(L"particle/psl/");
		if( npos != std::wstring::npos )
			name = name.substr(13);

		pParticleEffect->m_handle = pParMgr->CreateParticleSystem(name, pParticleEffect->m_parmeter, false);
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

	FontEffect* EffectManager::CreateFontEffect(TextBlockHandle textHandle, const XPFontEffectParam& structFontParam)
	{
		FontEffect *pFontEffect = new FontEffect(m_pEB, textHandle, structFontParam);
		return pFontEffect;
	}

	Effect* EffectManager::CreateEffect(std::wstring name, bool cycle)
	{
		if (name == L"")
			return NULL;

		if( 0 == name.find(L"ani") )
		{
			// 创建 XapEffect
			XapEffect* pXapEffect = new XapEffect(m_pEB);
			
			pXapEffect->m_name = name;
			pXapEffect->SetLoopNum(cycle?-1:1);
			return pXapEffect;
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
			
			pParticleEffect->m_handle = pParMgr->CreateParticleSystem(name, pParticleEffect->m_parmeter, false);
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
		else if ( 0 == name.find(L"geffect") )
		{
			GEffect* pGEffect = new GEffect(m_pEB);
			pGEffect->m_name = name;
			pGEffect->SetLoopNum(cycle?-1:1);
			return pGEffect;
		}
		else if ( 0 == name.find(L"/sound") )
		{
			AudioEffect* pAudioEffect = new AudioEffect(m_pEB);
			pAudioEffect->m_name = name;
			pAudioEffect->SetLoopNum(cycle?-1:1);
			return pAudioEffect;
		}
		else if ( 0 == name.find(L"spine") )
		{
			SpineEffect* pSpineEffect = new SpineEffect(m_pEB);
			pSpineEffect->m_name = name;
			pSpineEffect->SetLoopNum(cycle?-1:1);
			return pSpineEffect;
		}
		else
		{
			XPASSERT(false && L"effect name error");
			//这个名字有问题
			XPLOG_ERROR(L"effect name error: %s\n", name.c_str());
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
			if (removesound)
				pBegin->RemoveAudio();
			if (pBegin)
			{
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
			if (removesound)
				pMid->RemoveAudio();
			if (pMid)
			{
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
			if (removesound)
				pEnd->RemoveAudio();
			if (pEnd)
			{
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

	void EffectManager::OnTick()
	{
		std::list<IEffect*>::iterator it = m_willDelEffect.begin(), ie = m_willDelEffect.end();
		for (;it!=ie;++it)
		{
			delete *it;
		}
		m_willDelEffect.clear();
	}
};