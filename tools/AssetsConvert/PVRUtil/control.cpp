#include "StdAfx.h"
#include "control.h"
#include "filesystem.h"
#include "FreeImageWrapper.h"
#include "PVRUtil.h"
#include <afxwin.h>
#include <afxmt.h>
#include <cstdlib>
#include <cstdio>
#include <fstream>
#include <algorithm>
#include <functional>

 int dxt1_count = 0;
 int total_count = 0;

CMutex g_mtx;
CMutex g_atitcMtx;
CMutex g_dxt1Mtx;
CMutex g_etc1Mtx;

CCriticalSection g_pvr_cs;

CCriticalSection g_atitc_cs;

CCriticalSection g_dxt1_cs;

CCriticalSection g_etc1_cs;

std::vector<std::wstring> g_ExcludePaths;
std::vector<std::wstring> g_ExcludeCopyPaths;


//分割字符串
inline int split_string(const std::wstring &inputstr, const std::vector<std::wstring>& delimiters, std::vector<std::wstring> &substrs)
{
	if (inputstr.length() == 0 || delimiters.size() == 0) {
		return 0;
	}

	size_t find_pos = 0; //the finding position   
	while (true) {
		size_t split_pos = std::wstring::npos; // 最近的分隔符位置    
		size_t delimiter_size = 0; // 所用到的分隔符长度   
		std::vector<std::wstring>::const_iterator itrbeg = delimiters.begin();
		while (itrbeg != delimiters.end()) {
			size_t tpos = inputstr.find(*itrbeg, find_pos);
			if ((split_pos != std::wstring::npos && tpos != std::wstring::npos && tpos < split_pos)
				|| (split_pos == std::wstring::npos && tpos != std::wstring::npos)) {
				split_pos = tpos;
				delimiter_size = itrbeg->size();
			}
			++itrbeg;
		}
		if (split_pos != std::wstring::npos) {
			substrs.push_back(inputstr.substr(find_pos, split_pos - find_pos));
			find_pos = split_pos + delimiter_size;
		}
		else {
			substrs.push_back(inputstr.substr(find_pos)); // all the remainder is pushed back   
			break;
		}

	};
	return static_cast<int>(substrs.size());
}

bool isExistInExcludePaths(const std::wstring& path)
{
	size_t findPos = path.find(g_control.m_inputDir);
	std::wstring strSrcPathEnd;
	if (findPos != std::wstring::npos)
	{ 
		strSrcPathEnd = path.substr(g_control.m_inputDir.length(), path.length());
		if (strSrcPathEnd.find(L"\\") != std::wstring::npos)
		{
			strSrcPathEnd = strSrcPathEnd.substr(strSrcPathEnd.find(L"\\"), strSrcPathEnd.length());
		}
		else
		{
			strSrcPathEnd = L"";
		}
	}

	std::wstring srcFullPath = g_control.m_inputDir + strSrcPathEnd;

	for (size_t iItem = 0; iItem < g_ExcludePaths.size(); ++iItem)
	{
		std::wstring strExcludePaths = L"\\" + g_ExcludePaths[iItem];
		std::wstring strExclude = g_control.m_inputDir;
		if (strExclude.find(strExcludePaths) == std::wstring::npos)
		{
			strExclude += strExcludePaths;
		}

		if (srcFullPath.find(strExclude) != std::wstring::npos)
		{
			return true;
		}
	}
	return false;
}


bool copy_entier_directory( const std::wstring& src_dir, const std::wstring& dst_dir, void(control::*func)( const std::wstring& ) )
{
	SHFILEOPSTRUCT sfo; 

	std::vector<wchar_t> vsrc_dir, vdst_dir;

	for( int i = 0; i < src_dir.size(); i++ )
	{
		vsrc_dir.push_back(src_dir[i]);
	}

	vsrc_dir.push_back(L'\0');
	vsrc_dir.push_back(L'\0');

	for( int i = 0; i < dst_dir.size(); i++ )
	{
		vdst_dir.push_back(dst_dir[i]);
	}

	vdst_dir.push_back(L'\0');
	vdst_dir.push_back(L'\0');


	ZeroMemory(&sfo, sizeof(sfo));
	sfo.wFunc    = FO_COPY; 
	sfo.pFrom    = &vsrc_dir[0];
	sfo.pTo      = &vdst_dir[0];

	sfo.fFlags = FOF_SILENT | FOF_NOCONFIRMATION | FOF_NOCONFIRMMKDIR; 

	int ret = SHFileOperation(&sfo);
	BOOL b = sfo.fAnyOperationsAborted;
	DWORD error_no = GetLastError();
	if ( ret == 0 )
		return TRUE;
	else
		return FALSE;
}

bool copy_entier_directory_mt( const std::wstring& src_dir, const std::wstring& dst_dir,const std::tr1::function<void (const std::wstring&)>& _func )
{
	dir in_dir( src_dir );

	if( !CreateDirectory( dst_dir.c_str(), NULL ) )
	{
		std::wstring err_msg = L"Create Director: " + dst_dir + L" Failed !";
		::MessageBox( 0, err_msg.c_str(), L"Error", 0 );
		return false;
	}


	for( size_t i = 0; i < in_dir.m_items.size(); i++ )
	{
		const dir::item& dirItem = in_dir.m_items[i];
		std::wstring src_file = dirItem.fullPath;
		for (size_t iItem = 0; iItem < g_ExcludeCopyPaths.size(); ++iItem)
		{
			std::wstring strExcludeCopy = L"\\" + g_ExcludeCopyPaths[iItem];
			size_t nRet = src_file.find(strExcludeCopy);
			if (nRet != std::wstring::npos)
			{
				continue;
			}

			if (!dirItem.isDir)
			{
				std::wstring dst_file = src_file;
				dst_file.replace( 0, src_dir.length(), dst_dir );

				if(!CopyFile( src_file.c_str(), dst_file.c_str(), FALSE ))
				{
					std::wstring err_msg = L"Copy File: " + src_file + L" Failed !";
					::MessageBox( 0, err_msg.c_str(), L"Error", 0 );
					return false;
				}

				const std::wstring msg = L"Preparing: " + dst_file + L" OKAY !";

			//	_func( msg );
			}

			else
			{
				std::wstring dst_dir2;
				dst_dir2 = src_file;
				dst_dir2.replace(0, src_dir.length(), dst_dir);

				copy_entier_directory_mt(src_file.c_str(), dst_dir2.c_str(), _func);
			}
		}
	}
}

void control::AddMessage(const wchar_t* amessage, message::message_type type)
{
	g_mtx.Lock();
	if (type == message::window)
	{
		m_strWindowText = amessage;
	}else
	{
		message msg;
		msg.msgtype = type;
		msg.msg = amessage;

		m_vMessages.push_back(msg);
	}
	g_mtx.Unlock();
}

void control::AddPvrMessage( const std::wstring& msg )
{
	g_pvr_cs.Lock();
	m_pvr_message_queue.push_back(msg);
	g_pvr_cs.Unlock();
}

void control::AddATITCMessage( const std::wstring& msg )
{
	g_atitc_cs.Lock();
	m_atitc_message_queue.push_back(msg);
	g_atitc_cs.Unlock();
}

void control::AddDXT1Message( const std::wstring& msg )
{
	g_dxt1_cs.Lock();
	m_dxt1_message_queue.push_back(msg);
	g_dxt1_cs.Unlock();
}

void control::AddETC1Message( const std::wstring& msg )
{
	g_etc1_cs.Lock();
	m_etc1_message_queue.push_back(msg);
	g_etc1_cs.Unlock();
}


std::wstring control::GetWindowText()
{
	std::wstring ret;
	g_mtx.Lock();
	ret = m_strWindowText;
	g_mtx.Unlock();
	return ret;
}

control::message control::GetMessage(int idx)
{
	control::message ret ;
	g_mtx.Lock();
	if (idx < 0 || idx >= m_vMessages.size())
	{
		
	}else
	{
	 ret = m_vMessages[idx];
	}
	g_mtx.Unlock();
	return ret;
}

control::message control::FetchFirstMessage()
{
	control::message ret ;
	g_mtx.Lock();
	if ( m_vMessages.size() == 0)
	{
		ret.msg = L"";
	}
	else
	{
		std::vector<message>::iterator bit = m_vMessages.begin();
		ret = *bit;
		m_vMessages.erase(bit);
	}
	g_mtx.Unlock();
	return ret;
}

std::wstring control::FetchPvrFirstMessage()
{
	std::wstring ret ;
	g_pvr_cs.Lock();
	if ( m_pvr_message_queue.size() == 0)
	{
		ret = L"";
	}
	else
	{
		std::vector<std::wstring>::iterator bit = m_pvr_message_queue.begin();
		ret = *bit;
		m_pvr_message_queue.erase(bit);
	}
	g_pvr_cs.Unlock();
	return ret;
}

std::wstring control::FetchATITCFirstMessage()
{
	std::wstring ret ;
	g_atitc_cs.Lock();
	if ( m_atitc_message_queue.size() == 0)
	{
		ret = L"";
	}
	else
	{
		std::vector<std::wstring>::iterator bit = m_atitc_message_queue.begin();
		ret = *bit;
		m_atitc_message_queue.erase(bit);
	}
	g_atitc_cs.Unlock();
	return ret;
}

std::wstring control::FetchDXT1FirstMessage()
{
	std::wstring ret ;
	g_dxt1_cs.Lock();
	if ( m_dxt1_message_queue.size() == 0)
	{
		ret = L"";
	}
	else
	{
		std::vector<std::wstring>::iterator bit = m_dxt1_message_queue.begin();
		ret = *bit;
		m_dxt1_message_queue.erase(bit);
	}
	g_dxt1_cs.Unlock();
	return ret;
}

std::wstring control::FetchETC1FirstMessage()
{
	std::wstring ret ;
	g_etc1_cs.Lock();
	if ( m_etc1_message_queue.size() == 0)
	{
		ret = L"";
	}
	else
	{
		std::vector<std::wstring>::iterator bit = m_etc1_message_queue.begin();
		ret = *bit;
		m_etc1_message_queue.erase(bit);
	}
	g_etc1_cs.Unlock();
	return ret;
}

int control::GetMessageCount()
{
	int count = 0;
	g_mtx.Lock();
	count = m_vMessages.size();
	g_mtx.Unlock();
	return count;
}

void control::ClearMessages()
{
	g_mtx.Lock();
	m_vMessages.clear();
	g_mtx.Unlock();
}

void mySystem(const wchar_t* cmd, const wchar_t* arg)
{
	SHELLEXECUTEINFO info = {0};
	info.cbSize = sizeof(SHELLEXECUTEINFO);
	info.fMask = SEE_MASK_NOCLOSEPROCESS;
	info.hwnd = NULL;
	info.lpVerb = NULL;
	info.lpFile = cmd;
	info.lpParameters = arg;
	info.lpDirectory = NULL;
	info.nShow = SW_HIDE;
	info.hInstApp = NULL;

	BOOL ret = ShellExecuteEx(&info);

	if( !ret )
	{
		::MessageBox(0, arg, L"Execute Error", NULL);
	}

	WaitForSingleObject(info.hProcess, INFINITE);
}


control::control(void)
{
	m_iMode = 0;
	m_status = ENone;
//	m_CompleteEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
	m_targetFormat = TF_None;
	m_bPvrQualityFast = false;
	m_bPvrQualityNormal = false;
	m_bPvrQualityHigh = false;
	m_bPvrQualityBest = false;
	m_genMipmaps = false;
	m_atitc_rgba_explicit = false;
	m_atitc_rgb = false;
	m_atitc_rgba_interpolated = false;
	m_mergeEtc = false;
	m_converted_files = 0;
	m_need_convert_files = 0;
	m_convert2 = 0;
	m_pvr_convert_status = ENone;
	m_atitc_convert_status = ENone;
	m_dxt1_convert_status = ENone;
	m_etc1_convert_status = ENone;
	rescale = 1.0f;

	wchar_t tmpString[MAX_PATH];//最后这个就记录输出文件格式
	GetPrivateProfileString(L"EXCLUDE", L"paths", L"", tmpString, MAX_PATH, L".\\AssetsConvert.ini");
	std::wstring inPath = tmpString;
	std::vector<std::wstring> delimiters = { L",", L" "};
	int nRet = split_string(inPath, delimiters, g_ExcludePaths);
	GetPrivateProfileString(L"EXCLUDE", L"copypaths", L"", tmpString, MAX_PATH, L".\\AssetsConvert.ini");
	inPath = tmpString;
	delimiters = { L",", L" " };
	nRet = split_string(inPath, delimiters, g_ExcludeCopyPaths);
}

control::~control(void)
{
	
}

void control::begin(wchar_t* d)
{
#if(0)
	m_artresDir = d;
	m_status = EProcessing;
	AfxBeginThread(&ProcessThread, this);
#endif
}

void control::begin_convert()
{
	if( g_control.m_convert2 & control::CT_PVR )
	{
		m_pvr_output_dir = g_control.m_inputDir + L"_pvr";

		if( check_dir_existence(m_pvr_output_dir) )
		{
			std::wstring err_msg = L"PVR Output Dir: " + m_pvr_output_dir + L" has exist";
			::MessageBox( 0, err_msg.c_str(), L"Error", 0 );
		}
		else
		{
			//copy_entier_directory(g_control.m_inputDir, m_pvr_output_dir );
			AfxBeginThread(&convert2pvr, NULL);
		}
	}

	if( g_control.m_convert2 & control::CT_ATITC )
	{
		m_atitc_output_dir = g_control.m_inputDir + L"_atitc";
		if( check_dir_existence(m_atitc_output_dir) )
		{
			std::wstring err_msg = L"ATITC Output Dir: " + m_atitc_output_dir + L" has exist";
			::MessageBox( 0, err_msg.c_str(), L"Error", 0 );
		}
		else
		{
			//copy_entier_directory(g_control.m_inputDir, m_atitc_output_dir );
			AfxBeginThread(&convert2atitc, NULL);
		}
	}

	if( g_control.m_convert2 & control::CT_DXT1 )
	{
		m_dxt1_output_dir = g_control.m_inputDir + L"_dxt1";

		if( check_dir_existence(m_dxt1_output_dir) )
		{
			std::wstring err_msg = L"ATITC Output Dir: " + m_dxt1_output_dir + L" has exist";
			::MessageBox( 0, err_msg.c_str(), L"Error", 0 );
		}
		else
		{
			//copy_entier_directory(g_control.m_inputDir, m_dxt1_output_dir );
			AfxBeginThread(&convert2dxt1, NULL);
		}
	}

	if( g_control.m_convert2 & control::CT_ETC1 )
	{
		m_etc1_output_dir = g_control.m_inputDir + L"_etc1";

		if( check_dir_existence(m_etc1_output_dir) )
		{
			std::wstring err_msg = L"ETC1 Output Dir: " + m_etc1_output_dir + L" has exist";
			::MessageBox( 0, err_msg.c_str(), L"Error", 0 );
		}
		else
		{
			//copy_entier_directory(g_control.m_inputDir, m_etc1_output_dir );
			AfxBeginThread(&convert2etc1, NULL);
		}
	}
}

void control::abort()
{

}

UINT convert2pvr( LPVOID pArg )
{
	UNREFERENCED_PARAMETER(pArg);
	
	g_control.m_pvr_convert_status = control::EProcessing;
	
	std::wstring premsg;
	premsg += L"Preparing assets: " + g_control.m_pvr_output_dir;

	g_control.AddPvrMessage(premsg);

	copy_entier_directory_mt(g_control.m_inputDir, g_control.m_pvr_output_dir, std::tr1::bind( &control::AddPvrMessage, &g_control, std::tr1::placeholders::_1 ) );

	dir in_dir( g_control.m_pvr_output_dir );

	std::vector<dir::item> need_convert_items;
	std::vector<std::wstring> filters;
	filters.push_back(L"png");
	filters.push_back(L"jpg");
	filters.push_back(L"dds");
	filters.push_back(L"tga");
	filters.push_back(L"bmp");
	//filters.push_back(L"map_");
	//in_dir.get_file_item_recursive(need_convert_items, filters);
	in_dir.get_file_png_item_recursive(need_convert_items, filters,g_control.m_in_dir_items, g_control.rescale);

	std::wstring mipmaps;
	std::wstring quality = L" -q pvrtchigh";//" -q pvrtcnormal";//由正常品质变为高品质

	for( size_t i = 0; i < need_convert_items.size(); i++ )
	{
		const dir::item& cur_item = need_convert_items[i];
		std::wstring itemFullPath = g_control.m_in_dir_items[i]->fullPath;

		bool bExclude = false;
		for (size_t iItem = 0; iItem < g_ExcludePaths.size(); ++iItem)
		{
			std::wstring strExclude = L"\\" + g_ExcludePaths[iItem];
			strExclude += L"\\";
			size_t nRet = itemFullPath.find(strExclude);
			if (nRet != std::wstring::npos)
			{
				bExclude = true;
				break;
			}
		}
		if (bExclude) continue;

		if(cur_item.isCreateMipMap)
		{
			mipmaps = L" -m ";
		}
		else
		{
			mipmaps = L"";
		}

		int ficy = -1;
		unsigned int w = 0, h = 0;

		FreeImageWrapper::fiwGetImageInfo(cur_item.fullPath, ficy, w, h);

		//( (w % 2) != 0 || (h % 2) != 0 ) 
		float fW = log2f(w);
		float fH = log2f(h);
		if (fW == (int)fW && fH ==(int)fH)
		{
		}
		else 
		{
			std::wstring warn_msg = L"======Warning: ";
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			warn_msg += buff;
			warn_msg += cur_item.fullPath;
			warn_msg += L" is not power of 2 image file, convertor will skipped it !";

			g_control.AddPvrMessage(warn_msg);

			if (cur_item.converted_fullpath != L"")
			{
				DeleteFile(cur_item.fullPath.c_str());
			}
			
			continue;
		}

		if( ficy != FIC_RGB && ficy != FIC_RGBALPHA )
		{
			std::wstring warn_msg = L"======Warning: ";
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			warn_msg += buff;
			warn_msg += cur_item.fullPath;
			warn_msg += L" is not rgb or rgba image file !";

			g_control.AddPvrMessage(warn_msg);
		}

	//	if( __convert2pvr(quality, mipmaps, cur_item.fullPath) ) 
		if( __convert2pvr(quality, mipmaps, cur_item) )
		{
			std::wstring msg;
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			msg += buff;
			msg += L" Convert ";
			msg += cur_item.fullPath;
			msg += L" succeeded !";

			g_control.AddPvrMessage(msg);
		}
	}

	std::wstring msg;
	msg += L"************ pvr assets convert complete ! ************";

	g_control.AddPvrMessage(msg);

	g_control.m_pvr_convert_status = control::ENone;

	return 0;
}



bool __convert2pvr( const std::wstring& quality, const std::wstring& mipmaps, const dir::item& input_item )
{
	std::wstring input_file, pvr_file;

	if( input_item.converted_png && input_item.fullPath != L"" )
	{
		input_file = input_item.converted_fullpath;
	}
	else
	{
		input_file = input_item.fullPath;
	}

	pvr_file = input_file;

	size_t _pos = pvr_file.rfind(L".");

	pvr_file.replace( _pos, pvr_file.length()-_pos, L".pvr" );

	std::wstring arg = L" -i ";

	arg += input_file;

	arg += L" ";
	
	arg += mipmaps;

	arg += L" ";

	arg += quality;

	arg += L" -l -f PVRTC1_4 -o ";

	arg += pvr_file;

	mySystem(L"PVRTexToolCL.exe", arg.c_str());

	DeleteFile( input_file.c_str() );

	if( input_item.converted_png )
	{
		DeleteFile( input_item.fullPath.c_str() );
	}

	MoveFile( pvr_file.c_str(), input_item.fullPath.c_str() );

	return true;
}


UINT convert2atitc( LPVOID pArg )
{
	UNREFERENCED_PARAMETER( pArg );

	g_control.m_atitc_convert_status = control::EProcessing;

	std::wstring premsg;
	premsg += L"Preparing assets: " + g_control.m_atitc_output_dir;

	g_control.AddATITCMessage(premsg);

	void (control::*pAddATITCMessage)( const std::wstring& );
	pAddATITCMessage = &control::AddATITCMessage;

	copy_entier_directory_mt(g_control.m_inputDir, g_control.m_atitc_output_dir, std::tr1::bind(&control::AddATITCMessage, &g_control, std::tr1::placeholders::_1) );

	dir in_dir( g_control.m_atitc_output_dir );

	std::vector<dir::item> need_convert_items;
	std::vector<std::wstring> filters;

	filters.push_back(L"png");
	filters.push_back(L"tga");
	filters.push_back(L"dds");
	filters.push_back(L"jpg");
	filters.push_back(L"bmp");
	//filters.push_back(L"map_");
//	in_dir.get_file_item_recursive(need_convert_items, filters);
	in_dir.get_file_png_item_recursive(need_convert_items, filters,g_control.m_in_dir_items, g_control.rescale);

	std::wstring mipmap;

	for( size_t i = 0; i < need_convert_items.size(); i++ )
	{
		const dir::item& cur_item = need_convert_items[i];
		std::wstring itemFullPath = g_control.m_in_dir_items[i]->fullPath;

		bool bExclude = false;
		for (size_t iItem = 0; iItem < g_ExcludePaths.size(); ++iItem)
		{
			std::wstring strExclude = L"\\" + g_ExcludePaths[iItem];
			strExclude += L"\\";
			size_t nRet = itemFullPath.find(strExclude);
			if (nRet != std::wstring::npos)
			{
				bExclude = true;
				break;
			}
		}
		if (bExclude) continue;

		if(cur_item.isCreateMipMap)
		{
			mipmap = L" -mipmaps ";
		}
		else
		{
			mipmap = L"";
		}

		int ficy = -1;
		unsigned int w = 0, h = 0;

		FreeImageWrapper::fiwGetImageInfo(cur_item.fullPath, ficy, w, h);

		//if( (w % 2) != 0 || (h % 2) != 0 )
		float fW = log2f(w);
		float fH = log2f(h);
		if (fW == (int)fW && fH == (int)fH)
		{
		}
		else
		{
			std::wstring warn_msg = L"======Warning: ";
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			warn_msg += buff;
			warn_msg += cur_item.fullPath;
			warn_msg += L" is not power of 2 image file, convertor will skipped it !";

			g_control.AddATITCMessage(warn_msg);

			if (cur_item.converted_fullpath != L"")
			{
				DeleteFile(cur_item.fullPath.c_str());
			}
			continue;
		}

		if( ficy != FIC_RGB && ficy != FIC_RGBALPHA )
		{
			std::wstring warn_msg = L"======Warning: ";
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			warn_msg += buff;
			warn_msg += cur_item.fullPath;
			warn_msg += L" is not rgb or rgba image file !";

			g_control.AddATITCMessage(warn_msg);
		}
		//if(__convert2atitc( mipmap, cur_item.fullPath, ficy == FIC_RGBALPHA ))
		if(__convert2atitc( mipmap, cur_item, ficy == FIC_RGBALPHA ))
		{
			std::wstring msg;
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			msg += buff;
			msg += L" Convert ";
			msg += (ficy == FIC_RGBALPHA) ? L" [RGBA] " : L" [RGB] ";
			msg += cur_item.fullPath;
			msg += L" succeeded !";

			g_control.AddATITCMessage(msg);
		}
	}

	std::wstring msg;
	msg += L"************ atitc assets convert complete ! ************";

	g_control.AddATITCMessage(msg);

	g_control.m_atitc_convert_status = control::ENone;

	return true;
}

bool __convert2atitc( const std::wstring& mipmaps, const dir::item& input_item, bool has_alpha  )
{
	std::wstring atitc_file, input_file;
	
	if( input_item.converted_png && input_item.converted_fullpath != L"" )
	{
		input_file = input_item.converted_fullpath;
	}
	else 
	{
		input_file = input_item.fullPath;
	}

	atitc_file = input_file;

	size_t _pos = atitc_file.rfind(L".");

	atitc_file.replace( _pos, atitc_file.length()-_pos, L"temp.dds" );

	std::wstring alpha_option = L" -format .dds +fourCC \"ATC \"";

	if( has_alpha )
	{
		alpha_option = L" -format .dds +fourCC \"ATCA\"";
	}

	std::wstring arg = L" -convert -skip ";
	arg += mipmaps;
	arg += L" \"";
	arg += input_file;
	arg += L"\" ";
	arg += L" \"";
	arg += atitc_file;
	arg += L"\" ";
	arg += alpha_option;

	mySystem(L"TheCompressonator.exe", arg.c_str());

	DeleteFile( input_file.c_str() );

	if( input_item.converted_png && input_item.converted_fullpath != L"" )
	{
		DeleteFile( input_item.fullPath.c_str() );
	}

	MoveFile( atitc_file.c_str(), input_item.fullPath.c_str() );

	return true;
}


UINT convert2dxt1( LPVOID pArg )
{
	UNREFERENCED_PARAMETER( pArg );
	
	g_control.m_dxt1_convert_status = control::EProcessing;

	std::wstring premsg;
	premsg += L"Preparing assets: " + g_control.m_dxt1_output_dir;

	g_control.AddDXT1Message(premsg);

	copy_entier_directory_mt(g_control.m_inputDir, g_control.m_dxt1_output_dir, std::tr1::bind( &control::AddDXT1Message, &g_control, std::tr1::placeholders::_1 ) );

	dir in_dir( g_control.m_dxt1_output_dir );

	std::vector<dir::item> need_convert_items;
	std::vector<std::wstring> filters;


	filters.push_back(L"png");
	filters.push_back(L"dds");
	filters.push_back(L"tga");
	filters.push_back(L"jpg");
	filters.push_back(L"bmp");
	//filters.push_back(L"map_");
//	in_dir.get_file_item_recursive(need_convert_items, filters);
	in_dir.get_file_png_item_recursive(need_convert_items, filters,g_control.m_in_dir_items, g_control.rescale);

	std::wstring mipmap;

	for( size_t i = 0; i < need_convert_items.size(); i++ )
	{
		const dir::item& cur_item = need_convert_items[i];
		std::wstring itemFullPath = g_control.m_in_dir_items[i]->fullPath;

		bool bExclude = false;
		for (size_t iItem = 0; iItem < g_ExcludePaths.size(); ++iItem)
		{
			std::wstring strExclude = L"\\" + g_ExcludePaths[iItem];
			strExclude += L"\\";
			size_t nRet = itemFullPath.find(strExclude);
			if (nRet != std::wstring::npos)
			{
				bExclude = true;
				break;
			}
		}
		if (bExclude) continue;

		if(cur_item.isCreateMipMap)
		{
			mipmap = L" -mipmaps ";
		}
		else
		{
			mipmap = L"";
		}

		int ficy = -1;
		unsigned int w = 0, h = 0;

		FreeImageWrapper::fiwGetImageInfo(cur_item.fullPath, ficy, w, h);

		//if( (w % 2) != 0 || (h % 2) != 0 )
		float fW = log2f(w);
		float fH = log2f(h);
		if (fW == (int)fW && fH == (int)fH)
		{
		}
		else
		{
			std::wstring warn_msg = L"======Warning: ";
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			warn_msg += buff;
			warn_msg += cur_item.fullPath;
			warn_msg += L" is not power of 2 image file, convertor will skipped it !";

			g_control.AddDXT1Message(warn_msg);

			if (cur_item.converted_fullpath != L"")
			{
				DeleteFile(cur_item.fullPath.c_str());
			}
			continue;
		}

		if( ficy != FIC_RGB && ficy != FIC_RGBALPHA )
		{
			std::wstring warn_msg = L"======Warning: ";
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			warn_msg += buff;
			warn_msg += cur_item.fullPath;
			warn_msg += L" is not rgb or rgba image file !";

			g_control.AddDXT1Message(warn_msg);
		}

//		if(__convert2dxt1( mipmap, cur_item.fullPath, ficy == FIC_RGBALPHA ))
		if(__convert2dxt1( mipmap, cur_item, ficy == FIC_RGBALPHA ))
		{
			std::wstring msg;
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			msg += buff;
			msg += L" Convert ";
			msg += (ficy == FIC_RGBALPHA) ? L" [RGBA] " : L" [RGB] ";
			msg += cur_item.fullPath;
			msg += L" succeeded !";

			g_control.AddDXT1Message(msg);
		}

	}

	std::wstring msg;
	msg += L"************ dxt1 assets convert complete ! ************";

	g_control.AddDXT1Message(msg);

	g_control.m_dxt1_convert_status = control::ENone;

	return true;
}

bool __convert2dxt1( const std::wstring& mipmaps, const dir::item& input_item, bool has_alpha )
{
	std::wstring dxt1_file, input_file;

	if( input_item.converted_png && input_item.converted_fullpath != L"" )
	{
		input_file = input_item.converted_fullpath;
	}
	else
	{
		input_file = input_item.fullPath;
	}

	dxt1_file = input_file;

	size_t _pos = dxt1_file.rfind(L".");

	dxt1_file.replace( _pos, dxt1_file.length()-_pos, L"temp.dds" );

	std::wstring alpha_option = L" ";

	if( has_alpha )
	{
		alpha_option = L" +alpha_threshold 128 ";
	}

	std::wstring arg = L" -convert -skip ";

	arg += mipmaps;

	arg += L" \"";
	arg += input_file;
	arg += L"\" ";
	arg += L" \"";
	arg += dxt1_file;
	arg += L"\" ";

	if( has_alpha )
	{
		arg += L" -format .dds +fourCC \"DXT3\"";
		arg += alpha_option;
	}
	else
	{
		arg += L" -format .dds +fourCC \"DXT1\"";
	}

	mySystem(L"TheCompressonator.exe", arg.c_str());

	DeleteFile( input_file.c_str() );

	if( input_item.converted_png && input_item.converted_fullpath != L"" )
	{
		DeleteFile( input_item.fullPath.c_str() );
	}

	MoveFile( dxt1_file.c_str(), input_item.fullPath.c_str() );

	return true;
}

UINT convert2etc1( LPVOID pArg )
{
	UNREFERENCED_PARAMETER( pArg );

	g_control.m_etc1_convert_status = control::EProcessing;

	std::wstring premsg;
	premsg += L"Preparing assets: " + g_control.m_etc1_output_dir;

	g_control.AddETC1Message(premsg);

	copy_entier_directory_mt(g_control.m_inputDir, g_control.m_etc1_output_dir , std::tr1::bind(&control::AddETC1Message, &g_control, std::tr1::placeholders::_1));

	dir in_dir( g_control.m_etc1_output_dir );

	std::vector<dir::item> need_convert_items;
	std::vector<std::wstring> filters;

	filters.push_back(L"png");
	filters.push_back(L"tga");
	filters.push_back(L"dds");
	filters.push_back(L"jpg");
	filters.push_back(L"bmp");
	//filters.push_back(L"map_");
//	in_dir.get_file_item_recursive(need_convert_items, filters);
	in_dir.get_file_png_item_recursive(need_convert_items, filters,g_control.m_in_dir_items, g_control.rescale);

	for( size_t i = 0; i < need_convert_items.size(); i++ )
	{
		const dir::item& cur_item = need_convert_items[i];
		std::wstring itemFullPath = g_control.m_in_dir_items[i]->fullPath;

		bool bExclude = false;
		for (size_t iItem = 0; iItem < g_ExcludePaths.size(); ++iItem)
		{
			std::wstring strExclude = L"\\" + g_ExcludePaths[iItem];
			strExclude += L"\\";
			size_t nRet = itemFullPath.find(strExclude);
			if (nRet != std::wstring::npos)
			{
				bExclude = true;
				break;
			}
		}
		if (bExclude) continue;

		int ficy = -1;
		unsigned int w = 0, h = 0;

		FreeImageWrapper::fiwGetImageInfo(cur_item.fullPath, ficy, w, h);

		//if( (w % 2) != 0 || (h % 2) != 0 )
		float fW = log2f(w);
		float fH = log2f(h);
		if (fW == (int)fW && fH == (int)fH)
		{
		}
		else
		{
			std::wstring warn_msg = L"======Warning: ";
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			warn_msg += buff;
			warn_msg += cur_item.fullPath;
			warn_msg += L" is not power of 2 image file, convertor will skipped it !";

			g_control.AddETC1Message(warn_msg);

			if (cur_item.converted_fullpath != L"")
			{
				DeleteFile(cur_item.fullPath.c_str());
			}
			continue;
		}

		if( ficy != FIC_RGB && ficy != FIC_RGBALPHA )
		{
			std::wstring warn_msg = L"======Warning: ";
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			warn_msg += buff;
			warn_msg += cur_item.fullPath;
			warn_msg += L" is not rgb or rgba image file !";

			g_control.AddETC1Message(warn_msg);
		}

		std::wstring full_path = cur_item.fullPath;

		std::wstring out_dir = full_path.substr(0, full_path.length() - cur_item.fileName.length() - 1);

//		if( __convert2etc1( g_control.m_genMipmaps, cur_item.fullPath, out_dir, ficy == FIC_RGBALPHA ) )
		if( __convert2etc1( cur_item.isCreateMipMap, cur_item, out_dir, ficy == FIC_RGBALPHA ) )
		{
			std::wstring msg;
			wchar_t buff[128];
			wsprintf(buff, L"[%d/%d]", i + 1, need_convert_items.size());
			msg += buff;
			msg += L" Convert ";
			msg += (ficy == FIC_RGBALPHA) ? L" [RGBA] " : L" [RGB] ";
			msg += cur_item.fullPath;
			msg += L" succeeded !";

			g_control.AddETC1Message(msg);
		}

	}

	std::wstring msg;
	msg += L"************ etc1 assets convert complete ! ************";

	g_control.AddETC1Message(msg);

	g_control.m_etc1_convert_status = control::ENone;

	return true;
}

bool __convert2etc1( bool mipmaps, const dir::item& input_item, const std::wstring& output_dir, bool has_alpha )
{

	std::wstring etc1_file, input_file;

	if( input_item.converted_png && input_item.converted_fullpath != L"" )
	{
		input_file = input_item.converted_fullpath;
	}
	else
	{
		input_file = input_item.fullPath;
	}

	if( !has_alpha )
	{
		std::wstring etc1_file = input_file;

		size_t _pos = etc1_file.rfind(L".");

		etc1_file.replace( _pos, etc1_file.length()-_pos, L".pvr" );

		std::wstring mipmap = L" ";

		if( mipmaps )
		{
			mipmap = L" -m ";
		}

		std::wstring arg;
		arg += L" -i ";
		arg += input_file;
		arg += mipmap;
		arg += L" -f ETC1 -o ";
		arg += etc1_file;

		mySystem(L"PVRTexToolCL.exe", arg.c_str());

		DeleteFile( input_file.c_str() );

		if( input_item.converted_png && input_item.converted_fullpath != L"" )
		{
			DeleteFile( input_item.fullPath.c_str() );
		}

		MoveFile( etc1_file.c_str(), input_item.fullPath.c_str() );

		return true;
	}

	else
	{
		std::wstring etc1rgba_file = input_file;
		std::wstring etc1rgb_file = input_file;
		std::wstring etc1a_file = input_file;

		size_t _pos = etc1rgba_file.rfind(L".");

		etc1rgba_file.replace( _pos, etc1rgba_file.length()-_pos, L".pvr" );

		etc1rgb_file.replace(_pos, etc1rgb_file.length() - _pos, L".ktx");

		etc1a_file.replace( _pos, etc1a_file.length() - _pos, L"_alpha.ktx" );

		std::wstring mipmap = L" ";

		if( mipmaps )
		{
			mipmap = L" -mipmaps ";
		}

		std::wstring arg;

		arg += L" ";
		arg += input_file;
		arg += L" ";
		arg += output_dir;
		arg += L" ";
		arg += mipmap;
		arg += L" -c etc1 -as -ktx ";

		mySystem(L"etcpack.exe", arg.c_str());

		DeleteFile( input_file.c_str() );

		if( input_item.converted_png && input_item.converted_fullpath != L"" )
		{
			DeleteFile( input_item.fullPath.c_str() );
		}

		std::fstream ofs_rgba, ofs_rgb, ofs_a;

		if( input_item.converted_png && input_item.converted_fullpath != L"" )
		{
			ofs_rgba.open( input_item.fullPath.c_str(), std::ios::binary | std::ios::out );
		}
		else
		{
			ofs_rgba.open( input_file.c_str(), std::ios::binary | std::ios::out );
		}

		if( ofs_rgba.fail() )
		{
			//::MessageBox(NULL, L"Create File Merge File failed!", L"Error", MB_OK);
			return -1;
		}

		// 1) step : write identifier into file stream
		int identifier = FourCC<'E', 'T', 'C', 'A'>::value;
		ofs_rgba.write((const char*)&identifier, sizeof(int));

		// 2) step: write rgb file into file stream
		ofs_rgb.open( etc1rgb_file.c_str(), std::ios::binary | std::ios::in );

		if( ofs_rgb.fail() )
		{
			//::MessageBox(NULL, L"open etc1 rgb file failed !", L"Error", MB_OK);
			return -1;
		}

		ofs_rgb.seekg(0, std::ios::end);

		unsigned int rgb_size = ofs_rgb.tellg();

		ofs_rgb.seekg(0, std::ios::beg);

		ofs_rgba.write( (const char*)&rgb_size, sizeof(unsigned int) );

		ofs_a.open( etc1a_file.c_str(), std::ios::binary | std::ios::in );
		if( ofs_a.fail() )
		{
			//::MessageBox(NULL, L"open etc1 alpha file failed!", L"Error", MB_OK);
			return -1;				 
		}

		ofs_rgba << ofs_a.rdbuf();
		ofs_rgba << ofs_rgb.rdbuf();
		ofs_rgba.close();
		ofs_rgb.close();
		ofs_a.close();

		DeleteFile( etc1a_file.c_str() );
		DeleteFile( etc1rgb_file.c_str() );


		return true;
	}

	return false;
}
