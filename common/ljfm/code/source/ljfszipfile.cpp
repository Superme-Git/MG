#include "../include/common.h"
#include "../include/ljfileinfo.h"

#ifdef ANDROID
#include "cocos2d.h"
#endif

namespace LJFMX
{
	CLJFSZipFile::CLJFSZipFile() 
	{
		m_UnitPos = 0;
		m_UnitSize = 0;

		m_DataUnCompress = NULL;
		m_DataDeCode = NULL;

		m_pDataBegin = NULL;
		m_pDataCur = NULL;

	}
	CLJFSZipFile::~CLJFSZipFile() 
	{ 
		Close();
	}
	bool CLJFSZipFile::Open(void* FileInfo)
	{
		if (FileInfo == NULL)
		{
			return false;
		}
		LJFP_FileInfo* FI = (LJFP_FileInfo*)FileInfo;
		m_FullPathFileName = FI->GetFullPathFileName();
		unsigned char* pData = NULL;
#ifdef ANDROID
		unsigned long ulSize;
		if (FI->m_FileArea == 0)
		{
			std::string PFN = ws2s(FI->m_PathFileName);
			pData = cocos2d::CCFileUtils::sharedFileUtils()->getFileData(PFN.c_str(), "r", &ulSize);
		}
		else
		{
			LJFMX::CLJFSFile* pLJFSFile = new LJFMX::CLJFSFile();
			if (pLJFSFile->Open(FileInfo) == false)
			{
				return false;
			}
			Close();
			pData = (char*)malloc(pLJFSFile->GetSize());
			memset(pData, 0, pLJFSFile->GetSize());
			pLJFSFile->ReadAll(pData, pLJFSFile->GetSize());
			delete pLJFSFile;
		}

#else
		LJFMX::CLJFSFile* pLJFSFile = new LJFMX::CLJFSFile();
		if (pLJFSFile->Open(FileInfo) == false)
		{
			return false;
		}
		Close();
		pData = (unsigned char*)malloc(pLJFSFile->GetSize());
		memset(pData, 0, pLJFSFile->GetSize());
		pLJFSFile->ReadAll(pData, pLJFSFile->GetSize());
		delete pLJFSFile;
#endif
		m_pDataBegin = pData;
		m_pDataCur = m_pDataBegin;
		m_UnitSize = ((LJFP_FileInfo*)FI)->m_Size;
		if (FI->m_CodeType == 1)
		{
			DeCode(FI, m_pDataBegin, m_DataDeCode);
			if (pData)
			{
				free(pData);
				pData = NULL;
			}
			m_pDataBegin = m_DataDeCode;
			m_pDataCur = m_pDataBegin;
		}
		if (FI->m_CompressType == 1)
		{
			UnCompress(FI, m_pDataBegin, m_DataUnCompress);
			if (FI->m_CodeType == 1)
			{
				if (m_DataDeCode)
				{
					free(m_DataDeCode);
					m_DataDeCode = NULL;
				}
			}
			else
			{
				if (pData)
				{
					free(pData);
					pData = NULL;
				}
			}
			m_pDataBegin = m_DataUnCompress;
			m_pDataCur = m_pDataBegin;
			m_UnitSize = ((LJFP_FileInfo*)FI)->m_SizeOriginal;
		}
		return true;
	}
	void CLJFSZipFile::Close()
	{
		if (m_pDataBegin)
		{
			free(m_pDataBegin);
		}
		m_DataUnCompress = NULL;
		m_DataDeCode = NULL;
		m_pDataBegin = NULL;
		m_pDataCur = NULL;
	}
	bool CLJFSZipFile::UnCompress(void* pFI, unsigned char* pDataSrc, unsigned char*& pDataDst)
	{
		LJUnCompress((LJFP_FileInfo*)pFI, pDataSrc, pDataDst);
		return true;
	}
	bool CLJFSZipFile::DeCode(void* pFI, unsigned char* pDataSrc, unsigned char*& pDataDst)
	{		
		LJDeCode((LJFP_FileInfo*)pFI, pDataSrc, pDataDst);
		return true;
	}
	size_t CLJFSZipFile::Read(void* data, size_t size)
	{
		m_pDataCur = m_pDataBegin;
		m_pDataCur += GetPos();
		memcpy(data, m_pDataCur, size);
		m_UnitPos += size;
		return size;
	}
	size_t CLJFSZipFile::Write(const void* data, size_t size)
	{
		//return m_pLJFile->Write(data, size);
		return size;
	}
	fsize_t CLJFSZipFile::GetSize()
	{
		return m_UnitSize;
	}
	fsize_t CLJFSZipFile::Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm)
	{
		if (fsm == LJFM::FILE_SEEK_METHOD::FSM_SET)
		{
			m_UnitPos = pos;
		}
		else if (fsm == LJFM::FILE_SEEK_METHOD::FSM_CUR)
		{
			m_UnitPos += pos;
		}
		else if (fsm == LJFM::FILE_SEEK_METHOD::FSM_END)
		{
			m_UnitPos = GetSize() - pos - 1;
		}
		if (m_UnitPos >= GetSize())
		{
			m_UnitPos = GetSize() - 1;
		}
		if (m_UnitPos < 0)
		{
			m_UnitPos = 0;
		}
		return true;
	}
	fsize_t CLJFSZipFile::GetPos()
	{
		return m_UnitPos;
	}
	bool CLJFSZipFile::SetEnd()
	{
		Seek(0, LJFM::FILE_SEEK_METHOD::FSM_END);
		return true;
	}
	LJFM::LJFMID CLJFSZipFile::GetImage()
	{
		return BaseFileToImageDataEx(*this, 0, GetSize());
	}
	bool CLJFSZipFile::ReadAll(void* data, fsize_t size)
	{
		Seek(0, LJFM::FILE_SEEK_METHOD::FSM_SET);
		Read(data, GetSize());
		return true;
	}
	std::wstring CLJFSZipFile::GetFullPathFileName()
	{
		return m_FullPathFileName;
	}
}