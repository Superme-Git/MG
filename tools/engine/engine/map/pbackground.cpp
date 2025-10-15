#include "stdafx.h"
#include "pmap.h"

namespace Nuclear
{
	namespace SubMap
	{
		PBackGround::PBackGround(void) : m_backGroundInfoCount(0)
		{
		}

		PBackGround::~PBackGround(void)
		{
			ClearAll();
		}

		void PBackGround::ClearAll()
		{
			PBackGoundInfoVector::iterator iter, iterEnd;
			iter = m_backGroundInfos.begin();
			iterEnd = m_backGroundInfos.end();
			for (;iter!=iterEnd;++iter)
			{
				if (iter->pPics)
				{
					XFree(iter->pPics);
				}
			}
			m_backGroundInfos.clear();
		}

		PBackGround& PBackGround::operator= (const PBackGround &x)
		{
			ClearAll();
			PBackGoundInfoVector::const_iterator iter, iterEnd;
			m_backGroundInfos.reserve(x.m_backGroundInfos.size());
			iter = x.m_backGroundInfos.begin();
			iterEnd = x.m_backGroundInfos.end();
			int buffersize, i;
			for (i=0;iter!=iterEnd;++iter,++i)
			{
				m_backGroundInfos.push_back(*iter);
				buffersize = iter->width * iter->height * sizeof(ObjMapKey);
				m_backGroundInfos[i].pPics = static_cast<ObjMapKey*>(Nuclear::XAlloc(buffersize));
				memcpy(m_backGroundInfos[i].pPics, iter->pPics, buffersize);
			}
			m_backGroundInfoCount = x.m_backGroundInfoCount;
			return *this;
		}

		XOStream& PBackGround::marshal(XOStream &os) const
		{
			os << m_backGroundInfoCount;
			PBackGoundInfoVector::const_iterator iter, iterEnd;
			iter = m_backGroundInfos.begin();
			iterEnd = m_backGroundInfos.end();
			int i = 0;
			for (;iter!=iterEnd;++iter)
			{
				if (iter->m_bDeleted)
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
			if (i != m_backGroundInfoCount)
			{
				throw XMarshal::Exception();
			}
			return os;
		}

		const XIStream& PBackGround::unmarshal(const XIStream &os, unsigned int version)
		{
			PBackGoundInfo tmpInfo;
			int i, bufferSize;
			os >> m_backGroundInfoCount;
			for (i=0;i<m_backGroundInfoCount;++i)
			{
				m_backGroundInfos.push_back(tmpInfo);
				PBackGoundInfo &info = m_backGroundInfos[i];
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