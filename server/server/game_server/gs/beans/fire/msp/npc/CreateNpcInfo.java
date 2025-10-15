
package fire.msp.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class CreateNpcInfo implements Marshal {
	public long npckey; // npckey由GS模块负责生成
	public int npcbaseid; // npcbaseId
	public java.lang.String name; // npc名称
	public int dir; // 方向
	public long sceneid; // 场景id
	public long ownerid; // 如果大于0，则sceneid为mapid
	public long time; // 限时NPC的持续时间，单位毫秒，为0时是非限时NPC

	public CreateNpcInfo() {
		name = "";
	}

	public CreateNpcInfo(long _npckey_, int _npcbaseid_, java.lang.String _name_, int _dir_, long _sceneid_, long _ownerid_, long _time_) {
		this.npckey = _npckey_;
		this.npcbaseid = _npcbaseid_;
		this.name = _name_;
		this.dir = _dir_;
		this.sceneid = _sceneid_;
		this.ownerid = _ownerid_;
		this.time = _time_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(npckey);
		_os_.marshal(npcbaseid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(dir);
		_os_.marshal(sceneid);
		_os_.marshal(ownerid);
		_os_.marshal(time);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		npcbaseid = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		dir = _os_.unmarshal_int();
		sceneid = _os_.unmarshal_long();
		ownerid = _os_.unmarshal_long();
		time = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CreateNpcInfo) {
			CreateNpcInfo _o_ = (CreateNpcInfo)_o1_;
			if (npckey != _o_.npckey) return false;
			if (npcbaseid != _o_.npcbaseid) return false;
			if (!name.equals(_o_.name)) return false;
			if (dir != _o_.dir) return false;
			if (sceneid != _o_.sceneid) return false;
			if (ownerid != _o_.ownerid) return false;
			if (time != _o_.time) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += npcbaseid;
		_h_ += name.hashCode();
		_h_ += dir;
		_h_ += (int)sceneid;
		_h_ += (int)ownerid;
		_h_ += (int)time;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(npcbaseid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(dir).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append(time).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

