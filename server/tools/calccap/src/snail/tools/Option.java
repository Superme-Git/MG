package snail.tools;

public class Option {
	/**
	 * 名字，如：-f
	 */
	private String name;

	/**
	 * 参数值名，如果不需要，为null。
	 */
	private String valueName;

	/**
	 * 帮助描述，如：指定配置文件名
	 */
	private String description;

	/**
	 * 默认参数，null 表示不提供默认值。
	 */
	private String valueDefault;

	/**
	 * 参数数值。 解析过程中设置。
	 */
	private String value = null; 
	private boolean present = false;

	/**
	 * 选项在命令行中出现过，解析会设置成true。
	 * 
	 * 单独记录这个标记，用来处理那些不需要参数值，选项本身是否出现就是参数。
	 * @return
	 */
	public boolean isPresent() {
		return present;
	}

	public void setPresent() {
		this.present = true;
	}

	public Option(String [] option) {
		if (option.length != 4)
			throw new java.lang.IllegalArgumentException();
		setParams(option[0], option[1], option[2], option[3]);
	}

	private void setParams(String name, String valueName, String description, String valueDefault) {
		if (name != null && !Option.isOption(name))
			throw new IllegalArgumentException("Option name must startsWith '-'. name=" + name);
		// 当 name 为 null 时，这个选项仅仅用来输出帮助描述，不参与解析。
		this.name = name;
		this.valueName = valueName;
		this.description = description;
		this.valueDefault = valueDefault;
	}

	public Option(String name, String valueName, String description, String valueDefault) {
		this.setParams(name, valueName, description, valueDefault);
	}

	public boolean hasValue() {
		return valueName != null;
	}

	public String getName() {
		return name;
	}

	public static boolean isOption(String arg) {
		return arg.startsWith("-");
	}

	public String getValueName() {
		return valueName;
	}

	public String getDescription() {
		return description;
	} 

	public String getValueDefault() {
		return this.valueDefault;
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * 返回解析得到的值，可能为null
	 */
	public String getValuePresent() {
		return value;
	}
	/**
	 * 返回选项参数。仅支持单个String类型。
	 */
	public String getValue() {
		if ( false == hasValue() )
			throw new java.lang.IllegalStateException(this + " : no value");

		if ( null != value )
			return value;

		if (valueDefault != null && valueDefault.equals("!")) // value must present
			throw new java.lang.IllegalArgumentException(this + " : value required!");

		return valueDefault; // maybe null
	}

	public void setValue(String value) {
		if ( false == hasValue() )
			throw new IllegalStateException(this + " : no value");
		this.value = value;
	}
}
