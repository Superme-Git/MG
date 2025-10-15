#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS { 

struct TextDrawItem
{
	FONTEFEECTFLAG	effect;
	int				nCount;
	int				nText;

	// index key
	int				font;
	COLOR			clr;
	COLOR			clrGradual;	// 渐变色
	COLOR			bk;
	CSimpleString	text;	// 此处使用一个string对象, 因为manager不知外部何时删除相应的

	TextDrawItem()
	{
		effect = FET_NONE;
		nCount = 0;
		nText = -1;
		font = -1;
		clr = 0;
		clrGradual = 0;
		bk = 0;
	}
};

class CTextDrawImplementMap : public std::map<int,CTextDrawAbstractImplement*>
{
public:
	CTextDrawImplementMap() {}
	virtual ~CTextDrawImplementMap() {}
public:
	void Register( int n, CTextDrawAbstractImplement& tdai)
	{
		iterator it = find( n);
		if( it == end())
			insert( value_type( n, &tdai) );
		else
			Register( n + 1, tdai);
	}
	void Unregister( CTextDrawAbstractImplement& tdai)
	{
		for ( iterator it = begin(), ite = end(); it != ite; ++ it)
		{
			if ( it->second == &tdai)
			{
				erase( it);
				return;
			}
		}
	}
} g_TextDrawImplementMap;

struct TextMapIndex
{
	std::wstring	text;
	int				nIndex;

	TextMapIndex( const wchar_t* _text = NULL, int index = -1)
		: text( _text)
	{
		nIndex = index;
	}

	TextMapIndex( const TextMapIndex& rhs)
		: text( rhs.text), nIndex( rhs.nIndex)
	{

	}

	bool operator<( const TextMapIndex& dst) const
	{
		return text < dst.text;
	}

	const TextMapIndex& operator= ( const TextMapIndex& rhs)
	{
		this->text = rhs.text;
		this->nIndex = rhs.nIndex;

		return *this;
	}
};

class CTextMapIndexVector : public std::vector<TextMapIndex>
{
public:
	CTextMapIndexVector() {}
	virtual ~CTextMapIndexVector() {}
};

class CFontEffectTypeIndex : public std::map<GXWINDOW::FONTEFEECTFLAG, CTextMapIndexVector>
{
public:
	CFontEffectTypeIndex() {}
	virtual ~CFontEffectTypeIndex() {}
};

class CBKColorIndex : public std::map<GXWINDOW::COLOR, CFontEffectTypeIndex>
{
public:
	CBKColorIndex() {}
	virtual ~CBKColorIndex() {}
};

class CColorIndex : public std::map<GXWINDOW::COLOR, CBKColorIndex>
{
public:
	CColorIndex() {}
	virtual ~CColorIndex() {}
};

class CGradualColorIndex : public std::map<GXWINDOW::COLOR, CColorIndex>
{
public:
	CGradualColorIndex() {}
	virtual ~CGradualColorIndex() {}
};

class CTextDrawItemMultiIndex : public std::map<int, CGradualColorIndex>
{
public:
	CTextDrawItemMultiIndex() {}
	virtual ~CTextDrawItemMultiIndex() {}
};

class CTextDrawItems
{
	const int RESIZE_STEP;
	int m_nCount;
	std::vector<TextDrawItem> m_tdis;
	std::vector<int> m_nullItemIDs;
public:
	CTextDrawItems() : RESIZE_STEP( 512), m_nCount( 1), m_tdis( 1)
	{
	}
	virtual ~CTextDrawItems() {}
public:
	virtual const TextDrawItem& Insert( int index, TextDrawItem& tdi)
	{
		if ( m_nCount >= (int)m_tdis.size())
			m_tdis.resize( m_tdis.size() + RESIZE_STEP);

		TextDrawItem& t_tdi = m_tdis[index];
		t_tdi.effect = tdi.effect;
		t_tdi.bk = tdi.bk;
		t_tdi.clr = tdi.clr;
		t_tdi.clrGradual = tdi.clrGradual;
		t_tdi.font = tdi.font;
		t_tdi.nCount = tdi.nCount;
		t_tdi.nText = tdi.nText;
		t_tdi.text.assign( tdi.text.c_str());

		if ( index == m_nCount)
			m_nCount ++;

		return t_tdi;
	}

	virtual void Erase( int index)	// 不做实际删除,所以不需要全部更新TextDrawItem::nText就可以直接索引
	{
		if ( index >= m_nCount)
			return;

		TextDrawItem& tdi = m_tdis[index];
		tdi.nCount = 0;
		tdi.nText = -1;
		tdi.text.clear();

		// 将已经删除的item编号记下，使m_tdis中的被删除位置可重用
		if ( m_nullItemIDs.capacity() == m_nullItemIDs.size())
			m_nullItemIDs.reserve( RESIZE_STEP);
		m_nullItemIDs.push_back( index);
	}

	virtual int Size() const
	{
		return m_nCount;
	}

	virtual TextDrawItem& At( int index)
	{
		assert( index < m_nCount);
		return m_tdis[index];
	}


	const virtual TextDrawItem& At( int index) const
	{
		assert( index < m_nCount);
		return m_tdis[index];
	}

	void Clear()
	{
		m_tdis.clear();
	}

	int GetUsableIndex() // 返回 -1 则无可用index
	{
		if ( m_nullItemIDs.empty())
			return -1;

		std::vector<int>::iterator it = m_nullItemIDs.end() - 1;
		int id = *it;
		m_nullItemIDs.erase( it);

		return id;
	}
};


} // namespace INLINECLASS { 

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextDrawAbstractImplement::CTextDrawAbstractImplement()
{
}

CTextDrawAbstractImplement::~CTextDrawAbstractImplement()
{
	//CTextDrawManager::ReleaseInstance();
	INLINECLASS::g_TextDrawImplementMap.Unregister( *this);
}

int CTextDrawAbstractImplement::GetPriority()
{
	return 100;
}

void CTextDrawAbstractImplement::Register( CTextDrawAbstractImplement& tdai)
{
	INLINECLASS::g_TextDrawImplementMap.Register( tdai.GetPriority(), tdai);
}

CTextDrawAbstractImplement* CTextDrawAbstractImplement::GetPriorImplement()
{
	assert( !INLINECLASS::g_TextDrawImplementMap.empty());
	return INLINECLASS::g_TextDrawImplementMap.begin()->second;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextDrawManager* CTextDrawManager::m_pTextDrawManager = NULL;

CTextDrawManager::CTextDrawManager()
	: m_tdimi( new INLINECLASS::CTextDrawItemMultiIndex()), m_tdis( new INLINECLASS::CTextDrawItems()), m_nid( 0), m_Implement( NULL)
{
}

CTextDrawManager::~CTextDrawManager()
{
	assert( m_tdis->Size() ? true : NULL != m_Implement);
	if( m_Implement)
	{
		for( int i = 0; i < m_tdis->Size(); i ++)
		{
			const INLINECLASS::TextDrawItem& tdi = m_tdis->At( i);
			if ( tdi.nText != -1)
				m_Implement->DeleteTextDraw( tdi.nText);
		}
	}

	delete m_tdimi;

	m_tdis->Clear();
	delete m_tdis;
}

CTextDrawManager& CTextDrawManager::GetInstance()
{
	if ( !m_pTextDrawManager)
		m_pTextDrawManager = new CTextDrawManager();

	return *m_pTextDrawManager;
}

void CTextDrawManager::ReleaseInstance()
{
	if ( m_pTextDrawManager)
	{
		delete m_pTextDrawManager;
		m_pTextDrawManager = NULL;
	}
}

void CTextDrawManager::FreeAll()
{
	for( int i = 0; i < m_tdis->Size(); i ++)
	{
		const INLINECLASS::TextDrawItem& tdi = m_tdis->At( i);
		if ( tdi.nText != -1)
			m_Implement->DeleteTextDraw( tdi.nText);
	}

	m_tdimi->clear();
	m_tdis->Clear();
}

int CTextDrawManager::AddRef( int n)
{
	if ( n < 0 || n >= m_tdis->Size())
		return -1;

	INLINECLASS::TextDrawItem& tdi = m_tdis->At( n);
	tdi.nCount ++;

	return n;
}

int CTextDrawManager::GenerateTextDrawIndex()
{
	int id = m_tdis->GetUsableIndex();
	if ( id > 0)
		return id;

	return ++ m_nid;
}

int CTextDrawManager::NewTextDraw( const wchar_t* szText, int nFont, COLOR clr, COLOR clrGradual, COLOR bk, FONTEFEECTFLAG effect, COLOR clrEffect)
{
	if( NULL == szText || 0 == szText[0])
		return -1;
	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();
	assert( m_Implement);

	// 用多级索引分散map所需存储的数据量
	// nFont是第一级索引
	INLINECLASS::CTextDrawItemMultiIndex::iterator it_font = m_tdimi->find( nFont);
	if ( it_font == m_tdimi->end())
	{
		std::pair<INLINECLASS::CTextDrawItemMultiIndex::iterator, bool> insert_fontindex = m_tdimi->insert( std::make_pair( nFont, INLINECLASS::CGradualColorIndex()));
		if ( !insert_fontindex.second)
			return -1;
		it_font = insert_fontindex.first;
	}

	// clrGradual是第二级索引
	INLINECLASS::CGradualColorIndex& t_gradualcolorindex = it_font->second;
	INLINECLASS::CGradualColorIndex::iterator it_gradualcolor = t_gradualcolorindex.find( clrGradual);
	if ( it_gradualcolor == t_gradualcolorindex.end())
	{
		std::pair<INLINECLASS::CGradualColorIndex::iterator, bool> insert_gradualcolorindex = t_gradualcolorindex.insert( std::make_pair( clrGradual, INLINECLASS::CColorIndex()));
		if ( !insert_gradualcolorindex.second)
			return -1;
		it_gradualcolor = insert_gradualcolorindex.first;
	}

	// clr是第三级索引
	INLINECLASS::CColorIndex& t_colorindex = it_gradualcolor->second;
	INLINECLASS::CColorIndex::iterator it_color = t_colorindex.find( clr);
	if ( it_color == t_colorindex.end())
	{
		std::pair<INLINECLASS::CColorIndex::iterator, bool> insert_colorindex = t_colorindex.insert( std::make_pair( clr, INLINECLASS::CBKColorIndex()));
		if ( !insert_colorindex.second)
			return -1;
		it_color = insert_colorindex.first;
	}

	// bk是第四级索引
	INLINECLASS::CBKColorIndex& t_bkindex = it_color->second;
	INLINECLASS::CBKColorIndex::iterator it_bk = t_bkindex.find( bk);
	if ( it_bk == t_bkindex.end())
	{
		std::pair<INLINECLASS::CBKColorIndex::iterator, bool> insert_bkindex = t_bkindex.insert( std::make_pair( bk, INLINECLASS::CFontEffectTypeIndex()));
		if ( !insert_bkindex.second)
			return -1;
		it_bk = insert_bkindex.first;
	}

	// effect是第五级索引
	INLINECLASS::CFontEffectTypeIndex& t_effecttypeindex = it_bk->second;
	INLINECLASS::CFontEffectTypeIndex::iterator it_effecttype = t_effecttypeindex.find( effect);
	if ( it_effecttype == t_effecttypeindex.end())
	{
		std::pair<INLINECLASS::CFontEffectTypeIndex::iterator, bool> insert_effectindex = t_effecttypeindex.insert( std::make_pair( effect, INLINECLASS::CTextMapIndexVector()));
		if ( !insert_effectindex.second)
			return -1;
		it_effecttype = insert_effectindex.first;
	}

	// szText是第六级索引
	int nIndex = -1;
	INLINECLASS::CTextMapIndexVector& t_tdiv = it_effecttype->second;
	std::pair<INLINECLASS::CTextMapIndexVector::iterator, INLINECLASS::CTextMapIndexVector::iterator> search_range = std::equal_range(
		t_tdiv.begin(), t_tdiv.end(), INLINECLASS::TextMapIndex( szText));

	//for ( INLINECLASS::CTextMapIndexVector::iterator it = search_range.first; it != search_range.second; ++ it)
	//{
	//	nIndex = ( *it).nIndex;
	//	INLINECLASS::TextDrawItem& t_tdi = m_tdis->At( nIndex);
	//	if ( t_tdi.bUnderline == bUnderline)
	//	{
	//		t_tdi.nCount ++;
	//		return nIndex;
	//	}
	//}

	//int nImplIndex = m_Implement->NewTextDraw( szText, nFont, clr, bk, bUnderline);

	//INLINECLASS::TextDrawItem t_tdi;
	//t_tdi.bUnderline = bUnderline;
	//t_tdi.nCount = 1;
	//t_tdi.nText = nImplIndex;

	//t_tdi.font = nFont;
	//t_tdi.clr = clr;
	//t_tdi.bk = bk;
	//t_tdi.text.assign( szText);

	//nIndex = GenerateTextDrawIndex();

	//t_tdiv.insert( search_range.first, INLINECLASS::TextMapIndex( ( m_tdis->Insert( nIndex, t_tdi)).text.c_str(), nIndex));


	if ( search_range.first == search_range.second) // 没有找到
	{
		int nImplIndex = m_Implement->NewTextDraw( szText, nFont, clr, clrGradual, bk, effect, clrEffect);

		INLINECLASS::TextDrawItem t_tdi;
		t_tdi.nCount = 1;
		t_tdi.nText = nImplIndex;

		t_tdi.effect = effect;
		t_tdi.font = nFont;
		t_tdi.clr = clr;
		t_tdi.clrGradual = clrGradual;
		t_tdi.bk = bk;
		t_tdi.text.assign( szText);

		nIndex = GenerateTextDrawIndex();

		t_tdiv.insert( search_range.first, INLINECLASS::TextMapIndex( ( m_tdis->Insert( nIndex, t_tdi)).text.c_str(), nIndex));
	}
	else
	{
		nIndex = ( *( search_range.first)).nIndex;
		INLINECLASS::TextDrawItem& t_tdi = m_tdis->At( nIndex);
		t_tdi.nCount ++;
	}

	return nIndex;
}

int CTextDrawManager::FreeTextDraw( int n)
{
	if( n < 0 || n >= m_tdis->Size())
		return -1;

	INVALIDATE();

	INLINECLASS::TextDrawItem& tdi = m_tdis->At( n);

	INLINECLASS::CTextDrawItemMultiIndex::iterator it_font = m_tdimi->find( tdi.font);
	if ( it_font != m_tdimi->end())
	{
		INLINECLASS::CGradualColorIndex& t_gradualcolorindex = it_font->second;
		INLINECLASS::CGradualColorIndex::iterator it_clrgradual = t_gradualcolorindex.find( tdi.clrGradual);
		if ( it_clrgradual != t_gradualcolorindex.end())
		{
			INLINECLASS::CColorIndex& t_colorindex = it_clrgradual->second;
			INLINECLASS::CColorIndex::iterator it_clr = t_colorindex.find( tdi.clr);
			if ( it_clr != t_colorindex.end())
			{
				INLINECLASS::CBKColorIndex& t_bkcolorindex = it_clr->second;
				INLINECLASS::CBKColorIndex::iterator it_bkclr = t_bkcolorindex.find( tdi.bk);
				if ( it_bkclr != t_bkcolorindex.end())
				{
					INLINECLASS::CFontEffectTypeIndex& t_effectindex = it_bkclr->second;
					INLINECLASS::CFontEffectTypeIndex::iterator it_effect = t_effectindex.find( tdi.effect);
					if ( it_effect != t_effectindex.end())
					{
						INLINECLASS::CTextMapIndexVector& t_tmiv = it_effect->second;
						std::pair<INLINECLASS::CTextMapIndexVector::iterator, INLINECLASS::CTextMapIndexVector::iterator> search_range = std::equal_range(
							t_tmiv.begin(), t_tmiv.end(), INLINECLASS::TextMapIndex( tdi.text.c_str()));
						if ( search_range.first != search_range.second)
						{
							if ( 1 == tdi.nCount)
								t_tmiv.erase( search_range.first);
						}

						if ( t_tmiv.empty())
							t_bkcolorindex.erase( it_bkclr);
					}
				}

				if ( t_bkcolorindex.empty())
					t_colorindex.erase( it_clr);
			}
		}

		if ( t_gradualcolorindex.empty())
			m_tdimi->erase( it_font);
	}

	tdi.nCount --;
	if ( tdi.nCount > 0)
		return tdi.nCount;

	int nTextImpl = tdi.nText;
	m_tdis->Erase( n);

	assert( m_Implement);
	m_Implement->DeleteTextDraw( nTextImpl);

	return 0;
}

void CTextDrawManager::Draw( int n, int l, int t, int cx, int cy, int cw, int ch)
{
	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();
	assert( m_Implement);
	if ( n < 0 || n >= m_tdis->Size()) 
		return;
	INLINECLASS::TextDrawItem& tdi = m_tdis->At( n);

	m_Implement->Draw( tdi.nText, l + cx, t + cy, cx, cy, cw, ch, tdi.clr, tdi.clrGradual);
}

int CTextDrawManager::GetCharPixelWidth( const wchar_t& wt , int font)
{
	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();
	assert( m_Implement);
	return m_Implement->GetCharPixelWidth( wt, font);
}

int CTextDrawManager::GetStringPixelWidth( const wchar_t* sz, int nFont)
{
	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();
	assert( m_Implement);
	return m_Implement->GetStringPixelWidth( sz, nFont);
}

int CTextDrawManager::GetStringPixelWidth( int n, int nFont)
{
	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();
	assert( m_Implement);
	if ( n < 0 || n >= m_tdis->Size()) 
		return 0;
	INLINECLASS::TextDrawItem& tdi = m_tdis->At( n);

	return m_Implement->GetStringPixelWidth( tdi.nText, nFont);
}

int CTextDrawManager::GetFontHeight( int nFont)
{
	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();
	assert( m_Implement);
	return m_Implement->GetFontHeight( nFont);
}

int CTextDrawManager::GetFontHeightWithoutUnderline( int nFont)
{
	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();
	assert( m_Implement);
	return m_Implement->GetFontHeightWithoutUnderline( nFont);
}

void CTextDrawManager::SetShadow( int n, COLOR clr)
{
	if ( n < 0 || n >= m_tdis->Size()) 
		return;

	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();

	INLINECLASS::TextDrawItem& tdi = m_tdis->At( n);
	m_Implement->SetShadow( tdi.nText, clr);
}

void CTextDrawManager::SetStroke( int n, COLOR clr)
{
	if ( n < 0 || n >= m_tdis->Size()) 
		return;

	if( NULL == m_Implement)
		m_Implement = CTextDrawAbstractImplement::GetPriorImplement();

	INLINECLASS::TextDrawItem& tdi = m_tdis->At( n);
	m_Implement->SetStroke( tdi.nText, clr);
}

} // namespace GXWINDOW { 