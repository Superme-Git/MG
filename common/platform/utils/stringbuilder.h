#pragma once

#include <string>
#include <vector>
#include <map>

#ifdef ANDROID
#include "StringUtil.h"
#endif

namespace STRB {
	namespace helper {

		template<typename CharType,typename NumberType> inline void StringPrintf( CharType* buffer, size_t size, const CharType* format, NumberType number)
		{
			//assert( !"没有实现的CharType类型的StringPrintf");
		}
		template<typename NumberType> inline void StringPrintf( char* buffer, size_t size, const char* format, NumberType number)
		{
			snprintf( buffer, size, format, number);
		}

		template<typename NumberType> inline void StringPrintf( wchar_t* buffer, size_t size, const wchar_t* format, NumberType number)
		{
//			swprintf( buffer, size, format, number);
            SprintfW(buffer, size, format,number);
		}

		template<typename CharType> struct stringicmp
		{
			bool operator()( const std::basic_string<CharType>& a, const std::basic_string<CharType>& b) const
			{
				//assert( !"没有实现的CharType类型的stringicmp");
			}
		};

		template<> struct stringicmp<char>
		{
			bool operator()( const std::string& a, const std::string& b) const
			{
#ifdef _MSC_VER
				return _stricmp( a.c_str(), b.c_str()) < 0;
#else
				return strcasecmp( a.c_str(), b.c_str()) < 0;
#endif
			}
		};

		template<> struct stringicmp<wchar_t>
		{
			bool operator()( const std::wstring& a, const std::wstring& b) const
			{
#ifdef _MSC_VER
				return _wcsicmp( a.c_str(), b.c_str()) < 0;
#else
				return wcscasecmp( a.c_str(), b.c_str()) < 0;
#endif
			}
		};

	} // namespace helper {

	template<typename CharType> class CVariableDefines
	{
	public:
		typedef std::basic_string<CharType> StringType;
	private:
		typedef std::map< StringType, StringType, helper::stringicmp<CharType> > VDMap;
		VDMap		m_map;
	public:
		CVariableDefines() {}
		CVariableDefines( const CVariableDefines<CharType>& src)
		{
			m_map = src.m_map;
		}
		virtual ~CVariableDefines() {}
	public:
		//! return true = update；false = insert
		inline bool Set( const StringType& name, const StringType& value)
		{
			typename VDMap::iterator it = m_map.find( name);
			if( it == m_map.end())
			{
				m_map.insert( std::make_pair( name, value)).second;
				return true;
			}
			else
			{
				it->second = value;
				return false;
			}
		}
		//! retrun true = 找到了相应的变量，并且value有值了；false = 没有找到对应名字的变量
		bool Get( const StringType& name, StringType& value) const
		{
			typename VDMap::const_iterator it = m_map.find( name);
			if( it == m_map.end())
				return false;
			value = it->second;
			return true;
		}
	public:
		template<typename NumberType> inline bool SetFormat( const StringType& name, const CharType* format, NumberType value)
		{
			const size_t buffersize = 32;
			CharType		buffer[buffersize];

			helper::StringPrintf( buffer, buffersize, format, value);
			return Set( name, buffer);
		}
	};

	namespace helper {

		typedef CVariableDefines<char> CMultiByteVariableDefines;
		typedef CVariableDefines<wchar_t> CUnicodeVariableDefines;

		CMultiByteVariableDefines& GetGlobalMultiByteVariableDefinesInstance();
		CUnicodeVariableDefines& GetGlobalUnicodeVariableDefinesInstance();

		template<typename CharType> inline CVariableDefines<CharType>& GlobleVariableDefines()
		{
			//assert( !"没有实现的CharType类型的GlobleVariableDefines");
		}

		template<> inline CVariableDefines<char>& GlobleVariableDefines<char>()
		{
			return GetGlobalMultiByteVariableDefinesInstance();
		}

		template<> inline CVariableDefines<wchar_t>& GlobleVariableDefines<wchar_t>()
		{
			return GetGlobalUnicodeVariableDefinesInstance();
		}

	} // namespace helper {

	namespace helper {

		template<typename CharType> inline CharType GetVariableMarker()
		{
			//assert( !"没有实现的CharType类型的GetVariableMarker");
		}

		template<> inline char GetVariableMarker<char>()
		{
			return '$';
		}

		template<> inline wchar_t GetVariableMarker<wchar_t>()
		{
			return L'$';
		}

	} // namespace helper {

	namespace helper {

		template<typename CharType> inline const CharType* GetStringBuilderErrorFormatString()
		{
			//assert( !"没有实现的CharType类型的GetStringBuilderErrorFormatString");
		}

		template<> inline const char* GetStringBuilderErrorFormatString<char>()
		{
			return "bad format : $string$";
		}

		template<> inline const wchar_t* GetStringBuilderErrorFormatString<wchar_t>()
		{
			return L"bad format : $string$";
		}

		template<typename CharType> inline const CharType* GetStringBuilderLostVariableString()
		{
			//assert( !"没有实现的CharType类型的GetStringBuilderLostVariableString");
		}

		template<> inline const char* GetStringBuilderLostVariableString<char>()
		{
			return "lost Variable : string = \"$string$\" name=\"$name$\"";
		}

		template<> inline const wchar_t* GetStringBuilderLostVariableString<wchar_t>()
		{
			return L"lost Variable : string = \"$string$\" name=\"$name$\"";
		}

		template<typename CharType> inline const CharType* GetStringBuilderNameStringString()
		{
			//assert( !"没有实现的CharType类型的GetStringBuilderNameStringString");
		}

		template<> inline const char* GetStringBuilderNameStringString<char>()
		{
			return "string";
		}

		template<> inline const wchar_t* GetStringBuilderNameStringString<wchar_t>()
		{
			return L"string";
		}

		template<typename CharType> inline const CharType* GetStringBuilderNameNameString()
		{
			//assert( !"没有实现的CharType类型的GetStringBuilderNameNameString");
		}

		template<> inline const char* GetStringBuilderNameNameString<char>()
		{
			return "name";
		}

		template<> inline const wchar_t* GetStringBuilderNameNameString<wchar_t>()
		{
			return L"name";
		}

	}  // namespace helper {

	template<typename CharType> class CStringBuilder
	{
		CStringBuilder& operator=( const CStringBuilder&) { return *this; }
	public:
		typedef CVariableDefines<CharType> VariableDefinesType;
		typedef std::basic_string<CharType> StringType;
	private:
		mutable StringType		m_BufferString;
		VariableDefinesType		m_VariableDefines;
	public:
		inline CStringBuilder()	{}
		inline CStringBuilder( const VariableDefinesType& vd)
			: m_VariableDefines( vd)
		{}
		inline CStringBuilder( const CStringBuilder<CharType>& sb)
			: m_VariableDefines( sb.m_VariableDefines)
		{}
		inline ~CStringBuilder() {}
	public:
		inline VariableDefinesType& GetVariableDefines()
		{
			return m_VariableDefines;
		}
		inline const VariableDefinesType& GetVariableDefines() const
		{
			return m_VariableDefines;
		}
		static inline VariableDefinesType& GetGlobalVariableDefines()
		{
			return helper::GlobleVariableDefines<CharType>();
		}
	private:
		const CharType* MakeErrorFormatString( StringType& buffer, const StringType& src) const
		{
			CStringBuilder<CharType> sb;
			sb.GetVariableDefines().Set( helper::GetStringBuilderNameStringString<CharType>(), src);
			buffer = sb.GetString( helper::GetStringBuilderErrorFormatString<CharType>());
			return buffer.c_str();
		}
		const CharType* MakeLostVariableString( StringType& buffer, const StringType& src, const StringType& name) const
		{
			CStringBuilder<CharType> sb;
			sb.GetVariableDefines().Set( helper::GetStringBuilderNameStringString<CharType>(), src);
			sb.GetVariableDefines().Set( helper::GetStringBuilderNameNameString<CharType>(), name);
			buffer = sb.GetString( helper::GetStringBuilderLostVariableString<CharType>());
			return buffer.c_str();
		}
		inline bool GetVariableDefines( const StringType& name, StringType& value) const
		{
			if( m_VariableDefines.Get( name, value))
				return true;
			else
				return GetGlobalVariableDefines().Get( name, value);
		}
	public:
		const CharType* GetString( const CharType* src, size_t length = -1) const
		{
			m_BufferString.clear();
			if( NULL == src)
				return m_BufferString.c_str();
			if( (size_t)-1 == length)
				return GetString( StringType( src));
			else
				return GetString( StringType( src, length));
		}

		const CharType* GetString( const StringType& src) const
		{
			const CharType marker = helper::GetVariableMarker<CharType>();
			StringType name, value;

			m_BufferString.clear();
			m_BufferString.reserve( src.size());

			size_t currentpos = 0;
			size_t markerindex = src.find( marker, currentpos);
			while( StringType::npos != markerindex)
			{
				m_BufferString.append( src, currentpos, markerindex - currentpos);
				markerindex ++;
				size_t endindex = src.find( marker, markerindex);
				if( StringType::npos == endindex)
					return MakeErrorFormatString( m_BufferString, src);

				name = src.substr( markerindex, endindex - markerindex);
				if( name.empty())
				{
					m_BufferString.append( 1, marker);
				}
				else
				{
					if( !GetVariableDefines( name, value))
						return MakeLostVariableString( m_BufferString, src, name);
					m_BufferString.append( value);
				}

				currentpos = endindex + 1;
				markerindex = src.find( marker, currentpos);
			} // while( StringType::npos != markerindex)
			m_BufferString.append( src, currentpos, src.size() - currentpos);
			return m_BufferString.c_str();
		}

		inline bool Set( const StringType& name, const StringType& value)
		{
			return m_VariableDefines.Set( name, value);
		}

		template<typename NumberType> inline bool SetFormat( const StringType& name, const CharType* format, NumberType value)
		{
			return m_VariableDefines.SetFormat( name, format, value);
		}
	};

	typedef CStringBuilder<char> CStringBuilderA;
	typedef CStringBuilder<wchar_t> CStringBuilderW;

} // namespace STRB {
