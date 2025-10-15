
#pragma once

#include "fire/pb/battle/DemoAttr.hpp"
#include "fire/pb/battle/DemoBuff.hpp"
#include "fire/pb/battle/NewDemoResult.hpp"
#include <list>
#include <map>

namespace fire { 
namespace pb { 
namespace battle { 

class NewSubResultItem : public FireNet::Marshal
{
public:
	int subskillid;
	int subskillstarttime;
	std::list<fire::pb::battle::NewDemoResult> resultlist;

	NewSubResultItem() {
		subskillid = 0;
		subskillstarttime = 0;
	}

	NewSubResultItem(int _subskillid_, int _subskillstarttime_, const std::list<fire::pb::battle::NewDemoResult> & _resultlist_)
		: subskillid(_subskillid_), subskillstarttime(_subskillstarttime_), resultlist(_resultlist_) {
	}

	bool _validator_() const {
		for (std::list<fire::pb::battle::NewDemoResult>::const_iterator _i_ = resultlist.begin(); _i_ != resultlist.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << subskillid;
		_os_ << subskillstarttime;
		_os_ << FireNet::MarshalContainer(resultlist);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> subskillid;
		_os_ >> subskillstarttime;
		_os_ >> FireNet::MarshalContainer(resultlist);
		return _os_;
	}

};

} 
} 
} 
