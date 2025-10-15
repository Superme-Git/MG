#pragma once

#include <string>
#include <map>

namespace GXWADDIN {

// 对于文本数据<R/>或者<r/>为回车换行
// 动画图片 <A W=宽度 H=高度 S=图片号开始 E=图片号结束 I=显示间隔 M=鼠标事件通告标识/>
// 静态图片 <I W=宽度 H=高度 I=图片号 M=鼠标事件通告标识/>
// 格式文本 <T F=字体方案 I=按键信息>文本</T>
// 固定文本 <C F=字体方案 I=按键信息>文本</C>
// 按钮信息 <B F=字体方案 I=图标 O=鼠标在上面时的图标 D=鼠标按下时图标 W=图标高度 H=图标宽度 N=按钮点击事件通告标识 M=鼠标事件通告标识>文本</B>
// 表情图标 <E W=显示宽度 H=显示高度 U=图标原始宽度 V=图标原始高度 P=包号 I=表情号 M=鼠标事件通告标识/>

// tip info <A><P>...</P></A>
// tip info <I><P>...</P></I>
// tip info <T><P>...</P></T>

struct TextFormat
{
	int					m_nFont;
	GXWINDOW::COLOR		m_clrNormal;
	GXWINDOW::COLOR		m_clrNormalGradual;
	GXWINDOW::COLOR		m_clrNormalEffect;
	GXWINDOW::FONTEFEECTFLAG		m_nNormalEffectType;
	union
	{
		struct  
		{
			GXWINDOW::FONTEFEECTFLAG		m_nMouseOnEffectType;
			GXWINDOW::COLOR		m_clrMouseOn;
			GXWINDOW::COLOR		m_clrMouseOnGradual;
			GXWINDOW::COLOR		m_clrMouseOnEffect;
		};
		struct
		{
			GXWINDOW::FONTEFEECTFLAG		m_nSelectEffectType;
			GXWINDOW::COLOR		m_clrSelect;
			GXWINDOW::COLOR		m_clrSelectGradual;
			GXWINDOW::COLOR		m_clrSelectEffect;
			GXWINDOW::COLOR		m_clrBackground;
		};
	};
};

class CTextFormatManager
{
	typedef std::map<int,TextFormat>	CTextFormatMap;
	typedef std::map<int,std::wstring>	CTFID2NameMap;
	typedef std::map<std::wstring,int>	CTFName2IDMap;

	CTextFormatMap		m_tfmap;
	CTFID2NameMap		m_id2name;
	CTFName2IDMap		m_name2id;

	int					m_nIdGenerator;
	static CTextFormatManager		s_Instance;

	CTextFormatManager() : m_nIdGenerator( 0) {}
	~CTextFormatManager() {}
public:
	static CTextFormatManager& GetInstance() { return s_Instance; } 
public:
	void Load( const wchar_t* szFile, bool bLoadFromMem = false);
	void Save( const wchar_t* szFile) const;
public:
	int NewName( const std::wstring& name);
	void SetTextFormat( int nID, const TextFormat& tf, const wchar_t *name = 0); // add "name" by llq
	void Clear()
	{
		m_tfmap.clear();
		m_id2name.clear();
		m_name2id.clear();
		m_nIdGenerator = 0;
	}
public:
	bool GetName( int nID, std::wstring& name) const;
	bool GetID( const std::wstring& name, int& nid) const;
	bool GetTextFormat( int nID, TextFormat& tf) const;
	bool GetTextFormat( const std::wstring& name, TextFormat& tf) const;
};

class CXMLTextDecoder : public GXWINDOW::CAbstarctTextDecoder
{
public:
	CXMLTextDecoder() {}
	virtual ~CXMLTextDecoder() {}
public:
	virtual void Decode( const wchar_t* fulltext, GXWINDOW::CDecoderTextPieceList& tpl);
	virtual void Delete() { delete this; }
	virtual CAbstarctTextDecoder* Clone() const { return new CXMLTextDecoder(); }
};

class CXMLTextEncoder : public GXWINDOW::CAbstarctTextEncoder
{
	std::wstring		m_strResult;
public:
	CXMLTextEncoder() {}
	virtual ~CXMLTextEncoder() {}
public:
	virtual void AddTextPiece( const GXWINDOW::CDecoderTextPiece& tp);
	virtual const wchar_t* GetResult() { return m_strResult.c_str(); }
};

} // namespace GXWADDIN {