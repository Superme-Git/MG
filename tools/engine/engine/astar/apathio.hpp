
#pragma once

#include "apath.hpp"

#ifdef WIN32
#include "win32/share.hpp" // authc
#else
#include "common/marshal.h"
#endif

namespace astar
{

inline GNET::Marshal::OctetsStream & operator<<(GNET::Marshal::OctetsStream & os, const Direction & dir)
{
	return os << dir.x << dir.y << dir.z;
}

inline const GNET::Marshal::OctetsStream & operator>>(const GNET::Marshal::OctetsStream & os, Direction & dir)
{
	return os >> dir.x >> dir.y >> dir.z;
}

inline GNET::Marshal::OctetsStream & operator<<(GNET::Marshal::OctetsStream & os, const Point & point)
{
	return os << point.x << point.y << point.z << point.direction;
}

inline const GNET::Marshal::OctetsStream & operator>>(const GNET::Marshal::OctetsStream & os, Point & point)
{
	return os >> point.x >> point.y >> point.z >> point.direction;
}

inline void marshal(const Path & path, GNET::Octets & octets /* out */)
{
	GNET::Marshal::OctetsStream os;
	os << GNET::STLContainer<Path> (const_cast<Path&>(path));
	octets.swap((GNET::Octets&)os); // operator Octets&
}

inline void unmarshal(const GNET::Octets & octets, Path & path /* out*/)
{
	Path tmp;
	GNET::Marshal::OctetsStream os(octets);
	os >> GNET::STLContainer<Path> (tmp);
	path.swap(tmp);
}

} // namespace astar
