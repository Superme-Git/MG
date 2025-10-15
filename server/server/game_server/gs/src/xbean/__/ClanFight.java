
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanFight extends mkdb.XBean implements xbean.ClanFight {
	private long clan1; // 第一个公会 by changhao
	private int clanindex1; // 第一个公会 by changhao
	private String clanname1; // 第一个公会名字 by changhao
	private long clan2; // 第二个公会 by changhao
	private int clanindex2; // 第二个公会 by changhao
	private String clanname2; // 第二个公会名字 by changhao
	private int clanlevel1; // 第一个公会等级 by changhao
	private int clanlevel2; // 第二个公会等级 by changhao
	private int clan1scroe; // clan1积分 by changhao
	private int clan2scroe; // clan2积分 by changhao
	private int winner; // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao

	@Override
	public void _reset_unsafe_() {
		clan1 = 0L;
		clanindex1 = 0;
		clanname1 = "";
		clan2 = 0L;
		clanindex2 = 0;
		clanname2 = "";
		clanlevel1 = 0;
		clanlevel2 = 0;
		clan1scroe = 0;
		clan2scroe = 0;
		winner = 0;
	}

	ClanFight(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		clanname1 = "";
		clanname2 = "";
	}

	public ClanFight() {
		this(0, null, null);
	}

	public ClanFight(ClanFight _o_) {
		this(_o_, null, null);
	}

	ClanFight(xbean.ClanFight _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanFight) assign((ClanFight)_o1_);
		else if (_o1_ instanceof ClanFight.Data) assign((ClanFight.Data)_o1_);
		else if (_o1_ instanceof ClanFight.Const) assign(((ClanFight.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanFight _o_) {
		_o_._xdb_verify_unsafe_();
		clan1 = _o_.clan1;
		clanindex1 = _o_.clanindex1;
		clanname1 = _o_.clanname1;
		clan2 = _o_.clan2;
		clanindex2 = _o_.clanindex2;
		clanname2 = _o_.clanname2;
		clanlevel1 = _o_.clanlevel1;
		clanlevel2 = _o_.clanlevel2;
		clan1scroe = _o_.clan1scroe;
		clan2scroe = _o_.clan2scroe;
		winner = _o_.winner;
	}

	private void assign(ClanFight.Data _o_) {
		clan1 = _o_.clan1;
		clanindex1 = _o_.clanindex1;
		clanname1 = _o_.clanname1;
		clan2 = _o_.clan2;
		clanindex2 = _o_.clanindex2;
		clanname2 = _o_.clanname2;
		clanlevel1 = _o_.clanlevel1;
		clanlevel2 = _o_.clanlevel2;
		clan1scroe = _o_.clan1scroe;
		clan2scroe = _o_.clan2scroe;
		winner = _o_.winner;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(clan1);
		_os_.marshal(clanindex1);
		_os_.marshal(clanname1, mkdb.Const.IO_CHARSET);
		_os_.marshal(clan2);
		_os_.marshal(clanindex2);
		_os_.marshal(clanname2, mkdb.Const.IO_CHARSET);
		_os_.marshal(clanlevel1);
		_os_.marshal(clanlevel2);
		_os_.marshal(clan1scroe);
		_os_.marshal(clan2scroe);
		_os_.marshal(winner);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		clan1 = _os_.unmarshal_long();
		clanindex1 = _os_.unmarshal_int();
		clanname1 = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		clan2 = _os_.unmarshal_long();
		clanindex2 = _os_.unmarshal_int();
		clanname2 = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		clanlevel1 = _os_.unmarshal_int();
		clanlevel2 = _os_.unmarshal_int();
		clan1scroe = _os_.unmarshal_int();
		clan2scroe = _os_.unmarshal_int();
		winner = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ClanFight copy() {
		_xdb_verify_unsafe_();
		return new ClanFight(this);
	}

	@Override
	public xbean.ClanFight toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFight toBean() {
		_xdb_verify_unsafe_();
		return new ClanFight(this); // same as copy()
	}

	@Override
	public xbean.ClanFight toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFight toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getClan1() { // 第一个公会 by changhao
		_xdb_verify_unsafe_();
		return clan1;
	}

	@Override
	public int getClanindex1() { // 第一个公会 by changhao
		_xdb_verify_unsafe_();
		return clanindex1;
	}

	@Override
	public String getClanname1() { // 第一个公会名字 by changhao
		_xdb_verify_unsafe_();
		return clanname1;
	}

	@Override
	public com.locojoy.base.Octets getClanname1Octets() { // 第一个公会名字 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanname1(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getClan2() { // 第二个公会 by changhao
		_xdb_verify_unsafe_();
		return clan2;
	}

	@Override
	public int getClanindex2() { // 第二个公会 by changhao
		_xdb_verify_unsafe_();
		return clanindex2;
	}

	@Override
	public String getClanname2() { // 第二个公会名字 by changhao
		_xdb_verify_unsafe_();
		return clanname2;
	}

	@Override
	public com.locojoy.base.Octets getClanname2Octets() { // 第二个公会名字 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanname2(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getClanlevel1() { // 第一个公会等级 by changhao
		_xdb_verify_unsafe_();
		return clanlevel1;
	}

	@Override
	public int getClanlevel2() { // 第二个公会等级 by changhao
		_xdb_verify_unsafe_();
		return clanlevel2;
	}

	@Override
	public int getClan1scroe() { // clan1积分 by changhao
		_xdb_verify_unsafe_();
		return clan1scroe;
	}

	@Override
	public int getClan2scroe() { // clan2积分 by changhao
		_xdb_verify_unsafe_();
		return clan2scroe;
	}

	@Override
	public int getWinner() { // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao
		_xdb_verify_unsafe_();
		return winner;
	}

	@Override
	public void setClan1(long _v_) { // 第一个公会 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clan1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clan1) {
					public void rollback() { clan1 = _xdb_saved; }
				};}});
		clan1 = _v_;
	}

	@Override
	public void setClanindex1(int _v_) { // 第一个公会 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanindex1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanindex1) {
					public void rollback() { clanindex1 = _xdb_saved; }
				};}});
		clanindex1 = _v_;
	}

	@Override
	public void setClanname1(String _v_) { // 第一个公会名字 by changhao
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanname1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clanname1) {
					public void rollback() { clanname1 = _xdb_saved; }
				};}});
		clanname1 = _v_;
	}

	@Override
	public void setClanname1Octets(com.locojoy.base.Octets _v_) { // 第一个公会名字 by changhao
		_xdb_verify_unsafe_();
		this.setClanname1(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setClan2(long _v_) { // 第二个公会 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clan2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clan2) {
					public void rollback() { clan2 = _xdb_saved; }
				};}});
		clan2 = _v_;
	}

	@Override
	public void setClanindex2(int _v_) { // 第二个公会 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanindex2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanindex2) {
					public void rollback() { clanindex2 = _xdb_saved; }
				};}});
		clanindex2 = _v_;
	}

	@Override
	public void setClanname2(String _v_) { // 第二个公会名字 by changhao
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanname2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clanname2) {
					public void rollback() { clanname2 = _xdb_saved; }
				};}});
		clanname2 = _v_;
	}

	@Override
	public void setClanname2Octets(com.locojoy.base.Octets _v_) { // 第二个公会名字 by changhao
		_xdb_verify_unsafe_();
		this.setClanname2(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setClanlevel1(int _v_) { // 第一个公会等级 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanlevel1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanlevel1) {
					public void rollback() { clanlevel1 = _xdb_saved; }
				};}});
		clanlevel1 = _v_;
	}

	@Override
	public void setClanlevel2(int _v_) { // 第二个公会等级 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanlevel2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanlevel2) {
					public void rollback() { clanlevel2 = _xdb_saved; }
				};}});
		clanlevel2 = _v_;
	}

	@Override
	public void setClan1scroe(int _v_) { // clan1积分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clan1scroe") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clan1scroe) {
					public void rollback() { clan1scroe = _xdb_saved; }
				};}});
		clan1scroe = _v_;
	}

	@Override
	public void setClan2scroe(int _v_) { // clan2积分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clan2scroe") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clan2scroe) {
					public void rollback() { clan2scroe = _xdb_saved; }
				};}});
		clan2scroe = _v_;
	}

	@Override
	public void setWinner(int _v_) { // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "winner") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, winner) {
					public void rollback() { winner = _xdb_saved; }
				};}});
		winner = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanFight _o_ = null;
		if ( _o1_ instanceof ClanFight ) _o_ = (ClanFight)_o1_;
		else if ( _o1_ instanceof ClanFight.Const ) _o_ = ((ClanFight.Const)_o1_).nThis();
		else return false;
		if (clan1 != _o_.clan1) return false;
		if (clanindex1 != _o_.clanindex1) return false;
		if (!clanname1.equals(_o_.clanname1)) return false;
		if (clan2 != _o_.clan2) return false;
		if (clanindex2 != _o_.clanindex2) return false;
		if (!clanname2.equals(_o_.clanname2)) return false;
		if (clanlevel1 != _o_.clanlevel1) return false;
		if (clanlevel2 != _o_.clanlevel2) return false;
		if (clan1scroe != _o_.clan1scroe) return false;
		if (clan2scroe != _o_.clan2scroe) return false;
		if (winner != _o_.winner) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += clan1;
		_h_ += clanindex1;
		_h_ += clanname1.hashCode();
		_h_ += clan2;
		_h_ += clanindex2;
		_h_ += clanname2.hashCode();
		_h_ += clanlevel1;
		_h_ += clanlevel2;
		_h_ += clan1scroe;
		_h_ += clan2scroe;
		_h_ += winner;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clan1);
		_sb_.append(",");
		_sb_.append(clanindex1);
		_sb_.append(",");
		_sb_.append("'").append(clanname1).append("'");
		_sb_.append(",");
		_sb_.append(clan2);
		_sb_.append(",");
		_sb_.append(clanindex2);
		_sb_.append(",");
		_sb_.append("'").append(clanname2).append("'");
		_sb_.append(",");
		_sb_.append(clanlevel1);
		_sb_.append(",");
		_sb_.append(clanlevel2);
		_sb_.append(",");
		_sb_.append(clan1scroe);
		_sb_.append(",");
		_sb_.append(clan2scroe);
		_sb_.append(",");
		_sb_.append(winner);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clan1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanindex1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanname1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clan2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanindex2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanname2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanlevel1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanlevel2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clan1scroe"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clan2scroe"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("winner"));
		return lb;
	}

	private class Const implements xbean.ClanFight {
		ClanFight nThis() {
			return ClanFight.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanFight copy() {
			return ClanFight.this.copy();
		}

		@Override
		public xbean.ClanFight toData() {
			return ClanFight.this.toData();
		}

		public xbean.ClanFight toBean() {
			return ClanFight.this.toBean();
		}

		@Override
		public xbean.ClanFight toDataIf() {
			return ClanFight.this.toDataIf();
		}

		public xbean.ClanFight toBeanIf() {
			return ClanFight.this.toBeanIf();
		}

		@Override
		public long getClan1() { // 第一个公会 by changhao
			_xdb_verify_unsafe_();
			return clan1;
		}

		@Override
		public int getClanindex1() { // 第一个公会 by changhao
			_xdb_verify_unsafe_();
			return clanindex1;
		}

		@Override
		public String getClanname1() { // 第一个公会名字 by changhao
			_xdb_verify_unsafe_();
			return clanname1;
		}

		@Override
		public com.locojoy.base.Octets getClanname1Octets() { // 第一个公会名字 by changhao
			_xdb_verify_unsafe_();
			return ClanFight.this.getClanname1Octets();
		}

		@Override
		public long getClan2() { // 第二个公会 by changhao
			_xdb_verify_unsafe_();
			return clan2;
		}

		@Override
		public int getClanindex2() { // 第二个公会 by changhao
			_xdb_verify_unsafe_();
			return clanindex2;
		}

		@Override
		public String getClanname2() { // 第二个公会名字 by changhao
			_xdb_verify_unsafe_();
			return clanname2;
		}

		@Override
		public com.locojoy.base.Octets getClanname2Octets() { // 第二个公会名字 by changhao
			_xdb_verify_unsafe_();
			return ClanFight.this.getClanname2Octets();
		}

		@Override
		public int getClanlevel1() { // 第一个公会等级 by changhao
			_xdb_verify_unsafe_();
			return clanlevel1;
		}

		@Override
		public int getClanlevel2() { // 第二个公会等级 by changhao
			_xdb_verify_unsafe_();
			return clanlevel2;
		}

		@Override
		public int getClan1scroe() { // clan1积分 by changhao
			_xdb_verify_unsafe_();
			return clan1scroe;
		}

		@Override
		public int getClan2scroe() { // clan2积分 by changhao
			_xdb_verify_unsafe_();
			return clan2scroe;
		}

		@Override
		public int getWinner() { // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao
			_xdb_verify_unsafe_();
			return winner;
		}

		@Override
		public void setClan1(long _v_) { // 第一个公会 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanindex1(int _v_) { // 第一个公会 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname1(String _v_) { // 第一个公会名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname1Octets(com.locojoy.base.Octets _v_) { // 第一个公会名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClan2(long _v_) { // 第二个公会 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanindex2(int _v_) { // 第二个公会 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname2(String _v_) { // 第二个公会名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname2Octets(com.locojoy.base.Octets _v_) { // 第二个公会名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanlevel1(int _v_) { // 第一个公会等级 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanlevel2(int _v_) { // 第二个公会等级 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClan1scroe(int _v_) { // clan1积分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClan2scroe(int _v_) { // clan2积分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWinner(int _v_) { // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean toConst() {
			_xdb_verify_unsafe_();
			return this;
		}

		@Override
		public boolean isConst() {
			_xdb_verify_unsafe_();
			return true;
		}

		@Override
		public boolean isData() {
			return ClanFight.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanFight.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanFight.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanFight.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanFight.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanFight.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanFight.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanFight.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanFight.this.toString();
		}

	}

	public static final class Data implements xbean.ClanFight {
		private long clan1; // 第一个公会 by changhao
		private int clanindex1; // 第一个公会 by changhao
		private String clanname1; // 第一个公会名字 by changhao
		private long clan2; // 第二个公会 by changhao
		private int clanindex2; // 第二个公会 by changhao
		private String clanname2; // 第二个公会名字 by changhao
		private int clanlevel1; // 第一个公会等级 by changhao
		private int clanlevel2; // 第二个公会等级 by changhao
		private int clan1scroe; // clan1积分 by changhao
		private int clan2scroe; // clan2积分 by changhao
		private int winner; // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			clanname1 = "";
			clanname2 = "";
		}

		Data(xbean.ClanFight _o1_) {
			if (_o1_ instanceof ClanFight) assign((ClanFight)_o1_);
			else if (_o1_ instanceof ClanFight.Data) assign((ClanFight.Data)_o1_);
			else if (_o1_ instanceof ClanFight.Const) assign(((ClanFight.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanFight _o_) {
			clan1 = _o_.clan1;
			clanindex1 = _o_.clanindex1;
			clanname1 = _o_.clanname1;
			clan2 = _o_.clan2;
			clanindex2 = _o_.clanindex2;
			clanname2 = _o_.clanname2;
			clanlevel1 = _o_.clanlevel1;
			clanlevel2 = _o_.clanlevel2;
			clan1scroe = _o_.clan1scroe;
			clan2scroe = _o_.clan2scroe;
			winner = _o_.winner;
		}

		private void assign(ClanFight.Data _o_) {
			clan1 = _o_.clan1;
			clanindex1 = _o_.clanindex1;
			clanname1 = _o_.clanname1;
			clan2 = _o_.clan2;
			clanindex2 = _o_.clanindex2;
			clanname2 = _o_.clanname2;
			clanlevel1 = _o_.clanlevel1;
			clanlevel2 = _o_.clanlevel2;
			clan1scroe = _o_.clan1scroe;
			clan2scroe = _o_.clan2scroe;
			winner = _o_.winner;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(clan1);
			_os_.marshal(clanindex1);
			_os_.marshal(clanname1, mkdb.Const.IO_CHARSET);
			_os_.marshal(clan2);
			_os_.marshal(clanindex2);
			_os_.marshal(clanname2, mkdb.Const.IO_CHARSET);
			_os_.marshal(clanlevel1);
			_os_.marshal(clanlevel2);
			_os_.marshal(clan1scroe);
			_os_.marshal(clan2scroe);
			_os_.marshal(winner);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			clan1 = _os_.unmarshal_long();
			clanindex1 = _os_.unmarshal_int();
			clanname1 = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			clan2 = _os_.unmarshal_long();
			clanindex2 = _os_.unmarshal_int();
			clanname2 = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			clanlevel1 = _os_.unmarshal_int();
			clanlevel2 = _os_.unmarshal_int();
			clan1scroe = _os_.unmarshal_int();
			clan2scroe = _os_.unmarshal_int();
			winner = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ClanFight copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanFight toData() {
			return new Data(this);
		}

		public xbean.ClanFight toBean() {
			return new ClanFight(this, null, null);
		}

		@Override
		public xbean.ClanFight toDataIf() {
			return this;
		}

		public xbean.ClanFight toBeanIf() {
			return new ClanFight(this, null, null);
		}

		// mkdb.Bean interface. Data Unsupported
		public boolean xdbManaged() { throw new UnsupportedOperationException(); }
		public mkdb.Bean xdbParent() { throw new UnsupportedOperationException(); }
		public String xdbVarname()  { throw new UnsupportedOperationException(); }
		public Long    xdbObjId()   { throw new UnsupportedOperationException(); }
		public mkdb.Bean toConst()   { throw new UnsupportedOperationException(); }
		public boolean isConst()    { return false; }
		public boolean isData()     { return true; }

		@Override
		public long getClan1() { // 第一个公会 by changhao
			return clan1;
		}

		@Override
		public int getClanindex1() { // 第一个公会 by changhao
			return clanindex1;
		}

		@Override
		public String getClanname1() { // 第一个公会名字 by changhao
			return clanname1;
		}

		@Override
		public com.locojoy.base.Octets getClanname1Octets() { // 第一个公会名字 by changhao
			return com.locojoy.base.Octets.wrap(getClanname1(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getClan2() { // 第二个公会 by changhao
			return clan2;
		}

		@Override
		public int getClanindex2() { // 第二个公会 by changhao
			return clanindex2;
		}

		@Override
		public String getClanname2() { // 第二个公会名字 by changhao
			return clanname2;
		}

		@Override
		public com.locojoy.base.Octets getClanname2Octets() { // 第二个公会名字 by changhao
			return com.locojoy.base.Octets.wrap(getClanname2(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getClanlevel1() { // 第一个公会等级 by changhao
			return clanlevel1;
		}

		@Override
		public int getClanlevel2() { // 第二个公会等级 by changhao
			return clanlevel2;
		}

		@Override
		public int getClan1scroe() { // clan1积分 by changhao
			return clan1scroe;
		}

		@Override
		public int getClan2scroe() { // clan2积分 by changhao
			return clan2scroe;
		}

		@Override
		public int getWinner() { // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao
			return winner;
		}

		@Override
		public void setClan1(long _v_) { // 第一个公会 by changhao
			clan1 = _v_;
		}

		@Override
		public void setClanindex1(int _v_) { // 第一个公会 by changhao
			clanindex1 = _v_;
		}

		@Override
		public void setClanname1(String _v_) { // 第一个公会名字 by changhao
			if (null == _v_)
				throw new NullPointerException();
			clanname1 = _v_;
		}

		@Override
		public void setClanname1Octets(com.locojoy.base.Octets _v_) { // 第一个公会名字 by changhao
			this.setClanname1(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setClan2(long _v_) { // 第二个公会 by changhao
			clan2 = _v_;
		}

		@Override
		public void setClanindex2(int _v_) { // 第二个公会 by changhao
			clanindex2 = _v_;
		}

		@Override
		public void setClanname2(String _v_) { // 第二个公会名字 by changhao
			if (null == _v_)
				throw new NullPointerException();
			clanname2 = _v_;
		}

		@Override
		public void setClanname2Octets(com.locojoy.base.Octets _v_) { // 第二个公会名字 by changhao
			this.setClanname2(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setClanlevel1(int _v_) { // 第一个公会等级 by changhao
			clanlevel1 = _v_;
		}

		@Override
		public void setClanlevel2(int _v_) { // 第二个公会等级 by changhao
			clanlevel2 = _v_;
		}

		@Override
		public void setClan1scroe(int _v_) { // clan1积分 by changhao
			clan1scroe = _v_;
		}

		@Override
		public void setClan2scroe(int _v_) { // clan2积分 by changhao
			clan2scroe = _v_;
		}

		@Override
		public void setWinner(int _v_) { // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao
			winner = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanFight.Data)) return false;
			ClanFight.Data _o_ = (ClanFight.Data) _o1_;
			if (clan1 != _o_.clan1) return false;
			if (clanindex1 != _o_.clanindex1) return false;
			if (!clanname1.equals(_o_.clanname1)) return false;
			if (clan2 != _o_.clan2) return false;
			if (clanindex2 != _o_.clanindex2) return false;
			if (!clanname2.equals(_o_.clanname2)) return false;
			if (clanlevel1 != _o_.clanlevel1) return false;
			if (clanlevel2 != _o_.clanlevel2) return false;
			if (clan1scroe != _o_.clan1scroe) return false;
			if (clan2scroe != _o_.clan2scroe) return false;
			if (winner != _o_.winner) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += clan1;
			_h_ += clanindex1;
			_h_ += clanname1.hashCode();
			_h_ += clan2;
			_h_ += clanindex2;
			_h_ += clanname2.hashCode();
			_h_ += clanlevel1;
			_h_ += clanlevel2;
			_h_ += clan1scroe;
			_h_ += clan2scroe;
			_h_ += winner;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(clan1);
			_sb_.append(",");
			_sb_.append(clanindex1);
			_sb_.append(",");
			_sb_.append("'").append(clanname1).append("'");
			_sb_.append(",");
			_sb_.append(clan2);
			_sb_.append(",");
			_sb_.append(clanindex2);
			_sb_.append(",");
			_sb_.append("'").append(clanname2).append("'");
			_sb_.append(",");
			_sb_.append(clanlevel1);
			_sb_.append(",");
			_sb_.append(clanlevel2);
			_sb_.append(",");
			_sb_.append(clan1scroe);
			_sb_.append(",");
			_sb_.append(clan2scroe);
			_sb_.append(",");
			_sb_.append(winner);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
