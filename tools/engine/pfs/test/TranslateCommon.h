#pragma once
#include <assert.h>
#include <list>
#include <string>
#include <map>
#include "../pfs.h"
#include "../translate.h"

extern int g_TranslateId[];

class CTranslateCommon
{
public:

	enum
	{
		JPG = 0,
		TGA = 1,
		P00 = 2,
		P01 = 3,
		P02 = 4,
		P03 = 5,
		P04 = 6,
		P06 = 7,
		B00 = 8,
		B01 = 9,
		B02 = 10,
		B03 = 11,
		B04 = 12,
		B06 = 13,
		BMP = 14,			// ÎÞ encoder
		ZIP = 15,
		X20 = 16,
		COUNT = 17,
	};

	static bool Do( const std::wstring& filename, PFSX::TSID encoderId, PFSX::TSID decoderId)
	{
		CTranslateCommon translate( filename, encoderId, decoderId);
		return translate.Translate();
	}

	static bool DoAllPictures( const std::wstring& filename, PFSX::TSID decoderId);

private:
	const std::wstring&		m_filename;
	PFSX::TSID				m_encoderId;
	PFSX::TSID				m_decoderId;

	CTranslateCommon( const std::wstring& filename, PFSX::TSID encoderId, PFSX::TSID decoderId)
		: m_filename( filename), m_encoderId( encoderId), m_decoderId( decoderId) {}

	bool GetExtByTSID( PFSX::TSID tsid, std::wstring& extname)
	{
		if( g_TranslateId[JPG] == tsid)
			extname = L"jpg";
		else if( g_TranslateId[TGA] == tsid)
			extname = L"tga";
		else if( g_TranslateId[P00] == tsid)
			extname = L"p00";
		else if( g_TranslateId[P01] == tsid)
			extname = L"p01";
		else if( g_TranslateId[P02] == tsid)
			extname = L"p02";
		else if( g_TranslateId[P03] == tsid)
			extname = L"p03";
		else if( g_TranslateId[P04] == tsid)
			extname = L"p04";
		else if( g_TranslateId[P06] == tsid)
			extname = L"p06";
		else if( g_TranslateId[B00] == tsid)
			extname = L"b00";
		else if( g_TranslateId[B01] == tsid)
			extname = L"b01";
		else if( g_TranslateId[B02] == tsid)
			extname = L"b02";
		else if( g_TranslateId[B03] == tsid)
			extname = L"b03";
		else if( g_TranslateId[B04] == tsid)
			extname = L"b04";
		else if( g_TranslateId[B06] == tsid)
			extname = L"b06";
		else if( g_TranslateId[ZIP] == tsid)
			extname = L"zip";
		else if( g_TranslateId[X20] == tsid)
			extname = L"x20";
		else
			extname = L"tmp";
		return true;
	}

	bool Translate()
	{
		PFS::FILEPFSXATTRIB attribute = PFSX::MakeFilePfsxAttributes( m_encoderId, m_decoderId );
		PFS::CMetaInfo metainfo;
		metainfo.SetFilePfsxAttributes( attribute );
		std::wstring src = L"/src" + m_filename;
		PFS::CEnv::AddFileMetaInfo( src, metainfo);

		std::wstring extname;
		GetExtByTSID( m_encoderId, extname);
		std::wstring temp = L"/des" + m_filename + L"." + extname;
		PFS::CEnv::AddFileMetaInfo( temp, metainfo);

		if( !PFSX::TranslateFile( src, temp))
			return false;

		// »¹Ô­
		attribute = PFSX::MakeFilePfsxAttributes( m_decoderId, m_encoderId);
		metainfo.SetFilePfsxAttributes( attribute );
		PFS::CEnv::SetFileMetaInfo( temp, metainfo);

		GetExtByTSID( m_decoderId, extname);
		std::wstring des = temp + L"." + extname;
		if( !PFSX::TranslateFile( temp, des))
			return false;

		return true;
	}

};