//////////////////////////////////////////////////////////////////////////
//
// LayoutManager.h
//
// 窗口布局管理器
//
// author: yanghaibo
// date : 2008-11-10
//
#pragma once
#include <string>

class CDIYButton;
class CODProgressCtrl;
class CDIYEditCtrl;

class CLayoutManager
{
public:
	CLayoutManager(void);
	~CLayoutManager(void);

	//
	// summary: 指定资源包目录和配置文件，并为布局管理器指定被管理的面板窗口名
	//
	BOOL Load( LPCTSTR lpszDirectory, LPCTSTR lpszConfigFile, LPCTSTR lpszWndName );

	//
	// summary: 渲染窗口
	//
	BOOL SetPanel( CWnd* pPanel, BOOL bDoRanging = TRUE );

	//
	// summary: 从资源包中加载图标资源
	//
	HICON LoadIcon(LPCTSTR lpszName, BOOL bBigIcon);

	BOOL ApplyEdit( UINT nID, CDIYEditCtrl& rButton );

	//
	// summary: 将布局信息和渲染信息应用到nID所指定的按钮上，同时设置提示信息。
	//
	BOOL ApplyButton( UINT nID, CDIYButton& rButton );

	//
	// summary: 将布局信息和渲染信息应用到nID所指定的进度条上
	//
	BOOL ApplyProgressCtrl( UINT nID, CODProgressCtrl& rProgressCtrl);

	//
	// summary: 获取布局配置信息中为控件指定的位置信息
	//
	BOOL GetControlRect( LPCTSTR lpszName, CRect& rRect );

	//
	// summary: 使用布局管理器绘制面板窗口
	//
	BOOL OnPanelPaint(CDC *pDC);

	//
	// summary: 获取布局配置信息中为控件指定的颜色信息,对应于lpszAttrName属性
	//
	BOOL GetControlAttributeColor( LPCTSTR lpszControlName, LPCTSTR lpszAttrName, COLORREF& dwColor );

	//
	// summary: 获取布局配置信息中为控件指定的悬浮提示信息,对应于tipinfo属性
	//
	BOOL GetControlTipInfo( LPCTSTR lpszControlName, CStringW& rstrTipInfo );

	//
	// summary: 获取布局配置信息中为控件指定的颜色信息,对应于c属性
	//
	BOOL GetControlColor( LPCTSTR lpszName, COLORREF& dwColor );

	//
	// summary: 获取布局配置信息中为控件指定的背景色信息, 对应于b属性
	//
	BOOL GetControlBkColor( LPCTSTR lpszName, COLORREF& dwColor );

	//
	// summary: 获取布局配置信息中为窗口背景图片指定的位置及边框信息
	//
	BOOL GetFRImageRect( CRect& rRect, CRect& rBorder );

	//
	// summary: 获取布局配置信息中为窗口背景图片的内存dc
	//
	CDC& GetFRMemDC() { return m_dcMemFR; }

	//
	// summary: 获取布局配置信息中指定图片 HBITMAP
	//
	BOOL GetBitmap( LPCTSTR lpszControlName, LPCTSTR lpszImageElementName, HBITMAP& rBitmap, BOOL bDIB, CRect& rRect);

protected:
	//
	// summary: 加载资源包目录和并读取配置文件
	//
	BOOL _LoadLayout( LPCTSTR lpszDirectory, LPCTSTR lpszConfigFile);

	//
	// summary: 初始化布局管理器所对应的面板窗口
	//
	BOOL _InitPanel(BOOL bDoRanging);

	//
	// summary: 
	//		从资源包中加载指定的位图资源
	// 
	// parameter:
	//		lpszFileName - 相对于资源包根结点的相对文件名
	//
	// returns: 
	//		位图资源句柄。NULL表示加载失败
	//
	HBITMAP _LoadBitmap( LPCTSTR lpszFileName );
	HBITMAP _LoadDIBitmap( LPCTSTR lpszFileName, BOOL bPremultiply );

	//
	// summary: 
	//		解析配置文件中的Image结点，读取位图资源和剪裁坐标信息
	//
	// parameters:
	//		spImageNode - Image结点
	//		rBitmap - 保存读取的位图资源对象,使用方不可以DeleteObject
	//		bDIB - 读取的rBitmap为DIB还是DDB
	//		rcRect - 保存读取的剪裁坐标
	//		bPremultiply - 对于带有ALPHA通道的位图是否执行Premultiply操作。
	// returns:
	//		TRUE - 成功解析
	//		FALSE - 由于资源残缺等原因导致解析失败
	//
	BOOL _ParseImageNode(CComPtr<IXMLDOMNode> spImageNode, HBITMAP& rBitmap, BOOL bDIB, CRect& rRect, BOOL bPremultiply = TRUE);

	//
	// summary: 
	//		解析配置文件中的结点，读取坐标信息
	//
	// parameters:
	//		spNode - Node结点
	//		rcRect - 保存读取的剪裁坐标
	//
	// returns:
	//		TRUE - 成功解析
	//		FALSE - 由于资源残缺等原因导致解析失败
	//
	BOOL _ParseNodeRect(CComPtr<IXMLDOMNode> spNode, CRect& rRect);

protected:
	CWnd* m_wndPanel;
	CBitmap m_bitmap;
	BITMAP m_bitmapInfo;
	CDC m_dcMemFR;
	CComPtr<IXMLDOMNode> m_rootNode;
	CMapStringToPtr		m_ddbMap;
	CMapStringToPtr		m_dibMap;
	CMapStringToPtr		m_dibMapPremultiplied;

	std::wstring		m_wndName;
	std::wstring		m_mountName;
};
