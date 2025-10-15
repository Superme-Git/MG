
#ifndef _CEGUIRichEditboxEmotionComponent_h_
#define _CEGUIRichEditboxEmotionComponent_h_

#include "CEGUIRichEditboxComponent.h"
#include "CEGUIColourRect.h"

// Start of CEGUI namespace section
namespace CEGUI
{
//! String component that draws an image.

class CEGUIEXPORT RichEditboxEmotionComponent : public RichEditboxComponent
{
public:
    //! Constructor
    RichEditboxEmotionComponent();
	RichEditboxEmotionComponent(AnimationInstance* animationInstance);
	RichEditboxEmotionComponent(int emotionID);

	RichEditboxEmotionComponent(const RichEditboxEmotionComponent& other);

	//! Assignment.
	RichEditboxEmotionComponent& operator=(const RichEditboxEmotionComponent& rhs);
	RichEditboxEmotionComponent* operator=(const RichEditboxEmotionComponent* rhs);
	//virtual ~RichEditboxEmotionComponent();

	static const String EventNamespace;
	static const String EventFrameChange;
	static const String EventEmotionStart;
	static const String EventEmotionClick;
   
	//! Set the image to be drawn by this component.
    void setImage(const String& imageset, const String& image);
	void setImage(const Image* image) { d_image=image; }
    //! Set the image to be drawn by this component.
   
	const Image* getImage() const { return d_image; };
	const AnimationInstance* GetAniInstace() { return d_animationInstance; }
	void SetAniInstance(AnimationInstance* aniIns);

	void SetAnimateID(int aniID);
	int GetAniID() { return d_AnimateID; }
	void OnFrameChange();

	void setSize(const Size& sz);
	//! return the size for rendering the image (0s mean 'normal' size)
	const Size& getSize() const;

	void SetFrame(int frame);
	int GetFrame()const  { return d_CurFrame; }

    //! Set the colour values used when rendering this component.

    // implementation of abstract base interface
    void draw(GeometryBuffer* buffer, const Vector2& position,
              const ColourRect* mod_colours, const Rect* clip_rect,
              const float vertical_space, const float space_extra,bool border=false) ;
    Size getPixelSize() ;

    bool canSplit() ;
    RichEditboxComponent* split(float split_point, bool first_component);
    RichEditboxComponent* clone() ;
    size_t getSpaceCount() ;
	String GenerateParseText();
	String GeneratePureText(size_t startPos,size_t endPos);
	bool updata(float fElapse);

	float GetElapseTime() { return d_ElapseTime; }

	void SetScale(Vector2 scale) { d_Scale=scale; }

protected:
    //! pointer to the image drawn by the component.
    const Image* d_image;
    //! ColourRect object describing the colours to use when rendering.
    AnimationInstance* d_animationInstance;
	Size d_size;
	int  d_AnimateID;
	int d_CurFrame;
	float d_CurPosition;
	float d_Duration;
	float d_ElapseTime;

	bool onMouseButtonDown(MouseEventArgs& e);
	void onMouseEnter();
	void onMouseLeave();
	Vector2 d_Scale;
};



} // End of  CEGUI namespace section

#endif // end of guard _CEGUIRenderedStringImageComponent_h_
