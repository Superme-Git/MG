package mkdb;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

/**
 * xbean���Զ����������ͣ�֧��ϵ�л�����־���ع����������toConst��
 * ֻ���������в���xbean��������������н��в�������������£�xdb���׳��쳣�������
 * xbean����ֱ�ӻ���ͨ��������ʽ��������xbean���������γɰ�������
 * xbean����������̲߳����������⡣����������xbean������֧�֡�
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
	 * ��鲢���޸ĵ�ǰbean��parent��parent��varname��
	 * @param parent
	 * @param varname
	 * @param log �Ƿ��¼��־��������Bean��һ�δ�Ӳ��װ�ؽ���ʱ�����������Ƿ�ɹ��������ֶ�TRecord�����á�
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
			// parent ���޸Ĳ����ĸ���Ʒ�����commit������޸Ļ�ͨ��dirty�����ﲻ��ͨ�档
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
	 * �ݹ��ѯ�õ����е����ˡ�
	 * @return forefathers
	 */
	public final Forefathers xdbForefathers() {
		Forefathers forefathers = new Forefathers();
		xdbForefathers(forefathers);
		return forefathers;
	}

	/**
	 * �ݹ��ѯ�õ����е����ˡ�
	 * @param forefathers
	 */
	public final void xdbForefathers(Forefathers forefathers) {
		if (null != this._parent_) {
			forefathers.forefathers.add(this._parent_);
			this._parent_.xdbForefathers(forefathers);
		}
	}
	/**
	 * xbean ���е����ȡ�
	 * <p>������ȡ XDB �ڲ���Ϣ�����ڵ��Ի��߹��߿�����һ��Ӧ��Ӧ<b>����С�ĵ�ʹ��</b>���������
	 */
	public final class Forefathers {
		private List<Bean> forefathers = new ArrayList<Bean>();

		/**
		 * ����bean�Լ���
		 */
		public Bean getBean() {
			return XBean.this;
		}

		/**
		 * @return ���е������б���������������С�
		 */
		public List<Bean> getForefathers() {
			return forefathers;
		}

		/**
		 * @return ������ϵ�������Table���򷵻أ���������¶�����null��
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
		 * @return ������ϵ�������TRecord���򷵻أ���������¶�����null��
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
		 * ���ص�ǰbean������������
		 * @return null if the bean is not managed by mkdb��
		 */
		public Lockey getLockey() {
			TRecord<?, ?> record = this.getRecord();
			if (null != record)
				return record.getLockey();
			return null;
		}

		/**
		 * @return �Ƿ���MKDB�����¡� 
		 */
		public boolean isHeldByMkdb() {
			return null != this.getTable(); // ����Ҳ����ʹ�� getRecord, getLockey ���жϡ�
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
	 * У�� ... <b>�������������У����ԣ��Ժ���ܻ���������Ӹ���У�飬�벻Ҫ�����������á�</b>
	 * <p> ��⣺1 �Ƿ�ӵ����. 2 ...
	 */
	public final void _xdb_verify_unsafe_() {
		if (false == _xdb_verify_)
			return;

		// verify lock // �Ѿ�ӵ��ˢ��д�����߳��̲߳���Ҫ������� see Checkpoint��
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
