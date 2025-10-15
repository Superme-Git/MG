
#pragma once

#include "rpcgen.hpp"
#include "cert.hpp"
#include "CertKey.hpp"
#include "AuthClient.hpp"
#include "DeliverServer.hpp"

namespace gnet { 

class CertAnswer : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CertAnswer.inc"

	virtual	void Process(Manager *manager, Manager::Session::ID sid)
	{
		LOG_TRACE("CertAnswer ok,zoneid=%d,aid=%d",gdeliverd::DeliverServer::GetInstance()->GetZoneid(),gdeliverd::DeliverServer::GetInstance()->GetAid());
		gdeliverd::AuthClient *acm = gdeliverd::AuthClient::GetInstance();
		if(acm->bl_keepalive)
			return;
		gdeliverd::Cert cert((unsigned char*)authd_cert.begin(), authd_cert.size());
		if (cert.VerifyCert() == 0){
			GNET::Log::log(LOG_ERR,"Error to verify the cert");
			exit(-1);
		}

		if (cert.VerifyCertByCA() == 0){
			GNET::Log::log(LOG_ERR,"Error to verify the cert by CA");
			exit(-1);
		}

		int key_size = 8;
		GNET::Octets d_key1, d_key2;
		d_key1.resize(key_size);
		d_key2.resize(key_size);
	
		GNET::Security *random = GNET::Security::Create(GNET::RANDOM);
		random->Update(d_key1);
		random->Update(d_key2);
		random->Destroy();

		int buf_size = cert.GetBufferSize();
		GNET::Octets en_key1, en_key2;
		en_key1.resize(buf_size);
		en_key2.resize(buf_size);

		cert.Encrypt(key_size, (unsigned char*)d_key1.begin(), (unsigned char*)en_key1.begin());
		cert.Encrypt(key_size, (unsigned char*)d_key2.begin(), (unsigned char*)en_key2.begin());

		CertKey cert_key;
		cert_key.d_key1_encrypt.replace(en_key1.begin(),en_key1.size());
		cert_key.d_key2_encrypt.replace(en_key2.begin(),en_key2.size());		
		
		acm->SetISecurity(sid, GNET::ARCFOURSECURITY, d_key2);
		acm->SendProtocol(cert_key);
		acm->osec_key = d_key1; 
		acm->authd_cert = authd_cert;
		LOG_TRACE("send CertKey to au ok,zoneid=%d,aid=%d",gdeliverd::DeliverServer::GetInstance()->GetZoneid(),gdeliverd::DeliverServer::GetInstance()->GetAid());
	}

};

} 
