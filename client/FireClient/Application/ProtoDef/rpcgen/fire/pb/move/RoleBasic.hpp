
#pragma once

#include "fire/pb/move/Pos.hpp"
#include <list>

namespace fire { 
namespace pb { 
namespace move { 

class RoleBasic : public FireNet::Marshal
{
public:
	FireNet::Octets rolebasicoctets; // 角色数据
	fire::pb::move::Pos pos; // 当前坐标
	char posz; // 当前z坐标
	std::list<fire::pb::move::Pos> poses; // 个数不同代表的含义不同。1：走路的目的坐标；2：一段跳起始坐标；4：二段跳起始坐标*2

	RoleBasic() {
		posz = 0;
	}

	RoleBasic(const FireNet::Octets & _rolebasicoctets_, const fire::pb::move::Pos & _pos_, char _posz_, const std::list<fire::pb::move::Pos> & _poses_)
		: rolebasicoctets(_rolebasicoctets_), pos(_pos_), posz(_posz_), poses(_poses_) {
	}

	bool _validator_() const {
		if (!pos._validator_()) return false;
		for (std::list<fire::pb::move::Pos>::const_iterator _i_ = poses.begin(); _i_ != poses.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << rolebasicoctets;
		_os_ << pos;
		_os_ << posz;
		_os_ << FireNet::MarshalContainer(poses);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> rolebasicoctets;
		_os_ >> pos;
		_os_ >> posz;
		_os_ >> FireNet::MarshalContainer(poses);
		return _os_;
	}

};

} 
} 
} 
