
namespace ZIP{ 

	namespace helper { class CMemoryData; }

	class CDataBlock
	{
		helper::CMemoryData*	m_data;
	public:

		//
		// 注意：c仅为初始保留容量(capacity)，而非size
		//
		CDataBlock( size_t c = 0);

		// 注意：
		// data 为初始化数据
		// s为初始大小(size)
		CDataBlock( const void* data, size_t s);
		~CDataBlock();
		CDataBlock( const CDataBlock& src);
		CDataBlock& operator=( const CDataBlock& src);
	private:
		void Unique();

		// c is the reserved data length
		// s is the new size
		void UniqueReserveAndResize(size_t c, size_t s);
	public:
		void* GetData();
		const void* GetData() const;
		size_t GetSize() const;
		void Resize( size_t s);
		void Reserve( size_t s);
		void Clear( bool deep = false);
		void Assign( const void* data, size_t size);
		void Append( const void* data, size_t size);
		void Append( const CDataBlock& data);
		void Swap( CDataBlock& dst);
	public:
		bool Compare( const CDataBlock& dst) const;
		bool operator!=( const CDataBlock& dst) const;
		bool operator==( const CDataBlock& dst) const;
	public:
		void Dump() const;
	};
}