
#pragma once

#include "fire/pb/circletask/RewardItemUnit.hpp"
#include <vector>

namespace fire { 
namespace pb { 
namespace circletask { 

class ActiveQuestData : public FireNet::Marshal
{
public:
	int questid; // 任务id
	int queststate; // 任务状态
	int64_t dstnpckey; // 目的npckey
	int dstnpcid; // 目的npcbaseid
	int dstmapid; // 目的地图id
	int dstx; // 目的x坐标
	int dsty; // 目的y坐标
	int dstitemid; // 目的道具的id"
	int sumnum; // 累积次数.
	std::wstring npcname; // npc名字,目前仅用于强盗宝图
	int64_t rewardexp; // 奖励的经验
	int64_t rewardmoney; // 奖励的现金
	int64_t rewardsmoney; // 奖励的储备金
	std::vector<fire::pb::circletask::RewardItemUnit> rewarditems; // 奖励的道具

	ActiveQuestData() {
		questid = 0;
		queststate = 0;
		dstnpckey = 0;
		dstnpcid = 0;
		dstmapid = 0;
		dstx = 0;
		dsty = 0;
		dstitemid = 0;
		sumnum = 0;
		rewardexp = 0;
		rewardmoney = 0;
		rewardsmoney = 0;
	}

	ActiveQuestData(int _questid_, int _queststate_, int64_t _dstnpckey_, int _dstnpcid_, int _dstmapid_, int _dstx_, int _dsty_, int _dstitemid_, int _sumnum_, const std::wstring & _npcname_, int64_t _rewardexp_, int64_t _rewardmoney_, int64_t _rewardsmoney_, const std::vector<fire::pb::circletask::RewardItemUnit> & _rewarditems_)
		: questid(_questid_), queststate(_queststate_), dstnpckey(_dstnpckey_), dstnpcid(_dstnpcid_), dstmapid(_dstmapid_), dstx(_dstx_), dsty(_dsty_), dstitemid(_dstitemid_), sumnum(_sumnum_), npcname(_npcname_), rewardexp(_rewardexp_), rewardmoney(_rewardmoney_), rewardsmoney(_rewardsmoney_), rewarditems(_rewarditems_) {
	}

	bool _validator_() const {
		if (questid < 1) return false;
		for (std::vector<fire::pb::circletask::RewardItemUnit>::const_iterator _i_ = rewarditems.begin(); _i_ != rewarditems.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << questid;
		_os_ << queststate;
		_os_ << dstnpckey;
		_os_ << dstnpcid;
		_os_ << dstmapid;
		_os_ << dstx;
		_os_ << dsty;
		_os_ << dstitemid;
		_os_ << sumnum;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(npcname.length() * 2);
		for(size_t pos =0; pos < npcname.length(); pos++) {
		utf16 ch = (utf16)npcname[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << rewardexp;
		_os_ << rewardmoney;
		_os_ << rewardsmoney;
		_os_ << FireNet::MarshalContainer(rewarditems);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> questid;
		_os_ >> queststate;
		_os_ >> dstnpckey;
		_os_ >> dstnpcid;
		_os_ >> dstmapid;
		_os_ >> dstx;
		_os_ >> dsty;
		_os_ >> dstitemid;
		_os_ >> sumnum;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		npcname.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		npcname.insert(npcname.end(), (wchar_t)*ch);
		}
		}
		_os_ >> rewardexp;
		_os_ >> rewardmoney;
		_os_ >> rewardsmoney;
		_os_ >> FireNet::MarshalContainer(rewarditems);
		return _os_;
	}

};

} 
} 
} 
