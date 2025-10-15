
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ClanFightHistroyRank implements Marshal {
	public int rank; // 排名 by changhao
	public long clanid; // 公会id by changhao
	public java.lang.String clanname; // 公会名字 by changhao
	public int clanlevel; // 公会等级 by changhao
	public int fightcount; // 战斗次数 by changhao
	public int wincount; // 胜利次数 by changhao

	public ClanFightHistroyRank() {
		clanname = "";
	}

	public ClanFightHistroyRank(int _rank_, long _clanid_, java.lang.String _clanname_, int _clanlevel_, int _fightcount_, int _wincount_) {
		this.rank = _rank_;
		this.clanid = _clanid_;
		this.clanname = _clanname_;
		this.clanlevel = _clanlevel_;
		this.fightcount = _fightcount_;
		this.wincount = _wincount_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(rank);
		_os_.marshal(clanid);
		_os_.marshal(clanname, "UTF-16LE");
		_os_.marshal(clanlevel);
		_os_.marshal(fightcount);
		_os_.marshal(wincount);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rank = _os_.unmarshal_int();
		clanid = _os_.unmarshal_long();
		clanname = _os_.unmarshal_String("UTF-16LE");
		clanlevel = _os_.unmarshal_int();
		fightcount = _os_.unmarshal_int();
		wincount = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ClanFightHistroyRank) {
			ClanFightHistroyRank _o_ = (ClanFightHistroyRank)_o1_;
			if (rank != _o_.rank) return false;
			if (clanid != _o_.clanid) return false;
			if (!clanname.equals(_o_.clanname)) return false;
			if (clanlevel != _o_.clanlevel) return false;
			if (fightcount != _o_.fightcount) return false;
			if (wincount != _o_.wincount) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rank;
		_h_ += (int)clanid;
		_h_ += clanname.hashCode();
		_h_ += clanlevel;
		_h_ += fightcount;
		_h_ += wincount;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rank).append(",");
		_sb_.append(clanid).append(",");
		_sb_.append("T").append(clanname.length()).append(",");
		_sb_.append(clanlevel).append(",");
		_sb_.append(fightcount).append(",");
		_sb_.append(wincount).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

