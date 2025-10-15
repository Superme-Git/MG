
#ifndef _CEGUIRichEditboxButtonImageComponent_h_
#define _CEGUIRichEditboxButtonImageComponent_h_

#include "CEGUIRichEditboxComponent.h"
#include "CEGUIRichEditboxImageComponent.h"
#include "CEGUIColourRect.h"

// Start of CEGUI namespace section
namespace CEGUI
{
//! String component that draws an image.
class CEGUIEXPORT RichEditboxButtonImageComponent : public RichEditboxImageComponent
{
public:
    //! Constructor
    RichEditboxButtonImageComponent();
    RichEditboxButtonImageComponent(const String& nimageset, const String& nimage,const String& himageset, const String& himage,const String& pimageset, const String& pimage,const int& id);
    RichEditboxButtonImageComponent(const Image* image,const Image* himage,const Image* pimage,const int& id);
	
	static const String EventLeftButtonClick;			//!< The read-only mode for the edit box has been changed.
   
	void setHoverImage(const String& imageset, const String& image); 
    void setHoverImage(const Image* image);
   
	void setPushedImage(const String& imageset, const String& image); 
	void setPushedImage(const Image* image);

    const Image* getHoverImage() const;
	const Image* getPushedImage() const;

   
    // implementation of abstract base interface
    void draw(GeometryBuffer* buffer, const Vector2& position,
              const ColourRect* mod_colours, const Rect* clip_rect,
              const float vertical_space, const float space_extra,
			  bool bBorder=false) ;

	bool onMouseButtonDown(MouseEventArgs& e);
	bool onMouseButtonUp(MouseEventArgs& e);
	
	String GenerateParseText();
	int GetID() const{ return dataID;}
protected:

	virtual void    onMouseEnter();//{ d_bIsMouseOn=true; };
	virtual void    onMouseLeave();// { d_bIsMouseOn=false; };

	const Image*	d_HoverImage;
	const Image*	d_PushedImage;

	bool     d_Pushed;
	int      dataID;
};

} // End of  CEGUI namespace section

#endif // end of guard _CEGUIRenderedStringImageComponent_h_
