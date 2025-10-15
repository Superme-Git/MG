#include "nupslpathobject.h"


namespace Nuclear
{
	CPslPathObject::CPslPathObject(void)
	{
		m_vectorFullPathPoint.clear();
		m_nVer = 3; //0-->1 调字节序  1-->2 增加关键帧路径(m_bCurveSample==true)
					//版本2的关键帧路径上的所有点都是美术编辑好的，不需要曲线插值
					//2-->3 版本3的关键帧路径(m_bCurveSample==false)
					//版本3的关键帧路径美术编辑部分点，需要曲线插值

		m_bVectorCharPath = FALSE;
		m_bCurveSample = FALSE;
		m_vectorPathPoint.clear();
	}

	CPslPathObject::~CPslPathObject(void){}

	NuclearOStream& CPslPathObject::marshal(NuclearOStream &os) const
	{
		os << m_nVer;
		os << m_bVectorCharPath;
		os << m_bCurveSample;

		if(m_bVectorCharPath)
		{
			int nCount = (int)m_vectorFullPathPoint.size();
			os << nCount;
			for(int i=0; i<nCount; i++)
				os << m_vectorFullPathPoint[i];
		}
		else
		{ 
			int nCount = (int)m_vectorPathPoint.size();
			os << nCount;
			for(int i=0; i<nCount; i++)
			{
				os << m_vectorPathPoint[i].pt;
				os << m_vectorPathPoint[i].m_bCurve;
				os << m_vectorPathPoint[i].m_bLink;
				os << m_vectorPathPoint[i].pre_ControlPt;
				os << m_vectorPathPoint[i].next_ControlPt;

				os << m_vectorPathPoint[i].bKeyPoint;
			}
		}

		return os;
	}

	const INuclearStream& CPslPathObject::unmarshal(const INuclearStream &os)
	{
		os >> m_nVer;
		os >> m_bVectorCharPath;

		if(m_nVer<2)
			m_bCurveSample = false;
		else
			os >> m_bCurveSample;

		int nCount = 0;
		os >> nCount;

		if(m_bVectorCharPath)
		{
			m_vectorFullPathPoint.clear();
			for(int i=0; i<nCount; i++)
			{
				POINT pt;
				os >> pt;
				m_vectorFullPathPoint.push_back(pt);
			}
		}
		else
		{
			m_vectorPathPoint.clear();
			for(int i=0; i<nCount; i++)
			{
				PATHPOINT pathPoint;
				os >> pathPoint.pt;
				os >> pathPoint.m_bCurve;
				os >> pathPoint.m_bLink;
				os >> pathPoint.pre_ControlPt;
				os >> pathPoint.next_ControlPt;

				if(m_nVer>2) //版本3增加的
					os >> pathPoint.bKeyPoint;
				else
					pathPoint.bKeyPoint = false;

				m_vectorPathPoint.push_back(pathPoint);
			}
		}

		return os;
	}
}