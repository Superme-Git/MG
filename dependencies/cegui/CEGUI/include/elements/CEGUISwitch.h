//  CEGUISwitch.h
//  CEGUI

#ifndef CEGUI_CEGUISwitch_h
#define CEGUI_CEGUISwitch_h

#include "../CEGUIBase.h"
#include "CEGUIButtonBase.h"
#include "CEGUISwitchProperties.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

namespace CEGUI
{

    enum SwitchStatus
    {
        ON,
        OFF
    };

	enum SwitchAniState
	{
		MOVE_LEFT,
		MOVE_RIGHT,
		MOVE_STOP
	};
    
    class CEGUIEXPORT Switch : public ButtonBase
    {
    public:
        static const String EventNamespace;				//!< Namespace for global events
        static const String WidgetTypeName;             //!< Window factory name

		static const String ThumbNameSuffix;
        
        static const String EventStatusChanged;     //!< The status of the widget has changed.
        
        Switch(const String& type, const String& name);
        
        
        virtual ~Switch(void);
    
        void setStatus(const String& status);
        
        void setStatus(SwitchStatus status, bool ani = false);

		//just change status, don't fire event
		void setLookStatus(SwitchStatus status, bool ani = false);		//new add
        
        String getStatusString() const;
        
        SwitchStatus getStatus() const {return d_status;};
        
		bool	isPushed(void) const			{return d_pushed;}

		// whether switch staus when click, default is true
		// sometimes we just need the touch event but don't want it auto switch status
		void allowSwitchWhenClick(bool isAllow) { _allowSwitchWhenClick = isAllow; }

		Window* getThumbBtn();

	public:
		Switch(const String& type);
		const Switch& operator=(const Switch& t);
		virtual Window* clone(Window* wnd);
		virtual bool onRenameTemplatePrefix(const String& sPrefix){ return ButtonBase::onRenameTemplatePrefix(sPrefix); }

    protected:
        
        virtual bool testClassName_impl(const String& class_name) const
        {
            if (class_name=="Switch")	return true;
            return Window::testClassName_impl(class_name);
        }
        
		virtual void	initialiseComponents(bool bClone);

        virtual void	onStatusChange(WindowEventArgs& e);
        
		virtual void	onMouseButtonUp(MouseEventArgs& e);

		virtual void    onMouseButtonDown(MouseEventArgs& e);

		/*virtual bool    onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer);*/
        
		virtual void updateSelf(float elapsed);
        
        SwitchStatus   d_status;          //开关状态，on，off
        
    private:
        /*************************************************************************
         Static Properties for this class
         *************************************************************************/
        static SwitchProperties::Status	d_statusProperty;
        
        
        /*************************************************************************
         Private methods
         *************************************************************************/
        void	addSwitchProperties(void);
        
		SwitchAniState _moveState;
		float	_moveElapse;
		float	_moveBeginX;

		bool	_allowSwitchWhenClick;
    };

}

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif
