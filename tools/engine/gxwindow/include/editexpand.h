#pragma once

namespace GXWINDOW { 

class GXWINDOW_EXPORTS_DLL CFormatTextDecoder : public CAbstarctTextDecoder
{
public:
	CFormatTextDecoder();
	virtual ~CFormatTextDecoder();
public:
	virtual void Decode( const wchar_t* fulltext, CDecoderTextPieceList& tpl);
	virtual CAbstarctTextDecoder* Clone() const;
};

class GXWINDOW_EXPORTS_DLL CFormatTextEncoder : public CAbstarctTextEncoder
{
	INLINECLASS::CSimpleString*		m_ss;
public:
	CFormatTextEncoder();
	virtual ~CFormatTextEncoder();
public:
	virtual void AddTextPiece( const CDecoderTextPiece& tp);
	virtual const wchar_t* GetResult();
};

} // namespace GXWINDOW { 