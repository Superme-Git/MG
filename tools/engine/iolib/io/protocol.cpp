/*
#include "protocol.h"
#include "compress.h"
*/
#ifdef _MSC_VER
#include "../winiolib.h" 
#endif

#include "binder.h"

namespace GNET
{
Protocol::Manager::Session::ID Protocol::Manager::Session::session_id = 0;
Thread::Mutex Protocol::Manager::Session::session_id_mutex("Protocol::Manager::Session::session_id_mutex");
Protocol::Represent::Map* Protocol::Represent::map = new Protocol::Represent::Map();

#ifdef _MSC_VER
Thread::Mutex* Protocol::Represent::locker = new Thread::Mutex("Protocol::Represent::locker");
#else
Thread::Mutex Protocol::Represent::locker("Protocol::Represent::locker");
#endif

Protocol * Protocol::Decode(const Manager::Session::Stream &is)
{
	if (is.eos())
		return NULL;
	Protocol *protocol = NULL;
	Protocol::Type type = 0;
	size_t size = 0;
	try
	{
		if (is.check_policy)
		{
			is >> Marshal::Begin >> CompactUINT(type) >> CompactUINT(size) >> Marshal::Rollback;

			if (!is.session->StatePolicy(type) || !is.session->manager->InputPolicy(type, size))
			{
				Log::log(LOG_ERR,"Protocol state or size policy error. type=%d,size=%d",type,size);
				throw Protocol::Exception();
			}
			is.check_policy = false;
		}

		Manager::Session::Stream data(is.session);
		data.reserve(size);
		is >> Marshal::Begin >> CompactUINT(type) >> data >> Marshal::Commit;
		if ((protocol = Protocol::Create(type))) 
		{
			data >> *protocol;
		}
		is.check_policy = true;
	}
	catch (Marshal::Exception &)
	{
		if (protocol)
		{
			Log::log(LOG_ERR,"Protocol(type=%d,size=%d) Decode Error.", type, size);
			protocol->Destroy();
			throw Protocol::Exception();
		}
		is >> Marshal::Rollback;
	}
	return protocol;
}

};
