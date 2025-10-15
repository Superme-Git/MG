package com.locojoy.jmonitor.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.w3c.dom.Element;

import com.locojoy.jmonitor.ChartChain;
import com.locojoy.jmonitor.util.DomHelper;

public class ChartChainManager {

	private Element element;

	private ConcurrentMap<String, ChartChainImpl> chain_map = new ConcurrentHashMap<String, ChartChainImpl>();

	ChartChainManager(Element ele) throws Exception {
		element = ele;
		for (Element e : DomHelper.getElementsByName(ele, "chartchain")) {
			ChartChainImpl c = new ChartChainImpl(this, e);
			if (null != chain_map.put(c.getChartChainName(), c))
				throw new Exception(c.getChartChainName() + " duplicate chartchain");
		}
	}

	List<ChartChain> getChartChains() {
		return new ArrayList<ChartChain>(chain_map.values());
	}

	void addChartChain(ChartChainImpl chain) throws Exception {
		if (null != chain_map.putIfAbsent(chain.getChartChainName(), chain))
			throw new Exception(chain.getChartChainName() + " already exist");

		element.appendChild(chain.getElement());
	}

	boolean removeChartChain(String chartChainName) throws Exception {
		ChartChainImpl c = chain_map.remove(chartChainName);
		if (null != c) {
			element.removeChild(c.getElement());
			return true;
		}
		return false;
	}

}
