#include "win32/share.hpp" 
#include <map>

#include <stdio.h>
#include <stdlib.h>
#include <exception>

#include "../../platform/log/CoreLog.h"


//add temporary log for testin. by liugeng
#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

namespace aio
{
	namespace helper
	{
		static Manager::Session::ID static_session_id = 0;
		static FireNet::Mutex static_session_id_mutex;

	} // namespace helper {

	Manager::Session::ID Manager::Session::nextsid()
	{
		FireNet::Mutex::Scoped scoped(helper::static_session_id_mutex);
		return helper::static_session_id ++;
	}

	FireNet::NetSession *Manager::Session::Clone () const
	{
		return new Session(*this);
	}

#ifdef OS_IOS
	void Manager::Session::OnOpen(const FireNet::SockAddr& local, const FireNet::SockAddr& peer, const FireNet::Connector& mConnector)
	{
		std::string		hostaddr;
		{
			const struct sockaddr_in6* addr = peer.get<struct sockaddr_in6>();            
            char dstIP[256];
            inet_ntop(AF_INET, (void*)&addr->sin6_addr, dstIP, sizeof(dstIP));
            hostaddr = dstIP;
		}
		manager->AddSession(this, mConnector, hostaddr);
	}
#else
	void Manager::Session::OnOpen(const FireNet::SockAddr& local, const FireNet::SockAddr& peer, const FireNet::Connector& mConnector)
	{
		std::string		hostaddr;
		{
			const struct sockaddr_in* addr = peer.get<struct sockaddr_in>();
			hostaddr = inet_ntoa( addr->sin_addr);
		}
		manager->AddSession(this, mConnector, hostaddr);
	}
#endif

	void Manager::Session::OnOpen( const FireNet::Connector& mConnector)
	{
		std::string		hostaddr;
		manager->AddSession( this, mConnector, hostaddr);
	}
	void Manager::Session::OnClose()
	{
		{
			FireNet::Mutex::Scoped scoped( manager->mutex);
			manager->sessions.erase( this->mSID);
		}
		manager->OnDelSession(this);

		// yeqing 2015-10-22
		while (mOutputStreamBytes)
		{
			mOutputStreamBytes -= mOutputStream.front().size();
			mOutputStream.front().clear();
			mOutputStream.pop_front();
		}
	}
	void Manager::Session::OnAbort(const FireNet::SockAddr &mSockAddr, const FireNet::Connector& mConnector)
	{
		manager->AbortSession(mSockAddr, this, mConnector);
	}

	void Manager::Session::OnRecv()
	{
		FireNet::Octets& input = Input();
		mInputStream.insert(mInputStream.end(), input.begin(), input.end());
		input.clear();
		manager->Process( mSID, mInputStream);
	}

	void Manager::Session::OnSend()
	{
		// yeqing 2015-10-22
		if (urgency_support)
		{
			if (mOutputStreamBytes)
			{
				Output(mOutputStream.front());
				mOutputStreamBytes -= mOutputStream.front().size();
				mOutputStream.front().clear();
				mOutputStream.pop_front();
			}
		}
		else
		{
			while (mOutputStreamBytes)
			{
				Output(mOutputStream.front());
				mOutputStreamBytes -= mOutputStream.front().size();
				mOutputStream.front().clear();
				mOutputStream.pop_front();
			}
		}
	}

	bool Manager::Session::NoMoreData() const
	{
		return mOutputStreamBytes == 0;
	}
	bool Manager::Session::Send(FireNet::Octets ps, bool urg)
	{
		FireNet::Mutex::Scoped l(locker);
		if (ps.size())
		{
			mOutputStreamBytes += ps.size();
			if ( urgency_support && urg )
				mOutputStream.push_front(ps);
			else
				mOutputStream.push_back(ps);
			SendReady();
			return true;
		}
		return false;
	}

	bool Manager::Session::Send( const Protocol *protocol, bool urg)
	{
		return Send(protocol->encode(), urg);
	}
	bool Manager::Session::Send( Protocol *protocol, bool urg)
	{
		return Send(protocol->encode(), urg);
	}

	Manager::Session::Session(Manager *mMember)
		: mSID(nextsid()), mInputStream(this), mOutputStreamBytes(0), manager(mMember)
	{}
	Manager::Session::Session(const Session &rhs)
		: NetSession(rhs), mSID(nextsid()), mInputStream(this), mOutputStreamBytes(rhs.mOutputStreamBytes), manager(rhs.manager)
	{}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void Manager::ConnectTo( const FireNet::SockAddr& addr, const FireNet::Connector& mConnector)
	{
		FireNet::NetActiveIO::Open( Session( this), addr, mConnector);
	}
#ifdef OS_IOS
	void Manager::ConnectTo2(const FireNet::SockAddr& addr, const FireNet::Connector& mConnector, addrinfo* pAI)
    {
        FireNet::NetActiveIO::Open2( Session( this), addr, mConnector, pAI);
    }
#endif

	////////////////////////////////////////////////////////////////////////////////////////
	// Manager

	Manager::Manager()
		: mutex( new FireNet::Mutex)
	{
	}

	Manager::~Manager()
	{
		delete mutex;
	}

	void Manager::Send(Session::ID mSID, const FireNet::Octets & protocol)
	{
		FireNet::Mutex::Scoped scoped(*mutex);
		SessionMap::const_iterator it = sessions.find(mSID);
		if (it != sessions.end())
			it->second->Send(protocol, false);
	}

	void Manager::Send(Session::ID mSID, const Protocol & protocol)
	{
		Send(mSID, protocol.encode());
	}

	void Manager::Send(Session::ID mSID, const Protocol * protocol)
	{
		Send(mSID, protocol->encode());
	}

	void Manager::Close(Session::ID mSID, const char * info /*=NULL*/)
	{
		FireNet::Mutex::Scoped scoped(*mutex);
		SessionMap::iterator it = sessions.find(mSID);
		if (it != sessions.end())
			it->second->Close(info?info:"Protocol::Manager::Close");
	}

	void Manager::CloseAll()
	{
		SessionMap tmp;
		{
			FireNet::Mutex::Scoped scoped(*mutex);
			tmp.swap(sessions);
		}
		for (SessionMap::iterator it = tmp.begin(), ie = tmp.end(); it != ie; ++it)
			it->second->Close("Protocol::Manager::CloseAll");
	}

	void Manager::SetISecurity(Session::ID mSID, FireNet::Security::Type type, const FireNet::Octets &key)
	{
		FireNet::Mutex::Scoped scoped(*mutex);
		SessionMap::const_iterator it = sessions.find(mSID);
		if (it != sessions.end())
			it->second->SetISecurity(type, key);
	}


	void Manager::SetOSecurity(Session::ID mSID, FireNet::Security::Type type, const FireNet::Octets &key)
	{
		FireNet::Mutex::Scoped scoped(*mutex);
		SessionMap::const_iterator it = sessions.find(mSID);
		if (it != sessions.end())
			it->second->SetOSecurity(type, key);
	}

	void Manager::DispatchUnknownProtocol(Session::ID , Protocol::Type type, FireNet::Octets & pdata)
	{
		throw ProtocolException(type, (unsigned int)pdata.size(), "Protocol Unknown");
	}

	void Manager::AddProtocolInfo(Session::ID mSID, FireNet::ProtocolType type, uint32_t size, const char* info)
	{
#if USE_PROTOCOLINFO_LIST
		char text[512];
		sprintf(text, "sid=%d type=%d size=%d info=%s", mSID, type, size, info);

		FireNet::Mutex::Scoped scoped(*mutex);
		SessionMap::const_iterator it = sessions.find(mSID);
		if (it != sessions.end())
			it->second->AddLastProtocolInfo(text);
#endif
	}

	std::string Manager::GetProtocolInfo(Session::ID mSID)
	{
#if USE_PROTOCOLINFO_LIST
		FireNet::Mutex::Scoped scoped(*mutex);
		SessionMap::const_iterator it = sessions.find(mSID);
		if (it != sessions.end())
			return it->second->GetLastProtocolInfo();
#endif
		return "";
	}

	void Manager::Process(Session::ID mSID, FireNet::Octets & input)
	{
		FireNet::Marshal::OctetsStream mOutputStream(input);
		size_t mark = 0; // mOutputStream.position(). MUST BE ZERO.
		try
		{
			while (mOutputStream.size() > mark)
			{
				Protocol::Type type;
				uint32_t size;

				mOutputStream >> FireNet::CompactUINT(type) >> FireNet::CompactUINT(size);

				if (size > mOutputStream.size() - mOutputStream.position())
				{
					break; // protocol data not enough, wait for the left stream
				}

				AddProtocolInfo(mSID, type, size, "");

				mark = mOutputStream.position() + size;

				try
				{
					std::auto_ptr<Protocol> p(Protocol::Create(type));
					if (p.get())
					{
#if (defined WIN32) && (defined _DEBUG)
						char text[64];
						sprintf(text, "[Dispatch Protocol] %d\n", type);
						OutputDebugStringA(text);
//#elif defined ANDROID //add temporary log for testin. by liugeng
						LOGE("[Dispatch Protocol] %d\n", type);
#endif
						p->unmarshal(mOutputStream);
						if (mOutputStream.position() != mark) {
							throw ProtocolException(type, size, "data.size not match");
						}
						p.release()->Dispatch(mSID, this);
					}
					else
					{
#if (defined WIN32) && (defined _DEBUG)
						char text[64];
						sprintf(text, "[Dispatch Lua Protocol] %d\n", type);
						OutputDebugStringA(text);
//#elif defined ANDROID //add temporary log for testin. by liugeng
						LOGE("[Dispatch Lua Protocol] %d\n", type);
#endif
                        std::auto_ptr<LuaProtocol> lp( new LuaProtocol);
						lp->type = type;
						lp->data.resize(size);
						mOutputStream.pop_byte((char*)lp->data.begin(), size);
                        lp.release()->DispatchLuaProtocols(mSID, this);
					}
				}
				catch (ProtocolException & ex)
				{
					SDLOG_ERR(L"aio::ProtocolException : aio::Manager::Process(...) type = %d \n", type);
					throw ex;
				}
				catch (std::exception & ex)
				{
					SDLOG_ERR(L"std::exception : aio::Manager::Process(...) type = %d \n", type);
					throw ProtocolException(type, size, ex.what());
				}
				catch (FireNet::Marshal::Exception & )
				{
					SDLOG_ERR(L"FireNet::Marshal::Exception : aio::Manager::Process(...) type = %d \n", type);
					throw ProtocolException(type, size, "NuclearMarshal::Exception");
				}
			}
		}
		catch (FireNet::Marshal::Exception & ex)
		{
			// skip. protocol piece.
			char text[512];
			sprintf(text, "[Process] (FireNet::Marshal::Exception & ex):%s", ex.what.c_str());
			AddProtocolInfo(mSID, -1, -1, text);
		}
		catch (ProtocolException &ex)
		{
			char text[512];
			sprintf(text, "[Process] (ProtocolException &ex):%s", ex.what());
			AddProtocolInfo(mSID, -1, -1, text);
		}
		catch (std::exception & ex)
		{
			char text[512];
			sprintf(text, "[Process] (std::exception & ex):%s", ex.what());
			AddProtocolInfo(mSID, -1, -1, text);
		}
		catch (...)
		{
			char text[512];
			sprintf(text, "[Process] (...)");
			AddProtocolInfo(mSID, -1, -1, text);
		}
		input.erase(0, mark);
	}


	void Manager::Dispatch(Session::ID mSID, Protocol * p)
	{
		p->Process(this, mSID);
		delete p;
	}
    
    void Manager::DispatchLuaProtocols(Session::ID mSID, LuaProtocol * p)
    {
        delete p;
    }

	void Manager::AddSession(Session* session,const FireNet::Connector& mConnector, const std::string& hostaddr)
	{
		{
			FireNet::Mutex::Scoped scoped(*mutex);
			if (!sessions.insert(std::make_pair( session->getSID(), session)).second)
				throw std::runtime_error("AddSession duplicate sid");
		}
		OnAddSession( session->getSID(), mConnector, hostaddr);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	Protocol::Protocol() {}
	Protocol::Protocol(Type t)
		: type(t)
	{}
	Protocol::~Protocol() {}

	FireNet::Octets Protocol::encode() const
	{
		FireNet::Marshal::OctetsStream mOutputStream;
		{
			FireNet::Marshal::OctetsStream data;
			data << *this;
			if ( SizePolicy(data.size()) )
				mOutputStream << FireNet::CompactUINT(type) << data;
			else
				abort(); // XXX error report
		}
		return mOutputStream;
	}

	bool Protocol::SizePolicy( size_t ) const
	{
		return true;
	}

	void Protocol::Dispatch( Manager::Session::ID mSID, Manager * manager)
	{
		manager->Dispatch(mSID, this);
	}


	Protocol::Type Protocol::getType() const
	{
		return this->type;
	}


	typedef std::map<Protocol::Type, Protocol::Stub *> StubMap;

	FireNet::Mutex & stubMutex() { static FireNet::Mutex mutex; return mutex; }
	StubMap & stubMap() { static StubMap stubs; return stubs; }

	void Protocol::AddStub(Stub * stub)
	{
		FireNet::Mutex::Scoped scoped(stubMutex());
		if (!stubMap().insert(std::make_pair(stub->type, stub)).second)
			abort();
	}

	void Protocol::DelStub(Type type)
	{
		FireNet::Mutex::Scoped scoped(stubMutex());
		stubMap().erase(type);
	}

	Protocol* Protocol::Create(Type type)
	{
//#ifdef ANDROID
		FireNet::Mutex::Scoped scoped(stubMutex());
//#endif
		StubMap::iterator it = stubMap().find(type);
		if (it != stubMap().end())
			return it->second->create();
		return NULL;
	}

	void Protocol::DelStub( Stub* stub)
	{
		DelStub( stub->type);
	}


    void LuaProtocol::DispatchLuaProtocols( Manager::Session::ID mSID, Manager * manager)
    {
        manager->DispatchLuaProtocols(mSID, this);
    }
    
	ProtocolException::ProtocolException(Protocol::Type type, unsigned int size, const char * desc)
	{
		int len = _snprintf_s(_what, sizeof(_what) - 1, "ProtocolException type=%d size=%d desc='%s'", type, size, desc);
		if (len < 0) len = 0;
		_what[len] = 0;
	}
	const char* ProtocolException::what() const throw()
	{
		return _what;
	}

} // namespace aio
