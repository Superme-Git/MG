#pragma once

namespace Nuclear
{
	template<class T>
	class CRecyclingArray
	{
		template<class T>
		struct OBJ
		{
			T obj;
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
		//		OutputDebugString(L"CParticleArray::New: too many objects!\n");
				return NULL;
			}

			int i = m_nLastNew;
			for(int q=0; q < m_nMaxElements; q++) 
			{
				if( !(m_vObjs[i].m_bAlive) )
				{//�ҵ�
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
			if(m_nLastNew == m_nMaxElements) //9-18��Խ���..555555
				m_nLastNew = 0;

			//i���ϴη��صģ��϶�����free����...
			//�߼��Ͽ��Ա�֤���ӵ�һ�����ʱ..��һ�ξ��ҵ���λ

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
