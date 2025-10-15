#ifndef __Nuclear_PIMG_H
#define __Nuclear_PIMG_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\pobject.h"
#include "..\common\xptypes.h"

namespace Nuclear
{
	class PImg : public PObject
	{
	public:
		enum IMGVER{
			PREVER = 1, //编辑器中提供了转换到新版本的功能
			FOOVER = 2, //2008-12-05----把大图切割成512*512的小图存为dds文件，
						//加载地图时选择加载恰当的块达到减少内存使用的目的。
			NEWVER = 3, //变更字节序了..
			VERSION04 = 4, //增加了遮罩区域m_mask
			VERSION05 = 5, //文件纹理格式改为存对应的int值..
			VERSION06 = 6, //遮挡区域改为封闭的多边形...一般不超过10个顶点..
			CURVER = 6,
		};	

	public:
		PImg()
		{
			m_nRow = 0;
			m_nCol = 0;
			m_nFileSize = 0;
			m_nFileWidth = 0;
			m_nFileHeight = 0;

			m_filefmt = Nuclear::XPIFF_DDS;
			m_texfmt = Nuclear::XPTEXFMT_DXT5;

			m_dwVer = CURVER; // 这个变量初始化为当前版本其实是没道理的

			CenterPoint = Nuclear::CPOINT();
			for(int i=0; i<4; i++)
				pos[i] = Nuclear::CPOINT();
			m_vectorRct.clear();
			m_mask.clear();
		}
		//
		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);
		int GetSourceVersion() { return m_dwVer; }
		void ResetImgObject();//转新文件时候用...
		// 基本操作

		CPOINT CenterPoint; //重心 ---相对图片左上角的----右正、下正...
		CPOINT pos[4]; //左 下 右 上 0 1 2 3

		int m_nRow;
		int m_nCol;
		std::vector<CRECT> m_vectorRct;
		int m_nFileSize;
		int m_nFileWidth;
		int m_nFileHeight;
	//	std::vector<CRECT> m_mask; // todo 遮罩层, 人在此区域内时此物件会半透明显示
		std::vector<CPOINT> m_mask;

		int m_filefmt;		// 文件格式
		int m_texfmt;		// 纹理格式

		int m_dwVer; // 磁盘源文件的版本，不是对象的版本

	};
	
}

#endif