#include "HttpClient.h"
#include <string>
#include "../Curl/curl.h"

int CHttpClient::Get(const std::string & strUrl, void* stream, int* iResponseCode)
{
	*iResponseCode = -1;
	CURLcode res;
	CURL* curl = curl_easy_init();
	if(NULL == curl)
	{
		return CURLE_FAILED_INIT;
	}
	if(m_bDebug)
	{
		curl_easy_setopt(curl, CURLOPT_VERBOSE, 1);
		curl_easy_setopt(curl, CURLOPT_DEBUGFUNCTION, OnDebug);
	}
	curl_easy_setopt(curl, CURLOPT_URL, strUrl.c_str());
	curl_easy_setopt(curl, CURLOPT_READFUNCTION, NULL);
	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, OnWriteData);
	curl_easy_setopt(curl, CURLOPT_WRITEDATA, stream);
	/**
	* 当多个线程都使用超时处理的时候,同时主线程中有sleep或是wait等操作.
	* 如果不设置这个选项,libcurl将会发信号打断这个wait从而导致程序退出.
	*/
	//curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);
	curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1);
	curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 15);//连接超时,这个数值如果设置太短可能导致数据请求不到就断开了
	curl_easy_setopt(curl, CURLOPT_TIMEOUT, 30);//接收数据时超时设置,如果30秒内数据未接收完,直接退出
	res = curl_easy_perform(curl);
	if (res != CURLE_OK)
	{
		curl_easy_cleanup(curl);
		return res;
	}
	CURLcode code = curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, iResponseCode);
	if (code != CURLE_OK)
	{
		curl_easy_cleanup(curl);
		return code;
	}
	if (*iResponseCode != 200)
	{
		curl_easy_cleanup(curl);
		return -1;
	}
	return 0;
	curl_easy_cleanup(curl);
}
int CHttpClient::Gets(const std::string & strUrl, void* stream, const char * pCaPath)
{
	CURLcode res;
	CURL* curl = curl_easy_init();
	if(NULL == curl)
	{
		return CURLE_FAILED_INIT;
	}
	if(m_bDebug)
	{
		curl_easy_setopt(curl, CURLOPT_VERBOSE, 1);
		curl_easy_setopt(curl, CURLOPT_DEBUGFUNCTION, OnDebug);
	}
	curl_easy_setopt(curl, CURLOPT_URL, strUrl.c_str());
	curl_easy_setopt(curl, CURLOPT_READFUNCTION, NULL);
	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, OnWriteData);
	curl_easy_setopt(curl, CURLOPT_WRITEDATA, stream);
	curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);
	if(NULL == pCaPath)
	{
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, false);
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, false);
	}
	else
	{
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, true);
		curl_easy_setopt(curl, CURLOPT_CAINFO, pCaPath);
	}
	curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 15);//连接超时,这个数值如果设置太短可能导致数据请求不到就断开了
	curl_easy_setopt(curl, CURLOPT_TIMEOUT, 30);//接收数据时超时设置,如果30秒内数据未接收完,直接退出
	res = curl_easy_perform(curl);
	curl_easy_cleanup(curl);
	return res;
}