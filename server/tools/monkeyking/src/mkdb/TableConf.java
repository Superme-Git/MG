package mkdb;

import org.w3c.dom.*;

public final class TableConf {
	public static final int DEFAULT_CACHE_HIGH = 512;
	public static final int DEFAULT_CACHE_LOW = 256;
	public static final int DEFAULT_CACHE_CAPACITY = 10240;
	public static final int DEFAULT_CACHE_CLEAN_TRY = 256;

	// Storage �������
	private String name;
	private int cachehigh;
	private int cachelow;

	// TTable �������
	private int cacheCapacity;
	private TTable.Persistence persistence;
	// ��������ڱ��ʱ���ᱻ���浽����ط���
	private String lockname;

	// Table ��������
	private java.util.Map<String, String> otherAttrs = new java.util.HashMap<String, String>();

	// �����ڲ�ϵͳ�����ʱ��ֻ��ʼ��Storage������á�
	TableConf(String name, int cachehigh, int cachelow) {
		this.name = name;
		this.cachehigh = cachehigh;
		this.cachelow = cachelow;
	}

	// �������ж�ȡ��
	TableConf(Element self) {
		name = self.getAttribute("name");

		mkgen.Define define = mkgen.Define.getInstance();
		if (define.isParsed())
		{
			Integer cachecap = define.getCacheCap(self.getAttribute("cacheCapacity"));
			cacheCapacity = (cachecap != null ? cachecap.intValue() : DEFAULT_CACHE_CAPACITY);
			Integer[] cachepage = define.getCachePage(self.getAttribute("cachePage"));
			cachelow = (cachepage != null ? cachepage[0].intValue() : DEFAULT_CACHE_LOW);
			cachehigh = (cachepage != null ? cachepage[1].intValue() : DEFAULT_CACHE_HIGH);
		}
		else
		{
			cachehigh = MkdbConf.getInt(self, "cachehigh", DEFAULT_CACHE_HIGH);
			cachelow = MkdbConf.getInt(self, "cachelow", DEFAULT_CACHE_LOW);
			cacheCapacity = MkdbConf.getInt(self, "cacheCapacity", DEFAULT_CACHE_CAPACITY);
		}

		String tmp;
		lockname = (tmp = self.getAttribute("lock")).isEmpty() ? name : tmp;
		persistence = (tmp = self.getAttribute("persistence").toUpperCase()).isEmpty()
				? TTable.Persistence.DB : TTable.Persistence.valueOf(tmp);

		{
			NamedNodeMap attrs = self.getAttributes();
			for (int i = 0; i < attrs.getLength(); ++i) {
				Attr attr = (Attr)attrs.item(i);
				otherAttrs.put(attr.getName(), attr.getValue());
			}
			// ɾ����֪�����ԡ�
			otherAttrs.remove("name");
			otherAttrs.remove("cacheHigh");
			otherAttrs.remove("cacheLow");
			otherAttrs.remove("cacheCapacity");
			otherAttrs.remove("lock");
			otherAttrs.remove("persistence");
		}
	}

	public java.util.Map<String, String> getOtherAttrs() {
		return otherAttrs;
	}

	public String getOtherAttr(String name) {
		String value = otherAttrs.get(name);
		return value == null ? "" : value;
	}

	public int getOtherAttrInt(String name, int def) {
		String value = this.getOtherAttr(name);
		return value.isEmpty() ? def : Integer.valueOf(value);
	}

	public long getOtherAttrLong(String name, long def) {
		String value = this.getOtherAttr(name);
		return value.isEmpty() ? def : Long.valueOf(value);
	}

	public int getCacheCapacity() {
		return cacheCapacity;
	}

	public String getLockName() {
		return lockname;
	}

	public int getCacheHigh() {
		return cachehigh;
	}

	public int getCacheLow() {
		return cachelow;
	}

	public String getName() {
		return name;
	}

	public TTable.Persistence getPersistence() {
		return persistence;
	}
}
