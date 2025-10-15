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
 * interf_dec.h
 *
 *
 * Project:
 *    AMR Floating-Point Codec
 *
 * Contains:
 *    Defines interface to AMR decoder
 *
 */

#ifndef _interf_dec_h_
#define _interf_dec_h_

/*
 * Function prototypes
 */
/*
 * Conversion from packed bitstream to endoded parameters
 * Decoding parameters to speech
 */
void decoder_interface_decode( void *st, unsigned char *bits, short *synth, int bfi );

/*
 * Reserve and init. memory
 */
void *decoder_interface_init( void );

/*
 * Exit and free memory
 */
void decoder_interface_exit( void *state );

#endif

