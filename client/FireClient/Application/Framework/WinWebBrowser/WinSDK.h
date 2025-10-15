#ifndef __WIN_SDK_H__
#define __WIN_SDK_H__

#include<string>

class WinSDK
{
private:
	static WinSDK* m_Inst;

public:
	std::string m_strLoginUrl;
	std::string m_strChargeUrl;

public:
	WinSDK();
	~WinSDK();
	static WinSDK* getInstance();
	static void destroyInstance();

	//---------Encode ºÍ Decode
	std::string EncodeURL(const std::string &URL);
	std::string DecodeURL(const std::string &URL);
	char Dec2HexChar(long n);
	long HexChar2Dec(char c);
	
	void openLoginUrl();
	void onLoginSuccess(std::string& platformid, std::string& session);

	void openChargeUrl(std::string& productid, int productcount);

	void update();

	void openWinWebView(std::string& strUrl, float width, float height);
	void hideWinWebView();
	void resumeWinWebView();
	void closeWinWebView();

private:
	bool m_bWebViewVisible;
	std::string m_strPlatformId;
};

#endif