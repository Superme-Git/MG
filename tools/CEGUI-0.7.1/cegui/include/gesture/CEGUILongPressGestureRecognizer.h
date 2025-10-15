//
//  UILongPressGestureRecognizer.h
//  FireClient
//
//  Created by 杨印宇 on 13-1-6.
//
//

#ifndef __FireClient__UILongPressGestureRecognizer__
#define __FireClient__UILongPressGestureRecognizer__
#include "CEGUIGestureRecognizer.h"
namespace CEGUI {
namespace Gesture {
    class CEGUILongPressGestureRecognizer : public CEGUIGestureRecognizer {
    public:
        CEGUILongPressGestureRecognizer();
        virtual ~CEGUILongPressGestureRecognizer();
        virtual void addTarget(CEGUI::Window* pWindow);
        virtual bool onMouseButtonDown(const MouseEventArgs& e);
        virtual bool onMouseButtonUp(const MouseEventArgs& e);
        virtual bool onMouseMove(const MouseEventArgs& e);
        virtual bool onMouseLeaves(const MouseEventArgs& e);
		virtual bool update(const UpdateEventArgs& e);

	private:
		float abs(float);
		void updatePosition(const MouseEventArgs& e);

    public:
        float m_dlTimeInterval;
        float  m_fAllowableMovement;

    private:
        float d_touchbegan;
    };
}
}
#endif /* defined(__FireClient__UILongPressGestureRecognizer__) */
