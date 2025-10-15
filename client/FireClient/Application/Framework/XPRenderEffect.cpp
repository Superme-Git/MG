#include "stdafx.h"
#include "XPRenderEffect.h"
#include "GameUIManager.h"

XPRenderEffect::XPRenderEffect()
	:m_bClip(false)
{ }

XPRenderEffect::~XPRenderEffect()
{ }

void XPRenderEffect::setType(int type)
{
	m_iType = type;
}

void XPRenderEffect::setClip(bool clip)
{
	m_bClip = clip;
}

void XPRenderEffect::setScissor(int x, int y, int w, int h)
{
	scissor_x = x;
	scissor_y = y;
	scissor_w = w;
	scissor_h = h;
}

bool XPRenderEffect::update(const float elapsed, CEGUI::RenderingWindow& window)
{
	return true;
}

bool XPRenderEffect::realiseGeometry(CEGUI::RenderingWindow& window, CEGUI::GeometryBuffer& geometry)
{
	return true;
}

void XPRenderEffect::performPostRenderFunctions()
{
	if (1 == m_iType) {
		gGetGameUIManager()->RenderUIEffect(this);
	}
	else if (2 == m_iType) {
		gGetGameUIManager()->RenderWindowSprite(this);
	}
}

void XPRenderEffect::performPreRenderFunctions(const int pass)
{
}

int XPRenderEffect::getPassCount() const
{
	return 1;
}
