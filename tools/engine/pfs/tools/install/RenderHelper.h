///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : RenderHelper.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	definitions of Render Helpers 
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-9 Created.
///=============================================================================
#pragma once

namespace MHZX
{
	///-----------------------------------------------------------------------------
	///  颜色 BGRA32 类型(不同于COLORREF所定义的RGB24类型)
	///-----------------------------------------------------------------------------
	typedef DWORD COLOR;

	inline COLOR MakeColor( long r, long g, long b, long a = 255)
	{
		return  ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff) | (a << 24);
	}

	inline long GetRedValue( COLOR clrBGRA )
	{
		return (clrBGRA>>16)&0xFF;
	}

	inline long GetGreenValue( COLOR clrBGRA )
	{
		return (clrBGRA>>8)&0xFF;
	}

	inline long GetBlueValue( COLOR clrBGRA )
	{
		return clrBGRA&0xFF;
	}

	inline long GetAlphaValue( COLOR clrBGRA )
	{
		return (clrBGRA>>24)&0xFF;
	}

	inline COLOR RGB2COLOR( COLORREF clrRGB )
	{
		return MakeColor(  GetRValue(clrRGB), GetGValue(clrRGB), GetBValue (clrRGB) );
	}

	inline COLORREF COLOR2RGB( COLOR clrBGR )
	{
		return RGB( GetRedValue(clrBGR), GetGreenValue(clrBGR), GetBlueValue (clrBGR) );
	}

	inline COLORREF COLOR2RGBA( COLOR clrBGR )
	{
		return RGB( GetRedValue(clrBGR), GetGreenValue(clrBGR), GetBlueValue (clrBGR) ) | (clrBGR&0xFF000000);
	}

	//--------------------------------------------------------------------
	inline COLOR Gradient(const COLOR& c1, const COLOR& c2, double k)
	{
		typedef unsigned char value_type;
		typedef COLOR calc_type;
		const calc_type base_shift = 8;
		const calc_type base_scale = 1<<base_shift;
		calc_type ik = (calc_type)((k * base_scale)+0.5);
		
		calc_type r,g,b,a;
		r = GetRedValue(c1);
		g = GetGreenValue(c1);
		b = GetBlueValue(c1);
		a = GetAlphaValue(c1);
		calc_type cr,cg,cb,ca;
		cr = GetRedValue(c2);
		cg = GetGreenValue(c2);
		cb = GetBlueValue(c2);
		ca = GetAlphaValue(c2);

		calc_type rr,rg,rb,ra;
		rr = value_type(calc_type(r) + (((calc_type(cr) - r) * ik) >> base_shift));
		rg = value_type(calc_type(g) + (((calc_type(cg) - g) * ik) >> base_shift));
		rb = value_type(calc_type(b) + (((calc_type(cb) - b) * ik) >> base_shift));
		ra = value_type(calc_type(a) + (((calc_type(ca) - a) * ik) >> base_shift));
	
		COLOR ret = MakeColor (rr, rg, rb, ra );
		return ret;
	}

	class CRenderHelper
	{
	public:
		/// <summary>
		/// 	将rgba8格式的图像数据在执行AlphaBlend操作前进行一次Premultiply运算
		///	此后进行的AlphaBlend操作将与背景混合.
		/// </summary>
		/// <param name="buf">
		/// 	图像数据缓冲区
		/// </param>
		/// <param name="width">
		/// 	图像宽度(像素单位)
		/// </param>
		/// <param name="height">
		/// 	图像高度(像素单位)
		/// </param>
		static void PremultiplyImageBuffer(__inout_bcount(width*height*4) void* buf, __in unsigned width, __in unsigned height);

		/// <summary>
		/// 	将rgba8格式的图像数据的Alpha通道全部置为255(图像完全没有透明属性)
		/// </summary>
		/// <param name="buf">
		/// 	图像数据缓冲区
		/// </param>
		/// <param name="width">
		/// 	图像宽度(像素单位)
		/// </param>
		/// <param name="height">
		/// 	图像高度(像素单位)
		/// </param>
		static void OpaqueImageBuffer(__inout_bcount(width*height*4) void* buf, __in unsigned width, __in unsigned height);

		/// <summary>
		/// 	根据带Alpha通道的位图对象创建Window的Range对象(HRGN)
		/// </summary>
		/// <param name="hAlphaBmp">
		/// 	[in] 带Alpha通道的位图对象
		/// </param>
		/// <param name="hRgn">
		/// 	[out] 结果 Range 对象
		/// </param>
		/// <returns type="BOOL">
		/// 	成功时返回TRUE,失败时返回FALSE
		/// </returns>
		static BOOL CreateRgnByAlphaBits(__in HBITMAP hAlphaBmp, __out HRGN& hRgn);

		/// <summary>
		/// 	根据内存数据创建DIB位图对象
		///		该位图总是argb32位格式的,因此,buf的数据量应该为width*height*4个字节.
		/// </summary>
		/// <param name="pBuffer">
		/// 	内存数据首地址指针
		/// </param>
		/// <param name="nWidth">
		/// 	DIB的宽度(像素值)
		/// </param>
		/// <param name="nHeight">
		/// 	DIB的高度(像素值)
		/// </param>
		/// <param name="ppBitmapInfo">
		/// 	如果非空,可保存DIB的BITMAPINFO结构对象,使用完毕后调用FreeBitmapInfo()释放之.
		/// </param>
		/// <returns type="HBITMAP">
		/// 	成功时返回DIB位图对象.失败时返回NULL.
		/// </returns>
		/// <remarks>
		///		注: 下面是Wingdi.h中声明的 CreateDIBitmap API 的定义:
		///		不要将此函数与本函数混淆.
		///		WINGDIAPI HBITMAP WINAPI CreateDIBitmap(
		///			__in HDC hdc, 
		///			__in_opt CONST BITMAPINFOHEADER *pbmih, 
		///			__in DWORD flInit, __in_opt CONST VOID *pjBits,
		///			__in_opt CONST BITMAPINFO *pbmi, __in UINT iUsage);
		/// </remarks>
		static HBITMAP CreateDIBitmap(__in_bcount(nWidth*nHeight*4) LPVOID pBuffer, 
			__in unsigned nWidth, __in unsigned nHeight, __out_opt BITMAPINFO** ppBitmapInfo = NULL);

		/// <summary>
		/// 	释放由CreateDIBitmap返回的**ppBitmapInfo结构
		/// </summary>
		/// <param name="pBitmapInfo">
		/// 	由CreateDIBitmap返回的**ppBitmapInfo结构
		/// </param>
		static void FreeBitmapInfo( __in BITMAPINFO* pBitmapInfo );

		// 读取bmp文件
		static HBITMAP LoadBitmap( LPVOID pBuffer, size_t len, BOOL bPremultiply, int* width, int* height);

		// 读取tga文件
		static HBITMAP LoadTarga( LPCVOID pBuffer, size_t len, BOOL bPremultiply, int* width, int* height);
	};

} // namespace MHZX
