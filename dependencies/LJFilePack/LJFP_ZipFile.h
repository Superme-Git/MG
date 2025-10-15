#ifndef LJFP_ZipFile_H
#define LJFP_ZipFile_H

#include "LJFP_Var.h"

class LJFP_ZipFile
{
public:
	unsigned int m_uiKey;
	CRC32_Func m_CRC32Func;
	Zip_Func m_ZipFunc;
	UnZip_Func m_UnZipFunc;
	SMS4_Func m_SMS4Func;
	DeSMS4_Func m_DeSMS4Func;
	std::string m_strPassword;

	LJFP_ZipFile(unsigned int uiKey, CRC32_Func CRC32Func, Zip_Func ZipFunc, UnZip_Func UnZipFunc, SMS4_Func SMS4Func, DeSMS4_Func DeSMS4Func, std::string strPassword)
	{
		m_uiKey = uiKey;
		m_CRC32Func = CRC32Func;
		m_ZipFunc = ZipFunc;
		m_UnZipFunc = UnZipFunc;
		m_SMS4Func = SMS4Func;
		m_DeSMS4Func = DeSMS4Func;
		m_strPassword = strPassword;
	}
	~LJFP_ZipFile()
	{

	}
	int ZipStream(std::ifstream& FSSrc, std::ofstream& FSDst)
	{
		FSDst.seekp(0, std::ios_base::beg);
		FSSrc.seekg(0, std::ios_base::end);
		//Src--
		unsigned int SizeSrc = FSSrc.tellg();
		FSSrc.seekg(0, std::ios_base::beg);
		unsigned char* DataSrc = new unsigned char[SizeSrc];
		FSSrc.read((char*)&DataSrc[0], SizeSrc);
		unsigned int CRC32Src = m_CRC32Func(0, DataSrc, SizeSrc);
		//Zip--
		unsigned int SizeZip = SizeSrc + 256;
		unsigned char* DataZip = new unsigned char[SizeZip];		
		int iResult = m_ZipFunc(DataZip, &SizeZip, DataSrc, SizeSrc, 9);
		if (DataSrc){ delete[] DataSrc; }
		if (iResult != 0)
		{
			if (DataZip){ delete[] DataZip; }
			return iResult;
		}
		//SMS4--
		unsigned int SizeSMS4 = SizeZip;
		unsigned char* DataSMS4 = new unsigned char[SizeSMS4];
		m_SMS4Func(DataZip, DataSMS4, SizeSMS4, m_strPassword);
		if (DataZip){ delete[] DataZip; }
		//Write
		FSDst.write((char*)&m_uiKey, 4);
		FSDst.write((char*)&SizeSMS4, 4);
		FSDst.write((char*)&DataSMS4[0], SizeSMS4);
		FSDst.write((char*)&SizeZip, 4);
		FSDst.write((char*)&SizeSrc, 4);
		FSDst.write((char*)&CRC32Src, 4);
		if (DataSMS4){ delete[] DataSMS4; }
		return 0;
	}
	int ZipFile(std::wstring Src, std::wstring Dst)
	{
		std::ifstream FSSrc(ws2s(Src).c_str(), std::ios_base::binary);
		if (!FSSrc.is_open())
		{
			return -1;
		}
		std::ofstream FSDst(ws2s(Dst).c_str(), std::ios_base::binary);
		if (!FSDst.is_open())
		{
			FSSrc.close();
			return -2;
		}
		FSSrc.seekg(0, std::ios_base::beg);
		FSDst.seekp(0, std::ios_base::beg);
		int iResult = ZipStream(FSSrc, FSDst);
		FSDst.close();
		FSSrc.close();
		return iResult;
	}
	int UnZipStream(std::ifstream& FSSrc, std::ofstream& FSDst, unsigned int& SizeDst, unsigned int& CRC32Dst)
	{
		FSSrc.seekg(0, std::ios_base::beg);
		FSDst.seekp(0, std::ios_base::beg);
		unsigned int ui = 0;
		FSSrc.read((char*)&ui, 4);
		if (ui != m_uiKey)
		{
			return -1;
		}
		//SMS4--
		unsigned int SizeSMS4 = 0;
		FSSrc.read((char*)&SizeSMS4, 4);
		unsigned char* DataSMS4 = new unsigned char[SizeSMS4];
		FSSrc.read((char*)&DataSMS4[0], SizeSMS4);
		//Zip--
		unsigned int SizeZip = 0;
		FSSrc.read((char*)&SizeZip, 4);
		unsigned char* DataZip = new unsigned char[SizeZip];
		m_DeSMS4Func(DataSMS4, DataZip, SizeZip, m_strPassword);
		if (DataSMS4){ delete[] DataSMS4; }
		//Dst
		FSSrc.read((char*)&SizeDst, 4);
		FSSrc.read((char*)&CRC32Dst, 4);
		unsigned char* DataDst = new unsigned char[SizeDst];
		int iResult = m_UnZipFunc(DataDst, &SizeDst, DataZip, SizeZip);
		if (DataZip){ delete[] DataZip; }
		if (iResult != 0)
		{
			if (DataDst){ delete[] DataDst; }
			return -1;
		}
		unsigned int CRC32Cur = m_CRC32Func(0, DataDst, SizeDst);
		if (CRC32Cur != CRC32Dst)
		{
			if (DataDst){ delete[] DataDst; }
			return -2;
		}
		FSDst.write((char*)&DataDst[0], SizeDst);
		if (DataDst){ delete[] DataDst; }
		return 0;
	}
	int UnZipFile(std::wstring Src, std::wstring Dst, unsigned int& SizeDst, unsigned int& CRC32Dst)
	{
		std::ifstream FSSrc(ws2s(Src).c_str(), std::ios_base::binary);
		if (!FSSrc.is_open())
		{
			return -1;
		}
		std::ofstream FSDst(ws2s(Dst).c_str(), std::ios_base::binary);
		if (!FSDst.is_open())
		{
			FSSrc.close();
			return -2;
		}
		FSSrc.seekg(0, std::ios_base::beg);
		FSDst.seekp(0, std::ios_base::beg);
		int iResult = UnZipStream(FSSrc, FSDst, SizeDst, CRC32Dst);
		FSDst.close();
		FSSrc.close();
		return iResult;
	}
	int UnZipFile(std::wstring Src, std::wstring Dst)
	{
		unsigned int SizeDst;
		unsigned int CRC32Dst;
		return UnZipFile(Src, Dst, SizeDst, CRC32Dst);
	}
	
};

#endif //LJFP_ZipFile_H