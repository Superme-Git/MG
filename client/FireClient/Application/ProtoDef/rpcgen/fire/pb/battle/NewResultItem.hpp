
#pragma once

#include "fire/pb/battle/DemoAttr.hpp"
#include "fire/pb/battle/DemoBuff.hpp"
#include "fire/pb/battle/DemoExecute.hpp"
#include "fire/pb/battle/FighterInfo.hpp"
#include "fire/pb/battle/NewDemoResult.hpp"
#include "fire/pb/battle/NewSubResultItem.hpp"
#include <list>
#include <map>

namespace fire { 
namespace pb { 
namespace battle { 

class NewResultItem : public FireNet::Marshal
{
public:
	fire::pb::battle::DemoExecute execute;
	std::list<fire::pb::battle::NewSubResultItem> subresultlist;
	std::list<fire::pb::battle::FighterInfo> newfighter;
	std::map<int, float> rolechangedattrs; // 战斗脚本接收者，此动作后属性的变化（战斗中属性实时变化）
	std::map<int, float> petchangedattrs; // 战斗脚本接收者的宠物，此动作后属性的变化

	NewResultItem() {
	}

	NewResultItem(const fire::pb::battle::DemoExecute & _execute_, const std::list<fire::pb::battle::NewSubResultItem> & _subresultlist_, const std::list<fire::pb::battle::FighterInfo> & _newfighter_, const std::map<int, float> & _rolechangedattrs_, const std::map<int, float> & _petchangedattrs_)
		: execute(_execute_), subresultlist(_subresultlist_), newfighter(_newfighter_), rolechangedattrs(_rolechangedattrs_), petchangedattrs(_petchangedattrs_) {
	}

	bool _validator_() const {
		if (!execute._validator_()) return false;
		for (std::list<fire::pb::battle::NewSubResultItem>::const_iterator _i_ = subresultlist.begin(); _i_ != subresultlist.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		for (std::list<fire::pb::battle::FighterInfo>::const_iterator _i_ = newfighter.begin(); _i_ != newfighter.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << execute;
		_os_ << FireNet::MarshalContainer(subresultlist);
		_os_ << FireNet::MarshalContainer(newfighter);
		_os_ << FireNet::MarshalContainer(rolechangedattrs);
		_os_ << FireNet::MarshalContainer(petchangedattrs);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> execute;
		_os_ >> FireNet::MarshalContainer(subresultlist);
		_os_ >> FireNet::MarshalContainer(newfighter);
		_os_ >> FireNet::MarshalContainer(rolechangedattrs);
		_os_ >> FireNet::MarshalContainer(petchangedattrs);
		return _os_;
	}

};

} 
} 
} 
