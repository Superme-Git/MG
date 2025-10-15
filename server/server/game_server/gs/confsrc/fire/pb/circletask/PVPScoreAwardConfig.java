package fire.pb.circletask;


public class PVPScoreAwardConfig implements mytools.ConvMain.Checkable ,Comparable<PVPScoreAwardConfig>{

	public int compareTo(PVPScoreAwardConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PVPScoreAwardConfig(){
		super();
	}
	public PVPScoreAwardConfig(PVPScoreAwardConfig arg){
		this.id=arg.id ;
		this.score=arg.score ;
		this.score2=arg.score2 ;
		this.win=arg.win ;
		this.lose=arg.lose ;
		this.adwin=arg.adwin ;
		this.adlose=arg.adlose ;
		this.diswin=arg.diswin ;
		this.dislose=arg.dislose ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 
	 */
	public int score  = 0  ;
	
	public int getScore(){
		return this.score;
	}
	
	public void setScore(int v){
		this.score=v;
	}
	
	/**
	 * 
	 */
	public int score2  = 0  ;
	
	public int getScore2(){
		return this.score2;
	}
	
	public void setScore2(int v){
		this.score2=v;
	}
	
	/**
	 * 
	 */
	public int win  = 0  ;
	
	public int getWin(){
		return this.win;
	}
	
	public void setWin(int v){
		this.win=v;
	}
	
	/**
	 * 
	 */
	public int lose  = 0  ;
	
	public int getLose(){
		return this.lose;
	}
	
	public void setLose(int v){
		this.lose=v;
	}
	
	/**
	 * 
	 */
	public int adwin  = 0  ;
	
	public int getAdwin(){
		return this.adwin;
	}
	
	public void setAdwin(int v){
		this.adwin=v;
	}
	
	/**
	 * 
	 */
	public int adlose  = 0  ;
	
	public int getAdlose(){
		return this.adlose;
	}
	
	public void setAdlose(int v){
		this.adlose=v;
	}
	
	/**
	 * 
	 */
	public int diswin  = 0  ;
	
	public int getDiswin(){
		return this.diswin;
	}
	
	public void setDiswin(int v){
		this.diswin=v;
	}
	
	/**
	 * 
	 */
	public int dislose  = 0  ;
	
	public int getDislose(){
		return this.dislose;
	}
	
	public void setDislose(int v){
		this.dislose=v;
	}
	
	
};