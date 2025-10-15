#pragma once
// 操作线程，压缩、解压等后台操作都在这个线程中
#include "ThreadPublic.h"
#include <string>
#include <list>
#include "WinPFSDoc.h"

class COperatorList
{
public:
	enum
	{ 
		DEFLATE,		// 压缩
		INFLATE,		// 解压
		INFLATEEND,		// 一次解压过程结束
		EXIT,			// 线程退出
	};

	struct Item
	{
		int				m_type;		// 操作类型
		bool			m_isPath;	// 是否目录
		std::wstring	m_src;		// 源
		std::wstring	m_des;		// 目标
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

	// 解压文件
	// src为pfs文件中文件路径
	// des为windows文件路径
	bool InflateOneFile( const COperatorList::Item& item);

	// 解压文件完成后调用
	void OnEndInflateOneFile( const COperatorList::Item& item);

	// 解压子目录
	// 返回是否有子目录
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


