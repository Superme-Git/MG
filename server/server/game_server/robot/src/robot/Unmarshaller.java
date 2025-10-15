package robot;

import java.io.InputStream;

public interface Unmarshaller {
	Object unmarshal(InputStream input);
}
