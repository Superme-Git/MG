#ifndef __Nuclear_KEYFRAME_H_
#define __Nuclear_KEYFRAME_H_

#include "../common/nuxpmaths.h"
#include <vector>
using namespace std;
namespace Nuclear
{

	template<typename VALUE_T, typename INTER_FN>
	class CKeyValue
	{
	public:
		typedef VALUE_T value_t;

	private:
		float		m_frame;
		VALUE_T		m_value;

	public:
		CKeyValue()
			: m_frame( 0.0f)
		{}

		CKeyValue( float frame, const VALUE_T& value)
			: m_frame( frame), m_value( value)
		{}

		float	GetFrame() const 
		{
			return m_frame;
		}

		void	SetFrame( float frame)
		{
			m_frame = frame;
		}

		const VALUE_T&	GetValue() const
		{
			return m_value;
		}

		void SetValue( const VALUE_T& value)
		{
			m_value = value;
		}

		VALUE_T	Interpolation( float frame, float framePitch, const CKeyValue* nextKey)
		{
			if ( nextKey != NULL)
			{
				return INTER_FN()( m_value, nextKey->GetValue(), (frame - m_frame) / framePitch);
			} 
			else
			{
				return m_value;
			}
		}
	};

	template<typename VALUE_T, typename INTER_FN>
	bool operator<( const CKeyValue<VALUE_T, INTER_FN>& lts, const CKeyValue< VALUE_T, INTER_FN>& rhs )
	{
		return lts.GetFrame() < rhs.GetFrame();
	}

	template<typename VALUE_T, typename INTER_FN>
	bool operator<( const CKeyValue<VALUE_T, INTER_FN>& lts, float frame )
	{
		return lts.GetFrame() < frame;
	}

	template<typename T>
	struct LinerInter
	{
		T operator()( const T& v1, const T& v2, float s)
		{
			return Lerpf( v1, v2, s);
		}
	};

	typedef LinerInter<float>	FloatLinerInter;
	typedef LinerInter<NuclearFPoint> Vec2LinerInter;
	typedef LinerInter<NuclearVector4> Vec4LinerInter;

	typedef CKeyValue<NuclearFPoint, Vec2LinerInter>		CPosKeyValue;		//位置
	typedef CKeyValue<NuclearFPoint, Vec2LinerInter>		CScaleKeyValue;		//缩放
	typedef CKeyValue<float, FloatLinerInter>		CRotationKeyValue;	//旋转
	typedef CKeyValue<float, FloatLinerInter>		CAlphaKeyValue;		//Alpha透明度
	typedef CKeyValue<float, FloatLinerInter>		CDivideKeyValue;	//人物层次
	typedef CKeyValue<NuclearVector4, Vec4LinerInter>	CColorKeyValue;		//RGB颜色（不包括alpha）

   	template< typename T>
	class CKeyTable
	{
	public:
		typedef std::vector< T >	KEYS;
        typedef typename std::vector<T>::iterator Iterator;

	private:
		KEYS	m_keys;

	public:
		virtual bool operator<<( void*) { return false;}
		virtual bool operator>>( void*) { return false;}

	public:
		unsigned int	GetKeyCount() const
		{
			return (unsigned int)m_keys.size();
		}

		const T* GetKey( unsigned int pos) const
		{
			if ( pos >= m_keys.size())
				return NULL;
			return &(m_keys[pos]);
		}

		T* GetKey( unsigned int pos)
		{
			if ( pos >= m_keys.size())
				return NULL;
			return &(m_keys[pos]);
		}

		unsigned int	Insert( const T& key)
		{
			// 找出第一个大于值的位置
			Iterator it = std::upper_bound( m_keys.begin(), m_keys.end(), key);
			it = m_keys.insert( it, key);
			return (unsigned int)(it - m_keys.begin());
		}

		bool	Remove( unsigned int pos)
		{
			if ( pos >= m_keys.size())
				return false;

			m_keys.erase( m_keys.begin() + pos);
			return true;
		}

		void	Clear( )
		{
			m_keys.clear();
		}

		bool	Empty( ) const
		{
			return m_keys.empty();
		}

	public:
		typename T::value_t Interpolation( unsigned int keyPos, float frame)
		{
			assert( !m_keys.empty());
			typename KEYS::size_type size = m_keys.size();
			if ( size == 1			// 为1作的优化
				|| keyPos >= size)			
			{
				T& back = m_keys.back();
				return back.Interpolation( frame, frame, NULL);
			}
			else if ( size == 0)	// 为0作的容错
				return typename T::value_t();

			T* base = &(m_keys[keyPos]);
			if ( keyPos == 0 && frame <= base->GetFrame())		// 为起始关键不在0位置上作的容错性
				return base->Interpolation( frame, frame, NULL);

			for ( unsigned int i = keyPos + 1; i < size; ++i)
			{
				T* next = &(m_keys[i]);
				if ( frame < next->GetFrame())
				{
					return base->Interpolation( frame, next->GetFrame() - base->GetFrame(), next);
				}

				base = next;
			}

			return base->Interpolation( frame, frame, NULL);

		}
	};






}	// namesapce Nuclear

#endif	//__Nuclear_KEYFRAME_H_
