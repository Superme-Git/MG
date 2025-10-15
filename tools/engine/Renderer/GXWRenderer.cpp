#include <windows.h>
#include <assert.h>
#include <string>
//STL
#include <vector>
#include <map>
#include <list>
#include <set>
#include <deque>
#include <hash_map>
#include "..\engine\common\util.h"
#include "GXWRenderer.h"
#include "..\engine\renderer\ifontmanager.h"
#include "..\engine\common\xml.h"

namespace GXWRENDER {

CGXWRenderer::CGXWRenderer(Nuclear::Renderer* renderer)
{
	m_renderer = renderer;
	assert(m_renderer);
	m_fontmgr = m_renderer->GetFontManager();
	assert(m_fontmgr);

	m_picdefault = m_renderer->LoadPictureFromNativePath(L"ui/_default.png");

	XMLIO::CFileReader xmlreader;
	if(xmlreader.OpenNativeFile(L"fontdef.xml") == XMLIO::EC_SUCCESS)
	{
		XMLIO::CINode root;
		xmlreader.GetRootNode(root);
		XMLIO::CNodeList nodelist, nodelist2;
		root.GetChildren(nodelist);
		for(XMLIO::CNodeList::iterator it = nodelist.begin(); it != nodelist.end(); ++it)
		{
			if(it->GetName() == L"FontDef")
			{
				it->GetChildren(nodelist2);
				for(XMLIO::CNodeList::iterator it = nodelist2.begin(); it != nodelist2.end(); ++it)
				{
					if(it->GetName() == L"fd")
					{
						int fontid = it->GetAttributeInteger(L"fonttype", -1);
						int fontsize = it->GetAttributeInteger(L"size", -1);
						std::wstring fontfile;
						it->GetAttribute(L"file", fontfile);
						if(fontid < 0 || fontsize < 0 || fontfile.empty())
							continue;
//						fontfile.insert(0, L"/bin/");
						m_fontmgr->AddFontType(fontid, fontfile.c_str(), fontsize);
					}
				}
				break;
			}
		}
	}

	GXWINDOW::GXWSetDefaultRenderer(this);
	GXWINDOW::CTextDrawAbstractImplement::Register(*this);
}

CGXWRenderer::~CGXWRenderer()
{
	for(std::map<int, Nuclear::ParticleSystemHandle>::iterator it = m_pshmap.begin(); it != m_pshmap.end(); ++it)
		m_renderer->GetParticleManager()->DeleteParticleSystem(it->second);
	for(std::map<int, Nuclear::PictureHandle>::iterator it = m_picmap.begin(); it != m_picmap.end(); ++it)
		m_renderer->FreePicture(it->second);
}

void CGXWRenderer::Line(int sx, int sy, int ex, int ey, GXWINDOW::COLOR color)
{
	Nuclear::FRECT fr((float)sx, (float)sy, (float)(sx + ex), (float)(sy + ey));
	m_renderer->DrawLine(fr, (DWORD)color);
}

void CGXWRenderer::Rect(int l, int t, int r, int b, GXWINDOW::COLOR color)
{
	Nuclear::FRECT fr((float)l, (float)t, (float)r, (float)b);
	m_renderer->DrawBox(fr, (DWORD)color, Nuclear::XPFM_WIREFRAME);
}

void CGXWRenderer::FillRect(int l, int t, int r, int b, GXWINDOW::COLOR color)
{
	Nuclear::FRECT fr((float)l, (float)t, (float)r, (float)b);
	m_renderer->DrawBox(fr, (DWORD)color, Nuclear::XPFM_SOLIDFILL);
}

void CGXWRenderer::DrawPicture(const GXWINDOW::ImageParams& ip, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh)
{
	DrawPicture(ip, dx, dy, dw, dh, sx, sy, sw, sh, false, false);
}

void CGXWRenderer::DrawPicture(const GXWINDOW::ImageParams& ip, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh, bool bxMirro, bool byMirro)
{
	Nuclear::PictureHandle	hpic;

	std::map<int, Nuclear::PictureHandle>::const_iterator it = m_picmap.find(ip.nBitmap);
	if(it != m_picmap.end())
		hpic = it->second;
	else
	{
		const wchar_t* fname = GXWINDOW::CFileNameImages::GetInstance().QueryFileName(ip.nBitmap);
		if(!fname)
			return;

		hpic = m_renderer->LoadPictureFromNativePath(fname);
		if(hpic == Nuclear::INVALID_PICTURE_HANDLE)
		{
			hpic = m_picdefault;
			if(hpic == Nuclear::INVALID_PICTURE_HANDLE)
				return;
		}

		m_picmap.insert(std::make_pair(ip.nBitmap, hpic));
	}

	Nuclear::PictureInfo pi;
	if(!m_renderer->GetPictureInfo(hpic, pi))
		return;
	float iw = 1.0f / (float)pi.m_nPicWidth;
	float ih = 1.0f / (float)pi.m_nPicHeight;
	Nuclear::DrawPictureParam param;
	Nuclear::FRECT rect, srcRect;
	param.colorCount = 1;
	param.pColor = reinterpret_cast<const Nuclear::XPCOLOR*>(&ip.color);
	param.handle = hpic;
	if(!bxMirro)
	{
		rect.left = static_cast<float>(dx);
		rect.top = static_cast<float>(dy);
		rect.right = static_cast<float>(dx + dw);
		rect.bottom = static_cast<float>(dy + dh);
	} else {
		rect.left = static_cast<float>(dx + dw);
		rect.top = static_cast<float>(dy + dh);
		rect.right = static_cast<float>(dx);
		rect.bottom = static_cast<float>(dy);
	}
	param.pRect = &rect;
	if(!byMirro)
	{
		srcRect.left = iw * (ip.x + sx);
		srcRect.top = ih * (ip.y + sy);
		srcRect.right = iw * (sx + ip.x + sw);
		srcRect.bottom = ih * (sy + ip.y + sh);
	} else {
		srcRect.left = iw * (sx + ip.x + sw);
		srcRect.top = ih * (sy + ip.y + sh);
		srcRect.right = iw * (ip.x + sx);
		srcRect.bottom = ih * (ip.y + sy);
	}
	param.pSrcrect = &srcRect;
	m_renderer->DrawPicture(param);
}

int CGXWRenderer::NewTextDraw(const wchar_t* text, int fontid, GXWINDOW::COLOR color, GXWINDOW::COLOR clrGradual, GXWINDOW::COLOR bkcolor, GXWINDOW::FONTEFEECTFLAG effect, GXWINDOW::COLOR effcolor)
{
	Nuclear::TextBlockHandle tbh = m_fontmgr->NewText(text, fontid, color, clrGradual, bkcolor, effect & GXWINDOW::FET_UNDERLINE);
	if(tbh != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::ITextBlock* itb = m_fontmgr->GetTextBlock(tbh);
		if(itb)
		{
			if(effect & GXWINDOW::FET_SHADOW)
				itb->SetShadowColor(effcolor);
			else if(effect & GXWINDOW::FET_STROKE)
				itb->SetStrokeColor(effcolor);
		}
	}

	return (int)tbh;
}

const wchar_t* CGXWRenderer::GetText(int n)
{
	Nuclear::ITextBlock* itb = m_fontmgr->GetTextBlock(n);
	if(itb)
	{
		const wchar_t* text = itb->GetText();
		if(text)
			return text;
	}
	return L"";
}

void CGXWRenderer::DeleteTextDraw(int n)
{
	m_fontmgr->ReleaseTextBlock(n);
}

void CGXWRenderer::Draw(int n, int l, int t, int cx, int cy, int cw, int ch, GXWINDOW::COLOR color, GXWINDOW::COLOR clrGradual)
{
	Nuclear::FRECT fr((float)cx, (float)cy, (float)(cx + cw), (float)(cy + ch));
	m_fontmgr->DrawText((Nuclear::TextBlockHandle)n, (float)l, (float)t, &fr);
}

int CGXWRenderer::GetCharPixelWidth(const wchar_t& wc, int fontid)
{
	return m_fontmgr->GetCharWidth(wc, fontid);
}

int CGXWRenderer::GetStringPixelWidth(const wchar_t* ws, int fontid)
{
	return m_fontmgr->GetStringWidth(ws, fontid);
}

int CGXWRenderer::GetStringPixelWidth(int n, int fontid)
{
	Nuclear::ITextBlock* itb = m_fontmgr->GetTextBlock((Nuclear::TextBlockHandle)n);
	if(!itb)
		return 0;

	return m_fontmgr->GetStringWidth(itb->GetText(), fontid);
}

int CGXWRenderer::GetFontHeight(int fontid)
{
	return m_fontmgr->GetFontTypeHeight(fontid);
}

int CGXWRenderer::GetFontHeightWithoutUnderline( int fontid)
{
	return m_fontmgr->GetFontTypeHeight( fontid) - m_fontmgr->GetFontTypeUnderlineThickness( fontid);
}

int CGXWRenderer::NewParticleEffect(const wchar_t* name)
{
	static int s_counter = 0;
	int id;
	do
	{
		id = s_counter++;
		if(s_counter < 0)
			s_counter = 0;
	}
	while(m_pshmap.find(id) != m_pshmap.end());

	Nuclear::PSParameter psp;
	psp.cycle = true;
	Nuclear::ParticleSystemHandle psh = m_renderer->GetParticleManager()->CreateParticleSystem(name, psp);
	if(psh == Nuclear::INVALID_PS_HANDLE)
		return -1;

	m_pshmap.insert(std::make_pair(id, psh));
	return id;
}

void CGXWRenderer::DrawParticleEffect(int n)
{
	std::map<int, Nuclear::ParticleSystemHandle>::iterator it = m_pshmap.find(n);
	if(it == m_pshmap.end())
		return;

	m_renderer->GetParticleManager()->RenderParticleSystem(it->second);
}

void CGXWRenderer::DeleteParticleEffect(int n)
{
	std::map<int, Nuclear::ParticleSystemHandle>::iterator it = m_pshmap.find(n);
	if(it == m_pshmap.end())
		return;

	Nuclear::ParticleSystemHandle psh = it->second;
	m_pshmap.erase(it);

	m_renderer->GetParticleManager()->DeleteParticleSystem(psh);
}

}
