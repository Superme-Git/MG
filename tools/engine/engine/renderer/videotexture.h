
#ifndef __VIDEOTEXTURE_H__
#define __VIDEOTEXTURE_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <d3dx9.h>
#include <d3d9.h>

#include "..\dshowbaseclasses\streams.h"
#include "..\common\FileIOManager.h"

namespace Nuclear
{
//	class CMemReader;
	class CTextureRenderer;
	class DX9Renderer;
	class VideoTexture 
	{
//		friend class CMemReader;

		friend class CTextureRenderer;
		friend class DX9Renderer;
	public:
		VideoTexture(LPDIRECT3DDEVICE9 pd3dDevice); //, FileIOManager* pFileIOMan);
		~VideoTexture();

	public:
		void pauseMovie();
		void playMovie();
		void rePlayMovie();
		void stopMovie();
		bool isPlayingMovie();
		void SetSpeed(float NewRate);		//�����ٶ� (1.0 = normal rate.)
		void getMovieDimensions(int& nwidth, int& nheight);
		void SetLoop(bool bFlag);
		void HoldLastFrame();
		float GetTotalLength(); //��
		void SetPosition(float ftime); //��

	private:
		bool init(std::wstring filename, bool bTextureCaps_pow2);
		void Loop(); //�Ƿ����ļ�β..ѭ��
		void setTexture();	 //��������
		HRESULT Restore();		//�ؽ���Դ
		HRESULT ReleaseVideoTexture(); //�ͷ���Դ...

		bool DelTempFile(); 
		void setMovieDimensions(int nwidth, int nheight);
		HRESULT InitGraph(std::wstring filename);
		void CleanupDShow();
		float GetPosition();//����λ�� (between 0.0 and 1.0) 
		void Seek(float AbsolutePosition);	//�豸��ʧ����

		void CleanTextureContents();

	private:
		CComPtr<IGraphBuilder>  m_pGB;
		CComPtr<IMediaControl>  m_pMC;
		CComPtr<IMediaPosition> m_pMP;
		CComPtr<IMediaEvent>    m_pME;
		CComPtr<IMediaSeeking>  m_pMS;

		bool m_bTextureCaps_pow2; //2��

		LPDIRECT3DDEVICE9 m_pd3dDevice;		//��renderer����Ĳ���
	//	FileIOManager* m_pFileIOMan;
		LPDIRECT3DTEXTURE9 m_VideoD3DTexture;
		LPDIRECT3DTEXTURE9 m_VideoD3DTextureDes;

		//��ʵ��������
		int m_nTexWidth;
		int m_nTexHeight;

		int m_nVideoWidth;   //��Ƶ��
		int m_nVideoHeight;  //��Ƶ��
		float m_fPosTime; //�豸��ʧʱ��ͣ��

		bool m_bLoop; //�Ƿ�ѭ������
	};
};

#endif //__VIDEOTEXTURE_H__