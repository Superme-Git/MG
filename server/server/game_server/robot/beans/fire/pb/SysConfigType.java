
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SysConfigType implements Marshal , Comparable<SysConfigType>{
	public final static int Music = 1; // 音乐
	public final static int Volume = 2; // 音量
	public final static int SoundSpecEffect = 3; // 音效
	public final static int SceneEffect = 4; // 场景特效
	public final static int MaxScreenShowNum = 5; // 设置同屏最大显示人数
	public final static int ScreenRefresh = 6; // 画面刷新频率
	public final static int AutoVoiceGang = 7; // 自动语音--公会频道
	public final static int AutoVoiceWorld = 8; // 自动语音--世界频道
	public final static int AutoVoiceTeam = 9; // 自动语音--组队频道
	public final static int AutoVoiceSchool = 10; // 自动语音--职业频道
	public final static int RefuseFriend = 11; // 拒绝好友邀请
	public final static int WorldChannel = 12; // 世界频道
	public final static int GangChannel = 13; // 公会频道
	public final static int SchoolChannel = 14; // 职业频道
	public final static int CurrentChannel = 15; // 当前频道
	public final static int TeamChannel = 16; // 组队频道
	public final static int PVPNotify = 17; // PVPNotify
	public final static int friendchatencrypt = 18; // 好友聊天记录加密
	public final static int friendmessage = 19; // 只接受好友消息
	public final static int rolePointAdd = 20; // rolePointAdd
	public final static int petPointAdd = 21; // petPointAdd
	public final static int skillPointAdd = 22; // skillPointAdd
	public final static int huoyueduAdd = 23; // huoyueduAdd
	public final static int zhenfaAdd = 24; // zhenfaAdd
	public final static int skillopen = 25; // 技能开放
	public final static int factionopen = 26; // 公会开放
	public final static int petopen = 27; // 宠物开放
	public final static int patopen = 28; // 助战开放
	public final static int zuduichannel = 29; // 组队频道
	public final static int guajiopen = 30; // 挂机开放
	public final static int zhiyinopen = 31; // 指引开放
	public final static int huodongopen = 32; // 活动开放
	public final static int refuseqiecuo = 33; // 切磋
	public final static int ts_julonghuwei = 34; // 推送巨龙护卫
	public final static int ts_julongjuntuan = 35; // 推送巨龙军团
	public final static int ts_guanjunshilian = 36; // 推送冠军试炼
	public final static int ts_renwentansuo = 37; // 推送人文探索
	public final static int ts_1v1 = 38; // 推送1v1
	public final static int ts_gonghuifuben = 39; // 推送工会副本
	public final static int ts_3v3 = 40; // 推送3v3
	public final static int ts_zhihuishilian = 41; // 推送智慧试炼
	public final static int refuseclan = 42; // 拒绝公会邀请
	public final static int refuseotherseeequip = 43; // 拒绝别人查看装备 by changhao
	public final static int screenrecord = 44; // 录屏功能
	public final static int equipendure = 45; // 装备耐久
	public final static int ts_gonghuizhan = 46; // 工会战


	public SysConfigType() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SysConfigType) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SysConfigType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

