#ifndef		BPHELLOWORLDSERVICE
#define		BPHELLOWORLDSERVICE

#include	<binder/IBinder.h>
#include	"IHelloWorldService.hpp"

using namespace android;

class		BpHelloWorldService : public BpInterface<IHelloWorldService>
{
public:
  BpHelloWorldService(const sp<IBinder>& impl) : BpInterface<IHelloWorldService>(impl) {}

  virtual void	print()
  {
    Parcel	data;
    Parcel	reply;
    remote()->transact(HELLOWORLD, data, &reply);
  }
};

IMPLEMENT_META_INTERFACE(HelloWorldService, "HelloWorldService");

#endif
