//  usememory.cpp
//  share

#include "usememory.h"
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <assert.h>
bool GetMemUsage(double &aTotal, double &aUsermem, double &aFreemem)
{
	assert(false);
	return false;
}
#else

bool GetMemUsage(double &aTotal, double &aUsermem, double &aFreemem)
{
    //size_t length;
    //int mib[6];
    //
    //int pagesize;
    //mib[0] = CTL_HW;
    //mib[1] = HW_PAGESIZE;
    //length = sizeof(pagesize);
    //
    //if(sysctl(mib, 2, &pagesize, &length, NULL, 0) < 0)
    //{
    //    return false;
    //}
    //
    ////        int phymem;
    ////        mib[0] = CTL_HW;
    ////        mib[1] = HW_PHYSMEM;
    ////        length = sizeof(phymem);
    ////
    ////        if (sysctl(mib, 2, &phymem, &length, NULL, 0) < 0) {
    ////            return false;
    ////        }
    ////
    ////        aPhymem = pagesize * phymem;
    //
    //int usermem;
    //mib[0] = CTL_HW;
    //mib[1] = HW_USERMEM;
    //length = sizeof(usermem);
    //
    //if (sysctl(mib, 2, &usermem, &length, NULL, 0) < 0) {
    //    return false;
    //}
    //aUsermem = usermem * pagesize;
    //
    //vm_statistics_data_t st;
    //mach_msg_type_number_t count = HOST_VM_INFO_COUNT;
    //if(host_statistics(mach_host_self(), HOST_VM_INFO, (host_info_t)&st, &count) != KERN_SUCCESS)
    //{
    //    return false;
    //}
    //
    //double total = st.wire_count + st.active_count + st.inactive_count + st.free_count;
    //
    //aTotal = total * pagesize;
    //
    //aFreemem = st.free_count * pagesize;
    
    return true;
}
#endif
