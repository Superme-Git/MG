package xtable;


public class _DatabaseMetaData_ extends mkdb.util.DatabaseMetaData {
	@Override
	public boolean isVerifyMkdb() {
		return true;
	}
	public void DatabaseMetaData1(){
		// xbeans
		{
			Bean bean = new Bean("NameState", false, false);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "localid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "peerip"
				, "string", "", "", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("IdState", false, false);
			super.addVariableFor(bean
				, "nextid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "localid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "peerip"
				, "string", "", "", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		// cbeans
		// tables
		super.addTable("role", "DB", "string", false, "NameState", "", "key:32");
		super.addTable("clan", "DB", "string", false, "NameState", "", "key:32");
	}
	public _DatabaseMetaData_() {
		DatabaseMetaData1();
	}
}

