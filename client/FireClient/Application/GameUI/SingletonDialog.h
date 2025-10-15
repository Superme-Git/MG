#ifndef __SINGLETONDIALOG_HPP
#define __SINGLETONDIALOG_HPP

#include <cassert>

template<class T>
class SingletonDialog
{

public:
	static T* GetSingleton()
	{
		return m_sSingletonDlg;
	}

	static T* GetSingletonDialog()
	{
		if (m_sSingletonDlg == 0) {
			m_sSingletonDlg = new T;
			if (m_sSingletonDlg) {
				m_sSingletonDlg->OnCreate();
			}
		}
		return m_sSingletonDlg;
	}

	static void OnExit()
	{
		if (m_sSingletonDlg == NULL) {
			return;
		}
		m_sSingletonDlg->OnClose();
	}

	static void ToggleOpenHide()
	{
		if (m_sSingletonDlg == NULL) {
			m_sSingletonDlg = new T;
			if (m_sSingletonDlg) {
				m_sSingletonDlg->OnCreate();
			}
		} else {
			bool bVisible = m_sSingletonDlg->IsVisible();
			if(bVisible) {
				m_sSingletonDlg->OnClose();
			} else {
				m_sSingletonDlg->SetVisible(true);
			}
		}
	}

	static T* GetSingletonDialogAndShowIt()
	{
		if (m_sSingletonDlg == NULL) {
			m_sSingletonDlg = new T;
			if (m_sSingletonDlg) {
				m_sSingletonDlg->OnCreate();
			}
		} else {
			m_sSingletonDlg->SetVisible(true);
		}
		return m_sSingletonDlg;
	}

	static void  CloseDialog()
	{
		if (m_sSingletonDlg == NULL) {
			return;
		}
		m_sSingletonDlg->OnClose();
		delete m_sSingletonDlg;
		m_sSingletonDlg = NULL;
	}

private:
	static T* m_sSingletonDlg;

public:
	SingletonDialog()
	{ }

	virtual ~SingletonDialog()
	{
		m_sSingletonDlg = NULL;
	}
};

template<class T>
T* SingletonDialog<T>::m_sSingletonDlg = NULL;

#endif
