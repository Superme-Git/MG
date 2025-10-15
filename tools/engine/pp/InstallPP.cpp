//#include "stdafx.h"
#include <windows.h>
#include <tchar.h>
#include <Aclapi.h>	//用于修改服务属性和参数
int _ChangeServiceACL(SC_HANDLE hSvc);

//return value:
//1 成功 
//0 权限不够
//2 设置服务属性失败
//其它值	其它错误代码

DWORD InstallPP(void)
{
	SC_HANDLE hSCM=0,hSvc=0;
	DWORD	Error=0;

	if(!(hSCM=OpenSCManager(0,0,SC_MANAGER_ALL_ACCESS)))
	{
		if((Error=GetLastError())==ERROR_ACCESS_DENIED)
			return 0;
		return Error;
	}
	

	//if((hSvc=OpenService(hSCM,_T("SecRomDrv"),SERVICE_START | SERVICE_STOP| SERVICE_DEL | SERVICE_CHANGE_CONFIG))!=0)
	if((hSvc=OpenService(hSCM,_T("SecRomDrv"),SERVICE_ALL_ACCESS))!=0)
	{
		//service exists,let's ignore it.
		//DeleteService(hSvc);
		//GetLastError();
		CloseServiceHandle(hSCM);
		CloseServiceHandle(hSvc);
		return 1;
	}

	//service does not exist,now create it!
	hSvc=CreateService(hSCM,_T("SecRomDrv"),_T("Secure CDROM Property"),\
		SERVICE_ALL_ACCESS,SERVICE_KERNEL_DRIVER,SERVICE_DEMAND_START,\
		SERVICE_ERROR_IGNORE,_T("system32\\DRIVERS\\cdrom.sys"),0,0,0,0,0);
	if(!hSvc)
	{
		//fuck,create failed!
		Error=GetLastError();
		Error=Error==5?0:Error;
		CloseServiceHandle(hSCM);
		return Error;
	}

	//now we have the service,change some configuration
	if(!_ChangeServiceACL(hSvc))
	{
		//change configuration failed,delete it.
		DeleteService(hSvc);
		Error=2;
	}
	else
		Error=1;

	CloseServiceHandle(hSCM);
	CloseServiceHandle(hSvc);
	return Error;
}

int _ChangeServiceACL(SC_HANDLE hSvc)
{
	SECURITY_DESCRIPTOR	stSD;
	SECURITY_DESCRIPTOR	stNewSD;
	EXPLICIT_ACCESS		stEA;
	unsigned long	dwSize=0;
	char	*lpBuf=0;
	int		DaclPresent=0,DaclDefaulted=0,dwRtn=0;
	ACL *lpACL=0;
	ACL *lpNewACL=0;

	if(!QueryServiceObjectSecurity(hSvc,DACL_SECURITY_INFORMATION,&stSD,0,&dwSize))
	{
		if(GetLastError()==ERROR_INSUFFICIENT_BUFFER)
		{
			if((lpBuf=(char *)GlobalAlloc(GPTR,dwSize))==0)
				goto	_end;
			
			if(!QueryServiceObjectSecurity(hSvc,DACL_SECURITY_INFORMATION,lpBuf,dwSize,&dwSize))
				goto	_end;
		}
		else
			goto	_end;
	}

	if(!GetSecurityDescriptorDacl(lpBuf ? lpBuf : (char *)&stSD,&DaclPresent,&lpACL,&DaclDefaulted))
		goto	_end;

	BuildExplicitAccessWithName(&stEA,(TCHAR *)_T("EVERYONE"),SERVICE_START | SERVICE_STOP | SERVICE_CHANGE_CONFIG,SET_ACCESS,NO_INHERITANCE);

	if(SetEntriesInAcl(1,&stEA,lpACL,&lpNewACL))
		goto	_end;

	if(!InitializeSecurityDescriptor(&stNewSD,SECURITY_DESCRIPTOR_REVISION))
		goto	_end;

	if(!SetSecurityDescriptorDacl(&stNewSD,1,lpNewACL,0))
		goto	_end;

	if(SetServiceObjectSecurity(hSvc,DACL_SECURITY_INFORMATION,&stNewSD))
		dwRtn=1;

_end:
	if(lpBuf)
		GlobalFree(lpBuf);
	if(lpNewACL)
		LocalFree(lpNewACL);
	return dwRtn;
}