//
//  UIPanGestureRecognizer.h
//  FireClient
//
//  Created by 杨印宇 on 13-1-8.
//
//

#ifndef __FireClient__UIPanGestureRecognizer__
#define __FireClient__UIPanGestureRecognizer__

#include "CEGUIGestureRecognizer.h"
namespace CEGUI {
namespace Gesture {
    enum UIPanGestureRecognizerDirection {
        UIPanGestureRecognizerDirectionNone = 0,
        UIPanGestureRecognizerDirectionRight = 1 << 0,
        UIPanGestureRecognizerDirectionLeft  = 1 << 1,
        UIPanGestureRecognizerDirectionUp    = 1 << 2,
        UIPanGestureRecognizerDirectionDown  = 1 << 3
    };
//    union TanValue {
//        float d_val;
//        bool  d_infinite;
//    };
class CEGUIPanGestureRecognizer : public CEGUIGestureRecognizer {
public:
    CEGUIPanGestureRecognizer();
    virtual ~CEGUIPanGestureRecognizer();
public:
    int m_iMinimumNumberOfTouches;
    int m_iMaximumNumberOfTouches;

public:
	virtual void addTarget(CEGUI::Window*);
    virtual bool onMouseButtonDown(const MouseEventArgs& e);
    virtual bool onMouseButtonUp(const MouseEventArgs& e);
    virtual bool onMouseMove(const MouseEventArgs& e);
    virtual bool onMouseLeaves(const MouseEventArgs& e);
    const CEGUI::Vector2& velocityInView(CEGUI::Window* pWin = NULL);
	const CEGUI::Vector2& getAcceleration(){ return d_acceleration; }
	const String& getPushWndName(){ return d_PushWndName; }
	UIPanGestureRecognizerDirection GetPanDirection(int index = 0);

private:
    bool CheckBegan();
    
    void tryRefreshVelocity();
    float fabs(float f) { return f > 0.0f ? f : -f; }

private:
 //   float d_delX;
 //   float d_delY;
    double d_lastFrametime;
    CEGUI::Point d_lastpoint;
    CEGUI::Vector2 d_velocity;
    CEGUI::Vector2 d_acceleration;
    float d_refreshVelocityInterval;
    
    CEGUI::Vector2 d_velocitydata;
    double d_velocitydatatime;
    double d_velocitycalcperiod;
    float d_dragBeforeBegan;
    float d_dragMinMove;
	String d_PushWndName;
};
}
}

#endif /* defined(__FireClient__UIPanGestureRecognizer__) */
