
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 公会副本竞速榜
*/
public class FactionRaidRankRecord implements Marshal {
	public int rank; // 排名
	public long factionid; // 公会id
	public java.lang.String factionname; // 公会名字
	public long progressstime; // 进度时间
	public int progresss; // 公会进度
	public java.lang.String factionmonstername; // 会长名字
	public java.lang.String factioncopyname; // 公会副本名字 by changhao
	public float bosshp; // BOSS血量百分比 by changhao

	public FactionRaidRankRecord() {
		factionname = "";
		factionmonstername = "";
		factioncopyname = "";
	}

	public FactionRaidRankRecord(int _rank_, long _factionid_, java.lang.String _factionname_, long _progressstime_, int _progresss_, java.lang.String _factionmonstername_, java.lang.String _factioncopyname_, float _bosshp_) {
		this.rank = _rank_;
		this.factionid = _factionid_;
		this.factionname = _factionname_;
		this.progressstime = _progressstime_;
		this.progresss = _progresss_;
		this.factionmonstername = _factionmonstername_;
		this.factioncopyname = _factioncopyname_;
		this.bosshp = _bosshp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(rank);
		_os_.marshal(factionid);
		_os_.marshal(factionname, "UTF-16LE");
		_os_.marshal(progressstime);
		_os_.marshal(progresss);
		_os_.marshal(factionmonstername, "UTF-16LE");
		_os_.marshal(factioncopyname, "UTF-16LE");
		_os_.marshal(bosshp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rank = _os_.unmarshal_int();
		factionid = _os_.unmarshal_long();
		factionname = _os_.unmarshal_String("UTF-16LE");
		progressstime = _os_.unmarshal_long();
		progresss = _os_.unmarshal_int();
		factionmonstername = _os_.unmarshal_String("UTF-16LE");
		factioncopyname = _os_.unmarshal_String("UTF-16LE");
		bosshp = _os_.unmarshal_float();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof FactionRaidRankRecord) {
			FactionRaidRankRecord _o_ = (FactionRaidRankRecord)_o1_;
			if (rank != _o_.rank) return false;
			if (factionid != _o_.factionid) return false;
			if (!factionname.equals(_o_.factionname)) return false;
			if (progressstime != _o_.progressstime) return false;
			if (progresss != _o_.progresss) return false;
			if (!factionmonstername.equals(_o_.factionmonstername)) return false;
			if (!factioncopyname.equals(_o_.factioncopyname)) return false;
			if (bosshp != _o_.bosshp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rank;
		_h_ += (int)factionid;
		_h_ += factionname.hashCode();
		_h_ += (int)progressstime;
		_h_ += progresss;
		_h_ += factionmonstername.hashCode();
		_h_ += factioncopyname.hashCode();
		_h_ += Float.floatToIntBits(bosshp);
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rank).append(",");
		_sb_.append(factionid).append(",");
		_sb_.append("T").append(factionname.length()).append(",");
		_sb_.append(progressstime).append(",");
		_sb_.append(progresss).append(",");
		_sb_.append("T").append(factionmonstername.length()).append(",");
		_sb_.append("T").append(factioncopyname.length()).append(",");
		_sb_.append(bosshp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

