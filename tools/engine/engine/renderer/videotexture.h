
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
		void SetSpeed(float NewRate);		//播放速度 (1.0 = normal rate.)
		void getMovieDimensions(int& nwidth, int& nheight);
		void SetLoop(bool bFlag);
		void HoldLastFrame();
		float GetTotalLength(); //秒
		void SetPosition(float ftime); //秒

	private:
		bool init(std::wstring filename, bool bTextureCaps_pow2);
		void Loop(); //是否到了文件尾..循环
		void setTexture();	 //设置纹理
		HRESULT Restore();		//重建资源
		HRESULT ReleaseVideoTexture(); //释放资源...

		bool DelTempFile(); 
		void setMovieDimensions(int nwidth, int nheight);
		HRESULT InitGraph(std::wstring filename);
		void CleanupDShow();
		float GetPosition();//播放位置 (between 0.0 and 1.0) 
		void Seek(float AbsolutePosition);	//设备丢失处理

		void CleanTextureContents();

	private:
		CComPtr<IGraphBuilder>  m_pGB;
		CComPtr<IMediaControl>  m_pMC;
		CComPtr<IMediaPosition> m_pMP;
		CComPtr<IMediaEvent>    m_pME;
		CComPtr<IMediaSeeking>  m_pMS;

		bool m_bTextureCaps_pow2; //2幂

		LPDIRECT3DDEVICE9 m_pd3dDevice;		//从renderer传入的参数
	//	FileIOManager* m_pFileIOMan;
		LPDIRECT3DTEXTURE9 m_VideoD3DTexture;
		LPDIRECT3DTEXTURE9 m_VideoD3DTextureDes;

		//真实的纹理宽高
		int m_nTexWidth;
		int m_nTexHeight;

		int m_nVideoWidth;   //视频宽
		int m_nVideoHeight;  //视频高
		float m_fPosTime; //设备丢失时暂停点

		bool m_bLoop; //是否循环播放
	};
};

#endif //__VIDEOTEXTURE_H__