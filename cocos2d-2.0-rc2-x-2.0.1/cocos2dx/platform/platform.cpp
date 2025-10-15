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

#include "platform.h"

#include "CCStdC.h"

NS_CC_BEGIN

// ycl 废弃使用 cocos 定义的 cc_timeval 和相关函数，直接使用系统函数

//int CCTime::gettimeofdayCocos2d(struct cc_timeval *tp, void *tzp)
//{
//	CCLog("[CCTime::gettimeofdayCocos2d]");
//
//	CC_UNUSED_PARAM(tzp);
//	if (tp)
//	{
//
//#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
//
//		CCLog("getMilliSecondsJNI: %p", (void*)getMilliSecondsJNI);
//
//		int64_t ms = getMilliSecondsJNI();
//		if(ms == 0)
//		{
//			gettimeofday((struct timeval *)tp, 0);
//		}
//		else
//		{
//			CCLog("[CCTime::gettimeofdayCocos2d] ms=%lld", ms);
//			int64_t sec = ms / 1000;
//			tp->tv_sec = sec;
//			tp->tv_usec = ms * 1000 - tp->tv_sec * 1000000;
//
//			CCLog("[CCTime::gettimeofdayCocos2d] sec=%lld, tv_sec=%lld, tv_usec=%lld", sec, tp->tv_sec, tp->tv_usec);
//		}
//
//#else
//		gettimeofday((struct timeval *)tp, 0);
//#endif
//	}
//    return 0;
//}
//
//double CCTime::timersubCocos2d(struct timeval *start, struct timeval *end)
//{
//    if (! start || ! end)
//    {
//        return 0;
//    }
//    
//    return ((end->tv_sec*1000.0+end->tv_usec/1000.0) - (start->tv_sec*1000.0+start->tv_usec/1000.0));
//}

NS_CC_END
