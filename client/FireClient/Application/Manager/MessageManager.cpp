#include "stdafx.h"
#include "MessageManager.h"

#include "GameUIManager.h"
#include "ScriptingModules/LuaScriptModule/CEGUILuaFunctor.h"

MessageManager::MessageManager()
	: m_pCurrentMessageBox(NULL)
	, m_WillAutoCloseMessageBox(NULL)
{
	m_pMessageBoxList.clear();
}

MessageManager::~MessageManager()
{
	for (MessageBoxList::iterator itor = m_pMessageBoxList.begin(); itor != m_pMessageBoxList.end(); itor++)
	{
		CEGUI::EventArgs e;
		e.handled = 1;
		(*itor)->cancelEvent(e);
		delete (*itor);
		(*itor) = NULL;
	}
	m_pMessageBoxList.clear();
	m_pCurrentMessageBox = NULL;

	for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end(); itor++)
	{
		if ((*itor)->type != eConfirmSelectNetType)
		{
			CEGUI::WindowEventArgs e((*itor)->pCancelButton);
			e.handled = 1;
			(*itor)->cancelEvent(e);
		}
		if (*itor && (*itor)->pWindow) delete (*itor);
		(*itor) = NULL;
	}
	m_ConfirmBoxList.clear();
}

stConfirmBoxInfo::stConfirmBoxInfo()
	: pWindow(NULL)
	, pCancelButton(NULL)
	, userdata(NULL)
	, userID(0)
{ }

stConfirmBoxInfo::~stConfirmBoxInfo()
{
	if (pWindow) {
		CEGUI::WindowManager::getSingleton().destroyWindow(pWindow);
		pWindow = NULL;
	}
}

stMessageBoxInfo::stMessageBoxInfo()
	: pWindow(NULL)
	, userdata(NULL)
	, userID(0)
	, life(30000)	//默认是30s
	, userID2(0)	//有时候要两个参数
{ }

stMessageBoxInfo::~stMessageBoxInfo()
{
	if (pWindow) {
		CEGUI::WindowManager::getSingleton().destroyWindow(pWindow);
		pWindow = NULL;
	}
}

bool MessageManager::SetGroupChatFlash(int64_t roleID)  //设置某个与人聊天的最小框闪,返回true则说明在列表中存在某个人的最小化聊天框
{
	bool bExist = false;

	for (MessageBoxList::iterator it = m_pMessageBoxList.begin(); it != m_pMessageBoxList.end(); it++) {
		if ((*it)->type == eMsgType_GroupChat && (*it)->userID == roleID) {
			(*it)->userID2 = 1;
			bExist = true;
			break;
		}
	}

	return bExist;
}

void MessageManager::CloseGroupChatMiniBox(int64_t roleID)
{
	for (MessageBoxList::iterator it = m_pMessageBoxList.begin(); it != m_pMessageBoxList.end(); it++) {
		if ((*it)->type == eMsgType_GroupChat && (*it)->userID == roleID) {
			if ((*it) == m_pCurrentMessageBox) {
				m_pCurrentMessageBox = NULL;
			}
			delete (*it);
			(*it) = NULL;
			m_pMessageBoxList.erase(it);
			m_EventMessageListChange.Bingo();
			break;
		}
	}
}

bool MessageManager::SetFriendChatFlash(int64_t roleID)  //设置某个与人聊天的最小框闪,返回true则说明在列表中存在某个人的最小化聊天框
{
	bool bExist = false;

	for (MessageBoxList::iterator it = m_pMessageBoxList.begin(); it != m_pMessageBoxList.end(); it++) {
		if ((*it)->type == eMsgType_FriendChat && (*it)->userID == roleID) {
			(*it)->userID2 = 1;
			bExist = true;
			break;
		}
	}

	return bExist;
}

void MessageManager::CloseFriendChatMiniBox(int64_t roleID)
{
	for (MessageBoxList::iterator it = m_pMessageBoxList.begin(); it != m_pMessageBoxList.end(); it++)
	{
		if ((*it)->type == eMsgType_FriendChat && (*it)->userID == roleID) {
			if ((*it) == m_pCurrentMessageBox) {
				m_pCurrentMessageBox = NULL;
			}
			delete (*it);
			(*it) = NULL;
			m_pMessageBoxList.erase(it);
			m_EventMessageListChange.Bingo();
			break;
		}
	}
}

//判断是否可以新增confirm对话框
bool MessageManager::CanOpenNewConfirmBox(eConfirmType type, int64_t id, void* pData)
{
	//丢弃物品可以弹开很多个 //驱逐成员可以弹出多个
	if (type == eConfirmDropItem || type == eConfirmFamilyExpel || type == eConfirmBuyBaoChan) {
		for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end(); itor++) {
			if ((*itor)->type == type && (*itor)->userID == id) {
				return false;
			}
		}
		return true;
	}
	//有些是用新的confirmbox替代旧的confirmbox
	else if (type == eConfirmFightChief || type == eConfirmFBAppoint || type == eConfirmKeJuSignUp
		|| type == eConfirmInitPK || type == eConfirmSetFlyFlag || type == eConfirmPetSkillBook
		|| type == eConfirmLevel3Drug || type == eConfirmFreePet || type == eConfirmExchangeRepu
		|| type == eConfirmBanTalk) {
		CloseConfirmBox(type);	//则先关掉原来comfirmbox
		return true;
	}
	else {
		for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end(); itor++) {
			if ((*itor)->type == type) {
				return false;
			}
		}
		return true;
	}
}

//默认的cancel事件
bool MessageManager::HandleDefaultCancelEvent(const CEGUI::EventArgs &e)
{
	//说明是点击了关闭按钮，不是自然关闭的
	if (e.handled != 1) {
		const CEGUI::WindowEventArgs& windowargs = static_cast<const  CEGUI::WindowEventArgs&>(e);
		stConfirmBoxInfo* pConfirmBoxInfo = static_cast<stConfirmBoxInfo*>(windowargs.window->getUserData());
		if (pConfirmBoxInfo)
			gGetMessageManager()->RemoveConfirmBox(pConfirmBoxInfo);
	}
	return true;
}

bool MessageManager::HandleExitGameConfirmed(const CEGUI::EventArgs& e)
{
	gGetGameApplication()->ExitGame(eExitType_ToWindows);
	return true;
}

void MessageManager::Run(int now, int delta)
{
	MessageBoxRun(now, delta);
}

//删除某个确认框，主动点ok或cancel触发
void MessageManager::RemoveConfirmBox(stConfirmBoxInfo* pConfirmBoxInfo)
{
	for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end(); itor++) {
		if ((*itor) == pConfirmBoxInfo) {
			delete (*itor);
			(*itor) = NULL;
			itor = m_ConfirmBoxList.erase(itor);
			break;
		}
	}
}

//删除某个确认框，被动触发
void MessageManager::DeleteConfirmBox(stConfirmBoxInfo* pConfirmBoxInfo)
{
	for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end(); itor++)
	{
		if ((*itor) == pConfirmBoxInfo)
		{
			CEGUI::WindowEventArgs e((*itor)->pCancelButton);
			e.handled = 1;
			(*itor)->cancelEvent(e);
			delete (*itor);
			(*itor) = NULL;
			itor = m_ConfirmBoxList.erase(itor);
			break;
		}
	}
}

//关闭某一类型的confirmbox
//发默认消息
void MessageManager::CloseConfirmBox(eConfirmType type, bool senddefault)
{
	// 先调用一遍cancelEvent
	ConfirmBoxList tmpList = m_ConfirmBoxList;
	for (ConfirmBoxList::iterator itor = tmpList.begin(); itor != tmpList.end(); itor++) {
		if ((*itor)->type == type) {
			if (senddefault) {
				CEGUI::WindowEventArgs e((*itor)->pCancelButton);
				e.handled = 1;
				(*itor)->cancelEvent(e);
			}
		}
	}
	tmpList.clear();

	// 再释放
	for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end();) {
		if ((*itor)->type == type) {
			delete (*itor);
			(*itor) = NULL;
			itor = m_ConfirmBoxList.erase(itor);
		}
		else {
			itor++;
		}
	}
}

//关闭所有confirmbox
void MessageManager::CloseAllConfirmBox()
{
	// 先调用一遍cancelEvent
	ConfirmBoxList tmpList = m_ConfirmBoxList;
	for (ConfirmBoxList::iterator itor = tmpList.begin(); itor != tmpList.end(); itor++) {
		CEGUI::WindowEventArgs e((*itor)->pCancelButton);
		e.handled = 1;
		(*itor)->cancelEvent(e);
	}
	tmpList.clear();

	// 再释放
	for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end(); itor++) {
		delete (*itor);
		(*itor) = NULL;
	}
	m_ConfirmBoxList.clear();
}

stConfirmBoxInfo* MessageManager::AddConfirmBox(eConfirmType type, const std::wstring& message, lua_State* state, int okfunc, int okselfIndex, int cancelfunc, int cacelselfIndex, int64_t id, int64_t id2, void* pData, std::wstring oktext, std::wstring canceltext)
{
	CEGUI::LuaFunctor oklf(state, okfunc, okselfIndex);
	CEGUI::LuaFunctor cancellf(state, cancelfunc, cacelselfIndex);
	stConfirmBoxInfo* ret = AddConfirmBox(type, message, CEGUI::SubscriberSlot(oklf),
		CEGUI::SubscriberSlot(cancellf), id, id2, pData, oktext, canceltext);
	oklf.invalidateLuaRefs();
	cancellf.invalidateLuaRefs();
	return ret;
}

stMessageBoxInfo* MessageManager::AddMessageBox(const std::wstring& title, const std::wstring& message, lua_State* state, int okfunc, int okselfIndex, int cancelfunc, int cacelselfIndex, eMsgType type, int life, int64_t id, int64_t id2, void* pData, std::wstring oktext, std::wstring canceltext, int nRight, int pType)
{
	CEGUI::LuaFunctor oklf(state, okfunc, okselfIndex);
	CEGUI::LuaFunctor cancellf(state, cancelfunc, cacelselfIndex);
	stMessageBoxInfo* ret = AddMessageBox(title, message, CEGUI::SubscriberSlot(oklf), CEGUI::SubscriberSlot(cancellf)
		, type, life, id, id2, pData, oktext, canceltext, nRight, pType);
	oklf.invalidateLuaRefs();
	cancellf.invalidateLuaRefs();
	return ret;
}

//队员离队或者成为队长时的处理，发送关于组队PK的默认消息给服务器
void MessageManager::OnAbsentTeamAndBeTeamLeader()
{
	//关闭组队PK相关的二次确认框，并发送默认消息
	CloseMessageBoxByType(eMsgType_PK, true);
	CloseMessageBoxByType(eMsgType_Team);
}

void MessageManager::OnBattleEnd()
{
}

//战斗开始时的处理
void MessageManager::OnBattleBegin()
{
	//先隐藏当前对话框
	//HideCurrentMessageBox();
	//然后删除一些对话框 交易对话框要关闭，并且不发消息
	CloseMessageBoxByType(eMsgType_Trade);

	//关闭一些二次确认框
	CloseConfirmBox(eConfirmExitDream);
	CloseConfirmBox(eConfirmReturnDream);
	CloseConfirmBox(eConfirmResetPoint);
	CloseConfirmBox(eConfirmSaveAntique);
}

//切换地图时的处理
void MessageManager::OnMapChange()
{
	//先隐藏当前对话框
	HideCurrentMessageBox();
	//然后删除一些对话框 交易对话框要关闭，并且不发消息
	CloseMessageBoxByType(eMsgType_Trade);
}

//删除某种类型的2次确认框，bsenddefault代表删除的时候给服务器发消息与否，默认为false
void MessageManager::CloseMessageBoxByType(eMsgType type, bool bsenddefault)
{
	//然后删除一些对话框 交易对话框要关闭，并且不发消息
	for (MessageBoxList::iterator itor = m_pMessageBoxList.begin(); itor != m_pMessageBoxList.end();)
	{
		if ((*itor)->type == type)
		{
			//如果需要发默认消息
			if (bsenddefault)
			{
				CEGUI::EventArgs e;
				e.handled = 1;
				m_WillAutoCloseMessageBox = (*itor);	//即将delete掉的message，cancelEvent里要用的
				(*itor)->cancelEvent(e);
				m_WillAutoCloseMessageBox = NULL;
			}
			//如果是当前显示的二次确认框，置为NULL
			if ((*itor) == m_pCurrentMessageBox)
			{
				m_pCurrentMessageBox = NULL;
			}
			delete (*itor);
			(*itor) = NULL;
			itor = m_pMessageBoxList.erase(itor);
			m_EventMessageListChange.Bingo();
		}
		else
			itor++;
	}
}

bool MessageManager::isHaveMessageBox()
{
	for (MessageBoxList::iterator itor = m_pMessageBoxList.begin(); itor != m_pMessageBoxList.end(); itor++)
	{
		return true;
	}
	return false;
}

bool MessageManager::isHaveConfirmBox()
{
	for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end(); ++itor)
	{
		return true;
	}
	return false;
}

bool MessageManager::HaveConfirmBox(eConfirmType type)
{
	for (ConfirmBoxList::iterator itor = m_ConfirmBoxList.begin(); itor != m_ConfirmBoxList.end(); ++itor)
	{
		if ((*itor)->type == type)
		{
			return true;
		}
	}
	return false;

}

//是否存在某种类型的二次确认框
bool MessageManager::HaveMessageBox(eMsgType type)
{
	for (MessageBoxList::iterator itor = m_pMessageBoxList.begin(); itor != m_pMessageBoxList.end(); itor++)
	{
		if ((*itor)->type == type)
		{
			return true;
		}
	}
	return false;
}

//是否存在相同的messagebox
bool MessageManager::IsHaveSameMessageBox(eMsgType type, int64_t userid, int64_t userid2)
{
	for (MessageBoxList::iterator itor = m_pMessageBoxList.begin(); itor != m_pMessageBoxList.end(); itor++)
	{
		if ((*itor)->type == type && (*itor)->userID == userid && (*itor)->userID2 == userid2)
		{
			return true;
		}
	}
	return false;
}

//是否已经有相同title的messagebox了
stMessageBoxInfo* MessageManager::AlreadyHaveSameTitleMessageBox(std::wstring title)
{
	for (MessageBoxList::iterator itor = m_pMessageBoxList.begin(); itor != m_pMessageBoxList.end(); itor++)
	{
		if ((*itor)->title == title)
		{
			return (*itor);
		}
	}
	return NULL;
}

////关闭当前显示的messagebox////
void MessageManager::CloseCurrentShowMessageBox()
{
	if (m_pCurrentMessageBox)
	{
		MessageBoxList::iterator next = m_pMessageBoxList.end();
		for (MessageBoxList::iterator itor = m_pMessageBoxList.begin(); itor != m_pMessageBoxList.end(); itor++)
		{
			if ((*itor) == m_pCurrentMessageBox)
			{
				itor = m_pMessageBoxList.erase(itor);
				next = itor;
				break;
			}
		}
		delete m_pCurrentMessageBox;
		m_pCurrentMessageBox = NULL;

		if (next != m_pMessageBoxList.end()) //continue show next message. by liugeng
		{
			ChangeCurrentShowMessageBox(*next);
		}

		m_EventMessageListChange.Bingo();
	}
}

//隐藏当前显示的messagebox
void MessageManager::HideCurrentMessageBox()
{
	if (m_pCurrentMessageBox)
	{
		if (m_pCurrentMessageBox->pWindow)
		{
			m_pCurrentMessageBox->pWindow->setVisible(false);
		}
		m_pCurrentMessageBox = NULL;
		m_EventMessageListChange.Bingo();
	}
}

////隐藏当前显示的messagebox////
bool MessageManager::HideCurrentMessage(const CEGUI::EventArgs& e)
{
	HideCurrentMessageBox();
	return true;
}

////更换当前显示的messagebox////
void MessageManager::ChangeCurrentShowMessageBox(stMessageBoxInfo* pBox)
{
	//首先隐藏当前的messagebox
	if (m_pCurrentMessageBox&&m_pCurrentMessageBox->pWindow)
	{
		m_pCurrentMessageBox->pWindow->setVisible(false);
	}

	//然后用需要显示的messagebox替换
	if (pBox)
	{
		if (pBox->pWindow)
		{
			pBox->pWindow->setVisible(true);
		}

		m_pCurrentMessageBox = pBox;
	}
	m_EventMessageListChange.Bingo();
}

int64_t MessageManager::GetAutoCloseMessageUserID2()
{
	if (m_WillAutoCloseMessageBox)
		return m_WillAutoCloseMessageBox->userID2;
	else
		return -1;
}

int64_t MessageManager::GetAutoCloseMessageUserID()
{
	if (m_WillAutoCloseMessageBox)
		return m_WillAutoCloseMessageBox->userID;
	else
		return -1;
}

//这两个函数非常重要，用于即将自动关闭message发送默认cancel消息
void* MessageManager::GetAutoCloseMessageUserData()
{
	if (m_WillAutoCloseMessageBox)
		return m_WillAutoCloseMessageBox->userdata;
	else
		return NULL;
}

//得到第二个userID
int64_t MessageManager::GetUserID2()
{
	if (m_pCurrentMessageBox)
	{
		return m_pCurrentMessageBox->userID2;
	}
	else
		return -1;
}

//得到当前显示对话框的用户ID
int64_t MessageManager::GetUserID()
{
	if (m_pCurrentMessageBox)
		return m_pCurrentMessageBox->userID;
	else
		return -1;
}

//得到当前显示对话框用户数据
void* MessageManager::GetUserData()
{
	if (m_pCurrentMessageBox)
		return m_pCurrentMessageBox->userdata;
	else
		return NULL;
}

MessageBoxList& MessageManager::GetMessageBoxList()
{
	return m_pMessageBoxList;
}

//Run函数
void MessageManager::MessageBoxRun(int now, int delta)
{
	for (MessageBoxList::iterator itor = m_pMessageBoxList.begin(); itor != m_pMessageBoxList.end();)
	{
		(*itor)->life -= delta;
		if ((*itor)->life <= 0)
		{
			CEGUI::EventArgs e;
			e.handled = 1;
			m_WillAutoCloseMessageBox = (*itor);	//即将delete掉的message，cancelEvent里要用的
			(*itor)->cancelEvent(e);
			m_WillAutoCloseMessageBox = NULL;
			if ((*itor) == m_pCurrentMessageBox)
			{
				m_pCurrentMessageBox = NULL;
			}
			delete (*itor);
			(*itor) = NULL;
			itor = m_pMessageBoxList.erase(itor);
			m_EventMessageListChange.Bingo();
		}
		else
			itor++;
	}
	if (m_pCurrentMessageBox&&m_pCurrentMessageBox->pWindow)
	{
		if (m_pCurrentMessageBox->nRightTick == 1)
		{
			CEGUI::Window* pBtnRight = m_pCurrentMessageBox->pWindow->getChildAtIdx(0)->getChildAtIdx(0);
			if (pBtnRight)
			{
				pBtnRight->setText(m_pCurrentMessageBox->strRightBtnTitle + L"(" + StringCover::intTowstring(m_pCurrentMessageBox->life / 1000) + L")");
			}
		}
		else
		{
			CEGUI::Window* pCancelBtn = m_pCurrentMessageBox->pWindow->getChildAtIdx(0)->getChildAtIdx(1);
			pCancelBtn->setText(m_pCurrentMessageBox->okstring + L"(" + StringCover::intTowstring(m_pCurrentMessageBox->life / 1000) + L")");
		}


	}
}

///////添加一个MessageBox框///////
stMessageBoxInfo* MessageManager::AddMessageBox(const std::wstring& title, const std::wstring& message, const CEGUI::SubscriberSlot& okEvent, const CEGUI::SubscriberSlot& cancleEvent, eMsgType type, int life, int64_t id, int64_t id2, void* pData, std::wstring oktext, std::wstring canceltext, int nRight, int pType)
{

	CEGUI::String wndName = CEGUI::WindowManager::getSingleton().generateUniqueWindowName();

	stMessageBoxInfo* pMessageBoxInfo = new stMessageBoxInfo;
	CEGUI::Window* pMessageWindow = NULL;
	//好友聊天和群聊天就不用显示信息框，直接显示左边的最小化框
	if (type != eMsgType_FriendChat&&type != eMsgType_GroupChat)
	{
		pMessageWindow = CEGUI::WindowManager::getSingleton().loadWindowLayout("SpecialMessageBox.layout", wndName);
		if (pMessageWindow)
		{
			CEGUI::Window* pRootWindow = gGetGameUIManager()->GetMainRootWnd();
			if (pRootWindow != NULL)
			{
				pRootWindow->addChildWindow(pMessageWindow);

				CEGUI::Window* pBgWindow = pMessageWindow->getChildAtIdx(0);
				CEGUI::Window* pTitle = pBgWindow->getChildAtIdx(4);
				pTitle->getChildAtIdx(0)->setText(title);
				pMessageWindow->setUserData(pData);
				CEGUI::Window* m_pText = pBgWindow->getChildAtIdx(2);
				CEGUI::PushButton* pOKBtn = static_cast<CEGUI::PushButton*>(pBgWindow->getChildAtIdx(0));
				pOKBtn->subscribeEvent(CEGUI::PushButton::EventMouseClick, okEvent);
				pOKBtn->setText(oktext);			//自定义的ok按钮文字，默认为“确定”
				CEGUI::PushButton* pCancelBtn = static_cast<CEGUI::PushButton*>(pBgWindow->getChildAtIdx(1));
				pCancelBtn->subscribeEvent(CEGUI::PushButton::EventMouseClick, cancleEvent);
				pCancelBtn->setText(canceltext);	//自定义的cancel按钮文字，默认为“取消”
				CEGUI::PushButton* pMiniBtn = static_cast<CEGUI::PushButton*>(pBgWindow->getChildAtIdx(3));
				
				CEGUI::Font* fnt = m_pText->getFont();
				std::vector<std::wstring>	delimiters;
				delimiters.push_back(L"\\n");
				std::vector<std::wstring>	splits;
				StringCover::split_string(message,delimiters,splits);
				int lineCount = splits.size();
				float wd = m_pText->getPixelSize().d_width;
				for (size_t i = 0; i < splits.size(); i++)
				{
					float fLen = fnt->getTextExtent(splits[i]);
					int ct = int(fLen / wd);
					lineCount += ct;
				}

				float fnHeight = fnt->getFontHeight();
				CEGUI::UDim standardH = cegui_absdim(fnHeight);
				CEGUI::UDim h = cegui_absdim(lineCount*fnHeight);
				if (h.d_offset < standardH.d_offset)
				{
					h.d_offset = standardH.d_offset;
				}
				m_pText->setHeight(h);
				CEGUI::UDim ypos = m_pText->getYPosition();
				if (title == L"")
					ypos.d_offset = 40 - (h.d_offset - 144) / 2;
				else
					ypos.d_offset = ypos.d_offset - (h.d_offset-96) / 2;
				m_pText->setYPosition(ypos);
				m_pText->setText(message, 0xff57371d);

				pMiniBtn->setVisible(false);
				pOKBtn->setXPosition(CEGUI::UDim(0, pBgWindow->getPixelSize().d_width - pOKBtn->getPixelSize().d_width - 50.0f));
				pCancelBtn->setXPosition(CEGUI::UDim(0, 50.0f));

				if (pType == eConfirmOK)	//只有确定按钮
				{
					pOKBtn->setVisible(false);
					float width = 0.0f - pOKBtn->getPixelSize().d_width;
					pCancelBtn->setXPosition(CEGUI::UDim(0.5f, width / 2.0f));
				}
			}
		}
	}

	pMessageBoxInfo->title = title;
	pMessageBoxInfo->pWindow = pMessageWindow;
	pMessageBoxInfo->okstring = canceltext;
	pMessageBoxInfo->strRightBtnTitle = oktext;
	pMessageBoxInfo->userID = id;
	pMessageBoxInfo->userID2 = id2;
	pMessageBoxInfo->life = life;
	pMessageBoxInfo->type = type;
	pMessageBoxInfo->userdata = pData;
	pMessageBoxInfo->cancelEvent = cancleEvent;
	pMessageBoxInfo->nRightTick = nRight;

	if (m_pCurrentMessageBox == NULL) {
		m_pCurrentMessageBox = pMessageBoxInfo;
		if (m_pCurrentMessageBox&&m_pCurrentMessageBox->pWindow) {
			m_pCurrentMessageBox->pWindow->setVisible(true);
		}
	} else {
		if (pMessageWindow) {
			pMessageWindow->setVisible(false);
			if (!gGetGameUIManager()->IsGameUIVisible()) {
				pMessageWindow->OnShiedUI();
			}
		}

	}
	m_pMessageBoxList.push_back(pMessageBoxInfo);
	m_EventMessageListChange.Bingo();
	return pMessageBoxInfo;
};

//弹出普通的确认框
stConfirmBoxInfo* MessageManager::AddConfirmBox(eConfirmType type, const std::wstring& message, const CEGUI::SubscriberSlot& okEvent, const CEGUI::SubscriberSlot& cancleEvent, int64_t id, int64_t id2, void* pData, std::wstring oktext, std::wstring canceltext)
{
	//丢弃物品的对话框是可以同类型重复的
	if (!CanOpenNewConfirmBox(type, id, pData)) {
		return NULL;
	}

	if (oktext == L"") {
		oktext = MHSD_UTILS::GETSTRING(2037);
	}

	if (canceltext == L"") {
		canceltext = MHSD_UTILS::GETSTRING(2038);
	}

	std::wstring title = GetConfirmTitleByType(type);
	stConfirmBoxInfo* pConfirmBoxInfo = new stConfirmBoxInfo;
	CEGUI::String winname = CEGUI::WindowManager::getSingleton().generateUniqueWindowName();
	CEGUI::WindowManager& windMgr = CEGUI::WindowManager::getSingleton();
	CEGUI::Window* pMessageWindow = windMgr.loadWindowLayout("MessageBox.layout", winname);
	CEGUI::Window* pBgWindow = pMessageWindow->getChildAtIdx(0);
	CEGUI::Window* pCancelWindow = NULL;
	if (pMessageWindow)
	{
		CEGUI::Window* pRootWindow = gGetGameUIManager()->GetMainRootWnd(); //CEGUI::System::getSingleton().getGUISheet();

		if (type == eConfirmDropItem || type == eConfirmOpenShop || type == eConfirmAcupointUp
			|| type == eConfirmFamilyApply || type == eConfirmFightChief || type == eConfirmBuyItem
			|| type == eConfirmGiven || type == eConfirmBuyStuff || type == eConfirmDemonSlayerTask
			|| type == eConfirmRevokeInfo || type == eConfirmSaveAntique || type == eConfirmDelAntique || type == eConfirmDZXY
			|| type == eConfirmPetSkillBook || type == eConfirmAcution || type == eDrawPetStar
			|| type == eConfirmFreePet || type == eConfirmLeaveFaction || type == eConfirmExitGame
			|| type == eConfirmDropSkill || type == eConfirmPetEvolve || type == eConfirmDeleteColorScheme
			|| type == eConfirmCloseRemindUseItem || type == eConfirmCloseRemindUseItemTip
			|| type == eConfirmCloseReplaceEquip || type == eConfirmCloseReplaceEquipTip || type == eConfirmMixBindItem
			|| type == eConfirmReportRole || type == eConfirmEnterPetIsland || type == eConfirmLevel3Drug
			|| type == eConfirmXunBaoSell || type == eConfirmSelectNetType || type == eConfirmBuyStallBoard
			|| type == eConfirmGotoHighLevelMap || type == eConfirmAbandonTask || type == eConfirmBattleEscape)
		{
			CEGUI::System::getSingleton().setModalTarget(pMessageWindow);
			pMessageWindow->SeModalStateDrawEffect(false);
		}

		if (pRootWindow != NULL) {
			pRootWindow->addChildWindow(pMessageWindow);

			pMessageWindow->setAlwaysOnTop(true);
			pMessageWindow->setUserData(pData);
			CEGUI::Window* m_pText = pBgWindow->getChildAtIdx(2);
			m_pText->setText(message);

			CEGUI::PushButton* pOKBtn = static_cast<CEGUI::PushButton*>(pBgWindow->getChildAtIdx(0));
			pOKBtn->setUserData(pConfirmBoxInfo);
			pOKBtn->subscribeEvent(CEGUI::PushButton::EventClicked, okEvent);
			pOKBtn->setText(oktext);			//自定义的ok按钮文字，默认为“确定”

			CEGUI::PushButton* pCancelBtn = static_cast<CEGUI::PushButton*>(pBgWindow->getChildAtIdx(1));
			if (type == eConfirmOK) {
				pCancelBtn->setVisible(false);
				float width = 0.0f - pOKBtn->getPixelSize().d_width;
				pOKBtn->setXPosition(CEGUI::UDim(0.5f, width / 2.0f));
			} else {
				pCancelBtn->setUserData(pConfirmBoxInfo);
				pCancelBtn->setText(canceltext);	//自定义的cancel按钮文字，默认为“取消”
			}
			pCancelBtn->subscribeEvent(CEGUI::PushButton::EventClicked, cancleEvent);
			pCancelWindow = static_cast<CEGUI::Window*>(pCancelBtn);
		}
		pConfirmBoxInfo->pWindow = pMessageWindow;
		pConfirmBoxInfo->pCancelButton = pCancelWindow;
		pConfirmBoxInfo->type = type;
		pConfirmBoxInfo->userID = id;
		pConfirmBoxInfo->userID2 = id2;
		pConfirmBoxInfo->userdata = pData;
		pConfirmBoxInfo->cancelEvent = cancleEvent;
		m_ConfirmBoxList.push_back(pConfirmBoxInfo);
		return pConfirmBoxInfo;
	}
	return NULL;
}
