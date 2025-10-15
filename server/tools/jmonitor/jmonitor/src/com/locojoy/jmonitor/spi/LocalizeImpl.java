package com.locojoy.jmonitor.spi;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.locojoy.jmonitor.Localize;
import com.locojoy.jmonitor.util.DomHelper;

public class LocalizeImpl extends Localize {

	static private Logger logger = Logger.getLogger(LocalizeImpl.class.getName());
	
	static class BaseKey extends Localize {
		private LocalizeManager manager;
		private Element element;
		private String name;
		private boolean readonly;
		private Map<Long, String> seps;
		private String xmlDescription;
		
		private Map<String, Short> key2number;

		BaseKey(LocalizeManager man, Element ele) throws Exception {
			manager = man;
			element = ele;
			name = ele.getAttribute("name");
			if (name.isEmpty())
				throw new Exception("name emtpy");
			
			readonly = ele.getAttribute("readonly").equals("true");
			
			if (readonly && !name.startsWith("RO_"))
				throw new Exception("system created basekey " + name + " should startswith RO_");
			
			if (!readonly && name.startsWith("RO_"))
				throw new Exception("user created basekey " + name + " must not startswith RO_");
			
			seps = LocalizeImpl.getSeps(ele);
			
			if (readonly){
				key2number = new HashMap<String, Short>();
				for(Map.Entry<Long, String> ent : seps.entrySet()){
					key2number.put(ent.getValue(), ent.getKey().shortValue());
				}
			}
			xmlDescription = xml();
		}
		
		BaseKey( LocalizeManager man, String beanname ) throws Exception{
			manager = man;
			name =  "RO_" + beanname;
			readonly = true;
			seps = new TreeMap<Long, String>();
			key2number = new HashMap<String, Short>();
			xmlDescription = xml();
			
			
			Element e = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlDescription))).getDocumentElement();
			element = (Element) manager.getConfigure().getDocument().importNode(e, true);
		}
		
		short getKeyNumber(String key){
			assert readonly;
			
			Short v = key2number.get(key);
			if (null != v)
				return v;
			
			short thisv = 1;
			if (!key2number.isEmpty())
				thisv = (short) (Collections.max(key2number.values()) + 1);
			
			Element e = manager.getConfigure().getDocument().createElement("sep");
			e.setAttribute("local", key);
			e.setAttribute("o", String.valueOf(thisv));
			element.appendChild(e);
			key2number.put(key, thisv);
			seps.put(Short.valueOf(thisv).longValue(), key);
			
			logger.info("BaseKey.SystemSet: " + name + "." + key + "=" + thisv);
			try {
				xmlDescription = xml();
				manager.getConfigure().saveConfig();
			} catch (Exception e1) {
				logger.warning( name + ".getKeyNumber(" +  key + ")=" + thisv + " saveConfig error: " + e1.getMessage());
			}
			
			return thisv;
		}
		

		Element getElement() {
			return element;
		}

		Map<Long, String> getSeps() {
			return seps;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getXmlDescription() {
			return xmlDescription;
		}

		@Override
		public boolean isBaseKey() {
			return true;
		}
		
		@Override
		public boolean isReadOnly(){
			return readonly;
		}
		
		private String xml() throws Exception {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element ele = document.createElement("basekey");
			ele.setAttribute("name", name);
			if (readonly)
				ele.setAttribute("readonly", "true");
			for (Map.Entry<Long, String> e : seps.entrySet()) {
				Element e2 = document.createElement("sep");
				e2.setAttribute("o", String.valueOf(e.getKey()));
				e2.setAttribute("local", e.getValue());
				ele.appendChild(e2);
			}
			document.appendChild(ele);
			return DomHelper.documentToString(document);
		}

	};

	static Map<Long, String> getSeps(Element e) throws Exception {
		Map<Long, String> seps = new TreeMap<Long, String>();
		for (Element se : DomHelper.getElementsByName(e, "sep")) {
			long sepo = Long.parseLong(se.getAttribute("o"));
			String loc = se.getAttribute("local");
			if (loc.isEmpty())
				throw new Exception("local empty");

			if (null != seps.put(sepo, loc))
				throw new Exception(sepo + " duplicate o");
		}
		return seps;
	}

	static class NativeKeyLocal {
		String local;
		String ext;
		Map<Long, String> seps;

		NativeKeyLocal(String loc, String ex, Map<Long, String> sep) {
			local = loc;
			ext = ex;
			seps = sep;
		}
	}

	private LocalizeManager manager;
	private String beanName;
	private String beanLocalName;

	private Map<String, NativeKeyLocal> nativeKeyLocals = new TreeMap<String, NativeKeyLocal>();
	private Map<String, String> valueLocals = new TreeMap<String, String>();

	private Element element;
	private String xmlDescription;

	LocalizeImpl(LocalizeManager man, Element ele) throws Exception {
		manager = man;
		element = ele;
		beanName = ele.getAttribute("bean");
		beanLocalName = ele.getAttribute("local");

		if (beanName.isEmpty() || beanLocalName.isEmpty())
			throw new Exception("bean or local empty");

		for (Element e : DomHelper.getElementsByName(ele, "key")) {
			String o = e.getAttribute("o");
			String local = e.getAttribute("local");
			if (o.isEmpty() || local.isEmpty())
				throw new Exception("o or local empty");

			String ext = e.getAttribute("extends");

			if (null != nativeKeyLocals.put(o, new NativeKeyLocal(local, ext, getSeps(e))))
				throw new Exception(o + " duplicate key");
		}

		for (Element e : DomHelper.getElementsByName(ele, "value")) {
			String o = e.getAttribute("o");
			String local = e.getAttribute("local");
			if (o.isEmpty() || local.isEmpty())
				throw new Exception("o or local empty");

			if (null != valueLocals.put(o, local))
				throw new Exception(o + " duplicate value");
		}

		xmlDescription = xml();

	}

	Element getElement() {
		return element;
	}

	@Override
	public String getName() {
		return beanName;
	}

	@Override
	public String getXmlDescription() {
		return xmlDescription;
	}

	private String xml() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element ele = document.createElement("localize");
		ele.setAttribute("bean", beanName);
		ele.setAttribute("local", beanLocalName);

		for (Map.Entry<String, NativeKeyLocal> entry : nativeKeyLocals.entrySet()) {
			Element e = document.createElement("key");
			e.setAttribute("o", entry.getKey());
			NativeKeyLocal kl = entry.getValue();
			e.setAttribute("local", kl.local);

			if (!kl.ext.isEmpty())
				e.setAttribute("extends", kl.ext);

			for (Map.Entry<Long, String> ent : kl.seps.entrySet()) {
				Element e2 = document.createElement("sep");
				e2.setAttribute("o", String.valueOf(ent.getKey()));
				e2.setAttribute("local", ent.getValue());
				e.appendChild(e2);
			}

			ele.appendChild(e);
		}

		for (Map.Entry<String, String> entry : valueLocals.entrySet()) {
			Element e = document.createElement("value");
			e.setAttribute("o", entry.getKey());
			e.setAttribute("local", entry.getValue());
			ele.appendChild(e);
		}

		document.appendChild(ele);
		return DomHelper.documentToString(document);
	}

	@Override
	public String getLocalName() {
		return beanLocalName;
	}

	@Override
	public Map<String, KeyLocal> getKeyLocals() {
		Map<String, KeyLocal> map = new TreeMap<String, KeyLocal>();
		for (Map.Entry<String, NativeKeyLocal> entry : nativeKeyLocals.entrySet()) {
			NativeKeyLocal nkl = entry.getValue();
			Map<Long, String> seps = new TreeMap<Long, String>();
			if (!nkl.ext.isEmpty()) {
				BaseKey bk = manager.getBaseKey(nkl.ext);
				if (null != bk) {
					seps.putAll(bk.getSeps());
				}
			}
			seps.putAll(nkl.seps);

			map.put(entry.getKey(), new KeyLocal(nkl.local, seps));
		}
		return map;
	}

	@Override
	public Map<String, String> getValueLocals() {
		return valueLocals;
	}

	@Override
	public boolean isBaseKey() {
		return false;
	}

}