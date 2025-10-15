#ifndef __Nuclear_pslpathobject_H__
#define __Nuclear_pslpathobject_H__
#include "../common/nupobject.h"
#include "nuparticlesyscommon.h"

namespace Nuclear
{
	class CPslPathObject :	public NuclearObject
	{
	public:
		CPslPathObject(void);
		virtual ~CPslPathObject(void);

	public:
		std::vector<POINT>& GetFullPathPointVector() { return m_vectorFullPathPoint; }
		std::vector<PATHPOINT>& GetVectorPathPoint() { return m_vectorPathPoint; }

		void SetFullPathPointVector(std::vector<POINT>& data) { m_vectorFullPathPoint = data; }
		void SetVectorPathPoint(std::vector<PATHPOINT>& data) { m_vectorPathPoint = data; }

		void SetVersion(int nVer) { m_nVer = nVer; }
		void SetVectorCharPathFlag(BOOL bVectorCharPath) { m_bVectorCharPath = bVectorCharPath; }
		void SetCurveSampleFlag(BOOL bCurveSample) { m_bCurveSample = bCurveSample; }

		DWORD GetVersion() { return m_nVer; }
		BOOL GetVectorCharPointFlag() { return m_bVectorCharPath; }
		BOOL GetCurveSampleFlag() { return m_bCurveSample; }

		NuclearOStream& marshal(NuclearOStream &) const;
		const INuclearStream& unmarshal(const INuclearStream &);

	private:
		std::vector<POINT> m_vectorFullPathPoint;
		int m_nVer;
		int m_bVectorCharPath;
		std::vector<PATHPOINT> m_vectorPathPoint;

		int m_bCurveSample; //默认为FALSE m_bCurveSample,为TRUE则表示路径已经是美术编辑好的了,不需要采样就可以直接使用,目前用于编辑经络路径.


		CPslPathObject& operator=(const CPslPathObject&);

	};
}

#endif