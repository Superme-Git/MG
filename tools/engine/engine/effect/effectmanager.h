#ifndef __Nuclear_EFFECTMANAGER_H
#define __Nuclear_EFFECTMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	class Effect;
	class ParticleEffect;
	class EngineBase;
	class Sprite;
	class SebindManager;
	class IEffect;
	class GEffectXMLCaches;
	class FontEffect;

	class EffectManager//�ðɣ����Manager�������������ڲ��ġ���
	{
		// ��������
	public:
		EffectManager(EngineBase *pEB);
		~EffectManager();

		bool PreInit();

		FontEffect* CreateFontEffect(TextBlockHandle textHandle, const XPFontEffectParam& structFontParam);

		Effect* CreateEffect(std::wstring name, bool cycle);//�����cycle����������ӣ���ô��XPPCM_ALWAY_EMISSION���͵�
		ParticleEffect* CreateLinkedEffect(std::wstring name, const CPOINT &pt1, const CPOINT &pt2, float time);
		Effect* CreateListEffect(const std::wstring& begin, const std::wstring& midcyc, const std::wstring& end, int durTime, bool cycle, bool async, bool removesound);
		SebindManager* GetSebindManager() { return m_pSebindManager; }
		GEffectXMLCaches* GetGEffectCache() { return m_pGEffectCaches; }
		void OnTick();
		void RemoveEffect(IEffect* pEffect) { m_willDelEffect.push_back(pEffect); }
		
	public:
		struct SEffect
		{
			std::wstring m_name;
			std::wstring m_des;
		};
		typedef std::map<std::wstring, SEffect*> SEffectMap;

	private:

		EngineBase *m_pEB;
		SebindManager *m_pSebindManager;
		GEffectXMLCaches *m_pGEffectCaches;
		std::list<IEffect*> m_willDelEffect;
		SEffectMap m_SEffects;

		// ռ��
	private:
		EffectManager(const EffectManager&);
		EffectManager& operator=(const EffectManager&);
	};
	class EffectGC
	{
	private:
		friend class EffectManager;
		static EffectManager* pEffectMan;
	protected:
		inline void RemoveEffect(IEffect* pEffect)
		{
			pEffectMan->RemoveEffect(pEffect);
		}
	};
};

#endif