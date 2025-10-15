#include "stdafx.h"
#include "pslgroupobject.h"

namespace Nuclear
{
	pslgroupobject::pslgroupobject(void)
	{
	}

	pslgroupobject::~pslgroupobject(void)
	{
	}

	XOStream& pslgroupobject::marshal(XOStream &os) const
	{//save
		os << (int)CURVER;

		unsigned long count = (unsigned long)m_vectorPSLs.size();
		os << count;

		std::vector<PSLGROUP>::const_iterator it = m_vectorPSLs.begin();
		unsigned int nameSize = 0;
		for( ; it != m_vectorPSLs.end(); ++it)
		{
			nameSize = static_cast<unsigned int>(it->m_strPslName.size() * sizeof(wchar_t));
			os<<nameSize;
			os.push_byte(reinterpret_cast<const char*>(it->m_strPslName.data()), nameSize);

			os<<it->m_PSLpos;
		}

		return os;
	}

	const XIStream& pslgroupobject::unmarshal(const XIStream &os)
	{//load
		int nVer;
		os >> nVer;

		unsigned long count = 0;
		os >> count;

		unsigned int str_size = 0;
		wchar_t *str = NULL;

		for(unsigned long i=0; i<count; i++)
		{
			PSLGROUP pslg;
			os >> str_size;
			str = reinterpret_cast<wchar_t*>(XAlloc(str_size+2));
			ZeroMemory(str, str_size+2);
			os.pop_byte(reinterpret_cast<char*>(str), str_size);
			pslg.m_strPslName = str;
			XFree(str);

			os>>pslg.m_PSLpos;
			m_vectorPSLs.push_back(pslg);
		}

		return os;
	}

}