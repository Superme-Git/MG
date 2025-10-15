#include <stdarg.h>
#include "MP3Macro.h"
//    struct lame_internal_flags {
//
//  /********************************************************************
//   * internal variables NOT set by calling program, and should not be *
//   * modified by the calling program                                  *
//   ********************************************************************/
//
//        /*
//         * Some remarks to the Class_ID field:
//         * The Class ID is an Identifier for a pointer to this struct.
//         * It is very unlikely that a pointer to lame_global_flags has the same 32 bits
//         * in it's structure (large and other special properties, for instance prime).
//         *
//         * To test that the structure is right and initialized, use:
//         *     if ( gfc -> Class_ID == LAME_ID ) ...
//         * Other remark:
//         *     If you set a flag to 0 for uninit data and 1 for init data, the right test
//         *     should be "if (flag == 1)" and NOT "if (flag)". Unintended modification
//         *     of this element will be otherwise misinterpreted as an init.
//         */
//#  define  LAME_ID   0xFFF88E3B
//        unsigned long class_id;
//
//        int     lame_encode_frame_init;
//        int     iteration_init_init;
//        int     fill_buffer_resample_init;
//
//        SessionConfig_t cfg;
//
//        /* variables used by lame.c */
//        Bit_stream_struc bs;
//        III_side_info_t l3_side;
//
//        scalefac_struct scalefac_band;
//
//        PsyStateVar_t sv_psy; /* DATA FROM PSYMODEL.C */
//        PsyResult_t ov_psy;
//        EncStateVar_t sv_enc; /* DATA FROM ENCODER.C */
//        EncResult_t ov_enc;
//        QntStateVar_t sv_qnt; /* DATA FROM QUANTIZE.C */
//
//        RpgStateVar_t sv_rpg;
//        RpgResult_t ov_rpg;
//
//        /* optional ID3 tags, used in id3tag.c  */
//        struct id3tag_spec tag_spec;
//        uint16_t nMusicCRC;
//
//        uint16_t _unused;
//
//        /* CPU features */
//        struct {
//            unsigned int MMX:1; /* Pentium MMX, Pentium II...IV, K6, K6-2,
//                                   K6-III, Athlon */
//            unsigned int AMD_3DNow:1; /* K6-2, K6-III, Athlon      */
//            unsigned int SSE:1; /* Pentium III, Pentium 4    */
//            unsigned int SSE2:1; /* Pentium 4, K8             */
//            unsigned int _unused:28;
//        } CPU_features;
//
//
//        VBR_seek_info_t VBR_seek_table; /* used for Xing VBR header */
//
//        ATH_t  *ATH;         /* all ATH related stuff */
//
//        PsyConst_t *cd_psy;
//
//        /* used by the frame analyzer */
//        plotting_data *pinfo;
//        hip_t hip;
//
//        iteration_loop_t iteration_loop;
//
//        /* functions to replace with CPU feature optimized versions in takehiro.c */
//        int     (*choose_table) (const int *ix, const int *const end, int *const s);
//        void    (*fft_fht) (FLOAT *, int);
//        void    (*init_xrpow_core) (gr_info * const cod_info, FLOAT xrpow[576], int upper,
//                                    FLOAT * sum);
//
//        lame_report_function report_msg;
//        lame_report_function report_dbg;
//        lame_report_function report_err;
//    };
typedef enum short_block_e {
    short_block_not_set = -1, /* allow LAME to decide */
    short_block_allowed = 0, /* LAME may use them, even different block types for L/R */
    short_block_coupled, /* LAME may use them, but always same block types in L/R */
    short_block_dispensed, /* LAME will not use short blocks, long blocks only */
    short_block_forced  /* LAME will not use long blocks, short blocks only */
} short_block_t;

typedef enum vbr_mode_e {
  vbr_off=0,
  vbr_mt,               /* obsolete, same as vbr_mtrh */
  vbr_rh,
  vbr_abr,
  vbr_mtrh,
  vbr_max_indicator,    /* Don't use this! It's used for sanity checks.       */
  vbr_default=vbr_mtrh    /* change this to change the default VBR mode of LAME */
} vbr_mode;
/* MPEG modes */
typedef enum MPEG_mode_e {
  STEREO = 0,
  JOINT_STEREO,
  DUAL_CHANNEL,   /* LAME doesn't supports this! */
  MONO,
  NOT_SET,
  MAX_INDICATOR   /* Don't use this! It's used for sanity checks. */
} MPEG_mode;
/***********************************************************************
*
*  Control Parameters set by User.  These parameters are here for
*  backwards compatibility with the old, non-shared lib API.
*  Please use the lame_set_variablename() functions below
*
*
***********************************************************************/
struct lame_global_struct {
    unsigned int class_id;

    /* input description */
    unsigned long num_samples; /* number of samples. default=2^32-1           */
    int     num_channels;    /* input number of channels. default=2         */
    int     samplerate_in;   /* input_samp_rate in Hz. default=44.1 kHz     */
    int     samplerate_out;  /* output_samp_rate.
                                default: LAME picks best value
                                at least not used for MP3 decoding:
                                Remember 44.1 kHz MP3s and AC97           */
    float   scale;           /* scale input by this amount before encoding
                                at least not used for MP3 decoding          */
    float   scale_left;      /* scale input of channel 0 (left) by this
                                amount before encoding                      */
    float   scale_right;     /* scale input of channel 1 (right) by this
                                amount before encoding                      */

    /* general control params */
    int     analysis;        /* collect data for a MP3 frame analyzer?      */
    int     write_lame_tag;  /* add Xing VBR tag?                           */
    int     decode_only;     /* use lame/mpglib to convert mp3 to wav       */
    int     quality;         /* quality setting 0=best,  9=worst  default=5 */
    MPEG_mode mode;          /* see enum in lame.h
                                default = LAME picks best value             */
    int     force_ms;        /* force M/S mode.  requires mode=1            */
    int     free_format;     /* use free format? default=0                  */
    int     findReplayGain;  /* find the RG value? default=0       */
    int     decode_on_the_fly; /* decode on the fly? default=0                */
    int     write_id3tag_automatic; /* 1 (default) writes ID3 tags, 0 not */

    int     nogap_total;
    int     nogap_current;

    int     substep_shaping;
    int     noise_shaping;
    int     subblock_gain;   /*  0 = no, 1 = yes */
    int     use_best_huffman; /* 0 = no.  1=outside loop  2=inside loop(slow) */

    /*
     * set either brate>0  or compression_ratio>0, LAME will compute
     * the value of the variable not set.
     * Default is compression_ratio = 11.025
     */
    int     brate;           /* bitrate                                    */
    float   compression_ratio; /* sizeof(wav file)/sizeof(mp3 file)          */


    /* frame params */
    int     copyright;       /* mark as copyright. default=0           */
    int     original;        /* mark as original. default=1            */
    int     extension;       /* the MP3 'private extension' bit.
                                Meaningless                            */
    int     emphasis;        /* Input PCM is emphased PCM (for
                                instance from one of the rarely
                                emphased CDs), it is STRONGLY not
                                recommended to use this, because
                                psycho does not take it into account,
                                and last but not least many decoders
                                don't care about these bits          */
    int     error_protection; /* use 2 bytes per frame for a CRC
                                 checksum. default=0                    */
    int     strict_ISO;      /* enforce ISO spec as much as possible   */

    int     disable_reservoir; /* use bit reservoir?                     */

    /* quantization/noise shaping */
    int     quant_comp;
    int     quant_comp_short;
    int     experimentalY;
    int     experimentalZ;
    int     exp_nspsytune;

    int     preset;

    /* VBR control */
    vbr_mode VBR;
    float   VBR_q_frac;      /* Range [0,...,1[ */
    int     VBR_q;           /* Range [0,...,9] */
    int     VBR_mean_bitrate_kbps;
    int     VBR_min_bitrate_kbps;
    int     VBR_max_bitrate_kbps;
    int     VBR_hard_min;    /* strictly enforce VBR_min_bitrate
                                normaly, it will be violated for analog
                                silence                                 */


    /* resampling and filtering */
    int     lowpassfreq;     /* freq in Hz. 0=lame choses.
                                -1=no filter                          */
    int     highpassfreq;    /* freq in Hz. 0=lame choses.
                                -1=no filter                          */
    int     lowpasswidth;    /* freq width of filter, in Hz
                                (default=15%)                         */
    int     highpasswidth;   /* freq width of filter, in Hz
                                (default=15%)                         */



    /*
     * psycho acoustics and other arguments which you should not change
     * unless you know what you are doing
     */
    float   maskingadjust;
    float   maskingadjust_short;
    int     ATHonly;         /* only use ATH                         */
    int     ATHshort;        /* only use ATH for short blocks        */
    int     noATH;           /* disable ATH                          */
    int     ATHtype;         /* select ATH formula                   */
    float   ATHcurve;        /* change ATH formula 4 shape           */
    float   ATH_lower_db;    /* lower ATH by this many db            */
    int     athaa_type;      /* select ATH auto-adjust scheme        */
    float   athaa_sensitivity; /* dB, tune active region of auto-level */
    short_block_t short_blocks;
    int     useTemporal;     /* use temporal masking effect          */
    float   interChRatio;
    float   msfix;           /* Naoki's adjustment of Mid/Side maskings */

    int     tune;            /* 0 off, 1 on */
    float   tune_value_a;    /* used to pass values for debugging and stuff */

    float   attackthre;      /* attack threshold for L/R/M channel */
    float   attackthre_s;    /* attack threshold for S channel */


    struct {
        void    (*msgf) (const char *format, va_list ap);
        void    (*debugf) (const char *format, va_list ap);
        void    (*errorf) (const char *format, va_list ap);
    } report;

  /************************************************************************/
    /* internal variables, do not set...                                    */
    /* provided because they may be of use to calling application           */
  /************************************************************************/

    int     lame_allocated_gfp; /* is this struct owned by calling
                                   program or lame?                     */



  /**************************************************************************/
    /* more internal variables are stored in this structure:                  */
  /**************************************************************************/
//    lame_internal_flags *internal_flags;


    struct {
        int     mmx;
        int     amd3dnow;
        int     sse;

    } asm_optimizations;
};
struct lame_global_struct;
typedef struct lame_global_struct lame_global_flags;
typedef lame_global_flags *lame_t;
struct hip_global_struct;
typedef struct hip_global_struct hip_global_flags;
typedef hip_global_flags *hip_t;
struct PcmBuffer {
    void   *ch[2];           /* buffer for each channel */
    int     w;               /* sample width */
    int     n;               /* number samples allocated */
    int     u;               /* number samples used */
    int     skip_start;      /* number samples to ignore at the beginning */
    int     skip_end;        /* number samples to ignore at the end */
};
/* global data for get_audio.c. */
struct stMusicIn {
	const byte* ptr;
	size_t size;
	unsigned int cur;
};
typedef struct get_audio_global_data_struct {
    int     count_samples_carefully;
    int     pcmbitwidth;
    int     pcmswapbytes;
    int     pcm_is_unsigned_8bit;
    int     pcm_is_ieee_float;
    unsigned int num_samples_read;
	stMusicIn music_in;
    //FILE   *music_in;
    //SNDFILE *snd_file;
    hip_t   hip;
    PcmBuffer pcm32;
    PcmBuffer pcm16;
    size_t  in_id3v2_size;
    unsigned char* in_id3v2_tag;
} get_audio_global_data;
typedef enum sound_file_format_e {
    sf_unknown,
    sf_raw,
    sf_wave,
    sf_aiff,
    sf_mp1,                  /* MPEG Layer 1, aka mpg */
    sf_mp2,                  /* MPEG Layer 2 */
    sf_mp3,                  /* MPEG Layer 3 */
    sf_mp123,                /* MPEG Layer 1,2 or 3; whatever .mp3, .mp2, .mp1 or .mpg contains */
    sf_ogg
} sound_file_format;
typedef struct ReaderConfig
{
    sound_file_format input_format;
    int   swapbytes;                /* force byte swapping   default=0 */
    int   swap_channel;             /* 0: no-op, 1: swaps input channels */
    int   input_samplerate;
} ReaderConfig;

struct buf {
    unsigned char *pnt;
    long    size;
    long    pos;
    struct buf *next;
    struct buf *prev;
};

struct al_table2 {
    short   bits;
    short   d;
};

struct frame {
    int     stereo;
    int     single;          /* single channel (monophonic) */
    int     lsf;             /* 0 = MPEG-1, 1 = MPEG-2/2.5 */
    int     mpeg25;          /* 1 = MPEG-2.5, 0 = MPEG-1/2 */
    int     header_change;
    int     lay;             /* Layer */
    int     error_protection; /* 1 = CRC-16 code following header */
    int     bitrate_index;
    int     sampling_frequency; /* sample rate of decompressed audio in Hz */
    int     padding;
    int     extension;
    int     mode;
    int     mode_ext;
    int     copyright;
    int     original;
    int     emphasis;
    int     framesize;       /* computed framesize */

    /* AF: ADDED FOR LAYER1/LAYER2 */
    int     II_sblimit;
    struct al_table2 const *alloc;
    int     down_sample_sblimit;
    int     down_sample;
};

struct gr_info_s {
    int     scfsi;
    unsigned part2_3_length;
    unsigned big_values;
    unsigned scalefac_compress;
    unsigned block_type;
    unsigned mixed_block_flag;
    unsigned table_select[3];
    unsigned subblock_gain[3];
    unsigned maxband[3];
    unsigned maxbandl;
    unsigned maxb;
    unsigned region1start;
    unsigned region2start;
    unsigned preflag;
    unsigned scalefac_scale;
    unsigned count1table_select;
    real   *full_gain[3];
    real   *pow2gain;
};

struct III_sideinfo {
    unsigned main_data_begin;
    unsigned private_bits;
    struct {
        struct gr_info_s gr[2];
    } ch[2];
};

struct plotting_data {
    int     frameNum;        /* current frame number */
    int     frameNum123;
    int     num_samples;     /* number of pcm samples read for this frame */
    double  frametime;       /* starting time of frame, in seconds */
    double  pcmdata[2][1600];
    double  pcmdata2[2][1152 + 1152 - DECDELAY];
    double  xr[2][2][576];
    double  mpg123xr[2][2][576];
    double  ms_ratio[2];
    double  ms_ener_ratio[2];

    /* L,R, M and S values */
    double  energy_save[4][BLKSIZE]; /* psymodel is one ahead */
    double  energy[2][4][BLKSIZE];
    double  pe[2][4];
    double  thr[2][4][SBMAX_l];
    double  en[2][4][SBMAX_l];
    double  thr_s[2][4][3 * SBMAX_s];
    double  en_s[2][4][3 * SBMAX_s];
    double  ers_save[4];     /* psymodel is one ahead */
    double  ers[2][4];

    double  sfb[2][2][SBMAX_l];
    double  sfb_s[2][2][3 * SBMAX_s];
    double  LAMEsfb[2][2][SBMAX_l];
    double  LAMEsfb_s[2][2][3 * SBMAX_s];

    int     LAMEqss[2][2];
    int     qss[2][2];
    int     big_values[2][2];
    int     sub_gain[2][2][3];

    double  xfsf[2][2][SBMAX_l];
    double  xfsf_s[2][2][3 * SBMAX_s];

    int     over[2][2];
    double  tot_noise[2][2];
    double  max_noise[2][2];
    double  over_noise[2][2];
    int     over_SSD[2][2];
    int     blocktype[2][2];
    int     scalefac_scale[2][2];
    int     preflag[2][2];
    int     mpg123blocktype[2][2];
    int     mixed[2][2];
    int     mainbits[2][2];
    int     sfbits[2][2];
    int     LAMEmainbits[2][2];
    int     LAMEsfbits[2][2];
    int     framesize, stereo, js, ms_stereo, i_stereo, emph, bitrate, sampfreq, maindata;
    int     crc, padding;
    int     scfsi[2], mean_bits, resvsize;
    int     totbits;
};

typedef void (*lame_report_function)(const char *format, va_list ap);

typedef struct mpstr_tag {
    struct buf *head, *tail; /* buffer linked list pointers, tail points to oldest buffer */
    int     vbr_header;      /* 1 if valid Xing vbr header detected */
    int     num_frames;      /* set if vbr header present */
    int     enc_delay;       /* set if vbr header present */
    int     enc_padding;     /* set if vbr header present */
    /* header_parsed, side_parsed and data_parsed must be all set 1
       before the full frame has been parsed */
    int     header_parsed;   /* 1 = header of current frame has been parsed */
    int     side_parsed;     /* 1 = header of sideinfo of current frame has been parsed */
    int     data_parsed;
    int     free_format;     /* 1 = free format frame */
    int     old_free_format; /* 1 = last frame was free format */
    int     bsize;
    int     framesize;
    int     ssize;           /* number of bytes used for side information, including 2 bytes for CRC-16 if present */
    int     dsize;
    int     fsizeold;        /* size of previous frame, -1 for first */
    int     fsizeold_nopadding;
    struct frame fr;         /* holds the parameters decoded from the header */
    struct III_sideinfo sideinfo;
    unsigned char bsspace[2][MAXFRAMESIZE + 1024]; /* bit stream space used ???? */ /* MAXFRAMESIZE */
    real    hybrid_block[2][2][SBLIMIT * SSLIMIT];
    int     hybrid_blc[2];
    unsigned long header;
    int     bsnum;
    real    synth_buffs[2][2][0x110];
    int     synth_bo;
    int     sync_bitstream;  /* 1 = bitstream is yet to be synchronized */

    int     bitindex;
    unsigned char *wordpointer;
    plotting_data *pinfo;

    lame_report_function report_msg;
    lame_report_function report_dbg;
    lame_report_function report_err;
} MPSTR, *PMPSTR;

typedef struct {
  int header_parsed;   /* 1 if header was parsed and following data was
                          computed                                       */
  int stereo;          /* number of channels                             */
  int samplerate;      /* sample rate                                    */
  int bitrate;         /* bitrate                                        */
  int mode;            /* mp3 frame type                                 */
  int mode_ext;        /* mp3 frame type                                 */
  int framesize;       /* number of samples per mp3 frame                */

  /* this data is only computed if mpglib detects a Xing VBR header */
  unsigned long nsamp; /* number of samples in mp3 file.                 */
  int totalframes;     /* total number of frames in mp3 file             */

  /* this data is not currently computed by the mpglib routines */
  int framenum;        /* frames decoded counter                         */
} mp3data_struct;

typedef struct DecoderConfig
{
    int   mp3_delay;                /* to adjust the number of samples truncated during decode */
    int   mp3_delay_set;            /* user specified the value of the mp3 encoder delay to assume for decoding */
    int   disable_wav_header;
    mp3data_struct mp3input_data;
} DecoderConfig;

/* sample_t must be floating point, at least 32 bits */
typedef FLOAT sample_t;

/* these functions are used in get_audio.c */
static struct stDecoderProgress {
    int     last_mode_ext;
    int     frames_total;
    int     frame_ctr;
    int     framesize;
    unsigned long samples;
} global_decoder_progress;

struct stDecoderProgress;
typedef struct stDecoderProgress* DecoderProgress;
