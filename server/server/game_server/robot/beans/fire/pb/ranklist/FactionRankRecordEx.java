
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 公会榜
*/
public class FactionRankRecordEx implements Marshal {
	public int rank; // 排名
	public long factionid; // 公会id
	public java.lang.String factionname; // 公会名字
	public long progressstime; // 进度时间
	public int progresss; // 公会进度
	public int factionlevel; // 公会等级
	public int externdata; // 当前人数/综合实力
	public int hotellevel; // 旅馆等级

	public FactionRankRecordEx() {
		factionname = "";
	}

	public FactionRankRecordEx(int _rank_, long _factionid_, java.lang.String _factionname_, long _progressstime_, int _progresss_, int _factionlevel_, int _externdata_, int _hotellevel_) {
		this.rank = _rank_;
		this.factionid = _factionid_;
		this.factionname = _factionname_;
		this.progressstime = _progressstime_;
		this.progresss = _progresss_;
		this.factionlevel = _factionlevel_;
		this.externdata = _externdata_;
		this.hotellevel = _hotellevel_;
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
		_os_.marshal(factionlevel);
		_os_.marshal(externdata);
		_os_.marshal(hotellevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rank = _os_.unmarshal_int();
		factionid = _os_.unmarshal_long();
		factionname = _os_.unmarshal_String("UTF-16LE");
		progressstime = _os_.unmarshal_long();
		progresss = _os_.unmarshal_int();
		factionlevel = _os_.unmarshal_int();
		externdata = _os_.unmarshal_int();
		hotellevel = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof FactionRankRecordEx) {
			FactionRankRecordEx _o_ = (FactionRankRecordEx)_o1_;
			if (rank != _o_.rank) return false;
			if (factionid != _o_.factionid) return false;
			if (!factionname.equals(_o_.factionname)) return false;
			if (progressstime != _o_.progressstime) return false;
			if (progresss != _o_.progresss) return false;
			if (factionlevel != _o_.factionlevel) return false;
			if (externdata != _o_.externdata) return false;
			if (hotellevel != _o_.hotellevel) return false;
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
		_h_ += factionlevel;
		_h_ += externdata;
		_h_ += hotellevel;
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
		_sb_.append(factionlevel).append(",");
		_sb_.append(externdata).append(",");
		_sb_.append(hotellevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

