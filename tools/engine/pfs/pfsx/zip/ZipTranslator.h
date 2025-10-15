//////////////////////////////////////////////////////////////////////////
//
// ZipTranslator.h
//
// created:	2008/09/12
// author : yanghaibo
//

#ifdef _MSC_VER
#pragma once
#endif

#ifndef __ZIP_TRANSLATOR_H__
#define __ZIP_TRANSLATOR_H__

namespace PFSX {
	namespace ZIP {

		//
		// Initialize the lib
		//
		int Initialize();

		long GetEncoderCount();
		void* GetEncoderItem(long lIndex);
		long GetDecoderCount();
		void* GetDecoderItem(long lIndex);
	}
}

#endif // __ZIP_TRANSLATOR_H__