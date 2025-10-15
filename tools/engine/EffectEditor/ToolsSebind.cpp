#include "stdafx.h"
#include "ToolsSebind.h"

Nuclear::EffectClip* CToolsSebind::AddClip(Nuclear::XPDIRECTION dir, const std::wstring& action, int frameCount)
{
	Nuclear::ACTION_CLIPS& actClips = m_directionClips[dir];
	Nuclear::EffectClip& ec = actClips[action];
	if (ec.GetEndFrame() != 0)
		return NULL;
	ec.m_endFrame = frameCount - 1;
	return &ec;

}

void CToolsSebind::ClearAll()
{
	Nuclear::ACTION_CLIPS::iterator iter;
	for (int i=0;i<Nuclear::XPDIR_COUNT;++i)
	{
		for (iter=m_directionClips[i].begin();iter!=m_directionClips[i].end();++iter)
		{
			iter->second.SetAnimation(NULL);
		}
		m_directionClips[i].clear();
	}
}

void CToolsSebind::Save( XMLIO::CONode& root, int flag)
{
	for ( Nuclear::XPDIRECTION dir = Nuclear::XPDIR_TOP; dir <= Nuclear::XPDIR_TOPLEFT; dir = static_cast<Nuclear::XPDIRECTION>(dir + 1))
	{
		Nuclear::ACTION_CLIPS& actClips = m_directionClips[dir];
		if ( actClips.empty())
			continue;

		XMLIO::CONode dirNode;
		root.CreateSubElement( L"direction", dirNode);
		dirNode.SetAttribute( L"value", (int)dir);

		for ( Nuclear::ACTION_CLIPS::iterator i = actClips.begin(), e = actClips.end(); i != e; ++i)
		{
			const std::wstring& actionName = i->first;
			Nuclear::EffectClip& ec = i->second;

			XMLIO::CONode node;
			dirNode.CreateSubElement( actionName.c_str(), node);
			Nuclear::Effect* pEffect = ec.GetAnimation();
			ec.SetAnimation(NULL);
			ec.Save(node, flag);
			ec.SetAnimation(pEffect);
		}
	}
}

bool CToolsSebind::Save(const wchar_t* lpszPathName, int flag)
{
	XMLIO::CFileWriter fw;
	XMLIO::CONode node;
	fw.GetRootNode(node);
	Save(node, flag);
	int retcode = fw.SaveNative(lpszPathName);
	return retcode == XMLIO::EC_SUCCESS;
}

void CToolsSebind::ReleaseAll()
{
	ClearAll();
	if (m_pEffect)
		delete m_pEffect;
	m_pEffect = NULL;
}

CToolsSebind::~CToolsSebind(void)
{
	ReleaseAll();
}

CToolsSebind::CToolsSebind(void) : m_pEffect(NULL)
{
}

