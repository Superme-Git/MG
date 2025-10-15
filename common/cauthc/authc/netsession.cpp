#include "win32/share.hpp"

namespace FireNet {

	NetSession::~NetSession ()
	{
		isec->Destroy();
		osec->Destroy();
		delete locker;
	}
	NetSession::NetSession(const NetSession &rhs) : assoc_io(rhs.assoc_io), sending(false), closing(false), close_discard(rhs.close_discard),
		isec(rhs.isec->Clone()), osec(rhs.osec->Clone()), urgency_support(rhs.urgency_support), locker( new FireNet::Mutex)
	{
		ibuffer.reserve(rhs.ibuffer.capacity());
		obuffer.reserve(rhs.obuffer.capacity());
	}

	NetSession::NetSession() : assoc_io(NULL), sending(false), closing(false), close_discard(false), ibuffer(DEFAULTIOBUF), obuffer(DEFAULTIOBUF),
		isec(Security::Create(enumSECURITY_NULL)), osec(Security::Create(enumSECURITY_NULL))
		, urgency_support(false), locker( new FireNet::Mutex)
	{}

	Octets& NetSession::GetOBuffer()
	{
		osec->UpdateSelf(osecbuf);
		obuffer.insert(obuffer.end(), osecbuf.begin(), osecbuf.end());
		osecbuf.clear();
		return obuffer;
	}

	Octets& NetSession::GetIBuffer()
	{
		return ibuffer;
	}

	void NetSession::SendFinish()
	{
		if ( NoMoreData() )
		{
			sending = false;
			assoc_io->ForbidSend();
		}
	}

	bool NetSession::Output(const Octets &data)
	{
		if (obuffer.size() + osecbuf.size() + data.size() > obuffer.capacity())
			return false;
		osecbuf.insert(osecbuf.end(), data.begin(), data.end());
		return true;
	}

	Octets& NetSession::Input()
	{
		{
			FireNet::Mutex::Scoped l(*locker);
			isec->UpdateSelf(ibuffer);
		}
		isecbuf.insert(isecbuf.end(), ibuffer.begin(), ibuffer.end());
		ibuffer.clear();
		assoc_io->PermitRecv();
		return isecbuf;
	}

	void NetSession::SendReady()
	{
		if( sending )
			return;
		sending = true;
		assoc_io->PermitSend();
	}

	void NetSession::SetISecurity(Security::Type type, const Octets &key)
	{
		FireNet::Mutex::Scoped l(*locker);
		isec->Destroy();
		isec = Security::Create(type);
		isec->SetParameter(key);
	}

	void NetSession::SetOSecurity(Security::Type type, const Octets &key)
	{
		FireNet::Mutex::Scoped l(*locker);
		osec->Destroy();
		osec = Security::Create(type);
		osec->SetParameter(key);
	}

	void NetSession::Close(const char* info, bool locked)
	{
		if (locked)
		{
			_Close();
		}
		else
		{
			FireNet::Mutex::Scoped l(locker);
			_Close();
		}
		closeinfo.append(info).append(";");
	}

	void NetSession::Destroy() { delete this; }

	const std::string NetSession::getCloseInfo() const
	{
#if USE_PROTOCOLINFO_LIST
		return CloseInfo + " " + GetLastProtocolInfo();
#else
		return closeinfo;
#endif
	}

	void NetSession::_Close()
	{
		if ( closing)
			return;
		closing = true;
		assoc_io->PermitSend();
	}

#if USE_PROTOCOLINFO_LIST
	void NetSession::AddLastProtocolInfo(const char* info)
	{
		if (protocolinfo_list.size() >= 10)
		{
			protocolinfo_list.pop_front();
		}
		protocolinfo_list.push_back(info);
	}

	const std::string NetSession::GetLastProtocolInfo() const
	{
		std::string infos;
		std::list<std::string>::const_iterator it = protocolinfo_list.begin();
		for (int i = 0; it != protocolinfo_list.end(); i++, ++it)
		{
			char text[512];
			sprintf(text, "[%d]{%s}", i, (*it).c_str());
			infos.insert(0, text);
		}
		infos.insert(0, "protocolinfos:");
		return infos;
	}
#endif

} // namespace FireNet {

