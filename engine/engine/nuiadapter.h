//  iadapter.h
//  engine
#ifndef __Nuclear_iadapter_H__
#define __Nuclear_iadapter_H__

namespace Nuclear
{
    class iadapter
	{
    public:
        virtual bool init() = 0;
        
        virtual int get_screen_w() = 0;
        virtual int get_screen_h() = 0;
        
        virtual int get_logic_w() = 0;
        virtual int get_logic_h() = 0;
        
        virtual int get_display_x() = 0;
        virtual int get_display_y() = 0;
        
        virtual int get_display_w() = 0;
        virtual int get_display_h() = 0;
    };
}

#endif
