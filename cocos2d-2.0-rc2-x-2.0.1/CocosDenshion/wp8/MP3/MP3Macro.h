#undef REAL_IS_FLOAT
#define REAL_IS_FLOAT

#ifdef REAL_IS_FLOAT
#  define real float
#elif defined(REAL_IS_LONG_DOUBLE)
#  define real long double
#else
#  define real double
#endif

#define         FALSE                   0
#define         TRUE                    1

#define         SBLIMIT                 32
#define         SSLIMIT                 18

#define         MPG_MD_STEREO           0
#define         MPG_MD_JOINT_STEREO     1
#define         MPG_MD_DUAL_CHANNEL     2
#define         MPG_MD_MONO             3

#define MAXFRAMESIZE 2880
#define DECDELAY      528
/* FFT sizes */
#define BLKSIZE       1024
#define HBLKSIZE      (BLKSIZE/2 + 1)
#define BLKSIZE_s     256
#define HBLKSIZE_s    (BLKSIZE_s/2 + 1)
/* total number of scalefactor bands encoded */
#define SBMAX_l       22
#define SBMAX_s       13
#define hip_global_struct mpstr_tag
#define OUTSIZE_CLIPPED   (4096*sizeof(short))

#ifndef M_PI
#define M_PI       3.14159265358979323846
#endif
#ifndef M_SQRT2
#define M_SQRT2    1.41421356237309504880
#endif

#define         MAX_U_32_NUM            0xFFFFFFFF

#define MP3_ERR -1
#define MP3_OK  0
#define MP3_NEED_MORE 1
/* number of bytes needed by GetVbrTag to parse header */
#define XING_HEADER_SIZE 194
#define FRAMES_FLAG     0x0001
#define BYTES_FLAG      0x0002
#define TOC_FLAG        0x0004
#define VBR_SCALE_FLAG  0x0008
#define NUMTOCENTRIES 100
#define MAX_INPUT_FRAMESIZE 4096
#  define  LAME_ID   0xFFF88E3B
#define lame_report_fnc (void*)