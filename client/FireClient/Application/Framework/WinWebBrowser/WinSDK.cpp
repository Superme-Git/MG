#include "stdafx.h"
#include "WinSDK.h"
#include "gWebBrowser.h"
#include "DeviceInfo.h"
#include "LoginManager.h"
#include "GameApplication.h"
#include "MainRoleDataManager.h"

WinSDK* WinSDK::m_Inst = NULL;

WinSDK* WinSDK::getInstance()
{
	if (m_Inst == NULL) {
		m_Inst = new WinSDK();
	}
	return m_Inst;
}

void WinSDK::destroyInstance()
{
	if (m_Inst) {
		delete m_Inst;
		m_Inst = NULL;
	}
}

WinSDK::WinSDK()
{
	m_bWebViewVisible = false;
	m_strPlatformId = "";
	//m_strLoginUrl = "http://192.168.29.151:9001/ClientGame/Login.aspx?"; // 内网测试地址
	m_strLoginUrl = "http://billing.locojoy.com/api/nosdk/login/locojoy/ClientGameWeb.ashx?";
	//m_strChargeUrl = "http://192.168.29.151:9001/api/nosdk/pay/locojoy/ClientGameWeb.ashx?"; // 内网测试地址
	m_strChargeUrl = "http://billing.locojoy.com/api/nosdk/pay/locojoy/ClientGameWeb.ashx?";
}

WinSDK::~WinSDK()
{
}

std::string WinSDK::EncodeURL(const std::string &URL)
{  
	std::string strResult = "";  
	for ( unsigned int i=0; i<URL.size(); i++ )
	{  
		char c = URL[i];  
		if (  
			( '0'<=c && c<='9' ) ||  
			( 'a'<=c && c<='z' ) ||  
			( 'A'<=c && c<='Z' ) ||  
			c=='/' || c=='.'  
			) {  
				strResult += c;  
		}   
		else   
		{  
			int j = (short int)c;  
			if ( j < 0 )  
			{  
				j += 256;  
			}  
			int i1, i0;  
			i1 = j / 16;  
			i0 = j - i1*16;  
			strResult += '%';  
			strResult += Dec2HexChar(i1);  
			strResult += Dec2HexChar(i0);  
		}  
	}  

	return strResult;  
}  

std::string WinSDK::DecodeURL(const std::string &URL)
{  
	std::string result = "";  
	for ( unsigned int i=0; i<URL.size(); i++ )
	{  
		char c = URL[i];  
		if ( c != '%' )   
		{  
			result += c;  
		}   
		else   
		{  
			char c1 = URL[++i];  
			char c0 = URL[++i];  
			int num = 0;  
			num += HexChar2Dec(c1) * 16 + HexChar2Dec(c0);  
			result += char(num);  
		}  
	}  

	return result;  
}  

char WinSDK::Dec2HexChar(long n)
{  
	if ( 0 <= n && n <= 9 ) {  
		return char( short('0') + n );  
	} else if ( 10 <= n && n <= 15 ) {  
		return char( short('A') + n - 10 );  
	} else {  
		return char(0);  
	}  
}  

long WinSDK::HexChar2Dec(char c)
{  
	if ( '0'<=c && c<='9' ) {  
		return short(c-'0');  
	} else if ( 'a'<=c && c<='f' ) {  
		return ( short(c-'a') + 10 );  
	} else if ( 'A'<=c && c<='F' ) {  
		return ( short(c-'A') + 10 );  
	} else {  
		return -1;  
	}  
}  

#define MT3_GameID "88"
#define MT3_AppID "10024"
#define MT3_AppKey "8e55941bae2170d58170f8f3b85ed069"
#define MT3_GameKey "b18a26ffc632752987bd24a7bf0353f3"
#define MT3_Version "1"

void WinSDK::openLoginUrl()
{
	CEGUI::Window* textFrame = CEGUI::WindowManager::getSingleton().getWindow("WinLoginDlg/main");
	float width = textFrame->getPixelSize().d_width;
	float height = textFrame->getPixelSize().d_height;
	int logicWidth = Nuclear::GetEngine()->GetLogicWidth();
	int logicHeight = Nuclear::GetEngine()->GetLogicHeight();
	int screenWidth = Nuclear::GetEngine()->GetScreenWidth();
	int screenHeight = Nuclear::GetEngine()->GetScreenHeight();
	float finalWidth = width / logicWidth * screenWidth;
	float finalHeight = height / logicHeight * screenHeight;

	char md5Data[2048] = { 0 };
	std::string strData = "{\"AppId\":\"";
	strData += MT3_AppID;
	strData += "\",\"Ip\":\"";
	strData += DeviceInfo::sGetIPAddress();
	strData +="\",\"DeviceId\":\"";
	strData += DeviceInfo::sGetMacAddress();
	strData += "\"}";

	sprintf(md5Data, "%s%s%s", MT3_GameKey, MT3_Version, strData.c_str());
	std::string toMD5(md5Data);
	SECURITY_WIN32::MD5Hash md5;
	md5.UpdateSelf((const unsigned char*)toMD5.c_str(), (unsigned int)toMD5.length());
	const unsigned char* dest = md5.Final();
	char szmd5[64] = { 0 };
	for (int i = 0; i < 16; i++)
	{
		sprintf(szmd5, "%s%02x", szmd5, dest[i]);
	}
	std::string signData(szmd5);
	std::string strUrl = m_strLoginUrl;
	strUrl += "Version=";
	strUrl += MT3_Version;
	strUrl += "&GameId=";
	strUrl += MT3_GameID;
	strUrl += "&Data=";
	strUrl += EncodeURL(strData.c_str());
	strUrl += "&Sign=";
	strUrl += signData;

	openWinWebView(strUrl, finalWidth, finalHeight);
}

void WinSDK::onLoginSuccess(std::string& platformid, std::string& session)
{
	m_strPlatformId = platformid;
	std::string strSession = session;
	std::string strChannel = ws2s(gGetLoginManager()->GetCurChannelId());
	std::string strDeviceID = DeviceInfo::sGetIPAddress();
	std::string strMacAddress = DeviceInfo::sGetMacAddress();

	char aAccount[128] = "";
	sprintf(aAccount, "%s@@%s,%s", m_strPlatformId.c_str(), strChannel.c_str(), strMacAddress.c_str());

	gGetLoginManager()->SetPassword(StringCover::to_wstring(strSession));
	gGetLoginManager()->SetAccountInfo(StringCover::to_wstring(aAccount));
	gGetLoginManager()->SetDeviceid(StringCover::to_wstring(strMacAddress.empty() ? strDeviceID.c_str() : strMacAddress.c_str()));
	gGetLoginManager()->SetChannelId(StringCover::to_wstring(strChannel));

	if (gGetLoginManager()->getEnterMainStatus() == eEnterMainStatus_None)
		gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_LoginSuccess); // SDK登陆成功
	else {
		gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_GamePlaying);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("SelectServerEntry_EnableClick", true); // 设置选服界面按钮可点击
	}

	// 关闭WINDOW版本启动时的快速登录界面
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LoginQuickDialog.DestroyDialog");

	// 关闭WINDOW版本的登陆窗口
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("WinLoginDlg.DestroyDialog");
	if (gGetLoginManager()->isFirstEnter())
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("windowsexplain.getInstanceAndShow");
	}
	gGetLoginManager()->SaveAccount();

	cocos2d::CCEGLView *eglView = cocos2d::CCEGLView::sharedOpenGLView();
	::SetFocus(eglView->getHWnd());
}

void WinSDK::openChargeUrl(std::string& productid, int productcount)
{
	char md5Data[2048] = { 0 };
	std::string strData = "{\"AppId\":\"";
	strData += MT3_AppID;
	strData += "\",\"PlatformId\":\"";
	strData += m_strPlatformId;
	strData += "\",\"ServerId\":\"";
	strData += StringCover::intToString(gGetLoginManager()->getServerID());
	strData += "\",\"ChannelId\":\"";
	strData += ws2s(gGetLoginManager()->GetCurChannelId());
	strData += "\",\"RoleId\":\"";
	strData += StringCover::int64_tToString(GetMainRoleDataNumValue<int64_t>("roleid"));
	strData += "\",\"ProductId\":\"";
	strData += productid;
	strData += "\",\"ProductCount\":\"";
	strData += StringCover::intToString(productcount);
	strData += "\",\"Ip\":\"";
	strData += ws2s(gGetLoginManager()->GetIp());
	strData += "\",\"DeviceId\":\"";
	strData += DeviceInfo::sGetMacAddress();
	strData += "\"}";

	sprintf(md5Data, "%s%s%s", MT3_GameKey, MT3_Version, strData.c_str());
	std::string toMD5(md5Data);
	SECURITY_WIN32::MD5Hash md5;
	md5.UpdateSelf((const unsigned char*)toMD5.c_str(), (unsigned int)toMD5.length());
	const unsigned char* dest = md5.Final();
	char szmd5[64] = { 0 };
	for (int i = 0; i < 16; i++)
	{
		sprintf(szmd5, "%s%02x", szmd5, dest[i]);
	}
	std::string signData(szmd5);
	std::string strUrl = m_strChargeUrl;
	strUrl += "Version=";
	strUrl += MT3_Version;
	strUrl += "&GameId=";
	strUrl += MT3_GameID;
	strUrl += "&Data=";
	strUrl += EncodeURL(strData.c_str());
	strUrl += "&Sign=";
	strUrl += signData;

	size_t size = strUrl.length();
	wchar_t* buffer = new wchar_t[size + 1];
	MultiByteToWideChar(CP_ACP, 0, strUrl.c_str(), size, buffer, size*sizeof(wchar_t));
	buffer[size] = 0;
	ShellExecute(NULL, L"open", buffer, NULL, NULL, SW_SHOWNORMAL);
	delete[] buffer;
}

// 仅用于登陆的回调
void WinSDK::update()
{
	if (m_bWebViewVisible) {
		gWebBrowser::getInstance()->update();
	}
}

void WinSDK::openWinWebView(std::string& strUrl, float width, float height)
{
	gWebBrowser::getInstance()->setUrl(strUrl.c_str(), width, height);
	m_bWebViewVisible = true;
}

void WinSDK::hideWinWebView()
{
	gWebBrowser::getInstance()->setVisible(false);
}

void WinSDK::resumeWinWebView()
{
	gWebBrowser::getInstance()->setVisible(true);
}

void WinSDK::closeWinWebView()
{
	gWebBrowser::destroyInstance();
	m_bWebViewVisible = false;
}

