
rpcgen ���������ļ��ж����Э��������Ӧ�Ĵ��롣���ɴ������ marshal��unmarshal ��Э����صķ�����

-----------------------------------------------------------------------------------------------
�������ʹ�ü򵥣����������ҡ��ο��ִ�����ӣ�ֱ��ʹ�����������ŷ�ʽ��
����ĵ���¼�����л������ƺ�ϸ�ڡ���ϸ�Ķ�һ�Σ���ס��������١�

[ע��] 
   1 ���ĵ��ܿ��ܲ���������С�仯��
   2 ������ߵ����ԭ���Ǿ������ϸ������
     ���󱨸治�Ǻܾ�ȷ����̫���׶�λ��͵�������������½⡣
     ���ĳЩ��������׷������뱨���liuxinhua@locojoy.com��
     �����Ľ�������ʾ��

-----------------------------------------------------------------------------------------------

һ�����в���

    java -jar rpcgen.jar [options] ...

    Usage: java -jar rpcgen.jar [options] ...
            -h --help     print help

���������ļ�
    �������ļ��п��Զ���Ķ���application,protocol,rpc,provider,bean,service,manager��
    rpcgen��serviceΪ��λ�������ɡ�����service�ж����manager�������state��
    �ҵ���������protocol,rpc,provider��������Ӧ�Ĵ���ģ�塣
    ��ϸ˵���͹�ϵ��

    <application        ����Ӧ�á�
            name        Ӧ�����֣�Ҳ�ǵ�һ�����ֿռ䣬�����ڴ�Ӧ���ڶ���Ķ��󶼱������ڴ����ֿռ��С�
            shareHome   share���·������������Makefile���������� c++ �汾ʱ�����塣
            pvids       �涨��Ӧ��������Provider��pvid��Χ��pvid��Ҫȫ��Ψһ���涨��Χ���ٳ�ͻ��
            >
    
        <protocol       ����Э�顣�ο�share/io/protocol.h��
            name        Э�����֡�
            type        Э���š�����ȫ��Ψһ��
            maxsize     Э�������󳤶ȡ���ȫ���ƣ��ֿ�������
            prior       Э�����ȼ����������� c++ �汾ʱ�����塣
    
            <variable   ����Э��������������õı����μ�����֧�ֵ�"�����������ͱ�"��
                name    ���������������ʹ�û������ͺ��Զ������͡�
                type    �������͡�
                value   ���������ģ���������ͣ�����ָ��ģ��Value���͵����֡�
                key     ���������ģ������������ͣ�����ָ��ģ��Key���͵����֡�
                />
        </protocol>
    
        <bean           �Զ������ݽṹ���͡�
            name        �Զ����������֡�
            />
    
        <rpc            ����RPCЭ�顣�ο�share/io/rpc.h��
            name        RPC���֡�
            argument    RPC����������͡�
            result      RPC��Ӧ������͡�
            type        Э���š�����ȫ��Ψһ��
            maxsize     Э�������󳤶ȡ���ȫ���ƣ��ֿ�������
            prior       Э�����ȼ����������� c++ �汾ʱ�����塣
            timeout     RPC����ʱ���á�
            />
    
        <provider       ����Э���顣
            name        Э�������֡�
            pvid        Э�����š�����ȫ��Ψһ�������� application.pvids ��Χ�ڡ�
            >
            <protocol/> ����Э�顣
            <rpc/>      ����RPCЭ�顣
        </provider>
    
        <state          ����״̬���������õ�Protocol,Rpc,Provider��
            name        ״̬���֡�
            timeout     ״̬��ʱ����
            >
            <protocol
                ref     ���õ�Э�����֡����Э�鶨�����������ֿռ��У���Ҫʹ��ȫ����
                />
            <rpc
                ref     ���õ�RPCЭ�����֡����RPCЭ�鶨�����������ֿռ��У���Ҫʹ��ȫ����
                />
            <provider
                ref     ���õ�Э�������֡����Э���鶨�����������ֿռ��У���Ҫʹ��ȫ����
                />
        </state>
    
        <service        �������rpcgen�Է���Ϊ��λ�������ɡ�
            name        ���������Լ����ֿռ䡣rpcgen��������ļ������ɵ���Ŀ¼�¡�
            >
            <manager      �������ӹ��������ο� share/io/protocol.h
                name      ���������֡�
                type      ���������͡�client OR server OR provider
                initstate ��ʼ״̬���֡�������Ӧ״̬��
                port      socket�˿ڡ��������ɳ�ʼ���á�
                bind      ��������ʱ��ע���Ӧ��Provider��ֻ����typeΪproviderʱ��Ч��
                >
                <state
                    ref   ����״̬���֡����״̬�������������ֿռ��У���Ҫʹ��ȫ����
                    />
            </manager>
        </service>
    </application>

    * ������Provider���Protocol,Rpc����Ҫ���ڼ�����ǰ�����Э�飨�� au,gacd������Э�齨�鶨�嵽Provider�С�
    * ��ɰ�rpcgen�����ͬ���ڣ�service,manager,state�Ĺ�ϵ��
      �ɰ�rpcgen�У�service��������state��manager.initstate ���ó�ʼ���á�
      �°�rpcgen�У�manager��������state��manager.initstate Ҳ�������빦�ܡ�

      �����������뷽ʽ���������ɵ�c++��˵������manager���ܹ������������state��
      �������ɵ�java��˵��managerֻ֧���������state�а���������Э�顣


�������ø��ĺ�������������
    ���޸����ֿռ䣬�����µ����ֿռ��»����ɣ�����ɾ���ɿռ��µ��ļ���
    ���޸Ķ�������ʱ���������µ��ļ�������ɾ�����ļ���

    �����ļ�ֻ����һ�Σ�����Щ�ļ���ص������Ժ�ʹ�޸��ˣ��Ժ�Ҳ�����Զ����µ������С�

    1 ���������ɵ��ļ���
        c++:  rpcgen.cpp,rpcgen.hpp,protocols.hpp,rpcgen/
        java: beans/, XXX.xio.xml��Coder�������ǻᱻ���ǡ�

	2 ���˵�һ�����ɣ����Ŀ�����Ч�����á�

	                    c++                         java
    <application
            shareHome   ���Ը���                    δʹ��
            >    
        <service>
            <manager    
                type      ���Ը���                  ���Ը���
                initstate ���Ը���                  δʹ��
                port      ���Ը���                  ���Ը���
                bind      ���Ը���                  ֧�ָ��£���������ʱ����������ø��µ���Ӧ��XXX.xio.xml�С�
                >
            </manager>
        </service>
    </application>

�ġ��������ͺͱ�����ԵĶ�Ӧ��ϵ

  ����     c++            java
  ------------------------------------------------
  byte     char           byte
  int      int            int
  float    float          float
  long     int64_t        long
  octets   GNET::Octets   com.locojoy.base.Octets
  string   GNET::Octets   java.lang.String             *** encode="UTF-16LE"
  list     std::list      java.util.LinkedList
  vector   std::vector    java.util.ArrayList
  set      std::set       java.util.HashSet
  map      std::map       java.util.HashMap

�塢����Ĭ��ֵ

1.�������ͳ�ʼ��
Ĭ��ֵԭ��ֻ��ԭ�����ɳ�����û�о����κμ��ʹ�������Ĭ��ֵʵ���Ͽ���ʹ�������ط�����ĳ�����
����ʹ�ó������֣����������ְ������ֿռ�ʱ������java,c++�ָ�����һ���������⡣rpcgen�Զ�ת����

2.�ı����ͳ�ʼ����
������ͳ�ʼ���޷�ʹ�ó�����û�й�������������ǡ�

3.�������ͳ�ʼ��
���ڡ�.����java�����ֿռ�ָ�����Ҳ�Ǹ������ֵķָ�����Rpcgen����˳�����¡�
���Ĭ��ֵ�Ƿ�Ϸ��ĸ��㳣������ʽ���͵����������ɣ��������������ɣ�����c++ʱ����ѡ�.��ת��Ϊ��::������

�磺
  <variable name="f1" type="float" default="b.b"/> ����
  <variable name="f2" type="float" default="0.1f"/> ����
  <variable name="text" type="string" default="abc"/> �ַ�����ʼ��

����Э�����Ч����֤
  ���ͻ��߽���Э��ʱ����Э�������Ч����֤��

  ����
      ������variable�У��磺
      <variable ... validator="value=[min,max];capacity=max;key=[min,max]"/>

  ֵ����֤
      ������ֵ֤�ķ�Χ���磺value=[min,max]���Ƿ����min����maxʹ��"(),[]"�����֡�
      �������ӣ�
      value=[min,max] �ȼ��� min <= value && value <= max��
      value=(min,max] �ȼ��� min <  value && value <= max��

      �����䶨��
      value=[min,) �ȼ��� min <= value��������Ľ�������û�����壬������")"��
      value=(,) �൱�ڲ���֤��������Ŀ�ʼ��������û�����壬������"()"��

      ������
      ����ֵ��ʱ���ָ��"()[],"���Ǳ���ģ�����ʡ�ԡ�-- ������һ�£�������Щ������ö����ṩĬ��ֵ��
      Ϊ�����ֵ����д�����ɴ���ʱ����ֵ֤�����ͷ�Χ����Ч�ԣ�����Ҫע����ֵ��д����������ֵ����int��Χ��Ҫ����ֵ��β��"L"��
      ͬʱ��Ҳ֧��C++��javaͬʱ֧�ֵ���ֵд�����磺0x1000��1.23e-5f��1.0/7���﷨����ֻ���ڱ���ʱ���֡�
      keyҲ��ֵ�����һ�֣������ڶ���map��key��ֵ�����������valueһ����
      ֻ�л�������ֵ����֧��ֵ����֤���塣������ı��

  ������֤
      capacity=max������䳤���͵����������һ������������ʾ�����ɰ��������Ԫ�ظ�����

  ����֧�ֵ���Ч����֤

      ����     validator
      ------------------------------------------------
      byte     value=[min,max]
      int      value=[min,max]
      float    value=[min,max]
      long     value=[min,max]
      octets   capacity=max
      string   capacity=max
      list     capacity=max *value=[min,max]
      vector   capacity=max *value=[min,max]
      set      capacity=max *value=[min,max]
      map      capacity=max *key=[min,max] *value=[min,max]

      * �Ƿ�֧��Ҫ����Щ���������������Ƿ�֧��ֵ����֤��

  ʹ��˵��
      ��ֻ֤����Э��(protocol)����֧��RPC��ֻ��Э�鷢��(marshal֮ǰ)��Э�����(unmarshal֮��)ʱ������֤��
      ��������ʱ��ÿ�� bean ���̶����� _validator_() ������
      rpcgen �� -validateMarshal �� -validateUnmarshal ����������
      �ֱ��ʾ�� marshal ֮ǰ�� unmarshal ֮����� _validator_() �ĵ����жϺ���֤ʧ�ܵĴ���Ĭ�ϲ����á�
      һ����˵�����������͵�Э�鲻��֤ marshal���ͻ��˷��ͺͽ��ն���֤��

      ��֤ʧ�ܵĴ���Ŀǰ�ǣ�java���׳� java.lang.VerifyError("validator failed") �쳣��C++���׳� Marshal::Exception() �쳣��
      ���Ը�����Ҫ�� rpcgen.visitor.MarshalJava.make(...) �� rpcgen.visitor.MarshalCxx.make(...) ���޸ġ�

  �Ż�Ч�ʣ���δʵ�֣�
      ��� _validator_() Ϊ�գ�ʡ�Ե����á�����������֤����ʱ����Ҫ��֤����Ԫ�أ�Ӧ���н����Ե�Ч����
      ʵ����ʾ����Ҫʵ�� hasValidator()����Ҫ�ݹ��ж��ڲ�������bean�����������ܻ��������Ż�����һ���Ȳ�ʵ�֡�

  protocol.maxsize����δʵ�֣�
      ���䳤���Ͷ����� capacity����ô����Э�������СҲ�ɹ����ˡ�
      �����Ƴ����Ĵ�С��ԭ����maxsize�������̫��ʱ����ӡ������ʾ��
      ���ǵ������ԣ���ֱ��ʹ�ù��Ƴ����Ĵ�С����ԭ����maxsize���壬
      ����ͨ����ʽ������ѡ��Ż��滻��
      * ��ע * ������ܵ�һ�治ʵ�֡�
