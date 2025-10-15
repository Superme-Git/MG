#include "../stdafx.h"
#include "../Curl/curl.h"
#include "HttpClient.h"
#include "../Global/GlobalFunction.h"

#ifdef _DEBUG
#pragma comment(lib, "code/curl/libcurl_lib/libcurl_MTd_VS2010.lib")
#pragma comment(lib, "code/curl/openssl_lib/ssleay32_MTd_VS2010.lib")
#pragma comment(lib, "code/curl/openssl_lib/libeay32_MTd_VS2010.lib")
#else
#pragma comment(lib, "code/curl/libcurl_lib/libcurl_MT_VS2010.lib")
#pragma comment(lib, "code/curl/openssl_lib/ssleay32_MT_VS2010.lib")
#pragma comment(lib, "code/curl/openssl_lib/libeay32_MT_VS2010.lib")
#endif

//1:��������������Ŀ�:��Ŀ->����->������->����->����������,�� ws2_32.lib winmm.lib wldap32.lib  ��ӽ�ȥ
//2:����Ԥ����ѡ��:��Ŀ->����->c/c++ ->Ԥ������->Ԥ������,�� CURL_STATICLIB; ��ӽ�ȥ
//3:C/C++�����������п�MTd(���̵߳���,����Debug�汾)����MT(���߳�,����Release�汾)

CHttpClient::CHttpClient(void)
{
	m_bDebug = false;
}
CHttpClient::~CHttpClient(void)
{
}
//static int OnDebug(CURL *, curl_infotype itype, char * pData, size_t size, void *)
int CHttpClient::OnDebug(void *, int itype, char * pData, size_t size, void *)
{
	if (itype == 0)//CURLINFO_TEXT
	{
		//printf("[TEXT]%s\n", pData);
	}
	else if (itype == 1)//CURLINFO_HEADER_IN
	{
		printf("[HEADER_IN]%s\n", pData);
	}
	else if (itype == 2)//CURLINFO_HEADER_OUT
	{
		printf("[HEADER_OUT]%s\n", pData);
	}
	else if (itype == 3)//CURLINFO_DATA_IN
	{
		printf("[DATA_IN]%s\n", pData);
	}
	else if (itype == 4)//CURLINFO_DATA_OUT
	{
		printf("[DATA_OUT]%s\n", pData);
	}
	return 0;
}
size_t CHttpClient::OnWriteData(void* buffer, size_t size, size_t nmemb, void *stream)
{
	std::vector<char> *recvBuffer = (std::vector<char>*)stream;
	size_t sizes = size * nmemb;
	recvBuffer->insert(recvBuffer->end(), (char*)buffer, (char*)buffer + sizes);
	DoEvents();
	DoEventsUI();
	return sizes;
}
void CHttpClient::SetDebug(bool bDebug)
{
	m_bDebug = bDebug;
}
