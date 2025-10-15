//  CEGUISwitch.cpp
//  CEGUI

#include "elements/CEGUISwitch.h"
#include "elements/CEGUIPushButton.h"
#include "CEGUIWindowManager.h"

#define MOVE_DUR  0.2f //s

namespace CEGUI
{
    const String Switch::EventNamespace("Switch");
    const String Switch::WidgetTypeName("CEGUI/Switch");

	const String Switch::ThumbNameSuffix("__auto_thumb__");
    
    SwitchProperties::Status	Switch::d_statusProperty;

    const String Switch::EventStatusChanged( "StatusChanged" );
    
    
    Switch::Switch(const String& type, const String& name):
    ButtonBase(type, name),
    d_status(OFF),
	_moveState(MOVE_STOP),
	_moveElapse(0.0f),
	_allowSwitchWhenClick(true)
    {
        addSwitchProperties();
    }

	Switch::Switch(const String& type) :
		ButtonBase(type)
	{
	}
	const Switch& Switch::operator=(const Switch& t)
	{
		Window::operator=(t);
		d_status = t.d_status;
		d_statusProperty = t.d_statusProperty;
		_moveState = t._moveState;
		_moveElapse = t._moveElapse;
		_moveBeginX = t._moveBeginX;
		_allowSwitchWhenClick = t._allowSwitchWhenClick;
		return *this;
	}
	Window* Switch::clone(Window* wnd)
	{
		Switch* retWnd = (Switch*)wnd;
		if (retWnd == NULL)
			retWnd = new Switch(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == Switch::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

    Switch::~Switch()
    {
    }
    
    void Switch::setStatus(const String& status)
    {
        if (0 == status.compare("ON"))
        {
            setStatus(ON);
        }
        else if(0 == status.compare("OFF"))
        {
            setStatus(OFF);
        }
    }
    
	void Switch::setStatus(SwitchStatus status, bool ani)
    {
        if (status != d_status)
        {
            d_status= status;

			if (ani)
			{
				_moveState = (status==ON ? MOVE_RIGHT: MOVE_LEFT);
				_moveElapse = 0.0f;
				_moveBeginX = getThumbBtn()->getXPosition().d_offset;
			}
			else
			{
				float x = (status == ON ? getPixelSize().d_width - getThumbBtn()->getPixelSize().d_width : 0);
				getThumbBtn()->setXPosition(CEGUI::UDim(0,x));
			}

            invalidate();
            
            WindowEventArgs args(this);
            onStatusChange(args);
        }
    }
    
	void Switch::setLookStatus(SwitchStatus status, bool ani)
	{
		if (d_status != status)
		{
			d_status = status;
			if (ani)
			{
				_moveState = (status == ON ? MOVE_RIGHT : MOVE_LEFT);
				_moveElapse = 0.0f;
				_moveBeginX = getThumbBtn()->getXPosition().d_offset;
			}
			else
			{
				_moveState = MOVE_STOP;			//È¡Ïû¶¯»­
				float x = (status == ON ? getPixelSize().d_width - getThumbBtn()->getPixelSize().d_width : 0);
				getThumbBtn()->setXPosition(CEGUI::UDim(0, x));
			}
			invalidate();
		}
	}

    String Switch::getStatusString() const
    {
        if(ON == getStatus())
        {
            return String("ON");
        }
        else if (OFF == getStatus())
        {
            return String("OFF");
        }
        return String("ON");
    }
    
    void Switch::onStatusChange(WindowEventArgs& e)
    {
        fireEvent(EventStatusChanged, e, EventNamespace);
    }

	Window* Switch::getThumbBtn()
	{
        const char* s = getName().c_str();
		return WindowManager::getSingleton().getWindow(getName() + ThumbNameSuffix);
	}


    void Switch::onMouseButtonUp(MouseEventArgs& e)
    {
        if ((e.button == LeftButton) && isPushed())
        {
            Window* sheet = System::getSingleton().getGUISheet();
            
            ++e.handled;
        }
        // default handling
        Window::onMouseButtonUp(e);
    }
    
    void Switch::onMouseButtonDown(MouseEventArgs& e)
    {
        ButtonBase::onMouseButtonDown(e);
        if (e.button == LeftButton)
		{
			if (_moveState == MOVE_STOP && _allowSwitchWhenClick)
			{
				if (ON == getStatus())
				{
					setStatus(OFF, true);
				}
				else if (OFF == getStatus())
				{
					setStatus(ON, true);
				}
			}

			++e.handled;
        }
    }

    /*************************************************************************
     Add properties
     *************************************************************************/
    void Switch::addSwitchProperties(void)
    {
		addProperty(&d_statusProperty);
    }

	void Switch::initialiseComponents(bool bClone)
	{
		/*Window* thumb = getThumbBtn();
		thumb->subscriberEventDrag(Event::Subscriber(&CEGUI::Switch::onThumbDrag, this));*/
	
		if (!bClone)
		{
			getThumbBtn()->setMousePassThroughEnabled(true);
			EnableClickAni(false);
		}
	}

	void Switch::updateSelf(float elapsed)
	{
		Window::updateSelf(elapsed);

		switch (_moveState)
		{
		case MOVE_LEFT:
		{
			_moveElapse += elapsed;
			Window *thumb = getThumbBtn();
			float w = getPixelSize().d_width - thumb->getPixelSize().d_width;
			float x = w * (1 - _moveElapse / (MOVE_DUR * _moveBeginX / w));
			x = (x < 0 ? 0 : x);
			thumb->setXPosition(CEGUI::UDim(0, x));
			if (x == 0)
			{
				_moveState = MOVE_STOP;
			}
			break;
		}
		case MOVE_RIGHT:
		{
			_moveElapse += elapsed;
			Window *thumb = getThumbBtn();
			float w = getPixelSize().d_width - thumb->getPixelSize().d_width;
			float x = _moveBeginX + w * (_moveElapse / (MOVE_DUR*(w - _moveBeginX) / w));
			x = (x > w ? w : x);
			thumb->setXPosition(CEGUI::UDim(0, x));
			if (x == w)
			{
				_moveState = MOVE_STOP;
			}
			break;
		}
		case MOVE_STOP:
		default:
			break;
		}
		
	}
}
