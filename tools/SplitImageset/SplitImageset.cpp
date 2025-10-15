// SplitImageset.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include "SplitImageset.h"
#include "E:/MT_G/dependencies/RapidXml/Include/rapidxml.hpp"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

// 唯一的应用程序对象

CWinApp theApp;
#include <stdio.h>
#include <string>
#include <list>

const std::string g_Path = "../res/ui/imagesets/";

using namespace std;
class ImageInfo
{
public:
	std::string mName;
	int mPosX,mPosY;
	int mWidth, mHeight;
};

void SplitImageset(std::string imagesetName, std::string imageName,::string type, int leftWidth, int centerWidth, int rightWidth, int topHeight, int centerHeight, int bottomHeight)
{
	printf("split imagesetName=%s imageName=%s type=%s\n", imagesetName.c_str(), imageName.c_str(),type.c_str());
	//读入信息到缓冲区.
	FILE* fp;
	fopen_s(&fp, imagesetName.c_str(), "rb");
	if (fp == NULL)
	{
		printf("open file %s failed!\n", imagesetName.c_str());
		return;
	}
	fseek(fp, 0, 2);
	long length = ftell(fp);
	fseek(fp, 0, 0);
	char* text = (char*)malloc(length+1);
	fread(text, length, 1, fp);
	text[length] = 0;
	fclose(fp);

	//读入IMAGE.
	std::list<ImageInfo> images;
	rapidxml::xml_document<> doc;
	try
	{
		doc.parse<0>(text);
	}
	catch (...)
	{
		printf("parse file %s failed\n", imagesetName.c_str());
		free(text);
		return;
	}


	rapidxml::xml_node<>* imagesetNode = doc.first_node("Imageset");
	rapidxml::xml_attribute<>* attrImagesetName=imagesetNode->first_attribute("Name");
	rapidxml::xml_attribute<>* attrImageFile = imagesetNode->first_attribute("Imagefile");
	rapidxml::xml_node<>* imageNode = imagesetNode->first_node("Image");

	std::list<rapidxml::xml_attribute<>*> _allArribute;
	for (rapidxml::xml_attribute<>* attr = imagesetNode->first_attribute(); attr != NULL; attr = attr->next_attribute())
	{
		_allArribute.push_back(attr);
	}

	std::string sImagesetRow = "<Imageset";
	
	std::list<rapidxml::xml_attribute<>*>::iterator itt = _allArribute.begin();
	while (itt != _allArribute.end())
	{
		rapidxml::xml_attribute<>* attr = *itt;
		char ss[128];
		sprintf_s(ss, " %s=\"%s\"", attr->name(), attr->value());
		sImagesetRow.append(ss);
		_allArribute.erase(itt++);
	}
	sImagesetRow.append(">\n");
	printf(sImagesetRow.c_str());

	if (imagesetNode == NULL || attrImagesetName == NULL || attrImageFile == NULL || imageNode == NULL)
	{
		printf("get node Imageset or Name or Imagefile or Image failed!\n");
		free(text);
		return;
	}

	while (imageNode)
	{
		ImageInfo ii;
		rapidxml::xml_attribute<>* attrName=imageNode->first_attribute("Name");
		rapidxml::xml_attribute<>* attrXPos = imageNode->first_attribute("XPos");
		rapidxml::xml_attribute<>* attrYPos = imageNode->first_attribute("YPos");
		rapidxml::xml_attribute<>* attrWidth = imageNode->first_attribute("Width");
		rapidxml::xml_attribute<>* attrHeight = imageNode->first_attribute("Height");

		if (attrName == NULL || attrXPos == NULL || attrYPos == NULL || attrWidth == NULL || attrHeight == NULL)
		{
			printf("get node Name or XPos or YPos or Width or Height failed!\n");
			free(text);
			return;
		}

		ii.mName = attrName->value();
		ii.mPosX = atoi(attrXPos->value());
		ii.mPosY = atoi(attrYPos->value());
		ii.mWidth = atoi(attrWidth->value());
		ii.mHeight = atoi(attrHeight->value());
		images.push_back(ii);
		imageNode = imageNode->next_sibling();
	}

	//解析IMAGE.
	std::list<ImageInfo>::iterator it = images.begin();
	while (it != images.end())
	{
		if (!strcmp(it->mName.c_str(), imageName.c_str()))
		{
			if (type == "OnlyWidth")
			{
				ImageInfo left = *it;
				left.mName = it->mName + "_l";
				left.mWidth = leftWidth;
				images.insert(it, left);
				ImageInfo center = *it;
				center.mName = it->mName + "_c";
				center.mPosX += leftWidth;
				center.mWidth = centerWidth;
				images.insert(it, center);
				ImageInfo right = *it;
				right.mName = it->mName + "_r";
				right.mPosX += leftWidth + centerWidth;
				right.mWidth = rightWidth;
				images.insert(it, right);
			}
			else if (type == "OnlyHeight")
			{
				ImageInfo top = *it;
				top.mName = it->mName + "_t";
				top.mHeight = topHeight;
				images.insert(it, top);
				ImageInfo center = *it;
				center.mName = it->mName + "_c";
				center.mPosY += topHeight;
				center.mHeight = centerHeight;
				images.insert(it, center);
				ImageInfo bottom = *it;
				bottom.mName = it->mName + "_b";
				bottom.mPosY += topHeight + centerHeight;
				bottom.mHeight = bottomHeight;
				images.insert(it, bottom);
			}
			else if (type == "WidthAndHeight")
			{
				ImageInfo iis[3][3];
				iis[0][0].mName = imageName+"_lt";
				iis[0][1].mName = imageName+"_lc";
				iis[0][2].mName = imageName+"_lb";
				iis[1][0].mName = imageName + "_ct";
				iis[1][1].mName = imageName + "_cc";
				iis[1][2].mName = imageName + "_cb";
				iis[2][0].mName = imageName + "_rt";
				iis[2][1].mName = imageName + "_rc";
				iis[2][2].mName = imageName + "_rb";
				int widths[3] = { leftWidth, centerWidth, rightWidth };
				int heights[3] = { topHeight, centerHeight, bottomHeight };
				int xpositions[3] = { 0, leftWidth, leftWidth + centerWidth };
				int ypositions[3] = { 0, topHeight, topHeight + centerHeight };
				for (int x = 0; x < 3; x++)
				{
					for (int y = 0; y < 3; y++)
					{
						iis[x][y].mPosX = it->mPosX + xpositions[x];
						iis[x][y].mPosY = it->mPosY + ypositions[y];
						iis[x][y].mWidth = widths[x];
						iis[x][y].mHeight = heights[y];
						images.insert(it, iis[x][y]);
					}
				}
			}
			else
				printf("error type=%s\n", type.c_str());

			images.erase(it++);
		}
		else
			++it;
	}
	free(text);

	//写文件.
	DeleteFileA(imagesetName.c_str());
	FILE* fp2;
	fopen_s(&fp2, imagesetName.c_str(), "wt");

	//char sImagesetRow[512];
	//sprintf_s(sImagesetRow, 512, "<Imageset Name=\"%s\" Imagefile=\"%s.tga\" >\n", subs.c_str(), subs.c_str());
	::fwrite(sImagesetRow.c_str(), strlen(sImagesetRow.c_str()), 1, fp2);

	it = images.begin();
	while (it != images.end())
	{
		char sImageRow[256];
		sprintf_s(sImageRow,256,"    <Image Name=\"%s\" XPos=\"%d\" YPos=\"%d\" Width=\"%d\" Height=\"%d\" />\n",
			it->mName.c_str(),
			it->mPosX,
			it->mPosY,
			it->mWidth,
			it->mHeight
			);
		::fwrite(sImageRow, strlen(sImageRow),1,fp2);
		++it;
	}

	fprintf(fp2,"</Imageset>\n");
	::fclose(fp2);
}

void SplitImagesets()
{
	char buf[1000];
	int i = 1000;
	GetCurrentDirectoryA(1000, buf);
	printf("local path=%s\n", buf);

	//读入信息到缓冲区.
	FILE* fp;
	errno_t e = fopen_s(&fp, "SplitConfig.xml", "rb");
	if (fp == NULL)
	{
		printf("open SplitConfig.xml failed!\n");
	}
	fseek(fp, 0, 2);
	long length = ftell(fp);
	fseek(fp, 0, 0);
	char* text = (char*)malloc(length + 1);
	fread(text, length, 1, fp);
	while (text[length] != '>')
	{
		--length;
	}
	text[length+1] = 0;
	fclose(fp);

	//读入IMAGE.
	std::list<ImageInfo> images;
	rapidxml::xml_document<> doc;
	try
	{
		doc.parse<0>(text);
	}
	catch (...)
	{
		printf("parse SplitConfig.xml failed!\n");
		return;
	}

	rapidxml::xml_node<>* node = doc.first_node("frame");
	while (node)
	{
		rapidxml::xml_attribute<>* imagesetName = node->first_attribute("imageset");
		std::string sImagesetName=imagesetName->value();
		rapidxml::xml_attribute<>* imageName = node->first_attribute("image");
		std::string sImageName=imageName->value();
		rapidxml::xml_attribute<>* type = node->first_attribute("type");
		std::string sType=type->value();
		rapidxml::xml_attribute<>* leftWidth = node->first_attribute("left_width");
		int iLeftWidth = 0;
		if (leftWidth)
			iLeftWidth = atoi(leftWidth->value());
		rapidxml::xml_attribute<>* centerWidth = node->first_attribute("center_width");
		int iCenterWidth = 0;
		if (centerWidth)
			iCenterWidth = atoi(centerWidth->value());
		rapidxml::xml_attribute<>* rightWidth = node->first_attribute("right_width");
		int iRightWidth = 0;
		if (rightWidth)
			iRightWidth = atoi(rightWidth->value());
		rapidxml::xml_attribute<>* topHeight = node->first_attribute("top_height");
		int iTopHeight = 0;
		if (topHeight)
			iTopHeight = atoi(topHeight->value());
		rapidxml::xml_attribute<>* centerHeight = node->first_attribute("center_height");
		int iCenterHeight = 0;
		if (centerHeight)
			iCenterHeight = atoi(centerHeight->value());
		rapidxml::xml_attribute<>* bottomHeight = node->first_attribute("bottom_height");
		int iBottomHeight = 0;
		if (bottomHeight)
			iBottomHeight = atoi(bottomHeight->value());
		std::string sPathFileName = g_Path + sImagesetName;
		SplitImageset(sPathFileName, sImageName, sType, iLeftWidth, iCenterWidth, iRightWidth, iTopHeight, iCenterHeight, iBottomHeight);
		node = node->next_sibling();
	}

	printf("procress over!\n");
	::Sleep(3000);
}

int _tmain(int argc, TCHAR* argv[], TCHAR* envp[])
{

	int nRetCode = 0;
	HMODULE hModule = ::GetModuleHandle(NULL);

	if (hModule != NULL)
	{
		// 初始化 MFC 并在失败时显示错误
		if (!AfxWinInit(hModule, NULL, ::GetCommandLine(), 0))
		{
			// TODO:  更改错误代码以符合您的需要
			_tprintf(_T("错误:  MFC 初始化失败\n"));
			nRetCode = 1;
		}
		else
		{
			// TODO:  在此处为应用程序的行为编写代码。
		}
	}
	else
	{
		// TODO:  更改错误代码以符合您的需要
		_tprintf(_T("错误:  GetModuleHandle 失败\n"));
		nRetCode = 1;
	}
	SplitImagesets();

	return nRetCode;
}
