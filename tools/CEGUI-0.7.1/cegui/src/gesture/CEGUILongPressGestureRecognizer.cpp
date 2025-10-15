//
//  CEGUILongPressGestureRecognizer.cpp
//  FireClient
//
//  Created by 杨印宇 on 13-1-6.
//
//
#include "gesture/CEGUILongPressGestureRecognizer.h"
//#define SUBSCRIBER(X,T) pWindow->subscribeEvent(X, CEGUI::Event::Subscriber(T, this))
namespace CEGUI {
namespace Gesture {
    CEGUILongPressGestureRecognizer::CEGUILongPressGestureRecognizer():
    m_dlTimeInterval(2.0f)
    , m_fAllowableMovement(10.0f)
    , d_touchbegan(-1.0f)
    {
    }

    CEGUILongPressGestureRecognizer::~CEGUILongPressGestureRecognizer() {
    }

    void CEGUILongPressGestureRecognizer::addTarget(CEGUI::Window* pWindow) {
        CEGUIGestureRecognizer::addTarget(pWindow);
    }

    bool CEGUILongPressGestureRecognizer::onMouseButtonDown(const MouseEventArgs& e) {
        d_touchbegan = 0.0f;
        m_eState = GestureRecognizerStatePossible;
        return false;
    }
    
	void CEGUILongPressGestureRecognizer::updatePosition(const MouseEventArgs& e) {
		MouseEventArgs* event = (MouseEventArgs*)m_dEvent;
		*event = e;
    }
    
    float CEGUILongPressGestureRecognizer::abs(float n) {
        return n > 0 ? n : -n;
    }
    
    bool CEGUILongPressGestureRecognizer::onMouseMove(const MouseEventArgs& e) {
        const MouseEventArgs& mouseArgs = (const MouseEventArgs&)e;
        if (m_eState == GestureRecognizerStateBegan || m_eState == GestureRecognizerStateChanged) {
            updatePosition(e);
            m_eState = GestureRecognizerStateChanged;
            fireGestureEvent();
            return true;
        }
        if (abs(mouseArgs.moveDelta.d_x) + abs(mouseArgs.moveDelta.d_y) > m_fAllowableMovement) {
            d_touchbegan = -1.0f;
            printf("cancel longpress state.\n");
        }
        return false;
    }
    
    bool CEGUILongPressGestureRecognizer::onMouseButtonUp(const MouseEventArgs& e) {
        d_touchbegan = -1.0f;
        if (m_eState == GestureRecognizerStateBegan || m_eState == GestureRecognizerStateChanged) {
            updatePosition(e);
            m_eState = GestureRecognizerStateEnded;
            fireGestureEvent();
            return true;
        }
        return false;
    }
    
    bool CEGUILongPressGestureRecognizer::onMouseLeaves(const CEGUI::MouseEventArgs &e) {
        d_touchbegan = -1.0f;
        if (m_eState == GestureRecognizerStateBegan || m_eState == GestureRecognizerStateChanged) {
            updatePosition(e);
            m_eState = GestureRecognizerStateEnded;
            fireGestureEvent();
        }
        return false;

    }

	bool CEGUILongPressGestureRecognizer::update(const UpdateEventArgs& e)
	{
        const UpdateEventArgs& updateArgs = (const UpdateEventArgs&)e;
        if (d_touchbegan < 0)
            return false;
        d_touchbegan += updateArgs.d_timeSinceLastFrame;
        if (d_touchbegan > m_dlTimeInterval) {
			if (!m_dEvent)
			{
				m_dEvent = new UpdateEventArgs(e.window, updateArgs.d_timeSinceLastFrame);
				*m_dEvent = e;
			}
			m_eState = GestureRecognizerStateBegan;
            fireGestureEvent();
            d_touchbegan = -1.0f;
            return true;
        }
        return false;
    }
}
}