#ifndef __Nuclear_asingleton_H__
#define __Nuclear_asingleton_H__

namespace astar
{

	template <class T>
	class singleton : private T
	{
		singleton()  { }
		~singleton() { }
	public:
		static T &instance() { static singleton<T> s_oT; return(s_oT); } // ��̬��ʼ��ʱҲ�����á�
	};

	template <typename T>
	class singleton_default
	{
		struct object_creator
		{
			object_creator() { singleton_default<T>::instance(); }
			inline void do_nothing() const { }
		};
		static object_creator create_object;
		singleton_default();
	public:
		typedef T object_type;
		static object_type & instance()
		{
			static object_type obj;
			create_object.do_nothing(); // object_creator�Ǿ�̬�ģ��������ǿ������main֮ǰ�͹��졣
			return obj;
		}
	};

	template <typename T>
	typename singleton_default<T>::object_creator singleton_default<T>::create_object;

} // namespace astar

#endif