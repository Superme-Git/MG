//
//  ScreenRecordController.cpp
//  FireClient
//
//  Created by user on 16/5/6.
//
//

#include "ScreenRecordController.h"
#include "FireClientAppDelegate.h"

void StartRecordToC_Plus()
{
    [g_viewController startRecord];
}

void StopRecordToC_Plus()
{
    [g_viewController stopRecord];
}