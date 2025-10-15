#ifndef __MESSAGEMANAGER_H
#define __MESSAGEMANAGER_H

#include "MessageCommon.h"
#include "Singleton.hpp"
#include "../Event.h"

//人与系统之间的确认框
struct stConfirmBoxInfo
{
	stConfirmBoxInfo();
	~stConfirmBoxInfo();

	CEGUI::SubscriberSlot	cancelEvent;
	int64_t			userID;
	int64_t			userID2;
	void*			userdata;
	eConfirmType	type;
	CEGUI::Window*	pCancelButton;
	CEGUI::Window*	pOkButton;
	CEGUI::Window*	pWindow;
};

//人与人之间的交互确认框，可以最小化至屏幕右侧列表
struct stMessageBoxInfo
{
	stMessageBoxInfo();
	~stMessageBoxInfo();

	CEGUI::Window*	pWindow;
	CEGUI::SubscriberSlot	cancelEvent;
	int64_t			userID;
	int64_t         userID2;  //有时候需要两个ID参数
	void*			userdata;
	eMsgType	type;
	int				life;
	int				nRightTick;
	std::wstring	strRightBtnTitle;
	std::wstring	okstring;//用来存储取消的文字
	std::wstring	title;
};

typedef std::list<stConfirmBoxInfo*>	ConfirmBoxList;
typedef std::list<stMessageBoxInfo*>	MessageBoxList;

class MessageManager : public CSingleton<MessageManager>
{
public:
	MessageManager();
	~MessageManager();

public:
	static bool HandleDefaultCancelEvent(const CEGUI::EventArgs &e);
	void Run(int now, int delta);
	bool HandleExitGameConfirmed(const CEGUI::EventArgs& e);
	int64_t GetAutoCloseMessageUserID2();
	int64_t GetAutoCloseMessageUserID();
	void* GetAutoCloseMessageUserData();
	int64_t GetUserID2();															//得到第二个userID
	bool IsHaveSameMessageBox(eMsgType type, int64_t userid, int64_t userid2);	//是否存在相同的messagebox
	bool CanOpenNewConfirmBox(eConfirmType type, int64_t id, void* pData);			//判断是否可以新增confirm对话框
	void OnAbsentTeamAndBeTeamLeader();												//队伍数据变化时处理
	void OnMapChange();																//切换地图时的处理
	void OnBattleEnd();																//战斗结束时的处理
	void OnBattleBegin();															//战斗开始时的处理
	void DeleteConfirmBox(stConfirmBoxInfo* pInfo);									//删除某个确认框，被动触发
	void CloseConfirmBox(eConfirmType type, bool senddefault = true);				//关闭某一类型的确认框
	void RemoveConfirmBox(stConfirmBoxInfo* pInfo);									//删除某个确认框，主动点ok或cancel触发
	bool HideCurrentMessage(const CEGUI::EventArgs& e);								//隐藏当前显示对话框
	void HideCurrentMessageBox();													//隐藏当前显示框
	void CloseCurrentShowMessageBox();												//关闭当前显示的二次确认框
	void CloseMessageBoxByType(eMsgType type, bool bsenddefault = false);		//关闭某种类型的二次确认框
	int64_t GetUserID();
	void* GetUserData();
	bool SetGroupChatFlash(int64_t roleID);
	void CloseGroupChatMiniBox(int64_t roleID);
	bool SetFriendChatFlash(int64_t roleID);
	void CloseFriendChatMiniBox(int64_t roleID);
	bool isHaveMessageBox();
	bool isHaveConfirmBox();
	bool HaveConfirmBox(eConfirmType type);
	bool HaveMessageBox(eMsgType type);
	stMessageBoxInfo* AlreadyHaveSameTitleMessageBox(std::wstring title);
	void ChangeCurrentShowMessageBox(stMessageBoxInfo* pBox);
	void CloseAllConfirmBox();
	void MessageBoxRun(int now, int delta);
	MessageBoxList& GetMessageBoxList();
	stConfirmBoxInfo* AddConfirmBox(eConfirmType type, const std::wstring& message, lua_State* state, int okfunc, int okselfIndex, int cancelfunc, int cacelselfIndex, int64_t id = 0, int64_t id2 = 0, void* pData = NULL, std::wstring oktext = L"", std::wstring canceltext = L"");
	stConfirmBoxInfo* AddConfirmBox(eConfirmType type, const std::wstring& message, const CEGUI::SubscriberSlot& okEvent, const CEGUI::SubscriberSlot& cancleEvent, int64_t id = 0, int64_t id2 = 0, void* pData = NULL, std::wstring oktext = MHSD_UTILS::GETSTRING(2037).c_str(), std::wstring canceltext = MHSD_UTILS::GETSTRING(2038).c_str());
	stMessageBoxInfo* AddMessageBox(const std::wstring& title, const std::wstring& message, lua_State* state, int okfunc, int okselfIndex, int cancelfunc, int cacelselfIndex, eMsgType type, int life, int64_t id = 0, int64_t id2 = 0, void* pData = NULL, std::wstring oktext = L"", std::wstring canceltext = L"", int nRight = 0, int pType = 0);
	stMessageBoxInfo* AddMessageBox(const std::wstring& title, const std::wstring& message, const CEGUI::SubscriberSlot& okEvent, const CEGUI::SubscriberSlot& cancleEvent, eMsgType type, int life, int64_t id = 0, int64_t id2 = 0, void* pData = NULL, std::wstring oktext = MHSD_UTILS::GETSTRING(2035).c_str(), std::wstring canceltext = MHSD_UTILS::GETSTRING(2036).c_str(), int nRight = 0, int pType = 0);

public:
	CBroadcastEvent<NoParam>	m_EventMessageListChange;

private:
	int m_nRight;
	ConfirmBoxList		m_ConfirmBoxList;
	MessageBoxList		m_pMessageBoxList;
	stMessageBoxInfo*	m_pCurrentMessageBox;
	stMessageBoxInfo*	m_WillAutoCloseMessageBox;
};

inline MessageManager* gGetMessageManager()
{
	return MessageManager::GetInstance();
}
#endif
