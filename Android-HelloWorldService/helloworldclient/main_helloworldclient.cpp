#define LOG_TAG "HelloWorld/Service"

#include <sys/types.h>
#include <unistd.h>
#include <grp.h>

#include <utils/IPCThreadState.h>
#include <utils/ProcessState.h>
#include <utils/IServiceManager.h>
#include <utils/Log.h>

#include "helloworld.h"

int main(int argc, char *argv[])
{
        android::sp<android::IServiceManager> sm = android::defaultServiceManager();
        android::sp<android::IBinder> binder;
        android::sp<IHelloWorldClient> shw;

        do {
                binder = sm->getService(android::String16(HELLOWORLD_NAME));
                if (binder != 0)
                        break;
                usleep(500000); // 0.5 s
        } while(true);

        shw = android::interface_cast<IHelloWorldClient>(binder);
        shw->hellothere();

	return(0);
}
