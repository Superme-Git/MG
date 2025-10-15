#include "StdAfx.h"
#include "GlobalInfo.h"
#include "Resource.h"
#include <vector>
#include <sstream>
#include "../../pfs2.h"
#include "../../translate.h"

// TODO 先用原来的
const int CGlobalInfo::ZipID = PFSX::MakeTypeIDByName( 'Z', 'I', 'P');
const int CGlobalInfo::X2OID = PFSX::MakeTypeIDByName( 'X', '2', 'O');
const int CGlobalInfo::ZpdID = PFSX::MakeTypeIDByName( 'Z', 'P', 'D');
const int CGlobalInfo::BzfID = PFSX::MakeTypeIDByName( 'B', 'Z', 'F');
const int CGlobalInfo::NoDecode = 0;
const int CGlobalInfo::DecodeCount = 4;

CGlobalInfo::CGlobalInfo(void)
{
	Create();
}

CGlobalInfo::~CGlobalInfo(void)
{
}

bool CGlobalInfo::Create()
{
	m_lstSetupIcon.Create( IDB_BITMAP2, 16, 4, RGB(255,255,255));
	m_lstZipIcon.Create( IDB_BITMAP1, 16, 4, RGB(255,255,255));
	m_lstCheckIcon.Create( IDB_BITMAP3, 16, 4, RGB(255,255,255));

	// 添加默认压缩方式
	CAttributeDesc::CAttributeDescMap& mapZip = GetZipDesc().GetMap();
	mapZip[NO] = CAttributeDesc::CAttributeDescItem( -1, L"Do Not Compress");
	mapZip[RADIO1] = CAttributeDesc::CAttributeDescItem( 0, L"Zip Level 1");
	mapZip[RADIO2] = CAttributeDesc::CAttributeDescItem( 1, L"Zip Level 2");
	mapZip[RADIO3] = CAttributeDesc::CAttributeDescItem( 2, L"Zip Level 3");
	mapZip[RADIO4] = CAttributeDesc::CAttributeDescItem( 3, L"Zip Level 4");
	mapZip[RADIO5] = CAttributeDesc::CAttributeDescItem( 4, L"Zip Level 5");
	mapZip[RADIO6] = CAttributeDesc::CAttributeDescItem( 5, L"Zip Level 6");
	mapZip[RADIO7] = CAttributeDesc::CAttributeDescItem( 6, L"Zip Level 7");
	mapZip[RADIO8] = CAttributeDesc::CAttributeDescItem( 7, L"Zip Level 8");
	mapZip[RADIO9] = CAttributeDesc::CAttributeDescItem( 8, L"Zip Level 9");

	// mapSetup 保存每一种解码器的icon索引和描述信息
	CAttributeDesc::CAttributeDescMap& mapSetup = GetSetupDesc().GetMap();
	mapSetup[ZipID] = CAttributeDesc::CAttributeDescItem( 2, L"Encode to Zip");
	mapSetup[X2OID] = CAttributeDesc::CAttributeDescItem( 5, L"Encode to OctetsXML");
	mapSetup[ZpdID] = CAttributeDesc::CAttributeDescItem( 1, L"Encode to zipped");
	mapSetup[BzfID] = CAttributeDesc::CAttributeDescItem( 8, L"Encode to BZF");
	mapSetup[NoDecode] = CAttributeDesc::CAttributeDescItem( -1, L"Do Not Encode");

	return true;
}

// Trim
void TrimStr(std::wstring& s, const wchar_t cTrim=L' ')
{
	s.erase(0, s.find_first_not_of(cTrim));
	s.erase(s.find_last_not_of(cTrim)+1);
}

// 从sSrc中得到以cDelimiter分界的子字符串
bool GetSubStrFromStr(std::vector<std::wstring>& vecChild, const std::wstring& sSrc, const wchar_t cDelimiter=L',')
{
	std::wstringstream ss(sSrc);
	while (true)
	{
		std::wstring token;
		std::getline(ss, token, cDelimiter);
		if (ss.fail())
			break;
		TrimStr(token);
		vecChild.push_back(token);
	}
	return true;
}

int CFileIcon::GetFileIconIndex( const std::wstring& sFile)
{
	std::wstring sExt = CGlobalInfo::GetFileExt( sFile);
	CIconIndexMap::iterator itor = m_mapIcon.find( sExt);
	if( itor != m_mapIcon.end())
		return itor->second;

	SHFILEINFO sfi;
	HIMAGELIST hil;

	ZeroMemory(&sfi,sizeof(sfi));
	hil = (HIMAGELIST)SHGetFileInfo( sFile.c_str(), FILE_ATTRIBUTE_NORMAL, &sfi, sizeof(sfi),
		SHGFI_USEFILEATTRIBUTES|SHGFI_ICON|SHGFI_SMALLICON);
	if (sfi.hIcon)
	{
		int nIndex = m_lstIcon.Add( sfi.hIcon);
		m_mapIcon.insert( std::make_pair( sExt, nIndex));
		return nIndex;
	}
	return -1;
}