#pragma once

namespace GXWADDIN
{

/*
  ʹ��dom����xml��TextDecoder
**/
class CDomXMLTextDecoder : public GXWINDOW::CAbstarctTextDecoder
{
public:
	CDomXMLTextDecoder() {}
	virtual ~CDomXMLTextDecoder() {}
public:
	virtual void Decode( const wchar_t* fulltext, GXWINDOW::CDecoderTextPieceList& tpl);
	virtual void Delete() { delete this; }
	virtual CAbstarctTextDecoder* Clone() const { return new CDomXMLTextDecoder(); }
};

}