//////////////////////////////////////////////////////////////////////////
//
// Translator.cpp
// 

#include "Translator.h"

JSAMPARRAY alloc_sarray( int width, int height )
{
	size_t header = height * sizeof( JSAMPROW );
	size_t data_size = width * height * sizeof( JSAMPLE );
	char * buffer = (char *)malloc( header + data_size );
	JSAMPARRAY sarray = (JSAMPARRAY)buffer;
	for ( int i = 0; i < height; i ++ )
	{
		sarray[i] = (JSAMPROW)(buffer + header + i * width);
	}
	
	return sarray;
}

void free_sarray( JSAMPARRAY sarray )
{
	free( sarray );
}

JSAMPROW alloc_srow( int width )
{
	JSAMPROW srow = (JSAMPROW) malloc( width * sizeof( JSAMPLE ) );
	return srow;
}

void free_srow( JSAMPROW srow)
{
	free( srow );
}

static void progress_monitor(translator_common_ptr cinfo)
{
	//printf( 
	//	"counter: %d total: %d total passes: %d\n"
	//	"completed_extra_passes : %d total_extra_passes: %d\n", 
	//	cinfo->progress->pass_counter, 
	//	cinfo->progress->pass_limit, 
	//	cinfo->progress->total_passes,
	//	cinfo->progress->completed_extra_passes,
	//	cinfo->progress->total_extra_passes );
}

translator_progress_ptr my_translator_progress( translator_progress_ptr progress )
{
	progress->progress_monitor = progress_monitor;
	progress->pass_counter = 0; 
	progress->pass_limit = 0; 
	progress->total_passes = 0;
	progress->completed_extra_passes = 0;
	progress->total_extra_passes = 0;

	return progress;
}


void destroy_translator_source(translator_source_ptr sinfo)
{
	if( sinfo->buffer != NULL )
	{
		free_srow( sinfo->buffer );
		sinfo->buffer = NULL;
	}

	free( sinfo );
}

void destroy_translator_dest (translator_dest_ptr dinfo)
{
	if ( dinfo->buffer != NULL )
	{
		free_srow( dinfo->buffer );
		dinfo->buffer = NULL;
	}

	free( dinfo );
}

void create_common_translator(translator_common_ptr cinfo)
{
	cinfo->colormap = NULL;
	cinfo->err = (translator_error_ptr) malloc( sizeof( struct translator_error_mgr ) );
	cinfo->progress = (translator_progress_ptr)malloc( sizeof( struct translator_progress_mgr ) );
}

void destroy_common_translator(translator_common_ptr cinfo)
{
	if( cinfo->colormap != NULL )
	{
		free_sarray( cinfo->colormap );
		cinfo->colormap = NULL;
	}
	free( cinfo->err );	
	free( cinfo->progress );
}

