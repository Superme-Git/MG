package fire.pb.npc;


public class MonsterExtraAttrConfig  extends MonsterConfig {


	
	public MonsterExtraAttrConfig(MonsterConfig arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public MonsterExtraAttrConfig(){
		super();
	}
	public MonsterExtraAttrConfig(MonsterExtraAttrConfig arg){
		super(arg);
		this.addseal=arg.addseal ;
		this.defseal=arg.defseal ;
		this.wulicriticalFactor=arg.wulicriticalFactor ;
		this.wulicriticaldefFactor=arg.wulicriticaldefFactor ;
		this.wulicriticaldegreeFactor=arg.wulicriticaldegreeFactor ;
		this.fashucriticalFactor=arg.fashucriticalFactor ;
		this.fashucriticaldefFactor=arg.fashucriticaldefFactor ;
		this.fashucriticaldegreeFactor=arg.fashucriticaldegreeFactor ;
		this.heallvFactor=arg.heallvFactor ;
		this.healdegreeFactor=arg.healdegreeFactor ;
		this.physicchuantouFactor=arg.physicchuantouFactor ;
		this.physicdikangFactor=arg.physicdikangFactor ;
		this.magicchuantouFactor=arg.magicchuantouFactor ;
		this.magicdikangFactor=arg.magicdikangFactor ;
		this.medicaljiashenFactor=arg.medicaljiashenFactor ;
		this.onmedicaljiashenFactor=arg.onmedicaljiashenFactor ;
		this.kongzhijiachengFactor=arg.kongzhijiachengFactor ;
		this.kongzhimianyiFactor=arg.kongzhimianyiFactor ;
		this.wulicritical=arg.wulicritical ;
		this.wulicriticaldef=arg.wulicriticaldef ;
		this.wulicriticaldegree=arg.wulicriticaldegree ;
		this.fashucritical=arg.fashucritical ;
		this.fashucriticaldef=arg.fashucriticaldef ;
		this.fashucriticaldegree=arg.fashucriticaldegree ;
		this.heallv=arg.heallv ;
		this.healdegree=arg.healdegree ;
		this.physicchuantou=arg.physicchuantou ;
		this.physicdikang=arg.physicdikang ;
		this.magicchuantou=arg.magicchuantou ;
		this.magicdikang=arg.magicdikang ;
		this.medicaljiashen=arg.medicaljiashen ;
		this.onmedicaljiashen=arg.onmedicaljiashen ;
		this.kongzhijiacheng=arg.kongzhijiacheng ;
		this.kongzhimianyi=arg.kongzhimianyi ;
		this.extraattrid1=arg.extraattrid1 ;
		this.extraattrvalue1=arg.extraattrvalue1 ;
		this.extraattrid2=arg.extraattrid2 ;
		this.extraattrvalue2=arg.extraattrvalue2 ;
		this.extraattrid3=arg.extraattrid3 ;
		this.extraattrvalue3=arg.extraattrvalue3 ;
		this.extraattrid4=arg.extraattrid4 ;
		this.extraattrvalue4=arg.extraattrvalue4 ;
		this.extraattrid5=arg.extraattrid5 ;
		this.extraattrvalue5=arg.extraattrvalue5 ;
		this.extraattrid6=arg.extraattrid6 ;
		this.extraattrvalue6=arg.extraattrvalue6 ;
		this.extraattrid7=arg.extraattrid7 ;
		this.extraattrvalue7=arg.extraattrvalue7 ;
		this.extraattrid8=arg.extraattrid8 ;
		this.extraattrvalue8=arg.extraattrvalue8 ;
		this.extraattrid9=arg.extraattrid9 ;
		this.extraattrvalue9=arg.extraattrvalue9 ;
		this.extraattrid10=arg.extraattrid10 ;
		this.extraattrvalue10=arg.extraattrvalue10 ;
		this.fightbackprob=arg.fightbackprob ;
		this.fightbackhurtratio=arg.fightbackhurtratio ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public int addseal  = 0  ;
	
	public int getAddseal(){
		return this.addseal;
	}
	
	public void setAddseal(int v){
		this.addseal=v;
	}
	
	/**
	 * 
	 */
	public int defseal  = 0  ;
	
	public int getDefseal(){
		return this.defseal;
	}
	
	public void setDefseal(int v){
		this.defseal=v;
	}
	
	/**
	 * 
	 */
	public double wulicriticalFactor  = 0.0  ;
	
	public double getWulicriticalFactor(){
		return this.wulicriticalFactor;
	}
	
	public void setWulicriticalFactor(double v){
		this.wulicriticalFactor=v;
	}
	
	/**
	 * 
	 */
	public double wulicriticaldefFactor  = 0.0  ;
	
	public double getWulicriticaldefFactor(){
		return this.wulicriticaldefFactor;
	}
	
	public void setWulicriticaldefFactor(double v){
		this.wulicriticaldefFactor=v;
	}
	
	/**
	 * 
	 */
	public double wulicriticaldegreeFactor  = 0.0  ;
	
	public double getWulicriticaldegreeFactor(){
		return this.wulicriticaldegreeFactor;
	}
	
	public void setWulicriticaldegreeFactor(double v){
		this.wulicriticaldegreeFactor=v;
	}
	
	/**
	 * 
	 */
	public double fashucriticalFactor  = 0.0  ;
	
	public double getFashucriticalFactor(){
		return this.fashucriticalFactor;
	}
	
	public void setFashucriticalFactor(double v){
		this.fashucriticalFactor=v;
	}
	
	/**
	 * 
	 */
	public double fashucriticaldefFactor  = 0.0  ;
	
	public double getFashucriticaldefFactor(){
		return this.fashucriticaldefFactor;
	}
	
	public void setFashucriticaldefFactor(double v){
		this.fashucriticaldefFactor=v;
	}
	
	/**
	 * 
	 */
	public double fashucriticaldegreeFactor  = 0.0  ;
	
	public double getFashucriticaldegreeFactor(){
		return this.fashucriticaldegreeFactor;
	}
	
	public void setFashucriticaldegreeFactor(double v){
		this.fashucriticaldegreeFactor=v;
	}
	
	/**
	 * 
	 */
	public double heallvFactor  = 0.0  ;
	
	public double getHeallvFactor(){
		return this.heallvFactor;
	}
	
	public void setHeallvFactor(double v){
		this.heallvFactor=v;
	}
	
	/**
	 * 
	 */
	public double healdegreeFactor  = 0.0  ;
	
	public double getHealdegreeFactor(){
		return this.healdegreeFactor;
	}
	
	public void setHealdegreeFactor(double v){
		this.healdegreeFactor=v;
	}
	
	/**
	 * 
	 */
	public double physicchuantouFactor  = 0.0  ;
	
	public double getPhysicchuantouFactor(){
		return this.physicchuantouFactor;
	}
	
	public void setPhysicchuantouFactor(double v){
		this.physicchuantouFactor=v;
	}
	
	/**
	 * 
	 */
	public double physicdikangFactor  = 0.0  ;
	
	public double getPhysicdikangFactor(){
		return this.physicdikangFactor;
	}
	
	public void setPhysicdikangFactor(double v){
		this.physicdikangFactor=v;
	}
	
	/**
	 * 
	 */
	public double magicchuantouFactor  = 0.0  ;
	
	public double getMagicchuantouFactor(){
		return this.magicchuantouFactor;
	}
	
	public void setMagicchuantouFactor(double v){
		this.magicchuantouFactor=v;
	}
	
	/**
	 * 
	 */
	public double magicdikangFactor  = 0.0  ;
	
	public double getMagicdikangFactor(){
		return this.magicdikangFactor;
	}
	
	public void setMagicdikangFactor(double v){
		this.magicdikangFactor=v;
	}
	
	/**
	 * 
	 */
	public double medicaljiashenFactor  = 0.0  ;
	
	public double getMedicaljiashenFactor(){
		return this.medicaljiashenFactor;
	}
	
	public void setMedicaljiashenFactor(double v){
		this.medicaljiashenFactor=v;
	}
	
	/**
	 * 
	 */
	public double onmedicaljiashenFactor  = 0.0  ;
	
	public double getOnmedicaljiashenFactor(){
		return this.onmedicaljiashenFactor;
	}
	
	public void setOnmedicaljiashenFactor(double v){
		this.onmedicaljiashenFactor=v;
	}
	
	/**
	 * 
	 */
	public double kongzhijiachengFactor  = 0.0  ;
	
	public double getKongzhijiachengFactor(){
		return this.kongzhijiachengFactor;
	}
	
	public void setKongzhijiachengFactor(double v){
		this.kongzhijiachengFactor=v;
	}
	
	/**
	 * 
	 */
	public double kongzhimianyiFactor  = 0.0  ;
	
	public double getKongzhimianyiFactor(){
		return this.kongzhimianyiFactor;
	}
	
	public void setKongzhimianyiFactor(double v){
		this.kongzhimianyiFactor=v;
	}
	
	/**
	 * 
	 */
	public int wulicritical  = 0  ;
	
	public int getWulicritical(){
		return this.wulicritical;
	}
	
	public void setWulicritical(int v){
		this.wulicritical=v;
	}
	
	/**
	 * 
	 */
	public int wulicriticaldef  = 0  ;
	
	public int getWulicriticaldef(){
		return this.wulicriticaldef;
	}
	
	public void setWulicriticaldef(int v){
		this.wulicriticaldef=v;
	}
	
	/**
	 * 
	 */
	public double wulicriticaldegree  = 0.0  ;
	
	public double getWulicriticaldegree(){
		return this.wulicriticaldegree;
	}
	
	public void setWulicriticaldegree(double v){
		this.wulicriticaldegree=v;
	}
	
	/**
	 * 
	 */
	public int fashucritical  = 0  ;
	
	public int getFashucritical(){
		return this.fashucritical;
	}
	
	public void setFashucritical(int v){
		this.fashucritical=v;
	}
	
	/**
	 * 
	 */
	public int fashucriticaldef  = 0  ;
	
	public int getFashucriticaldef(){
		return this.fashucriticaldef;
	}
	
	public void setFashucriticaldef(int v){
		this.fashucriticaldef=v;
	}
	
	/**
	 * 
	 */
	public double fashucriticaldegree  = 0.0  ;
	
	public double getFashucriticaldegree(){
		return this.fashucriticaldegree;
	}
	
	public void setFashucriticaldegree(double v){
		this.fashucriticaldegree=v;
	}
	
	/**
	 * 
	 */
	public int heallv  = 0  ;
	
	public int getHeallv(){
		return this.heallv;
	}
	
	public void setHeallv(int v){
		this.heallv=v;
	}
	
	/**
	 * 
	 */
	public int healdegree  = 0  ;
	
	public int getHealdegree(){
		return this.healdegree;
	}
	
	public void setHealdegree(int v){
		this.healdegree=v;
	}
	
	/**
	 * 
	 */
	public int physicchuantou  = 0  ;
	
	public int getPhysicchuantou(){
		return this.physicchuantou;
	}
	
	public void setPhysicchuantou(int v){
		this.physicchuantou=v;
	}
	
	/**
	 * 
	 */
	public int physicdikang  = 0  ;
	
	public int getPhysicdikang(){
		return this.physicdikang;
	}
	
	public void setPhysicdikang(int v){
		this.physicdikang=v;
	}
	
	/**
	 * 
	 */
	public int magicchuantou  = 0  ;
	
	public int getMagicchuantou(){
		return this.magicchuantou;
	}
	
	public void setMagicchuantou(int v){
		this.magicchuantou=v;
	}
	
	/**
	 * 
	 */
	public int magicdikang  = 0  ;
	
	public int getMagicdikang(){
		return this.magicdikang;
	}
	
	public void setMagicdikang(int v){
		this.magicdikang=v;
	}
	
	/**
	 * 
	 */
	public int medicaljiashen  = 0  ;
	
	public int getMedicaljiashen(){
		return this.medicaljiashen;
	}
	
	public void setMedicaljiashen(int v){
		this.medicaljiashen=v;
	}
	
	/**
	 * 
	 */
	public int onmedicaljiashen  = 0  ;
	
	public int getOnmedicaljiashen(){
		return this.onmedicaljiashen;
	}
	
	public void setOnmedicaljiashen(int v){
		this.onmedicaljiashen=v;
	}
	
	/**
	 * 
	 */
	public int kongzhijiacheng  = 0  ;
	
	public int getKongzhijiacheng(){
		return this.kongzhijiacheng;
	}
	
	public void setKongzhijiacheng(int v){
		this.kongzhijiacheng=v;
	}
	
	/**
	 * 
	 */
	public int kongzhimianyi  = 0  ;
	
	public int getKongzhimianyi(){
		return this.kongzhimianyi;
	}
	
	public void setKongzhimianyi(int v){
		this.kongzhimianyi=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid1  = 0  ;
	
	public int getExtraattrid1(){
		return this.extraattrid1;
	}
	
	public void setExtraattrid1(int v){
		this.extraattrid1=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue1  = 0.0  ;
	
	public double getExtraattrvalue1(){
		return this.extraattrvalue1;
	}
	
	public void setExtraattrvalue1(double v){
		this.extraattrvalue1=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid2  = 0  ;
	
	public int getExtraattrid2(){
		return this.extraattrid2;
	}
	
	public void setExtraattrid2(int v){
		this.extraattrid2=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue2  = 0.0  ;
	
	public double getExtraattrvalue2(){
		return this.extraattrvalue2;
	}
	
	public void setExtraattrvalue2(double v){
		this.extraattrvalue2=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid3  = 0  ;
	
	public int getExtraattrid3(){
		return this.extraattrid3;
	}
	
	public void setExtraattrid3(int v){
		this.extraattrid3=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue3  = 0.0  ;
	
	public double getExtraattrvalue3(){
		return this.extraattrvalue3;
	}
	
	public void setExtraattrvalue3(double v){
		this.extraattrvalue3=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid4  = 0  ;
	
	public int getExtraattrid4(){
		return this.extraattrid4;
	}
	
	public void setExtraattrid4(int v){
		this.extraattrid4=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue4  = 0.0  ;
	
	public double getExtraattrvalue4(){
		return this.extraattrvalue4;
	}
	
	public void setExtraattrvalue4(double v){
		this.extraattrvalue4=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid5  = 0  ;
	
	public int getExtraattrid5(){
		return this.extraattrid5;
	}
	
	public void setExtraattrid5(int v){
		this.extraattrid5=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue5  = 0.0  ;
	
	public double getExtraattrvalue5(){
		return this.extraattrvalue5;
	}
	
	public void setExtraattrvalue5(double v){
		this.extraattrvalue5=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid6  = 0  ;
	
	public int getExtraattrid6(){
		return this.extraattrid6;
	}
	
	public void setExtraattrid6(int v){
		this.extraattrid6=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue6  = 0.0  ;
	
	public double getExtraattrvalue6(){
		return this.extraattrvalue6;
	}
	
	public void setExtraattrvalue6(double v){
		this.extraattrvalue6=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid7  = 0  ;
	
	public int getExtraattrid7(){
		return this.extraattrid7;
	}
	
	public void setExtraattrid7(int v){
		this.extraattrid7=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue7  = 0.0  ;
	
	public double getExtraattrvalue7(){
		return this.extraattrvalue7;
	}
	
	public void setExtraattrvalue7(double v){
		this.extraattrvalue7=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid8  = 0  ;
	
	public int getExtraattrid8(){
		return this.extraattrid8;
	}
	
	public void setExtraattrid8(int v){
		this.extraattrid8=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue8  = 0.0  ;
	
	public double getExtraattrvalue8(){
		return this.extraattrvalue8;
	}
	
	public void setExtraattrvalue8(double v){
		this.extraattrvalue8=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid9  = 0  ;
	
	public int getExtraattrid9(){
		return this.extraattrid9;
	}
	
	public void setExtraattrid9(int v){
		this.extraattrid9=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue9  = 0.0  ;
	
	public double getExtraattrvalue9(){
		return this.extraattrvalue9;
	}
	
	public void setExtraattrvalue9(double v){
		this.extraattrvalue9=v;
	}
	
	/**
	 * 
	 */
	public int extraattrid10  = 0  ;
	
	public int getExtraattrid10(){
		return this.extraattrid10;
	}
	
	public void setExtraattrid10(int v){
		this.extraattrid10=v;
	}
	
	/**
	 * 
	 */
	public double extraattrvalue10  = 0.0  ;
	
	public double getExtraattrvalue10(){
		return this.extraattrvalue10;
	}
	
	public void setExtraattrvalue10(double v){
		this.extraattrvalue10=v;
	}
	
	/**
	 * 
	 */
	public double fightbackprob  = 0.0  ;
	
	public double getFightbackprob(){
		return this.fightbackprob;
	}
	
	public void setFightbackprob(double v){
		this.fightbackprob=v;
	}
	
	/**
	 * 
	 */
	public double fightbackhurtratio  = 0.0  ;
	
	public double getFightbackhurtratio(){
		return this.fightbackhurtratio;
	}
	
	public void setFightbackhurtratio(double v){
		this.fightbackhurtratio=v;
	}
	
	
};