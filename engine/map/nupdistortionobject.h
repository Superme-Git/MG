#ifndef __Nuclear_PDISTORTIONOBJECT_H__
#define __Nuclear_PDISTORTIONOBJECT_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nupobject.h"
#include "../common/nuxptypes.h"

namespace Nuclear
{
	class NuclearDistortionObject : public NuclearObject
	{
	public:
		enum eDISTORTIONVER
		{
			Distort_Version_01 = 1,
			Distort_Version_AmplitudeCurve = 2, //�����Ϊ�����߹ؼ�֡...
			Distort_Version_DisLight = 3,		//dis�ļ�������ӵƹ�,�ƹ������ڵ�һ����С��һ�����ϲ㡢ֻ��һ���ƹ⡢�����˶�����
			Distort_Version_NotMountElement = 4,//��mount element��

			Distort_CURVER = 5,
		};	

		struct sDISTORTIONMESHOBJ
		{
			std::wstring strImgFileName; //��ʽ��:/map/elements/test/abc.set
			std::wstring strLightFile;	 //ͬ��

			NuclearDistortionMeshType nType; //����..
			float fCycNum; //���������ҵĺ�������
			std::vector<NuclearFPoint> fAmplitudes; //���߹ؼ�֡...
			float fSpeed; //�ƶ��ٶ�...�ƶ� 2PI ��Ҫ��ʱ�� 1.0f---------100.0f ��λ����

			sDISTORTIONMESHOBJ(NuclearDistortionMeshType Type=XPDISTORTIONTYPE_HORIZONTAL_1, float CycNum=0.2f, float Amplitude=1.0f, float Speed=2.0f)
			{
				strImgFileName = L"";
				strLightFile = L"";

				nType = Type;
				fCycNum = CycNum; //����ȷ���������� (0.0f-2.0f)
				fAmplitudes.clear();
				fAmplitudes.push_back(NuclearFPoint(0.0f, Amplitude));
				fAmplitudes.push_back(NuclearFPoint(1.0f, Amplitude));
				fSpeed = Speed; //һ��������Ҫ��ʱ��...
			}
		};

	public:
		NuclearDistortionObject(void);
		~NuclearDistortionObject(void);

		NuclearOStream& marshal(NuclearOStream &) const;
		const INuclearStream& unmarshal(const INuclearStream &);
		sDISTORTIONMESHOBJ GetDistortionMeshObj() const { return m_DistortionObj; }
		bool GetLightFlag() const { return m_bLightFlag; }

	private:
		sDISTORTIONMESHOBJ m_DistortionObj;
		bool			   m_bLightFlag;
	};

}

#endif