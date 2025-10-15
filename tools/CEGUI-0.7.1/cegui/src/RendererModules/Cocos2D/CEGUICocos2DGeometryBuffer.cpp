#define NOMINMAX
#include "CEGUICocos2DGeometryBuffer.h"

#include "CEGUISystem.h"
#include "CEGUIVertex.h"
#include "CEGUIRenderEffect.h"

#include "CEGUICocos2DTexture.h"
#include "CEGUICocos2DRenderer.h"

// Start of CEGUI namespace section
namespace CEGUI
{
//----------------------------------------------------------------------------//
	Cocos2DGeometryBuffer::Cocos2DGeometryBuffer() :
    d_activeTexture(0),
    d_translation(0, 0, 0),
    d_rotation(0, 0, 0),
	d_scale(1, 1, 1),
    d_pivot(0, 0, 0),
    d_effect(0),
    d_matrixValid(false),
	d_TargetSurface(0),
	d_WidthClip(1.0f),
	d_RenderSuccess(false),
	d_paterGeomBuffer(0)
{
}

Cocos2DGeometryBuffer::~Cocos2DGeometryBuffer()
{
	if (d_effect)
	{
		delete d_effect;
	}

	if (d_TargetSurface)
	{
		d_TargetSurface->removeGeometryBuffer(RQ_BASE, this);
	}
}

bool Cocos2DGeometryBuffer::test_draw() const
{
	Cocos2DRenderer* pRender = (Cocos2DRenderer*)System::getSingleton().getRenderer();
	if (!pRender)
		return false;

	d_RenderSuccess = true;
	const int pass_count = d_effect ? d_effect->getPassCount() : 1;
	for (int pass = 0; pass < pass_count; ++pass)
	{
		size_t pos = 0;
		BatchList::const_iterator i = d_batches.begin();
		for (; i != d_batches.end(); ++i)
		{
			const BatchInfo& batchInfo = *i;
			Cocos2DTexture* pTexture = batchInfo.first;

			if (!pRender)
			{
				d_RenderSuccess = false;
				continue;
			}
			if (!pTexture)
			{
				d_RenderSuccess = false;
				continue;
			}
			if (!pRender->isTextureValid((*i).first))
			{
				d_RenderSuccess = false;
				continue;
			}
			if ((*i).first->m_bIsLoading)
			{
				d_RenderSuccess = false;
				continue;
			}
		}
	}

	return d_RenderSuccess;
}

void Cocos2DGeometryBuffer::draw() const
{
	d_RenderSuccess = true;

	// setup clip region
	const Rect screenArea(Vector2(System::getSingleton().GetAdapter()->GetDisplayOffsetX(),
		System::getSingleton().GetAdapter()->GetDisplayOffsetY()),
		Size(System::getSingleton().GetAdapter()->GetDisplayWidth(), System::getSingleton().GetAdapter()->GetDisplayHeight()));
	//clip.right = clip.left + (clip.right - clip.left) * d_WidthClip;
	GLint vp[4];
	glGetIntegerv(GL_VIEWPORT, vp);

	Rect clipRect = d_clipRect;
	clipRect.d_left -= d_translation.d_x;
	clipRect.d_right -= d_translation.d_x;
	clipRect.d_top -= d_translation.d_y;
	clipRect.d_bottom -= d_translation.d_y;
	clipRect.d_left = clipRect.d_left * d_matrix.mat[0] + d_matrix.mat[12];
	clipRect.d_right = clipRect.d_right * d_matrix.mat[0] + d_matrix.mat[12];
	clipRect.d_top = clipRect.d_top * d_matrix.mat[5] + d_matrix.mat[13];
	clipRect.d_bottom = clipRect.d_bottom * d_matrix.mat[5] + d_matrix.mat[13];
	clipRect = System::getSingleton().GetAdapter()->LogicToScreen(clipRect);
	Rect tempRect = screenArea.getIntersection(clipRect);

	int scissor_x = tempRect.d_left;
	int scissor_y = System::getSingleton().GetAdapter()->GetScreenHeight() - tempRect.d_bottom;
	int scissor_w = tempRect.getWidth();
	int scissor_h = tempRect.getHeight();

	if (!isRotation())
	{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)

		glScissor(static_cast<GLint>(scissor_x),
			static_cast<GLint>(scissor_y),
			static_cast<GLint>(scissor_w),
			static_cast<GLint>(scissor_h));
#else
		glScissor(static_cast<GLint>(scissor_x),
			static_cast<GLint>(scissor_y),
			static_cast<GLint>(scissor_w),
			static_cast<GLint>(scissor_h));
#endif
	}
	
	bool bNeedRestoreScissorTest = false;
	if (!d_clipRectNeedConvert)
	{
		if (glIsEnabled(GL_SCISSOR_TEST))
		{
			glDisable(GL_SCISSOR_TEST);
			bNeedRestoreScissorTest = true;
		}
	}

	// apply the transformations we need to use.
	if (!d_matrixValid)
		updateMatrix();

	kmGLMatrixMode(KM_GL_MODELVIEW);
	kmGLLoadMatrix(&d_matrix);
	//	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	//glBlendFunc(CC_BLEND_SRC, CC_BLEND_DST);
	Cocos2DRenderer* pRender = (Cocos2DRenderer*)System::getSingleton().getRenderer();

	pRender->m_program->setUniformForModelViewProjectionMatrix();

	//    ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_PosColorTex);
	//    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );
	//    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );

	const int pass_count = d_effect ? d_effect->getPassCount() : 1;
	for (int pass = 0; pass < pass_count; ++pass)
	{
		// set up RenderEffect
		// if (d_effect)
		//     d_effect->performPreRenderFunctions(pass);

		// draw the batches

		size_t pos = 0;
		BatchList::const_iterator i = d_batches.begin();
		for (; i != d_batches.end(); ++i)
		{
			if (!pRender)
			{
				d_RenderSuccess = false;
				continue;
			}

			if (!pRender->isTextureValid((*i).first))
			{
				d_RenderSuccess = false;
				continue;
			}

			pRender->MarkRenderTexture((*i).first);

			if ((*i).first->m_bIsLoading)
			{
				pRender->CheckLoadingTexture((*i).first);
				d_TargetSurface->invalidate();
				pos += (*i).second;
				d_RenderSuccess = false;
				continue;
			}

			//yangbin--如果纹理失效，则重新加载
			Cocos2DTexture* pTex = (*i).first;
			GLuint texId = pTex->getTextureName();
			if (GL_FALSE == glIsTexture(texId))
			{
				pRender->ReleaseTexture(pTex);
				d_RenderSuccess = false;
				continue;
			}

			const unsigned int kQuadSize = sizeof(cocos2d::ccV3F_C4B_T2F);
			//cocos2d::ccGLBindTexture2D( (*i).first->getTextureName() );
			if ((*i).first->isEtc())
			{
				cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColorEtc);
				cocos2d::ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords | cocos2d::kCCVertexAttribFlag_Color);
				cocos2d::ccGLActiveTexture(GL_TEXTURE0);
				cocos2d::ccGLBindTexture2D((*i).first->getTextureName());
				cocos2d::ccGLActiveTexture(GL_TEXTURE1);
				cocos2d::ccGLBindTexture2D((*i).first->getAlphaName());
			}
			else
			{
				//cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColor);					//移动到UI绘制的最外层
				//cocos2d::ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords | cocos2d::kCCVertexAttribFlag_Color);
				cocos2d::ccGLActiveTexture(GL_TEXTURE0);
				cocos2d::ccGLBindTexture2D((*i).first->getTextureName());
			}

			unsigned long long offset = (unsigned long long)&d_vertices[pos];

			// vertex
			unsigned int diff = offsetof(cocos2d::ccV3F_C4B_T2F, vertices);
			glVertexAttribPointer(cocos2d::kCCVertexAttrib_Position, 3, GL_FLOAT, GL_FALSE, kQuadSize, (void*)(offset + diff));

			// color
			diff = offsetof(cocos2d::ccV3F_C4B_T2F, colors);
			glVertexAttribPointer(cocos2d::kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE, kQuadSize, (void*)(offset + diff));

			// texture coords
			diff = offsetof(cocos2d::ccV3F_C4B_T2F, texCoords);
			glVertexAttribPointer(cocos2d::kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE, kQuadSize, (void*)(offset + diff));

			glDrawArrays(GL_TRIANGLES, 0, (*i).second);

			if ((*i).first->isEtc())
			{
				cocos2d::ccGLActiveTexture(GL_TEXTURE1);
				cocos2d::ccGLBindTexture2D(0);
				cocos2d::ccGLActiveTexture(GL_TEXTURE0);
				cocos2d::ccGLBindTexture2D(0);
			}
			else
				cocos2d::ccGLBindTexture2D(0);

			if ((*i).first->isEtc())
			{
				cocos2d::CCShaderCache::sharedShaderCache()->popShader();
			}

			pos += (*i).second;
		}
	}

	//    { GLenum __error = glGetError(); if(__error) printf("OpenGL error 0x%04X in %s %d\n", __error, __FUNCTION__, __LINE__); }

	// clean up RenderEffect
	if (d_effect)
	{
		d_effect->setScissor(scissor_x, scissor_y, scissor_w, scissor_h);
		//		d_device->SetRenderState(D3DRS_SCISSORTESTENABLE, false);
		//		d_device->SetTransform(D3DTS_WORLD,&s_identityMatrix);
		d_effect->performPostRenderFunctions();
		if (d_TargetSurface)
		{
			d_TargetSurface->Reset();
		}
	}

	if (bNeedRestoreScissorTest)
	{
		glEnable(GL_SCISSOR_TEST);
	}
}

void Cocos2DGeometryBuffer::setTranslation(const Vector3& t)
{
    d_translation = t;
    d_matrixValid = false;
}

void Cocos2DGeometryBuffer::setRotation(const Vector3& r)
{
    d_rotation = r;
    d_matrixValid = false;
}

//----------------------------------------------------------------------------//
void Cocos2DGeometryBuffer::setPivot(const Vector3& p)
{
    d_pivot = p;
    d_matrixValid = false;
}

//----------------------------------------------------------------------------//
void Cocos2DGeometryBuffer::setScale(const Vector3& s)
{
	d_scale = s;
	d_matrixValid = false;
}

void Cocos2DGeometryBuffer::setClippingRegionNeedConvert(bool val)
{
	d_clipRectNeedConvert = val;
}

//----------------------------------------------------------------------------//
void Cocos2DGeometryBuffer::setClippingRegion(const Rect& region)
{
	const Rect screenArea(Vector2(0, 0),
		System::getSingleton().getRenderer()->getDisplaySize());
	
	Rect tempRect=screenArea.getIntersection(region);
	
	d_clipRect = tempRect;
}

//----------------------------------------------------------------------------//
void Cocos2DGeometryBuffer::appendVertex(const Vertex& vertex)
{
    appendGeometry(&vertex, 1);
}

//----------------------------------------------------------------------------//
void Cocos2DGeometryBuffer::appendGeometry(const Vertex* const vbuff,
                                             uint vertex_count)
{
    performBatchManagement();

    // update size of current batch
    d_batches.back().second += vertex_count;

    // buffer these vertices
    cocos2d::ccV3F_C4B_T2F vd;
    const Vertex* vs = vbuff;
    for (uint i = 0; i < vertex_count; ++i, ++vs)
    {
        // copy vertex info the buffer, converting from CEGUI::Vertex to
        // something directly usable by D3D as needed.
        vd.vertices.x       = vs->position.d_x ;
        vd.vertices.y       = vs->position.d_y ;
        vd.vertices.z       = vs->position.d_z;
        vd.colors.a			= (GLubyte)(vs->colour_val.getAlpha()*255);
		vd.colors.r			= (GLubyte)(vs->colour_val.getRed()*255);
		vd.colors.g			= (GLubyte)(vs->colour_val.getGreen()*255);
		vd.colors.b			= (GLubyte)(vs->colour_val.getBlue()*255);
        vd.texCoords.u      = vs->tex_coords.d_x;
        vd.texCoords.v      = vs->tex_coords.d_y;
        d_vertices.push_back(vd);
    }
}

void Cocos2DGeometryBuffer::setActiveTexture(Texture* texture)
{
    d_activeTexture = static_cast<Cocos2DTexture*>(texture);
}

Texture* Cocos2DGeometryBuffer::getActiveTexture() const
{
	return d_activeTexture;
}

void Cocos2DGeometryBuffer::reset()
{
    d_batches.clear();
    d_vertices.clear();
    d_activeTexture = 0;
}

uint Cocos2DGeometryBuffer::getVertexCount() const
{
    return d_vertices.size();
}

uint Cocos2DGeometryBuffer::getBatchCount() const
{
    return d_batches.size();
}

void Cocos2DGeometryBuffer::setRenderEffect(RenderEffect* effect)
{
    if (d_effect!=effect)
    {
		if (d_effect)
		{
			delete d_effect;
			d_effect=NULL;
		}
    }
    
	d_effect = effect;	
}

RenderEffect* Cocos2DGeometryBuffer::getRenderEffect()
{
    return d_effect;
}

void Cocos2DGeometryBuffer::performBatchManagement()
{
    if (d_batches.empty() || (d_activeTexture != d_batches.back().first))
        d_batches.push_back(BatchInfo(d_activeTexture, 0));
}

void Cocos2DGeometryBuffer::updateMatrix() const
{
	if (d_matrixValid)	return;

	kmGLMatrixMode(KM_GL_MODELVIEW);
	kmGLPushMatrix();

	Vector3 trans = this->getLocalTranslation();

	const Vector3 final_trans(trans.d_x + d_pivot.d_x,
		trans.d_y + d_pivot.d_y,
		trans.d_z + d_pivot.d_z);

	kmGLLoadIdentity();
    kmGLTranslatef(final_trans.d_x, final_trans.d_y, final_trans.d_z);
    kmGLScalef(d_scale.d_x, d_scale.d_y, d_scale.d_z);
	kmGLRotatef(d_rotation.d_z, 0.0f, 0.0f, 1.0f);
	kmGLRotatef(d_rotation.d_y, 0.0f, 1.0f, 0.0f);
    kmGLRotatef(d_rotation.d_x, 1.0f, 0.0f, 0.0f);
	kmGLTranslatef(-d_pivot.d_x, -d_pivot.d_y, -d_pivot.d_z);
	kmGLGetMatrix(KM_GL_MODELVIEW, &d_matrix);
	kmGLPopMatrix();

	if (d_paterGeomBuffer)
	{
		const kmMat4* paterMatrix = d_paterGeomBuffer->getMatrix();
		kmMat4Multiply(&d_matrix,paterMatrix,&d_matrix);
	}

    d_matrixValid = true;
}

const kmMat4* Cocos2DGeometryBuffer::getMatrix() const
{
	
    if (!d_matrixValid)
        updateMatrix();
	
    return &d_matrix;
}

RenderingSurface* Cocos2DGeometryBuffer::getTargetSurface()
{
	return d_TargetSurface;

}
void Cocos2DGeometryBuffer::SetRenderSurface(RenderingSurface* pSurface)
{
	d_TargetSurface=pSurface;

}

void Cocos2DGeometryBuffer::SetWidthClip(const float& clip)
{
	d_WidthClip = clip;
}

Vector3 Cocos2DGeometryBuffer::getLocalTranslation() const
{
	if (d_paterGeomBuffer)
	{
		return d_translation - d_paterGeomBuffer->d_translation;
	}
	return this->d_translation;
}

Vector3 Cocos2DGeometryBuffer::getLocalScale() const
{
	if (d_paterGeomBuffer)
	{
		return d_scale * d_paterGeomBuffer->getLocalScale();
	}
	return d_scale;
}

bool Cocos2DGeometryBuffer::isRotation() const
{
	if (this->d_rotation.d_z != 0.0f)
		return true;

	if (d_paterGeomBuffer)
		return d_paterGeomBuffer->isRotation();

	return false;
}

} // End of  CEGUI namespace section
