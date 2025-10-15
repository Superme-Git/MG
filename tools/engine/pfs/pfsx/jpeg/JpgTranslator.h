//////////////////////////////////////////////////////////////////////////
//
// JpgTranslator.h
//
// created:	2008/09/12
// author : yanghaibo
//

#ifdef _MSC_VER
#pragma once
#endif

#ifndef __JPG_TRANSLATOR_H__
#define __JPG_TRANSLATOR_H__

namespace PFSX {
	namespace JPEG {

		//
		// Initialize the lib
		//
		int Initialize();
		
		long GetEncoderCount();
		void* GetEncoderItem(long lIndex);
		long GetDecoderCount();
		void* GetDecoderItem(long lIndex);

	} // JPEG
}// PFSX

#endif // __JPG_TRANSLATOR_H__

