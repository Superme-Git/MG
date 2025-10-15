
#include "../../include/cauthc.h"
#include <time.h>

class LoginUI : public authc::ILoginUI
{
	time_t		m_stop;
public:
	virtual void onAuthHandSet( int num ) {}
	virtual void onAuthMatrixCard( int x[3], int y[3] ) {}
	virtual void onAuthPhoneToken( ) {}
	virtual void onAuthOk( unsigned int userid,int ZoneID,int aid,int algorithm)
	{
		printf( "onAuthOk\n");
		startStop();
	}

	virtual void onAuthError( authc::Action a, int e, const std::string & detail)
	{
		printf( "onAuthError\n");
		startStop();
	}
	virtual void OnAnnounceForbidInfo(char type,int time,int createtime, const void* reason, const size_t reasonsize)
	{
		printf( "OnAnnounceForbidInfo\n");
		startStop();
	}

	virtual void OnInstantAddCashRep(int retcode,int userid,int reserved) {}

	virtual void OnGetUserCouponRep(int retcode,int userid,int remain_coupon,int ToDayExchangeCoupon,int ToDayRemainExchangeCoupon,int reserved1,int reserved2)  {}
	virtual void OnCouponExchangeRep(int retcode,int userid,int remain_coupon,int ToDayExchangeCoupon,int ToDayRemainExchangeCoupon,int reserved1,int reserved2)  {}
	virtual void OnSSOGetTicketRep(int retcode,authc::SSOUser info,std::string Ticket,std::string local_context,std::string reserved) {}

	virtual void dispatch(aio::Manager * manager, aio::SessionIDType mSID, aio::Protocol * p)
	{
		authc::PushProtocol( manager, mSID, p);
	}

	LoginUI()
		: m_stop( 0)
	{
	}
	virtual ~LoginUI() { }
private:
	void startStop()
	{
		m_stop = time( NULL);
	}
public:
	bool isStop()
	{
		if( 0 == m_stop)
			return false;
		time_t now = time( NULL);
		return now - m_stop >= 20;
	}
};

int main( int argc, char* argv[])
{
	authc::Startup();

	LoginUI	ui;

	authc::Login::Param	mParam;

	mParam.address = "127.0.0.1";
	mParam.host = "localhost";
	mParam.port = "29000";
	mParam.UserName = "tj19911";
	mParam.password = "123456";
	mParam.isCheckVersion = false;
	mParam.iskickuser = true;

	authc::Login* login =  authc::newLogin( mParam, &ui);

	login->Start();
	while( !ui.isStop())
	{
		authc::ProcessProtocol();
		usleep( 100);
	}
	printf( "login->close()\n");
	login->Close();
	usleep( 3000);
	printf( "authc::cleanup()\n");
	authc::Cleanup();
	return 0;
}
