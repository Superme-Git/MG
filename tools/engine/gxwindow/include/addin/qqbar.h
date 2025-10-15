#pragma once

namespace GXWADDIN
{

// ʵ������QQ���������˵��Ŀؼ�
// ���QQBar�ؼ�ʵ��ֻ��һ��QQBar��ʽ����֯����, �ӱ���Ϊ��ť
// �����ݿؼ������б����¼������ݸ�����ʹ�������ⲿ���д���
// �ÿؼ�ֻ�������QQBar�ؼ�����ʾ��̬�Լ�չ���������¼�
// �ؼ�ʵ��������ģʽ:
// 1.����  �κ�ʱ�����ֻ����չ��һ��item������,
//         ���һ����չ���ı���ʱ��������item����
// 2.����  ����item���������ɿ���, ��������
// 3.ȫ��  ����itemȫ��չ��, ������ر�, ��ʱ���ⰴť��������
// !!!ע��, ��ʱ������Լ��: 
// 1.���ͳһ�޶�ΪQQBar���
// 2.���ⲿ��������ӵĿؼ�ʱ, �Ƚ����QQBar���Ƴ�


enum QQBar_Mode
{
	QBM_MUTEX,		// ����
	QBM_FREE,		// ����
	QBM_ALLOPEN,	// ȫ��
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
	// ָ��ģʽ
	void SetMode( QQBar_Mode m);
	QQBar_Mode GetMode() const;

	// ��ָ��λ�����һ��QQBarItem, ÿ��QQBarItem�ɱ�������ݹ���, �������ΪCTextImageCheckButton, ���ݿ���Ϊ�κοؼ�
	void InsertItem( int n, GXWINDOW::CAbstractButton& titlebtn, GXWINDOW::CComponent& content, __int64 param = 0);

	// ɾ��ָ��λ�õ�QQBarItem
	void RemoveItem( int n);

	// ��ȡ���ⰴť
	const GXWINDOW::CAbstractButton* GetItemTitle( int n) const;

	// ��ȡitem���ݿؼ�
	const GXWINDOW::CComponent* GetItemContent( int n) const;

	// ��ȡitem����
	int GetItemCount() const;

	// ���ò���
	void SetItemParam( int n, __int64 param);

	// ��ȡ����
	__int64 GetItemParam( int n) const;

	// չ��ָ��Item
	void ExpandItem( int n);

	// ��£ָ��Item
	void CloseItem( int n);

	// ��ȡָ��Item�Ƿ�չ��
	bool IsItemExpand( int n) const;

private:
	// ���ⰴť�����Ӧ
	virtual void OnButtonClick( GXWINDOW::CAbstractButton& c) override;

	// ���ݿؼ���С�ı���Ӧ
	virtual void OnComponentSizeChanged( GXWINDOW::CComponent& c, int nw, int nh, int ow, int oh) override;

	// ����QQBar�ؼ�����, (Ҳ������Զ�����: ��ĳ��item���ݿؼ������仯ʱ, ��ʹ������������)
	virtual void DoLayout() override;
};


} // namespace GXWADDIN