#include "stdafx.h"
#include "..\common\xml.h"
#include "ppathmap.h"

namespace Nuclear
{
	namespace SubMap
	{
		PPathMap::PPathMap(CFileIOManager* pFileIOMan): m_pFileIOMan(pFileIOMan), m_bChanged(false)
		{
		}

		PPathMap::~PPathMap(void)
		{
		}

		void PPathMap::Insert(TILE_SIZE_TYPE type, int key, const std::wstring &value)
		{
			std::map<int, std::wstring>::iterator iter;
			iter = m_mapPaths[type].find(key);
			if (iter == m_mapPaths[type].end())
			{
				m_mapPaths[type][key] = value;
				m_bChanged = true;
			} else if (iter->second != value) {
				iter->second = value;
				m_bChanged = true;
			}
		}

		bool PPathMap::Load()
		{
			XMLIO::CFileReader fr;
			std::wstring xmlpath = L"/map/tiles/pro.dat";
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

			int key;
			TILE_SIZE_TYPE type;
			for( XMLIO::CNodeList::iterator typeIt = typenl.begin(); typeIt != typenl.end(); ++typeIt)
			{
				XMLIO::CNodeList itemNl;
				XMLIO::CINode& typeNode = *typeIt;
				if (typeNode.GetName() == L"small")
					type = TST_SMALL;
				else if (typeNode.GetName() == L"middle")
					type = TST_MIDDLE;
				else if (typeNode.GetName() == L"big")
					type = TST_BIG;
				typeNode.GetChildren(itemNl);
				for (XMLIO::CNodeList::iterator itemIt = itemNl.begin(); itemIt != itemNl.end(); ++itemIt)
				{
					XMLIO::CINode& itemNode = *itemIt;
					if (L"item" != itemNode.GetName()) continue;
					key = itemNode.GetAttributeInteger(L"key");
					std::wstring str;
					if( (!itemNode.GetAttribute(L"name", str)) || str.empty() ) continue;
					m_mapPaths[type][key] = str;
				}
				
			}
			fr.CloseFile();
			return true;
		}

		bool PPathMap::Save() const
		{
			if (!m_bChanged)
				return true;

			wchar_t KeyStrs[3][10] = {L"small", L"middle", L"big"};
			XMLIO::CFileWriter fw;
			XMLIO::CONode root;
			fw.GetRootNode(root);
			std::map<int, std::wstring>::const_iterator it, ie;
			for (int i=0;i<TST_COUNT;++i)
			{
				XMLIO::CONode node;
				fw.CreateElement(root, KeyStrs[i], node);
				for(it=m_mapPaths[i].begin(), ie = m_mapPaths[i].end();it!=ie;++it)
				{
					XMLIO::CONode subnode;
					fw.CreateElement(node, L"item", subnode);
					subnode.SetAttribute(L"key", it->first);
					subnode.SetAttribute(L"name", it->second);
				}
			}
			int retcode = fw.Save(L"/map/tiles/pro.dat");
			return retcode == XMLIO::EC_SUCCESS;
		}

		std::wstring PPathMap::GetTileFileName(TILE_SIZE_TYPE type, SubMap::BiggerTileType tileData) const
		{
			int key;
			std::wstring path, filename;
			switch (type)
			{
			case TST_MIDDLE:
				path = L"/map/tiles/midtile/";
				break;
			case TST_BIG:
				path = L"/map/tiles/bigtile/";
				break;
			case TST_SMALL:
			default:
				return L"";
				break;
			}
			wchar_t temp[20];
			key = tileData.tileType;
			memset(temp, 0, 20);
			_itot_s(tileData.tileID, temp, 20, 10);
			filename = temp;
			std::map<int, std::wstring>::const_iterator iter = m_mapPaths[type].find(key);
			if (iter != m_mapPaths[type].end())
			{
				path = path + iter->second.c_str() + L"/";
				path = path + filename + TILE_TYPE;
				return path;
			}
			return L"";
		}

		std::wstring PPathMap::GetTileFileName(TileType tileData) const
		{
			int key;
			std::wstring path, filename;
			wchar_t temp[20];
			key = SubMap::PGround::GetGroundType(tileData);
			path = L"/map/tiles/smalltile/";
			memset(temp, 0, 20);
			_itot_s(SubMap::PGround::GetTileType(tileData), temp, 20, 10);
			filename = temp;
			filename = filename + L"-";
			memset(temp, 0, 20);
			_itot_s(SubMap::PGround::GetTileNumber(tileData), temp, 20, 10);
			filename = filename + temp;

			std::map<int, std::wstring>::const_iterator iter = m_mapPaths[TST_SMALL].find(key);
			if (iter != m_mapPaths[TST_SMALL].end())
			{
				path = path + iter->second.c_str() + L"/";
				path = path + filename + TILE_TYPE;
				return path;
			}
			return L"";

		}
	}
}