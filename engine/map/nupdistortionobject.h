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
			Distort_Version_AmplitudeCurve = 2, //振幅改为存曲线关键帧...
			Distort_Version_DisLight = 3,		//dis文件允许添加灯光,灯光与主节点一样大小、一定在上层、只有一个灯光、共享运动参数
			Distort_Version_NotMountElement = 4,//不mount element了

			Distort_CURVER = 5,
		};	

		struct sDISTORTIONMESHOBJ
		{
			std::wstring strImgFileName; //格式如:/map/elements/test/abc.set
			std::wstring strLightFile;	 //同上

			NuclearDistortionMeshType nType; //类型..
			float fCycNum; //控制正余弦的函数周期
			std::vector<NuclearFPoint> fAmplitudes; //曲线关键帧...
			float fSpeed; //移动速度...移动 2PI 需要的时间 1.0f---------100.0f 单位：秒

			sDISTORTIONMESHOBJ(NuclearDistortionMeshType Type=XPDISTORTIONTYPE_HORIZONTAL_1, float CycNum=0.2f, float Amplitude=1.0f, float Speed=2.0f)
			{
				strImgFileName = L"";
				strLightFile = L"";

				nType = Type;
				fCycNum = CycNum; //用来确定函数周期 (0.0f-2.0f)
				fAmplitudes.clear();
				fAmplitudes.push_back(NuclearFPoint(0.0f, Amplitude));
				fAmplitudes.push_back(NuclearFPoint(1.0f, Amplitude));
				fSpeed = Speed; //一个周期需要的时间...
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