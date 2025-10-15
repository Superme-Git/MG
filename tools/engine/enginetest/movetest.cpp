
#include <windows.h>
#include <vector>
#include "engine/iengine.h"
#include "astar/apathio.hpp"
#include "astar/alocation.hpp"
#include "pfs/pfs.h"

inline Nuclear::CPOINT make_point(LPARAM lParam)
{
	return Nuclear::CPOINT(LOWORD(lParam), HIWORD(lParam));
}

class MoveTestApp : public Nuclear::IApp , public Nuclear::ITimer
{
public:
	MoveTestApp()          { }
	virtual ~MoveTestApp() { }

	enum TestMode { eTestTrack, eTestActive, eTestPassive };
	void parseCmdLine(LPSTR lpCmdLine)
	{
		//MessageBoxA(NULL, lpCmdLine, lpCmdLine, MB_OK);
		std::vector<std::string> args;
		LPSTR head = lpCmdLine;
		for (; *lpCmdLine; ++lpCmdLine)
		{
			if (*lpCmdLine == ' ' && lpCmdLine > head)
			{
				args.push_back(std::string(head, lpCmdLine - head));
				head = lpCmdLine + 1;
			}
		}
		if (lpCmdLine > head)
			args.push_back(std::string(head, lpCmdLine - head));
		//for (size_t i = 0; i < args.size(); ++i) MessageBoxA(NULL, args[i].c_str(), args[i].c_str(), MB_OK);

		this->mode = eTestTrack;
		if (args.size() > 0)
		{
			if (0 == ::strcmp(args[0].c_str(), "active"))
				this->mode = eTestActive;
			else if (0 == ::strcmp(args[0].c_str(), "passive"))
				this->mode = eTestPassive;
		}
	}

	wchar_t * getWindowClassName() const { return L"movetestclassname"; }
	wchar_t * getWindowTitle() const     { return this->getWindowTitle(this->mode); }

	TestMode getMode() const { return this->mode; }
private:
	std::vector<Nuclear::ISprite*> follows; // front is attached
	TestMode mode;

	wchar_t * getWindowTitle(TestMode mode) const
	{
		switch (mode)
		{
		case eTestActive:  return L"Move Test Active";
		case eTestPassive: return L"Move Test Passive";
		default: return L"Move Test Track";
		}
	}
	/*
	int _action;
	wchar_t * action(int inc = 0)
	{
		static wchar_t * _actions[] = {L"attack1", L"attack2", L"attack3", L"attack4", L"attack5", L"attack6",
			L"attackedAction", L"combatRun", L"deadAction", L"gather", L"kiss", L"laugh", L"leapAction", L"leisureStand",
			L"normalRun", L"normalStand", L"taunt"};
		_action += inc;
		return _actions[(_action)%(sizeof(_actions)/sizeof(_actions[0]))];
	}
	*/

	void OnTimer()
	{
		switch (this->mode)
		{
		case eTestTrack:
			if (follows.size() > 1)
			{
				std::vector<Nuclear::ISprite*>::iterator first = follows.begin(), second = ++follows.begin();
				for ( ; second != follows.end(); ++second)
				{
					astar::Path trail;
					(*first)->TakeMoveTrail(trail);
					(*second)->MoveTo(trail);
					first = second;
				}
			}
			break;

		case eTestActive:
			{
				HWND passive = ::FindWindow(this->getWindowClassName(), this->getWindowTitle(eTestPassive));
				if (NULL == passive)
					break;

				astar::Path trail;
				follows.front()->TakeMoveTrail(trail);
				if (trail.empty())
					break;

				GNET::Octets buffer;
				astar::marshal(trail, buffer);
				// TODO random delay

				COPYDATASTRUCT cd;
				cd.dwData = 0;
				cd.cbData = (DWORD)buffer.size();
				cd.lpData = buffer.begin();
				::SendMessage(passive, WM_COPYDATA, 0, (LPARAM)&cd);
			}
			break;

		case eTestPassive:
			// see OnWindowsMessage::WM_COPYDATA
			break;
		}
	}

	std::string hex(const void * b, size_t size)
	{
		std::string str;
		for (const char * p = (const char*)b, *e=p+size; p != e; ++p)
		{
			char buffer[16];
			_snprintf_s(buffer, sizeof(buffer), "%02x ", *p);
			str.append(buffer);
		}
		return str;
	}

protected:
	enum
	{
		XPEC_BODY = 0,
		XPEC_ARMOR,
		XPEC_SHOULDER,
		XPEC_HAIR,
		XPEC_WEAPON,
		XPEC_HORSE,
		XPEC_MAXCOUNT = 6,
	};
	virtual bool OnInit()
	{
		int count = this->mode == eTestTrack ? 100 : 1;
		attacking = false;
		Nuclear::IWorld * iworld = Nuclear::GetEngine()->GetWorld();
		for (int x = 0; x < count; ++x)
		{
			Nuclear::ISprite * isprite = iworld->NewSprite(L"female");
			follows.push_back(isprite);

			isprite->SetComponent(0, L"1jnvbuyi");
			isprite->SetComponent(1, L"bangpaidizinvtou1");
			isprite->SetComponent(3, L"1jdnvjian");
			isprite->SetComponent(4, L"2");
			isprite->SetComponent(5, L"1");

			Nuclear::Font font = {14, 0, 0, 0xff00ff00};
			wchar_t title[256];
			int nstr = _snwprintf_s(title, sizeof(title), L"%2d", x);
			isprite->SetTitle(0, title, font);

			isprite->SetMoveMask(0xffffffff);
			isprite->SetMoveSpeed(320.f/1000);
			isprite->SetLocation(Nuclear::Location(512, 1535));

			this->OnUpdateSpriteAction(isprite, Nuclear::XPUSAT_MOVESTATE); // init action
		}
		if (follows.empty())
			return false;

		follows.front()->KeepMoveTrail(true); // passive 也保持尾迹，用来调试，而且保持所有的尾迹。永不删除。
		// eTestTrack == this->mode // don't keep if last
		for (size_t i = 1; i < follows.size() - 1; ++i) follows[i]->KeepMoveTrail(true);
		iworld->AttachCameraTo(follows.front());
		Nuclear::XPLoadmapParam lp;
		lp.objLoc.x = 512;
		lp.objLoc.y = 1535;
		iworld->LoadMap(L"q_5", L"maze/118.dat", &lp);

		Nuclear::GetEngine()->ScheduleTimer(this, 1000);
		Nuclear::GetEngine()->GetEnv()->ShowMapGrid(true);
		return true;
	}

	virtual bool OnExit()
	{
		Nuclear::IWorld * iworld = Nuclear::GetEngine()->GetWorld();
		iworld->DeleteAllSprite(false);
		follows.clear();
		Nuclear::GetEngine()->CancelTimer(this);
		moveTimer.stop();
		return true;
	}

	virtual void OnTick(int now, int delta)
	{
		/*PFSX::AsyncReadUpdate();*/
	}

	virtual bool OnBeforeRender(int now/*ms*/)
	{
		return true;
	}

	virtual void OnRenderUI(int now)
	{
		Nuclear::Renderer * renderer = Nuclear::GetEngine()->GetRenderer();
		Nuclear::FPOINT view = Nuclear::GetEngine()->GetWorld()->GetViewport().GetPos().ToFPOINT();
		Nuclear::FPOINT fp = lastAttackTarget.ToFPOINT();
		renderer->DrawLine(Nuclear::FRECT(fp.x - 100, fp.y, fp.x + 100, fp.y) - view, 0x7fff0000);
		renderer->DrawLine(Nuclear::FRECT(fp.x, fp.y - 100, fp.x, fp.y + 100) - view, 0x7fff0000);
		renderer->DrawLine(Nuclear::FRECT(fp.x - 100, fp.y - 100, fp.x + 100, fp.y + 100) - view, 0x7fff0000);
		renderer->DrawLine(Nuclear::FRECT(fp.x - 100, fp.y + 100, fp.x + 100, fp.y - 100) - view, 0x7fff0000);
		renderer->DrawLine(Nuclear::FRECT(fp, follows.front()->GetLocation().ToFPOINT()) - view, 0x7fffffff);

		renderer->DrawBox(Nuclear::FRECT(
			(follows.front()->GetLocation() - Nuclear::CPOINT(this->searchSize.x / 2, this->searchSize.y / 2)).ToFPOINT(),
			(float)this->searchSize.x, (float)this->searchSize.y)
			- view, 0xffffffff, Nuclear::XPFM_WIREFRAME);
	}

	virtual void OnPostRender(int now)
	{
	}

	struct MoveTimer : public Nuclear::ITimer
	{
		Nuclear::ISprite * hero;
		HWND hWnd;
		Nuclear::ISprite * sheep;
		int period;

		MoveTimer() { this->hero = NULL; this->sheep = NULL; }
		void start(Nuclear::ISprite * hero, HWND hWnd, int period, Nuclear::ISprite * sheep = NULL)
		{
			this->hero = hero; this->hWnd = hWnd;
			this->sheep = sheep;
			this->period = period;
			Nuclear::GetEngine()->ScheduleTimer(this, period);
		}
		void stop()
		{
			this->hero = NULL;
			this->sheep = NULL;
			Nuclear::GetEngine()->CancelTimer(this);
		}
		virtual void OnTimer()
		{
			if (!this->hero)
				return;
			if (this->sheep)
			{
				astar::Timer timer;
				this->hero->MoveTo(this->sheep->GetLocation(), 0, NULL);
				int e = timer.elapse(); 
				if (e - 100> this->period)
				{
					this->period = e; // 加大跟随间隔，如果寻路时间太长了。debug版的效率很差。
					Nuclear::GetEngine()->ScheduleTimer(this, this->period);
				}
			}
			else if (::GetKeyState(VK_LCONTROL) < 0)
			{
				Nuclear::IWorld * iworld = Nuclear::GetEngine()->GetWorld();
				const Nuclear::CPOINT view = iworld->GetViewport().GetPos();
				Nuclear::CPOINT point;
				Nuclear::GetEngine()->GetEnv()->GetCursorPosition( point);
				this->hero->MoveTo(point + view, 0, NULL);
			}
		}
	};
	MoveTimer moveTimer;
	Nuclear::CPOINT lastAttackTarget;
	bool attacking;

	Nuclear::CSIZE searchSize;
	virtual bool OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
	{
		Nuclear::IWorld * iworld = Nuclear::GetEngine()->GetWorld();
		const Nuclear::CPOINT view = iworld->GetViewport().GetPos();
		switch (msg)
		{
		case WM_COPYDATA:
			{
				moveTimer.stop();
				PCOPYDATASTRUCT pcd = (PCOPYDATASTRUCT)lParam;
				GNET::Octets buffer(pcd->lpData, pcd->cbData);
				astar::Path trail;
				astar::unmarshal(buffer, trail);
				follows.front()->MoveTo(trail);
			}
			break;

		case WM_RBUTTONDOWN:
			if (::GetKeyState(VK_LSHIFT) < 0)
			{
				follows.front()->SetLocation(make_point(lParam) + view);
			}
			else
			{
				lastAttackTarget = make_point(lParam) + view;
				searchSize.x = 512; searchSize.y = 300;
				follows.front()->MoveTo(lastAttackTarget, 50, &searchSize);
				// align to grid
				lastAttackTarget.x = lastAttackTarget.x / 16 * 16 + 8;
				lastAttackTarget.y = lastAttackTarget.y / 16 * 16 + 8;
				attacking = true;
			}
			break;

		case WM_MBUTTONDOWN:
			// LCONTROL ? clear : setup
			//this->mazeSet(make_point(lParam), ::GetKeyState(VK_LCONTROL) < 0 ? 0 : 1);
			break;

		case WM_SETCURSOR:
			break;

		case WM_MOUSEMOVE:
			if (::GetKeyState(VK_LCONTROL) >= 0)
				break;
			// fall
		case WM_LBUTTONDOWN:
			follows.front()->MoveTo(make_point(lParam) + view, 0, NULL);
			moveTimer.start(follows.front(), hWnd, 500); // start or reset
			attacking = false;
			return true;

		case WM_KEYDOWN:
			switch( wParam)
			{
			case VK_F3:
				Nuclear::GetEngine()->GetEnv()->ShowSpritePath(!Nuclear::GetEngine()->GetEnv()->IsShowSpritePath());
				Nuclear::GetEngine()->GetEnv()->ShowSpriteTrail(!Nuclear::GetEngine()->GetEnv()->IsShowSpriteTrail());
				break;

			case VK_F5:
				Nuclear::GetEngine()->GetEnv()->SetDynamicMapLoading(!Nuclear::GetEngine()->GetEnv()->IsDynamicMapLoading());
				break;
			case VK_F6:
				follows.front()->SetDirection((Nuclear::XPDIRECTION)((follows.front()->GetDirection() + 1) % Nuclear::XPDIR_COUNT));
				break;
			case VK_F7:
				follows.front()->KeepMoveTrail(!follows.front()->IsKeepMoveTrail());
				break;

			case VK_F8:
				follows.front()->MoveTo(0, 0, 0, NULL);
				break;

			case VK_F9:
				if (moveTimer.sheep)
					moveTimer.stop();
				else if (follows.front() != follows.back())
					moveTimer.start(follows.front(), hWnd, 500, follows.back());
				break;

			case VK_SPACE:
				Nuclear::GetEngine()->GetEnv()->SetSpriteMoveSmoothLimit(
					Nuclear::GetEngine()->GetEnv()->GetSpriteMoveSmoothLimit() == 1 ? 80 : 1);
				break;
			}
			break;
		}
		return false;
	}

	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type)
	{
		// 在这里可能需要根据精灵的逻辑类型，调用不同的动作配置函数。
		/*
		eStand 资源定位表。
		fight\ride  eRideNone                eRideHorse eRideFly
		eFightNone  normalStand|leisureStand
		eFight      normalStand
		eMoving 资源定位表。
		fight\ride  eRideNone eRideHorse eRideFly
		eFightNone  normalRun
		eFight      combatRun
		*/
		/*
		1 参数
		speed
		isMoving
		onHorse
		inFight
		inFly

		2 行为
		开始攻击目标
		跟踪攻击，根据目标位置修改行为

		3 逻辑服务器，地图服务器，第三方玩家状态同步。
		4 策划配置参数和数据联入。
		switch (state)
		{
		case fight0:
			SetDefaultAction(onHorse ? (IsMoving() ? "run2" : "stand2") : (IsMoving() ? "hrun" : "hstand"), false, 1.0f);
			break;

		case fight1:
			onHorse ? "hattack" : "attack1,"
			break;

		case Normal:
			IsMoving() ? (onHorse ? "hrun" : "run1") : (onHorse ? "hstand" : "stand1")
			break;
		}
		*/
		if (type == Nuclear::XPUSAT_MOVESTATE)
		{
			if (attacking)
			{
				if (sprite->IsMoving())
				{
					sprite->SetDefaultAction(L"hrun", false, 1.0f);
				}
				else
				{
					if (sprite == follows.front())
						sprite->SetDirection(this->lastAttackTarget);
					sprite->SetDefaultAction(L"hattack", false, 1.0f);
				}
			}
			else
				sprite->SetDefaultAction(sprite->IsMoving() ? L"hrun" : L"hstand", false, 1.0f);
		}
	}

public:
	static MoveTestApp & getInstance() { return *((MoveTestApp*)Nuclear::GetEngine()->GetApp()); }
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int WINAPI WinMain( HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nShowCmd )
{
	/*.
	{
		::Sleep(2000);
		wchar_t text[256];
		int nstr = _snwprintf_s(text, sizeof(text), L"ticks %d", astar::Timer::tickcount());
		MessageBox(NULL, text, text, MB_OK);
		return 0;
	}
	*/

	//PFSX::AsyncReadInitialize();
	MoveTestApp app;
	app.parseCmdLine(lpCmdLine);

	Nuclear::EngineParameter eparam;
	eparam.szClassName = app.getWindowClassName();
	eparam.szWindowTitle = app.getWindowTitle();
	eparam.bAsyncRead = true;
	if (app.getMode() == MoveTestApp::eTestTrack)
	{
		eparam.dmode.width = 1024;
		eparam.dmode.height = 600;
	}
	else
	{
		eparam.dmode.width = 640;
		eparam.dmode.height = 480;
	}
	eparam.pApp = &app;

	Nuclear::GetEngine()->Run(eparam);
	//PFSX::AsyncReadUninitialize();
	return 0;
}

#pragma comment( lib, "winmm.lib")
#pragma comment( lib, "d3dx9.lib")
#pragma comment( lib, "d3d9.lib")
#pragma comment( lib, "dxerr9.lib")
#pragma comment( lib, "dinput8.lib")
#pragma comment( lib, "dsound.lib")

#pragma comment( lib, "ws2_32.lib")
#pragma comment( lib, "perf.lib")

#pragma comment( lib, "freetype.lib")

#ifdef _DEBUG
#pragma comment( lib, "ogg_static_d.lib")
#pragma comment( lib, "vorbis_static_d.lib")
#pragma comment( lib, "vorbisfile_static_d.lib")
#pragma comment( lib, "zlibstaticd.lib")
#pragma comment( lib, "pfsd.lib")
#pragma comment( lib, "engined.lib")
#else
#pragma comment( lib, "ogg_static.lib")
#pragma comment( lib, "vorbis_static.lib")
#pragma comment( lib, "vorbisfile_static.lib")
#pragma comment( lib, "zlibstatic.lib")
#pragma comment( lib, "pfs.lib")
#pragma comment( lib, "engine.lib")
#endif
