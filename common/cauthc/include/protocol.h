#pragma once
#include "../net/FNet.h"

namespace aio {

	class Protocol;
    class LuaProtocol;
	class Mutex;

	class Manager : public FireNet::INetIO
	{
	public:
		class Session : public FireNet::NetSession
		{
			friend class Protocol;
			friend class Manager;

			struct Stream : public FireNet::Marshal::OctetsStream
			{
				Session *session;
				Stream(Session *s) : session(s)
				{}
			};
		public:
			typedef FireNet::NetSessionID ID;

			ID getSID() const
			{
				return mSID;
			}
		private:
			typedef std::map<ID, Session *> Map;
			static ID nextsid();

			ID mSID;
			Stream mInputStream;
			std::deque<FireNet::Octets> mOutputStream;
			size_t mOutputStreamBytes;
			Manager* manager;

			 FireNet::NetSession *Clone () const;

			void OnOpen(const FireNet::SockAddr& local, const FireNet::SockAddr& peer, const FireNet::Connector& mConnector);
			void OnOpen( const FireNet::Connector& mConnector);
			void OnClose();
			void OnAbort(const FireNet::SockAddr &mSockAddr, const FireNet::Connector& mConnector);
			void OnRecv();
			void OnSend();
			bool NoMoreData() const;
			bool Send( FireNet::Octets ps, bool urg);
			bool Send( const Protocol *protocol, bool urg);
			bool Send( Protocol *protocol, bool urg);

			Session(Manager *mMember);
			Session(const Session &rhs);
		};

	protected:
		FireNet::Mutex* mutex;
		typedef std::map<Session::ID, Session *> SessionMap;
		SessionMap sessions;
		void AddSession(Session*,const FireNet::Connector&, const std::string&);

	protected:
		Manager();
		virtual ~Manager();

		virtual void OnAddSession(Session::ID, const FireNet::Connector&, const std::string&)  = 0;
		virtual void OnDelSession(Session*)  = 0;
		virtual void AbortSession( const FireNet::SockAddr &, Session*, const FireNet::Connector&) = 0;

		virtual void Process(Session::ID mSID, FireNet::Octets & input);

		virtual void DispatchUnknownProtocol(Session::ID , FireNet::ProtocolType type, FireNet::Octets & pdata);

		void AddProtocolInfo(Session::ID mSID, FireNet::ProtocolType type, uint32_t size, const char* info);
		std::string GetProtocolInfo(Session::ID mSID);
	public:
		void Close(Session::ID mSID, const char * info = NULL);
		void Send(Session::ID mSID, const FireNet::Octets & protocol);
		void Send(Session::ID mSID, const Protocol & protocol);
		void Send(Session::ID mSID, const Protocol * protocol);

		void SetISecurity(Session::ID mSID, FireNet::Security::Type type, const FireNet::Octets &key);
		void SetOSecurity(Session::ID mSID, FireNet::Security::Type type, const FireNet::Octets &key);

		virtual void Dispatch(Session::ID mSID, Protocol * p);
        virtual void DispatchLuaProtocols(Session::ID mSID, LuaProtocol * p);
		void CloseAll();
	public:
		void ConnectTo( const FireNet::SockAddr& addr, const FireNet::Connector& mConnector);
#ifdef OS_IOS
		void ConnectTo2(const FireNet::SockAddr& addr, const FireNet::Connector& mConnector, addrinfo* pAI);
#endif
	};

	class Protocol : public FireNet::Marshal
	{
	public:
		typedef FireNet::ProtocolType Type;

	protected:
		Protocol();
		explicit Protocol(Type t);
	public:
		virtual ~Protocol();

		FireNet::Octets encode() const;

	public:
		typedef aio::Manager Manager;

		Type getType() const;
        virtual int PriorPolicy() const { return 0;}
        virtual int WaitingProtocol() const { return -1;}
		virtual bool SizePolicy(size_t ) const;
		virtual void Dispatch( Manager::Session::ID mSID, Manager * manager);
 

		virtual void Process( Manager*, Manager::Session::ID) = 0;
		virtual Protocol * Clone() const = 0;

	protected:
		Type type;
	public:
		struct Stub
		{
			Type type;
			virtual Protocol* create() = 0;
		};

		static Protocol* Create(Type type);
		static void AddStub(Stub * stub);
		static void DelStub(Type type);
		static void DelStub(Stub * stub);

		template < typename T >
		struct TStub : public Stub
		{
			TStub()
			{
				type = T::PROTOCOL_TYPE;
			}
			virtual Protocol* create()
			{
				return new T();
			}
		};
	};
    
    class LuaProtocol
    {
        typedef FireNet::ProtocolType Type;
    public:
        Type type;
        FireNet::Octets data;
        
        void DispatchLuaProtocols( Manager::Session::ID mSID, Manager * manager);
    };

	class ProtocolException : public std::exception
	{
		char _what[1024];
	public:
		ProtocolException(Protocol::Type type, unsigned int size, const char * desc);
		virtual const char * what() const throw ();
	};

} // namespace aio

