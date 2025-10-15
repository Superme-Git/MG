#ifndef __Nuclear_EFFECTMANAGER_H__
#define __Nuclear_EFFECTMANAGER_H__

namespace Nuclear
{
	class Effect;
	class ParticleEffect;
	class EngineBase;
	class Sprite;
	class SebindManager;
	class IEffect;

	class FontEffect;

	class EffectManager//好吧，这个Manager本来就在引擎内部的……
	{
		// 构造析构
	public:
		EffectManager(EngineBase *pEB);
		~EffectManager();

		bool PreInit();

		FontEffect* CreateFontEffect(TextBlockHandle textHandle, const sNuclearFontEffectParam& structFontParam);

		Effect* CreateEffect(std::wstring name, bool cycle, bool bPreLoad = false);//这里的cycle，如果是粒子，那么是XPPCM_ALWAY_EMISSION类型的
		ParticleEffect* CreateLinkedEffect(std::wstring name, const NuclearPoint &pt1, const NuclearPoint &pt2, float time);
		Effect* CreateListEffect(const std::wstring& begin, const std::wstring& midcyc, const std::wstring& end, int durTime, bool cycle, bool async, bool removesound);
		SebindManager* GetSebindManager() { return m_pSebindManager; }
		void OnTick();
		void RemoveEffect(IEffect* pEffect);

		Effect* GetEffectTemplet(const std::wstring& effectname);
		bool GetEffectAni(IEffect* pEffect, bool bPerpetualMem = false);
		bool LoadEffectAni(std::wstring name, bool bPerpetualMem = false);

	public:
		struct SEffect
		{
			std::wstring m_name;
			std::wstring m_des;
		};
		typedef std::map<std::wstring, SEffect*> SEffectMap;

	private:
		EffectManager(const EffectManager&);
		EffectManager& operator=(const EffectManager&);

	private:

		EngineBase *m_pEB;
		SebindManager *m_pSebindManager;
		std::list<IEffect*> m_willDelEffect;

		//GEffect模板队列
		typedef std::map<std::wstring, Effect*> GEffectTempletMap;
		GEffectTempletMap m_GEffectTempletMap;
		SEffectMap m_SEffects;
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