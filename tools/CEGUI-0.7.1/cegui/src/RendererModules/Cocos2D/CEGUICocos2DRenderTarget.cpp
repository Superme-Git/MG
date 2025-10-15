#define NOMINMAX
#include "CEGUICocos2DRenderTarget.h"

#include <CEGUIRenderQueue.h>
#include <CEGUIExceptions.h>
#include <CCGL.h>
#include <kazmath/kazmath.h>
#include <kazmath/GL/matrix.h>

#include "CEGUICocos2DGeometryBuffer.h"
#include "CEGUISystem.h"

namespace CEGUI
{
Cocos2DRenderTarget::Cocos2DRenderTarget(Cocos2DRenderer& owner) :
    d_owner(owner),
    d_area(0, 0, 0, 0),
    d_matrixValid(false)
{
}

void Cocos2DRenderTarget::draw(const GeometryBuffer* buffer)
{
    buffer->draw();
}

void Cocos2DRenderTarget::draw(const RenderQueue& queue)
{
    queue.draw();
}

void Cocos2DRenderTarget::setArea(const Rect& area)
{
    d_area = area;
    d_matrixValid = false;
}

const Rect& Cocos2DRenderTarget::getArea() const
{
    return d_area;
}

//----------------------------------------------------------------------------//
void Cocos2DRenderTarget::activate()
{
	if (!d_matrixValid)
		updateMatrix();
	
//	glViewport(d_area.d_left, d_area.d_top, d_area.getWidth(), d_area.getHeight());
    
    
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	int x = System::getSingleton().GetAdapter()->GetDisplayOffsetX();
    int y = System::getSingleton().GetAdapter()->GetDisplayOffsetY();
	glViewport(x, y,
              // ,
			  System::getSingleton().GetAdapter()->GetDisplayWidth(),
			  System::getSingleton().GetAdapter()->GetDisplayHeight()
			   );
	//kmGLLoadMatrix(&d_matrix);
	//kmGLRotatef(90, 0, 0, 1);
	/*
	float fWidth = System::getSingleton().GetAdapter()->GetDisplayWidth();
	float fHeight = System::getSingleton().GetAdapter()->GetDisplayHeight();
	float fOff = (fWidth - fHeight)*0.5f;
	kmMat4 matTrans;
	kmGLMatrixMode(KM_GL_PROJECTION);
    kmGLPushMatrix();	
	kmGLLoadIdentity();
	kmGLTranslatef(-fOff, fOff, 0.f);
	kmGLGetMatrix(KM_GL_PROJECTION, &matTrans);*/
	
	kmGLMatrixMode(KM_GL_PROJECTION);
	kmMat4 matrix;
	kmMat4Multiply(&matrix, cocos2d::CCEGLView::sharedOpenGLView()->getOrientationMatrix(), &d_matrix);
	//kmMat4Multiply(&matrix, &matrix, &matTrans);
	kmGLLoadMatrix(&matrix);
#else
	int x = System::getSingleton().GetAdapter()->GetDisplayOffsetX();
    int y = System::getSingleton().GetAdapter()->GetDisplayOffsetY();
	glViewport(x, y,
              // ,
			  System::getSingleton().GetAdapter()->GetDisplayWidth(),
			  System::getSingleton().GetAdapter()->GetDisplayHeight()
			   );
	kmGLMatrixMode(KM_GL_PROJECTION);
	kmGLLoadMatrix(&d_matrix);
#endif
	return;
}

void Cocos2DRenderTarget::deactivate()
{
}

//----------------------------------------------------------------------------//
void Cocos2DRenderTarget::unprojectPoint(const GeometryBuffer* buffer,
                                           const Vector2& p_in,
                                           Vector2& p_out) const
{
	
    if (!d_matrixValid)
        updateMatrix();
	
    p_out = p_in;
    return;
	assert("unimplement" && false);
 //   const Cocos2DGeometryBuffer& gb =
 //       static_cast<const Cocos2DGeometryBuffer&>(buff);

	//const GLint vp[4] = {
	//	static_cast<GLint>(d_area.d_left),
	//	static_cast<GLint>(d_area.d_top),
	//	static_cast<GLint>(d_area.getWidth()),
	//	static_cast<GLint>(d_area.getHeight())
	//};

	//double in_x, in_y, in_z = 0.0;

	//// unproject the ends of the ray
	//double r1_x, r1_y, r1_z;
	//double r2_x, r2_y, r2_z;
	//in_x = vp[2] * 0.5;
	//in_y = vp[3] * 0.5;
	//in_z = -d_viewDistance;
	//gluUnProject(in_x, in_y, in_z, gb.getMatrix(), d_matrix, vp,
	//	&r1_x, &r1_y, &r1_z);
	//in_x = p_in.d_x;
	//in_y = vp[3] - p_in.d_y;
	//in_z = 0.0;
	//gluUnProject(in_x, in_y, in_z, gb.getMatrix(), d_matrix, vp,
	//	&r2_x, &r2_y, &r2_z);

	//// project points to orientate them with GeometryBuffer plane
	//GLdouble p1_x, p1_y, p1_z;
	//GLdouble p2_x, p2_y, p2_z;
	//GLdouble p3_x, p3_y, p3_z;
	//in_x = 0.0;
	//in_y = 0.0;
	//gluProject(in_x, in_y, in_z, gb.getMatrix(), d_matrix, vp,
	//	&p1_x, &p1_y, &p1_z);
	//in_x = 1.0;
	//in_y = 0.0;
	//gluProject(in_x, in_y, in_z, gb.getMatrix(), d_matrix, vp,
	//	&p2_x, &p2_y, &p2_z);
	//in_x = 0.0;
	//in_y = 1.0;
	//gluProject(in_x, in_y, in_z, gb.getMatrix(), d_matrix, vp,
	//	&p3_x, &p3_y, &p3_z);

	//// calculate vectors for generating the plane
	//const double pv1_x = p2_x - p1_x;
	//const double pv1_y = p2_y - p1_y;
	//const double pv1_z = p2_z - p1_z;
	//const double pv2_x = p3_x - p1_x;
	//const double pv2_y = p3_y - p1_y;
	//const double pv2_z = p3_z - p1_z;
	//// given the vectors, calculate the plane normal
	//const double pn_x = pv1_y * pv2_z - pv1_z * pv2_y;
	//const double pn_y = pv1_z * pv2_x - pv1_x * pv2_z;
	//const double pn_z = pv1_x * pv2_y - pv1_y * pv2_x;
	//// calculate plane
	//const double pn_len = std::sqrt(pn_x * pn_x + pn_y * pn_y + pn_z * pn_z);
	//const double pl_a = pn_x / pn_len;
	//const double pl_b = pn_y / pn_len;
	//const double pl_c = pn_z / pn_len;
	//const double pl_d = -(p1_x * pl_a + p1_y * pl_b + p1_z * pl_c);
	//// calculate vector of picking ray
	//const double rv_x = r1_x - r2_x;
	//const double rv_y = r1_y - r2_y;
	//const double rv_z = r1_z - r2_z;
	//// calculate intersection of ray and plane
	//const double pn_dot_r1 = (r1_x * pn_x + r1_y * pn_y + r1_z * pn_z);
	//const double pn_dot_rv = (rv_x * pn_x + rv_y * pn_y + rv_z * pn_z);
	//const double tmp1 = pn_dot_rv != 0.0 ? (pn_dot_r1 + pl_d) / pn_dot_rv : 0.0;
	//const double is_x = r1_x - rv_x * tmp1;
	//const double is_y = r1_y - rv_y * tmp1;

	//p_out.d_x = static_cast<float>(is_x);
	//p_out.d_y = static_cast<float>(is_y);
}

//----------------------------------------------------------------------------//
void Cocos2DRenderTarget::updateMatrix() const
{
	const float w = d_area.getWidth();
	const float h = d_area.getHeight();
	const float aspect = w / h;
	const float midx = w * 0.5;
	const float midy = h * 0.5;
	d_viewDistance = midx / (aspect * 0.267949192431123f);
    
	kmGLMatrixMode(KM_GL_PROJECTION);
    kmGLPushMatrix();
	
	kmGLLoadIdentity();
    kmMat4 mProj;
    kmMat4PerspectiveProjection(&mProj, 30.0f, aspect, d_viewDistance * 0.5f, d_viewDistance * 2.0f);
    kmGLMultMatrix(&mProj);
    kmVec3 eye, center, up;
    kmVec3Fill(&eye, midx, midy, -d_viewDistance);
    kmVec3Fill(&center, midx, midy, 1.f);
    kmVec3Fill(&up, 0.f, -1.f, 0.f);
    kmMat4LookAt(&mProj, &eye, &center, &up);
    kmGLMultMatrix(&mProj);
	
    kmGLGetMatrix(KM_GL_PROJECTION, &d_matrix);
	kmGLPopMatrix();

	d_matrixValid = true;
}

void Cocos2DRenderTarget::Reset()
{
    d_matrixValid = false;
    activate();
}

} // End of  CEGUI namespace section
