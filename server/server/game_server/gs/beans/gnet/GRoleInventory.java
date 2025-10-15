
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 奖品信息
*/
public class GRoleInventory implements Marshal {
	public int id;
	public int pos;
	public int count;
	public int max_count;
	public byte container_id;
	public com.locojoy.base.Octets data;
	public int guid1;
	public int guid2;
	public int mask;
	public int proctype;
	public int reserved;

	public GRoleInventory() {
		id = 0;
		pos = -1;
		count = 0;
		max_count = 0;
		container_id = 0;
		data = new com.locojoy.base.Octets();
		guid1 = 0;
		guid2 = 0;
		mask = 0;
		proctype = 0;
		reserved = 0;
	}

	public GRoleInventory(int _id_, int _pos_, int _count_, int _max_count_, byte _container_id_, com.locojoy.base.Octets _data_, int _guid1_, int _guid2_, int _mask_, int _proctype_, int _reserved_) {
		this.id = _id_;
		this.pos = _pos_;
		this.count = _count_;
		this.max_count = _max_count_;
		this.container_id = _container_id_;
		this.data = _data_;
		this.guid1 = _guid1_;
		this.guid2 = _guid2_;
		this.mask = _mask_;
		this.proctype = _proctype_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(pos);
		_os_.marshal(count);
		_os_.marshal(max_count);
		_os_.marshal(container_id);
		_os_.marshal(data);
		_os_.marshal(guid1);
		_os_.marshal(guid2);
		_os_.marshal(mask);
		_os_.marshal(proctype);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		pos = _os_.unmarshal_int();
		count = _os_.unmarshal_int();
		max_count = _os_.unmarshal_int();
		container_id = _os_.unmarshal_byte();
		data = _os_.unmarshal_Octets();
		guid1 = _os_.unmarshal_int();
		guid2 = _os_.unmarshal_int();
		mask = _os_.unmarshal_int();
		proctype = _os_.unmarshal_int();
		reserved = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRoleInventory) {
			GRoleInventory _o_ = (GRoleInventory)_o1_;
			if (id != _o_.id) return false;
			if (pos != _o_.pos) return false;
			if (count != _o_.count) return false;
			if (max_count != _o_.max_count) return false;
			if (container_id != _o_.container_id) return false;
			if (!data.equals(_o_.data)) return false;
			if (guid1 != _o_.guid1) return false;
			if (guid2 != _o_.guid2) return false;
			if (mask != _o_.mask) return false;
			if (proctype != _o_.proctype) return false;
			if (reserved != _o_.reserved) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += pos;
		_h_ += count;
		_h_ += max_count;
		_h_ += (int)container_id;
		_h_ += data.hashCode();
		_h_ += guid1;
		_h_ += guid2;
		_h_ += mask;
		_h_ += proctype;
		_h_ += reserved;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(pos).append(",");
		_sb_.append(count).append(",");
		_sb_.append(max_count).append(",");
		_sb_.append(container_id).append(",");
		_sb_.append("B").append(data.size()).append(",");
		_sb_.append(guid1).append(",");
		_sb_.append(guid2).append(",");
		_sb_.append(mask).append(",");
		_sb_.append(proctype).append(",");
		_sb_.append(reserved).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

