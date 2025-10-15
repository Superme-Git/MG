#pragma once

namespace GXWINDOW {

	class GXWINDOW_EXPORTS_DLL CAudioInterface : public CUncopyableObject
	{
	public:
		CAudioInterface();
		virtual ~CAudioInterface();
	public:
		virtual void Play( int soundid) =  0;
	public:
		static void SetInstance( CAudioInterface& ai);
		static CAudioInterface& GetInstance();
	};
} // namespace GXWINDOW {
