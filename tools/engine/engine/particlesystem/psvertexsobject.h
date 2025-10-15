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
			VERSION_EDITINFO = 2, //��������͡�row��col
			VERSION_UV = 3,		  //����ṹ��ֻ��uv����xy��..Ŀ����Ϊ���ö���������ͼƬ��С�޹�..

			//����˳��Ϊ��ʱ�뷽��ʹ����ע��...
//			0------------3   
//			|            |   
//			|            |   
//			|            |   
//			|            |   
//			|            |   
//			1------------2   

			VERSION_2PicLineAngle = 4, //��ͼ�ֳ����ݵ��ߵĽǶ� 2011-7-27
			CURVER = 4,
		};	

	public:
		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);

		SHAPE_LIST& GetShapeList() { return m_shapeList; }
		void SetShapeList(const SHAPE_LIST& data) {	m_shapeList = data; }
		//����4�������Ǳ༭��ר�õ�
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
		//�������������Ǳ༭��ר�õ�...
		PSL_ABSRTACTSHAPETYPE m_shapeType;//�Զ��塢�����Ρ����Ρ�2pic
		int m_nRow;
		int m_nCol;
		int m_nLineAngle; //2pic LineAngle
		
	private:
//		PSVertexsObject(const PSVertexsObject&);
		PSVertexsObject& operator=(const PSVertexsObject&);
	};
}