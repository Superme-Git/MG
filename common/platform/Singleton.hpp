#ifndef __SINGLETON_HPP
#define __SINGLETON_HPP

#include "platform/platform_types.h"

template<class T>
class CSingleton
{
private:
	static T* m_pInstance;
public:
	inline static T* NewInstance(void)
	{
		if (m_pInstance == NULL)
		{
			m_pInstance = new T();
		}
		return m_pInstance;
	}
	inline static void RemoveInstance(void)
	{
		if (m_pInstance == NULL)
			return;
		delete m_pInstance;
		m_pInstance = NULL;
	}
	inline static T* GetInstance()
	{
		/*if(m_pInstance == NULL)
			m_pInstance = new T();*/
		return m_pInstance;
	}
protected:
	CSingleton(){}
	virtual ~CSingleton(){}
};

template<class T>
T* CSingleton<T>::m_pInstance = NULL;

#endif
