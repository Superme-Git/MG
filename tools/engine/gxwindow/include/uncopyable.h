#pragma once

namespace GXWINDOW { 

class GXWINDOW_EXPORTS_DLL CUncopyableObject
{
public:
	CUncopyableObject();
	virtual ~CUncopyableObject();
private:
	CUncopyableObject( const CUncopyableObject& src);
	CUncopyableObject& operator=( const CUncopyableObject& src);
};

} // namespace GXWINDOW { 
