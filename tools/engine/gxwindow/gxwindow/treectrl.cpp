 #include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS { 

class CTreeNodeExpandStateMap : public std::map<int,bool>
{
public:
	CTreeNodeExpandStateMap() {};
	virtual ~CTreeNodeExpandStateMap() {};
};

class CTreeDataModalDataChangeNotifySet : public CNotifySet<CTreeDataModalDataChangeNotify*>
{
public:
	CTreeDataModalDataChangeNotifySet() {}
	virtual ~CTreeDataModalDataChangeNotifySet() {}
};

static int GenerateTextTreeDataID()
{
	static int n = 0;
	return ++ n;
};

class CTextTreeData;

class CTextTreeDataVector : public std::vector<CTextTreeData*>
{
public:
	CTextTreeDataVector() {}
	virtual ~CTextTreeDataVector() { Clear(); }
public:
	iterator Find( CTextTreeData& ttd);
	template<typename MT> void ClearAllNormalText( MT mem);
	void ClearAllDrawText();
	void Clear();
};

class CTextTreeData : public CTreeNodeObject
{
	CTextDrawManager&		m_tdm;
public:
	CTextTreeData( CTextDrawManager& tdm)
		: m_tdm( tdm), parent( NULL), n( -1), s( -1), m( -1), len( 0)
	{
	}
	
	CTextTreeData( const CTextTreeData& src)
		: CTreeNodeObject( src), m_tdm( src.m_tdm), parent( src.parent), text( src.text), children( src.children), len( src.len)
	{
		//m_nid = src.m_nid;
		n = m_tdm.AddRef( src.n);
		s = m_tdm.AddRef( src.s);
		m = m_tdm.AddRef( src.m);
		//m_uiParam = src.GetParam();
		//m_llParam = src.GetLongParam();
	}
	virtual ~CTextTreeData()
	{
		ClearDrawText();
	}
public:
	CTextTreeData*		parent;
	std::wstring		text;
	int					n;
	int					s;
	int					m;
	int					len;
	CTextTreeDataVector	children;
public:
	CTextTreeData& operator=( const CTextTreeData& src)
	{
		if( this == &src)
			return *this;
		parent = src.parent;
		text = src.text;
		n = m_tdm.AddRef( src.n);
		s = m_tdm.AddRef( src.s);
		m = m_tdm.AddRef( src.m);
		len = src.len;
		m_nid = src.m_nid;
		m_uiParam = src.GetParam();
		m_llParam = src.GetLongParam();
		children = src.children;
		return *this;
	}
	void ClearDrawText()
	{
		if( -1 != n)
		{
			 m_tdm.FreeTextDraw( n);
			n = -1;
		}
		if( -1 != m)
		{
			 m_tdm.FreeTextDraw( m);
			m = -1;
		}
		if( -1 != s)
		{
			 m_tdm.FreeTextDraw( s);
			s = -1;
		}
	}
	void ClearAllDrawText()
	{
		ClearDrawText();
		children.ClearAllDrawText();
	}

	template<typename MT>
	void ClearAllNormalText( MT mem)
	{
		int& n = (this->*mem);
		if( -1 != n)
		{
			m_tdm.FreeTextDraw( n);
			n = -1;
		}
		children.ClearAllNormalText( mem);
	}

	virtual int GetNodeLength() const
	{
		return len;
	}
};

void CTextTreeDataVector::Clear() 
{
	for ( iterator it = begin(), ite = end(); it != ite; ++ it)
	{
		CTextTreeData* ttd = *it;
		ttd->ClearAllDrawText();
		delete ttd;
	}
	clear();
}

inline CTextTreeDataVector::iterator CTextTreeDataVector::Find( CTextTreeData& ttd)
{
	for( iterator it = begin(); it != end(); ++ it)
	{
		INLINECLASS::CTextTreeData& sttd = *(*it);
		if( sttd.IsSameNode( ttd) )
			return it;
	} // for( INLINECLASS::CTextTreeDataVector::iterator it = m_ptdv->begin(); it != m_ptdv->end(); ++ it)
	return end();
}

template<typename MT> 
inline void CTextTreeDataVector::ClearAllNormalText( MT mem)
{
	for( iterator it = begin(); it != end(); ++ it)
	{
		CTextTreeData* ttd = *it;
		ttd->ClearAllNormalText( mem);
	}
}

inline void CTextTreeDataVector::ClearAllDrawText()
{
	for( iterator it = begin(); it != end(); ++ it)
	{
		CTextTreeData* ttd = *it;
		ttd->ClearAllDrawText();
	}
}

struct TreeViewItem
{
	CTreeNodeObject*	node;
	bool				bIsLeaf;
	bool				bExpand;
	int					offset;
};

class CTreeViewItemVector : public std::vector<TreeViewItem>
{
public:
	CTreeViewItemVector() {}
	virtual ~CTreeViewItemVector() {}
};

class CTreeMouseOnChangeNotifySet : public CNotifySet<CTreeMouseOnChangeNotify*>
{
public:
	CTreeMouseOnChangeNotifySet() {}
	virtual ~CTreeMouseOnChangeNotifySet() {}
};

class CTreeSelectedChangeNotifySet : public CNotifySet<CTreeSelectedChangeNotify*>
{
public:
	CTreeSelectedChangeNotifySet() {}
	virtual ~CTreeSelectedChangeNotifySet() {}
};


class CIconTextTreeData;

class CIconTextTreeDataVector : public std::vector<CIconTextTreeData*>
{
public:
	CIconTextTreeDataVector() {}
	virtual ~CIconTextTreeDataVector() { Clear(); }
public:
	iterator Find( CIconTextTreeData& ttd);
	template<typename MT> void ClearAllNormalText( MT mem);
	void ClearAllDrawText();
	void Clear();
};

class CIconTextTreeData : public CTextTreeData
{
public:
	int		m_nIconIndex;
	int		m_nIconWidth;
	int		m_nIconHeight;
	int		m_nShowWidth;
	int		m_nShowHeight;
	CIconTextTreeDataVector m_ittdvChildren;
public:
	CIconTextTreeData( CTextDrawManager& tdm) : CTextTreeData(tdm), m_nIconIndex( -1), m_nIconWidth( 0), m_nIconHeight( 0), m_nShowWidth( 0), m_nShowHeight( 0)
	{
		//n = m_tdm.AddRef( src.n);
		//s = m_tdm.AddRef( src.s);
		//m = m_tdm.AddRef( src.m);
	}
	virtual ~CIconTextTreeData() {};
	CIconTextTreeData& operator=( const CIconTextTreeData& src)
	{
		if( this == &src)
			return *this;
		parent = src.parent;
		text = src.text;
		//n = m_tdm.AddRef( src.n);
		//s = m_tdm.AddRef( src.s);
		//m = m_tdm.AddRef( src.m);
		m_nid = src.m_nid;
		m_uiParam = src.GetParam();
		m_llParam = src.GetLongParam();
		m_nIconIndex = src.m_nIconIndex;
		m_nIconWidth = src.m_nIconWidth;
		m_nIconHeight = src.m_nIconHeight;
		m_nShowWidth = src.m_nShowWidth;
		m_nShowHeight = src.m_nShowHeight;
		m_ittdvChildren = src.m_ittdvChildren;
		return *this;
	}
};

inline CIconTextTreeDataVector::iterator CIconTextTreeDataVector::Find( CIconTextTreeData& ttd)
{
	for( iterator it = begin(); it != end(); ++ it)
	{
		INLINECLASS::CIconTextTreeData* sttd = *it;
		if( sttd->IsSameNode( ttd) )
			return it;
	} // for( INLINECLASS::CTextTreeDataVector::iterator it = m_ptdv->begin(); it != m_ptdv->end(); ++ it)
	return end();
}

template<typename MT> 
inline void CIconTextTreeDataVector::ClearAllNormalText( MT mem)
{
	for( iterator it = begin(); it != end(); ++ it)
	{
		CIconTextTreeData* ttd = *it;
		ttd->ClearAllNormalText( mem);
	}
}

inline void CIconTextTreeDataVector::ClearAllDrawText()
{
	for( iterator it = begin(); it != end(); ++ it)
	{
		CIconTextTreeData* ttd = *it;
		ttd->ClearAllDrawText();
	}
}

void CIconTextTreeDataVector::Clear()
{
	for ( iterator it = begin(), ite = end(); it != ite; ++ it)
	{
		CIconTextTreeData* ttd = *it;
		ttd->ClearAllDrawText();
		delete ttd;
	}
	clear();
}

} // namespace INLINECLASS {  

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTreeDataModalDataChangeNotify::CTreeDataModalDataChangeNotify()
{
}

CTreeDataModalDataChangeNotify::~CTreeDataModalDataChangeNotify()
{
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTreeDataModal::CTreeDataModal()
	: m_pParent( NULL), m_bShow( false), m_cns( new INLINECLASS::CTreeDataModalDataChangeNotifySet())
{
}

CTreeDataModal::~CTreeDataModal()
{
	if ( m_bShow)
		OnComponentHide();
	if ( m_pParent)
		m_pParent->ResetTreeDataModule( this);
	delete m_cns;
}

bool CTreeDataModal::IsLeaf( CTreeNodeObject* node)
{
	return 0 == GetChildCount( node);
}

void CTreeDataModal::TreeDataModalDataChangeNotifyAdd( CTreeDataModalDataChangeNotify& dcn)
{
	m_cns->Insert( &dcn);
}

void CTreeDataModal::TreeDataModalDataChangeNotifyRemove( CTreeDataModalDataChangeNotify& dcn)
{
	m_cns->Remove( &dcn);
}

void CTreeDataModal::FireDataChanged()
{
	m_cns->NotifyAll( *this, &CTreeDataModalDataChangeNotify::OnTreeDataModaDataChange);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTreeNodeObject::CTreeNodeObject()
	: m_nid( INLINECLASS::GenerateTextTreeDataID()), m_uiParam( 0), m_llParam( 0)
{
}

CTreeNodeObject::CTreeNodeObject( int nid)
	: m_nid( nid), m_uiParam( 0), m_llParam( 0)
{
}

CTreeNodeObject::CTreeNodeObject( const CTreeNodeObject& src)
: m_nid( src.m_nid), m_uiParam( src.m_uiParam), m_llParam( src.m_llParam)
{

}

CTreeNodeObject::~CTreeNodeObject()
{
}

bool CTreeNodeObject::IsSameNode( const CTreeNodeObject& tno) const
{
	return m_nid == tno.m_nid;
}

bool CTreeNodeObject::IsSameNode( const CTreeNodeObject* ptno) const
{
	if( NULL == ptno)
		return false;
	return m_nid == ptno->m_nid;
}

void CTreeNodeObject::SetParam( unsigned int uiParam)
{
	m_uiParam = uiParam;
}

unsigned int CTreeNodeObject::GetParam() const
{
	return m_uiParam;
}

void CTreeNodeObject::SetLongParam(unsigned __int64 llParam)
{
	m_llParam = llParam;
}

unsigned __int64 CTreeNodeObject::GetLongParam() const
{
	return m_llParam;
}

int CTreeNodeObject::GetNodeLength() const
{
	return 0;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextTreeDataModal::CTextTreeDataModal()
: m_tdm( &CTextDrawManager::GetInstance()), m_ptdv( new INLINECLASS::CTextTreeDataVector())
{
}

CTextTreeDataModal::~CTextTreeDataModal()
{
	delete m_ptdv;
}

int CTextTreeDataModal::GetRootCount()
{
	return (int)m_ptdv->size();
}

CTreeNodeObject* CTextTreeDataModal::GetRoot( int nIndex)
{
	assert( nIndex >= 0 && nIndex < GetRootCount());
	INLINECLASS::CTextTreeData* ttd = m_ptdv->at( nIndex);
	return ttd;
}

int CTextTreeDataModal::GetChildCount( CTreeNodeObject* parent)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( parent);
	assert( pttd);
	return (int)pttd->children.size();
}

CTreeNodeObject* CTextTreeDataModal::GetChild( CTreeNodeObject* parent, int nIndex)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( parent);
	assert( pttd);
	assert( nIndex >= 0 && nIndex < (int)pttd->children.size());
	INLINECLASS::CTextTreeData* ttd = pttd->children.at( nIndex);
	return ttd;
}

void CTextTreeDataModal::DrawComponent( CTreeNodeObject* node, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( node);
	assert( pttd);

	int n = -1;
	switch( cct)
	{
	case CCT_NORMAL:
		if( -1 == pttd->n)
		{
			pttd->n = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, m_clrNormal, m_clrNormalGradual, 0, m_uNormalEffect, m_clrNormalEffect);
			pttd->len = m_tdm->GetStringPixelWidth( pttd->n, m_nFont);
		}
		n = pttd->n;
		break;
	case CCT_MOUSEON:
		if( -1 == pttd->m)
			pttd->m = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, m_clrMouseOn, m_clrMouseOnGradual, 0, m_uMouseOnEffect, m_clrMouseOnEffect);
		n = pttd->m;
		break;
	case CCT_SELECT:
		if( -1 == pttd->s)
			pttd->s = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, m_clrSelected, m_clrSelectedGradual, 0, m_uSelectedEffect, m_clrSelectedEffect);
		n = pttd->s;
		break;
	default:
		assert( false);
		break;
	}
	if( -1 == n)
		return;
	m_tdm->Draw( n, l, t, cx, cy, cw, ch);
}

CTextDrawManager& CTextTreeDataModal::GetTextDrawManager()
{
	return *m_tdm;
}

CTreeNodeObject* CTextTreeDataModal::NewRoot( const wchar_t* szText, unsigned int uiParam)
{
	if( NULL == szText)
		szText = L"";

	INLINECLASS::CTextTreeData* newttd = new INLINECLASS::CTextTreeData( *m_tdm);

	newttd->text = szText;
	newttd->len = m_tdm->GetStringPixelWidth( szText, m_nFont);
	newttd->SetParam( uiParam);
	m_ptdv->push_back( newttd);

	INLINECLASS::CTextTreeData* ttd = m_ptdv->back();
	assert( ttd->IsSameNode( newttd));
	return ttd;
}

bool CTextTreeDataModal::RemoveRoot( CTreeNodeObject* node)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( node);
	assert( pttd);

	INLINECLASS::CTextTreeDataVector::iterator it = m_ptdv->Find( *pttd);

	if( it != m_ptdv->end())
	{
		delete *it;
		m_ptdv->erase( it);
		return true;
	}
	return false;
}

CTreeNodeObject* CTextTreeDataModal::NewNode( CTreeNodeObject* parent, const wchar_t* szText, unsigned int uiParam)
{
	if( NULL == szText)
		szText = L"";
	if( NULL == parent)
		return NewRoot( szText, uiParam);

	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( parent);
	assert( pttd);

	INLINECLASS::CTextTreeData* newttd = new INLINECLASS::CTextTreeData( *m_tdm);
	newttd->parent = pttd;
	newttd->text = szText;
	newttd->len = m_tdm->GetStringPixelWidth( szText, m_nFont);
	newttd->SetParam( uiParam);
	pttd->children.push_back( newttd);

	INLINECLASS::CTextTreeData* ttd = pttd->children.back();
	assert( ttd->IsSameNode( newttd));
	return ttd;
}

CTreeNodeObject* CTextTreeDataModal::InsertNode( CTreeNodeObject* parent, CTreeNodeObject* insertbefore, const wchar_t* szText, unsigned int uiParam)
{
	if ( NULL == insertbefore)
		return NewNode( parent, szText, uiParam);
	
	if( NULL == szText)
		szText = L"";
	if( NULL == parent)
		return NewRoot( szText, uiParam);

	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( parent);
	assert( pttd);

	INLINECLASS::CTextTreeData* pttdinsert = dynamic_cast<INLINECLASS::CTextTreeData*>( insertbefore);
	assert( pttdinsert);

	INLINECLASS::CTextTreeData* newttd = new INLINECLASS::CTextTreeData( *m_tdm);
	newttd->parent = pttd;
	newttd->text = szText;
	newttd->len = m_tdm->GetStringPixelWidth( szText, m_nFont);
	newttd->SetParam( uiParam);

	int pos = 0;
	for ( INLINECLASS::CTextTreeDataVector::iterator it = pttd->children.begin(); it != pttd->children.end(); ++ it, ++ pos)
	{
		if ( (*it)->IsSameNode( pttdinsert))
		{
			pttd->children.insert( it, newttd);
			break;
		}
	}
	if ( ( int)pttd->children.size() == pos)
		pttd->children.push_back( newttd);

	INLINECLASS::CTextTreeData* ttd = pttd->children.at( pos);
	assert( ttd->IsSameNode( newttd));
	return ttd;
}

bool CTextTreeDataModal::RemoveNode( CTreeNodeObject* node, CTreeNodeObject* parent)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( node);
	assert( pttd);

	if( NULL == pttd->parent)
	{
		return RemoveRoot( node);
	}

	INLINECLASS::CTextTreeData* pttdparent = dynamic_cast<INLINECLASS::CTextTreeData*>( pttd->parent);
	assert( pttdparent);

	INLINECLASS::CTextTreeDataVector::iterator it = pttdparent->children.Find( *pttd);
	if( it != pttdparent->children.end())
	{
		delete *it;
		pttdparent->children.erase( it);
		return true;
	}
	return false;
}

void CTextTreeDataModal::SetNodeText( CTreeNodeObject* node, const wchar_t* szText)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( node);
	assert( pttd);
	if( NULL == szText)
		pttd->text.erase();
	else
		pttd->text = szText;
	pttd->ClearDrawText();
	pttd->len = m_tdm->GetStringPixelWidth( szText, m_nFont);
}

const wchar_t* CTextTreeDataModal::GetNodeText( const CTreeNodeObject* node) const
{
	const INLINECLASS::CTextTreeData* pttd = dynamic_cast<const INLINECLASS::CTextTreeData*>( node);
	assert( pttd);
	return pttd->text.c_str();
}

void CTextTreeDataModal::SetNodeParam( CTreeNodeObject* node, unsigned int uiParam)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( node);
	assert( pttd);
	pttd->SetParam( uiParam);
}
unsigned int CTextTreeDataModal::GetNodeParam( CTreeNodeObject* node) const
{
	const INLINECLASS::CTextTreeData* pttd = dynamic_cast<const INLINECLASS::CTextTreeData*>( node);
	assert( pttd);
	return pttd->GetParam();
}

void CTextTreeDataModal::FreeAllTextDraw()
{
	m_ptdv->ClearAllDrawText();
}

void CTextTreeDataModal::RemoveAllNodes()
{
	m_ptdv->Clear();
	FireDataChanged();
}

void CTextTreeDataModal::SetTextColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrNormal != clr || m_clrNormalEffect != clrEffect || m_uNormalEffect != type || ( bGradualColor && ( clrGradual != m_clrNormalGradual)))
	{
		CSimpleTextable::SetTextColor( clr, type, clrEffect, bGradualColor, clrGradual);
		m_ptdv->ClearAllNormalText( &INLINECLASS::CTextTreeData::n);
	}
}

void CTextTreeDataModal::SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrSelected != clr || m_clrSelectedEffect != clrEffect || m_uSelectedEffect != type || ( bGradualColor && ( clrGradual != m_clrSelectedGradual)))
	{
		CSimpleTextable::SetTextSelectedColor( clr, type, clrEffect, bGradualColor, clrGradual);
		m_ptdv->ClearAllNormalText( &INLINECLASS::CTextTreeData::s);
	}
}

void CTextTreeDataModal::SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrMouseOn != clr || m_clrMouseOnEffect != clrEffect || m_uMouseOnEffect != type || ( bGradualColor && ( clrGradual != m_clrMouseOnGradual)))
	{
		CSimpleTextable::SetTextMouseOnColor( clr, type, clrEffect, bGradualColor, clrGradual);
		m_ptdv->ClearAllNormalText( &INLINECLASS::CTextTreeData::m);
	}
}

void CTextTreeDataModal::SetTextFont( int font)
{
	if( m_nFont == font)
		return;
	CSimpleTextable::SetTextFont( font);
	m_ptdv->ClearAllDrawText();
}

void CTextTreeDataModal::SetTextColor( CTreeNodeObject* node, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( node);
	assert( pttd);

	if ( -1 != pttd->n)
	{
		m_tdm->FreeTextDraw( pttd->n);
		pttd->n = -1;
	}

	pttd->n = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

void CTextTreeDataModal::SetTextSelectedColor( CTreeNodeObject* node, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( node);
	assert( pttd);

	if ( -1 != pttd->s)
	{
		m_tdm->FreeTextDraw( pttd->s);
		pttd->s = -1;
	}

	pttd->s = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

void CTextTreeDataModal::SetTextMouseOnColor( CTreeNodeObject* node, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::CTextTreeData* pttd = dynamic_cast<INLINECLASS::CTextTreeData*>( node);
	assert( pttd);

	if ( -1 != pttd->m)
	{
		m_tdm->FreeTextDraw( pttd->m);
		pttd->m = -1;
	}

	pttd->m = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CIconTextTreeDataModule::CIconTextTreeDataModule() : m_tdm( &CTextDrawManager::GetInstance()), m_pittdv( new INLINECLASS::CIconTextTreeDataVector())
{
}

CIconTextTreeDataModule::~CIconTextTreeDataModule()
{
	delete m_pittdv;
}


int CIconTextTreeDataModule::GetRootCount()
{
	return (int)m_pittdv->size();
}

CTreeNodeObject* CIconTextTreeDataModule::GetRoot( int nIndex)
{
	assert( nIndex >= 0 && nIndex < GetRootCount());
	INLINECLASS::CIconTextTreeData* ttd = m_pittdv->at( nIndex);
	return ttd;
}

int CIconTextTreeDataModule::GetChildCount( CTreeNodeObject* parent)
{
	INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<INLINECLASS::CIconTextTreeData*>( parent);
	assert( pttd);
	return (int)pttd->m_ittdvChildren.size();
}

CTreeNodeObject* CIconTextTreeDataModule::GetChild( CTreeNodeObject* parent, int nIndex)
{
	INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<INLINECLASS::CIconTextTreeData*>( parent);
	assert( pttd);
	assert( nIndex >= 0 && nIndex < (int)pttd->m_ittdvChildren.size());
	INLINECLASS::CIconTextTreeData* ttd = pttd->m_ittdvChildren.at( nIndex);
	return ttd;
}

void CIconTextTreeDataModule::DrawComponent( CTreeNodeObject* node, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);

	// 画图标

	// 画文字
	int n = -1;
	switch( cct)
	{
	case CCT_NORMAL:
		if( -1 == pttd->n)
			pttd->n = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, m_clrNormal, m_clrNormalGradual, 0, m_uNormalEffect, m_clrNormalEffect);
		n = pttd->n;
		break;
	case CCT_MOUSEON:
		if( -1 == pttd->m)
			pttd->m = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, m_clrMouseOn, m_clrMouseOnGradual, 0, m_uMouseOnEffect, m_clrMouseOnEffect);
		n = pttd->m;
		break;
	case CCT_SELECT:
		if( -1 == pttd->s)
			pttd->s = m_tdm->NewTextDraw( pttd->text.c_str(), m_nFont, m_clrSelected, m_clrSelectedGradual, 0, m_uSelectedEffect, m_clrSelectedEffect);
		n = pttd->s;
		break;
	default:
		assert( false);
		break;
	}
	if( -1 == n)
		return;
	m_tdm->Draw( n, l + pttd->m_nIconWidth, t, cx, cy, cw, ch);
}

CTreeNodeObject* CIconTextTreeDataModule::NewRoot( const wchar_t* szText, int nIcon, int nIconWidth, int nIconHeight, int nShowWidth, int nShowHeigth)
{
	if( NULL == szText)
		szText = L"";

	INLINECLASS::CIconTextTreeData* newttd = new INLINECLASS::CIconTextTreeData( *m_tdm);

	newttd->text = szText;
	newttd->m_nIconIndex = nIcon;
	newttd->m_nIconWidth = nIconWidth;
	newttd->m_nIconHeight = nIconHeight;
	newttd->m_nShowWidth = nShowWidth;
	newttd->m_nShowHeight = nShowHeigth;
	m_pittdv->push_back( newttd);

	INLINECLASS::CIconTextTreeData* ttd = m_pittdv->back();
	assert( ttd->IsSameNode( newttd));
	return ttd;
}

bool CIconTextTreeDataModule::RemoveRoot( CTreeNodeObject* node)
{
	INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);

	INLINECLASS::CIconTextTreeDataVector::iterator it = m_pittdv->Find( *pttd);

	if( it != m_pittdv->end())
	{
		delete *it;
		m_pittdv->erase( it);
		return true;
	}
	return false;
}

CTreeNodeObject* CIconTextTreeDataModule::NewNode( CTreeNodeObject* parent, const wchar_t* szText, int nIcon, int nIconWidth, int nIconHeight, int nShowWidth, int nShowHeigth)
{
	if( NULL == szText)
		szText = L"";
	if( NULL == parent)
		return NewRoot( szText, nIcon, nIconWidth, nIconHeight, nShowWidth, nShowHeigth);

	INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<INLINECLASS::CIconTextTreeData*>( parent);
	assert( pttd);

	INLINECLASS::CIconTextTreeData* newttd = new INLINECLASS::CIconTextTreeData( *m_tdm);
	newttd->text = szText;
	newttd->m_nIconIndex = nIcon;
	newttd->m_nIconWidth = nIconWidth;
	newttd->m_nIconHeight = nIconHeight;
	newttd->m_nShowWidth = nShowWidth;
	newttd->m_nShowHeight = nShowHeigth;
	pttd->m_ittdvChildren.push_back( newttd);

	INLINECLASS::CIconTextTreeData* ttd = pttd->m_ittdvChildren.back();
	assert( ttd->IsSameNode( newttd));
	return ttd;
}

bool CIconTextTreeDataModule::RemoveNode( CTreeNodeObject* node)
{
	INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);

	if( NULL == pttd->parent)
	{
		return RemoveRoot( node);
	}

	INLINECLASS::CIconTextTreeData* parent = dynamic_cast<INLINECLASS::CIconTextTreeData*>( pttd->parent);
	assert( parent);

	INLINECLASS::CIconTextTreeDataVector::iterator it = parent->m_ittdvChildren.Find( *pttd);
	if( it != parent->m_ittdvChildren.end())
	{
		delete *it;
		parent->m_ittdvChildren.erase( it);
		return true;
	}
	return false;
}

void CIconTextTreeDataModule::SetNodeText( CTreeNodeObject* node, const wchar_t* szText)
{
	INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);
	if( NULL == szText)
		pttd->text.erase();
	else
		pttd->text = szText;
	pttd->ClearDrawText();
}

const wchar_t* CIconTextTreeDataModule::GetNodeText( const CTreeNodeObject* node) const
{
	const INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<const INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);
	return pttd->text.c_str();
}

void CIconTextTreeDataModule::SetNodeIcon( CTreeNodeObject* node, int nIcon, int nIconWidth, int nIconHeight, int nShowWidth, int nShowHeigth)
{
	INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);
	pttd->m_nIconIndex = nIcon;
	pttd->m_nIconWidth = nIconWidth;
	pttd->m_nIconHeight = nIconHeight;
	pttd->m_nShowWidth = nShowWidth;
	pttd->m_nShowHeight = nShowHeigth;
}

int CIconTextTreeDataModule::GetNodeIcon( CTreeNodeObject* node) const
{
	const INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<const INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);
	return pttd->m_nIconIndex;
}

int CIconTextTreeDataModule::GetNodeIconWidth( CTreeNodeObject* node) const
{
	const INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<const INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);
	return pttd->m_nIconWidth;
}

int CIconTextTreeDataModule::GetNodeIconHeight( CTreeNodeObject* node) const
{
	const INLINECLASS::CIconTextTreeData* pttd = dynamic_cast<const INLINECLASS::CIconTextTreeData*>( node);
	assert( pttd);
	return pttd->m_nIconHeight;
}

void CIconTextTreeDataModule::FreeAllTextDraw()
{
	m_pittdv->ClearAllDrawText();
}

void CIconTextTreeDataModule::SetTextColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrNormal != clr || m_clrNormalEffect != clrEffect || m_uNormalEffect != type || ( bGradualColor && ( clrGradual != m_clrNormalGradual)))
	{
		CSimpleTextable::SetTextColor( clr, type, clrEffect, bGradualColor, clrGradual);
		m_pittdv->ClearAllNormalText( &INLINECLASS::CIconTextTreeData::n);
	}
}

void CIconTextTreeDataModule::SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrSelected != clr || m_clrSelectedEffect != clrEffect || m_uSelectedEffect != type || ( bGradualColor && ( clrGradual != m_clrSelectedGradual)))
	{
		CSimpleTextable::SetTextSelectedColor( clr, type, clrEffect);
		m_pittdv->ClearAllNormalText( &INLINECLASS::CIconTextTreeData::s);
	}
}

void CIconTextTreeDataModule::SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrMouseOn != clr || m_clrMouseOnEffect != clrEffect || m_uMouseOnEffect != type || ( bGradualColor && ( clrGradual != m_clrMouseOnGradual)))
	{
		CSimpleTextable::SetTextMouseOnColor( clr, type, clrEffect);
		m_pittdv->ClearAllNormalText( &INLINECLASS::CIconTextTreeData::m);
	}
}

void CIconTextTreeDataModule::SetTextFont( int font)
{
	if( m_nFont == font)
		return;
	CSimpleTextable::SetTextFont( font);
	m_pittdv->ClearAllDrawText();
}

void CIconTextTreeDataModule::RemoveAllNodes()
{
	m_pittdv->Clear();
	FireDataChanged();
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTreeMouseOnChangeNotify::CTreeMouseOnChangeNotify()
{
}

CTreeMouseOnChangeNotify::~CTreeMouseOnChangeNotify()
{
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTreeSelectedChangeNotify::CTreeSelectedChangeNotify()
{
}

CTreeSelectedChangeNotify::~CTreeSelectedChangeNotify()
{
}

void CTreeSelectedChangeNotify::OnTreeRightButtonSelectedChange(CTree& tree, CTreeNodeObject* no)
{
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTree::CTree()
	: m_ptdm( NULL), m_pviv( new INLINECLASS::CTreeViewItemVector()), m_pnoSelected( NULL), m_pnoMouseOn( NULL), m_pnoDrawing( NULL),
	m_nViewHeight( 20), m_nViewWidth( 100), m_nMaxViewWidth( 100), m_nBtnWidth( 20), m_nBtnHeight( 20), m_mcns( new INLINECLASS::CTreeMouseOnChangeNotifySet()),
	m_scns( new INLINECLASS::CTreeSelectedChangeNotifySet()), m_bImageBehindText( false), m_bAutoViewWidth( true), m_clrMouseOnBackGround( 0), m_clrSelectedBackGround( 0),
	m_expandstate( new INLINECLASS::CTreeNodeExpandStateMap())
{
}

CTree::~CTree()
{
	delete m_pviv;
	delete m_mcns;
	delete m_scns;
	delete m_expandstate;
}

void CTree::TreeMouseOnChangeNotifyAdd( CTreeMouseOnChangeNotify& mcn)
{
	m_mcns->Insert( &mcn);
}

void CTree::TreeMouseOnChangeNotifyRemove( CTreeMouseOnChangeNotify& mcn)
{
	m_mcns->Remove( &mcn);
}

void CTree::TreeSelectedChangeNotifyAdd( CTreeSelectedChangeNotify& scn)
{
	m_scns->Insert( &scn);
}

void CTree::TreeSelectedChangeNotifyRemove( CTreeSelectedChangeNotify& scn)
{
	m_scns->Remove( &scn);
}

void CTree::SetTreeDataModal( CTreeDataModal& tdm)
{
	if( m_ptdm)
		m_ptdm->TreeDataModalDataChangeNotifyRemove( *this);
	m_ptdm = &tdm;
	if( m_ptdm)
		m_ptdm->TreeDataModalDataChangeNotifyAdd( *this);
	OnTreeDataModaDataChange( *m_ptdm);
}

CTreeDataModal& CTree::GetTreeDataModal()
{
	return *m_ptdm;
}

const CTreeDataModal& CTree::GetTreeDataModal() const
{
	return *m_ptdm;
}

void CTree::SetImageNormal( const ImageParams& ip)
{
	m_ipStats.SetImage( ip, CImageable::NORMAL);
}

const ImageParams& CTree::GetImageNormal() const
{
	return *m_ipStats.GetImage( CImageable::NORMAL);
}

void CTree::SetImageNormalMouseOn( const ImageParams& ip)
{
	m_ipStats.SetImage( ip, IMG_NORMALMOUSEON);
}

const ImageParams& CTree::GetImageNormalMouseOn() const
{
	return *m_ipStats.GetImage( IMG_NORMALMOUSEON);
}


void CTree::SetImageExpand( const ImageParams& ip)
{
	m_ipStats.SetImage( ip, IMG_EXPAND);
}

const ImageParams& CTree::GetImageExpand() const
{
	return *m_ipStats.GetImage( IMG_EXPAND);
}

void CTree::SetImageExpandMouseOn( const ImageParams& ip)
{
	m_ipStats.SetImage( ip, IMG_EXPANDMOUSON);
}

const ImageParams& CTree::GetImageExpandMouseOn() const
{
	return *m_ipStats.GetImage( IMG_EXPANDMOUSON);
}

void CTree::SetImageLeaf( const ImageParams& ip)
{
	m_ipStats.SetImage( ip, IMG_LEAF);
}

const ImageParams& CTree::GetImageLeaf() const
{
	return *m_ipStats.GetImage( IMG_LEAF);
}

void CTree::SetImageLeafMouseOn( const ImageParams& ip)
{
	m_ipStats.SetImage( ip, IMG_LEAFMOUSEON);
}

const ImageParams& CTree::GetImageLeafMouseOn() const
{
	return *m_ipStats.GetImage( IMG_LEAFMOUSEON);
}

void CTree::SetImageLeafSelected( const ImageParams& ip)
{
	m_ipStats.SetImage( ip, IMG_LEAFSELECTED);
}

const ImageParams& CTree::GetImageLeafSelected() const
{
	return *m_ipStats.GetImage( IMG_LEAFSELECTED);
}

void CTree::SetImageBehindText( bool bBehind)
{
	m_bImageBehindText = bBehind;
}

bool CTree::IsImageBehindText() const
{
	return m_bImageBehindText;
}

void CTree::SetMouseOnBackGroundColor( COLOR clr)
{
	m_clrMouseOnBackGround = clr;
}

COLOR CTree::GetMouseOnBackGroundColor() const
{
	return m_clrMouseOnBackGround;
}

void CTree::SetSelectedBackGroundColor( COLOR clr)
{
	m_clrSelectedBackGround = clr;
}

COLOR CTree::GetSelectedBackGroundColor() const
{
	return m_clrSelectedBackGround;
}

void CTree::SetSize( int w, int h)
{
	CComponent::SetSize( w, h);
}

int CTree::GetButtonWidth() const
{
	return m_nBtnWidth;
}

void CTree::SetButtonWidth( int n)
{
	if( n <= 0)
		n = 20;
	if( n == m_nBtnWidth)
		return;
	m_nBtnWidth = n;
	CheckTreeSize();
}

int CTree::GetButtonHeight() const
{
	return m_nBtnHeight;
}

void CTree::SetButtonHeight( int n)
{
	if( n <= 0)
		n = 20;
	if ( n > m_nViewHeight)
		n = m_nViewHeight;
	if( n == m_nBtnHeight)
		return;
	m_nBtnHeight = n;
	//CheckTreeSize();
}

int CTree::GetViewWidth() const
{
	return m_nViewWidth;
}

void CTree::SetViewWidth( int n)
{
	if( n <= 0)
		n = 20;
	if( n == m_nViewWidth)
		return;
	m_nViewWidth = n;
	m_nMaxViewWidth = n;
	CheckTreeSize();
}

void CTree::SetAutoViewWidth( bool bAuto)
{
	m_bAutoViewWidth = bAuto;
}

bool CTree::IsAutoViewWidth() const
{
	return m_bAutoViewWidth;
}

int CTree::GetViewHeight() const
{
	return m_nViewHeight;
}

void CTree::SetViewHeight( int n)
{
	if( n <= 0)
		n = 20;
	if( n == m_nViewHeight)
		return;
	m_nViewHeight = n;
	CheckTreeSize();
}

void CTree::OnMouseLeave()
{
	m_pnoMouseOn = NULL;
	CComponent::OnMouseLeave();
}

void CTree::OnTreeDataModaDataChange( CTreeDataModal& ldm)
{
	if( NULL == m_ptdm)
	{
		m_pviv->clear();
	}
	else
	{
		int nCount = (int)m_ptdm->GetRootCount();

		// 整理一下m_expandstate
		INLINECLASS::CTreeNodeExpandStateMap* newmap = new INLINECLASS::CTreeNodeExpandStateMap();
		for ( int i = 0; i < nCount; ++ i)
		{
			CTreeNodeObject* pnode = m_ptdm->GetRoot( i);
			INLINECLASS::CTreeNodeExpandStateMap::iterator it = m_expandstate->find( pnode->GetNodeId());
			if ( it != m_expandstate->end())
				(*newmap)[pnode->GetNodeId()] = (*m_expandstate)[pnode->GetNodeId()];
			else
				(*newmap)[pnode->GetNodeId()] = false;
		}
		m_expandstate->swap( *newmap);
		delete newmap;

		// 更新m_pviv
		m_pviv->resize( nCount);
		for( int i = 0; i < nCount; i ++)
		{
			INLINECLASS::TreeViewItem& tvi = m_pviv->at( i);
			tvi.node = m_ptdm->GetRoot( i);
			tvi.bIsLeaf = m_ptdm->IsLeaf( tvi.node);
			tvi.bExpand = false;
			tvi.offset = 0;
		}

		// 对根节点的展开状态进行恢复
		for ( int i = 0; i < nCount; ++ i)
		{
			CTreeNodeObject* pnode = m_ptdm->GetRoot( i);
			INLINECLASS::CTreeNodeExpandStateMap::iterator it = m_expandstate->find( pnode->GetNodeId());
			if ( it != m_expandstate->end() && it->second)
				ExpandNode( pnode, true, true);
		}
	}
	m_pnoSelected = NULL;
	CheckTreeSize();
}

void CTree::CheckTreeSize()
{
	int nWidth = 0;
	m_nMaxViewWidth = 0;
	m_nBtnHeight = min( m_nBtnHeight, m_nViewHeight); // min( m_nBtnHeight, m_nViewWidth);
	//m_nViewWidth = m_nBtnWidth;
	for( INLINECLASS::CTreeViewItemVector::iterator it = m_pviv->begin(); it != m_pviv->end(); ++ it)
	{
		INLINECLASS::TreeViewItem& tvi = *it;
		//m_nViewWidth = max( m_nViewWidth, tvi.node->GetNodeLength());
		// int nw = ( m_bImageBehindText ? 0 : tvi.offset) * m_nBtnWidth + m_nBtnWidth +  ( m_bAutoViewWidth ? tvi.node->GetNodeLength() : m_nViewWidth);//m_nViewWidth;
		if ( m_bAutoViewWidth)
		{
			int nodelength = tvi.node->GetNodeLength();
			m_nMaxViewWidth = max( m_nMaxViewWidth, nodelength);
			int nw = m_bImageBehindText ? m_nBtnWidth : ( tvi.offset * m_nBtnWidth + m_nBtnWidth + tvi.node->GetNodeLength());
			nWidth = max( nWidth, nw);
		}
		else
		{
			int nw = m_bImageBehindText ? m_nBtnWidth : ( tvi.offset * m_nBtnWidth + m_nBtnWidth + m_nViewWidth);
			nWidth = max( nWidth, nw);
		}
	} // for( INLINECLASS::CTreeViewItemVector::iterator it = m_pviv->begin(); it != m_pviv->end(); ++ it)
	SetSize( nWidth, m_nViewHeight * (int)m_pviv->size());

	INVALIDATE();
}

bool CTree::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	CTreeNodeObject* pnew = GetNodeFromPos( x, y);
	bool bn = ( pnew && !pnew->IsSameNode( m_pnoSelected));
	if( pnew)
		m_pnoSelected = pnew;
	if( bn)
		m_scns->NotifyAll( *this, &CTreeSelectedChangeNotify::OnTreeSelectedChange , m_pnoSelected);

	//if( NULL == pnew || m_bImageBehindText)
		CheckExpandNode( x, y);
	return CComponent::OnLeftButtonDown( x, y, key);
}

bool CTree::OnRightButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	CTreeNodeObject* pnew = GetNodeFromPos( x, y);
	bool bn = ( pnew && !pnew->IsSameNode( m_pnoSelected));
	if( pnew)
		m_pnoSelected = pnew;
	if( bn)
		m_scns->NotifyAll( *this, &CTreeSelectedChangeNotify::OnTreeRightButtonSelectedChange , m_pnoSelected);

	//if( NULL == pnew || m_bImageBehindText)
		CheckExpandNode( x, y);
	return CComponent::OnRightButtonDown( x, y, key);
}

bool CTree::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	CTreeNodeObject* pnew = GetNodeFromPos( x, y);
	bool bn = ( pnew && !pnew->IsSameNode( m_pnoMouseOn));
	m_pnoMouseOn = pnew;
	if( bn)
		m_mcns->NotifyAll( *this, &CTreeMouseOnChangeNotify::OnTreeMouseOnChange, pnew);
	return CComponent::OnMouseMove( x, y, key);
}

CTreeNodeObject* CTree::GetCurrentSelect()
{
	return m_pnoSelected;
}

void CTree::SetCurrentSelect( CTreeNodeObject* tno)
{
	bool bn = ( tno && !tno->IsSameNode( m_pnoSelected));
	m_pnoSelected = tno;
	if( bn)
		m_scns->NotifyAll( *this, &CTreeSelectedChangeNotify::OnTreeSelectedChange , m_pnoSelected);
}

CTreeNodeObject* CTree::GetNodeFromPos( int x, int y)
{
	y = y - GetAbsPosY();
	assert( m_nViewHeight > 0);
	y /= m_nViewHeight;
	if( y < 0 || y >= (int)m_pviv->size())
		return NULL;
	x = x - GetAbsPosX();
	INLINECLASS::TreeViewItem& tvi = m_pviv->at( y);
	int n = m_bImageBehindText ? 0 : tvi.offset *  m_nBtnWidth;
	if( x <= n)
		return NULL;
	n += m_nMaxViewWidth + m_nBtnWidth;
	if( x >= n)
		return NULL;
	return tvi.node;
}

CTreeNodeObject* CTree::GetNodeWithoutImageFromPos( int x, int y)
{
	y = y - GetAbsPosY();
	assert( m_nViewHeight > 0);
	y /= m_nViewHeight;
	if( y < 0 || y >= (int)m_pviv->size())
		return NULL;
	x = x - GetAbsPosX();
	INLINECLASS::TreeViewItem& tvi = m_pviv->at( y);
	int n = m_bImageBehindText ? 0 : tvi.offset *  m_nBtnWidth + m_nBtnWidth;
	if( x <= n)
		return NULL;
	n += m_nViewWidth;
	if( x >= n)
		return NULL;
	return tvi.node;
}

void CTree::CheckExpandNode( int x, int y)
{
	y = y - GetAbsPosY();
	assert( m_nViewHeight > 0);
	y /= m_nViewHeight;
	if( y < 0 || y >= (int)m_pviv->size())
		return;
	x = x - GetAbsPosX();
	INLINECLASS::TreeViewItem& tvi = m_pviv->at( y);
	int n = m_bImageBehindText ? 0 : tvi.offset * m_nBtnWidth ;
	if( x <= n)
		return;
	n += m_nBtnWidth;
	if( x >= n)
		return;
	ExpandNode( tvi.node, !tvi.bExpand, false);
}

void CTree::CloseExpandNode( int nIndex)
{
	int		nCount = (int)m_pviv->size();

	assert( nIndex >= 0 && nIndex < nCount);
	INLINECLASS::TreeViewItem& tvi = m_pviv->at( nIndex);
	nCount -= (nIndex + 1);
	
	for( nIndex ++; nCount;)
	{
		INLINECLASS::TreeViewItem& subtvi = m_pviv->at( nIndex);
		if( subtvi.offset <= tvi.offset /*&& !m_bImageBehindText*/)
			break;
		nCount --;
		m_pviv->erase( m_pviv->begin() + nIndex);
	}
}

void CTree::MakeExpandNodeDeepVector( INLINECLASS::TreeViewItem& tvi, INLINECLASS::CTreeViewItemVector& tviv, bool bDeep)
{
	assert( m_ptdm);

	int n = m_ptdm->GetChildCount( tvi.node);
	for( int i = 0; i < n; i ++)
	{
		INLINECLASS::TreeViewItem curtvi;
		curtvi.node = m_ptdm->GetChild( tvi.node, i);
		curtvi.bIsLeaf = m_ptdm->IsLeaf( curtvi.node);
		curtvi.bExpand = false;
		curtvi.offset = /*m_bImageBehindText ? tvi.offset :*/ tvi.offset + 1;
		
		tviv.push_back( curtvi);
		if( bDeep && !curtvi.bIsLeaf)
		{
			INLINECLASS::TreeViewItem& _backtvi = tviv.back();
			_backtvi.bExpand = true;

			curtvi.bExpand = true;
	        MakeExpandNodeDeepVector( curtvi, tviv, true);
		}
	}
}

int CTree::GetViewItemIndex( CTreeNodeObject* pNode)
{
	size_t n = m_pviv->size();
	for( size_t i = 0; i < n; i ++)
	{
		INLINECLASS::TreeViewItem& tvi = m_pviv->at( i);
		if( tvi.node->IsSameNode( pNode))
			return (int)i;
	}
	return -1;
}

void CTree::ExpandNode( CTreeNodeObject* pNode, bool bExpand, bool bDeep)
{
	int nIndex = GetViewItemIndex( pNode);
	assert( -1 != nIndex);

	INLINECLASS::TreeViewItem& tvi = m_pviv->at( nIndex);
	if( tvi.bIsLeaf || tvi.bExpand == bExpand)
		return;

	if( bExpand)	
	{
		tvi.bExpand = true;
		INLINECLASS::CTreeViewItemVector	tviv;
		MakeExpandNodeDeepVector( tvi, tviv, bDeep);
		if( !tviv.empty())
		{
			m_pviv->insert( m_pviv->begin() + ( nIndex + 1), tviv.begin(), tviv.end());
			CheckTreeSize();
		}
	}
	else
	{
		tvi.bExpand = false;
		CloseExpandNode( nIndex);
		CheckTreeSize();
	}
	(*m_expandstate)[pNode->GetNodeId()] = bExpand;
}

bool CTree::GetRootExpand( CTreeNodeObject* pNode)
{
	int nIndex = GetViewItemIndex( pNode);
	assert( -1 != nIndex);

	INLINECLASS::TreeViewItem& tvi = m_pviv->at( nIndex);

	return tvi.bExpand && !tvi.bIsLeaf;
}

void CTree::DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bLeaf, bool bExpand, bool bMouseOn, bool bSelected)
{
	if( bLeaf)
	{
		if ( bMouseOn && m_ipStats.DrawImage( draw, l, t, cx, cy, cw, ch, IMG_LEAFMOUSEON))
			return;
		else if ( bSelected && m_ipStats.DrawImage( draw, l, t, cx, cy, cw, ch, IMG_LEAFSELECTED))
			return;
		m_ipStats.DrawImage( draw, l, t, cx, cy, cw, ch, IMG_LEAF);
	}
	else if( bExpand)
	{
		if ( bMouseOn && m_ipStats.DrawImage( draw, l, t, cx, cy, cw, ch, IMG_EXPANDMOUSON))
			return;
		m_ipStats.DrawImage( draw, l, t, cx, cy, cw, ch, IMG_EXPAND);
	}
	else
	{
		if ( bMouseOn && m_ipStats.DrawImage( draw, l, t, cx, cy, cw, ch, IMG_NORMALMOUSEON))
			return;
		m_ipStats.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::NORMAL);
	}
}

void CTree::DrawViewItem( INLINECLASS::TreeViewItem& tvi, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	int		nBtnBegin, nBtnEnd;
	int		nx, nw, nr = cx + cw;
	
	bool	bMouseOn = m_pnoMouseOn == NULL ? false : m_pnoMouseOn->IsSameNode( tvi.node);
	bool	bSelected = m_pnoSelected == NULL ?  false : m_pnoSelected->IsSameNode( tvi.node);

	nBtnBegin = m_bImageBehindText ? 0 : tvi.offset * m_nBtnWidth;
	nBtnEnd = nBtnBegin + m_nBtnWidth;
	if( nr < nBtnBegin)
		return;
	if( cx < nBtnEnd)
	{
		if( cx < nBtnBegin)
			nx = 0;
		else
			nx = cx - nBtnBegin;
		if( nr < nBtnEnd)
			nw = nr - nBtnBegin;
		else
			nw = m_nBtnWidth  - nx;
		DrawButton( draw, l + nBtnBegin, t + ( ( m_nViewHeight - m_nBtnHeight) >> 1), nx, cy, nw, min( m_nBtnHeight, ch), tvi.bIsLeaf, tvi.bExpand, bMouseOn, bSelected);

		if( nr < nBtnEnd)
			return;
		nx = 0;

		if ( !m_bImageBehindText)
			nw = nr - nBtnEnd;
	}
	else
	{
		nx = cx - nBtnEnd;
		nw = cw;
	}

	CTreeDataModal::CCTYPE		cct;
	if( tvi.node->IsSameNode( m_pnoSelected))
		cct = CTreeDataModal::CCT_SELECT;
	else if( tvi.node->IsSameNode( m_pnoMouseOn))
		cct = CTreeDataModal::CCT_MOUSEON;
	else
		cct = CTreeDataModal::CCT_NORMAL;

	if ( m_bImageBehindText)
		m_ptdm->DrawComponent( tvi.node, cct, draw, l + nBtnBegin, t, nx, cy, nw, ch);
	else
	{
		int nodewidth = min( nw, max( 0, tvi.node->GetNodeLength() + 5 - nx)); // +5只是为了在绘制背景色时留出余量
		if ( bMouseOn)
			draw.FillRect( l+nx+nBtnEnd, t+cy, l+nx+nBtnEnd+nodewidth, t+cy+ch, m_clrMouseOnBackGround);
		else if ( bSelected)
			draw.FillRect( l+nx+nBtnEnd, t+cy, l+nx+nBtnEnd+nodewidth, t+cy+ch, m_clrSelectedBackGround);
		m_ptdm->DrawComponent( tvi.node, cct, draw, l + nBtnEnd, t, nx, cy, nw, ch);
	}
}

void CTree::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if( NULL == m_ptdm)
		return;
	int		nCount = (int)m_pviv->size();
	if( 0 == nCount)
		return;

	int		nr = cx + cw, cb = cy + ch;

	assert( m_nViewHeight);
	int		nStart = cy / m_nViewHeight;
	int		nEnd = min( cb / m_nViewHeight, nCount - 1);
	if( nStart >= nCount)
		nStart = nCount - 1;
	
	int nt = m_nViewHeight * nStart;
	{
		int ny = cy - m_nViewHeight * nStart;
		int nh = m_nViewHeight - ny;
		nh = min( nh, ch);
		INLINECLASS::TreeViewItem& tvi = m_pviv->at( nStart);
		m_pnoDrawing = tvi.node;
		DrawViewItem( tvi, draw, l, t + nt, cx, ny, cw, nh);
	}

	for( int i = nStart + 1; i < nEnd; i ++)
	{
		nt += m_nViewHeight;
		INLINECLASS::TreeViewItem& tvi = m_pviv->at( i);
		m_pnoDrawing = tvi.node;
		DrawViewItem( tvi, draw, l, t + nt, cx, 0, cw, m_nViewHeight);
	}

	{
		nt += m_nViewHeight;
		int nh = cb - nt;
		INLINECLASS::TreeViewItem& tvi = m_pviv->at( nEnd);
		if( nh > 0)
		{
			m_pnoDrawing = tvi.node;
			DrawViewItem( tvi, draw, l, t + nt, cx, 0, cw, nh);
		}
	}
}

void CTree::OnComponentShow()
{
	m_ipStats.RegisterImages();
	if ( m_ptdm)
		m_ptdm->OnComponentShow();
	CComponent::OnComponentShow();
}

void CTree::OnComponentHide()
{
	m_ipStats.FreeImages();
	if ( m_ptdm)
		m_ptdm->OnComponentHide();
	CComponent::OnComponentHide();
}

void CTree::ResetTreeDataModule( CTreeDataModal* tdm)
{
	if ( tdm && m_ptdm == tdm)
		m_ptdm = NULL;
}

const CTreeNodeObject* CTree::GetCurrentDrawingNode() const
{
	return m_pnoDrawing;
}

} // namespace GXWINDOW { 
