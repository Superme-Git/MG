
#pragma once

#define JPEG_CJPEG_DJPEG	/* define proper options in jconfig.h */
#define JPEG_INTERNAL_OPTIONS	/* cjpeg.c,djpeg.c need to see xxx_SUPPORTED */
#include "jinclude.h"
#include "jpeglib.h"

#define JMESSAGE(code,string)	code ,
#define JERROR_H
#define JMAKE_ENUM_LIST
#include "jerror.h"		/* get library error codes too */

#include "cderror.h"		/* get application-specific error codes */

#include "../../pfs2.h"	/* for PFS::CBaseFile */

/*
* To support 12-bit JPEG data, we'd have to scale output down to 8 bits.
* This is not yet implemented.
*/

#if BITS_IN_JSAMPLE != 8
Sorry, this code only copes with 8-bit JSAMPLEs. /* deliberate syntax err */
#endif

typedef unsigned char U_CHAR;
#define UCH(x)	((int) (x))

using PFS::CBaseFile;

#undef JFWRITE
#undef JFREAD

#define JFREAD(file,buf,sizeofbuf)  \
	file->Read((void *) (buf), (size_t)(sizeofbuf))

#define JFWRITE(stream, buffer, size) \
	stream->Write(buffer, size)

#define	ReadOK(file,buffer,len)	(JFREAD(file,buffer,len) == ((size_t) (len)))

struct translator_error_mgr;
typedef struct translator_error_mgr * translator_error_ptr;

struct translator_progress_mgr;

typedef struct translator_progress_mgr * translator_progress_ptr;


struct translator_common_struct
{
	//jpeg_common_fields;
	 translator_error_ptr err;	/* Error handler module */
	 translator_progress_ptr progress; /* Progress monitor, or NULL if none */

	unsigned int image_height;
	unsigned int image_width;

	UINT8 density_unit;		/* JFIF code for pixel size units */
	UINT16 X_density;		/* Horizontal pixel density */
	UINT16 Y_density;		/* Vertical pixel density */
	JDIMENSION output_width;	/* scaled image width */
	JDIMENSION output_height;	/* scaled image height */
	int out_color_components;	/* # of color components in out_color_space */
	int input_components;		/* # of color components in input image */

	int data_precision;		/* bits of precision in image data */
	
	boolean quantize_colors;	/* TRUE=colormapped output wanted */
	/* When quantizing colors, the output colormap is described by these fields.
	* The application can supply a colormap by setting colormap non-NULL before
	* calling jpeg_start_decompress; otherwise a colormap is created during
	* jpeg_start_decompress or jpeg_start_output.
	* The map has out_color_components rows and actual_number_of_colors columns.
	*/
	int actual_number_of_colors;	/* number of entries in use */
	JSAMPARRAY colormap;		/* The color map as a 2-D pixel array */

	J_COLOR_SPACE out_color_space;	/* colorspace for output */	
};

typedef struct translator_common_struct* translator_common_ptr;

typedef struct translator_dest_struct * translator_dest_ptr;

struct translator_dest_struct
{
	/* common info during the translation */
	translator_common_ptr cinfo;

	/* start_output is called after jpeg_start_decompress finishes.
	* The color map will be ready at this time, if one is needed.
	*/
	void (*start_output)(translator_dest_ptr dinfo);
	/* Emit the specified number of pixel rows from the buffer. */
	void (*put_pixel_rows)(translator_dest_ptr dinfo,
		JDIMENSION rows_supplied);
	/* Finish up at the end of the image. */
	void (*finish_output)(translator_dest_ptr dinfo);

	/* Target file spec; filled in by djpeg.c after object is created. */
	CBaseFile * output_file;

	/* Output pixel-row buffer.  Created by module init or start_output.
	* Width is cinfo->output_width * cinfo->output_components;
	*/
	JSAMPROW buffer;
	
};


typedef struct translator_source_struct * translator_source_ptr ;

struct translator_source_struct {
	/* common info during the translation */
	translator_common_ptr cinfo;

	void (*start_input)(translator_source_ptr sinfo);

	/* preload_image maybe as a separated procedure for alpha channel info - yanghaibo */
	JDIMENSION (*preload_image)(translator_source_ptr sinfo);
	JDIMENSION (*get_pixel_rows)(translator_source_ptr sinfo);
	void (*finish_input)(translator_source_ptr sinfo);

	CBaseFile *input_file;

	/* input pixel-row buffer */
	JSAMPROW buffer;
};


/* Error handler object */

struct translator_error_mgr {
	/* Error exit handler: does not return to caller */
	JMETHOD(void, error_exit, (translator_common_ptr cinfo));
	/* Conditionally emit a trace or warning message */
	JMETHOD(void, emit_message, (translator_common_ptr cinfo, int msg_level));
	/* Routine that actually outputs a trace or error message */
	JMETHOD(void, output_message, (translator_common_ptr cinfo));
	/* Format a message string for the most recent JPEG error or message */
	JMETHOD(void, format_message, (translator_common_ptr cinfo, char * buffer));
#define JMSG_LENGTH_MAX  200	/* recommended size of format_message buffer */
	/* Reset error state variables at start of a new image */
	JMETHOD(void, reset_error_mgr, (translator_common_ptr cinfo));

	/* The message ID code and any parameters are saved here.
	* A message can have one string parameter or up to 8 int parameters.
	*/
	int msg_code;
#define JMSG_STR_PARM_MAX  80
	union {
		int i[8];
		char s[JMSG_STR_PARM_MAX];
	} msg_parm;

	/* Standard state variables for error facility */

	int trace_level;		/* max msg_level that will be displayed */

	/* For recoverable corrupt-data errors, we emit a warning message,
	* but keep going unless emit_message chooses to abort.  emit_message
	* should count warnings in num_warnings.  The surrounding application
	* can check for bad data by seeing if num_warnings is nonzero at the
	* end of processing.
	*/
	long num_warnings;		/* number of corrupt-data warnings */

	/* These fields point to the table(s) of error message strings.
	* An application can change the table pointer to switch to a different
	* message list (typically, to change the language in which errors are
	* reported).  Some applications may wish to add additional error codes
	* that will be handled by the JPEG library error mechanism; the second
	* table pointer is used for this purpose.
	*
	* First table includes all errors generated by JPEG library itself.
	* Error code 0 is reserved for a "no such error string" message.
	*/
	const char * const * jpeg_message_table; /* Library errors */
	int last_jpeg_message;    /* Table contains strings 0..last_jpeg_message */
	/* Second table can be added by application (see cjpeg/djpeg for example).
	* It contains strings numbered first_addon_message..last_addon_message.
	*/
	const char * const * addon_message_table; /* Non-library errors */
	int first_addon_message;	/* code for first string in addon table */
	int last_addon_message;	/* code for last string in addon table */
};

struct translator_progress_mgr {
	void (*progress_monitor)(translator_common_ptr cinfo);

	long pass_counter;		/* work units completed in this pass */
	long pass_limit;		/* total number of work units in this pass */
	int completed_passes;		/* passes completed so far */
	int total_passes;		/* total number of passes expected */

	int completed_extra_passes;	/* extra passes completed */
	int total_extra_passes;	/* total extra */
	///* last printed percentage stored here to avoid multiple printouts */
	//int percent_done;
};

extern void create_common_translator(translator_common_ptr cinfo);
extern void destroy_common_translator(translator_common_ptr cinfo);

extern translator_source_ptr my_init_read_bmp (translator_common_ptr cinfo);
extern translator_dest_ptr my_init_write_bmp (translator_common_ptr cinfo);

extern void destroy_translator_source (translator_source_ptr sinfo);
extern void destroy_translator_dest (translator_dest_ptr dinfo);

extern translator_error_ptr my_translator_error (translator_error_ptr err);
extern translator_progress_ptr my_translator_progress( translator_progress_ptr progress );

extern JSAMPARRAY alloc_sarray( int width, int height );
extern void free_sarray( JSAMPARRAY sarray );

extern JSAMPROW alloc_srow( int width );
extern void free_srow( JSAMPROW srow);


/* Fatal errors (print message and exit) */
#define ERREXIT(cinfo,code)  \
	((cinfo)->err->msg_code = (code), \
	(*(cinfo)->err->error_exit) ((translator_common_ptr) (cinfo)))
#define ERREXIT1(cinfo,code,p1)  \
	((cinfo)->err->msg_code = (code), \
	(cinfo)->err->msg_parm.i[0] = (p1), \
	(*(cinfo)->err->error_exit) ((translator_common_ptr) (cinfo)))
#define ERREXIT2(cinfo,code,p1,p2)  \
	((cinfo)->err->msg_code = (code), \
	(cinfo)->err->msg_parm.i[0] = (p1), \
	(cinfo)->err->msg_parm.i[1] = (p2), \
	(*(cinfo)->err->error_exit) ((translator_common_ptr) (cinfo)))
#define ERREXIT3(cinfo,code,p1,p2,p3)  \
	((cinfo)->err->msg_code = (code), \
	(cinfo)->err->msg_parm.i[0] = (p1), \
	(cinfo)->err->msg_parm.i[1] = (p2), \
	(cinfo)->err->msg_parm.i[2] = (p3), \
	(*(cinfo)->err->error_exit) ((translator_common_ptr) (cinfo)))
#define ERREXIT4(cinfo,code,p1,p2,p3,p4)  \
	((cinfo)->err->msg_code = (code), \
	(cinfo)->err->msg_parm.i[0] = (p1), \
	(cinfo)->err->msg_parm.i[1] = (p2), \
	(cinfo)->err->msg_parm.i[2] = (p3), \
	(cinfo)->err->msg_parm.i[3] = (p4), \
	(*(cinfo)->err->error_exit) ((translator_common_ptr) (cinfo)))
#define ERREXITS(cinfo,code,str)  \
	((cinfo)->err->msg_code = (code), \
	strncpy((cinfo)->err->msg_parm.s, (str), JMSG_STR_PARM_MAX), \
	(*(cinfo)->err->error_exit) ((translator_common_ptr) (cinfo)))

#define MAKESTMT(stuff)		do { stuff } while (0)

/* Nonfatal errors (we can keep going, but the data is probably corrupt) */
#define WARNMS(cinfo,code)  \
	((cinfo)->err->msg_code = (code), \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), -1))
#define WARNMS1(cinfo,code,p1)  \
	((cinfo)->err->msg_code = (code), \
	(cinfo)->err->msg_parm.i[0] = (p1), \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), -1))
#define WARNMS2(cinfo,code,p1,p2)  \
	((cinfo)->err->msg_code = (code), \
	(cinfo)->err->msg_parm.i[0] = (p1), \
	(cinfo)->err->msg_parm.i[1] = (p2), \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), -1))

/* Informational/debugging messages */
#define TRACEMS(cinfo,lvl,code)  \
	((cinfo)->err->msg_code = (code), \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), (lvl)))
#define TRACEMS1(cinfo,lvl,code,p1)  \
	((cinfo)->err->msg_code = (code), \
	(cinfo)->err->msg_parm.i[0] = (p1), \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), (lvl)))
#define TRACEMS2(cinfo,lvl,code,p1,p2)  \
	((cinfo)->err->msg_code = (code), \
	(cinfo)->err->msg_parm.i[0] = (p1), \
	(cinfo)->err->msg_parm.i[1] = (p2), \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), (lvl)))
#define TRACEMS3(cinfo,lvl,code,p1,p2,p3)  \
	MAKESTMT(int * _mp = (cinfo)->err->msg_parm.i; \
	_mp[0] = (p1); _mp[1] = (p2); _mp[2] = (p3); \
	(cinfo)->err->msg_code = (code); \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), (lvl)); )
#define TRACEMS4(cinfo,lvl,code,p1,p2,p3,p4)  \
	MAKESTMT(int * _mp = (cinfo)->err->msg_parm.i; \
	_mp[0] = (p1); _mp[1] = (p2); _mp[2] = (p3); _mp[3] = (p4); \
	(cinfo)->err->msg_code = (code); \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), (lvl)); )
#define TRACEMS5(cinfo,lvl,code,p1,p2,p3,p4,p5)  \
	MAKESTMT(int * _mp = (cinfo)->err->msg_parm.i; \
	_mp[0] = (p1); _mp[1] = (p2); _mp[2] = (p3); _mp[3] = (p4); \
	_mp[4] = (p5); \
	(cinfo)->err->msg_code = (code); \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), (lvl)); )
#define TRACEMS8(cinfo,lvl,code,p1,p2,p3,p4,p5,p6,p7,p8)  \
	MAKESTMT(int * _mp = (cinfo)->err->msg_parm.i; \
	_mp[0] = (p1); _mp[1] = (p2); _mp[2] = (p3); _mp[3] = (p4); \
	_mp[4] = (p5); _mp[5] = (p6); _mp[6] = (p7); _mp[7] = (p8); \
	(cinfo)->err->msg_code = (code); \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), (lvl)); )
#define TRACEMSS(cinfo,lvl,code,str)  \
	((cinfo)->err->msg_code = (code), \
	strncpy((cinfo)->err->msg_parm.s, (str), JMSG_STR_PARM_MAX), \
	(*(cinfo)->err->emit_message) ((translator_common_ptr) (cinfo), (lvl)))
