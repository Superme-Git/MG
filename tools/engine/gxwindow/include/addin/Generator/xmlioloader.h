#pragma once

namespace GXWADDIN
{

inline GXWINDOW::COLOR StrARGB2GXWColor( const wchar_t* argb, GXWINDOW::COLOR defaultvalue = 0xffffffff);

class CXMLIOGuiLoader : public CGuiLoader
{
public:
	CXMLIOGuiLoader();
	virtual ~CXMLIOGuiLoader();
public:
	virtual CComponentAttributes LoadGuiFile( const wchar_t* filename, bool bReadImagePFSMode);

	static void LoadTextFormatFile( const wchar_t* filename, CTextFormatMap& tfm);
};


}