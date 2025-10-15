/***********************************************************************
	filename: 	CEGUIDragContainer.cpp
	created:	14/2/2005
	author:		Paul D Turner
*************************************************************************/

#include "elements/CEGUISkillBox.h"
#include "CEGUIImageset.h"
#include "CEGUICoordConverter.h"
#include "CEGUIRenderingContext.h"
#include "CEGUIFont.h"
#include "elements/CEGUIItemTable.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIUDim.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    //////////////////////////////////////////////////////////////////////////
    // Window type string
    const String SkillBox::WidgetTypeName("CEGUI/SkillBox");
    // Event Strings
    const String SkillBox::EventNamespace("SkillBox");
    //
	const String SkillBox::EventOnClick( "SKillBoxClick" );
	//
	//const String SkillBox::EventUpdateSkillInfo( "UpdateSkillInfo" );

    //////////////////////////////////////////////////////////////////////////

	SkillBox::SkillBox(const String& type, const String& name) :
		Window(type, name),
		m_bBackgroundDynamic(false),
		d_image(NULL),
		d_backimage(NULL),
		d_tlCornerImg(NULL),
		d_isMouseOn(false),
		d_BackGroundEnable(true),
		d_ashy(false),
		d_selected(false),
		d_haveSelectedState(false),
		d_skillID(0),
		d_skillDueDate(0),
		d_pickupinbattle(false),
		sourceBoxName(L""),
		d_blnBackGroupOnTop(false)
    {
        SetDragMoveEnable(false);
		addSkillBoxProperties();

		// by bayaer, 因为策划要求不自动添加这两项属性 ， 所以这里不能初始化， 否则会读空值
#ifdef FORCEGUIEDITOR
		//setProperty("UnifiedMaxSize", PropertyHelper::uvector2ToString(UVector2(UDim(0.f, static_cast<float>(SkillBoxSize)),
		//	UDim(0.f,static_cast<float>(SkillBoxSize)))));
		//setProperty("UnifiedMinSize",PropertyHelper::uvector2ToString(UVector2(UDim(0.f,static_cast<float>(SkillBoxSize)),
		//	UDim(0.f,static_cast<float>(SkillBoxSize)))));
#else
		setProperty("UnifiedMaxSize", PropertyHelper::uvector2ToString(UVector2(UDim(0.f, static_cast<float>(SkillBoxSize)),
			UDim(0.f, static_cast<float>(SkillBoxSize)))));
		//setProperty("UnifiedMinSize", PropertyHelper::uvector2ToString(UVector2(UDim(0.f, static_cast<float>(SkillBoxSize)),
			//UDim(0.f, static_cast<float>(SkillBoxSize)))));
#endif
	}

	SkillBox::SkillBox(const String& type) :
		Window(type)
	{
	}
	const SkillBox& SkillBox::operator=(const SkillBox& t)
	{
		Window::operator=(t);
		d_isMouseOn = t.d_isMouseOn;
		d_BackGroundEnable = t.d_BackGroundEnable;
		d_ashy = t.d_ashy;
		d_selected = t.d_selected;
		d_haveSelectedState = t.d_haveSelectedState;
		d_image = t.d_image;
		d_backimage = t.d_backimage;
		d_tlCornerImg = t.d_tlCornerImg;
		sourceBoxName = t.sourceBoxName;
		d_skillID = t.d_skillID;
		d_skillDueDate = t.d_skillDueDate;
		for (int i = 0; i < SkillBoxTextType_NUM; i++)
		{
			m_TextUnits[i] = t.m_TextUnits[i];
		}
		d_pickupinbattle = t.d_pickupinbattle;
		d_blnBackGroupOnTop = t.d_blnBackGroupOnTop;
		return *this;
	}
	Window* SkillBox::clone(Window* wnd)
	{
		SkillBox* retWnd = (SkillBox*)wnd;
		if (retWnd == NULL)
			retWnd = new SkillBox(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == SkillBox::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

    SkillBox::~SkillBox(void)
    {
    }

	void SkillBox::Clear()
	{
		setUserData(NULL);
		SetImage(NULL);
		SetBackGroundImage(NULL);
		SetTextUnitText("");
		SetTextUnitText("",SkillBoxTextType_LeftTop);
		setTooltipText("");
		d_skillID = 0;
		d_skillDueDate = 0;
		sourceBoxName = L"";
		d_tlCornerImg = NULL;
	}

	void SkillBox::SetBackGroundEnable(bool bEnable)
	{
		if (bEnable!=d_BackGroundEnable)
		{
			d_BackGroundEnable=bEnable;
			invalidate(false);
		}
		
	}

	void SkillBox::addSkillBoxProperties(void)
	{

	}

	void SkillBox::SetImage( const Image* image) 
	{ 
		if (d_image!=image)
		{
			d_image=image;
			invalidate(false);
		}
		
	}

	void SkillBox::SetImage(String strImageset, String strImageName)
	{
//		const CEGUI::Image* pImage=&ImagesetManager::getSingleton().get(Imageset).getImage(ImageName);
//		if (pImage)
//		{
//			SetImage(pImage);
//		}
        
        if (ImagesetManager::getSingleton().isDefined(strImageset))
		{
            
            const Imageset& imageSet=ImagesetManager::getSingleton().get(strImageset);
			if (imageSet.isImageDefined(strImageName))
			{
                const CEGUI::Image* pImage=&imageSet.getImage(strImageName);
                
                
                if (pImage)
                {
                    SetImage(pImage);
                }
            }else
            {
                Logger::getSingleton().logEvent("set:"+strImageset+"image:"+strImageName+"not found", Errors);
            }
        }else
        {
            Logger::getSingleton().logEvent("set:"+strImageset+"image:"+strImageName+"not found", Errors);
        }
	}

	void SkillBox::SetCornerImage(const CEGUI::Image *image)
	{
		if (d_tlCornerImg != image)
		{
			d_tlCornerImg = image;
			invalidate(false);
		}
	}

	void SkillBox::SetCornerImage(CEGUI::String Imageset, CEGUI::String ImageName)
	{
		if (ImageName.empty())
			return;

		const CEGUI::Image* pImage = ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetCornerImage(pImage);
		}
	}

	void SkillBox::SetSelected(bool select)
	{
        if (select) {
            if(d_isMouseOn)
                return;
            d_isMouseOn = true;
            
//            Window::onMouseEnters(e);
            invalidate(false);
        }else
        {
            if(!d_isMouseOn)
                return;
            d_isMouseOn = false;
            
//            Window::onMouseLeaves(e);
            invalidate(false);
        }
		if (select != d_selected)
		{
			d_selected = select;
			invalidate();
		}
	}

	void SkillBox::onMouseEnters(MouseEventArgs& e)
	{
        return;
		//if (d_skillID == 0)
		//	return;
//		if(d_isMouseOn)
//			return;
//		d_isMouseOn = true;
//
//		Window::onMouseEnters(e);
//		invalidate(false);
	}

	void SkillBox::onMouseLeaves(MouseEventArgs& e)
	{
        return;
		//if (d_skillID == 0)
		//	return;

//		if(!d_isMouseOn)
//			return;
//		d_isMouseOn = false;
//
//		Window::onMouseLeaves(e);
//		invalidate(false);
	}

	void SkillBox::onMouseButtonDown(MouseEventArgs& e)
	{
		Window::onMouseButtonDown(e);
        //Window::CheckGuideEnd(e.button);
		if(d_haveSelectedState)
		{
			SetSelected(true);
		}
		fireEvent(EventOnClick, e, EventNamespace);
		++e.handled;

	}

	SkillBoxTextUnit* SkillBox::GetTextUnit(SkillBoxTextType type)
	{
		if (type>=0&&type<SkillBoxTextType_NUM)
		{
			return &m_TextUnits[type];
		}

		return NULL;
	}

	void SkillBox::SetTextUnit(CEGUI::String text,SkillBoxTextType type/* =SkillBoxTextType_RightBottom */,CEGUI::colour color)
	{
		SkillBoxTextUnit* pUnit=GetTextUnit(type);
		pUnit->type=type;
		if (NULL!=pUnit)
		{
			bool isNeedRedraw=false;
			if (text!=pUnit->Text)
			{
				pUnit->Text=text;
				isNeedRedraw=true;
			}
			if (color!=pUnit->color)
			{
				pUnit->color=color;
				isNeedRedraw=true;
			}
			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}
	}

	void SkillBox::SetTextUnitText(CEGUI::String text,SkillBoxTextType type)
	{
		SkillBoxTextUnit* pUnit=GetTextUnit(type);
		if (NULL!=pUnit)
		{
			bool isNeedRedraw=false;
			if (text!=pUnit->Text)
			{
				pUnit->Text=text;
				isNeedRedraw=true;
			}
			
			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}

	}
	void SkillBox::SetTextUnitColor(CEGUI::colour color,SkillBoxTextType type)
	{
		SkillBoxTextUnit* pUnit=GetTextUnit(type);
		if (NULL!=pUnit)
		{
			bool isNeedRedraw=false;
			if (color!=pUnit->color)
			{
				pUnit->color=color;
				isNeedRedraw=true;
			}

			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}
	}
	
	CEGUI::Point SkillBox:: GetTextUnitDrawPos(const Rect& rect,SkillBoxTextType type)
	{
		Point offset(0.0f,0.0f);
		SkillBoxTextUnit* pUnit=GetTextUnit(type);
		Font* pFont=getFont(true);
		
		float TextWide=pFont->getTextExtent(pUnit->Text);
		float TextHight=pFont->getFontHeight();
		
		if (NULL!=pUnit)
		{
			switch (type)
			{
			case SkillBoxTextType_RightBottom:
				{
					offset.d_x=rect.d_right-TextWide-4.0f;
					offset.d_y=rect.d_bottom-TextHight-4.0f;
				}
				break;
			case SkillBoxTextType_LeftTop:
				{
					offset.d_x=rect.d_left+4.0f;
					offset.d_y=rect.d_top+4.0f;
				}
				break;
			case SkillBoxTextType_LeftBottom:
				{
					offset.d_x=rect.d_left+4.0f;
					offset.d_y=rect.d_bottom-TextHight-4.0f;
				}
				break;
			}
			
			
		}
		
		return offset;
		
	}

	void SkillBox::SetBackGroundImage(const CEGUI::Image *image)
	{
		if (d_backimage!=image)
		{
			d_backimage=image;
			invalidate(false);
		}
	}

	void SkillBox::SetBackGroundImage(CEGUI::String strImageset, CEGUI::String strImageName)
	{
//		const CEGUI::Image* pImage=&ImagesetManager::getSingleton().get(Imageset).getImage(ImageName);
//		if (pImage)
//		{
//			SetBackGroundImage(pImage);
//		}
        
        if (ImagesetManager::getSingleton().isDefined(strImageset))
		{
            
            const Imageset& imageSet=ImagesetManager::getSingleton().get(strImageset);
			if (imageSet.isImageDefined(strImageName))
			{
                const CEGUI::Image* pImage=&imageSet.getImage(strImageName);
                
                
                if (pImage)
                {
                    SetBackGroundImage(pImage);
                }
            }else
            {
                Logger::getSingleton().logEvent("set:"+strImageset+"image:"+strImageName+"not found", Errors);
            }
        }else
        {
            Logger::getSingleton().logEvent("set:"+strImageset+"image:"+strImageName+"not found", Errors);
        }
	}

	void SkillBox::SetSkillID(int id)
	{
		d_skillID = id;
	}

	int SkillBox::GetSkillID() const
	{
		return d_skillID;
	}

	void SkillBox::SetSourceBox(const String& pbox)
	{
		sourceBoxName = pbox;
	}

	String SkillBox::GetSourceBox() const
	{
		return sourceBoxName;
	}

} // End of  CEGUI namespace section
