package fire.pb.gm;

public class GM_version extends GMCommand {
	@Override
	boolean exec(String[] args) {
		try {
			String implVer = this.getClass().getPackage()
					.getImplementationVersion();
			String speciVer = this.getClass().getPackage()
					.getSpecificationVersion();
			sendToGM("ImplementationVersion: " + implVer);
			sendToGM("SpecificationVersion: " + speciVer);

		} catch (Exception e) {
			logger.info("Cmd 'version' failed", e);
			return false;
		}

		return true;
	}

	@Override
	String usage() {
		return "version";
	}

}
