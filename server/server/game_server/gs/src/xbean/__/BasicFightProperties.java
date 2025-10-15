
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BasicFightProperties extends mkdb.XBean implements xbean.BasicFightProperties {
	private int str; // 力量
	private int iq; // 智力
	private int cons; // 体质
	private int endu; // 耐力
	private int agi; // 敏捷

	@Override
	public void _reset_unsafe_() {
		str = 0;
		iq = 0;
		cons = 0;
		endu = 0;
		agi = 0;
	}

	BasicFightProperties(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public BasicFightProperties() {
		this(0, null, null);
	}

	public BasicFightProperties(BasicFightProperties _o_) {
		this(_o_, null, null);
	}

	BasicFightProperties(xbean.BasicFightProperties _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BasicFightProperties) assign((BasicFightProperties)_o1_);
		else if (_o1_ instanceof BasicFightProperties.Data) assign((BasicFightProperties.Data)_o1_);
		else if (_o1_ instanceof BasicFightProperties.Const) assign(((BasicFightProperties.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BasicFightProperties _o_) {
		_o_._xdb_verify_unsafe_();
		str = _o_.str;
		iq = _o_.iq;
		cons = _o_.cons;
		endu = _o_.endu;
		agi = _o_.agi;
	}

	private void assign(BasicFightProperties.Data _o_) {
		str = _o_.str;
		iq = _o_.iq;
		cons = _o_.cons;
		endu = _o_.endu;
		agi = _o_.agi;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(str);
		_os_.marshal(iq);
		_os_.marshal(cons);
		_os_.marshal(endu);
		_os_.marshal(agi);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		str = _os_.unmarshal_int();
		iq = _os_.unmarshal_int();
		cons = _os_.unmarshal_int();
		endu = _os_.unmarshal_int();
		agi = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.BasicFightProperties copy() {
		_xdb_verify_unsafe_();
		return new BasicFightProperties(this);
	}

	@Override
	public xbean.BasicFightProperties toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BasicFightProperties toBean() {
		_xdb_verify_unsafe_();
		return new BasicFightProperties(this); // same as copy()
	}

	@Override
	public xbean.BasicFightProperties toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BasicFightProperties toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getStr() { // 力量
		_xdb_verify_unsafe_();
		return str;
	}

	@Override
	public int getIq() { // 智力
		_xdb_verify_unsafe_();
		return iq;
	}

	@Override
	public int getCons() { // 体质
		_xdb_verify_unsafe_();
		return cons;
	}

	@Override
	public int getEndu() { // 耐力
		_xdb_verify_unsafe_();
		return endu;
	}

	@Override
	public int getAgi() { // 敏捷
		_xdb_verify_unsafe_();
		return agi;
	}

	@Override
	public void setStr(int _v_) { // 力量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "str") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, str) {
					public void rollback() { str = _xdb_saved; }
				};}});
		str = _v_;
	}

	@Override
	public void setIq(int _v_) { // 智力
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "iq") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, iq) {
					public void rollback() { iq = _xdb_saved; }
				};}});
		iq = _v_;
	}

	@Override
	public void setCons(int _v_) { // 体质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "cons") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, cons) {
					public void rollback() { cons = _xdb_saved; }
				};}});
		cons = _v_;
	}

	@Override
	public void setEndu(int _v_) { // 耐力
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "endu") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, endu) {
					public void rollback() { endu = _xdb_saved; }
				};}});
		endu = _v_;
	}

	@Override
	public void setAgi(int _v_) { // 敏捷
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "agi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, agi) {
					public void rollback() { agi = _xdb_saved; }
				};}});
		agi = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BasicFightProperties _o_ = null;
		if ( _o1_ instanceof BasicFightProperties ) _o_ = (BasicFightProperties)_o1_;
		else if ( _o1_ instanceof BasicFightProperties.Const ) _o_ = ((BasicFightProperties.Const)_o1_).nThis();
		else return false;
		if (str != _o_.str) return false;
		if (iq != _o_.iq) return false;
		if (cons != _o_.cons) return false;
		if (endu != _o_.endu) return false;
		if (agi != _o_.agi) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += str;
		_h_ += iq;
		_h_ += cons;
		_h_ += endu;
		_h_ += agi;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(str);
		_sb_.append(",");
		_sb_.append(iq);
		_sb_.append(",");
		_sb_.append(cons);
		_sb_.append(",");
		_sb_.append(endu);
		_sb_.append(",");
		_sb_.append(agi);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("str"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("iq"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("cons"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endu"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("agi"));
		return lb;
	}

	private class Const implements xbean.BasicFightProperties {
		BasicFightProperties nThis() {
			return BasicFightProperties.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BasicFightProperties copy() {
			return BasicFightProperties.this.copy();
		}

		@Override
		public xbean.BasicFightProperties toData() {
			return BasicFightProperties.this.toData();
		}

		public xbean.BasicFightProperties toBean() {
			return BasicFightProperties.this.toBean();
		}

		@Override
		public xbean.BasicFightProperties toDataIf() {
			return BasicFightProperties.this.toDataIf();
		}

		public xbean.BasicFightProperties toBeanIf() {
			return BasicFightProperties.this.toBeanIf();
		}

		@Override
		public int getStr() { // 力量
			_xdb_verify_unsafe_();
			return str;
		}

		@Override
		public int getIq() { // 智力
			_xdb_verify_unsafe_();
			return iq;
		}

		@Override
		public int getCons() { // 体质
			_xdb_verify_unsafe_();
			return cons;
		}

		@Override
		public int getEndu() { // 耐力
			_xdb_verify_unsafe_();
			return endu;
		}

		@Override
		public int getAgi() { // 敏捷
			_xdb_verify_unsafe_();
			return agi;
		}

		@Override
		public void setStr(int _v_) { // 力量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIq(int _v_) { // 智力
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCons(int _v_) { // 体质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEndu(int _v_) { // 耐力
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAgi(int _v_) { // 敏捷
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
			return BasicFightProperties.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BasicFightProperties.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BasicFightProperties.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BasicFightProperties.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BasicFightProperties.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BasicFightProperties.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BasicFightProperties.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BasicFightProperties.this.hashCode();
		}

		@Override
		public String toString() {
			return BasicFightProperties.this.toString();
		}

	}

	public static final class Data implements xbean.BasicFightProperties {
		private int str; // 力量
		private int iq; // 智力
		private int cons; // 体质
		private int endu; // 耐力
		private int agi; // 敏捷

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.BasicFightProperties _o1_) {
			if (_o1_ instanceof BasicFightProperties) assign((BasicFightProperties)_o1_);
			else if (_o1_ instanceof BasicFightProperties.Data) assign((BasicFightProperties.Data)_o1_);
			else if (_o1_ instanceof BasicFightProperties.Const) assign(((BasicFightProperties.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BasicFightProperties _o_) {
			str = _o_.str;
			iq = _o_.iq;
			cons = _o_.cons;
			endu = _o_.endu;
			agi = _o_.agi;
		}

		private void assign(BasicFightProperties.Data _o_) {
			str = _o_.str;
			iq = _o_.iq;
			cons = _o_.cons;
			endu = _o_.endu;
			agi = _o_.agi;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(str);
			_os_.marshal(iq);
			_os_.marshal(cons);
			_os_.marshal(endu);
			_os_.marshal(agi);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			str = _os_.unmarshal_int();
			iq = _os_.unmarshal_int();
			cons = _os_.unmarshal_int();
			endu = _os_.unmarshal_int();
			agi = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.BasicFightProperties copy() {
			return new Data(this);
		}

		@Override
		public xbean.BasicFightProperties toData() {
			return new Data(this);
		}

		public xbean.BasicFightProperties toBean() {
			return new BasicFightProperties(this, null, null);
		}

		@Override
		public xbean.BasicFightProperties toDataIf() {
			return this;
		}

		public xbean.BasicFightProperties toBeanIf() {
			return new BasicFightProperties(this, null, null);
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
		public int getStr() { // 力量
			return str;
		}

		@Override
		public int getIq() { // 智力
			return iq;
		}

		@Override
		public int getCons() { // 体质
			return cons;
		}

		@Override
		public int getEndu() { // 耐力
			return endu;
		}

		@Override
		public int getAgi() { // 敏捷
			return agi;
		}

		@Override
		public void setStr(int _v_) { // 力量
			str = _v_;
		}

		@Override
		public void setIq(int _v_) { // 智力
			iq = _v_;
		}

		@Override
		public void setCons(int _v_) { // 体质
			cons = _v_;
		}

		@Override
		public void setEndu(int _v_) { // 耐力
			endu = _v_;
		}

		@Override
		public void setAgi(int _v_) { // 敏捷
			agi = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BasicFightProperties.Data)) return false;
			BasicFightProperties.Data _o_ = (BasicFightProperties.Data) _o1_;
			if (str != _o_.str) return false;
			if (iq != _o_.iq) return false;
			if (cons != _o_.cons) return false;
			if (endu != _o_.endu) return false;
			if (agi != _o_.agi) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += str;
			_h_ += iq;
			_h_ += cons;
			_h_ += endu;
			_h_ += agi;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(str);
			_sb_.append(",");
			_sb_.append(iq);
			_sb_.append(",");
			_sb_.append(cons);
			_sb_.append(",");
			_sb_.append(endu);
			_sb_.append(",");
			_sb_.append(agi);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
