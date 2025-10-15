#include "stdafx.h"
#include <vector>

class ProtoVariableDef
{
public:
	CString mName;//变量名字.
	CString mType;//变量类型.
	CString mKey;
	CString mValue;
	CString mAttr;
	CString mDefault;
};

class ProtoDef
{
public:
	CString mName;//协议名字.
	std::vector<ProtoVariableDef> mValues;
};

std::vector<ProtoDef> ga_ProtoDef;

void myMakeProtol(const char* inputFileName,const char* outputFileName)
{
	int pos = MultiByteToWideChar(CP_ACP, 0, inputFileName, strlen(inputFileName), 0, 0);
	wchar_t* wsPath = (wchar_t*)malloc((pos + 1) * 2);
	MultiByteToWideChar(CP_ACP, 0, inputFileName, strlen(inputFileName), wsPath, pos);

	CString s;
	std::vector<CString> cs;
	CStdioFile file;
	if (file.Open(wsPath, CFile::modeRead | CFile::readOnly))
	{
		file.ReadString(s);
		file.Close();
	}
	free(wsPath);

	for (int i = 0; i < cs.size(); ++i)
	{
		if (cs[i] != "protol")
		{
			printf("format error!\n");
			return;
		}
		++i; if (i == cs.size()) return;
		ProtoDef protoDef;
		protoDef.mName = cs[i];
		while (true)
		{
			if (cs[i] == "variable")
			{
				ProtoVariableDef var;
				++i; if (i == cs.size()) return;
				while (true)
				{
					if (cs[i] == "name=")
					{
						++i; if (i == cs.size()) return;
						var.mName = cs[i];
					}
					else if (cs[i] == "type=")
					{
						++i; if (i == cs.size()) return;
						var.mType = cs[i];
					}
					else if (cs[i] == "key=")
					{
						++i; if (i == cs.size()) return;
						var.mKey = cs[i];
					}
					else if (cs[i] == "value=")
					{
						++i; if (i == cs.size()) return;
						var.mValue = cs[i];
					}
					else if (cs[i] == "attr=")
					{
						++i; if (i == cs.size()) return;
						var.mAttr = cs[i];

					}
					else if (cs[i] == "default")
					{
						++i; if (i == cs.size()) return;
						var.mDefault = cs[i];
					}
					else
						break;
				}
				protoDef.mValues.push_back(var);
			}
		}

		//
		ga_ProtoDef.push_back(protoDef);
	}

	FILE* fp;
	fopen_s(&fp, outputFileName, "w+");
	if (fp)
	{
		fprintf(fp, "#include \"win32 / share.hpp\"");
		fprintf(fp, "#include \".. / include / authc.hpp\"");
		fprintf(fp, "#include \"rpcgen.hpp\"");
		fprintf(fp, "#include \"protocols.hpp\"");
		fprintf(fp, "#include \"loginbase.h\"");
		fprintf(fp, "		//#include \".. / .. / platform / log / CoreLog.h\"");
		fprintf(fp, "");
		fprintf(fp, "");
		fprintf(fp, "		namespace authc {");
		fprintf(fp, "#define GAMEID 88");
		fprintf(fp, "#define GAMEKEY \"b18a26ffc632752987bd24a7bf0353f3\"");
		fprintf(fp, "#define SESSION \"ewogICJhdmF0YXIiIDogIiIsCiAgImNoYW5uZWwiIDogImxvY29qb3kiLAogICJwbGF0Zm9ybWlkIiA6ICIyMTIwOTQ5MTYiLAogICJuaWNrIiA6ICIiLAogICJsb2dpbnRpbWUiIDogIjIwMTUtMTAtMTIgMDA6MDA6MTkiLAogICJjcGluZm8iIDogIiIsCiAgInNpZ24iIDogIjAzZjE0NGY4NGUzMWIzNGUyMWNhZTg3NzkxYTEwZmE1IiwKICAic2V4IiA6ICIiLAogICJhcHBpZCIgOiAiMTAwMTIiLAogICJuYW1lIiA6ICIiCn0 = \"");
		fprintf(fp, "#ifdef ANDROID	 ");
		fprintf(fp, "#define CHANNEL \"208800000\"");
		fprintf(fp, "#else");
		fprintf(fp, "#define CHANNEL \"108800000\"");
		fprintf(fp, "#endif");
		fprintf(fp, "");
		fprintf(fp, "#define CHILDCHANNEL \"\"");
		fprintf(fp, "#define DEVICEID \"33:44::55 : 66 : 77 : 88\"");
		fprintf(fp, "#define SERVERID \"1101961001\"");
		fprintf(fp, "class NetLoginModule : public Login, public LOGINBASE::CNotify");
		fprintf(fp, "{");
		fprintf(fp, "	class NetKeepAliveTimer : public GNET::Timer::Observer");
		fprintf(fp, "	{");
		fprintf(fp, "		NetLoginModule*		mImpl;");
		fprintf(fp, "		time_t			mLastSendTime;");
		fprintf(fp, "		time_t			mPeriod;");
		fprintf(fp, "	public:");
		fprintf(fp, "		NetKeepAliveTimer(NetLoginModule* impl)");
		fprintf(fp, "			: mImpl(impl)");
		fprintf(fp, "		{}");
		fprintf(fp, "		virtual ~NetKeepAliveTimer() {}");
		fprintf(fp, "	public:");
		fprintf(fp, "		virtual void UpdateSelf()");
		fprintf(fp, "		{");
		fprintf(fp, "			time_t now = GNET::Timer::GetTime();");
		fprintf(fp, "			if (now - mLastSendTime >= mPeriod)");
		fprintf(fp, "			{");
		fprintf(fp, "				mImpl->OnKeepAliveTimer();");
		fprintf(fp, "				mLastSendTime = now;");
		fprintf(fp, "			}");
		fprintf(fp, "		}");
		fprintf(fp, "		void NetLoginModule_StartTime(int period)");
		fprintf(fp, "		{");
		fprintf(fp, "			GNET::Timer::Detach(this);");
		fprintf(fp, "			GNET::Timer::Attach(this);");
		fprintf(fp, "			mLastSendTime = GNET::Timer::GetTime();");
		fprintf(fp, "			mPeriod = period;");
		fprintf(fp, "		}");
		fprintf(fp, "		void NetLoginModule_CancelTime()");
		fprintf(fp, "		{");
		fprintf(fp, "			GNET::Timer::Detach(this);");
		fprintf(fp, "		}");
		fprintf(fp, "	};");
		fprintf(fp, "public:");
		fprintf(fp, "	lid_t                               mLid;");
		fprintf(fp, "	LOGINBASE::CLogin *                 mLoginBase;");
		fprintf(fp, "	bool                                mAcready;");
		fprintf(fp, "	gnet::AddictionControl              mAc;");
		fprintf(fp, "	ILoginUI  *                         mLoginUI;");
		fprintf(fp, "	Param                               mParam;");
		fprintf(fp, "	aio::Protocol::Manager::Session::ID mSID;");
		fprintf(fp, "	std::string                         mHostAddress;");
		fprintf(fp, "");
		fprintf(fp, "	NetKeepAliveTimer					mKeepalivetimer;");
		fprintf(fp, "	bool								mSaready;");
		fprintf(fp, "	gnet::ServerAttr					mServerAttr;");
		fprintf(fp, "	int                                 mServerPing;");
		fprintf(fp, "");
		fprintf(fp, "	int									mSecureLevel;");
		fprintf(fp, "");
		fprintf(fp, "	NetLoginModule(const Login::Param & _param, ILoginUI * mLoginUI)");
		fprintf(fp, "		: mParam(_param), mKeepalivetimer(this)");
		fprintf(fp, "	{");
		fprintf(fp, "		mAcready = false;");
		fprintf(fp, "		mSaready = false;");
		fprintf(fp, "		mServerPing = 0;");
		fprintf(fp, "		mSecureLevel = 0;");
		fprintf(fp, "		mSID = 0;");
		fprintf(fp, "		this->mLoginUI = mLoginUI;");
		fprintf(fp, "		mLoginBase = new LOGINBASE::CLogin(*this);");
		fprintf(fp, "	}");
		fprintf(fp, "");
		fprintf(fp, "	ILoginUI * NetLoginModule_GetLoginUI() { return mLoginUI; }");
		fprintf(fp, "");
		fprintf(fp, "	bool NetLoginModule_CheckVersion(int Version) { return !mParam.isCheckVersion || Version == mParam.Version; }");
		fprintf(fp, "	virtual ~NetLoginModule();");
		fprintf(fp, "");
		fprintf(fp, "	/////////////////////////////////////");
		fprintf(fp, "	// KeepAlive");
		fprintf(fp, "	void OnKeepAliveTimer();");
		fprintf(fp, "	virtual void NetLoginModule_SetupKeepAlive(int period) override");
		fprintf(fp, "	{");
		fprintf(fp, "		if (period <= 0)");
		fprintf(fp, "		{");
		fprintf(fp, "			mKeepalivetimer.NetLoginModule_CancelTime();");
		fprintf(fp, "			return;");
		fprintf(fp, "		}");
		fprintf(fp, "");
		fprintf(fp, "		if (period < 1000)");
		fprintf(fp, "			period = 1000;");
		fprintf(fp, "		mKeepalivetimer.NetLoginModule_StartTime(period);");
		fprintf(fp, "	}");
		fprintf(fp, "");
		fprintf(fp, "	bool NetLoginModule_IsUseUSBKey() const { assert(mLoginBase); return mLoginBase->NetLoginModule_IsUseUSBKey(); }");
		fprintf(fp, "	virtual aio::Protocol::Manager::Session::ID GetSessionID() override { return mSID; }");
		fprintf(fp, "	///////////////////////////////////////////////////////////////////////////");
		fprintf(fp, "	// authc::Login");
		fprintf(fp, "	virtual lid_t GetID() override;");
		fprintf(fp, "	virtual void Start() override;");
		fprintf(fp, "	virtual void Close() override;");
		fprintf(fp, "	virtual void NetLoginModule_SendMatrixCardResponse(int v[3]) override;");
		fprintf(fp, "	virtual void sendHandSetResponse(int num) override;");
		fprintf(fp, "	virtual void NetLoginModule_SendPhoneToKeyResponse(int num) override;");
		fprintf(fp, "	virtual void send(const aio::Protocol & protocol) override;");
		fprintf(fp, "	virtual void send(const GNET::Octets & luaprotocol) override;");
		fprintf(fp, "	virtual bool NetLoginModule_GetAddiction(Addiction &) override;");
		fprintf(fp, "	virtual bool NetLoginModule_GetServerInfo(ServerInfo &) override;");
		fprintf(fp, "");
		fprintf(fp, "	virtual int  NetLoginModule_GetServerVersion() override      { return mParam.Version; }");
		fprintf(fp, "	virtual int NetLoginModule_GetServerPing() override          { return this->mServerPing; }");
		fprintf(fp, "	virtual int NetLoginModule_GetServerLevel() override		 { return this->mSecureLevel; }");
		fprintf(fp, "	virtual std::string getHostAddress() override { return mHostAddress; }");
		fprintf(fp, "");
		fprintf(fp, "	virtual bool NetLoginModule_CanEnterWorld() override;");
		fprintf(fp, "");
		fprintf(fp, "	virtual void NetLoginModule_InstantAddCash(const std::string & cardnum, const std::string & cardpasswd) override;");
		fprintf(fp, "	virtual void NetLoginModule_GetUserCoupon() override;");
		fprintf(fp, "	virtual void NetLoginModule_SSOGetTicket(SSOGetTicketInfo &) override;");
		fprintf(fp, "	virtual void NetLoginModule_CouponExchange(int coupon_number, int cash_number, int reserved1, int reserved2) override;");
		fprintf(fp, "	////////////////////////////////////////////////////////////////////////////");
		fprintf(fp, "	// LOGINBASE::CNotify");
		fprintf(fp, "	virtual void NetLoginModule_OnGetUserNameAndPassword(std::string& UserName, std::string& password) override;");
		fprintf(fp, "	virtual void NetLoginModule_OnChallenge(const GNET::Octets& responce) override;");
		fprintf(fp, "	virtual void NetLoginModule_OnMatrixCard(int x[3], int y[3]) override;");
		fprintf(fp, "	virtual void NetLoginModule_OnHandSet(int num) override;");
		fprintf(fp, "	virtual void NetLoginModule_OnPhoneToken() override;");
		fprintf(fp, "	virtual void NetLoginModule_OnSetOutSecurity(const GNET::Octets& key) override;");
		fprintf(fp, "	virtual void NetLoginModule_OnSetInSecurity(const GNET::Octets& key) override;");
		fprintf(fp, "	virtual void NetLoginModule_OnSendKeyExchange(const GNET::Octets& nonce) override;");
		fprintf(fp, "};");


		fclose(fp);
	}
}
