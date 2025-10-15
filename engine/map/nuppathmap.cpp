#include "../common/nuxml.h"
#include "nuppathmap.h"
#include "utils/StringUtil.h"
#include "utils/Utils.h"
#include <platform/platform_types.h>

#include "../../dependencies/LJXML//Include/LJXML.hpp"

namespace Nuclear
{
	namespace SubMap
	{
		PPathMap::PPathMap(NuclearFileIOManager* pFileIOMan): m_pFileIOMan(pFileIOMan), m_bChanged(false)
		{
		}

		PPathMap::~PPathMap(void)
		{
		}

		void PPathMap::Insert(eTILE_SIZE_TYPE type, int key, const std::wstring &value)
		{
			std::map<int, std::wstring>::iterator iter;
			iter = m_Paths[type].find(key);
			if (iter == m_Paths[type].end())
			{
				m_Paths[type][key] = value;
				m_bChanged = true;
			} else if (iter->second != value) {
				iter->second = value;
				m_bChanged = true;
			}
		}

		bool PPathMap::Load()
		{
			std::wstring xmlpath = L"/map/tiles/pro.dat";
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

			int key = 0;
			eTILE_SIZE_TYPE type = TST_SMALL;

			LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
			LJXML::LJXML_NodeList typenl;
			root->GetSubNodeList(typenl);
			for (int i = 0; i < typenl.size(); i++)
			{
				if (std::wstring(typenl[i]->name()).compare(L"small") == 0)
					type = TST_SMALL;
				else if (std::wstring(typenl[i]->name()).compare(L"middle") == 0)
					type = TST_MIDDLE;
				else if (std::wstring(typenl[i]->name()).compare(L"big") == 0)
					type = TST_BIG;

				LJXML::LJXML_NodeList itemnl;
				typenl[i]->GetSubNodeList(itemnl);
				for (int j = 0; j < itemnl.size(); j++)
				{
					if (std::wstring(itemnl[j]->name()).compare(L"item") != 0) continue;
					key = _wtoi(itemnl[j]->first_attribute(L"key")->value()); //itemNode.GetAttributeInteger(L"key");
					std::wstring str = itemnl[j]->first_attribute(L"name")->value();
					if(str.empty() ) continue;
					m_Paths[type][key] = str;
				}
			}

			return true;
		}

		bool PPathMap::Save() const
		{
            assert(false);
            return false;
		}

		std::wstring PPathMap::GetTileFileName(eTILE_SIZE_TYPE type, SubMap::uBiggerTileType tileData) const
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
            assert(false);
//			_itot_s(tileData.tileID, temp, 20, 10);
            
			filename = temp;
			std::map<int, std::wstring>::const_iterator iter = m_Paths[type].find(key);
			if (iter != m_Paths[type].end())
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
			key = SubMap::NuclearGround::GetGroundType(tileData);
			path = L"/map/tiles/smalltile/";
			memset(temp, 0, 20);
            assert(false);
//			_itot_s(SubMap::NuclearGround::GetTileType(tileData), temp, 20, 10);
			filename = temp;
			filename = filename + L"-";
			memset(temp, 0, 20);
            assert(false);
//			_itot_s(SubMap::NuclearGround::GetTileNumber(tileData), temp, 20, 10);
			filename = filename + temp;

			std::map<int, std::wstring>::const_iterator iter = m_Paths[TST_SMALL].find(key);
			if (iter != m_Paths[TST_SMALL].end())
			{
				path = path + iter->second.c_str() + L"/";
				path = path + filename + TILE_TYPE;
				return path;
			}
			return L"";

		}
	}
}