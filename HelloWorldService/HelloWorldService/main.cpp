#include <binder/IPCThreadState.h>
#include <binder/IServiceManager.h>
#include <binder/ProcessState.h>
#include "HelloWorldService.hpp"

using namespace android;

int main(int argc, char **argv)
{
  defaultServiceManager()->addService(String16("HelloWorldService"), new HelloWorldService());
  ProcessState::self()->startThreadPool();
  android::ProcessState::self()->startThreadPool();
  IPCThreadState::self()->joinThreadPool();
  return 0;
}
