#pragma once
// �����̣߳�ѹ������ѹ�Ⱥ�̨������������߳���
#include "ThreadPublic.h"
#include <string>
#include <list>
#include "WinPFSDoc.h"

class COperatorList
{
public:
	enum
	{ 
		DEFLATE,		// ѹ��
		INFLATE,		// ��ѹ
		INFLATEEND,		// һ�ν�ѹ���̽���
		EXIT,			// �߳��˳�
	};

	struct Item
	{
		int				m_type;		// ��������
		bool			m_isPath;	// �Ƿ�Ŀ¼
		std::wstring	m_src;		// Դ
		std::wstring	m_des;		// Ŀ��
	};

private:
	THREAD::CSemaphore		m_Semaphore;
	THREAD::CMutex			m_mutex;
	std::list<Item>			m_list;

public:
	COperatorList() {}
	~COperatorList() {}

	void Push( const Item& item)
	{
		{
			THREAD::CMutex::CScoped scope( m_mutex);
			m_list.push_back( item);
		}
		m_Semaphore.AddRef();
	}

	void PushFront( const Item& item)
	{
		{
			THREAD::CMutex::CScoped scope( m_mutex);
			m_list.push_front( item);
		}
		m_Semaphore.AddRef();
	}

	bool Pop( Item& item)
	{
		m_Semaphore.Wait();

		THREAD::CMutex::CScoped scope( m_mutex);
		if( !m_list.empty())
		{
			item = m_list.front();
			m_list.pop_front();
			return true;
		}
		return false;
	}

	void Clear()
	{
		THREAD::CMutex::CScoped scope( m_mutex);
		m_list.clear();
	}

	size_t Size()
	{
		THREAD::CMutex::CScoped scope( m_mutex);
		return m_list.size();
	}

	static COperatorList& GetInstance()
	{
		static COperatorList s_instance;
		return s_instance;
	}
};

class COperatorThread : public CWinThread
{
	DECLARE_DYNCREATE(COperatorThread)

protected:
	COperatorThread();           // protected constructor used by dynamic creation
	virtual ~COperatorThread();

	CWinPFSDoc*			m_doc;
	PFS::CDataBlock		m_buffer;
	CWnd*				m_pWnd;

	// ��ѹ�ļ�
	// srcΪpfs�ļ����ļ�·��
	// desΪwindows�ļ�·��
	bool InflateOneFile( const COperatorList::Item& item);

	// ��ѹ�ļ���ɺ����
	void OnEndInflateOneFile( const COperatorList::Item& item);

	// ��ѹ��Ŀ¼
	// �����Ƿ�����Ŀ¼
	bool InflateChildPath( const COperatorList::Item& item);

public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();

	void SetDocument( CWinPFSDoc* doc) { m_doc = doc; }

	void SetWnd( CWnd* pWnd) { m_pWnd = pWnd; }

protected:
	DECLARE_MESSAGE_MAP()

	// running and idle processing
	virtual int Run();
};


