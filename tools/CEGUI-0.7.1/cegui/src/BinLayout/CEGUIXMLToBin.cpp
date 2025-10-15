//////////////////////////////////////////////////////////////////////////
// File:		CEGUIXMLToBin.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/03/03
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUIXMLToBin.h"
#include "CEGUIString.h"
#include "CEGUISystem.h"
#include "BinLayout/CEGUIXMLFileData.h"
#include "CEGUIXMLParser.h"
#include "BinLayout/CEGUIBinLayoutFileSerializer.h"
#include "BinLayout/CEGUIFileStream.h"
#include "XMLParserModules/LJXMLParser/CEGUILJXMLParser.h"
#include "../../LJXML/Include/LJXMLStringHelper.hpp"

namespace CEGUI
{
	namespace BinLayout
	{
		bool XMLToBin::convert(const String& srcFilename, const String& dstFilename)
		{
			bool bRet = true;

			FILE* pSrcFile = NULL;
			char* pSrcFileData = NULL;
			FILE* pDstFile = NULL;

			do 
			{
				pSrcFile = fopen(srcFilename.c_str(), "rb");
				if (!pSrcFile)
				{
					printf("open file failed: %s\n", srcFilename.c_str());
					bRet = false;
					goto finished;
				}

				fseek(pSrcFile, 0, SEEK_END);
				int fileSize = ftell(pSrcFile);
				fseek(pSrcFile, 0, SEEK_SET);

				if (fileSize <= 0)
				{
					printf("file is empty: %s\n", srcFilename.c_str());

					bRet = true;
					goto finished;
				}

				char* pSrcFileData = new char[fileSize + 3];
				if (!pSrcFileData)
				{
					printf("allocate memory failed: %d\n", (fileSize + 3));
					
					bRet = false;
					goto finished;
				}
				memset(pSrcFileData, 0, fileSize + 3);

				fread(pSrcFileData, fileSize, 1, pSrcFile);
				fclose(pSrcFile);
				pSrcFile = NULL;

				if (0 == memcmp(pSrcFileData, LAYOUT_BIN_FILE_MAGIC, 4))
				{
					printf("file is already binary: %s\n", srcFilename.c_str());
					
					bRet = true;
					goto finished;
				}

				std::wstring wStr;
				LJXMLStringHelper::EncodeLJ codeType;
				LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)pSrcFileData, fileSize + 2, wStr, codeType);
				String strFileData = wStr;

				delete[] pSrcFileData;
				pSrcFileData = NULL;

				LJXMLParser parser;
				XMLFileData xmlFileData;
				parser.parseXMLContent(xmlFileData, strFileData);

				XMLFileData::NodeData* pRootData = xmlFileData.getRootData();
				if (!pRootData)
				{
					printf("cannot get root data from XMLFileData: %s\n", srcFilename.c_str());

					bRet = false;
					goto finished;
				}

				FILE* pDstFile = fopen(dstFilename.c_str(), "wb");
				if (!pDstFile)
				{
					printf("open file failed: %s\n", dstFilename.c_str());

					bRet = false;
					goto finished;
				}

				BinLayoutFileSerializer serializer;
				FileStream stream(pDstFile, false);
				if (!serializer.write(stream, LAYOUT_BIN_FILE_VERSION, pRootData))
				{
					printf("convert failed: %s\n", srcFilename.c_str());

					bRet = false;
					goto finished;
				}

			} while (false);

		finished:

			if (pDstFile)
			{
				fclose(pDstFile);
				pDstFile = NULL;
			}

			if (pSrcFileData)
			{
				delete[] pSrcFileData;
				pSrcFileData = NULL;
			}

			if (pSrcFile)
			{
				fclose(pSrcFile);
				pSrcFile = NULL;
			}

			return bRet;
		}
	}
}