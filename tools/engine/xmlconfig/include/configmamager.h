#pragma once

namespace XMLCONFIG {

	class CConfigManager
	{
		std::wstring		m_xmlfilepath;
		std::wstring		m_cachepath;
	public:
		inline CConfigManager() {}
		inline ~CConfigManager() {}
	public:
		inline void SetXMLFilePath( const std::wstring& path)
		{
			m_xmlfilepath = path;
		}
		inline const std::wstring& GetXMLFilePath() const
		{
			return m_xmlfilepath;
		}
		void SetCachePath( const std::wstring& path);

		template<class BeanType> inline CBeanCache<BeanType>* CreateBeanCacheInstance()
		{
			if( PFS::CBaseFile* file = TryGetCacheFile( GetValueTypeName<BeanType>()))
				return new CBeanCacheFile<BeanType>( file);
			else
				return new CBeanCacheMemory<BeanType>;
		}

		template<class TableType> struct BeanCacheProcedure
		{
			typedef typename TableType::BeanType BeanType;
			TableType& m_cache;

			inline BeanCacheProcedure( TableType& cache)
				: m_cache( cache) 
			{}
			inline void operator()( const BeanType& bean)
			{
				m_cache.InsertBean( bean);
			}
		};

		template<class TableType> inline void MakeTableValues( TableType& table)
		{
			typedef typename TableType::BeanType BeanType;
			std::wstring	classname = GetValueTypeName<BeanType>();
			LoadBeanFromXMLFile<BeanType>( GetXMLFilePath(), classname + L".xml", BeanCacheProcedure<TableType>( table));
		}

	private:
		PFS::CBaseFile* TryGetCacheFile( const std::wstring& classname);
	};

} // namespace XMLCONFIG {
