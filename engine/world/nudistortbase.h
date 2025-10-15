#ifndef __Nuclear_distortbase_H__
#define __Nuclear_distortbase_H__
#include "../engine/nurenderable.h"

namespace Nuclear
{
	struct PicResrc;
	struct DistortImgInfo
	{
		PicResrc* pPicResrc;//��Ӧimg�е�һС��..
		NuclearRect rect; //����ڸ�ͼƬ���Ͻǵ�����..
		DistortImgInfo() : pPicResrc(NULL), rect() {}
		DistortImgInfo(PicResrc* pPic, const NuclearRect& rct) : pPicResrc(pPic), rect(rct) {}
	};

	typedef std::vector<DistortImgInfo> DistortImgInfoVetor;
	
	const int XPGRIDSIZE = 16; //Ť������Ĵ�С...
	struct DistortionEffectParam //���ӡ����ȵ�Ʈ��...
	{
		NuclearDistortionMeshType nType; //����..
		float fCycNum; //���������ҵĺ�������
		std::vector<NuclearFPoint> fAmplitudes;
		float fSpeed; //�ƶ��ٶ�...�ƶ� 2PI ��Ҫ��ʱ�� 1.0f----100.0f ��λ����

		float fPos; //�������ֵ��ȷ������λ��.. y = a*sin(cyc*x + fPos)
		DistortionEffectParam(NuclearDistortionMeshType Type=XPDISTORTIONTYPE_HORIZONTAL_1, float CycNum=0.2f, float Amplitude=1.0f, float Speed = 2.0f, float Pos=0.0f)
		{
			nType = Type;
			fCycNum = CycNum; //����ȷ���������� (0.0f----2.0f)
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

		DistortImgInfoVetor m_vectorLightImgInfo; //�汾3����

		NuclearRect m_rct; //��������
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
		bool Update(float fTime); //��
		virtual void setPos(const NuclearRect &rct){m_rct = rct; } //DistortionObj����λ��

		void setZoom(float fZoom) { m_fZoom = fZoom; } //DistortionObj�������ű���
		void setColor(DWORD color) { m_color = color; }

		//������ɫ���������ģʽ�Ȳ������뷽��
		void PushAnPic(PicResrc *pPic, const NuclearRect& rect){m_vectorImgInfo.push_back(DistortImgInfo(pPic, rect));}
		void PushLightPic(PicResrc *pPic, const NuclearRect& rect){m_vectorLightImgInfo.push_back(DistortImgInfo(pPic, rect));}
	
	};
}

#endif