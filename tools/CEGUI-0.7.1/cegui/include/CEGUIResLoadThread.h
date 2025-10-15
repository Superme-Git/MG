//  CEGUIResLoadThread.h
//  CEGUI

#ifndef CEGUI_CEGUILoadResThread_h
#define CEGUI_CEGUILoadResThread_h

#include <platform/thread.h>
#include <platform/ksemaphore.h>

namespace CEGUI
{
    extern core::CSemaphore& g_LoadResSem;
    class CEGUIResLoadThread : public core::Thread
    {
    public:
        CEGUIResLoadThread();
        ~CEGUIResLoadThread();
        
        virtual void Run();
        static CEGUIResLoadThread* GetPtr();
		static void Destroy(); // yeqing 2015-10-19
    private:
        static CEGUIResLoadThread* s_Thread;
    };
}


#endif

