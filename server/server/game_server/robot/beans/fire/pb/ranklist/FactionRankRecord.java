
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class FactionRankRecord implements Marshal {
	public int rank; // 排名
	public java.lang.String factionname; // 公会名称
	public java.lang.String mastername; // 帮主名称
	public int level; // 公会等级
	public int camp; // 阵营
	public long factionkey; // 公会key

	public FactionRankRecord() {
		factionname = "";
		mastername = "";
	}

	public FactionRankRecord(int _rank_, java.lang.String _factionname_, java.lang.String _mastername_, int _level_, int _camp_, long _factionkey_) {
		this.rank = _rank_;
		this.factionname = _factionname_;
		this.mastername = _mastername_;
		this.level = _level_;
		this.camp = _camp_;
		this.factionkey = _factionkey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(rank);
		_os_.marshal(factionname, "UTF-16LE");
		_os_.marshal(mastername, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(camp);
		_os_.marshal(factionkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rank = _os_.unmarshal_int();
		factionname = _os_.unmarshal_String("UTF-16LE");
		mastername = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		camp = _os_.unmarshal_int();
		factionkey = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof FactionRankRecord) {
			FactionRankRecord _o_ = (FactionRankRecord)_o1_;
			if (rank != _o_.rank) return false;
			if (!factionname.equals(_o_.factionname)) return false;
			if (!mastername.equals(_o_.mastername)) return false;
			if (level != _o_.level) return false;
			if (camp != _o_.camp) return false;
			if (factionkey != _o_.factionkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rank;
		_h_ += factionname.hashCode();
		_h_ += mastername.hashCode();
		_h_ += level;
		_h_ += camp;
		_h_ += (int)factionkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rank).append(",");
		_sb_.append("T").append(factionname.length()).append(",");
		_sb_.append("T").append(mastername.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(camp).append(",");
		_sb_.append(factionkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

