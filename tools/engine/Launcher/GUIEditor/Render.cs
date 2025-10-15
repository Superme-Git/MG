using System;
using System.IO;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.Diagnostics;
using SlimDX;
using SlimDX.Direct3D9;
using SlimDXTexture = SlimDX.Direct3D9.Texture;
using SlimDXFont = SlimDX.Direct3D9.Font;

namespace GUIEditor2
{
	//:NOTE: 一个IRender及其内部获取或创建的对象只能被一个线程所用
	public static class CRenderFactory
	{
		public static IRender CreateSlimDXRender(Control control)
		{
			return new CSlimDXRender(control);
		}

		public static bool GetImageSize(string filename, out int width, out int height)
		{
			try
			{
				ImageInformation ii = ImageInformation.FromFile(filename);
				width  = ii.Width;
				height = ii.Height;
				return width > 0 && height > 0;
			}
			catch
			{
				width = height = 0;
				return false;
			}
		}
	}

	public enum EExpandMode			// 扩展模式(垂直和水平各选一种)
	{
		H_NONE		= 0x00000000,	// 水平:左对齐只画一个原宽度,越界剪裁
		H_CENTER	= 0x00000001,	// 水平:中心对齐只画一个原宽度,越界剪裁
		H_TILE		= 0x00000002,	// 水平:左对齐平铺原宽度,越界剪裁
		H_STRETCH	= 0x00000003,	// 水平:拉伸
		H_MASK		= 0x0000000f,
		V_NONE		= 0x00000000,	// 垂直:上对齐只画一个原高度,越界剪裁
		V_CENTER	= 0x00000010,	// 垂直:中心对齐只画一个原高度,越界剪裁
		V_TILE		= 0x00000020,	// 垂直:上对齐平铺原高度,越界剪裁
		V_STRETCH	= 0x00000030,	// 垂直:拉伸
		V_MASK		= 0x000000f0,
	}

	public enum ETextAlign			// 文字对齐(垂直和水平各选一种)
	{
		H_LEFT		= 0x00000000,	// 水平:左对齐
		H_CENTER	= 0x00000001,	// 水平:居中
		H_RIGHT		= 0x00000002,	// 水平:右对齐
		H_MASK		= 0x0000000f,
		V_TOP		= 0x00000000,	// 垂直:上对齐
		V_CENTER	= 0x00000010,	// 垂直:居中
		V_BOTTOM	= 0x00000020,	// 垂直:下对齐
		V_MASK		= 0x000000f0,
		SINGLELINE	= 0x00000100,	// 单行
	}

	public enum ETextEffect			// 文字特效(可多选)
	{
		NONE		= 0x00000000,	// 无特效
		UNDERLINE	= 0x00000001,	// 下划线
		SHADOW		= 0x00000002,	// 阴影
		OUTLINE		= 0x00000004,	// 轮廓
	}

	public struct SRenderState
	{
		public int	UsedVirtualMem;
		public int	UsedPhysicalMem;
		public int	UsedManagedMem;
		public int	AvailableTextureMem;
		public int	TextureObjectAllNum;
		public int	TextureObjectAliveNum;
		public int	FontObjectAllNum;
		public int	FontObjectAliveNum;
		public int	DxComObjectNum;
	}

	public interface IRender : IDisposable
	{
		IBrush		CreateBrush();
		IFont		CreateFont();
		bool		DrawBegin(Control wnd, Color bkcolor, float scale_x, float scale_y);
		bool		DrawEnd();
		bool		DrawHLine(Point pos, int len, Color color);
		bool		DrawVLine(Point pos, int len, Color color);
		bool		DrawBox(Rectangle rect, Color color);
		bool		DrawRect(Rectangle rect, Color color);
		bool		DrawRect(Rectangle rect, IBrush brush);
		bool		DrawText(Rectangle rect, string text, IFont font);
		bool		DrawTextEx(Rectangle rect, string text, IFont font, ETextEffect effect, Color effcolor);
		SRenderState GetState();
	}

	public interface IBrush
	{
		IRender		GetRender();
		IBrush		Clone();
		Color		GetColor();
		void		SetColor(Color color);
		bool		GetAlphaBlend();
		void		SetAlphaBlend(bool enable);
		string		GetImageFileName();
		bool		SetImageFile(string filename);	// 默认使用整个图像区域,扩展模式使用H_NONE|V_NONE
		Rectangle	GetImageRectAll();
		Rectangle	GetImageRect();
		void		SetImageRect(Rectangle rect, EExpandMode mode);
		EExpandMode	GetImageExpandMode();
		void		SetImageExpandMode(EExpandMode mode);
		void		GetImageBlock(out int w0, out int w1, out int h0, out int h1);
		void		SetImageBlock(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode);
	}

	public interface IFont
	{
		IRender		GetRender();
		IFont		Clone();
		Color		GetColor();
		void		SetColor(Color color);
		string		GetFontName();
		int			GetFontSize();
		bool		SetFont(string fontname, int fontsize);
		ETextAlign	GetAlign();
		void		SetAlign(ETextAlign align);
	}

	internal class CAutoTexture : IDisposable
	{
		private	SlimDXTexture m_tex;

		internal SlimDXTexture Texture
		{
			get { return m_tex; }
		}

		private CAutoTexture(SlimDXTexture tex)
		{
			m_tex = tex;
		}

		~CAutoTexture()
		{
			Dispose();
		}

		public void Dispose()
		{
			if(m_tex != null) { m_tex.Dispose(); m_tex = null; }
		}

		internal static CAutoTexture FromFile(Device device, string filename)
		{
			try
			{
				SlimDXTexture tex = SlimDXTexture.FromFile(device, filename, -1, -1, 1, Usage.None, Format.Unknown,
														   Pool.Managed, Filter.None, Filter.None, 0);
				if(tex != null)
					return new CAutoTexture(tex);
			}
			catch {}
			return null;
		}
	}

	internal class CSlimDXTexMgr
	{
		private readonly Device m_device;
		private readonly Dictionary<string, WeakReference> m_map = new Dictionary<string, WeakReference>();

		internal CSlimDXTexMgr(Device device)
		{
			Trace.Assert(device != null, "空参数");

			m_device = device;
		}

		internal CAutoTexture LoadTex(string filename)
		{
			Trace.Assert(filename != null, "空参数");

			CAutoTexture autotex = null;
			string key = Path.GetFullPath(filename).ToLower().Replace('/', '\\');

			if(m_map.ContainsKey(key))
			{
				autotex = m_map[key].Target as CAutoTexture;
				if(autotex != null)
					return autotex;
			}

			autotex = CAutoTexture.FromFile(m_device, filename);
			if(autotex != null)
				m_map[key] = new WeakReference(autotex);

			return autotex;
		}

		internal bool FreeTex(string filename)
		{
			Trace.Assert(filename != null, "空参数");

			string key = Path.GetFullPath(filename).ToLower().Replace('/', '\\');

			if(m_map.ContainsKey(key))
			{
				CAutoTexture autotex = m_map[key].Target as CAutoTexture;
				if(autotex != null)
					autotex.Dispose();
				return m_map.Remove(key);
			}
			return false;
		}

		internal void Dispose()
		{
			foreach(KeyValuePair<string, WeakReference> pair in m_map)
			{
				CAutoTexture autotex = pair.Value.Target as CAutoTexture;
				if(autotex != null)
					autotex.Dispose();
			}
			m_map.Clear();
		}

		internal void GetState(out int num_all, out int num_alive)
		{
			num_all = m_map.Count;
			num_alive = 0;
			foreach(KeyValuePair<string, WeakReference> pair in m_map)
			{
				if(pair.Value.IsAlive)
					++num_alive;
			}
		}
	}

	internal class CSlimDXBrush : IBrush
	{
		private readonly CSlimDXRender	m_render;
		private readonly CSlimDXTexMgr	m_texmgr;
		private Color					m_color = Color.FromArgb(255, 255, 255, 255);
		private string					m_texfilename;
		private CAutoTexture			m_autotex;
		private int						m_w, m_h;						// 纹理整体大小(像素)
		private Rectangle				m_rect;							// 纹理选择区域(像素)
		private Rectangle[]				m_blockrect = new Rectangle[9];	// 九宫格各块区域(像素)
		private EExpandMode				m_mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
		private bool					m_usealpha = true;

		internal CSlimDXBrush(CSlimDXRender render)
		{
			Trace.Assert(render != null, "空参数");

			m_render = render;
			m_texmgr = render.TexMgr;
		}

		public IRender GetRender()
		{
			return m_render;
		}

		public IBrush Clone()
		{
			IBrush brush = new CSlimDXBrush(m_render);
			brush.SetColor(m_color);
			brush.SetImageFile(m_texfilename);
			int w0, w1, h0, h1;
			GetImageBlock(out w0, out w1, out h0, out h1);
			brush.SetImageBlock(m_rect, w0, w1, h0, h1, m_mode);
			return brush;
		}

		public Color GetColor()
		{
			return m_color;
		}

		public bool GetAlphaBlend()
		{
			return m_usealpha;
		}

		public void SetAlphaBlend(bool enable)
		{
			m_usealpha = enable;
		}

		public void SetColor(Color color)
		{
			m_color = color;
		}

		public string GetImageFileName()
		{
			return m_texfilename;
		}

		public bool SetImageFile(string filename)
		{
			m_texfilename = null;
			m_autotex = null;
			m_rect.X = m_rect.Y = m_rect.Width = m_rect.Height = 0;
			m_w = m_h = 0;
			m_mode = EExpandMode.H_NONE | EExpandMode.V_NONE;

			if(filename == null)
				return true;

			CAutoTexture autotex = m_texmgr.LoadTex(filename);
			if(autotex == null)
				return false;

			SurfaceDescription sd = autotex.Texture.GetLevelDescription(0);
			Trace.Assert(sd != null, "获取纹理参数失败");
			m_texfilename = filename;
			m_autotex = autotex;
			m_w = sd.Width;
			m_h = sd.Height;

			SetImageRect(GetImageRectAll(), m_mode);

			return true;
		}

		public Rectangle GetImageRectAll()
		{
			return new Rectangle(0, 0, m_w, m_h);
		}

		public Rectangle GetImageRect()
		{
			return m_rect;
		}

		public void SetImageRect(Rectangle rect, EExpandMode mode)
		{
			SetImageBlock(rect, 0, 0, 0, 0, mode);
		}

		public EExpandMode GetImageExpandMode()
		{
			return m_mode;
		}

		public void SetImageExpandMode(EExpandMode mode)
		{
			m_mode = mode;
		}

		public void GetImageBlock(out int w0, out int w1, out int h0, out int h1)
		{
			w0 = m_blockrect[0].Width;
			w1 = m_blockrect[8].Width;
			h0 = m_blockrect[0].Height;
			h1 = m_blockrect[8].Height;
		}

		public void SetImageBlock(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_autotex == null) return;

			m_rect = rect;

			if(w0 + w1 > rect.Width ) w0 = w1 = 0;
			if(h0 + h1 > rect.Height) h0 = h1 = 0;

			m_blockrect[0].X = m_blockrect[3].X = m_blockrect[6].X = rect.Left;
			m_blockrect[1].X = m_blockrect[4].X = m_blockrect[7].X = rect.Left + w0;
			m_blockrect[2].X = m_blockrect[5].X = m_blockrect[8].X = rect.Right - w1;

			m_blockrect[0].Width = m_blockrect[3].Width = m_blockrect[6].Width = w0;
			m_blockrect[1].Width = m_blockrect[4].Width = m_blockrect[7].Width = rect.Width - w0 - w1;
			m_blockrect[2].Width = m_blockrect[5].Width = m_blockrect[8].Width = w1;

			m_blockrect[0].Y = m_blockrect[1].Y = m_blockrect[2].Y = rect.Top;
			m_blockrect[3].Y = m_blockrect[4].Y = m_blockrect[5].Y = rect.Top + h0;
			m_blockrect[6].Y = m_blockrect[7].Y = m_blockrect[8].Y = rect.Bottom - h1;

			m_blockrect[0].Height = m_blockrect[1].Height = m_blockrect[2].Height = h0;
			m_blockrect[3].Height = m_blockrect[4].Height = m_blockrect[5].Height = rect.Height - h0 - h1;
			m_blockrect[6].Height = m_blockrect[7].Height = m_blockrect[8].Height = h1;

			m_mode = mode;
		}

		internal CAutoTexture AutoTexture { get { return m_autotex; } }

		internal Rectangle GetBlockRect(int i)
		{
			Trace.Assert(i >= 0 && i < 9, "错误参数");

			return m_blockrect[i];
		}
	}

	internal class CAutoFont : IDisposable
	{
		private	SlimDXFont m_font;

		internal SlimDXFont Font
		{
			get { return m_font; }
		}

		private CAutoFont(SlimDXFont font)
		{
			m_font = font;
		}

		~CAutoFont()
		{
			Dispose();
		}

		public void Dispose()
		{
			if(m_font != null) { m_font.Dispose(); m_font = null; }
		}

		internal void OnLostDevice()
		{
			m_font.OnLostDevice();
		}

		internal void OnResetDevice()
		{
			m_font.OnResetDevice();
		}

		internal static CAutoFont CreateFont(Device device, string fontname, int fontsize)
		{
			try
			{
				SlimDXFont font = new SlimDXFont(device, fontsize, 0, FontWeight.Normal, 1, false,
												 CharacterSet.Default, Precision.Default,
												 FontQuality.ClearType, PitchAndFamily.Default, fontname);
				if(font != null)
					return new CAutoFont(font);
			}
			catch {}
			return null;
		}
	}

	internal class CSlimDXFontMgr
	{
		private readonly Device m_device;
		private readonly Dictionary<string, WeakReference> m_map = new Dictionary<string, WeakReference>();

		internal CSlimDXFontMgr(Device device)
		{
			Trace.Assert(device != null, "空参数");

			m_device = device;
		}

		internal CAutoFont LoadFont(string fontname, int fontsize)
		{
			Trace.Assert(fontname != null, "空参数");

			CAutoFont autofont = null;
			string key = fontname + '@' + fontsize;

			if(m_map.ContainsKey(key))
			{
				autofont = m_map[key].Target as CAutoFont;
				if(autofont != null)
					return autofont;
			}

			autofont = CAutoFont.CreateFont(m_device, fontname, fontsize);
			if(autofont != null)
				m_map[key] = new WeakReference(autofont);

			return autofont;
		}

		internal void OnLostDevice()
		{
			foreach(KeyValuePair<string, WeakReference> pair in m_map)
			{
				CAutoFont autofont = pair.Value.Target as CAutoFont;
				if(autofont != null)
					autofont.OnLostDevice();
			}
		}

		internal void OnResetDevice()
		{
			foreach(KeyValuePair<string, WeakReference> pair in m_map)
			{
				CAutoFont autofont = pair.Value.Target as CAutoFont;
				if(autofont != null)
					autofont.OnResetDevice();
			}
		}

		internal bool FreeFont(string fontname, int fontsize)
		{
			Trace.Assert(fontname != null, "空参数");

			string key = fontname + '@' + fontsize;

			if(m_map.ContainsKey(key))
			{
				CAutoFont autofont = m_map[key].Target as CAutoFont;
				if(autofont != null)
					autofont.Dispose();
				return m_map.Remove(key);
			}
			return false;
		}

		internal void Dispose()
		{
			foreach(KeyValuePair<string, WeakReference> pair in m_map)
			{
				CAutoFont autofont = pair.Value.Target as CAutoFont;
				if(autofont != null)
					autofont.Dispose();
			}
			m_map.Clear();
		}

		internal void GetState(out int num_all, out int num_alive)
		{
			num_all = m_map.Count;
			num_alive = 0;
			foreach(KeyValuePair<string, WeakReference> pair in m_map)
			{
				if(pair.Value.IsAlive)
					++num_alive;
			}
		}
	}

	internal class CSlimDXFont : IFont
	{
		private readonly CSlimDXRender	m_render;
		private readonly CSlimDXFontMgr	m_fontmgr;
		private Color					m_color = Color.FromArgb(255, 255, 255, 255);
		private string					m_fontname;
		private int						m_fontsize;
		private CAutoFont				m_autofont;
		private ETextAlign				m_align = ETextAlign.H_LEFT | ETextAlign.V_TOP;

		internal CSlimDXFont(CSlimDXRender render)
		{
			Trace.Assert(render != null, "空参数");

			m_render = render;
			m_fontmgr = render.FontMgr;
		}

		public IRender GetRender()
		{
			return m_render;
		}

		public IFont Clone()
		{
			IFont font = new CSlimDXFont(m_render);
			font.SetColor(m_color);
			font.SetFont(m_fontname, m_fontsize);
			return font;
		}

		public Color GetColor()
		{
			return m_color;
		}

		public void SetColor(Color color)
		{
			m_color = color;
		}

		public string GetFontName()
		{
			return m_fontname;
		}

		public int GetFontSize()
		{
			return m_fontsize;
		}

		public bool SetFont(string fontname, int fontsize)
		{
			Trace.Assert(fontname != null, "空参数");

			m_autofont = m_fontmgr.LoadFont(fontname, fontsize);
			if(m_autofont == null)
			{
				m_fontname = null;
				m_fontsize = 0;
				return false;
			}

			m_fontname = fontname;
			m_fontsize = fontsize;
			return true;
		}

		public ETextAlign GetAlign()
		{
			return m_align;
		}

		public void SetAlign(ETextAlign align)
		{
			m_align = align;
		}

		internal CAutoFont Font { get { return m_autofont; } }
	}

	internal class CSlimDXRender : IRender
	{
		private	readonly PresentParameters	m_param	= new PresentParameters();
		private Device						m_device;
		private Sprite						m_sprite;
		private CSlimDXTexMgr				m_texmgr;
		private CSlimDXFontMgr				m_fontmgr;
		private Surface						m_target;
		private Surface						m_backrt;
		private SlimDXTexture				m_solidtex;
		private IntPtr						m_hwnd;
		private Rectangle					m_srcrect;
		private Rectangle					m_dstrect;
		private Rectangle					m_backrect;
		private SpriteFlags					m_spriteflag = SpriteFlags.AlphaBlend;

		internal Device Device
		{
			get { return m_device; }
		}

		internal Sprite Sprite
		{
			get { return m_sprite; }
		}

		internal CSlimDXTexMgr TexMgr
		{
			get { return m_texmgr; }
		}

		internal CSlimDXFontMgr FontMgr
		{
			get { return m_fontmgr; }
		}

		internal bool Dead
		{
			get { return m_device == null; }
		}

		internal CSlimDXRender(Control control)
		{
			Trace.Assert(control != null, "空参数");

			Direct3D direct3d = null;
			int try_count = 0;

			for(;;)
			{
				try
				{
					direct3d = new Direct3D();

					Rectangle scrrect				= Screen.PrimaryScreen.Bounds;
					m_param.Windowed				= true;
					m_param.DeviceWindowHandle		= control.Handle;
					m_param.BackBufferWidth			= scrrect.Width;
					m_param.BackBufferHeight		= scrrect.Height;
					m_param.BackBufferFormat		= Format.X8R8G8B8;
					m_param.BackBufferCount			= 1;
					m_param.SwapEffect				= SwapEffect.Copy;
					m_param.Multisample				= MultisampleType.None;
					m_param.MultisampleQuality		= 0;
					m_param.EnableAutoDepthStencil	= false;
					m_param.PresentFlags			= PresentFlags.None;
					m_param.PresentationInterval	= PresentInterval.Immediate;

					CreateFlags cf = CreateFlags.SoftwareVertexProcessing;
					Capabilities caps = direct3d.GetDeviceCaps(0, DeviceType.Hardware);
					if((caps.DeviceCaps & DeviceCaps.HWTransformAndLight) != 0)
						cf = CreateFlags.HardwareVertexProcessing;

					m_device = new Device(direct3d, 0, DeviceType.Hardware, control.Handle, cf, m_param);
					Trace.Assert(m_device != null, "创建Direct3D9的Device失败");

					m_sprite = new Sprite(m_device);
					Trace.Assert(m_sprite != null, "创建Direct3D9的Sprite失败");

					m_texmgr = new CSlimDXTexMgr(m_device);
					Trace.Assert(m_texmgr != null, "创建纹理管理器失败");

					m_fontmgr = new CSlimDXFontMgr(m_device);
					Trace.Assert(m_fontmgr != null, "创建字体管理器失败");

					m_target = Surface.CreateRenderTarget(m_device, 2048, 2048, Format.X8R8G8B8, MultisampleType.None, 0, false);
					Trace.Assert(m_target != null, "创建渲染目标失败");
					Trace.Assert(m_device.SetRenderTarget(0, m_target).IsSuccess, "设置渲染目标失败");

					m_backrt = m_device.GetBackBuffer(0, 0);
					Trace.Assert(m_backrt != null, "无法获取后备缓存");

					m_solidtex = new SlimDXTexture(m_device, 1, 1, 1, Usage.None, Format.A8R8G8B8, Pool.Managed);
					Trace.Assert(m_solidtex != null, "创建纯色纹理失败");
					m_solidtex.LockRectangle(0, LockFlags.None).Data.Write<uint>(0xffffffff);
					m_solidtex.UnlockRectangle(0);

					direct3d.Dispose();
					return;
				}
				catch(Direct3D9Exception ex)
				{
					Dispose();
					if(direct3d != null) { direct3d.Dispose(); direct3d = null; }
					if(ex.ResultCode == ResultCode.OutOfVideoMemory && try_count < 1)
					{
						++try_count;
						GC.Collect();
						GC.WaitForPendingFinalizers();
						GC.Collect();
						GC.WaitForPendingFinalizers();
					}
					else
					{
						Trace.Assert(false, "SlimDX运行时异常\n请确认系统或当前目录是否存在d3dx9_41.dll", ex.Message);
						return;
					}
				}
			}
		}

		~CSlimDXRender()
		{
			Dispose();
		}

		public void Dispose()
		{
			if(m_solidtex != null)	{ m_solidtex.Dispose(); m_solidtex = null;	}
			if(m_backrt	  != null)	{ m_backrt  .Dispose(); m_backrt   = null;	}
			if(m_target	  != null)	{ m_target  .Dispose(); m_target   = null;	}
			if(m_fontmgr  != null)	{ m_fontmgr .Dispose();	m_fontmgr  = null;	}
			if(m_texmgr   != null)	{ m_texmgr  .Dispose();	m_texmgr   = null;	}
			if(m_sprite   != null)	{ m_sprite  .Dispose();	m_sprite   = null;	}
			if(m_device   != null)	{ m_device  .Dispose();	m_device   = null;	}
		}

		public IBrush CreateBrush()
		{
			if(Dead) return null;

			return new CSlimDXBrush(this);
		}

		public IFont CreateFont()
		{
			if(Dead) return null;

			return new CSlimDXFont(this);
		}

		public bool CheckDevice()
		{
			if(Dead) return false;

			Result r = m_device.TestCooperativeLevel();
			if(r == ResultCode.DeviceLost)
				return false;
			if(r == ResultCode.DeviceNotReset)
			{
				if(m_backrt != null) { m_backrt.Dispose(); m_backrt = null; }
				if(m_target != null) { m_target.Dispose(); m_target = null; }
				m_fontmgr.OnLostDevice();
				m_sprite.OnLostDevice();
				Rectangle scrrect			= Screen.PrimaryScreen.Bounds;
				m_param.BackBufferWidth		= scrrect.Width;
				m_param.BackBufferHeight	= scrrect.Height;

				if(m_device.Reset(m_param).IsFailure)
					return false;

				m_sprite.OnResetDevice();
				m_fontmgr.OnResetDevice();
				m_target = Surface.CreateRenderTarget(m_device, 2048, 2048, Format.X8R8G8B8, MultisampleType.None, 0, false);
				Trace.Assert(m_target != null, "创建渲染目标失败");
				Trace.Assert(m_device.SetRenderTarget(0, m_target).IsSuccess, "设置渲染目标失败");
				m_backrt = m_device.GetBackBuffer(0, 0);
				Trace.Assert(m_backrt != null, "无法获取后备缓存");
			}
			return true;
		}

		public bool DrawBegin(Control control, Color bkcolor, float scale_x, float scale_y)
		{
			if(Dead) return false;

			try
			{
				if(!CheckDevice()) return false;

				m_hwnd = control.Handle;
				if(scale_x <= 0) scale_x = 1;
				if(scale_y <= 0) scale_y = 1;

				// 调整渲染区
				m_dstrect = control.RectangleToScreen(control.ClientRectangle);
				if(m_dstrect.X >= 0) m_srcrect.X = 0;
				else
				{
					m_srcrect.X = (int)(-m_dstrect.X / scale_x);
					m_dstrect.Width += m_dstrect.X;
					m_dstrect.X = 0;
				}
				if(m_dstrect.Y >= 0) m_srcrect.Y = 0;
				else
				{
					m_srcrect.Y = (int)(-m_dstrect.Y / scale_y);
					m_dstrect.Height += m_dstrect.Y;
					m_dstrect.Y = 0;
				}

				if(m_dstrect.Right > m_param.BackBufferWidth)
					m_dstrect.Width = m_param.BackBufferWidth - m_dstrect.X;
				m_srcrect.Width = (int)(m_dstrect.Width / scale_x);
				m_dstrect.Width = (int)(m_srcrect.Width * scale_x);
				if(m_dstrect.Width + (int)scale_x <= m_param.BackBufferWidth)
				{
					m_dstrect.Width += (int)scale_x;
					++m_srcrect.Width;
				}
				if(m_dstrect.Bottom > m_param.BackBufferHeight)
					m_dstrect.Height = m_param.BackBufferHeight - m_dstrect.Y;
				m_srcrect.Height = (int)(m_dstrect.Height / scale_y);
				m_dstrect.Height = (int)(m_srcrect.Height * scale_y);
				if(m_dstrect.Height + (int)scale_y <= m_param.BackBufferHeight)
				{
					m_dstrect.Height += (int)scale_y;
					++m_srcrect.Height;
				}

				m_dstrect = control.RectangleToClient(m_dstrect);
				m_backrect.Width = m_dstrect.Width;
				m_backrect.Height = m_dstrect.Height;

				m_device.Viewport = new Viewport(m_srcrect.X, m_srcrect.Y, m_srcrect.Width, m_srcrect.Height);

				m_device.Clear(ClearFlags.Target, bkcolor, 1.0f, 0, new Rectangle[1]{m_srcrect});
				m_device.BeginScene();
				m_sprite.Begin(m_spriteflag);
				return true;
			}
			catch
			{
				return false;
			}
		}

		public virtual bool DrawEnd()
		{
			if(Dead) return false;

			try
			{
				m_sprite.End();
				m_device.EndScene();
				m_device.StretchRectangle(m_target, m_srcrect, m_backrt, m_backrect, TextureFilter.Point);
				m_device.Present(m_backrect, m_dstrect, m_hwnd);
				return true;
			}
			catch
			{
				return false;
			}
		}

		private void SetAlphaBlend(bool enable)
		{
			if((m_spriteflag & SpriteFlags.AlphaBlend) != 0)
			{
				if(!enable)
				{
					m_sprite.End();
					m_spriteflag &= ~SpriteFlags.AlphaBlend;
					m_sprite.Begin(m_spriteflag);
				}
			}
			else
			{
				if(enable)
				{
					m_sprite.End();
					m_spriteflag |= SpriteFlags.AlphaBlend;
					m_sprite.Begin(m_spriteflag);
				}
			}
		}

		public bool DrawHLine(Point p0, int len, Color color)
		{
			if(Dead) return false;

			SetAlphaBlend(true);

			m_sprite.Transform = Matrix.Scaling(len, 1, 1) * Matrix.Translation(p0.X, p0.Y, 0);
			return m_sprite.Draw(m_solidtex, color).IsSuccess;
		}

		public bool DrawVLine(Point p0, int len, Color color)
		{
			if(Dead) return false;

			SetAlphaBlend(true);

			m_sprite.Transform = Matrix.Scaling(1, len, 1) * Matrix.Translation(p0.X, p0.Y, 0);
			return m_sprite.Draw(m_solidtex, color).IsSuccess;
		}

		public bool DrawBox(Rectangle rect, Color color)
		{
			if(Dead) return false;

			SetAlphaBlend(true);

			if(rect.Width > 0 && rect.Height > 0)
			{
				Point pos = new Point(rect.Left, rect.Top);
				if(!DrawHLine(pos, rect.Width, color)) return false;
				if(rect.Width > 1)
				{
					pos.Y = rect.Bottom - 1;
					if(!DrawHLine(pos, rect.Width, color)) return false;
				}
				if(rect.Height > 2)
				{
					pos.X = rect.Left;
					pos.Y = rect.Top + 1;
					if(!DrawVLine(pos, rect.Height - 2, color)) return false;
					if(rect.Width > 1)
					{
						pos.X = rect.Right - 1;
						if(!DrawVLine(pos, rect.Height - 2, color)) return false;
					}
				}
			}

			return true;
		}

		public bool DrawRect(Rectangle rect, Color color)
		{
			if(Dead) return false;

			SetAlphaBlend(true);

			m_sprite.Transform = Matrix.Scaling(rect.Width, rect.Height, 1) * Matrix.Translation(rect.X, rect.Y, 0);
			return m_sprite.Draw(m_solidtex, color).IsSuccess;
		}

		public bool DrawRect(Rectangle rect, IBrush brush)
		{
			Trace.Assert(brush != null, "空参数");
			Trace.Assert(brush.GetRender() == this, "IBrush对象不与CD3DRender对象匹配");

			if(Dead) return false;

			CSlimDXBrush brushd3d = brush as CSlimDXBrush;
			CAutoTexture autotex = brushd3d.AutoTexture;
			if(autotex == null) return DrawRect(rect, brushd3d.GetColor());
			SlimDXTexture tex = autotex.Texture;
			int w0, w1, h0, h1;
			brush.GetImageBlock(out w0, out w1, out h0, out h1);
			if(w0 + w1 > rect.Width || h0 + h1 > rect.Height) return false;
			Rectangle irect = brush.GetImageRect();
			Color4 color = brushd3d.GetColor();
			Vector3 pos = new Vector3();
			EExpandMode em = brush.GetImageExpandMode();
			EExpandMode emw = em & EExpandMode.H_MASK;
			EExpandMode emh = em & EExpandMode.V_MASK;

			SetAlphaBlend(brush.GetAlphaBlend());

			switch(emw)
			{
				case EExpandMode.H_NONE:
					if(rect.Width > irect.Width)
						rect.Width = irect.Width;
					emw = EExpandMode.H_TILE;
					break;
				case EExpandMode.H_CENTER:
					if(rect.Width > irect.Width)
					{
						rect.X += (rect.Width - irect.Width) / 2;
						rect.Width = irect.Width;
					}
					else if(rect.Width < irect.Width)
					{
						irect.X += (irect.Width - rect.Width) / 2;
						irect.Width = rect.Width;
					}
					emw = EExpandMode.H_TILE;
					break;
			}

			switch(emh)
			{
				case EExpandMode.V_NONE:
					if(rect.Height > irect.Height)
						rect.Height = irect.Height;
					emh = EExpandMode.V_TILE;
					break;
				case EExpandMode.V_CENTER:
					if(rect.Height > irect.Height)
					{
						rect.Y += (rect.Height - irect.Height) / 2;
						rect.Height = irect.Height;
					}
					else if(rect.Height < irect.Height)
					{
						irect.Y += (irect.Height - rect.Height) / 2;
						irect.Height = rect.Height;
					}
					emh = EExpandMode.V_TILE;
					break;
			}

			bool tilew = (emw == EExpandMode.H_TILE);
			bool tileh = (emh == EExpandMode.V_TILE);
			int ww = rect.Width  - w0 - w1;
			int hh = rect.Height - h0 - h1;
			irect = brushd3d.GetBlockRect(4);
			int nw = (irect.Width  > 0 ? ww / irect.Width  : 0);
			int nh = (irect.Height > 0 ? hh / irect.Height : 0);
			int rw = (irect.Width  > 0 ? ww % irect.Width  : 0);
			int rh = (irect.Height > 0 ? hh % irect.Height : 0);
			float fw = (irect.Width  > 0 ? (float)ww / irect.Width  : 0);
			float fh = (irect.Height > 0 ? (float)hh / irect.Height : 0);
			int i, j;

			if(h0 > 0)
			{
				if(w0 > 0)
				{
					// 左上角
					pos.X = rect.Left;
					pos.Y = rect.Top;
					m_sprite.Transform = Matrix.Translation(pos);
					m_sprite.Draw(tex, brushd3d.GetBlockRect(0), color);
				}

				if(w1 > 0)
				{
					// 右上角
					pos.X = rect.Right - w1;
					pos.Y = rect.Top;
					m_sprite.Transform = Matrix.Translation(pos);
					m_sprite.Draw(tex, brushd3d.GetBlockRect(2), color);
				}
			}

			if(h1 > 0)
			{
				if(w0 > 0)
				{
					// 左下角
					pos.X = rect.Left;
					pos.Y = rect.Bottom - h1;
					m_sprite.Transform = Matrix.Translation(pos);
					m_sprite.Draw(tex, brushd3d.GetBlockRect(6), color);
				}

				if(w1 > 0)
				{
					// 右下角
					pos.X = rect.Right - w1;
					pos.Y = rect.Bottom - h1;
					m_sprite.Transform = Matrix.Translation(pos);
					m_sprite.Draw(tex, brushd3d.GetBlockRect(8), color);
				}
			}

			// 上边
			if(h0 > 0)
			{
				irect = brushd3d.GetBlockRect(1);
				pos.X = rect.Left + w0;
				pos.Y = rect.Top;
				if(tilew)
				{
					for(i = 0; i < nw; ++i)
					{
						m_sprite.Transform = Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
						pos.X += irect.Width;
					}
					if(rw > 0)
					{
						irect.Width = rw;
						m_sprite.Transform = Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
					}
				}
				else
				{
					m_sprite.Transform = Matrix.Scaling(fw, 1, 1) * Matrix.Translation(pos);
					m_sprite.Draw(tex, irect, color);
				}
			}

			// 下边
			if(h1 > 0)
			{
				irect = brushd3d.GetBlockRect(7);
				pos.X = rect.Left + w0;
				pos.Y = rect.Bottom - h1;
				if(tilew)
				{
					for(i = 0; i < nw; ++i)
					{
						m_sprite.Transform = Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
						pos.X += irect.Width;
					}
					if(rw > 0)
					{
						irect.Width = rw;
						m_sprite.Transform = Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
					}
				}
				else
				{
					m_sprite.Transform = Matrix.Scaling(fw, 1, 1) * Matrix.Translation(pos);
					m_sprite.Draw(tex, irect, color);
				}
			}

			// 左边
			if(w0 > 0)
			{
				irect = brushd3d.GetBlockRect(3);
				pos.X = rect.Left;
				pos.Y = rect.Top + h0;
				if(tileh)
				{
					m_sprite.Transform = Matrix.Identity;
					for(i = 0; i < nh; ++i)
					{
						m_sprite.Transform = Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
						pos.Y += irect.Height;
					}
					if(rh > 0)
					{
						irect.Height = rh;
						m_sprite.Transform = Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
					}
				}
				else
				{
					m_sprite.Transform = Matrix.Scaling(1, fh, 1) * Matrix.Translation(pos);
					m_sprite.Draw(tex, irect, color);
				}
			}

			// 右边
			if(w1 > 0)
			{
				irect = brushd3d.GetBlockRect(5);
				pos.X = rect.Right - w1;
				pos.Y = rect.Top + h0;
				if(tileh)
				{
					for(i = 0; i < nh; ++i)
					{
						m_sprite.Transform = Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
						pos.Y += irect.Height;
					}
					if(rh > 0)
					{
						irect.Height = rh;
						m_sprite.Transform = Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
					}
				}
				else
				{
					m_sprite.Transform = Matrix.Scaling(1, fh, 1) * Matrix.Translation(pos);
					m_sprite.Draw(tex, irect, color);
				}
			}

			// 中心
			irect = brushd3d.GetBlockRect(4);
			pos.X = rect.Left + w0;
			pos.Y = rect.Top + h0;
			if(tilew)
			{
				for(i = 0; i < nw; ++i)
				{
					if(tileh)
					{
						for(j = 0; j < nh; ++j)
						{
							m_sprite.Transform = Matrix.Translation(pos);
							m_sprite.Draw(tex, irect, color);
							pos.Y += irect.Height;
						}
						if(rh > 0)
						{
							irect.Height = rh;
							m_sprite.Transform = Matrix.Translation(pos);
							m_sprite.Draw(tex, irect, color);
							irect = brushd3d.GetBlockRect(4);
						}
					}
					else
					{
						m_sprite.Transform = Matrix.Scaling(1, fh, 1) * Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
					}
					pos.X += irect.Width;
					pos.Y = rect.Top + h0;
				}
				if(rw > 0)
				{
					irect.Width = rw;
					if(tileh)
					{
						for(j = 0; j < nh; ++j)
						{
							m_sprite.Transform = Matrix.Translation(pos);
							m_sprite.Draw(tex, irect, color);
							pos.Y += irect.Height;
						}
						if(rh > 0)
						{
							irect.Height = rh;
							m_sprite.Transform = Matrix.Translation(pos);
							m_sprite.Draw(tex, irect, color);
						}
					}
					else
					{
						m_sprite.Transform = Matrix.Scaling(1, fh, 1) * Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
					}
				}
			}
			else
			{
				if(tileh)
				{
					for(j = 0; j < nh; ++j)
					{
						m_sprite.Transform = Matrix.Scaling(fw, 1, 1) * Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
						pos.Y += irect.Height;
					}
					if(rh > 0)
					{
						irect.Height = rh;
						m_sprite.Transform = Matrix.Scaling(fw, 1, 1) * Matrix.Translation(pos);
						m_sprite.Draw(tex, irect, color);
					}
				}
				else
				{
					m_sprite.Transform = Matrix.Scaling(fw, fh, 1) * Matrix.Translation(pos);
					m_sprite.Draw(tex, irect, color);
				}
			}

			return true;
		}

		public bool DrawText(Rectangle rect, string text, IFont font)
		{
			Trace.Assert(font != null, "空参数");
			Trace.Assert(font.GetRender() == this, "IFont对象不与CD3DRender对象匹配");

			if(Dead) return false;

			SetAlphaBlend(true);

			DrawTextFormat format = DrawTextFormat.Left | DrawTextFormat.Top | DrawTextFormat.WordBreak;
			ETextAlign align = font.GetAlign();
			switch(align & ETextAlign.H_MASK)
			{
				case ETextAlign.H_LEFT:		format |= DrawTextFormat.Left;				break;
				case ETextAlign.H_CENTER:	format |= DrawTextFormat.Center;			break;
				case ETextAlign.H_RIGHT:	format |= DrawTextFormat.Right;				break;
			}
			switch(align & ETextAlign.V_MASK)
			{
				case ETextAlign.V_TOP:		format |= DrawTextFormat.Top;				break;
				case ETextAlign.V_CENTER:	format |= DrawTextFormat.VerticalCenter;	break;
				case ETextAlign.V_BOTTOM:	format |= DrawTextFormat.Bottom;			break;
			}
			if((align & ETextAlign.SINGLELINE) == ETextAlign.SINGLELINE)
				format |= DrawTextFormat.SingleLine;

			CSlimDXFont dxfont = font as CSlimDXFont;
			Rectangle rect1 = new Rectangle(rect.X + 1, rect.Y + 1, rect.Width, rect.Height);
			m_sprite.Transform = Matrix.Identity;
			return dxfont.Font.Font.DrawString(m_sprite, text, rect, format, font.GetColor()) != 0;
		}

		public bool DrawTextEx(Rectangle rect, string text, IFont font, ETextEffect effect, Color effcolor)
		{
			Trace.Assert(font != null, "空参数");
			Trace.Assert(font.GetRender() == this, "IFont对象不与CD3DRender对象匹配");

			if(Dead) return false;

			SetAlphaBlend(true);

			if((effect & ETextEffect.OUTLINE) == ETextEffect.OUTLINE)
			{
				Color color = font.GetColor();
				font.SetColor(effcolor);
				--rect.Y; DrawText(rect, text, font);
				++rect.X; DrawText(rect, text, font);
				++rect.Y; DrawText(rect, text, font);
				++rect.Y; DrawText(rect, text, font);
				--rect.X; DrawText(rect, text, font);
				--rect.X; DrawText(rect, text, font);
				--rect.Y; DrawText(rect, text, font);
				--rect.Y; DrawText(rect, text, font);
				++rect.X; ++rect.Y;
				font.SetColor(color);
			}
			else if((effect & ETextEffect.SHADOW) == ETextEffect.SHADOW)
			{
				Color color = font.GetColor();
				font.SetColor(effcolor);
				++rect.X; ++rect.Y; DrawText(rect, text, font);
				--rect.X; --rect.Y;
				font.SetColor(color);
			}

			if(!DrawText(rect, text, font))
				return false;

			if((effect & ETextEffect.UNDERLINE) == ETextEffect.UNDERLINE)
			{
				string[] strs = text.Split(new char[2]{'\r', '\n'});
				text = strs[0];
				DrawTextFormat format = DrawTextFormat.Left | DrawTextFormat.Top | DrawTextFormat.WordBreak | DrawTextFormat.SingleLine;
				ETextAlign align = font.GetAlign();
				switch(align & ETextAlign.H_MASK)
				{
					case ETextAlign.H_LEFT:		format |= DrawTextFormat.Left;				break;
					case ETextAlign.H_CENTER:	format |= DrawTextFormat.Center;			break;
					case ETextAlign.H_RIGHT:	format |= DrawTextFormat.Right;				break;
				}
				switch(align & ETextAlign.V_MASK)
				{
					case ETextAlign.V_TOP:		format |= DrawTextFormat.Top;				break;
					case ETextAlign.V_CENTER:	format |= DrawTextFormat.VerticalCenter;	break;
					case ETextAlign.V_BOTTOM:	format |= DrawTextFormat.Bottom;			break;
				}
				Rectangle recttext = (font as CSlimDXFont).Font.Font.MeasureString(m_sprite, text, format);
				Point p0 = new Point(rect.X + recttext.X, rect.Y + recttext.Bottom - 1);
				int hsize = recttext.Width;
				if(p0.Y >= rect.Bottom)
					return true;
				if((align & ETextAlign.H_CENTER) == ETextAlign.H_CENTER)
				{
					p0.X += rect.Width / 2;
				}
				if((align & ETextAlign.V_CENTER) == ETextAlign.V_CENTER)
				{
					p0.Y += rect.Height / 2;
				}
				if(p0.X < rect.X) p0.X = rect.X;

				if((effect & ETextEffect.OUTLINE) == ETextEffect.OUTLINE)
				{
					--p0.X; --p0.Y; DrawHLine(p0, hsize + 2, effcolor);
					++p0.Y;			DrawHLine(p0, hsize + 2, effcolor);
					++p0.Y;			DrawHLine(p0, hsize + 2, effcolor);
					++p0.X; --p0.Y;
				}
				else if((effect & ETextEffect.SHADOW) == ETextEffect.SHADOW)
				{
					++p0.X; ++p0.Y; DrawHLine(p0, hsize, effcolor);
					--p0.X; --p0.Y;
				}
				if(!DrawHLine(p0, hsize, font.GetColor()))
					return false;
			}
			return true;
		}

		public SRenderState GetState()
		{
			SRenderState state = new SRenderState();
			if(!Dead)
			{
				Process p = Process.GetCurrentProcess();
				state.UsedVirtualMem = (int)p.VirtualMemorySize64;
				state.UsedPhysicalMem = (int)p.WorkingSet64;
				state.UsedManagedMem = (int)GC.GetTotalMemory(false);
				state.AvailableTextureMem = m_device.AvailableTextureMemory;
				int num_all, num_alive;
				m_texmgr.GetState(out num_all, out num_alive);
				state.TextureObjectAllNum = num_all;
				state.TextureObjectAliveNum = num_alive;
				m_fontmgr.GetState(out num_all, out num_alive);
				state.FontObjectAllNum = num_all;
				state.FontObjectAliveNum = num_alive;
				state.DxComObjectNum = ObjectTable.Objects.Count;
			}
			return state;
		}
	}
}
