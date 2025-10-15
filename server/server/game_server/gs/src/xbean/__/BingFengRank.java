
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BingFengRank extends mkdb.XBean implements xbean.BingFengRank {
	private java.util.HashMap<Integer, xbean.BingFengRankList> ranks; // key为职业id

	@Override
	public void _reset_unsafe_() {
		ranks.clear();
	}

	BingFengRank(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>();
	}

	public BingFengRank() {
		this(0, null, null);
	}

	public BingFengRank(BingFengRank _o_) {
		this(_o_, null, null);
	}

	BingFengRank(xbean.BingFengRank _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BingFengRank) assign((BingFengRank)_o1_);
		else if (_o1_ instanceof BingFengRank.Data) assign((BingFengRank.Data)_o1_);
		else if (_o1_ instanceof BingFengRank.Const) assign(((BingFengRank.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BingFengRank _o_) {
		_o_._xdb_verify_unsafe_();
		ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>();
		for (java.util.Map.Entry<Integer, xbean.BingFengRankList> _e_ : _o_.ranks.entrySet())
			ranks.put(_e_.getKey(), new BingFengRankList(_e_.getValue(), this, "ranks"));
	}

	private void assign(BingFengRank.Data _o_) {
		ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>();
		for (java.util.Map.Entry<Integer, xbean.BingFengRankList> _e_ : _o_.ranks.entrySet())
			ranks.put(_e_.getKey(), new BingFengRankList(_e_.getValue(), this, "ranks"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(ranks.size());
		for (java.util.Map.Entry<Integer, xbean.BingFengRankList> _e_ : ranks.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.BingFengRankList _v_ = new BingFengRankList(0, this, "ranks");
				_v_.unmarshal(_os_);
				ranks.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.BingFengRank copy() {
		_xdb_verify_unsafe_();
		return new BingFengRank(this);
	}

	@Override
	public xbean.BingFengRank toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengRank toBean() {
		_xdb_verify_unsafe_();
		return new BingFengRank(this); // same as copy()
	}

	@Override
	public xbean.BingFengRank toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengRank toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.BingFengRankList> getRanks() { // key为职业id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "ranks"), ranks);
	}

	@Override
	public java.util.Map<Integer, xbean.BingFengRankList> getRanksAsData() { // key为职业id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.BingFengRankList> ranks;
		BingFengRank _o_ = this;
		ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>();
		for (java.util.Map.Entry<Integer, xbean.BingFengRankList> _e_ : _o_.ranks.entrySet())
			ranks.put(_e_.getKey(), new BingFengRankList.Data(_e_.getValue()));
		return ranks;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BingFengRank _o_ = null;
		if ( _o1_ instanceof BingFengRank ) _o_ = (BingFengRank)_o1_;
		else if ( _o1_ instanceof BingFengRank.Const ) _o_ = ((BingFengRank.Const)_o1_).nThis();
		else return false;
		if (!ranks.equals(_o_.ranks)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += ranks.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ranks);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("ranks"));
		return lb;
	}

	private class Const implements xbean.BingFengRank {
		BingFengRank nThis() {
			return BingFengRank.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BingFengRank copy() {
			return BingFengRank.this.copy();
		}

		@Override
		public xbean.BingFengRank toData() {
			return BingFengRank.this.toData();
		}

		public xbean.BingFengRank toBean() {
			return BingFengRank.this.toBean();
		}

		@Override
		public xbean.BingFengRank toDataIf() {
			return BingFengRank.this.toDataIf();
		}

		public xbean.BingFengRank toBeanIf() {
			return BingFengRank.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.BingFengRankList> getRanks() { // key为职业id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(ranks);
		}

		@Override
		public java.util.Map<Integer, xbean.BingFengRankList> getRanksAsData() { // key为职业id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.BingFengRankList> ranks;
			BingFengRank _o_ = BingFengRank.this;
			ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>();
			for (java.util.Map.Entry<Integer, xbean.BingFengRankList> _e_ : _o_.ranks.entrySet())
				ranks.put(_e_.getKey(), new BingFengRankList.Data(_e_.getValue()));
			return ranks;
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
			return BingFengRank.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BingFengRank.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BingFengRank.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BingFengRank.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BingFengRank.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BingFengRank.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BingFengRank.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BingFengRank.this.hashCode();
		}

		@Override
		public String toString() {
			return BingFengRank.this.toString();
		}

	}

	public static final class Data implements xbean.BingFengRank {
		private java.util.HashMap<Integer, xbean.BingFengRankList> ranks; // key为职业id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>();
		}

		Data(xbean.BingFengRank _o1_) {
			if (_o1_ instanceof BingFengRank) assign((BingFengRank)_o1_);
			else if (_o1_ instanceof BingFengRank.Data) assign((BingFengRank.Data)_o1_);
			else if (_o1_ instanceof BingFengRank.Const) assign(((BingFengRank.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BingFengRank _o_) {
			ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>();
			for (java.util.Map.Entry<Integer, xbean.BingFengRankList> _e_ : _o_.ranks.entrySet())
				ranks.put(_e_.getKey(), new BingFengRankList.Data(_e_.getValue()));
		}

		private void assign(BingFengRank.Data _o_) {
			ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>();
			for (java.util.Map.Entry<Integer, xbean.BingFengRankList> _e_ : _o_.ranks.entrySet())
				ranks.put(_e_.getKey(), new BingFengRankList.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(ranks.size());
			for (java.util.Map.Entry<Integer, xbean.BingFengRankList> _e_ : ranks.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					ranks = new java.util.HashMap<Integer, xbean.BingFengRankList>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.BingFengRankList _v_ = xbean.Pod.newBingFengRankListData();
					_v_.unmarshal(_os_);
					ranks.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.BingFengRank copy() {
			return new Data(this);
		}

		@Override
		public xbean.BingFengRank toData() {
			return new Data(this);
		}

		public xbean.BingFengRank toBean() {
			return new BingFengRank(this, null, null);
		}

		@Override
		public xbean.BingFengRank toDataIf() {
			return this;
		}

		public xbean.BingFengRank toBeanIf() {
			return new BingFengRank(this, null, null);
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
		public java.util.Map<Integer, xbean.BingFengRankList> getRanks() { // key为职业id
			return ranks;
		}

		@Override
		public java.util.Map<Integer, xbean.BingFengRankList> getRanksAsData() { // key为职业id
			return ranks;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BingFengRank.Data)) return false;
			BingFengRank.Data _o_ = (BingFengRank.Data) _o1_;
			if (!ranks.equals(_o_.ranks)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += ranks.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(ranks);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
