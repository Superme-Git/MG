#pragma once
#include <map>
#include <string>
#include <algorithm>
#include <afxcmn.h>

class CFileIcon
{
	typedef std::map<std::wstring, int>		CIconIndexMap;
	CIconIndexMap							m_mapIcon;
	CImageList								m_lstIcon;

	CFileIcon() : m_projectIconIndex(-1), m_directoryIconIndex(-1)
	{
		m_lstIcon.Create( 16, 16, ILC_COLOR32 | ILC_MASK, 0, 4);
		SHFILEINFO sfi;
		SHGetFileInfo( L"C:\\", NULL, &sfi, sizeof(sfi), SHGFI_ICON|SHGFI_SMALLICON);
		m_projectIconIndex = m_lstIcon.Add( sfi.hIcon);
		SHGetFileInfo(L"", FILE_ATTRIBUTE_DIRECTORY, &sfi, sizeof(sfi), SHGFI_ICON|SHGFI_SMALLICON);
		m_directoryIconIndex = m_lstIcon.Add( sfi.hIcon);
	}
public:
	int		m_projectIconIndex;			// 工程图标索引
	int		m_directoryIconIndex;		// 目录图标索引

	int GetFileIconIndex( const std::wstring& sFile);
	CImageList& GetImageList() { return m_lstIcon; }
	static CFileIcon& GetInstance()
	{
		static CFileIcon instance;
		return instance;
	}
};

class CAttributeDesc
{
public:
	struct CAttributeDescItem
	{
		int				nIconIndex;	// 表示压缩方式图标
		std::wstring	sDescript;
		CAttributeDescItem() : nIconIndex(-1) {}
		CAttributeDescItem( int nIcon, const std::wstring& sDesc) : nIconIndex( nIcon), sDescript( sDesc) {}
	};
	typedef std::map<int, CAttributeDescItem>	CAttributeDescMap;
private:
	CAttributeDescMap		m_map;
public:
	CAttributeDescMap& GetMap() { return m_map; }
	int LevelToIconIndex( int nLevel)
	{
		CAttributeDescMap::const_iterator itor = m_map.find(nLevel);
		if (itor != m_map.end())
		{
			return itor->second.nIconIndex ;
		}
		return -1;
	}
	const std::wstring& LevelToDesc( int nLevel)
	{
		CAttributeDescMap::const_iterator itor = m_map.find( nLevel);
		if( itor != m_map.end())
		{
			return itor->second.sDescript;
		}
		static std::wstring temp;
		return temp;
	}
	int DescToLevel( const std::wstring& sDesc, int nDefault)
	{
		CAttributeDescMap::const_iterator itor = m_map.begin();
		for (; itor != m_map.end(); ++itor)
		{
			if (itor->second.sDescript == sDesc)
				return itor->first;
		}
		return nDefault;
	}
};


class CGlobalInfo
{
	CGlobalInfo(void);
	virtual ~CGlobalInfo(void);

public:
	
	static const int ZipID;
	static const int X2OID;
	static const int ZpdID;
	static const int BzfID;
	static const int NoDecode;
	static const int DecodeCount;

	enum
	{
		NO = 0,
		RADIO1,
		RADIO2,
		RADIO3,
		RADIO4,
		RADIO5,
		RADIO6,
		RADIO7,
		RADIO8,
		RADIO9,
		RADIO_OTHERS,
	};

	static CGlobalInfo& GetInstance()
	{
		static CGlobalInfo s_instance;
		return s_instance;
	}
	static std::wstring GetFileExt( const std::wstring& filename)
	{
		std::wstring sExt = filename.substr( filename.find_last_of(L'.')+1);
		std::transform( sExt.begin(), sExt.end(), sExt.begin(), ::towlower);
		return sExt;
	}

	bool Create();
	CImageList& GetSetupIconList() { return m_lstSetupIcon; }
	CImageList& GetZipIconList() { return m_lstZipIcon; }
	CImageList& GetCheckIconList() { return m_lstCheckIcon; }
	CAttributeDesc& GetZipDesc() { return m_ZipDesc; }
	CAttributeDesc& GetSetupDesc() { return m_SetupDesc; }

private:
	CImageList			m_lstSetupIcon;
	CImageList			m_lstZipIcon;
	CImageList			m_lstCheckIcon;

	CAttributeDesc		m_ZipDesc;
	CAttributeDesc		m_SetupDesc;
};

