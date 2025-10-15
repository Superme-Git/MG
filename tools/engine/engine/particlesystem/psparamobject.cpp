#include "stdafx.h"
#include "PsParamObject.h"

namespace Nuclear
{

	CPsParamObject::CPsParamObject(void)
	{
	}

	CPsParamObject::~CPsParamObject(void)
	{
	}

	XOStream& CPsParamObject::marshal(XOStream &os) const
	{ //save
		os << (int)CURVER;

		os << m_psParam.m_fEmitterSpinAngle;
		os << m_psParam.m_fLineLength;
		os << m_psParam.m_fRctWidth;
		os << m_psParam.m_fRctHeight;

		int count = (int)m_psParam.m_vectorRctBlindArea.size();
		os << count;
		for(int i=0; i<count; i++)
			os << m_psParam.m_vectorRctBlindArea[i];

		count = (int)m_psParam.m_vectorBlockLine.size();
		os << count;
		for(int i=0; i<count; i++)
		{
			os << m_psParam.m_vectorBlockLine[i].pt0;
			os << m_psParam.m_vectorBlockLine[i].pt1;
		}

		count = (int)m_psParam.m_vectorReflectLine.size();
		os << count;
		for(int i=0; i<count; i++)
		{
			os << m_psParam.m_vectorReflectLine[i].pt0;
			os << m_psParam.m_vectorReflectLine[i].pt1;
		}

		return os;
	}

	const XIStream& CPsParamObject::unmarshal(const XIStream &os)
	{ //Load
		int nVer;
		os >> nVer;

		os >> m_psParam.m_fEmitterSpinAngle;
		os >> m_psParam.m_fLineLength;
		os >> m_psParam.m_fRctWidth;
		os >> m_psParam.m_fRctHeight;

		int count;
		os >> count;
		CRECT rct;
		for(int i=0; i<count; i++)
		{
			os >> rct;
			m_psParam.m_vectorRctBlindArea.push_back(rct);
		}

		os >> count;
		LINE line;
		for(int i=0; i<count; i++)
		{
			os >> line.pt0;
			os >> line.pt1;
			m_psParam.m_vectorBlockLine.push_back(line);
		}

		os >> count;
		for(int i=0; i<count; i++)
		{
			os >> line.pt0;
			os >> line.pt1;
			m_psParam.m_vectorReflectLine.push_back(line);
		}

		return os;
	}
}
