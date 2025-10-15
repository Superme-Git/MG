#include "../common/nulog.h"
#include "../common/nuljfmutil.h"
#include "../common/nuxml.h"
#include "../engine/nuenginebase.h"
#include "nuspritemanager.h"
#include "nuport.h"
#include <algorithm>

#include "utils/Utils.h"

#include <platform/platform_types.h>

#include "../../dependencies/LJXML//Include/LJXML.hpp"
#include "../../dependencies/LJXML/Include/LJXMLStringHelper.hpp"

namespace Nuclear
{
	enum XPLayer_Type
	{
		XPLT_RIDE = 1,			//01
		XPLT_EFFECT = 2,        //10
	};

	void Action::Frame::SetOrder(int dir, const int *ec, int size)
	{
		int64 r = 0;
		for (int i = 0; i < size; ++i)
			r |= ((int64)ec[i] << (i * 4));
		m_equipOrder[dir] = r;
	}

	void Action::SetFrameOrder(int seq, int dir, const int *ec, int size)
	{
		m_frames[seq].SetOrder(dir, ec, size);
	}

	void Action::Frame::GetOrder(int dir, int* ec, int size) const
	{
		int64 r = m_equipOrder[dir];
		for (int i = 0; i < size; ++i)
			ec[i] = (r >> (i * 4)) & 0xf;
	}

	void Action::GetFrameOrder(int seq, int dir, int* ec, int size) const
	{
		m_frames[seq].GetOrder(dir, ec, size);
	}

	void Action::SetFrameLink(int seq, const std::wstring &n, int f)
	{
		if (m_linkType == AL_NULL) return;
		if (m_links.size() != m_frames.size())
			m_links.resize(m_frames.size());
		m_links[seq].name = n;
		m_links[seq].frame = f;
	}

	void Action::SetFrameCount(int n)
	{
		if (m_frames.empty() || n <= (int)m_frames.size())
			m_frames.resize(n);
		else
			m_frames.resize(n, m_frames[m_frames.size() - 1]);
	}

	//
	NuclearOStream& Action::marshal(NuclearOStream &os) const
	{
		// 版本变更说明：
		// 4->5 变更字节序为 le
		// 5->6 增加链接动作
		// 6->7 8个方向每个方向的框
		// 7->8 没有base
		// 8->9 m_equipOrder 从 32 位整数变为 64 位整数

		os << (int)9;
		os << m_nTime;
		os << m_damagepoint;
		os << m_effectpoint;
		os << m_maskDir;
		os << m_maskEquip;
		os << m_stride;

		int n = (int)m_frames.size();
		os << n;
		for (int i = 0; i < n; ++i)
		{
			const Frame &f = m_frames[i];
			os << f.m_time;
			os << f.m_startTime;
			for (int j = 0; j < XPDIR_COUNT; ++j)
				os << f.m_equipOrder[j];
		}

		os << m_linkType;
		if (m_linkType)
		{
			for (int i = 0; i < n; ++i)
			{
				const Link &f = m_links[i];
				os << f.name;
				os << f.frame;
			}
		}
		for (int i = 0; i < 8; ++i)
		{
			os << m_rects[i];
		}
		return os;
	}

	const INuclearStream& Action::unmarshal(const INuclearStream &os)
	{
		int dummy;
		os >> dummy;

		int version = dummy;

		os >> m_nTime;
		os >> m_damagepoint;
		os >> m_effectpoint;
		os >> m_maskDir;
		os >> m_maskEquip;

		if (version <= 7)
		{
			NuclearPoint unuse;
			os >> unuse;
			os >> unuse;
			os >> unuse;
			os >> unuse;
			os >> unuse;
		}
		os >> m_stride;
		int n;
		os >> n;
		for (int i = 0; i < n; ++i)
		{
			Frame f;
			os >> f.m_time;
			os >> f.m_startTime;
			for (int j = 0; j < XPDIR_COUNT; ++j)
			{
				if (version <= 8)
				{
					int i32 = 0;
					os >> i32;
					f.m_equipOrder[j] = i32;
				}
				else
				{
					os >> f.m_equipOrder[j];
				}
			}
			m_frames.push_back(f);
		}

		if (version > 5)
		{
			os >> m_linkType;

			if (m_linkType)
			{
				m_links.resize(n);
				for (int i = 0; i < n; ++i)
				{
					os >> m_links[i].name;
					os >> m_links[i].frame;
				}
			}
		}
		else
		{
			m_linkType = AL_NULL;
		}
		if (version >= 7)
		{
			for (int i = 0; i < 8; ++i)
			{
				os >> m_rects[i];
				NormalizeRect(m_rects[i]);
			}
		}

		return os;
	}

	bool SpriteManager::XModel::AddAction(const std::wstring &name, const Action &act) // 编辑器使用
	{
		if (m_actions.find(name) != m_actions.end())
			return false;
		m_actions[name] = new Action(act);
		return true;
	}

	bool SpriteManager::XModel::DeleteAction(const std::wstring &name) // 编辑器使用
	{
		assert(false);
		return true;
	}

	bool SpriteManager::XModel::RenameAction(const std::wstring &oldname, const std::wstring &newname) // 编辑器使用
	{
		return true;
	}

	bool SpriteManager::XModel::LoadActions()
	{
		std::wstring xmlpath = L"/model/" + m_name + L"/action/action.lmx";
		char* fileBuf = NULL;
		std::streamsize ss = 0;
		if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
		{
			return false;
		}

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
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

		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		LJXML::LJXML_NodeList nl;
		root->GetSubNodeList(nl);
		for (int i = 0; i < nl.size(); i++)
		{
			if (std::wstring(nl[i]->name()).compare(L"action") != 0)
				continue;

			std::wstring str = nl[i]->first_attribute(L"name")->value();
			if (str.empty())
				continue;

			if (nl[i]->first_attribute(L"ref") && !(std::wstring(nl[i]->first_attribute(L"ref")->value()).empty()))
			{
				std::wstring ref = nl[i]->first_attribute(L"ref")->value();
				m_actionrefs[str] = ref;
			}
			else
			{
				if (m_actions.find(str) != m_actions.end()) continue;

				std::wstring fpath = L"/model/" + m_name + L"/action/" + str + L".act";
				NuclearBuffer actfiledata;
				if (m_pEB->GetFileIOManager()->GetFileImage(fpath, actfiledata))
				{
					Action act;
					if (act.LoadFromMem(actfiledata))
					{
						m_actions[str] = new Action(act);
					}
				}
			}
		}

		return true;
	}

	bool SpriteManager::XModel::SaveActions() const // 编辑器使用，只保存xml
	{
		assert(false);
		return false;
	}

	void SpriteManager::XModel::DumpAction(std::vector<std::wstring> &actlist) const // 编辑器使用
	{
		for (std::map<std::wstring, Action*>::const_iterator it = m_actions.begin(), ie = m_actions.end(); it != ie; ++it)
			actlist.push_back(it->first);
	}

	void SpriteManager::XModel::DumpActionRefMap(std::map<std::wstring, std::wstring> &actrefmap) const // 编辑器使用
	{
		actrefmap = m_actionrefs;
	}

	void SpriteManager::XModel::SetActionRefMap(const std::map<std::wstring, std::wstring> &actrefmap) // 编辑器使用
	{
		m_actionrefs = actrefmap;
		SaveActions();
	}

	void SpriteManager::XModel::DumpBaseAction(std::vector<std::pair<std::wstring, int> > &baseactlist) const // 编辑器使用
	{
		for (std::map<std::wstring, Action*>::const_iterator it = m_actions.begin(), ie = m_actions.end(); it != ie; ++it)
		{
			const Action *pAct = it->second;
			if (pAct->IsInd()) continue;
			baseactlist.push_back(std::pair<std::wstring, int>(it->first, pAct->GetFrameCount()));
		}
	}

	const Action* SpriteManager::XModel::GetAction(const std::wstring &actName) const
	{
		std::map<std::wstring, Action*>::const_iterator it = m_actions.find(actName);
		if (it == m_actions.end()) return NULL;
		return it->second;
	}

	const std::wstring& SpriteManager::XModel::GetActionRef(const std::wstring &actname) const
	{
		std::map<std::wstring, std::wstring>::const_iterator it = m_actionrefs.find(actname);
		if (it == m_actionrefs.end()) return actname;
		return it->second;
	}

	void SpriteManager::XModel::SetAction(const std::wstring &actName, const Action& act)
	{
		std::map<std::wstring, Action*>::const_iterator it = m_actions.find(actName);
		if (it == m_actions.end()) return;
		*(it->second) = act;
	}

	bool SpriteManager::XModel::SaveLayerConfig()
	{
		assert(false);
		return true;
	}

	bool SpriteManager::XModel::LoadLayerConfig()
	{
		std::wstring xmlpath = L"/model/" + m_name + L"/layerdef.lmx";
		char* fileBuf = NULL;
		std::streamsize ss = 0;
		if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
		{
			return false;
		}

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
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

		std::map<int, Layer> tmap;
		int maxid = 0;

		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		LJXML::LJXML_NodeList nl;
		root->GetSubNodeList(nl);
		for (int i = 0; i < nl.size(); i++)
		{
			if (std::wstring(nl[i]->name()).compare(L"layer") != 0)
			{
				if (std::wstring(nl[i]->name()).compare(L"dye") == 0)
				{
					LJXML::LJXML_Attr<LJXML::Char>* idtype = (LJXML::LJXML_Attr<LJXML::Char>*)nl[i]->first_attribute(L"type");
					if (idtype != NULL)
						m_bDyeEnable = _wtoi(idtype->value());
					if (m_bDyeEnable == 0) m_bDyeEnable = 1;			//兼容之前的版本
				}
				continue;
			}
			std::wstring idstr = nl[i]->first_attribute(L"id")->value();
			std::wstring namestr = nl[i]->first_attribute(L"name")->value();
			std::wstring desstr = nl[i]->first_attribute(L"des")->value();
			std::wstring ridestr = nl[i]->first_attribute(L"ride") ? nl[i]->first_attribute(L"ride")->value() : L"false";

			//这里需要加上返回值判断么？要不要将这几个wstring的声明扔到for循环的外面呢？
			int type = _wtoi(nl[i]->first_attribute(L"type")->value());

			int id = _wtoi(idstr.c_str());
			if (id <= 0 || id > XPSC_MAXCOUNT) continue;
			if (id > maxid) maxid = id;
			tmap[id] = Layer(namestr, desstr, ((type & XPLT_RIDE) != 0) || ridestr == L"true", (type & XPLT_EFFECT) != 0);
		}

		if (maxid == 0) return false;
		if ((int)tmap.size() != maxid) return false;
		for (int i = 1; i <= maxid; ++i)
			m_layers.push_back(tmap[i]);

		m_Components.resize(maxid);
		if (m_bDyeEnable)
		{
			std::wstring fpath = L"/model/" + m_name + L"/dyeinfo.dye";
			NuclearBuffer actfiledata;
			if (m_pEB->GetFileIOManager()->GetFileImage(fpath, actfiledata))
			{
				if (actfiledata.size() >= 4)
				{
					INuclearStream os(actfiledata);

					char fileHead[4];
					os.pop_byte(fileHead, sizeof(char) * 4);
					if (fileHead[0] != 'D' || fileHead[1] != 'Y' || fileHead[2] != 'E' || fileHead[3] != 'P')
						return false;
					int version = 0;
					os >> version;
					int partCount = 0;
					os >> partCount;

					m_DyeInfo.resize(partCount);

					for (int i = 0; i < partCount; i++)
					{
						int projCount = 0;
						os >> projCount;
						m_DyeInfo[i].resize(projCount);
						for (int j = 0; j < projCount; j++)
						{
							DyeInfo outInfo;
							os.pop_byte(&outInfo, sizeof(DyeInfo));
							m_DyeInfo[i][j] = outInfo;
						}
					}
				}
			}
		}
		return true;
	}

	bool SpriteManager::SaveModels()
	{
		assert(false);
		return false;
	}

	bool SpriteManager::PreInit()
	{
		XPASSERT(!m_pInitingModelVet);
		m_pInitingModelVet = new XModelVet();
		if (!m_pInitingModelVet)
			return false;

		std::wstring xmlpath = L"/model/sprites.set";
		char* fileBuf = NULL;
		std::streamsize ss = 0;
		if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
		{
			return false;
		}

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
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
		for (int i = 0; i < nl.size(); i++)
		{
			if (std::wstring(nl[i]->name()).compare(L"model") != 0) continue;

			std::wstring namestr = nl[i]->first_attribute(L"name")->value();
			std::wstring desstr = nl[i]->first_attribute(L"des")->value();

			if (namestr.empty() || desstr.empty() || m_models.find(namestr) != m_models.end())
			{
				r = false;
				continue;
			}

			XModel *pM = new XModel(m_pEB);
			pM->m_name = namestr;
			pM->m_des = desstr;

			pM->m_nType = nl[i]->first_attribute(L"type") ? _wtoi(nl[i]->first_attribute(L"type")->value()) : 0;
			pM->m_nTitlePos = _wtoi(nl[i]->first_attribute(L"titlepos")->value());
			pM->m_effectPos.x = _wtol(nl[i]->first_attribute(L"efctPosX")->value());
			pM->m_effectPos.y = _wtol(nl[i]->first_attribute(L"efctPosY")->value());
			pM->m_surfaceType = static_cast<NuclearWaterSurfaceType>(_wtoi(nl[i]->first_attribute(L"surfacetype")->value()));
			pM->m_base.left.x = nl[i]->first_attribute(L"blx") ? _wtol(nl[i]->first_attribute(L"blx")->value()) : -10;
			pM->m_base.left.y = _wtol(nl[i]->first_attribute(L"bly")->value());
			pM->m_base.right.x = nl[i]->first_attribute(L"brx") ? _wtol(nl[i]->first_attribute(L"brx")->value()) : 10;
			pM->m_base.right.y = _wtol(nl[i]->first_attribute(L"bry")->value());
			pM->m_fScale = nl[i]->first_attribute(L"scale") ? _wtof(nl[i]->first_attribute(L"scale")->value()) : 1;

			m_pInitingModelVet->push_back(pM);
		}

		return r;
	}

	ULONG64 SpriteManager::Init(size_t id, size_t batch)
	{
		XPASSERT(batch <= 64);
		XPASSERT(m_pInitingModelVet);
		size_t size = m_pInitingModelVet->size();
		XPASSERT(id < size);
		ULONG64 result = 0;
		ULONG64 mask = 1;
		XModel **ppM = &((*m_pInitingModelVet)[id]);
		XModel *pM = NULL;
		size -= id;
		size = std::min<size_t>(size, batch);
		id = 0;
		for (; id < size; ++id, ++ppM)
		{
			pM = *ppM;
			if (pM->m_nType == 0)
			{
				bool bRet = pM->LoadActions() && pM->LoadBase() && pM->LoadSoundParam();
				if (bRet)
				{
					m_models[pM->m_name] = pM;
				}
				else
				{
					delete pM;
					result |= (mask << id);
				}
			}
			else
			{
				m_models[pM->m_name] = pM;
			}
		}
		return result;
	}

	bool SpriteManager::PostInit()
	{
		XPASSERT(m_pInitingModelVet);
		delete m_pInitingModelVet;
		m_pInitingModelVet = NULL;
		actionType.Init(m_pEB->GetFileIOManager());
		actionType.Load();
		return true;
	}

	bool SpriteManager::XModel::LoadSoundParam()
	{
		std::wstring tmpname;
		tmpname.resize(4);
		std::wstring xmlpath = L"/model/" + m_name + L"/" + L"sound.inf";
		if (!LJFM::LJFMOpen::IsFileExisting(xmlpath))
		{
			return true;
		}

		char* fileBuf = NULL;
		std::streamsize ss = 0;
		if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
		{
			return true;
		}

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
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

		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		m_soundParam.keyComponentID = _wtoi(root->first_attribute(L"key")->value());

		LJXML::LJXML_NodeList nl;
		root->GetSubNodeList(nl);
		for (int i = 0; i < nl.size(); i++)
		{
			if (nl[i]->type() != rapidxml::node_element)
				continue;

			std::wstring componentName = nl[i]->first_attribute(L"name")->value();
			if (componentName.empty())
				continue;

			PActionSoundParamMap &pSmap = m_soundParam.soundmap[componentName];

			if (pSmap == NULL)
			{
				pSmap = new ActionSoundParamMap();
			}

			LJXML::LJXML_NodeList actList;
			nl[i]->GetSubNodeList(actList);
			for (int j = 0; j < actList.size(); j++)
			{
				if (actList[j]->type() != rapidxml::node_element)
					continue;

				std::wstring actionName = actList[j]->first_attribute(L"an")->value();
				if (actionName.empty())
					continue;

				int count = _wtoi(actList[j]->first_attribute(L"count")->value());//anode.GetAttributeInteger(L"count");
				if (count == 0)
					continue;

				if (count > 10)
					count = 10;//不能超过10个字符

				SpriteSoundParamVet &vet = (*pSmap)[actionName];
				vet.resize(count);
				tmpname[2] = L'0';
				tmpname[3] = L'\0';
				for (int i = 0; i < count; ++i, ++(tmpname[2]))
				{
					tmpname[0] = L's';
					tmpname[1] = L'u';
					if (!actList[j]->first_attribute(tmpname.c_str()))
						continue;
					vet[i].soundname = actList[j]->first_attribute(tmpname.c_str())->value();
					tmpname[0] = L'v';
					tmpname[1] = L'o';
					std::string strvol = LJXMLStringHelper::ws2s(actList[j]->first_attribute(tmpname.c_str())->value());
					vet[i].vol = (unsigned char)strvol[0];
					tmpname[0] = L'f';
					tmpname[1] = L'r';
					std::string strfreq = LJXMLStringHelper::ws2s(actList[j]->first_attribute(tmpname.c_str())->value());;
					vet[i].freq = (unsigned char)strfreq[0];
				}
			}
		}

		return true;
	}

	bool SpriteManager::XModel::SaveSoundParam()
	{
		assert(false);
		return true;
	}

	bool SpriteManager::XModel::LoadBase()
	{
		if (!LoadLayerConfig())
		{
			return false;
		}

		for (int i = 0; i < GetLayerCount(); ++i)
		{
			//set horse resource path specially. by liugeng
			std::wstring xmlpath = L"";
			if (GetLayerRideEnable(i))
			{
				xmlpath = L"/model/mt_zuoqi/" + GetLayerName(i) + L"/" + GetLayerName(i) + L".lmx";
			}
			else
			{
				xmlpath = L"/model/" + m_name + L"/" + GetLayerName(i) + L"/" + GetLayerName(i) + L".lmx";
			}
			char* fileBuf = NULL;
			std::streamsize ss = 0;
			if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
			{
				return false;
			}

			std::wstring fileData;
			LJXMLStringHelper::EncodeLJ codeType;
			if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
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

			LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
			LJXML::LJXML_NodeList nl;
			root->GetSubNodeList(nl);
			for (int j = 0; j < nl.size(); j++)
			{
				if (GetLayerName(i) != nl[j]->name())
					continue;

				std::wstring name = nl[j]->first_attribute(L"name")->value();
				if (name.empty()) continue;

				if (m_Components[i].find(name) != m_Components[i].end())
					continue;

				std::wstring linkname = nl[j]->first_attribute(L"linkname") ? nl[j]->first_attribute(L"linkname")->value() : L"";

				m_Components[i][name] = new Component(m_pEB, i, name, linkname);
			}
		}

		return true;
	}

	bool SpriteManager::XModel::SaveBase()
	{
		assert(false);
		return true;
	}

	void SpriteManager::XModel::DeleteAction2(const std::wstring &name) // 编辑器使用
	{
		for (int i = 0; i < GetLayerCount(); ++i)
		{
			for (std::map<std::wstring, Component*>::iterator it = m_Components[i].begin(), ie = m_Components[i].end(); it != ie; ++it)
			{
				it->second->DeleteAction(m_name, name);
			}
		}
	}

	void SpriteManager::XModel::RenameAction2(const std::wstring &oldname, const std::wstring &newname, bool renamexap) // 编辑器使用
	{
		for (int i = 0; i < GetLayerCount(); ++i)
		{
			for (std::map<std::wstring, Component*>::iterator it = m_Components[i].begin(), ie = m_Components[i].end(); it != ie; ++it)
			{
				it->second->RenameAction(m_name, oldname, newname, renamexap);
			}
		}
	}

	bool SpriteManager::XModel::AddComponent(const Component &equip) // 编辑器使用
	{
		assert(false);
		return true;
	}

	void SpriteManager::XModel::RenameComponent(const std::wstring &oldname, const std::wstring &newname) // 编辑器使用
	{
	}


	bool SpriteManager::XModel::DeleteComponent(const Component &equip) // 编辑器使用
	{
		assert(false);
		return true;
	}

	Component* SpriteManager::XModel::GetComponent(int ectype, const std::wstring &name)
	{
		assert(ectype >= 0 && ectype < GetLayerCount());
		std::map<std::wstring, Component*>::iterator it = m_Components[ectype].find(name);
		if (it == m_Components[ectype].end()) return NULL;
		return it->second;
	}

	int SpriteManager::XModel::GetDyePartCount()
	{
		if (m_bDyeEnable == 0)	return 0;
		return m_DyeInfo.size();
	}
	int SpriteManager::XModel::GetDyeProjCount(int part)
	{
		if (m_bDyeEnable == 0)	return 0;
		if (part < 0 || part >= m_DyeInfo.size())	return 0;
		return m_DyeInfo[part].size();
	}
	bool SpriteManager::XModel::GetDyeParam(int part, int index, DyeInfo& outInfo)
	{
		if (m_bDyeEnable == 0)	return false;
		if (part < 0 || part >= m_DyeInfo.size())	return false;
		if (index < 0 || index >= m_DyeInfo[part].size())	return false;
		outInfo = m_DyeInfo[part][index];
		return true;
	}

	SpriteManager::SpriteManager(EngineBase *pEB) : m_pEB(pEB), m_pInitingModelVet(NULL)
	{}

	SpriteManager::~SpriteManager()
	{
		if (m_pInitingModelVet)
			delete m_pInitingModelVet;
		for (ModelMap::iterator it = m_models.begin(), ie = m_models.end(); it != ie; ++it)
			delete it->second;
		m_models.clear();
	}

	SpriteManager::XModel::~XModel()
	{
		for (int i = 0; i < GetLayerCount(); ++i)
		{
			for (std::map<std::wstring, Component*>::iterator it = m_Components[i].begin(), ie = m_Components[i].end(); it != ie; ++it)
			{
				if (it->second)
				{
					it->second->Release();
					delete it->second;
				}
			}
			m_Components[i].clear();
		}

		ComponentSoundMap::iterator cit = m_soundParam.soundmap.begin(),
			cie = m_soundParam.soundmap.end();
		for (; cit != cie; ++cit)
		{
			delete cit->second;
		}
		m_soundParam.soundmap.clear();


		for (std::map<std::wstring, Action*>::iterator it = m_actions.begin(), ie = m_actions.end(); it != ie; ++it)
			delete it->second;
		m_actions.clear();
	}


	void Component::Release()
	{
		for (AniMap::iterator it = m_anis.begin(), ie = m_anis.end(); it != ie; ++it)
		{
			it->second.Release();
		}
	}

	void Component::DeleteAction(const std::wstring &modelname, const std::wstring &name) // 编辑器使用
	{
	}

	void Component::RenameAction(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname, bool renamexap) // 编辑器使用
	{
	}

	bool SpriteManager::AddModel(const std::wstring &newmodelname, const std::wstring &newmodeldes, const std::vector<Layer> &layers)
	{
		if (layers.empty() || m_models.find(newmodelname) != m_models.end()) return false;
		XModel *pM = new XModel(m_pEB);
		pM->m_name = newmodelname;
		pM->m_des = newmodeldes;
		pM->SaveActions();

		pM->m_layers = layers;
		pM->m_Components.resize(layers.size());
		pM->SaveBase();
		m_models[newmodelname] = pM;
		return SaveModels();
	}

	void SpriteManager::RemoveALayer(int layerIDMap[], int layerID, const std::wstring &modelname, const std::wstring &layername)	// 编辑器使用
	{
		assert(false);
	}

	bool SpriteManager::ModifyModel(const std::wstring &modelname, const std::wstring &newmodeldes, const std::vector<Layer> &layers)	//编辑器使用
	{
		assert(false);
		return false;
	}

	bool SpriteManager::DelModel(const std::wstring &modelname)
	{
		assert(false);
		return false;
	}

	void SpriteManager::DumpModelNames(std::vector<std::wstring> &modellist) const
	{
		modellist.clear();
		for (ModelMap::const_iterator it = m_models.begin(), ie = m_models.end(); it != ie; ++it)
			modellist.push_back(it->first);
	}

	SpriteManager::XModel* SpriteManager::GetXModel(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return NULL;
		return it->second;
	}

	int SpriteManager::GetModelTypeByModelName(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return -1;
		return it->second->m_nType;
	}

	std::wstring SpriteManager::GetModelDes(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return L"";
		return it->second->m_des;
	}

	bool SpriteManager::XModel::GetLayerRideEnable(int i) const
	{
		if (i < 0 || i >= (int)m_layers.size())
			return false;
		return m_layers[i].bRide;
	}

	bool SpriteManager::XModel::GetLayerEffectEnable(int i) const
	{
		if (i < 0 || i >= (int)m_layers.size())
			return false;
		return m_layers[i].bEffect;
	}

	std::wstring SpriteManager::XModel::GetLayerName(int i) const
	{
		if (i < 0 || i >= (int)m_layers.size())
			return L"unknown";
		return m_layers[i].name;
	}

	std::wstring SpriteManager::XModel::GetLayerDes(int i) const
	{
		if (i < 0 || i >= (int)m_layers.size())
			return L"未知";
		return m_layers[i].des;
	}

	void SpriteManager::XModel::DumpComponent(int ectype, std::vector<std::wstring> &equiplist) const // 编辑器使用
	{
		assert(ectype >= 0 && ectype < GetLayerCount());
		for (std::map<std::wstring, Component*>::const_iterator it = m_Components[ectype].begin(), ie = m_Components[ectype].end();
			it != ie; ++it)
			equiplist.push_back(it->first);
	}

	Component::~Component()
	{
		LoadingUrls::iterator it = m_loadingUrls.begin(), ie = m_loadingUrls.end();
		AniManager *pAniMan = m_pEB->GetAniManager();
		for (; it != ie; ++it)
		{
			pAniMan->RemoveLoadingNotify(it->first, this);
		}
	}

	void Component::OnLoadAniReady(const std::wstring &filename)
	{
		LoadingUrls::iterator it = m_loadingUrls.find(filename);
		if (it != m_loadingUrls.end())
		{
			LoadingActions &acts = it->second;
			AniManager *pAniMan = m_pEB->GetAniManager();
			NuclearWeakRef<XAni> &weakani = m_anis[acts.first];
			NuclearHardRef<XAni> hardref = weakani;
			if (!hardref.IsValid())
			{
				weakani = pAniMan->GetAni(filename, true, m_bLookupSegmPak);
				hardref = weakani;
			}
			hardref.SetTick(m_pEB->GetTick());
			ActionNameSet::iterator aIt = acts.second.begin(), aIe = acts.second.end();
			ComponentAni::iterator cIt, cIe = m_holdAni.end();
			for (; aIt != aIe; ++aIt)
			{
				cIt = m_holdAni.find(*aIt);
				if (cIt == cIe)
					continue;
				m_holdAni[*aIt].second.push_back(hardref);
			}
			m_loadingUrls.erase(it);
		}
	}

	void Component::OnLoadAniBaseReady(const std::wstring &filename)
	{
		LoadingUrls::iterator it = m_loadingUrls.find(filename);
		if (it != m_loadingUrls.end())
		{
			LoadingActions &acts = it->second;
			AniManager *pAniMan = m_pEB->GetAniManager();
			NuclearWeakRef<XAni> &weakani = m_anis[acts.first];
			NuclearHardRef<XAni> hardref = weakani;
			if (!hardref.IsValid())
			{
				weakani = pAniMan->GetAni(filename, true, m_bLookupSegmPak);
			}
		}
	}

	// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
	NuclearPrefetchResult Component::PrefetchAction(const std::wstring &modelname, const std::wstring &action_name, const ActionNameSet &linked_action_names, unsigned char bDye)
	{
		NuclearPrefetchResult result = XPPR_OK;
		ActionNameSet::const_iterator it = linked_action_names.begin(), ie = linked_action_names.end();
		for (; it != ie; ++it)
		{
			NuclearWeakRef<XAni> &weakani = m_anis[*it];
			NuclearHardRef<XAni> hardref = weakani;
			if ((!hardref.IsValid()) || hardref->GetState() == XAni::XPANISTATE_NOPICTURE)
			{
				//set horse resource path specially. by liugeng
				std::wstring uri = L"";
				if (m_pEB->GetSpriteManager()->GetLayerRideEnable(modelname, m_layer))
				{
					if ((*it).find(L"riding_") != 0)
					{
						return result;
					}
					uri = L"/model/mt_zuoqi/" + m_pEB->GetSpriteManager()->GetLayerName(modelname, m_layer)
						+ L"/" + (m_linkname.empty() ? m_name : m_linkname) + L"/" + *it + SFI_ExtName;
					bDye = 0;
				}
				else
				{
					std::wstring layername = m_pEB->GetSpriteManager()->GetLayerName(modelname, m_layer);
					if (layername == L"weapon" && (*it).find(L"riding_") == 0)
						continue;

					uri = L"/model/" + modelname + L"/" + layername
						+ L"/" + (m_linkname.empty() ? m_name : m_linkname) + L"/" + *it + SFI_ExtName;
					if (bDye != 0 && layername != L"body")
						bDye = 0;
				}
				switch (m_pEB->GetAniManager()->AsyncLoadAni(uri, this, m_bLookupSegmPak, bDye))
				{
				case XPEARR_LOADING:
				{
					LoadingActions &acts = m_loadingUrls[uri];
					if (acts.first.size() == 0)
					{
						acts.first = *it;
					}
					acts.second.insert(action_name);
				}
				break;
				case XPEARR_RESREADY:
				case XPEARR_LOAD_ERROR:
					weakani = m_pEB->GetAniManager()->GetAni(uri, true, m_bLookupSegmPak, false, bDye);
					break;
				}
				result = XPPR_ASYNC_LOADING;
			}
			else {
				hardref.SetTick(m_pEB->GetTick());
				if (result < XPPR_BASE_ASYNC_OK && hardref->GetState() == XAni::XPANISTATE_BASE_PIC)
					result = XPPR_BASE_ASYNC_OK;
			}
		}
		return result;
	}

	// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
	void Component::HoldAction(const std::wstring &modelname, const std::wstring &action_name, const ActionNameSet &linked_action_names, unsigned char bDye)
	{
		AniInfo &info = m_holdAni[action_name];
		++info.first;
		if (PrefetchAction(modelname, action_name, linked_action_names, bDye) == XPPR_OK)
		{
			if (info.first == 1)//第一次Hold
			{
				ActionNameSet::const_iterator it = linked_action_names.begin(), ie = linked_action_names.end();
				for (; it != ie; ++it)
				{
					NuclearHardRef<XAni> hardref = m_anis[*it];
					XPASSERT(hardref.IsValid());
					info.second.push_back(hardref);
				}
			}
		}
	}

	// 只有Hold住的Action才需要Release
	void Component::ReleaseAction(const std::wstring &action_name)
	{
		ComponentAni::iterator it = m_holdAni.find(action_name);
		if (it == m_holdAni.end())
			return;
		--(it->second.first);
		if (it->second.first == 0)
		{
			m_holdAni.erase(it);
		}
	}

	void Component::SetAniReleaseFlag(NuclearPaniReleaseFlag flag)
	{
		NuclearHardRef<XAni> hardref;
		for (AniMap::iterator it = m_anis.begin(), ie = m_anis.end(); it != ie; ++it)
		{
			hardref = it->second;
			if (hardref.IsValid())
				hardref->SetReleaseFlag(flag);
		}
	}

	NuclearWeakRef<XAni> Component::GetAni(const std::wstring &modelname, const std::wstring &actname, bool async, unsigned char bDye)
	{
		if (m_curActName != actname)
		{
			m_curActName = actname;
			m_needDraw = -1; //重置
		}

		if (m_needDraw == 0) //不需要绘制该层
		{
			return m_anis[actname];
		}

		NuclearWeakRef<XAni> &weakani = m_anis[actname];
		NuclearHardRef<XAni> hardref = weakani;
		if (!hardref.IsValid() || hardref->GetState() == XAni::XPANISTATE_NO_XAP)
		{
			//set horse resource path specially. by liugeng
			std::wstring uri = L"";
			if (m_pEB->GetSpriteManager()->GetLayerRideEnable(modelname, m_layer))
			{
				if (m_needDraw == -1)
				{
					if (actname.find(L"riding_") != 0)
					{
						m_needDraw = 0;
						return weakani;
					}
					m_needDraw = 1;
				}
				uri = L"/model/mt_zuoqi/" + m_pEB->GetSpriteManager()->GetLayerName(modelname, m_layer)
					+ L"/" + (m_linkname.empty() ? m_name : m_linkname) + L"/" + actname + SFI_ExtName;
				bDye = 0;
			}
			else
			{
				std::wstring layername = m_pEB->GetSpriteManager()->GetLayerName(modelname, m_layer);
				if (m_needDraw == -1)
				{
					if (layername == L"weapon" && actname.find(L"riding_") == 0)
					{
						m_needDraw = 0;
						return weakani;
					}
					m_needDraw = 1;
				}
				
				uri = L"/model/" + modelname + L"/" + layername
					+ L"/" + (m_linkname.empty() ? m_name : m_linkname) + L"/" + actname + SFI_ExtName;
				if (bDye != 0 && layername != L"body")
					bDye = 0;
			}
			weakani = m_pEB->GetAniManager()->GetAni(uri, async, m_bLookupSegmPak, false, bDye);
		}
		return weakani;
	}

	const Action* SpriteManager::GetAction(const std::wstring &modelname, const std::wstring &actName) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return NULL;
		return it->second->GetAction(actName);
	}

	void SpriteManager::SetAction(const std::wstring &modelname, const std::wstring &actName, const Action& act) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->SetAction(actName, act);
	}

	bool SpriteManager::SaveActions(const std::wstring &modelname) const // 编辑器使用
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->SaveActions();
	}

	bool SpriteManager::AddAction(const std::wstring &modelname, const std::wstring &name, const Action &act) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->AddAction(name, act);
	}

	bool SpriteManager::DeleteAction(const std::wstring &modelname, const std::wstring &name) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->DeleteAction(name);
	}

	bool SpriteManager::RenameAction(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->RenameAction(oldname, newname);
	}

	void SpriteManager::DumpAction(const std::wstring &modelname, std::vector<std::wstring> &actlist) const // 编辑器使用 
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->DumpAction(actlist);
	}

	void SpriteManager::DumpActionRefMap(const std::wstring &modelname, std::map<std::wstring, std::wstring> &actrefmap) const // 编辑器使用
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->DumpActionRefMap(actrefmap);
	}

	void SpriteManager::SetActionRefMap(const std::wstring &modelname, const std::map<std::wstring, std::wstring> &actrefmap) // 编辑器使用
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->SetActionRefMap(actrefmap);
	}

	void SpriteManager::DumpBaseAction(const std::wstring &modelname, std::vector<std::pair<std::wstring, int> > &baseactlist) const // 编辑器使用
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->DumpBaseAction(baseactlist);
	}

	bool SpriteManager::SaveBase(const std::wstring &modelname) //  编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->SaveBase();
	}

	bool SpriteManager::SaveSoundParam(const std::wstring &modelname) //  编辑器使用
	{

		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->SaveSoundParam();
	}

	void SpriteManager::DeleteAction2(const std::wstring &modelname, const std::wstring &name) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->DeleteAction2(name);
	}

	void SpriteManager::RenameAction2(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname, bool renamexap) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->RenameAction2(oldname, newname, renamexap);
	}

	bool SpriteManager::AddComponent(const std::wstring &modelname, const Component &equip) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->AddComponent(equip);
	}

	bool SpriteManager::DeleteComponent(const std::wstring &modelname, const Component &equip) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->DeleteComponent(equip);
	}

	Component* SpriteManager::GetComponent(const std::wstring &modelname, int ectype, const std::wstring &name)
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return NULL;
		return it->second->GetComponent(ectype, name);
	}

	void SpriteManager::DumpComponent(const std::wstring &modelname, int ectype, std::vector<std::wstring> &equiplist) const // 编辑器使用 
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->DumpComponent(ectype, equiplist);
	}

	void SpriteManager::RenameComponent(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return;
		it->second->RenameComponent(oldname, newname);
	}

	ModelSoundParam& SpriteManager::GetSoundParam(const std::wstring &modelname)
	{
		static ModelSoundParam param;
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end())
			return param;
		return it->second->m_soundParam;
	}

	const ModelSoundParam& SpriteManager::GetSoundParam(const std::wstring &modelname) const
	{
		static ModelSoundParam param;
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end())
			return param;
		return it->second->m_soundParam;
	}

	int SpriteManager::GetLayerCount(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return 0;
		return it->second->GetLayerCount();
	}

	unsigned char SpriteManager::GetDyeEnable(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return 0;
		return it->second->m_bDyeEnable;
	}

	int SpriteManager::GetTitlePos(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return 0;
		return it->second->GetTitlePos();
	}

	float SpriteManager::GetModelScale(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return 1;
		return it->second->GetScale();
	}

	const NuclearPoint& SpriteManager::GetEffectPos(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end())
		{
			static NuclearPoint tmpresult(0, 0);
			return tmpresult;
		}
		return it->second->GetEffectPos();
	}

	const NuclearBase& SpriteManager::GetBase(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end())
		{
			static NuclearBase tmpresult;
			return tmpresult;
		}
		return it->second->GetBase();
	}
	NuclearWaterSurfaceType SpriteManager::GetWaterSurfaceType(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return WST_ALWAYS_PLANE;
		return it->second->GetSurfaceType();
	}

	int SpriteManager::GetHorseHeight(const std::wstring &modelname, const std::wstring &horsename)
	{
		AllHorseHeightMap::iterator iter = m_horseHeights.find(modelname);
		if (iter == m_horseHeights.end())
		{
			m_horseHeights[modelname].Load(m_pEB->GetFileIOManager(), modelname);
			return m_horseHeights[modelname].GetHeight(horsename);
		}
		return iter->second.GetHeight(horsename);
	}

	float SpriteManager::GetHorseScale(const std::wstring &modelname, const std::wstring &horsename)
	{
		AllHorseHeightMap::iterator iter = m_horseHeights.find(modelname);
		if (iter == m_horseHeights.end())
		{
			m_horseHeights[modelname].Load(m_pEB->GetFileIOManager(), modelname);
			iter = m_horseHeights.find(modelname);
			if (iter == m_horseHeights.end())
				return 1.0;
			return iter->second.GetScale(horsename);
		}
		return iter->second.GetScale(horsename);
	}

	bool SpriteManager::SetTitlePos(const std::wstring &modelname, int pos) //  编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		it->second->SetTitlePos(pos);
		SaveModels();
		return true;
	}

	bool SpriteManager::SetEffectPos(const std::wstring &modelname, const NuclearPoint &pos) //  编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		it->second->SetEffectPos(pos);
		SaveModels();
		return true;
	}

	bool SpriteManager::SetBase(const std::wstring &modelname, const NuclearBase &base, bool savefile)
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		if (savefile)
		{
			NuclearBase tmpBase = base;
			NuclearPoint basept[4];
			const float tga = 1.60033f; // tan58
			basept[0] = base.left;
			basept[2] = base.right;
			basept[1].x = (base.left.x + base.right.x) / 2 + (LONG)(tga * (base.left.y - base.right.y) / 2);
			basept[1].y = (base.left.y + base.right.y) / 2 + (LONG)((base.left.x - base.right.x) / (2 * tga));
			basept[3].x = (base.left.x + base.right.x) / 2 + (LONG)(-tga * (base.left.y - base.right.y) / 2);
			basept[3].y = (base.left.y + base.right.y) / 2 + (LONG)((base.left.x - base.right.x) / (2 * -tga));
			int i;
			for (i = 0; i < 4; ++i)
			{
				if (basept[i].x < tmpBase.left.x)
				{
					tmpBase.left = basept[i];
				}
				if (basept[i].x > tmpBase.right.x)
				{
					tmpBase.right = basept[i];
				}
			}
			it->second->SetBase(tmpBase);
			SaveModels();
		}
		else {
			it->second->SetBase(base);
		}
		return true;
	}

	bool SpriteManager::SetHorseHeight(const std::wstring &modelname, const std::wstring &horsename, int pos) //  编辑器使用
	{
		AllHorseHeightMap::iterator iter = m_horseHeights.find(modelname);
		if (iter == m_horseHeights.end())
		{
			m_horseHeights[modelname].Load(m_pEB->GetFileIOManager(), modelname);
			m_horseHeights[modelname].SetHeight(horsename, pos);
			return m_horseHeights[modelname].Save(m_pEB->GetFileIOManager(), modelname);
		}
		iter->second.SetHeight(horsename, pos);
		return iter->second.Save(m_pEB->GetFileIOManager(), modelname);
	}

	bool SpriteManager::SetWaterSurfaceType(const std::wstring &modelname, NuclearWaterSurfaceType type) //  编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		it->second->SetSurfaceType(type);
		SaveModels();
		return true;
	}

	bool SpriteManager::GetLayerRideEnable(const std::wstring &modelname, int i) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->GetLayerRideEnable(i);
	}

	bool SpriteManager::GetLayerEffectEnable(const std::wstring &modelname, int i) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return false;
		return it->second->GetLayerEffectEnable(i);
	}

	std::wstring SpriteManager::GetLayerName(const std::wstring &modelname, int i) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return L"unknown";
		return it->second->GetLayerName(i);
	}

	std::wstring SpriteManager::GetLayerDes(const std::wstring &modelname, int i) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return L"未知";
		return it->second->GetLayerDes(i);
	}

	const std::wstring& SpriteManager::GetActionRef(const std::wstring &modelname, const std::wstring &actname) const
	{
		static std::wstring nilstr = L"";
		ModelMap::const_iterator it = m_models.find(modelname);
		if (it == m_models.end()) return nilstr;
		return it->second->GetActionRef(actname);
	}

	bool ActionType::Load()
	{
		std::wstring xmlpath = L"/model/actiontype.set";
		NuclearBuffer xmlfiledata;
		char* fileBuf = NULL;
		std::streamsize ss = 0;
		if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
		{
			return false;
		}

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
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

		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		LJXML::LJXML_NodeList typenl;
		root->GetSubNodeList(typenl);
		for (int i = 0; i < typenl.size(); ++i)
		{
			if (typenl[i]->type() != rapidxml::node_element)
				continue;

			int iID = _wtoi(typenl[i]->first_attribute(L"id")->value());
			unsigned char id = (unsigned char)iID;

			XPASSERT(id > 0);
			XPASSERT(id <= 16);
			std::wstring des = typenl[i]->first_attribute(L"des")->value();
			if (!des.empty())
			{
				m_actionTypeDes[id] = des;
			}

			LJXML::LJXML_NodeList itemNl;
			typenl[i]->GetSubNodeList(itemNl);
			for (int j = 0; j < itemNl.size(); ++j)
			{
				std::wstring actName = itemNl[j]->first_attribute(L"name")->value();
				if (!actName.empty())
				{
					m_actionTypeMap[actName] |= 1 << (id - 1);
				}
			}
		}

		return true;
	}

	bool HorseHeight::Load(NuclearFileIOManager* pFileIOMan, const std::wstring &modelname)
	{
		std::wstring xmlpath = L"/model/mt_zuoqi/horseheight.lmx"; //change save path. by liugeng
		char* fileBuf = NULL;
		std::streamsize ss = 0;
		if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
		{
			return false;
		}

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
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

		int height = 0;
		float scale = 1.0;
		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		LJXML::LJXML_NodeList horsenl;
		root->GetSubNodeList(horsenl);
		for (int i = 0; i < horsenl.size(); ++i)
		{
			if (horsenl[i]->type() != rapidxml::node_element)
				continue;
			scale = 1.0;
			height = _wtoi(horsenl[i]->first_attribute(L"height")->value()); //horseNode.GetAttributeInteger(L"height");
			if (horsenl[i]->first_attribute(L"scale"))
				scale = _wtof(horsenl[i]->first_attribute(L"scale")->value());

			std::wstring name = horsenl[i]->first_attribute(L"name")->value();
			if (!name.empty())
			{
				m_map[name] = height;
				m_mapscale[name] = scale;
			}
		}

		return true;
	}

	bool HorseHeight::Save(NuclearFileIOManager* pFileIOMan, const std::wstring &modelname) const
	{
		assert(false);
		return false;
	}


}