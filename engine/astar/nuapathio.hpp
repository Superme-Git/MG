#ifndef __Nuclear_apathio_H__
#define __Nuclear_apathio_H__

#include "apath.hpp"

#ifdef WIN32
#include "win32/share.hpp" // FireNet
#else
#include "common/marshal.h"
#endif

namespace astar
{

	inline void marshal(const Path & path, FireNet::Octets & octets /* out */)
	{
		FireNet::Marshal::OctetsStream os;
		os << FireNet::STLContainer<Path> (const_cast<Path&>(path));
		octets.swap((FireNet::Octets&)os); // operator Octets&
	}

	inline void unmarshal(const FireNet::Octets & octets, Path & path /* out*/)
	{
		Path tmp;
		FireNet::Marshal::OctetsStream os(octets);
		os >> FireNet::STLContainer<Path> (tmp);
		path.swap(tmp);
	}

	inline FireNet::Marshal::OctetsStream & operator<<(FireNet::Marshal::OctetsStream & os, const Direction & dir)
	{
		return os << dir.x << dir.y << dir.z;
	}

	inline const FireNet::Marshal::OctetsStream & operator>>(const FireNet::Marshal::OctetsStream & os, Direction & dir)
	{
		return os >> dir.x >> dir.y >> dir.z;
	}

	inline FireNet::Marshal::OctetsStream & operator<<(FireNet::Marshal::OctetsStream & os, const Point & point)
	{
		return os << point.x << point.y << point.z << point.direction;
	}

	inline const FireNet::Marshal::OctetsStream & operator>>(const FireNet::Marshal::OctetsStream & os, Point & point)
	{
		return os >> point.x >> point.y >> point.z >> point.direction;
	}

} // namespace astar

#endif
