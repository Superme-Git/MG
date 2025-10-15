
package fire.pb.clan.fight;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ClanFight implements Marshal {
	public long clanid1;
	public java.lang.String clanname1;
	public long clanid2;
	public java.lang.String clanname2;
	public int winner; // -1是还没结果0是第一个赢1是第二个赢 by changhao

	public ClanFight() {
		clanname1 = "";
		clanname2 = "";
	}

	public ClanFight(long _clanid1_, java.lang.String _clanname1_, long _clanid2_, java.lang.String _clanname2_, int _winner_) {
		this.clanid1 = _clanid1_;
		this.clanname1 = _clanname1_;
		this.clanid2 = _clanid2_;
		this.clanname2 = _clanname2_;
		this.winner = _winner_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(clanid1);
		_os_.marshal(clanname1, "UTF-16LE");
		_os_.marshal(clanid2);
		_os_.marshal(clanname2, "UTF-16LE");
		_os_.marshal(winner);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanid1 = _os_.unmarshal_long();
		clanname1 = _os_.unmarshal_String("UTF-16LE");
		clanid2 = _os_.unmarshal_long();
		clanname2 = _os_.unmarshal_String("UTF-16LE");
		winner = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ClanFight) {
			ClanFight _o_ = (ClanFight)_o1_;
			if (clanid1 != _o_.clanid1) return false;
			if (!clanname1.equals(_o_.clanname1)) return false;
			if (clanid2 != _o_.clanid2) return false;
			if (!clanname2.equals(_o_.clanname2)) return false;
			if (winner != _o_.winner) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)clanid1;
		_h_ += clanname1.hashCode();
		_h_ += (int)clanid2;
		_h_ += clanname2.hashCode();
		_h_ += winner;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanid1).append(",");
		_sb_.append("T").append(clanname1.length()).append(",");
		_sb_.append(clanid2).append(",");
		_sb_.append("T").append(clanname2.length()).append(",");
		_sb_.append(winner).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

