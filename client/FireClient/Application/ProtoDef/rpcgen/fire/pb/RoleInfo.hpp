
#pragma once

#include <map>

namespace fire { 
namespace pb { 

class RoleInfo : public FireNet::Marshal
{
public:
	int64_t roleid; // ID
	std::wstring rolename; // 名称
	int school; // 人物职业
	int shape; // 角色id
	int level; // 等级
	std::map<char, int> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装
	int64_t rolecreatetime; // 角色创建时间

	RoleInfo() {
		roleid = 0;
		school = 0;
		shape = 0;
		level = 0;
		rolecreatetime = 0;
	}

	RoleInfo(int64_t _roleid_, const std::wstring & _rolename_, int _school_, int _shape_, int _level_, const std::map<char, int> & _components_, int64_t _rolecreatetime_)
		: roleid(_roleid_), rolename(_rolename_), school(_school_), shape(_shape_), level(_level_), components(_components_), rolecreatetime(_rolecreatetime_) {
	}

	bool _validator_() const {
		if (roleid < 1) return false;
		if (level < 1) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << roleid;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(rolename.length() * 2);
		for(size_t pos =0; pos < rolename.length(); pos++) {
		utf16 ch = (utf16)rolename[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << school;
		_os_ << shape;
		_os_ << level;
		_os_ << FireNet::MarshalContainer(components);
		_os_ << rolecreatetime;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		rolename.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		rolename.insert(rolename.end(), (wchar_t)*ch);
		}
		}
		_os_ >> school;
		_os_ >> shape;
		_os_ >> level;
		_os_ >> FireNet::MarshalContainer(components);
		_os_ >> rolecreatetime;
		return _os_;
	}

};

} 
} 
