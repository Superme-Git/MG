package mkgen;

/**
 * Capacity ���ý���������������ʾ��������Ϣ��
 */
public class Capacity extends mkdb.util.CapacityConf {
	private final String message;

	@Override
	public void throwIf(boolean condition, String more) {
		if (condition)
			throw new IllegalArgumentException("invalid capacity! " + message + " info=" + more);
	}

	public void warnIf(boolean condition, String more) {
		if (condition)
			Main.warn(message + " info=" + more, 'c');
	}

	/**
	 * ����Ҫ�κ���������
	 */
	public void notNeed() {
		this.warnIf(
				super.getCapacity() != null
				|| super.getKey() != null
				|| super.getValue() != null
				, "capacity is not required.");
	}

	/**
	 * ��Ҫ��������
	 */
	public void capacityNeed() {
		this.warnIf(super.getCapacity() == null, "capacity is required.");
	}

	/**
	 * ����Ҫkey��������
	 */
	public void keyNotNeed() {
		this.warnIf(super.getKey() != null, "capacity.key is not required.");
	}

	/**
	 * ����Ҫvalue��������
	 */
	public void valueNotNeed() {
		this.warnIf(super.getValue() != null, "capacity.value is not required.");
	}

	/**
	 * ֻ��Ҫ��������
	 */
	public void capacityOnly() {
		this.capacityNeed();
		this.keyNotNeed();
		this.valueNotNeed();
	}

	public Capacity extractKey() {
		return new Capacity(super.getKey(), null, null, this.message + "<key>");
	}

	public Capacity extractValue() {
		return new Capacity(super.getValue(), null, null, this.message + "<value>");
	}

	public Capacity(Integer capacity, Integer key, Integer value, String message) {
		super(capacity, key, value);
		this.message = message;
	}

	public Capacity(String conf, String context) {
		super(conf);
		this.message = "CAPACITY conf=" + Main.quote(conf) + " name=" + context;
	}
}
