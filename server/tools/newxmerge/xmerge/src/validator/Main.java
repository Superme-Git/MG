package validator;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

public class Main {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
	private static String validate_db_dir = null;
	private static String foreign_db_dir = null;
	private static boolean output = false;
	
	private static void usage() {
		System.out.println("Usage: java -jar foreignValidator.jar -v validate_db_dir [-output foreign_db_dir]");
		System.out.println("validate_db_dir  进行foreign关系完备性检测的数据库所在路径");
		System.out.println("-output  foreign_db_dir[optional] 将数据库中具有foreign关系的数据记录在foreign_db_dir所示的DB中[可选项]");
		Runtime.getRuntime().exit(1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException{
		for (int i = 0; i < args.length; ++i) {
			if      (args[i].equals("-v")) validate_db_dir = args[++i];
			else if (args[i].equals("-output")){
				output= true;
			    foreign_db_dir = args[++i];
			}  
			else
				usage();
		}
			
		mkdb.util.Dbx.start("./");
		ForeignValidator validator = new ForeignValidator();
		validator.setDB(validate_db_dir);
		if(output)
			validator.setOutputDir(foreign_db_dir);
		
		mkdb.Trace.debug("validate start... ...");
		validator.doValidate();
		mkdb.Trace.debug("validate over... ...");
	}

}
