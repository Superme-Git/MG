
#pragma once


namespace fire { 
namespace pb { 
namespace clan { 

class ClanPositionType : public FireNet::Marshal
{
public:
	enum {
		ClanMember = 11, // 成员
		ClanArmyGroupElite4 = 10, // 第四军团精英
		ClanArmyGroupElite3 = 9, // 第三军团精英
		ClanArmyGroupElite2 = 8, // 第二军团精英
		ClanArmyGroupElite1 = 7, // 第一军团精英
		ClanArmyGroup4 = 6, // 第四军团长
		ClanArmyGroup3 = 5, // 第三军团长
		ClanArmyGroup2 = 4, // 第二军团长
		ClanArmyGroup1 = 3, // 第一军团长
		ClanViceMaster = 2, // 副会长
		ClanMaster = 1, // 会长
	};

	ClanPositionType() {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		return _os_;
	}

	bool operator<(const ClanPositionType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ClanPositionType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
