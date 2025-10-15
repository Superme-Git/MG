#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\fileiomanager.h"
#include "..\sprite\sprite.h"
#include "sebind.h"

namespace Nuclear 
{

	Sebind::Sebind(void)
	{
	}

	Sebind::~Sebind(void)
	{
	}

	

	EffectClip* Sebind::GetActionClip( XPDIRECTION dir, const std::wstring& actionName)
	{
		ACTION_CLIPS& actClips = m_directionClips[dir];

		ACTION_CLIPS::iterator it = actClips.find( actionName);
		if ( it != actClips.end())
		{
			EffectClip& ac = it->second;
			return &ac;
		}
		return NULL;
	}

	bool Sebind::Load( XMLIO::CINode& root, int flag)
	{
		XMLIO::CNodeList clipsNodeList;
		root.GetChildren( clipsNodeList);
		for ( XMLIO::CNodeList::iterator i = clipsNodeList.begin(), e = clipsNodeList.end(); i != e; ++i)
		{
			XMLIO::CINode& node = *i;
			if (node.GetType() != XMLIO::NT_ELEMENT)
				continue;

			int directionValue = node.GetAttributeInteger( L"value");
			if ( directionValue < 0 || directionValue >= XPDIR_COUNT)	// 错误的方向
			{
				XPLOG_ERROR(L"绑定文件方向错误 %d \n", directionValue);
				continue;
			}

			XPDIRECTION dir = static_cast<XPDIRECTION>(directionValue);
			ACTION_CLIPS& actClips = m_directionClips[dir];

			typedef std::pair<ACTION_CLIPS::iterator, bool> ret_t;

			// 遍历动作
			XMLIO::CNodeList actionNodeList;
			node.GetChildren( actionNodeList);
			for ( XMLIO::CNodeList::iterator j = actionNodeList.begin(), k = actionNodeList.end(); j != k; ++j)
			{
				XMLIO::CINode& actionNode = *j;
				if (node.GetType() != XMLIO::NT_ELEMENT)
					continue;
				std::wstring actionName = actionNode.GetName();

				ret_t ret = actClips.insert( make_pair( actionName, EffectClip()));
				if ( !ret.second)
					continue;

				EffectClip& ac = ret.first->second;
				if ( ac.Load( actionNode, NULL, flag))
				{
					actClips.erase( ret.first);		// 失败, 从map中移除
				}

			}
		}
		return true;
	}

	
	bool Sebind::Tick(Sprite* pSprite, Effect* pEffect, DWORD tickTime)
	{
		XPASSERT(pSprite);
		XPASSERT(pEffect);
		const std::wstring &actname = pSprite->GetCurActionName();
		return Tick(pSprite->GetDirection(), actname==L""?pSprite->GetDefaultAction():actname, static_cast<float>(pSprite->GetCurFrame()), 
			pSprite->GetLocation(), pEffect, tickTime);
	}


	XHardRef<Sebind> SebindManager::GetSebind(std::wstring uri)
	{
		SebindMap::iterator iter = m_mapSebinds.find(uri);
		if (iter != m_mapSebinds.end())
		{
			return iter->second;
		}

		XBuffer xmlfiledata;
		Sebind* pSebind = NULL;
		if( !m_pEB->GetFileIOManager()->GetFileImage(uri, xmlfiledata))
		{
			XPLOG_ERROR(L"读取绑定文件 %s 错误\n", uri.c_str());
		} else {
			XMLIO::CFileReader fr;
			int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
			if( XMLIO::EC_SUCCESS != nError)
			{
				XPLOG_ERROR(L"打开绑定文件 %s 错误\n", uri.c_str());
			} else {
				XMLIO::CINode root;
				if (!fr.GetRootNode(root))
				{
					XPLOG_ERROR(L"解释绑定文件 %s 错误\n", uri.c_str());
				} else {
					pSebind = new Sebind();
					if (!pSebind->Load(root))
					{
						delete pSebind;
						pSebind = NULL;
						XPLOG_ERROR(L"解释绑定文件内容 %s 错误\n", uri.c_str());
					}
				}
			}
			fr.CloseFile();
		}
		XHardRef<Sebind> result(pSebind);
		m_mapSebinds.insert(std::make_pair(uri, result));
		return result;
	}

	void SebindManager::ClearUnuseSebind()
	{
		SebindMap::iterator iter = m_mapSebinds.begin(), iterEnd = m_mapSebinds.end();
		for (;iter!=iterEnd;)
		{
			if (iter->second.GetHardRefCount() == 1)
			{
				m_mapSebinds.erase(iter++);
			} else {
				++iter;
			}
		}
	}
}
