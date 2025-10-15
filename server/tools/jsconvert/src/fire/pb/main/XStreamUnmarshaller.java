package fire.pb.main;

import com.thoughtworks.xstream.XStream;

public class XStreamUnmarshaller implements Unmarshaller {
	private final XStream xstream;
	
	XStreamUnmarshaller(){
		xstream=new XStream();
	}
	
	@Override
	public Object unmarshal(java.io.InputStream input) {		
		return xstream.fromXML(input);
	}

}
