#include "sysinfo.h"

static void sysInfo_Loop();

#ifdef ENABLE_IARM
static IARM_Result_t isSysInfoAvailable(void *arg);
#endif // ifdef ENABLE_IARM

int main(int argc, char *argv[])
{
#ifdef ENABLE_IARM
    IARM_Bus_RegisterCall(IARM_BUS_SYSINFO_API_isAvailable, isSysInfoAvailable);
#endif
#ifdef ENABLE_SD_NOTIFY
    sd_notifyf(0, "READY=1\n"
               "STATUS=systeminfo is Successfully Initialized\n"
               "MAINPID=%lu",
               (unsigned long) getpid());
#endif

    sysInfo_Loop();

}

#ifdef ENABLE_IARM
IARM_Result_t isSysInfoAvailable(void *arg)
{
    LOG_ENTRY_EXIT;
    LOG_INFO("[%s] IARM_BUS_SYSINFO_API_isAvailable is called", MODULE_NAME);
    return IARM_RESULT_SUCCESS;
}
#endif // ENABLE_IARM

void systemInfo_Loop()
{
    time_t curr = 0;
    while(1)
    {
        time(&curr);
#if !defined(ENABLE_XCAM_SUPPORT) && !defined(XHB1) && !defined(XHC3)
        printf("I-ARM SYSINFO-INFO: HeartBeat at %s\r\n", ctime(&curr));
#endif
        sleep(60);
    }
}
