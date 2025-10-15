/*
 * ===================================================================
 *  TS 26.104
 *  R99   V3.5.0 2003-03
 *  REL-4 V4.4.0 2003-03
 *  REL-5 V5.1.0 2003-03
 *  3GPP AMR Floating-point Speech Codec
 * ===================================================================
 *
 */

/*
 * interf_enc.h
 *
 *
 * Project:
 *    AMR Floating-Point Codec
 *
 * Contains:
 *    Defines interface to AMR encoder
 *
 */
#ifndef __Interf_enc_H__  
#define __Interf_enc_H__  

/*
 * include files
 */
#include"sp_enc.h"

#ifdef __cplusplus

int encoder_interface_encode( void *st, enum Mode mode, short *speech, unsigned char *serial,/* max size 31 bytes */int forceSpeech );   /* use speech mode */

/*
 * Reserve and init. memory
 */
void *encoder_interface_init( int dtx );

/*
 * Exit and free memory
 */
void encoder_interface_exit( void *state );

#endif

#endif
