#ifndef __Nuclear_SEBIND_H__
#define __Nuclear_SEBIND_H__

#include "../common/nuxref.h"
#include "../common/nulog.h"
#include "nueffectclip.h"

namespace Nuclear
{
	typedef std::map<std::wstring, EffectClip> ACTION_CLIPS;
	class SebindManager;
	class EngineBase;
	class Sprite;

	class Sebind
	{
	protected:
		ACTION_CLIPS m_directionClips[XPDIR_COUNT];
		friend class SebindManager;

	private:
		//不允许复制
		Sebind( const Sebind& );
		const Sebind& operator=( const Sebind& src);

	protected:
		bool Load(LJXML::LJXML_Node<LJXML::Char>* pRoot, int flag = 0);
		bool Update(NuclearDirection dir, const std::wstring& actionName, float frame, const NuclearFPoint &pos, Effect* pEffect, DWORD tickTime)
		{
			ACTION_CLIPS& actClips = GetActionClipList(dir);
			ACTION_CLIPS::iterator it = actClips.find( actionName);
			if ( it == actClips.end())
				return false;
			EffectClip& clip = it->second;
			if (pEffect->GetType() == XPET_GEFFECT)
			{
				dynamic_cast<GEffect*>(pEffect)->SetDivideLayer(0);
			}
			clip.SetAnimation(pEffect);
			NuclearFPoint scale(1.0f, 1.0f);
			clip.ResetAni();
			clip.Update(frame, tickTime, pos, scale, 0.0f, NuclearVector4(1.0f, 1.0f, 1.0f,1.0f), dir);
			clip.SetAnimation( NULL);
			return true;
		}
		
	public:
		Sebind(void);
		virtual ~Sebind(void);

		ACTION_CLIPS& GetActionClipList(NuclearDirection dir) { return m_directionClips[dir]; }
		EffectClip* GetActionClip( NuclearDirection dir, const std::wstring& actionName);
		bool Update(Sprite* pSprite, Effect* pEffect, DWORD tickTime);
	};
	

	class SebindManager
	{
	private:
		typedef std::map<std::wstring, NuclearHardRef<Sebind> > SebindMap;

	private:
		SebindMap m_mapSebinds;
		EngineBase *m_pEB;

	private:
		//不允许复制
		SebindManager( const SebindManager& );
		const SebindManager& operator=( const SebindManager& src);

	public:
		SebindManager(EngineBase *pEB) : m_pEB(pEB) { m_mapSebinds.clear(); }
		~SebindManager() {}

		NuclearHardRef<Sebind> GetSebind(std::wstring uri);
		void ClearUnuseSebind();
	};


}

#endif