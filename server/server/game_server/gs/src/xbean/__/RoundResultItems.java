
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoundResultItems extends mkdb.XBean implements xbean.RoundResultItems {
	private java.util.LinkedList<fire.pb.battle.NewResultItem> resultitems; // 一个回合战斗的demo
	private java.util.LinkedList<fire.pb.battle.AIOperation> aiactions; // 随战斗脚本播放的客户端AI动作
	private java.util.HashMap<Integer, Integer> fighterfinallyhps; // 回合结束时战斗者血量的最终值
	private java.util.HashMap<Integer, Integer> fighterfinallymps; // 回合结束时战斗者兰量的最终值

	@Override
	public void _reset_unsafe_() {
		resultitems.clear();
		aiactions.clear();
		fighterfinallyhps.clear();
		fighterfinallymps.clear();
	}

	RoundResultItems(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		resultitems = new java.util.LinkedList<fire.pb.battle.NewResultItem>();
		aiactions = new java.util.LinkedList<fire.pb.battle.AIOperation>();
		fighterfinallyhps = new java.util.HashMap<Integer, Integer>();
		fighterfinallymps = new java.util.HashMap<Integer, Integer>();
	}

	public RoundResultItems() {
		this(0, null, null);
	}

	public RoundResultItems(RoundResultItems _o_) {
		this(_o_, null, null);
	}

	RoundResultItems(xbean.RoundResultItems _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public xbean.RoundResultItems copy() {
		_xdb_verify_unsafe_();
		return new RoundResultItems(this);
	}

	@Override
	public xbean.RoundResultItems toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoundResultItems toBean() {
		_xdb_verify_unsafe_();
		return new RoundResultItems(this); // same as copy()
	}

	@Override
	public xbean.RoundResultItems toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoundResultItems toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<fire.pb.battle.NewResultItem> getResultitems() { // 一个回合战斗的demo
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "resultitems"), resultitems);
	}

	@Override
	public java.util.List<fire.pb.battle.AIOperation> getAiactions() { // 随战斗脚本播放的客户端AI动作
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "aiactions"), aiactions);
	}

	@Override
	public java.util.Map<Integer, Integer> getFighterfinallyhps() { // 回合结束时战斗者血量的最终值
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "fighterfinallyhps"), fighterfinallyhps);
	}

	@Override
	public java.util.Map<Integer, Integer> getFighterfinallyhpsAsData() { // 回合结束时战斗者血量的最终值
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> fighterfinallyhps;
		RoundResultItems _o_ = this;
		fighterfinallyhps = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fighterfinallyhps.entrySet())
			fighterfinallyhps.put(_e_.getKey(), _e_.getValue());
		return fighterfinallyhps;
	}

	@Override
	public java.util.Map<Integer, Integer> getFighterfinallymps() { // 回合结束时战斗者兰量的最终值
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "fighterfinallymps"), fighterfinallymps);
	}

	@Override
	public java.util.Map<Integer, Integer> getFighterfinallympsAsData() { // 回合结束时战斗者兰量的最终值
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> fighterfinallymps;
		RoundResultItems _o_ = this;
		fighterfinallymps = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fighterfinallymps.entrySet())
			fighterfinallymps.put(_e_.getKey(), _e_.getValue());
		return fighterfinallymps;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoundResultItems _o_ = null;
		if ( _o1_ instanceof RoundResultItems ) _o_ = (RoundResultItems)_o1_;
		else if ( _o1_ instanceof RoundResultItems.Const ) _o_ = ((RoundResultItems.Const)_o1_).nThis();
		else return false;
		if (!resultitems.equals(_o_.resultitems)) return false;
		if (!aiactions.equals(_o_.aiactions)) return false;
		if (!fighterfinallyhps.equals(_o_.fighterfinallyhps)) return false;
		if (!fighterfinallymps.equals(_o_.fighterfinallymps)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += resultitems.hashCode();
		_h_ += aiactions.hashCode();
		_h_ += fighterfinallyhps.hashCode();
		_h_ += fighterfinallymps.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(resultitems);
		_sb_.append(",");
		_sb_.append(aiactions);
		_sb_.append(",");
		_sb_.append(fighterfinallyhps);
		_sb_.append(",");
		_sb_.append(fighterfinallymps);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("resultitems"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("aiactions"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("fighterfinallyhps"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("fighterfinallymps"));
		return lb;
	}

	private class Const implements xbean.RoundResultItems {
		RoundResultItems nThis() {
			return RoundResultItems.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoundResultItems copy() {
			return RoundResultItems.this.copy();
		}

		@Override
		public xbean.RoundResultItems toData() {
			return RoundResultItems.this.toData();
		}

		public xbean.RoundResultItems toBean() {
			return RoundResultItems.this.toBean();
		}

		@Override
		public xbean.RoundResultItems toDataIf() {
			return RoundResultItems.this.toDataIf();
		}

		public xbean.RoundResultItems toBeanIf() {
			return RoundResultItems.this.toBeanIf();
		}

		@Override
		public java.util.List<fire.pb.battle.NewResultItem> getResultitems() { // 一个回合战斗的demo
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(resultitems);
		}

		@Override
		public java.util.List<fire.pb.battle.AIOperation> getAiactions() { // 随战斗脚本播放的客户端AI动作
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(aiactions);
		}

		@Override
		public java.util.Map<Integer, Integer> getFighterfinallyhps() { // 回合结束时战斗者血量的最终值
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(fighterfinallyhps);
		}

		@Override
		public java.util.Map<Integer, Integer> getFighterfinallyhpsAsData() { // 回合结束时战斗者血量的最终值
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> fighterfinallyhps;
			RoundResultItems _o_ = RoundResultItems.this;
			fighterfinallyhps = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fighterfinallyhps.entrySet())
				fighterfinallyhps.put(_e_.getKey(), _e_.getValue());
			return fighterfinallyhps;
		}

		@Override
		public java.util.Map<Integer, Integer> getFighterfinallymps() { // 回合结束时战斗者兰量的最终值
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(fighterfinallymps);
		}

		@Override
		public java.util.Map<Integer, Integer> getFighterfinallympsAsData() { // 回合结束时战斗者兰量的最终值
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> fighterfinallymps;
			RoundResultItems _o_ = RoundResultItems.this;
			fighterfinallymps = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fighterfinallymps.entrySet())
				fighterfinallymps.put(_e_.getKey(), _e_.getValue());
			return fighterfinallymps;
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
			return RoundResultItems.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoundResultItems.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoundResultItems.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoundResultItems.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoundResultItems.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoundResultItems.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoundResultItems.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoundResultItems.this.hashCode();
		}

		@Override
		public String toString() {
			return RoundResultItems.this.toString();
		}

	}

	public static final class Data implements xbean.RoundResultItems {
		private java.util.LinkedList<fire.pb.battle.NewResultItem> resultitems; // 一个回合战斗的demo
		private java.util.LinkedList<fire.pb.battle.AIOperation> aiactions; // 随战斗脚本播放的客户端AI动作
		private java.util.HashMap<Integer, Integer> fighterfinallyhps; // 回合结束时战斗者血量的最终值
		private java.util.HashMap<Integer, Integer> fighterfinallymps; // 回合结束时战斗者兰量的最终值

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			resultitems = new java.util.LinkedList<fire.pb.battle.NewResultItem>();
			aiactions = new java.util.LinkedList<fire.pb.battle.AIOperation>();
			fighterfinallyhps = new java.util.HashMap<Integer, Integer>();
			fighterfinallymps = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.RoundResultItems _o1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoundResultItems copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoundResultItems toData() {
			return new Data(this);
		}

		public xbean.RoundResultItems toBean() {
			return new RoundResultItems(this, null, null);
		}

		@Override
		public xbean.RoundResultItems toDataIf() {
			return this;
		}

		public xbean.RoundResultItems toBeanIf() {
			return new RoundResultItems(this, null, null);
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
		public java.util.List<fire.pb.battle.NewResultItem> getResultitems() { // 一个回合战斗的demo
			return resultitems;
		}

		@Override
		public java.util.List<fire.pb.battle.AIOperation> getAiactions() { // 随战斗脚本播放的客户端AI动作
			return aiactions;
		}

		@Override
		public java.util.Map<Integer, Integer> getFighterfinallyhps() { // 回合结束时战斗者血量的最终值
			return fighterfinallyhps;
		}

		@Override
		public java.util.Map<Integer, Integer> getFighterfinallyhpsAsData() { // 回合结束时战斗者血量的最终值
			return fighterfinallyhps;
		}

		@Override
		public java.util.Map<Integer, Integer> getFighterfinallymps() { // 回合结束时战斗者兰量的最终值
			return fighterfinallymps;
		}

		@Override
		public java.util.Map<Integer, Integer> getFighterfinallympsAsData() { // 回合结束时战斗者兰量的最终值
			return fighterfinallymps;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoundResultItems.Data)) return false;
			RoundResultItems.Data _o_ = (RoundResultItems.Data) _o1_;
			if (!resultitems.equals(_o_.resultitems)) return false;
			if (!aiactions.equals(_o_.aiactions)) return false;
			if (!fighterfinallyhps.equals(_o_.fighterfinallyhps)) return false;
			if (!fighterfinallymps.equals(_o_.fighterfinallymps)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += resultitems.hashCode();
			_h_ += aiactions.hashCode();
			_h_ += fighterfinallyhps.hashCode();
			_h_ += fighterfinallymps.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(resultitems);
			_sb_.append(",");
			_sb_.append(aiactions);
			_sb_.append(",");
			_sb_.append(fighterfinallyhps);
			_sb_.append(",");
			_sb_.append(fighterfinallymps);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
