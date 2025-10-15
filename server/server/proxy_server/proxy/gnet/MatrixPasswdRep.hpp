
#pragma once

#include "rpcgen.hpp"
#include "io/security.h"

namespace gnet { 

class MatrixPasswdRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixPasswdRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }
/*
	void GenerateKey(GNET::Octets &identity, GNET::Octets &password, GNET::Octets &nonce, GNET::Octets &key)
	{
		GNET::HMAC_MD5Hash hash;
		hash.SetParameter(identity);
		hash.Update(password);
		hash.Update(nonce);
		hash.Final(in_key);
	}

	void Setup(GNET::Octets &identity, GNET::Octets &password)
	{
		GNET::Security *random = GNET::Security::Create(GNET::RANDOM);
		random->Update(in_nonce.resize(16));
		random->Destroy();
		GenerateKey(identity, password, in_nonce, in_key);
	}
*/

};

} 
