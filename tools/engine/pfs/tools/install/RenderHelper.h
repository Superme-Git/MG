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
	///  ��ɫ BGRA32 ����(��ͬ��COLORREF�������RGB24����)
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
		/// 	��rgba8��ʽ��ͼ��������ִ��AlphaBlend����ǰ����һ��Premultiply����
		///	�˺���е�AlphaBlend�������뱳�����.
		/// </summary>
		/// <param name="buf">
		/// 	ͼ�����ݻ�����
		/// </param>
		/// <param name="width">
		/// 	ͼ����(���ص�λ)
		/// </param>
		/// <param name="height">
		/// 	ͼ��߶�(���ص�λ)
		/// </param>
		static void PremultiplyImageBuffer(__inout_bcount(width*height*4) void* buf, __in unsigned width, __in unsigned height);

		/// <summary>
		/// 	��rgba8��ʽ��ͼ�����ݵ�Alphaͨ��ȫ����Ϊ255(ͼ����ȫû��͸������)
		/// </summary>
		/// <param name="buf">
		/// 	ͼ�����ݻ�����
		/// </param>
		/// <param name="width">
		/// 	ͼ����(���ص�λ)
		/// </param>
		/// <param name="height">
		/// 	ͼ��߶�(���ص�λ)
		/// </param>
		static void OpaqueImageBuffer(__inout_bcount(width*height*4) void* buf, __in unsigned width, __in unsigned height);

		/// <summary>
		/// 	���ݴ�Alphaͨ����λͼ���󴴽�Window��Range����(HRGN)
		/// </summary>
		/// <param name="hAlphaBmp">
		/// 	[in] ��Alphaͨ����λͼ����
		/// </param>
		/// <param name="hRgn">
		/// 	[out] ��� Range ����
		/// </param>
		/// <returns type="BOOL">
		/// 	�ɹ�ʱ����TRUE,ʧ��ʱ����FALSE
		/// </returns>
		static BOOL CreateRgnByAlphaBits(__in HBITMAP hAlphaBmp, __out HRGN& hRgn);

		/// <summary>
		/// 	�����ڴ����ݴ���DIBλͼ����
		///		��λͼ����argb32λ��ʽ��,���,buf��������Ӧ��Ϊwidth*height*4���ֽ�.
		/// </summary>
		/// <param name="pBuffer">
		/// 	�ڴ������׵�ַָ��
		/// </param>
		/// <param name="nWidth">
		/// 	DIB�Ŀ��(����ֵ)
		/// </param>
		/// <param name="nHeight">
		/// 	DIB�ĸ߶�(����ֵ)
		/// </param>
		/// <param name="ppBitmapInfo">
		/// 	����ǿ�,�ɱ���DIB��BITMAPINFO�ṹ����,ʹ����Ϻ����FreeBitmapInfo()�ͷ�֮.
		/// </param>
		/// <returns type="HBITMAP">
		/// 	�ɹ�ʱ����DIBλͼ����.ʧ��ʱ����NULL.
		/// </returns>
		/// <remarks>
		///		ע: ������Wingdi.h�������� CreateDIBitmap API �Ķ���:
		///		��Ҫ���˺����뱾��������.
		///		WINGDIAPI HBITMAP WINAPI CreateDIBitmap(
		///			__in HDC hdc, 
		///			__in_opt CONST BITMAPINFOHEADER *pbmih, 
		///			__in DWORD flInit, __in_opt CONST VOID *pjBits,
		///			__in_opt CONST BITMAPINFO *pbmi, __in UINT iUsage);
		/// </remarks>
		static HBITMAP CreateDIBitmap(__in_bcount(nWidth*nHeight*4) LPVOID pBuffer, 
			__in unsigned nWidth, __in unsigned nHeight, __out_opt BITMAPINFO** ppBitmapInfo = NULL);

		/// <summary>
		/// 	�ͷ���CreateDIBitmap���ص�**ppBitmapInfo�ṹ
		/// </summary>
		/// <param name="pBitmapInfo">
		/// 	��CreateDIBitmap���ص�**ppBitmapInfo�ṹ
		/// </param>
		static void FreeBitmapInfo( __in BITMAPINFO* pBitmapInfo );

		// ��ȡbmp�ļ�
		static HBITMAP LoadBitmap( LPVOID pBuffer, size_t len, BOOL bPremultiply, int* width, int* height);

		// ��ȡtga�ļ�
		static HBITMAP LoadTarga( LPCVOID pBuffer, size_t len, BOOL bPremultiply, int* width, int* height);
	};

} // namespace MHZX
