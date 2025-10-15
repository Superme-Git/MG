#pragma once

namespace MHZX
{
	class PFS::CDevice;

	class CPakMetaFile
	{
	public:
		CPakMetaFile(void);
		~CPakMetaFile(void);
		
		bool Open( PFS::CDevice* pDevice );

		void Close();
	};

}