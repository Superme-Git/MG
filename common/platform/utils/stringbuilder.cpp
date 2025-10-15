#include "stringbuilder.h"

namespace STRB {
	namespace helper {

		CMultiByteVariableDefines& GetGlobalMultiByteVariableDefinesInstance()
		{
			static CMultiByteVariableDefines	instance;
			return instance;
		}
		CUnicodeVariableDefines& GetGlobalUnicodeVariableDefinesInstance()
		{
			static CUnicodeVariableDefines instance;
			return instance;
		}

	} // namespace helper {
} // namespace STRB {
