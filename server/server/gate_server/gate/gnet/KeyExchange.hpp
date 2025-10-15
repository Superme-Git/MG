
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"
#include "LinkSession.hpp"
#include "UserLoginReq.hpp"
#include "DeliveryClient.hpp"

namespace gnet { 

	class KeyExchange : public GNET::Protocol
	{
		private:
			//TODO argument key is nonce
			GNET::Octets& GenerateKey(GNET::Octets& identity, GNET::Octets& password, GNET::Octets &nonce, GNET::Octets &key)
			{
				GNET::HMAC_MD5Hash hash;
				hash.SetParameter(password);
				hash.Update(identity);
				hash.Update(nonce);
				return hash.Final(key);//generate secret key
			}


		public:
#include "rpcgen/gnet/KeyExchange.inc"

			void Setup(/*const*/ GNET::Octets &identity,/* const */GNET::Octets &password, Manager *manager, Manager::Session::ID sid)
			{
				GNET::Security *random = GNET::Security::Create(GNET::RANDOM);
				random->Update(nonce.resize(16));
				random->Destroy();

				GNET::Octets key;
				GNET::Octets finalkey = GenerateKey(identity, password, nonce, key);
				manager->SetISecurity(sid, GNET::ARCFOURSECURITY, finalkey);//set session decrypt key
			}

			virtual void Process(Manager *, Manager::Session::ID sid)
			{
				glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
				glinkd::LinkSession * psession = lsm->GetLinkSession(sid);
				if(NULL == psession) 
				{
					glinkd::LinkServer::log.info("KeyExchange:Process:psession=null,sid=%d", sid);
					return;
				}
				glinkd::LinkServer::log.info("KeyExchange:Process:user(id=%d) sid=%d", psession->getUserId(), sid);
				GNET::Octets key;
				GNET::Octets finalkey = GenerateKey(psession->getIdentity(),psession->getResponse(),nonce,key);
				lsm->SetOSecurity(sid,GNET::ARCFOURSECURITY,finalkey);//set session encrypt key
				//finalkey.dump();
				//userlogin
				if(!glinkd::DeliveryClient::GetInstance()->SendProtocol(UserLoginReq(UserLoginArg(psession->getUserId(),sid,blkickuser,0))))
				{
					lsm->SendErr(sid,GNET::ERR_LINK_NOT_CONNECT_DELIVER,"link not connect deliver!");
				}
			}

	};

} 
