/***********************************************************************
filename: 	XPRenderEffect.h
purpose:	小胖引擎的effect与UI特效的结合接口
*************************************************************************/
#pragma once
#include "CEGUIRenderEffect.h"


class XPRenderEffect : public CEGUI::RenderEffect
{
public: 
	XPRenderEffect();
	virtual ~XPRenderEffect();

public:
	void setType(int type);
	void setClip(bool clip);
	virtual void setScissor(int x, int y, int w, int h);
	virtual bool update(const float elapsed, CEGUI::RenderingWindow& window);
	virtual bool realiseGeometry(CEGUI::RenderingWindow& window, CEGUI::GeometryBuffer& geometry);
	virtual void performPostRenderFunctions();
	virtual void performPreRenderFunctions(const int pass);
	virtual int getPassCount() const;

public:
	int  m_iType;   //1 effect, 2 sprite
	bool m_bClip;
	int scissor_w;
	int scissor_h;
	int scissor_x;
	int scissor_y;

};
