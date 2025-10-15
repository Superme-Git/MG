#pragma once
#ifdef _MSC_VER
#include "ljfmimage.h"
namespace LJFMX
{

	namespace helper { class CAsyncRead; class CSyncRead; }

	enum { SIZE_ENTRY_FILE = -1, };

	class CAyncReadBaseNotify : public LJFM::LJFMUO
	{
		friend helper::CAsyncRead;
	public:
		CAyncReadBaseNotify() {}
		virtual ~CAyncReadBaseNotify() {}
	protected:

		virtual bool DoRead(const std::wstring& filename, LJFM::LJFMID& data, int& error) = 0;

		virtual void OnReady(int nError, const std::wstring& filename, const LJFM::LJFMID& data) {}
	};

	class CAyncReadNotify : public CAyncReadBaseNotify
	{
	public:
		CAyncReadNotify() {}
		virtual ~CAyncReadNotify() {}
	protected:
		virtual bool DoRead(const std::wstring& filename, LJFM::LJFMID& data, int& error);

		virtual bool DoAfterRead(int nError, const std::wstring& filename, const LJFM::LJFMID& data)
		{
			return false;
		}

		virtual bool DoOpen(LJFM::LJFMF& file, const std::wstring& filename)
		{
			return file.Open(filename, LJFM::FM_EXCL, LJFM::FA_RDONLY);
		}
	};

	int AsyncReadInitialize(int levelcount = 1);
	int AsyncReadUninitialize();
	int AsyncReadUpdate(bool bAll = false);
	int AsyncReadClear();
	int AsyncRead(const std::wstring& filename, CAyncReadBaseNotify& notify, int level = 0);


	class CSyncFile : public LJFM::LJFMIF
	{
		friend helper::CSyncRead;
	public:
		CSyncFile() {}
		virtual ~CSyncFile() {}
	public:
		bool Open(const std::wstring& filename);
	};

}

#elif defined(_OS_IOS)
namespace LJFMX
{
	class CSyncFile : public LJFM::LJFMIF
	{

	public:
		CSyncFile() {}
		virtual ~CSyncFile() {}
	public:
		bool Open(const std::wstring& filename);
	};
}
#endif
