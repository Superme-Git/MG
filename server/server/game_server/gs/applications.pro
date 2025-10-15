#
# This ProGuard configuration file illustrates how to process applications.
# Usage:
#     java -jar proguard.jar @applications.pro
#

# Specify the input jars, output jars, and library jars.

-injars dist/gs.jar
-outjars dist/gsencry.jar
#-injars dist/hotdeploy1.jar
#-outjars dist/hotdeploy.jar
#-injars lib/monkeyking.jar
#-outjars dist/monkeyking.jar

-libraryjars lib/commons-beanutils-1.8.0.jar
-libraryjars lib/commons-collections-3.1.jar
-libraryjars lib/commons-lang-2.6.jar
-libraryjars lib/commons-logging-1.2.jar
-libraryjars lib/convxml.jar
-libraryjars lib/counter.jar
-libraryjars lib/ezmorph-1.0.6.jar
-libraryjars lib/freemarker.jar
-libraryjars lib/h2-1.3.176.jar
#-libraryjars lib/hotagent.jar
#-libraryjars lib/iweb.jar
-libraryjars lib/jio.jar
-libraryjars lib/jmxc.jar
-libraryjars lib/json-lib-2.3-jdk15.jar
-libraryjars lib/log4j-1.2.15.jar
-libraryjars lib/poi-3.7-beta1-20100620.jar
-libraryjars lib/monkeyking.jar
-libraryjars lib/xmlpull-1.1.3.1.jar
-libraryjars lib/xpp3_min-1.1.4c.jar
-libraryjars lib/xstream-1.4.3.jar
#-libraryjars lib2/scaner.jar
-libraryjars libsys/rt.jar
-libraryjars lib/commons-codec-1.9.jar 
-libraryjars lib/httpasyncclient-4.1.1.jar 
-libraryjars lib/httpclient-4.5.2.jar 
-libraryjars lib/httpcore-4.4.4.jar 
-libraryjars lib/httpcore-nio-4.4.4.jar
-libraryjars lib/log4j-1.2-api-2.6.jar
-libraryjars lib/log4j-api-2.6.jar
-libraryjars lib/log4j-core-2.6.jar
-libraryjars lib/disruptor-3.3.4.jar


# Save the obfuscation mapping to a file, so you can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.

-printmapping out.map
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Preserve all annotations.

###################gs工程的类 start###############################
-dontwarn com.jmxservice.mt3interfaces.**
-keep class com.jmxservice.mt3interfaces.** { *;}

#-dontwarn config.**
#-keep class config.** { *;}

-dontwarn cross.**
#-keep class cross.** { *;}
-keepnames class cross.** { *;}

-dontwarn fire.**
-keep class fire.** { *;}
#-keepclasseswithmembers class fire.** { *;}

-dontwarn gacd.**
-keep class gacd.** { *;}

-dontwarn gnet.**
-keep class gnet.** { *;}
#-keepnames class gnet.** { *;}

#-dontwarn xbean.**
#-keep class xbean.** { *;}

-dontwarn mkdb.**
-keep class mkdb.** { *;}

-dontwarn mkio.**
-keep class mkio.** { *;}

#这个不能混淆，要不会报找不到_DatabaseMetaData_的异常
-dontwarn xtable.**
#-keep class xtable.** { *;}
-keep class xtable._DatabaseMetaData_
-keepnames class xtable.** { *;}

-dontwarn xunbao.**
-keep class xunbao.** { *;}


###################gs工程的类 end###############################

-dontwarn mkio.**
-keep class mkio.** { *;}
-dontwarn xgen.**
-keep class xgen.** { *;}
-dontwarn mkdb.**
-keep class mkdb.** { *;}

# You can print out the seeds that are matching the keep options below.

#-printseeds out.seeds

# Preserve all public applications.

-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Preserve all native method names and the names of their classes.

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# Preserve the special static methods that are required in all enumeration
# classes.

-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
# You can comment this out if your application doesn't use serialization.
# If your code contains serializable classes that have to be backward 
# compatible, please refer to the manual.

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Your application may contain more items that need to be preserved; 
# typically classes that are dynamically created using Class.forName:

# -keep public class mypackage.MyClass
# -keep public interface mypackage.MyInterface
# -keep public class * implements mypackage.MyInterface
