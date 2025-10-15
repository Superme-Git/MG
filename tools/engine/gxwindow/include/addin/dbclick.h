#pragma once

namespace GXWADDIN {

	class CDoubleClick;

	class CDoubleClickEvent : public GXWINDOW::CComponentNotify
	{
		friend class CDoubleClick;
	public:
		CDoubleClickEvent() {}
		virtual ~CDoubleClickEvent() {}
	protected:
		virtual void OnDoubleClick( CDoubleClick& dc, int x, int y, int key) = 0;
		virtual void OnSingleClick( CDoubleClick& dc, int x, int y, int key) {}
	};

	class CDoubleClick : public GXWINDOW::CTimerNotify
	{
		GXWINDOW::TIMERID	m_Event;
		bool	m_bOn;
		int		m_x;
		int		m_y;
		int		m_key;
		int		m_id;

		enum
		{
			TIME_DEALY	=	250,
		};

		CDoubleClickEvent*	m_notify;
	public:
		CDoubleClick( int id = -1) : m_id( id), m_notify( NULL), m_bOn( false) {}
		virtual ~CDoubleClick() {}
	protected:
		virtual void OnTimer( const GXWINDOW::TIMERID& nid)
		{
			m_bOn = false;
			if( m_notify)
				m_notify->OnSingleClick( *this, m_x, m_y, m_key);
		}
	public:
		void SetID( int id) 
		{ 
			m_id = id; 
		}
		int GetID() const
		{
			return m_id;
		}
		void SetNotify( CDoubleClickEvent& notify)
		{
			assert( NULL == m_notify && "只能设置一个通告对象");
			m_notify = &notify;
		}
		void RemoveNotify( CDoubleClickEvent& notify)
		{
			if( NULL == m_notify)
				return;
			if( &notify != m_notify)
			{
				assert( !"移除了一个不存在的通告对象");
				return;
			}
			m_notify = NULL;
		}
		void ClearNotify()
		{
			m_notify = NULL;
		}
		void OnButtonDown( int x, int y, int key)
		{
			if( m_bOn)
			{
				m_bOn = false;
				m_Event.CancelTimer();
				if( m_notify)
					m_notify->OnDoubleClick( *this, x, y, key);
			}
			else
			{
				m_Event.NewTimer( *this, TIME_DEALY);
				m_x = x;
				m_y = y;
				m_key = key;
				m_bOn = true;
			}
		}

	};
} // namespace GXWADDIN {