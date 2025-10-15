
#pragma once


namespace fire { 
namespace pb { 
namespace circletask { 

class SpecialQuestType : public FireNet::Marshal
{
public:
	enum {
		Mail = 801001, // 送信
		Patrol = 801002, // 巡逻
		BuyItem = 801003, // 买道具
		CatchPet = 801004, // 抓守护
		Demonstrate = 801005, // 示威
		DemonstrateEye = 801006, // 示威
		Rescue = 801007, // 援救
		Tame = 801008, // 降服
		CaiJi = 801010, // 采集
		CaiJiFinish = 801030, // 采集完成
		ChuanDiXiaoXi = 801011, // 传递消息
		KillMonster = 801012, // 打怪
		KillMonsterFinish = 801032, // 打怪完成
		FindItem = 801013, // 打怪寻物
		FindItemFinish = 801033, // 打怪寻物完成
		Answer = 801014, // 答题
		AnswerFinish = 801034, // 答题完成
		CatchIt_Normal = 1030001, // 捉鬼
		CatchIt_Increase = 1030002, // 鬼王
	};

	SpecialQuestType() {
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

	bool operator<(const SpecialQuestType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SpecialQuestType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
