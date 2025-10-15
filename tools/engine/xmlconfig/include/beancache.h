#pragma once

namespace XMLCONFIG {

	template<class BeanType> class CBeanCache
	{
	public:
		CBeanCache() {}
		virtual ~CBeanCache() {}
	public:
		virtual void InsertBean( const BeanType& bean) = 0;
		virtual const BeanType& GetBean( int id) const = 0;
		virtual void GetAllID( std::vector<int>& ids) const = 0;
	};

	template<class BeanType> class CBeanCacheMemory : public CBeanCache<BeanType>
	{
		typedef std::map<int,BeanType> CBeanMap;
		CBeanMap			m_map;
		mutable BeanType	m_temp;
	public:
		CBeanCacheMemory() 
		{
			m_temp.id = -1;
		}
		virtual ~CBeanCacheMemory() {}
	public:
		virtual void InsertBean( const BeanType& bean)
		{
			m_map.insert( std::make_pair( bean.id, bean));
		}
		virtual const BeanType& GetBean( int id) const
		{
			if( -1 == id)
				return m_temp;
			CBeanMap::const_iterator it = m_map.find( id);
			if( it == m_map.end())
				return m_temp;
			else
				return it->second;
		}
		virtual void GetAllID( std::vector<int>& ids) const
		{
			ids.clear();
			ids.reserve( m_map.size());

			for( CBeanMap::const_iterator it = m_map.begin(), ite = m_map.end(); it != ite; ++ it)
				ids.push_back( it->first);
		}
	};
	template<class BeanType> class CBeanCacheFile : public CBeanCache<BeanType>
	{
		typedef std::map<int,PFS::fsize_t> CKeyMap;
		CKeyMap				m_map;
		mutable BeanType	m_temp;
		PFS::CBaseFile*		m_file;
	public:
		CBeanCacheFile( PFS::CBaseFile* file)
			: m_file( file)
		{}
		virtual ~CBeanCacheFile() 
		{
			delete m_file;
		}
		virtual void InsertBean( const BeanType& bean)
		{
			PFS::fsize_t pos = m_file->GetPos();
			PFS::CFileStream		fs( *m_file);
			fs << bean;
			m_map.insert( std::make_pair( bean.id, pos));
		}
		virtual const BeanType& GetBean( int id) const
		{
			m_temp.id = -1;
			if( -1 == id)
				return m_temp;
			CKeyMap::const_iterator it = m_map.find( id);
			if( it == m_map.end())
				return m_temp;
			
			m_file->Seek( it->second, PFS::FSM_SET);

			try
			{
				PFS::CFileStream		fs( *m_file);
				fs >> m_temp;
			}
			catch( PFS::CFileStreamException&) 
			{
				m_temp.id = -1;
			}
			return m_temp;
		}
		virtual void GetAllID( std::vector<int>& ids) const
		{
			ids.clear();
			ids.reserve( m_map.size());

			for( CKeyMap::const_iterator it = m_map.begin(), ite = m_map.end(); it != ite; ++ it)
				ids.push_back( it->first);
		}
	};

} // namespace XMLCONFIG {

namespace XMLCONFIG {

	template<class Container> inline PFS::CFileStream& ContainerToFileStream( PFS::CFileStream& fs, const Container& src)
	{
		unsigned int count = (unsigned int)src.size();
		fs << count;
		for( typename Container::const_iterator it = src.begin(), ite = src.end(); it != ite; ++ it)
		{
			const typename Container::value_type& value = *it;
			fs << value;
		}
		return fs;
	}

	template<class ValueType> inline PFS::CFileStream& operator<<( PFS::CFileStream& fs, const std::vector<ValueType>& src)
	{
		return ContainerToFileStream( fs, src);
	}
	template<class ValueType> inline PFS::CFileStream& operator<<( PFS::CFileStream& fs, const std::set<ValueType>& src)
	{
		return ContainerToFileStream( fs, src);
	}

	template<class ValueType> inline const PFS::CFileStream& operator>>( const PFS::CFileStream& fs, std::vector<ValueType>& src)
	{
		unsigned int size;
		fs >> size;
		src.reserve( size);
		for( unsigned int i = 0; i < size; i ++)
		{
			ValueType	value;
			fs >> value;
			src.push_back( value);
		}
		return fs;
	}

	template<class ValueType> inline const PFS::CFileStream& operator>>( const PFS::CFileStream& fs, std::set<ValueType>& src)
	{
		unsigned int size;
		fs >> size;
		for( unsigned int i = 0; i < size; i ++)
		{
			ValueType	value;
			fs >> value;
			src.insert( value);
		}
		return fs;
	}


} // namespace XMLCONFIG {

namespace XMLCONFIG {

	template<class IndexType> class CUniqueIndex
	{
		typedef std::map<IndexType,int> CIndexMap;
		CIndexMap	m_map;
	public:
		inline CUniqueIndex() {}
		inline ~CUniqueIndex() {}
	public:
		inline void Insert( int id, const IndexType& index)
		{
			m_map.insert( std::make_pair( index, id));
		}
		inline int GetID( const IndexType& index) const
		{
			CIndexMap::const_iterator it = m_map.find( index);
			if( it == m_map.end())
				return -1;
			else
				return it->second;
		}
	};

	template<class IndexType> class CMultiIndex
	{
		typedef std::vector<int> CIDVector;
		typedef std::map<IndexType,CIDVector> CIndexMap;
		CIndexMap	m_map;
		CIDVector	m_temp;
	public:
		inline CMultiIndex() {}
		inline ~CMultiIndex() {}
	public:
		inline void Insert( int id, const IndexType& index)
		{
			m_map[index].push_back( id);
		}
		inline const std::vector<int>& GetID( const IndexType& index) const
		{
			CIndexMap::const_iterator it = m_map.find( index);
			if( it == m_map.end())
				return m_temp;
			else
				return it->second;
		}
	};

} // namespace XMLCONFIG {
