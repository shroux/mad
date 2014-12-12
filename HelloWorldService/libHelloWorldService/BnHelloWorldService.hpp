#ifndef			BNHELLOWORLDSERVICE
#define			BNHELLOWORLDSERVICE

#include		"IHelloWorldService.hpp"

using namespace		android;

class			BnHelloWorldService : public BnInterface<IHelloWorldService>
{
  virtual status_t	onTransact(uint32_t code, const Parcel& data,
				   Parcel* reply, uint32_t flags = 0)
  {
    switch(code) {
    case HELLOWORLD: {
      print();
      return NO_ERROR;
    } break;
    default:
      return BBinder::onTransact(code, data, reply, flags);
    }
  }
};

#endif
