#pragma once
#include "..\common\pobject.h"
#include "particlesyscommon.h"

namespace Nuclear
{
	class CPslPathObject :	public PObject
	{
	public:
		CPslPathObject(void);
		virtual ~CPslPathObject(void);

	public:
		std::vector<POINT>& GetFullPathPointVector() { return m_vectorFullPathPoint; }
		std::vector<PATHPOINT>& GetVectorPathPoint() { return m_vectorPathPoint; }

		void SetFullPathPointVector(std::vector<POINT>& data)
		{
			m_vectorFullPathPoint = data;
		}

		void SetVectorPathPoint(std::vector<PATHPOINT>& data)
		{
			m_vectorPathPoint = data;
		}

		void SetVersion(int nVer) { m_nVer = nVer; }
		void SetVectorCharPathFlag(BOOL bVectorCharPath) { m_bVectorCharPath = bVectorCharPath; }
		void SetCurveSampleFlag(BOOL bCurveSample) { m_bCurveSample = bCurveSample; }

		DWORD GetVersion() { return m_nVer; }
		BOOL GetVectorCharPointFlag() { return m_bVectorCharPath; }
		BOOL GetCurveSampleFlag() { return m_bCurveSample; }

	public:
		//
		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);
	private:
		std::vector<POINT> m_vectorFullPathPoint;
		int m_nVer;
		BOOL m_bVectorCharPath;
		std::vector<PATHPOINT> m_vectorPathPoint;

		BOOL m_bCurveSample; //2010-9-7�¼���,Ĭ��ΪFALSE m_bCurveSample
								//ΪTRUE���ʾ·���Ѿ��������༭�õ���,����Ҫ�����Ϳ���ֱ��ʹ��
								//Ŀǰ���ڱ༭����·��.

		//todo  �ַ�·�����ؼ�֡·������ͨ·��.... ��Ϊ��λ���������

	private:
	//	CPslPathObject(const CPslPathObject&); //����ʹ��Ĭ�ϵĿ������캯��
		CPslPathObject& operator=(const CPslPathObject&);

	};

}
