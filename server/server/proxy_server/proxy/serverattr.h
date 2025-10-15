#ifndef __GNET_SERVERATTR_H
#define __GNET_SERVERATTR_H
#include "rpcgen.hpp"

namespace gdeliverd
{
	class ServerAttr
	{
		union Flags
		{
			int _flags;
			struct {
				// attibutes
				int doubleExp:1;
				int doubleMoney:1;
				int doubleObject:1;
				int doubleSP:1;
				int freeZone:1;
			};
		};

		private:
		char load;
		int lambda;
		Flags flags;

		public:	
		ServerAttr() 
		{
			load = 0;
			lambda = 0;
			flags._flags = 0;
		}

		void SetLoad(char _load) 		{ load = _load; }
		void SetLambda(int _lambda) 		{ lambda = _lambda; }
		void SetDoubleExp(int _doubleExp ) 	{ flags.doubleExp = _doubleExp; }
		void SetDoubleMoney(int _doubleMoney ) 	{ flags.doubleMoney = _doubleMoney; }
		void SetDoubleObject(int _doubleObj ) 	{ flags.doubleObject = _doubleObj; }
		void SetDoubleSP(int _doubleSP ) 	{ flags.doubleSP = _doubleSP; }
		void SetFreeZone(int _freeZone) 	{ flags.freeZone = _freeZone; }
		
		char GetLoad() const 		{ return load; }
		int GetLambda() const		{ return lambda; }
		int GetDoubleExp() const	{ return flags.doubleExp; }
		int GetDoubleMoney() const 	{ return flags.doubleMoney; }
		int GetDoubleObject() const 	{ return flags.doubleObject; }
		int GetDoubleSP() const 	{ return flags.doubleSP; }
		int GetFreeZone() const 	{ return flags.freeZone;}
		int GetFlags() const 		{ return flags._flags; }
	};
}
#endif
