package xmerge;

import java.util.*;

import mkdb.util.DatabaseMetaData;
import mkdb.util.DatabaseMetaData.*;
import mkdb.util.DatabaseMetaData.Bean.Variable;
import mkdb.util.DatabaseMetaData.Type.TypeId;

/* table<key, value>
 * key 目前支持的Type为boolean, int , short, long, string, float, cbean
 * value 目前支持的Type为boolean, int, short, long, string, float, bean
 */

public class ForeignMeta {
	private DatabaseMetaData curMeta;
	enum REF_TYPE {KEY, VALUE}
	private Map<String, TableDepends> depRoots = new TreeMap<String, TableDepends>();
	private Map<String, Map<String, String>> foreigns = new HashMap<String, Map<String, String>>();
		
	public ForeignMeta() { 
		curMeta = DatabaseMetaData.getInstance();
	}
	
	public Map<String, TableDepends> getDepRoots() {
		return depRoots;
	}
	
	public Map<String, String> getForeigns(String tableName) {
		return foreigns.get(tableName);
	}
		
	public void initDepRoots() {	
		initNonRefTables();
		for (Table table : curMeta.getTables()) {
			if (!table.isPersistence())
				continue;
			// 可以并存，即同时是key和value都foreign
			{
				if (table.getForeign().getKey() != null) {
					addRef(table.getName(), new VarPath("key"), table.getForeign().getKey(), REF_TYPE.KEY);
				}
				if (table.getForeign().getValue() != null) {
					addRef(table.getName(), new VarPath("value"), table.getForeign().getValue(), REF_TYPE.VALUE);
				}				
			}
			{
				switch (table.getValueType().getTypeId()) {
				case BOOLEAN:
				case INT:
				case SHORT:
				case LONG:
				case STRING:
				case FLOAT:
				case BINARY:
					// 简单类型的foreign记录在table.getForeign()里
					break;
				case BEAN:
					Bean bean = curMeta.getBean(table.getValueName());
					findBeanRef(bean, new VarPath("value"), table);
				default:
					// 不可能情况，暂时不处理	
				}
			}
		}
		addNonRefToDepRoots();
		initForeigns();
//		dumpDepRoots();
//		mkdb.Trace.info(foreigns);
	}
	
	private boolean findBeanRef(Bean bean, VarPath varPath, Table table) {
		for (Variable var : bean.getVariables()) {
			switch (var.getTypeType().getTypeId()) {
			case BOOLEAN:
			case INT:
			case SHORT:
			case LONG:
			case STRING:
			case FLOAT:
				if (var.getForeign().getValue() != null) {
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					addRef(table.getName(), curVarPath, var.getForeign().getValue(), REF_TYPE.VALUE);
				}
				break;
			case LIST:
			case VECTOR:
			case SET:
				if (var.getForeign().getValue() != null) {
					// list,vector,set为foreign时，value只能是cbean而不能是xbean，是xbean的话在xbean里面定义
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					addRef(table.getName(), curVarPath, var.getForeign().getValue(), REF_TYPE.VALUE);
				}
				// 继续找value(xbean)里面的
				if (var.getValueType().getTypeId() == TypeId.BEAN) {
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					findBeanRef(curMeta.getBean(var.getValueTypeName()), curVarPath, table);
				}	
				break;
			case MAP:
			case TREEMAP:
				/*
				 * .<keyforeign>	:	key本身为foreign
				 * .<valueforeign>	:	value本身为foreign
				 * .<key>			:	key为bean，里面var为foreign，应该不可能，这里也列出来
				 * .<value>			:	value为bean，里面var为foreign
				 */
				if (var.getForeign().getKey() != null) {
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					curVarPath.addPath("<keyforeign>");
					addRef(table.getName(), curVarPath, var.getForeign().getKey(), REF_TYPE.VALUE);
				}
				if (var.getForeign().getValue() != null) {
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					curVarPath.addPath("<valueforeign>");
					addRef(table.getName(), curVarPath, var.getForeign().getValue(), REF_TYPE.VALUE);
				}
				if (var.getKeyType().getTypeId() == TypeId.BEAN) {
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					curVarPath.addPath("<key>");
					findBeanRef(curMeta.getBean(var.getKeyTypeName()), curVarPath, table);
				}
				if (var.getValueType().getTypeId() == TypeId.BEAN) {
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					curVarPath.addPath("<value>");
					findBeanRef(curMeta.getBean(var.getValueTypeName()), curVarPath, table);
				}	
				break;
			case BEAN:
				if (var.getForeign().getValue() != null) {
					// cbean
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					addRef(table.getName(), curVarPath, var.getForeign().getValue(), REF_TYPE.VALUE);
				} else {
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					findBeanRef(curMeta.getBean(var.getTypeName()), curVarPath, table);
				}
				break;
			case BINARY:
			case ANY:
			default:
				if (var.getForeign().getValue() != null) {
					// binary foreign="warn", do callback()
					// 暂时不处理，这里严格限制不允许出现这种情况
					throw new IllegalArgumentException("illegal foreign type, var = " + var);
				}
				break; 				
			}			
		}
		return true;
	}

	List<String> nonRefTables = new ArrayList<String>();
	private void initNonRefTables() {
		for (DatabaseMetaData.Table table : curMeta.getTables()) {
			if (!table.isPersistence())
				continue;
			nonRefTables.add(table.getName());
		}
	}
	
	private void addNonRefToDepRoots() {
		for (String table : nonRefTables) {
			if (depRoots.get(table) == null) {
				TableDepends newRoot = new TableDepends(table);
				depRoots.put(table, newRoot);
			} else {
				System.err.println("create depRoots fail. tableName = " + table);
			}
		}
	}
	
	public void dumpDepRoots() {
		Iterator<Map.Entry<String, TableDepends>> itr = depRoots.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, TableDepends> entry = itr.next();
			TableDepends depends = entry.getValue();
			mkdb.Trace.info("Root：");
			dumpDepTree(depends);
		}
	}
	
	public void dumpDepTree(TableDepends dep) {
		mkdb.Trace.info(dep.getName());
		for (VarPath var : dep.getVars()) {
			mkdb.Trace.info("\t" + var);
		}
		{
			Iterator<Map.Entry<String, TableDepends>> itr = dep.getRefKeyTables().entrySet().iterator();
			while (itr.hasNext()) {
				mkdb.Trace.info("\tkeyRef: ");
				Map.Entry<String, TableDepends> entry = itr.next();
				TableDepends depends = entry.getValue();
				dumpDepTree(depends);
			}			
		}
		{
			Iterator<Map.Entry<String, TableDepends>> itr = dep.getRefValueTables().entrySet().iterator();
			while (itr.hasNext()) {
				mkdb.Trace.info("\tvalueRef: ");
				Map.Entry<String, TableDepends> entry = itr.next();
				TableDepends depends = entry.getValue();
				dumpDepTree(depends);
			}			
		}
	}
	
	private void initForeigns() {
		Iterator<Map.Entry<String, TableDepends>> itr = depRoots.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, TableDepends> entry = itr.next();
			TableDepends depends = entry.getValue();
			initTree(depends);
		}
	}
	
	private void initTree(TableDepends dep) {
		{
			Iterator<Map.Entry<String, TableDepends>> itr = dep.getRefKeyTables().entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, TableDepends> entry = itr.next();
				TableDepends depends = entry.getValue();
				if (foreigns.get(depends.getName()) == null) {
					Map<String, String> vars = new HashMap<String, String>();
					vars.put("key", dep.getName());
					foreigns.put(depends.getName(), vars);
				} else {
					foreigns.get(depends.getName()).put("key", dep.getName());
				}
				initTree(depends);
			}			
		}
		{
			Iterator<Map.Entry<String, TableDepends>> itr = dep.getRefValueTables().entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, TableDepends> entry = itr.next();
				TableDepends depends = entry.getValue();
				if (foreigns.get(depends.getName()) == null) {
					Map<String, String> vars = new HashMap<String, String>();
					for (VarPath var : depends.getVars()) {
						vars.put(var.toString(), dep.getName());
					}
					foreigns.put(depends.getName(), vars);
				} else {
					for (VarPath var : depends.getVars()) {
						foreigns.get(depends.getName()).put(var.toString(), dep.getName());
					}
				}
			}			
		}
	}
	
	public boolean addRef(String srcNodeName, VarPath varPath, String destNodeName, REF_TYPE type) {
		// Util.traceln(srcNodeName + ":" + destNodeName + ":" + foreignVar + ":" + type + ":" + depRoots.size());
		// 在向depRoots中加入keyRef时，将srcNodeName和destNodeName从nonRefTables里删除
		if (type == REF_TYPE.KEY) {
			nonRefTables.remove(destNodeName);
			nonRefTables.remove(srcNodeName);
		}		
		for (TableDepends root : depRoots.values()) {
			// destNodeName为根
			if (root.getName().equals(destNodeName)) {
				return root.addNode(srcNodeName, varPath, type);
			}
			// 在root的子树里找
			if (root.addRef(srcNodeName, varPath, destNodeName, type))
				return true;
			// 是不是父节点，srcNodeName等于当前的根
			if (root.getName().equals(srcNodeName) && type == REF_TYPE.KEY) {
				// key为foreign，强关联，需要调整destNodeName为root
				// 需要考虑srcNodeName和destNodeName同时为根，且之前没有强关联的情况
				TableDepends newRoot = (newRoot = depRoots.get(destNodeName)) == null ? new TableDepends(destNodeName) : newRoot;
				newRoot.addNode(root, type);
				// 此处不需要了，因为type == REF_TYPE.KEY
				// newRoot.addVar(foreignVar, type);
				depRoots.remove(root.getName());
				depRoots.put(destNodeName, newRoot);
				return true;
			}
			if (root.getName().equals(srcNodeName) && type == REF_TYPE.VALUE) {
				// value为foreign，弱关联，不调整，继续找
			}			
		}
		// 所有根及子树里均没有找到，srcNodeName可能是树根，但此种情况下type == REF_TYPE.VALUE，不用调整
		TableDepends newRoot = new TableDepends(destNodeName);
		newRoot.addNode(srcNodeName, varPath, type);
		depRoots.put(destNodeName, newRoot);
		// 在向depRoots中加入valueRef时，添加destNodeName为根，将destNodeName从nonRefTables里删除
		nonRefTables.remove(destNodeName);
		return true;
	}
	
	public boolean isKeyRef(String tableName, String refTableName) {
		String name = getForeigns(refTableName).get("key");
		if (name != null && name.equals(tableName)) {
			return true;
		}
		return false;
	}

	public TableDepends findRefDepends(String tableName, String refTableName) {
		for (TableDepends root : depRoots.values()) {
			TableDepends dep = findDepTree(root, tableName, refTableName);
			if (dep != null)
				return dep;
		}
		return null;
	}
	
	private TableDepends findDepTree(TableDepends root, String tableName, String refTableName) {
		if (root.getName().equals(tableName) && root.getRefValueTables().get(refTableName) != null)
			return root.getRefValueTables().get(refTableName);
		for (TableDepends node : root.getRefKeyTables().values()) {
			TableDepends dep = findDepTree(node, tableName, refTableName);
			if (dep != null)
				return dep;
		}
		return null;
	}

	class VarPath {
		private List<String> varPath = new ArrayList<String>();
		private int curAccess = 0;
		private int length = 0;
		private boolean startWithKey = false;
		private boolean startWithValue = false; 
		
		public VarPath(String start) {
			if (start == null || start.isEmpty())
				throw new IllegalArgumentException("illegal VarPath start location.");
			varPath.add(start);
			if (start.equals("key")) {
				startWithKey = true;
			} else if (start.equals("value")) {
				startWithValue = true;
			}
			length = varPath.size();
		}
		
		public VarPath(VarPath other) {
			for (String path : other.varPath) {
				this.varPath.add(path);
			}
			this.length = varPath.size();
			this.startWithKey = other.startWithKey;
			this.startWithValue = other.startWithValue;
		}

		public VarPath copy() {
			return new VarPath(this);
		}
		
		public VarPath copyWithAccess() {
			VarPath newObj = new VarPath(this);
			newObj.curAccess = this.curAccess;
			return newObj;
		}
		
		public int getAccess() {
			return curAccess;
		}
		
		public void setAccess(int access) {
			this.curAccess = access;
		}
		
		public void addPath(String path) {
			varPath.add(path);
			length = varPath.size();
		}
		
		public boolean startWithKey() {
			return startWithKey;
		}
		
		public boolean onlyWithValue() {
			return startWithValue && (length == 1);
		}
		
		public boolean hasNext() {
			return (curAccess < length);
		}
		
		public String next() {
			return varPath.get(curAccess++);
		}
		
		public String current() {
			return varPath.get(curAccess);
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (String path : varPath) {
				sb.append(path + ".");
			}
			return sb.substring(0, sb.length() - 1);
		}
	}
	
	public class TableDepends {
		String tableName;
		private List<VarPath> vars = new ArrayList<VarPath>();
		private Map<String, TableDepends> refKeyTables = new TreeMap<String, TableDepends>();
		private Map<String, TableDepends> refValueTables = new TreeMap<String, TableDepends>();
		
		public TableDepends(String tableName) { this.tableName = tableName; }
		public String getName() { return tableName; }
		public Map<String, TableDepends> getRefKeyTables() { return refKeyTables; }
		public Map<String, TableDepends> getRefValueTables() { return refValueTables; }
		public List<VarPath> getVars() { return vars; }
		
		public boolean addRef(String srcNodeName, VarPath varPath, String destNodeName, REF_TYPE type) {
			// 只在refKeyTables里找，即其父节点必须为强关联时才能addRef
			for (TableDepends node : refKeyTables.values()) {
				if (node.getName().equals(destNodeName)) {
					return node.addNode(srcNodeName, varPath, type);
				}
				if (node.getName().equals(srcNodeName) && type == REF_TYPE.KEY) {
					// 非法情况，从srcNodeName出发只允许一条强关联，从逻辑上说不可能出现
				}
				// 还不是，递归遍历子树
				if (node.addRef(srcNodeName, varPath, destNodeName, type))
					return true;				
			}
			// 最后也没找到
			return false;
		}

		public boolean addNode(String srcNodeName, VarPath varPath, REF_TYPE type) {
			// 检查是否已经存在了
			TableDepends newNode = (newNode = getNode(srcNodeName, type)) == null ? new TableDepends(srcNodeName) : newNode;
			newNode.addVar(varPath, type);
			return addNode(newNode, type);
		}
		
		private boolean addVar(VarPath varPath, REF_TYPE type) {
			if (type == REF_TYPE.VALUE) {
				vars.add(varPath);
			}
			return true;	
		}
		
		private TableDepends getNode(String srcNodeName, REF_TYPE type) {
			if (type == REF_TYPE.KEY)
				return refKeyTables.get(srcNodeName);
			if (type == REF_TYPE.VALUE)
				return refValueTables.get(srcNodeName);
			return null;
		}
		
		private boolean addNode(TableDepends node, REF_TYPE type) {
			if (type == REF_TYPE.KEY)
				refKeyTables.put(node.getName(), node);
			if (type == REF_TYPE.VALUE)
				refValueTables.put(node.getName(), node);	
			return true;
		}
	}
}