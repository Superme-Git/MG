
#include "mkio_security_Security.h"

#include <fstream>
#include <string>

#include "core/io/streamcompress.h"
#include "core/io/security.h"
#include "core/common/octets.h"

#include "jcast.h"
/*
 * Class:     mkio_security_Security
 * Method:    open_compress
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_mkio_security_Security_open_1compress
  (JNIEnv * env, jobject)
  {
  		GNET::Compress * 
  			handle = new GNET::Compress();
  		return (jlong)(handle);
  }

/*
 * Class:     mkio_security_Security
 * Method:    compress_update
 * Signature: (J[BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_mkio_security_Security_compress_1update
  (JNIEnv * env, jobject, jlong handle, jbyteArray data, jint size)
  {
  		JEnv jenv(env);
  		//__TRY__BEGIN__
  		GNET::Compress * 
  			compress = reinterpret_cast<GNET::Compress*>(handle);
  		
  		GNET::Octets o( JByteArray(jenv, data).data(), size ); 		
  		compress->Update( o );  		  		 		 		
  		
  		return jenv.NewByteArray(o.begin(), o.size());
  		
  		//__TRY__END__
  }

/*
 * Class:     mkio_security_Security
 * Method:    compress_final
 * Signature: (J[BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_mkio_security_Security_compress_1final
  (JNIEnv * env, jobject, jlong handle, jbyteArray data, jint size)
  {
  		JEnv jenv(env);
  		//__TRY__BEGIN__
  		GNET::Compress * 
  			compress = reinterpret_cast<GNET::Compress*>(handle);
  		
  		GNET::Octets o( JByteArray(jenv, data).data(), size ); 		
  		compress->Final(o);  		  		 		 		
  		
  		return jenv.NewByteArray(o.begin(), o.size());
  		
  		//__TRY__END__
  }
  

/*
 * Class:     mkio_security_Security
 * Method:    close_compress
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_mkio_security_Security_close_1compress
  (JNIEnv * env, jobject, jlong handle)
  {
  		JEnv jenv(env);
  	  //__TRY__BEGIN__
  		GNET::Compress * 
  			compress = reinterpret_cast<GNET::Compress*>(handle);
  		delete compress;
  		//__TRY__END__
  }

/*
 * Class:     mkio_security_Security
 * Method:    open_decompress
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_mkio_security_Security_open_1decompress
  (JNIEnv * env, jobject)
  {
  		JEnv jenv(env);
  		GNET::Decompress * 
  			handle	= new GNET::Decompress();
  	  //__TRY__BEGIN__
  		//__TRY__END__
  		return (jlong)(handle);
  }

/*
 * Class:     mkio_security_Security
 * Method:    decompress_update
 * Signature: (J[BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_mkio_security_Security_decompress_1update
  (JNIEnv * env, jobject, jlong handle, jbyteArray data, jint size)
  {
  	  JEnv jenv(env);
  		GNET::Decompress * 
  			decompress = reinterpret_cast<GNET::Decompress*>(handle);
  		
  		GNET::Octets o( JByteArray(jenv, data).data(), size ); 		
  		decompress->Update( o );  		  		 		 		
  		
  		return jenv.NewByteArray(o.begin(), o.size());
  }

/*
 * Class:     mkio_security_Security
 * Method:    close_decompress
 * Signature: (J)J
 */
JNIEXPORT void JNICALL Java_mkio_security_Security_close_1decompress
  (JNIEnv * env, jobject, jlong handle)
  {
  		JEnv jenv(env);
  		GNET::Decompress * 
  			decompress = reinterpret_cast<GNET::Decompress*>(handle);
  		delete decompress;
  }
  
/*
 * Class:     mkio_security_Security
 * Method:    open_security
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_mkio_security_Security_open_1security
  (JNIEnv *, jobject, jint type)
  {
  	GNET::Security* handle = GNET::Security::Create( type );
  		
  	return (jlong)handle;
  }

/*
 * Class:     mkio_security_Security
 * Method:    security_set_parameter
 * Signature: (J[BI)V
 */
JNIEXPORT void JNICALL Java_mkio_security_Security_security_1set_1parameter
  (JNIEnv * env, jobject, jlong handle, jbyteArray key, jint keysize)
  {
  	JEnv jenv(env);
  	
  	GNET::Octets o( JByteArray(jenv, key).data(), keysize ); 
  	
  	reinterpret_cast<GNET::Security*>(handle)->SetParameter(o);
  	
  	return;
  	
  }

/*
 * Class:     mkio_security_Security
 * Method:    security_update
 * Signature: (J[BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_mkio_security_Security_security_1update
  (JNIEnv * env, jobject, jlong handle, jbyteArray data, jint datasize)
  {
  	  JEnv jenv(env);
  	  GNET::Octets o( JByteArray(jenv, data).data(), datasize ); 
  	  	
  	  reinterpret_cast<GNET::Security*>(handle)->Update(o);	
  	  return jenv.NewByteArray(o.begin(), o.size());
  }

/*
 * Class:     mkio_security_Security
 * Method:    security_final
 * Signature: (J[BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_mkio_security_Security_security_1final
  (JNIEnv * env, jobject, jlong handle, jbyteArray data, jint datasize)
  {
  	  JEnv jenv(env);
  	  GNET::Octets o( JByteArray(jenv, data).data(), datasize ); 
  	  	
  	  reinterpret_cast<GNET::Security*>(handle)->Final(o);	
  	  return jenv.NewByteArray(o.begin(), o.size());
  }

/*
 * Class:     mkio_security_Security
 * Method:    colse_security
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_mkio_security_Security_close_1security
  (JNIEnv *, jobject, jlong handle)
  {
  	reinterpret_cast<GNET::Security*>(handle)->Destroy();
  }