#pragma once
#include "..\common\pobject.h"
#include "iparticlesyscommon.h"

namespace Nuclear
{
	class CPsParamObject : public PObject
	{
	public:
		CPsParamObject(void);
		virtual ~CPsParamObject(void);

		enum PSPARAMVER
		{
			VERSION01 = 1, 
			//版本1记录内容：发射器旋转角度、线发射长度、矩形发射宽度、高度、
			//盲区、阻挡线、反射线
			//暂时就记录以上几个参数..
			CURVER = 1,
		};	

		struct PSPARAM
		{
			//版本1中存的参数---所有参数与PSModifyParam结构中的同名参数对应----以下
			float m_fEmitterSpinAngle;//发射器旋转角度--弧度为单位---顺时针为正--
			float m_fLineLength;//线发射的线长度//只考虑水平方向的线...			
			float m_fRctWidth;//矩形的长和宽
			float m_fRctHeight;
			std::vector<CRECT> m_vectorRctBlindArea;//粒子盲区
			std::vector<LINE> m_vectorBlockLine;//阻挡线..
			std::vector<LINE> m_vectorReflectLine;//反射线...
			//以上

			PSPARAM()
			{
				m_fEmitterSpinAngle = 0.0f;
				m_fLineLength = -1.0f; //这三个参数如果小于0，则不改变美术资源参数
				m_fRctWidth = -1.0f;
				m_fRctHeight = -1.0f;
				m_vectorRctBlindArea.clear();
				m_vectorBlockLine.clear();
				m_vectorReflectLine.clear();
			}
		};

	public:
		const PSPARAM& GetPsParam() const { return m_psParam; }
		void SetPsParam(const PSPARAM& psParam) { m_psParam = psParam; } //编辑器专用的

		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);

	private:
		PSPARAM m_psParam;

	private:
		CPsParamObject(const CPsParamObject&);
		CPsParamObject& operator=(const CPsParamObject&);
	};
}
