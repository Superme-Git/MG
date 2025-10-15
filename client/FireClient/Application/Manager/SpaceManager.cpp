
#include "SpaceManager.h"
#include "VoiceManager.h"
#include "SimpleAudioEngine.h"
#include "IconManager.h"
#include "utils/stringbuilder.h"
#include "Utils/base64.h"
#include "CEGUIImagesetManager.h"
#include "CEGUITexture.h"
#include "CEGUIImageset.h"
#include "oggenc/oggenc.h"
#include "Utils/Voice.h"



static SpaceManager g_spaceMgr;

SpaceManager* gGetSpaceManager()
{
    return &g_spaceMgr;
}

SpaceManager::SpaceManager()
{
}

SpaceManager::~SpaceManager()
{
}

void SpaceManager::Release()
{
	DeleteAllImage();
}

void SpaceManager::ReplaceString(std::string& strData, std::string strA, std::string strB)
{
	size_t pos = 0;
	size_t newStrnewStrLen = strB.length();
	size_t oldStroldStrLen = strA.length();
	while (true)
	{
		pos = strData.find(strA, pos);
		if (pos == std::string::npos)
			break;
		strData.replace(pos, oldStroldStrLen, strB);
		pos += newStrnewStrLen;
	}	
}

void SpaceManager::EncodeBase64Data(std::string& strData)
{
	 strData = Base64::Encode((const unsigned char*)strData.c_str(), strData.length());

	 ReplaceString(strData, "+", "_b");
	 ReplaceString(strData, "/", "_a");
	 ReplaceString(strData, "=", "_c");
}


std::string SpaceManager::UpdatePostData(std::string strData)
{
	std::string strParam = "$filedata$";
	size_t nPosBegin = strData.find(strParam);
	if (nPosBegin == std::string::npos)
	{
		return strData;
	}

	size_t nPosEnd = nPosBegin + strParam.length();
	std::string strRetData = strData.substr(0, nPosBegin);
	size_t nEndLength = strData.length() - nPosEnd;
	std::string strThreePartData = strData.substr(nPosEnd, nEndLength);

	EncodeBase64Data(m_strFileData);

	strRetData = strRetData + m_strFileData;
	strRetData = strRetData + strThreePartData;
	return strRetData;
}

void SpaceManager::SendRequest(std::string strProtocolId,std::string strUrl, std::string strData,int nHttpType,int nTimeOut)
{
	cocos2d::extension::CCHttpRequest* pRequest = new cocos2d::extension::CCHttpRequest();
	pRequest->setUrl(strUrl.c_str());
	pRequest->setTag(strProtocolId.c_str());
	if (strData.empty() == false)
	{
		size_t nPos = strData.find("$filedata$");
		if (nPos != std::string::npos)
		{
			strData = UpdatePostData(strData);
		}
		
		pRequest->setRequestData(strData.c_str(), strData.length());
	}
	pRequest->setRequestType((cocos2d::extension::CCHttpRequest::HttpRequestType) nHttpType);
	pRequest->setResponseCallback((cocos2d::CCObject*)this, httpresponse_selector(SpaceManager::ReceiveRequest_process));
	cocos2d::extension::CCHttpClient* pHttpClient = cocos2d::extension::CCHttpClient::getInstance();
	pHttpClient->setTimeoutForConnect(nTimeOut);
	pHttpClient->send(pRequest);
	pRequest->release();
}

void SpaceManager::ReceiveRequest_process(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response)
{
	cocos2d::extension::CCHttpRequest* pRequest = response->getHttpRequest();
	if (!pRequest)
	{
		return;
	}
	int nRetCode = response->getResponseCode();
	if (nRetCode != 200)
	{
		return;
	}
	std::string strProtocolParam = pRequest->getTag();
	std::vector<char>* res = response->getResponseData();
	m_strFileData = "";
	for (size_t i = 0; i < res->size(); i++)
	{
		m_strFileData += res->at(i);
	}

	const char* param0 = strProtocolParam.c_str();
	const char* param1 = m_strFileData.c_str();
	const char* param2 = "";
	const char* param3 = "";
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("Spaceprotocol.ReceiveRequest_process", param0, param1, param2, param3);
}


bool SpaceManager::SaveStringToFile(std::string& strImageData, std::string  strFilePath)
{
	FILE *fp = fopen(strFilePath.c_str(), "wb");
	if (!fp)
	{
		return false;
	}
	size_t nSize = strImageData.length();
	fseek(fp, 0, SEEK_SET);
	unsigned char* pBuffer = new unsigned char[nSize];
	memcpy(pBuffer, strImageData.c_str(), nSize);
	fwrite(pBuffer, sizeof(unsigned char), nSize, fp);
	fclose(fp);
	delete[] pBuffer;
	return true;

}


void SpaceManager::ClearCurFileData()
{
	m_strFileData.clear();
}

std::string SpaceManager::SaveImageToCurString(cocos2d::CCImage* pImage, bool bIsToRGB, std::string strFilePath)
{
	bool bSaveRet = pImage->saveToFile(strFilePath.c_str(), bIsToRGB);
	if (!bSaveRet)
	{
		return "";
	}	
	FILE *fp = fopen(strFilePath.c_str(), "rb");
	if (!fp)
	{
		return "";
	}
	fseek(fp, 0, SEEK_END);
	size_t nSize = ftell(fp);
	fseek(fp, 0, SEEK_SET);
	unsigned char* pBuffer = new unsigned char[nSize];
	fread(pBuffer, sizeof(unsigned char), nSize, fp);
	fclose(fp);

	m_strFileData = "";
	m_strFileData.resize(nSize);
	for (size_t i = 0; i < nSize; ++i)
	{
		unsigned char charOne = *(pBuffer + i);
		m_strFileData[i] = charOne;
	}
	delete[] pBuffer;
	return m_strFileData;
 }

 bool SpaceManager::Initialize(const char* tempFilePath)
 {
	 m_strTempFilePath = tempFilePath;
	 return true;
 }

 std::string SpaceManager::GetTempFilePath()
 {
	 return m_strTempFilePath;
 }

 std::string SpaceManager::SaveDefaultVoiceToCurString(std::string strTempFilePath)
 {
	 std::vector<short> voiceDatas = gGetVoiceManager()->readTempWavData();
     std::vector<char> outDatas = encodeWAV2SPEEX(voiceDatas);
     
     m_strFileData = "";
     for (size_t i = 0; i < outDatas.size(); ++i)
     {
         m_strFileData += outDatas[i];
     }
     return m_strFileData;
 }

 void SpaceManager::SaveVoice(std::string strSoundUrl, std::string strSounFilePath)
 {
     
     std::vector<short> datas = decodeSPEEX2WAV(m_strFileData); //decodeAMR2WAV(voiceData);
     
     std::string strFilePathName = strSounFilePath;
     FILE* fp = fopen(strFilePathName.c_str(), "w+");
     if (fp)
     {
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
         unsigned int type = 0x46464952;
         unsigned int dataLength = datas.size() * 2;
         fwrite(&type, sizeof(type), 1, fp);
         int fileLength = dataLength + 36;
         fwrite(&fileLength, sizeof(fileLength), 1, fp);
         char typeString[9];
         strcpy(typeString, "WAVEfmt ");
         fwrite(typeString, 8, 1, fp);
         PCMWAVEFORMAT fileHeader;
         fileHeader.wBitsPerSample = 0;
         fileHeader.wf.wFormatTag = 16;
         fileHeader.wf.nChannels = 0;
         fileHeader.wf.nSamplesPerSec = 65537;
         fileHeader.wf.nAvgBytesPerSec = 11025;
         fileHeader.wf.nBlockAlign = 11025;
         fwrite(&fileHeader, sizeof(fileHeader), 1, fp);
         unsigned short reserveData[2] = { 1, 8 };
         fwrite(&(reserveData[0]), 1, sizeof(short), fp);
         fwrite(&(reserveData[1]), 1, sizeof(short), fp);
         fwrite("data", 4, 1, fp);
         fwrite(&dataLength, 4, 1, fp);
         float time = float(datas.size()) / float(fileHeader.wf.nAvgBytesPerSec);
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID || CC_TARGET_PLATFORM == CC_PLATFORM_IOS
         unsigned int dataLength = datas.size() * 2;
         short nBitsPerSample = 16;	// BitsPerSample
         
         fwrite("RIFF", 4, 1, fp);
         int totalDataLen = dataLength + 36;
         fwrite(&totalDataLen, sizeof(totalDataLen), 1, fp);
         fwrite("WAVEfmt ", 8, 1, fp);
         int subChunckSize = 16;	// Sub-chunk size, 16 for PCM
         fwrite(&subChunckSize, sizeof(subChunckSize), 1, fp);
         short nAudioFormat = 1;	// AudioFormat, 1 for PCM
         fwrite(&nAudioFormat, sizeof(nAudioFormat), 1, fp);
         short nChannels = 1;	// Number of channels, 1 for mono, 2 for stereo
         fwrite(&nChannels, sizeof(nChannels), 1, fp);
         int nSamplesPerSec = 8000;	// Sample rate
         fwrite(&nSamplesPerSec, sizeof(nSamplesPerSec), 1, fp);
         int nAvgBytesPerSec = nSamplesPerSec * nBitsPerSample * nChannels / 8;	// Byte rate, SampleRate * BitsPerSample * NumberOfChannels / 8
         fwrite(&nAvgBytesPerSec, sizeof(nAvgBytesPerSec), 1, fp);
         short nBlockAlign = nChannels * nBitsPerSample / 8; // Block align, NumberOfChannels * BitsPerSample / 8
         fwrite(&nBlockAlign, sizeof(nBlockAlign), 1, fp);
         fwrite(&nBitsPerSample, sizeof(nBitsPerSample), 1, fp);
         fwrite("data", 4, 1, fp);
         fwrite(&dataLength, 4, 1, fp);
         float time = float(datas.size()) / float(nSamplesPerSec);
#endif
         for (size_t i = 0; i < datas.size(); i++)
         {
             fwrite(&(datas[i]), sizeof(short), 1, fp);
         }
         fclose(fp);
		 m_fSoundTime = time;
     }
     
     m_strSoundUrl = strSoundUrl;
     
 }
 float SpaceManager::getSoundTime()
 {
	 return m_fSoundTime;
 }

int SpaceManager::GetCocosImageWidth(cocos2d::CCImage* pCocosImage)
{
    int nWidth = pCocosImage->getWidth();
    return nWidth;
}

int SpaceManager::GetCocosImageHeight(cocos2d::CCImage* pCocosImage)
{
    int nHeight = pCocosImage->getHeight();
    return nHeight;
}

std::string SpaceManager::GetCurSoundUrl()
{
    return m_strSoundUrl;
}

void SpaceManager::DeleteImageWithUrlKey(std::string strUrlKey)
{
    for (size_t nIndex = 0; nIndex < m_vCeguiImage.size(); ++nIndex)
    {
        sSpacePicData& picData = m_vCeguiImage[nIndex];
        if (picData.strUrl == strUrlKey)
        {
            if (picData.pCocosImage)
            {
                delete picData.pCocosImage;
            }
            CEGUI::ImagesetManager::getSingleton().destroy(picData.strImageSetName);
            m_vCeguiImage.erase(m_vCeguiImage.begin() + nIndex);
            return;
        }
    }
}

void SpaceManager::DeleteAllImage()
{
    std::vector< sSpacePicData>::iterator it = m_vCeguiImage.begin();
    while (it != m_vCeguiImage.end())
    {
        sSpacePicData* pPicData = &(*it);
        if (pPicData)
        {
            if (pPicData->pCocosImage)
            {
                delete pPicData->pCocosImage;
            }
            CEGUI::ImagesetManager::getSingleton().destroy(pPicData->strImageSetName);
        }
        ++it;
    }
    m_vCeguiImage.clear();
}


void SpaceManager::DeleteTopImage()
{
    for (size_t nIndex = 0; nIndex < m_vCeguiImage.size(); ++nIndex)
    {
        sSpacePicData& picData = m_vCeguiImage[nIndex];
        if (picData.bAutoDelete == false)
            continue;
        if (picData.pCocosImage)
        {
            delete picData.pCocosImage;
        }
        CEGUI::ImagesetManager::getSingleton().destroy(picData.strImageSetName);
        m_vCeguiImage.erase(m_vCeguiImage.begin()+nIndex);
        return;
    }
}

int SpaceManager::GetSaveImageCount()
{
    return (int)m_vCeguiImage.size();
}

unsigned int SpaceManager::GetCurStringLength()
{
    unsigned int nLen =  m_strFileData.length();
    return nLen;
}

CEGUI::Image* SpaceManager::SaveCeguiImageWithUrl(std::string strFilePath, bool bAutoDelete,bool bSaveCocosImage,std::string strUrl, std::string strImageName, std::string strImageSetName, int nImageType, bool bUseCurString)
{
    bool bSaveRet = SaveStringToFile(m_strFileData, strFilePath);
    cocos2d::CCImage* pCocoImage = new cocos2d::CCImage();
    
    int nImageFormat = cocos2d::CCImage::EImageFormat::kFmtJpg;
    if (nImageType == CEGUI::Texture::PixelFormat::PF_RGB)
    {
        nImageFormat = cocos2d::CCImage::EImageFormat::kFmtJpg;
	 }
	 else
	 {
		 nImageFormat = cocos2d::CCImage::EImageFormat::kFmtPng;
	 }
	bool bRet = pCocoImage->initWithImageFile(strFilePath.c_str(), (cocos2d::CCImage::EImageFormat) nImageFormat);
	 if (!bRet)
	 {
		 delete pCocoImage;
		 return NULL;
	 }
	 unsigned char* pBuffer = pCocoImage->getData();
	 if (!pBuffer)
	 {
		 return NULL;
	 }
	 float nWidth = pCocoImage->getWidth();
	 float nHeight = pCocoImage->getHeight();
	 CEGUI::Rect rect(0, 0, nWidth, nHeight);
	 CEGUI::String imageName = CEGUI::String(strImageName);
	 CEGUI::String imageSetName = CEGUI::String(strImageSetName);

	 CEGUI::Texture* pTexture = &CEGUI::System::getSingleton().getRenderer()->createTexture();

	 if (!pTexture)
	 {
		 return NULL;
	 }

	 CEGUI::Texture::PixelFormat pixelFormat = pCocoImage->hasAlpha() ? CEGUI::Texture::PF_RGBA : CEGUI::Texture::PF_RGB;

	 pTexture->loadFromMemory(pBuffer, CEGUI::Size(nWidth, nHeight), pixelFormat, false);
	 CEGUI::Imageset& imageSet = CEGUI::ImagesetManager::getSingleton().create(imageSetName, *pTexture);
     imageSet.defineImage(imageName, rect, CEGUI::Point(0, 0));
	 const CEGUI::Image& ceguiImage = imageSet.getImage(imageName);
	 const CEGUI::Image* pCeguiImage = &ceguiImage; //new CEGUI::Image((const char*)pBuffer, imageName, imageSetName, rect, (CEGUI::Texture::PixelFormat) nImageType);

	 sSpacePicData spacePicData;
	 spacePicData.bAutoDelete = bAutoDelete;
	 spacePicData.strUrl = strUrl;
	 spacePicData.pCeguiImage = (CEGUI::Image*)pCeguiImage;
	 spacePicData.pCocosImage = pCocoImage;
	 spacePicData.strImageName = strImageName;
	 spacePicData.strImageSetName = strImageSetName;

	 if (!bSaveCocosImage)
	 {
		 spacePicData.pCocosImage = NULL;
		 delete pCocoImage;
	 }

	 m_vCeguiImage.push_back( spacePicData);
	 return spacePicData.pCeguiImage;
 }

 CEGUI::Image* SpaceManager::GetCeguiImageWithUrl(std::string strUrl)
 {
	 for (size_t nIndex = 0; nIndex < m_vCeguiImage.size(); ++nIndex)
	 {
		 sSpacePicData& picData = m_vCeguiImage[nIndex];
		 if (picData.strUrl == strUrl)
		 {
			 return picData.pCeguiImage;
		 }
	 }
	 return NULL;
 }

 cocos2d::CCImage* SpaceManager::GetCocosImageWithUrl(std::string strUrl)
 {
	 for (size_t nIndex = 0; nIndex < m_vCeguiImage.size(); ++nIndex)
	 {
		 sSpacePicData& picData = m_vCeguiImage[nIndex];
		 if (picData.strUrl == strUrl)
		 {
			 return picData.pCocosImage;
		 }
	 }
	 return NULL;
 }

 std::string SpaceManager::GetUrlEnCode(const std::string& str)
 {
	 std::string encodeUrl = "";
	 int len = str.length();
	 if (len == 0)
	 {
		 return encodeUrl;
	 }

	 std::string sDontEncode = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_~.";

	 for (int i = 0; i < len; i++)
	 {
		 char cToFind = str.at(i);
		 if (-1 == sDontEncode.find(cToFind, 0))
		 {
			 std::string tmp = GetUrlEncodeFormat(cToFind);
			 encodeUrl.append(tmp);
		 }
		 else if (cToFind == ' ')
		 {
			 encodeUrl += '+';
		 }
		 else{
			 encodeUrl += cToFind;
		 }
	 }

	 return encodeUrl;
	
 }
 std::string SpaceManager::GetUrlEncodeFormat(const unsigned char cValue)
 {
	 std::string tmp;
	 tmp.append("%");
	 unsigned int nDiv = cValue / 16;
	 unsigned int nMod = cValue % 16;
	 tmp.append(GetDecimalToHexString(nDiv));
	 tmp.append(GetDecimalToHexString(nMod));

	 return tmp;
 }

 std::string SpaceManager::GetDecimalToHexString(unsigned int nValue)
 {
	 std::string tmp;
	 if (nValue < 10)
	 {
		 tmp += ((char)nValue + 48);
	 }
	 else{
		 switch (nValue)
		 {
		 case 10:tmp.append("A"); break;
		 case 11:tmp.append("B"); break;
		 case 12:tmp.append("C"); break;
		 case 13:tmp.append("D"); break;
		 case 14:tmp.append("E"); break;
		 case 15:tmp.append("F"); break;
		 default:break;

		 }
	 }
	 return tmp;
 }

 std::string SpaceManager::JsonDecode(std::string& strJsonString, std::string& strName, std::string& strDefault)
 {
	 Json* pRoot = Json_create(strJsonString.c_str());
	 if (!pRoot)
	 {
		 return "";
	 }
	 std::string strRet = Json_getString(pRoot, strName.c_str(), strDefault.c_str());
	 Json_dispose(pRoot);
	 return strRet;

 }


 Json* SpaceManager::SpaceJson_create(std::string& strJsonString)
 {
	return Json_create(strJsonString.c_str());
 }

 Json *SpaceManager::SpaceJson_getItem(Json *object, std::string strName)
 {
	 if (!object)
	 {
		 return NULL;
	 }
	 return Json_getItem(object, strName.c_str());
 }

 std::string SpaceManager::SpaceJson_getString(Json* object, std::string name, std::string defaultValue)
 {
	 if (!object)
	 {
		 return defaultValue;
	 }
	 return Json_getString(object, name.c_str(), defaultValue.c_str());
 }

 float SpaceManager::SpaceJson_getFloat(Json* value, std::string name, float defaultValue)
 {
	 if (!value)
	 {
		 return 0;
	 }
	 return Json_getFloat(value, name.c_str(), defaultValue);
 }

 int SpaceManager::SpaceJson_getInt(Json* value, std::string name, int defaultValue)
 {
	 if (!value)
	 {
		 return 0;
	 }
	 return Json_getInt(value, name.c_str(),defaultValue);
 }

 void SpaceManager::SpaceJson_dispose(Json *c)
 {
	 Json_dispose(c);
 }


 static const unsigned char firstByteMark[7] = { 0x00, 0x00, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC };
 std::string SpaceManager::Json_decode(std::string& strJson)
 {
	 const char* str = strJson.c_str();
	 
	 //static const char* parse_string(Json *item, const char* str) {
		 const char* ptr = str + 1;
		 char* ptr2;
		 unsigned int nLen = 1024 * 10;
		 char* out = new char[nLen];
		 int len = 0;
		 unsigned uc, uc2;

		 //if (*str != '\"') { /* TODO: don't need this check when called from parse_value, but do need from parse_object */
		//	 ep = str;
		//	 return 0;
		// } /* not a string! */

		 //while (*ptr != '\"' && *ptr && ++len)
		//	 if (*ptr++ == '\\') ptr++; /* Skip escaped quotes. */

		 //out = MALLOC(char, len + 1); /* The length needed for the string, roughly. */
		 //if (!out) return 0;

		 ptr = str + 1;
		 ptr2 = out;
		 //while (*ptr != '\"' && *ptr)
		 while (*ptr)
		 {
			 if (*ptr != '\\')
				 *ptr2++ = *ptr++;
			 else 
			 {
				 ptr++;
				 switch (*ptr) 
				 {
				 case 'b':
					 *ptr2++ = '\b';
					 break;
				 case 'f':
					 *ptr2++ = '\f';
					 break;
				 case 'n':
					 *ptr2++ = '\n';
					 break;
				 case 'r':
					 *ptr2++ = '\r';
					 break;
				 case 't':
					 *ptr2++ = '\t';
					 break;
				 case 'u': /* transcode utf16 to utf8. */
					 sscanf(ptr + 1, "%4x", &uc);
					 ptr += 4; /* get the unicode char. */

					 if ((uc >= 0xDC00 && uc <= 0xDFFF) || uc == 0) break; /* check for invalid.	*/

					 /* TODO provide an option to ignore surrogates, use unicode replacement character? */
					 if (uc >= 0xD800 && uc <= 0xDBFF) /* UTF16 surrogate pairs.	*/
					 {
						 if (ptr[1] != '\\' || ptr[2] != 'u') break; /* missing second-half of surrogate.	*/
						 sscanf(ptr + 3, "%4x", &uc2);
						 ptr += 6;
						 if (uc2 < 0xDC00 || uc2 > 0xDFFF) break; /* invalid second-half of surrogate.	*/
						 uc = 0x10000 + (((uc & 0x3FF) << 10) | (uc2 & 0x3FF));
					 }

					 len = 4;
					 if (uc < 0x80)
						 len = 1;
					 else if (uc < 0x800)
						 len = 2;
					 else if (uc < 0x10000) len = 3;
					 ptr2 += len;

					 switch (len) {
					 case 4:
						 *--ptr2 = ((uc | 0x80) & 0xBF);
						 uc >>= 6;
						 /* fallthrough */
					 case 3:
						 *--ptr2 = ((uc | 0x80) & 0xBF);
						 uc >>= 6;
						 /* fallthrough */
					 case 2:
						 *--ptr2 = ((uc | 0x80) & 0xBF);
						 uc >>= 6;
						 /* fallthrough */
					 case 1:
						 *--ptr2 = (uc | firstByteMark[len]);
					 }
					 ptr2 += len;
					 break;
				 default:
					 *ptr2++ = *ptr;
					 break;
				 }
				 ptr++;
			 }
		 }
		 //*ptr2 = 0;
		 
		 //if (*ptr == '\"') ptr++; /* TODO error handling if not \" or \0 ? */
		 std::string strRetString  = out;
		 delete[] out;
		 return strRetString;
	// }

 }

 bool SpaceManager::ImageScaleToRect(cocos2d::CCImage* pImage, int nWidth, int nHeight)
 {
	 if (!pImage)
	 {
		 return false;
	 }
	 return pImage->scaleToRect(nWidth, nHeight);
 }


 //-----------------------------------
 cocos2d::CCImage* SpaceManager::getTestCocos2dImage(std::string strFileName)
 {
	 cocos2d::CCImage* pImage = new cocos2d::CCImage();
	 std::string strFilePath = GetTempFilePath() + strFileName.c_str();
	 pImage->initWithImageFile(strFilePath.c_str());
	 return pImage;
 }


 const CEGUI::Image* SpaceManager::getTestCeguiImage(std::string strFileName)
 {
	 /*
	 std::string strFilePath = GetTempFilePath() + strFileName.c_str();
	 cocos2d::CCImage* pImage = new cocos2d::CCImage();
	 pImage->initWithImageFile(strFilePath.c_str());

	 //----------------------------
	 bool bAlpha = true;
	 std::string strPicData = SaveImageToCurString(pImage, !bAlpha, strFilePath);
	 //-------------------------------------------

	 std::string strUrl = "123url";
	 std::string strImageData = strPicData;
	 CEGUI::String strImageName = CEGUI::String("123image");
	 CEGUI::String strImageSetName = CEGUI::String("123setname");
	 int nWidth = 172;
	 int nHeight = 172;
	 int nSaveMax = 20;
	 CEGUI::Image* pCeguiImage = SaveCeguiImageWithUrl(strFilePath, false, strUrl, strImageData, strImageName, 1);
	 return pCeguiImage;
	 */
	 return NULL;

 }