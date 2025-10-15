#include <Windows.h>
#include <string>
#include <map>
#include <vector>
#include <assert.h>
#include "geninclude.h"
#include "uiloader.h"
#include "xmlioloader.h"

namespace GXWADDIN
{

const wchar_t* DEFAULT_FOCUS					= L"defaultfocus";
const wchar_t* BACKGROUND_IMAGE					= L"Image1";
const wchar_t* BARRUB_IMAGE_NORMAL				= L"ImageBlock1";
const wchar_t* BARRUB_IMAGE_MOUSEON				= L"ImageBlock2";
const wchar_t* BARRUB_IMAGE_DOWN				= L"ImageBlock3";
const wchar_t* BARSLOT_IMAGE					= L"ImageSlot";
const wchar_t* SCBUPBTN_IMAGE_NORMAL			= L"ImageUp1";
const wchar_t* SCBUPBTN_IMAGE_MOUSEON			= L"ImageUp2";
const wchar_t* SCBUPBTN_IMAGE_DONW				= L"ImageUp3";
const wchar_t* SCBUPBTN_IMAGE_DISABLE			= L"ImageUp0";
const wchar_t* SCBDNBTN_IMAGE_NORMAL			= L"ImageDown1";
const wchar_t* SCBDNBTN_IMAGE_MOUSEON			= L"ImageDown2";
const wchar_t* SCBDNBTN_IMAGE_DONW				= L"ImageDown3";
const wchar_t* SCBDNBTN_IMAGE_DISABLE			= L"ImageDown0";
const wchar_t* BUTTON_IMAGE_NORMAL				= L"Image1";
const wchar_t* BUTTON_IMAGE_MOUSEON				= L"Image2";
const wchar_t* BUTTON_IMAGE_DOWN				= L"Image3";
const wchar_t* BUTTON_IMAGE_DOWNON				= L"Image4";
const wchar_t* BUTTON_IMAGE_DISABLE				= L"Image0";
const wchar_t* SCROLLBAR						= L"ScrollBar";
const wchar_t* SCROLLBAR_V						= L"VScrollBar";
const wchar_t* SCROLLBAR_H						= L"HScrollBar";
const wchar_t* LISTBOX_IMAGE_LINE				= L"ImageLine";
const wchar_t* LISTBOX_MOUSEON_LINE				= L"LineBkImage1";
const wchar_t* LISTBOX_SELECTED_LINE			= L"LineBkImage3";
const wchar_t* COMBOBOX_IMAGE_BOX				= L"ImageBox";
const wchar_t* COMBOBOX_IMAGE_LIST				= L"ImageList";
const wchar_t* COMBOBOX_MOUSEON_LINE			= L"LineBkImage1";
const wchar_t* COMBOBOX_SELECTED_LINE			= L"LineBkImage3";
const wchar_t* TABLE_MOUSEON_LINE				= L"LineBkImage1";
const wchar_t* TABLE_SELECTED_LINE				= L"LineBkImage3";

CXMLIOGuiLoader					g_defaultGuiLoader;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

inline bool IsTileMode( const wchar_t* attr)
{
	return 0 == _wcsicmp( attr, L"tile");
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


CComponentAttributes::CComponentAttributes()
{

}

CComponentAttributes::~CComponentAttributes()
{

}

CComponentAttributes& CComponentAttributes::operator=( const CComponentAttributes& src)
{
	if ( &src == this)
		return *this;

	this->m_strAttrs = src.m_strAttrs;
	this->m_childAttrs = src.m_childAttrs;

	return *this;
}

const wchar_t* CComponentAttributes::QueryAttribute( const wchar_t* key, const wchar_t* defaultValue) const
{
	assert( key);

	StrMap::const_iterator it = m_strAttrs.find( key);
	if ( it == m_strAttrs.end())
		return defaultValue;

	return it->second.c_str();
}

int CComponentAttributes::QueryIntegerAttribute(const wchar_t* key, int defaultValue) const
{
	if ( const wchar_t* value = QueryAttribute( key))
	{
		const wchar_t* p = value;
		if ( *p)
		{
			while ( *p)
			{
				wchar_t ch = *p;
				if ( !iswdigit( ch) && ch != L'+' && ch != L'-')
					return defaultValue;
				++p;
			}
			return _wtoi( value);
		}
	}
	return defaultValue;
}

bool CComponentAttributes::QueryBoolAttribute( const wchar_t* key, bool defaultValue) const
{
	if ( const wchar_t* value = QueryAttribute( key))
		return 0 == _wcsicmp( value, L"true");

	return defaultValue;
}

GXWINDOW::COLOR CComponentAttributes::QueryColorAttribute( const wchar_t* key, GXWINDOW::COLOR defaultvalue) const
{
	if ( const wchar_t* sc = QueryAttribute( key))
		return StrARGB2GXWColor( sc, defaultvalue);
	else
		return defaultvalue;
}

const CComponentAttributes* CComponentAttributes::QueryChildAttribute( const wchar_t* key) const
{
	assert( key);

	AttrMap::const_iterator it = m_childAttrs.find( key);
	if ( it == m_childAttrs.end())
		return NULL;

	return &it->second;
}

void CComponentAttributes::InsertAttribute( const wchar_t* key, const wchar_t* value)
{
	assert( key);
	m_strAttrs[ key] = value;
}

void CComponentAttributes::InsertChildAttribute( const wchar_t* key, const CComponentAttributes& value)
{
	assert( key);
	m_childAttrs[ key] = value;
	m_childAttrOrders.push_back( key);
}

CComponentAttributes& CComponentAttributes::NewChildAttribute( const wchar_t* key)
{
	assert( key);
	AttrMap::iterator it = m_childAttrs.find( key);
	//assert( it == m_childAttrs.end());
	if ( it != m_childAttrs.end())
	{
		return *(it->second.m_slots.insert( it->second.m_slots.end(), CComponentAttributes()));
	}
	else
	{
		m_childAttrOrders.push_back( key);
		std::pair<AttrMap::iterator, bool> insertit = m_childAttrs.insert( std::make_pair( key, CComponentAttributes()));
		return insertit.first->second;
	}
}

void CComponentAttributes::Clear()
{
	m_strAttrs.clear();
	m_childAttrs.clear();
	m_slots.clear();
}

int CComponentAttributes::GetSlotCount() const
{
	return ( int)m_slots.size();
}

const CComponentAttributes& CComponentAttributes::GetSlotAttribute( int n) const
{
	return m_slots.at( n);
}

const CComponentAttributes::AttrMap& CComponentAttributes::GetChildMap() const
{
	return m_childAttrs;
}

const CComponentAttributes::AttrOrder& CComponentAttributes::GetChildOrder() const
{
	return m_childAttrOrders;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

std::wstring CGuiGenerator::s_path = L"./";
CTextFormatMap	CGuiGenerator::s_tfm;
bool CGuiGenerator::s_bReadImgPFSMode = false;
bool CGuiGenerator::s_bSetTransparentAttr = true;
CGuiGenerator::GeneratorMap		CGuiGenerator::s_genmap;

CGuiGenerator::CGuiGenerator(const wchar_t* filename, CGuiLoader* ploader, bool bReadImagePFSMode)
{
	assert( filename);

	m_filename = filename;
	if ( NULL == ploader)
		m_pguiloader = &g_defaultGuiLoader;
	else
		m_pguiloader = ploader;
}

CGuiGenerator::~CGuiGenerator()
{
}

void CGuiGenerator::FreeAlllGenerator()
{
	s_genmap.clear();
}

void CGuiGenerator::SetGuiFilePath( const wchar_t* path)
{
	s_path = path;
}

void CGuiGenerator::SetComponentTransparentAttr( bool b) 
{ 
	s_bSetTransparentAttr = b;
}

void CGuiGenerator::SetReadImagePFSMode( bool b)
{
	s_bReadImgPFSMode = b;
}

void CGuiGenerator::LoadTextFormat( const wchar_t* name)
{
	CXMLIOGuiLoader::LoadTextFormatFile( name, s_tfm);
}

const GUIEditorFontProfile* CGuiGenerator::GetGuiEditorFontProfile( const wchar_t* name)
{
	CTextFormatMap::iterator it = s_tfm.find( name);
	if ( it != s_tfm.end())
		return &( it->second);
	return NULL;
}

// 根据name值获取一个GUIEditor2.xml字体方案的字体id
int  CGuiGenerator::GetGuiEditorFontType( const wchar_t* name)
{
	CTextFormatMap::iterator it = s_tfm.find( name);
	if ( it != s_tfm.end())
		return it->second.nFontType;
	return 0;
}

// 根据name值和指定state获取一个GUIEditor2.xml字体方案中的相应状态字体信息
const TEXTSTATE* CGuiGenerator::GetGuiEditorTextState( const wchar_t* name, TEXTSTATEFLAG state)
{
	CTextFormatMap::iterator it = s_tfm.find( name);
	if ( it != s_tfm.end())
	{
		CTextStateMap& tsm = it->second.textStateMap;
		CTextStateMap::iterator itsm = tsm.find( state);
		if ( itsm != tsm.end())
			return &( itsm->second);
	}
	return NULL;
}

// 根据name值获取一个GUIEditor2.xml字体方案
const TEXTFORMAT* CGuiGenerator::GetGuiEditorTextFormat( const wchar_t* name)
{
	CTextFormatMap::iterator it = s_tfm.find( name);
	if ( it != s_tfm.end())
		return &it->second;
	return NULL;
}

CGuiGenerator& CGuiGenerator::GetGenerator( const wchar_t* filename, CGuiLoader* ploader)
{
	// 同一个xml, 只在第一次获取时进行Load操作
	GeneratorMap::iterator it = s_genmap.find( filename);
	if ( it != s_genmap.end())
	{
		return it->second;
	}
	else
	{
		std::pair<GeneratorMap::iterator, bool> insertit = s_genmap.insert( std::make_pair( filename, CGuiGenerator( filename, ploader, CGuiGenerator::s_bReadImgPFSMode)));
		assert( insertit.second && "GetGenerator()失败!");
		insertit.first->second.Load();
		return insertit.first->second;
	}
}

void CGuiGenerator::Load()
{
	m_ca = m_pguiloader->LoadGuiFile( ( s_path + L"/" + m_filename).c_str(), CGuiGenerator::s_bReadImgPFSMode);
}

void CGuiGenerator::Clear()
{
	m_ca.Clear();
}

void CGuiGenerator::Reload()
{
	Clear();
	Load();
}

// 九宫格图片资源配置
void CGuiGenerator::GenerateTileImage( GXWINDOW::CImagesFill& ti, int nImageIndex, const CComponentAttributes& ca, int componentw, int componenth, GXWINDOW::COLOR clr, bool bVTile, bool bHTile)
{
	GenerateTileImage( ti, nImageIndex, ca.QueryIntegerAttribute( L"x"), ca.QueryIntegerAttribute( L"y"), ca.QueryIntegerAttribute( L"w"), ca.QueryIntegerAttribute( L"h"),
		ca.QueryIntegerAttribute( L"w0"), ca.QueryIntegerAttribute( L"h0"), ca.QueryIntegerAttribute( L"w1"), ca.QueryIntegerAttribute( L"h1"), componentw, componenth, clr, bVTile, bHTile);
}

void CGuiGenerator::GenerateTileImage( GXWINDOW::CImagesFill& ti, int nImageIndex, int x, int y, int w, int h, int w0, int h0, int w1, int h1, int componentw, int componenth, GXWINDOW::COLOR clr, bool bVTile, bool bHTile)
{
	int wc = w - w0 - w1;
	int hc = h - h0 - h1;

	if ( h0 > 0)
	{
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x,			y,				clr), GXWINDOW::IFP_TOPLEFT);
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x + w0,		y,				clr), GXWINDOW::IFP_TOP);
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x + w0 + wc,	y,				clr), GXWINDOW::IFP_TOPRIGHT);
	}

	if ( w0 > 0)
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x,			y + h0,			clr), GXWINDOW::IFP_LEFT);
	if ( wc > 0 && hc > 0)
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x + w0,		y + h0,			clr), GXWINDOW::IFP_CENTER);
	if ( w1 > 0)
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x + w0 + wc,	y + h0,			clr), GXWINDOW::IFP_RIGHT);

	if ( h1 > 0)
	{
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x,			y + h0 + hc,	clr), GXWINDOW::IFP_BOTTOMLEFT);
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x + w0,		y + h0 + hc,	clr), GXWINDOW::IFP_BOTTOM);
		ti.SetTileImage( GXWINDOW::CImageParams( nImageIndex, x + w0 + wc,	y + h0 + hc,	clr), GXWINDOW::IFP_BOTTOMRIGHT);
	}

	ti.SetTileSize( max( 0, wc), max( 0, hc));
	ti.SetGridSize( max( 0, w0), max( 0, h0), GXWINDOW::IFP_TOPLEFT);
	ti.SetGridSize( max( 0, componentw - w0 - w1), max( 0, componenth - h0 - h1), GXWINDOW::IFP_CENTER);
	ti.SetGridSize( max( 0, w1), max( 0, h1), GXWINDOW::IFP_BOTTOMRIGHT);
	ti.SetFillMode( bVTile, bHTile);
}

void CGuiGenerator::GenerateButton( GXWINDOW::CAbstractButton& ab, const wchar_t* name)
{
	GenerateComponent( ab, name);
}

void CGuiGenerator::GenerateCheckButton( GXWINDOW::CAbstractCheckButton& acb, const wchar_t* name)
{
	GenerateButton( acb, name);
	
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		acb.SetButtonBoxStyle( GXWINDOW::BBS_TILE);
		acb.SetBoxWidth( pchild->QueryIntegerAttribute( L"boxwidth", acb.GetWidth()));

		GXWINDOW::COLOR clr = pchild->QueryColorAttribute( L"b");

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_NORMAL))
			GenerateTileImage( acb.GetTileImageNormal(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, acb.GetWidth(), acb.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_MOUSEON))
			GenerateTileImage( acb.GetTileImageMouseIn(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, acb.GetWidth(), acb.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DOWN))
			GenerateTileImage( acb.GetTileImageSelect(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, acb.GetBoxWidth(), acb.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DOWNON))
			GenerateTileImage( acb.GetTileImageMouseInSelect(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, acb.GetBoxWidth(), acb.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DISABLE))
			GenerateTileImage( acb.GetTileImageDisable(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, acb.GetWidth(), acb.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));
	}
}

void CGuiGenerator::GenerateRadioButton( GXWINDOW::CAbstractRadioButton& arb, const wchar_t* name)
{
	GenerateCheckButton( arb, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
		arb.SetGroupID( pchild->QueryIntegerAttribute( L"groupid"));
}

void CGuiGenerator::GenerateTextable( GXWINDOW::CTextable& ta, const CComponentAttributes& ca)
{
	if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( ca.QueryAttribute( L"i")))
	{
		ta.SetComponentFont( ptf->nFontType);

		if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_NORMAL))
			ta.SetComponentTextColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_MOUSEON))
			ta.SetComponentTextMouseInColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_DISABLE))
			ta.SetComponentTextDisableColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
	}
	else
	{
		ta.SetComponentFont( ca.QueryIntegerAttribute( L"i"));
		ta.SetComponentTextDisableColor( ca.QueryColorAttribute( L"c0"));
		ta.SetComponentTextColor( ca.QueryColorAttribute( L"c1"));
		ta.SetComponentTextMouseInColor( ca.QueryColorAttribute( L"c3"));
	}

	ta.SetComponentText( ca.QueryAttribute( L"t"));
}

void CGuiGenerator::GenerateSimpleTextable( GXWINDOW::CSimpleTextable& sta, const wchar_t* name)
{
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( pchild->QueryAttribute( L"i")))
		{
			sta.SetTextFont( ptf->nFontType);

			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_NORMAL))
				sta.SetTextColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_SELECT))
				sta.SetTextSelectedColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_MOUSEON))
				sta.SetTextMouseOnColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		}
		else
		{
			sta.SetTextFont( pchild->QueryIntegerAttribute( L"i"));
			sta.SetTextColor( pchild->QueryColorAttribute( L"c1"));
			sta.SetTextMouseOnColor( pchild->QueryColorAttribute( L"c2"));
			sta.SetTextSelectedColor( pchild->QueryColorAttribute( L"c3"));
		}
	}
}

void CGuiGenerator::GenerateTextArea( GXWINDOW::CTextArea& ta, const wchar_t* name)
{
	GenerateComponent( ta, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( pchild->QueryAttribute( L"i")))
		{
			ta.SetDefaultFont( ptf->nFontType);
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_NORMAL))
				ta.SetDefaultTextColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);

		}
		else
		{
			ta.SetDefaultFont( pchild->QueryIntegerAttribute( L"i"));
			ta.SetDefaultTextColor( pchild->QueryColorAttribute( L"c"));
		}

		ta.AppendText( pchild->QueryAttribute( L"t"));

		ta.SetSingleLine( !pchild->QueryBoolAttribute( L"multiline"));
		ta.SetSelectAllow( pchild->QueryBoolAttribute( L"select"));
		ta.SetPassword( pchild->QueryBoolAttribute( L"password"));
	}
}

void CGuiGenerator::GenerateEditArea( GXWINDOW::CEditArea& ea, const wchar_t* name)
{
	GenerateTextArea( ea, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		ea.SetNumber( pchild->QueryBoolAttribute( L"number"));
		ea.SetLimit( pchild->QueryIntegerAttribute( L"limit", 1024));
	}

	if ( const CComponentAttributes* pdfcs = m_ca.QueryChildAttribute( DEFAULT_FOCUS))
	{
		if ( 0 == _wcsicmp( name, pdfcs->QueryAttribute( L"name")))
			ea.SetDefaultFocus();
	}
}

void CGuiGenerator::GenerateListDataModule( GXWINDOW::CListDataModule& ldm, const CComponentAttributes ca)
{
	if ( GXWINDOW::CTextListDataModule* ptldm = dynamic_cast<GXWINDOW::CTextListDataModule*>( &ldm))
	{
		if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( ca.QueryAttribute( L"i")))
		{
			ptldm->SetTextFont( ptf->nFontType);

			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_NORMAL))
				ptldm->SetTextColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_SELECT))
				ptldm->SetTextSelectedColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_MOUSEON))
				ptldm->SetTextMouseOnColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		}
		else
		{
			ptldm->SetTextFont( ca.QueryIntegerAttribute( L"i"));
			ptldm->SetTextColor( ca.QueryColorAttribute( L"c1"));
			ptldm->SetTextMouseOnColor( ca.QueryColorAttribute( L"c2"));
			ptldm->SetTextSelectedColor( ca.QueryColorAttribute( L"c3"));
		}
		ptldm->SetBackGroundMouseOnColor( ca.QueryColorAttribute( L"b1"));
		ptldm->SetBackGroundSelectedColor( ca.QueryColorAttribute( L"b3"));
	}
	else if ( GXWINDOW::CImageListDataModlue* pildm = dynamic_cast<GXWINDOW::CImageListDataModlue*>( &ldm))
	{
		pildm->SetImageMouseOnColor( ca.QueryColorAttribute( L"b1"));
		pildm->SetImageSelectedColor( ca.QueryColorAttribute( L"b3"));
	}
}

void CGuiGenerator::GenerateSubScrollBar( GXWINDOW::CScrollBar& scb, const CComponentAttributes& ca)
{
	int w = ca.QueryIntegerAttribute( L"w");
	int h = ca.QueryIntegerAttribute( L"h");
	scb.SetSize( w, h);
	scb.SetVertical( ca.QueryBoolAttribute( L"vertical"));
	scb.SetButtonSize( scb.GetVertical() ? w : h);
	scb.SetAdaptMode( ca.QueryBoolAttribute( L"autoblock", true) );
	
	GXWINDOW::COLOR clr = GXWINDOW::MakeColor( 255, 255, 255);

	if ( const CComponentAttributes* pBG = ca.QueryChildAttribute( BARSLOT_IMAGE))
	{
		int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pBG->QueryAttribute( L"f"));
		GXWINDOW::CImagesFill& bgf = scb.GetTilesBackGround();
		scb.SetBackGroundStyle( GXWINDOW::BGS_IMAGE);
		GenerateTileImage( bgf, nImageFileIndex, *pBG, scb.GetWidth(), scb.GetHeight(), clr, IsTileMode( pBG->QueryAttribute( L"mh")), IsTileMode( pBG->QueryAttribute( L"mw")));
	}
	else
		scb.SetBackGroundStyleColor( clr);

	if ( const CComponentAttributes* prub = ca.QueryChildAttribute( BARRUB_IMAGE_NORMAL))
	{
		scb.SetRubSize( scb.GetVertical() ? prub->QueryIntegerAttribute( L"h") : prub->QueryIntegerAttribute( L"w"));
		scb.SetSliderRubStyle( GXWINDOW::SRS_IMAGE);

		int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( prub->QueryAttribute( L"f"));
		GXWINDOW::CImagesFill& rubnormal = scb.GetRubTilesNormal();
		GenerateTileImage( rubnormal, nImageFileIndex, *prub, scb.GetWidth(), scb.GetHeight(), clr, IsTileMode( prub->QueryAttribute( L"mh")), IsTileMode( prub->QueryAttribute( L"mw")));
	}

	if ( const CComponentAttributes* prub = ca.QueryChildAttribute( BARRUB_IMAGE_MOUSEON))
	{
		int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( prub->QueryAttribute( L"f"));
		GXWINDOW::CImagesFill& rubnormal = scb.GetRubTilesMouseOn();
		GenerateTileImage( rubnormal, nImageFileIndex, *prub, scb.GetWidth(), scb.GetHeight(), clr, IsTileMode( prub->QueryAttribute( L"mh")), IsTileMode( prub->QueryAttribute( L"mw")));
	}

	if ( const CComponentAttributes* prub = ca.QueryChildAttribute( BARRUB_IMAGE_DOWN))
	{
		int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( prub->QueryAttribute( L"f"));
		GXWINDOW::CImagesFill& rubnormal = scb.GetRubTilesMoving();
		GenerateTileImage( rubnormal, nImageFileIndex, *prub, scb.GetWidth(), scb.GetHeight(), clr, IsTileMode( prub->QueryAttribute( L"mh")), IsTileMode( prub->QueryAttribute( L"mw")));
	}

	{
		GXWINDOW::CImageParams btns[4];

		if ( const CComponentAttributes* pubtn = ca.QueryChildAttribute( SCBUPBTN_IMAGE_NORMAL))
		{
			scb.SetButtonSize( scb.GetVertical() ? pubtn->QueryIntegerAttribute( L"h") : pubtn->QueryIntegerAttribute( L"w"));
			btns[0] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));
		}

		if ( const CComponentAttributes* pubtn = ca.QueryChildAttribute( SCBUPBTN_IMAGE_MOUSEON))
			btns[1] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

		if ( const CComponentAttributes* pubtn = ca.QueryChildAttribute( SCBUPBTN_IMAGE_DONW))
			btns[2] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

		if ( const CComponentAttributes* pubtn = ca.QueryChildAttribute( SCBUPBTN_IMAGE_DISABLE))
			btns[3] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

		scb.SetTopLeftButtonImage( btns[0], btns[1], btns[2], btns[3]);
	}

	{
		GXWINDOW::CImageParams btns[4];

		if ( const CComponentAttributes* pubtn = ca.QueryChildAttribute( SCBDNBTN_IMAGE_NORMAL))
			btns[0] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

		if ( const CComponentAttributes* pubtn = ca.QueryChildAttribute( SCBDNBTN_IMAGE_MOUSEON))
			btns[1] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

		if ( const CComponentAttributes* pubtn = ca.QueryChildAttribute( SCBDNBTN_IMAGE_DONW))
			btns[2] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

		if ( const CComponentAttributes* pubtn = ca.QueryChildAttribute( SCBDNBTN_IMAGE_DISABLE))
			btns[3] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

		scb.SetRightBottomButtonImage( btns[0], btns[1], btns[2], btns[3]);
	}
}

void CGuiGenerator::GenerateComponent( GXWINDOW::CComponent& c, const wchar_t* name)
{
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		c.SetComponentName( name);
		c.SetPos( pchild->QueryIntegerAttribute( L"x", 0), pchild->QueryIntegerAttribute( L"y", 0));
		c.SetSize( pchild->QueryIntegerAttribute( L"w", 0), pchild->QueryIntegerAttribute( L"h", 0));
		c.SetViewOnly( pchild->QueryBoolAttribute( L"viewonly"));
		if ( CGuiGenerator::s_bSetTransparentAttr)
			c.SetTransparent( pchild->QueryBoolAttribute( L"transparent"));
		
		int nTip = pchild->QueryIntegerAttribute( L"tipinfo", -1);
		if ( nTip >= 0)
			c.SetTipText( nTip);
		else if ( const wchar_t* sTip = pchild->QueryAttribute( L"tipinfo"))
			c.SetTipText( sTip);
	}
	else
	{
		// assert( 0 && "不允许配置uixml文件中不存在的控件!"); 我干！！！！
	}
}

void CGuiGenerator::GenerateBKComponent( GXWINDOW::CBKComponent& c, const wchar_t* name)
{
	GenerateComponent( c, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GXWINDOW::COLOR bgcolor = pchild->QueryColorAttribute( L"b");

		if ( const CComponentAttributes* pBG = pchild->QueryChildAttribute( BACKGROUND_IMAGE))
		{
			int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pBG->QueryAttribute( L"f"));
			GXWINDOW::CImagesFill& bgf = c.GetTilesBackGround();
			c.SetBackGroundStyle( GXWINDOW::BGS_IMAGE);
			GenerateTileImage( bgf, nImageFileIndex, *pBG, c.GetWidth(), c.GetHeight(), bgcolor, IsTileMode( pBG->QueryAttribute( L"mh")), IsTileMode( pBG->QueryAttribute( L"mw")));
		}
		else
			c.SetBackGroundStyleColor( bgcolor);
	}
}

void CGuiGenerator::GenerateSliderBar( GXWINDOW::CSliderBar& sb, const wchar_t* name)
{
	GenerateBKPanel( sb, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GXWINDOW::COLOR clr = GXWINDOW::MakeColor( 255, 255, 255);

		sb.SetVertical( pchild->QueryBoolAttribute( L"vertical"));

		if ( const CComponentAttributes* prub = pchild->QueryChildAttribute( BARRUB_IMAGE_NORMAL))
		{
			sb.SetRubSize( sb.GetVertical() ? prub->QueryIntegerAttribute( L"h") : prub->QueryIntegerAttribute( L"w"));
			sb.SetSliderRubStyle( GXWINDOW::SRS_IMAGE);

			int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( prub->QueryAttribute( L"f"));
			GXWINDOW::CImagesFill& rubnormal = sb.GetRubTilesNormal();
			GenerateTileImage( rubnormal, nImageFileIndex, *prub, sb.GetWidth(), sb.GetHeight(), clr, IsTileMode( prub->QueryAttribute( L"mh")), IsTileMode( prub->QueryAttribute( L"mw")));
		}

		if ( const CComponentAttributes* prub = pchild->QueryChildAttribute( BARRUB_IMAGE_MOUSEON))
		{
			int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( prub->QueryAttribute( L"f"));
			GXWINDOW::CImagesFill& rubnormal = sb.GetRubTilesMouseOn();
			GenerateTileImage( rubnormal, nImageFileIndex, *prub, sb.GetWidth(), sb.GetHeight(), clr, IsTileMode( prub->QueryAttribute( L"mh")), IsTileMode( prub->QueryAttribute( L"mw")));
		}

		if ( const CComponentAttributes* prub = pchild->QueryChildAttribute( BARRUB_IMAGE_DOWN))
		{
			int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( prub->QueryAttribute( L"f"));
			GXWINDOW::CImagesFill& rubnormal = sb.GetRubTilesMoving();
			GenerateTileImage( rubnormal, nImageFileIndex, *prub, sb.GetWidth(), sb.GetHeight(), clr, IsTileMode( prub->QueryAttribute( L"mh")), IsTileMode( prub->QueryAttribute( L"mw")));
		}
	}
}

void CGuiGenerator::GenerateProgressBar( GXWINDOW::CProgressBar& pb, const wchar_t* name)
{
	GeneratePanel( pb, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		if ( const CComponentAttributes* pp = pchild->QueryChildAttribute( BACKGROUND_IMAGE))
		{
			int w0 = pp->QueryIntegerAttribute( L"w0");
			int w1 = pp->QueryIntegerAttribute( L"w1");
			int h0 = pp->QueryIntegerAttribute( L"h0");
			int h1 = pp->QueryIntegerAttribute( L"h1");

			if ( !w0 && !w1 && !h0 && !h1)
				pb.SetProgressBarStyleImage( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pp->QueryAttribute( L"f")), pp->QueryIntegerAttribute( L"x"), pp->QueryIntegerAttribute( L"y")));
			else if ( w0 && !w1)
				pb.SetProgressBarStyleSimpleTile( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pp->QueryAttribute( L"f")), pp->QueryIntegerAttribute( L"x"), pp->QueryIntegerAttribute( L"y")), pp->QueryIntegerAttribute( L"w"), pp->QueryIntegerAttribute( L"h"));
			else
			{
				pb.SetProgressBarStyle( GXWINDOW::PBS_TILE);
				GenerateTileImage( pb.GetProgressBarImageFill(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pp->QueryAttribute( L"f")), *pp, pp->QueryIntegerAttribute( L"w"), pp->QueryIntegerAttribute( L"h"), pchild->QueryColorAttribute( L"b"), IsTileMode( pp->QueryAttribute( L"mh")), IsTileMode( pp->QueryAttribute( L"mw")));
			}
		}
	}
}

void CGuiGenerator::GenerateScrollBar( GXWINDOW::CScrollBar& sb, const wchar_t* name)
{
	GenerateSliderBar( sb, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		sb.SetAdaptMode( pchild->QueryBoolAttribute( L"autoblock", true) );

		{
			GXWINDOW::CImageParams btns[4];

			if ( const CComponentAttributes* pubtn = pchild->QueryChildAttribute( SCBUPBTN_IMAGE_NORMAL))
			{
				sb.SetButtonSize( sb.GetVertical() ? pubtn->QueryIntegerAttribute( L"h") : pubtn->QueryIntegerAttribute( L"w"));
				btns[0] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));
			}

			if ( const CComponentAttributes* pubtn = pchild->QueryChildAttribute( SCBUPBTN_IMAGE_MOUSEON))
				btns[1] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

			if ( const CComponentAttributes* pubtn = pchild->QueryChildAttribute( SCBUPBTN_IMAGE_DONW))
				btns[2] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

			if ( const CComponentAttributes* pubtn = pchild->QueryChildAttribute( SCBUPBTN_IMAGE_DISABLE))
				btns[3] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

			sb.SetTopLeftButtonImage( btns[0], btns[1], btns[2], btns[3]);
		}

		{
			GXWINDOW::CImageParams btns[4];

			if ( const CComponentAttributes* pubtn = pchild->QueryChildAttribute( SCBDNBTN_IMAGE_NORMAL))
				btns[0] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

			if ( const CComponentAttributes* pubtn = pchild->QueryChildAttribute( SCBDNBTN_IMAGE_MOUSEON))
				btns[1] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

			if ( const CComponentAttributes* pubtn = pchild->QueryChildAttribute( SCBDNBTN_IMAGE_DONW))
				btns[2] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

			if ( const CComponentAttributes* pubtn = pchild->QueryChildAttribute( SCBDNBTN_IMAGE_DISABLE))
				btns[3] = GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pubtn->QueryAttribute( L"f")), pubtn->QueryIntegerAttribute( L"x"), pubtn->QueryIntegerAttribute( L"y"));

			sb.SetRightBottomButtonImage( btns[0], btns[1], btns[2], btns[3]);
		}
	}
}

void CGuiGenerator::GenerateImageButton( GXWINDOW::CImageButton& ib, const wchar_t* name)
{
	GenerateButton( ib, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_NORMAL))
			ib.SetComponentImageNormal( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), pchild->QueryColorAttribute( L"b")));
		
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_MOUSEON))
			ib.SetComponentImageMouseIn( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), pchild->QueryColorAttribute( L"b")));

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DOWN))
			ib.SetImageButtonDown( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), pchild->QueryColorAttribute( L"b")));

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DISABLE))
			ib.SetComponentImageDisable( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), pchild->QueryColorAttribute( L"b")));
	}
}

void CGuiGenerator::GenerateTextButton( GXWINDOW::CTextButton& tb, const wchar_t* name)
{
	GenerateButton( tb, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GenerateTextable( tb, *pchild);

		if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( pchild->QueryAttribute( L"i")))
		{
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_DOWN))
				tb.SetComponentTextMouseDownColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		}
		else
		{
			tb.SetComponentTextMouseDownColor( pchild->QueryColorAttribute( L"c2"));
		}
	}
}

void CGuiGenerator::GenerateTileImageButton( GXWINDOW::CTileImageButton& tib, const wchar_t* name)
{
	GenerateButton( tib, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GXWINDOW::COLOR clr = pchild->QueryColorAttribute( L"b");

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_NORMAL))
			GenerateTileImage( tib.GetTileImageNormal(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, tib.GetWidth(), tib.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_MOUSEON))
			GenerateTileImage( tib.GetTileImageMouseIn(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, tib.GetWidth(), tib.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DOWN))
			GenerateTileImage( tib.GetTileImageButtonDown(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, tib.GetWidth(), tib.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DISABLE))
			GenerateTileImage( tib.GetTileImageDisable(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), *pbtn, tib.GetWidth(), tib.GetHeight(), clr, IsTileMode( pbtn->QueryAttribute( L"mh")), IsTileMode( pbtn->QueryAttribute( L"mw")));
	}
}

void CGuiGenerator::GenerateTextImageButton( GXWINDOW::CTextImageButton& tib, const wchar_t* name)
{
	GenerateTileImageButton( tib, name);
	
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GenerateTextable( tib, *pchild);

		if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( pchild->QueryAttribute( L"i")))
		{
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_DOWN))
				tib.SetComponentTextMouseDownColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		}
		else
		{
			tib.SetComponentTextMouseDownColor( pchild->QueryColorAttribute( L"c2"));
		}
	}
}

void CGuiGenerator::GenerateImageCheckButton( GXWINDOW::CImageCheckButton& icb, const wchar_t* name)
{
	GenerateCheckButton( icb, name);
}

void CGuiGenerator::GenerateTextCheckButton( GXWINDOW::CTextCheckButton& tcb, const wchar_t* name)
{
	GenerateCheckButton( tcb, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
		GenerateTextable( tcb, *pchild);
}

void CGuiGenerator::GenerateImageRadioButton( GXWINDOW::CImageRadioButton& irb, const wchar_t* name)
{
	GenerateRadioButton( irb, name);
}

void CGuiGenerator::GenerateTextRadioButton( GXWINDOW::CTextRadioButton& trb, const wchar_t* name)
{
	GenerateRadioButton( trb, name);
	
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
		GenerateTextable( trb, *pchild);
}

void CGuiGenerator::GenerateTextImageRadioButton( GXWINDOW::CTextImageRadioButton& tirb, const wchar_t* name)
{
	GenerateRadioButton( tirb, name);
	
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GenerateTextable( tirb, *pchild);

		if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( pchild->QueryAttribute( L"i")))
		{
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_SELECT))
				tirb.SetComponentTextSelectedColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_SELECT_MOUSEON))
				tirb.SetComponentTextSelectedMouseInColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		}
	}
}

void CGuiGenerator::GenerateTextImageCheckButton( GXWINDOW::CTextImageCheckButton& ticb, const wchar_t* name)
{
	GenerateCheckButton( ticb, name);
	
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GenerateTextable( ticb, *pchild);

		if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( pchild->QueryAttribute( L"i")))
		{
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_SELECT))
				ticb.SetComponentTextSelectedColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_SELECT_MOUSEON))
				ticb.SetComponentTextSelectedMouseInColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		}
	}
}

void CGuiGenerator::GenerateCombinationEditArea( GXWINDOW::CCombinationEditArea& cea, const wchar_t* name)
{
	GenerateEditArea( cea.GetEditArea(), name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		cea.SetPos( pchild->QueryIntegerAttribute( L"x", 0), pchild->QueryIntegerAttribute( L"y", 0));
		cea.SetSize( pchild->QueryIntegerAttribute( L"w", 0), pchild->QueryIntegerAttribute( L"h", 0));

		const CComponentAttributes::AttrMap& children = pchild->GetChildMap();
		if ( !children.empty())
			GenerateSubScrollBar( *cea.GetScrollPanel().GetVerticalScrollBar(), children.begin()->second);
	}
}

void CGuiGenerator::GenerateCombinationSingleLineEditArea( GXWINDOW::CCombinationSingleLineEditArea& cslea, const wchar_t* name)
{
	GenerateCombinationEditArea( cslea, name);
}

void CGuiGenerator::GeneratePanel( GXWINDOW::CPanel& p, const wchar_t* name)
{
	GenerateComponent( p, name);
}

void CGuiGenerator::GenerateBKPanel( GXWINDOW::CBKPanel& bp, const wchar_t* name)
{
	GeneratePanel( bp, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GXWINDOW::COLOR bgcolor = pchild->QueryColorAttribute( L"b");

		const wchar_t* bgimgtype = BACKGROUND_IMAGE;
		if ( GXWINDOW::CSliderBar* psb = dynamic_cast<GXWINDOW::CSliderBar*>( &bp))	// 调整下类型, 因为有些控件的图片资源是以背景为基础的
			bgimgtype = BARSLOT_IMAGE;

		if ( const CComponentAttributes* pBG = pchild->QueryChildAttribute( bgimgtype))
		{
			int nImageFileIndex = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pBG->QueryAttribute( L"f"));
			GXWINDOW::CImagesFill& bgf = bp.GetTilesBackGround();
			bp.SetBackGroundStyle( GXWINDOW::BGS_IMAGE);
			GenerateTileImage( bgf, nImageFileIndex, *pBG, bp.GetWidth(), bp.GetHeight(), bgcolor, IsTileMode( pBG->QueryAttribute( L"mh")), IsTileMode( pBG->QueryAttribute( L"mw")));
		}
		else
			bp.SetBackGroundStyleColor( bgcolor);
	}
}

void CGuiGenerator::GenerateFrame( GXWINDOW::CFrame& f, const wchar_t* name)
{
	GenerateBKPanel( f, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{	
		if ( f.GetPosX() == 0 && f.GetPosY() == 0)
			f.SetPos( pchild->QueryIntegerAttribute( L"x_1024_768", 0), pchild->QueryIntegerAttribute( L"y_1024_768", 0));

		std::wstring value = pchild->QueryAttribute( L"movetype");
		if ( 0 == _wcsicmp( value.c_str(), L"all"))
			f.SetMoveStyle( GXWINDOW::FMS_ALL);
		else if ( 0 == _wcsicmp( value.c_str(), L"title"))
			f.SetMoveStyle( GXWINDOW::FMS_TITLE);
		else if ( 0 == _wcsicmp( value.c_str(), L"body"))
			f.SetMoveStyle( GXWINDOW::FMS_BODY);
		else
			f.SetMoveStyle( GXWINDOW::FMS_NONE);
		f.SetTitleHeight( pchild->QueryIntegerAttribute( L"titleheight", 0));
		
		f.SetAutoClose( pchild->QueryBoolAttribute( L"autoclose"));
		f.SetViewAsPopup( pchild->QueryBoolAttribute( L"popup"));
		
		if ( const wchar_t* pdf = pchild->QueryAttribute( DEFAULT_FOCUS))
		{
			CComponentAttributes df;
			df.InsertAttribute( L"name", pdf);
			m_ca.InsertChildAttribute( DEFAULT_FOCUS, df);
		}
	}
}

void CGuiGenerator::GenerateImageComponent( GXWINDOW::CImageComponent& ic, const wchar_t* name)
{
	GenerateComponent( ic, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_NORMAL))
			ic.SetComponentImageNormal( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), pchild->QueryColorAttribute( L"b")));

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_MOUSEON))
			ic.SetComponentImageMouseIn( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), pchild->QueryColorAttribute( L"b")));

		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DISABLE))
			ic.SetComponentImageDisable( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), pchild->QueryColorAttribute( L"b")));
	}
}

void CGuiGenerator::GenerateTileImageComponent( GXWINDOW::CTileImageComponent& tic, const wchar_t* name){}

void CGuiGenerator::GenerateListBox( GXWINDOW::CListBox& lb, GXWINDOW::CListDataModule& ldm, const wchar_t* name)
{
	GenerateBKComponent( lb, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		// 配置datamodule
		GenerateListDataModule( ldm, *pchild);

		lb.SetClientComponentHeight( pchild->QueryIntegerAttribute( L"lineheight", 20));
		lb.SetMultiSelect( pchild->QueryBoolAttribute( L"multiselect"));
		lb.SetListDataModule( ldm);

		GXWINDOW::COLOR clr = pchild->QueryColorAttribute( L"b");

		if ( const CComponentAttributes* pil = pchild->QueryChildAttribute( LISTBOX_IMAGE_LINE))
			GenerateTileImage( ldm.GetRowBackGround(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pil->QueryAttribute( L"f")), *pil, pil->QueryIntegerAttribute( L"x"), pil->QueryIntegerAttribute( L"y"), clr, IsTileMode( pil->QueryAttribute( L"mh")), IsTileMode( pil->QueryAttribute( L"mw")));

		if ( const CComponentAttributes* pml = pchild->QueryChildAttribute( LISTBOX_MOUSEON_LINE))
			GenerateTileImage( lb.GetMouseOnHighLightImage(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pml->QueryAttribute( L"f")), pml->QueryIntegerAttribute(L"x"), pml->QueryIntegerAttribute(L"y"), pml->QueryIntegerAttribute(L"w"), pml->QueryIntegerAttribute(L"h"), pml->QueryIntegerAttribute(L"w0"), pml->QueryIntegerAttribute(L"h0"), pml->QueryIntegerAttribute(L"w1"), pml->QueryIntegerAttribute(L"h1"), lb.GetWidth(), lb.GetClientComponentHeight(), 0xffffffff, IsTileMode(pml->QueryAttribute(L"mh")), IsTileMode((pml->QueryAttribute(L"mw"))));

		if ( const CComponentAttributes* psl = pchild->QueryChildAttribute( LISTBOX_SELECTED_LINE))
			GenerateTileImage( lb.GetSelectedHightLightImage(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( psl->QueryAttribute( L"f")), psl->QueryIntegerAttribute(L"x"), psl->QueryIntegerAttribute(L"y"), psl->QueryIntegerAttribute(L"w"), psl->QueryIntegerAttribute(L"h"), psl->QueryIntegerAttribute(L"w0"), psl->QueryIntegerAttribute(L"h0"), psl->QueryIntegerAttribute(L"w1"), psl->QueryIntegerAttribute(L"h1"), lb.GetWidth(), lb.GetClientComponentHeight(), 0xffffffff, IsTileMode(psl->QueryAttribute(L"mh")), IsTileMode((psl->QueryAttribute(L"mw"))));

	}
}

void CGuiGenerator::GenerateComboBox( GXWINDOW::CComboBox& cb, GXWINDOW::CListDataModule& ldm, const wchar_t* name)
{
	GenerateBKComponent( cb, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GXWINDOW::COLOR clr = 0xffffffff;

		if ( const CComponentAttributes* pib = pchild->QueryChildAttribute( COMBOBOX_IMAGE_BOX))
			GenerateTileImage( cb.GetTilesBackGround(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pib->QueryAttribute( L"f")), *pib, cb.GetWidth(), cb.GetHeight(), clr, IsTileMode( pib->QueryAttribute( L"mh")), IsTileMode( pib->QueryAttribute( L"mw")));
		
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_NORMAL))
			cb.GetButton()->SetComponentImageNormal( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), clr));
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_MOUSEON))
			cb.GetButton()->SetComponentImageMouseIn( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), clr));
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DOWN))
			cb.GetButton()->SetImageButtonDown( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), clr));
		if ( const CComponentAttributes* pbtn = pchild->QueryChildAttribute( BUTTON_IMAGE_DISABLE))
			cb.GetButton()->SetComponentImageDisable( GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pbtn->QueryAttribute( L"f")), pbtn->QueryIntegerAttribute( L"x"), pbtn->QueryIntegerAttribute( L"y"), clr));

		if ( const CComponentAttributes* pil = pchild->QueryChildAttribute( COMBOBOX_IMAGE_LIST))
			GenerateTileImage( cb.GetDrawListBackGround(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pil->QueryAttribute( L"f")), *pil, cb.GetWidth(), pchild->QueryIntegerAttribute( L"listboxh"), clr, IsTileMode( pil->QueryAttribute( L"mh")), IsTileMode( pil->QueryAttribute( L"mw")));
		else
			cb.SetDrawListBackGroundColor( pchild->QueryColorAttribute( L"b"));

		const CComponentAttributes::AttrMap& children = pchild->GetChildMap();
		for ( CComponentAttributes::AttrMap::const_iterator it = children.begin(), ite = children.end(); it != ite; ++ it)
		{
			const CComponentAttributes& cas = it->second;

			const wchar_t* value = cas.QueryAttribute( L"vertical", NULL);
			if( NULL == value)
				continue;

			if(  0 == _wcsicmp( value, L"true"))
				GenerateSubScrollBar( *cb.GetScrollPanel()->GetVerticalScrollBar(), cas);
			else
				GenerateSubScrollBar( *cb.GetScrollPanel()->GetHorizontalScrollBar(), cas);
		}

		GenerateListDataModule( ldm, *pchild);
		cb.SetListDataModule( ldm);
		cb.SetClientComponentHeight( pchild->QueryIntegerAttribute( L"lineheight"));
		cb.SetButtonWidth( pchild->QueryIntegerAttribute( L"btnwidth"));
		cb.SetListBoxSize( pchild->QueryIntegerAttribute( L"listboxw"), pchild->QueryIntegerAttribute( L"listboxh"));
		cb.SetBorderWidth( pchild->QueryIntegerAttribute( L"border"));

		if ( const CComponentAttributes* pml = pchild->QueryChildAttribute( COMBOBOX_MOUSEON_LINE))
			GenerateTileImage( cb.GetMouseOnHighLightImage(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pml->QueryAttribute( L"f")), pml->QueryIntegerAttribute(L"x"), pml->QueryIntegerAttribute(L"y"), pml->QueryIntegerAttribute(L"w"), pml->QueryIntegerAttribute(L"h"), pml->QueryIntegerAttribute(L"w0"), pml->QueryIntegerAttribute(L"h0"), pml->QueryIntegerAttribute(L"w1"), pml->QueryIntegerAttribute(L"h1"), cb.GetListBoxWidth(), cb.GetClientComponentHeight(), 0xffffffff, IsTileMode(pml->QueryAttribute(L"mh")), IsTileMode((pml->QueryAttribute(L"mw"))));

		if ( const CComponentAttributes* psl = pchild->QueryChildAttribute( COMBOBOX_SELECTED_LINE))
			GenerateTileImage( cb.GetSelectedHightLightImage(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( psl->QueryAttribute( L"f")), psl->QueryIntegerAttribute(L"x"), psl->QueryIntegerAttribute(L"y"), psl->QueryIntegerAttribute(L"w"), psl->QueryIntegerAttribute(L"h"), psl->QueryIntegerAttribute(L"w0"), psl->QueryIntegerAttribute(L"h0"), psl->QueryIntegerAttribute(L"w1"), psl->QueryIntegerAttribute(L"h1"), cb.GetListBoxWidth(), cb.GetClientComponentHeight(), 0xffffffff, IsTileMode(psl->QueryAttribute(L"mh")), IsTileMode((psl->QueryAttribute(L"mw"))));
	}
}

void CGuiGenerator::GenerateScrollPanel( GXWINDOW::CScrollPanel& sp, const wchar_t* name)
{
	GenerateComponent( sp, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		const CComponentAttributes::AttrMap& children = pchild->GetChildMap();
		for ( CComponentAttributes::AttrMap::const_iterator it = children.begin(), ite = children.end(); it != ite; ++ it)
		{
			if ( it->second.QueryBoolAttribute( L"vertical"))
				GenerateSubScrollBar( *sp.GetVerticalScrollBar(), it->second);
			else
				GenerateSubScrollBar( *sp.GetHorizontalScrollBar(), it->second);
		}
	}
}

void CGuiGenerator::GenerateTabControlButton( GXWINDOW::CAbstractRadioButton* prb, const CComponentAttributes& ca, int btnw, int btnh, GXWINDOW::COLOR clr)
{
	prb->SetButtonBoxStyle( GXWINDOW::BBS_TILE);

	if ( const CComponentAttributes* pimg = ca.QueryChildAttribute( BUTTON_IMAGE_NORMAL))
		GenerateTileImage( prb->GetTileImageNormal(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pimg->QueryAttribute( L"f")), *pimg, btnw, btnh, clr, IsTileMode( pimg->QueryAttribute( L"mh")), IsTileMode( pimg->QueryAttribute( L"mw")));
	if ( const CComponentAttributes* pimg = ca.QueryChildAttribute( BUTTON_IMAGE_MOUSEON))
		GenerateTileImage( prb->GetTileImageMouseIn(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pimg->QueryAttribute( L"f")), *pimg, btnw, btnh, clr, IsTileMode( pimg->QueryAttribute( L"mh")), IsTileMode( pimg->QueryAttribute( L"mw")));
	if ( const CComponentAttributes* pimg = ca.QueryChildAttribute( BUTTON_IMAGE_DOWN))
		GenerateTileImage( prb->GetTileImageSelect(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pimg->QueryAttribute( L"f")), *pimg, btnw, btnh, clr, IsTileMode( pimg->QueryAttribute( L"mh")), IsTileMode( pimg->QueryAttribute( L"mw")));
	if ( const CComponentAttributes* pimg = ca.QueryChildAttribute( BUTTON_IMAGE_DOWNON))
		GenerateTileImage( prb->GetTileImageMouseInSelect(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pimg->QueryAttribute( L"f")), *pimg, btnw, btnh, clr, IsTileMode( pimg->QueryAttribute( L"mh")), IsTileMode( pimg->QueryAttribute( L"mw")));
	if ( const CComponentAttributes* pimg = ca.QueryChildAttribute( BUTTON_IMAGE_DISABLE))
		GenerateTileImage( prb->GetTileImageDisable(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pimg->QueryAttribute( L"f")), *pimg, btnw, btnh, clr, IsTileMode( pimg->QueryAttribute( L"mh")), IsTileMode( pimg->QueryAttribute( L"mw")));

	if ( GXWINDOW::CTextImageRadioButton* ptirb = dynamic_cast<GXWINDOW::CTextImageRadioButton*>( prb))
	{
		GenerateTextable( *ptirb, ca);
		ptirb->SetComponentText( ca.QueryAttribute( L"n"));

		if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( ca.QueryAttribute( L"i")))
		{
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_SELECT))
				ptirb->SetComponentTextSelectedColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
			if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_SELECT_MOUSEON))
				ptirb->SetComponentTextSelectedMouseInColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
		}
	}
}

void CGuiGenerator::GenerateTabControl( GXWINDOW::CTabControl& tc, const wchar_t* name)
{
	GenerateBKComponent( tc, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		int btnw = pchild->QueryIntegerAttribute( L"btnwidth");
		int btnh = pchild->QueryIntegerAttribute( L"btnheight");

		tc.SetButtonWidth( btnw);
		tc.SetButtonHeight( btnh);
		tc.SetTitleSize( pchild->QueryIntegerAttribute( L"headsize", 20));
		tc.SetTitleOffset( pchild->QueryIntegerAttribute( L"btnborder"));

		GXWINDOW::COLOR clr = GXWINDOW::MakeColor( 255, 255, 255);

		CComponentAttributes::AttrVec btns;
		for ( CComponentAttributes::AttrOrder::const_iterator it = pchild->GetChildOrder().begin(), ite = pchild->GetChildOrder().end(); it != ite; ++ it)
		{
			if (  0 == _wcsicmp( BACKGROUND_IMAGE, (*it).c_str()))
				continue;

			btns.push_back( *( pchild->QueryChildAttribute( (*it).c_str())));
		}

		//for ( CComponentAttributes::AttrMap::const_iterator it = pchild->GetChildMap().begin(), ite = pchild->GetChildMap().end(); it != ite; ++ it)
		//{
		//	if ( 0 == _wcsicmp( BACKGROUND_IMAGE, it->first.c_str()))
		//		continue;
		//	btns.push_back( it->second);
		//}

		if ( btns.size() > 1)
		{
			int index = 0;
			for (  CComponentAttributes::AttrVec::const_iterator it = btns.begin(), ite = btns.end(); it != ite; ++ it, ++ index)
				GenerateTabControlButton( tc.GetRadioButton( index), *it, btnw, btnh, clr);
		}
		else if ( btns.size() == 1)
		{
			const CComponentAttributes& btn0 = *btns.begin();
			GenerateTabControlButton( tc.GetRadioButton( 0), btn0, btnw, btnh, clr);

			for ( int i = 0; i < btn0.GetSlotCount(); ++ i)
				GenerateTabControlButton( tc.GetRadioButton( i + 1), btn0.GetSlotAttribute( i), btnw, btnh, clr);
		}

		tc.SetTitleVertical( pchild->QueryBoolAttribute( L"vertical"));
		tc.SetTitleRightBottom( pchild->QueryBoolAttribute( L"reverse"));
	}
}

void CGuiGenerator::GenerateTable( GXWINDOW::CTable& t, GXWINDOW::CTableDataModule& tdm, const wchar_t* name)
{
	GenerateBKComponent( t, name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		t.SetTableDataModule( tdm);

		// 配置datamodule
		{
			if ( GXWINDOW::CTextTableDataModule* pttdm = dynamic_cast<GXWINDOW::CTextTableDataModule*>( &tdm))
			{
				pttdm->SetBackMouseOnColor( pchild->QueryColorAttribute( L"b1"));
				pttdm->SetBackSelectedColor( pchild->QueryColorAttribute( L"b3"));
				GenerateSimpleTextable( *pttdm, name);

				// 设置表头字样
				if ( TEXTFORMAT* ptf = s_tfm.QueryTextFormat( pchild->QueryAttribute( L"headfont")))
				{
					pttdm->SetTableHeaderFont( ptf->nFontType);

					if ( TEXTSTATE* pts = ptf->textStateMap.QueryTextState( TSF_NORMAL))
						pttdm->SetTableHeaderColor( pts->color, pts->effect, pts->effectColor, pts->bGraudal, pts->color2);
				}

				bool bColSizable = pchild->QueryBoolAttribute( L"dragwidth");
				if ( const CComponentAttributes* pcol0 = pchild->QueryChildAttribute( L"Column"))
				{
					if ( const CComponentAttributes* phdimg0 = pcol0->QueryChildAttribute( BACKGROUND_IMAGE))
					{
						pttdm->InsertColumn( L"");
						tdm.FireDataChanged();
						t.GetTableHeader().SetHeaderCellImage( 0, GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( phdimg0->QueryAttribute( L"f")), phdimg0->QueryIntegerAttribute( L"x"), phdimg0->QueryIntegerAttribute( L"y"), pcol0->QueryColorAttribute( L"b")),
							phdimg0->QueryIntegerAttribute( L"w"), phdimg0->QueryIntegerAttribute( L"h"));
					}
					else
					{
						pttdm->InsertColumn( pcol0->QueryAttribute( L"t"));
						tdm.FireDataChanged();
					}
					t.GetTableHeader().GetHeaderColumn( 0).bSizable = bColSizable;
					t.GetTableHeader().GetHeaderColumn( 0).nWidth = pcol0->QueryIntegerAttribute( L"w");

					for ( int i = 0; i < pcol0->GetSlotCount(); ++ i)
					{
						const CComponentAttributes& ca = pcol0->GetSlotAttribute( i);
						if ( const CComponentAttributes* phdimg = ca.QueryChildAttribute( BACKGROUND_IMAGE))
						{
							pttdm->InsertColumn( L"");
							tdm.FireDataChanged();
							t.GetTableHeader().SetHeaderCellImage( i + 1, GXWINDOW::CImageParams( GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( phdimg->QueryAttribute( L"f")), phdimg->QueryIntegerAttribute( L"x"), phdimg->QueryIntegerAttribute( L"y"), ca.QueryColorAttribute( L"b")),
								phdimg->QueryIntegerAttribute( L"w"), phdimg->QueryIntegerAttribute( L"h"));
						}
						else
						{
							pttdm->InsertColumn( ca.QueryAttribute( L"t"));
							tdm.FireDataChanged();
						}
						t.GetTableHeader().GetHeaderColumn( i + 1).bSizable = bColSizable;
						t.GetTableHeader().GetHeaderColumn( i + 1).nWidth = ca.QueryIntegerAttribute( L"w");
					}
				}
			}

			bool bUseImageGrid = false;
			if ( const CComponentAttributes* pih = pchild->QueryChildAttribute( L"ImageHead"))
			{
				bUseImageGrid = true;
				tdm.SetGridStyle( GXWINDOW::CTableDataModule::GRID_IMAGE);
				GenerateTileImage( tdm.GetHeaderGridImagesFill(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pih->QueryAttribute( L"f")), *pih, pih->QueryIntegerAttribute( L"w"), pih->QueryIntegerAttribute( L"h"), GXWINDOW::MakeColor( 255, 255, 255), IsTileMode( pih->QueryAttribute( L"mh")), IsTileMode( pih->QueryAttribute( L"mw")));
			}
			if ( const CComponentAttributes* pic = pchild->QueryChildAttribute( L"ImageCell"))
			{
				bUseImageGrid = true;
				tdm.SetGridStyle( GXWINDOW::CTableDataModule::GRID_IMAGE);
				GenerateTileImage( tdm.GetCellGridImagesFill(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pic->QueryAttribute( L"f")), *pic, pic->QueryIntegerAttribute( L"w"), pic->QueryIntegerAttribute( L"h"), GXWINDOW::MakeColor( 255, 255, 255), IsTileMode( pic->QueryAttribute( L"mh")), IsTileMode( pic->QueryAttribute( L"mw")));
			}
			if ( !bUseImageGrid)
			{
				tdm.SetGridStyle( GXWINDOW::CTableDataModule::GRID_LINE);
				tdm.SetGridColor( pchild->QueryColorAttribute( L"gridcolor"));
			}
		}

		t.GetTableHeader().FireTableHeaderColumnSizeChanged();

		if ( int hh = pchild->QueryIntegerAttribute( L"headheight"))
			t.SetTableHeaderHeight( hh);
		else
			t.SetTableHeaderHeight( 1);
		t.SetRowHeight( pchild->QueryIntegerAttribute( L"lineheight"));

		if ( const CComponentAttributes* pml = pchild->QueryChildAttribute( TABLE_MOUSEON_LINE))
			GenerateTileImage( t.GetRowMouseOnImage(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( pml->QueryAttribute( L"f")), pml->QueryIntegerAttribute(L"x"), pml->QueryIntegerAttribute(L"y"), pml->QueryIntegerAttribute(L"w"), pml->QueryIntegerAttribute(L"h"), pml->QueryIntegerAttribute(L"w0"), pml->QueryIntegerAttribute(L"h0"), pml->QueryIntegerAttribute(L"w1"), pml->QueryIntegerAttribute(L"h1"), t.GetWidth(), t.GetRowHeight(), 0xffffffff, IsTileMode(pml->QueryAttribute(L"mh")), IsTileMode((pml->QueryAttribute(L"mw"))));

		if ( const CComponentAttributes* psl = pchild->QueryChildAttribute( TABLE_SELECTED_LINE))
			GenerateTileImage( t.GetRowSelectedImage(), GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( psl->QueryAttribute( L"f")), psl->QueryIntegerAttribute(L"x"), psl->QueryIntegerAttribute(L"y"), psl->QueryIntegerAttribute(L"w"), psl->QueryIntegerAttribute(L"h"), psl->QueryIntegerAttribute(L"w0"), psl->QueryIntegerAttribute(L"h0"), psl->QueryIntegerAttribute(L"w1"), psl->QueryIntegerAttribute(L"h1"), t.GetWidth(), t.GetRowHeight(), 0xffffffff, IsTileMode(psl->QueryAttribute(L"mh")), IsTileMode((psl->QueryAttribute(L"mw"))));

	}
}

void CGuiGenerator::GenerateCombinationTextArea( GXWINDOW::CCombinationTextArea& cta, const wchar_t* name)
{
	GenerateTextArea( cta.GetTextArea(), name);

	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		cta.SetPos( pchild->QueryIntegerAttribute( L"x", 0), pchild->QueryIntegerAttribute( L"y", 0));
		cta.SetSize( pchild->QueryIntegerAttribute( L"w", 0), pchild->QueryIntegerAttribute( L"h", 0));

		const CComponentAttributes::AttrMap& children = pchild->GetChildMap();
		if ( !children.empty())
			GenerateSubScrollBar( *cta.GetScrollPanel().GetVerticalScrollBar(), children.begin()->second);
	}
}

void CGuiGenerator::GenerateTextComponent( GXWINDOW::CTextComponent& tc, const wchar_t* name)
{
	GenerateComponent( tc, name);
	
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		GenerateTextable( tc, *pchild);

		if ( pchild->QueryBoolAttribute( L"center"))
			tc.SetTextAlign( GXWINDOW::TXA_CENTER);
	}
}

void CGuiGenerator::GenerateTree( GXWINDOW::CTree& t, const wchar_t* name){}

const wchar_t* CGuiGenerator::QueryComponentAttribute( const wchar_t* name, const wchar_t* key, const wchar_t* defaultValue) const
{
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		return pchild->QueryAttribute( key, defaultValue);
	}

	return defaultValue;
}

int CGuiGenerator::QueryComponentIntegerAttribute( const wchar_t* name, const wchar_t* key, int defaultValue) const
{
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		return pchild->QueryIntegerAttribute( key, defaultValue);
	}

	return defaultValue;
}

bool CGuiGenerator::QueryComponentBoolAttribute( const wchar_t* name, const wchar_t* key, bool defaultValue) const
{
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		return pchild->QueryBoolAttribute( key, defaultValue);
	}

	return defaultValue;
}

GXWINDOW::COLOR CGuiGenerator::QueryComponentColorAttribute( const wchar_t* name, const wchar_t* key, GXWINDOW::COLOR defaultvalue) const
{
	if ( const CComponentAttributes* pchild = m_ca.QueryChildAttribute( name))
	{
		return pchild->QueryColorAttribute( key, defaultvalue);
	}

	return defaultvalue;
}

}