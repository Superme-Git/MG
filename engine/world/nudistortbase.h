#ifndef __Nuclear_distortbase_H__
#define __Nuclear_distortbase_H__
#include "../engine/nurenderable.h"

namespace Nuclear
{
	struct PicResrc;
	struct DistortImgInfo
	{
		PicResrc* pPicResrc;//对应img中的一小块..
		NuclearRect rect; //相对于该图片左上角的坐标..
		DistortImgInfo() : pPicResrc(NULL), rect() {}
		DistortImgInfo(PicResrc* pPic, const NuclearRect& rct) : pPicResrc(pPic), rect(rct) {}
	};

	typedef std::vector<DistortImgInfo> DistortImgInfoVetor;
	
	const int XPGRIDSIZE = 16; //扭曲网格的大小...
	struct DistortionEffectParam //旗子、布等的飘动...
	{
		NuclearDistortionMeshType nType; //类型..
		float fCycNum; //控制正余弦的函数周期
		std::vector<NuclearFPoint> fAmplitudes;
		float fSpeed; //移动速度...移动 2PI 需要的时间 1.0f----100.0f 单位：秒

		float fPos; //根据这个值来确定函数位置.. y = a*sin(cyc*x + fPos)
		DistortionEffectParam(NuclearDistortionMeshType Type=XPDISTORTIONTYPE_HORIZONTAL_1, float CycNum=0.2f, float Amplitude=1.0f, float Speed = 2.0f, float Pos=0.0f)
		{
			nType = Type;
			fCycNum = CycNum; //用来确定函数周期 (0.0f----2.0f)
			fAmplitudes.clear();
			fAmplitudes.push_back(NuclearFPoint(0.0F, Amplitude));
			fAmplitudes.push_back(NuclearFPoint(1.0F, Amplitude));

			fSpeed = Speed;
			fPos = Pos;
		}
	};

	class DistortBase :	public Renderable
	{
	private:
		static NuclearTCT1Vertex s_vertexs[1089]; //33*33
		static WORD s_index[6144]; // 32*32*6

	protected:
		DistortionEffectParam m_effectParam;
		DistortImgInfoVetor m_vectorImgInfo;

		DistortImgInfoVetor m_vectorLightImgInfo; //版本3增加

		NuclearRect m_rct; //世界坐标
		float m_fZoom;
		DWORD m_color;

	protected:
		bool render(Canvas* canvas, bool isAlpha, bool realRender);

	public:
		DistortBase(EngineBase* pEB, const DistortionEffectParam& effectParam);
		virtual ~DistortBase(void);

		virtual bool RenderBottom(Canvas* canvas) { return true; }

		virtual bool RenderMid(Canvas* canvas, bool realRender) { return render(canvas, true, realRender); }
		virtual void RenderBeforeMid(bool realRender) { render(NULL, false, realRender); }
		virtual bool Render(Canvas* canvas, bool realRender) { return render(canvas, false, realRender); }
		bool Update(float fTime); //秒
		virtual void setPos(const NuclearRect &rct){m_rct = rct; } //DistortionObj绘制位置

		void setZoom(float fZoom) { m_fZoom = fZoom; } //DistortionObj绘制缩放比例
		void setColor(DWORD color) { m_color = color; }

		//顶点颜色、所需绘制模式等参数传入方法
		void PushAnPic(PicResrc *pPic, const NuclearRect& rect){m_vectorImgInfo.push_back(DistortImgInfo(pPic, rect));}
		void PushLightPic(PicResrc *pPic, const NuclearRect& rect){m_vectorLightImgInfo.push_back(DistortImgInfo(pPic, rect));}
	
	};
}

#endif