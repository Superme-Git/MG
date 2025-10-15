
package fire.msp.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class NpcInfo implements Marshal {
	public long npckey; // npckey由GS模块负责生成
	public int npcbaseid; // npcbaseId
	public java.lang.String name; // npc名称
	public int shape; // npc造型
	public long sceneid; // 场景id
	public int mapid; // 地图id
	public java.lang.String mapname; // 场景名
	public int posx; // 坐标
	public int posy;
	public int posz;
	public long time; // 限时NPC的持续时间，默认为0时不是限时NPC

	public NpcInfo() {
		name = "";
		mapname = "";
	}

	public NpcInfo(long _npckey_, int _npcbaseid_, java.lang.String _name_, int _shape_, long _sceneid_, int _mapid_, java.lang.String _mapname_, int _posx_, int _posy_, int _posz_, long _time_) {
		this.npckey = _npckey_;
		this.npcbaseid = _npcbaseid_;
		this.name = _name_;
		this.shape = _shape_;
		this.sceneid = _sceneid_;
		this.mapid = _mapid_;
		this.mapname = _mapname_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.posz = _posz_;
		this.time = _time_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(npckey);
		_os_.marshal(npcbaseid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(shape);
		_os_.marshal(sceneid);
		_os_.marshal(mapid);
		_os_.marshal(mapname, "UTF-16LE");
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(posz);
		_os_.marshal(time);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		npcbaseid = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		sceneid = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		mapname = _os_.unmarshal_String("UTF-16LE");
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		posz = _os_.unmarshal_int();
		time = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof NpcInfo) {
			NpcInfo _o_ = (NpcInfo)_o1_;
			if (npckey != _o_.npckey) return false;
			if (npcbaseid != _o_.npcbaseid) return false;
			if (!name.equals(_o_.name)) return false;
			if (shape != _o_.shape) return false;
			if (sceneid != _o_.sceneid) return false;
			if (mapid != _o_.mapid) return false;
			if (!mapname.equals(_o_.mapname)) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (posz != _o_.posz) return false;
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
		_h_ += shape;
		_h_ += (int)sceneid;
		_h_ += mapid;
		_h_ += mapname.hashCode();
		_h_ += posx;
		_h_ += posy;
		_h_ += posz;
		_h_ += (int)time;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(npcbaseid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append("T").append(mapname.length()).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(posz).append(",");
		_sb_.append(time).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

