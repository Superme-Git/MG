
#pragma once


namespace fire { 
namespace pb { 

class SysConfigType : public FireNet::Marshal
{
public:
	enum {
		Music = 1, // 音乐
		Volume = 2, // 音量
		SoundSpecEffect = 3, // 音效
		SceneEffect = 4, // 场景特效
		MaxScreenShowNum = 5, // 设置同屏最大显示人数
		ScreenRefresh = 6, // 画面刷新频率
		AutoVoiceGang = 7, // 自动语音--公会频道
		AutoVoiceWorld = 8, // 自动语音--世界频道
		AutoVoiceTeam = 9, // 自动语音--组队频道
		AutoVoiceSchool = 10, // 自动语音--职业频道
		RefuseFriend = 11, // 拒绝好友邀请
		WorldChannel = 12, // 世界频道
		GangChannel = 13, // 公会频道
		SchoolChannel = 14, // 职业频道
		CurrentChannel = 15, // 当前频道
		TeamChannel = 16, // 组队频道
		PVPNotify = 17, // PVPNotify
		friendchatencrypt = 18, // 好友聊天记录加密
		friendmessage = 19, // 只接受好友消息
		rolePointAdd = 20, // rolePointAdd
		petPointAdd = 21, // petPointAdd
		skillPointAdd = 22, // skillPointAdd
		huoyueduAdd = 23, // huoyueduAdd
		zhenfaAdd = 24, // zhenfaAdd
		skillopen = 25, // 技能开放
		factionopen = 26, // 公会开放
		petopen = 27, // 宠物开放
		patopen = 28, // 助战开放
		zuduichannel = 29, // 组队频道
		guajiopen = 30, // 挂机开放
		zhiyinopen = 31, // 指引开放
		huodongopen = 32, // 活动开放
		refuseqiecuo = 33, // 切磋
		ts_julonghuwei = 34, // 推送巨龙护卫
		ts_julongjuntuan = 35, // 推送巨龙军团
		ts_guanjunshilian = 36, // 推送冠军试炼
		ts_renwentansuo = 37, // 推送人文探索
		ts_1v1 = 38, // 推送1v1
		ts_gonghuifuben = 39, // 推送工会副本
		ts_3v3 = 40, // 推送3v3
		ts_zhihuishilian = 41, // 推送智慧试炼
		refuseclan = 42, // 拒绝公会邀请
		refuseotherseeequip = 43, // 拒绝别人查看装备 by changhao
		screenrecord = 44, // 录屏功能
		equipendure = 45, // 装备耐久
		ts_gonghuizhan = 46, // 工会战
		rolldianshezhi = 47, // ROLL点设置
		framesimplify = 48, // 界面简化
	};

	SysConfigType() {
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

	bool operator<(const SysConfigType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SysConfigType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
