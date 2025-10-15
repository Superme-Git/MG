#include "MP3.h"
#include <math.h>
#include <assert.h>
#include "huffman.h"
typedef std::vector<byte>* OutStream;
static int read_byte(void* bf, size_t len) {
	int blen = global.music_in.cur + len > global.music_in.size ? global.music_in.size - global.music_in.cur : len;
	if (blen > 0) {
		memcpy(bf, global.music_in.ptr + global.music_in.cur, blen); 
		global.music_in.cur += blen;
	}
	return blen;
} 
static int skip_byte(size_t len) {
	int blen = global.music_in.cur + len > global.music_in.size ? global.music_in.size - global.music_in.cur : len;
	if (blen > 0)
		global.music_in.cur += blen;
	return blen;
}
real    muls[27][64];
static int gd_are_hip_tables_layer1_initialized = 0;
static int gd_are_hip_tables_layer2_initialized = 0;

static unsigned char grp_3tab[32 * 3] = { 0, }; /* used: 27 */
static unsigned char grp_5tab[128 * 3] = { 0, }; /* used: 125 */
static unsigned char grp_9tab[1024 * 3] = { 0, }; /* used: 729 */
static size_t lenOfId3v2Tag(unsigned char const* buf)
{
    unsigned int b0 = buf[0] & 127;
    unsigned int b1 = buf[1] & 127;
    unsigned int b2 = buf[2] & 127;
    unsigned int b3 = buf[3] & 127;
    return (((((b0 << 7) + b1) << 7) + b2) << 7) + b3;
}
static int gd_are_hip_tables_layer3_initialized = 0;

static real ispow[8207];
static real aa_ca[8], aa_cs[8];
static real COS1[12][6];
static real win[4][36];
static real win1[4][36];
static real gainpow2[256 + 118 + 4];
static real COS9[9];
static real COS6_1, COS6_2;
static real tfcos36[9];
static real tfcos12[3];

struct bandInfoStruct {
    short   longIdx[23];
    short   longDiff[22];
    short   shortIdx[14];
    short   shortDiff[13];
};

static int longLimit[9][23];
static int shortLimit[9][14];

/* *INDENT-OFF* */

static const struct bandInfoStruct bandInfo[9] = { 

/* MPEG 1.0 */
 { {0,4,8,12,16,20,24,30,36,44,52,62,74, 90,110,134,162,196,238,288,342,418,576},
   {4,4,4,4,4,4,6,6,8, 8,10,12,16,20,24,28,34,42,50,54, 76,158},
   {0,4*3,8*3,12*3,16*3,22*3,30*3,40*3,52*3,66*3, 84*3,106*3,136*3,192*3},
   {4,4,4,4,6,8,10,12,14,18,22,30,56} } ,

 { {0,4,8,12,16,20,24,30,36,42,50,60,72, 88,106,128,156,190,230,276,330,384,576},
   {4,4,4,4,4,4,6,6,6, 8,10,12,16,18,22,28,34,40,46,54, 54,192},
   {0,4*3,8*3,12*3,16*3,22*3,28*3,38*3,50*3,64*3, 80*3,100*3,126*3,192*3},
   {4,4,4,4,6,6,10,12,14,16,20,26,66} } ,

 { {0,4,8,12,16,20,24,30,36,44,54,66,82,102,126,156,194,240,296,364,448,550,576} ,
   {4,4,4,4,4,4,6,6,8,10,12,16,20,24,30,38,46,56,68,84,102, 26} ,
   {0,4*3,8*3,12*3,16*3,22*3,30*3,42*3,58*3,78*3,104*3,138*3,180*3,192*3} ,
   {4,4,4,4,6,8,12,16,20,26,34,42,12} }  ,

/* MPEG 2.0 */
 { {0,6,12,18,24,30,36,44,54,66,80,96,116,140,168,200,238,284,336,396,464,522,576},
   {6,6,6,6,6,6,8,10,12,14,16,20,24,28,32,38,46,52,60,68,58,54 } ,
   {0,4*3,8*3,12*3,18*3,24*3,32*3,42*3,56*3,74*3,100*3,132*3,174*3,192*3} ,
   {4,4,4,6,6,8,10,14,18,26,32,42,18 } } ,
                                             /* docs: 332. mpg123: 330 */
 { {0,6,12,18,24,30,36,44,54,66,80,96,114,136,162,194,232,278,332,394,464,540,576},
   {6,6,6,6,6,6,8,10,12,14,16,18,22,26,32,38,46,54,62,70,76,36 } ,
   {0,4*3,8*3,12*3,18*3,26*3,36*3,48*3,62*3,80*3,104*3,136*3,180*3,192*3} ,
   {4,4,4,6,8,10,12,14,18,24,32,44,12 } } ,

 { {0,6,12,18,24,30,36,44,54,66,80,96,116,140,168,200,238,284,336,396,464,522,576},
   {6,6,6,6,6,6,8,10,12,14,16,20,24,28,32,38,46,52,60,68,58,54 },
   {0,4*3,8*3,12*3,18*3,26*3,36*3,48*3,62*3,80*3,104*3,134*3,174*3,192*3},
   {4,4,4,6,8,10,12,14,18,24,30,40,18 } } ,
/* MPEG 2.5 */
 { {0,6,12,18,24,30,36,44,54,66,80,96,116,140,168,200,238,284,336,396,464,522,576} ,
   {6,6,6,6,6,6,8,10,12,14,16,20,24,28,32,38,46,52,60,68,58,54},
   {0,12,24,36,54,78,108,144,186,240,312,402,522,576},
   {4,4,4,6,8,10,12,14,18,24,30,40,18} },
 { {0,6,12,18,24,30,36,44,54,66,80,96,116,140,168,200,238,284,336,396,464,522,576} ,
   {6,6,6,6,6,6,8,10,12,14,16,20,24,28,32,38,46,52,60,68,58,54},
   {0,12,24,36,54,78,108,144,186,240,312,402,522,576},
   {4,4,4,6,8,10,12,14,18,24,30,40,18} },
 { {0,12,24,36,48,60,72,88,108,132,160,192,232,280,336,400,476,566,568,570,572,574,576},
   {12,12,12,12,12,12,16,20,24,28,32,40,48,56,64,76,90,2,2,2,2,2},
   {0, 24, 48, 72,108,156,216,288,372,480,486,492,498,576},
   {8,8,8,12,16,20,24,28,36,2,2,2,26} } ,
};
real    decwin[512 + 32];
static real cos64[16], cos32[8], cos16[4], cos8[2], cos4[1];
real   *pnts[] = { cos64, cos32, cos16, cos8, cos4 };

/* *INDENT-OFF* */

static const double dewin[512] = {
   0.000000000,-0.000015259,-0.000015259,-0.000015259,
  -0.000015259,-0.000015259,-0.000015259,-0.000030518,
  -0.000030518,-0.000030518,-0.000030518,-0.000045776,
  -0.000045776,-0.000061035,-0.000061035,-0.000076294,
  -0.000076294,-0.000091553,-0.000106812,-0.000106812,
  -0.000122070,-0.000137329,-0.000152588,-0.000167847,
  -0.000198364,-0.000213623,-0.000244141,-0.000259399,
  -0.000289917,-0.000320435,-0.000366211,-0.000396729,
  -0.000442505,-0.000473022,-0.000534058,-0.000579834,
  -0.000625610,-0.000686646,-0.000747681,-0.000808716,
  -0.000885010,-0.000961304,-0.001037598,-0.001113892,
  -0.001205444,-0.001296997,-0.001388550,-0.001480103,
  -0.001586914,-0.001693726,-0.001785278,-0.001907349,
  -0.002014160,-0.002120972,-0.002243042,-0.002349854,
  -0.002456665,-0.002578735,-0.002685547,-0.002792358,
  -0.002899170,-0.002990723,-0.003082275,-0.003173828,
  -0.003250122,-0.003326416,-0.003387451,-0.003433228,
  -0.003463745,-0.003479004,-0.003479004,-0.003463745,
  -0.003417969,-0.003372192,-0.003280640,-0.003173828,
  -0.003051758,-0.002883911,-0.002700806,-0.002487183,
  -0.002227783,-0.001937866,-0.001617432,-0.001266479,
  -0.000869751,-0.000442505, 0.000030518, 0.000549316,
   0.001098633, 0.001693726, 0.002334595, 0.003005981,
   0.003723145, 0.004486084, 0.005294800, 0.006118774,
   0.007003784, 0.007919312, 0.008865356, 0.009841919,
   0.010848999, 0.011886597, 0.012939453, 0.014022827,
   0.015121460, 0.016235352, 0.017349243, 0.018463135,
   0.019577026, 0.020690918, 0.021789551, 0.022857666,
   0.023910522, 0.024932861, 0.025909424, 0.026840210,
   0.027725220, 0.028533936, 0.029281616, 0.029937744,
   0.030532837, 0.031005859, 0.031387329, 0.031661987,
   0.031814575, 0.031845093, 0.031738281, 0.031478882,
   0.031082153, 0.030517578, 0.029785156, 0.028884888,
   0.027801514, 0.026535034, 0.025085449, 0.023422241,
   0.021575928, 0.019531250, 0.017257690, 0.014801025,
   0.012115479, 0.009231567, 0.006134033, 0.002822876,
  -0.000686646,-0.004394531,-0.008316040,-0.012420654,
  -0.016708374,-0.021179199,-0.025817871,-0.030609131,
  -0.035552979,-0.040634155,-0.045837402,-0.051132202,
  -0.056533813,-0.061996460,-0.067520142,-0.073059082,
  -0.078628540,-0.084182739,-0.089706421,-0.095169067,
  -0.100540161,-0.105819702,-0.110946655,-0.115921021,
  -0.120697021,-0.125259399,-0.129562378,-0.133590698,
  -0.137298584,-0.140670776,-0.143676758,-0.146255493,
  -0.148422241,-0.150115967,-0.151306152,-0.151962280,
  -0.152069092,-0.151596069,-0.150497437,-0.148773193,
  -0.146362305,-0.143264771,-0.139450073,-0.134887695,
  -0.129577637,-0.123474121,-0.116577148,-0.108856201,
  -0.100311279,-0.090927124,-0.080688477,-0.069595337,
  -0.057617187,-0.044784546,-0.031082153,-0.016510010,
  -0.001068115, 0.015228271, 0.032379150, 0.050354004,
   0.069168091, 0.088775635, 0.109161377, 0.130310059,
   0.152206421, 0.174789429, 0.198059082, 0.221984863,
   0.246505737, 0.271591187, 0.297210693, 0.323318481,
   0.349868774, 0.376800537, 0.404083252, 0.431655884,
   0.459472656, 0.487472534, 0.515609741, 0.543823242,
   0.572036743, 0.600219727, 0.628295898, 0.656219482,
   0.683914185, 0.711318970, 0.738372803, 0.765029907,
   0.791213989, 0.816864014, 0.841949463, 0.866363525,
   0.890090942, 0.913055420, 0.935195923, 0.956481934,
   0.976852417, 0.996246338, 1.014617920, 1.031936646,
   1.048156738, 1.063217163, 1.077117920, 1.089782715,
   1.101211548, 1.111373901, 1.120223999, 1.127746582,
   1.133926392, 1.138763428, 1.142211914, 1.144287109,
   1.144989014
};
const int samplerate_table[3][4] = {
    {22050, 24000, 16000, -1}, /* MPEG 2 */
    {44100, 48000, 32000, -1}, /* MPEG 1 */
    {11025, 12000, 8000, -1}, /* MPEG 2.5 */
};
/* *INDENT-ON* */

static int mapbuf0[9][152];
static int mapbuf1[9][156];
static int mapbuf2[9][44];
static int *map[9][3];
static int *mapend[9][3];

static unsigned int n_slen2[512]; /* MPEG 2.0 slen for 'normal' mode */
static unsigned int i_slen2[256]; /* MPEG 2.0 slen for intensity stereo */

static real tan1_1[16], tan2_1[16], tan1_2[16], tan2_2[16];
static real pow1_1[2][16], pow2_1[2][16], pow1_2[2][16], pow2_2[2][16];

const int tabsel_123 [2] [3] [16] = {
   { {0,32,64,96,128,160,192,224,256,288,320,352,384,416,448,},
     {0,32,48,56, 64, 80, 96,112,128,160,192,224,256,320,384,},
     {0,32,40,48, 56, 64, 80, 96,112,128,160,192,224,256,320,} },

   { {0,32,48,56,64,80,96,112,128,144,160,176,192,224,256,},
     {0,8,16,24,32,40,48,56,64,80,96,112,128,144,160,},
     {0,8,16,24,32,40,48,56,64,80,96,112,128,144,160,} }
};

const long freqs[9] = { 44100, 48000, 32000,
                        22050, 24000, 16000,
                        11025, 12000,  8000 };
const int bitrate_table[3][16] = {
    {0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, -1}, /* MPEG 2 */
    {0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, -1}, /* MPEG 1 */
    {0, 8, 16, 24, 32, 40, 48, 56, 64, -1, -1, -1, -1, -1, -1, -1}, /* MPEG 2.5 */
};
/* *INDENT-OFF* */
static const int pretab1 [22] = {0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,2,2,3,3,3,2,0}; /* char enough ? */
static const int pretab2 [22] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
void ExitMP3(PMPSTR mp)
{
    struct buf *b, *bn;

    b = mp->tail;
    while (b) {
        free(b->pnt);
        bn = b->next;
        free(b);
        b = bn;
    }
}

int hip_decode_exit(hip_t hip)
{
    if (hip) {
        ExitMP3(hip);
        free(hip);
    }
    return 0;
}


void hip_init_tables_layer1(void)
{
    if (gd_are_hip_tables_layer1_initialized) {
        return;
    }
    gd_are_hip_tables_layer1_initialized = 1;
}




void hip_init_tables_layer2(void)
{
    static const double mulmul[27] = {
        0.0, -2.0 / 3.0, 2.0 / 3.0,
        2.0 / 7.0, 2.0 / 15.0, 2.0 / 31.0, 2.0 / 63.0, 2.0 / 127.0, 2.0 / 255.0,
        2.0 / 511.0, 2.0 / 1023.0, 2.0 / 2047.0, 2.0 / 4095.0, 2.0 / 8191.0,
        2.0 / 16383.0, 2.0 / 32767.0, 2.0 / 65535.0,
        -4.0 / 5.0, -2.0 / 5.0, 2.0 / 5.0, 4.0 / 5.0,
        -8.0 / 9.0, -4.0 / 9.0, -2.0 / 9.0, 2.0 / 9.0, 4.0 / 9.0, 8.0 / 9.0
    };
    static const unsigned char base[3][9] = {
        {1, 0, 2,},
        {17, 18, 0, 19, 20,},
        {21, 1, 22, 23, 0, 24, 25, 2, 26}
    };
    int     i, j, k, l, len;
    real   *table;
    static const int tablen[3] = { 3, 5, 9 };
    static unsigned char *itable, *tables[3] = { grp_3tab, grp_5tab, grp_9tab };

    if (gd_are_hip_tables_layer2_initialized) {
        return;
    }
    gd_are_hip_tables_layer2_initialized = 1;

    for (i = 0; i < 3; i++) {
        itable = tables[i];
        len = tablen[i];
        for (j = 0; j < len; j++)
            for (k = 0; k < len; k++)
                for (l = 0; l < len; l++) {
                    *itable++ = base[i][l];
                    *itable++ = base[i][k];
                    *itable++ = base[i][j];
                }
    }

    for (k = 0; k < 27; k++) {
        double  m = mulmul[k];
        table = muls[k];
        for (j = 3, i = 0; i < 63; i++, j--)
            *table++ = (real) (m * pow(2.0, (double) j / 3.0));
        *table++ = 0.0;
    }
}



static unsigned int  get1bit(PMPSTR mp)
{
    unsigned char rval;
    rval = *mp->wordpointer << mp->bitindex;

    mp->bitindex++;
    mp->wordpointer += (mp->bitindex >> 3);
    mp->bitindex &= 7;

    return rval >> 7;
}




/* 
 * init tables for layer-3 
 */
void
hip_init_tables_layer3(void)
{
    int     i, j, k;

    if (gd_are_hip_tables_layer3_initialized) {
        return;
    }
    gd_are_hip_tables_layer3_initialized = 1;

    for (i = -256; i < 118 + 4; i++)
        gainpow2[i + 256] = pow((double) 2.0, -0.25 * (double) (i + 210));

    for (i = 0; i < 8207; i++)
        ispow[i] = pow((double) i, (double) 4.0 / 3.0);

    for (i = 0; i < 8; i++) {
        static const double Ci[8] = { -0.6, -0.535, -0.33, -0.185, -0.095, -0.041, -0.0142, -0.0037 };
        double  sq = sqrt(1.0 + Ci[i] * Ci[i]);
        aa_cs[i] = 1.0 / sq;
        aa_ca[i] = Ci[i] / sq;
    }

    for (i = 0; i < 18; i++) {
        win[0][i] = win[1][i] =
            0.5 * sin(M_PI / 72.0 * (double) (2 * (i + 0) + 1)) / cos(M_PI *
                                                                      (double) (2 * (i + 0) +
                                                                                19) / 72.0);
        win[0][i + 18] = win[3][i + 18] =
            0.5 * sin(M_PI / 72.0 * (double) (2 * (i + 18) + 1)) / cos(M_PI *
                                                                       (double) (2 * (i + 18) +
                                                                                 19) / 72.0);
    }
    for (i = 0; i < 6; i++) {
        win[1][i + 18] = 0.5 / cos(M_PI * (double) (2 * (i + 18) + 19) / 72.0);
        win[3][i + 12] = 0.5 / cos(M_PI * (double) (2 * (i + 12) + 19) / 72.0);
        win[1][i + 24] =
            0.5 * sin(M_PI / 24.0 * (double) (2 * i + 13)) / cos(M_PI *
                                                                 (double) (2 * (i + 24) +
                                                                           19) / 72.0);
        win[1][i + 30] = win[3][i] = 0.0;
        win[3][i + 6] =
            0.5 * sin(M_PI / 24.0 * (double) (2 * i + 1)) / cos(M_PI * (double) (2 * (i + 6) + 19) /
                                                                72.0);
    }

    for (i = 0; i < 9; i++)
        COS9[i] = cos(M_PI / 18.0 * (double) i);

    for (i = 0; i < 9; i++)
        tfcos36[i] = 0.5 / cos(M_PI * (double) (i * 2 + 1) / 36.0);
    for (i = 0; i < 3; i++)
        tfcos12[i] = 0.5 / cos(M_PI * (double) (i * 2 + 1) / 12.0);

    COS6_1 = cos(M_PI / 6.0 * (double) 1);
    COS6_2 = cos(M_PI / 6.0 * (double) 2);

    for (i = 0; i < 12; i++) {
        win[2][i] =
            0.5 * sin(M_PI / 24.0 * (double) (2 * i + 1)) / cos(M_PI * (double) (2 * i + 7) / 24.0);
        for (j = 0; j < 6; j++)
            COS1[i][j] = cos(M_PI / 24.0 * (double) ((2 * i + 7) * (2 * j + 1)));
    }

    for (j = 0; j < 4; j++) {
        static int const len[4] = { 36, 36, 12, 36 };
        for (i = 0; i < len[j]; i += 2)
            win1[j][i] = +win[j][i];
        for (i = 1; i < len[j]; i += 2)
            win1[j][i] = -win[j][i];
    }

    for (i = 0; i < 16; i++) {
        double  t = tan((double) i * M_PI / 12.0);
        tan1_1[i] = t / (1.0 + t);
        tan2_1[i] = 1.0 / (1.0 + t);
        tan1_2[i] = M_SQRT2 * t / (1.0 + t);
        tan2_2[i] = M_SQRT2 / (1.0 + t);

        for (j = 0; j < 2; j++) {
            double  base = pow(2.0, -0.25 * (j + 1.0));
            double  p1 = 1.0, p2 = 1.0;
            if (i > 0) {
                if (i & 1)
                    p1 = pow(base, (i + 1.0) * 0.5);
                else
                    p2 = pow(base, i * 0.5);
            }
            pow1_1[j][i] = p1;
            pow2_1[j][i] = p2;
            pow1_2[j][i] = M_SQRT2 * p1;
            pow2_2[j][i] = M_SQRT2 * p2;
        }
    }

    for (j = 0; j < 9; j++) {
        struct bandInfoStruct const *bi = (struct bandInfoStruct const *) &bandInfo[j];
        int    *mp;
        int     cb, lwin;
        short const *bdf;

        mp = map[j][0] = mapbuf0[j];
        bdf = bi->longDiff;
        for (i = 0, cb = 0; cb < 8; cb++, i += *bdf++) {
            *mp++ = (*bdf) >> 1;
            *mp++ = i;
            *mp++ = 3;
            *mp++ = cb;
        }
        bdf = bi->shortDiff + 3;
        for (cb = 3; cb < 13; cb++) {
            int     l = (*bdf++) >> 1;
            for (lwin = 0; lwin < 3; lwin++) {
                *mp++ = l;
                *mp++ = i + lwin;
                *mp++ = lwin;
                *mp++ = cb;
            }
            i += 6 * l;
        }
        mapend[j][0] = mp;

        mp = map[j][1] = mapbuf1[j];
        bdf = bi->shortDiff + 0;
        for (i = 0, cb = 0; cb < 13; cb++) {
            int     l = (*bdf++) >> 1;
            for (lwin = 0; lwin < 3; lwin++) {
                *mp++ = l;
                *mp++ = i + lwin;
                *mp++ = lwin;
                *mp++ = cb;
            }
            i += 6 * l;
        }
        mapend[j][1] = mp;

        mp = map[j][2] = mapbuf2[j];
        bdf = bi->longDiff;
        for (cb = 0; cb < 22; cb++) {
            *mp++ = (*bdf++) >> 1;
            *mp++ = cb;
        }
        mapend[j][2] = mp;

    }

    for (j = 0; j < 9; j++) {
        for (i = 0; i < 23; i++) {
            longLimit[j][i] = (bandInfo[j].longIdx[i] - 1 + 8) / 18 + 1;
            if (longLimit[j][i] > SBLIMIT)
                longLimit[j][i] = SBLIMIT;
        }
        for (i = 0; i < 14; i++) {
            shortLimit[j][i] = (bandInfo[j].shortIdx[i] - 1) / 18 + 1;
            if (shortLimit[j][i] > SBLIMIT)
                shortLimit[j][i] = SBLIMIT;
        }
    }

    for (i = 0; i < 5; i++) {
        for (j = 0; j < 6; j++) {
            for (k = 0; k < 6; k++) {
                int     n = k + j * 6 + i * 36;
                i_slen2[n] = i | (j << 3) | (k << 6) | (3 << 12);
            }
        }
    }
    for (i = 0; i < 4; i++) {
        for (j = 0; j < 4; j++) {
            for (k = 0; k < 4; k++) {
                int     n = k + j * 4 + i * 16;
                i_slen2[n + 180] = i | (j << 3) | (k << 6) | (4 << 12);
            }
        }
    }
    for (i = 0; i < 4; i++) {
        for (j = 0; j < 3; j++) {
            int     n = j + i * 3;
            i_slen2[n + 244] = i | (j << 3) | (5 << 12);
            n_slen2[n + 500] = i | (j << 3) | (2 << 12) | (1 << 15);
        }
    }

    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            for (k = 0; k < 4; k++) {
                int     l;
                for (l = 0; l < 4; l++) {
                    int     n = l + k * 4 + j * 16 + i * 80;
                    n_slen2[n] = i | (j << 3) | (k << 6) | (l << 9) | (0 << 12);
                }
            }
        }
    }
    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            for (k = 0; k < 4; k++) {
                int     n = k + j * 4 + i * 20;
                n_slen2[n + 400] = i | (j << 3) | (k << 6) | (1 << 12);
            }
        }
    }
}
void lame_report_def(const char *format, va_list args)
{
    //(void) vfprintf(stderr, format, args);
    //fflush(stderr); /* an debug function should flush immediately */
}
void make_decode_tables(long scaleval)
{
    int     i, j, k, kr, divv;
    real   *table, *costab;


    for (i = 0; i < 5; i++) {
        kr = 0x10 >> i;
        divv = 0x40 >> i;
        costab = pnts[i];
        for (k = 0; k < kr; k++)
            costab[k] = (real) (1.0 / (2.0 * cos(M_PI * ((double) k * 2.0 + 1.0) / (double) divv)));
    }

    table = decwin;
    scaleval = -scaleval;
    for (i = 0, j = 0; i < 256; i++, j++, table += 32) {
        if (table < decwin + 512 + 16)
            table[16] = table[0] = (real) (dewin[j] * scaleval);
        if (i % 32 == 31)
            table -= 1023;
        if (i % 64 == 63)
            scaleval = -scaleval;
    }

    for ( /* i=256 */ ; i < 512; i++, j--, table += 32) {
        if (table < decwin + 512 + 16)
            table[16] = table[0] = (real) (dewin[j] * scaleval);
        if (i % 32 == 31)
            table -= 1023;
        if (i % 64 == 63)
            scaleval = -scaleval;
    }
}

int InitMP3(PMPSTR mp)
{
    hip_init_tables_layer1();
    hip_init_tables_layer2();
    hip_init_tables_layer3();

    memset(mp, 0, sizeof(MPSTR));

    mp->framesize = 0;
    mp->num_frames = 0;
    mp->enc_delay = -1;
    mp->enc_padding = -1;
    mp->vbr_header = 0;
    mp->header_parsed = 0;
    mp->side_parsed = 0;
    mp->data_parsed = 0;
    mp->free_format = 0;
    mp->old_free_format = 0;
    mp->ssize = 0;
    mp->dsize = 0;
    mp->fsizeold = -1;
    mp->bsize = 0;
    mp->head = mp->tail = NULL;
    mp->fr.single = -1;
    mp->bsnum = 0;
    mp->wordpointer = mp->bsspace[mp->bsnum] + 512;
    mp->bitindex = 0;
    mp->synth_bo = 1;
    mp->sync_bitstream = 1;

    mp->report_dbg = &lame_report_def;
    mp->report_err = &lame_report_def;
    mp->report_msg = &lame_report_def;

    make_decode_tables(32767);

    return 1;
}

hip_t hip_decode_init(void)
{
    hip_t hip = (hip_t)calloc(1, sizeof(hip_global_flags));
    InitMP3(hip);
    return hip;
}
static void
initPcmBuffer(PcmBuffer * b, int w)
{
    b->ch[0] = 0;
    b->ch[1] = 0;
    b->w = w;
    b->n = 0;
    b->u = 0;
    b->skip_start = 0;
    b->skip_end = 0;
}
//int
//is_lame_internal_flags_valid(const lame_internal_flags * gfc)
//{
//    if (gfc == NULL)
//        return 0;
//    if (gfc->class_id != LAME_ID)
//        return 0;
//    return 1;
//}
/* Encoder delay. */
int
lame_get_encoder_delay(const lame_global_flags * gfp)
{
    //if (is_lame_global_flags_valid(gfp)) {
    //    lame_internal_flags const *const gfc = gfp->internal_flags;
    //    if (is_lame_internal_flags_valid(gfc)) {
    //        return gfc->ov_enc.encoder_delay;
    //    }
    //}
    return 0;
}

static void
setSkipStartAndEnd(lame_t gfp, int enc_delay, int enc_padding)
{
    int     skip_start = 0, skip_end = 0;

    if (global_decoder.mp3_delay_set)
        skip_start = global_decoder.mp3_delay;

    switch (global_reader.input_format) {
    case sf_mp123:
        break;

    case sf_mp3:
        if (skip_start == 0) {
            if (enc_delay > -1 || enc_padding > -1) {
                if (enc_delay > -1)
                    skip_start = enc_delay + 528 + 1;
                if (enc_padding > -1)
                    skip_end = enc_padding - (528 + 1);
            }
            else
                skip_start = lame_get_encoder_delay(gfp) + 528 + 1;
        }
        else {
            /* user specified a value of skip. just add for decoder */
            skip_start += 528 + 1; /* mp3 decoder has a 528 sample delay, plus user supplied "skip" */
        }
        break;
    case sf_mp2:
        skip_start += 240 + 1;
        break;
    case sf_mp1:
        skip_start += 240 + 1;
        break;
    case sf_raw:
        skip_start += 0; /* other formats have no delay *//* is += 0 not better ??? */
        break;
    case sf_wave:
        skip_start += 0; /* other formats have no delay *//* is += 0 not better ??? */
        break;
    case sf_aiff:
        skip_start += 0; /* other formats have no delay *//* is += 0 not better ??? */
        break;
    default:
        skip_start += 0; /* other formats have no delay *//* is += 0 not better ??? */
        break;
    }
    skip_start = skip_start < 0 ? 0 : skip_start;
    skip_end = skip_end < 0 ? 0 : skip_end;
    global. pcm16.skip_start = global.pcm32.skip_start = skip_start;
    global. pcm16.skip_end = global.pcm32.skip_end = skip_end;
}

static int check_aid(const unsigned char *header)
{
    return 0 == memcmp(header, "AiD\1", 4);
}
static int is_syncword_mp123(const void *const headerptr)
{
    const unsigned char *const p = (const unsigned char *const)headerptr;
    static const char abl2[16] = { 0, 7, 7, 7, 0, 7, 0, 0, 0, 0, 0, 8, 8, 8, 8, 8 };

    if ((p[0] & 0xFF) != 0xFF)
        return 0;       /* first 8 bits must be '1' */
    if ((p[1] & 0xE0) != 0xE0)
        return 0;       /* next 3 bits are also */
    if ((p[1] & 0x18) == 0x08)
        return 0;       /* no MPEG-1, -2 or -2.5 */
    switch (p[1] & 0x06) {
    default:
    case 0x00:         /* illegal Layer */
        return 0;

    case 0x02:         /* Layer3 */
        if (global_reader.input_format != sf_mp3 && global_reader.input_format != sf_mp123) {
            return 0;
        }
        global_reader.input_format = sf_mp3;
        break;

    case 0x04:         /* Layer2 */
        if (global_reader.input_format != sf_mp2 && global_reader.input_format != sf_mp123) {
            return 0;
        }
        global_reader.input_format = sf_mp2;
        break;

    case 0x06:         /* Layer1 */
        if (global_reader.input_format != sf_mp1 && global_reader.input_format != sf_mp123) {
            return 0;
        }
        global_reader.input_format = sf_mp1;
        break;
    }
    if ((p[1] & 0x06) == 0x00)
        return 0;       /* no Layer I, II and III */
    if ((p[2] & 0xF0) == 0xF0)
        return 0;       /* bad bitrate */
    if ((p[2] & 0x0C) == 0x0C)
        return 0;       /* no sample frequency with (32,44.1,48)/(1,2,4)     */
    if ((p[1] & 0x18) == 0x18 && (p[1] & 0x06) == 0x04 && abl2[p[2] >> 4] & (1 << (p[3] >> 6)))
        return 0;
    if ((p[3] & 3) == 2)
        return 0;       /* reserved enphasis mode */
    return 1;
}
static struct buf *addbuf(PMPSTR mp, unsigned char *buf, int size)
{
    struct buf *nbuf;

    nbuf = (struct buf *) malloc(sizeof(struct buf));
    if (!nbuf) {
    //    lame_report_fnc(mp->report_err, "hip: addbuf() Out of memory!\n");
        return NULL;
    }
    nbuf->pnt = (unsigned char *) malloc((size_t) size);
    if (!nbuf->pnt) {
        free(nbuf);
        return NULL;
    }
    nbuf->size = size;
    memcpy(nbuf->pnt, buf, (size_t) size);
    nbuf->next = NULL;
    nbuf->prev = mp->head;
    nbuf->pos = 0;

    if (!mp->tail) {
        mp->tail = nbuf;
    }
    else {
        mp->head->next = nbuf;
    }

    mp->head = nbuf;
    mp->bsize += size;

    return nbuf;
}
int
head_check(unsigned long head, int check_layer)
{
    /*
       look for a valid header.  
       if check_layer > 0, then require that
       nLayer = check_layer.  
     */

    /* bits 13-14 = layer 3 */
    int     nLayer = 4 - ((head >> 17) & 3);

    if ((head & 0xffe00000) != 0xffe00000) {
        /* syncword */
        return FALSE;
    }

    if (nLayer == 4)
        return FALSE;

    if (check_layer > 0 && nLayer != check_layer)
        return FALSE;

    if (((head >> 12) & 0xf) == 0xf) {
        /* bits 16,17,18,19 = 1111  invalid bitrate */
        return FALSE;
    }
    if (((head >> 10) & 0x3) == 0x3) {
        /* bits 20,21 = 11  invalid sampling freq */
        return FALSE;
    }
    if ((head & 0x3) == 0x2)
        /* invalid emphasis */
        return FALSE;
    return TRUE;
}

static int
sync_buffer(PMPSTR mp, int free_match)
{
    /* traverse mp structure without modifying pointers, looking
     * for a frame valid header.
     * if free_format, valid header must also have the same
     * samplerate.   
     * return number of bytes in mp, before the header
     * return -1 if header is not found
     */
    unsigned int b[4] = { 0, 0, 0, 0 };
    int     i, h, pos;
    struct buf *buf = mp->tail;
    if (!buf)
        return -1;

    pos = buf->pos;
    for (i = 0; i < mp->bsize; i++) {
        /* get 4 bytes */

        b[0] = b[1];
        b[1] = b[2];
        b[2] = b[3];
        while (pos >= buf->size) {
            buf = buf->next;
            pos = buf->pos;
            if (!buf) {
                return -1;
                /* not enough data to read 4 bytes */
            }
        }
        b[3] = buf->pnt[pos];
        ++pos;

        if (i >= 3) {
            struct frame *fr = &mp->fr;
            unsigned long head;

            head = b[0];
            head <<= 8;
            head |= b[1];
            head <<= 8;
            head |= b[2];
            head <<= 8;
            head |= b[3];
            h = head_check(head, fr->lay);

            if (h && free_match) {
                /* just to be even more thorough, match the sample rate */
                int     mode, stereo, sampling_frequency, mpeg25, lsf;

                if (head & (1 << 20)) {
                    lsf = (head & (1 << 19)) ? 0x0 : 0x1;
                    mpeg25 = 0;
                }
                else {
                    lsf = 1;
                    mpeg25 = 1;
                }

                mode = ((head >> 6) & 0x3);
                stereo = (mode == MPG_MD_MONO) ? 1 : 2;

                if (mpeg25)
                    sampling_frequency = 6 + ((head >> 10) & 0x3);
                else
                    sampling_frequency = ((head >> 10) & 0x3) + (lsf * 3);
                h = ((stereo == fr->stereo) && (lsf == fr->lsf) && (mpeg25 == fr->mpeg25) &&
                     (sampling_frequency == fr->sampling_frequency));
            }

            if (h) {
                return i - 3;
            }
        }
    }
    return -1;
}
typedef struct {
    int     h_id;            /* from MPEG header, 0=MPEG2, 1=MPEG1 */
    int     samprate;        /* determined from MPEG header */
    int     flags;           /* from Vbr header data */
    int     frames;          /* total bit stream frames from Vbr header data */
    int     bytes;           /* total bit stream bytes from Vbr header data */
    int     vbr_scale;       /* encoded vbr scale from Vbr header data */
    unsigned char toc[NUMTOCENTRIES]; /* may be NULL if toc not desired */
    int     headersize;      /* size of VBR header, in bytes */
    int     enc_delay;       /* encoder delay */
    int     enc_padding;     /* encoder paddign added at end of stream */
} VBRTAGDATA;
static const char VBRTag0[] = { "Xing" };
static const char VBRTag1[] = { "Info" };
/* check for magic strings*/
static int
IsVbrTag(const unsigned char *buf)
{
    int     isTag0, isTag1;

    isTag0 = ((buf[0] == VBRTag0[0]) && (buf[1] == VBRTag0[1]) && (buf[2] == VBRTag0[2])
              && (buf[3] == VBRTag0[3]));
    isTag1 = ((buf[0] == VBRTag1[0]) && (buf[1] == VBRTag1[1]) && (buf[2] == VBRTag1[2])
              && (buf[3] == VBRTag1[3]));

    return (isTag0 || isTag1);
}
static int
ExtractI4(const unsigned char *buf)
{
    int     x;
    /* big endian extract */
    x = buf[0];
    x <<= 8;
    x |= buf[1];
    x <<= 8;
    x |= buf[2];
    x <<= 8;
    x |= buf[3];
    return x;
}
int
GetVbrTag(VBRTAGDATA * pTagData, const unsigned char *buf)
{
    int     i, head_flags;
    int     h_bitrate, h_id, h_mode, h_sr_index, h_layer;
    int     enc_delay, enc_padding;

    /* get Vbr header data */
    pTagData->flags = 0;

    /* get selected MPEG header data */
    h_layer = (buf[1] >> 1) & 3;
    if ( h_layer != 0x01 ) {
        /* the following code assumes Layer-3, so give up here */
        return 0;
    }
    h_id = (buf[1] >> 3) & 1;
    h_sr_index = (buf[2] >> 2) & 3;
    h_mode = (buf[3] >> 6) & 3;
    h_bitrate = ((buf[2] >> 4) & 0xf);
    h_bitrate = bitrate_table[h_id][h_bitrate];

    /* check for FFE syncword */
    if ((buf[1] >> 4) == 0xE)
        pTagData->samprate = samplerate_table[2][h_sr_index];
    else
        pTagData->samprate = samplerate_table[h_id][h_sr_index];
    /* if( h_id == 0 ) */
    /*  pTagData->samprate >>= 1; */



    /*  determine offset of header */
    if (h_id) {
        /* mpeg1 */
        if (h_mode != 3)
            buf += (32 + 4);
        else
            buf += (17 + 4);
    }
    else {
        /* mpeg2 */
        if (h_mode != 3)
            buf += (17 + 4);
        else
            buf += (9 + 4);
    }

    if (!IsVbrTag(buf))
        return 0;

    buf += 4;

    pTagData->h_id = h_id;

    head_flags = pTagData->flags = ExtractI4(buf);
    buf += 4;           /* get flags */

    if (head_flags & FRAMES_FLAG) {
        pTagData->frames = ExtractI4(buf);
        buf += 4;
    }

    if (head_flags & BYTES_FLAG) {
        pTagData->bytes = ExtractI4(buf);
        buf += 4;
    }

    if (head_flags & TOC_FLAG) {
        if (pTagData->toc != NULL) {
            for (i = 0; i < NUMTOCENTRIES; i++)
                pTagData->toc[i] = buf[i];
        }
        buf += NUMTOCENTRIES;
    }

    pTagData->vbr_scale = -1;

    if (head_flags & VBR_SCALE_FLAG) {
        pTagData->vbr_scale = ExtractI4(buf);
        buf += 4;
    }

    pTagData->headersize = ((h_id + 1) * 72000 * h_bitrate) / pTagData->samprate;

    buf += 21;
    enc_delay = buf[0] << 4;
    enc_delay += buf[1] >> 4;
    enc_padding = (buf[1] & 0x0F) << 8;
    enc_padding += buf[2];
    /* check for reasonable values (this may be an old Xing header, */
    /* not a INFO tag) */
    if (enc_delay < 0 || enc_delay > 3000)
        enc_delay = -1;
    if (enc_padding < 0 || enc_padding > 3000)
        enc_padding = -1;

    pTagData->enc_delay = enc_delay;
    pTagData->enc_padding = enc_padding;

#ifdef DEBUG_VBRTAG
    fprintf(stderr, "\n\n********************* VBR TAG INFO *****************\n");
    fprintf(stderr, "tag         :%s\n", VBRTag);
    fprintf(stderr, "head_flags  :%d\n", head_flags);
    fprintf(stderr, "bytes       :%d\n", pTagData->bytes);
    fprintf(stderr, "frames      :%d\n", pTagData->frames);
    fprintf(stderr, "VBR Scale   :%d\n", pTagData->vbr_scale);
    fprintf(stderr, "enc_delay  = %i \n", enc_delay);
    fprintf(stderr, "enc_padding= %i \n", enc_padding);
    fprintf(stderr, "toc:\n");
    if (pTagData->toc != NULL) {
        for (i = 0; i < NUMTOCENTRIES; i++) {
            if ((i % 10) == 0)
                fprintf(stderr, "\n");
            fprintf(stderr, " %3d", (int) (pTagData->toc[i]));
        }
    }
    fprintf(stderr, "\n***************** END OF VBR TAG INFO ***************\n");
#endif
    return 1;           /* success */
}
/*
traverse mp data structure without changing it
(just like sync_buffer)
pull out Xing bytes
call vbr header check code from LAME
if we find a header, parse it and also compute the VBR header size
if no header, do nothing.

bytes = number of bytes before MPEG header.  skip this many bytes
before starting to read
return value: number of bytes in VBR header, including syncword
*/
static int
check_vbr_header(PMPSTR mp, int bytes)
{
    int     i, pos;
    struct buf *buf = mp->tail;
    unsigned char xing[XING_HEADER_SIZE];
    VBRTAGDATA pTagData;

    pos = buf->pos;
    /* skip to valid header */
    for (i = 0; i < bytes; ++i) {
        while (pos >= buf->size) {
            buf = buf->next;
            if (!buf)
                return -1; /* fatal error */
            pos = buf->pos;
        }
        ++pos;
    }
    /* now read header */
    for (i = 0; i < XING_HEADER_SIZE; ++i) {
        while (pos >= buf->size) {
            buf = buf->next;
            if (!buf)
                return -1; /* fatal error */
            pos = buf->pos;
        }
        xing[i] = buf->pnt[pos];
        ++pos;
    }

    /* check first bytes for Xing header */
    mp->vbr_header = GetVbrTag(&pTagData, xing);
    if (mp->vbr_header) {
        mp->num_frames = pTagData.frames;
        mp->enc_delay = pTagData.enc_delay;
        mp->enc_padding = pTagData.enc_padding;

        /* lame_report_fnc(mp->report_msg,"hip: delays: %i %i \n",mp->enc_delay,mp->enc_padding); */
        /* lame_report_fnc(mp->report_msg,"hip: Xing VBR header dectected.  MP3 file has %i frames\n", pTagData.frames); */
        if (pTagData.headersize < 1)
            return 1;
        return pTagData.headersize;
    }
    return 0;
}

void
remove_buf(PMPSTR mp)
{
    struct buf *buf = mp->tail;

    mp->tail = buf->next;
    if (mp->tail)
        mp->tail->prev = NULL;
    else {
        mp->tail = mp->head = NULL;
    }

    free(buf->pnt);
    free(buf);

}

static int
read_buf_byte(PMPSTR mp)
{
    unsigned int b;

    int     pos;


    pos = mp->tail->pos;
    while (pos >= mp->tail->size) {
        remove_buf(mp);
        if (!mp->tail) {
     //       lame_report_fnc(mp->report_err, "hip: Fatal error! tried to read past mp buffer\n");
            exit(1);
        }
        pos = mp->tail->pos;
    }

    b = mp->tail->pnt[pos];
    mp->bsize--;
    mp->tail->pos++;


    return b;
}

static void
copy_mp(PMPSTR mp, int size, unsigned char *ptr)
{
    int     len = 0;

    while (len < size && mp->tail) {
        int     nlen;
        int     blen = mp->tail->size - mp->tail->pos;
        if ((size - len) <= blen) {
            nlen = size - len;
        }
        else {
            nlen = blen;
        }
        memcpy(ptr + len, mp->tail->pnt + mp->tail->pos, (size_t) nlen);
        len += nlen;
        mp->tail->pos += nlen;
        mp->bsize -= nlen;
        if (mp->tail->pos == mp->tail->size) {
            remove_buf(mp);
        }
    }
}
static void
read_head(PMPSTR mp)
{
    unsigned long head;

    head = read_buf_byte(mp);
    head <<= 8;
    head |= read_buf_byte(mp);
    head <<= 8;
    head |= read_buf_byte(mp);
    head <<= 8;
    head |= read_buf_byte(mp);

    mp->header = head;
}
/*
 * decode a header and write the information
 * into the frame structure
 */
int
decode_header(PMPSTR mp, struct frame *fr, unsigned long newhead)
{


    if (newhead & (1 << 20)) {
        fr->lsf = (newhead & (1 << 19)) ? 0x0 : 0x1;
        fr->mpeg25 = 0;
    }
    else {
        fr->lsf = 1;
        fr->mpeg25 = 1;
    }


    fr->lay = 4 - ((newhead >> 17) & 3);
    if (((newhead >> 10) & 0x3) == 0x3) {
        lame_report_fnc(mp->report_err, "Stream error\n");
        exit(1);
    }
    if (fr->mpeg25) {
        fr->sampling_frequency = 6 + ((newhead >> 10) & 0x3);
    }
    else
        fr->sampling_frequency = ((newhead >> 10) & 0x3) + (fr->lsf * 3);

    fr->error_protection = ((newhead >> 16) & 0x1) ^ 0x1;

    if (fr->mpeg25)     /* allow Bitrate change for 2.5 ... */
        fr->bitrate_index = ((newhead >> 12) & 0xf);

    fr->bitrate_index = ((newhead >> 12) & 0xf);
    fr->padding = ((newhead >> 9) & 0x1);
    fr->extension = ((newhead >> 8) & 0x1);
    fr->mode = ((newhead >> 6) & 0x3);
    fr->mode_ext = ((newhead >> 4) & 0x3);
    fr->copyright = ((newhead >> 3) & 0x1);
    fr->original = ((newhead >> 2) & 0x1);
    fr->emphasis = newhead & 0x3;

    fr->stereo = (fr->mode == MPG_MD_MONO) ? 1 : 2;

    switch (fr->lay) {
    case 1:
        fr->framesize = (long) tabsel_123[fr->lsf][0][fr->bitrate_index] * 12000;
        fr->framesize /= freqs[fr->sampling_frequency];
        fr->framesize = ((fr->framesize + fr->padding) << 2) - 4;
        fr->down_sample = 0;
        fr->down_sample_sblimit = SBLIMIT >> (fr->down_sample);
        break;

    case 2:
        fr->framesize = (long) tabsel_123[fr->lsf][1][fr->bitrate_index] * 144000;
        fr->framesize /= freqs[fr->sampling_frequency];
        fr->framesize += fr->padding - 4;
        fr->down_sample = 0;
        fr->down_sample_sblimit = SBLIMIT >> (fr->down_sample);
        break;

    case 3:
#if 0
        fr->do_layer = do_layer3;
        if (fr->lsf)
            ssize = (fr->stereo == 1) ? 9 : 17;
        else
            ssize = (fr->stereo == 1) ? 17 : 32;
#endif

#if 0
        if (fr->error_protection)
            ssize += 2;
#endif
        if (fr->framesize > MAX_INPUT_FRAMESIZE) {
            lame_report_fnc(mp->report_err, "Frame size too big.\n");
            fr->framesize = MAX_INPUT_FRAMESIZE;
            return (0);
        }


        if (fr->bitrate_index == 0)
            fr->framesize = 0;
        else {
            fr->framesize = (long) tabsel_123[fr->lsf][2][fr->bitrate_index] * 144000;
            fr->framesize /= freqs[fr->sampling_frequency] << (fr->lsf);
            fr->framesize = fr->framesize + fr->padding - 4;
        }
        break;
    default:
        lame_report_fnc(mp->report_err, "Sorry, layer %d not supported\n", fr->lay);
        return (0);
    }
    /*    print_header(mp, fr); */

    return 1;
}

unsigned int
getbits(PMPSTR mp, int number_of_bits)
{
    unsigned long rval;

    if (number_of_bits <= 0 || !mp->wordpointer)
        return 0;

    {
        rval = mp->wordpointer[0];
        rval <<= 8;
        rval |= mp->wordpointer[1];
        rval <<= 8;
        rval |= mp->wordpointer[2];
        rval <<= mp->bitindex;
        rval &= 0xffffff;

        mp->bitindex += number_of_bits;

        rval >>= (24 - number_of_bits);

        mp->wordpointer += (mp->bitindex >> 3);
        mp->bitindex &= 7;
    }
    return rval;
}
unsigned int
getbits_fast(PMPSTR mp, int number_of_bits)
{
    unsigned long rval;

    {
        rval = mp->wordpointer[0];
        rval <<= 8;
        rval |= mp->wordpointer[1];
        rval <<= mp->bitindex;
        rval &= 0xffff;
        mp->bitindex += number_of_bits;

        rval >>= (16 - number_of_bits);

        mp->wordpointer += (mp->bitindex >> 3);
        mp->bitindex &= 7;
    }
    return rval;
}
/*
 * Side Info for MPEG 2.0 / LSF
 */
static void
III_get_side_info_2(PMPSTR mp, int stereo, int ms_stereo, long sfreq, int single)
{
    int     ch;
    int     powdiff = (single == 3) ? 4 : 0;

    mp->sideinfo.main_data_begin = getbits(mp, 8);

    if (stereo == 1)
        mp->sideinfo.private_bits = get1bit(mp);
    else
        mp->sideinfo.private_bits = getbits_fast(mp, 2);

    for (ch = 0; ch < stereo; ch++) {
        struct gr_info_s *gr_infos = &(mp->sideinfo.ch[ch].gr[0]);
        unsigned int qss;

        gr_infos->part2_3_length = getbits(mp, 12);
        gr_infos->big_values = getbits_fast(mp, 9);
        if (gr_infos->big_values > 288) {
            lame_report_fnc(mp->report_err, "big_values too large! %i\n", gr_infos->big_values);
            gr_infos->big_values = 288;
        }
        qss = getbits_fast(mp, 8);
        gr_infos->pow2gain = gainpow2 + 256 - qss + powdiff;
        if (mp->pinfo != NULL) {
            mp->pinfo->qss[0][ch] = qss;
        }


        if (ms_stereo)
            gr_infos->pow2gain += 2;
        gr_infos->scalefac_compress = getbits(mp, 9);
/* window-switching flag == 1 for block_Type != 0 .. and block-type == 0 -> win-sw-flag = 0 */
        if (get1bit(mp)) {
            int     i;
            gr_infos->block_type = getbits_fast(mp, 2);
            gr_infos->mixed_block_flag = get1bit(mp);
            gr_infos->table_select[0] = getbits_fast(mp, 5);
            gr_infos->table_select[1] = getbits_fast(mp, 5);
            /*
             * table_select[2] not needed, because there is no region2,
             * but to satisfy some verifications tools we set it either.
             */
            gr_infos->table_select[2] = 0;
            for (i = 0; i < 3; i++) {
                unsigned int sbg = (getbits_fast(mp, 3) << 3);
                gr_infos->full_gain[i] = gr_infos->pow2gain + sbg;
                if (mp->pinfo != NULL)
                    mp->pinfo->sub_gain[0][ch][i] = sbg / 8;

            }

            if (gr_infos->block_type == 0) {
                lame_report_fnc(mp->report_err, "Blocktype == 0 and window-switching == 1 not allowed.\n");
                /* error seems to be very good recoverable, so don't exit */
                /* exit(1); */
            }
            /* region_count/start parameters are implicit in this case. */
/* check this again! */
            if (gr_infos->block_type == 2) {
                if (sfreq == 8)
                    gr_infos->region1start = 36;
                else
                    gr_infos->region1start = 36 >> 1;
            }
            else if (sfreq == 8)
/* check this for 2.5 and sfreq=8 */
                gr_infos->region1start = 108 >> 1;
            else
                gr_infos->region1start = 54 >> 1;
            gr_infos->region2start = 576 >> 1;
        }
        else {
            unsigned int i, r0c, r1c, region0index, region1index;
            for (i = 0; i < 3; i++)
                gr_infos->table_select[i] = getbits_fast(mp, 5);
            r0c = getbits_fast(mp, 4);
            r1c = getbits_fast(mp, 3);
            region0index = r0c+1;
            if (region0index > 22) {
                region0index = 22;
                lame_report_fnc(mp->report_err, "region0index > 22\n");
            }
            region1index = r0c+1 + r1c+1;
            if (region1index > 22) {
                region1index = 22;
                lame_report_fnc(mp->report_err, "region1index > 22\n");
            }
            gr_infos->region1start = bandInfo[sfreq].longIdx[region0index] >> 1;
            gr_infos->region2start = bandInfo[sfreq].longIdx[region1index] >> 1;
            gr_infos->block_type = 0;
            gr_infos->mixed_block_flag = 0;
        }
        gr_infos->scalefac_scale = get1bit(mp);
        gr_infos->count1table_select = get1bit(mp);
    }
}
 static void
III_get_side_info_1(PMPSTR mp, int stereo,
                    int ms_stereo, long sfreq, int single)
{
    int     ch, gr;
    int     powdiff = (single == 3) ? 4 : 0;

    mp->sideinfo.main_data_begin = getbits(mp, 9);
    if (stereo == 1)
        mp->sideinfo.private_bits = getbits_fast(mp, 5);
    else
        mp->sideinfo.private_bits = getbits_fast(mp, 3);

    for (ch = 0; ch < stereo; ch++) {
        mp->sideinfo.ch[ch].gr[0].scfsi = -1;
        mp->sideinfo.ch[ch].gr[1].scfsi = getbits_fast(mp, 4);
    }

    for (gr = 0; gr < 2; gr++) {
        for (ch = 0; ch < stereo; ch++) {
            struct gr_info_s *gr_infos = &(mp->sideinfo.ch[ch].gr[gr]);

            gr_infos->part2_3_length = getbits(mp, 12);
            gr_infos->big_values = getbits_fast(mp, 9);
            if (gr_infos->big_values > 288) {
                lame_report_fnc(mp->report_err, "big_values too large! %i\n", gr_infos->big_values);
                gr_infos->big_values = 288;
            }
            {
                unsigned int qss = getbits_fast(mp, 8);
                gr_infos->pow2gain = gainpow2 + 256 - qss + powdiff;
                if (mp->pinfo != NULL) {
                    mp->pinfo->qss[gr][ch] = qss;
                }
            }
            if (ms_stereo)
                gr_infos->pow2gain += 2;
            gr_infos->scalefac_compress = getbits_fast(mp, 4);
/* window-switching flag == 1 for block_Type != 0 .. and block-type == 0 -> win-sw-flag = 0 */
            if (get1bit(mp)) {
                int     i;
                gr_infos->block_type = getbits_fast(mp, 2);
                gr_infos->mixed_block_flag = get1bit(mp);
                gr_infos->table_select[0] = getbits_fast(mp, 5);
                gr_infos->table_select[1] = getbits_fast(mp, 5);


                /*
                 * table_select[2] not needed, because there is no region2,
                 * but to satisfy some verifications tools we set it either.
                 */
                gr_infos->table_select[2] = 0;
                for (i = 0; i < 3; i++) {
                    unsigned int sbg = (getbits_fast(mp, 3) << 3);
                    gr_infos->full_gain[i] = gr_infos->pow2gain + sbg;
                    if (mp->pinfo != NULL)
                        mp->pinfo->sub_gain[gr][ch][i] = sbg / 8;
                }

                if (gr_infos->block_type == 0) {
                    lame_report_fnc(mp->report_err, "Blocktype == 0 and window-switching == 1 not allowed.\n");
                    /* error seems to be very good recoverable, so don't exit */
                    /* exit(1); */
                }
                /* region_count/start parameters are implicit in this case. */
                gr_infos->region1start = 36 >> 1;
                gr_infos->region2start = 576 >> 1;
            }
            else {
                unsigned int i, r0c, r1c, region0index, region1index;
                for (i = 0; i < 3; i++)
                    gr_infos->table_select[i] = getbits_fast(mp, 5);
                r0c = getbits_fast(mp, 4);
                r1c = getbits_fast(mp, 3);
                region0index = r0c+1;
                if (region0index > 22) {
                    region0index = 22;
                    lame_report_fnc(mp->report_err, "region0index > 22\n");
                }
                region1index = r0c+1 + r1c+1;
                if (region1index > 22) {
                    region1index = 22;
                    lame_report_fnc(mp->report_err, "region1index > 22\n");
                }
                gr_infos->region1start = bandInfo[sfreq].longIdx[region0index] >> 1;
                gr_infos->region2start = bandInfo[sfreq].longIdx[region1index] >> 1;
                gr_infos->block_type = 0;
                gr_infos->mixed_block_flag = 0;
            }
            gr_infos->preflag = get1bit(mp);
            gr_infos->scalefac_scale = get1bit(mp);
            gr_infos->count1table_select = get1bit(mp);
        }
    }
}
int
decode_layer3_sideinfo(PMPSTR mp)
{
    struct frame *fr = &mp->fr;
    int     stereo = fr->stereo;
    int     single = fr->single;
    int     ms_stereo;
    int     sfreq = fr->sampling_frequency;
    int     granules;
    int     ch, gr, databits;

    if (stereo == 1) {  /* stream is mono */
        single = 0;
    }

    if (fr->mode == MPG_MD_JOINT_STEREO) {
        ms_stereo = fr->mode_ext & 0x2;
    }
    else
        ms_stereo = 0;


    if (fr->lsf) {
        granules = 1;
        III_get_side_info_2(mp, stereo, ms_stereo, sfreq, single);
    }
    else {
        granules = 2;
        III_get_side_info_1(mp, stereo, ms_stereo, sfreq, single);
    }

    databits = 0;
    for (gr = 0; gr < granules; ++gr) {
        for (ch = 0; ch < stereo; ++ch) {
            struct gr_info_s *gr_infos = &(mp->sideinfo.ch[ch].gr[gr]);
            databits += gr_infos->part2_3_length;
        }
    }
    return databits - 8 * mp->sideinfo.main_data_begin;
}

int
set_pointer(PMPSTR mp, long backstep)
{
    unsigned char *bsbufold;

    if (mp->fsizeold < 0 && backstep > 0) {
        lame_report_fnc(mp->report_err, "hip: Can't step back %ld bytes!\n", backstep);
        return MP3_ERR;
    }
    bsbufold = mp->bsspace[1 - mp->bsnum] + 512;
    mp->wordpointer -= backstep;
    if (backstep)
        memcpy(mp->wordpointer, bsbufold + mp->fsizeold - backstep, (size_t) backstep);
    mp->bitindex = 0;
    return MP3_OK;
}

/*
 * don't forget to apply the same changes to III_dequantize_sample_ms() !!!
 */
static int
III_dequantize_sample(PMPSTR mp, real xr[SBLIMIT][SSLIMIT], int *scf,
                      struct gr_info_s *gr_infos, int sfreq, int part2bits)
{
    int     shift = 1 + gr_infos->scalefac_scale;
    real   *xrpnt = (real *) xr;
    int     l[3], l3;
    int     part2remain = gr_infos->part2_3_length - part2bits;
    int    *me;

    /* lame_report_fnc(mp->report_dbg,"part2remain = %d, gr_infos->part2_3_length = %d, part2bits = %d\n",
       part2remain, gr_infos->part2_3_length, part2bits); */

    {
        int     i;

        for (i = (&xr[SBLIMIT][0] - xrpnt) >> 1; i > 0; i--) {
            *xrpnt++ = 0.0;
            *xrpnt++ = 0.0;
        }

        xrpnt = (real *) xr;
    }

    {
        int     bv = gr_infos->big_values;
        int     region1 = gr_infos->region1start;
        int     region2 = gr_infos->region2start;

        l3 = ((576 >> 1) - bv) >> 1;
/*
 * we may lose the 'odd' bit here !! 
 * check this later again 
 */
        if (bv <= region1) {
            l[0] = bv;
            l[1] = 0;
            l[2] = 0;
        }
        else {
            l[0] = region1;
            if (bv <= region2) {
                l[1] = bv - l[0];
                l[2] = 0;
            }
            else {
                l[1] = region2 - l[0];
                l[2] = bv - region2;
            }
        }
    }
    /* MDH crash fix */
    {
        int     i;
        for (i = 0; i < 3; i++) {
            if (l[i] < 0) {
                lame_report_fnc(mp->report_err, "hip: Bogus region length (%d)\n", l[i]);
                l[i] = 0;
            }
        }
    }
    /* end MDH crash fix */

    if (gr_infos->block_type == 2) {
        /*
         * decoding with short or mixed mode BandIndex table 
         */
        int     i, max[4];
        int     step = 0, lwin = 0, cb = 0;
        real    v = 0.0;
        int    *m, mc;

        if (gr_infos->mixed_block_flag) {
            max[3] = -1;
            max[0] = max[1] = max[2] = 2;
            m = map[sfreq][0];
            me = mapend[sfreq][0];
        }
        else {
            max[0] = max[1] = max[2] = max[3] = -1;
            /* max[3] not really needed in this case */
            m = map[sfreq][1];
            me = mapend[sfreq][1];
        }

        mc = 0;
        for (i = 0; i < 2; i++) {
            int     lp = l[i];
            struct newhuff const *h = (struct newhuff const *) (ht + gr_infos->table_select[i]);
            for (; lp; lp--, mc--) {
                int     x, y;
                if ((!mc)) {
                    mc = *m++;
                    xrpnt = ((real *) xr) + (*m++);
                    lwin = *m++;
                    cb = *m++;
                    if (lwin == 3) {
                        v = gr_infos->pow2gain[(*scf++) << shift];
                        step = 1;
                    }
                    else {
                        v = gr_infos->full_gain[lwin][(*scf++) << shift];
                        step = 3;
                    }
                }
                {
                    short const *val = (short const *) h->table;
                    while ((y = *val++) < 0) {
                        if (get1bit(mp))
                            val -= y;
                        part2remain--;
                    }
                    x = y >> 4;
                    y &= 0xf;
                }
                if (x == 15) {
                    max[lwin] = cb;
                    part2remain -= h->linbits + 1;
                    x += getbits(mp, (int) h->linbits);
                    if (get1bit(mp))
                        *xrpnt = -ispow[x] * v;
                    else
                        *xrpnt = ispow[x] * v;
                }
                else if (x) {
                    max[lwin] = cb;
                    if (get1bit(mp))
                        *xrpnt = -ispow[x] * v;
                    else
                        *xrpnt = ispow[x] * v;
                    part2remain--;
                }
                else
                    *xrpnt = 0.0;
                xrpnt += step;
                if (y == 15) {
                    max[lwin] = cb;
                    part2remain -= h->linbits + 1;
                    y += getbits(mp, (int) h->linbits);
                    if (get1bit(mp))
                        *xrpnt = -ispow[y] * v;
                    else
                        *xrpnt = ispow[y] * v;
                }
                else if (y) {
                    max[lwin] = cb;
                    if (get1bit(mp))
                        *xrpnt = -ispow[y] * v;
                    else
                        *xrpnt = ispow[y] * v;
                    part2remain--;
                }
                else
                    *xrpnt = 0.0;
                xrpnt += step;
            }
        }
        for (; l3 && (part2remain > 0); l3--) {
            struct newhuff const *h = (struct newhuff const *) (htc + gr_infos->count1table_select);
            short const *val = (short const *) h->table;
            short   a;

            while ((a = *val++) < 0) {
                part2remain--;
                if (part2remain < 0) {
                    part2remain++;
                    a = 0;
                    break;
                }
                if (get1bit(mp))
                    val -= a;
            }
            for (i = 0; i < 4; i++) {
                if (!(i & 1)) {
                    if (!mc) {
                        mc = *m++;
                        xrpnt = ((real *) xr) + (*m++);
                        lwin = *m++;
                        cb = *m++;
                        if (lwin == 3) {
                            v = gr_infos->pow2gain[(*scf++) << shift];
                            step = 1;
                        }
                        else {
                            v = gr_infos->full_gain[lwin][(*scf++) << shift];
                            step = 3;
                        }
                    }
                    mc--;
                }
                if ((a & (0x8 >> i))) {
                    max[lwin] = cb;
                    part2remain--;
                    if (part2remain < 0) {
                        part2remain++;
                        break;
                    }
                    if (get1bit(mp))
                        *xrpnt = -v;
                    else
                        *xrpnt = v;
                }
                else
                    *xrpnt = 0.0;
                xrpnt += step;
            }
        }

        while (m < me) {
            if (!mc) {
                mc = *m++;
                xrpnt = ((real *) xr) + *m++;
                if ((*m++) == 3)
                    step = 1;
                else
                    step = 3;
                m++;    /* cb */
            }
            mc--;
            *xrpnt = 0.0;
            xrpnt += step;
            *xrpnt = 0.0;
            xrpnt += step;
/* we could add a little opt. here:
 * if we finished a band for window 3 or a long band
 * further bands could copied in a simple loop without a
 * special 'map' decoding
 */
        }

        gr_infos->maxband[0] = max[0] + 1;
        gr_infos->maxband[1] = max[1] + 1;
        gr_infos->maxband[2] = max[2] + 1;
        gr_infos->maxbandl = max[3] + 1;

        {
            int     rmax = max[0] > max[1] ? max[0] : max[1];
            rmax = (rmax > max[2] ? rmax : max[2]) + 1;
            gr_infos->maxb = rmax ? shortLimit[sfreq][rmax] : longLimit[sfreq][max[3] + 1];
        }

    }
    else {
        /*
         * decoding with 'long' BandIndex table (block_type != 2)
         */
        int const *pretab = (int const *) (gr_infos->preflag ? pretab1 : pretab2);
        int     i, max = -1;
        int     cb = 0;
        int    *m = map[sfreq][2];
        real    v = 0.0;
        int     mc = 0;

        /*
         * long hash table values
         */
        for (i = 0; i < 3; i++) {
            int     lp = l[i];
            struct newhuff const *h = (struct newhuff const *) (ht + gr_infos->table_select[i]);

            for (; lp; lp--, mc--) {
                int     x, y;

                if (!mc) {
                    mc = *m++;
                    v = gr_infos->pow2gain[((*scf++) + (*pretab++)) << shift];
                    cb = *m++;
                }
                {
                    short const *val = (short const *) h->table;
                    while ((y = *val++) < 0) {
                        if (get1bit(mp))
                            val -= y;
                        part2remain--;
                    }
                    x = y >> 4;
                    y &= 0xf;
                }
                if (x == 15) {
                    max = cb;
                    part2remain -= h->linbits + 1;
                    x += getbits(mp, (int) h->linbits);
                    if (get1bit(mp))
                        *xrpnt++ = -ispow[x] * v;
                    else
                        *xrpnt++ = ispow[x] * v;
                }
                else if (x) {
                    max = cb;
                    if (get1bit(mp))
                        *xrpnt++ = -ispow[x] * v;
                    else
                        *xrpnt++ = ispow[x] * v;
                    part2remain--;
                }
                else
                    *xrpnt++ = 0.0;

                if (y == 15) {
                    max = cb;
                    part2remain -= h->linbits + 1;
                    y += getbits(mp, (int) h->linbits);
                    if (get1bit(mp))
                        *xrpnt++ = -ispow[y] * v;
                    else
                        *xrpnt++ = ispow[y] * v;
                }
                else if (y) {
                    max = cb;
                    if (get1bit(mp))
                        *xrpnt++ = -ispow[y] * v;
                    else
                        *xrpnt++ = ispow[y] * v;
                    part2remain--;
                }
                else
                    *xrpnt++ = 0.0;
            }
        }

        /*
         * short (count1table) values
         */
        for (; l3 && (part2remain > 0); l3--) {
            struct newhuff const *h = (struct newhuff const *) (htc + gr_infos->count1table_select);
            short const *val = (short const *) h->table;
            short   a;

            while ((a = *val++) < 0) {
                part2remain--;
                if (part2remain < 0) {
                    part2remain++;
                    a = 0;
                    break;
                }
                if (get1bit(mp))
                    val -= a;
            }
            for (i = 0; i < 4; i++) {
                if (!(i & 1)) {
                    if (!mc) {
                        mc = *m++;
                        cb = *m++;
                        v = gr_infos->pow2gain[((*scf++) + (*pretab++)) << shift];
                    }
                    mc--;
                }
                if ((a & (0x8 >> i))) {
                    max = cb;
                    part2remain--;
                    if (part2remain < 0) {
                        part2remain++;
                        break;
                    }
                    if (get1bit(mp))
                        *xrpnt++ = -v;
                    else
                        *xrpnt++ = v;
                }
                else
                    *xrpnt++ = 0.0;
            }
        }

        /* 
         * zero part
         */
        for (i = (&xr[SBLIMIT][0] - xrpnt) >> 1; i; i--) {
            *xrpnt++ = 0.0;
            *xrpnt++ = 0.0;
        }

        gr_infos->maxbandl = max + 1;
        gr_infos->maxb = longLimit[sfreq][gr_infos->maxbandl];
    }

    while (part2remain > 16) {
        getbits(mp, 16); /* Dismiss stuffing Bits */
        part2remain -= 16;
    }
    if (part2remain > 0)
        getbits(mp, part2remain);
    else if (part2remain < 0) {
        lame_report_fnc(mp->report_err, "hip: Can't rewind stream by %d bits!\n", -part2remain);
        return 1;       /* -> error */
    }
    return 0;
}


/* 
 * III_stereo: calculate real channel values for Joint-I-Stereo-mode
 */
static void
III_i_stereo(real xr_buf[2][SBLIMIT][SSLIMIT], int *scalefac,
             struct gr_info_s *gr_infos, int sfreq, int ms_stereo, int lsf)
{
    real(*xr)[SBLIMIT * SSLIMIT] = (real(*)[SBLIMIT * SSLIMIT]) xr_buf;
    struct bandInfoStruct const *bi = (struct bandInfoStruct const *) &bandInfo[sfreq];
    real   *tabl1, *tabl2;

    if (lsf) {
        int     p = gr_infos->scalefac_compress & 0x1;
        if (ms_stereo) {
            tabl1 = pow1_2[p];
            tabl2 = pow2_2[p];
        }
        else {
            tabl1 = pow1_1[p];
            tabl2 = pow2_1[p];
        }
    }
    else {
        if (ms_stereo) {
            tabl1 = tan1_2;
            tabl2 = tan2_2;
        }
        else {
            tabl1 = tan1_1;
            tabl2 = tan2_1;
        }
    }

    if (gr_infos->block_type == 2) {
        int     lwin, do_l = 0;
        if (gr_infos->mixed_block_flag)
            do_l = 1;

        for (lwin = 0; lwin < 3; lwin++) { /* process each window */
            /* get first band with zero values */
            int     is_p, sb, idx, sfb = gr_infos->maxband[lwin]; /* sfb is minimal 3 for mixed mode */
            if (sfb > 3)
                do_l = 0;

            for (; sfb < 12; sfb++) {
                is_p = scalefac[sfb * 3 + lwin - gr_infos->mixed_block_flag]; /* scale: 0-15 */
                if (is_p != 7) {
                    real    t1, t2;
                    sb = bi->shortDiff[sfb];
                    idx = bi->shortIdx[sfb] + lwin;
                    t1 = tabl1[is_p];
                    t2 = tabl2[is_p];
                    for (; sb > 0; sb--, idx += 3) {
                        real    v = xr[0][idx];
                        xr[0][idx] = v * t1;
                        xr[1][idx] = v * t2;
                    }
                }
            }

#if 1
/* in the original: copy 10 to 11 , here: copy 11 to 12 
maybe still wrong??? (copy 12 to 13?) */
            is_p = scalefac[11 * 3 + lwin - gr_infos->mixed_block_flag]; /* scale: 0-15 */
            sb = bi->shortDiff[12];
            idx = bi->shortIdx[12] + lwin;
#else
            is_p = scalefac[10 * 3 + lwin - gr_infos->mixed_block_flag]; /* scale: 0-15 */
            sb = bi->shortDiff[11];
            idx = bi->shortIdx[11] + lwin;
#endif
            if (is_p != 7) {
                real    t1, t2;
                t1 = tabl1[is_p];
                t2 = tabl2[is_p];
                for (; sb > 0; sb--, idx += 3) {
                    real    v = xr[0][idx];
                    xr[0][idx] = v * t1;
                    xr[1][idx] = v * t2;
                }
            }
        }               /* end for(lwin; .. ; . ) */

        if (do_l) {
/* also check l-part, if ALL bands in the three windows are 'empty'
 * and mode = mixed_mode 
 */
            int     sfb = gr_infos->maxbandl;
            int     idx = bi->longIdx[sfb];

            for (; sfb < 8; sfb++) {
                int     sb = bi->longDiff[sfb];
                int     is_p = scalefac[sfb]; /* scale: 0-15 */
                if (is_p != 7) {
                    real    t1, t2;
                    t1 = tabl1[is_p];
                    t2 = tabl2[is_p];
                    for (; sb > 0; sb--, idx++) {
                        real    v = xr[0][idx];
                        xr[0][idx] = v * t1;
                        xr[1][idx] = v * t2;
                    }
                }
                else
                    idx += sb;
            }
        }
    }
    else {              /* ((gr_infos->block_type != 2)) */

        int     sfb = gr_infos->maxbandl;
        int     is_p, idx = bi->longIdx[sfb];
        for (; sfb < 21; sfb++) {
            int     sb = bi->longDiff[sfb];
            is_p = scalefac[sfb]; /* scale: 0-15 */
            if (is_p != 7) {
                real    t1, t2;
                t1 = tabl1[is_p];
                t2 = tabl2[is_p];
                for (; sb > 0; sb--, idx++) {
                    real    v = xr[0][idx];
                    xr[0][idx] = v * t1;
                    xr[1][idx] = v * t2;
                }
            }
            else
                idx += sb;
        }

        is_p = scalefac[20]; /* copy l-band 20 to l-band 21 */
        if (is_p != 7) {
            int     sb;
            real    t1 = tabl1[is_p], t2 = tabl2[is_p];

            for (sb = bi->longDiff[21]; sb > 0; sb--, idx++) {
                real    v = xr[0][idx];
                xr[0][idx] = v * t1;
                xr[1][idx] = v * t2;
            }
        }
    }                   /* ... */
}

static void
III_antialias(real xr[SBLIMIT][SSLIMIT], struct gr_info_s *gr_infos)
{
    int     sblim;

    if (gr_infos->block_type == 2) {
        if (!gr_infos->mixed_block_flag)
            return;
        sblim = 1;
    }
    else {
        sblim = gr_infos->maxb - 1;
    }

    /* 31 alias-reduction operations between each pair of sub-bands */
    /* with 8 butterflies between each pair                         */

    {
        int     sb;
        real   *xr1 = (real *) xr[1];

        for (sb = sblim; sb; sb--, xr1 += 10) {
            int     ss;
            real   *cs = aa_cs, *ca = aa_ca;
            real   *xr2 = xr1;

            for (ss = 7; ss >= 0; ss--) { /* upper and lower butterfly inputs */
                real    bu = *--xr2, bd = *xr1;
                *xr2 = (bu * (*cs)) - (bd * (*ca));
                *xr1++ = (bd * (*cs++)) + (bu * (*ca++));
            }
        }
    }
}

/*
 * read scalefactors
 */

static int
III_get_scale_factors_1(PMPSTR mp, int *scf, struct gr_info_s *gr_infos)
{
    static const unsigned char slen[2][16] = {
        {0, 0, 0, 0, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4},
        {0, 1, 2, 3, 0, 1, 2, 3, 1, 2, 3, 1, 2, 3, 2, 3}
    };
    int     numbits;
    int     num0 = slen[0][gr_infos->scalefac_compress];
    int     num1 = slen[1][gr_infos->scalefac_compress];

    if (gr_infos->block_type == 2) {
        int     i = 18;
        numbits = (num0 + num1) * 18;

        if (gr_infos->mixed_block_flag) {
            for (i = 8; i; i--)
                *scf++ = getbits_fast(mp, num0);
            i = 9;
            numbits -= num0; /* num0 * 17 + num1 * 18 */
        }

        for (; i; i--)
            *scf++ = getbits_fast(mp, num0);
        for (i = 18; i; i--)
            *scf++ = getbits_fast(mp, num1);
        *scf++ = 0;
        *scf++ = 0;
        *scf++ = 0;     /* short[13][0..2] = 0 */
    }
    else {
        int     i;
        int     scfsi = gr_infos->scfsi;

        if (scfsi < 0) { /* scfsi < 0 => granule == 0 */
            for (i = 11; i; i--)
                *scf++ = getbits_fast(mp, num0);
            for (i = 10; i; i--)
                *scf++ = getbits_fast(mp, num1);
            numbits = (num0 + num1) * 10 + num0;
        }
        else {
            numbits = 0;
            if (!(scfsi & 0x8)) {
                for (i = 6; i; i--)
                    *scf++ = getbits_fast(mp, num0);
                numbits += num0 * 6;
            }
            else {
                scf += 6;
            }

            if (!(scfsi & 0x4)) {
                for (i = 5; i; i--)
                    *scf++ = getbits_fast(mp, num0);
                numbits += num0 * 5;
            }
            else {
                scf += 5;
            }

            if (!(scfsi & 0x2)) {
                for (i = 5; i; i--)
                    *scf++ = getbits_fast(mp, num1);
                numbits += num1 * 5;
            }
            else {
                scf += 5;
            }

            if (!(scfsi & 0x1)) {
                for (i = 5; i; i--)
                    *scf++ = getbits_fast(mp, num1);
                numbits += num1 * 5;
            }
            else {
                scf += 5;
            }
        }

        *scf++ = 0;     /* no l[21] in original sources */
    }
    return numbits;
}


static int
III_get_scale_factors_2(PMPSTR mp, int *scf, struct gr_info_s *gr_infos, int i_stereo)
{
    unsigned char const *pnt;
    int     i, j;
    unsigned int slen;
    int     n = 0;
    int     numbits = 0;

  /* *INDENT-OFF* */
  static const unsigned char stab[3][6][4] = {
   { { 6, 5, 5,5 } , { 6, 5, 7,3 } , { 11,10,0,0} ,
     { 7, 7, 7,0 } , { 6, 6, 6,3 } , {  8, 8,5,0} } ,
   { { 9, 9, 9,9 } , { 9, 9,12,6 } , { 18,18,0,0} ,
     {12,12,12,0 } , {12, 9, 9,6 } , { 15,12,9,0} } ,
   { { 6, 9, 9,9 } , { 6, 9,12,6 } , { 15,18,0,0} ,
     { 6,15,12,0 } , { 6,12, 9,6 } , {  6,18,9,0} } }; 
  /* *INDENT-ON* */

    if (i_stereo)       /* i_stereo AND second channel -> do_layer3() checks this */
        slen = i_slen2[gr_infos->scalefac_compress >> 1];
    else
        slen = n_slen2[gr_infos->scalefac_compress];

    gr_infos->preflag = (slen >> 15) & 0x1;

    n = 0;
    if (gr_infos->block_type == 2) {
        n++;
        if (gr_infos->mixed_block_flag)
            n++;
    }

    pnt = (unsigned char const *) stab[n][(slen >> 12) & 0x7];

    for (i = 0; i < 4; i++) {
        int     num = slen & 0x7;
        slen >>= 3;
        if (num) {
            for (j = 0; j < (int) (pnt[i]); j++)
                *scf++ = getbits_fast(mp, num);
            numbits += pnt[i] * num;
        }
        else {
            for (j = 0; j < (int) (pnt[i]); j++)
                *scf++ = 0;
        }
    }

    n = (n << 1) + 1;
    for (i = 0; i < n; i++)
        *scf++ = 0;

    return numbits;
}

static void dct36(real *inbuf,real *o1,real *o2,real *wintab,real *tsbuf)
{
  {
    real *in = inbuf;

    in[17]+=in[16]; in[16]+=in[15]; in[15]+=in[14];
    in[14]+=in[13]; in[13]+=in[12]; in[12]+=in[11];
    in[11]+=in[10]; in[10]+=in[9];  in[9] +=in[8];
    in[8] +=in[7];  in[7] +=in[6];  in[6] +=in[5];
    in[5] +=in[4];  in[4] +=in[3];  in[3] +=in[2];
    in[2] +=in[1];  in[1] +=in[0];

    in[17]+=in[15]; in[15]+=in[13]; in[13]+=in[11]; in[11]+=in[9];
    in[9] +=in[7];  in[7] +=in[5];  in[5] +=in[3];  in[3] +=in[1];

  {

#define MACRO0(v) { \
    real tmp; \
    out2[9+(v)] = (tmp = sum0 + sum1) * w[27+(v)]; \
    out2[8-(v)] = tmp * w[26-(v)];  } \
    sum0 -= sum1; \
    ts[SBLIMIT*(8-(v))] = out1[8-(v)] + sum0 * w[8-(v)]; \
    ts[SBLIMIT*(9+(v))] = out1[9+(v)] + sum0 * w[9+(v)]; 
#define MACRO1(v) { \
    real sum0,sum1; \
    sum0 = tmp1a + tmp2a; \
    sum1 = (tmp1b + tmp2b) * tfcos36[(v)]; \
    MACRO0(v); }
#define MACRO2(v) { \
    real sum0,sum1; \
    sum0 = tmp2a - tmp1a; \
    sum1 = (tmp2b - tmp1b) * tfcos36[(v)]; \
    MACRO0(v); }

    const real *c = COS9;
    real *out2 = o2;
    real *w = wintab;
    real *out1 = o1;
    real *ts = tsbuf;

    real ta33,ta66,tb33,tb66;

    ta33 = in[2*3+0] * c[3];
    ta66 = in[2*6+0] * c[6];
    tb33 = in[2*3+1] * c[3];
    tb66 = in[2*6+1] * c[6];

    { 
      real tmp1a,tmp2a,tmp1b,tmp2b;
      tmp1a =             in[2*1+0] * c[1] + ta33 + in[2*5+0] * c[5] + in[2*7+0] * c[7];
      tmp1b =             in[2*1+1] * c[1] + tb33 + in[2*5+1] * c[5] + in[2*7+1] * c[7];
      tmp2a = in[2*0+0] + in[2*2+0] * c[2] + in[2*4+0] * c[4] + ta66 + in[2*8+0] * c[8];
      tmp2b = in[2*0+1] + in[2*2+1] * c[2] + in[2*4+1] * c[4] + tb66 + in[2*8+1] * c[8];

      MACRO1(0);
      MACRO2(8);
    }

    {
      real tmp1a,tmp2a,tmp1b,tmp2b;
      tmp1a = ( in[2*1+0] - in[2*5+0] - in[2*7+0] ) * c[3];
      tmp1b = ( in[2*1+1] - in[2*5+1] - in[2*7+1] ) * c[3];
      tmp2a = ( in[2*2+0] - in[2*4+0] - in[2*8+0] ) * c[6] - in[2*6+0] + in[2*0+0];
      tmp2b = ( in[2*2+1] - in[2*4+1] - in[2*8+1] ) * c[6] - in[2*6+1] + in[2*0+1];

      MACRO1(1);
      MACRO2(7);
    }

    {
      real tmp1a,tmp2a,tmp1b,tmp2b;
      tmp1a =             in[2*1+0] * c[5] - ta33 - in[2*5+0] * c[7] + in[2*7+0] * c[1];
      tmp1b =             in[2*1+1] * c[5] - tb33 - in[2*5+1] * c[7] + in[2*7+1] * c[1];
      tmp2a = in[2*0+0] - in[2*2+0] * c[8] - in[2*4+0] * c[2] + ta66 + in[2*8+0] * c[4];
      tmp2b = in[2*0+1] - in[2*2+1] * c[8] - in[2*4+1] * c[2] + tb66 + in[2*8+1] * c[4];

      MACRO1(2);
      MACRO2(6);
    }

    {
      real tmp1a,tmp2a,tmp1b,tmp2b;
      tmp1a =             in[2*1+0] * c[7] - ta33 + in[2*5+0] * c[1] - in[2*7+0] * c[5];
      tmp1b =             in[2*1+1] * c[7] - tb33 + in[2*5+1] * c[1] - in[2*7+1] * c[5];
      tmp2a = in[2*0+0] - in[2*2+0] * c[4] + in[2*4+0] * c[8] + ta66 - in[2*8+0] * c[2];
      tmp2b = in[2*0+1] - in[2*2+1] * c[4] + in[2*4+1] * c[8] + tb66 - in[2*8+1] * c[2];

      MACRO1(3);
      MACRO2(5);
    }

    {
      real sum0,sum1;
      sum0 =  in[2*0+0] - in[2*2+0] + in[2*4+0] - in[2*6+0] + in[2*8+0];
      sum1 = (in[2*0+1] - in[2*2+1] + in[2*4+1] - in[2*6+1] + in[2*8+1] ) * tfcos36[4];
      MACRO0(4);
    }
  }

  }
}


/*
 * new DCT12
 */
static void dct12(real *in,real *rawout1,real *rawout2,real *wi,real *ts)
{
#define DCT12_PART1 \
             in5 = in[5*3];  \
     in5 += (in4 = in[4*3]); \
     in4 += (in3 = in[3*3]); \
     in3 += (in2 = in[2*3]); \
     in2 += (in1 = in[1*3]); \
     in1 += (in0 = in[0*3]); \
                             \
     in5 += in3; in3 += in1; \
                             \
     in2 *= COS6_1; \
     in3 *= COS6_1; \

#define DCT12_PART2 \
     in0 += in4 * COS6_2; \
                          \
     in4 = in0 + in2;     \
     in0 -= in2;          \
                          \
     in1 += in5 * COS6_2; \
                          \
     in5 = (in1 + in3) * tfcos12[0]; \
     in1 = (in1 - in3) * tfcos12[2]; \
                         \
     in3 = in4 + in5;    \
     in4 -= in5;         \
                         \
     in2 = in0 + in1;    \
     in0 -= in1;


   {
     real in0,in1,in2,in3,in4,in5;
     real *out1 = rawout1;
     ts[SBLIMIT*0] = out1[0]; ts[SBLIMIT*1] = out1[1]; ts[SBLIMIT*2] = out1[2];
     ts[SBLIMIT*3] = out1[3]; ts[SBLIMIT*4] = out1[4]; ts[SBLIMIT*5] = out1[5];
 
     DCT12_PART1

     {
       real tmp0,tmp1 = (in0 - in4);
       {
         real tmp2 = (in1 - in5) * tfcos12[1];
         tmp0 = tmp1 + tmp2;
         tmp1 -= tmp2;
       }
       ts[(17-1)*SBLIMIT] = out1[17-1] + tmp0 * wi[11-1];
       ts[(12+1)*SBLIMIT] = out1[12+1] + tmp0 * wi[6+1];
       ts[(6 +1)*SBLIMIT] = out1[6 +1] + tmp1 * wi[1];
       ts[(11-1)*SBLIMIT] = out1[11-1] + tmp1 * wi[5-1];
     }

     DCT12_PART2

     ts[(17-0)*SBLIMIT] = out1[17-0] + in2 * wi[11-0];
     ts[(12+0)*SBLIMIT] = out1[12+0] + in2 * wi[6+0];
     ts[(12+2)*SBLIMIT] = out1[12+2] + in3 * wi[6+2];
     ts[(17-2)*SBLIMIT] = out1[17-2] + in3 * wi[11-2];

     ts[(6+0)*SBLIMIT]  = out1[6+0] + in0 * wi[0];
     ts[(11-0)*SBLIMIT] = out1[11-0] + in0 * wi[5-0];
     ts[(6+2)*SBLIMIT]  = out1[6+2] + in4 * wi[2];
     ts[(11-2)*SBLIMIT] = out1[11-2] + in4 * wi[5-2];
  }

  in++;

  {
     real in0,in1,in2,in3,in4,in5;
     real *out2 = rawout2;
 
     DCT12_PART1

     {
       real tmp0,tmp1 = (in0 - in4);
       {
         real tmp2 = (in1 - in5) * tfcos12[1];
         tmp0 = tmp1 + tmp2;
         tmp1 -= tmp2;
       }
       out2[5-1] = tmp0 * wi[11-1];
       out2[0+1] = tmp0 * wi[6+1];
       ts[(12+1)*SBLIMIT] += tmp1 * wi[1];
       ts[(17-1)*SBLIMIT] += tmp1 * wi[5-1];
     }

     DCT12_PART2

     out2[5-0] = in2 * wi[11-0];
     out2[0+0] = in2 * wi[6+0];
     out2[0+2] = in3 * wi[6+2];
     out2[5-2] = in3 * wi[11-2];

     ts[(12+0)*SBLIMIT] += in0 * wi[0];
     ts[(17-0)*SBLIMIT] += in0 * wi[5-0];
     ts[(12+2)*SBLIMIT] += in4 * wi[2];
     ts[(17-2)*SBLIMIT] += in4 * wi[5-2];
  }

  in++; 

  {
     real in0,in1,in2,in3,in4,in5;
     real *out2 = rawout2;
     out2[12]=out2[13]=out2[14]=out2[15]=out2[16]=out2[17]=0.0;

     DCT12_PART1

     {
       real tmp0,tmp1 = (in0 - in4);
       {
         real tmp2 = (in1 - in5) * tfcos12[1];
         tmp0 = tmp1 + tmp2;
         tmp1 -= tmp2;
       }
       out2[11-1] = tmp0 * wi[11-1];
       out2[6 +1] = tmp0 * wi[6+1];
       out2[0+1] += tmp1 * wi[1];
       out2[5-1] += tmp1 * wi[5-1];
     }

     DCT12_PART2

     out2[11-0] = in2 * wi[11-0];
     out2[6 +0] = in2 * wi[6+0];
     out2[6 +2] = in3 * wi[6+2];
     out2[11-2] = in3 * wi[11-2];

     out2[0+0] += in0 * wi[0];
     out2[5-0] += in0 * wi[5-0];
     out2[0+2] += in4 * wi[2];
     out2[5-2] += in4 * wi[5-2];
  }
}
/*
 * III_hybrid
 */
static void
III_hybrid(PMPSTR mp, real fsIn[SBLIMIT][SSLIMIT], real tsOut[SSLIMIT][SBLIMIT],
           int ch, struct gr_info_s *gr_infos)
{
    real   *tspnt = (real *) tsOut;
    real(*block)[2][SBLIMIT * SSLIMIT] = mp->hybrid_block;
    int    *blc = mp->hybrid_blc;
    real   *rawout1, *rawout2;
    int     bt;
    int     sb = 0;

    {
        int     b = blc[ch];
        rawout1 = block[b][ch];
        b = -b + 1;
        rawout2 = block[b][ch];
        blc[ch] = b;
    }


    if (gr_infos->mixed_block_flag) {
        sb = 2;
        dct36(fsIn[0], rawout1, rawout2, win[0], tspnt);
        dct36(fsIn[1], rawout1 + 18, rawout2 + 18, win1[0], tspnt + 1);
        rawout1 += 36;
        rawout2 += 36;
        tspnt += 2;
    }

    bt = gr_infos->block_type;
    if (bt == 2) {
        for (; sb < (int) gr_infos->maxb; sb += 2, tspnt += 2, rawout1 += 36, rawout2 += 36) {
            dct12(fsIn[sb], rawout1, rawout2, win[2], tspnt);
            dct12(fsIn[sb + 1], rawout1 + 18, rawout2 + 18, win1[2], tspnt + 1);
        }
    }
    else {
        for (; sb < (int) gr_infos->maxb; sb += 2, tspnt += 2, rawout1 += 36, rawout2 += 36) {
            dct36(fsIn[sb], rawout1, rawout2, win[bt], tspnt);
            dct36(fsIn[sb + 1], rawout1 + 18, rawout2 + 18, win1[bt], tspnt + 1);
        }
    }

    for (; sb < SBLIMIT; sb++, tspnt++) {
        int     i;
        for (i = 0; i < SSLIMIT; i++) {
            tspnt[i * SBLIMIT] = *rawout1++;
            *rawout2++ = 0.0;
        }
    }
}
int
decode_layer3_frame(PMPSTR mp, unsigned char *pcm_sample, int *pcm_point,
          int (*synth_1to1_mono_ptr) (PMPSTR, real *, unsigned char *, int *),
          int (*synth_1to1_ptr) (PMPSTR, real *, int, unsigned char *, int *))
{
    int     gr, ch, ss, clip = 0;
    int     scalefacs[2][39]; /* max 39 for short[13][3] mode, mixed: 38, long: 22 */
    /*  struct III_sideinfo sideinfo; */
    struct frame *fr = &(mp->fr);
    int     stereo = fr->stereo;
    int     single = fr->single;
    int     ms_stereo, i_stereo;
    int     sfreq = fr->sampling_frequency;
    int     stereo1, granules;
    real    hybridIn[2][SBLIMIT][SSLIMIT];
    real    hybridOut[2][SSLIMIT][SBLIMIT];

    if (set_pointer(mp, (int) mp->sideinfo.main_data_begin) == MP3_ERR)
        return 0;

    if (stereo == 1) {  /* stream is mono */
        stereo1 = 1;
        single = 0;
    }
    else if (single >= 0) /* stream is stereo, but force to mono */
        stereo1 = 1;
    else
        stereo1 = 2;

    if (fr->mode == MPG_MD_JOINT_STEREO) {
        ms_stereo = fr->mode_ext & 0x2;
        i_stereo = fr->mode_ext & 0x1;
    }
    else
        ms_stereo = i_stereo = 0;


    if (fr->lsf) {
        granules = 1;
    }
    else {
        granules = 2;
    }

    for (gr = 0; gr < granules; gr++) {

        {
            struct gr_info_s *gr_infos = &(mp->sideinfo.ch[0].gr[gr]);
            long    part2bits;

            if (fr->lsf)
                part2bits = III_get_scale_factors_2(mp, scalefacs[0], gr_infos, 0);
            else {
                part2bits = III_get_scale_factors_1(mp, scalefacs[0], gr_infos);
            }

            if (mp->pinfo != NULL) {
                int     i;
                mp->pinfo->sfbits[gr][0] = part2bits;
                for (i = 0; i < 39; i++)
                    mp->pinfo->sfb_s[gr][0][i] = scalefacs[0][i];
            }

            /* lame_report_fnc(mp->report_err, "calling III dequantize sample 1 gr_infos->part2_3_length %d\n", gr_infos->part2_3_length); */
            if (III_dequantize_sample(mp, hybridIn[0], scalefacs[0], gr_infos, sfreq, part2bits))
                return clip;
        }
        if (stereo == 2) {
            struct gr_info_s *gr_infos = &(mp->sideinfo.ch[1].gr[gr]);
            long    part2bits;
            if (fr->lsf)
                part2bits = III_get_scale_factors_2(mp, scalefacs[1], gr_infos, i_stereo);
            else {
                part2bits = III_get_scale_factors_1(mp, scalefacs[1], gr_infos);
            }
            if (mp->pinfo != NULL) {
                int     i;
                mp->pinfo->sfbits[gr][1] = part2bits;
                for (i = 0; i < 39; i++)
                    mp->pinfo->sfb_s[gr][1][i] = scalefacs[1][i];
            }

            /* lame_report_fnc(mp->report_err, "calling III dequantize sample 2  gr_infos->part2_3_length %d\n", gr_infos->part2_3_length); */
            if (III_dequantize_sample(mp, hybridIn[1], scalefacs[1], gr_infos, sfreq, part2bits))
                return clip;

            if (ms_stereo) {
                int     i;
                for (i = 0; i < SBLIMIT * SSLIMIT; i++) {
                    real    tmp0, tmp1;
                    tmp0 = ((real *) hybridIn[0])[i];
                    tmp1 = ((real *) hybridIn[1])[i];
                    ((real *) hybridIn[1])[i] = tmp0 - tmp1;
                    ((real *) hybridIn[0])[i] = tmp0 + tmp1;
                }
            }

            if (i_stereo)
                III_i_stereo(hybridIn, scalefacs[1], gr_infos, sfreq, ms_stereo, fr->lsf);

            if (ms_stereo || i_stereo || (single == 3)) {
                if (gr_infos->maxb > mp->sideinfo.ch[0].gr[gr].maxb)
                    mp->sideinfo.ch[0].gr[gr].maxb = gr_infos->maxb;
                else
                    gr_infos->maxb = mp->sideinfo.ch[0].gr[gr].maxb;
            }

            switch (single) {
            case 3:
                {
                    int     i;
                    real   *in0 = (real *) hybridIn[0], *in1 = (real *) hybridIn[1];
                    for (i = 0; i < (int) (SSLIMIT * gr_infos->maxb); i++, in0++)
                        *in0 = (*in0 + *in1++); /* *0.5 done by pow-scale */
                }
                break;
            case 1:
                {
                    int     i;
                    real   *in0 = (real *) hybridIn[0], *in1 = (real *) hybridIn[1];
                    for (i = 0; i < (int) (SSLIMIT * gr_infos->maxb); i++)
                        *in0++ = *in1++;
                }
                break;
            }
        }

        if (mp->pinfo != NULL) {
            int     i, sb;
            float   ifqstep;

            mp->pinfo->bitrate = tabsel_123[fr->lsf][fr->lay - 1][fr->bitrate_index];
            mp->pinfo->sampfreq = freqs[sfreq];
            mp->pinfo->emph = fr->emphasis;
            mp->pinfo->crc = fr->error_protection;
            mp->pinfo->padding = fr->padding;
            mp->pinfo->stereo = fr->stereo;
            mp->pinfo->js = (fr->mode == MPG_MD_JOINT_STEREO);
            mp->pinfo->ms_stereo = ms_stereo;
            mp->pinfo->i_stereo = i_stereo;
            mp->pinfo->maindata = mp->sideinfo.main_data_begin;

            for (ch = 0; ch < stereo1; ch++) {
                struct gr_info_s *gr_infos = &(mp->sideinfo.ch[ch].gr[gr]);
                mp->pinfo->big_values[gr][ch] = gr_infos->big_values;
                mp->pinfo->scalefac_scale[gr][ch] = gr_infos->scalefac_scale;
                mp->pinfo->mixed[gr][ch] = gr_infos->mixed_block_flag;
                mp->pinfo->mpg123blocktype[gr][ch] = gr_infos->block_type;
                mp->pinfo->mainbits[gr][ch] = gr_infos->part2_3_length;
                mp->pinfo->preflag[gr][ch] = gr_infos->preflag;
                if (gr == 1)
                    mp->pinfo->scfsi[ch] = gr_infos->scfsi;
            }


            for (ch = 0; ch < stereo1; ch++) {
                struct gr_info_s *gr_infos = &(mp->sideinfo.ch[ch].gr[gr]);
                ifqstep = (mp->pinfo->scalefac_scale[gr][ch] == 0) ? .5 : 1.0;
                if (2 == gr_infos->block_type) {
                    for (i = 0; i < 3; i++) {
                        for (sb = 0; sb < 12; sb++) {
                            int     j = 3 * sb + i;
                            /*
                               is_p = scalefac[sfb*3+lwin-gr_infos->mixed_block_flag]; 
                             */
                            /* scalefac was copied into pinfo->sfb_s[] above */
                            mp->pinfo->sfb_s[gr][ch][j] =
                                -ifqstep * mp->pinfo->sfb_s[gr][ch][j - gr_infos->mixed_block_flag];
                            mp->pinfo->sfb_s[gr][ch][j] -= 2 * (mp->pinfo->sub_gain[gr][ch][i]);
                        }
                        mp->pinfo->sfb_s[gr][ch][3 * sb + i] =
                            -2 * (mp->pinfo->sub_gain[gr][ch][i]);
                    }
                }
                else {
                    for (sb = 0; sb < 21; sb++) {
                        /* scalefac was copied into pinfo->sfb[] above */
                        mp->pinfo->sfb[gr][ch][sb] = mp->pinfo->sfb_s[gr][ch][sb];
                        if (gr_infos->preflag)
                            mp->pinfo->sfb[gr][ch][sb] += pretab1[sb];
                        mp->pinfo->sfb[gr][ch][sb] *= -ifqstep;
                    }
                    mp->pinfo->sfb[gr][ch][21] = 0;
                }
            }



            for (ch = 0; ch < stereo1; ch++) {
                int     j = 0;
                for (sb = 0; sb < SBLIMIT; sb++)
                    for (ss = 0; ss < SSLIMIT; ss++, j++)
                        mp->pinfo->mpg123xr[gr][ch][j] = hybridIn[ch][sb][ss];
            }
        }


        for (ch = 0; ch < stereo1; ch++) {
            struct gr_info_s *gr_infos = &(mp->sideinfo.ch[ch].gr[gr]);
            III_antialias(hybridIn[ch], gr_infos);
            III_hybrid(mp, hybridIn[ch], hybridOut[ch], ch, gr_infos);
        }

        for (ss = 0; ss < SSLIMIT; ss++) {
            if (single >= 0) {
                clip += (*synth_1to1_mono_ptr) (mp, hybridOut[0][ss], pcm_sample, pcm_point);
            }
            else {
                int     p1 = *pcm_point;
                clip += (*synth_1to1_ptr) (mp, hybridOut[0][ss], 0, pcm_sample, &p1);
                clip += (*synth_1to1_ptr) (mp, hybridOut[1][ss], 1, pcm_sample, pcm_point);
            }
        }
    }

    return clip;
}

static int decodeMP3_clipchoice(PMPSTR mp, unsigned char *in, int isize, char *out, int *done,
                     int (*synth_1to1_mono_ptr) (PMPSTR, real *, unsigned char *, int *),
                     int (*synth_1to1_ptr) (PMPSTR, real *, int, unsigned char *, int *))
{
    int     i, iret, bits, bytes;
    if (in && isize && addbuf(mp, in, isize) == NULL)
        return MP3_ERR;

    /* First decode header */
    if (!mp->header_parsed) {

        if (mp->fsizeold == -1 || mp->sync_bitstream) {
            int     vbrbytes;
            mp->sync_bitstream = 0;

            /* This is the very first call.   sync with anything */
            /* bytes= number of bytes before header */
            bytes = sync_buffer(mp, 0);

            /* now look for Xing VBR header */
            if (mp->bsize >= bytes + XING_HEADER_SIZE) {
                /* vbrbytes = number of bytes in entire vbr header */
                vbrbytes = check_vbr_header(mp, bytes);
            }
            else {
                /* not enough data to look for Xing header */
#ifdef HIP_DEBUG
                lame_report_fnc(mp->report_dbg, "hip: not enough data to look for Xing header\n");
#endif
                return MP3_NEED_MORE;
            }

            if (mp->vbr_header) {
                /* do we have enough data to parse entire Xing header? */
                if (bytes + vbrbytes > mp->bsize) {
                    /* lame_report_fnc(mp->report_err,"hip: not enough data to parse entire Xing header\n"); */
                    return MP3_NEED_MORE;
                }

                /* read in Xing header.  Buffer data in case it
                 * is used by a non zero main_data_begin for the next
                 * frame, but otherwise dont decode Xing header */
#ifdef HIP_DEBUG
                lame_report_fnc(mp->report_dbg, "hip: found xing header, skipping %i bytes\n", vbrbytes + bytes);
#endif
                for (i = 0; i < vbrbytes + bytes; ++i)
                    read_buf_byte(mp);
                /* now we need to find another syncword */
                /* just return and make user send in more data */

                return MP3_NEED_MORE;
            }
        }
        else {
            /* match channels, samplerate, etc, when syncing */
            bytes = sync_buffer(mp, 1);
        }

        /* buffer now synchronized */
        if (bytes < 0) {
            /* lame_report_fnc(mp->report_err,"hip: need more bytes %d\n", bytes); */
            return MP3_NEED_MORE;
        }
        if (bytes > 0) {
            /* there were some extra bytes in front of header.
             * bitstream problem, but we are now resynced 
             * should try to buffer previous data in case new
             * frame has nonzero main_data_begin, but we need
             * to make sure we do not overflow buffer
             */
            int     size;
      //      lame_report_fnc(mp->report_err, "hip: bitstream problem, resyncing skipping %d bytes...\n", bytes);
            mp->old_free_format = 0;
#if 1
            /* FIXME: correct ??? */
            mp->sync_bitstream = 1;
#endif
            /* skip some bytes, buffer the rest */
            size = (int) (mp->wordpointer - (mp->bsspace[mp->bsnum] + 512));

            if (size > MAXFRAMESIZE) {
                /* wordpointer buffer is trashed.  probably cant recover, but try anyway */
     //           lame_report_fnc(mp->report_err, "hip: wordpointer trashed.  size=%i (%i)  bytes=%i \n",
     //                   size, MAXFRAMESIZE, bytes);
                size = 0;
                mp->wordpointer = mp->bsspace[mp->bsnum] + 512;
            }

            /* buffer contains 'size' data right now 
               we want to add 'bytes' worth of data, but do not 
               exceed MAXFRAMESIZE, so we through away 'i' bytes */
            i = (size + bytes) - MAXFRAMESIZE;
            for (; i > 0; --i) {
                --bytes;
                read_buf_byte(mp);
            }

            copy_mp(mp, bytes, mp->wordpointer);
            mp->fsizeold += bytes;
        }

        read_head(mp);
        decode_header(mp, &mp->fr, mp->header);
        mp->header_parsed = 1;
        mp->framesize = mp->fr.framesize;
        mp->free_format = (mp->framesize == 0);

        if (mp->fr.lsf)
            mp->ssize = (mp->fr.stereo == 1) ? 9 : 17;
        else
            mp->ssize = (mp->fr.stereo == 1) ? 17 : 32;
        if (mp->fr.error_protection)
            mp->ssize += 2;

        mp->bsnum = 1 - mp->bsnum; /* toggle buffer */
        mp->wordpointer = mp->bsspace[mp->bsnum] + 512;
        mp->bitindex = 0;

        /* for very first header, never parse rest of data */
        if (mp->fsizeold == -1) {
#ifdef HIP_DEBUG
            lame_report_fnc(mp->report_dbg, "hip: not parsing the rest of the data of the first header\n");
#endif
            return MP3_NEED_MORE;
        }
    }                   /* end of header parsing block */

    /* now decode side information */
    if (!mp->side_parsed) {

        /* Layer 3 only */
        if (mp->fr.lay == 3) {
            if (mp->bsize < mp->ssize)
                return MP3_NEED_MORE;

            copy_mp(mp, mp->ssize, mp->wordpointer);

            if (mp->fr.error_protection)
                getbits(mp, 16);
            bits = decode_layer3_sideinfo(mp);
            /* bits = actual number of bits needed to parse this frame */
            /* can be negative, if all bits needed are in the reservoir */
            if (bits < 0)
                bits = 0;

            /* read just as many bytes as necessary before decoding */
            mp->dsize = (bits + 7) / 8;
            
            if (!mp->free_format) {
                /* do not read more than framsize data */
                int framesize = mp->fr.framesize - mp->ssize;
                if (mp->dsize > framesize) {
                    lame_report_fnc(mp->report_err,
                            "hip: error audio data exceeds framesize by %d bytes\n", 
                            mp->dsize - framesize);
                    mp->dsize = framesize;
                }
            }
#ifdef HIP_DEBUG
            lame_report_fnc(mp->report_dbg,
                    "hip: %d bits needed to parse layer III frame, number of bytes to read before decoding dsize = %d\n",
                    bits, mp->dsize);
#endif

            /* this will force mpglib to read entire frame before decoding */
            /* mp->dsize= mp->framesize - mp->ssize; */

        }
        else {
            /* Layers 1 and 2 */

            /* check if there is enough input data */
            if (mp->fr.framesize > mp->bsize)
                return MP3_NEED_MORE;

            /* takes care that the right amount of data is copied into wordpointer */
            mp->dsize = mp->fr.framesize;
            mp->ssize = 0;
        }

        mp->side_parsed = 1;
    }

    /* now decode main data */
    iret = MP3_NEED_MORE;
    if (!mp->data_parsed) {
        if (mp->dsize > mp->bsize) {
            return MP3_NEED_MORE;
        }

        copy_mp(mp, mp->dsize, mp->wordpointer);

        *done = 0;

        /*do_layer3(&mp->fr,(unsigned char *) out,done); */
        switch (mp->fr.lay) {
        case 1:
            if (mp->fr.error_protection)
                getbits(mp, 16);

        //    decode_layer1_frame(mp, (unsigned char *) out, done);
            break;

        case 2:
            if (mp->fr.error_protection)
                getbits(mp, 16);

        //    decode_layer2_frame(mp, (unsigned char *) out, done);
            break;

        case 3:
            decode_layer3_frame(mp, (unsigned char *) out, done, synth_1to1_mono_ptr, synth_1to1_ptr);
            break;
        default:
            lame_report_fnc(mp->report_err, "hip: invalid layer %d\n", mp->fr.lay);
        }

        mp->wordpointer = mp->bsspace[mp->bsnum] + 512 + mp->ssize + mp->dsize;

        mp->data_parsed = 1;
        iret = MP3_OK;
    }


    /* remaining bits are ancillary data, or reservoir for next frame 
     * If free format, scan stream looking for next frame to determine
     * mp->framesize */
    if (mp->free_format) {
        if (mp->old_free_format) {
            /* free format.  bitrate must not vary */
            mp->framesize = mp->fsizeold_nopadding + (mp->fr.padding);
        }
        else {
            bytes = sync_buffer(mp, 1);
            if (bytes < 0)
                return iret;
            mp->framesize = bytes + mp->ssize + mp->dsize;
            mp->fsizeold_nopadding = mp->framesize - mp->fr.padding;
#if 0
               lame_report_fnc(mp->report_dbg,"hip: freeformat bitstream:  estimated bitrate=%ikbs  \n",
               8*(4+mp->framesize)*freqs[mp->fr.sampling_frequency]/
               (1000*576*(2-mp->fr.lsf)));
#endif
        }
    }

    /* buffer the ancillary data and reservoir for next frame */
    bytes = mp->framesize - (mp->ssize + mp->dsize);
    if (bytes > mp->bsize) {
        return iret;
    }

    if (bytes > 0) {
        int     size;
#if 1
        /* FIXME: while loop OK ??? */
        while (bytes > 512) {
            read_buf_byte(mp);
            bytes--;
            mp->framesize--;
        }
#endif
        copy_mp(mp, bytes, mp->wordpointer);
        mp->wordpointer += bytes;

        size = (int) (mp->wordpointer - (mp->bsspace[mp->bsnum] + 512));
        if (size > MAXFRAMESIZE) {
            lame_report_fnc(mp->report_err, "hip: fatal error.  MAXFRAMESIZE not large enough.\n");
        }

    }

    /* the above frame is completely parsed.  start looking for next frame */
    mp->fsizeold = mp->framesize;
    mp->old_free_format = mp->free_format;
    mp->framesize = 0;
    mp->header_parsed = 0;
    mp->side_parsed = 0;
    mp->data_parsed = 0;

    return iret;
}
/* copy mono samples */
#define COPY_MONO(DST_TYPE, SRC_TYPE)                                                           \
    DST_TYPE *pcm_l = (DST_TYPE *)pcm_l_raw;                                                    \
    SRC_TYPE const *p_samples = (SRC_TYPE const *)p;                                            \
    for (i = 0; i < processed_samples; i++)                                                     \
      *pcm_l++ = (DST_TYPE)(*p_samples++);

/* copy stereo samples */
#define COPY_STEREO(DST_TYPE, SRC_TYPE)                                                         \
    DST_TYPE *pcm_l = (DST_TYPE *)pcm_l_raw, *pcm_r = (DST_TYPE *)pcm_r_raw;                    \
    SRC_TYPE const *p_samples = (SRC_TYPE const *)p;                                            \
    for (i = 0; i < processed_samples; i++) {                                                   \
      *pcm_l++ = (DST_TYPE)(*p_samples++);                                                      \
      *pcm_r++ = (DST_TYPE)(*p_samples++);                                                      \
    }
static int
decode1_headersB_clipchoice(PMPSTR pmp, unsigned char *buffer, int len,
                            char pcm_l_raw[], char pcm_r_raw[], mp3data_struct * mp3data,
                            int *enc_delay, int *enc_padding,
                            char *p, size_t psize, int decoded_sample_size,
                            int (*decodeMP3_ptr) (PMPSTR, unsigned char *, int, char *, int,
                            int *))
{
    static const int smpls[2][4] = {
        /* Layer   I    II   III */
        {0, 384, 1152, 1152}, /* MPEG-1     */
        {0, 384, 1152, 576} /* MPEG-2(.5) */
    };

    int     processed_bytes;
    int     processed_samples; /* processed samples per channel */
    int     ret;
    int     i;

    mp3data->header_parsed = 0;

    ret = (*decodeMP3_ptr) (pmp, buffer, len, p, (int) psize, &processed_bytes);
    /* three cases:  
     * 1. headers parsed, but data not complete
     *       pmp->header_parsed==1 
     *       pmp->framesize=0           
     *       pmp->fsizeold=size of last frame, or 0 if this is first frame
     *
     * 2. headers, data parsed, but ancillary data not complete
     *       pmp->header_parsed==1 
     *       pmp->framesize=size of frame           
     *       pmp->fsizeold=size of last frame, or 0 if this is first frame
     *
     * 3. frame fully decoded:  
     *       pmp->header_parsed==0 
     *       pmp->framesize=0           
     *       pmp->fsizeold=size of frame (which is now the last frame)
     *
     */
    if (pmp->header_parsed || pmp->fsizeold > 0 || pmp->framesize > 0) {
        mp3data->header_parsed = 1;
        mp3data->stereo = pmp->fr.stereo;
        mp3data->samplerate = freqs[pmp->fr.sampling_frequency];
        mp3data->mode = pmp->fr.mode;
        mp3data->mode_ext = pmp->fr.mode_ext;
        mp3data->framesize = smpls[pmp->fr.lsf][pmp->fr.lay];

        /* free format, we need the entire frame before we can determine
         * the bitrate.  If we haven't gotten the entire frame, bitrate=0 */
        if (pmp->fsizeold > 0) /* works for free format and fixed, no overrun, temporal results are < 400.e6 */
            mp3data->bitrate = 8 * (4 + pmp->fsizeold) * mp3data->samplerate /
                (1.e3 * mp3data->framesize) + 0.5;
        else if (pmp->framesize > 0)
            mp3data->bitrate = 8 * (4 + pmp->framesize) * mp3data->samplerate /
                (1.e3 * mp3data->framesize) + 0.5;
        else
            mp3data->bitrate = tabsel_123[pmp->fr.lsf][pmp->fr.lay - 1][pmp->fr.bitrate_index];



        if (pmp->num_frames > 0) {
            /* Xing VBR header found and num_frames was set */
            mp3data->totalframes = pmp->num_frames;
            mp3data->nsamp = mp3data->framesize * pmp->num_frames;
            *enc_delay = pmp->enc_delay;
            *enc_padding = pmp->enc_padding;
        }
    }

    switch (ret) {
    case MP3_OK:
        switch (pmp->fr.stereo) {
        case 1:
            processed_samples = processed_bytes / decoded_sample_size;
            if (decoded_sample_size == sizeof(short)) {
                COPY_MONO(short, short)
            }
            else {
                COPY_MONO(sample_t, FLOAT)
            }
            break;
        case 2:
            processed_samples = (processed_bytes / decoded_sample_size) >> 1;
            if (decoded_sample_size == sizeof(short)) {
                COPY_STEREO(short, short)
            }
            else {
                COPY_STEREO(sample_t, FLOAT)
            }
            break;
        default:
            processed_samples = -1;
            assert(0);
            break;
        }
        break;

    case MP3_NEED_MORE:
        processed_samples = 0;
        break;

    case MP3_ERR:
        processed_samples = -1;
        break;

    default:
        processed_samples = -1;
        assert(0);
        break;
    }

    /*fprintf(stderr,"ok, more, err:  %i %i %i\n", MP3_OK, MP3_NEED_MORE, MP3_ERR ); */
    /*fprintf(stderr,"ret = %i out=%i\n", ret, processed_samples ); */
    return processed_samples;
}

static void
dct64_1(real * out0, real * out1, real * b1, real * b2, real * samples)
{

    {
        real   *costab = pnts[0];

        b1[0x00] = samples[0x00] + samples[0x1F];
        b1[0x1F] = (samples[0x00] - samples[0x1F]) * costab[0x0];

        b1[0x01] = samples[0x01] + samples[0x1E];
        b1[0x1E] = (samples[0x01] - samples[0x1E]) * costab[0x1];

        b1[0x02] = samples[0x02] + samples[0x1D];
        b1[0x1D] = (samples[0x02] - samples[0x1D]) * costab[0x2];

        b1[0x03] = samples[0x03] + samples[0x1C];
        b1[0x1C] = (samples[0x03] - samples[0x1C]) * costab[0x3];

        b1[0x04] = samples[0x04] + samples[0x1B];
        b1[0x1B] = (samples[0x04] - samples[0x1B]) * costab[0x4];

        b1[0x05] = samples[0x05] + samples[0x1A];
        b1[0x1A] = (samples[0x05] - samples[0x1A]) * costab[0x5];

        b1[0x06] = samples[0x06] + samples[0x19];
        b1[0x19] = (samples[0x06] - samples[0x19]) * costab[0x6];

        b1[0x07] = samples[0x07] + samples[0x18];
        b1[0x18] = (samples[0x07] - samples[0x18]) * costab[0x7];

        b1[0x08] = samples[0x08] + samples[0x17];
        b1[0x17] = (samples[0x08] - samples[0x17]) * costab[0x8];

        b1[0x09] = samples[0x09] + samples[0x16];
        b1[0x16] = (samples[0x09] - samples[0x16]) * costab[0x9];

        b1[0x0A] = samples[0x0A] + samples[0x15];
        b1[0x15] = (samples[0x0A] - samples[0x15]) * costab[0xA];

        b1[0x0B] = samples[0x0B] + samples[0x14];
        b1[0x14] = (samples[0x0B] - samples[0x14]) * costab[0xB];

        b1[0x0C] = samples[0x0C] + samples[0x13];
        b1[0x13] = (samples[0x0C] - samples[0x13]) * costab[0xC];

        b1[0x0D] = samples[0x0D] + samples[0x12];
        b1[0x12] = (samples[0x0D] - samples[0x12]) * costab[0xD];

        b1[0x0E] = samples[0x0E] + samples[0x11];
        b1[0x11] = (samples[0x0E] - samples[0x11]) * costab[0xE];

        b1[0x0F] = samples[0x0F] + samples[0x10];
        b1[0x10] = (samples[0x0F] - samples[0x10]) * costab[0xF];
    }


    {
        real   *costab = pnts[1];

        b2[0x00] = b1[0x00] + b1[0x0F];
        b2[0x0F] = (b1[0x00] - b1[0x0F]) * costab[0];
        b2[0x01] = b1[0x01] + b1[0x0E];
        b2[0x0E] = (b1[0x01] - b1[0x0E]) * costab[1];
        b2[0x02] = b1[0x02] + b1[0x0D];
        b2[0x0D] = (b1[0x02] - b1[0x0D]) * costab[2];
        b2[0x03] = b1[0x03] + b1[0x0C];
        b2[0x0C] = (b1[0x03] - b1[0x0C]) * costab[3];
        b2[0x04] = b1[0x04] + b1[0x0B];
        b2[0x0B] = (b1[0x04] - b1[0x0B]) * costab[4];
        b2[0x05] = b1[0x05] + b1[0x0A];
        b2[0x0A] = (b1[0x05] - b1[0x0A]) * costab[5];
        b2[0x06] = b1[0x06] + b1[0x09];
        b2[0x09] = (b1[0x06] - b1[0x09]) * costab[6];
        b2[0x07] = b1[0x07] + b1[0x08];
        b2[0x08] = (b1[0x07] - b1[0x08]) * costab[7];

        b2[0x10] = b1[0x10] + b1[0x1F];
        b2[0x1F] = (b1[0x1F] - b1[0x10]) * costab[0];
        b2[0x11] = b1[0x11] + b1[0x1E];
        b2[0x1E] = (b1[0x1E] - b1[0x11]) * costab[1];
        b2[0x12] = b1[0x12] + b1[0x1D];
        b2[0x1D] = (b1[0x1D] - b1[0x12]) * costab[2];
        b2[0x13] = b1[0x13] + b1[0x1C];
        b2[0x1C] = (b1[0x1C] - b1[0x13]) * costab[3];
        b2[0x14] = b1[0x14] + b1[0x1B];
        b2[0x1B] = (b1[0x1B] - b1[0x14]) * costab[4];
        b2[0x15] = b1[0x15] + b1[0x1A];
        b2[0x1A] = (b1[0x1A] - b1[0x15]) * costab[5];
        b2[0x16] = b1[0x16] + b1[0x19];
        b2[0x19] = (b1[0x19] - b1[0x16]) * costab[6];
        b2[0x17] = b1[0x17] + b1[0x18];
        b2[0x18] = (b1[0x18] - b1[0x17]) * costab[7];
    }

    {
        real   *costab = pnts[2];

        b1[0x00] = b2[0x00] + b2[0x07];
        b1[0x07] = (b2[0x00] - b2[0x07]) * costab[0];
        b1[0x01] = b2[0x01] + b2[0x06];
        b1[0x06] = (b2[0x01] - b2[0x06]) * costab[1];
        b1[0x02] = b2[0x02] + b2[0x05];
        b1[0x05] = (b2[0x02] - b2[0x05]) * costab[2];
        b1[0x03] = b2[0x03] + b2[0x04];
        b1[0x04] = (b2[0x03] - b2[0x04]) * costab[3];

        b1[0x08] = b2[0x08] + b2[0x0F];
        b1[0x0F] = (b2[0x0F] - b2[0x08]) * costab[0];
        b1[0x09] = b2[0x09] + b2[0x0E];
        b1[0x0E] = (b2[0x0E] - b2[0x09]) * costab[1];
        b1[0x0A] = b2[0x0A] + b2[0x0D];
        b1[0x0D] = (b2[0x0D] - b2[0x0A]) * costab[2];
        b1[0x0B] = b2[0x0B] + b2[0x0C];
        b1[0x0C] = (b2[0x0C] - b2[0x0B]) * costab[3];

        b1[0x10] = b2[0x10] + b2[0x17];
        b1[0x17] = (b2[0x10] - b2[0x17]) * costab[0];
        b1[0x11] = b2[0x11] + b2[0x16];
        b1[0x16] = (b2[0x11] - b2[0x16]) * costab[1];
        b1[0x12] = b2[0x12] + b2[0x15];
        b1[0x15] = (b2[0x12] - b2[0x15]) * costab[2];
        b1[0x13] = b2[0x13] + b2[0x14];
        b1[0x14] = (b2[0x13] - b2[0x14]) * costab[3];

        b1[0x18] = b2[0x18] + b2[0x1F];
        b1[0x1F] = (b2[0x1F] - b2[0x18]) * costab[0];
        b1[0x19] = b2[0x19] + b2[0x1E];
        b1[0x1E] = (b2[0x1E] - b2[0x19]) * costab[1];
        b1[0x1A] = b2[0x1A] + b2[0x1D];
        b1[0x1D] = (b2[0x1D] - b2[0x1A]) * costab[2];
        b1[0x1B] = b2[0x1B] + b2[0x1C];
        b1[0x1C] = (b2[0x1C] - b2[0x1B]) * costab[3];
    }

    {
        real const cos0 = pnts[3][0];
        real const cos1 = pnts[3][1];

        b2[0x00] = b1[0x00] + b1[0x03];
        b2[0x03] = (b1[0x00] - b1[0x03]) * cos0;
        b2[0x01] = b1[0x01] + b1[0x02];
        b2[0x02] = (b1[0x01] - b1[0x02]) * cos1;

        b2[0x04] = b1[0x04] + b1[0x07];
        b2[0x07] = (b1[0x07] - b1[0x04]) * cos0;
        b2[0x05] = b1[0x05] + b1[0x06];
        b2[0x06] = (b1[0x06] - b1[0x05]) * cos1;

        b2[0x08] = b1[0x08] + b1[0x0B];
        b2[0x0B] = (b1[0x08] - b1[0x0B]) * cos0;
        b2[0x09] = b1[0x09] + b1[0x0A];
        b2[0x0A] = (b1[0x09] - b1[0x0A]) * cos1;

        b2[0x0C] = b1[0x0C] + b1[0x0F];
        b2[0x0F] = (b1[0x0F] - b1[0x0C]) * cos0;
        b2[0x0D] = b1[0x0D] + b1[0x0E];
        b2[0x0E] = (b1[0x0E] - b1[0x0D]) * cos1;

        b2[0x10] = b1[0x10] + b1[0x13];
        b2[0x13] = (b1[0x10] - b1[0x13]) * cos0;
        b2[0x11] = b1[0x11] + b1[0x12];
        b2[0x12] = (b1[0x11] - b1[0x12]) * cos1;

        b2[0x14] = b1[0x14] + b1[0x17];
        b2[0x17] = (b1[0x17] - b1[0x14]) * cos0;
        b2[0x15] = b1[0x15] + b1[0x16];
        b2[0x16] = (b1[0x16] - b1[0x15]) * cos1;

        b2[0x18] = b1[0x18] + b1[0x1B];
        b2[0x1B] = (b1[0x18] - b1[0x1B]) * cos0;
        b2[0x19] = b1[0x19] + b1[0x1A];
        b2[0x1A] = (b1[0x19] - b1[0x1A]) * cos1;

        b2[0x1C] = b1[0x1C] + b1[0x1F];
        b2[0x1F] = (b1[0x1F] - b1[0x1C]) * cos0;
        b2[0x1D] = b1[0x1D] + b1[0x1E];
        b2[0x1E] = (b1[0x1E] - b1[0x1D]) * cos1;
    }

    {
        real const cos0 = pnts[4][0];

        b1[0x00] = b2[0x00] + b2[0x01];
        b1[0x01] = (b2[0x00] - b2[0x01]) * cos0;
        b1[0x02] = b2[0x02] + b2[0x03];
        b1[0x03] = (b2[0x03] - b2[0x02]) * cos0;
        b1[0x02] += b1[0x03];

        b1[0x04] = b2[0x04] + b2[0x05];
        b1[0x05] = (b2[0x04] - b2[0x05]) * cos0;
        b1[0x06] = b2[0x06] + b2[0x07];
        b1[0x07] = (b2[0x07] - b2[0x06]) * cos0;
        b1[0x06] += b1[0x07];
        b1[0x04] += b1[0x06];
        b1[0x06] += b1[0x05];
        b1[0x05] += b1[0x07];

        b1[0x08] = b2[0x08] + b2[0x09];
        b1[0x09] = (b2[0x08] - b2[0x09]) * cos0;
        b1[0x0A] = b2[0x0A] + b2[0x0B];
        b1[0x0B] = (b2[0x0B] - b2[0x0A]) * cos0;
        b1[0x0A] += b1[0x0B];

        b1[0x0C] = b2[0x0C] + b2[0x0D];
        b1[0x0D] = (b2[0x0C] - b2[0x0D]) * cos0;
        b1[0x0E] = b2[0x0E] + b2[0x0F];
        b1[0x0F] = (b2[0x0F] - b2[0x0E]) * cos0;
        b1[0x0E] += b1[0x0F];
        b1[0x0C] += b1[0x0E];
        b1[0x0E] += b1[0x0D];
        b1[0x0D] += b1[0x0F];

        b1[0x10] = b2[0x10] + b2[0x11];
        b1[0x11] = (b2[0x10] - b2[0x11]) * cos0;
        b1[0x12] = b2[0x12] + b2[0x13];
        b1[0x13] = (b2[0x13] - b2[0x12]) * cos0;
        b1[0x12] += b1[0x13];

        b1[0x14] = b2[0x14] + b2[0x15];
        b1[0x15] = (b2[0x14] - b2[0x15]) * cos0;
        b1[0x16] = b2[0x16] + b2[0x17];
        b1[0x17] = (b2[0x17] - b2[0x16]) * cos0;
        b1[0x16] += b1[0x17];
        b1[0x14] += b1[0x16];
        b1[0x16] += b1[0x15];
        b1[0x15] += b1[0x17];

        b1[0x18] = b2[0x18] + b2[0x19];
        b1[0x19] = (b2[0x18] - b2[0x19]) * cos0;
        b1[0x1A] = b2[0x1A] + b2[0x1B];
        b1[0x1B] = (b2[0x1B] - b2[0x1A]) * cos0;
        b1[0x1A] += b1[0x1B];

        b1[0x1C] = b2[0x1C] + b2[0x1D];
        b1[0x1D] = (b2[0x1C] - b2[0x1D]) * cos0;
        b1[0x1E] = b2[0x1E] + b2[0x1F];
        b1[0x1F] = (b2[0x1F] - b2[0x1E]) * cos0;
        b1[0x1E] += b1[0x1F];
        b1[0x1C] += b1[0x1E];
        b1[0x1E] += b1[0x1D];
        b1[0x1D] += b1[0x1F];
    }

    out0[0x10 * 16] = b1[0x00];
    out0[0x10 * 12] = b1[0x04];
    out0[0x10 * 8] = b1[0x02];
    out0[0x10 * 4] = b1[0x06];
    out0[0x10 * 0] = b1[0x01];
    out1[0x10 * 0] = b1[0x01];
    out1[0x10 * 4] = b1[0x05];
    out1[0x10 * 8] = b1[0x03];
    out1[0x10 * 12] = b1[0x07];

    b1[0x08] += b1[0x0C];
    out0[0x10 * 14] = b1[0x08];
    b1[0x0C] += b1[0x0a];
    out0[0x10 * 10] = b1[0x0C];
    b1[0x0A] += b1[0x0E];
    out0[0x10 * 6] = b1[0x0A];
    b1[0x0E] += b1[0x09];
    out0[0x10 * 2] = b1[0x0E];
    b1[0x09] += b1[0x0D];
    out1[0x10 * 2] = b1[0x09];
    b1[0x0D] += b1[0x0B];
    out1[0x10 * 6] = b1[0x0D];
    b1[0x0B] += b1[0x0F];
    out1[0x10 * 10] = b1[0x0B];
    out1[0x10 * 14] = b1[0x0F];

    b1[0x18] += b1[0x1C];
    out0[0x10 * 15] = b1[0x10] + b1[0x18];
    out0[0x10 * 13] = b1[0x18] + b1[0x14];
    b1[0x1C] += b1[0x1a];
    out0[0x10 * 11] = b1[0x14] + b1[0x1C];
    out0[0x10 * 9] = b1[0x1C] + b1[0x12];
    b1[0x1A] += b1[0x1E];
    out0[0x10 * 7] = b1[0x12] + b1[0x1A];
    out0[0x10 * 5] = b1[0x1A] + b1[0x16];
    b1[0x1E] += b1[0x19];
    out0[0x10 * 3] = b1[0x16] + b1[0x1E];
    out0[0x10 * 1] = b1[0x1E] + b1[0x11];
    b1[0x19] += b1[0x1D];
    out1[0x10 * 1] = b1[0x11] + b1[0x19];
    out1[0x10 * 3] = b1[0x19] + b1[0x15];
    b1[0x1D] += b1[0x1B];
    out1[0x10 * 5] = b1[0x15] + b1[0x1D];
    out1[0x10 * 7] = b1[0x1D] + b1[0x13];
    b1[0x1B] += b1[0x1F];
    out1[0x10 * 9] = b1[0x13] + b1[0x1B];
    out1[0x10 * 11] = b1[0x1B] + b1[0x17];
    out1[0x10 * 13] = b1[0x17] + b1[0x1F];
    out1[0x10 * 15] = b1[0x1F];
}

/*
 * the call via dct64 is a trick to force GCC to use
 * (new) registers for the b1,b2 pointer to the bufs[xx] field
 */
void
dct64(real * a, real * b, real * c)
{
    real    bufs[0x40];
    dct64_1(a, b, bufs, bufs + 0x20, c);
}

#define SYNTH_1TO1_CLIPCHOICE(TYPE,WRITE_SAMPLE)         \
  static const int step = 2;                             \
  int bo;                                                \
  TYPE *samples = (TYPE *) (out + *pnt);                 \
                                                         \
  real *b0,(*buf)[0x110];                                \
  int clip = 0;                                          \
  int bo1;                                               \
                                                         \
  bo = mp->synth_bo;                                     \
                                                         \
  if(!channel) {                                         \
    bo--;                                                \
    bo &= 0xf;                                           \
    buf = mp->synth_buffs[0];                            \
  }                                                      \
  else {                                                 \
    samples++;                                           \
    buf = mp->synth_buffs[1];                            \
  }                                                      \
                                                         \
  if(bo & 0x1) {                                         \
    b0 = buf[0];                                         \
    bo1 = bo;                                            \
    dct64(buf[1]+((bo+1)&0xf),buf[0]+bo,bandPtr);        \
  }                                                      \
  else {                                                 \
    b0 = buf[1];                                         \
    bo1 = bo+1;                                          \
    dct64(buf[0]+bo,buf[1]+bo+1,bandPtr);                \
  }                                                      \
                                                         \
  mp->synth_bo = bo;                                     \
                                                         \
  {                                                      \
    int j;                                               \
    real *window = decwin + 16 - bo1;                    \
                                                         \
    for (j=16;j;j--,b0+=0x10,window+=0x20,samples+=step) \
    {                                                    \
      real sum;                                          \
      sum  = window[0x0] * b0[0x0];                      \
      sum -= window[0x1] * b0[0x1];                      \
      sum += window[0x2] * b0[0x2];                      \
      sum -= window[0x3] * b0[0x3];                      \
      sum += window[0x4] * b0[0x4];                      \
      sum -= window[0x5] * b0[0x5];                      \
      sum += window[0x6] * b0[0x6];                      \
      sum -= window[0x7] * b0[0x7];                      \
      sum += window[0x8] * b0[0x8];                      \
      sum -= window[0x9] * b0[0x9];                      \
      sum += window[0xA] * b0[0xA];                      \
      sum -= window[0xB] * b0[0xB];                      \
      sum += window[0xC] * b0[0xC];                      \
      sum -= window[0xD] * b0[0xD];                      \
      sum += window[0xE] * b0[0xE];                      \
      sum -= window[0xF] * b0[0xF];                      \
                                                         \
      WRITE_SAMPLE (TYPE,samples,sum,clip);              \
    }                                                    \
                                                         \
    {                                                    \
      real sum;                                          \
      sum  = window[0x0] * b0[0x0];                      \
      sum += window[0x2] * b0[0x2];                      \
      sum += window[0x4] * b0[0x4];                      \
      sum += window[0x6] * b0[0x6];                      \
      sum += window[0x8] * b0[0x8];                      \
      sum += window[0xA] * b0[0xA];                      \
      sum += window[0xC] * b0[0xC];                      \
      sum += window[0xE] * b0[0xE];                      \
      WRITE_SAMPLE (TYPE,samples,sum,clip);              \
      b0-=0x10,window-=0x20,samples+=step;               \
    }                                                    \
    window += bo1<<1;                                    \
                                                         \
    for (j=15;j;j--,b0-=0x10,window-=0x20,samples+=step) \
    {                                                    \
      real sum;                                          \
      sum = -window[-0x1] * b0[0x0];                     \
      sum -= window[-0x2] * b0[0x1];                     \
      sum -= window[-0x3] * b0[0x2];                     \
      sum -= window[-0x4] * b0[0x3];                     \
      sum -= window[-0x5] * b0[0x4];                     \
      sum -= window[-0x6] * b0[0x5];                     \
      sum -= window[-0x7] * b0[0x6];                     \
      sum -= window[-0x8] * b0[0x7];                     \
      sum -= window[-0x9] * b0[0x8];                     \
      sum -= window[-0xA] * b0[0x9];                     \
      sum -= window[-0xB] * b0[0xA];                     \
      sum -= window[-0xC] * b0[0xB];                     \
      sum -= window[-0xD] * b0[0xC];                     \
      sum -= window[-0xE] * b0[0xD];                     \
      sum -= window[-0xF] * b0[0xE];                     \
      sum -= window[-0x0] * b0[0xF];                     \
                                                         \
      WRITE_SAMPLE (TYPE,samples,sum,clip);              \
    }                                                    \
  }                                                      \
  *pnt += 64*sizeof(TYPE);                               \
                                                         \
  return clip;          
 /* old WRITE_SAMPLE_CLIPPED */
#define WRITE_SAMPLE_CLIPPED(TYPE,samples,sum,clip) \
  if( (sum) > 32767.0) { *(samples) = 0x7fff; (clip)++; } \
  else if( (sum) < -32768.0) { *(samples) = -0x8000; (clip)++; } \
  else { *(samples) = (TYPE)((sum)>0 ? (sum)+0.5 : (sum)-0.5) ; }

#define WRITE_SAMPLE_UNCLIPPED(TYPE,samples,sum,clip) \
  *samples = (TYPE)sum;

  /* *INDENT-OFF* */

 /* versions: clipped (when TYPE == short) and unclipped (when TYPE == real) of synth_1to1_mono* functions */
#define SYNTH_1TO1_MONO_CLIPCHOICE(TYPE,SYNTH_1TO1)                    \
  TYPE samples_tmp[64];                                                \
  TYPE *tmp1 = samples_tmp;                                            \
  int i,ret;                                                           \
  int pnt1 = 0;                                                        \
                                                                       \
  ret = SYNTH_1TO1 (mp,bandPtr,0,(unsigned char *) samples_tmp,&pnt1); \
  out += *pnt;                                                         \
                                                                       \
  for(i=0;i<32;i++) {                                                  \
    *( (TYPE *) out) = *tmp1;                                          \
    out += sizeof(TYPE);                                               \
    tmp1 += 2;                                                         \
  }                                                                    \
  *pnt += 32*sizeof(TYPE);                                             \
                                                                       \
  return ret; 

int
synth_1to1(PMPSTR mp, real * bandPtr, int channel, unsigned char *out, int *pnt)
{
    SYNTH_1TO1_CLIPCHOICE(short, WRITE_SAMPLE_CLIPPED)
}
int
synth_1to1_mono(PMPSTR mp, real * bandPtr, unsigned char *out, int *pnt)
{
    SYNTH_1TO1_MONO_CLIPCHOICE(short, synth_1to1)
}


int
decodeMP3(PMPSTR mp, unsigned char *in, int isize, char *out, int osize, int *done)
{
    if (osize < 4608) {
    //    lame_report_fnc(mp->report_err, "hip: Insufficient memory for decoding buffer %d\n", osize);
        return MP3_ERR;
    }

    /* passing pointers to the functions which clip the samples */
    return decodeMP3_clipchoice(mp, in, isize, out, done, synth_1to1_mono, synth_1to1);
}
int hip_decode1_headersB(hip_t hip, unsigned char *buffer,
                      size_t len,
                      short pcm_l[], short pcm_r[], mp3data_struct * mp3data,
                      int *enc_delay, int *enc_padding)
{
    static char out[OUTSIZE_CLIPPED];
    if (hip) {
        return decode1_headersB_clipchoice(hip, buffer, len, (char *) pcm_l, (char *) pcm_r, mp3data,
                                           enc_delay, enc_padding, out, OUTSIZE_CLIPPED,
                                           sizeof(short), decodeMP3);
    }
    return -1;
}

static int lame_decode_initfile(int* enc_delay, int *enc_padding) {

	byte buf[100];
	int aid_header;
	int freeformat = 0;
	int len = 4;
	if (read_byte(buf, len) != len)
		return -1;
	while (buf[0] == 'I' && buf[1] == 'D' && buf[2] == '3') {
		len = 6;
		if (read_byte(&buf[4], len) != len)
			return -1;
        len = lenOfId3v2Tag(&buf[6]);
        if (global.in_id3v2_size < 1) {
            global.in_id3v2_size = 10 + len;
			global.in_id3v2_tag = (unsigned char*)malloc(global.in_id3v2_size);
            if (global.in_id3v2_tag) {
                memcpy(global.in_id3v2_tag, buf, 10);
				if (read_byte(global.in_id3v2_tag + 10, len) != len)
					return -1;
                len = 0; /* copied, nothing to skip */
            }
            else {
                global.in_id3v2_size = 0;
            }
        }
		skip_byte(len);
        len = 4;
		if (read_byte(buf, len) != len)
			return -1;
	}

	aid_header = check_aid(buf);
    if (aid_header) {
        if (read_byte(&buf, 2) != 2)
			return -1;
        aid_header = (unsigned char) buf[0] + 256 * (unsigned char) buf[1];
        /* skip rest of AID, except for 6 bytes we have already read */
        skip_byte(aid_header - 6);

        /* read 4 more bytes to set up buffer for MP3 header check */
        if (read_byte(&buf, len)!=len)
			return -1;
    }
	len = 4;
    while (!is_syncword_mp123(buf)) {
        unsigned int i;
        for (i = 0; i < len - 1; i++)
            buf[i] = buf[i + 1];
		if (read_byte(buf + len - 1, 1) != 1)
			return -1;
    }

    if ((buf[2] & 0xf0) == 0) {
        freeformat = 1;
    }

	short int pcm_l[1152], pcm_r[1152];
	
	mp3data_struct * mp3data = &global_decoder.mp3input_data;
	int ret = hip_decode1_headersB(global.hip, buf, len, pcm_l, pcm_r, mp3data, enc_delay, enc_padding);
    if (-1 == ret)
        return -1;

    /* repeat until we decode a valid mp3 header.  */
    while (!mp3data->header_parsed) {
		len = read_byte(buf, sizeof(buf));
		if (len != sizeof(buf))
            return -1;
        ret =
            hip_decode1_headersB(global.hip, buf, len, pcm_l, pcm_r, mp3data, enc_delay,
                                 enc_padding);
        if (-1 == ret)
            return -1;
    }

    if (mp3data->bitrate == 0 && !freeformat) {
        return lame_decode_initfile(enc_delay, enc_padding);
    }

    if (mp3data->totalframes > 0) {
        /* mpglib found a Xing VBR header and computed nsamp & totalframes */
    }
    else {
        /* set as unknown.  Later, we will take a guess based on file size
         * ant bitrate */
        mp3data->nsamp = MAX_U_32_NUM;
    }


    /*
       report_printf("ret = %i NEED_MORE=%i \n",ret,MP3_NEED_MORE);
       report_printf("stereo = %i \n",mp.fr.stereo);
       report_printf("samp = %i  \n",freqs[mp.fr.sampling_frequency]);
       report_printf("framesize = %i  \n",framesize);
       report_printf("bitrate = %i  \n",mp3data->bitrate);
       report_printf("num frames = %ui  \n",num_frames);
       report_printf("num samp = %ui  \n",mp3data->nsamp);
       report_printf("mode     = %i  \n",mp.fr.mode);
     */

	return 0;
}
int
is_lame_global_flags_valid(const lame_global_flags * gfp)
{
    if (gfp == NULL)
        return 0;
    if (gfp->class_id != LAME_ID)
        return 0;
    return 1;
}
/* number of channels in input stream */
int
lame_set_num_channels(lame_global_flags * gfp, int num_channels)
{
    if (is_lame_global_flags_valid(gfp)) {
        /* default = 2 */
        if (2 < num_channels || 0 == num_channels) {
            return -1;  /* we don't support more than 2 channels */
        }
        gfp->num_channels = num_channels;
        return 0;
    }
    return -1;
}
static lame_t  gf;
/* input samplerate */
int
lame_set_in_samplerate(lame_global_flags * gfp, int in_samplerate)
{
    if (is_lame_global_flags_valid(gfp)) {
        /* input sample rate in Hz,  default = 44100 Hz */
        gfp->samplerate_in = in_samplerate;
        return 0;
    }
    return -1;
}

/* number of samples */
/* it's unlikely for this function to return an error */
int
lame_set_num_samples(lame_global_flags * gfp, unsigned long num_samples)
{
    if (is_lame_global_flags_valid(gfp)) {
        /* default = 2^32-1 */
        gfp->num_samples = num_samples;
        return 0;
    }
    return -1;
}
int
lame_init_old(lame_global_flags * gfp)
{
/*    lame_internal_flags *gfc;
    SessionConfig_t *cfg;

    disable_FPE();*/      /* disable floating point exceptions */

    memset(gfp, 0, sizeof(lame_global_flags));

    gfp->class_id = LAME_ID;

    //if (NULL == (gfc = gfp->internal_flags = calloc(1, sizeof(lame_internal_flags))))
        //return -1;

    //cfg = &gfc->cfg;

    /* Global flags.  set defaults here for non-zero values */
    /* see lame.h for description */
    /* set integer values to -1 to mean that LAME will compute the
     * best value, UNLESS the calling program as set it
     * (and the value is no longer -1)
     */
    //gfp->strict_ISO = MDB_MAXIMUM;

    gfp->mode = NOT_SET;
    gfp->original = 1;
    gfp->samplerate_in = 44100;
    gfp->num_channels = 2;
    gfp->num_samples = MAX_U_32_NUM;

    gfp->write_lame_tag = 1;
    gfp->quality = -1;
    gfp->short_blocks = short_block_not_set;
    gfp->subblock_gain = -1;

    gfp->lowpassfreq = 0;
    gfp->highpassfreq = 0;
    gfp->lowpasswidth = -1;
    gfp->highpasswidth = -1;

    gfp->VBR = vbr_off;
    gfp->VBR_q = 4;
    gfp->ATHcurve = -1;
    gfp->VBR_mean_bitrate_kbps = 128;
    gfp->VBR_min_bitrate_kbps = 0;
    gfp->VBR_max_bitrate_kbps = 0;
    gfp->VBR_hard_min = 0;
    //cfg->vbr_min_bitrate_index = 1; /* not  0 ????? */
    //cfg->vbr_max_bitrate_index = 13; /* not 14 ????? */

    gfp->quant_comp = -1;
    gfp->quant_comp_short = -1;

    gfp->msfix = -1;

    //gfc->sv_qnt.OldValue[0] = 180;
    //gfc->sv_qnt.OldValue[1] = 180;
    //gfc->sv_qnt.CurrentStep[0] = 4;
    //gfc->sv_qnt.CurrentStep[1] = 4;
    //gfc->sv_qnt.masking_lower = 1;

    gfp->attackthre = -1;
    gfp->attackthre_s = -1;

    gfp->scale = 1;
    gfp->scale_left = 1;
    gfp->scale_right = 1;

    gfp->athaa_type = -1;
    gfp->ATHtype = -1;  /* default = -1 = set in lame_init_params */
    /* 2 = equal loudness curve */
    gfp->athaa_sensitivity = 0.0; /* no offset */
    gfp->useTemporal = -1;
    gfp->interChRatio = -1;

    /* The reason for
     *       int mf_samples_to_encode = ENCDELAY + POSTDELAY;
     * ENCDELAY = internal encoder delay.  And then we have to add POSTDELAY=288
     * because of the 50% MDCT overlap.  A 576 MDCT granule decodes to
     * 1152 samples.  To synthesize the 576 samples centered under this granule
     * we need the previous granule for the first 288 samples (no problem), and
     * the next granule for the next 288 samples (not possible if this is last
     * granule).  So we need to pad with 288 samples to make sure we can
     * encode the 576 samples we are interested in.
     */
    //gfc->sv_enc.mf_samples_to_encode = ENCDELAY + POSTDELAY;
  //  gfc->ov_enc.encoder_padding = 0;
    //gfc->sv_enc.mf_size = ENCDELAY - MDCTDELAY; /* we pad input with this many 0's */

    gfp->findReplayGain = 0;
    gfp->decode_on_the_fly = 0;

    //gfc->cfg.decode_on_the_fly = 0;
    //gfc->cfg.findReplayGain = 0;
    //gfc->cfg.findPeakSample = 0;

    //gfc->ov_rpg.RadioGain = 0;
    //gfc->ov_rpg.noclipGainChange = 0;
    //gfc->ov_rpg.noclipScale = -1.0;

    gfp->asm_optimizations.mmx = 1;
    gfp->asm_optimizations.amd3dnow = 1;
    gfp->asm_optimizations.sse = 1;

    gfp->preset = 0;

    gfp->write_id3tag_automatic = 1;

    gfp->report.debugf = &lame_report_def;
    gfp->report.errorf = &lame_report_def;
    gfp->report.msgf = &lame_report_def;
    return 0;
}
lame_global_flags *
lame_init(void)
{
    lame_global_flags *gfp;
    int     ret;

//    init_log_table();

    gfp = (lame_t)calloc(1, sizeof(lame_global_flags));
    if (gfp == NULL)
        return NULL;

    ret = lame_init_old(gfp);
    if (ret != 0) {
        free(gfp);
        return NULL;
    }

    gfp->lame_allocated_gfp = 1;
    return gfp;
}
unsigned long
lame_get_num_samples(const lame_global_flags * gfp)
{
    if (is_lame_global_flags_valid(gfp)) {
        return gfp->num_samples;
    }
    return 0;
}
int
lame_get_in_samplerate(const lame_global_flags * gfp)
{
    if (is_lame_global_flags_valid(gfp)) {
        return gfp->samplerate_in;
    }
    return 0;
}
int
lame_get_num_channels(const lame_global_flags * gfp)
{
    if (is_lame_global_flags_valid(gfp)) {
        return gfp->num_channels;
    }
    return 0;
}
int
samples_to_skip_at_start(void)
{
    return global.pcm32.skip_start;
}

int
samples_to_skip_at_end(void)
{
    return global.pcm32.skip_end;
}
int init_infile(lame_t& gfp, const byte* mp3Stream, size_t inSize)
{
    int     enc_delay = 0, enc_padding = 0;
    /* open the input file */
    global. count_samples_carefully = 0;
    global. num_samples_read = 0;
    global. pcmbitwidth = 8;// global_raw_pcm.in_bitwidth;
    global. pcmswapbytes = global_reader.swapbytes;
    global. pcm_is_unsigned_8bit = 0;//global_raw_pcm.in_signed == 1 ? 0 : 1;
    global. pcm_is_ieee_float = 0;
    global. hip = 0;
//    global. snd_file = 0;
    global. in_id3v2_size = 0;
    global. in_id3v2_tag = 0;
	global_reader.input_format = sf_mp123;

	if (global.hip) {
        hip_decode_exit(global.hip);
    }
    global. hip = hip_decode_init();
	memset(&global_decoder.mp3input_data, 0, sizeof(mp3data_struct));
//    if (is_mpeg_file_format(global_reader.input_format)) {
	global.music_in.ptr = mp3Stream;
	global.music_in.cur = 0;
	global.music_in.size = inSize;
	lame_decode_initfile(&enc_delay, &enc_padding);
	if (-1 == lame_set_num_channels(gfp, global_decoder.mp3input_data.stereo)) {
        return 0;
    }
    if (global_reader.input_samplerate == 0) {
        (void) lame_set_in_samplerate(gfp, global_decoder.mp3input_data.samplerate);
    }
    else {
        (void) lame_set_in_samplerate(gfp, global_reader.input_samplerate);
    }
    (void) lame_set_num_samples(gfp, global_decoder.mp3input_data.nsamp);
	if (lame_get_num_samples(gfp) == MAX_U_32_NUM) {
         double  flen = inSize;//lame_get_file_size(musicin); /* try to figure out num_samples */
        if (flen >= 0) {
            /* try file size, assume 2 bytes per sample */
            if (global_decoder.mp3input_data.bitrate > 0) {
                double  totalseconds =
                    (flen * 8.0 / (1000.0 * global_decoder.mp3input_data.bitrate));
                unsigned long tmp_num_samples =
                    (unsigned long) (totalseconds * lame_get_in_samplerate(gfp));

                (void) lame_set_num_samples(gfp, tmp_num_samples);
                global_decoder.mp3input_data.nsamp = tmp_num_samples;
            }
        }
    }
//        global. music_in = open_mpeg_file(gfp, inPath, &enc_delay, &enc_padding);
//    }
//    else {
//#ifdef LIBSNDFILE
//        if (strcmp(inPath, "-") != 0) { /* not for stdin */
//            global. snd_file = open_snd_file(gfp, inPath);
//        }
//#endif
//        if (global.snd_file == 0) {
//            global. music_in = open_wave_file(gfp, inPath);
//        }
//    }
    initPcmBuffer(&global.pcm32, sizeof(int));
    initPcmBuffer(&global.pcm16, sizeof(short));
    setSkipStartAndEnd(gfp, enc_delay, enc_padding);
    {
        unsigned long n = lame_get_num_samples(gfp);
        if (n != MAX_U_32_NUM) {
            unsigned long const discard = global.pcm32.skip_start + global.pcm32.skip_end;
            lame_set_num_samples(gfp, n > discard ? n - discard : 0);
        }
    }
//    return (global.snd_file != NULL || global.music_in != NULL) ? 1 : -1;
	return 1;
}
static unsigned int curOut = 0;
static void fwrite(const void* ptr, int size, int count, OutStream wavStream) {
	const unsigned char* pp = (const unsigned char*)ptr;
	if (wavStream->size() < curOut + size*count) {
		wavStream->resize(wavStream->empty() ? 1 : wavStream->size() + 1024*512);
		while (wavStream->size() < curOut + size*count ) {
			wavStream->resize(wavStream->size() + 1024*512);
		}
	}
	memcpy(wavStream->data() + curOut, ptr, size*count);
	//for (size_t c = 0; c < count; c++) {
	//	for (size_t i = 0; i < size; i++) {
	//		wavStream->insert(curOut + c*size + i, *(pp + c*size+i));
	//	}
	//}
	curOut += size*count;
}
static void
write_16_bits_low_high(OutStream fp, int val)
{
    unsigned char bytes[2];
    bytes[0] = (val & 0xff);
    bytes[1] = ((val >> 8) & 0xff);
    fwrite(bytes, 1, 2, fp);
}

static void
write_32_bits_low_high(OutStream fp, int val)
{
    unsigned char bytes[4];
    bytes[0] = (val & 0xff);
    bytes[1] = ((val >> 8) & 0xff);
    bytes[2] = ((val >> 16) & 0xff);
    bytes[3] = ((val >> 24) & 0xff);
    fwrite(bytes, 1, 4, fp);
}
int WriteWaveHeader(OutStream fp, int pcmbytes, int freq, int channels, int bits)
{
    int     bytes = (bits + 7) / 8;

    /* quick and dirty, but documented */
    fwrite("RIFF", 1, 4, fp); /* label */
    write_32_bits_low_high(fp, pcmbytes + 44 - 8); /* length in bytes without header */
    fwrite("WAVEfmt ", 2, 4, fp); /* 2 labels */
    write_32_bits_low_high(fp, 2 + 2 + 4 + 4 + 2 + 2); /* length of PCM format declaration area */
    write_16_bits_low_high(fp, 1); /* is PCM? */
    write_16_bits_low_high(fp, channels); /* number of channels */
    write_32_bits_low_high(fp, freq); /* sample frequency in [Hz] */
    write_32_bits_low_high(fp, freq * channels * bytes); /* bytes per second */
    write_16_bits_low_high(fp, channels * bytes); /* bytes per sample time */
    write_16_bits_low_high(fp, bits); /* bits per sample */
    fwrite("data", 1, 4, fp); /* label */
    write_32_bits_low_high(fp, pcmbytes); /* length in bytes of raw PCM data */

    return 0;
}

/* Size of MPEG frame. */
int
lame_get_framesize(const lame_global_flags * gfp)
{
    if (is_lame_global_flags_valid(gfp)) {
		int mode_gr = gfp->samplerate_out <= 24000 ? 1 : 2;
		return 576 * mode_gr;
        //lame_internal_flags const *const gfc = gfp->internal_flags;
        //if (is_lame_internal_flags_valid(gfc)) {
        //    SessionConfig_t const *const cfg = &gfc->cfg;
        //    return 576 * cfg->mode_gr;
        //}
    }
    return 0;
}
int
hip_decode1_headers(hip_t hip, unsigned char *buffer,
                     size_t len, short pcm_l[], short pcm_r[], mp3data_struct * mp3data)
{
    int     enc_delay, enc_padding;
    return hip_decode1_headersB(hip, buffer, len, pcm_l, pcm_r, mp3data, &enc_delay, &enc_padding);
}
static int
lame_decode_fromfile(short pcm_l[], short pcm_r[], mp3data_struct * mp3data)
{
    int     ret = 0;
    size_t  len = 0;
    unsigned char buf[1024];

    /* first see if we still have data buffered in the decoder: */
    ret = hip_decode1_headers(global.hip, buf, len, pcm_l, pcm_r, mp3data);
    if (ret != 0)
        return ret;


    /* read until we get a valid output frame */
    for (;;) {
		len = read_byte(buf, 1024);
        //len = fread(buf, 1, 1024, fd);
        if (len == 0) {
            /* we are done reading the file, but check for buffered data */
            ret = hip_decode1_headers(global.hip, buf, len, pcm_l, pcm_r, mp3data);
            if (ret <= 0) {
                hip_decode_exit(global.hip); /* release mp3decoder memory */
                global. hip = 0;
                return -1; /* done with file */
            }
            break;
        }

        ret = hip_decode1_headers(global.hip, buf, len, pcm_l, pcm_r, mp3data);
        if (ret == -1) {
            hip_decode_exit(global.hip); /* release mp3decoder memory */
            global. hip = 0;
            return -1;
        }
        if (ret > 0)
            break;
    }
    return ret;
}

static int
read_samples_mp3(lame_t gfp, short int mpg123pcm[2][1152])
{
    int     out;
    int     samplerate;
    static const char type_name[] = "MP3 file";

    out = lame_decode_fromfile( mpg123pcm[0], mpg123pcm[1], &global_decoder.mp3input_data);
    /*
     * out < 0:  error, probably EOF
     * out = 0:  not possible with lame_decode_fromfile() ???
     * out > 0:  number of output samples
     */
    if (out < 0) {
        memset(mpg123pcm, 0, sizeof(**mpg123pcm) * 2 * 1152);
        return 0;
    }

    if (lame_get_num_channels(gfp) != global_decoder.mp3input_data.stereo) {
        out = -1;
    }
    samplerate = global_reader.input_samplerate;
    if (samplerate == 0) {
        samplerate = global_decoder.mp3input_data.samplerate;
    }
    if (lame_get_in_samplerate(gfp) != samplerate) {
        out = -1;
    }
    return out;
}
static int
get_audio_common(lame_t gfp, int buffer[2][1152], short buffer16[2][1152])
{
    int     num_channels = lame_get_num_channels(gfp);
    int     insamp[2 * 1152];
    short   buf_tmp16[2][1152];
    int     samples_read;
    int     framesize;
    int     samples_to_read;
    unsigned int remaining, tmp_num_samples;
    int     i;
    int    *p;

    /* 
     * NOTE: LAME can now handle arbritray size input data packets,
     * so there is no reason to read the input data in chuncks of
     * size "framesize".  EXCEPT:  the LAME graphical frame analyzer 
     * will get out of sync if we read more than framesize worth of data.
     */

    samples_to_read = framesize = lame_get_framesize(gfp);
    assert(framesize <= 1152);

    /* get num_samples */
    //if (is_mpeg_file_format(global_reader.input_format)) {
        tmp_num_samples = global_decoder.mp3input_data.nsamp;
    //}
    //else {
    //    tmp_num_samples = lame_get_num_samples(gfp);
    //}

    /* if this flag has been set, then we are carefull to read
     * exactly num_samples and no more.  This is useful for .wav and .aiff
     * files which have id3 or other tags at the end.  Note that if you
     * are using LIBSNDFILE, this is not necessary 
     */
    if (global.count_samples_carefully) {
        if (global.num_samples_read < tmp_num_samples) {
            remaining = tmp_num_samples - global.num_samples_read;
        }
        else {
            remaining = 0;
        }
        if (remaining < (unsigned int) framesize && 0 != tmp_num_samples)
            /* in case the input is a FIFO (at least it's reproducible with
               a FIFO) tmp_num_samples may be 0 and therefore remaining
               would be 0, but we need to read some samples, so don't
               change samples_to_read to the wrong value in this case */
            samples_to_read = remaining;
    }

//    if (is_mpeg_file_format(global_reader.input_format)) {
        if (buffer != NULL)
            samples_read = read_samples_mp3(gfp, buf_tmp16);
        else
            samples_read = read_samples_mp3(gfp, buffer16);
        if (samples_read < 0) {
            return samples_read;
        }
//    }
//    else {
//        if (global.snd_file) {
//#ifdef LIBSNDFILE
//            samples_read = sf_read_int(global.snd_file, insamp, num_channels * samples_to_read);
//#else
//            samples_read = 0;
//#endif
//        }
//        else {
//            samples_read =
//                read_samples_pcm(global.music_in, insamp, num_channels * samples_to_read);
//        }
//        if (samples_read < 0) {
//            return samples_read;
//        }
//        p = insamp + samples_read;
//        samples_read /= num_channels;
//        if (buffer != NULL) { /* output to int buffer */
//            if (num_channels == 2) {
//                for (i = samples_read; --i >= 0;) {
//                    buffer[1][i] = *--p;
//                    buffer[0][i] = *--p;
//                }
//            }
//            else if (num_channels == 1) {
//                memset(buffer[1], 0, samples_read * sizeof(int));
//                for (i = samples_read; --i >= 0;) {
//                    buffer[0][i] = *--p;
//                }
//            }
//            else
//                assert(0);
//        }
//        else {          /* convert from int; output to 16-bit buffer */
//            if (num_channels == 2) {
//                for (i = samples_read; --i >= 0;) {
//                    buffer16[1][i] = *--p >> (8 * sizeof(int) - 16);
//                    buffer16[0][i] = *--p >> (8 * sizeof(int) - 16);
//                }
//            }
//            else if (num_channels == 1) {
//                memset(buffer16[1], 0, samples_read * sizeof(short));
//                for (i = samples_read; --i >= 0;) {
//                    buffer16[0][i] = *--p >> (8 * sizeof(int) - 16);
//                }
//            }
//            else
//                assert(0);
//        }
//    }

    /* LAME mp3 output 16bit -  convert to int, if necessary */
    if (true/*is_mpeg_file_format(global_reader.input_format)*/) {
        if (buffer != NULL) {
            for (i = samples_read; --i >= 0;)
                buffer[0][i] = buf_tmp16[0][i] << (8 * sizeof(int) - 16);
            if (num_channels == 2) {
                for (i = samples_read; --i >= 0;)
                    buffer[1][i] = buf_tmp16[1][i] << (8 * sizeof(int) - 16);
            }
            else if (num_channels == 1) {
                memset(buffer[1], 0, samples_read * sizeof(int));
            }
            else
                assert(0);
        }
    }


    /* if num_samples = MAX_U_32_NUM, then it is considered infinitely long.
       Don't count the samples */
    if (tmp_num_samples != MAX_U_32_NUM)
        global. num_samples_read += samples_read;

    return samples_read;
}

static int
addPcmBuffer(PcmBuffer * b, void *a0, void *a1, int read)
{
    int     a_n;

    if (b == 0) {
        return 0;
    }
    if (read < 0) {
        return b->u - b->skip_end;
    }
    if (b->skip_start >= read) {
        b->skip_start -= read;
        return b->u - b->skip_end;
    }
    a_n = read - b->skip_start;

    if (b != 0 && a_n > 0) {
        int const a_skip = b->w * b->skip_start;
        int const a_want = b->w * a_n;
        int const b_used = b->w * b->u;
        int const b_have = b->w * b->n;
        int const b_need = b->w * (b->u + a_n);
        if (b_have < b_need) {
            b->n = b->u + a_n;
            b->ch[0] = realloc(b->ch[0], b_need);
            b->ch[1] = realloc(b->ch[1], b_need);
        }
        b->u += a_n;
        if (b->ch[0] != 0 && a0 != 0) {
            char   *src = (char*)a0;
            char   *dst = (char*)b->ch[0];
            memcpy(dst + b_used, src + a_skip, a_want);
        }
        if (b->ch[1] != 0 && a1 != 0) {
            char   *src = (char*)a1;
            char   *dst = (char*)b->ch[1];
            memcpy(dst + b_used, src + a_skip, a_want);
        }
    }
    b->skip_start = 0;
    return b->u - b->skip_end;
}

static int
takePcmBuffer(PcmBuffer * b, void *a0, void *a1, int a_n, int mm)
{
    if (a_n > mm) {
        a_n = mm;
    }
    if (b != 0 && a_n > 0) {
        int const a_take = b->w * a_n;
        if (a0 != 0 && b->ch[0] != 0) {
            memcpy(a0, b->ch[0], a_take);
        }
        if (a1 != 0 && b->ch[1] != 0) {
            memcpy(a1, b->ch[1], a_take);
        }
        b->u -= a_n;
        if (b->u < 0) {
            b->u = 0;
            return a_n;
        }
        if (b->ch[0] != 0) {
            memmove(b->ch[0], (char *) b->ch[0] + a_take, b->w * b->u);
        }
        if (b->ch[1] != 0) {
            memmove(b->ch[1], (char *) b->ch[1] + a_take, b->w * b->u);
        }
    }
    return a_n;
}

int
get_audio16(lame_t gfp, short buffer[2][1152])
{
    int     used = 0, read = 0;
    do {
        read = get_audio_common(gfp, NULL, buffer);
        used = addPcmBuffer(&global.pcm16, buffer[0], buffer[1], read);
    } while (used <= 0 && read > 0);
    if (read < 0) {
        return read;
    }
    if (global_reader.swap_channel == 0)
        return takePcmBuffer(&global.pcm16, buffer[0], buffer[1], used, 1152);
    else
        return takePcmBuffer(&global.pcm16, buffer[1], buffer[0], used, 1152);
}

static void
addSamples(DecoderProgress dp, int iread)
{
    dp->samples += iread;
    dp->frame_ctr += dp->samples / dp->framesize;
    dp->samples %= dp->framesize;
    if (dp->frames_total < dp->frame_ctr) {
        dp->frames_total = dp->frame_ctr;
    }
}

void
decoder_progress(DecoderProgress dp, const mp3data_struct * mp3data, int iread)
{
    addSamples(dp, iread);

    //console_printf("\rFrame#%6i/%-6i %3i kbps",
    //               dp->frame_ctr, dp->frames_total, mp3data->bitrate);

    /* Programmed with a single frame hold delay */
    /* Attention: static data */

    /* MP2 Playback is still buggy. */
    /* "'00' subbands 4-31 in intensity_stereo, bound==4" */
    /* is this really intensity_stereo or is it MS stereo? */

    if (mp3data->mode == JOINT_STEREO) {
        int     curr = mp3data->mode_ext;
        int     last = dp->last_mode_ext;
        //console_printf("  %s  %c",
        //               curr & 2 ? last & 2 ? " MS " : "LMSR" : last & 2 ? "LMSR" : "L  R",
        //               curr & 1 ? last & 1 ? 'I' : 'i' : last & 1 ? 'i' : ' ');
        dp->last_mode_ext = curr;
    }
    else {
       /* console_printf("         ");*/
        dp->last_mode_ext = 0;
    }
/*    console_printf ("%s", Console_IO.str_clreoln ); */
    //console_printf("        \b\b\b\b\b\b\b\b");
    //console_flush();
}
#define LOW__BYTE(x) (x & 0x00ff)
#define HIGH_BYTE(x) ((x >> 8) & 0x00ff)

void
put_audio16(OutStream outf, short Buffer[2][1152], int iread, int nch)
{
    char    data[2 * 1152 * 2];
    int     i, m = 0;

    if (global_decoder.disable_wav_header && global_reader.swapbytes) {
        if (nch == 1) {
            for (i = 0; i < iread; i++) {
                short   x = Buffer[0][i];
                /* write 16 Bits High Low */
                data[m++] = HIGH_BYTE(x);
                data[m++] = LOW__BYTE(x);
            }
        }
        else {
            for (i = 0; i < iread; i++) {
                short   x = Buffer[0][i], y = Buffer[1][i];
                /* write 16 Bits High Low */
                data[m++] = HIGH_BYTE(x);
                data[m++] = LOW__BYTE(x);
                /* write 16 Bits High Low */
                data[m++] = HIGH_BYTE(y);
                data[m++] = LOW__BYTE(y);
            }
        }
    }
    else {
        if (nch == 1) {
            for (i = 0; i < iread; i++) {
                short   x = Buffer[0][i];
                /* write 16 Bits Low High */
                data[m++] = LOW__BYTE(x);
                data[m++] = HIGH_BYTE(x);
            }
        }
        else {
            for (i = 0; i < iread; i++) {
                short   x = Buffer[0][i], y = Buffer[1][i];
                /* write 16 Bits Low High */
                data[m++] = LOW__BYTE(x);
                data[m++] = HIGH_BYTE(x);
                /* write 16 Bits Low High */
                data[m++] = LOW__BYTE(y);
                data[m++] = HIGH_BYTE(y);
            }
        }
    }
    if (m > 0) {
		try {
			 fwrite(data, 1, m, outf);
		} catch(std::bad_alloc& e) {
		}
    }
    //if (global_writer.flush_write == 1) {
    //    fflush(outf);
    //}
}
static
unsigned long calcEndPadding(unsigned long samples, int pcm_samples_per_frame)
{
    unsigned long end_padding;
    samples += 576;
    end_padding = pcm_samples_per_frame - (samples % pcm_samples_per_frame);
    if (end_padding < 576)
        end_padding += pcm_samples_per_frame;
    return end_padding;
}

static
unsigned long calcNumBlocks(unsigned long samples, int pcm_samples_per_frame)
{
    unsigned long end_padding;
    samples += 576;
    end_padding = pcm_samples_per_frame - (samples % pcm_samples_per_frame);
    if (end_padding < 576)
        end_padding += pcm_samples_per_frame;
    return (samples + end_padding) / pcm_samples_per_frame;
}
DecoderProgress
decoder_progress_init(unsigned long n, int framesize)
{
    DecoderProgress dp = &global_decoder_progress;
    dp->last_mode_ext =0;
    dp->frames_total = 0;
    dp->frame_ctr = 0;
    dp->framesize = framesize;
    dp->samples = 0;
    if (n != (0ul-1ul)) {
        if (framesize == 576 || framesize == 1152) {
            dp->frames_total = calcNumBlocks(n, framesize);
            dp->samples = 576 + calcEndPadding(n, framesize);
        }
        else if (framesize > 0) {
            dp->frames_total = n / framesize;
        }
        else {
            dp->frames_total = n;
        }
    }
    return dp;
}
int lame_decoder(const byte* mp3Stream, size_t inSize, OutStream wavStream) {
	
	if (gf == 0) {
		gf = lame_init();
	}
	lame_t& gfp = gf;
	
	init_infile(gfp, mp3Stream, inSize);
	int     enc_delay = 0, enc_padding = 0;
	
	short int Buffer[2][1152];
    int     i, iread;
    double  wavsize;
    int     tmp_num_channels = lame_get_num_channels(gfp);
    int     skip_start = samples_to_skip_at_start();
    int     skip_end = samples_to_skip_at_end();
    DecoderProgress dp = 0;

 //   if (!(tmp_num_channels >= 1 && tmp_num_channels <= 2)) {
 //       error_printf("Internal error.  Aborting.");
 //       exit(-1);
 //   }

 //   if (global_ui_config.silent < 9) {
 //       console_printf("\rinput:  %s%s(%g kHz, %i channel%s, ",
 //                      strcmp(inPath, "-") ? inPath : "<stdin>",
 //                      strlen(inPath) > 26 ? "\n\t" : "  ",
 //                      lame_get_in_samplerate(gfp) / 1.e3,
 //                      tmp_num_channels, tmp_num_channels != 1 ? "s" : "");

 //       printInputFormat(gfp);

 //       console_printf(")\noutput: %s%s(16 bit, Microsoft WAVE)\n",
 //                      strcmp(outPath, "-") ? outPath : "<stdout>",
 //                      strlen(outPath) > 45 ? "\n\t" : "  ");

 //       if (skip_start > 0)
 //           console_printf("skipping initial %i samples (encoder+decoder delay)\n", skip_start);
 //       if (skip_end > 0)
 //           console_printf("skipping final %i samples (encoder padding-decoder delay)\n", skip_end);

        switch (global_reader.input_format) {
        case sf_mp3:
        case sf_mp2:
        case sf_mp1:
            dp = decoder_progress_init(lame_get_num_samples(gfp),
                                       global_decoder.mp3input_data.framesize);
            break;
        case sf_raw:
        case sf_wave:
        case sf_aiff:
        default:
            dp = decoder_progress_init(lame_get_num_samples(gfp),
                                       lame_get_in_samplerate(gfp) < 32000 ? 576 : 1152);
            break;
        }
 //   }

 //   if (0 == global_decoder.disable_wav_header)
	curOut = 0;
	int cur = 0;
	WriteWaveHeader(wavStream, 0x7FFFFFFF, lame_get_in_samplerate(gfp), tmp_num_channels, 16);
    /* unknown size, so write maximum 32 bit signed value */

    wavsize = 0;
    do {
		iread = get_audio16(gfp, Buffer); /* read in 'iread' samples */
        if (iread >= 0) {
            wavsize += iread;
            if (dp != 0) {
                decoder_progress(dp, &global_decoder.mp3input_data, iread);
            }
            put_audio16(wavStream, Buffer, iread, tmp_num_channels);
        }
    } while (iread > 0);

    i = (16 / 8) * tmp_num_channels;
    assert(i > 0);
    if (wavsize <= 0) {
        wavsize = 0;
    }
    else if (wavsize > 0xFFFFFFD0 / i) {
        wavsize = 0xFFFFFFD0;
    }
    else {
        wavsize *= i;
    }
 //   /* if outf is seekable, rewind and adjust length */
 //   if (!global_decoder.disable_wav_header && strcmp("-", outPath)
 //       && !fseek(outf, 0l, SEEK_SET))
	curOut = 0;
    WriteWaveHeader(wavStream, (int) wavsize, lame_get_in_samplerate(gfp), tmp_num_channels, 16);
 //   fclose(outf);
 //   close_infile();

 //   if (dp != 0)
 //       decoder_progress_finish(dp);
    return 0;
}

