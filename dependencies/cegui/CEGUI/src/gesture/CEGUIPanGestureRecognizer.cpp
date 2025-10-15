//
//  UIPanGestureRecognizer.cpp
//  FireClient
//
//  Created by 杨印宇 on 13-1-8.
//
//

#include "gesture/CEGUIPanGestureRecognizer.h"
//#define SUBSCRIBER(X,T) pWindow->subscribeEvent(X, CEGUI::Event::Subscriber(T, this))
namespace CEGUI {
namespace Gesture {
    CEGUIPanGestureRecognizer::CEGUIPanGestureRecognizer() : m_iMinimumNumberOfTouches(1),
    m_iMaximumNumberOfTouches(5),
    d_lastFrametime(0.0f),
    d_velocitycalcperiod(0.0f),
    d_refreshVelocityInterval(0.03f),
    d_dragBeforeBegan(0.0f),
    d_dragMinMove(30.0f)
    {
        d_lastpoint.d_x = 0.0f;
        d_lastpoint.d_y = 0.0f;
        d_velocity.d_x = 0.0f;
        d_velocity.d_y = 0.0f;
        
        d_velocitydata.d_x = 0.0f;
        d_velocitydata.d_y = 0.0f;
        d_velocitydatatime = 0.0f;
    }
    
    CEGUIPanGestureRecognizer::~CEGUIPanGestureRecognizer() 
	{
	}
    
    void CEGUIPanGestureRecognizer::addTarget(CEGUI::Window *pWindow) {
        CEGUIGestureRecognizer::addTarget(pWindow);
    }
    
    bool CEGUIPanGestureRecognizer::onMouseButtonDown(const MouseEventArgs& e) {
        const MouseEventArgs& mouseArgs = (const MouseEventArgs&)e;
        d_lastpoint.d_x = mouseArgs.position.d_x;
        d_lastpoint.d_y = mouseArgs.position.d_y;
        d_lastFrametime = CEGUI::System::getSingleton().currentTime();
        d_velocity.d_x = 0.0f;
        d_velocity.d_y = 0.0f;
        d_dragBeforeBegan = 0.0f;
        d_velocitydata.d_x = 0.0f;
        d_velocitydata.d_y = 0.0f;
        d_velocitydatatime = 0.0f;
        d_inMove = false;
		d_PushWndName = e.window->getName();
        return false;
    }
    
    bool CEGUIPanGestureRecognizer::CheckBegan() {
        if (d_dragBeforeBegan >= d_dragMinMove)
            return true;
        if (fabs(d_velocity.d_x) > 500.0f || fabs(d_velocity.d_y) > 500.0f) {
            return true;
        }
        return false;
    }
    
	bool CEGUIPanGestureRecognizer::onMouseMove(const MouseEventArgs& e) {
        double current = CEGUI::System::getSingleton().currentTime();
        d_lastFrametime = current;
        const MouseEventArgs& mouseArgs = (const MouseEventArgs&)e;
        float xOffset=fabs(mouseArgs.position.d_x - d_lastpoint.d_x);
        float yOffset=fabs(mouseArgs.position.d_y - d_lastpoint.d_y);
        d_lastpoint.d_x = mouseArgs.position.d_x;
        d_lastpoint.d_y = mouseArgs.position.d_y;
        d_inMove = true;
        tryRefreshVelocity();
        if (m_eState != GestureRecognizerStateBegan && m_eState != GestureRecognizerStateChanged) {
            d_dragBeforeBegan += fabs(xOffset + yOffset);
            if (!CheckBegan()) {
                return false;
            }
            d_dragBeforeBegan = 0.0f;
        }
        if (m_eState == GestureRecognizerStateBegan || m_eState == GestureRecognizerStateChanged) {
            m_eState = GestureRecognizerStateChanged;
        } else {
            m_eState = GestureRecognizerStateBegan;
        }

		if (!m_dEvent)
		{
			m_dEvent = new MouseEventArgs(e.window);
		}
		*m_dEvent = e;
        fireGestureEvent();
        return true;
    }
    
    
    
    bool CEGUIPanGestureRecognizer::onMouseButtonUp(const MouseEventArgs& e) {
        d_inMove = false;
		d_PushWndName = "";
        if (m_eState != GestureRecognizerStateBegan && m_eState != GestureRecognizerStateChanged)
            return false;
        const MouseEventArgs& mouseArgs = (const MouseEventArgs&)e;
        m_eState = GestureRecognizerStateEnded;
        d_lastpoint.d_x = mouseArgs.position.d_x;
        d_lastpoint.d_y = mouseArgs.position.d_y;
        //printf("mouse up at position(%f,%f)\n", d_lastpoint.d_x, d_lastpoint.d_y);
        double current = CEGUI::System::getSingleton().currentTime();
        d_lastFrametime = current;
        
        tryRefreshVelocity();
		if (!m_dEvent)
		{
			m_dEvent = new MouseEventArgs(e.window);
			*m_dEvent = e;
		}
		fireGestureEvent();
        return true;
    }
    
    bool CEGUIPanGestureRecognizer::onMouseLeaves(const MouseEventArgs& e)
    {
        return false;
    }
    
    const CEGUI::Vector2& CEGUIPanGestureRecognizer::velocityInView(CEGUI::Window* pWin) {
        return d_velocity;
    }
    
    void CEGUIPanGestureRecognizer::tryRefreshVelocity() {
        //printf("d_lastpoint: %f \n",d_lastpoint.d_y);
        assert(d_refreshVelocityInterval != 0.0f);
        if (d_velocitydatatime < 0.01f &&
            d_velocitydatatime > -0.01f) {
            d_velocitydata.d_x = d_lastpoint.d_x;
            d_velocitydata.d_y = d_lastpoint.d_y;
            d_velocitydatatime = d_lastFrametime;
            return;
        }
        if ((d_velocity.d_x < 0.01f && d_velocity.d_x > -0.01f
             && d_velocity.d_y < 0.01f && d_velocity.d_y > -0.01f )
            || d_lastFrametime -d_velocitydatatime > d_refreshVelocityInterval) {
            double new_v_calc_period = d_lastFrametime - d_velocitydatatime;
            float new_v_x = (d_lastpoint.d_x - d_velocitydata.d_x) / (new_v_calc_period);
            float new_v_y = (d_lastpoint.d_y - d_velocitydata.d_y) / (new_v_calc_period);
            if(d_velocitycalcperiod > 0)
            {
                d_acceleration.d_x = (new_v_x - d_velocity.d_x) / (new_v_calc_period + d_velocitycalcperiod);
                d_acceleration.d_y = (new_v_y - d_velocity.d_y) / (new_v_calc_period + d_velocitycalcperiod);
            }
            
            d_velocitycalcperiod = new_v_calc_period;
            d_velocity.d_x = new_v_x;
            d_velocity.d_y = new_v_y;
            //printf("d_velocity.y: %f \n",new_v_y);
            d_velocitydatatime = 0.0f;
            d_velocitydata.d_x = 0.0f;
            d_velocitydata.d_y = 0.0f;
        }
    }
    UIPanGestureRecognizerDirection CEGUIPanGestureRecognizer::GetPanDirection(int index) {
        if (d_velocity.d_x > 0.0f && d_velocity.d_x > fabs(d_velocity.d_y)) {
            return UIPanGestureRecognizerDirectionRight;
        }
        if (d_velocity.d_x < 0.0f && -d_velocity.d_x > fabs(d_velocity.d_y)) {
            return UIPanGestureRecognizerDirectionLeft;
        }
        if (d_velocity.d_y > 0.0f && d_velocity.d_y > fabs(d_velocity.d_x)) {
            return UIPanGestureRecognizerDirectionDown;
        }
        if (d_velocity.d_y < 0.0f && -d_velocity.d_y > fabs(d_velocity.d_x)) {
            return UIPanGestureRecognizerDirectionUp;
        }
            
        return UIPanGestureRecognizerDirectionNone;
    }
}
}