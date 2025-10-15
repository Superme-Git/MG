	.text
	.macro	MD5_ROUND64
	//0
	mov	%ecx, %edi
	mov	(%rsi), %r10d
	xor	%edx, %edi
	and	%ebx, %edi
	lea	-680876936(%eax, %r10d, 1), %eax
	xor	%edx, %edi
	add	%edi, %eax
	mov	%ebx, %edi
	rol	$7, %eax
	mov	4(%rsi), %r10d
	add	%ebx, %eax
	//1
	xor	%ecx, %edi
	and	%eax, %edi
	lea	-389564586(%edx, %r10d, 1), %edx
	xor	%ecx, %edi
	add	%edi, %edx
	mov	%eax, %edi
	rol	$12, %edx
	mov	8(%rsi), %r10d
	add	%eax, %edx
	//2
	xor	%ebx, %edi
	and	%edx, %edi
	lea	606105819(%ecx, %r10d, 1), %ecx
	xor	%ebx, %edi
	add	%edi, %ecx
	mov	%edx, %edi
	rol	$17, %ecx
	mov	12(%rsi), %r10d
	add	%edx, %ecx
	//3
	xor	%eax, %edi
	and	%ecx, %edi
	lea	-1044525330(%ebx, %r10d, 1), %ebx
	xor	%eax, %edi
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$22, %ebx
	mov	16(%rsi), %r10d
	add	%ecx, %ebx
	//4
	xor	%edx, %edi
	and	%ebx, %edi
	lea	-176418897(%eax, %r10d, 1), %eax
	xor	%edx, %edi
	add	%edi, %eax
	mov	%ebx, %edi
	rol	$7, %eax
	mov	20(%rsi), %r10d
	add	%ebx, %eax
	//5
	xor	%ecx, %edi
	and	%eax, %edi
	lea	1200080426(%edx, %r10d, 1), %edx
	xor	%ecx, %edi
	add	%edi, %edx
	mov	%eax, %edi
	rol	$12, %edx
	mov	24(%rsi), %r10d
	add	%eax, %edx
	//6
	xor	%ebx, %edi
	and	%edx, %edi
	lea	-1473231341(%ecx, %r10d, 1), %ecx
	xor	%ebx, %edi
	add	%edi, %ecx
	mov	%edx, %edi
	rol	$17, %ecx
	mov	28(%rsi), %r10d
	add	%edx, %ecx
	//7
	xor	%eax, %edi
	and	%ecx, %edi
	lea	-45705983(%ebx, %r10d, 1), %ebx
	xor	%eax, %edi
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$22, %ebx
	mov	32(%rsi), %r10d
	add	%ecx, %ebx
	//8
	xor	%edx, %edi
	and	%ebx, %edi
	lea	1770035416(%eax, %r10d, 1), %eax
	xor	%edx, %edi
	add	%edi, %eax
	mov	%ebx, %edi
	rol	$7, %eax
	mov	36(%rsi), %r10d
	add	%ebx, %eax
	//9
	xor	%ecx, %edi
	and	%eax, %edi
	lea	-1958414417(%edx, %r10d, 1), %edx
	xor	%ecx, %edi
	add	%edi, %edx
	mov	%eax, %edi
	rol	$12, %edx
	mov	40(%rsi), %r10d
	add	%eax, %edx
	//10
	xor	%ebx, %edi
	and	%edx, %edi
	lea	-42063(%ecx, %r10d, 1), %ecx
	xor	%ebx, %edi
	add	%edi, %ecx
	mov	%edx, %edi
	rol	$17, %ecx
	mov	44(%rsi), %r10d
	add	%edx, %ecx
	//11
	xor	%eax, %edi
	and	%ecx, %edi
	lea	-1990404162(%ebx, %r10d, 1), %ebx
	xor	%eax, %edi
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$22, %ebx
	mov	48(%rsi), %r10d
	add	%ecx, %ebx
	//12
	xor	%edx, %edi
	and	%ebx, %edi
	lea	1804603682(%eax, %r10d, 1), %eax
	xor	%edx, %edi
	add	%edi, %eax
	mov	%ebx, %edi
	rol	$7, %eax
	mov	52(%rsi), %r10d
	add	%ebx, %eax
	//13
	xor	%ecx, %edi
	and	%eax, %edi
	lea	-40341101(%edx, %r10d, 1), %edx
	xor	%ecx, %edi
	add	%edi, %edx
	mov	%eax, %edi
	rol	$12, %edx
	mov	56(%rsi), %r10d
	add	%eax, %edx
	//14
	xor	%ebx, %edi
	and	%edx, %edi
	lea	-1502002290(%ecx, %r10d, 1), %ecx
	xor	%ebx, %edi
	add	%edi, %ecx
	mov	%edx, %edi
	rol	$17, %ecx
	mov	60(%rsi), %r10d
	add	%edx, %ecx
	//15
	xor	%eax, %edi
	and	%ecx, %edi
	lea	1236535329(%ebx, %r10d, 1), %ebx
	xor	%eax, %edi
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$22, %ebx
	mov	4(%rsi), %r10d
	add	%ecx, %ebx
	//16
	lea	-165796510(%eax, %r10d, 1), %eax
	xor	%ebx, %edi
	and	%edx, %edi
	mov	24(%rsi), %r10d
	xor	%ecx, %edi
	add	%edi, %eax
	mov	%ebx, %edi
	rol	$5, %eax
	add	%ebx, %eax
	//17
	lea	-1069501632(%edx, %r10d, 1), %edx
	xor	%eax, %edi
	and	%ecx, %edi
	mov	44(%rsi), %r10d
	xor	%ebx, %edi
	add	%edi, %edx
	mov	%eax, %edi
	rol	$9, %edx
	add	%eax, %edx
	//18
	lea	643717713(%ecx, %r10d, 1), %ecx
	xor	%edx, %edi
	and	%ebx, %edi
	mov	(%rsi), %r10d
	xor	%eax, %edi
	add	%edi, %ecx
	mov	%edx, %edi
	rol	$14, %ecx
	add	%edx, %ecx
	//19
	lea	-373897302(%ebx, %r10d, 1), %ebx
	xor	%ecx, %edi
	and	%eax, %edi
	mov	20(%rsi), %r10d
	xor	%edx, %edi
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$20, %ebx
	add	%ecx, %ebx
	//20
	lea	-701558691(%eax, %r10d, 1), %eax
	xor	%ebx, %edi
	and	%edx, %edi
	mov	40(%rsi), %r10d
	xor	%ecx, %edi
	add	%edi, %eax
	mov	%ebx, %edi
	rol	$5, %eax
	add	%ebx, %eax
	//21
	lea	38016083(%edx, %r10d, 1), %edx
	xor	%eax, %edi
	and	%ecx, %edi
	mov	60(%rsi), %r10d
	xor	%ebx, %edi
	add	%edi, %edx
	mov	%eax, %edi
	rol	$9, %edx
	add	%eax, %edx
	//22
	lea	-660478335(%ecx, %r10d, 1), %ecx
	xor	%edx, %edi
	and	%ebx, %edi
	mov	16(%rsi), %r10d
	xor	%eax, %edi
	add	%edi, %ecx
	mov	%edx, %edi
	rol	$14, %ecx
	add	%edx, %ecx
	//23
	lea	-405537848(%ebx, %r10d, 1), %ebx
	xor	%ecx, %edi
	and	%eax, %edi
	mov	36(%rsi), %r10d
	xor	%edx, %edi
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$20, %ebx
	add	%ecx, %ebx
	//24
	lea	568446438(%eax, %r10d, 1), %eax
	xor	%ebx, %edi
	and	%edx, %edi
	mov	56(%rsi), %r10d
	xor	%ecx, %edi
	add	%edi, %eax
	mov	%ebx, %edi
	rol	$5, %eax
	add	%ebx, %eax
	//25
	lea	-1019803690(%edx, %r10d, 1), %edx
	xor	%eax, %edi
	and	%ecx, %edi
	mov	12(%rsi), %r10d
	xor	%ebx, %edi
	add	%edi, %edx
	mov	%eax, %edi
	rol	$9, %edx
	add	%eax, %edx
	//26
	lea	-187363961(%ecx, %r10d, 1), %ecx
	xor	%edx, %edi
	and	%ebx, %edi
	mov	32(%rsi), %r10d
	xor	%eax, %edi
	add	%edi, %ecx
	mov	%edx, %edi
	rol	$14, %ecx
	add	%edx, %ecx
	//27
	lea	1163531501(%ebx, %r10d, 1), %ebx
	xor	%ecx, %edi
	and	%eax, %edi
	mov	52(%rsi), %r10d
	xor	%edx, %edi
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$20, %ebx
	add	%ecx, %ebx
	//28
	lea	-1444681467(%eax, %r10d, 1), %eax
	xor	%ebx, %edi
	and	%edx, %edi
	mov	8(%rsi), %r10d
	xor	%ecx, %edi
	add	%edi, %eax
	mov	%ebx, %edi
	rol	$5, %eax
	add	%ebx, %eax
	//29
	lea	-51403784(%edx, %r10d, 1), %edx
	xor	%eax, %edi
	and	%ecx, %edi
	mov	28(%rsi), %r10d
	xor	%ebx, %edi
	add	%edi, %edx
	mov	%eax, %edi
	rol	$9, %edx
	add	%eax, %edx
	//30
	lea	1735328473(%ecx, %r10d, 1), %ecx
	xor	%edx, %edi
	and	%ebx, %edi
	mov	48(%rsi), %r10d
	xor	%eax, %edi
	add	%edi, %ecx
	mov	%edx, %edi
	rol	$14, %ecx
	add	%edx, %ecx
	//31
	lea	-1926607734(%ebx, %r10d, 1), %ebx
	xor	%ecx, %edi
	and	%eax, %edi
	mov	20(%rsi), %r10d
	xor	%edx, %edi
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$20, %ebx
	add	%ecx, %ebx
	//32
	xor	%edx, %edi
	xor	%ebx, %edi
	lea	-378558(%eax, %r10d, 1), %eax
	add	%edi, %eax
	mov	32(%rsi), %r10d
	rol	$4, %eax
	mov	%ebx, %edi
	//33
	lea	-2022574463(%edx, %r10d, 1), %edx
	add	%ebx, %eax
	xor	%ecx, %edi
	xor	%eax, %edi
	mov	44(%rsi), %r10d
	add	%edi, %edx
	mov	%eax, %edi
	rol	$11, %edx
	add	%eax, %edx
	//34
	xor	%ebx, %edi
	xor	%edx, %edi
	lea	1839030562(%ecx, %r10d, 1), %ecx
	add	%edi, %ecx
	mov	56(%rsi), %r10d
	rol	$16, %ecx
	mov	%edx, %edi
	//35
	lea	-35309556(%ebx, %r10d, 1), %ebx
	add	%edx, %ecx
	xor	%eax, %edi
	xor	%ecx, %edi
	mov	4(%rsi), %r10d
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$23, %ebx
	add	%ecx, %ebx
	//36
	xor	%edx, %edi
	xor	%ebx, %edi
	lea	-1530992060(%eax, %r10d, 1), %eax
	add	%edi, %eax
	mov	16(%rsi), %r10d
	rol	$4, %eax
	mov	%ebx, %edi
	//37
	lea	1272893353(%edx, %r10d, 1), %edx
	add	%ebx, %eax
	xor	%ecx, %edi
	xor	%eax, %edi
	mov	28(%rsi), %r10d
	add	%edi, %edx
	mov	%eax, %edi
	rol	$11, %edx
	add	%eax, %edx
	//38
	xor	%ebx, %edi
	xor	%edx, %edi
	lea	-155497632(%ecx, %r10d, 1), %ecx
	add	%edi, %ecx
	mov	40(%rsi), %r10d
	rol	$16, %ecx
	mov	%edx, %edi
	//39
	lea	-1094730640(%ebx, %r10d, 1), %ebx
	add	%edx, %ecx
	xor	%eax, %edi
	xor	%ecx, %edi
	mov	52(%rsi), %r10d
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$23, %ebx
	add	%ecx, %ebx
	//40
	xor	%edx, %edi
	xor	%ebx, %edi
	lea	681279174(%eax, %r10d, 1), %eax
	add	%edi, %eax
	mov	(%rsi), %r10d
	rol	$4, %eax
	mov	%ebx, %edi
	//41
	lea	-358537222(%edx, %r10d, 1), %edx
	add	%ebx, %eax
	xor	%ecx, %edi
	xor	%eax, %edi
	mov	12(%rsi), %r10d
	add	%edi, %edx
	mov	%eax, %edi
	rol	$11, %edx
	add	%eax, %edx
	//42
	xor	%ebx, %edi
	xor	%edx, %edi
	lea	-722521979(%ecx, %r10d, 1), %ecx
	add	%edi, %ecx
	mov	24(%rsi), %r10d
	rol	$16, %ecx
	mov	%edx, %edi
	//43
	lea	76029189(%ebx, %r10d, 1), %ebx
	add	%edx, %ecx
	xor	%eax, %edi
	xor	%ecx, %edi
	mov	36(%rsi), %r10d
	add	%edi, %ebx
	mov	%ecx, %edi
	rol	$23, %ebx
	add	%ecx, %ebx
	//44
	xor	%edx, %edi
	xor	%ebx, %edi
	lea	-640364487(%eax, %r10d, 1), %eax
	add	%edi, %eax
	mov	48(%rsi), %r10d
	rol	$4, %eax
	mov	%ebx, %edi
	//45
	lea	-421815835(%edx, %r10d, 1), %edx
	add	%ebx, %eax
	xor	%ecx, %edi
	xor	%eax, %edi
	mov	60(%rsi), %r10d
	add	%edi, %edx
	mov	%eax, %edi
	rol	$11, %edx
	add	%eax, %edx
	//46
	xor	%ebx, %edi
	xor	%edx, %edi
	lea	530742520(%ecx, %r10d, 1), %ecx
	add	%edi, %ecx
	mov	8(%rsi), %r10d
	rol	$16, %ecx
	mov	%edx, %edi
	//47
	lea	-995338651(%ebx, %r10d, 1), %ebx
	add	%edx, %ecx
	xor	%eax, %edi
	xor	%ecx, %edi
	mov	(%rsi), %r10d
	add	%edi, %ebx
	mov	$4294967295, %edi
	rol	$23, %ebx
	xor	%edx, %edi
	add	%ecx, %ebx
	//48
	or	%ebx, %edi
	lea	-198630844(%eax, %r10d, 1), %eax
	xor	%ecx, %edi
	mov	28(%rsi), %r10d
	add	%edi, %eax
	mov	$4294967295, %edi
	rol	$6, %eax
	xor	%ecx, %edi
	add	%ebx, %eax
	//49
	or	%eax, %edi
	lea	1126891415(%edx, %r10d, 1), %edx
	xor	%ebx, %edi
	mov	56(%rsi), %r10d
	add	%edi, %edx
	mov	$4294967295, %edi
	rol	$10, %edx
	xor	%ebx, %edi
	add	%eax, %edx
	//50
	or	%edx, %edi
	lea	-1416354905(%ecx, %r10d, 1), %ecx
	xor	%eax, %edi
	mov	20(%rsi), %r10d
	add	%edi, %ecx
	mov	$4294967295, %edi
	rol	$15, %ecx
	xor	%eax, %edi
	add	%edx, %ecx
	//51
	or	%ecx, %edi
	lea	-57434055(%ebx, %r10d, 1), %ebx
	xor	%edx, %edi
	mov	48(%rsi), %r10d
	add	%edi, %ebx
	mov	$4294967295, %edi
	rol	$21, %ebx
	xor	%edx, %edi
	add	%ecx, %ebx
	//52
	or	%ebx, %edi
	lea	1700485571(%eax, %r10d, 1), %eax
	xor	%ecx, %edi
	mov	12(%rsi), %r10d
	add	%edi, %eax
	mov	$4294967295, %edi
	rol	$6, %eax
	xor	%ecx, %edi
	add	%ebx, %eax
	//53
	or	%eax, %edi
	lea	-1894986606(%edx, %r10d, 1), %edx
	xor	%ebx, %edi
	mov	40(%rsi), %r10d
	add	%edi, %edx
	mov	$4294967295, %edi
	rol	$10, %edx
	xor	%ebx, %edi
	add	%eax, %edx
	//54
	or	%edx, %edi
	lea	-1051523(%ecx, %r10d, 1), %ecx
	xor	%eax, %edi
	mov	4(%rsi), %r10d
	add	%edi, %ecx
	mov	$4294967295, %edi
	rol	$15, %ecx
	xor	%eax, %edi
	add	%edx, %ecx
	//55
	or	%ecx, %edi
	lea	-2054922799(%ebx, %r10d, 1), %ebx
	xor	%edx, %edi
	mov	32(%rsi), %r10d
	add	%edi, %ebx
	mov	$4294967295, %edi
	rol	$21, %ebx
	xor	%edx, %edi
	add	%ecx, %ebx
	//56
	or	%ebx, %edi
	lea	1873313359(%eax, %r10d, 1), %eax
	xor	%ecx, %edi
	mov	60(%rsi), %r10d
	add	%edi, %eax
	mov	$4294967295, %edi
	rol	$6, %eax
	xor	%ecx, %edi
	add	%ebx, %eax
	//57
	or	%eax, %edi
	lea	-30611744(%edx, %r10d, 1), %edx
	xor	%ebx, %edi
	mov	24(%rsi), %r10d
	add	%edi, %edx
	mov	$4294967295, %edi
	rol	$10, %edx
	xor	%ebx, %edi
	add	%eax, %edx
	//58
	or	%edx, %edi
	lea	-1560198380(%ecx, %r10d, 1), %ecx
	xor	%eax, %edi
	mov	52(%rsi), %r10d
	add	%edi, %ecx
	mov	$4294967295, %edi
	rol	$15, %ecx
	xor	%eax, %edi
	add	%edx, %ecx
	//59
	or	%ecx, %edi
	lea	1309151649(%ebx, %r10d, 1), %ebx
	xor	%edx, %edi
	mov	16(%rsi), %r10d
	add	%edi, %ebx
	mov	$4294967295, %edi
	rol	$21, %ebx
	xor	%edx, %edi
	add	%ecx, %ebx
	//60
	or	%ebx, %edi
	lea	-145523070(%eax, %r10d, 1), %eax
	xor	%ecx, %edi
	mov	44(%rsi), %r10d
	add	%edi, %eax
	mov	$4294967295, %edi
	rol	$6, %eax
	xor	%ecx, %edi
	add	%ebx, %eax
	//61
	or	%eax, %edi
	lea	-1120210379(%edx, %r10d, 1), %edx
	xor	%ebx, %edi
	mov	8(%rsi), %r10d
	add	%edi, %edx
	mov	$4294967295, %edi
	rol	$10, %edx
	xor	%ebx, %edi
	add	%eax, %edx
	//62
	or	%edx, %edi
	lea	718787259(%ecx, %r10d, 1), %ecx
	xor	%eax, %edi
	mov	36(%rsi), %r10d
	add	%edi, %ecx
	mov	$4294967295, %edi
	rol	$15, %ecx
	xor	%eax, %edi
	add	%edx, %ecx
	//63
	or	%ecx, %edi
	lea	-343485551(%ebx, %r10d, 1), %ebx
	xor	%edx, %edi
	add	%edi, %ebx
	rol	$21, %ebx
	add	%ecx, %ebx
	.endm

#extern "C" void md5_init_context( unsigned char context[88] );
#extern "C" void md5_update( unsigned char context[88], const void *input, unsigned int inputlen );
#extern "C" void md5_final( unsigned char context[88] );
#extern "C" void md5_digest( unsigned char digest[16], const void *input, unsigned int inputlen );

.global md5_init_context, md5_update, md5_final, md5_digest

	.align	2
md5_init_context:
.Lmd5_init_context:
	movl	$0x67452301, (%rdi)
	movl	$0xEFCDAB89, 4(%rdi)
	movl	$0x98BADCFE, 8(%rdi)
	movl	$0x10325476, 12(%rdi)
	movq	$0, 16(%rdi)
	ret

	.align	2
md5_digest:
	add	$-88, %rsp
	push	%rdi
	lea	8(%rsp), %rdi
	call	.Lmd5_init_context
	call	.Lmd5_update
	lea	8(%rsp), %rdi
	call	.Lmd5_final
	pop	%rdi
	mov	(%rsp), %rax	
	mov	8(%rsp), %rdx
	mov	%rax, (%rdi)
	mov	%rdx, 8(%rdi)
	add	$88, %rsp
	ret

	.align	2
md5_update:
.Lmd5_update:
	mov	16(%rdi), %rax
	add	%rdx, 16(%rdi)
	and	$0x3F, %rax
	mov	$64, %rcx
	mov	%rdx, %r8
	mov	%rdi, %r9
	mov	%rbx, %r11
	jz	.Lhas_no_remain
	sub	%rax, %rcx
	cmp	%rcx, %rdx
	jae	.Lneed_transform
	lea	24(%r9, %rax), %rdi
	mov	%rdx, %rcx
	and	$7, %rdx
	shr	$3, %rcx
	cld	
	rep	movsq
	mov	%rdx, %rcx
	rep	movsb
	ret
.Lneed_transform:
	lea	24(%r9, %rax), %rdi
	sub	%rcx, %r8
	mov	%rcx, %rax
	shr	$3, %rcx
	and	$7, %rax
	cld
	rep	movsq
	mov	%rax, %rcx
	rep	movsb
	push	%rsi
	mov	(%r9),   %eax
	mov	4(%r9),  %ebx
	mov	8(%r9),  %ecx
	mov	12(%r9), %edx
	lea	24(%r9), %rsi
	MD5_ROUND64
	add	%eax, (%r9)
	add	%ebx, 4(%r9)
	add	%ecx, 8(%r9)
	add	%edx, 12(%r9)
	pop	%rsi
.Lhas_no_remain:
	mov	%r8, %rcx
	mov	%r8, %rax
	and	$0x3F, %rcx
	jz	.Lneed_not_copy_remain
	mov	%rsi, %r10
	and	$0xFFFFFFFFFFFFFFC0, %rax
	add	%rax, %rsi
	mov	%rcx, %rax
	shr	$3, %rcx
	and	$7, %rax
	lea	24(%r9), %rdi
	cld
	rep	movsq
	mov	%rax, %rcx
	rep	movsb	
	mov	%r10, %rsi
.Lneed_not_copy_remain:
	shr	$6, %r8
	jz	.Lfinish
	mov	(%r9),   %eax
	mov	4(%r9),  %ebx
	mov	8(%r9),  %ecx
	mov	12(%r9), %edx
.Lupdate_next:
	MD5_ROUND64
	add	$64, %rsi
	add	(%r9),   %eax	
	add	4(%r9),  %ebx	
	add	8(%r9),  %ecx	
	add	12(%r9), %edx	
	add	$-1, %r8
	mov	%eax, (%r9)
	mov	%ebx, 4(%r9)
	mov	%ecx, 8(%r9)
	mov	%edx, 12(%r9)
	jnz	.Lupdate_next
.Lfinish:	
	mov	%r11, %rbx
	ret

	.align	2
md5_final:
.Lmd5_final:
	mov	%rbx, %r11
	mov	%rdi, %r9
	mov	16(%rdi), %rcx
	lea	24(%rdi), %rsi
	and	$0x3F, %rcx
	movb	$0x80, 24(%rdi, %rcx)
	cmp	$56, %rcx
	lea	25(%rdi, %rcx), %rdi
	jb	.Lpadding_len
	neg	%rcx
	add	$63, %rcx
	xor	%rax, %rax
	mov	%rcx, %rdx
	shr	$3, %rcx
	and	$7, %rdx
	cld
	rep	stosq
	mov	%rdx, %rcx
	rep	stosb
	mov	(%r9),   %eax
	mov	4(%r9),  %ebx
	mov	8(%r9),  %ecx
	mov	12(%r9), %edx
	MD5_ROUND64
	add	%eax, (%r9)
	add	%ebx, 4(%r9)
	add	%ecx, 8(%r9)
	add	%edx, 12(%r9)
	lea	24(%r9), %rdi
	mov	$-1, %rcx
.Lpadding_len:
	neg	%rcx
	add	$55, %rcx
	xor	%rax, %rax
	mov	%rcx, %rdx
	shr	$3, %rcx
	and	$7, %rdx
	cld
	rep	stosq
	mov	%rdx, %rcx
	rep	stosb
	mov	16(%r9), %rax
	shl	$3, %rax
	mov	%rax, (%rdi)
	mov	(%r9),   %eax
	mov	4(%r9),  %ebx
	mov	8(%r9),  %ecx
	mov	12(%r9), %edx
	MD5_ROUND64
	add	%eax, (%r9)
	add	%ebx, 4(%r9)
	add	%ecx, 8(%r9)
	add	%edx, 12(%r9)
	mov	%r11, %rbx
	ret

