
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRoleEnterScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRoleEnterScene extends __SRoleEnterScene__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790441;

	public int getType() {
		return 790441;
	}

	public final static int CHANNEAL = 516012; // 跳转点跳转
	public final static int ITEM = 516013; // 道具跳转
	public final static int SYSTEM_DRAG = 516014; // 系统拉人
	public final static int ENTER = 516015; // 进入游戏
	public final static int DEATH = 516016; // 死亡跳转
	public final static int CHEFU = 516017; // 跳转
	public final static int QUEST_CG = 516018; // 任务跳转（播动画）
	public final static int FORCE_GOTO = 516019; // 强制跳转
	public final static int SKILL = 516020; // 回职业技能跳转
	public final static int MARRIAGE = 516021; // 结婚任务跳转
	public final static int QUEST = 516022; // 任务跳转（不播动画）
	public final static int DRAGONBOAT = 516023; // 进入赛船地图
	public final static int GM_HOLD = 516025; // GM拉人
	public final static int INSTANCE = 516026; // 副本任务
	public final static int ENTER_COMMON_SCENCE = 516028; // 通用副本进入
	public final static int ENTER_LINE_SCENCE = 516029; // 一次性副本进入
	public final static int CLAN_GOTO = 516030; // 公会副本跳转
	public final static int BING_FENG_GOTO = 516031; // 冰封王座副本跳转

	public java.lang.String ownername; // 场景所属者名字(副本场景有效)
	public fire.pb.move.Pos destpos; // 目标地点
	public byte destz; // 目标z
	public int changetype; // 跳转类型(参考以上三个枚举值)
	public long sceneid; // 场景id
	public byte weatherid; // 气候Id
	public java.lang.String tipsparm; // tips参数

	public SRoleEnterScene() {
		ownername = "";
		destpos = new fire.pb.move.Pos();
		tipsparm = "";
	}

	public SRoleEnterScene(java.lang.String _ownername_, fire.pb.move.Pos _destpos_, byte _destz_, int _changetype_, long _sceneid_, byte _weatherid_, java.lang.String _tipsparm_) {
		this.ownername = _ownername_;
		this.destpos = _destpos_;
		this.destz = _destz_;
		this.changetype = _changetype_;
		this.sceneid = _sceneid_;
		this.weatherid = _weatherid_;
		this.tipsparm = _tipsparm_;
	}

	public final boolean _validator_() {
		if (!destpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ownername, "UTF-16LE");
		_os_.marshal(destpos);
		_os_.marshal(destz);
		_os_.marshal(changetype);
		_os_.marshal(sceneid);
		_os_.marshal(weatherid);
		_os_.marshal(tipsparm, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ownername = _os_.unmarshal_String("UTF-16LE");
		destpos.unmarshal(_os_);
		destz = _os_.unmarshal_byte();
		changetype = _os_.unmarshal_int();
		sceneid = _os_.unmarshal_long();
		weatherid = _os_.unmarshal_byte();
		tipsparm = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRoleEnterScene) {
			SRoleEnterScene _o_ = (SRoleEnterScene)_o1_;
			if (!ownername.equals(_o_.ownername)) return false;
			if (!destpos.equals(_o_.destpos)) return false;
			if (destz != _o_.destz) return false;
			if (changetype != _o_.changetype) return false;
			if (sceneid != _o_.sceneid) return false;
			if (weatherid != _o_.weatherid) return false;
			if (!tipsparm.equals(_o_.tipsparm)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += ownername.hashCode();
		_h_ += destpos.hashCode();
		_h_ += (int)destz;
		_h_ += changetype;
		_h_ += (int)sceneid;
		_h_ += (int)weatherid;
		_h_ += tipsparm.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(ownername.length()).append(",");
		_sb_.append(destpos).append(",");
		_sb_.append(destz).append(",");
		_sb_.append(changetype).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(weatherid).append(",");
		_sb_.append("T").append(tipsparm.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

