//  geom.h
//  engine

#ifndef __Nuclear_geom_H__
#define __Nuclear_geom_H__

#if (!defined WIN32) && (!defined _WIN32)
struct POINT
{
    int x,y;
};

struct RECT
{
    int left,top,right,bottom;
};

typedef RECT* LPRECT;
#endif

#endif
