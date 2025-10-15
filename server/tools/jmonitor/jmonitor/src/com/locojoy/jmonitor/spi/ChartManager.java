package com.locojoy.jmonitor.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.w3c.dom.Element;

import com.locojoy.jmonitor.util.DomHelper;

public class ChartManager {

	private Configure configure;
	private Element element;

	private ConcurrentMap<String, Chart> chart_map = new ConcurrentHashMap<String, Chart>();

	ChartManager(Configure config, Element ele) throws Exception {
		configure = config;
		element = ele;

		for (Element e : DomHelper.getElementsByName(ele, "chart")) {
			Chart c = new Chart(this, e);
			if (null != chart_map.put(c.getChartName(), c))
				throw new Exception(c.getChartName() + " duplicate chart");
		}
	}
	
	
	Configure getConfigure(){
		return configure;
	}
	

	List<Chart> getCharts() {
		return new ArrayList<Chart>(chart_map.values());
	}

	Chart getChart(String chartName) {
		return chart_map.get(chartName);
	}

	void addChart(Chart chart) throws Exception {
		if (null != chart_map.putIfAbsent(chart.getChartName(), chart))
			throw new Exception(chart.getChartName() + " already exist");

		element.appendChild(chart.getElement());
	}

	boolean removeChart(String chartName) throws Exception {
		Chart c = chart_map.remove(chartName);
		if (null != c) {
			element.removeChild(c.getElement());
			return true;
		}
		return false;
	}

}
