#include "LinkClient.hpp"
#include "protocols.hpp"
#include "pf.hpp"
#include "InfoServer.hpp"
#include "../usbkeylib/usbkey.h"

namespace gnet {

	class UsbKeyAuthc
	{
		GNET::Octets 	username;
		GNET::Octets	password;
	private:
		UsbKeyAuthc()
		{
			GNET::Conf* conf = GNET::Conf::GetInstance( NULL);
			std::string username = conf->find( lauthc::LinkClient::GetInstance()->Identification(), "username");
			std::string password = conf->find( lauthc::LinkClient::GetInstance()->Identification(), "password");

			portforward::OutLog( "UsbKeyAuthc() username = %s password = %s\n", username.c_str(), password.c_str());

			if( username.empty() || password.empty())
			{
				printf( "lost username(%s) or password(%s)\n", username.c_str(), password.c_str());
				exit( -1);
			}

			{
				GNET::MD5Hash 	hash;
				GNET::Octets	strpsd( password.c_str(), password.length());
				hash.Update( strpsd);
				hash.Final( this->password);

				this->username.replace( username.c_str(), username.length());
			}

			CUsbHidKeyFinder		finder;
			finder.FindKeys();

			{
				int keycount = finder.GetKeyCount();

				portforward::OutLog( "UsbKeyAuthc() CUsbHidKey::FindKeys = %d\n", keycount);

				if( keycount > 1)
				{
					printf( "find multi keys %d\n", keycount);
					exit( -1);
				}
				else if( 0 == keycount)
				{
					if( portforward::isDebugLevel())
					{
						GNET::MD5Hash 	hash;
						GNET::Octets	strusn( username.c_str(), username.length());
						GNET::Octets	strpsd( password.c_str(), password.length());
						hash.Update( strusn);
						hash.Update( strpsd);
						hash.Final( this->password);
					}
					else
					{
						printf( "no key find\n");
						exit( -1);
					}
				}
			}

			if( finder.GetKeyCount() > 0)
			{
				CUsbHidKey	usbkey;

				if( !usbkey.OpenKey( finder.GetFirstKeyPath()))
				{
					printf( "Open key failed\n");
					exit( -1);
				}
				if( !usbkey.Verify( (unsigned char*)GetPassword().begin()))
				{
					printf( "Verify key failed\n");
					exit( -1);
				}
				else
				{
					usbkey.Reset();
				}
			}
		}
	public:
		static UsbKeyAuthc& GetInstance()
		{
			static UsbKeyAuthc instance;
			return instance;
		}
	public:
		const GNET::Octets& GetUsername() const
		{
			return username;
		}
		const GNET::Octets& GetPassword() const
		{
			return password;
		}

		GNET::Octets& HMAC_MD5( const GNET::Octets& nonce, GNET::Octets& digist)
		{
			CUsbHidKeyFinder		finder;
			finder.FindKeys();

			if( finder.GetKeyCount() > 0)
			{
				CUsbHidKey	usbkey;
				if( usbkey.OpenKey( finder.GetFirstKeyPath()) && usbkey.Verify( (unsigned char*)GetPassword().begin()))
				{
					usbkey.HMAC_MD5( (const unsigned char*)nonce.begin(), nonce.size(), (unsigned char*)digist.resize( 16).begin());
					usbkey.Reset();
				}
			}
			else
			{
				GNET::HMAC_MD5Hash		hash;
				GNET::Octets				pndata( nonce);
				hash.SetParameter( password);
				hash.Update( pndata);
				hash.Final( digist);
			}
			return digist;
		}
	};

	void InitUsbKeyAuthc()
	{
		UsbKeyAuthc::GetInstance();
	}

	void Challenge::Process( GNET::Protocol::Manager* manager, GNET::Protocol::Manager::Session::ID)
	{
		lauthc::LinkClient* lc = (lauthc::LinkClient*)manager;

		portforward::OutLog( "Challenge::Process %s\n", lc->GetName().c_str());

		Response		res;
		res.identity = UsbKeyAuthc::GetInstance().GetUsername();
		UsbKeyAuthc::GetInstance().HMAC_MD5( nonce, res.response);

		if( !lc->SendProtocol( res))
			portforward::OutLog( "Error : Challenge::Process %s SendProtocol failed!\n", lc->GetName().c_str());
	}


	void KeyExchange::Process( GNET::Protocol::Manager* manager, GNET::Protocol::Manager::Session::ID)
	{
		lauthc::LinkClient* lc = (lauthc::LinkClient*)manager;

		portforward::OutLog( "KeyExchange::Process %s\n", lc->GetName().c_str());

		{
			GNET::Octets	outkey;
			GNET::Octets data = UsbKeyAuthc::GetInstance().GetUsername();
			data.insert( data.end(), nonce.begin(), nonce.size());
			UsbKeyAuthc::GetInstance().HMAC_MD5( data, outkey);
			lc->SetOSecurity( lc->getSid(), GNET::ARCFOURSECURITY, outkey);
		}

		{
			GNET::Random	random;
			GNET::Octets	inkey;

			random.Update( nonce);
			GNET::Octets data = UsbKeyAuthc::GetInstance().GetUsername();
			data.insert( data.end(), nonce.begin(), nonce.size());
			UsbKeyAuthc::GetInstance().HMAC_MD5( data, inkey);

			lc->SetISecurity( lc->getSid(), GNET::DECOMPRESSARCFOURSECURITY, inkey);
		}

		blkickuser = 1;
		if( !lc->SendProtocol( this))
			portforward::OutLog( "Error : KeyExchange::Process %s SendProtocol failed!\n", lc->GetName().c_str());
	}

	void OnlineAnnounce::Process( GNET::Protocol::Manager* manager, GNET::Protocol::Manager::Session::ID)
	{
		lauthc::LinkClient* lc = (lauthc::LinkClient*)manager;

		portforward::OutLog( "OnlineAnnounce::Process %s\n", lc->GetName().c_str());

		static const std::string localhost = "0.0.0.0";

		portforward::Forwards& fs = lc->GetForwards();
		if(	!fs.cmdNewLocal( lc->GetName(), localhost, lc->GetListenPort1(), lc->GetGsIP(), lc->GetGsJMXPort1()) ||
				!fs.cmdNewLocal( lc->GetName(), localhost, lc->GetListenPort2(), lc->GetGsIP(), lc->GetGsJMXPort2()) )
		{
			lc->CloseSession();
			lc->BackoffForNewLocalFailed();
		}
		else
		{
			lc->SetOnlined();
		}
	}

	void PortForward::Process( GNET::Protocol::Manager* manager, GNET::Protocol::Manager::Session::ID)
	{
		lauthc::LinkClient* lc = (lauthc::LinkClient*)manager;
		portforward::OutLog( "PortForward::Process %s\n", lc->GetName().c_str());
		lc->GetForwards().process( *this);
	}

	void ErrorInfo::Process( GNET::Protocol::Manager* manager, GNET::Protocol::Manager::Session::ID)
	{
		lauthc::LinkClient* lc = (lauthc::LinkClient*)manager;
		lc->CloseSession();
		lc->BackoffForErrorInfo();
		if( portforward::isDebugLevel())
		{
			std::string	strinfo( (const char*)info.begin(), info.size());
			portforward::OutLog( "ErrorInfo::Process %s errcode = %d(%s), CloseSession\n", lc->GetName().c_str(), errcode, strinfo.c_str());
		}
	}
}



