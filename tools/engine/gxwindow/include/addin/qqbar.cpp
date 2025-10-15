#include "gxwaddindef.h"

namespace GXWADDIN
{

struct QQBarItem
{
	bool						expand;		// 展开标记, 展开时为true, 关闭时为false
	GXWINDOW::CAbstractButton*	title;		// 标题按钮, 绑定了一个外部的Button对象
	GXWINDOW::CComponent*		content;	// 内容, 绑定一个外部的控件对象
	__int64						param;		// 供外部使用的一个辅助参数, 爱咋用咋用
};
class QQBarItemVector : public std::vector<QQBarItem> 
{
public:
	QQBarItemVector() {}
	virtual ~QQBarItemVector() {}
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CQQBar::CQQBar() : m_pitems( new QQBarItemVector()), m_qbmode( QBM_MUTEX)
{

}

CQQBar::~CQQBar()
{
	delete m_pitems;
}

void CQQBar::SetMode( QQBar_Mode m)
{
	m_qbmode = m;
	RemoveAllComponent();
	for ( QQBarItemVector::iterator it = m_pitems->begin(), ite = m_pitems->end(); it != ite; ++ it)
	{
		QQBarItem& item = *it;
		item.expand = ( QBM_ALLOPEN == m_qbmode);
	}
	DoLayout();
}

QQBar_Mode CQQBar::GetMode() const
{
	return m_qbmode;
}

// 在指定位置添加一个QQBarItem, 每个QQBarItem由标题和内容构成, 标题必须为CTextImageCheckButton, 内容可设为任何控件
void CQQBar::InsertItem( int n, GXWINDOW::CAbstractButton& titlebtn, GXWINDOW::CComponent& content, __int64 param)
{
	if ( n < 0)
		n = 0;
	else if ( n > (int)m_pitems->size())
		n = (int)m_pitems->size();

	if ( CPanel* p = dynamic_cast<CPanel*>( titlebtn.GetParent()))
		p->RemoveComponent( &titlebtn);
	if ( CPanel* p = dynamic_cast<CPanel*>( content.GetParent()))
		p->RemoveComponent( &content);

	// 限定宽度
	titlebtn.SetSize( GetWidth(), titlebtn.GetHeight());
	content.SetSize( GetWidth(), content.GetHeight());

	QQBarItem newitem;
	newitem.expand = ( QBM_ALLOPEN == m_qbmode);
	newitem.param = param;
	newitem.title = &titlebtn;
	newitem.title->ButtonClickNotifyAdd( *this);

	content.SetPos( 0, 0);
	newitem.content = &content;
	newitem.content->ComponentSizeChangedNotifyAdd( *this);

	m_pitems->insert( m_pitems->begin() + n, newitem);

	InsertComponent( newitem.title);
	InsertComponent( newitem.content);

	DoLayout();
}

// 删除指定位置的QQBarItem
void CQQBar::RemoveItem( int n)
{
	if ( n < 0 || n >= (int)m_pitems->size())
		return;

	QQBarItem& item = (*m_pitems)[n];
	item.title->ButtonClickNotifyRemove( *this);
	item.content->ComponentSizeChangedNotifyRemove( *this);

	RemoveComponent( item.title);
	RemoveComponent( item.content);

	m_pitems->erase( m_pitems->begin() + n);

	DoLayout();
}

// 获取标题按钮
const GXWINDOW::CAbstractButton* CQQBar::GetItemTitle( int n) const
{
	assert( n >= 0 && n < (int)m_pitems->size());

	return (*m_pitems)[n].title;
}

// 获取item内容控件
const GXWINDOW::CComponent* CQQBar::GetItemContent( int n) const
{
	assert( n >= 0 && n < (int)m_pitems->size());

	return (*m_pitems)[n].content;
}

// 获取Item个数
int CQQBar::GetItemCount() const
{
	return (int)m_pitems->size();
}

// 设置参数
void CQQBar::SetItemParam( int n, __int64 param)
{
	assert( n >= 0 && n < (int)m_pitems->size());

	(*m_pitems)[n].param = param;
}

// 获取参数
__int64 CQQBar::GetItemParam( int n) const
{
	assert( n >= 0 && n < (int)m_pitems->size());

	return (*m_pitems)[n].param;
}

// 标题按钮点击响应
void CQQBar::OnButtonClick( GXWINDOW::CAbstractButton& c)
{
	if ( QBM_ALLOPEN == m_qbmode)
	{
		if ( GXWINDOW::CAbstractCheckButton* pbtn = dynamic_cast<GXWINDOW::CAbstractCheckButton*>( &c))
			pbtn->SetChecked( false, false);
		return;
	}

	for ( QQBarItemVector::iterator it = m_pitems->begin(), ite = m_pitems->end(); it != ite; ++ it)
	{
		QQBarItem& item = *it;
		if ( item.title->IsSameComponent( c))
		{
			item.expand = !item.expand;
		}
		else if ( QBM_MUTEX == m_qbmode)
		{
			item.expand = false;
			if ( GXWINDOW::CAbstractCheckButton* pbtn = dynamic_cast<GXWINDOW::CAbstractCheckButton*>( item.title))
				pbtn->SetChecked( false, false);
		}
	}

	DoLayout();
}

// 内容控件大小改变响应
void CQQBar::OnComponentSizeChanged( GXWINDOW::CComponent& c, int nw, int nh, int ow, int oh)
{
	if ( CPanel* p = dynamic_cast<CPanel*>( c.GetParent()))
		p->SetSize( c.GetWidth(), c.GetHeight());
	DoLayout();
}

// 更新QQBar控件布局
void CQQBar::DoLayout()
{
	int x = 0;
	int y = 0;

	RemoveAllComponent();
	for ( QQBarItemVector::iterator it = m_pitems->begin(), ite = m_pitems->end(); it != ite; ++ it)
	{
		QQBarItem& item = *it;
		item.title->SetPos( x, y);
		y += item.title->GetHeight();
		InsertComponent( item.title);
		if ( item.expand)
		{
			item.content->SetPos( x, y);
			y += item.content->GetHeight();
			InsertComponent( item.content);
		}
	}

	SetSize( GetWidth(), y);
}

// 展开指定Item
void CQQBar::ExpandItem( int n)
{
	if ( QBM_ALLOPEN == m_qbmode)
		return;

	assert( n >= 0 && n < (int)m_pitems->size());
	QQBarItem& target = (*m_pitems)[n];
	if ( target.expand)
		return;

	for ( QQBarItemVector::iterator it = m_pitems->begin(), ite = m_pitems->end(); it != ite; ++ it)
	{
		QQBarItem& item = *it;
		if ( item.title->IsSameComponent( target.title))
		{
			item.expand = true;
			if ( GXWINDOW::CAbstractCheckButton* pbtn = dynamic_cast<GXWINDOW::CAbstractCheckButton*>( item.title))
				pbtn->SetChecked( true, false);
		}
		else if ( QBM_MUTEX == m_qbmode)
		{
			item.expand = false;
			if ( GXWINDOW::CAbstractCheckButton* pbtn = dynamic_cast<GXWINDOW::CAbstractCheckButton*>( item.title))
				pbtn->SetChecked( false, false);
		}
	}

	DoLayout();
}

// 合拢指定Item
void CQQBar::CloseItem( int n)
{
	if ( QBM_ALLOPEN == m_qbmode)
		return;

	assert( n >= 0 && n < (int)m_pitems->size());
	QQBarItem& target = (*m_pitems)[n];
	if ( !target.expand)
		return;

	target.expand = false;
	if ( GXWINDOW::CAbstractCheckButton* pbtn = dynamic_cast<GXWINDOW::CAbstractCheckButton*>( target.title))
		pbtn->SetChecked( false, false);

	DoLayout();
}

// 获取指定Item是否展开
bool CQQBar::IsItemExpand( int n) const
{
	assert( n >= 0 && n < (int)m_pitems->size());
	return (*m_pitems)[n].expand;
}

} // namespace GXWADDIN
