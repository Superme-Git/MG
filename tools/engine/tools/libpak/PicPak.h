#pragma once

#include "../../pfs/pfs2.h"
#include "PakMetaFile.h"
#include "Picture.h"

namespace MHZX
{
	class CPicture;
	class CPicPak
	{
	public:
		CPicPak(void);

		~CPicPak(void);

		//
		// lpszPakPath - pakĿ¼��pak�ļ�������·���������Ŀ¼������'/'��'\\'��β��
		//
		bool Open( const wchar_t * lpszPakPath, bool bCreateNew = false);
		void Close();

		size_t GetPictureCount() const
		{
			return m_vPictures.size();
		}

		bool InitPicture( size_t index );

		CPicture* GetPicture( size_t index );

		bool AddPicture( CPicture* pPicture );

		bool ReplacePicture( size_t index, CPicture* pPicture );
		
		PFS::CDevice & GetDevice()
		{
			return m_device;
		}

		const PFS::CDevice & GetDevice() const
		{
			return m_device;
		}

		bool CommitMeta();

	protected:
		PFS::CDevice m_device;
		CPakMetaFile m_metaFile;
		std::wstring m_strPakPath;
		std::vector<CPicture*> m_vPictures;
		bool m_bPictureChanged;
	};


} // namespace MHZX