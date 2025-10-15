//////////////////////////////////////////////////////////////////////////
// File:		CEGUIStream.h
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUIStream_h
#define _CEGUIStream_h

#include "CEGUIBase.h"
#include "CEGUIUDim.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class CEGUIEXPORT Stream
		{
		public:
			virtual ~Stream() {}

			virtual bool isEOF() const = 0;
			virtual int getSeekPos() const = 0;
			virtual void setSeekPos(int pos) = 0;
			virtual bool read(void* pDstBuffer, int size) = 0;
			virtual bool write(const void* pSrcBuffer, int size) = 0;

			virtual void skip(int delta) { setSeekPos(getSeekPos() + delta); }

			// some helper function

			template<class T>
			bool readT(T& t)
			{
				return read(&t, sizeof(t));
			}

			template<class T>
			bool writeT(const T& t)
			{
				return write(&t, sizeof(t));
			}

			template<class T>
			Stream& operator >> (T& val)
			{
				readT(val);
				return *this;
			}

			template<class T>
			Stream& operator << (const T& val)
			{
				writeT(val);
				return *this;
			}

			Stream& operator >> (std::string& val);
			Stream& operator << (const std::string& val);

			Stream& operator >> (colour& val);
			Stream& operator << (const colour& val);

			Stream& operator >> (ColourRect& val);
			Stream& operator << (const ColourRect& val);

			Stream& operator >> (Rect& val);
			Stream& operator << (const Rect& val);

			Stream& operator >> (Size& val);
			Stream& operator << (const Size& val);

			Stream& operator >> (String& val);
			Stream& operator << (const String& val);

			Stream& operator >> (UDim& val);
			Stream& operator << (const UDim& val);

			Stream& operator >> (UVector2& val);
			Stream& operator << (const UVector2& val);

			Stream& operator >> (URect& val);
			Stream& operator << (const URect& val);

			Stream& operator >> (UBox& val);
			Stream& operator << (const UBox& val);

			Stream& operator >> (Vector2& val);
			Stream& operator << (const Vector2& val);

			Stream& operator >> (Vector3& val);
			Stream& operator << (const Vector3& val);

			Stream& operator >> (Vertex& val);
			Stream& operator << (const Vertex& val);
		};
	}
}

#endif