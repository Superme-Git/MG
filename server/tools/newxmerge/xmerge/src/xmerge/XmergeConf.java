package xmerge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.locojoy.base.Marshal.OctetsStream;



public final class XmergeConf {
	
	private final Map<String, Table> tables = new HashMap<String, Table>();
	private final Map<String, Table> cleanTables = new HashMap<String, Table>();
	private final Map<String, Table> preProcessTables = new HashMap<String, Table>();
	
	public Map<Long, Integer> facKey2Id = new HashMap<Long, Integer>();
	public Map<Long, Integer> famKey2Id = new HashMap<Long, Integer>();
	
	private static String xmergexml = null;
	
	public static final String SRCDIR = "srcDB";
	public static final String DESTDIR = "destDB";
	public static final String RESDIR = "resultDB";
	public static final String ERRKEYDIR = "errKeyDB";
	public static final String ERRKEYFOREIGNDIR = "errForeignDB";
	public static final String FOREIGN_TABLE_NAME = "foreigntable";
	public static final long MAX_WALK_COUNT = 100000;
	
	private String srcDbHome;
	private String destDbHome;
	private String resultDbHome;
	private String errorKeysDbHome;
	
	private String defaultWorkDbHome = destDbHome;
	private String foreignInfoDbHome;
	private String foreignInfoTableName;
	private String libPath = "bin";
	
	private static XmergeConf instance;
	
	public void setSrcDbHome(String srcHome){
		if (null == srcHome || srcHome.isEmpty())
				throw new Error("setSrcDbHome error ... ...");
		this.srcDbHome = srcHome;
	}
	
	public void setDestDbHome(String destHome){
		if (null == destHome || destHome.isEmpty())
			throw new Error("setDestDbHome error ... ...");
		this.destDbHome = destHome;
		this.defaultWorkDbHome = destDbHome;
	}
	
	public void setResultDbHome(String resultHome) {
		if (null == resultHome || resultHome.isEmpty())
			throw new Error("setResultDbHome error ... ...");
		this.resultDbHome = resultHome;
	}
	
	public void setErrorKeysDbHome(String errorHome) {
		if (null == errorHome || errorHome.isEmpty())
			throw new Error("setErrorKeysDbHome error... ...");
		this.errorKeysDbHome = errorHome;
	}
	
	public void setForeignInfoDbHome(String foreignInfoDbHome) {
		if (null == foreignInfoDbHome || foreignInfoDbHome.isEmpty())
			throw new Error("setForeignInfoDbHome error... ...");
		this.foreignInfoDbHome = foreignInfoDbHome;
	}
	
	public void setForeignInfoTableName(String foreignInfoTableName) {
		this.foreignInfoTableName = foreignInfoTableName;
	}
	
	public void setLibPath(String libpath) {
		if (null == libpath || libpath.isEmpty())
			throw new Error("libpath : " + libpath + " is invalid... ...");
		this.libPath = libpath;
	}
	
	public String getSrcDbHome() { return this.srcDbHome; }
	public String getDestDbHome(){ return this.destDbHome; }
	public String getResultDbHome() { return this.resultDbHome; }
	public String getErrorKeysDbHome() { return this.errorKeysDbHome; }	
	public String getDefaultWorkDbHome() { return defaultWorkDbHome; }
	public String getForeignInfoDbHome() { return foreignInfoDbHome; }
	public String getForeignInfoTableName() { return foreignInfoTableName; }
	public String getLibPath() { return this.libPath; }
	
	public void addTable(String tableName, boolean isKnown, String className){
		this.tables.put(tableName, new Table(tableName, isKnown, className));
	}
	
	public void addTable(String tableName, boolean isKnown, IMerge merger){
		this.tables.put(tableName, new Table(tableName, isKnown, merger));
	}
	
	public void removeTable(String tableName){
		this.tables.remove(tableName);
	}
	
	/**
	 * <li>parse xmerge.xml
	 * <li>initialize work space
	 * */
	private XmergeConf(String xmergexml){
		try {
			if(null != xmergexml){
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmergexml);
				parse(doc.getDocumentElement());
			}else
				throw new RuntimeException("need xmerge.xml");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// set default
//		setSrcDbHome(SRCDIR);
//		setDestDbHome(DESTDIR);
//		setResultDbHome(RESDIR);
//		setErrorKeysDbHome(ERRKEYDIR);
//		setForeignInfoTableName(FOREIGN_TABLE_NAME);
	}
			
	public static XmergeConf getInstance(){
		if(null == instance){
			if(null != XmergeConf.xmergexml)
				instance = new XmergeConf(XmergeConf.xmergexml);
			else 
				instance = new XmergeConf("./xmerge.xml");
		}
		return instance;
	}
	
	public static void SetConf(String xmergexml){
		if(null == XmergeConf.xmergexml)
			XmergeConf.xmergexml = xmergexml;
	}

	private void parse(Element root) throws Exception {
		
		String libPath = root.getAttribute("libpath");
		String srcDir = root.getAttribute("srcDir");
		String destDir = root.getAttribute("destDir");
		String resultDir = root.getAttribute("resultDir");
		
		if(null != srcDir) setSrcDbHome(srcDir);
		if(null != destDir) setDestDbHome(destDir);
		if(null != resultDir) setResultDbHome(resultDir);
		if(null != libPath) setLibPath(libPath);
		
		NodeList childnodes = root.getChildNodes();
		
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element e = (Element) node;
			String nodename = e.getNodeName();

			if (nodename.equals("cleartable")){
				String tablename = e.getAttribute("name");
				cleanTables.put(tablename, new Table(tablename,true, new IMerge() {
					
					@Override
					public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
				
						return false;
					}
				}));
			}else if (nodename.equals("preprocesstable")){
				String tablename = e.getAttribute("name");
				if(preProcessTables.containsKey(tablename)){
					throw new RuntimeException(tablename+" preprocesstable table 閰嶇疆閲嶅" );
				}
				preProcessTables.put(tablename, new Table(tablename,true, new IMerge() {
					
					@Override
					public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
						
						return false;
					}
				},e.getAttribute("class")));
			}
			else if (nodename.equals("table")){
				String tablename = e.getAttribute("name");
				String classname = e.getAttribute("class");
				String conflict = e.getAttribute("type");
				if(conflict.equals("known_key_conflict")){
					if(tables.containsKey(tablename)){
						throw new RuntimeException(tablename+" table 閰嶇疆閲嶅" );
					}
					tables.put(tablename, new Table(tablename, true, classname));
				}
			} else if (nodename.equals("allocator")) {
				String tablename = e.getAttribute("name");
				String classname = e.getAttribute("class");
				allocators.put(tablename, classname);
			} else {
				throw new RuntimeException("node=" + nodename);
			}
		}
	}
	
	private Map<String, String> allocators = new HashMap<String, String>();
	public Map<String, String> getAllocators() { return allocators; }
	
	public int getTablesCount(){
		return tables.size();
	}
	
	public Collection<Table> getTables() {
		return Collections.unmodifiableCollection(tables.values());
	}
	public Collection<Table> getCleanTables() {
		return Collections.unmodifiableCollection(cleanTables.values());
	}
	public Collection<Table> getPreprocessTables() {
		return Collections.unmodifiableCollection(preProcessTables.values());
	}
	
	public boolean isKnownConflict(String tablename){
		Table t = tables.get(tablename);
		if(null != t)
			return t.isConflict();
		else
			return false;
	}
	public boolean isCleanTable(String tablename){
		Table t = cleanTables.get(tablename);
		if(null != t)
			return true;
		else
			return false;
	}
	public boolean isPreProcessTable(String tablename){
		Table t = preProcessTables.get(tablename);
		if(null != t)
			return true;
		else
			return false;
	}
	public IPreprocess getPreprocess(String tableName){
		Table t = preProcessTables.get(tableName);
		if (t!=null){
			return t.getPreprocess();
		}
		return null;
	}
	public IMerge getMerger(String tablename){
		if(isKnownConflict(tablename))
			return tables.get(tablename).getMerger();
		return null;
	}
		
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("tables:\n");
		for (Table t : this.getTables()) {
			sb.append(t).append("\n");
		}
		sb.append("cleantables:\n");
		for (Table t : getCleanTables()) {
			sb.append(t).append("\n");
		}
		sb.append("Dirs:\n");
		sb.append("srcDir: ").append(this.srcDbHome).append("\n");
		sb.append("destDir: ").append(this.destDbHome).append("\n");
		sb.append("libpath: ").append(this.libPath).append("\n");
		return sb.toString();
		
	}

	public class Table {
		private final String name;
		private final boolean is_know_key_conflict;
		private final IMerge merger;
		private IPreprocess preprocess;
		
		public Table(String name, boolean is_know_key_conflict, String classname) {
			this.name = name;
			this.is_know_key_conflict = is_know_key_conflict;
			if (classname!=null&&classname.length()>0) {
			try{
				Class<?> mergeClass = Class.forName(classname);
				Object instance = mergeClass.newInstance();
				if (!(instance instanceof IMerge))
					throw new Error("invalid UserMergeImplement");
				this.merger = (IMerge)instance;
			}catch(Exception e){
				throw new Error(e);
			}
			}else {
				throw new Error("invalid merge name!");
			}
		}
		public Table(String name, boolean is_know_key_conflict, IMerge merger,String preProcessClassName) {
			this(name, is_know_key_conflict, merger);
		
			if (preProcessClassName!=null&&preProcessClassName.length()>0) {
				try{
					Class<?> preProcessClass = Class.forName(preProcessClassName);
					Object instance = preProcessClass.newInstance();
					if (!(instance instanceof IPreprocess))
						throw new Error("invalid IPreprocessImplement");
					this.preprocess = (IPreprocess)instance;
				}catch(Exception e){
					throw new Error(e);
				}
			}else {
				throw new Error("invalid preprocess name!");
			}
		}
		
		public Table(String name, boolean is_know_key_conflict, IMerge merger) {
			this.name = name;
			this.is_know_key_conflict = is_know_key_conflict;
			this.merger = merger;
		}

		@SuppressWarnings("unused")
		private String getName() {
			return name;
		}

		private boolean isConflict() {
			return is_know_key_conflict;
		}
		
		private IMerge getMerger() {
			return merger;
		}
		private IPreprocess getPreprocess() {
			return preprocess;
		}
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(this.name);
			sb.append("->").append(this.is_know_key_conflict ? "know":"unknow");
			sb.append("->").append(this.merger.getClass().getName());
			return sb.toString();
		}
	}
	
//	public static void main(String args[]) {
//		System.setProperty("line.separator", "\n");
//		System.out.println( XmergeConf.getInstance().toString());
//
//	}
}

