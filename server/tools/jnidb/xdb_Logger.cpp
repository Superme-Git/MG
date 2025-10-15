
#include "xdb_Logger.h"
#ifdef WIN32
#include "share/common/db_win32.h"
#else
#include "share/common/db.h"
#endif
#include "jcast.h"
#include "dbhelper.hpp"

jlong JNICALL Java_mkdb_Logger_create(JNIEnv * env, jobject, jstring dir, jint pages)
{
	JEnv jenv(env);
	__TRY__BEGIN__

	if (NULL != dir)
	{
		// 转到基类再 reinterpret_cast
		GNET::Logger * logger = new GNET::GlobalLogger(JUTFString(jenv, dir).c_str(), pages);
		return reinterpret_cast<jlong>(logger);
	}

	// special logger
	switch (pages)
	{
	case 0:
		{
			// 转到基类再 reinterpret_cast
			GNET::Logger * logger = new GNET::__db_helper::NullLogger();
			return reinterpret_cast<jlong>(logger);
		}
		break;
	case 1:
		{
			// 转到基类再 reinterpret_cast
			GNET::Logger * logger = new GNET::__db_helper::PageLogger();
			return reinterpret_cast<jlong>(logger);
		}
		break;

	default:
		return 0;
	}

	__TRY__END__
	return 0;
}

/*
 * return
 *   0  success
 *   1  recover success. retry once.
 *   -1 error
 */
jint JNICALL Java_mkdb_Logger_verify(JNIEnv * env, jobject, jlong handle)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	GNET::Logger * logger = reinterpret_cast<GNET::Logger*>(handle);

	switch (logger->integrity_verify())
	{
	case GNET::PageLogger::CLEAN:
		return 0;

	case GNET::PageLogger::CORRUPT:
		return GNET::PageLogger::CLEAN == logger->truncate() ? 1 : -1;

	case GNET::PageLogger::REDO:
		return GNET::PageLogger::CLEAN == logger->redo(time(NULL)) ? 1 : -1;

	default:
		return -1;
	}
	__TRY__END__
	return -1;
}

void JNICALL Java_mkdb_Logger_close(JNIEnv * env, jobject, jlong handle)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	GNET::Logger * logger = reinterpret_cast<GNET::Logger*>(handle);
	delete logger;
	__TRY__END__
}

jboolean JNICALL Java_mkdb_Logger_prepare(JNIEnv * env, jobject, jlong handle)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	GNET::Logger * logger = reinterpret_cast<GNET::Logger*>(handle);
	return logger->prepare() == GNET::Logger::PREPARED ? JNI_TRUE : JNI_FALSE;
	__TRY__END__
	return JNI_FALSE;
}

jboolean JNICALL Java_mkdb_Logger_commit(JNIEnv * env, jobject, jlong handle, jlong timestamp)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	GNET::Logger * logger = reinterpret_cast<GNET::Logger*>(handle);
	return logger->commit((GNET::__db_helper::time32_t)(timestamp/1000)) == GNET::Logger::CLEAN ? JNI_TRUE : JNI_FALSE;
	__TRY__END__
	return JNI_FALSE;
}

jlong JNICALL Java_mkdb_Logger_loggerid(JNIEnv * env, jobject, jlong handle)
{
	JEnv jenv(env);
	__TRY__BEGIN__
	GNET::Logger * logger = reinterpret_cast<GNET::Logger*>(handle);
	GNET::GlobalLogger * global = dynamic_cast<GNET::GlobalLogger*>(logger);
	if (NULL != global)
		return (jlong)global->get_logger_id();
	__TRY__END__
	return 0;
}

