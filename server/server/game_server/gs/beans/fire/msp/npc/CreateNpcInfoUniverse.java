
package fire.msp.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** GS发送请求，要求地图模块召出NPC的详细信息
*/
public class CreateNpcInfoUniverse implements Marshal {
	public int npcbaseid; // npcbaseId
	public java.lang.String name; // npc名称
	public int shape; // npc造型
	public long ownerid; // ownerid
	public int mapid; // 地图id
	public int posx; // 坐标
	public int posy;
	public int num; // 创建NPC的个数
	public long npckey; // 为了仗义疏财活动加的
	public long time;

	public CreateNpcInfoUniverse() {
		name = "";
	}

	public CreateNpcInfoUniverse(int _npcbaseid_, java.lang.String _name_, int _shape_, long _ownerid_, int _mapid_, int _posx_, int _posy_, int _num_, long _npckey_, long _time_) {
		this.npcbaseid = _npcbaseid_;
		this.name = _name_;
		this.shape = _shape_;
		this.ownerid = _ownerid_;
		this.mapid = _mapid_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.num = _num_;
		this.npckey = _npckey_;
		this.time = _time_;
	}

	public final boolean _validator_() {
		if (npcbaseid <= 0) return false;
		if (mapid <= 0) return false;
		if (num <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(npcbaseid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(shape);
		_os_.marshal(ownerid);
		_os_.marshal(mapid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(num);
		_os_.marshal(npckey);
		_os_.marshal(time);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npcbaseid = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		ownerid = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		time = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CreateNpcInfoUniverse) {
			CreateNpcInfoUniverse _o_ = (CreateNpcInfoUniverse)_o1_;
			if (npcbaseid != _o_.npcbaseid) return false;
			if (!name.equals(_o_.name)) return false;
			if (shape != _o_.shape) return false;
			if (ownerid != _o_.ownerid) return false;
			if (mapid != _o_.mapid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (num != _o_.num) return false;
			if (npckey != _o_.npckey) return false;
			if (time != _o_.time) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcbaseid;
		_h_ += name.hashCode();
		_h_ += shape;
		_h_ += (int)ownerid;
		_h_ += mapid;
		_h_ += posx;
		_h_ += posy;
		_h_ += num;
		_h_ += (int)npckey;
		_h_ += (int)time;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcbaseid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(num).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(time).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

