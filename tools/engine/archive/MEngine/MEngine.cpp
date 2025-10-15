#include "stdafx.h"
//#include "../iolib/winiolib.h"
//#include "../pfs/asyncread.h"
#include "MEngine.h"

namespace MEngine {

#define IMPLEMENT_EQUALS(C)													\
	virtual bool Equals(Object^ obj) override								\
	{																		\
		if(obj == nullptr) return false;									\
		C^ c = dynamic_cast<C^>(obj);										\
		return c != nullptr && GetPointer() == c->GetPointer();				\
	}

#define IMPLEMENT_EQUALS_STATIC(C)														\
	static bool Equals(Object^ a, Object^ b)											\
	{																					\
		if(a == nullptr || b == nullptr) return false;									\
		C^ ca = dynamic_cast<C^>(a);													\
		C^ cb = dynamic_cast<C^>(b);													\
		return ca != nullptr && cb != nullptr && ca->GetPointer() == cb->GetPointer();	\
	}

static gcroot<INotify^> g_notify;

static bool XNotify(int progress)
{
	return (INotify^)g_notify != nullptr ? g_notify->OnNotify(progress) : true;
}

private ref class CEffect : IEffect
{
	Nuclear::IEffect* m_xeffect;

public:
	IMPLEMENT_EQUALS(CEffect);
	IMPLEMENT_EQUALS_STATIC(CEffect);

	CEffect(Nuclear::IEffect* xeffect) : m_xeffect(xeffect)
	{
		if(!m_xeffect)
			throw gcnew Exception(L"new CEffect with null pointer");
	}

	Nuclear::IEffect* GetPointer()
	{
		return m_xeffect;
	}

	virtual void SetLocation(Location location) override
	{
		Nuclear::Location loc(location.X, location.Y);
		m_xeffect->SetLocation(loc);
	}

	virtual Location GetLocation() override
	{
		Nuclear::Location loc = m_xeffect->GetLocation();
		return Location(loc.x, loc.y);
	}

	virtual void SetDirection(Direction direction) override
	{
		m_xeffect->SetDirection((Nuclear::DIRECTION)direction);
	}
};

private ref class CSprite : ISprite
{
	Nuclear::ISprite* m_xsprite;

public:
	IMPLEMENT_EQUALS(CSprite);
	IMPLEMENT_EQUALS_STATIC(CSprite);

	CSprite(Nuclear::ISprite* xsprite) : m_xsprite(xsprite)
	{
		if(!m_xsprite)
			throw gcnew Exception(L"new CSprite with null pointer");
	}

	Nuclear::ISprite* GetPointer()
	{
		return m_xsprite;
	}

	virtual SpriteType GetType() override
	{
		return (SpriteType)m_xsprite->GetType();
	}

	virtual void SetLocation(Location location) override
	{
		Nuclear::Location loc(location.X, location.Y);
		m_xsprite->SetLocation(loc);
	}

	virtual Location GetLocation() override
	{
		Nuclear::Location loc = m_xsprite->GetLocation();
		return Location(loc.x, loc.y);
	}

	virtual void SetDirection(Direction direction) override
	{
		m_xsprite->SetDirection((Nuclear::DIRECTION)direction);
	}

	virtual void SetVisible(bool v) override
	{
		m_xsprite->SetVisible(v);
	}

	virtual void SetEquipment(EquipComponent ec, String^ res, Drawing::Color color) override
	{
		if(res == nullptr)
			return;

		pin_ptr<const wchar_t> pin_res = PtrToStringChars(res);
		m_xsprite->SetEquipment((Nuclear::EQUIP_COMPONENT)ec, reinterpret_cast<const wchar_t*>(pin_res), (DWORD)color.ToArgb());
	}

	virtual void SetDefaultAction(String^ action_name, bool hold_last_frame, float freq) override
	{
		if(action_name == nullptr)
			return;

		pin_ptr<const wchar_t> pin_action_name = PtrToStringChars(action_name);
		m_xsprite->SetDefaultAction(reinterpret_cast<const wchar_t*>(pin_action_name), hold_last_frame, freq);
	}

	virtual void PlayAction(String^ action_name, float freq) override
	{
		if(action_name == nullptr)
			return;

		pin_ptr<const wchar_t> pin_action_name = PtrToStringChars(action_name);
		m_xsprite->PlayAction(reinterpret_cast<const wchar_t*>(pin_action_name), freq);
	}

	virtual void SetDurativeEffect(String^ effect_name, float freq) override
	{
		if(effect_name == nullptr)
			return;

		pin_ptr<const wchar_t> pin_effect_name = PtrToStringChars(effect_name);
		m_xsprite->SetDurativeEffect(reinterpret_cast<const wchar_t*>(pin_effect_name), freq);
	}

	virtual void UnsetDurativeEffect(String^ effect_name) override
	{
		if(effect_name == nullptr)
			return;

		pin_ptr<const wchar_t> pin_effect_name = PtrToStringChars(effect_name);
		m_xsprite->UnsetDurativeEffect(reinterpret_cast<const wchar_t*>(pin_effect_name));
	}

	virtual void PlayEffect(String^ effect_name, float freq) override
	{
		if(effect_name == nullptr)
			return;

		pin_ptr<const wchar_t> pin_effect_name = PtrToStringChars(effect_name);
		m_xsprite->PlayEffect(reinterpret_cast<const wchar_t*>(pin_effect_name), freq);
	}

	virtual void PlayBubbleTextEffect(String^ txt, Font font) override
	{
		if(txt == nullptr)
			return;

		pin_ptr<const wchar_t> pin_txt = PtrToStringChars(txt);
		Nuclear::Font xfont;
		xfont.size = font.Size;
		xfont.type = font.Type;
		xfont.style = font.Style;
		xfont.color = font.Color;
		m_xsprite->PlayBubbleTextEffect(reinterpret_cast<const wchar_t*>(pin_txt), xfont);
	}

	virtual void SetTitle(int slot, String^ title, Font font) override
	{
		if(title == nullptr)
			return;

		pin_ptr<const wchar_t> pin_title = PtrToStringChars(title);
		Nuclear::Font xfont;
		xfont.size = font.Size;
		xfont.type = font.Type;
		xfont.style = font.Style;
		xfont.color = font.Color;
		m_xsprite->SetTitle(slot, reinterpret_cast<const wchar_t*>(pin_title), xfont);
	}

	virtual void UnsetTitle(int slot) override
	{
		m_xsprite->UnsetTitle(slot);
	}
};

private ref class CWorld : IWorld
{
	Nuclear::IWorld* m_xworld;

public:
	IMPLEMENT_EQUALS(CWorld);
	IMPLEMENT_EQUALS_STATIC(CWorld);

	CWorld(Nuclear::IWorld* xworld) : m_xworld(xworld)
	{
		if(!m_xworld)
			throw gcnew Exception(L"new CWorld with null pointer");
	}

	Nuclear::IWorld* GetPointer()
	{
		return m_xworld;
	}

	virtual bool LoadMap(String^ res, INotify^ notify) override
	{
		if(res == nullptr)
			return false;

		g_notify = notify;
		pin_ptr<const wchar_t> pin_res = PtrToStringChars(res);
		return m_xworld->LoadMap(reinterpret_cast<const wchar_t*>(pin_res), notify != nullptr ? XNotify : 0);
	}

	virtual bool UnloadMap() override
	{
		return m_xworld->UnloadMap();
	}

	virtual ISprite^ NewSprite(SpriteType type) override
	{
		Nuclear::ISprite* is = m_xworld->NewSprite((Nuclear::SPRITE_TYPE)type);
		return is ? gcnew CSprite(is) : nullptr;
	}

	virtual ISprite^ CloneSprite(ISprite^ sprite) override
	{
		if(sprite == nullptr)
			return nullptr;

		Nuclear::ISprite* is = m_xworld->CloneSprite(((CSprite^)sprite)->GetPointer());
		return is ? gcnew CSprite(is) : nullptr;
	}

	virtual void DeleteSprite(ISprite^ sprite) override
	{
		if(sprite == nullptr)
			return;

		m_xworld->DeleteSprite(((CSprite^)sprite)->GetPointer());
	}

	virtual bool SelectSprites(Location loc, [Runtime::InteropServices::Out] array<ISprite^>^ %sprites) override
	{
		if(&sprites == nullptr)
			return false;

		Nuclear::Location xloc(loc.X, loc.Y);
		std::vector<Nuclear::ISprite*> vec;
		if(!m_xworld->SelectSprites(xloc, vec))
		{
			sprites = gcnew array<ISprite^>(0);
			return false;
		}

		int n = vec.size();
		sprites = gcnew array<ISprite^>(n);
		for(int i = 0; i < n; ++i)
			sprites[i] = gcnew CSprite(vec[i]);

		return true;
	}

	virtual IEffect^ NewEffect(String^ effect_name, float freq) override
	{
		if(effect_name == nullptr)
			return nullptr;

		pin_ptr<const wchar_t> pin_effect_name = PtrToStringChars(effect_name);
		Nuclear::IEffect* ie = m_xworld->NewEffect(reinterpret_cast<const wchar_t*>(pin_effect_name), freq);
		return ie ? gcnew CEffect(ie) : nullptr;
	}

	virtual void DeleteEffect(IEffect^ effect) override
	{
		if(effect == nullptr)
			return;

		m_xworld->DeleteEffect(((CEffect^)effect)->GetPointer());
	}

	virtual void PlayEffect(String^ effect_name, Location loc, Direction dir, float freq) override
	{
		if(effect_name == nullptr)
			return;

		pin_ptr<const wchar_t> pin_effect_name = PtrToStringChars(effect_name);
		Nuclear::Location xloc(loc.X, loc.Y);
		m_xworld->PlayEffect(reinterpret_cast<const wchar_t*>(pin_effect_name), xloc, (Nuclear::DIRECTION)dir, freq);
	}

	virtual void AttachCameraTo(ISprite^ sprite) override
	{
		if(sprite == nullptr)
			return;

		m_xworld->AttachCameraTo(((CSprite^)sprite)->GetPointer());
	}

	virtual System::Drawing::Rectangle GetViewport() override
	{
		Nuclear::CRECT xrect = m_xworld->GetViewport();
		System::Drawing::Rectangle rect(xrect.left, xrect.top, xrect.right - xrect.left, xrect.bottom - xrect.top);
		return rect;
	}

	virtual void SetViewportLT(int left, int top) override
	{
		m_xworld->SetViewportLT(left, top);
	}
};

private ref class CEnv : IEnv
{
	Nuclear::IEnv* m_xenv;

public:
	IMPLEMENT_EQUALS(CEnv);
	IMPLEMENT_EQUALS_STATIC(CEnv);

	CEnv(Nuclear::IEnv* xenv) : m_xenv(xenv)
	{
		if(!m_xenv)
			throw gcnew Exception(L"new CEnv with null pointer");
	}

	Nuclear::IEnv* GetPointer()
	{
		return m_xenv;
	}

	virtual IntPtr GetWindow() override
	{
		return (IntPtr)m_xenv->GetWindow();
	}

	virtual bool SetDisplayMode(DisplayMode dm) override
	{
		Nuclear::DisplayMode xdm;
		xdm.width = dm.Width;
		xdm.height = dm.Height;
		xdm.format = (int)dm.Format;
		return m_xenv->SetDisplayMode(xdm);
	}

	virtual DisplayMode GetDisplayMode() override
	{
		Nuclear::DisplayMode xdm = m_xenv->GetDisplayMode();
		DisplayMode dm;
		dm.Width = xdm.width;
		dm.Height = xdm.height;
		dm.Format = (DisplayFormat)xdm.format;
		return dm;
	}

	virtual bool ShowCursor(bool isshow) override
	{
		return m_xenv->ShowCursor(isshow);
	}

	virtual bool SetCursorPosition(int x, int y) override
	{
		return m_xenv->SetCursorPosition(x, y);
	}

	virtual Cursor RegisterCursor(String^ filename, int x, int y, int offsetx, int offsety) override
	{
		if(filename == nullptr)
			return Cursor(-1);

		pin_ptr<const wchar_t> pin_filename = PtrToStringChars(filename);
		return Cursor((int)m_xenv->RegisterCursor(reinterpret_cast<LPCWSTR>(pin_filename), x, y, offsetx, offsety));
	}

	virtual bool ChangeCursor(Cursor cursor) override
	{
		return m_xenv->ChangeCursor((Nuclear::CursorHandle)cursor.Handle);
	}

	virtual SystemInfo GetSystemInfo() override
	{
		Nuclear::SystemInfo xsi = m_xenv->GetSystemInfo();
		SystemInfo si;
		si.AdapterDesc = gcnew String(xsi.adapterdes.c_str());
		si.OsVersion = gcnew String(xsi.osversioin.c_str());
		si.VideoMem = gcnew String(xsi.videomem.c_str());
		si.SysMem = gcnew String(xsi.sysmem.c_str());
		return si;
	}

	virtual void SetConsoleInfo(ConsoleInfo info, bool on) override
	{
		m_xenv->SetConsoleInfo((Nuclear::XPCONSOLE_INFO)info, on);
	}
};

private ref class CQuery : IQuery
{
	Nuclear::IQuery* m_xquery;

public:
	IMPLEMENT_EQUALS(CQuery);
	IMPLEMENT_EQUALS_STATIC(CQuery);

	CQuery(Nuclear::IQuery* xquery) : m_xquery(xquery)
	{
		if(!m_xquery)
			throw gcnew Exception(L"new CQuery with null pointer");
	}

	Nuclear::IQuery* GetPointer()
	{
		return m_xquery;
	}

	virtual bool GetActionInfo(String^ actname, [Runtime::InteropServices::Out] ActionInfo %info) override
	{
		if(actname == nullptr || &info == nullptr)
			return false;

		pin_ptr<const wchar_t> pin_actname = PtrToStringChars(actname);
		Nuclear::IQuery::ActionInfo ai;
		if(m_xquery->GetActionInfo(reinterpret_cast<const wchar_t*>(pin_actname), ai))
		{
			info.Time = ai.nTime;
			info.Frame = ai.nFrame;
			info.Stride = ai.nStride;
			return true;
		}
		return false;
	}
};

private ref class CRenderer : IRenderer
{
	Nuclear::Renderer*	m_xrenderer;

public:
	IMPLEMENT_EQUALS(CRenderer);
	IMPLEMENT_EQUALS_STATIC(CRenderer);

	CRenderer(Nuclear::Renderer* xrenderer) : m_xrenderer(xrenderer)
	{
		if(!m_xrenderer)
			throw gcnew Exception(L"new CRenderer with null pointer");
	}

	Nuclear::Renderer* GetPointer()
	{
		return m_xrenderer;
	}

	virtual int DrawLine(float x1, float y1, float x2, float y2, Drawing::Color color) override
	{
		return m_xrenderer->DrawLine(x1, y1, x2, y2, (DWORD)color.ToArgb());
	}

	virtual int DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Drawing::Color color) override
	{
		return m_xrenderer->DrawTriangle(x1, y1, x2, y2, x3, y3, (DWORD)color.ToArgb());
	}

	virtual int DrawBox(float x1, float y1, float x2, float y2, Drawing::Color color, int fillmode) override
	{
		return m_xrenderer->DrawBox(x1, y1, x2, y2, (DWORD)color.ToArgb(), (DWORD)fillmode);
	}

	virtual int DrawCircle(float x, float y, float r, Drawing::Color color, int fillmode) override
	{
		return m_xrenderer->DrawCircle(x, y, r, (DWORD)color.ToArgb(), (DWORD)fillmode);
	}

	virtual PictureHandle LoadPicture(String^ filename) override
	{
		if(filename == nullptr)
			return PictureHandle(Nuclear::INVALID_PICTURE_HANDLE);

		pin_ptr<const wchar_t> pin_filename = PtrToStringChars(filename);
		return PictureHandle(m_xrenderer->LoadPicture(reinterpret_cast<const wchar_t*>(pin_filename)));
	}

	virtual PictureHandle LoadPictureFromPFSPath(String^ filename) override
	{
		if(filename == nullptr)
			return PictureHandle(Nuclear::INVALID_PICTURE_HANDLE);

		pin_ptr<const wchar_t> pin_filename = PtrToStringChars(filename);
		return PictureHandle(m_xrenderer->LoadPictureFromPFSPath(reinterpret_cast<const wchar_t*>(pin_filename)));
	}

	virtual PictureHandle LoadPictureFromMem(array<Byte>^ data) override
	{
		if(data == nullptr)
			return PictureHandle(Nuclear::INVALID_PICTURE_HANDLE);

		pin_ptr<Byte> pin_data = &data[0];
		return PictureHandle(m_xrenderer->LoadPictureFromMem(reinterpret_cast<const void*>(pin_data), data->Length));
	}

	virtual PictureHandle NewPicture32(int width, int height) override
	{
		return PictureHandle(m_xrenderer->NewPicture(width, height));
	}

	virtual bool GetPictureData(PictureHandle handle, array<Byte>^ data, int pos) override
	{
		if(data == nullptr)
			return false;

		if(data->Length < pos) return false;
		pin_ptr<Byte> pin_data = &data[0];
		return m_xrenderer->GetPictureData((Nuclear::PictureHandle)handle.Handle, reinterpret_cast<void*>(pin_data), data->Length - pos, 0);
	}

	virtual bool GetPictureData(PictureHandle handle, array<Byte>^ data, int pos, Drawing::Rectangle rect) override
	{
		if(data == nullptr)
			return false;

		if(data->Length < pos) return false;
		pin_ptr<Byte> pin_data = &data[0];
		RECT xrect;
		xrect.left = rect.Left;
		xrect.top = rect.Top;
		xrect.right = rect.Right;
		xrect.bottom = rect.Bottom;
		return m_xrenderer->GetPictureData((Nuclear::PictureHandle)handle.Handle, reinterpret_cast<void*>(pin_data), data->Length - pos, &xrect);
	}

	virtual bool SetPictureData(PictureHandle handle, array<Byte>^ data, int pos) override
	{
		if(data == nullptr)
			return false;

		if(data->Length < pos) return false;
		pin_ptr<const Byte> pin_data = &data[0];
		return m_xrenderer->SetPictureData((Nuclear::PictureHandle)handle.Handle, reinterpret_cast<const void*>(pin_data), data->Length - pos, 0);
	}

	virtual bool SetPictureData(PictureHandle handle, array<Byte>^ data, int pos, Drawing::Rectangle rect) override
	{
		if(data == nullptr)
			return false;

		if(data->Length < pos) return false;
		pin_ptr<const Byte> pin_data = &data[0];
		RECT xrect;
		xrect.left = rect.Left;
		xrect.top = rect.Top;
		xrect.right = rect.Right;
		xrect.bottom = rect.Bottom;
		return m_xrenderer->SetPictureData((Nuclear::PictureHandle)handle.Handle, reinterpret_cast<const void*>(pin_data), data->Length - pos, &xrect);
	}

	virtual bool FreePicture(PictureHandle handle) override
	{
		return m_xrenderer->FreePicture((Nuclear::PictureHandle)handle.Handle);
	}

	virtual int DrawPicture(PictureHandle handle, float x1, float y1, float x2, float y2, Drawing::Color color,
		TextureBlendMode blend, float sx1, float sy1, float sx2, float sy2) override
	{
		return m_xrenderer->DrawPicture((Nuclear::PictureHandle)handle.Handle, x1, y1, x2, y2, (DWORD)color.ToArgb(),
			(Nuclear::XPTEXTURE_BLENDMODE)blend, sx1, sy1, sx2, sy2);
	}

	virtual int DrawText(String^ text, Drawing::Rectangle rect, Drawing::Color color) override
	{
		if(text == nullptr)
			return false;

		pin_ptr<const wchar_t> pin_text = PtrToStringChars(text);
		RECT xrect;
		xrect.left = rect.Left;
		xrect.top = rect.Top;
		xrect.right = rect.Right;
		xrect.bottom = rect.Bottom;
		return m_xrenderer->DrawText(reinterpret_cast<const wchar_t*>(pin_text), text->Length, &xrect, (DWORD)color.ToArgb());
	}

	virtual int ShowCursor(bool f) override
	{
		return m_xrenderer->ShowCursor(f);
	}

	virtual int SetCursorPosition(int x, int y) override
	{
		return m_xrenderer->SetCursorPosition(x, y);
	}

	virtual int SetCursorPicture(PictureHandle handle, int offsetx, int offsety) override
	{
		return m_xrenderer->SetCursorPicture((Nuclear::PictureHandle)handle.Handle, offsetx, offsety);
	}

	virtual void ResetFPSCounter() override
	{
		m_xrenderer->ResetFPSCounter();
	}

	virtual float GetFPS() override
	{
		return m_xrenderer->GetFPS();
	}

	virtual float GetLatestFPS() override
	{
		return m_xrenderer->GetLatestFPS();
	}

	virtual bool GetPictureInfo(PictureHandle handle, [Runtime::InteropServices::Out] Drawing::Size %picinfo) override
	{
		Nuclear::PictureInfo pi;
		if(m_xrenderer->GetPictureInfo((Nuclear::PictureHandle)handle.Handle, pi))
		{
			picinfo.Width = pi.m_nPicWidth;
			picinfo.Height = pi.m_nPicHeight;
			return true;
		}
		return false;
	}

	virtual String^ GetAdapterDescription() override
	{
		std::wstring desc = m_xrenderer->GetAdapterDescription();
		return gcnew String(desc.c_str());
	}

	virtual int GetAvailableTextureMem() override
	{
		return m_xrenderer->GetAvailableTextureMem();
	}
};

class CApp : public Nuclear::IApp	// 不能创建在栈上
{
	gcroot<MEngine::IApp^> m_iapp;

public:
	CApp(MEngine::IApp^ iapp) : m_iapp(iapp)
	{
		if(!iapp)
			throw gcnew Exception(L"new CApp with null pointer");
	}

	virtual IApp* Clone() const
	{
		return new CApp(m_iapp);
	}

	virtual void Destroy()
	{
		delete this;
	}

	virtual bool OnInit()
	{
		::SetErrorMode(0);
		return m_iapp->OnInit();
	}

	virtual void OnIdle()
	{
		m_iapp->OnIdle();
	}

	virtual bool OnExit()
	{
		bool ret = m_iapp->OnExit();
		::SetErrorMode(SEM_NOGPFAULTERRORBOX);	// 解决"0x5dd560a0"指令内存访问问题
		return ret;
	}

	virtual bool OnBeforeRender()
	{
		return m_iapp->OnBeforeRender();
	}

	virtual void OnPostRender()
	{
		m_iapp->OnPostRender();
	}

	virtual bool OnWindowsMessage(HWND hwnd, UINT msg, WPARAM wp, LPARAM lp)
	{
		return m_iapp->OnWindowsMessage((IntPtr)hwnd, (UInt32)msg, (UInt32)wp, (UInt32)lp);
	}
};

bool Engine::Run(EngineParameter ep)
{
	if(ep.App == nullptr)
		throw gcnew Exception(L"new Engine with null pointer");

	m_hwnd = ep.Hwnd;

//	PFSX::AsyncReadInitialize();

	Nuclear::EngineParameter xep;
	pin_ptr<const wchar_t> pin_window_class = PtrToStringChars(ep.WindowClass);
	pin_ptr<const wchar_t> pin_window_title = PtrToStringChars(ep.WindowTitle);
	xep.szClassName = reinterpret_cast<LPCWSTR>(pin_window_class);
	xep.szWindowTitle = reinterpret_cast<LPCWSTR>(pin_window_title);
	xep.hwnd = (HWND)ep.Hwnd.ToPointer();
	xep.width = ep.Width;
	xep.height = ep.Height;
	xep.async_pfs = ep.Async;
	xep.app = new CApp(ep.App);
	bool ret = m_xengine->Run(xep);

//	PFSX::AsyncReadUninitialize();

	return ret;
}

IWorld^ Engine::GetWorld()
{
	return gcnew CWorld(m_xengine->GetWorld());
}

IEnv^ Engine::GetEnv()
{
	return gcnew CEnv(m_xengine->GetEnv());
}

IQuery^ Engine::GetQuery()
{
	return gcnew CQuery(m_xengine->GetQuery());
}

IRenderer^ Engine::GetRenderer()
{
	return gcnew CRenderer(m_xengine->GetRenderer());
}

}

namespace GNET {
	class Octets { struct Rep { int a, b, c; static Rep null; }; };
	Octets::Rep Octets::Rep::null = { 0, 0, 1 };	// fix link error when linking without iolib
}
