#include "nupmap.h"

namespace Nuclear
{
	namespace SubMap
	{
		NuclearBackGround::NuclearBackGround(void) : m_BackGroundInfoCount(0)
		{
		}

		NuclearBackGround::~NuclearBackGround(void)
		{
			clearAll();
		}

		void NuclearBackGround::clearAll()
		{
			NuclearBackGoundInfoVector::iterator iter, iterEnd;
			iter = m_BackGroundInfos.begin();
			iterEnd = m_BackGroundInfos.end();
			for (;iter!=iterEnd;++iter)
			{
				if (iter->pPics)
				{
					XFree(iter->pPics);
				}
			}
			m_BackGroundInfos.clear();
		}

		NuclearBackGround& NuclearBackGround::operator= (const NuclearBackGround &x)
		{
			clearAll();
			NuclearBackGoundInfoVector::const_iterator iter, iterEnd;
			m_BackGroundInfos.reserve(x.m_BackGroundInfos.size());
			iter = x.m_BackGroundInfos.begin();
			iterEnd = x.m_BackGroundInfos.end();
			int buffersize, i;
			for (i=0;iter!=iterEnd;++iter,++i)
			{
				m_BackGroundInfos.push_back(*iter);
				buffersize = iter->width * iter->height * sizeof(ObjMapKey);
				m_BackGroundInfos[i].pPics = static_cast<ObjMapKey*>(Nuclear::XAlloc(buffersize));
				memcpy(m_BackGroundInfos[i].pPics, iter->pPics, buffersize);
			}
			m_BackGroundInfoCount = x.m_BackGroundInfoCount;
			return *this;
		}

		NuclearOStream& NuclearBackGround::marshal(NuclearOStream &os) const
		{
			os << m_BackGroundInfoCount;
			NuclearBackGoundInfoVector::const_iterator iter, iterEnd;
			iter = m_BackGroundInfos.begin();
			iterEnd = m_BackGroundInfos.end();
			int i = 0;
			for (;iter!=iterEnd;++iter)
			{
				if (iter->deleted)
					continue;
				++i;
				os << iter->name;
				os << iter->pos;
				os << iter->relSpeed;
				os << iter->activeRect;
				os << iter->girdWidth;
				os << iter->girdHeight;
				os << iter->width;
				os << iter->height;
				os << iter->wrapHorz;
				os << iter->wrapVert;
				os.push_byte(iter->pPics, iter->width*iter->height*sizeof(ObjMapKey));
			}
			if (i != m_BackGroundInfoCount)
			{
				throw NuclearMarshal::Exception();
			}
			return os;
		}

		const INuclearStream& NuclearBackGround::unmarshal(const INuclearStream &os, unsigned int version)
		{
			NuclearBackGoundInfo tmpInfo;
			int i, bufferSize;
			os >> m_BackGroundInfoCount;
			for (i=0;i<m_BackGroundInfoCount;++i)
			{
				m_BackGroundInfos.push_back(tmpInfo);
				NuclearBackGoundInfo &info = m_BackGroundInfos[i];
				os >> info.name;
				os >> info.pos;
				os >> info.relSpeed;
				os >> info.activeRect;
				os >> info.girdWidth;
				os >> info.girdHeight;
				os >> info.width;
				os >> info.height;
				os >> info.wrapHorz;
				os >> info.wrapVert;
				bufferSize = info.width*info.height*sizeof(ObjMapKey);
				info.pPics = static_cast<ObjMapKey*>(XAlloc(bufferSize));
				os.pop_byte(info.pPics, bufferSize);
			}
			return os;
		}
	}
}