//////////////////////////////////////////////////////////////////////////
//
// LayoutManager.h
//
// ���ڲ��ֹ�����
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
	// summary: ָ����Դ��Ŀ¼�������ļ�����Ϊ���ֹ�����ָ�����������崰����
	//
	BOOL Load( LPCTSTR lpszDirectory, LPCTSTR lpszConfigFile, LPCTSTR lpszWndName );

	//
	// summary: ��Ⱦ����
	//
	BOOL SetPanel( CWnd* pPanel, BOOL bDoRanging = TRUE );

	//
	// summary: ����Դ���м���ͼ����Դ
	//
	HICON LoadIcon(LPCTSTR lpszName, BOOL bBigIcon);

	BOOL ApplyEdit( UINT nID, CDIYEditCtrl& rButton );

	//
	// summary: ��������Ϣ����Ⱦ��ϢӦ�õ�nID��ָ���İ�ť�ϣ�ͬʱ������ʾ��Ϣ��
	//
	BOOL ApplyButton( UINT nID, CDIYButton& rButton );

	//
	// summary: ��������Ϣ����Ⱦ��ϢӦ�õ�nID��ָ���Ľ�������
	//
	BOOL ApplyProgressCtrl( UINT nID, CODProgressCtrl& rProgressCtrl);

	//
	// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ����λ����Ϣ
	//
	BOOL GetControlRect( LPCTSTR lpszName, CRect& rRect );

	//
	// summary: ʹ�ò��ֹ�����������崰��
	//
	BOOL OnPanelPaint(CDC *pDC);

	//
	// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ������ɫ��Ϣ,��Ӧ��lpszAttrName����
	//
	BOOL GetControlAttributeColor( LPCTSTR lpszControlName, LPCTSTR lpszAttrName, COLORREF& dwColor );

	//
	// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ����������ʾ��Ϣ,��Ӧ��tipinfo����
	//
	BOOL GetControlTipInfo( LPCTSTR lpszControlName, CStringW& rstrTipInfo );

	//
	// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ������ɫ��Ϣ,��Ӧ��c����
	//
	BOOL GetControlColor( LPCTSTR lpszName, COLORREF& dwColor );

	//
	// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ���ı���ɫ��Ϣ, ��Ӧ��b����
	//
	BOOL GetControlBkColor( LPCTSTR lpszName, COLORREF& dwColor );

	//
	// summary: ��ȡ����������Ϣ��Ϊ���ڱ���ͼƬָ����λ�ü��߿���Ϣ
	//
	BOOL GetFRImageRect( CRect& rRect, CRect& rBorder );

	//
	// summary: ��ȡ����������Ϣ��Ϊ���ڱ���ͼƬ���ڴ�dc
	//
	CDC& GetFRMemDC() { return m_dcMemFR; }

	//
	// summary: ��ȡ����������Ϣ��ָ��ͼƬ HBITMAP
	//
	BOOL GetBitmap( LPCTSTR lpszControlName, LPCTSTR lpszImageElementName, HBITMAP& rBitmap, BOOL bDIB, CRect& rRect);

protected:
	//
	// summary: ������Դ��Ŀ¼�Ͳ���ȡ�����ļ�
	//
	BOOL _LoadLayout( LPCTSTR lpszDirectory, LPCTSTR lpszConfigFile);

	//
	// summary: ��ʼ�����ֹ���������Ӧ����崰��
	//
	BOOL _InitPanel(BOOL bDoRanging);

	//
	// summary: 
	//		����Դ���м���ָ����λͼ��Դ
	// 
	// parameter:
	//		lpszFileName - �������Դ������������ļ���
	//
	// returns: 
	//		λͼ��Դ�����NULL��ʾ����ʧ��
	//
	HBITMAP _LoadBitmap( LPCTSTR lpszFileName );
	HBITMAP _LoadDIBitmap( LPCTSTR lpszFileName, BOOL bPremultiply );

	//
	// summary: 
	//		���������ļ��е�Image��㣬��ȡλͼ��Դ�ͼ���������Ϣ
	//
	// parameters:
	//		spImageNode - Image���
	//		rBitmap - �����ȡ��λͼ��Դ����,ʹ�÷�������DeleteObject
	//		bDIB - ��ȡ��rBitmapΪDIB����DDB
	//		rcRect - �����ȡ�ļ�������
	//		bPremultiply - ���ڴ���ALPHAͨ����λͼ�Ƿ�ִ��Premultiply������
	// returns:
	//		TRUE - �ɹ�����
	//		FALSE - ������Դ��ȱ��ԭ���½���ʧ��
	//
	BOOL _ParseImageNode(CComPtr<IXMLDOMNode> spImageNode, HBITMAP& rBitmap, BOOL bDIB, CRect& rRect, BOOL bPremultiply = TRUE);

	//
	// summary: 
	//		���������ļ��еĽ�㣬��ȡ������Ϣ
	//
	// parameters:
	//		spNode - Node���
	//		rcRect - �����ȡ�ļ�������
	//
	// returns:
	//		TRUE - �ɹ�����
	//		FALSE - ������Դ��ȱ��ԭ���½���ʧ��
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
