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
	* ������̶߳�ʹ�ó�ʱ�����ʱ��,ͬʱ���߳�����sleep����wait�Ȳ���.
	* ������������ѡ��,libcurl���ᷢ�źŴ�����wait�Ӷ����³����˳�.
	*/
	//curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);
	curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1);
	curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 15);//���ӳ�ʱ,�����ֵ�������̫�̿��ܵ����������󲻵��ͶϿ���
	curl_easy_setopt(curl, CURLOPT_TIMEOUT, 30);//��������ʱ��ʱ����,���30��������δ������,ֱ���˳�
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
	curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 15);//���ӳ�ʱ,�����ֵ�������̫�̿��ܵ����������󲻵��ͶϿ���
	curl_easy_setopt(curl, CURLOPT_TIMEOUT, 30);//��������ʱ��ʱ����,���30��������δ������,ֱ���˳�
	res = curl_easy_perform(curl);
	curl_easy_cleanup(curl);
	return res;
}