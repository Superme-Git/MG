//////////////////////////////////////////////////////////////////////////
//
// Marshal.h
//
// �ɵ�ǰ�����������ֽ���Ĵ�������ؽӿں�ʵ��
//
// author : yanghaibo
// date : 2008-11-17
//
//////////////////////////////////////////////////////////////////////////

#pragma once

#include "pfsutility.h" // for PFS::CDataBlock

namespace PFS
{
namespace MARSHAL
{	

	//
	// �������쳣
	//
	class CMarshalException { };

	// ǰ������
	class CMemoryStream;

	//
	// ���пɱ�CMemoryStream���������Զ��������Ӧ��ʵ�ֵĽӿ�
	//
	class IMarshalable
	{
	public:
		virtual CMemoryStream& marshal(CMemoryStream &os) const = 0;
		virtual CMemoryStream& unmarshal(CMemoryStream &os) = 0;
	};
	
	//
	// ���ڴ����ݽ��д���������
	//
	class CMemoryStream
	{
		CDataBlock  data;
		unsigned int pos;

		template<typename T>
		CMemoryStream& push_byte(T t)
		{
			data.Append(&t, sizeof(t));
			return *this;
		}

		template<typename T>
		CMemoryStream& pop_byte(T &t) 
		{
			if (pos + sizeof(t) > data.GetSize()) 
				throw CMarshalException();

			t = *(T *)( (char*)data.GetData() + pos );
			pos += sizeof(t);
			return *this;
		}

	public:
		//////////////////////////////////////////////////////////////////////////
		//
		// ���캯��
		//
		//////////////////////////////////////////////////////////////////////////

		CMemoryStream() : pos(0) {}
		CMemoryStream(const CDataBlock &o) : data(o), pos(0) {}
		CMemoryStream(const CMemoryStream &os) : data(os.data), pos(0) {}
		const CMemoryStream& operator = (const CMemoryStream &os) 
		{
			if (&os != this)
			{
				pos  = os.pos;
				data = os.data;
			}
			return *this;
		}

	public:
		//////////////////////////////////////////////////////////////////////////
		//
		// �ǳ���ӿ�
		//
		//////////////////////////////////////////////////////////////////////////

		bool operator == (const CMemoryStream &os) const { return data == os.data; }
		bool operator != (const CMemoryStream &os) const { return data != os.data; }

		size_t size() const { return data.GetSize(); }
		void swap (CMemoryStream &os) { data.Swap(os.data); unsigned int _pos = pos; pos = os.pos; os.pos = _pos; }
		operator CDataBlock& () { return data; }
		void reserve(size_t size) { data.Reserve(size); }
		void clear() { data.Clear(); pos = 0; }
		bool eos() const { return pos == data.GetSize(); }		

	public:
		//////////////////////////////////////////////////////////////////////////
		//
		// д�����ӿ�
		//
		//////////////////////////////////////////////////////////////////////////
		CMemoryStream& operator << (char x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (unsigned char x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (bool x) 
		{
			return push_byte((unsigned char)x);
		}

		CMemoryStream& operator << (short x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (unsigned short x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (int x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (unsigned int x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (long x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (unsigned long x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (long long x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (unsigned long long x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (float x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (double x) 
		{
			return push_byte(x);
		}

		CMemoryStream& operator << (const IMarshalable &x)
		{ 
			return x.marshal(*this);
		}

		CMemoryStream& push_byte(const char *x, size_t len)
		{
			data.Append(x, len);
			return *this;
		}

	public:
		//////////////////////////////////////////////////////////////////////////
		//
		// �������ӿ�
		//
		//////////////////////////////////////////////////////////////////////////

		CMemoryStream& operator >> (char &x)
		{
			return pop_byte(x);
		}
		
		CMemoryStream& operator >> (unsigned char &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (bool &x) 
		{
			unsigned char _x;
			pop_byte(_x);
			x = !!_x;
			return *this;
		}

		CMemoryStream& operator >> (short &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (unsigned short &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (int &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (unsigned int &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (long &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (unsigned long &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (long long &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (unsigned long long &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (float &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (double &x)
		{
			return pop_byte(x);
		}

		CMemoryStream& operator >> (IMarshalable &x)
		{
			return x.unmarshal(*this);
		}
		
		CMemoryStream& pop_byte(char *x, size_t len)
		{
			if (pos + len > data.GetSize())
			{
				throw CMarshalException();
			}

			memcpy(x, (char*)data.GetData()+pos, len);
			pos += (unsigned int)len;
			return *this;
		}

	}; // CMemoryStream
	
};// namespace MARSHAL

}; // namespace PFS