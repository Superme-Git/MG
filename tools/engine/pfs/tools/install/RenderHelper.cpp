///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : RenderHelper.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Implementations of Render Helpers 
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-9 Created.
///=============================================================================
#include "StdAfx.h"
#include "RenderHelper.h"
#include "TargaDecoder.h"
#include <math.h>

///=============================================================================
/// 下面的代码摘自AGG 2.5. 略有修改.
///=============================================================================
namespace HELPER
{
	//-------------------------------------------------------- Default basic types
	typedef signed char		 int8;         //----int8
	typedef unsigned char	 int8u;        //----int8u
	typedef short			 int16;        //----int16
	typedef unsigned short	 int16u;       //----int16u
	typedef int				 int32;        //----int32
	typedef unsigned		 int32u;       //----int32u
	typedef signed __int64   int64;        //----int64
	typedef unsigned __int64 int64u;       //----int64u

#if defined(_MSC_VER)
#define _INLINE_ __forceinline
#else
#define _INLINE_ inline
#endif

	_INLINE_ int iround(double v)
	{
		return int((v < 0.0) ? v - 0.5 : v + 0.5);
	}
	_INLINE_ int uround(double v)
	{
		return unsigned(v + 0.5);
	}
	_INLINE_ unsigned ufloor(double v)
	{
		return unsigned(v);
	}
	_INLINE_ unsigned uceil(double v)
	{
		return unsigned(ceil(v));
	}

	// Supported byte orders for RGB and RGBA pixel formats
	//=======================================================================
	struct order_rgb  { enum rgb_e  { R=0, G=1, B=2, rgb_tag }; };       //----order_rgb
	struct order_bgr  { enum bgr_e  { B=0, G=1, R=2, rgb_tag }; };       //----order_bgr
	struct order_rgba { enum rgba_e { R=0, G=1, B=2, A=3, rgba_tag }; }; //----order_rgba
	struct order_argb { enum argb_e { A=0, R=1, G=2, B=3, rgba_tag }; }; //----order_argb
	struct order_abgr { enum abgr_e { A=0, B=1, G=2, R=3, rgba_tag }; }; //----order_abgr
	struct order_bgra { enum bgra_e { B=0, G=1, R=2, A=3, rgba_tag }; }; //----order_bgra

	//===================================================================rgba8
	struct rgba8
	{
		typedef int8u  value_type;
		typedef int32u calc_type;
		typedef int32  long_type;
		enum base_scale_e
		{
			base_shift = 8,
			base_scale = 1 << base_shift,
			base_mask  = base_scale - 1
		};
		typedef rgba8 self_type;


		value_type r;
		value_type g;
		value_type b;
		value_type a;

		//--------------------------------------------------------------------
		rgba8() {}

		//--------------------------------------------------------------------
		rgba8(unsigned r_, unsigned g_, unsigned b_, unsigned a_=base_mask) :
		r(value_type(r_)), 
			g(value_type(g_)), 
			b(value_type(b_)), 
			a(value_type(a_)) {}

		//--------------------------------------------------------------------
		rgba8(const self_type& c, unsigned a_) :
		r(c.r), g(c.g), b(c.b), a(value_type(a_)) {}


		//--------------------------------------------------------------------
		void clear()
		{
			r = g = b = a = 0;
		}

		//--------------------------------------------------------------------
		const self_type& transparent()
		{
			a = 0;
			return *this;
		}

		//--------------------------------------------------------------------
		const self_type& opaque()
		{
			a = base_mask;
			return *this;
		}

		//--------------------------------------------------------------------
		const self_type& opacity(double a_)
		{
			if(a_ < 0.0) a_ = 0.0;
			if(a_ > 1.0) a_ = 1.0;
			a = (value_type)uround(a_ * double(base_mask));
			return *this;
		}

		//--------------------------------------------------------------------
		double opacity() const
		{
			return double(a) / double(base_mask);
		}

		//--------------------------------------------------------------------
		_INLINE_ const self_type& premultiply()
		{
			if(a == base_mask) return *this;
			if(a == 0)
			{
				r = g = b = 0;
				return *this;
			}
			r = value_type((calc_type(r) * a) >> base_shift);
			g = value_type((calc_type(g) * a) >> base_shift);
			b = value_type((calc_type(b) * a) >> base_shift);
			return *this;
		}

		//--------------------------------------------------------------------
		_INLINE_ const self_type& premultiply(unsigned a_)
		{
			if(a == base_mask && a_ >= base_mask) return *this;
			if(a == 0 || a_ == 0)
			{
				r = g = b = a = 0;
				return *this;
			}
			calc_type r_ = (calc_type(r) * a_) / a;
			calc_type g_ = (calc_type(g) * a_) / a;
			calc_type b_ = (calc_type(b) * a_) / a;
			r = value_type((r_ > a_) ? a_ : r_);
			g = value_type((g_ > a_) ? a_ : g_);
			b = value_type((b_ > a_) ? a_ : b_);
			a = value_type(a_);
			return *this;
		}

		//--------------------------------------------------------------------
		_INLINE_ const self_type& demultiply()
		{
			if(a == base_mask) return *this;
			if(a == 0)
			{
				r = g = b = 0;
				return *this;
			}
			calc_type r_ = (calc_type(r) * base_mask) / a;
			calc_type g_ = (calc_type(g) * base_mask) / a;
			calc_type b_ = (calc_type(b) * base_mask) / a;
			r = value_type((r_ > calc_type(base_mask)) ? calc_type(base_mask) : r_);
			g = value_type((g_ > calc_type(base_mask)) ? calc_type(base_mask) : g_);
			b = value_type((b_ > calc_type(base_mask)) ? calc_type(base_mask) : b_);
			return *this;
		}

		//--------------------------------------------------------------------
		_INLINE_ self_type gradient(const self_type& c, double k) const
		{
			self_type ret;
			calc_type ik = uround(k * base_scale);
			ret.r = value_type(calc_type(r) + (((calc_type(c.r) - r) * ik) >> base_shift));
			ret.g = value_type(calc_type(g) + (((calc_type(c.g) - g) * ik) >> base_shift));
			ret.b = value_type(calc_type(b) + (((calc_type(c.b) - b) * ik) >> base_shift));
			ret.a = value_type(calc_type(a) + (((calc_type(c.a) - a) * ik) >> base_shift));
			return ret;
		}

		//--------------------------------------------------------------------
		template<class GammaLUT>
		_INLINE_ void apply_gamma_dir(const GammaLUT& gamma)
		{
			r = gamma.dir(r);
			g = gamma.dir(g);
			b = gamma.dir(b);
		}

		//--------------------------------------------------------------------
		template<class GammaLUT>
		_INLINE_ void apply_gamma_inv(const GammaLUT& gamma)
		{
			r = gamma.inv(r);
			g = gamma.inv(g);
			b = gamma.inv(b);
		}

		//--------------------------------------------------------------------
		static self_type no_color() { return self_type(0,0,0,0); }

	};


	//=========================================================multiplier_rgba
	template<class ColorT, class Order> struct multiplier_rgba
	{
		typedef typename ColorT::value_type value_type;
		typedef typename ColorT::calc_type calc_type;

		//--------------------------------------------------------------------
		static _INLINE_ void premultiply(value_type* p)
		{
			calc_type a = p[Order::A];
			if(a < ColorT::base_mask)
			{
				if(a == 0)
				{
					p[Order::R] = p[Order::G] = p[Order::B] = 0;
					return;
				}
				p[Order::R] = value_type((p[Order::R] * a + ColorT::base_mask) >> ColorT::base_shift);
				p[Order::G] = value_type((p[Order::G] * a + ColorT::base_mask) >> ColorT::base_shift);
				p[Order::B] = value_type((p[Order::B] * a + ColorT::base_mask) >> ColorT::base_shift);
			}
		}


		//--------------------------------------------------------------------
		static _INLINE_ void demultiply(value_type* p)
		{
			calc_type a = p[Order::A];
			if(a < ColorT::base_mask)
			{
				if(a == 0)
				{
					p[Order::R] = p[Order::G] = p[Order::B] = 0;
					return;
				}
				calc_type r = (calc_type(p[Order::R]) * ColorT::base_mask) / a;
				calc_type g = (calc_type(p[Order::G]) * ColorT::base_mask) / a;
				calc_type b = (calc_type(p[Order::B]) * ColorT::base_mask) / a;
				p[Order::R] = value_type((r > ColorT::base_mask) ? ColorT::base_mask : r);
				p[Order::G] = value_type((g > ColorT::base_mask) ? ColorT::base_mask : g);
				p[Order::B] = value_type((b > ColorT::base_mask) ? ColorT::base_mask : b);
			}
		}

		//--------------------------------------------------------------------
		static _INLINE_ void opaque(value_type* p)
		{
			p[Order::A] = ColorT::base_mask;
		}
	};

	//=============================================================blender_rgba
	template<class ColorT, class Order> struct blender_rgba
	{
		typedef ColorT color_type;
		typedef Order order_type;
		typedef typename color_type::value_type value_type;
		typedef typename color_type::calc_type calc_type;
		enum base_scale_e 
		{ 
			base_shift = color_type::base_shift,
			base_mask  = color_type::base_mask
		};

		//--------------------------------------------------------------------
		static _INLINE_ void blend_pix(value_type* p, 
			unsigned cr, unsigned cg, unsigned cb,
			unsigned alpha, 
			unsigned cover=0)
		{
			calc_type r = p[Order::R];
			calc_type g = p[Order::G];
			calc_type b = p[Order::B];
			calc_type a = p[Order::A];
			p[Order::R] = (value_type)(((cr - r) * alpha + (r << base_shift)) >> base_shift);
			p[Order::G] = (value_type)(((cg - g) * alpha + (g << base_shift)) >> base_shift);
			p[Order::B] = (value_type)(((cb - b) * alpha + (b << base_shift)) >> base_shift);
			p[Order::A] = (value_type)((alpha + a) - ((alpha * a + base_mask) >> base_shift));
		}
	};

	//----------------------------------------------------------const_row_info
	template<class T> struct const_row_info
	{
		int x1, x2;
		const T* ptr;
		const_row_info() {}
		const_row_info(int x1_, int x2_, const T* ptr_) : 
		x1(x1_), x2(x2_), ptr(ptr_) {}
	};

	//===========================================================row_accessor
	template<class T> class row_accessor
	{
	public:
		typedef const_row_info<T> row_data;

		//-------------------------------------------------------------------
		row_accessor() :
		m_buf(0),
			m_start(0),
			m_width(0),
			m_height(0),
			m_stride(0)
		{
		}

		//--------------------------------------------------------------------
		row_accessor(T* buf, unsigned width, unsigned height, int stride) :
		m_buf(0),
			m_start(0),
			m_width(0),
			m_height(0),
			m_stride(0)
		{
			attach(buf, width, height, stride);
		}


		//--------------------------------------------------------------------
		void attach(T* buf, unsigned width, unsigned height, int stride)
		{
			m_buf = m_start = buf;
			m_width = width;
			m_height = height;
			m_stride = stride;
			if(stride < 0) 
			{ 
				m_start = m_buf - int(height - 1) * stride;
			}
		}

		//--------------------------------------------------------------------
		_INLINE_       T* buf()          { return m_buf;    }
		_INLINE_ const T* buf()    const { return m_buf;    }
		_INLINE_ unsigned width()  const { return m_width;  }
		_INLINE_ unsigned height() const { return m_height; }
		_INLINE_ int      stride() const { return m_stride; }
		_INLINE_ unsigned stride_abs() const 
		{
			return (m_stride < 0) ? unsigned(-m_stride) : unsigned(m_stride); 
		}

		//--------------------------------------------------------------------
		_INLINE_       T* row_ptr(int, int y, unsigned) 
		{ 
			return m_start + y * m_stride; 
		}
		_INLINE_       T* row_ptr(int y)       { return m_start + y * m_stride; }
		_INLINE_ const T* row_ptr(int y) const { return m_start + y * m_stride; }
		_INLINE_ row_data row    (int y) const 
		{ 
			return row_data(0, m_width-1, row_ptr(y)); 
		}

		//--------------------------------------------------------------------
		template<class RenBuf>
		void copy_from(const RenBuf& src)
		{
			unsigned h = height();
			if(src.height() < h) h = src.height();

			unsigned l = stride_abs();
			if(src.stride_abs() < l) l = src.stride_abs();

			l *= sizeof(T);

			unsigned y;
			unsigned w = width();
			for (y = 0; y < h; y++)
			{
				memcpy(row_ptr(0, y, w), src.row_ptr(y), l);
			}
		}

		//--------------------------------------------------------------------
		void clear(T value)
		{
			unsigned y;
			unsigned w = width();
			unsigned stride = stride_abs();
			for(y = 0; y < height(); y++)
			{
				T* p = row_ptr(0, y, w);
				unsigned x;
				for(x = 0; x < stride; x++)
				{
					*p++ = value;
				}
			}
		}

	private:
		//--------------------------------------------------------------------
		T*            m_buf;    // Pointer to renrdering buffer
		T*            m_start;  // Pointer to first pixel depending on stride 
		unsigned      m_width;  // Width in pixels
		unsigned      m_height; // Height in pixels
		int           m_stride; // Number of bytes per row. Can be < 0
	};

	typedef row_accessor<int8u> rendering_buffer;

	//===============================================copy_or_blend_rgba_wrapper
	template<class Blender> struct copy_or_blend_rgba_wrapper
	{
		typedef typename Blender::color_type color_type;
		typedef typename Blender::order_type order_type;
		typedef typename color_type::value_type value_type;
		typedef typename color_type::calc_type calc_type;
		enum base_scale_e
		{
			base_shift = color_type::base_shift,
			base_scale = color_type::base_scale,
			base_mask  = color_type::base_mask
		};

		//--------------------------------------------------------------------
		static _INLINE_ void copy_or_blend_pix(value_type* p, 
			unsigned cr, unsigned cg, unsigned cb,
			unsigned alpha)
		{
			if(alpha)
			{
				if(alpha == base_mask)
				{
					p[order_type::R] = cr;
					p[order_type::G] = cg;
					p[order_type::B] = cb;
					p[order_type::A] = base_mask;
				}
				else
				{
					Blender::blend_pix(p, cr, cg, cb, alpha);
				}
			}
		}

		//--------------------------------------------------------------------
		static _INLINE_ void copy_or_blend_pix(value_type* p, 
			unsigned cr, unsigned cg, unsigned cb,
			unsigned alpha,
			unsigned cover)
		{
			if(cover == 255)
			{
				copy_or_blend_pix(p, cr, cg, cb, alpha);
			}
			else
			{
				if(alpha)
				{
					alpha = (alpha * (cover + 1)) >> 8;
					if(alpha == base_mask)
					{
						p[order_type::R] = cr;
						p[order_type::G] = cg;
						p[order_type::B] = cb;
						p[order_type::A] = base_mask;
					}
					else
					{
						Blender::blend_pix(p, cr, cg, cb, alpha, cover);
					}
				}
			}
		}
	};


	//=================================================pixfmt_alpha_blend_rgba
	template<class Blender, class RenBuf, class PixelT = int32u> 
	class pixfmt_alpha_blend_rgba
	{
	public:
		typedef RenBuf   rbuf_type;
		typedef typename rbuf_type::row_data row_data;
		typedef PixelT   pixel_type;
		typedef Blender  blender_type;
		typedef typename blender_type::color_type color_type;
		typedef typename blender_type::order_type order_type;
		typedef typename color_type::value_type value_type;
		typedef typename color_type::calc_type calc_type;
		typedef copy_or_blend_rgba_wrapper<blender_type> cob_type;
		enum base_scale_e
		{
			base_shift = color_type::base_shift,
			base_scale = color_type::base_scale,
			base_mask  = color_type::base_mask,
			pix_width  = sizeof(pixel_type)
		};

		//--------------------------------------------------------------------
		pixfmt_alpha_blend_rgba() : m_rbuf(0) {}
		explicit pixfmt_alpha_blend_rgba(rbuf_type& rb) : m_rbuf(&rb) {}
		void attach(rbuf_type& rb) { m_rbuf = &rb; }

		//--------------------------------------------------------------------
		template<class PixFmt>
		bool attach(PixFmt& pixf, int x1, int y1, int x2, int y2)
		{
			rect_i r(x1, y1, x2, y2);
			if(r.clip(rect_i(0, 0, pixf.width()-1, pixf.height()-1)))
			{
				int stride = pixf.stride();
				m_rbuf->attach(pixf.pix_ptr(r.x1, stride < 0 ? r.y2 : r.y1), 
					(r.x2 - r.x1) + 1,
					(r.y2 - r.y1) + 1,
					stride);
				return true;
			}
			return false;
		}

		//--------------------------------------------------------------------
		_INLINE_ unsigned width()  const { return m_rbuf->width();  }
		_INLINE_ unsigned height() const { return m_rbuf->height(); }
		_INLINE_ int      stride() const { return m_rbuf->stride(); }

		//--------------------------------------------------------------------
		_INLINE_       int8u* row_ptr(int y)       { return m_rbuf->row_ptr(y); }
		_INLINE_ const int8u* row_ptr(int y) const { return m_rbuf->row_ptr(y); }
		_INLINE_ row_data     row(int y)     const { return m_rbuf->row(y); }

		//--------------------------------------------------------------------
		template<class Function> void for_each_pixel(Function f)
		{
			unsigned y;
			for(y = 0; y < height(); ++y)
			{
				row_data r = m_rbuf->row(y);
				if(r.ptr)
				{
					unsigned len = r.x2 - r.x1 + 1;
					value_type* p = 
						(value_type*)m_rbuf->row_ptr(r.x1, y, len) + (r.x1 << 2);
					do
					{
						f(p);
						p += 4;
					}
					while(--len);
				}
			}
		}

		//--------------------------------------------------------------------
		void premultiply()
		{
			for_each_pixel(multiplier_rgba<color_type, order_type>::premultiply);
		}

		//--------------------------------------------------------------------
		void demultiply()
		{
			for_each_pixel(multiplier_rgba<color_type, order_type>::demultiply);
		}

		//--------------------------------------------------------------------
		void opaque()
		{
			for_each_pixel(multiplier_rgba<color_type, order_type>::opaque);
		}

	private:
		rbuf_type* m_rbuf;
	};

	typedef blender_rgba<rgba8, order_rgba> blender_rgba32; //----blender_rgba32

	//-----------------------------------------------------------------------
	typedef int32u pixel32_type;
	typedef pixfmt_alpha_blend_rgba<blender_rgba32, rendering_buffer, pixel32_type> pixfmt_rgba32; //----pixfmt_rgba32

}//namespace HELPER

namespace MHZX
{

//////////////////////////////////////////////////////////////////////////
//
// static functions
//
void CRenderHelper::PremultiplyImageBuffer(void* buf, unsigned width, unsigned height)
{
	HELPER::rendering_buffer rbuf;
	rbuf.attach((HELPER::int8u*)buf, width, height, width*sizeof(HELPER::pixfmt_rgba32::pixel_type));
	HELPER::pixfmt_rgba32 pixf(rbuf);
	pixf.premultiply();
}

void CRenderHelper::OpaqueImageBuffer(void* buf, unsigned width, unsigned height)
{
	HELPER::rendering_buffer rbuf;
	rbuf.attach((HELPER::int8u*)buf, width, height, width*sizeof(HELPER::pixfmt_rgba32::pixel_type));
	HELPER::pixfmt_rgba32 pixf(rbuf);
	pixf.opaque();
}

BOOL CRenderHelper::CreateRgnByAlphaBits(HBITMAP hAlphaBmp, HRGN& hRgn)
{
	BITMAP BitMap;
	if( !::GetObjectW( hAlphaBmp, sizeof(BITMAP), &BitMap ) )
	{
		return FALSE;
	}

	//取得图像的长和宽
	int nW = BitMap.bmWidth;
	int nH = BitMap.bmHeight;

	//创建总的窗体区域，初始region为0
	DWORD dwCount = BitMap.bmHeight * BitMap.bmWidthBytes;

	LPBYTE lpBits = new BYTE[dwCount];
	DWORD dwRet = ::GetBitmapBits(hAlphaBmp, dwCount, lpBits);
	if ( dwRet != dwCount )
	{
		delete[] lpBits;
		return FALSE;
	}

	if ( BitMap.bmBitsPixel != 32 )
	{
		// No alpha channel
		DeleteObject(hRgn);
		hRgn = CreateRectRgn(0,0,nW,nH); 
		return TRUE;
	}

	DeleteObject(hRgn);
	hRgn = CreateRectRgn(0,0,0,0); 

	BYTE* pLine = NULL;
	BYTE* pBit = NULL;
	int y;
	for(y = 0; y < nH ; y++)
	{
		pLine = lpBits + BitMap.bmWidthBytes * (y);
		pBit = pLine;
		HRGN rgnTemp; //保存临时region

		int iX = 0;
		do
		{
			//跳过透明色找到下一个非透明色的点.
			while (iX < nW && pBit[3] == 0 )
			{
				pBit += 4;
				iX ++;
			}
			//记住这个起始点
			int iLeftX = iX;
			//寻找下个透明色的点
			while (iX < nW && (pBit[3] != 0) )
			{
				iX ++;
				pBit += 4;
			}

			if (iLeftX < nW)
			{
				//创建一个包含起点与终点间高为1像素的临时“region”
				rgnTemp = CreateRectRgn(iLeftX, y, iX, y+1);
				//合并到主"region".
				CombineRgn(hRgn, hRgn, rgnTemp, RGN_OR);
				//删除临时"region"
				DeleteObject(rgnTemp);
			}

		}while(iX < nW);
	}

	delete[] lpBits;

	return TRUE; 
}

HBITMAP CRenderHelper::CreateDIBitmap(LPVOID pBuffer, unsigned nWidth, unsigned nHeight, BITMAPINFO** ppBitmapInfo/* = NULL*/)
{
	HBITMAP hBitmap = NULL;
	LPVOID pDibBits = NULL;
	DWORD biSizeImage = nWidth*nHeight*4;
	if( ppBitmapInfo == NULL )
	{
		BITMAPINFO tBitmapInfo = {{0},{0}};
		tBitmapInfo.bmiHeader.biSize = sizeof (BITMAPINFOHEADER);
		tBitmapInfo.bmiHeader.biPlanes = 1;
		tBitmapInfo.bmiHeader.biBitCount = 32;
		tBitmapInfo.bmiHeader.biCompression = BI_RGB;
		tBitmapInfo.bmiHeader.biXPelsPerMeter = 0;
		tBitmapInfo.bmiHeader.biYPelsPerMeter = 0;
		tBitmapInfo.bmiHeader.biClrUsed = 0;
		tBitmapInfo.bmiHeader.biClrImportant = 0;
		tBitmapInfo.bmiHeader.biWidth = nWidth;
		tBitmapInfo.bmiHeader.biHeight = -(LONG)nHeight;
		tBitmapInfo.bmiHeader.biSizeImage = biSizeImage;
		hBitmap = ::CreateDIBSection( NULL,
			&tBitmapInfo, DIB_RGB_COLORS, &pDibBits, NULL, 0 );
		if( hBitmap == NULL || pDibBits == NULL )
		{
			return NULL;
		}
	}
	else
	{
		*ppBitmapInfo = (BITMAPINFO*)::HeapAlloc( ::GetProcessHeap(), 0, sizeof(BITMAPINFO) );
		if( *ppBitmapInfo == NULL )
		{
			return NULL;
		}

		BITMAPINFO& tBitmapInfo = **ppBitmapInfo;

		tBitmapInfo.bmiHeader.biSize = sizeof (BITMAPINFOHEADER);
		tBitmapInfo.bmiHeader.biPlanes = 1;
		tBitmapInfo.bmiHeader.biBitCount = 32;
		tBitmapInfo.bmiHeader.biCompression = BI_RGB;
		tBitmapInfo.bmiHeader.biXPelsPerMeter = 0;
		tBitmapInfo.bmiHeader.biYPelsPerMeter = 0;
		tBitmapInfo.bmiHeader.biClrUsed = 0;
		tBitmapInfo.bmiHeader.biClrImportant = 0;
		tBitmapInfo.bmiHeader.biWidth = nWidth;
		tBitmapInfo.bmiHeader.biHeight = -(LONG)nHeight;
		tBitmapInfo.bmiHeader.biSizeImage = biSizeImage;

		hBitmap = ::CreateDIBSection( NULL,
			&tBitmapInfo, DIB_RGB_COLORS, &pDibBits, NULL, 0 );
		if( hBitmap == NULL || pDibBits == NULL )
		{
			::HeapFree( ::GetProcessHeap(), 0, *ppBitmapInfo );
			return NULL;
		}
	}

	memcpy( pDibBits, pBuffer, biSizeImage );
	return hBitmap;
}

void CRenderHelper::FreeBitmapInfo( BITMAPINFO* pBitmapInfo )
{
	if ( pBitmapInfo != NULL ) 
	{
		HeapFree(GetProcessHeap(), 0 , pBitmapInfo);
	}
}

HBITMAP CRenderHelper::LoadBitmap( LPVOID pBuffer, size_t len, BOOL bPremultiply, int* width, int* height)
{
	ASSERT( pBuffer);

	BITMAPFILEHEADER* fileHeader = (BITMAPFILEHEADER*)pBuffer;

	// Check file type
	if( fileHeader->bfType != 'MB' )
	{
		return NULL;
	}

	// Read info header
	
	BITMAPINFOHEADER* infoHeader = (BITMAPINFOHEADER*)(fileHeader + 1);
	size_t sizeOfBits = infoHeader->biSizeImage ? infoHeader->biSizeImage :
		fileHeader->bfSize - fileHeader->bfOffBits;
	if ( sizeOfBits != len - sizeof( BITMAPFILEHEADER) - sizeof( BITMAPINFOHEADER))
	{
		// empty bitmap
		return NULL;
	}
	const void* bmpBuffer = (const void*)(infoHeader + 1);
	

	BITMAPINFO* tBitmapInfo = (BITMAPINFO*)infoHeader;

	// 根据位图实际数据生成bitmap,而不根据桌面设备的显示位数
	LPVOID bitsArray = NULL;
	HBITMAP hBitmap = ::CreateDIBSection (
		NULL,
		tBitmapInfo,
		DIB_RGB_COLORS,
		&bitsArray,
		NULL,
		0);
	if( NULL == bitsArray)
	{
		return NULL;
	}
	int strideAbs = 0;
	if( infoHeader->biBitCount == 32 )
	{
		strideAbs = infoHeader->biWidth * 4;
	}
	else
	{
		strideAbs = (infoHeader->biWidth * infoHeader->biBitCount + 31 ) >> 5 << 2; 
	}
	//UINT sizeOfBits = infoHeader.biSizeImage;
	sizeOfBits = infoHeader->biHeight * strideAbs;

	memcpy( bitsArray, bmpBuffer, sizeOfBits);
	if( infoHeader->biBitCount == 32 && bPremultiply)
	{
		PremultiplyImageBuffer( bitsArray, infoHeader->biWidth, infoHeader->biHeight);
	}
	if( width)
	{
		*width = infoHeader->biWidth;
	}
	if( height)
	{
		*height = infoHeader->biHeight;
	}
	return hBitmap;
}

// 读取tga文件
HBITMAP CRenderHelper::LoadTarga( LPCVOID pBuffer, size_t len, BOOL bPremultiply, int* width, int* height)
{
	// 从内存数据解析TGA图像,并生成HBITMAP对象
	HBITMAP hBitmap = NULL;
	BITMAPINFO* pBitmapInfo = HELPER::texReadTGAData(pBuffer, len, &hBitmap, bPremultiply);
	if( pBitmapInfo == NULL )
	{
		return NULL;
	}

	if( width)
	{
		*width = pBitmapInfo->bmiHeader.biWidth;
	}
	
	if( height)
	{
		*height = pBitmapInfo->bmiHeader.biHeight;
	}

	return hBitmap;
}

} // namespace MHZX