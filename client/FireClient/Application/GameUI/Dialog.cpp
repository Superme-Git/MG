#include "stdafx.h"
#include "Dialog.h"
#include "elements/CEGUIFrameWindow.h"
#include "GameUIManager.h"
#include "SceneMovieManager.h"


std::set<Dialog*> Dialog::m_setDlgs;
Dialog* Dialog::s_pTickingDlg = NULL;

Dialog::Dialog()
	: m_pMainFrame(NULL)
	, m_dwLife(0)
	, m_bCloseIsHide(false)
	, m_bEscClose(true)
	, m_dwCreatTime(0)
	, m_pParentWindow(NULL)
	, m_bRelease(false)
	, m_bCloseing(false)
	, m_fElapseTime(0)
{
	m_eDialogType = eDialogType_RBtnClose;
	Dialog::m_setDlgs.insert(this);
}

Dialog::~Dialog()
{
	//be sure this dialog is removed from Dialog::ma_Dlgs
	if (s_pTickingDlg != this)
	{
		std::set<Dialog*>::iterator it = Dialog::m_setDlgs.find(this);
		if (it != Dialog::m_setDlgs.end())
		{
			Dialog::m_setDlgs.erase(it);
		}
	}
	
}

void Dialog::OnCreate(CEGUI::Window* pParentWindow, const CEGUI::String& name_prefix)
{
    m_pParentWindow = pParentWindow;
	if (m_pMainFrame && m_bCloseIsHide)
	{
		SetVisible(true);
		return;
	}

	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();

	const CEGUI::String& fileName = GetLayoutFileName();
	try{
		m_pMainFrame = winMgr.loadWindowLayout(fileName, name_prefix);
		if (m_pMainFrame)
		{
			m_bCloseIsHide = m_pMainFrame->GetCloseIsHide();
		}
	}
	catch (...)
	{
		return;
	}
	m_pMainFrame->SetEscClose(m_bEscClose);

	if (m_eDialogType & eDialogType_ChatOutWnd)
	{
	}
	else
	{
		if (CEGUI::System::getSingleton().getGUISheet() == NULL)
			return;
		if (CEGUI::System::getSingleton().getGUISheet() == m_pMainFrame)
		{
			return;
		}

		if (pParentWindow == NULL)
		{
			if (gGetSceneMovieManager() && gGetSceneMovieManager()->isOnSceneMovie())
			{
				if (gGetGameUIManager()->GetMainRootWnd())
				{
					if (m_eDialogType & eDialogType_SceneMovieWnd)
					{
						CEGUI::System::getSingleton().getGUISheet()->addChildWindow(m_pMainFrame);
					}
					else
					{
						gGetGameUIManager()->AddWndToRootWindow(m_pMainFrame);
					}
				}
			}
			else
			{
				CEGUI::System::getSingleton().getGUISheet()->addChildWindow(m_pMainFrame);
			}
		}
		else
		{
			pParentWindow->addChildWindow(m_pMainFrame);
		}
	}

	if (m_eDialogType & eDialogType_InScreenCenter)
	{
		if (m_pMainFrame)
		{
			m_pMainFrame->CenterInParent();
		}
	}

	if (GetWindow()->GetCreateWndEffect() != CEGUI::CreateWndEffect_None) {
		GetWindow()->BeginCreateEffect();
	}

	CEGUI::PushButton* closeBtn = GetCloseBtn();
	if (NULL != closeBtn)
	{
		closeBtn->subscribeEvent(CEGUI::PushButton::EventClicked, CEGUI::Event::Subscriber(&Dialog::HandleCloseBtnClick, this));
	}
	if (m_eDialogType & eDialogType_RBtnClose)
	{
		GetWindow()->subscribeEvent(CEGUI::Window::EventMouseButtonDown, CEGUI::Event::Subscriber(&Dialog::HandleRightButtonDown, this));
		GetWindow()->SetHandleDragMove(true);
	}

	if (m_eDialogType & eDialogType_TimeLimit)
	{
		m_dwCreatTime = Nuclear::GetMilliSeconds();
		GetWindow()->subscribeEvent(CEGUI::Window::EventWindowUpdated, CEGUI::Event::Subscriber(&Dialog::HandleTimePast, this));
	}

	GetWindow()->subscribeEvent(CEGUI::Window::EventShown, CEGUI::Event::Subscriber(&Dialog::HandleDialogOpen, this));
	GetWindow()->subscribeEvent(CEGUI::Window::EventHidden, CEGUI::Event::Subscriber(&Dialog::HandleDialogClose, this));
	GetWindow()->subscribeEvent(CEGUI::Window::EventDestructionStarted, CEGUI::Event::Subscriber(&Dialog::HandleDialogClose, this));
	if (GetWindow()->GetCloseWndEffect() != CEGUI::CloseWndEffect_None)
	{
		GetWindow()->subscribeEvent(CEGUI::Window::EventCloseWndEffectEnd, CEGUI::Event::Subscriber(&Dialog::HandleCloseEffectEnd, this));
	}

	if (GetWindow()->isModalAfterShow())
	{
		GetWindow()->setModalState(true);
	}

	if (pParentWindow == NULL)
	{
		gGetGameUIManager()->AddUIDialog(m_pMainFrame, this);
	}

	if (gGetGameUIManager()->isEnableDlgSound())
	{
		if (GetWindow()->isSoundEable())
		{
			CEGUI::String soundRes = GetWindow()->GetSoundResource();
			if (!soundRes.empty())
			{
				gGetGameUIManager()->PlayUISound(soundRes);
			}
		}
	}
}

bool Dialog::HandleCloseEffectEnd(const CEGUI::EventArgs &e)
{
	if (m_pMainFrame == NULL)
		return true;

	const CEGUI::WindowEventArgs& windowArgs = static_cast<const CEGUI::WindowEventArgs&>(e);
	if (windowArgs.window == GetWindow()) {
		if (m_bRelease) {
			Dialog::OnClose();
		}
	}
	return true;
}

void Dialog::RemoveWindowEvent()
{
	m_pMainFrame->cleanupAllEvent();
}

bool Dialog::HandleDialogClose(const CEGUI::EventArgs&)
{
	if (gGetGameUIManager()->isEnableDlgSound())
	{
		if (GetWindow()->isSoundEable())
		{
			CEGUI::String soundRes = GetWindow()->GetCloseSoundResource();
			if (!soundRes.empty())
			{
				gGetGameUIManager()->PlayUISound(soundRes);
			}
		}
	}
	if (GetWindow()->isModalAfterShow())
	{
		GetWindow()->setModalState(false);
	}
	return true;
}

bool Dialog::HandleDialogOpen(const CEGUI::EventArgs&)
{
	if (gGetGameUIManager()->isEnableDlgSound())
	{
		if (GetWindow()->isSoundEable())
		{
			CEGUI::String soundRes = GetWindow()->GetSoundResource();
			if (!soundRes.empty())
			{
				gGetGameUIManager()->PlayUISound(soundRes);
			}
		}
	}

	if (GetWindow()->isModalAfterShow())
	{
		GetWindow()->setModalState(true);
	}

	return true;
}

bool Dialog::HandleTimePast(const CEGUI::EventArgs&)
{
	unsigned int curTime = Nuclear::GetMilliSeconds();
	if (curTime - m_dwCreatTime > m_dwLife)
	{
		OnClose();
	}
	return true;
}

bool Dialog::HandleRightButtonDown(const CEGUI::EventArgs &e)
{
	CEGUI::Window* ModalWindow = CEGUI::System::getSingleton().getModalTarget();
	if (ModalWindow == GetWindow())
	{
		return true;
	}

	const CEGUI::MouseEventArgs& windowArgs = static_cast<const CEGUI::MouseEventArgs&>(e);
	if (windowArgs.button == CEGUI::RightButton)
	{
		OnClose();
		return true;
	}
	return false;
}

bool Dialog::HandleCloseBtnClick(const CEGUI::EventArgs &e)
{
	OnClose();
	return true;
}

CEGUI::PushButton* Dialog::GetCloseBtn()
{
	if (m_pMainFrame && m_pMainFrame->getType().find("FrameWindow") != CEGUI::String::npos)
	{
		CEGUI::FrameWindow* pFrame = (CEGUI::FrameWindow*)m_pMainFrame;
		if (NULL != pFrame)
		{
			CEGUI::PushButton* pCloseBtn = pFrame->getCloseButton();
			if (NULL != pCloseBtn)
			{
				return pCloseBtn;
			}
		}
	}

	return NULL;
}

CEGUI::Window* Dialog::GetWindow()
{
	return m_pMainFrame;
}

void Dialog::CheckDisTimerCallBack()
{
}

void Dialog::SetVisible(bool bVisible)
{
	if (m_pMainFrame == NULL)
		return;

	bool bOldIsVis = m_pMainFrame->isVisible();

	if (bVisible != bOldIsVis) {
		if (m_pMainFrame->GetCreateWndEffect() != CEGUI::CreateWndEffect_None&&!bOldIsVis) {
			if (m_eDialogType & eDialogType_InScreenCenter)
			{
				if (m_pMainFrame)
				{
					m_pMainFrame->CenterInParent();
				}
			}
			m_pMainFrame->BeginCreateEffect();
		}

		if (m_pMainFrame->GetCloseWndEffect() != CEGUI::CloseWndEffect_None&&bOldIsVis) {
			m_bRelease = false;
			m_pMainFrame->BeginCloseEffect();
		}
	}

	m_pMainFrame->setVisible(bVisible);

	if (bVisible)
	{
		if (!m_pMainFrame->isMousePassThroughEnabled())
			m_pMainFrame->activate();
		if (GetWindow()->isModalAfterShow())
		{
			GetWindow()->setModalState(true);
		}
	}
}

bool Dialog::IsVisible()
{
	return (m_pMainFrame == NULL) ? false : m_pMainFrame->isVisible();
}

void Dialog::OnClose2(bool waitAnimAndEffect, bool ClearGameUIManager)
{
	if (m_pMainFrame == NULL)
		return;

	if (waitAnimAndEffect == false)
	{
		ReleaseDialog(ClearGameUIManager);
		std::set<Dialog*>::iterator it = Dialog::m_setDlgs.find(this);
		if (it != Dialog::m_setDlgs.end())
		{
			Dialog::m_setDlgs.erase(it);
		}
	}
}

void Dialog::OnClose()
{
	if (m_pMainFrame == NULL)
		return;

	m_bCloseing = true;
}

void Dialog::ReleaseDialog(bool ClearGameUIManager)
{
	if (m_pMainFrame == NULL)
		return;

	OnDestroyAnimation(this->m_pMainFrame);
	OnDestroyEffect(this->m_pMainFrame);

	if (m_pParentWindow == NULL&&ClearGameUIManager)
		gGetGameUIManager()->RemoveUIDialog(m_pMainFrame);
	CEGUI::WindowManager::getSingleton().destroyWindow(m_pMainFrame);
	m_pMainFrame = NULL;
	DestroyDialog();
}

void Dialog::OnDestroyEffect(CEGUI::Window* wnd)
{
}

void Dialog::OnDestroyAnimation(CEGUI::Window* wnd)
{
}

void Dialog::SetCloseIsHide(bool bCloseIsHide)
{
	m_bCloseIsHide = bCloseIsHide;
}

bool Dialog::CloseIsHide()
{
	return m_bCloseIsHide;
}

void Dialog::setEscClose(bool escclose)
{
	m_pMainFrame->SetEscClose(escclose);
}

void Dialog::OnTickAllDlg(unsigned int curTime)
{
	std::set<Dialog*>::iterator it = Dialog::m_setDlgs.begin();
	while (it != Dialog::m_setDlgs.end())
	{
		Dialog* dlg = *it;
		dlg->OnTick(curTime);
		++it;
	}
	//释放无效的DIALOG.
	it = Dialog::m_setDlgs.begin();
	while (it != Dialog::m_setDlgs.end())
	{
		s_pTickingDlg = *it;
		if (s_pTickingDlg->m_bCloseing &&s_pTickingDlg->m_mapOnCloseAnimationNotify.size() == 0 && s_pTickingDlg->m_setOnCloseEffectNotify.size() == 0)
		{
			s_pTickingDlg->ReleaseDialog();
			Dialog::m_setDlgs.erase(it++);
		}
		else{
			++it;
		}
	}

	s_pTickingDlg = NULL;
}

void Dialog::OnTick(unsigned int deltaTime)
{
	m_fElapseTime += float(deltaTime)*0.001f;
}

void Dialog::OnEffectOver(Nuclear::IEffect* effect)
{
	std::set<Dialog*>::iterator it = Dialog::m_setDlgs.begin();
	while (it != Dialog::m_setDlgs.end())
	{
		Dialog* dlg = *it;
		std::set<Nuclear::IEffect*>::iterator it2 = dlg->m_setOnCloseEffectNotify.begin();
		while (it2 != dlg->m_setOnCloseEffectNotify.end())
		{
			if (*it2 == effect)
			{
				dlg->m_setOnCloseEffectNotify.erase(it2);
				return;
			}
			++it2;
		}
		++it;
	}
}
