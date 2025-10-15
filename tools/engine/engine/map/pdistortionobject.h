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
			Version_AmplitudeCurve = 2, //振幅改为存曲线关键帧...
			Version_DisLight = 3,		//dis文件允许添加灯光 --2010-10-19
										//灯光与主节点一样大小、一定在上层、只有一个灯光、共享运动参数
			CURVER = 3,
		};	

		struct DISTORTIONMESHOBJ
		{
			std::wstring strImgFileName;
			std::wstring strLightFile;	 //同上

			XPDISTORTIONMESHTYPE nType; //类型..
			float fCycNum; //控制正余弦的函数周期
		//	float fAmplitude;  //振幅..
			std::vector<FPOINT> fAmplitudes; //曲线关键帧...
			float fSpeed; //移动速度...移动 2PI 需要的时间 1.0f---------100.0f 单位：秒

			DISTORTIONMESHOBJ(XPDISTORTIONMESHTYPE Type=XPDISTORTIONTYPE_HORIZONTAL_1, 
								float CycNum=0.2f, float Amplitude=1.0f, float Speed=2.0f)
			{
				strImgFileName = L"";
				strLightFile = L"";

				nType = Type;
				fCycNum = CycNum; //用来确定函数周期 (0.0f------2.0f)
			//	fAmplitude = Amplitude; //与网格宽度相乘确定振幅 (0.0f--------5.0f)
				fAmplitudes.clear();
				fAmplitudes.push_back(FPOINT(0.0f, Amplitude));
				fAmplitudes.push_back(FPOINT(1.0f, Amplitude));
				fSpeed = Speed; //一个周期需要的时间...
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