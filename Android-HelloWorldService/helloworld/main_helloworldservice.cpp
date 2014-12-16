#define LOG_TAG "HelloWorld/Service"

#include <sys/types.h>
#include <unistd.h>
#include <grp.h>

#include <utils/IPCThreadState.h>
#include <utils/ProcessState.h>
#include <utils/IServiceManager.h>
#include <utils/Log.h>

#include "helloworldservice.h"

int main(int argc, char *argv[])
{
	HelloWorldService::instantiate();
	android::ProcessState::self()->startThreadPool();

	android::IPCThreadState::self()->joinThreadPool();
	return(0);
}
