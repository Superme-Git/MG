#pragma once

#include "..\engine\renderable.h"

namespace Nuclear
{
	struct PicResrc;
	struct DistortImgInfo
	{//��Ӧimg�е�һС��..
		PicResrc* pPicResrc;
		CRECT rect; //����ڸ�ͼƬ���Ͻǵ�����..
		DistortImgInfo() : pPicResrc(NULL), rect() {}
		DistortImgInfo(PicResrc* pPic, const CRECT& rct) : pPicResrc(pPic), rect(rct) {}
	};

	typedef std::vector<DistortImgInfo> DistortImgInfoVetor;
	
	const int XPGRIDSIZE = 16; //Ť������Ĵ�С...
	struct DistortionEffectParam //���ӡ����ȵ�Ʈ��...
	{
		XPDISTORTIONMESHTYPE nType; //����..
		float fCycNum; //���������ҵĺ�������
		std::vector<FPOINT> fAmplitudes;
		float fSpeed; //�ƶ��ٶ�...�ƶ� 2PI ��Ҫ��ʱ�� 1.0f---------100.0f ��λ����

		float fPos; //�������ֵ��ȷ������λ��.. y = a*sin(cyc*x + fPos)
		//ÿ��update-----fPos += fDetaTime*2*PI/fSpeed; --------fpos -= 2*PI;

		DistortionEffectParam(XPDISTORTIONMESHTYPE Type=XPDISTORTIONTYPE_HORIZONTAL_1, 
			float CycNum=0.2f, float Amplitude=1.0f, float Speed = 2.0f, float Pos=0.0f)
		{
			nType = Type;
			fCycNum = CycNum; //����ȷ���������� (0.0f------2.0f)
			//	fAmplitude = Amplitude; //�����������ȷ����� (0.0f--------5.0f)
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

		DistortImgInfoVetor m_vectorLightImgInfo; //�汾3����

		CRECT m_rct; //��������
		float m_fZoom;
		DWORD m_color;

	public:
		DistortBase(EngineBase* pEB, const DistortionEffectParam& effectParam);
		virtual ~DistortBase(void);

		virtual bool Render(Canvas* canvas);
		bool Update(float fTime); //��
		virtual void setPos(const CRECT &rct)
		{
			m_rct = rct; 
		} //DistortionObj����λ��

		void setZoom(float fZoom) { m_fZoom = fZoom; } //DistortionObj�������ű���
		void setColor(DWORD color) { m_color = color; }
		//������ɫ���������ģʽ�Ȳ������뷽��...

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
