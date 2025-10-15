#ifndef __Nuclear_ISELECTABLEOBJ_H__
#define __Nuclear_ISELECTABLEOBJ_H__

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	// 可用通过鼠标选择到
	class ISelectableObj
	{
	public:
		ISelectableObj() { }
		virtual ~ISelectableObj() { }
	};

}

#endif