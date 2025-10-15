//#include "stdafx.h"
#include <windows.h>
#include <tchar.h>
#include <winioctl.h>	//用于i/o IRP
#include <Aclapi.h>		//用于修改服务属性和参数

#include "native.h"
#include "PerfectProtector.h"
#include "VMProtectSDK.h"


#pragma comment(lib,"shlwapi.lib")
#pragma comment(lib,"ntdll.lib")
#pragma comment(lib,"kernel32.lib")


#define	IOCTL_INT22				CTL_CODE(FILE_DEVICE_UNKNOWN,0x800, METHOD_BUFFERED, FILE_READ_ACCESS | FILE_WRITE_ACCESS)
#define	IOCTL_GACDSERVICE		CTL_CODE(FILE_DEVICE_UNKNOWN,0x801, METHOD_BUFFERED, FILE_READ_ACCESS | FILE_WRITE_ACCESS)


#pragma optimize("",off)
	typedef   void   (CALLBACK*   _GetNativeSystemInfo)(LPSYSTEM_INFO);
	bool Is64BitSystem()
	{
		SYSTEM_INFO stSI={0};
		HMODULE hDll=0;
		TCHAR szBuf[512]={0};

		VMProtectBeginVirtualization
		_GetNativeSystemInfo lpGetNativeSystemInfo=0;
		hDll=GetModuleHandle(_T("kernel32.dll"));
		if(hDll==0)
		{
			//Protector::_ErrorRet(175);	//Is64BitSystem:GetModuleHandle failed
			return false;
		}

		lpGetNativeSystemInfo=(_GetNativeSystemInfo)GetProcAddress(hDll,(LPCSTR)"GetNativeSystemInfo");
		if(lpGetNativeSystemInfo==0)
		{
			//Protector::_ErrorRet(176);	//Is64BitSystem:GetProcAddress failed
			return false;
		}

		lpGetNativeSystemInfo(&stSI);

		if(stSI.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_AMD64 || stSI.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_IA64)
			return true;

		return false;
		VMProtectEnd
	}

	




	//用于和内核通信
	DWORD DoIntCall_x64_Internal(DWORD* lpBuf,DWORD dwSize,DWORD *lpOutBuf,DWORD dwOutBufSize)
	{
		DWORD* lpBufOut=0;
		DWORD dwVar=0;
		DWORD dwKey=0;
		DWORD dwKey1=0;
		DWORD dwStatus=0;
		DWORD dwSizeRtn=0;
		DWORD szBuf[128]={0};
		DWORD szBufOut[128]={0};
		DWORD dwLastError=0;
		VMProtectBeginVirtualization

		//先对buf加密
		lpBufOut=(DWORD*)GlobalLock(GlobalAlloc(GPTR,dwSize*2+0x10));
		if(lpBufOut==0)
		{
			dwLastError=GetLastError();
			Protector::_Log(177,2,dwSize*2+0x10,dwLastError);	//DoIntCall_x64_Internal:GlobalAlloc %d failed %d
			return -10;
		}
		
		//dwVar=tid ^ (pid<<8)
		dwVar=GetCurrentThreadId() ^ (GetCurrentProcessId()<<8);

		//dwKey
		Common_GetRandom();
		__asm mov dwKey,eax;

		//encrypt
		__asm {
			pushad
			pushfd
			mov esi,lpBuf;
			mov edi,lpBufOut;
			mov ecx,dwSize;
			mov ebx,dwVar;
			mov edx,dwKey;
		}
		Common_Encrypt(a);
		__asm {
			mov dwKey1,edx;
			popfd
			popad
		}

		//;加密后数据格式
		//;len|checksum|header|data|

#ifdef Print_Encryption
		DbgPrint("encryption:size %x,var %08x,key %08x,key1 %08x\n",dwSize,dwVar,dwKey,dwKey1);
		unsigned char* lpPrint=(unsigned char*)lpBufOut;
		DbgPrint("len %02x checksum %02x header %08x\n",lpPrint[0],lpPrint[1],(DWORD)(DWORD &)lpPrint[2]);
		for(unsigned int i=0;i<dwSize;i++)
		{
			DbgPrint("%02x ",lpPrint[6+i]);
		}
		DbgPrint("\n");
#endif

		//set data
		szBuf[0]='iylf';
		szBuf[1]='ikgn';
		szBuf[2]='ress';
		szBuf[3]=(dwKey1-dwVar)^Common_Encryption_Header;
		szBuf[4]=((DWORD)(DWORD &)lpBufOut-dwKey1)^Common_Encryption_Header;

#ifdef dp
		DbgPrint("DoIntCall_x64_Internal:cmd %d\n",lpBuf[0]);
#endif
		
		dwStatus=DeviceIoControl(Protector::hDevice,IOCTL_INT22,szBuf,sizeof(szBuf),szBuf,sizeof(szBuf),&dwSizeRtn,0);
		if(dwStatus==0)
		{
			Protector::_ErrorRet(178);		//DoIntCall_x64_Internal:DeviceIoControl failed
			GlobalFree(lpBufOut);
			return -11;
		}
		
		//szBuf结构
		//dwRtn^ret_magic_number|dwKey1  (ret_magic_number=dwVar^key1)
		//或者
		//(-1|-2|-3|-4)
		if(szBuf[0]==-1 || szBuf[0]==-2 || szBuf[0]==-3 || szBuf[0]==-4)
		{
			Protector::_Log(179,1,szBuf[0]);	//DoIntCall_x64_Internal:get return value %x
			GlobalFree(lpBufOut);
			return szBuf[0];
		}


		//lpBufOut经过了加密，解密到szBufOut
		dwKey=szBuf[1];
		__asm {
			mov ecx,dwKey;	//dwKey1
			mov edx,dwVar;
			mov esi,lpBufOut;
			lea	edi,szBufOut;
		}
		Common_Decrypt(a);

		//解密后结构是
		//dwRtn

		GlobalFree(lpBufOut);
		
		if(dwOutBufSize==0)
		{
			//只返回一个值
			if(szBufOut[0]!=szBuf[0])
			{
				Protector::_Log(180,2,szBufOut[0],szBuf[0]);	//DoIntCall_x64_Internal:get return value %x!=%x
				return -12;
			}

			return szBufOut[0]^dwVar^dwKey1;
		}
		else
		{
			//返回一串数，如网络调用
			RtlMoveMemory(lpOutBuf,szBufOut,dwOutBufSize);
			return 0;
		}
		VMProtectEnd
	}


	

	

	//gacd接口的64位实现
	//返回值 
	//0		调用成功
	//-1	DeviceIoControl调用失败

	//lpBuf的返回值
	//;-1		调用来源非法
	//;-2		服务ID不支持
	//其他值	内部功能函数的返回值
	DWORD GACD_Service_x64(DWORD dwSid,DWORD *lpBuf,DWORD dwSize,DWORD dwKey)
	{
		DWORD dwSizeRtn=0;
		DWORD szBuf[128]={0};
		DWORD dwStatus=0;
		VMProtectBeginVirtualization
		/*
		;参数
		;eax	服务号^key-3389
		;ecx	buf大小^key-3389
		;esi	buf地址^key-3389
		;edx	key^GACD_Service_MagicWord
		;ebx	'flyi'
		;edi	'ngki'
		*/
		//set data
		szBuf[0]=(dwSid^dwKey)-3389;
		szBuf[1]=(dwSize^dwKey)-3389;
		szBuf[2]=((DWORD)(DWORD &)lpBuf^dwKey)-3389;
		szBuf[3]=dwKey^GACD_Service_MagicWord;
		szBuf[4]='iylf';
		szBuf[5]='ikgn';
	
		dwStatus=DeviceIoControl(Protector::hDevice,IOCTL_GACDSERVICE,szBuf,sizeof(szBuf),szBuf,sizeof(szBuf),&dwSizeRtn,0);
		if(dwStatus==0)
		{
			Protector::_ErrorRet(178);		//GACD_Service_x64:DeviceIoControl failed
			return -1;
		}
		VMProtectEnd
		return 0;
	}


	DWORD DoIntCall_x64(DWORD dwCmd)
	{
		DWORD szBuf[]={0};
		szBuf[0]=dwCmd;
		return DoIntCall_x64_Internal(szBuf,4,0,0);
	}

	DWORD DoIntCallValue_x64(DWORD dwCmd,DWORD dwValue1)
	{
		DWORD szBuf[2]={0};
		szBuf[0]=dwCmd;
		szBuf[1]=dwValue1;
		return DoIntCall_x64_Internal(szBuf,sizeof(szBuf),0,0);
	}

	DWORD DoIntCall3Value_x64(DWORD dwCmd,DWORD dwValue1,DWORD dwValue2,DWORD dwValue3)
	{
		DWORD szBuf[4]={0};
		szBuf[0]=dwCmd;
		szBuf[1]=dwValue1;
		szBuf[2]=dwValue2;
		szBuf[3]=dwValue3;
		return DoIntCall_x64_Internal(szBuf,sizeof(szBuf),0,0);
	}

	DWORD DoIntCall_Net_x64(DWORD *lpOutBuf,DWORD dwOutBufSize)
	{
		DWORD szBuf=2;
		return DoIntCall_x64_Internal(&szBuf,sizeof(szBuf),lpOutBuf,dwOutBufSize);
	}

	DWORD _CheckAlive_Client_x64(DWORD dwServerKey,DWORD dwRoleId,unsigned char *lpOutBuf)
	{
		DWORD szBuf[PP_NETCHECK_DATA_SIZE]={0};
		DWORD dwVar=0;
		DWORD dwKey=0;
		VMProtectBeginVirtualization
		szBuf[5]=GetTickCount();
		szBuf[4]=GetTickCount();

		//;----------------
		//;取得驱动版本及其他较验信息
		//;----------------
		DoIntCall_Net_x64(&szBuf[1],12);

		//;----------------
		//;取时间戳
		//;----------------
		szBuf[0]=GetTickCount();

		//;----------------
		//;加密数据
		//;----------------

		//;---------------------
		//;取一个随机数作为dwVar
		//;---------------------
		Common_GetRandom()
		__asm mov dwVar,eax;

		dwKey=(dwServerKey^dwRoleId)-dwVar;

		//encrypt
		__asm {
			lea esi,szBuf;
			mov edi,lpOutBuf;
			mov ecx,PP_NETCHECK_DATA_SIZE;
			mov ebx,dwVar;
			mov edx,dwKey;
		}
		Common_Encrypt(a);
		//;这时，数据格式是：head|len|data|checksum
		//;header=Common_Encryption_Header^dwKey1^dwVar
		//;其中,dwKey1是dwKey循环左移若干位得到，这里由于加密16个字节，最后dwKey1等于是dwKey循环左移8位

		//;clear plaint data
		RtlZeroMemory(szBuf,sizeof(szBuf));

		//;save dwVar
		(DWORD &)(lpOutBuf[PP_NETCHECK_DATA_SIZE+6])=dwVar^0x20071224;
		VMProtectEnd
		//;这时，数据格式是：head|len|data|checksum|dwVar
		return 0;
	}

#pragma optimize("",  on)


