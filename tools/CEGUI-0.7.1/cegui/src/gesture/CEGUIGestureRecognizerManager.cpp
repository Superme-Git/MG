//
//  CEGUIGestureRecognizerManager.cpp
//  CEGUI
//
//  Created by 杨印宇 on 13-1-23.
//
//

#include "../../include/gesture/CEGUIGestureRecognizerManager.h"
#include "../../include/gesture/CEGUILongPressGestureRecognizer.h"
#include "../../include/gesture/CEGUIPanGestureRecognizer.h"
namespace CEGUI {
namespace Gesture {
CEGUIGestureRecognizerManager::CEGUIGestureRecognizerManager() {
}

CEGUIGestureRecognizerManager::~CEGUIGestureRecognizerManager() {
    for (std::map<GestureRecognizerType, CEGUIGestureRecognizer*>::iterator iter = d_recognizers.begin(); iter != d_recognizers.end(); iter++) {
        delete iter->second;
    }
    d_recognizers.clear();
}

bool CEGUIGestureRecognizerManager::AddRecoginzer(Window& window, GestureRecognizerType type, CEGUI::Event::Subscriber subscriber) {
    CEGUIGestureRecognizer* pRecognizer = d_recognizers[type];

	if(pRecognizer&&pRecognizer->isEventPresent(CEGUIGestureRecognizer::EventGesture))
	{
		pRecognizer->removeAllEvents();
		delete pRecognizer;
		pRecognizer = NULL;
	}

    if (pRecognizer == NULL) {
        switch (type) {
            case LongPress:
                
                pRecognizer = new CEGUILongPressGestureRecognizer;
//                pRecognizer->SetType(LongPress);
                break;
            case Pan:
                pRecognizer = new CEGUIPanGestureRecognizer;
//                pRecognizer->SetType(Pan);
                break;
            default:
                return false;
        }
        d_recognizers[type] = pRecognizer;
    }
    if (pRecognizer) {
        pRecognizer->addTarget(&window);
        pRecognizer->subscribeEvent(CEGUIGestureRecognizer::EventGesture, subscriber);
        return true;
    }
    return false;
}
    
    bool CEGUIGestureRecognizerManager::update(const CEGUI::UpdateEventArgs &e) {
        bool handled = false;

		/* std::map<GestureRecognizerType, CEGUIGestureRecognizer*>::iterator iter = d_recognizers.begin();
		std::map<GestureRecognizerType, CEGUIGestureRecognizer*>::iterator iterEnd = d_recognizers.begin();

		for (; iter != iterEnd; iter++) {
		if (iter->second->update(e)) {
		handled = true;
		}
		}*/
        return handled;
    }
    
    bool CEGUIGestureRecognizerManager::onMouseButtonDown(const CEGUI::MouseEventArgs &e) {
        bool handled = false;
        for (std::map<GestureRecognizerType, CEGUIGestureRecognizer*>::iterator iter = d_recognizers.begin(); iter != d_recognizers.end(); iter++) {
            if (iter->second->onMouseButtonDown(e)) {
                handled = true;
            }
        }
        return handled;
    }
    
    bool CEGUIGestureRecognizerManager::onMouseButtonUp(const CEGUI::MouseEventArgs &e) {
        bool handled = false;
        for (std::map<GestureRecognizerType, CEGUIGestureRecognizer*>::iterator iter = d_recognizers.begin(); iter != d_recognizers.end(); iter++) {
            if (iter->second->onMouseButtonUp(e)) {
                handled = true;
            }
        }
        return handled;
    }
    
    bool CEGUIGestureRecognizerManager::onMouseMove(const CEGUI::MouseEventArgs &e) {
        bool handled = false;
        for (std::map<GestureRecognizerType, CEGUIGestureRecognizer*>::iterator iter = d_recognizers.begin(); iter != d_recognizers.end(); iter++) {
            if (iter->second->onMouseMove(e)) {
                handled = true;
            }
        }
        return handled;
    }
    
    bool CEGUIGestureRecognizerManager::onMouseLeaves(const CEGUI::MouseEventArgs &e) {
        bool handled = false;
        for (std::map<GestureRecognizerType, CEGUIGestureRecognizer*>::iterator iter = d_recognizers.begin(); iter != d_recognizers.end(); iter++) {
            if (iter->second->onMouseLeaves(e)) {
                handled = true;
            }
        }
        return handled;
    }
    
    bool CEGUIGestureRecognizerManager::CanReleaseCapture()
    {
       
        for (std::map<GestureRecognizerType, CEGUIGestureRecognizer*>::iterator iter = d_recognizers.begin(); iter != d_recognizers.end(); iter++)
        {
            //if (iter->first==Pan&&(iter->second->GetState()==GestureRecognizerStateBegan||iter->second->GetState()==GestureRecognizerStateChanged))
            if(iter->first==Pan && iter->second->inMove())
            {
                return false;
            }
        }
        return true;
        
    }

}}
