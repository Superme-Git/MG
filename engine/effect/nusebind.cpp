
#include "../common/nulog.h"
#include "utils/Utils.h"
#include "../sprite/nusprite.h"
#include "nusebind.h"

#include "../../dependencies/LJXML//Include/LJXML.hpp"

namespace Nuclear 
{

	Sebind::Sebind(void)
	{
	}

	Sebind::~Sebind(void)
	{
	}

	EffectClip* Sebind::GetActionClip( NuclearDirection dir, const std::wstring& actionName)
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

	bool Sebind::Load(LJXML::LJXML_Node<LJXML::Char>* pRoot, int flag)
	{
		LJXML::LJXML_NodeList typenl;
		pRoot->GetSubNodeList(typenl);
		for (size_t i = 0; i < typenl.size(); i++)
		{
			if (typenl[i]->type() != rapidxml::node_element)
				continue;

			int directionValue = _wtoi(typenl[i]->first_attribute(L"value")->value());// node.GetAttributeInteger(L"value");
			if ( directionValue < 0 || directionValue >= XPDIR_COUNT)	// 错误的方向
			{
				XPLOG_ERROR(L"绑定文件方向错误 %d \n", directionValue);
				continue;
			}

			NuclearDirection dir = static_cast<NuclearDirection>(directionValue);
			ACTION_CLIPS& actClips = m_directionClips[dir];

			typedef std::pair<ACTION_CLIPS::iterator, bool> ret_t;

			// 遍历动作
			LJXML::LJXML_NodeList actnl;
			typenl[i]->GetSubNodeList(actnl);
			for (size_t j = 0; j < actnl.size(); j++)
			{
				if (actnl[j]->type() != rapidxml::node_element)
					continue;

				std::wstring actionName = actnl[j]->name();

				ret_t ret = actClips.insert(make_pair(actionName, EffectClip()));
				if ( !ret.second)
					continue;

				EffectClip& ac = ret.first->second;
				if (ac.Load(actnl[j], NULL, flag))
				{
					actClips.erase( ret.first);		// 失败,从map中移除
				}
			}
		}
		return true;
	}

	
	bool Sebind::Update(Sprite* pSprite, Effect* pEffect, DWORD tickTime)
	{
		XPASSERT(pSprite);
		XPASSERT(pEffect);
		const std::wstring &actname = pSprite->GetCurActionName();
		return Update(pSprite->GetDirection(), actname==L""?pSprite->GetDefaultAction():actname, static_cast<float>(pSprite->GetCurFrame()), 
			pSprite->GetLocation(), pEffect, tickTime);
	}


	NuclearHardRef<Sebind> SebindManager::GetSebind(std::wstring uri)
	{
		SebindMap::iterator iter = m_mapSebinds.find(uri);
		if (iter != m_mapSebinds.end())
		{
			return iter->second;
		}

		char* fileBuf = NULL;
		std::streamsize ss = 0;
		Sebind* pSebind = NULL;
		if (GetBuffFromFile(uri, fileBuf, ss) == 0)
		{
			XPLOG_ERROR(L"读取绑定文件 %s 错误\n", uri.c_str());
		}
		else
		{
			std::wstring fileData;
			LJXMLStringHelper::EncodeLJ codeType;
			if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, (size_t)ss, fileData, codeType))
			{
				delete[] fileBuf;
				XPLOG_ERROR(L"打开绑定文件 %s 错误\n", uri.c_str());
			}
			else
			{
				delete[] fileBuf;

				LJXML::LJXML_Doc<LJXML::Char> doc;
				LJXML::Char* charData = doc.LoadFromString(fileData);
				if (!doc.first_node())
				{
					XPLOG_ERROR(L"解释绑定文件 %s 错误\n", uri.c_str());
				}
				else
				{
					pSebind = new Sebind();
					LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
					if (!pSebind->Load(root))
					{
						delete pSebind;
						pSebind = NULL;
						XPLOG_ERROR(L"解释绑定文件内容 %s 错误\n", uri.c_str());
					}
				}
			}
		}

		NuclearHardRef<Sebind> result(pSebind);
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
