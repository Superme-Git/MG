#include "stdafx.h"
#include "TranslateCommon.h"

int g_TranslateId[] = {
PFSX::MakeTypeIDByName( 'J', 'F', 'I', 'F'),
PFSX::MakeTypeIDByName( 'T', 'G', 'A' ),
PFSX::MakeTypeIDByName( 'P', '0', '0'),
PFSX::MakeTypeIDByName( 'P', '0', '1'),
PFSX::MakeTypeIDByName( 'P', '0', '2'),
PFSX::MakeTypeIDByName( 'P', '0', '3'),
PFSX::MakeTypeIDByName( 'P', '0', '4'),
PFSX::MakeTypeIDByName( 'P', '0', '6'),
PFSX::MakeTypeIDByName( 'B', '0', '0'),
PFSX::MakeTypeIDByName( 'B', '0', '1'),
PFSX::MakeTypeIDByName( 'B', '0', '2'),
PFSX::MakeTypeIDByName( 'B', '0', '3'),
PFSX::MakeTypeIDByName( 'B', '0', '4'),
PFSX::MakeTypeIDByName( 'B', '0', '6'),
0,
PFSX::MakeTypeIDByName( 'Z', 'I', 'P'),
PFSX::MakeTypeIDByName( 'X', '2', 'O'),
};

bool CTranslateCommon::DoAllPictures( const std::wstring& filename, PFSX::TSID decoderId)
{
	for( int i=0; i<=BMP; i++)
	{
		if( g_TranslateId[i] == decoderId)
			continue;
		if( !Do( filename, g_TranslateId[i], decoderId))
			return false;
	}
	return true;
}