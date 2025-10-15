#include "nupdistortionobject.h"

namespace Nuclear
{

	NuclearDistortionObject::NuclearDistortionObject(void)
	{
		m_bLightFlag = false;
	}

	NuclearDistortionObject::~NuclearDistortionObject(void)
	{
	}

	NuclearOStream& NuclearDistortionObject::marshal(NuclearOStream &os) const
	{
		os << (int)Distort_CURVER;

		//版本3开始这么写wstring的
		os<<m_DistortionObj.strImgFileName;
		os<<m_DistortionObj.strLightFile;

		os<<m_bLightFlag;

		os<<(int)(m_DistortionObj.nType);
		os<<m_DistortionObj.fCycNum;
		int cnt = (int)m_DistortionObj.fAmplitudes.size();
		os<<cnt;
		for(int i=0; i<cnt; i++)
		{
			os<<m_DistortionObj.fAmplitudes[i];
		}
		os<<m_DistortionObj.fSpeed;
 
		return os;
	}

	const INuclearStream& NuclearDistortionObject::unmarshal(const INuclearStream &os)
	{ 
		int nVer;
		os >> nVer;

		if (nVer>Distort_Version_AmplitudeCurve) //版本3
		{
			os >> m_DistortionObj.strImgFileName;
			os >> m_DistortionObj.strLightFile;
			os>>m_bLightFlag;
		}
		else
		{
			unsigned int str_size = 0;
			wchar_t *str = NULL;
		
			os >> m_DistortionObj.strImgFileName;

			m_DistortionObj.strLightFile = L"";
			m_bLightFlag = false;
		}
		if (nVer < Distort_Version_NotMountElement)	//版本4，
		{
			if (m_DistortionObj.strImgFileName.size() > 0)
				m_DistortionObj.strImgFileName = m_DistortionObj.strImgFileName;
			if (m_DistortionObj.strLightFile.size() > 0)
				m_DistortionObj.strLightFile = m_DistortionObj.strLightFile;
		}

		int ntype;
		os>>ntype;
		m_DistortionObj.nType = (NuclearDistortionMeshType)ntype;
		os>>m_DistortionObj.fCycNum;
		m_DistortionObj.fAmplitudes.clear();
		if (nVer < Distort_Version_AmplitudeCurve) //版本1
		{
			float fAmplitude;
			os>>fAmplitude;
			m_DistortionObj.fAmplitudes.push_back(NuclearFPoint(0.0f, fAmplitude));
			m_DistortionObj.fAmplitudes.push_back(NuclearFPoint(1.0f, fAmplitude));
		}
		else
		{
			int cnt = 0;
			os>>cnt;
			for(int i=0; i<cnt; i++)
			{
				NuclearFPoint value;
				os>>value;
				m_DistortionObj.fAmplitudes.push_back(value);
			}
		}
		os>>m_DistortionObj.fSpeed;

		return os;
	}


}
