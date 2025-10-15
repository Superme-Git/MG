#include "StdAfx.h"
#include "control.h"
#include "filesystem.h"

#include <afxmt.h>
#include <cstdlib>
#include <cstdio>
#include <fstream>
#include <algorithm>

 int dxt1_count = 0;
 int total_count = 0;

CMutex g_mtx;
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

	ShellExecuteEx(&info);
	WaitForSingleObject(info.hProcess, INFINITE);
}

//void execute_cmd(std::wstring command)
//{
//	char cmd[1024];
//	size_t len;
//	wcstombs_s(&len , &cmd[0], 1024, (wchar_t*)command.c_str(), command.length());
//	WinExec(cmd, SW_HIDE);
//}




UINT ConvertingThread( LPVOID pArg )
{
	UNREFERENCED_PARAMETER(pArg);

	g_control.m_status = control::EProcessing;

	switch( g_control.m_targetFormat )
	{
	case control::TF_PVR:
		convert_to_pvr();
		break;

	case control::TF_ETC1_RGB8:
		convert_to_etc1();
		break;

	case control::TF_ETC1_RGBA8_AA:
		convert_to_etc1_rgba_aa();
		break;

	case control::TF_ETC1_RGBA8_AS:
		convert_to_etc1_rgba_as();
		break;

	case control::TF_ATITC:
		convert_to_atitc();
		break;

	case control::TF_DXT1:
		convert_to_dxt1();
		break;
	}


	g_control.m_status = control::ENone;
	
	return 0;
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
	AfxBeginThread(&ConvertingThread, NULL);
//	ConvertingThread(NULL);
}

void control::abort()
{

}



int convert_to_pvr()
{
	dir in_dir( g_control.m_inputDir );

	std::wstring out_dir = g_control.m_outputDir;

	if( out_dir[out_dir.size() - 1] != '\\' || out_dir[out_dir.size() - 1] != '/' )
	{
		out_dir += L"\\";
	}

	std::wstring mipmaps = L"";

	if( g_control.m_genMipmaps )
	{
		mipmaps = L" -m ";
	}
	
	std::wstring quality = L" -q pvrtcnormal";

	if( g_control.m_bPvrQualityNormal )
	{
		quality = L" -q pvrtcnormal";
	}

	else if( g_control.m_bPvrQualityHigh )
	{
		quality = L" -q pvrtchigh";
	}

	else if( g_control.m_bPvrQualityBest )
	{
		quality = L" -q pvrtcbest";
	}

	else if( g_control.m_bPvrQualityFast )
	{
		quality = L" -q pvrtcfast";
	}

	std::wstring _ext_name = L"";
	if( g_control.m_output_ext == control::OE_PVR )
	{
		_ext_name = L".pvr";
	}

	VF cpyfs = in_dir.get_files();

	for (unsigned int i = 0; i < cpyfs.size(); ++i)
	{
		std::wstring _ext = in_dir.m_items[i].ext;
		
		std::transform(_ext.begin(), _ext.end(), _ext.begin(), tolower);

		if( in_dir.m_items[i].isDir ) continue;

		if( _ext != L"png" && _ext != L"tga" && _ext != L"jpg") 
		{
			std::wstring _msg;
			wchar_t buff[128];
			wsprintf(buff, L"Warning: [%d/%d]", i + 1, cpyfs.size());
			_msg = L"Invalid Input file ";
			_msg += buff;
			_msg += cpyfs[i];
			g_control.AddMessage( _msg.c_str(), control::message::error );

			continue;
		}

		if( g_control.m_output_ext == control::OE_ORIG )
		{
			_ext_name = std::wstring(L".") + _ext;
		}

		dir::item& cur_item = in_dir.m_items[i];

		std::wstring target_file = out_dir + cur_item.fileName;

		size_t _npos = target_file.rfind(L".");

		if( _npos == std::wstring::npos )
		{
			return -1;
		}

		target_file.replace( _npos, target_file.length() - _npos, L".pvr" );

		std::wstring msg;
		wchar_t buff[128];
		wsprintf(buff, L"[%d/%d]", i + 1, cpyfs.size());
		msg = buff;
		msg += L"Convert ";
		msg += cpyfs[i];
		msg += L" to ";
		msg += target_file;

		g_control.AddMessage(msg.c_str());	
		std::wstring arg;

		// Do Converting;-i D:\work_space_0\BBZS_new\client\Media\Scene\szwyj\szwyj_0_0.png -m -f PVRTC1_4 -o D:\work_space_0\BBZS_new\client\Media\Scene\szwyj\szwyj_0_0.pvr
		arg += L" -i ";
		arg += cpyfs[i];

		arg += mipmaps;
		arg += quality;
		arg += L" -l -f PVRTC1_4 -o ";
		arg += target_file;
		mySystem(L"PVRTexToolCL.exe", arg.c_str());

		std::wstring exsit_file = target_file;
		target_file.replace( _npos, target_file.length() - _npos, _ext_name );
		MoveFileEx( exsit_file.c_str(), target_file.c_str(), MOVEFILE_REPLACE_EXISTING ); // temporary add
	}

	std::wstring msg;
	wchar_t buff[128];
	wsprintf(buff, L"Convert complete !");
	msg = buff;
	g_control.AddMessage(msg.c_str());	

	return 0;

}

int convert_to_etc1()
{
	dir in_dir( g_control.m_inputDir );

	std::wstring out_dir = g_control.m_outputDir;

	if( out_dir[out_dir.size() - 1] != '\\' || out_dir[out_dir.size() - 1] != '/' )
	{
		out_dir += L"\\";
	}

	std::wstring mipmaps = L"";
	std::wstring prema = L" -p ";
	if( g_control.m_genMipmaps )
	{
		mipmaps = L" -m ";
	}

	std::wstring _ext_name = L"";
	if( g_control.m_output_ext == control::OE_PVR )
	{
		_ext_name = L".pvr";
	}

	VF cpyfs = in_dir.get_files();

	for (unsigned int i = 0; i < cpyfs.size(); ++i)
	{
		std::wstring _ext = in_dir.m_items[i].ext;

		std::transform(_ext.begin(), _ext.end(), _ext.begin(), tolower);

		if( in_dir.m_items[i].isDir ) continue;

		if( _ext != L"png" && _ext != L"tga" && _ext != L"jpg") 
		{
			std::wstring _msg;
			wchar_t buff[128];
			wsprintf(buff, L"Warning: [%d/%d]", i + 1, cpyfs.size());
			_msg = L"Invalid Input file ";
			_msg += buff;
			_msg += cpyfs[i];
			g_control.AddMessage( _msg.c_str(), control::message::error );

			continue;
		}

		if( g_control.m_output_ext == control::OE_ORIG )
		{
			_ext_name = std::wstring(L".") + _ext;
		}

		dir::item& cur_item = in_dir.m_items[i];

		std::wstring target_file = out_dir + cur_item.fileName;

		size_t _npos = target_file.rfind(L".");

		if( _npos == std::wstring::npos )
		{
			return -1;
		}

		target_file.replace( _npos, target_file.length() - _npos, L".pvr" );

		std::wstring msg;
		wchar_t buff[128];
		wsprintf(buff, L"[%d/%d]", i + 1, cpyfs.size());
		msg = buff;
		msg += L"Convert ";
		msg += cpyfs[i];
		msg += L" to ";
		msg += target_file;

		g_control.AddMessage(msg.c_str());	
		std::wstring arg;

		// Do Converting;-i D:\work_space_0\BBZS_new\client\Media\Scene\szwyj\szwyj_0_0.png -m -f PVRTC1_4 -o D:\work_space_0\BBZS_new\client\Media\Scene\szwyj\szwyj_0_0.pvr
		arg += L" -i ";
		arg += cpyfs[i];
		arg += mipmaps;
		arg += prema;
		arg += L" -f ETC1 -o ";
		arg += target_file;
		mySystem(L"PVRTexToolCL.exe", arg.c_str());
		std::wstring exsit_file = target_file;
		target_file.replace( _npos, target_file.length() - _npos, _ext_name );

		MoveFileEx( exsit_file.c_str(), target_file.c_str(), MOVEFILE_REPLACE_EXISTING );

	}

	std::wstring msg;
	wchar_t buff[128];
	wsprintf(buff, L"Convert complete !");
	msg = buff;
	g_control.AddMessage(msg.c_str());	

//	g_control.m_status = control::ENone;

	return 0;
}

int convert_to_atitc()
{

	dir in_dir( g_control.m_inputDir );

	std::wstring out_dir = g_control.m_outputDir;

	if( out_dir[out_dir.size() - 1] != '\\' || out_dir[out_dir.size() - 1] != '/' )
	{
		out_dir += L"\\";
	}

	std::wstring mipmaps = L"";

	if( g_control.m_genMipmaps )
	{
		mipmaps = L" -mipmaps ";
	}

	std::wstring _ext_name = L"";
	if( g_control.m_output_ext == control::OE_PVR )
	{
		_ext_name = L".pvr";
	}

	std::wstring alpha_option = L" -format .dds +fourCC \"ATC \"";
	if( g_control.m_atitc_rgb )
	{
		alpha_option = L" -format .dds +fourCC \"ATC \"";
	}

	else if( g_control.m_atitc_rgba_explicit )
	{
		alpha_option = L" -format .dds +fourCC \"ATCA\"";
	}

	else if( g_control.m_atitc_rgba_interpolated )
	{
		alpha_option = L" -format .dds +fourCC \"ATCI\"";
	}

	VF cpyfs = in_dir.get_files();

	for (unsigned int i = 0; i < cpyfs.size(); ++i)
	{
		std::wstring _ext = in_dir.m_items[i].ext;

		std::transform(_ext.begin(), _ext.end(), _ext.begin(), tolower);

		if( in_dir.m_items[i].isDir ) continue;

		if( _ext != L"png" && _ext != L"tga" && _ext != L"jpg") 
		{
			std::wstring _msg;
			wchar_t buff[128];
			wsprintf(buff, L"Warning: [%d/%d]", i + 1, cpyfs.size());
			_msg = L"Invalid Input file ";
			_msg += buff;
			_msg += cpyfs[i];
			g_control.AddMessage( _msg.c_str(), control::message::error );

			continue;
		}

		if( g_control.m_output_ext == control::OE_ORIG )
		{
			_ext_name = std::wstring(L".") + _ext;
		}

		dir::item& cur_item = in_dir.m_items[i];

		std::wstring target_file = out_dir + cur_item.fileName;

		size_t _npos = target_file.rfind(L".");

		if( _npos == std::wstring::npos )
		{
			return -1;
		}

		target_file.replace( _npos, target_file.length() - _npos, _ext_name );

		std::wstring exsit_file = target_file;
		exsit_file.replace( _npos, target_file.length() - _npos, L".DDS" );

		std::wstring msg;
		wchar_t buff[128];
		wsprintf(buff, L"[%d/%d]", i + 1, cpyfs.size());
		msg = buff;
		msg += L"Convert ";
		msg += cpyfs[i];
		msg += L" to ";
		msg += target_file;

		g_control.AddMessage(msg.c_str());	
		std::wstring arg;

		arg += L" -convert -skip ";
		arg += mipmaps;
		arg += L" \"";
		arg += cpyfs[i] ;
		arg += L"\" ";
		arg += L" \"";
		arg += target_file;
		arg += L"\" ";
		//arg += L" -format .dds +fourCC \"ATCA\"";
		arg += alpha_option;

		mySystem(L"TheCompressonator", arg.c_str());

		MoveFileEx( exsit_file.c_str(), target_file.c_str(), MOVEFILE_REPLACE_EXISTING );
	}

	std::wstring msg;
	wchar_t buff[128];
	wsprintf(buff, L"Convert complete !");
	msg = buff;
	g_control.AddMessage(msg.c_str());

	return 0;
}

int convert_to_dxt1()
{
	dir in_dir( g_control.m_inputDir );

	std::wstring out_dir = g_control.m_outputDir;

	if( out_dir[out_dir.size() - 1] != '\\' || out_dir[out_dir.size() - 1] != '/' )
	{
		out_dir += L"\\";
	}

	std::wstring _ext_name = L"";
	if( g_control.m_output_ext == control::OE_PVR )
	{
		_ext_name = L".pvr";
	}

	std::wstring mipmaps = L"";
	if( g_control.m_genMipmaps )
	{
		mipmaps = L" -mipmaps ";
	}

	std::wstring alpha_threshold = L"";

	if( g_control.m_dxt1Alpha )
	{
		alpha_threshold = L" +alpha_threshold 128 ";
	}

	VF cpyfs = in_dir.get_files();

	for (unsigned int i = 0; i < cpyfs.size(); ++i)
	{
		std::wstring _ext = in_dir.m_items[i].ext;

		std::transform(_ext.begin(), _ext.end(), _ext.begin(), tolower);

		if( in_dir.m_items[i].isDir ) continue;

		if( _ext != L"png" && _ext != L"tga" && _ext != L"jpg") 
		{
			std::wstring _msg;
			wchar_t buff[128];
			wsprintf(buff, L"Warning: [%d/%d]", i + 1, cpyfs.size());
			_msg = L"Invalid Input file ";
			_msg += buff;
			_msg += cpyfs[i];
			g_control.AddMessage( _msg.c_str(), control::message::error );
			continue;
		}

		if( g_control.m_output_ext == control::OE_ORIG )
		{
			_ext_name = std::wstring(L".") + _ext;
		}

		dir::item& cur_item = in_dir.m_items[i];

		std::wstring target_file = out_dir + cur_item.fileName;

		size_t _npos = target_file.rfind(L".");

		if( _npos == std::wstring::npos )
		{
			return -1;
		}

		target_file.replace( _npos, target_file.length() - _npos, _ext_name );

		std::wstring exsit_file = target_file;
		exsit_file.replace( _npos, target_file.length() - _npos, L".DDS" );

		std::wstring msg;
		wchar_t buff[128];
		wsprintf(buff, L"[%d/%d]", i + 1, cpyfs.size());
		msg = buff;
		msg += L"Convert ";
		msg += cpyfs[i];
		msg += L" to ";
		msg += target_file;

		g_control.AddMessage(msg.c_str());	
		std::wstring arg;

		arg += L" -convert -skip ";

		arg += mipmaps;

		arg += L" \"";
		arg += cpyfs[i] ;
		arg += L"\" ";
		arg += L" \"";
		arg += target_file;
		arg += L"\" ";
		arg += L" -format .dds +fourCC \"DXT1\"";
		arg += alpha_threshold;
		mySystem(L"TheCompressonator", arg.c_str());

		MoveFileEx( exsit_file.c_str(), target_file.c_str(), MOVEFILE_REPLACE_EXISTING );
	}

	std::wstring msg;
	wchar_t buff[128];
	wsprintf(buff, L"Convert complete !");
	msg = buff;
	g_control.AddMessage(msg.c_str());

	return 0;
}


int convert_to_etc1_rgba_aa()
{
	dir in_dir( g_control.m_inputDir );

	std::wstring out_dir = g_control.m_outputDir;

	if( out_dir[out_dir.size() - 1] != '\\' || out_dir[out_dir.size() - 1] != '/' )
	{
		out_dir += L"\\";
	}
	std::wstring mipmaps = L"";
	if( g_control.m_genMipmaps )
	{
		mipmaps = L" -mipmaps ";
	}
	std::wstring _ext_name = L"";
	if( g_control.m_output_ext == control::OE_PVR )
	{
		_ext_name = L".pvr";
	}

	VF cpyfs = in_dir.get_files();

	for (unsigned int i = 0; i < cpyfs.size(); ++i)
	{
		std::wstring _ext = in_dir.m_items[i].ext;


		std::transform(_ext.begin(), _ext.end(), _ext.begin(), tolower);

		if( in_dir.m_items[i].isDir ) continue;

		if( _ext != L"png" && _ext != L"tga" && _ext != L"jpg") 
		{
			std::wstring _msg;
			wchar_t buff[128];
			wsprintf(buff, L"Warning: [%d/%d]", i + 1, cpyfs.size());
			_msg = L"Invalid Input file ";
			_msg += buff;
			_msg += cpyfs[i];
			g_control.AddMessage( _msg.c_str(), control::message::error );
			continue;
		}


		if( g_control.m_output_ext == control::OE_ORIG )
		{
			_ext_name = std::wstring(L".") + _ext;
		}

		dir::item& cur_item = in_dir.m_items[i];

		std::wstring target_file = out_dir + cur_item.fileName;
		std::wstring target_ktx_file = target_file;
		size_t _npos = target_file.rfind(L".");

		if( _npos == std::wstring::npos )
		{
			return -1;
		}

		target_file.replace( _npos, target_file.length() - _npos, std::wstring(L".") + _ext_name ); // OUTPUT  FILE NAME

		target_ktx_file.replace( _npos, target_ktx_file.length() - _npos, L".ktx" );

		std::wstring msg;
		wchar_t buff[128];
		wsprintf(buff, L"[%d/%d]", i + 1, cpyfs.size());
		msg = buff;
		msg += L"Convert ";
		msg += cpyfs[i];
		msg += L" to ";
		msg += target_file;

		g_control.AddMessage(msg.c_str());	
		std::wstring arg;

		arg += L" ";
		arg += cpyfs[i];
		arg += L" ";
		arg += g_control.m_outputDir;
		arg += L" ";
		arg += mipmaps;

		arg += L" -c etc1 -aa -ktx";
		
		mySystem(L"etcpack", arg.c_str());

		MoveFileEx( target_ktx_file.c_str(), target_file.c_str(), MOVEFILE_REPLACE_EXISTING );

	}

	std::wstring msg;
	wchar_t buff[128];
	wsprintf(buff, L"Convert complete !");
	msg = buff;
	g_control.AddMessage(msg.c_str());

	return 0;
}

int convert_to_etc1_rgba_as()
{
	dir in_dir( g_control.m_inputDir );

	std::wstring out_dir = g_control.m_outputDir;

	if( out_dir[out_dir.size() - 1] != '\\' || out_dir[out_dir.size() - 1] != '/' )
	{
		out_dir += L"\\";
	}
	std::wstring mipmaps = L"";
	if( g_control.m_genMipmaps )
	{
		mipmaps = L" -mipmaps ";
	}

	std::wstring _ext_name = L"";
	if( g_control.m_output_ext == control::OE_PVR )
	{
		_ext_name = L".pvr";
	}

	VF cpyfs = in_dir.get_files();

	for (unsigned int i = 0; i < cpyfs.size(); ++i)
	{
		std::wstring _ext = in_dir.m_items[i].ext;

		std::transform(_ext.begin(), _ext.end(), _ext.begin(), tolower);

		if( in_dir.m_items[i].isDir ) continue;


		if( _ext != L"png" && _ext != L"tga" && _ext != L"jpg") 
		{
			std::wstring _msg;
			wchar_t buff[128];
			wsprintf(buff, L"Warning: [%d/%d]", i + 1, cpyfs.size());
			_msg = L"Invalid Input file ";
			_msg += buff;
			_msg += cpyfs[i];
			g_control.AddMessage( _msg.c_str(), control::message::error );
			continue;
		}

		if( g_control.m_output_ext == control::OE_ORIG )
		{
			_ext_name = std::wstring(L".") + _ext;
		}


		dir::item& cur_item = in_dir.m_items[i];

		std::wstring target_file = out_dir + cur_item.fileName;

		size_t _npos = target_file.rfind(L".");

		if( _npos == std::wstring::npos )
		{
			return -1;
		}
/*

		target_file.replace( _npos, target_file.length() - _npos, L".pvr" );

		std::wstring exsit_file = target_file;
		exsit_file.replace( _npos, target_file.length() - _npos, L".DDS" );
*/

		std::wstring exsit_rgb_file = target_file;
		exsit_rgb_file.replace(_npos, exsit_rgb_file.length() - _npos, L".ktx");

		std::wstring target_rgb_file = exsit_rgb_file;
		target_rgb_file.replace(_npos, target_rgb_file.length()-_npos, L".pvr");
		
		std::wstring exsit_alpha_file = target_file;
		exsit_alpha_file.replace(_npos, exsit_alpha_file.length() - _npos, L"_alpha.ktx");

		std::wstring target_alpha_file = exsit_alpha_file;
		target_alpha_file.replace(_npos, target_alpha_file.length()-_npos, L"_alpha.pvr");

		std::wstring msg;
		wchar_t buff[128];
		wsprintf(buff, L"[%d/%d]", i + 1, cpyfs.size());
		msg = buff;
		msg += L"Convert ";
		msg += cpyfs[i];
		msg += L" to ";
		msg += target_file;

		g_control.AddMessage(msg.c_str());	
		std::wstring arg;

		arg += L" ";
		arg += cpyfs[i];
		arg += L" ";
		arg += g_control.m_outputDir;
		arg += L" ";
		arg += mipmaps;

		arg += L" -c etc1 -as -ktx";

		mySystem(L"etcpack", arg.c_str());

		if( g_control.m_mergeEtc )
		{
			std::wstring rgba_name = target_rgb_file;
			rgba_name.replace( _npos, rgba_name.length() - _npos, _ext_name );
			std::fstream ofs_rgba, ofs_rgb, ofs_a;
			ofs_rgba.open( rgba_name.c_str(), std::ios::binary | std::ios::out );
			if( ofs_rgba.fail() )
			{
				::MessageBox(NULL, L"Create File Merge File faild!", L"Error", MB_OK);
				return -1;
			}
			// 1) step : write identifier into file stream
			int identifier = FourCC<'E', 'T', 'C', 'A'>::value;
			ofs_rgba.write((const char*)&identifier, sizeof(int));

			// 2) step: write rgb file into file stream
			ofs_rgb.open( exsit_rgb_file.c_str(), std::ios::binary | std::ios::in );

			if( ofs_rgb.fail() )
			{
				::MessageBox(NULL, L"Create File Merge File faild!", L"Error", MB_OK);
				return -1;				 
			}

			// 2.1) step: write rgb file size into file stream
			ofs_rgb.seekg(0, std::ios::end);

			unsigned int rgb_size = ofs_rgb.tellg();

			ofs_rgb.seekg(0, std::ios::beg);

			ofs_rgba.write( (const char*)&rgb_size, sizeof(unsigned int) );

//			ofs_rgba << ofs_rgb.rdbuf();

			// 3) write alpha file into file stream
			ofs_a.open( exsit_alpha_file.c_str(), std::ios::binary | std::ios::in );
			if( ofs_a.fail() )
			{
				::MessageBox(NULL, L"Create File Merge File faild!", L"Error", MB_OK);
				return -1;				 
			}

			ofs_rgba << ofs_a.rdbuf();
			ofs_rgba << ofs_rgb.rdbuf();
			ofs_rgba.close();
			ofs_rgb.close();
			ofs_a.close();

		}

		// Do Converting;-i D:\work_space_0\BBZS_new\client\Media\Scene\szwyj\szwyj_0_0.png -m -f PVRTC1_4 -o D:\work_space_0\BBZS_new\client\Media\Scene\szwyj\szwyj_0_0.pvr

//		MoveFileEx( exsit_file.c_str(), target_file.c_str(), MOVEFILE_REPLACE_EXISTING );
	}

	std::wstring msg;
	wchar_t buff[128];
	wsprintf(buff, L"Convert complete !");
	msg = buff;
	g_control.AddMessage(msg.c_str());

	return 0;
}