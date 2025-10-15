package com.locojoy.jmonitor.spi;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.locojoy.jmonitor.IChart;
import com.locojoy.jmonitor.MultiLine;
import com.locojoy.jmonitor.util.DomHelper;

public class Chart implements IChart {
	static public final class Line {

		private String bean;
		private String value;
		private String key;
		private String graph;
		private String type;

		Map<String, MultiLine.Range> ranges = new HashMap<String, MultiLine.Range>();
		Map<String, String> key2refs = new HashMap<String, String>();
		Map<String, String> ref2keys = new HashMap<String, String>();

		public Line(String b, String v, String k, String graph, String type) {
			bean = b;
			value = v;
			key = k;
			this.graph = graph;
			this.type = type;
		}

		public Line() {
		}

		public String getBean() {
			return bean;
		}

		public String getValue() {
			return value;
		}

		public String getKey() {
			return key;
		}

		public String getGraph() {
			return graph;
		}

		public String getType() {
			return type;
		}

		public Map<String, MultiLine.Range> applyRanges(Map<String, MultiLine.Range> commonRanges) {
			Map<String, MultiLine.Range> res = new HashMap<String, MultiLine.Range>();

			for (Map.Entry<String, MultiLine.Range> entry : commonRanges.entrySet()) {
				String k = entry.getKey();
				if (k.equals(TableColumn.TIME))
					res.put(k, entry.getValue());
				else if (k.equals(TableColumn.ZID))
					res.put(k, entry.getValue());
				else {
					String realk = ref2keys.get(k);
					if (null != realk)
						res.put(realk, entry.getValue());
				}
			}
			res.putAll(ranges); // 私有约束优先级最高
			return res;
		}

		public Line clone() {
			Line l = new Line(bean, value, key, graph, type);
			l.ranges.putAll(ranges);
			l.key2refs.putAll(key2refs);
			l.ref2keys.putAll(ref2keys);
			return l;
		}

	}

	Configure configure;
	private String chartName;
	private Map<String, Line> lines = new HashMap<String, Line>();
	private Map<String, MultiLine.Range> ranges = new HashMap<String, MultiLine.Range>(); // 公共约束

	private Element element;
	private String xmlDescription;

	Chart(ChartManager man, Element ele) throws Exception {
		configure = man.getConfigure();
		element = ele;
		chartName = ele.getAttribute("name");

		for (Element e : DomHelper.getElementsByName(ele, "range")) {
			MultiLine.Range r = range(e);
			String rname = e.getAttribute("key");
			if (null != ranges.put(rname, r))
				throw new Exception(rname + " duplicate range");
		}

		for (Element e : DomHelper.getElementsByName(ele, "line")) {
			Line l = line(e);
			String lname = e.getAttribute("name");
			if (lname.length() == 0)
				throw new Exception("line name empty");
			if (null != lines.put(lname, l))
				throw new Exception(lname + " duplicate line");
		}

		xmlDescription = xml();
	}

	private Line line(Element ele) throws Exception {
		String bean = ele.getAttribute("bean");
		String value = ele.getAttribute("value");
		String key = ele.getAttribute("key");
		String graph = ele.getAttribute("graph");
		String type = ele.getAttribute("type");
		String slicesstr = ele.getAttribute("pieslices");
		int pieslices = slicesstr.isEmpty() ? 0 : Integer.valueOf(slicesstr);
		
		ChartChainImpl.verifyGraph(type, key, graph, pieslices);

		Table table = configure.getTableManager().getTable(bean);
		List<String> keylist = null != table ? table.getKeyList() : null;

		Line line = new Line(bean, value, key, graph, type);
		for (Element e : DomHelper.getElementsByName(ele, "range")) {
			MultiLine.Range r = range(e);
			String k = e.getAttribute("key");
			if (null != line.ranges.put(k, r))
				throw new Exception(k + " duplicate range");

			if (null != keylist) {// verify
				if (!keylist.contains(k))
					throw new Exception(k + " not in table");
			}
		}

		for (Element e : DomHelper.getElementsByName(ele, "rangeref")) {
			String k = e.getAttribute("key");
			String ref = e.getAttribute("ref");
			if (!ranges.containsKey(ref))
				throw new Exception(ref + " not exists");

			if (line.ranges.containsKey(k))
				throw new Exception(k + " already exist in range");

			if (null != line.key2refs.put(k, ref))
				throw new Exception(k + " duplicate key in rangeref");

			if (null != line.ref2keys.put(ref, k))
				throw new Exception(k + " duplicate ref in rangeref");

			if (null != keylist) {// verify
				if (!keylist.contains(k))
					throw new Exception(k + " not in table");
			}
		}

		return line;
	}

	private MultiLine.Range range(Element ele) throws Exception {
		Long low = null;
		Long up = null;
		try {
			low = Long.parseLong(ele.getAttribute("low"));
			up = Long.parseLong(ele.getAttribute("up"));
		} catch (Exception e) {
		}
		if (low == null && up == null)
			throw new Exception();

		return new MultiLine.Range(low, up);
	}

	Map<String, MultiLine.Range> tryApplyRanges(Map<String, MultiLine.Range> newRanges) throws Exception {

		Map<String, MultiLine.Range> res = new HashMap<String, MultiLine.Range>(ranges);
		if (null == newRanges)
			return res;

		for (Map.Entry<String, MultiLine.Range> entry : newRanges.entrySet()) {
			String key = entry.getKey();
			MultiLine.Range old = res.put(key, entry.getValue());
			if (null == old && !key.equals(TableColumn.TIME) && !key.equals(TableColumn.ZID))
				throw new Exception(key + " unapplyable to " + chartName);
		}

		return res;
	}

	@Override
	public String getChartName() {
		return chartName;
	}

	@Override
	public Set<String> getContrainableKeys() {
		return new HashSet<String>(ranges.keySet());
	}

	@Override
	public String getXmlDescription() {
		return xmlDescription;
	}

	private String xml() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element ele = document.createElement("chart");
		ele.setAttribute("name", chartName);

		for (Map.Entry<String, MultiLine.Range> entry : ranges.entrySet()) {
			Element r = document.createElement("range");
			r.setAttribute("key", entry.getKey());
			Long low = entry.getValue().getLow();
			Long up = entry.getValue().getUp();
			if (null != low)
				r.setAttribute("low", low.toString());
			if (null != up)
				r.setAttribute("up", up.toString());

			ele.appendChild(r);
		}

		for (Map.Entry<String, Line> entry : lines.entrySet()) {
			Element e = document.createElement("line");
			e.setAttribute("name", entry.getKey());

			Line line = entry.getValue();
			e.setAttribute("bean", line.getBean());
			e.setAttribute("value", line.getValue());
			e.setAttribute("key", line.getKey());
			if (!line.getType().isEmpty())
				e.setAttribute("type", line.getType());

			for (Map.Entry<String, MultiLine.Range> rentry : line.ranges.entrySet()) {
				MultiLine.Range r = rentry.getValue();
				Long low = r.getLow();
				Long up = r.getUp();
				Element re = document.createElement("range");
				re.setAttribute("key", rentry.getKey());
				if (null != low)
					re.setAttribute("low", low.toString());
				if (null != up)
					re.setAttribute("up", up.toString());

				e.appendChild(re);
			}

			ele.appendChild(e);
		}

		document.appendChild(ele);

		return DomHelper.documentToString(document);
	}

	Element getElement() {
		return element;
	}

	Map<String, Line> getLines() {
		return lines;
	}

}