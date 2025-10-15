#pragma once

namespace GXWADDIN
{

// 实现类似QQ主界面抽屉菜单的控件
// 这个QQBar控件实际只是一个QQBar形式的组织容器, 子标题为按钮
// 各内容控件（如列表）的事件和内容更新由使用者在外部自行处理
// 该控件只负责控制QQBar控件的显示形态以及展开、合起事件
// 控件实现了三种模式:
// 1.互斥  任何时刻最多只允许展开一个item的内容,
//         点击一个已展开的标题时将合起其item内容
// 2.相容  各个item均独立自由开闭, 互不干扰
// 3.全开  所有item全部展开, 不允许关闭, 此时标题按钮不处理点击
// !!!注意, 用时有两个约定: 
// 1.宽度统一限定为QQBar宽度
// 2.在外部析构已添加的控件时, 先将其从QQBar中移除


enum QQBar_Mode
{
	QBM_MUTEX,		// 互斥
	QBM_FREE,		// 相容
	QBM_ALLOPEN,	// 全开
};

class QQBarItemVector;
class CQQBar 
	: public GXWINDOW::CPanel
	, virtual public GXWINDOW::CButtonClickNotify
	, virtual public GXWINDOW::CComponentSizeChangedNotify
{
	QQBarItemVector*	m_pitems;
	QQBar_Mode			m_qbmode;
public:
	CQQBar();
	virtual ~CQQBar();
public:
	// 指定模式
	void SetMode( QQBar_Mode m);
	QQBar_Mode GetMode() const;

	// 在指定位置添加一个QQBarItem, 每个QQBarItem由标题和内容构成, 标题必须为CTextImageCheckButton, 内容可设为任何控件
	void InsertItem( int n, GXWINDOW::CAbstractButton& titlebtn, GXWINDOW::CComponent& content, __int64 param = 0);

	// 删除指定位置的QQBarItem
	void RemoveItem( int n);

	// 获取标题按钮
	const GXWINDOW::CAbstractButton* GetItemTitle( int n) const;

	// 获取item内容控件
	const GXWINDOW::CComponent* GetItemContent( int n) const;

	// 获取item个数
	int GetItemCount() const;

	// 设置参数
	void SetItemParam( int n, __int64 param);

	// 获取参数
	__int64 GetItemParam( int n) const;

	// 展开指定Item
	void ExpandItem( int n);

	// 合拢指定Item
	void CloseItem( int n);

	// 获取指定Item是否展开
	bool IsItemExpand( int n) const;

private:
	// 标题按钮点击响应
	virtual void OnButtonClick( GXWINDOW::CAbstractButton& c) override;

	// 内容控件大小改变响应
	virtual void OnComponentSizeChanged( GXWINDOW::CComponent& c, int nw, int nh, int ow, int oh) override;

	// 更新QQBar控件布局, (也许可以自动调用: 当某个item内容控件发生变化时, 由使用者主动调用)
	virtual void DoLayout() override;
};


} // namespace GXWADDIN