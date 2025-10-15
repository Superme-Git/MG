#ifndef TTYPES_H
#define TTYPES_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace TMan
{
	typedef unsigned int ThumbnailHandle;
	const ThumbnailHandle INVALID_THUNMBNAIL_HANDLE = 0;

	typedef unsigned int PictureHandle;
	const PictureHandle INVALID_PICTURE_HANDLE = 0;

	enum TPOOL_FORMAT
	{
		TPF_DEFAULT = 0,
		TPF_8888 = 0,
		TPF_4444 = 1,
	};

	template<class T>
	struct TRECT
	{
		T left;
		T top;
		T right;
		T bottom;

		TRECT(T l = 0, T t = 0, T r = 0, T b = 0) : left(l), top(t), right(r), bottom(b) { }
		TRECT& Assign(T l, T t, T r, T b) { left = l; top = t; right = r; bottom = b; return *this; }
		T Width() const { return right - left; }
		T Height() const { return bottom - top; }
	};

	typedef TRECT<float> FRECT;
	typedef TRECT<int> CRECT;
}

#endif //#ifndef TTYPES_H