//////////////////////////////////////////////////////////////////////////
//
// PicTranslator.h
//
// created:	2008/10/06
// author : yanghaibo
//

#ifdef _MSC_VER
#pragma once
#endif

#ifndef __PIC_TRANSLATOR_H__
#define __PIC_TRANSLATOR_H__

namespace PFSX {
	namespace PIC {

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

#endif // __PIC_TRANSLATOR_H__