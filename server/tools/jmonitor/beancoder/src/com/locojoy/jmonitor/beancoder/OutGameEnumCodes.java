package com.locojoy.jmonitor.beancoder;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class OutGameEnumCodes {

	private MXBeanData			beandata;
	private Coder					coder;

	final private Collection<BeanVariable> enumvariables = new LinkedList<BeanVariable>();
	
	public OutGameEnumCodes( MXBeanData data, Coder c) {
		beandata = data;
		coder = c;
		
		makeVariables();
	}
	
	private final void makeVariables() {

		final Map<String,BeanData> beandatas = coder.getBeandatas();
		final BeanData keybeandata = beandatas.get( beandata.getMXBeanType().getKeyBeanDataType());
		
		for( final BeanVariable bv : keybeandata.getVariables()) {
			if( bv.isEnum())
				enumvariables.add( bv);
		}
	}
	
	public final void outCodes( PrintStream ps) {
		final Map<String,BeanData> beandatas = coder.getBeandatas();
		final BeanData valuebeandata = beandatas.get( beandata.getMXBeanType().getValueBeanDataType());
		
		for( final BeanVariable bv : valuebeandata.getVariables())
			outValueBeanVariable( bv, ps);
	}
	
	private final void outValueBeanVariable( final BeanVariable valuebean, PrintStream ps) {
		for( final BeanVariable enumv : enumvariables)
			outEnumValueBeanVariable( valuebean, enumv, ps);
	}
	
	private final void outValueBeanArgs( final BeanData keybeandata, final BeanVariable enumbean, PrintStream ps) {
		final Collection<BeanVariable> enumbeans = new LinkedList<BeanVariable>();
		enumbeans.add( enumbean);
		outValueBeanArgs( keybeandata, enumbeans, ps);
	}

	private final void outValueBeanArgs( final BeanData keybeandata, final Collection<BeanVariable> enumbeans, PrintStream ps) {
		final Collection<BeanVariable> keybeans = keybeandata.getVariables();
		if( keybeans.size() == enumbeans.size())
			return;
		for( BeanVariable bv : keybeans) {
			if( enumbeans.contains( bv))
				continue;
				ps.print( bv.getType() + " " + bv.getName() + ", ");
		}
	}

	private final void outValueBeanVariable( final BeanData keybeandata, final BeanVariable enumbean, final EnumItem enumitem, PrintStream ps) {
		final Map<BeanVariable,EnumItem> enumbeans = new HashMap<BeanVariable,EnumItem>();
		enumbeans.put( enumbean, enumitem);
		outValueBeanVariable( keybeandata, enumbeans, ps);
	}

	private final void outValueBeanVariable( final BeanData keybeandata, final Map<BeanVariable,EnumItem> enumbeans, PrintStream ps) {
		final Collection<BeanVariable> keybeans = keybeandata.getVariables();
		if( keybeans.size() == enumbeans.size())
			return;
		for( BeanVariable bv : keybeans) {
			EnumItem ei = enumbeans.get( bv);
			if( null == ei)
				ps.print( bv.getName() + ", ");
			else
				ps.print( bv.getName() + "_Value_" + ei.getName() + ", ");
		}
	}

	private final void outEnumValueBeanVariable( final BeanVariable valuebean, final BeanVariable enumbean, PrintStream ps) {
		final Map<String,BeanData> beandatas = coder.getBeandatas();
		final String rolebasename = coder.getRolebase().getName();
		final BeanData keybeandata = beandatas.get( beandata.getMXBeanType().getKeyBeanDataType());

		for( EnumItem ei : enumbean.getEnumItems()) {
			ps.print( "\tpublic void set" + valuebean.getName() + ei.getName() + "Value( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "long roleid, ");
				outValueBeanArgs( keybeandata, enumbean, ps);
			} else if( keybeandata.getExtendClass().isEmpty()) {
				outValueBeanArgs( keybeandata, enumbean, ps);
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( valuebean.getType() + " _value_) {");
			
			ps.print( "\t\tset" + valuebean.getName() + "Value( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "roleid, ");
				outValueBeanVariable( keybeandata, enumbean, ei, ps);
			} else if( keybeandata.getExtendClass().isEmpty()) {
				outValueBeanVariable( keybeandata, enumbean, ei, ps);
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( " _value_);");
			ps.println( "\t}");
			ps.println();
		}
	}
}
