// picconverter.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <windows.h>
#include <string>
#include <vector>
#include <map>
#include <set>
#include "..\engine\renderer\renderer.h"
#include "..\engine\engine\animanager.h"

LRESULT CALLBACK MsgProc( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
{
	return DefWindowProc( hWnd, msg, wParam, lParam );
}

void ResizePic(int &value);

void convert2DXTX(Nuclear::Renderer* pRenderer, const std::wstring &path, Nuclear::XPTEXTURE_FORMAT fmt)
{
	WIN32_FIND_DATA findFileData;
	HANDLE hFind = INVALID_HANDLE_VALUE;
	std::wstring file = path + L"/*";

	std::wstring oldXapName = L"";
	
	hFind = FindFirstFile(file.c_str(), &findFileData);
	BOOL ok = (hFind != INVALID_HANDLE_VALUE);
	for (;ok;ok=FindNextFile(hFind, &findFileData))
	{
		std::wstring tmpPath = findFileData.cFileName;
		
		if (findFileData.dwFileAttributes == FILE_ATTRIBUTE_DIRECTORY)
		{
			if (wcscmp(findFileData.cFileName, L".") == 0 ||
				wcscmp(findFileData.cFileName, L"..") == 0)
				continue;
			convert2DXTX(pRenderer, path + L"\\" + findFileData.cFileName, fmt);

		}
		else
		{
			size_t le = wcslen(findFileData.cFileName);
			if (le < 4)
				continue;

			//检查文件后缀
			std::wstring extFileName = findFileData.cFileName;
			int DotPos				 = extFileName.find(L".");
			extFileName				 = extFileName.substr(DotPos + 1);

			if(extFileName==L"dds")
			{
				//获取文件名称（如：attack1_res000.dds）
				std::wstring filestr = path + L"\\" + findFileData.cFileName;
	
				std::wstring tmpFindName	= findFileData.cFileName;
				std::wstring tmpFindDDsName = tmpFindName;
				int nFileNamePos			= tmpFindName.find_last_of(L"_") + 1;
				if(nFileNamePos==0)
					continue;

				tmpFindDDsName				= tmpFindDDsName.substr(nFileNamePos);
				tmpFindDDsName				= L"_" + tmpFindDDsName;
				tmpFindName.replace(nFileNamePos-1,tmpFindDDsName.size(),L"",0);
				std::wstring tmpXapFile		= path + L"\\" + tmpFindName + SFI_ExtName;
				
				FILE *fXap = Nuclear::FOpen(tmpXapFile.c_str(),L"rb");
				if (!fXap) continue;

				Nuclear::PAniPack loadPack;
				if(!loadPack.Load(fXap)) continue;
				fclose(fXap);

				//Convert DDS Part to 2的N次幂
				Nuclear::PictureHandle handle = pRenderer->LoadPictureFromNativePath(filestr);
				if (handle == Nuclear::INVALID_PICTURE_HANDLE)
					continue;

				Nuclear::XBuffer buffer;
				Nuclear::PictureInfo tmpInfo;
				if (!pRenderer->GetPictureInfo(handle, tmpInfo))
					continue;

				int srcWid = 0, srcHei = 0, dstWid = 0, dstHei = 0;
				Nuclear::PictureHandle scaledHandle = pRenderer->ClonePicture(handle, 1, true, srcWid, srcHei, dstWid, dstHei);
				
				if(scaledHandle == Nuclear::INVALID_PICTURE_HANDLE)
					continue;
				
				if(fXap&&(oldXapName!=tmpXapFile))
				{
					DeleteFile(tmpXapFile.c_str());
					fXap = NULL;

					std::vector<std::vector<std::vector<Nuclear::PAniPack::FileSec> > >& fileDatas = loadPack.GetDatas();
					for(unsigned i=0; i<fileDatas.size(); ++i)
					{
						for(unsigned j=0; j<fileDatas[i].size(); ++j)
						{
							for(unsigned k=0; k<fileDatas[i][j].size(); ++k)
							{
								std::wstring tmpXapName = path + L"\\" + tmpFindName;
								std::wstring tmpPicPath = tmpXapName + fileDatas[i][j][k].strPicPath;

								Nuclear::PictureHandle tmphandle = pRenderer->LoadPictureFromNativePath(tmpPicPath);
								if (tmphandle == Nuclear::INVALID_PICTURE_HANDLE)
									continue;

								Nuclear::XBuffer tmpBuffer;
								Nuclear::PictureInfo tmpPicInfo;
								if (!pRenderer->GetPictureInfo(tmphandle, tmpPicInfo))
									continue;

								pRenderer->FreePicture(tmphandle);
								tmphandle = NULL;

								int tmpsrcWid = tmpPicInfo.m_nPicWidth;
								int tmpsrcHei = tmpPicInfo.m_nPicHeight;
								
								int tmpDstWid = tmpsrcWid;
								int tmpDstHei = tmpsrcHei;
								ResizePic(tmpDstWid);
								ResizePic(tmpDstHei);

								float fWidScale = (float)tmpsrcWid / tmpDstWid;
								float fHeiScale = (float)tmpsrcHei / tmpDstHei;
				
								fileDatas[i][j][k].rctData.left   *= fWidScale;
								fileDatas[i][j][k].rctData.right  *= fWidScale;
								fileDatas[i][j][k].rctData.top	  *= fHeiScale;
								fileDatas[i][j][k].rctData.bottom *= fHeiScale;
							}
						}
					}

					FILE *fpXap = Nuclear::FOpen(tmpXapFile.c_str(),L"wb");
					loadPack.Save(fpXap);
					wprintf(L"Convert ani Success!!!: %s \n",tmpXapFile.c_str());
					oldXapName = tmpXapFile;
					fclose(fpXap);
				}

				if (!pRenderer->SaveTextureToFile(scaledHandle, fmt, Nuclear::XPIFF_DDS, buffer))
						continue;
					
				wprintf(L"Convert DDS Success!!!: %s \n",filestr.c_str());
				DeleteFile(filestr.c_str());
				
				pRenderer->FreePicture(handle);
				pRenderer->FreePicture(scaledHandle);
				
				FILE *f = Nuclear::FOpen(filestr.c_str(), L"wb"); 
				if( f == NULL )
					continue;

				Nuclear::FWrite(f, buffer.constbegin(), buffer.size());
				fclose(f);
			}
		}
	}
}

//2015 Yao
void ResizePic(int &value)
{
	if(value <= 16)
	{
		value = 16;
	}
	else if(value > 16 && value <= 32)
	{
		value = 32;
	}
	else if(value > 32 && value <= 64)
	{
		value = 64;
	}
	else if(value > 64 && value <= 128)
	{
		value = 128;
	}
	else if(value > 128 && value <= 256)
	{
		value = 256;
	}
	else if(value > 256 && value <= 512)
	{
		value = 512;
	}
	else if(value > 512 && value <= 1024)
	{
		value = 1024;
	}
	else if(value > 1024)
	{
		value = 2048;
	}
}

int _tmain(int argc, _TCHAR* argv[])
{
	_CrtSetDbgFlag( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
	wchar_t tmpString[MAX_PATH];//最后这个就记录输出文件格式
	GetPrivateProfileString(L"INPUT", L"path", L"", tmpString, MAX_PATH, L".\\PicConverter.ini");
	std::wstring inPath = tmpString;
	if (inPath.size() == 0)
	{
		MessageBox(0, L"输入路径错误", 0, 0);
		return 0;
	}

	Nuclear::XPTEXTURE_FORMAT fmt;
	int fmtx;
	fmtx = GetPrivateProfileInt(L"INPUT", L"DXTx", 5, L".\\PicConverter.ini");
	switch (fmtx)
	{
	case 1:
		fmt = Nuclear::XPTEXFMT_DXT1;
		break;
	case 2:
		fmt = Nuclear::XPTEXFMT_DXT2;
		break;
	case 3:
		fmt = Nuclear::XPTEXFMT_DXT3;
		break;
	case 4:
		fmt = Nuclear::XPTEXFMT_DXT4;
		break;
	case 5:
		fmt = Nuclear::XPTEXFMT_DXT5;
		break;
	default:
		MessageBox(0, L"输入格式错误", 0, 0);
		return 0;
		break;
	}

	WNDCLASSEX wc = { sizeof(WNDCLASSEX) }; // 类结构
	// 注册窗口类
	wc.style = CS_CLASSDC;
	wc.lpfnWndProc = MsgProc;
	wc.hInstance = GetModuleHandle(NULL);
	wc.lpszClassName =  L"PicConverter";

	RegisterClassEx( &wc );

	// 初始化主窗口
	HWND hWnd = CreateWindow(wc.lpszClassName, wc.lpszClassName,
		WS_POPUP | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX,
		100, 100, 100, 100, GetDesktopWindow(), NULL, wc.hInstance, NULL );
	Nuclear::Renderer* pRenderer;
	Nuclear::XDisplayMode mode;
	if( !CreateRenderer(&pRenderer, hWnd, mode, NULL, 0) )
	{
		//XPALARM(L"渲染器创建失败");
		return 0;
	}

	convert2DXTX(pRenderer, inPath, fmt);
	Nuclear::DestroyRenderer(pRenderer);
	return 0;
}

