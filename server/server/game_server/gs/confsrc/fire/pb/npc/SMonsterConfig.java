package fire.pb.npc;


public class SMonsterConfig  extends MonsterExtraAttrConfig {

	public int compareTo(SMonsterConfig o){
		return this.id-o.id;
	}

	
	public SMonsterConfig(MonsterExtraAttrConfig arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SMonsterConfig(){
		super();
	}
	public SMonsterConfig(SMonsterConfig arg){
		super(arg);
		this.daodi=arg.daodi ;
		this.DefaultBattleEp=arg.DefaultBattleEp ;
		this.MaxSp=arg.MaxSp ;
		this.DefaultBattleSp=arg.DefaultBattleSp ;
		this.hpMaxAttackFactor=arg.hpMaxAttackFactor ;
		this.hpMaxFactor=arg.hpMaxFactor ;
		this.hpMaxConstant=arg.hpMaxConstant ;
		this.mpMaxFactor=arg.mpMaxFactor ;
		this.mpMaxConstant=arg.mpMaxConstant ;
		this.attackFactor=arg.attackFactor ;
		this.attackConstant=arg.attackConstant ;
		this.defFactor=arg.defFactor ;
		this.defConstant=arg.defConstant ;
		this.magicattFactor=arg.magicattFactor ;
		this.magicattConstant=arg.magicattConstant ;
		this.magicDefFactor=arg.magicDefFactor ;
		this.magicDefConstant=arg.magicDefConstant ;
		this.attallFactor=arg.attallFactor ;
		this.attallConstant=arg.attallConstant ;
		this.speedFactor=arg.speedFactor ;
		this.speedConstant=arg.speedConstant ;
		this.medicalFactor=arg.medicalFactor ;
		this.medicalConstant=arg.medicalConstant ;
		this.sealhitFactor=arg.sealhitFactor ;
		this.sealhitConstant=arg.sealhitConstant ;
		this.unsealFactor=arg.unsealFactor ;
		this.unsealConstant=arg.unsealConstant ;
		this.skills=arg.skills ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public int daodi  = 0  ;
	
	public int getDaodi(){
		return this.daodi;
	}
	
	public void setDaodi(int v){
		this.daodi=v;
	}
	
	/**
	 * 
	 */
	public int DefaultBattleEp  = 0  ;
	
	public int getDefaultBattleEp(){
		return this.DefaultBattleEp;
	}
	
	public void setDefaultBattleEp(int v){
		this.DefaultBattleEp=v;
	}
	
	/**
	 * 
	 */
	public int MaxSp  = 0  ;
	
	public int getMaxSp(){
		return this.MaxSp;
	}
	
	public void setMaxSp(int v){
		this.MaxSp=v;
	}
	
	/**
	 * 
	 */
	public int DefaultBattleSp  = 0  ;
	
	public int getDefaultBattleSp(){
		return this.DefaultBattleSp;
	}
	
	public void setDefaultBattleSp(int v){
		this.DefaultBattleSp=v;
	}
	
	/**
	 * 
	 */
	public double hpMaxAttackFactor  = 0.0  ;
	
	public double getHpMaxAttackFactor(){
		return this.hpMaxAttackFactor;
	}
	
	public void setHpMaxAttackFactor(double v){
		this.hpMaxAttackFactor=v;
	}
	
	/**
	 * 
	 */
	public double hpMaxFactor  = 0.0  ;
	
	public double getHpMaxFactor(){
		return this.hpMaxFactor;
	}
	
	public void setHpMaxFactor(double v){
		this.hpMaxFactor=v;
	}
	
	/**
	 * 
	 */
	public double hpMaxConstant  = 0.0  ;
	
	public double getHpMaxConstant(){
		return this.hpMaxConstant;
	}
	
	public void setHpMaxConstant(double v){
		this.hpMaxConstant=v;
	}
	
	/**
	 * 
	 */
	public double mpMaxFactor  = 0.0  ;
	
	public double getMpMaxFactor(){
		return this.mpMaxFactor;
	}
	
	public void setMpMaxFactor(double v){
		this.mpMaxFactor=v;
	}
	
	/**
	 * 
	 */
	public double mpMaxConstant  = 0.0  ;
	
	public double getMpMaxConstant(){
		return this.mpMaxConstant;
	}
	
	public void setMpMaxConstant(double v){
		this.mpMaxConstant=v;
	}
	
	/**
	 * 
	 */
	public double attackFactor  = 0.0  ;
	
	public double getAttackFactor(){
		return this.attackFactor;
	}
	
	public void setAttackFactor(double v){
		this.attackFactor=v;
	}
	
	/**
	 * 
	 */
	public double attackConstant  = 0.0  ;
	
	public double getAttackConstant(){
		return this.attackConstant;
	}
	
	public void setAttackConstant(double v){
		this.attackConstant=v;
	}
	
	/**
	 * 
	 */
	public double defFactor  = 0.0  ;
	
	public double getDefFactor(){
		return this.defFactor;
	}
	
	public void setDefFactor(double v){
		this.defFactor=v;
	}
	
	/**
	 * 
	 */
	public double defConstant  = 0.0  ;
	
	public double getDefConstant(){
		return this.defConstant;
	}
	
	public void setDefConstant(double v){
		this.defConstant=v;
	}
	
	/**
	 * 
	 */
	public double magicattFactor  = 0.0  ;
	
	public double getMagicattFactor(){
		return this.magicattFactor;
	}
	
	public void setMagicattFactor(double v){
		this.magicattFactor=v;
	}
	
	/**
	 * 
	 */
	public double magicattConstant  = 0.0  ;
	
	public double getMagicattConstant(){
		return this.magicattConstant;
	}
	
	public void setMagicattConstant(double v){
		this.magicattConstant=v;
	}
	
	/**
	 * 
	 */
	public double magicDefFactor  = 0.0  ;
	
	public double getMagicDefFactor(){
		return this.magicDefFactor;
	}
	
	public void setMagicDefFactor(double v){
		this.magicDefFactor=v;
	}
	
	/**
	 * 
	 */
	public double magicDefConstant  = 0.0  ;
	
	public double getMagicDefConstant(){
		return this.magicDefConstant;
	}
	
	public void setMagicDefConstant(double v){
		this.magicDefConstant=v;
	}
	
	/**
	 * 
	 */
	public double attallFactor  = 0.0  ;
	
	public double getAttallFactor(){
		return this.attallFactor;
	}
	
	public void setAttallFactor(double v){
		this.attallFactor=v;
	}
	
	/**
	 * 
	 */
	public double attallConstant  = 0.0  ;
	
	public double getAttallConstant(){
		return this.attallConstant;
	}
	
	public void setAttallConstant(double v){
		this.attallConstant=v;
	}
	
	/**
	 * 
	 */
	public double speedFactor  = 0.0  ;
	
	public double getSpeedFactor(){
		return this.speedFactor;
	}
	
	public void setSpeedFactor(double v){
		this.speedFactor=v;
	}
	
	/**
	 * 
	 */
	public double speedConstant  = 0.0  ;
	
	public double getSpeedConstant(){
		return this.speedConstant;
	}
	
	public void setSpeedConstant(double v){
		this.speedConstant=v;
	}
	
	/**
	 * 
	 */
	public double medicalFactor  = 0.0  ;
	
	public double getMedicalFactor(){
		return this.medicalFactor;
	}
	
	public void setMedicalFactor(double v){
		this.medicalFactor=v;
	}
	
	/**
	 * 
	 */
	public double medicalConstant  = 0.0  ;
	
	public double getMedicalConstant(){
		return this.medicalConstant;
	}
	
	public void setMedicalConstant(double v){
		this.medicalConstant=v;
	}
	
	/**
	 * 
	 */
	public double sealhitFactor  = 0.0  ;
	
	public double getSealhitFactor(){
		return this.sealhitFactor;
	}
	
	public void setSealhitFactor(double v){
		this.sealhitFactor=v;
	}
	
	/**
	 * 
	 */
	public double sealhitConstant  = 0.0  ;
	
	public double getSealhitConstant(){
		return this.sealhitConstant;
	}
	
	public void setSealhitConstant(double v){
		this.sealhitConstant=v;
	}
	
	/**
	 * 
	 */
	public double unsealFactor  = 0.0  ;
	
	public double getUnsealFactor(){
		return this.unsealFactor;
	}
	
	public void setUnsealFactor(double v){
		this.unsealFactor=v;
	}
	
	/**
	 * 
	 */
	public double unsealConstant  = 0.0  ;
	
	public double getUnsealConstant(){
		return this.unsealConstant;
	}
	
	public void setUnsealConstant(double v){
		this.unsealConstant=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<fire.pb.npc.SMonsterSkill> skills  ;
	
	public java.util.ArrayList<fire.pb.npc.SMonsterSkill> getSkills(){
		return this.skills;
	}
	
	public void setSkills(java.util.ArrayList<fire.pb.npc.SMonsterSkill> v){
		this.skills=v;
	}
	
	
};