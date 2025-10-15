///=============================================================================
/// Product   : PFS
/// Project   : pfsx-x2o
/// Module    : X2OTranslator.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	XML/Octets Translator
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-1-21 Created.
///=============================================================================
#ifdef _MSC_VER
#pragma once
#endif

#ifndef __X2O_TRANSLATOR_H__
#define __X2O_TRANSLATOR_H__

namespace PFSX {
	namespace X2O {
		//
		// Initialize the lib
		//
		int Initialize();

		long GetEncoderCount();
		void* GetEncoderItem(long lIndex);
		long GetDecoderCount();
		void* GetDecoderItem(long lIndex);

	} // namespace X2O
}// namespace PFSX

#endif // __X2O_TRANSLATOR_H__