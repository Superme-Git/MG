#pragma  once

#include "../../../engine/common/nupobject.h"
#include "../../../engine/common/nuxmarshal.h"

template<class T>
class RegionBuffer : public Nuclear::NuclearObject
{
private:
	RegionBuffer(const RegionBuffer&){}
	RegionBuffer& operator=(const RegionBuffer&){return *this;}
private:
	unsigned int  m_Width;
	unsigned int m_Height;
	std::map<unsigned int,T> m_buffer;//key是符合的，高16位宽，低16位搞。value 是eRegionType的位组合
    
public:
	RegionBuffer():m_Width(0),m_Height(0){}
	
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
			m_buffer[key] = regiontype;		
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

	virtual Nuclear::NuclearOStream& marshal(Nuclear::NuclearOStream & os) const
	{
		const char fileHead[4] = {'Q','U','Y','U'};
		os.push_byte(fileHead, sizeof(char) * 4);
		os << m_Width;
		os << m_Height;
		os << (int)m_buffer.size();
		typename std::map<unsigned int,T>::const_iterator it = m_buffer.begin();
		typename std::map<unsigned int,T>::const_iterator itend = m_buffer.end();
		while(it != itend)
		{
			os << it->first << it->second;
			++it;
		}

		return os;
	}
	virtual const Nuclear::INuclearStream& unmarshal(const Nuclear::INuclearStream & os)
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
};
