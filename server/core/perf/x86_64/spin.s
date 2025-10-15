
	.text
	.align	8
#extern "C" void spin(volatile int *locker);
	.globl	spin
spin:
	cmpl	$0, (%rdi) 
	je	try_lock
	pause
	jmp	spin
try_lock:
	lock	btsl	$0, (%rdi)
	jc	spin
	ret


