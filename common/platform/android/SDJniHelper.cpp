#include "SDJniHelper.h"
#include <android/log.h>
#include <string.h>

#if 1
#define  LOG_TAG    "SDJniHelper"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define  LOGD(...) 
#endif

namespace MT3
{

#define JAVAVM    JniHelper::getJavaVM()

using namespace std;

extern "C"
{


    //////////////////////////////////////////////////////////////////////////
    // java vm helper function
    //////////////////////////////////////////////////////////////////////////

    static bool getEnv(JNIEnv **env)
    {
        bool bRet = false;

        do 
        {
			// ·ÀÖ¹ÓÎÏ·¸ÕÆô¶¯Ê±¿ÕÖ¸Õë´íÎó
			if (!JAVAVM)
			{
				break;
			}

			if (JAVAVM->GetEnv((void**)env, JNI_VERSION_1_4) != JNI_OK)
            {
                LOGD("Failed to get the environment using GetEnv()");
                break;
            }

            if (JAVAVM->AttachCurrentThread(env, 0) < 0)
            {
                LOGD("Failed to get the environment using AttachCurrentThread()");
                break;
            }

            bRet = true;
        } while (0);        

        return bRet;
    }

    static jclass getClassID_(const char *className, JNIEnv *env)
    {
        JNIEnv *pEnv = env;
        jclass ret = 0;

        do 
        {
            if (! pEnv)
            {
                if (! getEnv(&pEnv))
                {
                    break;
                }
            }
            
            ret = pEnv->FindClass(className);
            if (! ret)
            {
				LOGD("Failed to find class of %s", className);
				pEnv->ExceptionClear();
				break;
            }
        } while (0);

        return ret;
    }

    static bool getStaticMethodInfo_(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
    {
        jmethodID methodID = 0;
        JNIEnv *pEnv = 0;
        bool bRet = false;

        do 
        {
            if (! getEnv(&pEnv))
            {
                break;
            }

            jclass classID = getClassID_(className, pEnv);
			if (! classID)
			{
				break;
			}

            methodID = pEnv->GetStaticMethodID(classID, methodName, paramCode);
            if (! methodID)
            {
				LOGD("Failed to find static method id of %s", methodName);
				pEnv->ExceptionClear();
				break;
            }

            methodinfo.classID = classID;
            methodinfo.env = pEnv;
            methodinfo.methodID = methodID;

            bRet = true;
        } while (0);

        return bRet;
    }

    static bool getMethodInfo_(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
    {
        jmethodID methodID = 0;
        JNIEnv *pEnv = 0;
        bool bRet = false;

        do 
        {
            if (! getEnv(&pEnv))
            {
                break;
            }

            jclass classID = getClassID_(className, pEnv);

            methodID = pEnv->GetMethodID(classID, methodName, paramCode);
            if (! methodID)
            {
                LOGD("Failed to find method id of %s", methodName);
                break;
            }

            methodinfo.classID = classID;
            methodinfo.env = pEnv;
            methodinfo.methodID = methodID;

            bRet = true;
        } while (0);

        return bRet;
    }

    static string jstring2string_(jstring jstr)
    {
        if (jstr == NULL)
        {
            return "";
        }
        
        JNIEnv *env = 0;

        if (! getEnv(&env))
        {
            return 0;
        }

        const char* chars = env->GetStringUTFChars(jstr, NULL);
        string ret(chars);
        env->ReleaseStringUTFChars(jstr, chars);

        return ret;
    }
}


JavaVM* JniHelper::m_psJavaVM = NULL;

JavaVM* JniHelper::getJavaVM()
{
    return m_psJavaVM;
}

void JniHelper::setJavaVM(JavaVM *javaVM)
{
    m_psJavaVM = javaVM;
}

jclass JniHelper::getClassID(const char *className, JNIEnv *env)
{
    return getClassID_(className, env);
}

bool JniHelper::getStaticMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
{
    return getStaticMethodInfo_(methodinfo, className, methodName, paramCode);
}

bool JniHelper::getMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
{
    return getMethodInfo_(methodinfo, className, methodName, paramCode);
}

string JniHelper::jstring2string(jstring str)
{
    return jstring2string_(str);
}

}
