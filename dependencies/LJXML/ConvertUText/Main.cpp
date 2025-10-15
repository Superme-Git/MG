#include <iostream>
#include <sstream>
#include "../Include/LJXMLStringHelper.hpp"
using namespace LJXMLStringHelper;
using namespace std;

unsigned int CodeToUI(wchar_t Code)
{
	unsigned int Result = 0;
	if (Code >= L'0' && Code <= L'9')
	{
		Result = Code - L'0';
	}
	else if (Code >= L'A' && Code <= L'F')
	{
		Result = Code - L'A' + 10;
	}
	else if (Code >= L'a' && Code <= L'f')
	{
		Result = Code - L'a' + 10;
	}
	return Result;
}

wstring CodesToText(wstring Codes)
{
	int CurIndex = 0;
	wstring Result;
	for (int i = 0; i < Codes.size() - 5; i++)
	{
		unsigned char Code1 = 0;
		unsigned char Code2 = 0;
		if (Codes[i] == L'\\' && Codes[i + 1] == L'u')
		{
			Code1 += CodeToUI(Codes[i + 2]) * 16;
			Code1 += CodeToUI(Codes[i + 3]) * 1;
			Code2 += CodeToUI(Codes[i + 4]) * 16;
			Code2 += CodeToUI(Codes[i + 5]) * 1;
			wchar_t Code = 0;
			Code += Code1 * 256;
			Code += Code2;
			Result += Code;
			i = i + 5;
		}
		else
		{
			Result += Codes[i];
		}
		CurIndex = i;
	}
	for (int i = CurIndex; i < Codes.size() - 1; i++)
	{
		Result += Codes[i + 1];
	}
	wstring Result2;
	CurIndex = Result.size() - 1;
	for (int i = 0; i < Result.size() - 4; i++)
	{
		//����߼���������ȥ������֮����ļ�����VS����ÿ���س�������ʾ�������е�����,������UEStudio����ʾ��������
		//�������ַ����е�ȷֻ��һ�ݵ�\r\n��vs�л���ʾ�ɶ��е�ԭ���в���ȷ,�Ʋ���ܺͺ����UTF16תUTF8ʱ����β�ַ��й�
		//Ŀǰ���ﻹ�ǲ����κδ�����
		//if (Result[i] == L'\r' && Result[i + 1] == L'\n')// && Result[i + 2] == L'\r' && Result[i + 3] == L'\n'
		//{
		//	//Result2 += L'\r';
		//	//i = i + 1;
		//	Result2 += Result[i];
		//}
		//else
		//{
		//	Result2 += Result[i];
		//}
		Result2 += Result[i];
		CurIndex = i;
	}
	for (int i = CurIndex; i < Result.size() - 1; i++)
	{
		Result2 += Result[i + 1];
	}
	return Result2;
}
int LoadFromStream(std::fstream& FS, char*& FileData, streamsize& SS)
{
	SS = 0;
	FS.seekg(0, std::ios_base::end);
	SS = FS.tellg();
	FS.seekg(0, std::ios_base::beg);
	FileData = (char*)malloc(SS + 1);
	FS.read(FileData, SS);
	FS.close();
	FileData[SS] = 0;
	return 0;
}
int LoadFromFile(std::string FileName, char*& FileData, streamsize& SS)
{
	std::fstream FS;
	FS.open(FileName.c_str(), std::ios_base::binary | std::ios_base::in);//std::ios_base::binary | 
	return LoadFromStream(FS, FileData, SS);
}
int LoadFromFile(std::wstring FileName, char*& FileData, streamsize& SS)
{
	std::fstream FS;
	FS.open(FileName.c_str(), std::ios_base::binary | std::ios_base::in);//std::ios_base::binary | 
	return LoadFromStream(FS, FileData, SS);
}
int SaveToStream(std::fstream& FS, wstring FileData, Encode EC)
{
	if (EC == UTF_16)
	{
		char c = 0xff;
		FS.write(&c, 1);
		c = 0xfe;
		FS.write(&c, 1);
		FS.write((char*)FileData.c_str(), FileData.size());
	}
	else if (EC == UTF_8)
	{
		char c = 0xef;
		FS.write(&c, 1);
		c = 0xbb;
		FS.write(&c, 1);
		c = 0xbf;
		FS.write(&c, 1);
		string s = U16ToU8(FileData);
		FS.write(s.c_str(), s.size());
	}
	else if (EC == UTF_8_NO_MARK)
	{
		string s = U16ToU8(FileData);
		FS.write(s.c_str(), s.size());
	}
	else if (EC == ANSI)
	{
		string s = ws2s(FileData);
		FS.write(s.c_str(), s.size());
	}
	return 0;
}
int SaveToFile(std::string FileName, wstring FileData, Encode EC)
{
	std::fstream FS;
	FS.open(FileName.c_str(), std::ios_base::out);
	SaveToStream(FS, FileData, EC);
	FS.close();
	return 0;
}
int SaveToFile(std::wstring FileName, wstring FileData, Encode EC)
{
	std::fstream FS;
	FS.open(FileName.c_str(), std::ios_base::out);
	SaveToStream(FS, FileData, EC);
	FS.close();
	return 0;
}

int main(int argc, char **argv)
{
	std::locale::global(std::locale(""));
	std::wcout << L"�˳��������޸������е������ַ�" << L"\n"; 
	std::wcout << L"����:\\u5F00\\u59CB\\u65F6\\u95F4�������ַ�����ת����:��ʼʱ��" << L"\n";
	wstring InFName;
	if (argc < 2)
	{
		std::wcout << L"������Դ���������϶��ļ�����EXE�Ͻ���ת��" << L"\n";
		system("PAUSE");
		exit(0);
	}
	std::wcout << L"�����ļ���Ϊ:" << argc - 1 << L"\n";
	for (int i = 1; i < argc; i++)
	{
		std::wstringstream ss;
		ss << argv[i];
		ss >> InFName;
		//InFName = L"F:\\Work\\MT_G\\client\\FireClient\\Application\\SceneObj\\DroptItem.h";
		//InFName = L"F:\\Work\\MT_G\\client\\FireClient\\Application\\SceneObj\\GameApplication.cpp";
		std::wcout << i << L":" << InFName << L"\n";
		wstring OutFName;
		OutFName = InFName + L".new";
		char* FileData = NULL;
		streamsize SS;
		LoadFromFile(InFName, FileData, SS);
		wstring WS;
		Encode EC;
		reallyLoadFromMemory((unsigned char*)FileData, SS, WS, EC);
		wstring Result;
		Result = CodesToText(WS);
		SaveToFile(OutFName, Result, EC);
		std::wcout << i << OutFName << L":���" << L"\n";
	}
	system("PAUSE");
}