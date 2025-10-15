//
//  CEGUIGestureRecognizer.h
//  FireClient
//
//  Created by —Ó”°”Ó on 13-1-6.
//
//

#ifndef __FireClient__UIGestureRecognizer__
#define __FireClient__UIGestureRecognizer__
#include "CEGUIWindow.h"
#include "CEGUIEvent.h"
#include "CEGUIGestureRecognizerManager.h"
namespace CEGUI {
namespace Gesture {
    enum GestureRecognizerState {
        GestureRecognizerStatePossible,   // the recognizer has not yet recognized its gesture, but may be evaluating touch events. this is the default state
        
        GestureRecognizerStateBegan,      // the recognizer has received touches recognized as the gesture. the action method will be called at the next turn of the run loop
        GestureRecognizerStateChanged,    // the recognizer has received touches recognized as a change to the gesture. the action method will be called at the next turn of the run loop
        GestureRecognizerStateEnded,      // the recognizer has received touches recognized as the end of the gesture. the action method will be called at the next turn of the run loop and the recognizer will be reset to UIGestureRecognizerStatePossible
        GestureRecognizerStateCancelled,  // the recognizer has received touches resulting in the cancellation of the gesture. the action method will be called at the next turn of the run loop. the recognizer will be reset to UIGestureRecognizerStatePossible
        
        GestureRecognizerStateFailed,     // the recognizer has received a touch sequence that can not be recognized as the gesture. the action method will not be called and the recognizer will be reset to UIGestureRecognizerStatePossible
        
        // Discrete Gestures ®C gesture recognizers that recognize a discrete event but do not report changes (for example, a tap) do not transition through the Began and Changed states and can not fail or be cancelled
        GestureRecognizerStateRecognized = GestureRecognizerStateEnded // the recognizer has received touches recognized as the gesture. the action method will be called at the next turn of the run loop and the recognizer will be reset to UIGestureRecognizerStatePossible
    };

	class CEGUIGestureRecognizer;
    class SlotFunctorBase
    {
    public:
        virtual ~SlotFunctorBase() {};
        virtual bool operator()(CEGUIGestureRecognizer* args) = 0;
    };
    template<typename T>
    class MemberFunctionSlot : public SlotFunctorBase
    {
    public:
        //! Member function slot type.
        typedef bool(T::*MemberFunctionType)(CEGUIGestureRecognizer* args);
        MemberFunctionSlot(MemberFunctionType func, T* obj) :
        d_function(func),
        d_object(obj)
        {}
        
        virtual bool operator()(CEGUIGestureRecognizer* args)
        {
            return (d_object->*d_function)(args);
        }
    private:
        MemberFunctionType d_function;
        T* d_object;
    };

    class CEGUIGestureRecognizer : public EventSet {
    public:
        static const String EventNameSpace;
        static const String EventGesture;
    public:
        CEGUIGestureRecognizer();
        virtual ~CEGUIGestureRecognizer();
    public:
        virtual void addTarget(Window*);
        virtual bool onMouseButtonDown(const MouseEventArgs& e) { return false; }
        virtual bool onMouseButtonUp(const MouseEventArgs& e) { return false; }
        virtual bool onMouseMove(const MouseEventArgs& e) { return false; }
        virtual bool onMouseLeaves(const MouseEventArgs& e) { return false; }
        virtual bool update(const UpdateEventArgs& e) { return false; }

		GestureRecognizerState GetState() { return m_eState; }
        bool inMove() {return d_inMove;}
        const CEGUI::EventArgs* GetEvent() const;
        int numberOfTouches() { return 1; }

    protected:
        bool d_inMove;
        GestureRecognizerState	m_eState;
		MouseEventArgs*			m_dEvent;
        
        void fireGestureEvent();
    };
    
};
}
#endif /* defined(__FireClient__UIGestureRecognizer__) */
