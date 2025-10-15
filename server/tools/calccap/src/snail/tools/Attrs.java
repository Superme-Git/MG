package snail.tools;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.ObjectName;

public class Attrs extends Tool {

	@Override
	public void build(Options options) {
		options.add(Jmxc.options());
		String[][] attrs = {
				{ "-b", "bean", "name of bean. sample: mkdb:type=Protocol", "!" },
				{ null, "[...]", "attrs.", null },
		};
		options.add(attrs);
	}

	@Override
	public void run(Options options) throws Exception {
		Jmxc jmxc = Jmxc.connect(options);
		try {
			ObjectName beanName = new ObjectName(options.getValue("-b"));
			String attrNames[];
			if (options.hasToken()) {
				attrNames = options.getTokens().toArray(new String[options.getTokens().size()]);
			} else {
				// get all attrs
				MBeanInfo beanInfo = jmxc.getProxy().getSnapshotMBeanServerConnection().getMBeanInfo(beanName);
				MBeanAttributeInfo [] attrs = beanInfo.getAttributes();
				attrNames = new String[attrs.length];
				for (int i = 0; i < attrs.length; ++i) {
					MBeanAttributeInfo attr = attrs[i];
					attrNames[i] = attr.getName();
				}
			}
			AttributeList alist = jmxc.getProxy().getAttributes(beanName, attrNames);
			int size = alist.size();
			for (int i = 0; i < size; ++i) {
				Attribute attr = (Attribute)alist.get(i);
				String value = "" + attr.getValue();

				StringBuilder sb = new StringBuilder();
				sb.append(value).append(Tool.spaceN(25, value)).append(attr.getName());
				System.out.println(sb.toString());
			}
		} finally {
			jmxc.close();
		}
	}

}
