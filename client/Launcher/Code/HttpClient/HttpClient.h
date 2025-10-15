#ifndef __HTTP_CURL_H__
#define __HTTP_CURL_H__

#include <string>

class CHttpClient
{
public:
	CHttpClient(void);
	~CHttpClient(void);

public:
	/**
	* @brief HTTP POST请求
	* @param strUrl 输入参数,请求的Url地址,如:http://www.baidu.com
	* @param strPost 输入参数,使用如下格式para1=val1&para2=val2&…
	* @param strResponse 输出参数,返回的内容
	* @return 返回是否Post成功
	*/
	int Post(const std::string & strUrl, const std::string & strPost, std::string & strResponse);

	/**
	* @brief HTTP GET请求
	* @param strUrl 输入参数,请求的Url地址,如:http://www.baidu.com
	* @param strResponse 输出参数,返回的内容
	* @return 返回是否Get成功
	*/
	int Get(const std::string & strUrl, void* stream, int* iResponseCode);

	/**
	* @brief HTTPS POST请求,无证书版本
	* @param strUrl 输入参数,请求的Url地址,如:https://www.alipay.com
	* @param strPost 输入参数,使用如下格式ppara1=val1&para2=val2&…
	* @param strResponse 输出参数,返回的内容
	* @param pCaPath 输入参数,为CA证书的路径.如果输入为NULL,则不验证服务器端证书的有效性.
	* @return 返回是否Post成功
	*/
	int Posts(const std::string & strUrl, const std::string & strPost, std::string & strResponse, const char * pCaPath = NULL);

	/**
	* @brief HTTPS GET请求,无证书版本
	* @param strUrl 输入参数,请求的Url地址,如:https://www.alipay.com
	* @param strResponse 输出参数,返回的内容
	* @param pCaPath 输入参数,为CA证书的路径.如果输入为NULL,则不验证服务器端证书的有效性.
	* @return 返回是否Get成功
	*/
	int Gets(const std::string & strUrl, void* stream, const char * pCaPath = NULL);
	static size_t OnWriteData(void* buffer, size_t size, size_t nmemb, void *stream);

public:
	void SetDebug(bool bDebug);
	static int OnDebug(void *, int itype, char * pData, size_t size, void *);

private:
	bool m_bDebug;
};

#endif
