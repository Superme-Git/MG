#pragma once

#include <string>
#include <set>

namespace LJFM
{
	class LJFMBF;

	class LJFMBase
	{
	private:
		typedef std::set<std::wstring>	CRefMountFSSet;
		CRefMountFSSet		m_rmfs;
		bool				m_bMetaChanged;

	protected:
		std::wstring		m_deviceName;
		LJFMBase*			m_BaseLJFM;
		unsigned short		m_usFilesMetaVersion;

	public:
		LJFMBase() : m_BaseLJFM(NULL), m_bMetaChanged(false), m_usFilesMetaVersion(0x0100) {}
		LJFMBase(FS_TYPE type);

		virtual ~LJFMBase() {}

	public:
		virtual LJFMBase* Clone() const = 0;
		virtual void Delete()
		{
			delete this;
		}
		virtual int Initialize(const std::wstring& device) = 0;
		virtual FS_TYPE GetFSType() const = 0;
		virtual int OpenFile(const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, LJFMBF*& file) = 0;
		virtual int RemoveFile(const std::wstring& filename) = 0;

#ifdef CreateDirectory
#pragma push_macro( "CreateDirectory" )
#undef CreateDirectory
#endif // CreateDirectory

#ifdef RemoveDirectory
#pragma push_macro( "RemoveDirectory" )
#undef RemoveDirectory
#endif // RemoveDirectory

		virtual int CreateDirectory(const std::wstring& path, bool bFailIfExisting = false) = 0;
		virtual int RemoveDirectory(const std::wstring& path, bool bFailIfNotEmpty = false) = 0;
		virtual bool IsDirectoryExisting(const std::wstring& path) = 0;

		virtual bool IsFSBusy() const = 0;
		virtual bool IsFileExisting(const std::wstring& filename) = 0;
		virtual void OnFileClose(LJFMBF* pFile) {};

		virtual void CheckMetaInfo() = 0;

		virtual bool Destroy();


	public:
		static LJFMBase* Create(FS_TYPE type);

	};
}