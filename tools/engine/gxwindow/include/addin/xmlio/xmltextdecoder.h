#pragma once

#include <string>
#include <map>

namespace GXWADDIN {

// �����ı�����<R/>����<r/>Ϊ�س�����
// ����ͼƬ <A W=��� H=�߶� S=ͼƬ�ſ�ʼ E=ͼƬ�Ž��� I=��ʾ��� M=����¼�ͨ���ʶ/>
// ��̬ͼƬ <I W=��� H=�߶� I=ͼƬ�� M=����¼�ͨ���ʶ/>
// ��ʽ�ı� <T F=���巽�� I=������Ϣ>�ı�</T>
// �̶��ı� <C F=���巽�� I=������Ϣ>�ı�</C>
// ��ť��Ϣ <B F=���巽�� I=ͼ�� O=���������ʱ��ͼ�� D=��갴��ʱͼ�� W=ͼ��߶� H=ͼ���� N=��ť����¼�ͨ���ʶ M=����¼�ͨ���ʶ>�ı�</B>
// ����ͼ�� <E W=��ʾ��� H=��ʾ�߶� U=ͼ��ԭʼ��� V=ͼ��ԭʼ�߶� P=���� I=����� M=����¼�ͨ���ʶ/>

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