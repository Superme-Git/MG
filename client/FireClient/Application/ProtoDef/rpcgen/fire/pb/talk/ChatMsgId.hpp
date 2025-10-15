
#pragma once


namespace fire { 
namespace pb { 
namespace talk { 

class ChatMsgId : public FireNet::Marshal
{
public:
	enum {
		CHAT_SPEED_LIMIT = 140497, // 请不要说话太快
		CANNOT_USE_TEAM_CHANNEL = 140498, // 你还没有队伍
		CHAT_WORLD_CHANNEL_TIME_LIMIT = 140500, // 每隔N秒才能在世界频道发言
		CHAT_WORLD_CHANNEL_LEVEL_LIMIT = 140501, // 等级不足N级不能在世界频道发言
		CANNOT_USE_FACTION_CHANNEL = 141053, // 未加入公会,不能使用公会频道
		CHAT_TEAM_APPLY_CHANNEL_TIME_LIMIT = 150028, // 每隔N秒才能在组队申请频道发言
		CHAT_SCHOOL_CHANNEL_LEVEL_LIMIT = 160471, // 等级不足N级不能在职业频道发言
		CHAT_CURRENT_CHANNEL_LEVEL_LIMIT = 142924, // 等级不足N级不能在当前频道发言
	};

	ChatMsgId() {
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

	bool operator<(const ChatMsgId &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ChatMsgId &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
