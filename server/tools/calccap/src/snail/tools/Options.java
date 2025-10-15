package snail.tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令行选项
 * 
 * sample
 	1 定义选项
	String [][] _opts_ = {
	//	选项名       值名      帮助描述            默认值
		{"-input",  "file", "input file name",  null},           // 可以不提供，保持值为null。
		{"-output", "file", "output file name", "defaultValue"}, // 可以不提供，此为默认值。
		{"-mode",   "N",    "mode no.",         "!"},            // 必须提供。
		{null,      null,   "help only",     null},           // 只用来输出帮助描述。
		{"-debug",  null,   "debug?",   null},                   // 无参数值。
	};
		//////////////////////////////////////////
		// 参数值定义表
		// 值名       说明                       默认值
		// null      不需要参数                  忽略此项定义
		// valueName 需要参数，此为参数名字        null          可以不提供参数值。保持值为null。
		//                                     !             必须不提供参数值。
		//                                     defaultValue  可以不提供参数值。此为默认值。
		//
		// 注意："！" 被用来表示参数值必须设置，所以无法用这个当作默认值。

	2 初始化选项
	Options opts = new Options(_opts_);

	3 解析命令行
	opts.parse(args);

	4 使用
	opts.getValue("-input");
	opts.getValue("-output");
	opts.getValue("-debug") != null;

	5 打印选项
	opts.printUsage("java -jar myjar"); // 这里的参数也可以是 MainClass 的名字。

	6 打印结果例子，按1的定义
	Usage: java -jar myjar [options]
		-input  file  input file name
		-output file  output file name - default: defaultValue
		-mode   N     mode no.
		-debug        debug?
 *
 */
public class Options {
	private Map<String, Option> options = new HashMap<String, Option>();
	private List<String> tokens = new LinkedList<String>(); // 按出现顺序保存不是选项的参数。
	private List<Option> ordered = new ArrayList<Option>();  // 按加入顺序输出帮助。
	private int maxOptionNameLength = 0;
	private int maxValueNameLength = 0;

	public Options() {
	}

	public Options(String [][] options) {
		this.add(options);
	}

	public void add(String [][] options) {
		for (String[] option : options)
			this.add(option);
	}

	public void add(String[] option) {
		this.add(new Option(option));
	}

	public void add(Option option) {
		if (option.getName() != null && null != options.put(option.getName(), option))
			throw new RuntimeException(option + " : duplicate name");
		ordered.add(option);
		if (option.getName() != null && option.getName().length() > maxOptionNameLength)
			maxOptionNameLength = option.getName().length();
		if (option.getValueName() != null && option.getValueName().length() > maxValueNameLength)
			maxValueNameLength = option.getValueName().length();
	}

	public void parse(String [] args) {
		parse(args, 0);
	}

	public void parse(String [] args, int offset) {
		Option last = null;
		for (int i = offset; i < args.length; ++i) {
			String arg = args[i];
			if (Option.isOption(arg)) {
				last = options.get(arg);
				if (null == last)
					throw new IllegalArgumentException(arg + " : unknown option");
				last.setPresent();
			} else if (null != last) {
				last.setValue(arg);
				last = null; // a option has one value
			} else {
				tokens.add(arg);
			}
		}

		// verify 验证需要的参数都已提供。
		for (Option option : options.values())
			if (option.hasValue())
				option.getValue();
	}

	/**
	 * 根据加入顺序打印所有选项以及描述。
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String lineSeprarator = System.getProperty("line.separator", "\n");
		int nl = this.maxOptionNameLength + 2;
		int vl = this.maxValueNameLength + 2;

		for (Option opt : ordered) {
			sb.append("    ");
			if (null != opt.getName()) sb.append(opt.getName());
			sb.append(Tool.spaceN(nl, opt.getName()));
			if (null != opt.getValueName()) sb.append(opt.getValueName());
			sb.append(Tool.spaceN(vl, opt.getValueName()));
			sb.append(": ").append(opt.getDescription());
			if (opt.hasValue() && opt.getValueDefault() != null) {
				if (opt.getValueDefault().equals("!"))
					; // sb.append(" :REQUIRED!");
				else
					sb.append(" :DEFAULT=").append(opt.getValueDefault());
			}
			sb.append(lineSeprarator);
		}
		return sb.toString();
	}

	/**
	 * 返回所有的选项。
	 * @return
	 */
	public Map<String, Option> getOptions() {
		return options;
	}

	/**
	 * 返回选项之外的参数列表。
	 * @return
	 */
	public List<String> getTokens() {
		return tokens;
	}

	public boolean hasToken() {
		return false == tokens.isEmpty();
	}

	/**
	 * 返回选项参数值。
	 * 
	 * 对于无参数的选项， null 表示这个选项没有出现过
	 * 对于有参数的选项，肯定不会返回 null。
	 * 
	 * @param optname
	 * @return
	 * @throws RuntimeException if option not exist
	 */
	public String getValue(String optname) {
		Option option = this.getOption(optname);
		if (option.hasValue())
			return option.getValue();
		return option.isPresent() ? "" : null;
	}
//
//	/**
//	 * 选项参数值是否存过。
//	 * 
//	 * 对于无参数的选项：true 表示选项已配置；false 表示选项未经配置。
//	 * 对于有参数的选项：true 参数已配置，not null；false表示参数没有配置，能不能得到参数就看是否有默认值。  
//	 * 
//	 * @param optname
//	 * @return
//	 * @throws RuntimeException if option not exist
//	 */
//	public boolean isPresent(String optname) {
//		Option option = this.getOption(optname);
//		if (option.getValueType() == Option.VALUE_TYPE_NONE)
//			return option.isPresent();
//		return option.getValuePresent() != null;
//	}

	/**
	 * 返回选项。
	 * @param optname 必须是正确的名字。
	 * @return
	 */
	public Option getOption(String optname) {
		Option option = this.options.get(optname);
		if (null == option)
			throw new RuntimeException(optname + " : unknown option");
		return option;
	}

	public static void main(String args[]) {
		String [][] _opts_ = {
				//	选项名       值名      帮助描述            默认值
					{"-input",  "file", "input file name",  null},           // 可以不提供，保持值为null
					{"-output", "file", "output file name", "defaultValue"}, // 可以不提供，此为默认值
					{"-mode",   "N",    "mode no.",         "!"},            // 必须提供。
					{null,      null,   "help only",     null},           // 只用来输出帮助描述。
					{"-debug",  null,   "debug?",   null},                   // 无参数值
				};

		Options opts = new Options(_opts_);
		System.out.println(opts);

		opts.parse(args);
		for (String optName : opts.getOptions().keySet())
			System.out.println(optName + "=" + opts.getValue(optName));
		System.out.println(opts.getTokens());
	}
}