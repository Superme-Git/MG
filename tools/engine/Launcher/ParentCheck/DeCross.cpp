#include "stdafx.h"
#include <windows.h>
#include <TCHAR.H>
#include "Cross.h"

//���ܺ���
//bool _DecryptionMem(PVOID a,CHECK_LOADER *b)
//��һ�����������ڴ��ַ���ڶ���������һ���ṹ�壬��ѽ���������д������ṹ�塣




BOOL WINAPI _DecryptionMem(PVOID pBuff,CHECK_LOADER *pCheck)
{
    DWORD dwMagic1,dwMagic2,dwMagic1Con;
	DWORD dwRandom;
	DWORD dwLastDword;
	BYTE bCheckSum;
	BYTE pContext[17];
	BYTE pRandom[4];

	
    dwMagic1 = 0x4d5a4d57;
	dwMagic2 = 0x5a4d584c;
	
	memcpy(pContext,pBuff,17);
	memcpy(&dwLastDword,pContext+13,4);
    dwRandom = dwLastDword ^ dwMagic2;
	memcpy(pRandom,&dwRandom,4);
	bCheckSum = pContext[12];
    if(bCheckSum != CheckSum(pContext,12))//checksum error
        return FALSE;
    //check sum ok, now loop and xor again, from tail to head
	for(int i = 8;i>=0;i--)
	{
		pContext[i+3] ^= pRandom[0];
		pContext[i+2] ^= pRandom[1];
		pContext[i+1] ^= pRandom[2];
		pContext[i] ^= pRandom[3];
	}
	memcpy(&dwMagic1Con,pContext+8,4);
	if(dwMagic1Con != dwMagic1)//magic1���Գ�
		return FALSE;
	memcpy(pCheck,pContext,8);
	return TRUE;
}