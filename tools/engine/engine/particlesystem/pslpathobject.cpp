#include "stdafx.h"
#include "pslpathobject.h"


namespace Nuclear
{
	CPslPathObject::CPslPathObject(void)
	{
		m_vectorFullPathPoint.clear();
		m_nVer = 3; //0-->1 ���ֽ���  1-->2 ���ӹؼ�֡·��(m_bCurveSample==true)
					//�汾2�Ĺؼ�֡·���ϵ����е㶼�������༭�õģ�����Ҫ���߲�ֵ
					//2-->3 �汾3�Ĺؼ�֡·��(m_bCurveSample==false)
					//�汾3�Ĺؼ�֡·�������༭���ֵ㣬��Ҫ���߲�ֵ

		m_bVectorCharPath = FALSE;
		m_bCurveSample = FALSE;
		m_vectorPathPoint.clear();
	}

	CPslPathObject::~CPslPathObject(void)
	{
	}

	XOStream& CPslPathObject::marshal(XOStream &os) const
	{ //save
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

	const XIStream& CPslPathObject::unmarshal(const XIStream &os)
	{ //Load
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

				if(m_nVer>2) //�汾3���ӵ�
					os >> pathPoint.bKeyPoint;
				else
					pathPoint.bKeyPoint = false;

				m_vectorPathPoint.push_back(pathPoint);
			}
		}

		return os;
	}
}