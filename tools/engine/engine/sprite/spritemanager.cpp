#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\pfsutil.h"
#include "..\common\xml.h"
#include "..\common\fileiomanager.h"
#include "..\engine\enginebase.h"
#include "spritemanager.h"
#include <iostream>
#include <fstream>

namespace Nuclear
{
	enum XPLayer_Type
	{
		XPLT_RIDE = 1,			//01
		XPLT_EFFECT = 2,        //10
	};

	void Action::Frame::SetOrder(int dir, const int *ec, int size)
	{
		__int64 r = 0;
		for(int i=0; i<size;++i)
			r |= ((__int64)ec[i] << (i*4));
		m_equipOrder[dir] = r;
	}

	void Action::SetFrameOrder(int seq, int dir, const int *ec, int size)
	{
		m_frames[seq].SetOrder(dir, ec, size);
	}

	void Action::Frame::GetOrder(int dir, int* ec, int size) const
	{
		__int64 r = m_equipOrder[dir];
		for(int i=0; i<size;++i)
			ec[i] = (r >> (i*4))&0xf;
	}

	void Action::GetFrameOrder(int seq,  int dir, int* ec, int size) const
	{
		m_frames[seq].GetOrder(dir, ec, size);
	}

	void Action::SetFrameLink(int seq, const std::wstring &n, int f)
	{
		if( m_linkType == AL_NULL ) return;
		if( m_links.size() != m_frames.size() )
			m_links.resize(m_frames.size());
		m_links[seq].name = n;
		m_links[seq].frame = f;
	}

	void Action::SetFrameCount(int n ) 
	{ 
		if( m_frames.empty() || n <= (int)m_frames.size()) 
			m_frames.resize(n); 
		else 
			m_frames.resize(n, m_frames[m_frames.size()-1]); 
	}

	//
	XOStream& Action::marshal(XOStream &os) const
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
		for(int i=0; i<n; ++i)
		{
			const Frame &f = m_frames[i];
			os << f.m_time;
			os << f.m_startTime;
			for(int j=0; j<XPDIR_COUNT; ++j)
				os << f.m_equipOrder[j];
		}

		os << m_linkType;
		if( m_linkType )
		{
			for(int i=0; i<n; ++i)
			{
				const Link &f = m_links[i];
				os << f.name;
				os << f.frame;
			}
		}
		for(int i=0; i<8; ++i)
		{
			os << m_rects[i];
		}
		return os;
	}

	const XIStream& Action::unmarshal(const XIStream &os)
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
			CPOINT unuse;
			os >> unuse;
			os >> unuse;
			os >> unuse;
			os >> unuse;
			os >> unuse;
		}
		os >> m_stride;
		int n;
		os >> n;
		for(int i=0; i<n; ++i)
		{
			Frame f;
			os >> f.m_time;
			os >> f.m_startTime;
			for(int j=0; j<XPDIR_COUNT; ++j)
			{
				if(version <= 8)
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

		if( version > 5 )
		{
			os >> m_linkType;

			if( m_linkType )
			{
				m_links.resize(n);
				for(int i=0; i<n; ++i)
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
			for (int i=0;i<8;++i)
			{
				os >> m_rects[i];
				NormalizeRect(m_rects[i]);
			}
		}

		return os;
	}

	bool SpriteManager::XModel::AddAction(const std::wstring &name, const Action &act) // 编辑器使用
	{
		if( m_actions.find(name) != m_actions.end() )
			return false;
		m_actions[name] = new Action(act);
		return true;
	}

	bool SpriteManager::XModel::DeleteAction(const std::wstring &name) // 编辑器使用
	{
		std::map<std::wstring,Action*>::iterator it = m_actions.find(name);
		if( it == m_actions.end() )
			return false;

		std::wstring actfilename = L"/sprite"+m_name+L"/action/";
		actfilename += name;
		actfilename += L".act";
		//PFS::CEnv::RemoveFile(actfilename);
		std::wstring nativename;
		if( PFS::CEnv::PFSFileNameToNativeFileName( actfilename, nativename) )
			::DeleteFile(nativename.c_str());
		delete it->second;
		m_actions.erase(it);
		return true;
	}

	bool SpriteManager::XModel::RenameAction(const std::wstring &oldname, const std::wstring &newname) // 编辑器使用
	{
		std::map<std::wstring,Action*>::iterator it = m_actions.find(oldname);
		if( it == m_actions.end() )
			return false;

		std::wstring oldactfilename = L"/sprite"+m_name+L"/action/";
		oldactfilename += oldname;
		oldactfilename += L".act";

		std::wstring newactfilename = L"/sprite"+m_name+L"/action/";
		newactfilename += newname;
		newactfilename += L".act";

		std::wstring oldnativename;
		std::wstring newnativename;

		if( PFS::CEnv::PFSFileNameToNativeFileName( oldactfilename, oldnativename) 
			&& PFS::CEnv::PFSFileNameToNativeFileName( newactfilename, newnativename) )
		{
			::MoveFile(oldnativename.c_str(), newnativename.c_str());
		}
		Action *pact = it->second;
		m_actions.erase(it);
		m_actions[newname] = pact;
		return true;
	}

	bool SpriteManager::XModel::LoadActions()
	{
		XBuffer xmlfiledata;
		if( !m_pEB->GetFileIOManager()->GetFileImage(L"/model/" + m_name + L"/action/action.lmx", xmlfiledata))
			return true;
		XMLIO::CFileReader		fr;
		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
			return false;

		XMLIO::CINode	root;
		if( ! fr.GetRootNode(root))
			return false;

		XMLIO::CNodeList nl;
		root.GetChildren(nl);

		for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if (L"action" != node.GetName()) continue;
			std::wstring str;
			if ( (!node.GetAttribute(L"name", str) ) || str.empty())
				continue;
			std::wstring ref;
			if( node.GetAttribute(L"ref", ref) && (!ref.empty()) )
			{
				m_actionrefs[str] = ref;
			}
			else
			{
				if( m_actions.find(str) != m_actions.end() ) continue;

				std::wstring fpath = L"/model/"+ m_name + L"/action/" + str + L".act";
				XBuffer actfiledata;
				if( m_pEB->GetFileIOManager()->GetFileImage(fpath, actfiledata))
				{
					Action act;
					if( act.LoadFromMem(actfiledata) )
					{
						m_actions[str] = new Action(act);
					}
				}
			}
		}
		fr.CloseFile();

		bLoadedAction = true;

		return true;
	}

	bool SpriteManager::XModel::SaveActions() const // 编辑器使用，只保存xml
	{
		XMLIO::CFileWriter fw;
		XMLIO::CONode node;
		fw.GetRootNode(node);//不需要判断返回值，因为fw是一个全新的对象，不依赖外界参数以及环境

		for(std::map<std::wstring,std::wstring>::const_iterator it = m_actionrefs.begin(), ie = m_actionrefs.end(); it != ie; ++it)
		{
			const std::wstring &ref = it->second;
			if( m_actions.find(ref) == m_actions.end() ) continue;
			XMLIO::CONode subnode;
			if ( !fw.CreateElement( node, L"action", subnode ) )
				continue;
			subnode.SetAttribute( L"name", it->first);
			subnode.SetAttribute( L"ref", it->second);
		}

		for(std::map<std::wstring, Action*>::const_iterator it = m_actions.begin(), ie = m_actions.end(); it != ie; ++it)
		{
			XMLIO::CONode subnode;
			if ( !fw.CreateElement( node, L"action", subnode) )
				continue;
			subnode.SetAttribute( L"name", it->first);
		}

		const std::wstring filename = L"/model/" + m_name + L"/action/action.lmx";
		bool bExist = PFS::CFile::IsFileExisting(filename);
		int nValue = !bExist ? 1 : 0;

		int retcode = fw.Save( filename, nValue);
		return retcode == XMLIO::EC_SUCCESS;
	}

	void SpriteManager::XModel::DumpAction(std::vector<std::wstring> &actlist) const // 编辑器使用
	{
		for(std::map<std::wstring, Action*>::const_iterator it = m_actions.begin(), ie = m_actions.end(); it != ie; ++it)
			actlist.push_back(it->first);
	}

	void SpriteManager::XModel::DumpActionRefMap(std::map<std::wstring,std::wstring> &actrefmap) const // 编辑器使用
	{
		actrefmap = m_actionrefs;
	}

	void SpriteManager::XModel::SetActionRefMap(const std::map<std::wstring,std::wstring> &actrefmap) // 编辑器使用
	{
		m_actionrefs = actrefmap;
		SaveActions();
	}

	void SpriteManager::XModel::DumpBaseAction(std::vector<std::pair<std::wstring,int> > &baseactlist) const // 编辑器使用
	{
		for(std::map<std::wstring, Action*>::const_iterator it = m_actions.begin(), ie = m_actions.end(); it != ie; ++it)
		{
			const Action *pAct = it->second;
			if( pAct->IsInd() ) continue;
			baseactlist.push_back(std::pair<std::wstring, int>(it->first, pAct->GetFrameCount()));
		}
	}

	const Action* SpriteManager::XModel::GetAction(const std::wstring &actName) const
	{
		std::map<std::wstring, Action*>::const_iterator it = m_actions.find(actName);
		if( it == m_actions.end()) return NULL;
		return it->second;
	}

	const std::wstring& SpriteManager::XModel::GetActionRef(const std::wstring &actname) const
	{
		std::map<std::wstring,std::wstring>::const_iterator it = m_actionrefs.find(actname);
		if( it == m_actionrefs.end() ) return actname;
		return it->second;
	}

	void SpriteManager::XModel::SetAction(const std::wstring &actName, const Action& act)
	{
		std::map<std::wstring, Action*>::const_iterator it = m_actions.find(actName);
		if( it == m_actions.end()) return;
		*(it->second) = act;
	}

	bool SpriteManager::XModel::LoadDyeInfo()
	{
		std::wstring dyeInfoPath = L"/model/"+ m_name + L"/dyeinfo.dye";

		PFS::CFile cf;
		if( cf.IsFileExisting(dyeInfoPath) == false )
			return false;

		XBuffer buffer;
		if( !m_pEB->GetFileIOManager()->GetFileImage(dyeInfoPath, buffer))
			return false;
		if( buffer.size() == 0 )
			return false;
		XIStream os(buffer);

		char fileHead[4];
		os.pop_byte(fileHead, sizeof(char) * 4);
		if (fileHead[0] != 'D' || fileHead[1] != 'Y' || fileHead[2] != 'E' || fileHead[3] != 'P' )
		{
			XPLOG_ERROR(L"dyt fileHead Error\n");
			return false;
		}

		int version = 0;
		os >> version;
		int partCount = 0;
		os >> partCount;
		m_DyeInfos.resize(partCount);
		for( int i = 0; i < partCount; i++ )
		{
			int projCount = 0;
			os >> projCount;
			PartInfo& info = m_DyeInfos[i];
			info.resize(projCount);
			for( int j = 0; j < projCount; j++ )
			{
				os.pop_byte(&info[j],sizeof(DyeInfo));
			}
		}

		cf.Close();
		return true;
	}

	bool SpriteManager::XModel::SaveDyeInfo()
	{
		std::wstring dyeInfoPath = L"/model/"+ m_name + L"/dyeinfo.dye";
		
		PFS::CFile cf;
		if( cf.Open(dyeInfoPath, PFS::FM_CREAT, PFS::FA_WRONLY ))
		{
			XOStream os;
			const char fileHead[4] = {'D','Y','E','P'};
			os.push_byte(fileHead, sizeof(char) * 4);	//head
			os << VERSION;		//version
			int partSize = getDyePartCount();
			os << partSize;
			for( size_t i = 0; i < m_DyeInfos.size(); i++ )
			{
				PartInfo& info = m_DyeInfos[i];

				int projSize = info.size();
				os << projSize;
				for( size_t j = 0; j < info.size(); j++ )
				{
					os.push_byte(&info[j],sizeof(DyeInfo));
				}
			}
			cf.Write(os.begin(),os.size());
		}

		cf.Close();
		return true;
	}

	bool SpriteManager::XModel::SaveLayerConfig()
	{
		XMLIO::CFileWriter fw;
		XMLIO::CONode node;
		fw.GetRootNode(node);

		if ( m_ndyeEnable != 0)
		{
			XMLIO::CONode subnode;
			fw.CreateElement( node, L"dye",subnode);
			subnode.SetAttribute( L"type", m_ndyeEnable);
		}
		int id = 0;
		int type;
		for(std::vector<Layer>::const_iterator it = m_layers.begin(), ie = m_layers.end(); it != ie; ++it)
		{
			++id;
			XMLIO::CONode subnode;
			if ( !fw.CreateElement( node, L"layer", subnode ) )
				continue;
			subnode.SetAttribute( L"id", id);
			subnode.SetAttribute( L"name", it->name);
			subnode.SetAttribute( L"des", it->des);
			type = it->bRide?XPLT_RIDE:0;
			type |= it->bEffect?XPLT_EFFECT:0;
			subnode.SetAttribute( L"type", type);
		}

		int retcode = fw.Save( L"/model/" + m_name + L"/layerdef.lmx");
		return retcode == XMLIO::EC_SUCCESS;
		return true;
	}

	bool SpriteManager::XModel::LoadLayerConfig()
	{
		XMLIO::CFileReader		fr;
		std::wstring xmlpath = L"/model/"+ m_name + L"/layerdef.lmx";
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

		std::map<int, Layer> tmap;
		int maxid = 0;

		XMLIO::CNodeList nl;

		root.GetChildren(nl);

		for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if ( L"layer" != node.GetName())
			{
				if ( L"dye" == node.GetName())
				{
					m_ndyeEnable = node.GetAttributeInteger(L"type", 0);
					if(m_ndyeEnable == 0)	m_ndyeEnable = 1;
				}
				continue;
			}
			std::wstring idstr;
			node.GetAttribute(L"id", idstr);
			std::wstring namestr;
			node.GetAttribute(L"name", namestr);
			std::wstring desstr;
			node.GetAttribute(L"des", desstr);
			std::wstring ridestr;
			node.GetAttribute(L"ride", ridestr);
			//这里需要加上返回值判断么？要不要将这几个wstring的声明扔到for循环的外面呢？
			int type = node.GetAttributeInteger(L"type", 0);

			int id = _wtoi(idstr.c_str());
			if( id <= 0 || id > XPSC_MAXCOUNT ) continue;
			if( id > maxid ) maxid = id;
			tmap[id] = Layer(namestr, desstr, ((type & XPLT_RIDE) != 0) || ridestr == L"true", (type & XPLT_EFFECT) != 0 );
		}
		fr.CloseFile();

		if( maxid == 0 ) return false;
		if( (int)tmap.size() != maxid ) return false;
		m_layers.clear();
		for(int i=1; i<= maxid; ++i)
			m_layers.push_back(tmap[i]);
		m_Components.resize(maxid);

		bLoadLayerConfig = true;

		return true;
	}

	bool SpriteManager::SaveModels()
	{
		XMLIO::CFileWriter fw;
		XMLIO::CONode node;
		fw.GetRootNode(node);

		for(ModelMap::const_iterator it = m_models.begin(), ie = m_models.end(); it != ie; ++it)
		{
			XMLIO::CONode subnode;
			if (fw.CreateElement( node, L"model", subnode))
			{
				subnode.SetAttribute( L"name", it->first);
				subnode.SetAttribute( L"des", it->second->m_des);
				subnode.SetAttribute( L"type", it->second->m_nType);
				subnode.SetAttribute( L"titlepos", it->second->m_nTitlePos);
				subnode.SetAttribute( L"efctPosX", it->second->m_effectPos.x);
				subnode.SetAttribute( L"efctPosY", it->second->m_effectPos.y);
				subnode.SetAttribute( L"surfacetype", it->second->m_surfaceType);
				subnode.SetAttribute( L"blx", it->second->m_base.left.x);
				subnode.SetAttribute( L"bly", it->second->m_base.left.y);
				subnode.SetAttribute( L"brx", it->second->m_base.right.x);
				subnode.SetAttribute( L"bry", it->second->m_base.right.y);
				subnode.SetAttribute(L"scale", it->second->m_fScale);
			}
		}

		int retcode = fw.Save( std::wstring(L"/model/sprites") + ImageSet_ExtName);
		return retcode == XMLIO::EC_SUCCESS;
	}

	bool SpriteManager::PreInit()
	{
		XMLIO::CFileReader		fr;

		std::wstring xmlpath = std::wstring(L"/model/sprites") + ImageSet_ExtName;
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
		XPASSERT(!m_pInitingModelVet);
		m_pInitingModelVet = new XModelVet();
		if (!m_pInitingModelVet)
			return false;

		bool r = true;

		XMLIO::CNodeList nl;
		root.GetChildren(nl);
		m_pInitingModelVet->reserve(nl.size());
		for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if ( L"model" != node.GetName()) continue;

			std::wstring namestr;
			node.GetAttribute(L"name", namestr);
			std::wstring desstr;
			node.GetAttribute(L"des", desstr);

			if( namestr.empty() || desstr.empty() || m_models.find(namestr) != m_models.end() )
			{
				r = false;
				continue;
			}

			XModel *pM = new XModel(m_pEB);
			pM->m_name = namestr;
			pM->m_des = desstr;

			pM->m_nType = node.GetIntAttribute(L"type");
			pM->m_nTitlePos = node.GetIntAttribute(L"titlepos");
			pM->m_effectPos.x = node.GetIntAttribute(L"efctPosX");
			pM->m_effectPos.y = node.GetIntAttribute(L"efctPosY");
			pM->m_surfaceType = static_cast<WATER_SURFACE_TYPE>(node.GetIntAttribute(L"surfacetype"));
			pM->m_base.left.x = node.GetIntAttribute(L"blx", -10);
			pM->m_base.left.y = node.GetIntAttribute(L"bly");
			pM->m_base.right.x = node.GetIntAttribute(L"brx", 10);
			pM->m_base.right.y = node.GetIntAttribute(L"bry");
			pM->m_fScale = node.GetAttributeFloat(L"scale", 1);

			m_pInitingModelVet->push_back(pM);
		}
		fr.CloseFile();
		return r;
	}

	unsigned __int64 SpriteManager::Init(size_t id, size_t batch)
	{
		XPASSERT(batch <= 64);
		XPASSERT(m_pInitingModelVet);
		size_t size = m_pInitingModelVet->size();
		XPASSERT(id < size);
		unsigned __int64 result = 0;
		unsigned __int64 mask = 1;
		XModel *pM = NULL;
		XModel **ppM = &((*m_pInitingModelVet)[id]);
		size -= id;
		size = min(size, batch);
		id = 0;
		for (;id<size;++id,++ppM)
		{
			pM = *ppM;
			if (pM->m_nType == 0)
			{
				pM->LoadDyeInfo();
				if (pM->LoadActions() && pM->LoadBase() && pM->LoadSoundParam())
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
		
		std::wstring xmlpath = L"/model/" + m_name +L"/" + L"sound.inf";
		XBuffer xmlfiledata;
		if( !m_pEB->GetFileIOManager()->GetFileImage(xmlpath, xmlfiledata))
			return true;//没有也可以

		XMLIO::CFileReader fr;
		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
			return false;//如果有但是打开错误，就return false了

		XMLIO::CINode root;

		if( !fr.GetRootNode(root) )
		{
			fr.CloseFile();
			return false;
		}

		m_soundParam.keyComponentID = root.GetAttributeInteger(L"key");
		XMLIO::CNodeList nl;
		root.GetChildren(nl);
		for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if (node.GetType() != XMLIO::NT_ELEMENT)
				continue;
			std::wstring componentName;
			if (!node.GetAttribute(L"name", componentName))
				continue;
			PActionSoundParamMap &pSmap = m_soundParam.soundmap[componentName];
			if (pSmap == NULL)//肯定的吧。。。。
			{
				pSmap = new ActionSoundParamMap();
			}
			XMLIO::CNodeList actList;
			node.GetChildren(actList);
			for( XMLIO::CNodeList::iterator ait = actList.begin(); ait != actList.end(); ++ ait)
			{
				XMLIO::CINode& anode = *ait;
				if (anode.GetType() != XMLIO::NT_ELEMENT)
					continue;
				std::wstring actionName;
				if (!anode.GetAttribute(L"an", actionName))
					continue;
				int count = anode.GetAttributeInteger(L"count");
				if (count == 0)
					continue;
				if (count > 10)
					count = 10;//不能超过10个。。。。。
				SpriteSoundParamVet &vet = (*pSmap)[actionName];
				vet.resize(count);
				tmpname[2] = L'0';
				tmpname[3] = L'\0';
				for (int i=0;i<count;++i,++(tmpname[2]))
				{
					tmpname[0] = L's';
					tmpname[1] = L'u';
					if (!anode.GetAttribute(tmpname, vet[i].soundname))
						continue;
					tmpname[0] = L'v';
					tmpname[1] = L'o';
					vet[i].vol = static_cast<unsigned char>(anode.GetAttributeInteger(tmpname));
					tmpname[0] = L'f';
					tmpname[1] = L'r';
					vet[i].freq = static_cast<unsigned char>(anode.GetAttributeInteger(tmpname));					
				}
			}
		}
		return true;
	}

	bool SpriteManager::XModel::SaveSoundParam()
	{
		std::wstring tmpname;
		tmpname.resize(4);
		XMLIO::CFileWriter fw;
		XMLIO::CONode node;
		fw.GetRootNode(node);
		node.SetAttribute(L"key", m_soundParam.keyComponentID);
		ComponentSoundMap::iterator it = m_soundParam.soundmap.begin(), 
			ie = m_soundParam.soundmap.end();
		for (;it!=ie;++it)
		{
			if (it->second == NULL)
				continue;
			XMLIO::CONode componnode;
			node.CreateSubElement(L"component", componnode);
			componnode.SetAttribute(L"name", it->first);
			ActionSoundParamMap::iterator aIt = it->second->begin(), 
				aIe = it->second->end();
			for (;aIt!=aIe;++aIt)
			{
				if (aIt->second.size() > 0)
				{
					XMLIO::CONode actionnode;
					componnode.CreateSubElement(L"act", actionnode);
					actionnode.SetAttribute(L"an", aIt->first);
					XPASSERT(aIt->second.size() < 11);
					actionnode.SetAttribute(L"count", aIt->second.size());
					SpriteSoundParamVet::iterator sIt = aIt->second.begin(),
						sIe = aIt->second.end();
					tmpname[2] = L'0';
					tmpname[3] = L'\0';
					int i=0;
					for (;sIt!=sIe;++sIt, ++(tmpname[2]))
					{
						tmpname[0] = L's';
						tmpname[1] = L'u';
						actionnode.SetAttribute(tmpname, sIt->soundname.c_str());
						tmpname[0] = L'v';
						tmpname[1] = L'o';
						actionnode.SetAttribute(tmpname, (int)sIt->vol);
						tmpname[0] = L'f';
						tmpname[1] = L'r';
						actionnode.SetAttribute(tmpname, (int)sIt->freq);
					}
				}
			}
		}
		if( XMLIO::EC_SUCCESS != fw.Save( L"/model/" + m_name +L"/" + L"sound.inf" ) )
			return false;
		return true;
	}

	bool SpriteManager::XModel::LoadBase()
	{
		if( !LoadLayerConfig() )
		{
			return false;
		}

		for(int i=0; i< GetLayerCount(); ++i)
		{
			XMLIO::CFileReader		fr;
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

			XMLIO::CNodeList nl;
			root.GetChildren(nl);
			for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
			{
				XMLIO::CINode& node = *it;
				if (GetLayerName(i) != node.GetName()) continue;
				std::wstring name;
				node.GetAttribute(L"name", name);

				if( name.empty() ) continue;
				if( m_Components[i].find(name) != m_Components[i].end() ) continue;
				std::wstring linkname;
				node.GetAttribute(L"linkname", linkname);
				m_Components[i][name] = new Component(m_pEB, i, name, linkname);
			}
			fr.CloseFile();
		}

		bLoadedBase = true;

		return true;
	}

	bool SpriteManager::XModel::SaveBase()
	{
		if( !SaveLayerConfig() )
		{
			return false;
		}

		for(int i=0; i<(int)m_layers.size(); ++i)
		{
			XMLIO::CFileWriter fw;
			XMLIO::CONode node;
			fw.GetRootNode(node);
			
			//2015 增加lmx内容
			for(std::map<std::wstring, Component*>::const_iterator it = m_Components[i].begin(), ie = m_Components[i].end(); it != ie; ++it)
			{
				XMLIO::CONode subnode;
				if ( fw.CreateElement( node, m_layers[i].name, subnode ) )
				{
					subnode.SetAttribute( L"name", it->second->GetName());
					std::wstring linkname = it->second->GetLinkName();
					if( !linkname.empty() )
						subnode.SetAttribute( L"linkname", linkname);
				}
			}

			std::wstring filename = L"/model/"+ m_name +L"/" + m_layers[i].name+L"/"+m_layers[i].name+L".lmx";
			int nValue = 0;
			if( XMLIO::EC_SUCCESS != fw.Save(filename, nValue))
				return false;
		}
		return true;
	} 

	void SpriteManager::XModel::DeleteAction2(const std::wstring &name) // 编辑器使用
	{
		for(int i=0; i<GetLayerCount(); ++i)
		{
			for(std::map<std::wstring, Component*>::iterator it = m_Components[i].begin(), ie = m_Components[i].end(); it != ie; ++it)
			{
				it->second->DeleteAction(m_name, name);
			}
		}
	}

	std::wstring SpriteManager::GetModelPath(const std::wstring& modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return L"";
		return it->second->m_ModelPath;
	}

	//std::wstring SpriteManager::GetModelBodyPath(const std::wstring& modelname) const
	//{
	//	ModelMap::const_iterator it = m_models.find(modelname);
	//	if( it == m_models.end() ) return L"";
	//	XModel* model = it->second;
	//	if(!model) return L"";
	//	for(size_t i = 0; i < model->m_layers.size(); ++i)
	//	{
	//		Layer& layer = model->m_layers[i];
	//		if(layer.name == L"body")
	//		{
	//			return model->m_ModelPath+ L"\\" + layer.name + L"\\bodyonly";
	//		}
	//	}

	//	return L"";
	//}

	void SpriteManager::XModel::RenameAction2(const std::wstring &oldname, const std::wstring &newname, bool renamexap) // 编辑器使用
	{
		for(int i=0; i<GetLayerCount(); ++i)
		{
			for(std::map<std::wstring, Component*>::iterator it = m_Components[i].begin(), ie = m_Components[i].end(); it != ie; ++it)
			{
				it->second->RenameAction(m_name, oldname, newname, renamexap);
			}
		}
	}

	bool SpriteManager::XModel::AddComponent(const Component &equip) // 编辑器使用
	{
		assert(equip.GetLayer() >= 0 && equip.GetLayer() < GetLayerCount());
		if( m_Components[equip.GetLayer()].find(equip.GetName()) != m_Components[equip.GetLayer()].end() )
			return false;
		m_Components[equip.GetLayer()][equip.GetName()] = new Component(equip);
		
		std::wstring nativedir;
		PFS::CEnv::PFSFileNameToNativeFileName(L"/model/"+m_name+L"/"+m_layers[equip.GetLayer()].name+L"/"+equip.GetName(), nativedir);
		CreateDirectory(nativedir.c_str(), NULL);
		return true;
	}

	void SpriteManager::XModel::RenameComponent(const std::wstring &oldname, const std::wstring &newname) // 编辑器使用
	{
		for(int i=0; i<(int)m_layers.size();++i)
		{
			std::map<std::wstring, Component*> &m = m_Components[i];
			std::map<std::wstring, Component*>::iterator it = m.find(oldname);
			if( it == m.end() ) continue;
			if( m.find(newname) != m.end() ) continue;
			Component *pe = it->second;
			pe->SetName(newname);
			m.erase(it);
			m[newname] = pe;
			std::wstring oldnativedir;
			PFS::CEnv::PFSFileNameToNativeFileName(L"/model/"+m_name+L"/"+m_layers[i].name+L"/"+oldname, oldnativedir);
			std::wstring newnativedir;
			PFS::CEnv::PFSFileNameToNativeFileName(L"/model/"+m_name+L"/"+m_layers[i].name+L"/"+newname, newnativedir);
			::MoveFile(oldnativedir.c_str(), newnativedir.c_str());
		}
		SaveBase();
	}


	bool SpriteManager::XModel::DeleteComponent(const Component &equip) // 编辑器使用
	{
		assert(equip.GetLayer() >= 0 && equip.GetLayer() < GetLayerCount());
		std::map<std::wstring, Component*>::iterator it = m_Components[equip.GetLayer()].find(equip.GetName());
		if( it == m_Components[equip.GetLayer()].end() )
			return false;
		if( it->second )
		{
			it->second->Release();
			delete it->second;
		}
		m_Components[equip.GetLayer()].erase(it);
		PFS::CEnv::RemoveFile(L"/model/"+m_name+L"/"+m_layers[equip.GetLayer()].name+L"/"+equip.GetName()+L"/"+equip.GetName()+L".lmx");
		std::wstring nativedir;
		PFS::CEnv::PFSFileNameToNativeFileName(L"/model/"+m_name+L"/"+m_layers[equip.GetLayer()].name+L"/"+equip.GetName(), nativedir);
		DeleteWinDir(nativedir);
		return true;
	}

	Component* SpriteManager::XModel::GetComponent(int ectype, const std::wstring &name)
	{
		assert(ectype >= 0 && ectype < GetLayerCount());
		std::map<std::wstring, Component*>::iterator it = m_Components[ectype].find(name);
		if( it == m_Components[ectype].end() ) return NULL;
		return it->second;
	}

	SpriteManager::SpriteManager(EngineBase *pEB) : m_pEB(pEB), m_pInitingModelVet(NULL)
	{}

	SpriteManager::~SpriteManager()
	{
		if (m_pInitingModelVet)
			delete m_pInitingModelVet;
		for(ModelMap::iterator it = m_models.begin(), ie = m_models.end(); it != ie; ++it)
			delete it->second;
	}

	SpriteManager::XModel::~XModel()
	{
		for(int i=0; i<GetLayerCount(); ++i)
		{
			for(std::map<std::wstring, Component*>::iterator it = m_Components[i].begin(), ie = m_Components[i].end(); it != ie; ++it)
			{
				if( it->second )
				{
					it->second->Release();
					delete it->second;
				}
			}
			m_Components[i].clear();
		}

		ComponentSoundMap::iterator cit = m_soundParam.soundmap.begin(), 
			cie = m_soundParam.soundmap.end();
		for (;cit!=cie;++cit)
		{
			delete cit->second;
		}
		m_soundParam.soundmap.clear();


		for(std::map<std::wstring, Action*>::iterator it = m_actions.begin(), ie = m_actions.end(); it != ie; ++it)
			delete it->second;
		m_actions.clear();
	}


	void Component::Release()
	{
		for(AniMap::iterator it = m_anis.begin(), ie = m_anis.end(); it != ie; ++it)
		{
			it->second.Release();	
		}
	}

	void Component::SetName(const std::wstring &name) // 编辑器使用
	{
		if( name != m_name)
		{ 
			m_name = name;
			if( m_linkname.empty() )
				m_anis.clear();
		}
	}

	void Component::SetLinkName(const std::wstring &linkname)// 编辑器使用
	{
		if( linkname != m_linkname)
		{
			m_linkname = linkname;
			m_anis.clear();
		}
	}

	void Component::DeleteAction(const std::wstring &modelname, const std::wstring &name) // 编辑器使用
	{
		SpriteManager *pSprMan = m_pEB->GetSpriteManager();
		if (!pSprMan)
			return;
		std::map<std::wstring,XWeakRef<XAni> >::iterator it = m_anis.find(name);
		if( it != m_anis.end())
			m_anis.erase(it);
		std::wstring uri = L"/model/" + modelname+L"/" + pSprMan->GetLayerName(modelname, m_layer) +
			L"/" + m_name +L"/" +name + SFI_ExtName;
		//PFS::CEnv::RemoveFile(uri);
		std::wstring nativename;
		if( PFS::CEnv::PFSFileNameToNativeFileName( uri, nativename) )
			::DeleteFile(nativename.c_str());
	}

	void Component::RenameAction(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname, bool renamexap) // 编辑器使用
	{
		std::map<std::wstring,XWeakRef<XAni> >::iterator it = m_anis.find(oldname);
		if( it != m_anis.end())
		{
			m_anis.erase(it);
		}

		if( renamexap )
		{
			SpriteManager *pSprMan = m_pEB->GetSpriteManager();
			if (!pSprMan)
				return;
			std::wstring olduri = L"/model/"+modelname+L"/"+pSprMan->GetLayerName(modelname, m_layer) + L"/" + m_name +L"/"
				+oldname + SFI_ExtName;
			std::wstring newuri = L"/model/"+modelname+L"/"+pSprMan->GetLayerName(modelname, m_layer) + L"/" + m_name +L"/"
				+newname + SFI_ExtName;
			std::wstring oldnativename;
			std::wstring newnativename;
			if( PFS::CEnv::PFSFileNameToNativeFileName( olduri, oldnativename) 
				&& PFS::CEnv::PFSFileNameToNativeFileName( newuri, newnativename)  )
			{
				::MoveFile(oldnativename.c_str(), newnativename.c_str());
			}
		}
	}

	//void Component::AddDyeInfo(int index, int h, int s, int l) // 编辑器使用
	//{
	//	for (size_t i = 0; i < m_DyeInfos.size(); ++i)
	//	{
	//		if (m_DyeInfos[i].projIndex == index)
	//		{
	//			m_DyeInfos[i].iH = h;
	//			m_DyeInfos[i].iS = s;
	//			m_DyeInfos[i].iL = l;
	//			return;
	//		}
	//	}
	//	m_DyeInfos.push_back(DyeInfo(index, h, s, l));
	//}

	//void Component::DelDyeInfo(int index) // 编辑器使用
	//{
	//	DyeInfoVec::iterator it = m_DyeInfos.begin();
	//	while (it != m_DyeInfos.end())
	//	{
	//		if ((*it).projIndex == index)
	//		{
	//			m_DyeInfos.erase(it);
	//			return;
	//		}
	//		++it;
	//	}
	//}

	//void Component::SetCurDyeIndex(int curIndex)
	//{
	//	m_CurDyeIndex = curIndex;
	//}

	//Component::DyeInfo* Component::GetDyeInfo(int index)
	//{
	//	Component::DyeInfoVec::iterator it = m_DyeInfos.begin();
	//	while (it != m_DyeInfos.end())
	//	{
	//		if ((*it).projIndex == index)
	//		{
	//			return &(*it);
	//		}
	//		++it;
	//	}

	//	return NULL;
	//}

	bool SpriteManager::AddModel(const std::wstring &newmodelname, const std::wstring &newmodeldes, const std::vector<Layer> &layers)
	{
		if( layers.empty() || m_models.find(newmodelname) != m_models.end() ) return false;
		XModel *pM = new XModel(m_pEB);
		pM->m_name = newmodelname;
		pM->m_des = newmodeldes;
		pM->SaveActions();

		pM->m_layers = layers;
		pM->m_Components.resize(layers.size());
		
		pM->SaveBase();
		pM->SaveDyeInfo();
		m_models[newmodelname] = pM;

		if (!pM->bLoadedAction)
		{
			pM->LoadActions();
		}
		if (!pM->bLoadedBase)
		{
			pM->LoadBase();
		}
		if (!pM->bLoadLayerConfig)
		{
			pM->LoadSoundParam();
		}

		return SaveModels();
	}

	void SpriteManager::AddCompon(const std::wstring &modelname, const std::wstring &componentname, int num)
	{
		if(m_models.find(modelname) == m_models.end() ) return;
		
		m_models[modelname]->m_Components.resize(1);
		
		//2015 Yao
		{
			m_models[modelname]->m_Components[0][componentname] = new Component(m_pEB, 0,componentname, L"");
		}
	}

	void SpriteManager::RemoveALayer(int layerIDMap[], int layerID, const std::wstring &modelname, const std::wstring &layername)	// 编辑器使用
	{
		layerIDMap[layerID] = -1;
		if (layerID+1<XPSC_MAXCOUNT)
		{
			for (int j=layerID+1;j<XPSC_MAXCOUNT;++j)
			{
				--layerIDMap[j];
			}
		}
		//删除文件夹
		//
		std::wstring nativename;
		if( PFS::CEnv::PFSFileNameToNativeFileName( L"/model/" + modelname + L"/" + layername, nativename) )
		{
			if(_waccess(nativename.c_str(), 0) != -1)  // 文件夹存在的话，再删除
			{
				if( !RemoveDirectory(nativename.c_str()))
				{
					std::wstring errNfo = L"删除目录\n" + nativename + L"\n失败，请手工删除";
					MessageBox(NULL,  errNfo.c_str(), L"提示", NULL);
				}
			}
		}
	}

	bool SpriteManager::ModifyModel(const std::wstring &modelname, const std::wstring &newmodeldes, const std::vector<Layer> &layers)	//编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;

		XModel* pModel = it->second;

		pModel->m_des = newmodeldes;

		int layerIDMap[XPSC_MAXCOUNT];
		int usedLayer[XPSC_MAXCOUNT];
		int tmpLayerOrder[XPSC_MAXCOUNT];
		int tmpNewLayerOrder[XPSC_MAXCOUNT];

		for (int i=0;i<XPSC_MAXCOUNT;++i)
		{
			layerIDMap[i] = i;
			usedLayer[i] = 0;
		}

		int oldLayerCount = static_cast<int>(pModel->m_layers.size());
		int newLayerCount = static_cast<int>(layers.size());
		
		for(int i = 0; i < oldLayerCount; ++i)
		{
			const Layer& oldLayer = pModel->m_layers[i];

			bool bDel = true;
			for(int j = 0; j < newLayerCount; ++j)
			{
				const Layer& newLayer = layers[j];
				if(oldLayer.name == newLayer.name)
				{
					bDel = false;
					break;
				}
			}

			if(bDel)
			{
				RemoveALayer(layerIDMap, i, modelname, oldLayer.name);
			}
		}

		int dir, frame, frameCount, tick, mask, newMask;
		Action* act;
		tick = 0;
		for(std::map<std::wstring, Action*>::const_iterator ait = pModel->m_actions.begin(), aie = pModel->m_actions.end(); ait != aie; ++ait)
		{
			act = ait->second;
			frameCount = act->GetFrameCount();
			for (dir=0;dir<XPDIR_COUNT;++dir)
			{
				if (!act->TestDir(dir))
					continue;
				for (frame=0;frame<frameCount;++frame)
				{
					act->GetFrameOrder(frame, dir, tmpLayerOrder, oldLayerCount);
					int j = 0;
					++tick;
					for (int i=0;i<oldLayerCount;++i)
					{
						if ((tmpNewLayerOrder[j] = layerIDMap[tmpLayerOrder[i]]) >= 0)	//这里的确是=，先赋值然后判断它是不是负数
						{
							usedLayer[tmpNewLayerOrder[j++]] = tick;
						}
					}
					for (int i=0;i<newLayerCount;++i)
					{
						if (usedLayer[i] < tick)
						{
							usedLayer[i] = tick;
							tmpNewLayerOrder[j++] = i;

							if(j >= newLayerCount || j >= XPSC_MAXCOUNT)
							{
								break;
							}
						}
					}
					act->SetFrameOrder(frame, dir, tmpNewLayerOrder, newLayerCount);
				}	
			}
			mask = act->GetEquipMask();

			newMask = 0;
			for(int i=0, j=0; i < oldLayerCount; ++i)
			{
				if (layerIDMap[i] >= 0)
				{
					if (mask & (1<<i)) 
					{
						newMask |= 1 << j;
					}
					++j;
				}
			}
			act->SetEquipMask(newMask);

			std::wstring fpath = L"/model/"+modelname+L"/action/" + ait->first + L".act";
			act->Save(fpath.c_str());
			/*
			XMLIO::CONode subnode;
			if ( !fw.CreateElement( node, L"action", subnode) )
			continue;
			subnode.SetAttribute( L"name", it->first);*/
		}

		pModel->m_layers = layers;
		pModel->m_Components.resize(layers.size());
		pModel->SaveBase();
		pModel->SaveDyeInfo();

		return SaveModels();
	}

	bool SpriteManager::DelModel(const std::wstring &modelname)
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		delete it->second;
		m_models.erase(it);

		std::wstring nativename;
		if( PFS::CEnv::PFSFileNameToNativeFileName( L"/model/" + modelname, nativename) )
		{
			if( !RemoveDirectory(nativename.c_str()))
			{
				MessageBox(NULL, L"删除目录失败，请手工删除", L"提示", NULL);
			}
		}
		//
		return SaveModels();
	}

	void SpriteManager::DumpModelNames(std::vector<std::wstring> &modellist) const
	{
		modellist.clear();
		for(ModelMap::const_iterator it = m_models.begin(), ie = m_models.end(); it != ie; ++it)
			modellist.push_back(it->first);
	}

	SpriteManager::XModel* SpriteManager::GetXModel(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return NULL;
		return it->second;
	}

	std::wstring SpriteManager::GetModelDes(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return L"";
		return it->second->m_des;
	}

	bool SpriteManager::XModel::GetLayerRideEnable(int i) const
	{
		if( i <0 || i >=(int)m_layers.size())
			return false;
		return m_layers[i].bRide;
	}

	bool SpriteManager::XModel::GetLayerEffectEnable(int i) const
	{
		if( i <0 || i >=(int)m_layers.size())
			return false;
		return m_layers[i].bEffect;
	}

	std::wstring SpriteManager::XModel::GetLayerName(int i) const 
	{ 
		if( i <0 || i >=(int)m_layers.size())
			return L"unknown";
		return m_layers[i].name; 
	}

	std::wstring SpriteManager::XModel::GetLayerDes(int i) const 
	{ 
		if( i <0 || i >=(int)m_layers.size())
			return L"未知";
		return m_layers[i].des; 
	}

	void SpriteManager::XModel::DumpComponent(int ectype, std::vector<std::wstring> &equiplist) const // 编辑器使用
	{
		assert(ectype >= 0 && ectype < GetLayerCount());
		std::map<std::wstring, Component*>::const_iterator it = m_Components[ectype].begin(), ie = m_Components[ectype].end();
		for(;it != ie; ++it)
		{
			equiplist.push_back(it->first);
		}
	}


	void SpriteManager::XModel::AddDyePart() // 编辑器使用
	{
		PartInfo info;
		m_DyeInfos.push_back(info);
	}
	void SpriteManager::XModel::DelDyePart(int part) // 编辑器使用
	{
		if(part < 0 || part >= (int)m_DyeInfos.size())	return;
		std::vector<PartInfo>::iterator it = m_DyeInfos.begin() + part;
		m_DyeInfos.erase(it);
	}
	void SpriteManager::XModel::AddDyeProj(int part) // 编辑器使用
	{
		if(part < 0 || part >= (int)m_DyeInfos.size())	return;
		PartInfo& info = m_DyeInfos[part];
		DyeInfo dinfo;
		info.push_back(dinfo);
	}
	void SpriteManager::XModel::DelDyeProj(int part,int proj) // 编辑器使用
	{
		if(part < 0 || part >= (int)m_DyeInfos.size())	return;
		PartInfo& info = m_DyeInfos[part];
		if(proj < 0 || proj >= (int)info.size())	return;
		PartInfo::iterator it = info.begin() + proj;
		info.erase(it);
	}
	SpriteManager::XModel::DyeInfo* SpriteManager::XModel::GetDyeInfo(int part,int proj)
	{
		if(part < 0 || part >= (int)m_DyeInfos.size())	return NULL;
		PartInfo& info = m_DyeInfos[part];
		if(proj < 0 || proj >= (int)info.size())	return NULL;
		return &info[proj];
	}
	int SpriteManager::XModel::getDyePartCount()	// 编辑器使用
	{
		return m_DyeInfos.size();
	}
	int SpriteManager::XModel::getDyeProjCount(int part)// 编辑器使用
	{
		if( part < 0 || part >= (int)m_DyeInfos.size())	return 0;
		PartInfo info = m_DyeInfos[part];
		return info.size();
	}

	Component::~Component()
	{
		LoadingUrls::iterator it = m_loadingUrls.begin(), ie = m_loadingUrls.end();
		AniManager *pAniMan = m_pEB->GetAniManager();
		for (;it!=ie;++it)
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
			XWeakRef<XAni> &weakani = m_anis[acts.first];
			XHardRef<XAni> hardref = weakani;
			if (!hardref.IsValid())
			{
				weakani = pAniMan->GetAni(filename, true);
				hardref = weakani;
			}
			hardref.SetTick(m_pEB->GetTick());
			ActionNameSet::iterator aIt = acts.second.begin(), aIe = acts.second.end();
			ComponentAni::iterator cIt, cIe = m_holdAni.end();
			for (;aIt!=aIe;++aIt)
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
			XWeakRef<XAni> &weakani = m_anis[acts.first];
			XHardRef<XAni> hardref = weakani;
			if (!hardref.IsValid())
			{
				weakani = pAniMan->GetAni(filename, true);
			}
		}
	}

	// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
	XP_PREFETCH_RESULT Component::PrefetchAction(const std::wstring &modelname, const std::wstring &action_name, const ActionNameSet &linked_action_names)
	{
		XP_PREFETCH_RESULT result = XPPR_OK;
		ActionNameSet::const_iterator it = linked_action_names.begin(), ie = linked_action_names.end();
	
		for (;it!=ie;++it)
		{
			XWeakRef<XAni> &weakani = m_anis[*it];
			XHardRef<XAni> hardref = weakani;
			if ((!hardref.IsValid()) || hardref->GetState() == XAni::XPANISTATE_NOPICTURE)
			{
				//set horse resource path specially. by liugeng
				std::wstring uri = L"";
				if (m_pEB->GetSpriteManager()->GetLayerRideEnable(modelname, m_layer))
				{
					uri = L"/model/mt_zuoqi/" + m_pEB->GetSpriteManager()->GetLayerName(modelname, m_layer)
						+ L"/" + (m_linkname.empty() ? m_name : m_linkname) + L"/" + *it + SFI_ExtName;
				}
				else
				{
					uri = L"/model/" + modelname + L"/" + m_pEB->GetSpriteManager()->GetLayerName(modelname, m_layer)
						+ L"/" + (m_linkname.empty() ? m_name : m_linkname) + L"/" + *it + SFI_ExtName;
				}
				switch (m_pEB->GetAniManager()->AsyncLoadAni(uri, this))
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
					weakani = m_pEB->GetAniManager()->GetAni(uri, true);
					break;
				}
				result = XPPR_ASYNC_LOADING;
			} else {
				hardref.SetTick(m_pEB->GetTick());
				if (result < XPPR_BASE_ASYNC_OK && hardref->GetState() == XAni::XPANISTATE_BASE_PIC)
					result = XPPR_BASE_ASYNC_OK;
			}
		}
		return result;
	}

	// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
	void Component::HoldAction(const std::wstring &modelname, const std::wstring &action_name, const ActionNameSet &linked_action_names)
	{
		AniInfo &info = m_holdAni[action_name];
		++info.first;
		if (PrefetchAction(modelname, action_name, linked_action_names) == XPPR_OK)
		{
			if (info.first == 1)//第一次Hold
			{
				ActionNameSet::const_iterator it = linked_action_names.begin(), ie = linked_action_names.end();
				for (;it!=ie;++it)
				{
					XHardRef<XAni> hardref = m_anis[*it];
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

	XWeakRef<XAni> Component::GetAni(const std::wstring &modelname, const std::wstring &actname, bool async)
	{
		XWeakRef<XAni> &weakani = m_anis[actname];
		XHardRef<XAni> hardref = weakani;
		if (!hardref.IsValid() || hardref->GetState() == XAni::XPANISTATE_NO_XAP)
		{
			//set horse resource path specially. by liugeng
			std::wstring uri = L"";
			if (m_pEB->GetSpriteManager()->GetLayerRideEnable(modelname, m_layer))
			{
				uri = L"/model/mt_zuoqi/" + m_pEB->GetSpriteManager()->GetLayerName(modelname, m_layer)
					+ L"/" + (m_linkname.empty() ? m_name : m_linkname) + L"/" + actname + SFI_ExtName;
			}
			else
			{
				uri = L"/model/" + modelname + L"/" + m_pEB->GetSpriteManager()->GetLayerName(modelname, m_layer)
					+ L"/" + (m_linkname.empty() ? m_name : m_linkname) + L"/" + actname + SFI_ExtName;
			}
			weakani = m_pEB->GetAniManager()->GetAni(uri, async);
		}
		return weakani;
	}

	const Action* SpriteManager::GetAction(const std::wstring &modelname, const std::wstring &actName) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return NULL;
		return it->second->GetAction(actName);
	}

	void SpriteManager::SetAction(const std::wstring &modelname, const std::wstring &actName, const Action& act) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->SetAction(actName,act);
	}

	bool SpriteManager::SaveActions(const std::wstring &modelname) const // 编辑器使用
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->SaveActions();
	}

	bool SpriteManager::AddAction(const std::wstring &modelname, const std::wstring &name, const Action &act) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->AddAction(name,act);
	}

	bool SpriteManager::DeleteAction(const std::wstring &modelname, const std::wstring &name) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->DeleteAction(name);
	}

	bool SpriteManager::RenameAction(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->RenameAction(oldname,newname);
	}

	void SpriteManager::DumpAction(const std::wstring &modelname, std::vector<std::wstring> &actlist) const // 编辑器使用 
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->DumpAction(actlist);
	}

	void SpriteManager::DumpActionRefMap(const std::wstring &modelname, std::map<std::wstring,std::wstring> &actrefmap) const // 编辑器使用
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->DumpActionRefMap(actrefmap);
	}

	void SpriteManager::SetActionRefMap(const std::wstring &modelname, const std::map<std::wstring,std::wstring> &actrefmap) // 编辑器使用
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->SetActionRefMap(actrefmap);
	}

	void SpriteManager::DumpBaseAction(const std::wstring &modelname, std::vector<std::pair<std::wstring,int> > &baseactlist) const // 编辑器使用
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->DumpBaseAction(baseactlist);
	}

	bool SpriteManager::SaveBase(const std::wstring &modelname) //  编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->SaveBase();
	}

	bool SpriteManager::SaveSoundParam(const std::wstring &modelname) //  编辑器使用
	{

		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->SaveSoundParam();
	}

	void SpriteManager::DeleteAction2(const std::wstring &modelname, const std::wstring &name) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->DeleteAction2(name);
	}

	void SpriteManager::RenameAction2(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname, bool renamexap) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->RenameAction2(oldname, newname, renamexap);
	}

	bool SpriteManager::AddComponent(const std::wstring &modelname, const Component &equip) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->AddComponent(equip);
	}

	bool SpriteManager::DeleteComponent(const std::wstring &modelname, const Component &equip) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->DeleteComponent(equip);
	}

	Component* SpriteManager::GetComponent(const std::wstring &modelname, int ectype, const std::wstring &name)
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return NULL;
		return it->second->GetComponent(ectype,name);
	}

	void SpriteManager::DumpComponent(const std::wstring &modelname, int ectype, std::vector<std::wstring> &equiplist) const // 编辑器使用 
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->DumpComponent(ectype, equiplist);
	}

	void SpriteManager::RenameComponent(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname) // 编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;
		it->second->RenameComponent(oldname, newname);
	}

	ModelSoundParam& SpriteManager::GetSoundParam(const std::wstring &modelname)
	{
		static ModelSoundParam param;
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) 
			return param;
		return it->second->m_soundParam;
	}

	const ModelSoundParam& SpriteManager::GetSoundParam(const std::wstring &modelname) const
	{
		static ModelSoundParam param;
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) 
			return param;
		return it->second->m_soundParam;
	}

	int SpriteManager::GetLayerCount(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return 0;

		wchar_t workdir[MAX_PATH];
		::GetCurrentDirectory(MAX_PATH, workdir);
		std::wstring wdir = workdir;
		int npos = (int)wdir.rfind('\\');
		wdir = std::wstring(wdir, 0, npos);
		std::wstring sysFilePath = _T("\\res");
		std::wstring strTemp = wdir + sysFilePath;
		it->second->m_ModelPath = strTemp + L"\\model\\"+ modelname;

		return it->second->GetLayerCount();
	}

	int SpriteManager::GetDyeEnable(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return 0;

		return it->second->m_ndyeEnable;
	}

	void SpriteManager::SetDyeEnable(const std::wstring &modelname,int i)
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;

		it->second->m_ndyeEnable = i;
	}

	void SpriteManager::SaveDyeInfo(const std::wstring &modelname)
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;

		it->second->SaveDyeInfo();
	}
	void SpriteManager::LoadDyeInfo(const std::wstring &modelname)
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return;

		it->second->LoadDyeInfo();
	}

	int SpriteManager::GetTitlePos(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return 0;
		return it->second->GetTitlePos();
	}

	const CPOINT& SpriteManager::GetEffectPos(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) 
		{
			static CPOINT tmpresult(0, 0);
			return tmpresult;
		}
		return it->second->GetEffectPos();
	}

	const XPBase& SpriteManager::GetBase(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) 
		{
			static XPBase tmpresult;
			return tmpresult;
		}
		return it->second->GetBase();
	}
	WATER_SURFACE_TYPE SpriteManager::GetWaterSurfaceType(const std::wstring &modelname) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return WST_ALWAYS_PLANE;
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


	bool SpriteManager::SetTitlePos(const std::wstring &modelname, int pos) //  编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		it->second->SetTitlePos(pos);
		SaveModels();
		return true;
	}

	bool SpriteManager::SetEffectPos(const std::wstring &modelname, const CPOINT &pos) //  编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		it->second->SetEffectPos(pos);
		SaveModels();
		return true;
	}

	bool SpriteManager::SetBase(const std::wstring &modelname, const XPBase &base, bool savefile)
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		if (savefile)
		{
			XPBase tmpBase = base;
			CPOINT basept[4];
			const float tga = 1.60033f; // tan58
			basept[0] = base.left;
			basept[2] = base.right;
			basept[1].x = (base.left.x+base.right.x)/2 + (LONG)(tga * (base.left.y-base.right.y)/2);
			basept[1].y = (base.left.y+base.right.y)/2 + (LONG)((base.left.x-base.right.x)/(2*tga));
			basept[3].x = (base.left.x+base.right.x)/2 + (LONG)(-tga * (base.left.y-base.right.y)/2);
			basept[3].y = (base.left.y+base.right.y)/2 + (LONG)((base.left.x-base.right.x)/(2*-tga));
			int i;
			for (i=0;i<4;++i)
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
		} else {
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

	bool SpriteManager::SetWaterSurfaceType(const std::wstring &modelname, WATER_SURFACE_TYPE type) //  编辑器使用
	{
		ModelMap::iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		it->second->SetSurfaceType(type);
		SaveModels();
		return true;
	}

	bool SpriteManager::GetLayerRideEnable(const std::wstring &modelname, int i) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->GetLayerRideEnable(i);
	}

	bool SpriteManager::GetLayerEffectEnable(const std::wstring &modelname, int i) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return false;
		return it->second->GetLayerEffectEnable(i);
	}

	std::wstring SpriteManager::GetLayerName(const std::wstring &modelname, int i) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return L"unknown";
		return it->second->GetLayerName(i);
	}

	std::wstring SpriteManager::GetLayerDes(const std::wstring &modelname, int i) const
	{
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return L"未知";
		return it->second->GetLayerDes(i);
	}

	const std::wstring& SpriteManager::GetActionRef(const std::wstring &modelname, const std::wstring &actname) const
	{
		static std::wstring nilstr = L"";
		ModelMap::const_iterator it = m_models.find(modelname);
		if( it == m_models.end() ) return nilstr;
		return it->second->GetActionRef(actname);
	}

	bool ActionType::Load()
	{
		XMLIO::CFileReader fr;
		std::wstring xmlpath = std::wstring(L"/model/actiontype") + ImageSet_ExtName;
		XBuffer xmlfiledata;
		if( !m_pFileIOMan->GetFileImage(xmlpath, xmlfiledata))
			return false;

		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
		{
			fr.CloseFile();
			return false;
		}

		XMLIO::CINode	root;
		if (!fr.GetRootNode(root))
		{
			fr.CloseFile();
			return false;
		}


		XMLIO::CNodeList typenl;
		root.GetChildren(typenl);

		unsigned char id;
		for( XMLIO::CNodeList::iterator typeIt = typenl.begin(); typeIt != typenl.end(); ++typeIt)
		{
			XMLIO::CNodeList itemNl;
			XMLIO::CINode& typeNode = *typeIt;
			if (typeNode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			id = static_cast<unsigned char>(typeNode.GetAttributeInteger(L"id"));
			XPASSERT(id > 0);
			XPASSERT(id <= 16);
			std::wstring des;
			if (typeNode.GetAttribute(L"des", des))
			{
				m_actionTypeDes[id] = des;
			}
			typeNode.GetChildren(itemNl);
			for (XMLIO::CNodeList::iterator itemIt = itemNl.begin(); itemIt != itemNl.end(); ++itemIt)
			{
				XMLIO::CINode& itemNode = *itemIt;
				std::wstring actName;
				if (itemNode.GetAttribute(L"name", actName))
				{
					m_actionTypeMap[actName] |= 1 << (id - 1);
				}
			}
		}
		fr.CloseFile();
		return true;
	}

	bool HorseHeight::Load(CFileIOManager* pFileIOMan, const std::wstring &modelname)
	{
		XMLIO::CFileReader fr;
		
		std::wstring xmlpath = L"/model/mt_zuoqi/horseheight.lmx"; //change save path. by liugeng
		XBuffer xmlfiledata;
		if( !pFileIOMan->GetFileImage(xmlpath, xmlfiledata))
			return false;

		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
		{
			fr.CloseFile();
			return false;
		}

		XMLIO::CINode	root;
		if (!fr.GetRootNode(root))
		{
			fr.CloseFile();
			return false;
		}


		XMLIO::CNodeList horsenl;
		root.GetChildren(horsenl);

		int height;
		for( XMLIO::CNodeList::iterator horseIt = horsenl.begin(); horseIt != horsenl.end(); ++horseIt)
		{
			XMLIO::CNodeList itemNl;
			XMLIO::CINode& horseNode = *horseIt;
			if (horseNode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			height = horseNode.GetAttributeInteger(L"height");

			std::wstring name;
			if (horseNode.GetAttribute(L"name", name))
			{
				m_map[name] = height;
			}
		}
		fr.CloseFile();
		return true;
	}

	bool HorseHeight::Save(CFileIOManager* pFileIOMan, const std::wstring &modelname) const
	{
		XMLIO::CFileWriter fw;
		XMLIO::CONode node;
		fw.GetRootNode(node);
		for (HorseHeightMap::const_iterator it = m_map.begin(), ie = m_map.end(); it != ie; ++it)
		{
			XMLIO::CONode subNode;
			if (fw.CreateElement(node, L"horse", subNode))
			{
				subNode.SetAttribute( L"name", it->first );
				subNode.SetAttribute( L"height", it->second );
			}
		}

		int retcode = fw.Save( L"/model/mt_zuoqi/horseheight.lmx"); //change save path. by liugeng
		return retcode == XMLIO::EC_SUCCESS;
	}


}