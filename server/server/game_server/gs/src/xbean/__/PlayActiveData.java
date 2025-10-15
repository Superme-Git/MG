
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PlayActiveData extends mkdb.XBean implements xbean.PlayActiveData {
	private int id; // 
	private int count; // 完成次数
	private int count2; // 完成次数2
	private float activeness; // 完成共获得的活跃度

	@Override
	public void _reset_unsafe_() {
		id = 0;
		count = 0;
		count2 = 0;
		activeness = 0.0f;
	}

	PlayActiveData(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public PlayActiveData() {
		this(0, null, null);
	}

	public PlayActiveData(PlayActiveData _o_) {
		this(_o_, null, null);
	}

	PlayActiveData(xbean.PlayActiveData _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PlayActiveData) assign((PlayActiveData)_o1_);
		else if (_o1_ instanceof PlayActiveData.Data) assign((PlayActiveData.Data)_o1_);
		else if (_o1_ instanceof PlayActiveData.Const) assign(((PlayActiveData.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PlayActiveData _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		count = _o_.count;
		count2 = _o_.count2;
		activeness = _o_.activeness;
	}

	private void assign(PlayActiveData.Data _o_) {
		id = _o_.id;
		count = _o_.count;
		count2 = _o_.count2;
		activeness = _o_.activeness;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(count);
		_os_.marshal(count2);
		_os_.marshal(activeness);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		count = _os_.unmarshal_int();
		count2 = _os_.unmarshal_int();
		activeness = _os_.unmarshal_float();
		return _os_;
	}

	@Override
	public xbean.PlayActiveData copy() {
		_xdb_verify_unsafe_();
		return new PlayActiveData(this);
	}

	@Override
	public xbean.PlayActiveData toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PlayActiveData toBean() {
		_xdb_verify_unsafe_();
		return new PlayActiveData(this); // same as copy()
	}

	@Override
	public xbean.PlayActiveData toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PlayActiveData toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getCount() { // 完成次数
		_xdb_verify_unsafe_();
		return count;
	}

	@Override
	public int getCount2() { // 完成次数2
		_xdb_verify_unsafe_();
		return count2;
	}

	@Override
	public float getActiveness() { // 完成共获得的活跃度
		_xdb_verify_unsafe_();
		return activeness;
	}

	@Override
	public void setId(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setCount(int _v_) { // 完成次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "count") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, count) {
					public void rollback() { count = _xdb_saved; }
				};}});
		count = _v_;
	}

	@Override
	public void setCount2(int _v_) { // 完成次数2
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "count2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, count2) {
					public void rollback() { count2 = _xdb_saved; }
				};}});
		count2 = _v_;
	}

	@Override
	public void setActiveness(float _v_) { // 完成共获得的活跃度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activeness") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, activeness) {
					public void rollback() { activeness = _xdb_saved; }
				};}});
		activeness = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PlayActiveData _o_ = null;
		if ( _o1_ instanceof PlayActiveData ) _o_ = (PlayActiveData)_o1_;
		else if ( _o1_ instanceof PlayActiveData.Const ) _o_ = ((PlayActiveData.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (count != _o_.count) return false;
		if (count2 != _o_.count2) return false;
		if (activeness != _o_.activeness) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += count;
		_h_ += count2;
		_h_ += activeness;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(count);
		_sb_.append(",");
		_sb_.append(count2);
		_sb_.append(",");
		_sb_.append(activeness);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("count"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("count2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activeness"));
		return lb;
	}

	private class Const implements xbean.PlayActiveData {
		PlayActiveData nThis() {
			return PlayActiveData.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PlayActiveData copy() {
			return PlayActiveData.this.copy();
		}

		@Override
		public xbean.PlayActiveData toData() {
			return PlayActiveData.this.toData();
		}

		public xbean.PlayActiveData toBean() {
			return PlayActiveData.this.toBean();
		}

		@Override
		public xbean.PlayActiveData toDataIf() {
			return PlayActiveData.this.toDataIf();
		}

		public xbean.PlayActiveData toBeanIf() {
			return PlayActiveData.this.toBeanIf();
		}

		@Override
		public int getId() { // 
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getCount() { // 完成次数
			_xdb_verify_unsafe_();
			return count;
		}

		@Override
		public int getCount2() { // 完成次数2
			_xdb_verify_unsafe_();
			return count2;
		}

		@Override
		public float getActiveness() { // 完成共获得的活跃度
			_xdb_verify_unsafe_();
			return activeness;
		}

		@Override
		public void setId(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCount(int _v_) { // 完成次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCount2(int _v_) { // 完成次数2
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActiveness(float _v_) { // 完成共获得的活跃度
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
			return PlayActiveData.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PlayActiveData.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PlayActiveData.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PlayActiveData.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PlayActiveData.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PlayActiveData.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PlayActiveData.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PlayActiveData.this.hashCode();
		}

		@Override
		public String toString() {
			return PlayActiveData.this.toString();
		}

	}

	public static final class Data implements xbean.PlayActiveData {
		private int id; // 
		private int count; // 完成次数
		private int count2; // 完成次数2
		private float activeness; // 完成共获得的活跃度

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.PlayActiveData _o1_) {
			if (_o1_ instanceof PlayActiveData) assign((PlayActiveData)_o1_);
			else if (_o1_ instanceof PlayActiveData.Data) assign((PlayActiveData.Data)_o1_);
			else if (_o1_ instanceof PlayActiveData.Const) assign(((PlayActiveData.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PlayActiveData _o_) {
			id = _o_.id;
			count = _o_.count;
			count2 = _o_.count2;
			activeness = _o_.activeness;
		}

		private void assign(PlayActiveData.Data _o_) {
			id = _o_.id;
			count = _o_.count;
			count2 = _o_.count2;
			activeness = _o_.activeness;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(count);
			_os_.marshal(count2);
			_os_.marshal(activeness);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			count = _os_.unmarshal_int();
			count2 = _os_.unmarshal_int();
			activeness = _os_.unmarshal_float();
			return _os_;
		}

		@Override
		public xbean.PlayActiveData copy() {
			return new Data(this);
		}

		@Override
		public xbean.PlayActiveData toData() {
			return new Data(this);
		}

		public xbean.PlayActiveData toBean() {
			return new PlayActiveData(this, null, null);
		}

		@Override
		public xbean.PlayActiveData toDataIf() {
			return this;
		}

		public xbean.PlayActiveData toBeanIf() {
			return new PlayActiveData(this, null, null);
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
		public int getId() { // 
			return id;
		}

		@Override
		public int getCount() { // 完成次数
			return count;
		}

		@Override
		public int getCount2() { // 完成次数2
			return count2;
		}

		@Override
		public float getActiveness() { // 完成共获得的活跃度
			return activeness;
		}

		@Override
		public void setId(int _v_) { // 
			id = _v_;
		}

		@Override
		public void setCount(int _v_) { // 完成次数
			count = _v_;
		}

		@Override
		public void setCount2(int _v_) { // 完成次数2
			count2 = _v_;
		}

		@Override
		public void setActiveness(float _v_) { // 完成共获得的活跃度
			activeness = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PlayActiveData.Data)) return false;
			PlayActiveData.Data _o_ = (PlayActiveData.Data) _o1_;
			if (id != _o_.id) return false;
			if (count != _o_.count) return false;
			if (count2 != _o_.count2) return false;
			if (activeness != _o_.activeness) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += count;
			_h_ += count2;
			_h_ += activeness;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(count);
			_sb_.append(",");
			_sb_.append(count2);
			_sb_.append(",");
			_sb_.append(activeness);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
