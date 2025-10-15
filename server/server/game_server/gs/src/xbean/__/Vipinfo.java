
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Vipinfo extends mkdb.XBean implements xbean.Vipinfo {
	private int vipexp; // 已充符石数量
	private int viplevel; // vip等级
	private int bonus; // 可领奖励
	private int gotbonus; // 已领奖励

	@Override
	public void _reset_unsafe_() {
		vipexp = 0;
		viplevel = 0;
		bonus = 0;
		gotbonus = 0;
	}

	Vipinfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		vipexp = 0;
		viplevel = 0;
		bonus = 0;
		gotbonus = 0;
	}

	public Vipinfo() {
		this(0, null, null);
	}

	public Vipinfo(Vipinfo _o_) {
		this(_o_, null, null);
	}

	Vipinfo(xbean.Vipinfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Vipinfo) assign((Vipinfo)_o1_);
		else if (_o1_ instanceof Vipinfo.Data) assign((Vipinfo.Data)_o1_);
		else if (_o1_ instanceof Vipinfo.Const) assign(((Vipinfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Vipinfo _o_) {
		_o_._xdb_verify_unsafe_();
		vipexp = _o_.vipexp;
		viplevel = _o_.viplevel;
		bonus = _o_.bonus;
		gotbonus = _o_.gotbonus;
	}

	private void assign(Vipinfo.Data _o_) {
		vipexp = _o_.vipexp;
		viplevel = _o_.viplevel;
		bonus = _o_.bonus;
		gotbonus = _o_.gotbonus;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(vipexp);
		_os_.marshal(viplevel);
		_os_.marshal(bonus);
		_os_.marshal(gotbonus);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		vipexp = _os_.unmarshal_int();
		viplevel = _os_.unmarshal_int();
		bonus = _os_.unmarshal_int();
		gotbonus = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.Vipinfo copy() {
		_xdb_verify_unsafe_();
		return new Vipinfo(this);
	}

	@Override
	public xbean.Vipinfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Vipinfo toBean() {
		_xdb_verify_unsafe_();
		return new Vipinfo(this); // same as copy()
	}

	@Override
	public xbean.Vipinfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Vipinfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getVipexp() { // 已充符石数量
		_xdb_verify_unsafe_();
		return vipexp;
	}

	@Override
	public int getViplevel() { // vip等级
		_xdb_verify_unsafe_();
		return viplevel;
	}

	@Override
	public int getBonus() { // 可领奖励
		_xdb_verify_unsafe_();
		return bonus;
	}

	@Override
	public int getGotbonus() { // 已领奖励
		_xdb_verify_unsafe_();
		return gotbonus;
	}

	@Override
	public void setVipexp(int _v_) { // 已充符石数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "vipexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, vipexp) {
					public void rollback() { vipexp = _xdb_saved; }
				};}});
		vipexp = _v_;
	}

	@Override
	public void setViplevel(int _v_) { // vip等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "viplevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, viplevel) {
					public void rollback() { viplevel = _xdb_saved; }
				};}});
		viplevel = _v_;
	}

	@Override
	public void setBonus(int _v_) { // 可领奖励
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bonus") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bonus) {
					public void rollback() { bonus = _xdb_saved; }
				};}});
		bonus = _v_;
	}

	@Override
	public void setGotbonus(int _v_) { // 已领奖励
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "gotbonus") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, gotbonus) {
					public void rollback() { gotbonus = _xdb_saved; }
				};}});
		gotbonus = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Vipinfo _o_ = null;
		if ( _o1_ instanceof Vipinfo ) _o_ = (Vipinfo)_o1_;
		else if ( _o1_ instanceof Vipinfo.Const ) _o_ = ((Vipinfo.Const)_o1_).nThis();
		else return false;
		if (vipexp != _o_.vipexp) return false;
		if (viplevel != _o_.viplevel) return false;
		if (bonus != _o_.bonus) return false;
		if (gotbonus != _o_.gotbonus) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += vipexp;
		_h_ += viplevel;
		_h_ += bonus;
		_h_ += gotbonus;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(vipexp);
		_sb_.append(",");
		_sb_.append(viplevel);
		_sb_.append(",");
		_sb_.append(bonus);
		_sb_.append(",");
		_sb_.append(gotbonus);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("vipexp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("viplevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bonus"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("gotbonus"));
		return lb;
	}

	private class Const implements xbean.Vipinfo {
		Vipinfo nThis() {
			return Vipinfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Vipinfo copy() {
			return Vipinfo.this.copy();
		}

		@Override
		public xbean.Vipinfo toData() {
			return Vipinfo.this.toData();
		}

		public xbean.Vipinfo toBean() {
			return Vipinfo.this.toBean();
		}

		@Override
		public xbean.Vipinfo toDataIf() {
			return Vipinfo.this.toDataIf();
		}

		public xbean.Vipinfo toBeanIf() {
			return Vipinfo.this.toBeanIf();
		}

		@Override
		public int getVipexp() { // 已充符石数量
			_xdb_verify_unsafe_();
			return vipexp;
		}

		@Override
		public int getViplevel() { // vip等级
			_xdb_verify_unsafe_();
			return viplevel;
		}

		@Override
		public int getBonus() { // 可领奖励
			_xdb_verify_unsafe_();
			return bonus;
		}

		@Override
		public int getGotbonus() { // 已领奖励
			_xdb_verify_unsafe_();
			return gotbonus;
		}

		@Override
		public void setVipexp(int _v_) { // 已充符石数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setViplevel(int _v_) { // vip等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBonus(int _v_) { // 可领奖励
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGotbonus(int _v_) { // 已领奖励
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
			return Vipinfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Vipinfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Vipinfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Vipinfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Vipinfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Vipinfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Vipinfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Vipinfo.this.hashCode();
		}

		@Override
		public String toString() {
			return Vipinfo.this.toString();
		}

	}

	public static final class Data implements xbean.Vipinfo {
		private int vipexp; // 已充符石数量
		private int viplevel; // vip等级
		private int bonus; // 可领奖励
		private int gotbonus; // 已领奖励

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			vipexp = 0;
			viplevel = 0;
			bonus = 0;
			gotbonus = 0;
		}

		Data(xbean.Vipinfo _o1_) {
			if (_o1_ instanceof Vipinfo) assign((Vipinfo)_o1_);
			else if (_o1_ instanceof Vipinfo.Data) assign((Vipinfo.Data)_o1_);
			else if (_o1_ instanceof Vipinfo.Const) assign(((Vipinfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Vipinfo _o_) {
			vipexp = _o_.vipexp;
			viplevel = _o_.viplevel;
			bonus = _o_.bonus;
			gotbonus = _o_.gotbonus;
		}

		private void assign(Vipinfo.Data _o_) {
			vipexp = _o_.vipexp;
			viplevel = _o_.viplevel;
			bonus = _o_.bonus;
			gotbonus = _o_.gotbonus;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(vipexp);
			_os_.marshal(viplevel);
			_os_.marshal(bonus);
			_os_.marshal(gotbonus);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			vipexp = _os_.unmarshal_int();
			viplevel = _os_.unmarshal_int();
			bonus = _os_.unmarshal_int();
			gotbonus = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.Vipinfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.Vipinfo toData() {
			return new Data(this);
		}

		public xbean.Vipinfo toBean() {
			return new Vipinfo(this, null, null);
		}

		@Override
		public xbean.Vipinfo toDataIf() {
			return this;
		}

		public xbean.Vipinfo toBeanIf() {
			return new Vipinfo(this, null, null);
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
		public int getVipexp() { // 已充符石数量
			return vipexp;
		}

		@Override
		public int getViplevel() { // vip等级
			return viplevel;
		}

		@Override
		public int getBonus() { // 可领奖励
			return bonus;
		}

		@Override
		public int getGotbonus() { // 已领奖励
			return gotbonus;
		}

		@Override
		public void setVipexp(int _v_) { // 已充符石数量
			vipexp = _v_;
		}

		@Override
		public void setViplevel(int _v_) { // vip等级
			viplevel = _v_;
		}

		@Override
		public void setBonus(int _v_) { // 可领奖励
			bonus = _v_;
		}

		@Override
		public void setGotbonus(int _v_) { // 已领奖励
			gotbonus = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Vipinfo.Data)) return false;
			Vipinfo.Data _o_ = (Vipinfo.Data) _o1_;
			if (vipexp != _o_.vipexp) return false;
			if (viplevel != _o_.viplevel) return false;
			if (bonus != _o_.bonus) return false;
			if (gotbonus != _o_.gotbonus) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += vipexp;
			_h_ += viplevel;
			_h_ += bonus;
			_h_ += gotbonus;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(vipexp);
			_sb_.append(",");
			_sb_.append(viplevel);
			_sb_.append(",");
			_sb_.append(bonus);
			_sb_.append(",");
			_sb_.append(gotbonus);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
