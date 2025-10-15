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

		BOOL m_bCurveSample; //2010-9-7新加入,默认为FALSE m_bCurveSample
								//为TRUE则表示路径已经是美术编辑好的了,不需要采样就可以直接使用
								//目前用于编辑经络路径.

		//todo  字符路径、关键帧路径、普通路径.... 改为用位标记来区分

	private:
	//	CPslPathObject(const CPslPathObject&); //允许使用默认的拷贝构造函数
		CPslPathObject& operator=(const CPslPathObject&);

	};

}
