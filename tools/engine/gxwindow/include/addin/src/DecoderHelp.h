#pragma once
#include "comxml.h"

namespace GXWADDIN
{

	void DomDecodeElementEmotion( INLINECLASS::CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
	{
		int w = Ele.ReadInteger( L"W", L"w", 32);
		int h = Ele.ReadInteger( L"H", L"h", 32);
		int u = Ele.ReadInteger( L"U", L"u", 32);
		int v = Ele.ReadInteger( L"V", L"v", 32);
		int p = Ele.ReadInteger( L"P", L"p", 0);
		int i = Ele.ReadInteger( L"I", L"i", 0);
		CComBSTR	strMouseNotify;
		Ele.ReadString( L"M", L"n", strMouseNotify);

		tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetTipInfoText().c_str(), p, i, w, h, u, v, strMouseNotify));
	}

	void DomDecodeElementAnimate( INLINECLASS::CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
	{
		int w = Ele.ReadInteger( L"W", L"w", 32);
		int h = Ele.ReadInteger( L"H", L"h", 32);
		int i = Ele.ReadInteger( L"I", L"i", 500);
		int s = Ele.ReadInteger( L"S", L"s", 0);
		int e = Ele.ReadInteger( L"E", L"e", 0);
		CComBSTR	strMouseNotify;
		Ele.ReadString( L"M", L"n", strMouseNotify);

		tpl.Insert( GXWINDOW::CDecoderTextPiece( s, e, w, h, i, Ele.GetTipInfoText().c_str(), strMouseNotify));
	}

	void DomDecodeElementImage( INLINECLASS::CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
	{
		int w = Ele.ReadInteger( L"W", L"w", 32);
		int h = Ele.ReadInteger( L"H", L"h", 32);
		int i = Ele.ReadInteger( L"I", L"i", 0);
		CComBSTR	strMouseNotify;
		Ele.ReadString( L"M", L"n", strMouseNotify);
		tpl.Insert( GXWINDOW::CDecoderTextPiece( i, w, h, Ele.GetTipInfoText().c_str(), strMouseNotify));
	}

	void DomDecodeElementText( INLINECLASS::CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl, GXWINDOW::DTP_TYPE dtpt)
	{
		int f = Ele.ReadInteger( L"F", L"f", 0);
		CComBSTR	strNotify;
		Ele.ReadString( L"I", L"i", strNotify);
		TextFormat		tf;
		bool b = CTextFormatManager::GetInstance().GetTextFormat( f, tf);
		assert( b);
		tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetText().c_str(), tf.m_nFont, f, tf.m_clrNormal, tf.m_clrNormalGradual, tf.m_clrSelect, tf.m_clrSelectGradual, tf.m_clrBackground, strNotify, 
			GXWINDOW::DTPT_COMPLEX == dtpt, Ele.GetTipInfoText().c_str(), tf.m_nNormalEffectType, tf.m_clrNormalEffect, tf.m_nSelectEffectType, tf.m_clrSelectEffect));
	}

	void DomDecodeElementButton( INLINECLASS::CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
	{
		int f = Ele.ReadInteger( L"F", L"f", 0);
		TextFormat		tf;
		bool b = CTextFormatManager::GetInstance().GetTextFormat( f, tf);
		assert( b);
		CComBSTR	strNotify;
		Ele.ReadString( L"N", L"n", strNotify);
		int nImage = Ele.ReadInteger( L"I", L"i", -1);
		int nImageMouseOn = Ele.ReadInteger( L"O", L"o", -1);
		int nImageDown = Ele.ReadInteger( L"D", L"d", -1);
		int nImageWidth = Ele.ReadInteger( L"W", L"w", nImage < 0 ? 0 : GXWINDOW::EBE_DEFAULT_BUTTON_WIDTH);
		int nImageHeight = Ele.ReadInteger( L"H", L"h", nImage < 0 ? 0 : GXWINDOW::EBE_DEFAULT_BUTTON_HEIGHT);
		CComBSTR	strMouseNotify;
		Ele.ReadString( L"M", L"n", strMouseNotify);

		tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetText().c_str(), tf.m_nFont, f, tf.m_clrNormal, tf.m_clrNormalGradual, tf.m_clrMouseOn, tf.m_clrMouseOnGradual, nImage, nImageMouseOn, nImageDown, strNotify, 
			nImageWidth, nImageHeight, Ele.GetTipInfoText().c_str(), strMouseNotify, tf.m_nNormalEffectType, tf.m_clrNormalEffect, tf.m_nSelectEffectType, tf.m_clrSelectEffect));
	}

	void DomDecodeElement( CComPtr<IXMLDOMElement>& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
	{
		CComBSTR	bstrName;

		HRESULT	hr = Ele->get_tagName( &bstrName);
		if( FAILED( hr))
			return;
		if( 0 == bstrName.m_str[0])
			return;
		if( bstrName.m_str[1])
			return;
		switch( bstrName.m_str[0]) 
		{
		case L'A':
		case L'a':
			DomDecodeElementAnimate( INLINECLASS::CDomElement( Ele), tpl);
			break;
		case L'I':
		case L'i':
			DomDecodeElementImage( INLINECLASS::CDomElement( Ele), tpl);
			break;
		case L'T':
		case L't':
			DomDecodeElementText( INLINECLASS::CDomElement( Ele), tpl, GXWINDOW::DTPT_FORMAT_TEXT);
			break;
		case L'C':
		case L'c':
			DomDecodeElementText( INLINECLASS::CDomElement( Ele), tpl, GXWINDOW::DTPT_COMPLEX);
			break;
		case L'R':
		case L'r':
			tpl.Insert( GXWINDOW::CDecoderTextPiece());
			break;
		case L'B':
		case L'b':
			DomDecodeElementButton( INLINECLASS::CDomElement( Ele), tpl);
			break;
		case L'E':
		case L'e':
			DomDecodeElementEmotion( INLINECLASS::CDomElement( Ele), tpl);
			break;
		default:
			break;
		}
	}

} // namespace
