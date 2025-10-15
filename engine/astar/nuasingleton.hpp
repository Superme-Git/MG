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
		static T &instance() { static singleton<T> s_oT; return(s_oT); } // 静态初始化时也可适用。
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
			create_object.do_nothing(); // object_creator是静态的，这个调用强迫它在main之前就构造。
			return obj;
		}
	};

	template <typename T>
	typename singleton_default<T>::object_creator singleton_default<T>::create_object;

} // namespace astar

#endif