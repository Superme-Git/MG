#pragma once

#include "..\common\pobject.h"
#include "particlesyscommon.h"

namespace Nuclear
{
	class PSVertexsObject :	public PObject
	{
	public:
		PSVertexsObject(void);
		virtual ~PSVertexsObject(void);

	public:
		enum VERTEXSVER
		{
			VERSION = 1,
			VERSION_EDITINFO = 2, //顶点的类型、row、col
			VERSION_UV = 3,		  //顶点结构中只存uv不存xy了..目的是为了让顶点数据与图片大小无关..

			//顶点顺序为逆时针方向，使用者注意...
//			0------------3   
//			|            |   
//			|            |   
//			|            |   
//			|            |   
//			|            |   
//			1------------2   

			VERSION_2PicLineAngle = 4, //把图分成两份的线的角度 2011-7-27
			CURVER = 4,
		};	

	public:
		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);

		SHAPE_LIST& GetShapeList() { return m_shapeList; }
		void SetShapeList(const SHAPE_LIST& data) {	m_shapeList = data; }
		//以下4个方法是编辑器专用的
		void SetShapeType(PSL_ABSRTACTSHAPETYPE shapeType) { m_shapeType = shapeType; }
		void SetShapeRow(int nRow) { m_nRow = nRow; }
		void SetShapeCol(int nCol) { m_nCol = nCol; }
		void SetLineAngle(int nLineAngle) { m_nLineAngle = nLineAngle; }

		PSL_ABSRTACTSHAPETYPE GetShapeType() { return m_shapeType; }
		int GetShapeRow() { return m_nRow; }
		int GetShapeCol() { return m_nCol; }
		int GetLineAngle() { return m_nLineAngle; }

	private:
		SHAPE_LIST m_shapeList;
		//以下三个参数是编辑器专用的...
		PSL_ABSRTACTSHAPETYPE m_shapeType;//自定义、三角形、矩形、2pic
		int m_nRow;
		int m_nCol;
		int m_nLineAngle; //2pic LineAngle
		
	private:
//		PSVertexsObject(const PSVertexsObject&);
		PSVertexsObject& operator=(const PSVertexsObject&);
	};
}