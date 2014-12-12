#ifndef		HELLOWORLDSERVICE
#define		HELLOWORLDSERVICE

#include	"BnHelloWorldService.hpp"

class		HelloWorldService : public BnHelloWorldService
{
  virtual void	print()
  {
    printf("%s\n", "Hello World");
  }
};

#endif
