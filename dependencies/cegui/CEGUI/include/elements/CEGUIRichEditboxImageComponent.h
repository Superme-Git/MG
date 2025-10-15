
#ifndef _CEGUIRichEditboxImageComponent_h_
#define _CEGUIRichEditboxImageComponent_h_

#include "CEGUIRichEditboxComponent.h"
#include "CEGUIColourRect.h"

// Start of CEGUI namespace section
namespace CEGUI
{
//! String component that draws an image.
class CEGUIEXPORT RichEditboxImageComponent : public RichEditboxComponent
{
public:
    //! Constructor
    RichEditboxImageComponent();
    RichEditboxImageComponent(const String& imageset, const String& image,const int& specialid = 0);
    RichEditboxImageComponent(const Image* image,const int& specialid = 0);
	
    //! Set the image to be drawn by this component.
    void setImage(const String& imageset, const String& image);
    //! Set the image to be drawn by this component.
    void setImage(const Image* image);
    //! return the current set image that will be drawn by this component
    const Image* getImage() const;

    //! Set the colour values used when rendering this component.
    void setColours(const ColourRect& cr);
    //! Set the colour values used when rendering this component.
    void setColours(const colour& c);
    //! return the ColourRect object used when drawing this component.
    const ColourRect& getColours() const;
    //! set the size for rendering the image (0s mean 'normal' size)
    void setSize(const Size& sz);
    //! return the size for rendering the image (0s mean 'normal' size)
    const Size& getSize() const;

    // implementation of abstract base interface
    void draw(GeometryBuffer* buffer, const Vector2& position,
              const ColourRect* mod_colours, const Rect* clip_rect,
              const float vertical_space, const float space_extra,bool bBorder=false) ;
    Size getPixelSize() ;

    bool canSplit() ;
    RichEditboxComponent* split(float split_point, bool first_component);
    RichEditboxComponent* clone() ;
    size_t getSpaceCount() ;
	String GenerateParseText();

	void SetImageScale(float scale) { d_ImageScale=scale; }
	const int GetSpecialID() const { return d_specialID;}
protected:

	virtual void    onMouseEnter();//{ d_bIsMouseOn=true; };
	virtual void    onMouseLeave();// { d_bIsMouseOn=false; };

    //! pointer to the image drawn by the component.
    const Image* d_image;
    //! ColourRect object describing the colours to use when rendering.
    ColourRect d_colours;
    //! target size to render the image at (0s mean natural size)
    Size d_size;

	float d_xPos;//ͼƬ�����richeditbox��xƫ����
	float d_yPos;//ͼƬ�����richeditbox��yƫ����

	//�������⴦���id����ͨ��ͼƬ��ӦֵΪ0��
	//����������ֲ��н�����Ʒ��ͼƬ��d_specialIDΪ ������Ʒ������,ֵС��100
	//���������׷�ٽ�������ʾ���񵹼�ʱ��ͼƬ��d_specialIDΪ����id��ֵһ������100
	int  d_specialID;
	
	static const Vector2 sTextOffset;
	float d_ImageScale;  //���ű���

};

} // End of  CEGUI namespace section

#endif // end of guard _CEGUIRenderedStringImageComponent_h_
