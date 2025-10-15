package fire.pb.battle;


public class SBattleMagicConfig implements mytools.ConvMain.Checkable ,Comparable<SBattleMagicConfig>{

	public int compareTo(SBattleMagicConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		
		private static final long serialVersionUID = 1L;
		
	}
	public SBattleMagicConfig(){
		super();
	}
	public SBattleMagicConfig(SBattleMagicConfig arg){
		this.id=arg.id ;
		this.genemagicid=arg.genemagicid ;
		this.genemagictime=arg.genemagictime ;
		this.geneeffecttime=arg.geneeffecttime ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	
	public int genemagicid  = 0  ;
	
	public int getGenemagicid(){
		return this.genemagicid;
	}
	
	public void setGenemagicid(int v){
		this.genemagicid=v;
	}
	
	
	public int genemagictime  = 0  ;
	
	public int getGenemagictime(){
		return this.genemagictime;
	}
	
	public void setGenemagictime(int v){
		this.genemagictime=v;
	}
	
	
	public int geneeffecttime  = 0  ;
	
	public int getGeneeffecttime(){
		return this.geneeffecttime;
	}
	
	public void setGeneeffecttime(int v){
		this.geneeffecttime=v;
	}
	
	
};