/****************************************************************************
Copyright (c) 2010 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/

#ifndef __CC_TOUCH_H__
#define __CC_TOUCH_H__

#include "cocoa/CCObject.h"
#include "cocoa/CCGeometry.h"

NS_CC_BEGIN

/**
 * @addtogroup input
 * @{
 */

class CC_DLL CCTouch : public CCObject
{
public:
    CCTouch() 
        : m_nId(0)
		, m_Force(0)
    {}
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	/** returns the current touch location in OpenGL coordinates */
    CCPoint getLocation() const;
    /** returns the previous touch location in OpenGL coordinates */
    CCPoint getPreviousLocation() const;
    /** returns the start touch location in OpenGL coordinates */
    CCPoint getStartLocation() const;
    /** returns the delta of 2 current touches locations in screen coordinates */
    CCPoint getDelta() const;
    /** returns the current touch location in screen coordinates */
    CCPoint getLocationInView() const;
    /** returns the previous touch location in screen coordinates */
    CCPoint getPreviousLocationInView() const;
    /** returns the start touch location in screen coordinates */
    CCPoint getStartLocationInView() const;
#else
    CCPoint locationInView() { return m_point; }
    CCPoint previousLocationInView() { return m_prevPoint; }
#endif
    void setTouchInfo(int id, float x, float y, float force = 0)
    {
        m_nId = id;
        m_prevPoint = m_point;
        m_point.x   = x;
        m_point.y   = y;
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		if (!m_startPointCaptured)
        {
            m_startPoint = m_point;
            m_startPointCaptured = true;
        }
#endif
    }

    int getID() const
    {
        return m_nId;
    }

	float getForce() const { return m_Force; }

private:
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	bool m_startPointCaptured;
    CCPoint m_startPoint;
#endif
    int m_nId;
    CCPoint m_point;
    CCPoint m_prevPoint;
	float m_Force;  // Á¦¶È
};

class CC_DLL CCEvent : public CCObject
{
};

// end of input group
/// @}

NS_CC_END

#endif  // __PLATFORM_TOUCH_H__
