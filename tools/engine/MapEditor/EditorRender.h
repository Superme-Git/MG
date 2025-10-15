#pragma once
#include "..\engine\renderer\dx9renderer.h"

class CEditorRender :
	public Nuclear::DX9Renderer
{
private:
	Nuclear::PictureHandle m_ChunkHandle;
	bool m_bDrawLayerChunk;

public:
	virtual bool Begin(Nuclear::XPCOLOR color = 0xc090)
	{
		if (!m_bDrawLayerChunk)
			return __super::Begin(color);
		return __super::Begin(0x00000000);
	}
	virtual void SetTextureColorBlendType(Nuclear::XPTEXCOLORBLEND_TYPE colorop)
	{
		if (!m_bDrawLayerChunk)
			__super::SetTextureColorBlendType(colorop);
		else
			__super::SetTextureColorBlendType(Nuclear::XPTOP_SELECTARG1);
	}

	virtual void SetAlphaBlendDstType(Nuclear::XPALPHABLEND_TYPE dstblend)
	{
		if (!m_bDrawLayerChunk)
			__super::SetAlphaBlendDstType(dstblend);
		else
			__super::SetAlphaBlendDstType(Nuclear::XPBLEND_ONE);
	}

	virtual void SetAlphaBlendSrcType(Nuclear::XPALPHABLEND_TYPE srcblend)
	{
		if (!m_bDrawLayerChunk)
			__super::SetAlphaBlendSrcType(srcblend);
		else
			__super::SetAlphaBlendSrcType(Nuclear::XPBLEND_SRCALPHA);
	}

	virtual void SetAlphaBlendEnable(bool f)
	{
		if (!m_bDrawLayerChunk)
			__super::SetAlphaBlendEnable(f);
		else
			__super::SetAlphaBlendEnable(true);
	}

	virtual bool DrawParticles(Nuclear::PictureHandle handle, const Nuclear::UTCT1VERTEX *vertices, int nVertexNum/*顶点个数*/)
	{
		if (!m_bDrawLayerChunk)
			return __super::DrawParticles(handle, vertices, nVertexNum);
		return __super::DrawParticles(m_ChunkHandle, vertices, nVertexNum);
	}

	virtual bool DrawParticles(Nuclear::PictureHandle handle, const Nuclear::TCT1VERTEX *vertices, int nVertexNum/*顶点个数*/)
	{
		if (!m_bDrawLayerChunk)
			return __super::DrawParticles(handle, vertices, nVertexNum);
		return __super::DrawParticles(m_ChunkHandle, vertices, nVertexNum);
	}

	virtual bool DrawTriangleFan(Nuclear::PictureHandle handle, const Nuclear::TCT1VERTEX *vertices, int nVertexNum/*顶点个数*/)
	{
		if (!m_bDrawLayerChunk)
			return __super::DrawTriangleFan(handle, vertices, nVertexNum);
		return __super::DrawTriangleFan(m_ChunkHandle, vertices, nVertexNum);
	}

	virtual bool DrawBatchPicture(const Nuclear::DrawBatchPictureParam &param)
	{
		if (!m_bDrawLayerChunk) 
			return __super::DrawBatchPicture(param);
		Nuclear::DrawBatchPictureParam par = param;
		par.handle = m_ChunkHandle;
		par.colorCount = 1;
		Nuclear::XPCOLOR color = 0xFFFFFFFF;
		par.pColor = &color;
		par.blend = Nuclear::XPTEXBM_ADDITIVE;
		return __super::DrawBatchPicture(par);
	}

	bool LoadChunkPicture(const std::wstring imgpath)
	{
		m_ChunkHandle = LoadPicture(imgpath);
		return (m_ChunkHandle != Nuclear::INVALID_PICTURE_HANDLE);
	}

	bool EnableLayerChunk(bool block = true)
	{
		if (block && (m_ChunkHandle == Nuclear::INVALID_PICTURE_HANDLE))
		{
			m_bDrawLayerChunk = false;
			return false;
		}
		m_bDrawLayerChunk = block;
		return true;
	}

	bool GetLayerChunkState() const { return m_bDrawLayerChunk; }
	virtual ~CEditorRender(void);

	CEditorRender(Nuclear::CFileIOManager *pFileIOMan);

};



void DestroyEditorRenderer(Nuclear::Renderer *r);
bool CreateEditorRenderer(Nuclear::Renderer **ppr, HWND hwnd, const Nuclear::XDisplayMode &dmode, Nuclear::CFileIOManager *pFileIOMan, DWORD flags = 0);