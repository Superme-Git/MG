package mkdb;

/**
 * 
 * ���ڲ��ӿڶ��塣
 * 
 * Ϊ�˳䵱Record��parent�����Լ̳��� XBean��
 * 
 * @author cn
 * 
 */
public abstract class Table extends XBean {
	public static enum Persistence {
		MEMORY, DB
	}

	Table() {
		super(null, null);
	}

	abstract Storage open(MkdbConf xconf, Logger logger);

	abstract void close();

	public abstract String getName();
	public abstract Persistence getPersistence();

	// ϵͳ����ʵ�֡�
	// �û�����Ҫ(TTable)��
	protected void initialize(Tables tables) {
	}

	// �û�����Ҫ(TTable)��
	void logNotify() {
	}
}
