//
//  UIGestureRecognizer.cpp
//  FireClient
//
//  Created by 杨印宇 on 13-1-6.
//
//

#include "gesture/CEGUIGestureRecognizer.h"
namespace CEGUI {
namespace Gesture {
    const String CEGUIGestureRecognizer::EventGesture("GestureRecognized");
    const String CEGUIGestureRecognizer::EventNameSpace("Gesture");
    CEGUIGestureRecognizer::CEGUIGestureRecognizer() :
    m_eState(GestureRecognizerStatePossible)
    {
		m_dEvent = 0;
    }
    
    CEGUIGestureRecognizer::~CEGUIGestureRecognizer() {
		if (m_dEvent)
		{
			delete m_dEvent;
			m_dEvent = 0;
		}
    }

    void CEGUIGestureRecognizer::addTarget(CEGUI::Window* pWindow/*, Gesture_Callback callback*/) {
    }
    
    const CEGUI::EventArgs* CEGUIGestureRecognizer::GetEvent() const {
		return m_dEvent;
    }
    
    void CEGUIGestureRecognizer::fireGestureEvent() {
        GestureEventArgs ge = GestureEventArgs(this);
        fireEvent(EventGesture, ge, EventNameSpace);
    }
}
}
