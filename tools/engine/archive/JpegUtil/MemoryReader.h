#pragma once

#include "jinclude.h"
#include "jpeglib.h"
#include "objidl.h"

typedef struct my_cjpeg_source_struct * my_cjpeg_source_ptr ;

struct my_cjpeg_source_struct {
	void (*start_input)(j_compress_ptr cinfo,
		my_cjpeg_source_ptr sinfo);

	JDIMENSION (*get_pixel_rows)(j_compress_ptr cinfo,
		my_cjpeg_source_ptr sinfo);

	void (*finish_input) (j_compress_ptr cinfo,
		my_cjpeg_source_ptr sinfo);

	// whole image buffer
	BYTE* whole_image;

	// one row buffer
	JSAMPROW buffer;
};

extern my_cjpeg_source_ptr my_init_read_mem (
	j_compress_ptr cinfo, const BYTE* data, int nWidth, int nHeight );
