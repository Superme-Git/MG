//
//  CEGUIGestureRecognizerManager.h
//  CEGUI
//
//  Created by 杨印宇 on 13-1-23.
//
//

#ifndef CEGUI_CEGUIGestureRecognizerManager_h
#define CEGUI_CEGUIGestureRecognizerManager_h
#include "CEGUIGestureRecognizer.h"
#include <map>
namespace CEGUI {
namespace Gesture {
enum GestureRecognizerType {
    LongPress, Pan
};
class CEGUIGestureRecognizerManager {
    
public:
    CEGUIGestureRecognizerManager();
    virtual ~CEGUIGestureRecognizerManager();
    bool AddRecoginzer(Window& window, GestureRecognizerType, CEGUI::Event::Subscriber);
    bool onMouseButtonDown(const MouseEventArgs& e);
    bool onMouseButtonUp(const MouseEventArgs& e);
    bool onMouseMove(const MouseEventArgs& e);
    bool onMouseLeaves(const MouseEventArgs& e);
    bool update(const UpdateEventArgs& e);
    
    bool CanReleaseCapture();
private:
    std::map<GestureRecognizerType, CEGUIGestureRecognizer*> d_recognizers;
};}}


#endif
