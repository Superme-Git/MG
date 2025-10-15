/* OggEnc
 *
 * This program is distributed under the GNU General Public License, version 2.
 * A copy of this license is included with this source.
 *
 * Copyright 2000-2005, Michael Smith <msmith@xiph.org>
 *
 * Portions from Vorbize, (c) Kenneth Arnold <kcarnold-xiph@arnoldnet.net>
 * and libvorbis examples, (c) Monty <monty@xiph.org>
 */

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <getopt.h>
#include <string.h>
#include <time.h>
#include <locale.h>
#include <errno.h>
#include <sys/types.h>
#if defined WIN32 || defined _WIN32
#include <process.h>
#endif
#if defined IOS
#include <unistd.h>
#endif

#include "platform.h"
#include "encode.h"
#include "audio.h"
#include "utf8.h"
#include "i18n.h"

static char* gs_cafbuffer = 0;
static long gs_cafSize = 0;
static long gs_cafpos = 0;

int ReadFromWav(unsigned char* buffer, long length)
{
    if (length <= 0) {
        return 0;
    }

    long __remaining = gs_cafSize - gs_cafpos;
    if (__remaining <= 0) {
        return 0;
    }

    long __copy = (length < __remaining) ? length : __remaining;

    memcpy(buffer, gs_cafbuffer + gs_cafpos, __copy);
    gs_cafpos += __copy;
    return __copy;
}

int SaveWavData2Ogg(const char* pData, int length, const char* out_fn)
{
	gs_cafbuffer = (char*)pData;
	gs_cafSize = length;
	gs_cafpos = 0;

	/* Default values */
	oe_options opt = {
		NULL, 0, NULL, 0, NULL, 0, NULL, 0, NULL, 0,
		NULL, 0, NULL, 0, NULL, 0, NULL, 0, NULL, 0,
		1, 0, 0, 0,
		16, 8000, 1, 0,
		NULL, DEFAULT_NAMEFMT_REMOVE, DEFAULT_NAMEFMT_REPLACE,
		NULL,
		0, -1, -1, -1,
		.3, -1,
		0, 0, 0.f,
		0, 0, 0, 0, 0 };

	opt.quality = 0.1f * 1;
	opt.quality_set = 1;

	if (!opt.fixedserial)
	{
		srand(time(NULL) ^ 1000);
		opt.serial = rand();
	}
	opt.skeleton_serial = opt.serial;
	opt.kate_serial = opt.skeleton_serial;
	opt.rawmode = 1;

	oe_enc_opt      enc_opts;
	vorbis_comment  vc;
	memset(&vc, 0, sizeof(vc));
	FILE *out = NULL;
	input_format *format;

	enc_opts.serialno = opt.serial++;
	enc_opts.skeleton_serialno = opt.skeleton_serial++;
	enc_opts.kate_serialno = opt.kate_serial++;
	enc_opts.progress_update = update_statistics_full;
	enc_opts.start_encode = start_encode_full;
	enc_opts.end_encode = final_statistics;
	enc_opts.error = encode_error;
	enc_opts.comments = &vc;
	enc_opts.copy_comments = opt.copy_comments;
	enc_opts.with_skeleton = opt.with_skeleton;
	enc_opts.ignorelength = opt.ignorelength; 

	if (opt.rawmode)
	{
		input_format raw_format = { NULL, 0, raw_open, wav_close, "raw", N_("RAW file reader") };

		enc_opts.rate = opt.raw_samplerate;
		enc_opts.channels = opt.raw_channels;
		enc_opts.samplesize = opt.raw_samplesize;
		enc_opts.endianness = opt.raw_endianness;

		format = &raw_format;
		format->open_func(NULL, &enc_opts, NULL, 0);
	}

	// 保证输出路径上所有目录已创建，避免 fopen 失败
	if (create_directories((char*)out_fn, opt.isutf8) != 0) {
		// 目录创建失败，返回错误
		return 1;
	}
	out = oggenc_fopen(out_fn, "wb", opt.isutf8);

	enc_opts.out = out;
	enc_opts.comments = &vc;
	enc_opts.filename = strdup(out_fn);
	enc_opts.infilename = strdup("");
	enc_opts.managed = opt.managed;
	enc_opts.bitrate = opt.nominal_bitrate;
	enc_opts.min_bitrate = opt.min_bitrate;
	enc_opts.max_bitrate = opt.max_bitrate;
	enc_opts.quality = opt.quality;
	enc_opts.quality_set = opt.quality_set;
	enc_opts.advopt = opt.advopt;
	enc_opts.advopt_count = opt.advopt_count;
	enc_opts.lyrics = NULL;
	enc_opts.lyrics_language = NULL;

	enc_opts.progress_update = update_statistics_notime;

	if (oe_encode(&enc_opts))
		return 1;

	// 释放分配的文件名内存，避免泄漏（不区分平台）
	if (enc_opts.filename) free(enc_opts.filename);
	if (enc_opts.infilename) free(enc_opts.infilename);

	// 安全关闭输入资源
	if (format && format->close_func) {
		format->close_func(enc_opts.readdata);
	}

	// 关闭输出文件
	if (out) fclose(out);

	gs_cafbuffer = 0;
	gs_cafSize = 0;
	gs_cafpos = 0;

	return 0;
}
