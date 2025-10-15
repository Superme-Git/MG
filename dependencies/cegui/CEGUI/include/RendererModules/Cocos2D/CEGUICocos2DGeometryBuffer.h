#ifndef _CEGUICocos2DGeometryBuffer_h_
#define _CEGUICocos2DGeometryBuffer_h_

#include "../../CEGUIGeometryBuffer.h"
#include "../../CEGUIRect.h"

#include "CEGUICocos2DRenderer.h"
#include "CEGUIRenderingSurface.h"

#include <ccTypes.h>
#include <kazmath/kazmath.h>

#include <utility>
#include <vector>

#if defined(_MSC_VER)
#   pragma warning(push)
#   pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
class Cocos2DTexture;

class COCOS2D_GUIRENDERER_API Cocos2DGeometryBuffer : public GeometryBuffer
{
public:
    //! Constructor
	Cocos2DGeometryBuffer();
	~Cocos2DGeometryBuffer();

    //! return pointer to D3DXMATRIX used as world transform.
    const kmMat4* getMatrix() const;
	void setScale(const Vector3& s);

    // implementation of abstract members from GeometryBuffer
    virtual void draw() const;
	virtual bool test_draw() const;
    virtual void setTranslation(const Vector3& t);
    virtual void setRotation(const Vector3& r);
    virtual void setPivot(const Vector3& p);
	virtual void setClippingRegionNeedConvert(bool val);
	virtual void setClippingRegion(const Rect& region);
    virtual void appendVertex(const Vertex& vertex);
    virtual void appendGeometry(const Vertex* const vbuff, uint vertex_count);
    virtual void setActiveTexture(Texture* texture);
    virtual void reset();
    
	virtual Texture*		getActiveTexture() const;
    virtual uint			getVertexCount() const;
    virtual uint			getBatchCount() const;
    virtual void			setRenderEffect(RenderEffect* effect);
    virtual RenderEffect*	getRenderEffect();

	RenderingSurface* getTargetSurface();
	void SetRenderSurface(RenderingSurface*);
	void SetWidthClip(const float&);
	virtual bool isRenderSuccess(){ return d_RenderSuccess; }
	virtual void SetPater(GeometryBuffer* buffer){ d_paterGeomBuffer = (Cocos2DGeometryBuffer*)buffer; }
    virtual void updateMatrix() const;


	void SetUpdateMatrixFlag(bool matrixValid){d_matrixValid = matrixValid;}

protected:
    void performBatchManagement();
	Vector3 getLocalTranslation() const;
	Vector3 getLocalScale() const;
	bool isRotation() const;

    //! last texture that was set as active
    Cocos2DTexture* d_activeTexture;
    typedef std::pair<Cocos2DTexture*, uint> BatchInfo;
    typedef std::vector<BatchInfo> BatchList;
    BatchList d_batches;
    
	typedef std::vector<cocos2d::ccV3F_C4B_T2F> VertexList;
    VertexList d_vertices;
    
	bool d_clipRectNeedConvert;
    Rect d_clipRect;
    Vector3 d_translation;
    Vector3 d_rotation;
	Vector3 d_scale;
    Vector3 d_pivot;

    RenderEffect* d_effect;
   
    //LPDIRECT3DDEVICE9 d_device;
    //! model matrix cache
    //mutable float d_matrix[16];
    mutable kmMat4 d_matrix;
    //! true when d_matrix is valid and up to date
    mutable bool d_matrixValid;
	RenderingSurface* d_TargetSurface;

	float d_WidthClip;
	mutable bool d_RenderSuccess;

	Cocos2DGeometryBuffer*	d_paterGeomBuffer;
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#   pragma warning(pop)
#endif

#endif  // end of guard _CEGUICocos2DGeometryBuffer_h_
