//////////////////////////////////////////////////////////////////////////
// File:		LocationDetector_win32.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/05/13
//////////////////////////////////////////////////////////////////////////

#include <LocationDetector.h>

LocationDetector::LocationDetector()
{}

LocationDetector::~LocationDetector()
{}

bool LocationDetector::isAvailable() const
{
	return false;
}

bool LocationDetector::isAllowed() const
{
	return false;
}

void LocationDetector::startDetect()
{
	onFinishDetect(false);
}