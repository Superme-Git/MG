#include "gxwindowdef.h"

namespace GXWINDOW { 

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFormatTextDecoder::CFormatTextDecoder()
{
}

CFormatTextDecoder::~CFormatTextDecoder()
{
}

CAbstarctTextDecoder* CFormatTextDecoder::Clone() const
{
	return new CFormatTextDecoder();
}
void CFormatTextDecoder::Decode( const wchar_t* fulltext, CDecoderTextPieceList& tpl)
{
	tpl.Insert( CDecoderTextPiece( fulltext));
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFormatTextEncoder::CFormatTextEncoder()
	: m_ss( new INLINECLASS::CSimpleString())
{
}

CFormatTextEncoder::~CFormatTextEncoder()
{
	delete m_ss;
}

const wchar_t* CFormatTextEncoder::GetResult()
{
	return m_ss->c_str();
}

void CFormatTextEncoder::AddTextPiece( const CDecoderTextPiece& tp)
{
	if( DTPT_DEFAULT_TEXT == tp.GetType())
		m_ss->append( tp.GetText(), tp.GetTextLength());
}


} // namespace GXWINDOW { 