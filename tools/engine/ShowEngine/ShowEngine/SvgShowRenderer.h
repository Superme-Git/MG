//////////////////////////////////////////////////////////////////////////
//
// SvgShowRenderer.h
//
// author : yanghaibo
// date :	2008-11-06
//
//////////////////////////////////////////////////////////////////////////
#pragma once

#include "ShowRenderer.h"

#include "..\agg-2.5\examples\svg_viewer\svg_render_engine.h"

class CSvgShowRenderer : public IShowRenderer
{
public:
	CSvgShowRenderer(void);
	~CSvgShowRenderer(void);


	//====================================================================
	// 初始化及其相关接口
	//====================================================================

	//
	// summary:
	//		解析Show的内存数据，type
	// 
	// parameter:
	//		[in] pShowData - Show的内存数据
	//		[in] size - Show的内存数据大小，以字节计
	//
	// returns:
	//		正确解析，返回true；失败，返回false。
	//
	virtual bool ParseData(const void* pShowData, size_t size);

	//
	// summary:
	//		解析指定的Show文件
	//
	// parameter:
	//		pShowFileName - show 的文件名，由文件名后缀确定图像类型
	//
	// returns:
	//		正确解析，返回true；失败，返回false。
	//
	virtual bool ParseFile(const wchar_t* pShowFileName);

	//====================================================================
	// Show本身定义的尺寸
	// 该尺寸不受放射变换和设备信息影响
	// ParseData成功后有效
	//====================================================================

	//
	// summary: Show的宽度
	//
	virtual double GetShowWidth() const;

	//
	// summary: Show的高度
	//
	virtual double GetShowHeight() const;

	//
	// summary: 
	//		设置显示设备信息
	//		如未设置，将使用Show本身定义的大小确定设备大小
	//
	// parameter:
	//		[in] width - 设备宽度
	//		[in] height - 设备高度
	//
	virtual void SetDeviceSize( unsigned width, unsigned height );

	//
	// summary: 
	//		获取当前设置的显示设备宽度
	//
	virtual unsigned GetDeviceWidth() const;

	//
	// summary: 
	//		获取当前设置的显示设备高度
	//
	virtual unsigned GetDeviceHeight() const;

	//
	// summary:
	//		设置显示设备的DPI信息
	//
	// parameter:
	//		[in] dpi - 一般为 96 或者 120. Windows下，可根据如下函数取得缺省值
	//		GetDeviceCaps(GetDC(NULL), LOGPIXELSX).
	//
	virtual void SetDPI(unsigned dpi);

	//
	// summary:
	//		获取当前绘制设备的DPI
	//
	// returns:
	//		返回当前绘制环境所设置的DPI或者内部使用的缺省DPI
	//
	virtual unsigned GetDPI() const;

	//
	// summary:
	//		设置图像的32位背景色
	// 
	// remarks:
	//		32位背景色定义模式：A8R8G8B8, 从高到低组成的4字节颜色值。
	//		其中A8位段为透明度信息。如果为0，则图像背景将全透明，如果为255，将不透明。
	//
	virtual void SetBgColor(  unsigned long argb8 );

	//
	// summary:
	//		获取当前缺省的背景色或者用户设置的背景色。
	// 
	// remarks:
	//		如果用户未曾调用SetBgColor()设置背景色，那么缺省为0。
	//
	// returns:
	//		32位背景色值
	//
	virtual unsigned long GetBgColor( ) const;
	//
	// summary:
	//		设置原始矢量图的调整仿射变换矩阵参数。
	//		该矩阵参数用于调整原始矢量图，因此，它将应用在编辑仿射变换之前。
	//		编辑放射变换指的是Translate()/Scale()/Rotate()/ApplyTransAffine().
	//		对于同一张矢量图，这个仿射变换矩阵参数应该只需设置一次。
	//
	// parameter:
	//		[in] matrix - 6个单位长度的double数组
	//
	// returns:
	//		设置成功返回true,无效参数返回false.
	//
	virtual bool SetAdjustTransAffine(const double* matrix);

	//
	// summary:
	//		取得原始矢量图的调整仿射变换矩阵参数。
	//
	// parameter:
	//		[out] matrix - 6个单位长度的double数组
	//
	virtual void GetAdjustTransAffine(double* matrix) const;

	//
	// summary:
	//		绘制Show
	//
	virtual void Draw();

	//
	// summary:
	//		在指定的内存中绘制Show
	//
	// parameter:
	//		[in] buf - 用于容纳A8R8G8B8格式像素数据的内存首地址指针
	//		[in] width - 内存图像像素宽度
	//		[in] height - 内存图像像素高度
	//		[in] bFillWithBgColor - 在绘制图像前是否首先使用背景色进行填充
	//		[in] bDemultiply - 是否对内存像素进行alpha恢复运算
	// 
	// remarks:
	//		对于多层渲染过程来说，第一次需要指定bFillWithBgColor，使用背景色填充图像，
	//		如未指定，调用方需首先对buf清0。
	//		如果整个图像将与背景图进行alpha叠加，当buf中的图像经多次叠加之后，
	//		最后一次的DrawOnBuffer操作一般需要进行Demultiply运算，以便与背景完美叠加.
	//
	//		另外，这种渲染模式不受外部设定的DeviceSize的影响.
	//		如果不执行OnDraw(), GetBitmapData()不会返回经DrawOnBuffer()渲染的图像，
	//		GetBitmapSize()不一定等于经DrawOnBuffer()渲染的图像的buffer大小，除非width和height与DeviceSize完全相同.
	//
	virtual void DrawOnBuffer( void* buf, unsigned width, unsigned height, bool bFillWithBgColor, bool bDemultiply );

	//====================================================================
	// 仿射变换及其相关接口
	// 每次执行仿射变换之后，需要调用Draw执行重绘操作
	//====================================================================

	//
	// summary:	平移
	//
	// parameter:
	//		dx - x 方向平移
	//		dy - y 方向平移
	//		abs - true, 绝对平移, false 相对于当前状态平移
	//
	virtual bool Translate( double dx, double dy, bool abs );

	//
	// summary:	缩放
	//
	// parameter: 
	//		scale - 放大倍数
	//		abs - true, 绝对放大, false 相对于当前状态放大
	//
	//		1.0以上的数据，图像将被放大
	//		0~1.0以以内的数据，图像将被缩小
	//		0(含0)及负数，无效参数，返回false.
	//
	virtual bool Scale( double scale, bool abs );

	//
	// summary:	旋转
	//
	// parameter: 
	//		rad - 旋转的角度，以弧度计，
	//		正值顺时针旋转，负值逆时针旋转
	//		abs - true, 绝对旋转, false 相对于当前状态旋转
	//
	virtual bool Rotate( double rad, bool abs );

	//
	// summary: 重置仿射变换，使图像回到初始状态
	//
	virtual bool OriginalView();

	//
	// summary: 获取当前图像所累积的仿射变换参数
	// 
	// parameter:
	//		[out] matrix - 仿射变换的矩阵参数数组，6个单位长
	//
	virtual void GetTransAffine( double* matrix ) const;

	//
	// summary: 在当前图像上应用仿射变换
	// 
	// parameter:
	//		[in] matrix - 仿射变换的矩阵参数数组，6个单位长
	//
	virtual void ApplyTransAffine( const double* matrix );

	//
	// summary: 在当前图像上仿射变换一个指定点
	// 
	// parameter:
	//		[inout] x - 仿射变换的横坐标
	//		[inout] y - 仿射变换的纵坐标
	//
	virtual void Transform( double& x, double& y ) const;

	//
	// summary:	Show的边界位置坐标
	//		当前Show的边界坐标受仿射变换操作影响。
	//		特别说明，如果已经对图像应用了旋转变换，
	//		该ImageBox将是旋转前的ImageBox的BoundingBox，
	//		而不仅仅是ImagePixel的BoundingBox。
	//
	virtual void GetImageBox(
		double* left, double* top, double* right, double* bottom) const;

	//
	// summary:
	//		获取Show的图像数据
	//		bgra32 format
	//
	virtual const void* GetBitmapData() const;

	//
	// summary:
	//		获取Show的图像数据大小，以字节计
	//
	virtual size_t GetBitmapSize() const;

	// 
	// summary:
	//		销毁ShowRenderer对象
	//
	virtual void Destroy();

protected:
	svg::svg_render_engine m_svg_render;
};

//////////////////////////////////////////////////////////////////////////
//
// inline 函数实现
//
inline void CSvgShowRenderer::Destroy()
{
	delete this;
}
