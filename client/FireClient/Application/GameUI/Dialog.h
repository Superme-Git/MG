/***********************************************************************
filename: 	Dialog.h
purpose:	Defines the interface for the dialog
*************************************************************************/

#ifndef __DIALOG_HPP
#define __DIALOG_HPP

#include "CEGUIWindow.h"
#include "CEGUIWindowManager.h"
#include "CEGUISystem.h"
#include "SingletonDialog.h"
#include "elements/CEGUIPushButton.h"


#include "../Event.h"

namespace Nuclear
{
	class IEffect;
}

enum eDialogType
{
	eDialogType_Null = 0x00,
	eDialogType_TimeLimit = 0x01 << 0,		//对话框有生命期
	eDialogType_BattleClose = 0x01 << 1,	//进战斗关闭
	eDialogType_MoveClose = 0x01 << 2,		//主角移动关闭
	eDialogType_RBtnClose = 0x01 << 3,		//右键空白处关闭
	eDialogType_MapChangeClose = 0x01 << 4,	//跳地图关闭
	eDialogType_ChatOutWnd = 0x01 << 5,		//外置聊天窗口中的对话框
	eDialogType_SceneMovieWnd = 0x01 << 6,  //场景动画中的对话框
	eDialogType_InScreenCenter = 0x01 << 7, //初始化时或切换分辨率后居中的窗口
	eDialogType_ImmeDel = 0x01 << 8,		//立即删除模式
	eDialogType_Max,
};

class Dialog
{
public:
    friend class GameUImanager;

public:
	Dialog();
	virtual ~Dialog();

	void OnCreate(CEGUI::Window* pParentWindow = NULL, const CEGUI::String& name_prefix = ""); //创建对话框

	virtual void RemoveWindowEvent();//只在推出游戏至登陆界面或角色选择界面时调用
	virtual void DestroyDialog() = 0;
	virtual void OnClose2(bool waitAnimAndEffect, bool ClearGameUIManager); //关闭对话框
	virtual void OnClose(); //关闭对话框

	virtual void SetVisible(bool bVisible);
	bool IsVisible(); //当前对话框是否可见 true 可见，false 不可见

	virtual void CheckDisTimerCallBack();

	virtual CEGUI::String GetLayoutFileName() = 0; //得到加载对话框所需的layout文件名

	void SetCloseIsHide(bool bCloseIsHide);
	virtual void setEscClose(bool escclose);
	bool CloseIsHide();

	CEGUI::Window* GetWindow();

	CEGUI::PushButton* GetCloseBtn();

	bool HandleDialogClose(const CEGUI::EventArgs&); //界面关闭或者隐藏（播放音效，必须在窗口销毁前调用）
	bool HandleDialogOpen(const CEGUI::EventArgs&);  //界面打开

	static void OnEffectOver(Nuclear::IEffect* effect);
	static void OnTickAllDlg(unsigned int deltaTime);
	virtual bool HandleCloseEffectEnd(const CEGUI::EventArgs&);

	virtual bool HandleTimePast(const CEGUI::EventArgs&);
	virtual bool HandleRightButtonDown(const CEGUI::EventArgs&);
	virtual bool HandleCloseBtnClick(const CEGUI::EventArgs&);
    
	CEGUI::Window* m_pParentWindow;
	CEGUI::Window* m_pMainFrame;	 //对话框的主窗口
	int		m_eDialogType;
	bool    m_bEscClose;	 //是否按esc键退出
	unsigned int   m_dwLife; //生命期
private:

	void OnDestroyAnimation(CEGUI::Window* wnd);
	void ReleaseDialog(bool ClearGameUIManager = true);
	void OnDestroyEffect(CEGUI::Window* wnd);
	bool OnCloseEffect(CEGUI::Window* wnd);//如果没有特效被创建，返回失败.
	virtual void OnTick(unsigned int deltaTime);

	bool m_bRelease;
	unsigned int  m_dwCreatTime; //对话框的创建时间
	bool m_bCloseIsHide;	//是否隐藏即关闭
	static Dialog* s_pTickingDlg;
	bool m_bCloseing;
	std::map<CEGUI::Window*, float> m_mapOnCloseAnimationNotify;
	float m_fElapseTime;
	static std::set<Dialog*> m_setDlgs;
	std::set<Nuclear::IEffect*> m_setOnCloseEffectNotify;
};

#endif
