package mkdb;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

/**
 * xbean是自定义数据类型，支持系列化，日志（回滚），深拷贝，toConst。
 * 只能在事务中操作xbean。如果不在事务中进行操作，多数情况下，xdb会抛出异常报告错误。
 * xbean可以直接或者通过容器方式包含其他xbean，但不能形成包含环。
 * xbean本身不处理多线程并发访问问题。并发访问由xbean管理器支持。
 */
public class XBean implements Bean {
	private static final AtomicLong objid = new AtomicLong();

	private final Long _objid_ = objid.incrementAndGet();
	private XBean _parent_;
	private String _varname_;

	public XBean(XBean parent, String varname) {
		_parent_ = parent;
		_varname_ = varname;
	}

	/**
	 * 检查并且修改当前bean的parent和parent中varname。
	 * @param parent
	 * @param varname
	 * @param log 是否记录日志。当数据Bean第一次从硬盘装载进来时，不管事务是否成功，都保持对TRecord在引用。
	 */
	final void xdbParent(XBean parent, String varname, boolean log) {
		if (null != parent) {
			if (null != _parent_) // parent != null && _parent_ != null
				throw new XManagedError();
		}
		else {
			if (null == _parent_) // parent == null && _parent_ == null
				throw new XManagedError("not managed");
		}
		if (log)
			Transaction.currentSavepoint().addIfAbsent(new LogKey(this, "_parent_"), new LogXP());
		_parent_ = parent;
		_varname_ = varname;
	}

	private class LogXP implements Log {
		private XBean parent;
		private String varname;

		LogXP() {
			this.parent = _parent_;
			this.varname = _varname_;
		}

		@Override
		public void commit() {
			// parent 是修改操作的副产品，如果commit，相关修改会通告dirty。这里不再通告。
			// notifyMkdbDirty();
		}

		@Override
		public void rollback() {
			_parent_ = this.parent;
			_varname_ = this.varname;
		}
	}

	@Override
	public void _reset_unsafe_() {
		
	}
	
	@Override
	public final boolean xdbManaged() {
		return _parent_ != null;
	}

	@Override
	public final String xdbVarname() {
		return _varname_;
	}

	@Override
	public final Bean xdbParent() {
		return _parent_;
	}

	void xdbLogNotify(mkdb.logs.LogNotify notify) {
		if (null != _parent_)
			_parent_.xdbLogNotify(notify.push(new LogKey(_parent_, xdbVarname())));
	}

	@Override
	public final Long xdbObjId() {
		return _objid_;
	}

	@Override
	public OctetsStream marshal(OctetsStream arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bean toConst() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isConst() {
		return false;
	}

	@Override
	public boolean isData() {
		return false;
	}

	public mkdb.logs.Listenable newListenable() {
		return null;
	}

	/**
	 * 递归查询得到所有的先人。
	 * @return forefathers
	 */
	public final Forefathers xdbForefathers() {
		Forefathers forefathers = new Forefathers();
		xdbForefathers(forefathers);
		return forefathers;
	}

	/**
	 * 递归查询得到所有的先人。
	 * @param forefathers
	 */
	public final void xdbForefathers(Forefathers forefathers) {
		if (null != this._parent_) {
			forefathers.forefathers.add(this._parent_);
			this._parent_.xdbForefathers(forefathers);
		}
	}
	/**
	 * xbean 所有的祖先。
	 * <p>用来获取 XDB 内部信息。用于调试或者工具开发，一般应用应<b>谨慎小心的使用</b>这个东西。
	 */
	public final class Forefathers {
		private List<Bean> forefathers = new ArrayList<Bean>();

		/**
		 * 返回bean自己。
		 */
		public Bean getBean() {
			return XBean.this;
		}

		/**
		 * @return 所有的祖先列表，从最近到最老排列。
		 */
		public List<Bean> getForefathers() {
			return forefathers;
		}

		/**
		 * @return 如果最老的祖先是Table，则返回，其他情况下都返回null。
		 */
		public Table getTable() {
			if (forefathers.isEmpty())
				return null;
			Bean last = forefathers.get(forefathers.size() - 1);
			if (last instanceof Table)
				return (Table)last;
			return null;
		}

		/**
		 * @return 如果次老的祖先是TRecord，则返回，其他情况下都返回null。
		 */
		public TRecord<?, ?> getRecord() {
			if (forefathers.size() < 2)
				return null;
			Bean lesslast = forefathers.get(forefathers.size() - 2);
			if (lesslast instanceof TRecord<?, ?>)
				return (TRecord<?, ?>)lesslast;
			return null;
		}

		/**
		 * 返回当前bean所关联的锁。
		 * @return null if the bean is not managed by mkdb。
		 */
		public Lockey getLockey() {
			TRecord<?, ?> record = this.getRecord();
			if (null != record)
				return record.getLockey();
			return null;
		}

		/**
		 * @return 是否处于MKDB管理下。 
		 */
		public boolean isHeldByMkdb() {
			return null != this.getTable(); // 这里也可以使用 getRecord, getLockey 来判断。
		}
	}

	private static volatile boolean _xdb_verify_ = false;

	public static void _set_xdb_verify_(boolean enable) {
		_xdb_verify_ = enable;
	}

	public static boolean _is_xdb_verify_() {
		return _xdb_verify_;
	}

	/**
	 * 校验 ... <b>这个方法仅用于校验测试，以后可能会在这里添加更多校验，请不要当作功能来用。</b>
	 * <p> 检测：1 是否拥有锁. 2 ...
	 */
	public final void _xdb_verify_unsafe_() {
		if (false == _xdb_verify_)
			return;

		// verify lock // 已经拥有刷新写锁的线程线程不需要检查锁， see Checkpoint。
		if (false == mkdb.Mkdb.getInstance().getTables().isHeldFlushWriteLockByCurrentThread()) {
			Lockey lockey = this.xdbForefathers().getLockey();
			if (null != lockey && false == lockey.isHeldByCurrentThread())
				throw new UnfilialError(this.getClass().getName());
		}
	}

	public static class UnfilialError extends Error {
		private static final long serialVersionUID = -2377572699783238493L;

		public UnfilialError(String message) {
			super(message);
		}
	}
}
