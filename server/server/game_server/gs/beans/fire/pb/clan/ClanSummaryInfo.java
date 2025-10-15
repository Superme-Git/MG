
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ClanSummaryInfo implements Marshal {
	public long clanid; // 公会key
	public int index; // 公会序号
	public java.lang.String clanname; // 公会名称
	public int membernum; // 公会人数
	public int clanlevel; // 公会等级
	public java.lang.String clanmastername; // 会长名字
	public long clanmasterid; // 会长id
	public java.lang.String oldclanname; // 公会曾用名
	public int hotellevel; // 公会旅馆等级

	public ClanSummaryInfo() {
		clanname = "";
		clanmastername = "";
		oldclanname = "";
	}

	public ClanSummaryInfo(long _clanid_, int _index_, java.lang.String _clanname_, int _membernum_, int _clanlevel_, java.lang.String _clanmastername_, long _clanmasterid_, java.lang.String _oldclanname_, int _hotellevel_) {
		this.clanid = _clanid_;
		this.index = _index_;
		this.clanname = _clanname_;
		this.membernum = _membernum_;
		this.clanlevel = _clanlevel_;
		this.clanmastername = _clanmastername_;
		this.clanmasterid = _clanmasterid_;
		this.oldclanname = _oldclanname_;
		this.hotellevel = _hotellevel_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(clanid);
		_os_.marshal(index);
		_os_.marshal(clanname, "UTF-16LE");
		_os_.marshal(membernum);
		_os_.marshal(clanlevel);
		_os_.marshal(clanmastername, "UTF-16LE");
		_os_.marshal(clanmasterid);
		_os_.marshal(oldclanname, "UTF-16LE");
		_os_.marshal(hotellevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanid = _os_.unmarshal_long();
		index = _os_.unmarshal_int();
		clanname = _os_.unmarshal_String("UTF-16LE");
		membernum = _os_.unmarshal_int();
		clanlevel = _os_.unmarshal_int();
		clanmastername = _os_.unmarshal_String("UTF-16LE");
		clanmasterid = _os_.unmarshal_long();
		oldclanname = _os_.unmarshal_String("UTF-16LE");
		hotellevel = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ClanSummaryInfo) {
			ClanSummaryInfo _o_ = (ClanSummaryInfo)_o1_;
			if (clanid != _o_.clanid) return false;
			if (index != _o_.index) return false;
			if (!clanname.equals(_o_.clanname)) return false;
			if (membernum != _o_.membernum) return false;
			if (clanlevel != _o_.clanlevel) return false;
			if (!clanmastername.equals(_o_.clanmastername)) return false;
			if (clanmasterid != _o_.clanmasterid) return false;
			if (!oldclanname.equals(_o_.oldclanname)) return false;
			if (hotellevel != _o_.hotellevel) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)clanid;
		_h_ += index;
		_h_ += clanname.hashCode();
		_h_ += membernum;
		_h_ += clanlevel;
		_h_ += clanmastername.hashCode();
		_h_ += (int)clanmasterid;
		_h_ += oldclanname.hashCode();
		_h_ += hotellevel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanid).append(",");
		_sb_.append(index).append(",");
		_sb_.append("T").append(clanname.length()).append(",");
		_sb_.append(membernum).append(",");
		_sb_.append(clanlevel).append(",");
		_sb_.append("T").append(clanmastername.length()).append(",");
		_sb_.append(clanmasterid).append(",");
		_sb_.append("T").append(oldclanname.length()).append(",");
		_sb_.append(hotellevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

