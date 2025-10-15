#include "stdafx.h"
#include "pdistortionobject.h"

namespace Nuclear
{

PDistortionObject::PDistortionObject(void)
{
	m_bLightFlag = false;
}

PDistortionObject::~PDistortionObject(void)
{
}

XOStream& PDistortionObject::marshal(XOStream &os) const
{ //save
	os << (int)CURVER;

// 	unsigned int nameSize = 0;
// 	nameSize = static_cast<unsigned int>(m_distortionObj.strImgFileName.size() * sizeof(wchar_t));
// 	os<<nameSize;
// 	os.push_byte(m_distortionObj.strImgFileName.data(), nameSize);
// 
// 	nameSize = static_cast<unsigned int>(m_distortionObj.strLightFile.size() * sizeof(wchar_t));
// 	os<<nameSize;
// 	os.push_byte(m_distortionObj.strLightFile.data(), nameSize);

	//版本3开始这么写wstring的
	os<<m_distortionObj.strImgFileName;
	os<<m_distortionObj.strLightFile;

	os<<m_bLightFlag;

	os<<(int)(m_distortionObj.nType);
	os<<m_distortionObj.fCycNum;
//	os<<m_distortionObj.fAmplitude; //版本1--2 振幅变为了存曲线..
	int cnt = (int)m_distortionObj.fAmplitudes.size();
	os<<cnt;
	for(int i=0; i<cnt; i++)
	{
		os<<m_distortionObj.fAmplitudes[i];
	}
	os<<m_distortionObj.fSpeed;
 
	return os;
}

const XIStream& PDistortionObject::unmarshal(const XIStream &os)
{ //Load
	int nVer;
	os >> nVer;

	if(nVer>Version_AmplitudeCurve) //版本3
	{
		os >> m_distortionObj.strImgFileName;
		os >> m_distortionObj.strLightFile;
		os>>m_bLightFlag;
	}
	else
	{
		unsigned int str_size = 0;
		wchar_t *str = NULL;

		os >> str_size;
		str = static_cast<wchar_t*>(XAlloc(str_size+2));
		ZeroMemory(str, str_size+2);
		os.pop_byte(str, str_size);
		m_distortionObj.strImgFileName = str;
		XFree(str);

		m_distortionObj.strLightFile = L"";
		m_bLightFlag = false;
	}

	int ntype;
	os>>ntype;
	m_distortionObj.nType = (XPDISTORTIONMESHTYPE)ntype;
	os>>m_distortionObj.fCycNum;
	m_distortionObj.fAmplitudes.clear();
	if(nVer < Version_AmplitudeCurve) //版本1
	{
		float fAmplitude;
		os>>fAmplitude;
		m_distortionObj.fAmplitudes.push_back(FPOINT(0.0f, fAmplitude));
		m_distortionObj.fAmplitudes.push_back(FPOINT(1.0f, fAmplitude));
	}
	else
	{
		int cnt = 0;
		os>>cnt;
		for(int i=0; i<cnt; i++)
		{
			FPOINT value;
			os>>value;
			m_distortionObj.fAmplitudes.push_back(value);
		}
	}
	os>>m_distortionObj.fSpeed;

	return os;
}


}
