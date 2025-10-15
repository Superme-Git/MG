#ifndef __FIRENET_MARSHAL_H__
#define __FIRENET_MARSHAL_H__
#pragma once

#include "../../platform/log/CoreLog.h"

namespace FireNet
{
	template<typename T>
	inline T& remove_const(const T &t) { return const_cast<T&>(t); }

	class Marshal
	{
	public:
		class Exception
		{
		public:
			std::string what;
			Exception (){}
			Exception( const char *t):what(t){}
		};

		enum Transaction { Begin, Commit, Rollback };

		class OctetsStream
		{
			enum { MAXSPARE = 16384};
			Octets  data;
			mutable uint32_t pos;
			mutable uint32_t tranpos;

			template<typename T> OctetsStream& push_byte(T t)
			{
				data.insert(data.end(), &t, sizeof(t));
				return *this;
			}
			template<typename T> void pop_byte(T &t) const
			{
				if (pos + sizeof(t) > data.size()) {
					throw Marshal::Exception("marshal::pop_byte\n");
				}
				t = *(T *)( (char*)data.begin() + pos );
				pos += sizeof(t);
			}
			uint8_t pop_byte_8() const;
			uint16_t pop_byte_16() const;
			uint32_t pop_byte_32() const;
			uint64_t pop_byte_64() const;

			friend class CompactUINT;
			friend class CompactSINT;

        public:
			OctetsStream& compact_uint32( size_t x);
			OctetsStream& compact_sint32( int32_t x);
			const OctetsStream& uncompact_uint32( const uint32_t &x) const;
			const OctetsStream& uncompact_sint32( const int32_t &x) const;
		
			OctetsStream();
			~OctetsStream(){}
			OctetsStream(const Octets &o);
			OctetsStream(const OctetsStream &os);

			OctetsStream& operator = (const OctetsStream &os);

			bool operator == (const OctetsStream &os) const;
			bool operator != (const OctetsStream &os) const;
			size_t size() const;
			void swap (OctetsStream &os);
			operator Octets& ();
			operator const Octets& () const;
			size_t position() const;
			bool isvalid();

			void *begin();
			void *end();
			void reserve(size_t size);
			const void *begin() const;
			const void *end() const;
			void insert(void *pos, const void *x, size_t len);
			void insert(void *pos, const void *x, const void *y);
			void erase(void *x, void *y);
			void clear();

			OctetsStream& push_byte(const char *x, size_t len);

			OctetsStream& operator << (char x);
			OctetsStream& operator << (unsigned char x);
			OctetsStream& operator << (bool x);
			OctetsStream& operator << (short x);
			OctetsStream& operator << (unsigned short x);
			OctetsStream& operator << (int32_t x);
			OctetsStream& operator << (uint32_t x);
			OctetsStream& operator << (int64_t x);
			OctetsStream& operator << (uint64_t x);
			OctetsStream& operator << (float x);
			OctetsStream& operator << (double x);
			OctetsStream& operator << (const Marshal &x);
			OctetsStream& operator << (const Octets &x);

			template<typename T>
			OctetsStream& operator << (const std::basic_string<T> &x)
			{
				STATIC_ASSERT(sizeof(T) == 1);

				unsigned int bytes = x.length()*sizeof(T); // count of bytes
				compact_uint32(bytes);
				insert(end(), (void*)x.c_str(), bytes );
				return *this;
			}

			template<typename T1, typename T2>
			OctetsStream& operator << (const std::pair<T1, T2> &x) { return *this << x.first << x.second; }

			template<typename T>
			OctetsStream& operator << (const std::vector<T> &x) { return *this <<( MarshalContainer(x)); }

			template<typename T>
			OctetsStream& operator << (const std::list<T> &x) { return *this <<( MarshalContainer(x)); }

			template<typename T>
			OctetsStream& operator << (const std::deque<T> &x) { return *this <<( MarshalContainer(x)); }

			template<typename T1, typename T2>
			OctetsStream& operator << (const std::map<T1, T2> &x) { return *this <<( MarshalContainer(x)); }

			bool eos() const;

			const OctetsStream& operator >> (Marshal::Transaction trans) const;
			const OctetsStream& operator >> (const char &x) const;
			const OctetsStream& operator >> (const unsigned char &x) const;
			const OctetsStream& operator >> (const bool &x) const;
			const OctetsStream& operator >> (const short &x) const;
			const OctetsStream& operator >> (const unsigned short &x) const;
			const OctetsStream& operator >> (const int32_t &x) const;
			const OctetsStream& operator >> (const uint32_t &x) const;
			const OctetsStream& operator >> (const int64_t &x) const;
			const OctetsStream& operator >> (const uint64_t &x) const;
			const OctetsStream& operator >> (const float &x) const;
			const OctetsStream& operator >> (const double &x) const;
			const OctetsStream& operator >> (const Marshal &x) const;
			const OctetsStream& operator >> (const Octets &x) const;

			template<typename T>
			const OctetsStream& operator >> (const std::basic_string<T> &x) const
			{
				STATIC_ASSERT(sizeof(T) == 1);

				uint32_t bytes;
				uncompact_uint32(bytes);
				if (bytes % sizeof(T))
					throw Marshal::Exception("invalid length");
				if (bytes > data.size() - pos)
					throw Marshal::Exception();
				remove_const(x).assign((T*)((char*)data.begin()+pos), bytes/sizeof(T));
				pos += bytes;
				return *this;
			}

			void pop_byte(char *x, size_t len) const;

			template<typename T1, typename T2>
			const OctetsStream& operator >> (const std::pair<T1, T2> &x) const { return *this >> remove_const(x.first) >> remove_const(x.second); }

			template<typename T>
			const OctetsStream& operator >> (const std::vector<T> &x) const { return *this >>( MarshalContainer(x)); }

			template<typename T>
			const OctetsStream& operator >> (const std::deque<T> &x) const { return *this >>( MarshalContainer(x)); }

			template<typename T>
			const OctetsStream& operator >> (const std::list<T> &x) const { return *this >>( MarshalContainer(x)); }

			template<typename T1, typename T2>
			const OctetsStream& operator >> (const std::map<T1, T2> &x) const { return *this >>( MarshalContainer(x)); }
                
            //////////////////////////////////////////////////////////////////
            //封装接口，注册进入lua
            char unmarshal_char();
            unsigned char unmarshal_uchar();
            short unmarshal_short();
            bool unmarshal_bool();
            int32_t unmarshal_int32();
            uint32_t unmarshal_uint32();
            int64_t unmarshal_int64();
            uint64_t unmarshal_uint64();
            float unmarshal_float();
            void unmarshal_wstring(std::wstring& x);
            void unmarshal_octets(FireNet::Octets& x);
                
            
            void marshal_char(char x);
            void marshal_uchar(unsigned char x);
            void marshal_short(short x);
            void marshal_bool(bool x);
            void marshal_int32(int32_t x);
            void marshal_uint32(uint32_t x);
            void marshal_int64(int64_t x);
            void marshal_uint64(uint64_t x);
            void marshal_float(float x);
            void marshal_octets(FireNet::Octets& x);
            void marshal_wstring(std::wstring& x);
                
            Octets getdata() {return data;}
		};

		virtual OctetsStream& marshal(OctetsStream &) const = 0;
		virtual const OctetsStream& unmarshal(const OctetsStream &) = 0;
		virtual ~Marshal() { }
	};

	class CompactUINT : public Marshal
	{
		uint32_t *pi;
	public:
		explicit CompactUINT(const uint32_t &i): pi(&remove_const(i)) { }

		OctetsStream& marshal(OctetsStream &os) const { return os.compact_uint32(*pi); }
		const OctetsStream& unmarshal(const OctetsStream &os) { return os.uncompact_uint32(*pi); }
	};

	class CompactSINT : public Marshal
	{
		int32_t *pi;
	public:
		explicit CompactSINT(const int32_t &i): pi(&remove_const(i)) { }

		OctetsStream& marshal(OctetsStream &os) const { return os.compact_sint32(*pi); }
		const OctetsStream& unmarshal(const OctetsStream &os) { return os.uncompact_sint32(*pi); }
	};

	template<typename Container>
	class STLContainer : public Marshal
	{
		Container *pc;
	public:
		explicit STLContainer(Container &c) : pc(&c) { }
		OctetsStream& marshal(OctetsStream &os) const
		{
			os << CompactUINT(pc->size());
			for (typename Container::const_iterator i = pc->begin(), e = pc->end(); i != e; ++i)
				os << *i;
			return os;
		}
		const OctetsStream& unmarshal(const OctetsStream &os)
		{
			pc->clear();
			uint32_t size;
			for (os >> CompactUINT(size); size > 0; --size)
			{
				typename Container::value_type tmp;
				os >> tmp;
				pc->insert(pc->end(), tmp);
			}
			return os;
		}
	};

	template<typename Container>
	inline STLContainer<Container> MarshalContainer(const Container &c)
	{
		return STLContainer<Container> (remove_const(c));
	}

} // namespace FireNet {

#endif