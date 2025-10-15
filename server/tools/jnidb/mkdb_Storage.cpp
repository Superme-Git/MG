
#include "mkdb_Storage.h"
#ifdef WIN32
#include "core/common/db_win32.h"
#else
#include "core/common/db.h"
#endif

#include <fstream>
#include <string>
#include "jcast.h"
#include "dbhelper.hpp"

//#define JUNBO_DEBUG

#ifdef JUNBO_DEBUG
std::ofstream logout("D:/log.txt");
#endif

// new added class
class MyQueryData : public GNET::IQueryData
{
private:
	const JEnv & jenv;
	jobject	     iwalk;
	jmethodID    jmid;

public:
	MyQueryData(const JEnv & _jenv, jobject iwalk)
		: jenv(_jenv)
	{
		this->iwalk = iwalk;
		this->jmid = jenv.GetMethodID(jenv.GetObjectClass(iwalk), "onRecord", "([B[B)Z");
	}

	virtual bool update( const void *key, size_t key_len, const void *val, size_t val_len )
	{
		__xdb_helper::Value uncompressed;
		__xdb_helper::uncompress(val, val_len, uncompressed);
		jbyteArray valuearray = this->jenv.NewByteArray(uncompressed.data(), uncompressed.size());
		jbyteArray keyarray   = this->jenv.NewByteArray(key, key_len);
		jboolean ret = this->jenv.CallBooleanMethod(iwalk, jmid, keyarray, valuearray);

		{ // release every updata call, not until walk total complete
			this->jenv.DeleteLocalRef(keyarray);
			this->jenv.DeleteLocalRef(valuearray);
		}
		return ret;
	}
};

/*
 * Class:     mkdb_Storage
 * Method:    walk
 * Signature: (JLmkdb/Storage/IWalk;)V
 */
void JNICALL Java_mkdb_Storage_walk(JNIEnv * env, jobject, jlong handle, jobject walk)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	MyQueryData walker(jenv, walk);
	storage->walk(&walker);
	__TRY__END__
}

/*
 * Class:     mkdb_Storage
 * Method:    browse
 * Signature: (JLmkdb/Storage/IWalk;I)V
 */
void JNICALL Java_mkdb_Storage_browse (JNIEnv * env, jobject, jlong handle, jobject walk, jint cachesize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	jenv.JExceptionThrowIf(cachesize < 0, "java/lang/IllegalArgumentException", "cachesize is negative.");
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	MyQueryData walker(jenv, walk);
	storage->browse(&walker, (size_t)cachesize);
	__TRY__END__
}

/*
 * Class:     mkdb_Storage
 * Method:    exist
 * Signature: (J[BI)Z
 */
jboolean JNICALL Java_mkdb_Storage_exist(JNIEnv * env, jobject, jlong handle, jbyteArray key, jint keysize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	return storage->exist(JByteArray(jenv, key).data(), keysize) ?  JNI_TRUE : JNI_FALSE;
	__TRY__END__
	return JNI_FALSE;
}

/*
 * Class:     mkdb_Storage
 * Method:    find
 * Signature: (J[BI)[B
 */
jbyteArray JNICALL Java_mkdb_Storage_find(JNIEnv * env, jobject, jlong handle, jbyteArray key, jint keysize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	__xdb_helper::Value value;
	storage->find(JByteArray(jenv, key).data(), keysize, value);
	if (NULL == value.data())
		return NULL;
	return jenv.NewByteArray(value.data(), value.size());
	__TRY__END__
	return NULL;
}

/*
 * Class:     mkdb_Storage
 * Method:    replace
 * Signature: (J[BI[BI)Z
 */
jboolean JNICALL Java_mkdb_Storage_replace(JNIEnv * env, jobject, jlong handle,
	jbyteArray key, jint keysize, jbyteArray value, jint valuesize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	return storage->replace(JByteArray(jenv, key).data(), keysize, JByteArray(jenv, value).data(), valuesize) ? JNI_TRUE : JNI_FALSE;
	__TRY__END__
	return JNI_FALSE;
}

/*
 * Class:     mkdb_Storage
 * Method:    insert
 * Signature: (J[BI[BI)Z
 */
jboolean JNICALL Java_mkdb_Storage_insert(JNIEnv * env, jobject, jlong handle,
	jbyteArray key, jint keysize, jbyteArray value, jint valuesize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	JByteArray jkey(jenv, key), jvalue(jenv, value);
	return storage->insert(jkey.data(), keysize, jvalue.data(), valuesize) ? JNI_TRUE : JNI_FALSE;
	__TRY__END__
	return JNI_FALSE;
}

/*
 * Class:     mkdb_Storage
 * Method:    remove
 * Signature: (J[BI)Z
 */
jboolean JNICALL Java_mkdb_Storage_remove(JNIEnv * env, jobject, jlong handle, jbyteArray key, jint keysize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	return storage->remove(JByteArray(jenv, key).data(), keysize) ? JNI_TRUE : JNI_FALSE;
	__TRY__END__
	return JNI_FALSE;
}

/*
 * Class:     mkdb_Storage
 * Method:    open
 * Signature: (JLjava/lang/String;II)J
 */
jlong JNICALL Java_mkdb_Storage_open(JNIEnv * env, jobject, jlong loggerhandle, jstring filename, jint high, jint low)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	GNET::Logger * logger = reinterpret_cast<GNET::Logger*>(loggerhandle);
	// 转到基类再 reinterpret_cast。虽然这里没有继承关系，不过这样写应该是个好习惯。
	__xdb_helper::Storage * storage = new __xdb_helper::Storage(logger, JUTFString(jenv, filename).c_str(), high, low);
	return reinterpret_cast<jlong>(storage);
	__TRY__END__
	return 0;
}

/*
 * Class:     mkdb_Storage
 * Method:    close
 * Signature: (J)V
 */
void JNICALL Java_mkdb_Storage_close(JNIEnv * env, jobject, jlong handle)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	delete storage;
	__TRY__END__
}

/*
 * Class:     mkdb_Storage
 * Method:    snapshot_create
 * Signature: (J)V
 */
void JNICALL Java_mkdb_Storage_snapshot_1create(JNIEnv * env, jobject, jlong handle)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	storage->snapshot_create();
	__TRY__END__
}

/*
 * Class:     mkdb_Storage
 * Method:    snapshot_release
 * Signature: (J)V
 */
void JNICALL Java_mkdb_Storage_snapshot_1release(JNIEnv * env, jobject, jlong handle)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	storage->snapshot_release();
	__TRY__END__
}

class MyQueryDataRaw : public GNET::IQueryData
{
private:
	const JEnv & jenv;
	jobject	     iwalk;
	jmethodID	 jmid;
public:
	MyQueryDataRaw(const JEnv & _jenv, jobject iwalk)
		: jenv(_jenv)
	{
		this->iwalk = iwalk;
		this->jmid = jenv.GetMethodID(jenv.GetObjectClass(iwalk), "onRecord", "([B[B)Z");
	}

	virtual bool update( const void *key, size_t key_len, const void *val, size_t val_len )
	{
		jbyteArray keyarray = this->jenv.NewByteArray(key, key_len);
		jbyteArray valuearray = this->jenv.NewByteArray(val, val_len);
		jboolean ret = this->jenv.CallBooleanMethod(this->iwalk, this->jmid, keyarray, valuearray);

		{ // release every updata call, not until walk total complete
			this->jenv.DeleteLocalRef(keyarray);
			this->jenv.DeleteLocalRef(valuearray);
		}
		return ret;
	}
};

/*
 * Class:     mkdb_Storage
 * Method:    _walk
 * Signature: (JLmkdb/Storage/IWalk;)V
 */
void JNICALL Java_mkdb_Storage__1walk(JNIEnv * env, jobject,
	jlong handle, jobject walk)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	MyQueryDataRaw walker(jenv, walk);
	storage->walk(&walker);
	__TRY__END__
}

/*
 * Class:     mkdb_Storage
 * Method:    _browse
 * Signature: (JLmkdb/Storage/IWalk;I)V
 */
void JNICALL Java_mkdb_Storage__1browse(JNIEnv * env, jobject,
	jlong handle, jobject walk, jint cachesize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	jenv.JExceptionThrowIf(cachesize < 0, "java/lang/IllegalArgumentException", "cachesize is negative.");
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	MyQueryDataRaw walker(jenv, walk);	
	storage->browse(&walker, (size_t)cachesize);
	__TRY__END__
}

/*
 * Class:     mkdb_Storage
 * Method:    _find
 * Signature: (J[BI)[B
 */
jbyteArray JNICALL Java_mkdb_Storage__1find(JNIEnv * env, jobject,
	jlong handle, jbyteArray key, jint keysize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	__xdb_helper::Value value;
	storage->_find(JByteArray(jenv, key).data(), keysize, value);
	if (value.data())
		return jenv.NewByteArray(value.data(), value.size());
	__TRY__END__
	return NULL;
}

/*
 * Class:     mkdb_Storage
 * Method:    _replace
 * Signature: (J[BI[BI)Z
 */
jboolean JNICALL Java_mkdb_Storage__1replace(JNIEnv * env,
	jobject, jlong handle, jbyteArray key, jint keysize, jbyteArray value, jint valuesize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	return storage->_replace(JByteArray(jenv, key).data(), keysize, JByteArray(jenv, value).data(), valuesize)
		? JNI_TRUE : JNI_FALSE;
	__TRY__END__
	return JNI_FALSE;
}

/*
 * Class:     mkdb_Storage
 * Method:    _insert
 * Signature: (J[BI[BI)Z
 */
jboolean JNICALL Java_mkdb_Storage__1insert(JNIEnv * env, jobject,
	jlong handle, jbyteArray key, jint keysize, jbyteArray value, jint valuesize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	return storage->_insert(JByteArray(jenv, key).data(), keysize, JByteArray(jenv, value).data(), valuesize);
	__TRY__END__
	return JNI_FALSE;
}

/*
 * Class:     mkdb_Storage
 * Method:    firstKey
 * Signature: (J)[B
 */
jbyteArray JNICALL Java_mkdb_Storage_firstKey(JNIEnv * env, jobject, jlong handle)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	__xdb_helper::Value result;
	if (storage->firstKey(result))
		return jenv.NewByteArray(result.data(), result.size());
	__TRY__END__
	return NULL;
}

/*
 * Class:     mkdb_Storage
 * Method:    nextKey
 * Signature: (J[BI)[B
 */
jbyteArray JNICALL Java_mkdb_Storage_nextKey(JNIEnv * env, jobject, jlong handle, jbyteArray key, jint keysize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Storage * storage = reinterpret_cast<__xdb_helper::Storage*>(handle);
	__xdb_helper::Value result;
	if (storage->nextKey(JByteArray(jenv, key).data(), keysize, result))
		return jenv.NewByteArray(result.data(), result.size());
	__TRY__END__
	return NULL;
}

/*
 * Class:     mkdb_Storage
 * Method:    _compress
 * Signature: ([BI)[B
 */
jbyteArray JNICALL Java_mkdb_Storage__1compress(JNIEnv * env, jclass, jbyteArray data, jint datasize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Value result;
	__xdb_helper::compress(JByteArray(jenv, data).data(), datasize, result);
	return jenv.NewByteArray(result.data(), result.size());
	__TRY__END__
	return NULL;
}

/*
 * Class:     mkdb_Storage
 * Method:    _uncompress
 * Signature: ([BI)[B
 */
jbyteArray JNICALL Java_mkdb_Storage__1uncompress(JNIEnv * env, jclass, jbyteArray data, jint datasize)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	__xdb_helper::Value result;
	__xdb_helper::uncompress(JByteArray(jenv, data).data(), datasize, result);
	return jenv.NewByteArray(result.data(), result.size());
	__TRY__END__
	return NULL;
}
