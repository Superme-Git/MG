
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WeiBoNotify extends mkdb.XBean implements xbean.WeiBoNotify {
	private java.util.HashMap<Integer, xbean.WeiBoChoice> weibo; // key=类型 value=微博相关数据
	private int takeawardflag; // 1=可以领奖  2=领取过了

	@Override
	public void _reset_unsafe_() {
		weibo.clear();
		takeawardflag = 0;
	}

	WeiBoNotify(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>();
	}

	public WeiBoNotify() {
		this(0, null, null);
	}

	public WeiBoNotify(WeiBoNotify _o_) {
		this(_o_, null, null);
	}

	WeiBoNotify(xbean.WeiBoNotify _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WeiBoNotify) assign((WeiBoNotify)_o1_);
		else if (_o1_ instanceof WeiBoNotify.Data) assign((WeiBoNotify.Data)_o1_);
		else if (_o1_ instanceof WeiBoNotify.Const) assign(((WeiBoNotify.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WeiBoNotify _o_) {
		_o_._xdb_verify_unsafe_();
		weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>();
		for (java.util.Map.Entry<Integer, xbean.WeiBoChoice> _e_ : _o_.weibo.entrySet())
			weibo.put(_e_.getKey(), new WeiBoChoice(_e_.getValue(), this, "weibo"));
		takeawardflag = _o_.takeawardflag;
	}

	private void assign(WeiBoNotify.Data _o_) {
		weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>();
		for (java.util.Map.Entry<Integer, xbean.WeiBoChoice> _e_ : _o_.weibo.entrySet())
			weibo.put(_e_.getKey(), new WeiBoChoice(_e_.getValue(), this, "weibo"));
		takeawardflag = _o_.takeawardflag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(weibo.size());
		for (java.util.Map.Entry<Integer, xbean.WeiBoChoice> _e_ : weibo.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(takeawardflag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.WeiBoChoice _v_ = new WeiBoChoice(0, this, "weibo");
				_v_.unmarshal(_os_);
				weibo.put(_k_, _v_);
			}
		}
		takeawardflag = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.WeiBoNotify copy() {
		_xdb_verify_unsafe_();
		return new WeiBoNotify(this);
	}

	@Override
	public xbean.WeiBoNotify toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WeiBoNotify toBean() {
		_xdb_verify_unsafe_();
		return new WeiBoNotify(this); // same as copy()
	}

	@Override
	public xbean.WeiBoNotify toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WeiBoNotify toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.WeiBoChoice> getWeibo() { // key=类型 value=微博相关数据
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "weibo"), weibo);
	}

	@Override
	public java.util.Map<Integer, xbean.WeiBoChoice> getWeiboAsData() { // key=类型 value=微博相关数据
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.WeiBoChoice> weibo;
		WeiBoNotify _o_ = this;
		weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>();
		for (java.util.Map.Entry<Integer, xbean.WeiBoChoice> _e_ : _o_.weibo.entrySet())
			weibo.put(_e_.getKey(), new WeiBoChoice.Data(_e_.getValue()));
		return weibo;
	}

	@Override
	public int getTakeawardflag() { // 1=可以领奖  2=领取过了
		_xdb_verify_unsafe_();
		return takeawardflag;
	}

	@Override
	public void setTakeawardflag(int _v_) { // 1=可以领奖  2=领取过了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "takeawardflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, takeawardflag) {
					public void rollback() { takeawardflag = _xdb_saved; }
				};}});
		takeawardflag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WeiBoNotify _o_ = null;
		if ( _o1_ instanceof WeiBoNotify ) _o_ = (WeiBoNotify)_o1_;
		else if ( _o1_ instanceof WeiBoNotify.Const ) _o_ = ((WeiBoNotify.Const)_o1_).nThis();
		else return false;
		if (!weibo.equals(_o_.weibo)) return false;
		if (takeawardflag != _o_.takeawardflag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += weibo.hashCode();
		_h_ += takeawardflag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(weibo);
		_sb_.append(",");
		_sb_.append(takeawardflag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("weibo"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("takeawardflag"));
		return lb;
	}

	private class Const implements xbean.WeiBoNotify {
		WeiBoNotify nThis() {
			return WeiBoNotify.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WeiBoNotify copy() {
			return WeiBoNotify.this.copy();
		}

		@Override
		public xbean.WeiBoNotify toData() {
			return WeiBoNotify.this.toData();
		}

		public xbean.WeiBoNotify toBean() {
			return WeiBoNotify.this.toBean();
		}

		@Override
		public xbean.WeiBoNotify toDataIf() {
			return WeiBoNotify.this.toDataIf();
		}

		public xbean.WeiBoNotify toBeanIf() {
			return WeiBoNotify.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.WeiBoChoice> getWeibo() { // key=类型 value=微博相关数据
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(weibo);
		}

		@Override
		public java.util.Map<Integer, xbean.WeiBoChoice> getWeiboAsData() { // key=类型 value=微博相关数据
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.WeiBoChoice> weibo;
			WeiBoNotify _o_ = WeiBoNotify.this;
			weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>();
			for (java.util.Map.Entry<Integer, xbean.WeiBoChoice> _e_ : _o_.weibo.entrySet())
				weibo.put(_e_.getKey(), new WeiBoChoice.Data(_e_.getValue()));
			return weibo;
		}

		@Override
		public int getTakeawardflag() { // 1=可以领奖  2=领取过了
			_xdb_verify_unsafe_();
			return takeawardflag;
		}

		@Override
		public void setTakeawardflag(int _v_) { // 1=可以领奖  2=领取过了
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
			return WeiBoNotify.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WeiBoNotify.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WeiBoNotify.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WeiBoNotify.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WeiBoNotify.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WeiBoNotify.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WeiBoNotify.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WeiBoNotify.this.hashCode();
		}

		@Override
		public String toString() {
			return WeiBoNotify.this.toString();
		}

	}

	public static final class Data implements xbean.WeiBoNotify {
		private java.util.HashMap<Integer, xbean.WeiBoChoice> weibo; // key=类型 value=微博相关数据
		private int takeawardflag; // 1=可以领奖  2=领取过了

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>();
		}

		Data(xbean.WeiBoNotify _o1_) {
			if (_o1_ instanceof WeiBoNotify) assign((WeiBoNotify)_o1_);
			else if (_o1_ instanceof WeiBoNotify.Data) assign((WeiBoNotify.Data)_o1_);
			else if (_o1_ instanceof WeiBoNotify.Const) assign(((WeiBoNotify.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WeiBoNotify _o_) {
			weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>();
			for (java.util.Map.Entry<Integer, xbean.WeiBoChoice> _e_ : _o_.weibo.entrySet())
				weibo.put(_e_.getKey(), new WeiBoChoice.Data(_e_.getValue()));
			takeawardflag = _o_.takeawardflag;
		}

		private void assign(WeiBoNotify.Data _o_) {
			weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>();
			for (java.util.Map.Entry<Integer, xbean.WeiBoChoice> _e_ : _o_.weibo.entrySet())
				weibo.put(_e_.getKey(), new WeiBoChoice.Data(_e_.getValue()));
			takeawardflag = _o_.takeawardflag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(weibo.size());
			for (java.util.Map.Entry<Integer, xbean.WeiBoChoice> _e_ : weibo.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(takeawardflag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					weibo = new java.util.HashMap<Integer, xbean.WeiBoChoice>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.WeiBoChoice _v_ = xbean.Pod.newWeiBoChoiceData();
					_v_.unmarshal(_os_);
					weibo.put(_k_, _v_);
				}
			}
			takeawardflag = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.WeiBoNotify copy() {
			return new Data(this);
		}

		@Override
		public xbean.WeiBoNotify toData() {
			return new Data(this);
		}

		public xbean.WeiBoNotify toBean() {
			return new WeiBoNotify(this, null, null);
		}

		@Override
		public xbean.WeiBoNotify toDataIf() {
			return this;
		}

		public xbean.WeiBoNotify toBeanIf() {
			return new WeiBoNotify(this, null, null);
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
		public java.util.Map<Integer, xbean.WeiBoChoice> getWeibo() { // key=类型 value=微博相关数据
			return weibo;
		}

		@Override
		public java.util.Map<Integer, xbean.WeiBoChoice> getWeiboAsData() { // key=类型 value=微博相关数据
			return weibo;
		}

		@Override
		public int getTakeawardflag() { // 1=可以领奖  2=领取过了
			return takeawardflag;
		}

		@Override
		public void setTakeawardflag(int _v_) { // 1=可以领奖  2=领取过了
			takeawardflag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WeiBoNotify.Data)) return false;
			WeiBoNotify.Data _o_ = (WeiBoNotify.Data) _o1_;
			if (!weibo.equals(_o_.weibo)) return false;
			if (takeawardflag != _o_.takeawardflag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += weibo.hashCode();
			_h_ += takeawardflag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(weibo);
			_sb_.append(",");
			_sb_.append(takeawardflag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
