package xmerge;

import java.util.*;

import mkdb.util.DatabaseMetaData;
import mkdb.util.DatabaseMetaData.*;
import mkdb.util.DatabaseMetaData.Bean.Variable;
import mkdb.util.DatabaseMetaData.Type.TypeId;

/* table<key, value>
 * key Ŀǰ֧�ֵ�TypeΪboolean, int , short, long, string, float, cbean
 * value Ŀǰ֧�ֵ�TypeΪboolean, int, short, long, string, float, bean
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
			// ���Բ��棬��ͬʱ��key��value��foreign
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
					// �����͵�foreign��¼��table.getForeign()��
					break;
				case BEAN:
					Bean bean = curMeta.getBean(table.getValueName());
					findBeanRef(bean, new VarPath("value"), table);
				default:
					// �������������ʱ������	
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
					// list,vector,setΪforeignʱ��valueֻ����cbean��������xbean����xbean�Ļ���xbean���涨��
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					addRef(table.getName(), curVarPath, var.getForeign().getValue(), REF_TYPE.VALUE);
				}
				// ������value(xbean)�����
				if (var.getValueType().getTypeId() == TypeId.BEAN) {
					VarPath curVarPath = varPath.copy();
					curVarPath.addPath(var.getName());
					findBeanRef(curMeta.getBean(var.getValueTypeName()), curVarPath, table);
				}	
				break;
			case MAP:
			case TREEMAP:
				/*
				 * .<keyforeign>	:	key����Ϊforeign
				 * .<valueforeign>	:	value����Ϊforeign
				 * .<key>			:	keyΪbean������varΪforeign��Ӧ�ò����ܣ�����Ҳ�г���
				 * .<value>			:	valueΪbean������varΪforeign
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
					// ��ʱ�����������ϸ����Ʋ���������������
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
			mkdb.Trace.info("Root��");
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
		// ����depRoots�м���keyRefʱ����srcNodeName��destNodeName��nonRefTables��ɾ��
		if (type == REF_TYPE.KEY) {
			nonRefTables.remove(destNodeName);
			nonRefTables.remove(srcNodeName);
		}		
		for (TableDepends root : depRoots.values()) {
			// destNodeNameΪ��
			if (root.getName().equals(destNodeName)) {
				return root.addNode(srcNodeName, varPath, type);
			}
			// ��root����������
			if (root.addRef(srcNodeName, varPath, destNodeName, type))
				return true;
			// �ǲ��Ǹ��ڵ㣬srcNodeName���ڵ�ǰ�ĸ�
			if (root.getName().equals(srcNodeName) && type == REF_TYPE.KEY) {
				// keyΪforeign��ǿ��������Ҫ����destNodeNameΪroot
				// ��Ҫ����srcNodeName��destNodeNameͬʱΪ������֮ǰû��ǿ���������
				TableDepends newRoot = (newRoot = depRoots.get(destNodeName)) == null ? new TableDepends(destNodeName) : newRoot;
				newRoot.addNode(root, type);
				// �˴�����Ҫ�ˣ���Ϊtype == REF_TYPE.KEY
				// newRoot.addVar(foreignVar, type);
				depRoots.remove(root.getName());
				depRoots.put(destNodeName, newRoot);
				return true;
			}
			if (root.getName().equals(srcNodeName) && type == REF_TYPE.VALUE) {
				// valueΪforeign������������������������
			}			
		}
		// ���и����������û���ҵ���srcNodeName�����������������������type == REF_TYPE.VALUE�����õ���
		TableDepends newRoot = new TableDepends(destNodeName);
		newRoot.addNode(srcNodeName, varPath, type);
		depRoots.put(destNodeName, newRoot);
		// ����depRoots�м���valueRefʱ�����destNodeNameΪ������destNodeName��nonRefTables��ɾ��
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
			// ֻ��refKeyTables���ң����丸�ڵ����Ϊǿ����ʱ����addRef
			for (TableDepends node : refKeyTables.values()) {
				if (node.getName().equals(destNodeName)) {
					return node.addNode(srcNodeName, varPath, type);
				}
				if (node.getName().equals(srcNodeName) && type == REF_TYPE.KEY) {
					// �Ƿ��������srcNodeName����ֻ����һ��ǿ���������߼���˵�����ܳ���
				}
				// �����ǣ��ݹ��������
				if (node.addRef(srcNodeName, varPath, destNodeName, type))
					return true;				
			}
			// ���Ҳû�ҵ�
			return false;
		}

		public boolean addNode(String srcNodeName, VarPath varPath, REF_TYPE type) {
			// ����Ƿ��Ѿ�������
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