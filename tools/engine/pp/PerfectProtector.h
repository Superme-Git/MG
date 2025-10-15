#pragma once

//#define protector_ParentCheck
#include "VMProtectSDK.h"
namespace Protector{
int StartProtector(void);
void StopProtector(void);
}


#pragma warning(disable:4748)	//this warning is about /GS
#define Common_Encryption_Header 0x8d227f93

#define	macro_call_GetSystemTimeAsFileTime()	__asm	call	dword ptr[GetSystemTimeAsFileTime]
#define	macro_call_GetTickCount()				__asm	call	dword ptr[GetTickCount]
#define	macro_call_GetCurrentProcessId()		__asm	call	dword ptr[GetCurrentProcessId]
#define macro_call_DbgPrint()					__asm	call	dword ptr[DbgPrint]

#define Common_GetRandom() \
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

#define Common_Encrypt(l) \
	__asm 	push	ecx \
	__asm 	push	edi \
	__asm 	add	edi,5	 \
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
	__asm 	xor	al,54h \
	__asm 	jmp	_Common_Encrypt_4##l \
	__asm _Common_Encrypt_2##l: \
	__asm 	cmp	ecx,1 \
	__asm 	jnz	_Common_Encrypt_3##l \
	__asm 	xor	al,95h \
	__asm 	jmp	_Common_Encrypt_4##l \
	__asm _Common_Encrypt_3##l: \
	__asm 	test	ecx,ecx \
	__asm 	jnz	_Common_Encrypt_4##l \
	__asm 	xor	al,0e8h \
	__asm _Common_Encrypt_4##l: \
	__asm 	stosb \
	__asm 	jmp	_Common_Encrypt_Continue##l	 \
	__asm _Common_Encrypt_End##l: \
	__asm 	rol	edx,8	 \
	__asm 	pop	edi \
	__asm 	pop	ecx \
	__asm 	mov	eax,Common_Encryption_Header \
	__asm 	xor	eax,edx	 \
	__asm 	xor	eax,ebx	 \
	__asm 	stosd		 \
	__asm 	mov	eax,ecx	 \
	__asm 	xor	al,dl \
	__asm 	xor	al,dh \
	__asm 	xor	al,1 \
	__asm 	stosb		 \
	__asm 	mov	dword ptr [edi+ecx],edx \
	__asm 	push	ecx \
	__asm 	mov	esi,edi	 \
	__asm 	sub	esi,5	 \
	__asm 	cld \
	__asm 	xor	eax,eax \
	__asm 	xor	edx,edx \
	__asm 	add	ecx,9	 \
	__asm _Common_Encrypt_again##l: \
	__asm 	lodsb \
	__asm 	rol	al,cl \
	__asm 	xor	dl,al \
	__asm 	loop	_Common_Encrypt_again##l \
	__asm 	pop	ecx \
	__asm 	mov	al,dl	 \
	__asm 	mov	edx,dword ptr [edi+ecx]	 \
	__asm 	mov	dword ptr [edi+ecx],0 \
	__asm 	mov	byte ptr [edi+ecx],al \
	__asm 	xor	eax,eax

#define Common_Decrypt(l) \
	__asm 	push	ebx \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	esi \
	__asm 	push	edi \
	__asm 	pushfd \
	__asm 	mov	eax,edx				 \
	__asm 	xor	eax,dword ptr [esi]		 \
	__asm 	xor	eax,ecx				 \
	__asm 	xor	eax,Common_Encryption_Header	 \
	__asm 	mov	ebx,eax \
	__asm 	xor	eax,eax \
	__asm 	test	ebx,ebx \
	__asm 	jnz	_Common_Decrypt_end##l		 \
	__asm 	xor	eax,eax \
	__asm 	cld \
	__asm 	mov	al,byte ptr [esi+4] \
	__asm 	xor	al,cl \
	__asm 	xor	al,ch \
	__asm 	xor	al,1 \
	__asm 	test	al,al \
	__asm 	jz	_Common_Decrypt_end##l \
	__asm 	push	ecx \
	__asm 	movzx	ecx,al		 \
	__asm 	pop	ebx		 \
	__asm 	push	edx \
	__asm 	push	ecx \
	__asm 	push	esi \
	__asm 	xor	edx,edx \
	__asm 	mov	dl,byte ptr [esi+5+ecx]		 \
	__asm 	mov	dword ptr [esi+5+ecx],ebx	 \
	__asm 	add	ecx,9				 \
	__asm 	cld \
	__asm 	xor	eax,eax \
	__asm   _Common_Decrypt_again##l: \
	__asm 	lodsb \
	__asm 	rol	al,cl \
	__asm 	xor	dl,al \
	__asm 	loop	_Common_Decrypt_again##l \
	__asm 	pop	esi \
	__asm 	pop	ecx \
	__asm 	xor	eax,eax \
	__asm 	mov	dword ptr [esi+5+ecx],eax	 \
	__asm 	test	dl,dl \
	__asm 	pop	edx \
	__asm 	jnz	_Common_Decrypt_end##l \
	__asm 	add	edi,ecx \
	__asm 	dec	edi \
	__asm 	lea	esi,[esi+ecx+5] \
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
	__asm 	xor	al,0e8h \
	__asm 	jmp	_Common_Decrypt_4##l \
	__asm _Common_Decrypt_1##l: \
	__asm 	cmp	edx,2 \
	__asm 	jnz	_Common_Decrypt_2##l \
	__asm 	xor	al,95h \
	__asm 	jmp	_Common_Decrypt_4##l \
	__asm _Common_Decrypt_2##l: \
	__asm 	cmp	edx,3 \
	__asm 	jnz	_Common_Decrypt_3##l \
	__asm 	xor	al,54h \
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

#define DoIntCall(CommandId,l,ErrorLabel) \
	__asm 	push	esi \
	__asm 	push	edi \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	ebx \
	__asm 	pushfd \
	__asm 	mov	ebx,esp \
	__asm 	sub	esp,13*4	 \
	__asm 	lea	edi,[ebx-0x34] \
	__asm 	xor	eax,eax \
	__asm 	mov	ecx,13 \
	__asm 	rep	stosd \
	macro_call_GetCurrentProcessId() \
	__asm 	mov	dword ptr [ebx-4],eax \
	__asm 	mov	dword ptr [ebx-0x1c],CommandId \
	__asm 	lea	esi,[ebx-0x1c]		 \
	__asm 	lea	edi,[ebx-0x34]		 \
	__asm 	mov	ecx,4			 \
	__asm	push ebx	\
	__asm	mov	ebx,eax	\
	__asm	Common_GetRandom() \
	__asm	mov edx,eax \
	__asm 	Common_Encrypt(l) \
	__asm	pop	ebx	\
	__asm	mov	dword ptr [ebx-0x1c],0	\
	__asm 	mov	ecx,edx \
	__asm 	xor	ecx,[ebx-4]			 \
	__asm 	lea	eax,[ebx-0x34] \
	__asm 	xor	eax,edx \
	__asm 	mov	edx,eax			 \
	__asm	push ebx	\
	__asm 	mov	ebx,'iylf'		 \
	__asm 	mov	esi,'ikgn' \
	__asm 	mov	edi,'ress' \
	__asm 	int	0x22 \
	__asm	pop	ebx \
	__asm 	test	eax,eax \
	__asm 	jnz	_DoIntCall_end##l \
	__asm 	mov	eax,dword ptr [ebx-0x34] \
	__asm 	xor	eax,edx			 \
	__asm 	xor	eax,dword ptr [ebx-4] \
	__asm 	xor	eax,Common_Encryption_Header \
	__asm 	test	eax,eax \
	__asm 	jnz	_DoIntCall_end1##l \
	__asm 	mov	ecx,edx	 \
	__asm 	mov	edx,dword ptr [ebx-4]	 \
	__asm 	lea	edi,[ebx-0x8]	 \
	__asm 	lea	esi,[ebx-0x34]	 \
	__asm 	Common_Decrypt(l) \
	__asm   test eax,eax \
	__asm   jz _DoIntCall_end1##l \
	__asm 	mov	eax,dword ptr [ebx-0x8] \
	__asm	mov	dword ptr [ebx-8],0	\
	__asm 	jmp	_DoIntCall_end##l \
	__asm   _DoIntCall_end1##l: \
	__asm 	xor eax,eax	 \
	__asm _DoIntCall_end##l: \
	__asm 	mov	esp,ebx \
	__asm 	popfd \
	__asm 	pop	ebx \
	__asm 	pop	edx \
	__asm 	pop	ecx \
	__asm 	pop	edi \
	__asm 	pop	esi \
	__asm   test eax,eax \
	__asm   jz ErrorLabel

//入口：esi=lpOutBuf(至少是3个dword)
//出口：版本号|发布号|其他较验信息
#define DoIntCall_Net(l) \
	__asm 	push	esi \
	__asm 	push	edi \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	ebx	 \
	__asm 	pushfd \
	__asm 	mov	ebx,esp \
	__asm 	sub	esp,13*4		 \
	__asm 	lea	edi,[ebx-34h]		 \
	__asm 	xor	eax,eax  \
	__asm 	mov	ecx,13 \
	__asm 	rep	stosd \
	__asm 	mov	dword ptr [ebx-8],esi	 \
	macro_call_GetCurrentProcessId() \
	__asm 	mov	dword ptr [ebx-4],eax \
	__asm 	mov	dword ptr [ebx-1ch],2	 \
	__asm 	lea	esi,[ebx-1ch]		 \
	__asm 	lea	edi,[ebx-34h]		 \
	__asm 	mov	ecx,4			 \
	__asm 	push	ebx \
	__asm 	mov	ebx,eax			 \
	__asm 	Common_GetRandom() \
	__asm 	mov	edx,eax \
	__asm 	Common_Encrypt(l) \
	__asm 	pop	ebx \
	__asm 	mov	dword ptr [ebx-1ch],0 \
	__asm 	mov	ecx,edx \
	__asm 	xor	ecx,dword ptr [ebx-4]	 \
	__asm 	lea	eax,[ebx-34h] \
	__asm 	xor	eax,edx \
	__asm 	mov	edx,eax			 \
	__asm 	push	ebx \
	__asm 	mov	ebx,'iylf'		 \
	__asm 	mov	esi,'ikgn' \
	__asm 	mov	edi,'ress' \
	__asm 	int	22h \
	__asm 	pop	ebx \
	__asm 	mov	ecx,edx			 \
	__asm 	mov	edx,dword ptr [ebx-4]	 \
	__asm 	lea	edi,[ebx-1ch]		 \
	__asm 	lea	esi,[ebx-34h]		 \
	__asm 	Common_Decrypt(l) \
	__asm 	lea	esi,[ebx-1ch]  \
	__asm 	mov	edi,[ebx-8] \
	__asm 	mov	ecx,12			 \
	__asm 	rep	movsb \
	__asm 	mov	esp,ebx \
	__asm 	popfd \
	__asm 	pop	ebx \
	__asm 	pop	edx \
	__asm 	pop	ecx \
	__asm 	pop	edi \
	__asm 	pop	esi


#define DoIntCallNoErrorLabel(CommandId,l) \
	__asm 	push	esi \
	__asm 	push	edi \
	__asm 	push	ecx \
	__asm 	push	edx \
	__asm 	push	ebx \
	__asm 	pushfd \
	__asm 	mov	ebx,esp \
	__asm 	sub	esp,13*4	 \
	__asm 	lea	edi,[ebx-0x34] \
	__asm 	xor	eax,eax \
	__asm 	mov	ecx,13 \
	__asm 	rep	stosd \
	macro_call_GetCurrentProcessId() \
	__asm 	mov	dword ptr [ebx-4],eax \
	__asm 	mov	dword ptr [ebx-0x1c],CommandId \
	__asm 	lea	esi,[ebx-0x1c]		 \
	__asm 	lea	edi,[ebx-0x34]		 \
	__asm 	mov	ecx,4			 \
	__asm	push ebx	\
	__asm	mov	ebx,eax	\
	__asm	Common_GetRandom() \
	__asm	mov edx,eax \
	__asm 	Common_Encrypt(l) \
	__asm	pop	ebx	\
	__asm	mov	dword ptr [ebx-0x1c],0	\
	__asm 	mov	ecx,edx \
	__asm 	xor	ecx,[ebx-4]			 \
	__asm 	lea	eax,[ebx-0x34] \
	__asm 	xor	eax,edx \
	__asm 	mov	edx,eax			 \
	__asm	push ebx	\
	__asm 	mov	ebx,'iylf'		 \
	__asm 	mov	esi,'ikgn' \
	__asm 	mov	edi,'ress' \
	__asm 	int	0x22 \
	__asm	pop	ebx \
	__asm 	test	eax,eax \
	__asm 	jnz	_DoIntCall_end##l \
	__asm 	mov	eax,dword ptr [ebx-0x34] \
	__asm 	xor	eax,edx			 \
	__asm 	xor	eax,dword ptr [ebx-4] \
	__asm 	xor	eax,Common_Encryption_Header \
	__asm 	test	eax,eax \
	__asm 	jnz	_DoIntCall_end1##l \
	__asm 	mov	ecx,edx	 \
	__asm 	mov	edx,dword ptr [ebx-4]	 \
	__asm 	lea	edi,[ebx-0x8]	 \
	__asm 	lea	esi,[ebx-0x34]	 \
	__asm 	Common_Decrypt(l) \
	__asm   test eax,eax \
	__asm   jz _DoIntCall_end1##l \
	__asm 	mov	eax,dword ptr [ebx-0x8] \
	__asm	mov	dword ptr [ebx-8],0	\
	__asm 	jmp	_DoIntCall_end##l \
	__asm _DoIntCall_end1##l: \
	__asm 	xor eax,eax	 \
	__asm _DoIntCall_end##l: \
	__asm 	mov	esp,ebx \
	__asm 	popfd \
	__asm 	pop	ebx \
	__asm 	pop	edx \
	__asm 	pop	ecx \
	__asm 	pop	edi \
	__asm 	pop	esi


//;加密完：header|len|data|checksum|dwVar,		26个字节
//;header										4字节
//;len											1字节
//;其中data的明文是 时间戳|版本|发布号|较验数据	16字节
//;checksum										1字节
//;dwVar,用于解密								4字节
#define _CheckAlive_Client(dwServerKey,dwRoleId,lpOutBuf) \
	__asm 	pushad \
	__asm 	pushfd \
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
	__asm 	mov	ecx,16 \
	__asm 	push	edi \
	__asm 	push	ebx \
	__asm 	Common_Encrypt(a##efscd) \
	__asm 	pop	ebx	 \
	__asm 	pop	edi	 \
	__asm 	xor	eax,eax \
	__asm 	mov	dword ptr [esp],eax \
	__asm 	mov	dword ptr [esp+4],eax \
	__asm 	mov	dword ptr [esp+8],eax \
	__asm 	mov	dword ptr [esp+0xc],eax \
	__asm 	add	esp,16 \
	__asm 	xor	ebx,0x20071224 \
	__asm 	mov	dword ptr [edi+22],ebx \
	__asm 	popfd \
	__asm 	popad
