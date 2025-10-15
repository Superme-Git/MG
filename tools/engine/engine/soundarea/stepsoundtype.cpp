#include "stdafx.h"
#include "..\common\fileiomanager.h"
#include "..\common\log.h"
#include "..\common\xml.h"
#include "stepsoundtype.h"

namespace Nuclear
{
	StepSoundType::StepSoundType(void)
	{
	}

	StepSoundType::~StepSoundType(void)
	{
	}

	bool StepSoundType::LoadSoundType(IFileIO *pFIO)
	{
		m_groundMap.clear();
		m_actorMap.clear();
		m_allSounds.clear();
		m_groundColor.clear();
		XMLIO::CFileReader fr;
		std::wstring xmlpath = L"/sound/step/type.xml";
		XBuffer xmlfiledata;
		if( !pFIO->GetFileImage(xmlpath, xmlfiledata))
			return true;

		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
			return false;

		XMLIO::CINode root;
		if( !fr.GetRootNode(root) )
		{
			fr.CloseFile();
			return false;
		}
		XMLIO::CNodeList nl;
		root.GetChildren(nl);
		for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if (node.GetType() != XMLIO::NT_ELEMENT)
				continue;
			if (node.GetName() == L"gtype")
			{
				XMLIO::CNodeList nlist;
				node.GetChildren(nlist);
				for( XMLIO::CNodeList::iterator sit = nlist.begin(); sit != nlist.end(); ++ sit)
				{
					XMLIO::CINode snode = *sit;
					if (snode.GetType() != XMLIO::NT_ELEMENT)
						continue;
					std::wstring des;
					if (!snode.GetAttribute(L"des", des))
						continue;
					int id = snode.GetAttributeInteger(L"id");
					m_groundMap[id] = des;
					m_groundColor[id] = snode.GetAttributeUnsignedInteger(L"color", 0xFFFFFFFF);;
				}
			} else if (node.GetName() == L"atype")
			{
				XMLIO::CNodeList nlist;
				node.GetChildren(nlist);
				for( XMLIO::CNodeList::iterator sit = nlist.begin(); sit != nlist.end(); ++ sit)
				{
					XMLIO::CINode snode = *sit;
					if (snode.GetType() != XMLIO::NT_ELEMENT)
						continue;
					std::wstring name, des;
					if (!snode.GetAttribute(L"name", name))
						continue;
					if (!snode.GetAttribute(L"des", des))
						continue;
					m_actorMap[name] = des;
					XPSSCountMap &cmap = m_allSounds[name];
					XMLIO::CFileReader sfr;
					std::wstring sxmlpath = L"/sound/step/" + name + L"/sounds.xml";
					XBuffer sxmlfiledata;
					if(!pFIO->GetFileImage(sxmlpath, sxmlfiledata))
						continue;
					int nsError = sfr.OpenData(sxmlfiledata.constbegin(), sxmlfiledata.size());
					if( XMLIO::EC_SUCCESS != nsError)
						continue;
					XMLIO::CINode sroot;
					if( !sfr.GetRootNode(sroot) )
					{
						sfr.CloseFile();
						continue;
					}
					XMLIO::CNodeList snl;
					sroot.GetChildren(snl);
					for( XMLIO::CNodeList::iterator ssit = snl.begin(); ssit != snl.end(); ++ ssit)
					{
						XMLIO::CINode& ssnode = *ssit;
						if (ssnode.GetType() != XMLIO::NT_ELEMENT)
							continue;
						cmap[ssnode.GetAttributeInteger(L"id")] = ssnode.GetAttributeInteger(L"count");
					}
				}
			} else {
				XPASSERT(L"stepSoundType node name error!" && false);
			}
		}
		return true;
	}
	bool StepSoundType::LoadModelMap(IFileIO *pFIO)
	{
		m_modelMap.clear();
		XMLIO::CFileReader fr;
		std::wstring xmlpath = L"/sound/step/sprite.xml";
		XBuffer xmlfiledata;
		if( !pFIO->GetFileImage(xmlpath, xmlfiledata))
			return true;

		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
			return false;

		XMLIO::CINode root;
		if( !fr.GetRootNode(root) )
		{
			fr.CloseFile();
			return false;
		}
		XMLIO::CNodeList nl;
		root.GetChildren(nl);
		for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if (node.GetType() != XMLIO::NT_ELEMENT)
				continue;
			if (node.GetName() == L"model")
			{
				XMLIO::CNodeList nlist;
				node.GetChildren(nlist);
				std::wstring modelname;
				if (!node.GetAttribute(L"name", modelname))
					continue;
				SSModelType &modelType = m_modelMap[modelname];
				for( XMLIO::CNodeList::iterator sit = nlist.begin(); sit != nlist.end(); ++ sit)
				{
					XMLIO::CINode snode = *sit;
					if (snode.GetType() != XMLIO::NT_ELEMENT)
						continue;
					if (snode.GetName() == L"ride")
					{
						std::wstring name, type;
						snode.GetAttribute(L"name", name);
						snode.GetAttribute(L"type", type);
						modelType.ridetype[name] = type;
					} else if (snode.GetName() == L"notify")
					{
						StepNotifieV notify;
						snode.GetAttribute(L"act", notify.actname);
						notify.frame = snode.GetAttributeInteger(L"frame");
						modelType.notifies.push_back(notify);
					} else if (snode.GetName() == L"default")
					{
						snode.GetAttribute(L"type", modelType.defaultType);
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
		XMLIO::CFileWriter fw;
		XMLIO::CONode root, gtype, atype;
		fw.GetRootNode(root);
		XPSSGTypeMap::iterator gIt = m_groundMap.begin(), gIe = m_groundMap.end();
		root.CreateSubElement(L"gtype", gtype);
		for (;gIt!=gIe;++gIt)
		{
			XMLIO::CONode node;
			gtype.CreateSubElement(L"type", node);
			node.SetAttribute(L"id", gIt->first);
			node.SetAttribute(L"des", gIt->second);
			node.SetAttribute(L"color", static_cast<unsigned int>(m_groundColor[gIt->first].data));
		}

		XPSSATypeMap::iterator aIt = m_actorMap.begin(), aIe = m_actorMap.end();
		root.CreateSubElement(L"atype", atype);
		for (;aIt!=aIe;++aIt)
		{
			XMLIO::CONode node;
			atype.CreateSubElement(L"type", node);
			node.SetAttribute(L"name", aIt->first);
			node.SetAttribute(L"des", aIt->second);
			XPSSCountMap &cmap = m_allSounds[aIt->first];
			XMLIO::CFileWriter sfw;
			XMLIO::CONode sroot;
			sfw.GetRootNode(sroot);
			XPSSCountMap::iterator sIt = cmap.begin(), sIe = cmap.end();
			for (;sIt!=sIe;++sIt)
			{
				XMLIO::CONode node;
				sroot.CreateSubElement(L"type", node);
				node.SetAttribute(L"id", sIt->first);
				node.SetAttribute(L"count", sIt->second);
			}
			sfw.SaveNative(soundDir + L"step\\" + aIt->first + L"\\sounds.xml");
		}
		fw.SaveNative( soundDir + L"step\\type.xml");
		return true;
	}

	bool StepSoundType::SaveModelMap(const std::wstring &soundDir)
	{
		XMLIO::CFileWriter fw;
		XMLIO::CONode root;
		fw.GetRootNode(root);
		XPSSModelMap::iterator mIt = m_modelMap.begin(), mIe = m_modelMap.end();
		for (;mIt!=mIe;++mIt)
		{
			XMLIO::CONode model;
			root.CreateSubElement(L"model", model);
			model.SetAttribute(L"name", mIt->first);
			XMLIO::CONode defaultType;
			model.CreateSubElement(L"default", defaultType);
			defaultType.SetAttribute(L"type", mIt->second.defaultType);
			XPRideType::iterator rIt = mIt->second.ridetype.begin(), rIe = mIt->second.ridetype.end();
			for (;rIt!=rIe;++rIt)
			{
				XMLIO::CONode ride;
				model.CreateSubElement(L"ride", ride);
				ride.SetAttribute(L"name", rIt->first);
				ride.SetAttribute(L"type", rIt->second);
			}
			XPStepNotifies::iterator nIt = mIt->second.notifies.begin(), nIe = mIt->second.notifies.end();
			for (;nIt!=nIe;++nIt)
			{
				XMLIO::CONode notify;
				model.CreateSubElement(L"notify", notify);
				notify.SetAttribute(L"act", nIt->actname);
				notify.SetAttribute(L"frame", nIt->frame);
			}
		}
		fw.SaveNative( soundDir + L"step\\sprite.xml");
		return true;
	}

	bool StepSoundType::Init(IFileIO *pFIO)
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