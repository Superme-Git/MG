//////////////////////////////////////////////////////////////////////////
//
// TargaTranslator.h
//
// created:	2008/09/12
// author : yanghaibo
//

#ifdef _MSC_VER
#pragma once
#endif

#ifndef __TARGA_TRANSLATOR_H__
#define __TARGA_TRANSLATOR_H__

namespace PFSX {
	namespace TARGA {
		//
		// Initialize the lib
		//
		int Initialize();

		long GetEncoderCount();
		void* GetEncoderItem(long lIndex);
		long GetDecoderCount();
		void* GetDecoderItem(long lIndex);

	} // TARGA
}// PFSX

#endif // __TARGA_TRANSLATOR_H__