package com.locojoy.jmonitor.webapp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.locojoy.jmonitor.util.DomHelper;



public class AccessManager {
	
	static public class Forbid{
		private String path;
		private Set<String> methods = new HashSet<String>();
		
		Forbid(Element e) throws Exception{
			path = e.getAttribute("path");
			if (path.length() == 0)
				throw new Exception("forbid path empty");
			for(String m: e.getAttribute("methods").split(","))
			{
				String tm = m.trim();
				if (!tm.isEmpty())
					methods.add(tm.toUpperCase());
			}
			if (methods.isEmpty())
				throw new Exception("forbid methods empty");
		}
		
		public String getPath(){
			return path;
		}
		
		public Set<String> getMethods(){
			return methods;
		}
	}
	
	private Map<String, List<Forbid>> role_forbids_map = new HashMap<String, List<Forbid>> ();
	private ConcurrentHashMap<String, String>  user_role_map  = new ConcurrentHashMap<String, String> ();
	private Map<String, Element> role_ele_map = new HashMap<String, Element> ();
	
	private String xmlfile;
	private Document document; 
	
	
	public AccessManager(String xmlfn) throws Exception{
		xmlfile = xmlfn;
		
		File file = new File(xmlfile);
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		Element ele = document.getDocumentElement();
		
		for(Element e: DomHelper.getElementsByName(ele, "access")){
			String role = e.getAttribute("role");
			if (role.length() == 0)
				throw new Exception("access role empty");
			
			List<Forbid> fbs = new ArrayList<Forbid>();
			for(Element fe : DomHelper.getElementsByName(e, "forbid"))
				fbs.add( new Forbid(fe) );
			
			role_forbids_map.put(role, fbs);
		}
		
		for(Element e: DomHelper.getElementsByName(ele, "role")){
			String role = e.getAttribute("name");
			if (role.length() == 0)
				throw new Exception("role name empty");

			role_ele_map.put(role, e);
			
			for(Element ue : DomHelper.getElementsByName(e, "user")){
				String user = ue.getAttribute("name");
				if (user.length() == 0)
					throw new Exception("role user empty");
				if (null != user_role_map.put(user, role))
					throw new Exception("user " + user + " multiple role");
			}
		}
		
	}
	
	public Map<String, List<Forbid>> getRoleForbidsMap(){
		return role_forbids_map;
	}
	
	public synchronized Map<String, List<String>> getRoleUsersMap(){
		
		Map<String, List<String>> r2us = new HashMap<String, List<String>>(); 
		for(String r : role_ele_map.keySet())
			r2us.put(r, new ArrayList<String>());
		
		for(Map.Entry<String, String> e : user_role_map.entrySet()){
			String u = e.getKey();
			String r = e.getValue();
			
			List<String> us = r2us.get(r);
			if (null == us)
				r2us.put(r, us=new ArrayList<String>() );
			
			us.add(u);
		}
		
		return r2us;
	}
	
	public String getRole(String user){
		return user_role_map.get(user);
	}
	
	public boolean checkRolePermission(String role, String path, String method){
		if (null == role)
			return false;
		List<Forbid> fs = role_forbids_map.get(role);
		if (null == fs)
			return true;
		for(Forbid f : fs){
			if (f.methods.contains(method.toUpperCase()) && path.matches(f.path))
				return false;
		}
		
		return true;
	}
	
	public synchronized void add(String role, String user) throws Exception{
		if (user_role_map.containsKey(user))
			throw new Exception("user already in role");
		
		user_role_map.put(user, role);
		
		Element e = role_ele_map.get(role);
		Element n = document.createElement("user");
		n.setAttribute("name", user);
		e.appendChild(n);
		DomHelper.prettySave(document, xmlfile);
		
	}
	
	public synchronized boolean remove(String role, String user) throws Exception {
		if(null != user_role_map.remove(user)){
			Element e = role_ele_map.get(role);
			for( Element u : DomHelper.getElementsByName(e, "user")){
				if (u.getAttribute("name").equals(user)){
					e.removeChild(u);
					DomHelper.prettySave(document, xmlfile);
					break;
				}
			}
			return true;
		}
		return false;
	}
	
}
