#pragma once
#include "ljfmopen.h"

namespace LJFM
{
	class LJFMF : public LJFMBF
	{
	private:
		LJFMBF*	m_file;

	protected:
		void AttackBaseFile(LJFMBF* file);
		LJFMBF* DetackBaseFile();
	public:
		LJFMF() : m_file(NULL) {}
		virtual ~LJFMF();
	public:
		LJFMBF& GetBaseFile() { return *m_file; }
	public:
		virtual size_t Read(void* data, size_t size);
		virtual size_t Write(const void* data, size_t size);
		virtual fsize_t GetSize();
		virtual fsize_t Seek(foff_t pos, FILE_SEEK_METHOD fsm);
		virtual fsize_t GetPos();
		virtual bool SetEnd();
		virtual LJFMID GetImage();

		virtual bool ReadAll(void* data, fsize_t size);

	public:
		static std::wstring GetFullPathFileName(const std::wstring& filename);
		virtual bool Open(const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa);
		virtual void Close();

	public:

	};

	class LJFMIF : public LJFMBF
	{
	private:
		LJFMID	m_data;
		fsize_t		m_sizeOffset;
	public:
		LJFMIF() : m_sizeOffset(0) {}
		LJFMIF(const LJFMID& id) : m_data(id), m_sizeOffset(0) {}
		LJFMIF(const LJFMDB& data) : m_data(LJFMDBToID(data)), m_sizeOffset(0) {}
		virtual ~LJFMIF() {}
	public:
		virtual size_t Read(void* data, size_t size);
		virtual size_t Write(const void* data, size_t size);
		virtual fsize_t GetSize();
		virtual fsize_t Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm);
		virtual fsize_t GetPos();
		virtual bool SetEnd();

		virtual LJFM::LJFMID GetImage();

		virtual bool ReadAll(void* data, fsize_t size);

	public:
		virtual bool Open(const LJFMID& image);
		virtual bool Open(const LJFMDB& data);
	};

	class LJFMDF : public LJFMBF
	{
	protected:
		LJFMDB		m_data;
		size_t			m_offset;
	public:
		LJFMDF() : m_offset(0) {}
		virtual ~LJFMDF() {}
	public:
		virtual size_t Read(void* data, size_t size);
		virtual size_t Write(const void* data, size_t size);
		virtual fsize_t GetSize();
		virtual fsize_t Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm);
		virtual fsize_t GetPos();
		virtual bool SetEnd();
		virtual LJFM::LJFMID GetImage();

		virtual bool ReadAll(void* data, fsize_t size);
	};

	class LJFMMF : public LJFMDF
	{
	public:
		virtual size_t Read(void* data, size_t size);

		virtual bool ReadAll(void* data, fsize_t size);
	};

}
