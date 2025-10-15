#pragma once
#include "../../src/common.h"

#if _MSC_VER == 1310
#define _itow_s( n, z, s, d) _itow( n, z, d)
#endif

namespace XMLIO
{
	class CINode;
	class CONode;
}

namespace XMLMETAFILE {

static const std::wstring TABLE = L"0123456789abcdef";
static const std::wstring FILENAME = L"N";
static const std::wstring FILESIZE = L"S";
static const std::wstring METHOD = L"M";
static const std::wstring ATTRIBUTES = L"A";
static const std::wstring DOWNLOADURL = L"U";
static const std::wstring FILEHASH = L"H";

using PFS::fsize_t;

class CTransfer
{
	CTransfer() {}
	~CTransfer() {}

public:
	static wchar_t HexToInt( wchar_t hexChar )
	{
		if( hexChar >= L'0' && hexChar <= L'9' )
		{
			return (wchar_t)(hexChar - L'0');
		}
		else if( hexChar >= L'a' && hexChar <= L'f' )
		{
			return (wchar_t)(hexChar - L'a' + 10 );
		}
		else if( hexChar >= L'A' && hexChar <= L'F' )
		{
			return (wchar_t)(hexChar - L'A' + 10 );
		}
		else
		{
			return 0;
		}
	}

	static PFS::CDataBlock String2Data( const std::wstring& str)
	{
		// str必须保证为偶数长度字符数，否则无法保证正确性
		assert( str.size() % 2 == 0 );

		PFS::CDataBlock data;
		const wchar_t* pwstr = str.c_str();
		size_t len = str.size();

		data.Reserve( (len + 1) /2 );
		char* pstr = (char*)data.GetData();
		size_t j = 0;
		if ( len %2 != 0 )
		{
			len--;
			for( size_t i=0; i<len; i+=2)
			{
				pstr[j++] = (char)(((unsigned int)HexToInt(pwstr[i]) << 4) + HexToInt(pwstr[i+1]));
			}
			
			pstr[j++] = (char) HexToInt(pwstr[len]);
		}
		else
		{
			for( size_t i=0; i<len; i+=2)
			{
				pstr[j++] = (char)(((unsigned int)HexToInt(pwstr[i]) << 4) + HexToInt(pwstr[i+1]));
			}
		}

		data.Resize( j );

		return data;
	}

	static int String2Int( const std::wstring& str) { return _wtoi( str.c_str()); }
	
	static fsize_t String2FileSize( const std::wstring& str) { return _wtoi64( str.c_str()); }
	
	static void Data2String( const PFS::CDataBlock& data, std::wstring& des)
	{
		//des.reserve( data.size() * 2);
		des.resize( data.GetSize()*2);
		wchar_t* pwdes = const_cast<wchar_t*>( des.c_str());
		char* c = (char*)data.GetData();
		char* end = c + data.GetSize();
		int i = 0;
		while( c < end)
		{
			pwdes[i++] = TABLE[ ((*c) >> 4) & 0x0f];
			pwdes[i++] = TABLE[ (*c) & 0x0f];
			c++;
		}
	}

	static void Int2String( int n, std::wstring& des)
	{
		const size_t	size = 16;
		//des.reserve( size);
		wchar_t		str[size];
		_itow_s( n, str, size, 10);
		des = str;
	}

	static void FileSize2String( fsize_t fsize, std::wstring& des)
	{
		const size_t	size = 16;
		//des.reserve( size);
		wchar_t		str[size];
		_i64tow_s( fsize, str, size, 10);
		des = str;
	}

	static PFS::FILE_DATA_HASH String2Hash( const std::wstring& strHash )
	{
		// HashString必须是二进制的8字节hash值转换为16进制表示形式的字符串
		assert( strHash.size() == sizeof(PFS::FILE_DATA_HASH)*2 );

		PFS::FILE_DATA_HASH hash = 0;
		PFS::CDataBlock data = String2Data( strHash );
		if( data.GetSize() != 0)
		{
			memcpy( &hash, data.GetData(), sizeof( PFS::FILE_DATA_HASH));
		}
		return hash;
	}

	static std::wstring& Hash2String( PFS::FILE_DATA_HASH hash, std::wstring& strHash )
	{
		PFS::CDataBlock data;
		data.Append(&hash, sizeof( PFS::FILE_DATA_HASH));
		Data2String( data, strHash);
		assert( strHash.size() == sizeof(PFS::FILE_DATA_HASH)*2 );
		return strHash;
	}
};


class CMetaFileRead
{
	CMetaFileRead() { }
	~CMetaFileRead() {}

	static void DoParseData( const XMLIO::CINode& rn, PFS::CMetaInfoMap& mm);
	static void DoParseOneData( const XMLIO::CINode& rn, PFS::CMetaInfoMap& mm);
public:
	static bool DoRead( PFS::CBaseFile& basefile, PFS::CMetaInfoMap& mm);
	static bool DoRead( PFS::CDataBlock& data, PFS::CMetaInfoMap& mm);
};

class CMetaFileWrite
{
	static void DoDumpFile( XMLIO::CONode& wn, const PFS::CMetaInfoMap& mm);
	static void DoDumpOneMetaInfo( XMLIO::CONode& wn, const std::wstring& filename, const PFS::CMetaInfo& mi);
public:
	static bool DoWrite( PFS::CBaseFile& basefile, const PFS::CMetaInfoMap& mm);
	static bool DoWriteData( PFS::CDataBlock& data, const PFS::CMetaInfoMap& mm);
};

} // namespace PFS {