#pragma once

//下面2个开关要关闭！！
//#define  dp
//#define  Print_Encryption

//以下开关都要打开！！
#include "PerfectProtector-x64.h"

//#define protector_ParentCheck

#define	LoadFromLoader
#define  Windows7_Surport
#define	X64_Surport


#define DISABLE_BREAK_IN
#define	DISABLE_MSG_HOOK

#define		Enbale_PP

#define		Enable_Driver
#define		PP_InsertPid
#define		PP_CallGate
#define		PP_IntCallCheck


#define    Enable_TestThread

namespace Protector{
	int StartProtector(TCHAR* CurrentDir);
	void StopProtector(void);
	void _Log(...);
	int	_ErrorRet(DWORD dwID);
	extern WORD GACD_CallGate_Selector[];
	extern DWORD My_GetTickCount();
	extern HANDLE hDevice;
	extern PVOID lpDrvIdentificationMem;
	extern bool	bIsX64;
}


#pragma warning(disable:4748)	//this warning is about /GS
#pragma warning(disable:4996)

//#define Common_Encryption_Header 0x8d227f93
#define Common_Encryption_Header 0x3f193a51


#define	macro_call_GetSystemTimeAsFileTime()	__asm	call	dword ptr[GetSystemTimeAsFileTime]
#define	macro_call_GetTickCount()				__asm	call	dword ptr[GetTickCount]
#define	macro_call_GetCurrentProcessId()		__asm	call	dword ptr[GetCurrentProcessId]
#define	macro_call_GetCurrentThreadId()			__asm	call	dword ptr[GetCurrentThreadId]
#define macro_call_DbgPrint()					__asm	call	dword ptr[DbgPrint]
#define	macro_call_GetThreadTimes()				__asm	call	dword ptr[GetThreadTimes]

#define PP_NETCHECK_DATA_SIZE 64
/*
#define Common_GetRandom_old() \
	__asm 	push	esi \
	__asm 	push	ecx \
	__asm	push	edx	\
	__asm 	push	0 \
	__asm 	push	0 \
	__asm 	push	esp \
	macro_call_GetSystemTimeAsFileTime() \
	__asm 	xor	esi,dword ptr [esp] \
	__asm 	xor	esi,dword ptr [esp+4] \
	__asm 	add	esp,8 \
	macro_call_GetTickCount()\
	__asm 	xor	esi,eax \
	__asm 	xor	esi,0x1f3d9021 \
	__asm 	mov	edx,esi	 \
	__asm	pop	edx\
	__asm 	pop	ecx \
	__asm 	pop	esi
*/

#define Common_GetRandom() \
	__asm 	push	ebx \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	0 \
	__asm 	push	0 \
	__asm 	mov	eax,esp \
	__asm   push	0 \
	__asm   push	0 \
	__asm   push	0 \
	__asm	push	eax \
	__asm	push	-1 \
	macro_call_GetThreadTimes() \
	__asm	xor	ebx,ebx \
	__asm 	mov	ebx,dword ptr [esp] \
	__asm 	xor	ebx,dword ptr [esp+4] \
	__asm 	add	esp,8 \
	macro_call_GetTickCount()\
	__asm 	xor	ebx,eax \
	__asm 	xor	ebx,0394ab39ch \
	__asm 	mov	eax,ebx \
	__asm 	pop	edx \
	__asm 	pop	ecx \
	__asm 	pop	ebx

#define Common_Encrypt(l) \
	__asm 	push	ecx \
	__asm 	push	edi \
	__asm 	add	edi,6	 \
	__asm 	cld \
	__asm _Common_Encrypt_Continue##l: \
	__asm 	test	ecx,ecx \
	__asm 	jz	_Common_Encrypt_End##l \
	__asm 	lodsb \
	__asm 	dec	ecx \
	__asm 	xor	al,dl \
	__asm 	ror	edx,8 \
	__asm 	cmp	ecx,3 \
	__asm 	jl	_Common_Encrypt_1##l \
	__asm 	xor	al,byte ptr [esi+2] \
	__asm 	jmp	_Common_Encrypt_4##l \
	__asm _Common_Encrypt_1##l: \
	__asm 	cmp	ecx,2 \
	__asm 	jnz	_Common_Encrypt_2##l \
	__asm 	xor	al,49h \
	__asm 	jmp	_Common_Encrypt_4##l \
	__asm _Common_Encrypt_2##l: \
	__asm 	cmp	ecx,1 \
	__asm 	jnz	_Common_Encrypt_3##l \
	__asm 	xor	al,73h \
	__asm 	jmp	_Common_Encrypt_4##l \
	__asm _Common_Encrypt_3##l: \
	__asm 	test	ecx,ecx \
	__asm 	jnz	_Common_Encrypt_4##l \
	__asm 	xor	al,0dch \
	__asm _Common_Encrypt_4##l: \
	__asm 	stosb \
	__asm 	jmp	_Common_Encrypt_Continue##l	 \
	__asm _Common_Encrypt_End##l: \
	__asm 	rol	edx,8	 \
	__asm 	pop	edi \
	__asm 	pop	ecx \
	__asm 	mov	eax,Common_Encryption_Header \
	__asm 	push	edx \
	__asm 	shr	edx,1 \
	__asm 	xor	eax,edx	 \
	__asm 	pop	edx \
	__asm 	push	ebx \
	__asm 	shl	ebx,3 \
	__asm 	xor	eax,ebx	 \
	__asm 	pop	ebx \
	__asm 	mov	dword ptr [edi+2],eax		 \
	__asm 	mov	eax,ecx	 \
	__asm 	xor	al,dl \
	__asm 	xor	al,bl \
	__asm 	sub	al,4 \
	__asm 	mov	byte ptr [edi],al		 \
	__asm 	mov	byte ptr [edi+1],0 \
	__asm 	mov	dword ptr [edi+ecx+6],edx	 \
	__asm 	push	ecx \
	__asm 	mov	esi,edi	 \
	__asm 	cld \
	__asm 	xor	eax,eax \
	__asm 	xor	edx,edx \
	__asm 	add	ecx,10	 \
	__asm _Common_Enrypt_again##l: \
	__asm 	lodsb \
	__asm 	rol	al,cl	 \
	__asm 	xor	dl,al	 \
	__asm 	loop	_Common_Enrypt_again##l \
	__asm 	pop	ecx \
	__asm 	mov	al,dl	 \
	__asm 	mov	edx,dword ptr [edi+ecx+6]	 \
	__asm 	mov	dword ptr [edi+ecx+6],0	 \
	__asm 	mov	byte ptr [edi+1],al	 \
	__asm 	xor	eax,eax


#define Common_Decrypt(l) \
__asm 	push	ebx \
__asm 	push	ecx \
__asm 	push	edx \
__asm 	push	esi \
__asm 	push	edi \
__asm 	pushfd \
__asm 	mov	eax,edx				 \
__asm 	shl	eax,3 \
__asm 	xor	eax,dword ptr [esi+2]		 \
__asm 	push	ecx \
__asm 	shr	ecx,1 \
__asm 	xor	eax,ecx				 \
__asm 	pop	ecx \
__asm 	xor	eax,Common_Encryption_Header	 \
__asm 	mov	ebx,eax \
__asm 	xor	eax,eax \
__asm 	test	ebx,ebx \
__asm 	jnz	_Common_Decrypt_end##l		 \
__asm 	cld \
__asm 	mov	al,byte ptr [esi] \
__asm 	add	al,4 \
__asm 	xor	al,cl \
__asm 	xor	al,dl \
__asm 	test	al,al \
__asm 	jz	_Common_Decrypt_end##l \
__asm 	push	ecx \
__asm 	movzx	ecx,al		 \
__asm 	pop	ebx		 \
__asm 	push	edx \
__asm 	push	ecx \
__asm 	push	esi \
__asm 	xor	edx,edx \
__asm 	mov	dl,byte ptr [esi+1]		 \
__asm 	mov	byte ptr [esi+1],0		 \
__asm 	mov	dword ptr [esi+6+ecx],ebx	 \
__asm 	add	ecx,10				 \
__asm 	cld \
__asm 	xor	eax,eax \
__asm _Common_Decrypt_again##l: \
__asm 	lodsb \
__asm 	rol	al,cl \
__asm 	xor	dl,al \
__asm 	loop	_Common_Decrypt_again##l \
__asm 	pop	esi \
__asm 	pop	ecx \
__asm 	xor	eax,eax \
__asm 	mov	dword ptr [esi+6+ecx],eax	 \
__asm 	test	dl,dl \
__asm 	pop	edx \
__asm 	jnz	_Common_Decrypt_end##l \
__asm 	add	edi,ecx \
__asm 	dec	edi \
__asm 	lea	esi,[esi+ecx+6] \
__asm 	dec	esi		 \
__asm 	xor	edx,edx \
__asm _Common_Decrypt_Continue##l: \
__asm 	xor	eax,eax \
__asm 	inc	eax \
__asm 	test	ecx,ecx \
__asm 	jz	_Common_Decrypt_end##l \
__asm 	std \
__asm 	lodsb \
__asm 	inc	edx \
__asm 	xor	al,bl \
__asm 	rol	ebx,8 \
__asm 	cmp	edx,1 \
__asm 	jnz	_Common_Decrypt_1##l \
__asm 	xor	al,0dch \
__asm 	jmp	_Common_Decrypt_4##l \
__asm _Common_Decrypt_1##l: \
__asm 	cmp	edx,2 \
__asm 	jnz	_Common_Decrypt_2##l \
__asm 	xor	al,73h \
__asm 	jmp	_Common_Decrypt_4##l \
__asm _Common_Decrypt_2##l: \
__asm 	cmp	edx,3 \
__asm 	jnz	_Common_Decrypt_3##l \
__asm 	xor	al,49h \
__asm 	jmp	_Common_Decrypt_4##l \
__asm _Common_Decrypt_3##l: \
__asm 	xor	al,byte ptr [edi+3] \
__asm _Common_Decrypt_4##l: \
__asm 	stosb \
__asm 	dec	ecx \
__asm 	jmp	_Common_Decrypt_Continue##l \
__asm _Common_Decrypt_end##l: \
__asm 	popfd \
__asm 	pop	edi \
__asm 	pop	esi \
__asm 	pop	edx \
__asm 	pop	ecx \
__asm 	pop	ebx






#define DoIntCall_x32(CommandId,l,ErrorLabel) \
	__asm 	push	esi \
	__asm 	push	edi \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	ebx \
	__asm 	pushfd \
	__asm 	mov	ebx,esp \
	__asm 	sub	esp,19*4		 \
	__asm 	lea	edi,[ebx-4ch]		 \
	__asm 	xor	eax,eax \
	__asm 	mov	ecx,19 \
	__asm 	rep	stosd \
	macro_call_GetCurrentThreadId() \
	__asm 	push	eax \
	macro_call_GetCurrentProcessId() \
	__asm 	pop	ecx \
	__asm 	shl	ecx,8 \
	__asm 	xor	eax,ecx \
	__asm 	mov	dword ptr [ebx-8],eax	\
	__asm 	mov	dword ptr [ebx-2ch],CommandId \
	__asm 	lea	esi,[ebx-2ch]		 \
	__asm 	lea	edi,[ebx-4ch]		 \
	__asm	mov	ecx,4 \
	__asm 	push	ebx \
	__asm 	mov	ebx,eax	\
	__asm	Common_GetRandom() \
	__asm 	mov	edx,eax \
	__asm	Common_Encrypt(l) \
	__asm 	pop	ebx \
	__asm 	mov	dword ptr [ebx-2ch],0	 \
	__asm 	push	edx \
	__asm	push	ebx \
	__asm 	mov	esi,edx				 \
	__asm 	sub	esi,dword ptr [ebx-8]		 \
	__asm 	xor	esi,Common_Encryption_Header	 \
	__asm 	lea	edi,[ebx-4ch] \
	__asm 	sub	edi,edx				 \
	__asm 	xor	edi,Common_Encryption_Header	 \
	__asm 	mov	ebx,'iylf'			 \
	__asm 	mov	ecx,'ikgn' \
	__asm 	mov	edx,'ress' \
	__asm   push fs \
	__asm 	int	22h \
	__asm   pop fs \
	__asm	mov	ecx,edx \
	__asm	pop	ebx \
	__asm 	pop	edx \
	__asm 	cmp	eax,-1 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-2 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-3 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-4 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	xor	eax,edx			 \
	__asm 	xor	eax,dword ptr [ebx-8]	 \
	__asm 	test	eax,eax \
	__asm 	jz	_DoIntCall_end##l	 \
	__asm 	push	edx \
	__asm 	mov	edx,dword ptr [ebx-8]	 \
	__asm 	lea	edi,[ebx-0ch]	 \
	__asm 	lea	esi,[ebx-4ch]	 \
	__asm	Common_Decrypt(l) \
	__asm 	pop	edx \
	__asm 	test	eax,eax \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	mov	eax,dword ptr [ebx-0ch]	 \
	__asm 	xor	eax,edx \
	__asm 	xor	eax,dword ptr [ebx-8]	 \
	__asm 	mov	dword ptr [ebx-8],0 \
	__asm 	jmp	_DoIntCall_end##l \
	__asm _DoIntCall_Error##l: \
	__asm 	xor	eax,eax		 \
	__asm _DoIntCall_end##l: \
	__asm 	mov	esp,ebx \
	__asm 	popfd \
	__asm 	pop	ebx \
	__asm 	pop	edx \
	__asm 	pop	ecx \
	__asm 	pop	edi \
	__asm 	pop	esi	\
	__asm   test eax,eax \
	__asm   jz ErrorLabel

#define DoIntCallValue_x32(CommandId,l,ErrorLabel,Value) \
	__asm 	push	esi \
	__asm 	push	edi \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	ebx \
	__asm 	pushfd \
	__asm 	mov	ebx,esp \
	__asm 	sub	esp,19*4		 \
	__asm 	lea	edi,[ebx-4ch]		 \
	__asm 	xor	eax,eax \
	__asm 	mov	ecx,19 \
	__asm 	rep	stosd \
	macro_call_GetCurrentThreadId() \
	__asm 	push	eax \
	macro_call_GetCurrentProcessId() \
	__asm 	pop	ecx \
	__asm 	shl	ecx,8 \
	__asm 	xor	eax,ecx \
	__asm 	mov	dword ptr [ebx-8],eax	\
	__asm 	mov	dword ptr [ebx-2ch],CommandId \
	__asm   push eax \
	__asm   mov eax,Value \
	__asm   mov dword ptr [ebx-28h],eax \
	__asm   pop eax \
	__asm 	lea	esi,[ebx-2ch]		 \
	__asm 	lea	edi,[ebx-4ch]		 \
	__asm 	mov	ecx,8			 \
	__asm 	push	ebx \
	__asm 	mov	ebx,eax	\
	__asm	Common_GetRandom() \
	__asm 	mov	edx,eax \
	__asm	Common_Encrypt(l) \
	__asm 	pop	ebx \
	__asm 	mov	dword ptr [ebx-2ch],0	 \
	__asm 	mov	dword ptr [ebx-28h],0	 \
	__asm 	push	edx \
	__asm	push	ebx \
	__asm 	mov	esi,edx				 \
	__asm 	sub	esi,dword ptr [ebx-8]		 \
	__asm 	xor	esi,Common_Encryption_Header	 \
	__asm 	lea	edi,[ebx-4ch] \
	__asm 	sub	edi,edx				 \
	__asm 	xor	edi,Common_Encryption_Header	 \
	__asm 	mov	ebx,'iylf'			 \
	__asm 	mov	ecx,'ikgn' \
	__asm 	mov	edx,'ress' \
	__asm   push fs \
	__asm 	int	22h \
	__asm   pop fs \
	__asm	mov	ecx,edx \
	__asm	pop	ebx \
	__asm 	pop	edx \
	__asm 	cmp	eax,-1 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-2 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-3 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-4 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	xor	eax,edx			 \
	__asm 	xor	eax,dword ptr [ebx-8]	 \
	__asm 	test	eax,eax \
	__asm 	jz	_DoIntCall_end##l	 \
	__asm 	push	edx \
	__asm 	mov	edx,dword ptr [ebx-8]	 \
	__asm 	lea	edi,[ebx-0ch]	 \
	__asm 	lea	esi,[ebx-4ch]	 \
	__asm	Common_Decrypt(l) \
	__asm 	pop	edx \
	__asm 	test	eax,eax \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	mov	eax,dword ptr [ebx-0ch]	 \
	__asm 	xor	eax,edx \
	__asm 	xor	eax,dword ptr [ebx-8]	 \
	__asm 	mov	dword ptr [ebx-8],0 \
	__asm 	jmp	_DoIntCall_end##l \
	__asm _DoIntCall_Error##l: \
	__asm 	xor	eax,eax		 \
	__asm _DoIntCall_end##l: \
	__asm 	mov	esp,ebx \
	__asm 	popfd \
	__asm 	pop	ebx \
	__asm 	pop	edx \
	__asm 	pop	ecx \
	__asm 	pop	edi \
	__asm 	pop	esi	\
	__asm   test eax,eax \
	__asm   jz ErrorLabel

#define DoIntCall3Value_x32(CommandId,l,ErrorLabel,Value1,Value2,Value3) \
	__asm 	push	esi \
	__asm 	push	edi \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	ebx \
	__asm 	pushfd \
	__asm 	mov	ebx,esp \
	__asm 	sub	esp,19*4		 \
	__asm 	lea	edi,[ebx-4ch]		 \
	__asm 	xor	eax,eax \
	__asm 	mov	ecx,19 \
	__asm 	rep	stosd \
	macro_call_GetCurrentThreadId() \
	__asm 	push	eax \
	macro_call_GetCurrentProcessId() \
	__asm 	pop	ecx \
	__asm 	shl	ecx,8 \
	__asm 	xor	eax,ecx \
	__asm 	mov	dword ptr [ebx-8],eax	\
	__asm 	mov	dword ptr [ebx-2ch],CommandId \
	__asm   push eax \
	__asm   mov eax,Value1 \
	__asm   mov dword ptr [ebx-28h],eax \
	__asm   mov eax,Value2 \
	__asm   mov dword ptr [ebx-24h],eax \
	__asm   mov eax,Value3 \
	__asm   mov dword ptr [ebx-20h],eax \
	__asm   pop eax \
	__asm 	lea	esi,[ebx-2ch]		 \
	__asm 	lea	edi,[ebx-4ch]		 \
	__asm 	mov	ecx,16			 \
	__asm 	push	ebx \
	__asm 	mov	ebx,eax	\
	__asm	Common_GetRandom() \
	__asm 	mov	edx,eax \
	__asm	Common_Encrypt(l) \
	__asm 	pop	ebx \
	__asm 	mov	dword ptr [ebx-2ch],0	 \
	__asm 	mov	dword ptr [ebx-28h],0	 \
	__asm 	mov	dword ptr [ebx-24h],0	 \
	__asm 	mov	dword ptr [ebx-20h],0	 \
	__asm 	push	edx \
	__asm	push	ebx \
	__asm 	mov	esi,edx				 \
	__asm 	sub	esi,dword ptr [ebx-8]		 \
	__asm 	xor	esi,Common_Encryption_Header	 \
	__asm 	lea	edi,[ebx-4ch] \
	__asm 	sub	edi,edx				 \
	__asm 	xor	edi,Common_Encryption_Header	 \
	__asm 	mov	ebx,'iylf'			 \
	__asm 	mov	ecx,'ikgn' \
	__asm 	mov	edx,'ress' \
	__asm   push fs \
	__asm 	int	22h \
	__asm   pop fs \
	__asm	mov	ecx,edx \
	__asm	pop	ebx \
	__asm 	pop	edx \
	__asm 	cmp	eax,-1 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-2 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-3 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	cmp	eax,-4 \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	xor	eax,edx			 \
	__asm 	xor	eax,dword ptr [ebx-8]	 \
	__asm 	test	eax,eax \
	__asm 	jz	_DoIntCall_end##l	 \
	__asm 	push	edx \
	__asm 	mov	edx,dword ptr [ebx-8]	 \
	__asm 	lea	edi,[ebx-0ch]	 \
	__asm 	lea	esi,[ebx-4ch]	 \
	__asm	Common_Decrypt(l) \
	__asm 	pop	edx \
	__asm 	test	eax,eax \
	__asm 	jz	_DoIntCall_Error##l \
	__asm 	mov	eax,dword ptr [ebx-0ch]	 \
	__asm 	xor	eax,edx \
	__asm 	xor	eax,dword ptr [ebx-8]	 \
	__asm 	mov	dword ptr [ebx-8],0 \
	__asm 	jmp	_DoIntCall_end##l \
	__asm _DoIntCall_Error##l: \
	__asm 	xor	eax,eax		 \
	__asm _DoIntCall_end##l: \
	__asm 	mov	esp,ebx \
	__asm 	popfd \
	__asm 	pop	ebx \
	__asm 	pop	edx \
	__asm 	pop	ecx \
	__asm 	pop	edi \
	__asm 	pop	esi	\
	__asm   test eax,eax \
	__asm   jz ErrorLabel


#define  DoIntCall(CommandId,l,ErrorLabel)	\
	if(!Protector::bIsX64)									\
{ \
	DoIntCall_x32(CommandId,l,ErrorLabel)	\
} \
else										\
{	\
	DoIntCall_x64(CommandId); \
	__asm   test eax,eax \
	__asm   jz ErrorLabel \
	__asm   test eax,80000000h \
	__asm   jnz ErrorLabel \
}	

#define  DoIntCallValue(CommandId,l,ErrorLabel,Value)	\
if(!Protector::bIsX64)									\
{ \
	DoIntCallValue_x32(CommandId,l,ErrorLabel,Value)	\
} \
else										\
{	\
	DoIntCallValue_x64(CommandId,Value); \
	__asm   test eax,eax \
	__asm   jz ErrorLabel \
	__asm   test eax,80000000h \
	__asm   jnz ErrorLabel \
}	


#define  DoIntCall3Value(CommandId,l,ErrorLabel,Value1,Value2,Value3) \
if(!Protector::bIsX64)									\
{ \
	DoIntCall3Value_x32(CommandId,l,ErrorLabel,Value1,Value2,Value3)	\
} \
else										\
{	\
	DoIntCall3Value_x64(CommandId,Value1,Value2,(DWORD)(DWORD &)Value3); \
	__asm   test eax,eax \
	__asm   jz ErrorLabel \
	__asm   test eax,80000000h \
	__asm   jnz ErrorLabel \
}	

#define _CheckAlive_Client(dwServerKey,dwRoleId,lpOutBuf) \
if(!Protector::bIsX64)									\
{ \
	_CheckAlive_Client_x32(dwServerKey,dwRoleId,lpOutBuf)	\
} \
else										\
{	\
	_CheckAlive_Client_x64(dwServerKey,dwRoleId,(unsigned char*)lpOutBuf); \
}	




#define DoIntCall_Net(l) \
	__asm 	push	esi \
	__asm 	push	edi \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	ebx	 \
	__asm 	pushfd \
	__asm 	mov	ebx,esp \
	__asm 	sub	esp,19*4		 \
	__asm 	lea	edi,[ebx-4ch]		 \
	__asm 	xor	eax,eax \
	__asm 	mov	ecx,19 \
	__asm 	rep	stosd \
	__asm 	mov	dword ptr [ebx-0ch],esi	 \
	macro_call_GetCurrentThreadId() \
	__asm 	push	eax \
	macro_call_GetCurrentProcessId() \
	__asm 	pop	ecx \
	__asm 	shl	ecx,8 \
	__asm 	xor	eax,ecx \
	__asm 	mov	dword ptr [ebx-8],eax	 \
	__asm 	mov	dword ptr [ebx-2ch],2	 \
	__asm 	lea	esi,[ebx-2ch]		 \
	__asm 	lea	edi,[ebx-4ch]		 \
	__asm 	mov	ecx,4			 \
	__asm 	push	ebx \
	__asm 	mov	ebx,eax			 \
	__asm 	Common_GetRandom() \
	__asm 	mov	edx,eax \
	__asm 	Common_Encrypt(l) \
	__asm 	pop	ebx \
	__asm 	mov	dword ptr [ebx-2ch],0	 \
	__asm 	push	edx	 \
	__asm 	push	ebx	 \
	__asm 	mov	esi,edx				 \
	__asm 	sub	esi,dword ptr [ebx-8]		 \
	__asm 	xor	esi,Common_Encryption_Header	 \
	__asm 	lea	edi,[ebx-4ch] \
	__asm 	sub	edi,edx				 \
	__asm 	xor	edi,Common_Encryption_Header	 \
	__asm 	mov	ebx,'iylf'			 \
	__asm 	mov	ecx,'ikgn' \
	__asm 	mov	edx,'ress' \
	__asm   push fs \
	__asm   int 22h \
	__asm   pop fs \
	__asm 	mov	ecx,edx	 \
	__asm 	pop	ebx \
	__asm 	pop	edx \
	__asm 	mov	edx,dword ptr [ebx-8]	 \
	__asm 	lea	edi,[ebx-2ch]		 \
	__asm 	lea	esi,[ebx-4ch]		 \
	__asm 	Common_Decrypt(l) \
	__asm 	lea	esi,[ebx-2ch] \
	__asm 	mov	edi,[ebx-0ch] \
	__asm 	mov	ecx,12			 \
	__asm 	rep	movsb \
	__asm 	mov	esp,ebx \
	__asm 	popfd \
	__asm 	pop	ebx \
	__asm 	pop	edx \
	__asm 	pop	ecx \
	__asm 	pop	edi \
	__asm 	pop	esi


//;加密完：|len|checksum|header|data|dwVar,		26个字节
//;header										4字节
//;len											1字节
//;其中data的明文是 时间戳|版本|发布号|较验数据	字节
//;checksum										1字节
//;dwVar,用于解密								4字节


#define _CheckAlive_Client_x32(dwServerKey,dwRoleId,lpOutBuf) \
	__asm 	pushad \
	__asm 	pushfd \
	__asm	sub	esp,PP_NETCHECK_DATA_SIZE-24 \
	__asm	call	[Protector::My_GetTickCount]  \
	__asm	push eax \
	macro_call_GetTickCount()\
	__asm	push eax \
	__asm   mov eax,dwServerKey \
	__asm   xor eax,dwRoleId \
	__asm	lea edi,lpOutBuf \
	__asm 	mov	ebx,eax \
	__asm 	xor	eax,eax \
	__asm 	push	eax \
	__asm 	push	eax \
	__asm 	push	eax \
	__asm 	mov	esi,esp \
	DoIntCall_Net(ss) \
	__asm	push	ebx \
	__asm 	int 0x2a  \
	__asm	pop		ebx \
	__asm 	push	eax \
	__asm 	mov	edx,ebx	 \
	__asm 	Common_GetRandom() \
	__asm 	mov	ebx,eax	 \
	__asm 	sub	edx,ebx	 \
	__asm 	mov	esi,esp \
	__asm 	mov	ecx,PP_NETCHECK_DATA_SIZE \
	__asm 	push	edi \
	__asm 	push	ebx \
	__asm 	Common_Encrypt(a##efscd) \
	__asm 	pop	ebx	 \
	__asm 	pop	edi	 \
	__asm	push	edi \
	__asm	mov	edi,esp \
	__asm	add	edi,4 \
	__asm	mov	ecx,PP_NETCHECK_DATA_SIZE \
	__asm	xor	eax,eax \
	__asm	cld \
	__asm	repnz	stosb \
	__asm	pop	edi \
	__asm 	add	esp,PP_NETCHECK_DATA_SIZE \
	__asm 	xor	ebx,0x20071224 \
	__asm 	mov	dword ptr [edi+PP_NETCHECK_DATA_SIZE+6],ebx \
	__asm 	popfd \
	__asm 	popad

#define GACD_Decrypt_Buf(lpBuf,dwSize,dwKey,l) \
	__asm 	pushad \
	__asm 	pushfd \
	__asm 	mov	esi,lpBuf \
	__asm 	mov	edi,esi \
	__asm 	mov	ecx,dwSize \
	__asm 	mov	edx,dwKey \
	__asm 	_GACD_Decrypt_Buf_Continue##l: \
	__asm 	test ecx,ecx \
	__asm   jz _GACD_Decrypt_Buf_end##l \
	__asm 	lodsb \
	__asm 	inc	dl \
	__asm 	shl	dl,1 \
	__asm 	xor	al,dl \
	__asm 	stosb \
	__asm 	dec	ecx \
	__asm	jmp _GACD_Decrypt_Buf_Continue##l \
	__asm 	_GACD_Decrypt_Buf_end##l: \
	__asm 	popfd \
	__asm 	popad

#define GACD_Encrypt_Buf(lpBuf,dwSize,dwKey,l) \
	__asm 	pushad \
	__asm 	pushfd \
	__asm 	mov	esi,lpBuf \
	__asm 	mov	edi,esi \
	__asm 	mov	ecx,dwSize \
	__asm 	mov	edx,dwKey \
	__asm 	_GACD_Encrypt_Buf_Continue##l: \
	__asm 	test ecx,ecx \
	__asm   jz _GACD_Encrypt_Buf_end##l \
	__asm 	lodsb \
	__asm 	inc	dl \
	__asm 	shl	dl,1 \
	__asm 	xor	al,dl \
	__asm 	stosb \
	__asm 	dec	ecx \
	__asm	jmp _GACD_Encrypt_Buf_Continue##l \
	__asm	_GACD_Encrypt_Buf_end##l: \
	__asm 	popfd \
	__asm 	popad

#define GACD_Service_MagicWord 0x95191677

#define GACD_Service_x32(dwSid,dwBuf,dwSize,dwKey) \
	__asm 	pushad \
	__asm 	pushfd \
	__asm   push fs \
	__asm 	mov	eax,dwSid \
	__asm 	mov	ecx,dwSize \
	__asm 	mov	esi,dwBuf \
	__asm 	mov	edx,dwKey \
	__asm 	mov	ebx,'flyi' \
	__asm 	mov	edi,'ngki' \
	__asm 	xor	eax,edx	 \
	__asm 	sub	eax,3389	 \
	__asm 	xor	ecx,edx \
	__asm 	sub	ecx,3389	 \
	__asm 	xor	esi,edx \
	__asm 	sub	esi,3389	 \
	__asm 	xor	edx,GACD_Service_MagicWord	 \
	__asm 	int 0x23 \
	__asm   pop fs \
	__asm 	popfd \
	__asm 	popad

#define GACD_Service(dwSid,dwBuf,dwSize,dwKey) \
if(!Protector::bIsX64)									\
{ \
	GACD_Service_x32(dwSid,dwBuf,dwSize,dwKey)	\
} \
else										\
{	\
	GACD_Service_x64(dwSid,dwBuf,dwSize,dwKey); \
}


#define GACD_Service_CallGate(dwSid,dwBuf,dwSize,dwKey) \
	__asm 	pushad \
	__asm 	pushfd \
	__asm 	mov	eax,dwSid \
	__asm 	mov	ecx,dwSize \
	__asm 	mov	esi,dwBuf \
	__asm 	mov	edx,dwKey \
	__asm 	mov	ebx,'flyi' \
	__asm 	mov	edi,'ngki' \
	__asm 	xor	eax,edx	 \
	__asm 	sub	eax,3389	 \
	__asm 	xor	ecx,edx \
	__asm 	sub	ecx,3389	 \
	__asm 	xor	esi,edx \
	__asm 	sub	esi,3389	 \
	__asm 	xor	edx,GACD_Service_MagicWord	 \
	__asm 	call	fword ptr [Protector::GACD_CallGate_Selector] \
	__asm 	popfd \
	__asm 	popad

#define _GetTickCount_2kxp() \
	__asm 	push	edx	\
	__asm 	mov	edx,0x7ffe0000	\
	__asm 	mov	eax,dword ptr [edx]	\
	__asm 	mul	dword ptr [edx+4]	\
	__asm 	shrd	eax,edx,0x18	\
	__asm 	pop	edx	\

#define _GetTickCount_2k3VistaWindows7() \
	__asm 	push esi	\
	__asm 	push ecx	\
	__asm 	push edx	\
	__asm 	mov	esi,0x7ffe0000	\
	__asm 	_GetTickCount_2k3Vista_Loop:	\
	__asm 	mov	ecx,dword ptr [esi+324h]	\
	__asm 	mov	edx,dword ptr [esi+320h]	\
	__asm 	cmp	ecx,dword ptr [esi+328h]	\
	__asm 	je	_GetTickCount_2k3Vista_GoOn	\
	_asm	_emit 0xf3 \
	__asm	_emit 0x90 \
	__asm 	jmp	_GetTickCount_2k3Vista_Loop	\
	__asm 	_GetTickCount_2k3Vista_GoOn:	\
	__asm 	mov	eax,dword ptr [esi+4]	\
	__asm 	mul	edx	\
	__asm 	shl	ecx,8	\
	__asm 	imul	ecx,dword ptr [esi+4]	\
	__asm 	shrd	eax,edx,0x18	\
	__asm 	shr	edx,18h	\
	__asm 	add	eax,ecx	\
	__asm 	pop	edx	\
	__asm 	pop	ecx	\
	__asm 	pop	esi
