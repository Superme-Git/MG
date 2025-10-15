
package fire.msp.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** Transfor相关的方法 END
*/
public class CreateNpcInfo implements Marshal {
	public int npcid;
	public java.lang.String npcname;
	public int dir;
	public long sceneid; // 场景id
	public int xpos;
	public int ypos;

	public CreateNpcInfo() {
		npcname = "";
	}

	public CreateNpcInfo(int _npcid_, java.lang.String _npcname_, int _dir_, long _sceneid_, int _xpos_, int _ypos_) {
		this.npcid = _npcid_;
		this.npcname = _npcname_;
		this.dir = _dir_;
		this.sceneid = _sceneid_;
		this.xpos = _xpos_;
		this.ypos = _ypos_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(npcid);
		_os_.marshal(npcname, "UTF-16LE");
		_os_.marshal(dir);
		_os_.marshal(sceneid);
		_os_.marshal(xpos);
		_os_.marshal(ypos);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npcid = _os_.unmarshal_int();
		npcname = _os_.unmarshal_String("UTF-16LE");
		dir = _os_.unmarshal_int();
		sceneid = _os_.unmarshal_long();
		xpos = _os_.unmarshal_int();
		ypos = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CreateNpcInfo) {
			CreateNpcInfo _o_ = (CreateNpcInfo)_o1_;
			if (npcid != _o_.npcid) return false;
			if (!npcname.equals(_o_.npcname)) return false;
			if (dir != _o_.dir) return false;
			if (sceneid != _o_.sceneid) return false;
			if (xpos != _o_.xpos) return false;
			if (ypos != _o_.ypos) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcid;
		_h_ += npcname.hashCode();
		_h_ += dir;
		_h_ += (int)sceneid;
		_h_ += xpos;
		_h_ += ypos;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcid).append(",");
		_sb_.append("T").append(npcname.length()).append(",");
		_sb_.append(dir).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(xpos).append(",");
		_sb_.append(ypos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

