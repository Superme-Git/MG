#pragma once

namespace fire{
	class UnCopyObject
	{
		UnCopyObject( UnCopyObject&) {}
		UnCopyObject& operator=( UnCopyObject&) { return *this; }
	public:
		UnCopyObject(){};
		virtual ~UnCopyObject(){};
	};
}
