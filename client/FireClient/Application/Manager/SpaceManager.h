#ifndef __SPACEMANAGER_H__
#define __SPACEMANAGER_H__ 

#include "network/HttpClient.h"
#include "network/HttpRequest.h"
#include "network/HttpResponse.h"

#include "CEGUIImage.h" //dependencies\cegui\CEGUI\include\CEGUIImage.h
#include "../../../../cocos2d-2.0-rc2-x-2.0.1/extensions/libSpine/spine-c/src/spine/Json.h"



class SpaceManager : public cocos2d::CCObject
{
public:
	struct sSpacePicData
	{
		std::string strUrl;
		CEGUI::Image* pCeguiImage;
		cocos2d::CCImage* pCocosImage;
		bool bAutoDelete;
		std::string strImageName;
		std::string strImageSetName;
		
	};
	SpaceManager();
	virtual ~SpaceManager();

	void SendRequest(std::string strProtocolId, std::string strUrl, std::string strData = "", int nHttpType = cocos2d::extension::CCHttpRequest::HttpRequestType::kHttpPost, int nTimeOut = 5);
	void ReceiveRequest_process(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);

	bool Initialize(const char* tempFilePath);
	std::string GetTempFilePath();
	std::string SaveImageToCurString(cocos2d::CCImage* pImage, bool bIsToRGB, std::string strFilePath);
	std::string SaveDefaultVoiceToCurString(std::string strTempFilePath);
	std::string GetCurSoundUrl();
	void SaveVoice(std::string strSoundUrl, std::string strSounFilePath);
	void DeleteAllImage();
	void DeleteImageWithUrlKey(std::string strUrlKey);
	bool SaveStringToFile(std::string& strImageData, std::string  strFilePath);
	CEGUI::Image* SaveCeguiImageWithUrl(std::string strFilePath, bool bAutoDelete,bool bSaveCocosImage, std::string strUrl, std::string strImageName, std::string strImageSetName, int nImageType = 1, bool bUseCurString=false);
	CEGUI::Image* GetCeguiImageWithUrl(std::string strUrl);
	cocos2d::CCImage* GetCocosImageWithUrl(std::string strUrl);
	void Release();
	void DeleteTopImage();
	std::string GetUrlEnCode(const std::string& str);
	int GetSaveImageCount();
	
	std::string GetUrlEncodeFormat(const unsigned char cValue);
	std::string GetDecimalToHexString(unsigned int nValue);

	void ClearCurFileData();
	std::string UpdatePostData(std::string strData);

	int GetCocosImageWidth(cocos2d::CCImage* pCocosImage);
	int GetCocosImageHeight(cocos2d::CCImage* pCocosImage);

	void ReplaceString(std::string& strData, std::string strA, std::string strB);
	void EncodeBase64Data(std::string& strData);
	std::string JsonDecode(std::string& strJsonString, std::string& strName, std::string& strDefault);

	bool ImageScaleToRect(cocos2d::CCImage* pImage, int nWidth, int nHeight);
	unsigned int GetCurStringLength();
	std::string Json_decode(std::string& strJson);
	//-------------------

	Json* SpaceJson_create(std::string& strJsonString);
	Json * SpaceJson_getItem(Json *object, std::string strName);
	std::string SpaceJson_getString(Json* object, std::string name, std::string defaultValue);
	float SpaceJson_getFloat(Json* value, std::string name, float defaultValue);
	int SpaceJson_getInt(Json* value, std::string name, int defaultValue);
	void SpaceJson_dispose(Json *c);
	//-------------------

	float getSoundTime();
	//wangbin test api
	cocos2d::CCImage* getTestCocos2dImage(std::string strFileName);
	const CEGUI::Image* getTestCeguiImage(std::string strFileName);

private:
	std::vector< sSpacePicData > m_vCeguiImage;
	std::string m_strSoundUrl;
	std::string m_strFileData;
	std::string m_strTempFilePath;
	float m_fSoundTime;
	
};

SpaceManager* gGetSpaceManager();

#endif
