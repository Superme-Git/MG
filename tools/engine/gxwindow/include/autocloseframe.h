#pragma once

namespace GXWADDIN
{

class GXWINDOW_EXPORTS_DLL CAutoCloseFrame : public GXWINDOW::CKeyInputNotify
{
public:
	CAutoCloseFrame() { GXWINDOW::CKeyInput::GetInstance().KeyInputNotifyAdd( *this); }
	virtual ~CAutoCloseFrame() { GXWINDOW::CKeyInput::GetInstance().KeyInputNotifyRemove( *this); }
protected:
	virtual bool OnKeyInput( UINT uMsg, WPARAM wParam, LPARAM lParam)
	{
		if( WM_KEYDOWN == uMsg && OnFrameKeyPress( (int)wParam))
			return true;
		return false;
	}
	virtual bool OnSetKeyFocus() { return true; }
	virtual void OnLostKeyFocus() {}
	virtual bool OnFrameKeyPress( int key) = 0;
	virtual void DoSetFocus()
	{
		GXWINDOW::CKeyInput::GetInstance().SetFocus( *this);
	}
};

class GXWINDOW_EXPORTS_DLL CHookEditKeyFrame
{
public:
	CHookEditKeyFrame() {}
	virtual ~CHookEditKeyFrame() {}
public:
	class CHEKEditArea : public GXWINDOW::CEditArea
	{
		CHookEditKeyFrame&	m_Container;
	public:
		CHEKEditArea( CHookEditKeyFrame& container, GXWINDOW::CAbstarctTextDecoder& decoder) : m_Container( container), GXWINDOW::CEditArea( decoder) {}
		virtual ~CHEKEditArea() {}
	protected:
		virtual bool OnKeyInput( UINT uMsg, WPARAM wParam, LPARAM lParam)
		{
			if(uMsg == WM_CHAR && m_Container.OnEditKeyPress( *this, (int)wParam))
				return true;
			else if(uMsg == WM_KEYDOWN && m_Container.OnKeyDown( *this, wParam, lParam))
				return true;
			return GXWINDOW::CEditArea::OnKeyInput( uMsg, wParam, lParam);
		}
	};
	class CHEKSEditAllocator : public GXWINDOW::CCombinationEditAreaAllocator
	{
		CHookEditKeyFrame&	m_Container;
	public:
		CHEKSEditAllocator( CHookEditKeyFrame& Container, GXWINDOW::CAbstarctTextDecoder& decoder = GXWINDOW::CFormatTextDecoder()) : m_Container( Container), GXWINDOW::CCombinationEditAreaAllocator( decoder) {}
		virtual ~CHEKSEditAllocator() {}
	public:
		virtual GXWINDOW::CEditArea* NewEditArea()
		{
			return new CHEKEditArea( m_Container, GetTextDecoder());
		}
	};
protected:
	virtual bool OnEditKeyPress( GXWINDOW::CEditArea& ea, int key) = 0;
	virtual bool OnKeyDown( GXWINDOW::CEditArea& ea, WPARAM wParam, LPARAM lParam) = 0;
};

} // namespace GXWADDIN
