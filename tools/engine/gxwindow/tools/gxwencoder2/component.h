#pragma once

class CCodeComponent;
class CComponent;
typedef std::list<CComponent> ComponentList;
typedef std::list<const CComponent*> ComponentPointerList;



inline void ConvertBStrToTStr( BSTR bstrSrc, std::string& strResult)
{
	int		nResult;
	UINT	acp = GetACP();

	nResult = WideCharToMultiByte( acp, 0, bstrSrc, -1, NULL, NULL, NULL, NULL);
	if( 0 == nResult)
		return;
	char*		szBuffer;
	szBuffer = new char[nResult + 1];

	nResult = WideCharToMultiByte( acp, 0, bstrSrc, -1, szBuffer, nResult, NULL, NULL);
	szBuffer[nResult] = 0;
	strResult = szBuffer;
	delete[] szBuffer;
}

inline std::string ConvertBStrToTStr( BSTR bstrSrc)
{
	std::string strTemp;
	ConvertBStrToTStr( bstrSrc, strTemp);
	return strTemp;
}

inline std::string ConvertVariantToString( CComVariant& var)
{
	if( VT_BSTR != var.vt)
		return "";

	std::string strTemp;
	ConvertBStrToTStr( var.bstrVal, strTemp);
	return strTemp;
}


class CPropertyMap : public std::map<std::string,std::string>
{
public:
	CPropertyMap()
	{
	}
	~CPropertyMap()
	{
	}
public:
	void Push( const std::string& strAttribute, const std::string& strValue)
	{
		(*this)[strAttribute] = strValue;
	}
	std::string Find( const std::string& strAttribute) const
	{
		const_iterator it = find( strAttribute);
		if( it == end())
			return "";
		return it->second;
	}
};

class CComponent
{
	std::string		m_type;
	std::string		m_name;
	std::string		m_parenttype;
	CPropertyMap	m_properties;
	ComponentList	m_children;
public:
	CComponent();
	virtual ~CComponent();
public:
	void SetType( const std::string& type);
	void SetName( const std::string& name);
	void SetParentType( const std::string& type);
	const char* GetType() const;
	const char* GetName() const;
	const char* GetParentType() const;

	CPropertyMap& GetPropertyMap();
	ComponentList& GetChilderList();

	virtual void Clear();
	virtual std::string QueryPropertyValue( const std::string& strProperty) const;
	virtual  const CComponent* FindComponent( const std::string& strName) const;
	virtual void FindComponent( const std::string& strType, ComponentPointerList& components) const;
	virtual void FindChildren( const std::string& strType, ComponentPointerList& children) const;
	virtual const CComponent* FindComponentByType( const std::string& strType) const;
	virtual bool HasType( const std::string& strType) const;
	virtual const CComponent* FindChild( const std::string& strType) const;
	virtual CComponent* FindChild( const std::string& strType);

	virtual void RemoveChild( const std::string& name);

	virtual void Clone( const CComponent& component)
	{
		if ( this == &component)
			return;

		m_type = component.m_type;
		m_name = component.m_name;
		m_parenttype = component.m_parenttype;
		m_properties = component.m_properties;
		m_children = component.m_children;
	}

	virtual bool IsSameComponent( const CComponent& component) const
	{
		return this == &component;
	}
};

typedef std::map<std::string, ComponentPointerList> NotifyVariantMap;

struct TEXTSTATE
{
	std::string szColor;	// 字体颜色
	std::string szColor2;	// 渐变色
	std::string szEffectColor;
	bool		bUnderline;
	bool		bShadow;
	bool		bOutline;
	bool		bGraudal;	// 是否使用渐变色

	TEXTSTATE() : bUnderline( false), bShadow( false), bOutline( false), bGraudal( false)
	{
	}
	TEXTSTATE( const TEXTSTATE& src) : szColor( src.szColor), szColor2( src.szColor2), szEffectColor( src.szEffectColor), bUnderline( src.bUnderline), bShadow( src.bShadow), bOutline( src.bOutline), bGraudal( src.bGraudal)
	{
	}

	TEXTSTATE& operator=( const TEXTSTATE& src)
	{
		if ( this == &src)
			return *this;

		szColor = src.szColor;
		szColor2 = src.szColor2;
		szEffectColor = src.szEffectColor;
		bUnderline = src.bUnderline;
		bShadow = src.bShadow;
		bOutline = src.bOutline;
		bGraudal = src.bGraudal;

		return *this;
	}
};

enum TEXTSTATEFLAG
{
	TSF_DISABLE = 0,
	TSF_NORMAL = 1,
	TSF_MOUSEON = 2,
	TSF_DOWN = 3,
	TSF_SELECT = 3,
	TSF_DOWN_MOUSEON = 4,
	TSF_SELECT_MOUSEON = 4,
	TSF_STATE_MAXNUM = 4,
};

typedef std::map<int, TEXTSTATE> CTextStateMap;

struct TEXTFORMAT
{
	int				nFontType;
	CTextStateMap	textStateMap;
};

struct TEXTRESREF
{
	std::string		referenceUI;
	std::string		referenceComponent;
};
typedef std::vector<TEXTRESREF> CTextResRefVector;

struct TEXTRES
{
	int					id;
	CTextResRefVector	refs;
};

struct TEXTRESKEY
{
	int					id;
	std::string			text;

	bool operator<( const TEXTRESKEY& key)
	{
		return this->id < key.id;
	}

	bool operator==( const TEXTRESKEY& key)
	{
		return this->id == key.id;
	}
};

typedef std::map<std::string, TEXTFORMAT> CTextFormatMap;
typedef std::map<std::string, TEXTRES> CTextResMap;
typedef std::vector<int> CIntVector;
typedef std::vector<TEXTRESKEY> CTextResKeyVector;

class CContext
{
	enum { eMinTextResID = 10000, };

	bool				m_bPakMode;
	bool				m_bUseTextFormat;
	CPropertyMap		m_variant;
	CPropertyMap		m_imageres;
	NotifyVariantMap	m_notifymap;
	std::vector<std::string>	m_notifyclassSplit;
	CTextFormatMap		m_textformatMap;
	std::vector<std::string>	m_namespace;
	std::string			m_uiname;
	std::string			m_textResXmlFile;
	CTextResMap			m_textResMap;
	CTextResKeyVector	m_textResIDs;
	int					m_genmode;

	CStringCodeOut		m_imagereg;

public:
	CContext();
	virtual ~CContext();

	void Clear();

	void PushVariant(const std::string& componentname, const std::string& variantname);
	std::string QueryVariant( const std::string& componentname);
	std::string QueryVariant( const CComponent& component);
	std::string QueryImageRes( const std::string& iamgefilename);
	std::string GetImageReg() { return m_imagereg.GetString(); }

	void PushNotifyVariant( const std::string& type, const CComponent* component);
	const ComponentPointerList& GetNotifyVariants( const std::string& type);

	const std::vector<std::string>& SplitNotifyClass( const std::string& notifyclass);
	const std::vector<std::string>& GetSplitedNotifyClass() const;

	void SetPakMode( bool bPakMode)
	{
		m_bPakMode = bPakMode;
	}

	void LoadTextFormat( const char* szTextFormat);
	bool IsTextFormat() const
	{
		return m_bUseTextFormat;
	}

	const TEXTFORMAT* GetTextFormatMap( std::string profilename)
	{
		CTextFormatMap::iterator it = m_textformatMap.find( profilename);
		//assert( it != m_textformatMap.end() && "所设置的字体样式不在样式表中");
		if ( it == m_textformatMap.end())
		{
			printf( "所设置的字体样式 [%s] 不在样式表中\n", profilename.c_str());
			return NULL;
		}
		return &( it->second);
	}

	void SetNameSpace( std::string szNamespace);
	const std::vector<std::string>& GetNameSpace() const;

	void SetUIName( const std::string& name ) { m_uiname = name; }
	const std::string GetUIName() const { return m_uiname; }

	void SetGenMode( int mode) { m_genmode = mode; }
	int GetGenMode() const { return m_genmode; }
	
	bool IsUseTextRes() const { return !m_textResXmlFile.empty(); }
	void LoadTextResXmlFile( const std::string file);
	void SaveTextResXmlFile();
	int LoadTextResID( const std::string& text, const std::string& refui, const std::string& refcomponent);
};


inline void str2wstr( const char* szSrc, std::wstring& strResult)
{
	int		nResult;

	nResult = MultiByteToWideChar( CP_ACP, 0, szSrc, -1, NULL, NULL);
	if( 0 == nResult)
		return;
	wchar_t* wsBuffer= new wchar_t[nResult + 1];
	nResult = MultiByteToWideChar( CP_ACP, 0, szSrc, -1, wsBuffer, nResult);
	wsBuffer[nResult] = 0;
	strResult = wsBuffer;
	delete[] wsBuffer;
}

inline void wstr2str( const wchar_t* szSrc, std::string& strResult)
{
	int		nResult;

	nResult = WideCharToMultiByte( CP_ACP, 0, szSrc, -1, NULL, NULL, NULL, false);
	if( 0 == nResult)
		return;
	char* wsBuffer= new char[nResult + 1];
	nResult = WideCharToMultiByte( CP_ACP, 0, szSrc, -1, wsBuffer, nResult, NULL, false);
	wsBuffer[nResult] = 0;
	strResult = wsBuffer;
	delete[] wsBuffer;
}