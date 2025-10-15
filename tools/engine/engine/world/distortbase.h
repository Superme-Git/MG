#pragma once

#include "..\engine\renderable.h"

namespace Nuclear
{
	struct PicResrc;
	struct DistortImgInfo
	{//对应img中的一小块..
		PicResrc* pPicResrc;
		CRECT rect; //相对于该图片左上角的坐标..
		DistortImgInfo() : pPicResrc(NULL), rect() {}
		DistortImgInfo(PicResrc* pPic, const CRECT& rct) : pPicResrc(pPic), rect(rct) {}
	};

	typedef std::vector<DistortImgInfo> DistortImgInfoVetor;
	
	const int XPGRIDSIZE = 16; //扭曲网格的大小...
	struct DistortionEffectParam //旗子、布等的飘动...
	{
		XPDISTORTIONMESHTYPE nType; //类型..
		float fCycNum; //控制正余弦的函数周期
		std::vector<FPOINT> fAmplitudes;
		float fSpeed; //移动速度...移动 2PI 需要的时间 1.0f---------100.0f 单位：秒

		float fPos; //根据这个值来确定函数位置.. y = a*sin(cyc*x + fPos)
		//每次update-----fPos += fDetaTime*2*PI/fSpeed; --------fpos -= 2*PI;

		DistortionEffectParam(XPDISTORTIONMESHTYPE Type=XPDISTORTIONTYPE_HORIZONTAL_1, 
			float CycNum=0.2f, float Amplitude=1.0f, float Speed = 2.0f, float Pos=0.0f)
		{
			nType = Type;
			fCycNum = CycNum; //用来确定函数周期 (0.0f------2.0f)
			//	fAmplitude = Amplitude; //与网格宽度相乘确定振幅 (0.0f--------5.0f)
			fAmplitudes.clear();
			fAmplitudes.push_back(FPOINT(0.0F, Amplitude));
			fAmplitudes.push_back(FPOINT(1.0F, Amplitude));

			fSpeed = Speed;
			fPos = Pos;
		}
	};

	class DistortBase :	public Renderable
	{
	private:
		static TCT1VERTEX s_vertexs[1089]; //33*33
		static WORD s_index[6144]; // 32*32 *6

	protected:
		DistortionEffectParam m_effectParam;
		DistortImgInfoVetor m_vectorImgInfo;

		DistortImgInfoVetor m_vectorLightImgInfo; //版本3增加

		CRECT m_rct; //世界坐标
		float m_fZoom;
		DWORD m_color;

	public:
		DistortBase(EngineBase* pEB, const DistortionEffectParam& effectParam);
		virtual ~DistortBase(void);

		virtual bool Render(Canvas* canvas);
		bool Update(float fTime); //秒
		virtual void setPos(const CRECT &rct)
		{
			m_rct = rct; 
		} //DistortionObj绘制位置

		void setZoom(float fZoom) { m_fZoom = fZoom; } //DistortionObj绘制缩放比例
		void setColor(DWORD color) { m_color = color; }
		//顶点颜色、所需绘制模式等参数传入方法...

		void PushAnPic(PicResrc *pPic, const CRECT& rect)
		{
			m_vectorImgInfo.push_back(DistortImgInfo(pPic, rect));
		}

		void PushLightPic(PicResrc *pPic, const CRECT& rect)
		{
			m_vectorLightImgInfo.push_back(DistortImgInfo(pPic, rect));
		}
	
	};
};
