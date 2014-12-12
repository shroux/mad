#ifndef		IHELLOWORLDSERVICE
#define		IHELLOWORLDSERVICE

#include	<binder/IInterface.h>
#include	<binder/IBinder.h>

using namespace android;

class		IHelloWorldService : public IInterface {
protected: 
  enum {
    HELLOWORLD = IBinder::FIRST_CALL_TRANSACTION
  };

public:
  DECLARE_META_INTERFACE(HelloWorldService);
  virtual void	print() = 0;
  // virtual const String16&     getInterfaceDescriptor() const;
};

#endif
