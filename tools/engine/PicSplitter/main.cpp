#include <windows.h>
#include <string>
#include <vector>
#include <map>
#include <set>
#include "..\engine\renderer\renderer.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\common\util.h"

LRESULT CALLBACK MsgProc( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
{
	return DefWindowProc( hWnd, msg, wParam, lParam );
}

INT WINAPI WinMain( HINSTANCE hInst, HINSTANCE, LPSTR, INT )
{
	_CrtSetDbgFlag( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
	wchar_t tmpString[MAX_PATH];//最后这个就记录输出文件格式
	GetPrivateProfileString(L"INPUT", L"filename", L"", tmpString, MAX_PATH, L".\\PicSplitter.ini");
	std::wstring infilename = tmpString;
	if (infilename.size() == 0)
	{
		MessageBox(0, L"输入文件名错误", 0, 0);
		return 0;
	}
	int outWidth = GetPrivateProfileInt(L"OUTPUT", L"width", 512, L".\\PicSplitter.ini");
	int outHeight = GetPrivateProfileInt(L"OUTPUT", L"height", 512, L".\\PicSplitter.ini");
	GetPrivateProfileString(L"OUTPUT", L"format", L"DXT5", tmpString, MAX_PATH, L".\\PicSplitter.ini");
	Nuclear::XPTEXTURE_FORMAT texFormat;
	Nuclear::XPIMAGE_FILEFORMAT fileFormat;
	bool ok = false;
	switch (tmpString[0])
	{
	case 'D'://DXTx
	case 'd':
		if ((tmpString[1] == 'X' || tmpString[1] == 'x') &&
			(tmpString[2] == 'T' || tmpString[2] == 't'))
		{
			ok = true;
			fileFormat = Nuclear::XPIFF_DDS;
			switch (tmpString[3])
			{
			case '1':
				texFormat = Nuclear::XPTEXFMT_DXT1;
				break;
			case '2':
				texFormat = Nuclear::XPTEXFMT_DXT2;
				break;
			case '3':
				texFormat = Nuclear::XPTEXFMT_DXT3;
				break;
			case '4':
				texFormat = Nuclear::XPTEXFMT_DXT4;
				break;
			case '5':
				texFormat = Nuclear::XPTEXFMT_DXT5;
				break;
			default:
				ok = false;
			}
		}
		break;
	case 'J'://JPG
	case 'j':
		if ((tmpString[1] == 'P' || tmpString[1] == 'p') &&
			(tmpString[2] == 'G' || tmpString[2] == 'g'))
		{
			ok = true;
			fileFormat = Nuclear::XPIFF_JPG;
			texFormat = Nuclear::XPTEXFMT_A8R8G8B8;
		}
		break;
	case 'T'://tga
	case 't':
		if ((tmpString[1] == 'G' || tmpString[1] == 'g') &&
			(tmpString[2] == 'A' || tmpString[2] == 'a'))
		{
			ok = true;
			fileFormat = Nuclear::XPIFF_TGA;
			texFormat = Nuclear::XPTEXFMT_A8R8G8B8;
		}
		break;
	case 'B'://bmp
	case 'b':
		if ((tmpString[1] == 'M' || tmpString[1] == 'm') &&
			(tmpString[2] == 'P' || tmpString[2] == 'p'))
		{
			ok = true;
			fileFormat = Nuclear::XPIFF_BMP;
			texFormat = Nuclear::XPTEXFMT_A8R8G8B8;
		}
		break;
	case 'P'://png
	case 'p':
		if ((tmpString[1] == 'N' || tmpString[1] == 'n') &&
			(tmpString[2] == 'G' || tmpString[2] == 'g'))
		{
			ok = true;
			fileFormat = Nuclear::XPIFF_PNG;
			texFormat = Nuclear::XPTEXFMT_A8R8G8B8;
		}
		break;
	default:
		ok = false;
	}
	if (!ok)
	{
		MessageBox(0, L"输出文件格式错误", 0, 0);
		return 0;
	}

	GetPrivateProfileString(L"OUTPUT", L"filenameformat", L"output-%d-%d.dds", tmpString, MAX_PATH, L".\\PicSplitter.ini");
	//后面不能写tmpString

	WNDCLASSEX wc = { sizeof(WNDCLASSEX) }; // 类结构
	// 注册窗口类
	wc.style = CS_CLASSDC;
	wc.lpfnWndProc = MsgProc;
	wc.hInstance = GetModuleHandle(NULL);
	wc.lpszClassName =  L"PicSplitter";

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

	PFS::CFile file;
	if( !file.Open(infilename.c_str(), PFS::FM_EXCL, PFS::FA_RDONLY ))
		return false;
	int file_size = (int)file.GetSize();
	if( file_size == 0 ) return 0;
	Nuclear::XBuffer data;
	data.resize(file_size);
	file.ReadAll(data.normalbegin(), data.size());
	Nuclear::CRECT rect;
	std::vector<std::pair<Nuclear::CRECT, Nuclear::XBuffer> > dest;
	int row, col, i, j;
	pRenderer->SplitTexture(data.constbegin(), data.size(), texFormat, fileFormat, rect, dest, row, col, outWidth, outHeight);
	std::vector<std::pair<Nuclear::CRECT, Nuclear::XBuffer> >::iterator it = dest.begin(), ie = dest.end();
	wchar_t filename[MAX_PATH];
	i = 0;
	j = 0;
	for (;it!=ie;++it, ++i)
	{
		if (i >= col)
		{
			++j;
			i=0;
		}
		wsprintf(filename, tmpString, j, i);
		FILE *f = Nuclear::FOpen(filename, L"wb");
		if( f == NULL )
			return 0;
		Nuclear::FWrite(f, it->second.constbegin(), it->second.size());
		fclose(f);
	}
	Nuclear::DestroyRenderer(pRenderer);
	return 0;
}