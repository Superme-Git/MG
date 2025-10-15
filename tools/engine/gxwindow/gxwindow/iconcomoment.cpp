#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS { 

class CAnimateIconVector : public std::vector<int>
{
public:
	CAnimateIconVector() {}
	virtual ~CAnimateIconVector() {}
};

} // namespace INLINECLASS { 

CIconImageManager::CAbstractImplement::CAbstractImplement() { }

CIconImageManager::CAbstractImplement::~CAbstractImplement() { }

CIconImageManager::CIconImageManager()
	: m_impl( NULL)
{
}

CIconImageManager::~CIconImageManager()
{
}

void CIconImageManager::SetImplement( CAbstractImplement& impl)
{
	m_impl = &impl;
}

void CIconImageManager::DrawIcon32( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch)
{
	if( NULL == m_impl)
		return;
	m_impl->DrawIcon32( draw, nIcon, l, t, cx, cy, cw, ch);
}

void CIconImageManager::DrawIcon16( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch)
{
	if( NULL == m_impl)
		return;
	m_impl->DrawIcon16( draw, nIcon, l, t, cx, cy, cw, ch);
}

void CIconImageManager::DrawEmotion( CDraw& draw, int nPack, int nEmotion, int l, int t, int cw, int ch, int sx, int sy, int sw, int sh)
{
	if ( NULL == m_impl)
		return;
	m_impl->DrawEmotion( draw, nPack, nEmotion, l, t, cw, ch, sx, sy, sw, sh);
}

void CIconImageManager::DrawTreeIcon( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch)
{
	if ( NULL == m_impl)
		return;
	m_impl->DrawTreeIcon( draw, nIcon, l, t, cx, cy, cw, ch);
}

CIconImageManager& CIconImageManager::GetInstance()
{
	static CIconImageManager		iim;
	return iim;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAnimateIconComponent::CAnimateIconComponent()
	: m_aiv( new INLINECLASS::CAnimateIconVector)
{
}

CAnimateIconComponent::~CAnimateIconComponent()
{
	delete m_aiv;
}

int CAnimateIconComponent::GetCount() const
{
	return (int)m_aiv->size();
}

int CAnimateIconComponent::GetAt( int nIndex) const
{
	assert( nIndex >= 0 && nIndex < GetCount());
	return m_aiv->at( nIndex);
}

void CAnimateIconComponent::SetAt( int nIndex, int nIcon)
{
	assert( nIndex >= 0 && nIndex < GetCount());
	m_aiv->at( nIndex) = nIcon;
}

void CAnimateIconComponent::Insert( int nIcon)
{
	m_aiv->push_back( nIcon);
}

void CAnimateIconComponent::RemoveAt( int nIndex)
{
	assert( nIndex >= 0 && nIndex < GetCount());
	m_aiv->erase( m_aiv->begin() + nIndex);
}

void CAnimateIconComponent::RemoveAll()
{
	m_aiv->clear();
}

void CAnimateIconComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	int nCurrentIndex = GetCurrentIndex();
	if( nCurrentIndex < 0 || nCurrentIndex >= GetCount())
		return;
	CIconImageManager::GetInstance().DrawIcon32( draw, GetAt( nCurrentIndex), l, t, cx, cy, cw, ch);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CIconComponent::CIconComponent()
	: m_nImage( -1)
{
}

CIconComponent::~CIconComponent()
{
}

void CIconComponent::SetIcon( int nIcon)
{
	m_nImage = nIcon;
	INVALIDATE();
}

int CIconComponent::GetIcon() const
{
	return m_nImage;
}

void CIconComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);

	// [注意!!]这里要显示正确需要加上偏移 l+cx, t+cy, 目前梦诛和武侠没统一, 所以先不处理, 外面使用接口的请注意自己加上
	if( -1 != m_nImage)
		CIconImageManager::GetInstance().DrawIcon32( draw, m_nImage, l, t, cx, cy, cw, ch); 
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CEmotionComponent::CEmotionComponent() : m_nPack( -1), m_nEmotion( -1), m_nShowWidth( 0), m_nShowHeight( 0), m_nIconWidth( 0), m_nIconHeight( 0), m_fXScale( 1.0f), m_fYScale( 1.0f)
{
}

CEmotionComponent::~CEmotionComponent()
{

}

void CEmotionComponent::Draw(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	int sx = cx == 0 ? 0 : (int)( m_fXScale * cx + 0.5f);
	int sy = cy == 0 ? 0 : (int)( m_fYScale * cy + 0.5f);
	int sw = cw == m_nShowWidth ? m_nIconWidth : (int)( m_fXScale * cw + 0.5f);
	int sh = ch == m_nShowHeight ? m_nIconHeight : (int)( m_fYScale * ch + 0.5f);
	if( -1 != m_nPack && -1 != m_nEmotion)
		CIconImageManager::GetInstance().DrawEmotion( draw, m_nPack, m_nEmotion, l, t, cw, ch, sx, sy, sw, sh);
}

void CEmotionComponent::SetEmotion( int nPack, int nEmotion)
{
	m_nPack = nPack;
	m_nEmotion = nEmotion;
	INVALIDATE();
}

int CEmotionComponent::GetEmotionPack() const
{
	return m_nPack;
}

int CEmotionComponent::GetEmotion() const
{
	return m_nEmotion;
}

void CEmotionComponent::SetIconSize( int w, int h)
{
	m_nIconWidth = w;
	m_nIconHeight = h;
	if ( m_nShowWidth > 0)
		m_fXScale = (float)m_nIconWidth / m_nShowWidth;
	if ( m_nShowHeight > 0)
		m_fYScale = (float)m_nIconHeight / m_nShowHeight;
	INVALIDATE();
}

int CEmotionComponent::GetIconWidth() const
{
	return m_nIconWidth;
}

int CEmotionComponent::GetIconHeight() const
{
	return m_nIconHeight;
}

void CEmotionComponent::SetShowSize( int w, int h)
{
	m_nShowWidth = w;
	m_nShowHeight = h;
	m_fXScale = (float)m_nIconWidth / m_nShowWidth;
	m_fYScale = (float)m_nIconHeight / m_nShowHeight;
	INVALIDATE();
}

int CEmotionComponent::GetShowWidth() const
{
	return m_nShowWidth;
}

int CEmotionComponent::GetShowHeight() const
{
	return m_nShowHeight;
}

} // namespace GXWINDOW { 