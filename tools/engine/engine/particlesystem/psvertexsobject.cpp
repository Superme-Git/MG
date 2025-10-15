#include "stdafx.h"
#include "PSVertexsObject.h"


namespace Nuclear
{

PSVertexsObject::PSVertexsObject(void)
{
	m_shapeList.clear();

	m_shapeType = PSLSHAPETYPE_VORONOI;//�Զ��塢�����Ρ�����
	m_nRow = 0;
	m_nCol = 0;
	m_nLineAngle = 0;
}

PSVertexsObject::~PSVertexsObject(void)
{
}

XOStream& PSVertexsObject::marshal(XOStream &os) const
{ //save
	os << (int)CURVER;

	//�汾2����
	os << (int)m_shapeType;//�Զ��塢�����Ρ�����
	os << m_nRow;
	os << m_nCol;
	//2
	os << m_nLineAngle; //�汾4����

	int nShapeCount = (int)m_shapeList.size();
	os << nShapeCount;

	for(int i=0; i<nShapeCount; i++)
	{
		SHAPE shape = m_shapeList[i];
		int nVertex = (int)( shape.vertexs.size() );
		os << nVertex;
	//	os << shape.center.x; //�汾3������
	//	os << shape.center.y;
		os << shape.center.u;
		os << shape.center.v;
		for(int j=0; j<nVertex; j++)
		{
	//		os << shape.vertexs[j].x;
	//		os << shape.vertexs[j].y;
			os << shape.vertexs[j].u;
			os << shape.vertexs[j].v;
		}
	}

	return os;
}

const XIStream& PSVertexsObject::unmarshal(const XIStream &os)
{ //Load
	int dwVer;
	os >> dwVer;

	if(dwVer > 1)
	{
		int type;
		os >> type;//�Զ��塢�����Ρ�����
		m_shapeType = (PSL_ABSRTACTSHAPETYPE)type;
		os >> m_nRow;
		os >> m_nCol;
	}

	if(dwVer > 3)
	{
		os >> m_nLineAngle;
	}

	m_shapeList.clear();
	int nShapeCount;
	os >> nShapeCount;
	for(int i=0; i<nShapeCount; i++)
	{
		SHAPE shape;
		int nVertex;
		os >> nVertex;
		float fx, fy;
		if(dwVer < 3)
		{
			os >> fx;
			os >> fy;
		}
		os >> shape.center.u;
		os >> shape.center.v;
		VERTEX_LIST vertexs;
		for(int j=0; j<nVertex; j++)
		{
			Vertex vertex;
			if(dwVer < 3)
			{
				os >> fx;
				os >> fy;
			}
			os >> vertex.u;
			os >> vertex.v;

			vertexs.push_back(vertex);
		}
		shape.vertexs = vertexs;

		m_shapeList.push_back(shape);
	}

	return os;
}

}
