
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AttentionGoods extends mkdb.XBean implements xbean.AttentionGoods {
	private java.util.ArrayList<xbean.AttentionGoodsBean> buyattentions; // 购买关注,数据库表中的id,关注数量最多8个
	private java.util.ArrayList<xbean.AttentionGoodsBean> publicityattentions; // 公示关注,数据库表中的id,关注数量最多8个

	@Override
	public void _reset_unsafe_() {
		buyattentions.clear();
		publicityattentions.clear();
	}

	AttentionGoods(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		buyattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		publicityattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
	}

	public AttentionGoods() {
		this(0, null, null);
	}

	public AttentionGoods(AttentionGoods _o_) {
		this(_o_, null, null);
	}

	AttentionGoods(xbean.AttentionGoods _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AttentionGoods) assign((AttentionGoods)_o1_);
		else if (_o1_ instanceof AttentionGoods.Data) assign((AttentionGoods.Data)_o1_);
		else if (_o1_ instanceof AttentionGoods.Const) assign(((AttentionGoods.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AttentionGoods _o_) {
		_o_._xdb_verify_unsafe_();
		buyattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		for (xbean.AttentionGoodsBean _v_ : _o_.buyattentions)
			buyattentions.add(new AttentionGoodsBean(_v_, this, "buyattentions"));
		publicityattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		for (xbean.AttentionGoodsBean _v_ : _o_.publicityattentions)
			publicityattentions.add(new AttentionGoodsBean(_v_, this, "publicityattentions"));
	}

	private void assign(AttentionGoods.Data _o_) {
		buyattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		for (xbean.AttentionGoodsBean _v_ : _o_.buyattentions)
			buyattentions.add(new AttentionGoodsBean(_v_, this, "buyattentions"));
		publicityattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		for (xbean.AttentionGoodsBean _v_ : _o_.publicityattentions)
			publicityattentions.add(new AttentionGoodsBean(_v_, this, "publicityattentions"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(buyattentions.size());
		for (xbean.AttentionGoodsBean _v_ : buyattentions) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(publicityattentions.size());
		for (xbean.AttentionGoodsBean _v_ : publicityattentions) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.AttentionGoodsBean _v_ = new AttentionGoodsBean(0, this, "buyattentions");
			_v_.unmarshal(_os_);
			buyattentions.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.AttentionGoodsBean _v_ = new AttentionGoodsBean(0, this, "publicityattentions");
			_v_.unmarshal(_os_);
			publicityattentions.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.AttentionGoods copy() {
		_xdb_verify_unsafe_();
		return new AttentionGoods(this);
	}

	@Override
	public xbean.AttentionGoods toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AttentionGoods toBean() {
		_xdb_verify_unsafe_();
		return new AttentionGoods(this); // same as copy()
	}

	@Override
	public xbean.AttentionGoods toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AttentionGoods toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.AttentionGoodsBean> getBuyattentions() { // 购买关注,数据库表中的id,关注数量最多8个
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "buyattentions"), buyattentions);
	}

	public java.util.List<xbean.AttentionGoodsBean> getBuyattentionsAsData() { // 购买关注,数据库表中的id,关注数量最多8个
		_xdb_verify_unsafe_();
		java.util.List<xbean.AttentionGoodsBean> buyattentions;
		AttentionGoods _o_ = this;
		buyattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		for (xbean.AttentionGoodsBean _v_ : _o_.buyattentions)
			buyattentions.add(new AttentionGoodsBean.Data(_v_));
		return buyattentions;
	}

	@Override
	public java.util.List<xbean.AttentionGoodsBean> getPublicityattentions() { // 公示关注,数据库表中的id,关注数量最多8个
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "publicityattentions"), publicityattentions);
	}

	public java.util.List<xbean.AttentionGoodsBean> getPublicityattentionsAsData() { // 公示关注,数据库表中的id,关注数量最多8个
		_xdb_verify_unsafe_();
		java.util.List<xbean.AttentionGoodsBean> publicityattentions;
		AttentionGoods _o_ = this;
		publicityattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		for (xbean.AttentionGoodsBean _v_ : _o_.publicityattentions)
			publicityattentions.add(new AttentionGoodsBean.Data(_v_));
		return publicityattentions;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AttentionGoods _o_ = null;
		if ( _o1_ instanceof AttentionGoods ) _o_ = (AttentionGoods)_o1_;
		else if ( _o1_ instanceof AttentionGoods.Const ) _o_ = ((AttentionGoods.Const)_o1_).nThis();
		else return false;
		if (!buyattentions.equals(_o_.buyattentions)) return false;
		if (!publicityattentions.equals(_o_.publicityattentions)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += buyattentions.hashCode();
		_h_ += publicityattentions.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buyattentions);
		_sb_.append(",");
		_sb_.append(publicityattentions);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buyattentions"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("publicityattentions"));
		return lb;
	}

	private class Const implements xbean.AttentionGoods {
		AttentionGoods nThis() {
			return AttentionGoods.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AttentionGoods copy() {
			return AttentionGoods.this.copy();
		}

		@Override
		public xbean.AttentionGoods toData() {
			return AttentionGoods.this.toData();
		}

		public xbean.AttentionGoods toBean() {
			return AttentionGoods.this.toBean();
		}

		@Override
		public xbean.AttentionGoods toDataIf() {
			return AttentionGoods.this.toDataIf();
		}

		public xbean.AttentionGoods toBeanIf() {
			return AttentionGoods.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.AttentionGoodsBean> getBuyattentions() { // 购买关注,数据库表中的id,关注数量最多8个
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(buyattentions);
		}

		public java.util.List<xbean.AttentionGoodsBean> getBuyattentionsAsData() { // 购买关注,数据库表中的id,关注数量最多8个
			_xdb_verify_unsafe_();
			java.util.List<xbean.AttentionGoodsBean> buyattentions;
			AttentionGoods _o_ = AttentionGoods.this;
		buyattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		for (xbean.AttentionGoodsBean _v_ : _o_.buyattentions)
			buyattentions.add(new AttentionGoodsBean.Data(_v_));
			return buyattentions;
		}

		@Override
		public java.util.List<xbean.AttentionGoodsBean> getPublicityattentions() { // 公示关注,数据库表中的id,关注数量最多8个
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(publicityattentions);
		}

		public java.util.List<xbean.AttentionGoodsBean> getPublicityattentionsAsData() { // 公示关注,数据库表中的id,关注数量最多8个
			_xdb_verify_unsafe_();
			java.util.List<xbean.AttentionGoodsBean> publicityattentions;
			AttentionGoods _o_ = AttentionGoods.this;
		publicityattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		for (xbean.AttentionGoodsBean _v_ : _o_.publicityattentions)
			publicityattentions.add(new AttentionGoodsBean.Data(_v_));
			return publicityattentions;
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
			return AttentionGoods.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AttentionGoods.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AttentionGoods.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AttentionGoods.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AttentionGoods.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AttentionGoods.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AttentionGoods.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AttentionGoods.this.hashCode();
		}

		@Override
		public String toString() {
			return AttentionGoods.this.toString();
		}

	}

	public static final class Data implements xbean.AttentionGoods {
		private java.util.ArrayList<xbean.AttentionGoodsBean> buyattentions; // 购买关注,数据库表中的id,关注数量最多8个
		private java.util.ArrayList<xbean.AttentionGoodsBean> publicityattentions; // 公示关注,数据库表中的id,关注数量最多8个

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			buyattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
			publicityattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
		}

		Data(xbean.AttentionGoods _o1_) {
			if (_o1_ instanceof AttentionGoods) assign((AttentionGoods)_o1_);
			else if (_o1_ instanceof AttentionGoods.Data) assign((AttentionGoods.Data)_o1_);
			else if (_o1_ instanceof AttentionGoods.Const) assign(((AttentionGoods.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AttentionGoods _o_) {
			buyattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
			for (xbean.AttentionGoodsBean _v_ : _o_.buyattentions)
				buyattentions.add(new AttentionGoodsBean.Data(_v_));
			publicityattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
			for (xbean.AttentionGoodsBean _v_ : _o_.publicityattentions)
				publicityattentions.add(new AttentionGoodsBean.Data(_v_));
		}

		private void assign(AttentionGoods.Data _o_) {
			buyattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
			for (xbean.AttentionGoodsBean _v_ : _o_.buyattentions)
				buyattentions.add(new AttentionGoodsBean.Data(_v_));
			publicityattentions = new java.util.ArrayList<xbean.AttentionGoodsBean>();
			for (xbean.AttentionGoodsBean _v_ : _o_.publicityattentions)
				publicityattentions.add(new AttentionGoodsBean.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(buyattentions.size());
			for (xbean.AttentionGoodsBean _v_ : buyattentions) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(publicityattentions.size());
			for (xbean.AttentionGoodsBean _v_ : publicityattentions) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.AttentionGoodsBean _v_ = xbean.Pod.newAttentionGoodsBeanData();
				_v_.unmarshal(_os_);
				buyattentions.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.AttentionGoodsBean _v_ = xbean.Pod.newAttentionGoodsBeanData();
				_v_.unmarshal(_os_);
				publicityattentions.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.AttentionGoods copy() {
			return new Data(this);
		}

		@Override
		public xbean.AttentionGoods toData() {
			return new Data(this);
		}

		public xbean.AttentionGoods toBean() {
			return new AttentionGoods(this, null, null);
		}

		@Override
		public xbean.AttentionGoods toDataIf() {
			return this;
		}

		public xbean.AttentionGoods toBeanIf() {
			return new AttentionGoods(this, null, null);
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
		public java.util.List<xbean.AttentionGoodsBean> getBuyattentions() { // 购买关注,数据库表中的id,关注数量最多8个
			return buyattentions;
		}

		@Override
		public java.util.List<xbean.AttentionGoodsBean> getBuyattentionsAsData() { // 购买关注,数据库表中的id,关注数量最多8个
			return buyattentions;
		}

		@Override
		public java.util.List<xbean.AttentionGoodsBean> getPublicityattentions() { // 公示关注,数据库表中的id,关注数量最多8个
			return publicityattentions;
		}

		@Override
		public java.util.List<xbean.AttentionGoodsBean> getPublicityattentionsAsData() { // 公示关注,数据库表中的id,关注数量最多8个
			return publicityattentions;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AttentionGoods.Data)) return false;
			AttentionGoods.Data _o_ = (AttentionGoods.Data) _o1_;
			if (!buyattentions.equals(_o_.buyattentions)) return false;
			if (!publicityattentions.equals(_o_.publicityattentions)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += buyattentions.hashCode();
			_h_ += publicityattentions.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(buyattentions);
			_sb_.append(",");
			_sb_.append(publicityattentions);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
