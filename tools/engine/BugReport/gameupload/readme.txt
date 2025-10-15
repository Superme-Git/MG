1. prepare mysql server.
    create mysql database : game_upload
    create table exceptions using sql\gameupload.sql
2. install ant
3. install jdk
4. install tomcat

modify src\\hibernate.cfg.xml : 
  set the correct database connection url, connection username and connection password as the production environment:
    <property name="connection.url">
        jdbc:mysql://10.68.32.6:3306/game_upload?useUnicode=true&amp;characterEncoding=UTF-8&amp;characterSetResults=UTF-8
    </property>
    <property name="connection.username">root</property>
    <property name="connection.password"></property>

copy config/* ---> %CATALINA_HOME%/common/upload_config/
copy common-lib/mysql-connector-java-5.1.6-bin.jar ---> %CATALINA_HOME%/lib if there is not a similar one over there.
copy ant-lib/catalina-ant.jar ---> %ANT_HOME%/lib (the ant runtime lib directory).

modify build.xml:
  change the following properties value as the product environment: 
  <property name="manager.url"   value="http://localhost:8080/manager"/>
  <property name="manager.username"   value="tomcat"/>
  <property name="manager.password"   value="managertomcat"/>

auto deploy:
    run deploy.bat.
    or run build.bat,deploy-only.bat.
    It would do the following actions:
        1. pack WebContext/** ---> gameupload.war
        2. copy gameupload.war ---> %CATALINA_HOME%/webapps/

re-deploy:
    run undeploy.bat or remove.bat first, then run deploy.bat.

Please contact liudongxue01238@locojoy.com if any problems.

2011-6-29