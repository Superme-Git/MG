#pragma once

namespace GXWINDOW {

class CComponent;

namespace INLINECLASS { class CKeyInputNotifySet; }

class GXWINDOW_EXPORTS_DLL CKeyInputNotify : public CComponentNotify
{
	friend class CKeyInput;
public:
	CKeyInputNotify();
	virtual ~CKeyInputNotify();
protected:
	virtual bool OnKeyInput( UINT uMsg, WPARAM wParam, LPARAM lParam) = 0;
	virtual bool OnSetKeyFocus() = 0;
	virtual void OnLostKeyFocus() = 0;
};

class GXWINDOW_EXPORTS_DLL CKeyEnterEscapeHook : public CComponentNotify
{
	friend class CKeyInput;
public:
	CKeyEnterEscapeHook();
	virtual ~CKeyEnterEscapeHook();
protected:
	virtual bool OnKeyEnterEscapePress( bool bEnter) = 0;
};

class GXWINDOW_EXPORTS_DLL CKeyInput : public CUncopyableObject
{
	friend class CDesktop;

	INLINECLASS::CKeyInputNotifySet*	m_kins;
	CKeyInputNotify*					m_lastkin;
	CKeyInputNotify*					m_defaultkin;

	CKeyEnterEscapeHook*				m_EnterEscapeHook;

	CKeyInput();
	virtual ~CKeyInput();
public:
	static CKeyInput& GetInstance();
public:
	void KeyInputNotifyAdd( CKeyInputNotify& kin);
	void KeyInputNotifyRemove( CKeyInputNotify& kin);

	void SetFocus( CKeyInputNotify& kin);
	CKeyInputNotify* GetFocus();
	void SetDefaultFocus(CKeyInputNotify *pkin);
	CKeyInputNotify* GetDefaultFocus();
	void ReleaseFocus( CKeyInputNotify& kin);
	void ReleaseFocus();

	void SetEnterEscapeHook( CKeyEnterEscapeHook& hook);
	void ReleaseEnterEscapeHook( CKeyEnterEscapeHook& hook);
	void ReleaseEnterEscapeHook();
private:
	bool OnDispatchCharMessage( UINT uMsg, WPARAM wParam, LPARAM lParam, CDesktop& desktop);
};

} // namespace GXWINDOW {
