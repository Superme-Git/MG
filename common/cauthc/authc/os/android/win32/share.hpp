#ifndef __FIRENET_ANDROID_SHARE_HPP__
#define __FIRENET_ANDROID_SHARE_HPP__

#include "../libshare.h"

#define override
#define throwexceptionfunction throw()
#define _snprintf_s snprintf

#include "../../linux/mutex.hpp"
#include "../../linux/thread.hpp"
#include "../../linux/socketfunc.h"

#include "../../../timer.h"
#include "../../../pollio.h"
#include "../../../netio.h"
#include "../../../activeio.h"
#include "../../../ioengine.h"

#endif // #ifndef __FIRENET_SHARE_H__

