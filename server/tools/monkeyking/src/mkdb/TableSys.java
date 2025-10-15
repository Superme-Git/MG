package mkdb;

import com.locojoy.base.Marshal.OctetsStream;

public final class TableSys extends Table {
	// open �Ժ��⼸����������ֻ�ж�ȡ����������Ҫ��������
	private StorageSys storage;
	private mkdb.util.AutoKeys autoKeys;

	public mkdb.util.AutoKeys getAutoKeys() {
		return autoKeys;
	}

	//////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getName() {
		return "_sys_";
	}

	@Override
	public Persistence getPersistence() {
		return Persistence.DB;
	}

	@Override
	Storage open(MkdbConf xconf, Logger logger) {
		if (null != storage)
			throw new XError("TableSys has opened : " + getName());

		TableConf tconf = xconf.getTableConf(getName());
		if (null == tconf)
			tconf = new TableConf(getName(), 1, 1);

		storage = new StorageSys(logger, xconf, tconf);

		// װ��ϵͳ�����ݼ�¼
		autoKeys = storage.loadAutoKeys(xconf, tconf);

		// ������storage.flush����ʵ�֣���Checkpoint.flush����ͳһˢ�¡�
		//Trace.debug("load:" + autoKeys.toString());
		return storage;
	}

	@Override
	void close() {
		if (null != storage) {
			storage.close();
			storage = null;
		}
	}

	private final class StorageSys extends Storage {
		StorageSys(Logger logger, MkdbConf xconf, TableConf tconf) {
			super(logger, xconf, tconf);
		}

		mkdb.util.AutoKeys loadAutoKeys(MkdbConf xconf, TableConf tconf) {
			mkdb.util.UniqNameConf unc = mkdb.Mkdb.getInstance().getConf().getUniqNameConf();
			if (unc == null) // û��Ψһ�����ã���ʱҲû��autoKey
				return null;

			int localId = unc.getLocalId();
			if (localId < 0) // �����ã��������� autoKey��
				return null;

			keyOfAutoKeys = new OctetsStream().marshal("mkdb.util.AutoKeys." + String.valueOf(localId), mkdb.Const.IO_CHARSET);
			OctetsStream valueOfAutoKeys = super.find(keyOfAutoKeys);

			// key �������ݡ��� key �а��� localId������ʹ�úϷ�ʱ����˳����
			// rename "mkdb.util.AutoKeys" to "mkdb.util.AutoKeys." + localId
			OctetsStream oldKey = new OctetsStream().marshal("mkdb.util.AutoKeys", Const.IO_CHARSET);
			OctetsStream oldValue = super.find(oldKey);
			if (null != oldValue) {
				if (null != valueOfAutoKeys)
					throw new XError("autoKeys corrupt: new? old?");

				super.replaceUnsafe(keyOfAutoKeys, oldValue);
				super.removeUnsafe(oldKey);
				// ��� XDB û���κ��޸ģ�checkpoint���ᱻ���á�see Checkpoint.flush checkpoint. 
				// ����ͨ���� AutoKeys ��� dirty��α���޸ġ���Ȼ��α�죬�������޸���һ�α���Ҳ�ǿ��Եģ�Ҳ���˴��š�
				return new mkdb.util.AutoKeys(oldValue, localId, true);
			}

			return new mkdb.util.AutoKeys(valueOfAutoKeys, localId, false);
		}

		private OctetsStream keyOfAutoKeys = null; // ��סkey����ֹ�����޸ģ�Ҳ�е��Ż����á�

		/**
		 * ����
		 * 1 autoKey �����з��ʶ����ڴ��У�snapshotValue ���� Checkpoint �߳�������ʡ�
		 * 2 flush�������棬����Ҫ���ض� storage �ķ��ʡ�see TStorage & TRecord��
		 * 3 marshalN, marshal0, snapshot �� flush ���Ტ����������������
		 */
		private OctetsStream snapshotValue = null;

		@Override
		int marshalN() {
			if (null != TableSys.this.autoKeys && TableSys.this.autoKeys.isDirty()) {
				snapshotValue = TableSys.this.autoKeys.encodeValue(-1922908697795782568L);
				return 1;
			}
			return 0;
		}

		@Override
		int marshal0() {
			return 0;
		}

		@Override
		int snapshot() {
			return null != snapshotValue ? 1 : 0;
		}

		@Override
		int flush() {
			if (null != snapshotValue) {
				super.replaceUnsafe(keyOfAutoKeys, snapshotValue);
				snapshotValue = null;
				return 1;
			}
			return 0;
		}

	}

}
