package fire.script;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import fire.pb.attr.AttrType;
import fire.pb.battle.BattleCfg;
import fire.pb.battle.ai.Condition;
import fire.pb.battle.ai.TargetFilter;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.javascript.SJavaScriptConfig;
import fire.pb.main.IModule;
import fire.pb.main.Main;
import fire.pb.main.ModuleMgr;

public class Module implements IModule {

	public final static String MODULE_NAME = "script";
	
	static Map<String, Integer> javaScriptExpr = new HashMap<String, Integer>(); 
	static Map<String, Integer> funExpr = new HashMap<String, Integer>();

	static Map<Integer, List<Integer>> funExprElements = new HashMap<Integer, List<Integer>>();
	private static boolean isEndRegister = false;
	public static Module getInstance()
	{
		return ((fire.script.Module)ModuleMgr.getInstance().getModuleByName(MODULE_NAME));
	}
	public static boolean registerJavaScriptExpr(String expr, boolean bfun, List<Integer> elements) {
		
		
		if (isEndRegister == false && javaScriptExpr.containsKey(expr) == false) {
			javaScriptExpr.put(expr, javaScriptExpr.size());
			if(bfun == true)
			{
				funExpr.put(expr, funExpr.size());
				if(elements != null && elements.size() > 0)
				{
					funExprElements.put(funExpr.size()-1, elements);
				}
			}
			return true;
		}
		if(isEndRegister == true)
			return false;
		return false;
	} 
	public static void setEndRegister(boolean isEndRegister)
	{
		Module.isEndRegister = isEndRegister;
	}

	private javax.script.ScriptEngineManager em;
	
	private javax.script.ScriptEngine se;

	Map<String, JavaScript> javaScripts = new HashMap<String, JavaScript>();

	
	List<String> extParamNames = new ArrayList<String>();
	
	public String converjs2fun(String js, List<Integer> elements)
	{
		String fun = js;
		String param = "with(Math)";
		fun = fun.replace(param, "");
		
		for (int i = 0; i < fire.pb.effect.Module.fightAttrTypeIds.length; i++)
		{
			if(fire.pb.effect.Module.fightAttrTypeIds[i] != AttrType.EFFECT_POINT)
			{
				String name = fire.pb.effect.Module.getInstance().getEffectNameById(fire.pb.effect.Module.fightAttrTypeIds[i]);
				if(fun.contains(name+"a"))
				{
					param = name+"a";
					fun = fun.replace(param, "opf.getEffectRole().getAttrById("+fire.pb.effect.Module.fightAttrTypeIds[i]+")");
	//				usedattrtypea.add(fire.pb.effect.Module.fightAttrTypeIds[i]);
				}
				if(fun.contains(name+"b"))
				{
					param = name+"b";
					fun = fun.replace(param, "aimf.getEffectRole().getAttrById("+fire.pb.effect.Module.fightAttrTypeIds[i]+")");
	//				usedattrtypeb.add(fire.pb.effect.Module.fightAttrTypeIds[i]);
				}
			}
			else
			{
				String name = fire.pb.effect.Module.getInstance().getEffectNameById(fire.pb.effect.Module.fightAttrTypeIds[i]);
				if(fun.contains(name+"a"))
				{
					param = name+"a";
					fun = fun.replace(param, "opf.getFighterBean().getInitattrs().get("+fire.pb.effect.Module.fightAttrTypeIds[i]+")");
	//				usedattrtypea.add(fire.pb.effect.Module.fightAttrTypeIds[i]);
				}
				if(fun.contains(name+"b"))
				{
					param = name+"b";
					fun = fun.replace(param, "aimf.getFighterBean().getInitattrs().get("+fire.pb.effect.Module.fightAttrTypeIds[i]+")");
	//				usedattrtypeb.add(fire.pb.effect.Module.fightAttrTypeIds[i]);
				}
			}
		}	
		for (int i = 0; i < fire.pb.effect.Module.extfightAttrTypeIds.length; i++)
		{
			String name = fire.pb.effect.Module.getInstance().getEffectNameById(fire.pb.effect.Module.extfightAttrTypeIds[i]);
			if(js.contains(name+"a"))
			{
				param = name+"a";
				fun = fun.replace(param, "opf.getEffectRole().getAttrById("+fire.pb.effect.Module.extfightAttrTypeIds[i]+")");
			}
			if(js.contains(name+"b"))
			{
				param = name+"b";
				fun = fun.replace(param, "aimf.getEffectRole().getAttrById("+fire.pb.effect.Module.extfightAttrTypeIds[i]+")");
			}
		}		
		
		param = "skilllevela";
		fun = fun.replace(param, "engine.getDouble(\"skillleveala\").intValue()");
		param = "skilllevel";
		fun = fun.replace(param, "engine.getDouble(\"skilllevel\").intValue()");
		param = "skillleveala";
		fun = fun.replace(param, "skilllevel");

		param = "gradea";		
		fun = fun.replace(param, "engine.getDouble(\"gradea\").intValue()");
		
		param = "gradeb";
		fun = fun.replace(param, "aimf.getEffectRole().getLevel()");

		for(String extParamName:extParamNames)
		{
			param = extParamName;
			String aimstr = "engine.getDouble(\""+param+"\").intValue()";
			fun = fun.replace(param, aimstr);
		}
		
		param = "random(";
		fun = fun.replace(param, "Math.random(");
		param = "floor(";
		fun = fun.replace(param, "Math.floor(");
		param = "max(";
		fun = fun.replace(param, "Math.max(");
		param = "min(";
		fun = fun.replace(param, "Math.min(");
		param = "pow(";
		fun = fun.replace(param, "Math.pow(");
		param = "abs(";
		fun = fun.replace(param, "Math.abs(");
		param = "round(";
		fun = fun.replace(param, "Math.round(");
		
		param = "pve";
		fun = fun.replace(param, "((boolean)engine.get(\"pve\"))");

		param = "havebuffa(";
		fun = fun.replace(param, "opf.getBuffAgent().existBuff(");
		param = "havebuffb(";
		fun = fun.replace(param, "aimf.getBuffAgent().existBuff(");

		if(elements!=null&&elements.size() > 0)
		{
			for (Integer element: elements) {
				// 生成函数并注册
				param = "_"+element+"_";
				if( 9999< element && element <100000)
					fun = fun.replace(param, "engine.getDouble(\"_"+element+"_\")");
				else
					fun = fun.replace(param, "(Boolean)engine.get(\"_"+element+"_\")");
			}	
		}
		
		return fun;
	}

	public Object eval(String script) throws ScriptException{
		return se.eval(script);
	}
	
	@Override
	public void exit() {
		
	}
	
	public JavaScript getJavaScript(String fileName) {
		JavaScript js = javaScripts.get(fileName);
		return js != null ? js : null;
	}	

	public String getJavaScriptExprFunName(String expr) {
		if (javaScriptExpr.containsKey(expr)) {
			int id = javaScriptExpr.get(expr);
			String funcName = "unnamed_" + id;
			return funcName;
		}
		return "";
	}

	public int getJavaScriptExprID(String expr) {
		if (javaScriptExpr.containsKey(expr)) {
			return javaScriptExpr.get(expr);
		}
		return -1;
	}

	@Override
	public void init() throws Exception {
		em=new javax.script.ScriptEngineManager();
		se=em.getEngineByName("JavaScript");
		
		InitextParamNames();
		return;
	}

	
	
	
	
	
	public void InitextParamNames()
	{
		extParamNames.clear();
		extParamNames.add("quality");
		extParamNames.add("friendlva");
		extParamNames.add("survivala");
		extParamNames.add("survivalnopeta");
		extParamNames.add("survivalb");
		extParamNames.add("survivalnopetb");
		extParamNames.add("effectnuma");
		extParamNames.add("TeamLv");
		extParamNames.add("TeamNum");
		extParamNames.add("RoleLv");
		extParamNames.add("Time");		
		extParamNames.add("Ring");
		extParamNames.add("PetLv");
		extParamNames.add("CurExp");
		extParamNames.add("MapLv");
		extParamNames.add("MonsterNum");
		extParamNames.add("MasterNum");
		extParamNames.add("MonsterLv");
		extParamNames.add("IsNoMul");
		extParamNames.add("IsDouble");
		extParamNames.add("IsSTrible");
		extParamNames.add("IsTrible");
		extParamNames.add("IsTL");
		extParamNames.add("IsPetTrible");
		extParamNames.add("Score");		
		extParamNames.add("Saveid");
		extParamNames.add("Discount");
		extParamNames.add("Rank");
		extParamNames.add("MaxLv");
		extParamNames.add("MinLv");
		extParamNames.add("IsSerMul");
		extParamNames.add("BNum");
		extParamNames.add("xiakeLv");
		extParamNames.add("vipLv");
		extParamNames.add("DkTrible");
		extParamNames.add("TjTrible");
		extParamNames.add("MergeServer");
		extParamNames.add("StdExp");
		extParamNames.add("StdMoney");
		extParamNames.add("IsDbPoint");		//是否开启双倍点数
		extParamNames.add("TaskLv");		//x循环任务配置表.xlsx 任务难度
		extParamNames.add("SwXs");			//声望系数		
		extParamNames.add("battleround");	//战斗回合数	
		extParamNames.add("fighterround");	//战斗者出现数
		extParamNames.add("airound");		//ai生效回合数
		extParamNames.add("maindamage");
		
		extParamNames.add("FuBenId");		//副本id
		extParamNames.add("FuBenLv");		//副本进入等级
		extParamNames.add("AnswerCnt");		//答题正确次数
		extParamNames.add("PVPCnt");		//竞技场连胜次数
		extParamNames.add("PVPTargetCnt");	//竞技场对手连胜次数
		
		extParamNames.add("preaimcount");
		extParamNames.add("rolenum");
		extParamNames.add("ServerLv");
	}
	
	
	public void initJavaScriptContent(Map<Integer, SJavaScriptConfig> sJavaScriptConfigs) {
		
		for (SJavaScriptConfig config : sJavaScriptConfigs.values()) {
			File f = new File(config.getFilePath() + config.getFileName());
			FileInputStream fis = null;
			try {
				if (f.exists() && f.isFile()) {
					fis = new FileInputStream(f);
					int length = fis.available();
					byte buff[] = new byte[length];
					fis.read(buff);
					String content = new String(buff);
					javaScripts.put(config.getFileName(), new JavaScript(content));
				}
			}catch (IOException e) {
					e.printStackTrace();
			} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			}
		}
	}

	
	public Object invokeFunction(IJavaScriptEngine engine, String expr, Object... args) {

		String funcName = getJavaScriptExprFunName(expr);
		if (!funcName.isEmpty()) {
			Invocable inv = (Invocable)engine.getEngine();
			try {
				return inv.invokeFunction(funcName, args);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	
	public boolean isRegisterJavaScriptExpr(String expr) {
		return getJavaScriptExprID(expr) != -1;
	}
	
	
	public void registerAllExprToEngine(ScriptEngine engine) {
		for (Map.Entry<String, Integer> entry : javaScriptExpr.entrySet()) {
			// 生成函数并注册
			String funcName = "unnamed_" + entry.getValue();
			String funcDefine = String.format("function %s(){ %s; }", funcName, entry.getKey());
			try {
				boolean isCompile = false;
				if (isCompile) {
					if (engine instanceof Compilable) {
						Compilable c = (Compilable)engine;
						CompiledScript cs = c.compile(funcDefine);
						cs.eval();
					}
				} else {
					engine.eval(funcDefine);
				}
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		}
		Writer2File();
		isEndRegister = true;
	}

	
	public void registerJavaScriptExpr()
	{	
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getMoneyAwardList				()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getGoldAwardList 				()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getCreditAwardList 				()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getContributionAwardList			()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getExpAwardList					()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getFesCreditAwardList			()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getFaction_crontriList			()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getFactionTask_crontriList		()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getFactionTaskProfit_crontriList	()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getShengWangAwardList 			()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getPetExpAwardList				()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getNpcCountScriptList			()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		for (JavaScript javaScript : fire.pb.activity.award.AwardMgr.getInstance().getNpcControlCntScriptList		()){if(javaScript != null && javaScript.getJavaScriptString() != null) fire.script.Module.registerJavaScriptExpr(javaScript.getJavaScriptString(), true, null);}
		
		for (Map.Entry<Integer, Condition> entry : fire.pb.battle.ai.BattleAIMgr.getInstance().getConditions().entrySet()) 
		{
			//targetFilterIds
			if (entry.getValue().strGoalRequire != null&&entry.getValue().strGoalRequire.getJavaScriptString()!= null) {
				fire.script.Module.registerJavaScriptExpr(entry.getValue().strGoalRequire.getJavaScriptString(), true, entry.getValue().targetFilterIds);
			}
			if (entry.getValue().jscondition != null&&entry.getValue().jscondition.getJavaScriptString()!= null) {
				fire.script.Module.registerJavaScriptExpr(entry.getValue().jscondition.getJavaScriptString(), true, null);
			}
		}		
		
		for (Map.Entry<Integer, TargetFilter> entry : fire.pb.battle.ai.BattleAIMgr.getInstance().getTargetFilters().entrySet()) 
		{	
			if (entry.getValue().buffjs != null&&entry.getValue().buffjs.getJavaScriptString()!= null) {
				fire.script.Module.registerJavaScriptExpr(entry.getValue().buffjs.getJavaScriptString(), true, entry.getValue().buffs);
			}
		}
		
		for (Map.Entry<Integer, BattleCfg> entry : fire.pb.battle.Module.getInstance().getBattleConfigs().entrySet()) 
		{
			if (entry.getValue().monsterNumScript != null&&entry.getValue().monsterNumScript.getJavaScriptString()!= null) {
				fire.script.Module.registerJavaScriptExpr(entry.getValue().monsterNumScript.getJavaScriptString(), true, null);
			}
			if (entry.getValue().leveljs != null&&entry.getValue().leveljs.getJavaScriptString()!= null) {
				fire.script.Module.registerJavaScriptExpr(entry.getValue().leveljs.getJavaScriptString(), true, null);
			}
		}		
		
		for (ConstantlyBuffConfig cBuffConfig : fire.pb.buff.Module.getInstance().getCBuffConfig().values()) 
		{
			//targetFilterIds
			for(JavaScript js:cBuffConfig.effectJavaScripts().values())
			{
				if (js != null&&js.getJavaScriptString() != null) {
					fire.script.Module.registerJavaScriptExpr(js.getJavaScriptString(), true, null);
				}				
			}
		}
	}
	
	public void Writer2File()
	{
		try {
				File file = null;
				if (!Main.getCurPath().equals("")){
					file = new File(Main.getCurPath() + "src/fire/script/JsFunManager.java");
				}
				else{
					file = new File(Main.getCurPath() + "src/fire/script/JsFunManager.java.bak");
				}
				
				if(file.exists()){
					file.delete();
					file.createNewFile();
					}
	
				final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				
				writer.write("package fire.script; \n\n");
				
				writer.write("import java.util.HashMap;\n");
				writer.write("import java.util.Map;\n");
				writer.write("import fire.pb.battle.Fighter;\n\n");
				
				writer.write("public class JsFunManager\n{\n");				

				writer.write("\tstatic Map<String, Integer> funMap = new HashMap<String, Integer>();\n");
				
				writer.write("\tpublic JsFunManager(){InitFunMap();}\n");			
				writer.write("\tstatic public float randfloat(int t, int t1) { return (float)(t1 > t ? Math.random()*(t1-t) + t : Math.random()*(t-t1) + t1) ; }\n");
				writer.write("\tstatic public int   randint(int t,int t1) {return (int)(t1 > t ? Math.round(Math.random()*(t1-t)) + t : Math.round(Math.random()*(t-t1)) + t1) ;}\n");
				writer.write("\tstatic public int GetFunID(String fun)	{  if(funMap.size() < 1) return -1;if(funMap.get(fun)!= null) return funMap.get(fun); else return -1; }\n");
				writer.write("\tstatic public void InitFunMap()\n\t{\n");
				
				for (Map.Entry<String, Integer> entry : funExpr.entrySet()) {
					writer.write("\t\tfunMap.put(\""+entry.getKey()+"\","+entry.getValue()+");\n");
				}
				writer.write("\t}\n");
				
				writer.write("\tpublic static Object JsFunbyID(IJavaScriptEngine engine, Fighter opf, Fighter aimf, int id)\n\t{\n");
				writer.write("\t\tswitch(id)\n\t\t{\n");
				
				for (Map.Entry<String, Integer> entry : funExpr.entrySet()) {
					// 生成函数并注册
					writer.write("\t\t\tcase "+entry.getValue()+": ");
					String js =entry.getKey();
					try {
						js = converjs2fun(js, funExprElements.get(entry.getValue()));
					} catch (Exception e) {
						e.printStackTrace();
					}	
					String funcDefine = String.format(" %s\n", js);
					writer.write(funcDefine);
				}
				writer.write("\t\t}\n\t\treturn null;\n\t}\n}");
				
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
