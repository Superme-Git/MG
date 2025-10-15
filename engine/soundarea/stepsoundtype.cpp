#include "stepsoundtype.h"
#include "nuifileio.h"
#include "../../dependencies/LJXML/Include/LJXMLStringHelper.hpp"
#include "../../dependencies/LJXML/Include/LJXML.hpp"
#include "nulog.h"

namespace Nuclear
{
	StepSoundType::StepSoundType(void)
	{
	}

	StepSoundType::~StepSoundType(void)
	{
	}

	bool StepSoundType::LoadSoundType(INuclearFileIO *pFIO)
	{
		m_groundMap.clear();
		m_actorMap.clear();
		m_allSounds.clear();
		m_groundColor.clear();
		
		std::wstring xmlpath = L"/sound/step/type.xml";
		NuclearBuffer xmlfiledata;
		if( !pFIO->GetFileImage(xmlpath, xmlfiledata))
			return true;

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)xmlfiledata.constbegin(), xmlfiledata.size(), fileData, codeType))
		{
			return false;
		}

		LJXML::LJXML_Doc<LJXML::Char> doc;
		LJXML::Char* charData = doc.LoadFromString(fileData);
		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		if (!root)
		{
			return false;
		}

		LJXML::LJXML_NodeList nl;
		root->GetSubNodeList(nl);

		for (LJXML::LJXML_NodeList::iterator it = nl.begin(); it != nl.end(); ++it)
		{
			LJXML::LJXML_Node<LJXML::Char>* node = *it;
			if (node->GetType() != rapidxml::node_element)
				continue;
			if (node->GetName() == L"gtype")
			{
				LJXML::LJXML_NodeList nlist;
				node->GetSubNodeList(nlist);
				for( LJXML::LJXML_NodeList::iterator sit = nlist.begin(); sit != nlist.end(); ++ sit)
				{
					LJXML::LJXML_Node<LJXML::Char>* snode = *sit;
					if (snode->GetType() != rapidxml::node_element)
						continue;
					std::wstring des;
					
					if (!snode->GetAttribute(L"des", des))
						continue;
					int id = snode->GetAttributeInteger(L"id");
					m_groundMap[id] = des;
					m_groundColor[id] = snode->GetAttributeUnsignedInteger(L"color", 0xFFFFFFFF);
				}
			} else if (node->GetName() == L"atype")
			{
				LJXML::LJXML_NodeList nlist;
				node->GetChildren(nlist);
				for( LJXML::LJXML_NodeList::iterator sit = nlist.begin(); sit != nlist.end(); ++ sit)
				{
					LJXML::LJXML_Node<LJXML::Char>* snode = *sit;
					if (snode->GetType() != rapidxml::node_element)
						continue;
					std::wstring name, des;
					if (!snode->GetAttribute(L"name", name))
						continue;
					if (!snode->GetAttribute(L"des", des))
						continue;
					m_actorMap[name] = des;
					XPSSCountMap &cmap = m_allSounds[name];
					
					std::wstring sxmlpath = L"/sound/step/" + name + L"/sounds.xml";
					NuclearBuffer sxmlfiledata;
					if(!pFIO->GetFileImage(sxmlpath, sxmlfiledata))
						continue;
					
					std::wstring ws;
					if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)sxmlfiledata.constbegin(), sxmlfiledata.size(), ws, codeType))
						continue;

					LJXML::LJXML_Doc<> sdoc;
					sdoc.LoadFromString(ws);
					LJXML::LJXML_Node<>* sroot = (LJXML::LJXML_Node<>*)sdoc.first_node();
					if (!sroot)
					{
						continue;
					}

					LJXML::LJXML_NodeList snl;
					sroot->GetChildren(snl);
					for( LJXML::LJXML_NodeList::iterator ssit = snl.begin(); ssit != snl.end(); ++ ssit)
					{
						LJXML::LJXML_Node<LJXML::Char>* ssnode = *ssit;
						if (ssnode->GetType() != rapidxml::node_element)
							continue;
						cmap[ssnode->GetAttributeInteger(L"id")] = ssnode->GetAttributeInteger(L"count");
					}
				}
			} else {
				XPASSERT(L"stepSoundType node name error!" && false);
			}
		}
		return true;
	}
	bool StepSoundType::LoadModelMap(INuclearFileIO *pFIO)
	{
		m_modelMap.clear();
		
		std::wstring xmlpath = L"/sound/step/sprite.xml";
		NuclearBuffer xmlfiledata;
		if( !pFIO->GetFileImage(xmlpath, xmlfiledata))
			return true;

		LJXML::LJXML_Doc<> doc;
		if (!doc.LoadFromBuffer(xmlfiledata.constbegin(), xmlfiledata.size()))
		{
			return true;
		}

		LJXML::LJXML_Node<>* root = doc.GetRootNode();
		if (!root)
		{
			return true;
		}

		LJXML::LJXML_NodeList nl;
		root->GetChildren(nl);
		for( LJXML::LJXML_NodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			LJXML::LJXML_Node<LJXML::Char>* node = *it;
			if (node->GetType() != rapidxml::node_element)
				continue;
			if (node->GetName() == L"model")
			{
				LJXML::LJXML_NodeList nlist;
				node->GetChildren(nlist);
				std::wstring modelname;
				if (!node->GetAttribute(L"name", modelname))
					continue;
				SSModelType &modelType = m_modelMap[modelname];
				for( LJXML::LJXML_NodeList::iterator sit = nlist.begin(); sit != nlist.end(); ++ sit)
				{
					LJXML::LJXML_Node<LJXML::Char>* snode = *sit;
					if (snode->GetType() != rapidxml::node_element)
						continue;
					if (snode->GetName() == L"ride")
					{
						std::wstring name, type;
						snode->GetAttribute(L"name", name);
						snode->GetAttribute(L"type", type);
						modelType.ridetype[name] = type;
					} else if (snode->GetName() == L"notify")
					{
						StepNotifieV notify;
						snode->GetAttribute(L"act", notify.actname);
						notify.frame = snode->GetAttributeInteger(L"frame");
						modelType.notifies.push_back(notify);
					} else if (snode->GetName() == L"default")
					{
						snode->GetAttribute(L"type", modelType.defaultType);
					} else {
						XPASSERT(L"stepSound ModelMap SNODE name error!" && false);
					}
				}
			} else {
				XPASSERT(L"stepSound ModelMap node name error!" && false);
			}
		}
		return true;
	}

	bool StepSoundType::SaveSoundType(const std::wstring &soundDir)
	{
		return true;
	}

	bool StepSoundType::SaveModelMap(const std::wstring &soundDir)
	{
		return true;
	}

	bool StepSoundType::Init(INuclearFileIO *pFIO)
	{
		bool bST = LoadSoundType(pFIO);
		bool bMM = LoadModelMap(pFIO);
		return bST && bMM;
	}

	bool StepSoundType::Save(const std::wstring &soundDir)
	{
		bool bST = SaveSoundType(soundDir);
		bool bMM = SaveModelMap(soundDir);
		return bST && bMM;
	}

	//获得当角色类型是actname，地表类型是groundType的声音的数量，如果返回值是false，代表是用默认声音
	bool StepSoundType::GetSoundCount(const std::wstring &actname, int groundType, int &count) const
	{
		XPSSAllCountMap::const_iterator ait = m_allSounds.find(actname);
		if (ait == m_allSounds.end())
		{
			count = 0;
			return false;
		}
		const XPSSCountMap &cmap = ait->second;
		XPSSCountMap::const_iterator sit = cmap.find(groundType);
		if (sit == cmap.end() || sit->second == 0)
		{
			XPSSCountMap::const_iterator ssit = cmap.find(0);
			if (ssit == cmap.end())
				count = 0;
			else
				count = ssit->second;
			return false;
		}
		count = sit->second;
		return true;
	}

	bool StepSoundType::SetModelDefaultType(const std::wstring &modelname, const std::wstring &actorname)
	{
		if (actorname == L"")
		{
			m_modelMap.erase(modelname);
			return true;
		}
		if (m_allSounds.find(actorname) == m_allSounds.end())
			return false;
		m_modelMap[modelname].defaultType = actorname;
		return true;
	}

	bool StepSoundType::SetModelRideType(const std::wstring &modelname, const std::wstring &ridename, const std::wstring &actorname)
	{
		if (actorname == L"")
		{
			if (m_modelMap.find(modelname) != m_modelMap.end())
			{
				m_modelMap[modelname].ridetype.erase(ridename);
			}
			return true;
		}
		if (m_allSounds.find(actorname) == m_allSounds.end())
			return false;
		m_modelMap[modelname].ridetype[ridename] = actorname;
		return true;
	}

	bool StepSoundType::InsertModelStepNotify(const std::wstring &modelname, const StepNotifieV &notify)
	{
		if (m_modelMap.find(modelname) != m_modelMap.end())
		{
			m_modelMap[modelname].notifies.push_back(notify);
			return true;
		}
		return false;
	}
	bool StepSoundType::RemoveModelStepNotify(const std::wstring &modelname, size_t id)
	{
		if (m_modelMap.find(modelname) != m_modelMap.end())
		{
			XPStepNotifies &notifies = m_modelMap[modelname].notifies;
			if (notifies.size() <= id)
				return false;
			notifies.erase(notifies.begin() + id);
			return true;
		}
		return false;
	}

	bool StepSoundType::ModifyModelStepNotify(const std::wstring &modelname, size_t id, const StepNotifieV &notify)
	{
		if (m_modelMap.find(modelname) != m_modelMap.end())
		{
			XPStepNotifies &notifies = m_modelMap[modelname].notifies;
			if (notifies.size() <= id)
				return false;
			*(notifies.begin() + id) = notify;
			return true;
		}
		return false;
	}
}