#ifndef __Nuclear_PDISTORTIONOBJECT_H
#define __Nuclear_PDISTORTIONOBJECT_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\pobject.h"
#include "..\common\xptypes.h"

namespace Nuclear
{
	class PDistortionObject :	public PObject
	{
	public:
		PDistortionObject(void);
		~PDistortionObject(void);

	public:
		enum DISTORTIONVER
		{
			Version_01 = 1,
			Version_AmplitudeCurve = 2, //�����Ϊ�����߹ؼ�֡...
			Version_DisLight = 3,		//dis�ļ�������ӵƹ� --2010-10-19
										//�ƹ������ڵ�һ����С��һ�����ϲ㡢ֻ��һ���ƹ⡢�����˶�����
			CURVER = 3,
		};	

		struct DISTORTIONMESHOBJ
		{
			std::wstring strImgFileName;
			std::wstring strLightFile;	 //ͬ��

			XPDISTORTIONMESHTYPE nType; //����..
			float fCycNum; //���������ҵĺ�������
		//	float fAmplitude;  //���..
			std::vector<FPOINT> fAmplitudes; //���߹ؼ�֡...
			float fSpeed; //�ƶ��ٶ�...�ƶ� 2PI ��Ҫ��ʱ�� 1.0f---------100.0f ��λ����

			DISTORTIONMESHOBJ(XPDISTORTIONMESHTYPE Type=XPDISTORTIONTYPE_HORIZONTAL_1, 
								float CycNum=0.2f, float Amplitude=1.0f, float Speed=2.0f)
			{
				strImgFileName = L"";
				strLightFile = L"";

				nType = Type;
				fCycNum = CycNum; //����ȷ���������� (0.0f------2.0f)
			//	fAmplitude = Amplitude; //�����������ȷ����� (0.0f--------5.0f)
				fAmplitudes.clear();
				fAmplitudes.push_back(FPOINT(0.0f, Amplitude));
				fAmplitudes.push_back(FPOINT(1.0f, Amplitude));
				fSpeed = Speed; //һ��������Ҫ��ʱ��...
			}
		};

	public:
		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);

	private:
	public:
		DISTORTIONMESHOBJ m_distortionObj;
		bool m_bLightFlag;
	};

}

#endif