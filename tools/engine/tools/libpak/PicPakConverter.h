#pragma once
namespace MHZX
{
	class IImageEncoder;
	class CPicPak;

	class CPicPakConverter
	{
	public:
		bool Convert(CPicPak & rSourcePak, CPicPak& rDestPak, IImageEncoder* pEncoder, bool bVerbose = false );
	};
}