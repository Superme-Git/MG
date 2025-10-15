package com.locojoy.jmonitor.spi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.locojoy.jmonitor.ChartChain;
import com.locojoy.jmonitor.MultiLine.SepKey;
import com.locojoy.jmonitor.util.DomHelper;
import com.locojoy.jmonitor.util.StringHelper;

public class ChartChainImpl extends ChartChain {

	private String chartChainName;
	private First first;
	private List<Next> nexts = new ArrayList<Next>();
	private Element element;
	
	private String xmlDescription;

	public ChartChainImpl(ChartChainManager man, Element ele) throws Exception {
		element = ele;
		chartChainName = ele.getAttribute("name");
		{
			List<Element> fes = DomHelper.getElementsByName(ele, "first");
			if ( fes.isEmpty() )
				throw new Exception("no first element");
			Element fe = fes.get(0);
			String cn = fe.getAttribute("chart");
			if (cn.length() > 0)
				first = new First(cn);
			else{
				String bn = fe.getAttribute("bean");
				String key = fe.getAttribute("key");
				String values = fe.getAttribute("values");
				
				if (bn.length() == 0 || key.length() == 0 || values.length() == 0)
					throw new Exception("first no bean or key attr");
				
				String days = fe.getAttribute("days");
				String zid  = fe.getAttribute("zid");
				Integer d = days.length() > 0 ? Integer.valueOf(days) : null;
				Integer z = zid.length() > 0 ? Integer.valueOf(zid) : null;
				
				String sepkey  = fe.getAttribute("sepkey");
				List<String> vals = Arrays.asList(values.split(","));
				
				String graph = fe.getAttribute("graph");
				String type = fe.getAttribute("type");
				String slicesstr = fe.getAttribute("pieslices");
				int pieslices = slicesstr.isEmpty() ? 0 : Integer.valueOf(slicesstr);
				
				verifyGraph(type, key, graph, pieslices);
				
				first = new First(bn, key, d, z, vals, verifySepkey(sepkey, vals.size() > 1), graph, type, pieslices);
			}
		}
		
		for (Element e : DomHelper.getElementsByName(ele, "next")) {
			String name = e.getAttribute("chart");
			if (name.length() > 0)
				nexts.add( new Next(name) );
			else{
				String key = e.getAttribute("key");
				String values = e.getAttribute("values");
				if (key.length() == 0 || values.length() == 0)
					throw new Exception("next no key or values attr");
				
				String sepkey = e.getAttribute("sepkey");
				List<String> vals = Arrays.asList(values.split(","));
				
				String graph = e.getAttribute("graph");
				String type = e.getAttribute("type");
				String slicesstr = e.getAttribute("pieslices");
				int pieslices = slicesstr.isEmpty() ? 0 : Integer.valueOf(slicesstr);
				
				verifyGraph(type, key, graph, pieslices);
				nexts.add( new Next(key, vals, verifySepkey(sepkey, vals.size() > 1), graph, type, pieslices) );
			}
		}
		
		xmlDescription = xml();
		
	}
	
	private SepKey verifySepkey(String sepkey, boolean multi_value) throws Exception{
		if (sepkey.length() == 0)
			return null;
		
		if (multi_value)
			throw new Exception("multi values with sepkey is forbidden");
		
		String[] ss = sepkey.split(",");
		String key = ss[0];
		int min = Integer.parseInt(ss[1]);
		int max = Integer.parseInt(ss[2]);
		int sep = Integer.parseInt(ss[3]);
		
		SepKey sk = new SepKey(key, min, max, sep);
		sk.verify();
		return sk;
	}
	
	static void verifyGraph(String type, String key, String graph, int slices) throws Exception{
		if (!type.isEmpty()){
			if ( type.equals(TYPE_ASCOUNTER) || type.equals(TYPE_ASCOUNTER_SHOWRATE) || type.equals(TYPE_SHOWRATE)) {
				if (!key.equals(TableColumn.TIME))
					throw new Exception("ascounter or rate key " + key + " not time");
				if (!graph.isEmpty() && !graph.equals("line"))
					throw new Exception("ascounter or rate graph " + graph + " not line");
			}else
				throw new Exception("type should be " + TYPE_ASCOUNTER + "/" + TYPE_ASCOUNTER_SHOWRATE + "/" + TYPE_SHOWRATE);
		}
		if (slices < 0)
			throw new Exception("pieslices < 0");
		
		if (slices > 0 && !graph.isEmpty() && !graph.equals(GRAPH_PIE))
			throw new Exception("pieslices="+slices+",but graph not pie" );
	}
	
	private String sepKeyString(SepKey sk){
		return StringHelper.join(Arrays.asList(sk.getKey(), sk.getMin(), sk.getMax(), sk.getSep()), ",");
	}
	
	
	Element getElement() {
		return element;
	}
		
	@Override
	public String getChartChainName() {
		return chartChainName;
	}
	

	@Override
	public String getXmlDescription() {
		return xmlDescription;
	}
	
	private String xml() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element ele = document.createElement("chartchain");
		ele.setAttribute("name", chartChainName);
		
		Element e = document.createElement("first");
		if (first.isChart())
			e.setAttribute("chart", first.getName());
		else{
			e.setAttribute("bean", first.getName());
			e.setAttribute("key", first.getKey());
			Integer d = first.getDays();
			if (null != d)
				e.setAttribute("days", String.valueOf(d));
			
			Integer z = first.getZid();
			if (null != z)
				e.setAttribute("zid", String.valueOf(z));
			
			e.setAttribute("values", StringHelper.join(first.getValues(), ","));
			SepKey sk = first.getSepkey();
			if (null != sk)
				e.setAttribute("sepkey", sepKeyString(sk));
			
			String graph = first.getGraph();
			if (null != graph && !graph.isEmpty())
				e.setAttribute("graph", graph);

			String type = first.getType();
			if (null != type && !type.isEmpty())
				e.setAttribute("type", first.getType());
			
			int pieslices = first.getPieSlices();
			if (pieslices > 0)
				e.setAttribute("pieslices", String.valueOf(pieslices));
			
		}
		ele.appendChild(e);
		
		for (Next n : nexts) {
			e = document.createElement("next");
			if (n.isChart()){
				e.setAttribute("chart", n.getName());
			}else{
				e.setAttribute("key", n.getName());
				e.setAttribute("values", StringHelper.join(n.getValues(), ","));
				
				SepKey sk = n.getSepkey();
				if (null != sk)
					e.setAttribute("sepkey", sepKeyString(sk));
				
				String graph = n.getGraph();
				if (null != graph && !graph.isEmpty())
					e.setAttribute("graph", graph);
				
				String type = n.getType();
				if (null != type && !type.isEmpty())
					e.setAttribute("type", n.getType());

				int pieslices = n.getPieSlices();
				if (pieslices > 0)
					e.setAttribute("pieslices", String.valueOf(pieslices));
				
			}
			ele.appendChild(e);
		}
		
		document.appendChild(ele);
		return DomHelper.documentToString(document);
	}

	@Override
	public First getFirst() {
		return first;
	}

	@Override
	public List<Next> getNexts() {
		return nexts;
	}
	
	
}