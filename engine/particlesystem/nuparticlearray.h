#ifndef __Nuclear_particlearray_H__
#define __Nuclear_particlearray_H__

namespace Nuclear
{
	template<typename T>
	class CRecyclingArray
	{
		template<typename T2>
		struct OBJ
		{
			T2 obj;
			bool m_bAlive;
			OBJ() : m_bAlive(false) { }
			
		};

		int m_nMaxElements;
		int m_nUsedElements;
		int m_nLastNew;
		std::vector<OBJ<T> > m_vObjs;

	public:

		CRecyclingArray(int count)
		{
			m_nMaxElements = count;
			m_nUsedElements = 0;
			m_nLastNew = 0;
			m_vObjs.resize(count);
		}

		void Resize(int count)
		{
			DeleteAll();
			m_nMaxElements = count;
			m_nUsedElements = 0;
			m_nLastNew = 0;
			m_vObjs.resize(count);
		}

		T* New()
		{
			if(GetNumFreeElements() < 1)
			{
				return NULL;
			}

			int i = m_nLastNew;
			for(int q=0; q < m_nMaxElements; q++) 
			{
				if( !(m_vObjs[i].m_bAlive) )
				{//找到
					break;
				}
				else
				{
					i++;
					if(i >= m_nMaxElements)
						i = 0;
				}
			}

			m_nUsedElements++;
			m_vObjs[i].m_bAlive = true;
			m_nLastNew = i+1;
			if(m_nLastNew == m_nMaxElements) //9-18会越界的..555555
				m_nLastNew = 0;

			//i是上次返回的，肯定不是free的了...,逻辑上可以保证粒子第一遍产生时..能一次就找到空位
			return &(m_vObjs[i].obj);
		}

		bool Delete(int index)
		{
			if( index < 0 || index >= m_nMaxElements || !(m_vObjs[index].m_bAlive) ) 
				return false;
			m_vObjs[index].m_bAlive = false;
			m_nUsedElements--;
			return true;
		}

		int GetNumFreeElements(void) const { return( m_nMaxElements-GetNumUsedElements() ); }
		int GetNumUsedElements(void) const { return m_nUsedElements; }

		int GetTotalElements(void) const { return m_nMaxElements; }

		T* GetAt(int index)
		{
			if(index<0 || index>=m_nMaxElements)
			{
				return NULL;
			}
			return &(m_vObjs[index].obj);
		}

		const T* GetAt(int index) const
		{
			if(index<0 || index>=m_nMaxElements)
			{
				return NULL;
			}
			return &(m_vObjs[index].obj);
		}

		bool IsAlive(int index) const { return m_vObjs[index].m_bAlive; }

		void DeleteAll(void)
		{
			for(int q=0; q<m_nMaxElements; q++)
			{
				m_vObjs[q].m_bAlive = false;
			}
			m_nUsedElements = 0;
			m_nLastNew = 0;
		}
	};
}

#endif