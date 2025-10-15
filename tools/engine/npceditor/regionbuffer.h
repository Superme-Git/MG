#pragma  once


//#include "../engine/engine/common/pobject.h"
//#include "../engine/engine/common/xmarshal.h"

template<class T>
class CRegionBuffer : public Nuclear::PObject
{
private:
	CRegionBuffer(const CRegionBuffer&){}
	CRegionBuffer& operator=(const CRegionBuffer&){return *this;}

public:

	CRegionBuffer():m_Width(0),m_Height(0){}
	
	void Init(const unsigned int& w, const unsigned int& h)
	{
		assert(w <= 65535 || h <= 65535);
		m_Width = w;
		m_Height = h;
		m_buffer.clear();
	}

	bool IsEmpty()
	{
		return m_buffer.empty();
	}

	void AddPoint(const unsigned int& w, const unsigned int& h,const T& regiontype)
	{
		assert(w <= 65535 || h <= 65535);
		unsigned int key = w << 16 | h;

		if (m_buffer.count(key) > 0)
		{
			T val = m_buffer[key];
			
			val |= regiontype;
		
			m_buffer[key] = val;
		}
		else{
			m_buffer[key] |= regiontype;
		}	     
	}

	void DelPoint(const unsigned int& w, const unsigned int& h,const T& regiontype)
	{
		assert(w <= 65535 || h <= 65535);
		unsigned int key = w << 16 | h;
		
		if (m_buffer.count(key) > 0)
		{
			T val = m_buffer[key];
			
			val &= ~regiontype;
			
			m_buffer[key] = val;
		}
	}

	bool CheckPointType(const unsigned int& w, const unsigned int& h,const T& regiontype)
	{
		assert(w <= 65535 || h <= 65535);
		unsigned int key = w << 16 | h;

		if (m_buffer.count(key) == 0)
			return false;
		
		T val = m_buffer[key];
		return ((val & regiontype) > 0);
		
		
	}

	T GetPointValue(const unsigned int& w, const unsigned int& h)
	{
		assert(w <= 65535 || h <= 65535);
		
		unsigned int key = w << 16 | h;

		if (m_buffer.count(key) == 0)
			return 0;

		T val = m_buffer[key];
		return val;
	}

	virtual Nuclear::XOStream& marshal(Nuclear::XOStream & os) const
	{
		const char fileHead[4] =  {'Q','U','Y','U'};
		os.push_byte(&fileHead,sizeof(char)*4);
		
		os << m_Width;
		os << m_Height;
		os << (int)m_buffer.size();
		std::map<unsigned int,T>::const_iterator it		= m_buffer.begin();
		std::map<unsigned int,T>::const_iterator itend	= m_buffer.end();
		
		while(it != itend)
		{
			
			os << it->first<< it->second;
			++it;
		}

		return os;
	}
	virtual const Nuclear::XIStream& unmarshal(const Nuclear::XIStream & os)
	{
		char fileHead[4];
		os.pop_byte(fileHead, sizeof(char) * 4);
		
		if (fileHead[0] != 'Q' || fileHead[1] != 'U' || fileHead[2] != 'Y' || fileHead[3] != 'U' )
		{
			throw Exception();
		}
		
		m_buffer.clear();
		
		os >> m_Width;
		os >> m_Height;

		size_t bsize = m_buffer.size();

		os >> bsize;

		for (size_t i = 0; i < bsize; ++i)
		{
			unsigned int key;
			T val;
			
			os >> key;
			os >> val;

			m_buffer[key] = val;
		}
		return os;
	}

private:
	unsigned int  m_Width;
	unsigned int m_Height;
	std::map<unsigned int,T> m_buffer;//key�Ƿ��ϵģ���16λ����16λ�㡣value ��RegionType��λ���
};