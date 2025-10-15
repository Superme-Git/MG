#include "gxwindowdef.h"

namespace GXWINDOW { 

CUncopyableObject::CUncopyableObject()
{
}

CUncopyableObject::~CUncopyableObject()
{
}

CUncopyableObject::CUncopyableObject( const CUncopyableObject& src)
{
}

CUncopyableObject& CUncopyableObject::operator=( const CUncopyableObject& src)
{
	return *this;
}

} // namespace GXWINDOW { 
