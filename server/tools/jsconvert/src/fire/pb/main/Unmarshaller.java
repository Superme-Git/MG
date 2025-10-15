package fire.pb.main;


import java.io.InputStream;

public interface Unmarshaller {

	Object unmarshal(InputStream input) ;
}
