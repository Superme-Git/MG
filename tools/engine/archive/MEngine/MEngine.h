#pragma once

#include "../engine/iengine.h"

using namespace System;

namespace MEngine {

public ref class IApp abstract
{
public:
	virtual bool OnInit() = 0;
	virtual void OnIdle() = 0;
	virtual bool OnExit() = 0;
	virtual bool OnBeforeRender() = 0;
	virtual void OnPostRender() = 0;
	virtual bool OnWindowsMessage(IntPtr hwnd, UInt32 msg, UInt32 wParam, UInt32 lParam) = 0;
};

public enum struct Direction : System::Int32
{
	Top = Nuclear::XPDIR_TOP,
	TopRight = Nuclear::XPDIR_TOPRIGHT,
	Right = Nuclear::XPDIR_RIGHT,
	BottomRight = Nuclear::XPDIR_BOTTOMRIGHT,
	Bottom = Nuclear::XPDIR_BOTTOM,
	BottomLeft = Nuclear::XPDIR_BOTTOMLEFT,
	Left = Nuclear::XPDIR_LEFT,
	TopLeft = Nuclear::XPDIR_TOPLEFT,
	Count = Nuclear::XPDIR_COUNT,
};

public value struct Location
{
public:
	property int X;
	property int Y;

	Location(int x, int y)
	{
		X = x;
		Y = y;
	}

	Location(Location^ loc)
	{
		X = loc->X;
		Y = loc->Y;
	}
};

public ref class IEffect abstract
{
public:
	virtual void SetLocation(Location location) = 0;
	virtual Location GetLocation() = 0;
	virtual void SetDirection(Direction direction) = 0;
};

public enum struct SpriteType : System::Int32
{
	Player = Nuclear::XPSPRITE_PLAYER,
	NPC = Nuclear::XPSPRITE_NPC,
};

public enum struct EquipComponent : System::Int32
{
	Body = Nuclear::XPEC_BODY,
	Armor = Nuclear::XPEC_ARMOR,
	Shoulder = Nuclear::XPEC_SHOULDER,
	Hair = Nuclear::XPEC_HAIR,
	Weapon = Nuclear::XPEC_WEAPON,
	Horse = Nuclear::XPEC_HORSE,
	Count = Nuclear::XPEC_COUNT,
};

public value struct Font
{
public:
	property int Size;
	property int Type;
	property int Style;
	property UInt32 Color;
};

public ref class ISprite abstract
{
public:
	virtual SpriteType GetType() = 0;
	virtual void SetLocation(Location location) = 0;
	virtual Location GetLocation() = 0;
	virtual void SetDirection(Direction direction) = 0;
	virtual void SetVisible(bool v) = 0;
	virtual void SetEquipment(EquipComponent ec, String^ res, Drawing::Color color) = 0;
	virtual void SetDefaultAction(String^ action_name, bool hold_last_frame, float freq) = 0;
	virtual void PlayAction(String^ action_name, float freq) = 0;
	virtual void SetDurativeEffect(String^ effect_name, float freq) = 0;
	virtual void UnsetDurativeEffect(String^ effect_name) = 0;
	virtual void PlayEffect(String^ effect_name, float freq) = 0;
	virtual void PlayBubbleTextEffect(String^ txt, Font font) = 0;
	virtual void SetTitle(int slot, String^ title, Font font) = 0;
	virtual void UnsetTitle(int slot) = 0;
};

public ref class INotify abstract
{
public:
	virtual bool OnNotify(int progress) = 0;
};

public ref class IWorld abstract
{
public:
	virtual bool LoadMap(String^ res, INotify^ notify) = 0;
	virtual bool UnloadMap() = 0;
	virtual ISprite^ NewSprite(SpriteType type) = 0;
	virtual ISprite^ CloneSprite(ISprite^ sprite) = 0;
	virtual void DeleteSprite(ISprite^ sprite) = 0;
	virtual bool SelectSprites(Location loc, [Runtime::InteropServices::Out] array<ISprite^>^ %sprites) = 0;
	virtual IEffect^ NewEffect(String^ effect_name, float freq) = 0;
	virtual void DeleteEffect(IEffect^ effect) = 0;
	virtual void PlayEffect(String^ effect_name, Location loc, Direction dir, float freq) = 0;
	virtual void AttachCameraTo(ISprite^ sprite) = 0;
	virtual Drawing::Rectangle GetViewport() = 0;
	virtual void SetViewportLT(int left, int top) = 0;
};

public enum struct DisplayFormat : System::Int32
{
	R8G8B8 = Nuclear::XPDFMT_R8G8B8,
	A8R8G8B8 = Nuclear::XPDFMT_A8R8G8B8,
	X8R8G8B8 = Nuclear::XPDFMT_X8R8G8B8,
	R5G6B5 = Nuclear::XPDFMT_R5G6B5,
	X1R5G5B5 = Nuclear::XPDFMT_X1R5G5B5,
	A1R5G5B5 = Nuclear::XPDFMT_A1R5G5B5,
	A4R4G4B4 = Nuclear::XPDFMT_A4R4G4B4,
	R3G3B2 = Nuclear::XPDFMT_R3G3B2,
	A8 = Nuclear::XPDFMT_A8,
	A8R3G3B2 = Nuclear::XPDFMT_A8R3G3B2,
	X4R4G4B4 = Nuclear::XPDFMT_X4R4G4B4,
	A2B10G10R10 = Nuclear::XPDFMT_A2B10G10R10,
	A8B8G8R8 = Nuclear::XPDFMT_A8B8G8R8,
	X8B8G8R8 = Nuclear::XPDFMT_X8B8G8R8,
	G16R16 = Nuclear::XPDFMT_G16R16,
	A2R10G10B10 = Nuclear::XPDFMT_A2R10G10B10,
	A16B16G16R16 = Nuclear::XPDFMT_A16B16G16R16,
	A8P8 = Nuclear::XPDFMT_A8P8,
	P8 = Nuclear::XPDFMT_P8,
	L8 = Nuclear::XPDFMT_L8,
	L16 = Nuclear::XPDFMT_L16,
	A8L8 = Nuclear::XPDFMT_A8L8,
	A4L4 = Nuclear::XPDFMT_A4L4,
};

public value struct DisplayMode
{
public:
	property int Width;
	property int Height;
	property DisplayFormat Format;

	DisplayMode(int w, int h, DisplayFormat format)
	{
		Width = w;
		Height = h;
		Format = format;
	}

	DisplayMode(DisplayMode^ dm)
	{
		Width = dm->Width;
		Height = dm->Height;
		Format = dm->Format;
	}
};

public enum struct ConsoleInfo : System::Int32
{
	Fps = Nuclear::XPCSINFO_FPS,
	Coordinate = Nuclear::XPCSINFO_COORDINATE,
	RenderInfo = Nuclear::XPCSINFO_RENDERINFO,
	SysInfo = Nuclear::XPCSINFO_SYSINFO,
	WorldInfo = Nuclear::XPCSINFO_WORLDINFO,
	All = Nuclear::XPCSINFO_ALL,
};

public value struct SystemInfo
{
public:
	property String^ AdapterDesc;
	property String^ OsVersion;
	property String^ VideoMem;
	property String^ SysMem;

	SystemInfo(SystemInfo^ si)
	{
		AdapterDesc = si->AdapterDesc;
		OsVersion = si->OsVersion;
		VideoMem = si->VideoMem;
		SysMem = si->SysMem;
	}
};

public value struct Cursor
{
	initonly int m_handle;
public:
	property int Handle
	{
		int get()
		{
			return m_handle;
		}
	}

	Cursor(int handle) : m_handle(handle) {}
};

public ref class IEnv abstract
{
public:
	virtual IntPtr GetWindow() = 0;
	virtual bool SetDisplayMode(DisplayMode mode) = 0;
	virtual DisplayMode GetDisplayMode() = 0;
	virtual bool ShowCursor(bool isshow) = 0;
	virtual bool SetCursorPosition(int x, int y) = 0;
	virtual Cursor RegisterCursor(String^ filename, int x, int y, int offsetx, int offsety) = 0;
	virtual bool ChangeCursor(Cursor cursor) = 0;
	virtual SystemInfo GetSystemInfo() = 0;
	virtual void SetConsoleInfo(ConsoleInfo info, bool on) = 0;
};

public value struct ActionInfo
{
public:
	property int Time;
	property int Frame;
	property int Stride;
};

public ref class IQuery abstract
{
public:
	virtual bool GetActionInfo(String^ actname, [Runtime::InteropServices::Out] ActionInfo %info) = 0;
};

public value struct PictureHandle
{
	initonly int m_handle;
public:
	property int Handle
	{
		int get()
		{
			return m_handle;
		}
	}

	PictureHandle(int handle) : m_handle(handle) {}
};

public enum struct TextureBlendMode : System::Int32
{
	Default = Nuclear::XPTEXBM_DEFAULT,
	Modulate = Nuclear::XPTEXBM_MODULATE,
	Additive = Nuclear::XPTEXBM_ADDITIVE,
};

public ref class IRenderer abstract
{
public:
	virtual int DrawLine(float x1, float y1, float x2, float y2, Drawing::Color color) = 0;
	virtual int DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Drawing::Color color) = 0;
	virtual int DrawBox(float x1, float y1, float x2, float y2, Drawing::Color color, int fillmode) = 0;
	virtual int DrawCircle(float x, float y, float r, Drawing::Color color, int fillmode) = 0;
	virtual PictureHandle LoadPicture(String^ filename) = 0;
	virtual PictureHandle LoadPictureFromPFSPath(String^ filename) = 0;
	virtual PictureHandle LoadPictureFromMem(array<Byte>^ data) = 0;
	virtual PictureHandle NewPicture32(int width, int height) = 0;
	virtual bool GetPictureData(PictureHandle handle, array<Byte>^ data, int pos) = 0;
	virtual bool GetPictureData(PictureHandle handle, array<Byte>^ data, int pos, Drawing::Rectangle rect) = 0;
	virtual bool SetPictureData(PictureHandle handle, array<Byte>^ data, int pos) = 0;
	virtual bool SetPictureData(PictureHandle handle, array<Byte>^ data, int pos, Drawing::Rectangle rect) = 0;
	virtual bool FreePicture(PictureHandle handle) = 0;
	virtual int DrawPicture(PictureHandle handle, float x1, float y1, float x2, float y2, Drawing::Color color,
		TextureBlendMode blend, float sx1, float sy1, float sx2, float sy2) = 0;
	virtual int DrawText(String^ text, Drawing::Rectangle rect, Drawing::Color color) = 0;
	virtual int ShowCursor(bool f) = 0;
	virtual int SetCursorPosition(int x, int y) = 0;
	virtual int SetCursorPicture(PictureHandle handle, int offsetx, int offsety) = 0;
	virtual void ResetFPSCounter() = 0;
	virtual float GetFPS() = 0;
	virtual float GetLatestFPS() = 0;
	virtual bool GetPictureInfo(PictureHandle handle, [Runtime::InteropServices::Out] Drawing::Size %picinfo) = 0;
	virtual String^ GetAdapterDescription() = 0;
	virtual int GetAvailableTextureMem() = 0;
};

public enum struct EngineParameterFlag : System::Int32
{
	None = 0,
	VerticalSync = Nuclear::XPCRF_VERTICALSYNC,
	Mode16 = Nuclear::XPCRF_MODE16,
};

public value struct EngineParameter
{
public:
	property String^				WindowClass;
	property String^				WindowTitle;
	property IntPtr					Hwnd;
	property int					Width;
	property int					Height;
	property bool					Mode16;
	property bool					Async;
	property EngineParameterFlag	Flag;
	property IApp^					App;

	EngineParameter(String^ wc, String^ wt, int w, int h, IApp^ app)
	{
		if(app == nullptr)
			throw gcnew Exception(L"new EngineParameter with null pointer app");

		WindowClass = (wc != nullptr ? wc : L"MEngineWindowClass");
		WindowTitle = (wt != nullptr ? wt : L"MEngineWindowTitle");
		Width = (w > 0 ? w : 800);
		Height = (w > 0 ? w : 600);
		Mode16 = false;
		Async =	false;	// 暂时锁定用同步模式
		Flag = EngineParameterFlag::None;
		App = app;
	}

	EngineParameter(System::Windows::Forms::Control^ control, IApp^ app)
	{
		if(control == nullptr)
			throw gcnew Exception(L"new EngineParameter with null pointer control");
		if(app == nullptr)
			throw gcnew Exception(L"new EngineParameter with null pointer app");

		Hwnd = control->Handle;
		Width = control->Width;
		Height = control->Height;
		Mode16 = false;
		Async =	false;	// 暂时锁定用同步模式
		Flag = EngineParameterFlag::None;
		App = app;
	}
};

public ref class Engine
{
	static Engine^		ms_singleton;
	Nuclear::IEngine*	m_xengine;
	IntPtr				m_hwnd;

private:
	Engine()
	{
		m_xengine = Nuclear::GetEngine();
		m_hwnd = IntPtr::Zero;
	}

public:
	static Engine^ GetEngine()
	{
		if(ms_singleton == nullptr)
			ms_singleton = gcnew Engine();
		return ms_singleton;
	}

	bool Run(EngineParameter ep);

	bool DoWindowsMessage(UInt32 msg, UInt32 wp, UInt32 lp)
	{
		return m_xengine->OnWindowsMessage((HWND)m_hwnd.ToPointer(), (UINT)msg, (WPARAM)wp, (LPARAM)lp);
	}

	void DoIdle()
	{
		m_xengine->OnIdle();
	}

	bool Exit()
	{
		m_xengine->Exit();
		return true;
	}

	void Terminate()
	{
		m_xengine->Terminate();
	}

/*
	void Shutdown()
	{
		System::GC::Collect();
		::TerminateProcess(::GetCurrentProcess(), 0);	// 解决"0x5dd560a0"指令内存访问问题(现在改用另一种方法了)
	}
*/

	IWorld^ GetWorld();
	IEnv^ GetEnv();
	IQuery^ GetQuery();
	IRenderer^ GetRenderer();
};

}
