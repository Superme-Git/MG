
rpcgen 根据配置文件中定义的协议生成相应的代码。生成代码完成 marshal，unmarshal 等协议相关的方法。

-----------------------------------------------------------------------------------------------
这个工具使用简单，但功能杂乱。参考现存的例子，直接使用是最快的入门方式。
这个文档纪录了所有基本限制和细节。仔细阅读一次，记住多少算多少。

[注意] 
   1 本文档很可能不包含后续小变化。
   2 这个工具的设计原则是尽可能严格检查错误。
     错误报告不是很精确，不太容易定位（偷懒阿），还请谅解。
     如果某些错误很容易发生，请报告给liuxinhua@locojoy.com。
     慢慢改进错误提示。

-----------------------------------------------------------------------------------------------

一、运行参数

    java -jar rpcgen.jar [options] ...

    Usage: java -jar rpcgen.jar [options] ...
            -h --help     print help

二、配置文件
    在配置文件中可以定义的对象，application,protocol,rpc,provider,bean,service,manager。
    rpcgen以service为单位进行生成。根据service中定义的manager所引入的state。
    找到所有引入protocol,rpc,provider，生成相应的代码模板。
    详细说明和关系：

    <application        定义应用。
            name        应用名字，也是第一级名字空间，所有在此应用内定义的对象都被包含在此名字空间中。
            shareHome   share库的路径，用于生成Makefile。仅在生成 c++ 版本时有意义。
            pvids       规定此应用内所有Provider的pvid范围。pvid需要全局唯一。规定范围减少冲突。
            >
    
        <protocol       定义协议。参考share/io/protocol.h。
            name        协议名字。
            type        协议编号。必须全局唯一。
            maxsize     协议包的最大长度。安全限制，抵抗攻击。
            prior       协议优先级。仅在生成 c++ 版本时有意义。
    
            <variable   定义协议变量参数。可用的变量参见后面支持的"基本变量类型表"。
                name    变量名。这里可以使用基本类型和自定义类型。
                type    变量类型。
                value   如果变量是模板容器类型，这里指名模板Value类型的名字。
                key     如果变量是模板关联容器类型，这里指名模板Key类型的名字。
                />
        </protocol>
    
        <bean           自定义数据结构类型。
            name        自定义类型名字。
            />
    
        <rpc            定义RPC协议。参考share/io/rpc.h。
            name        RPC名字。
            argument    RPC请求参数类型。
            result      RPC响应结果类型。
            type        协议编号。必须全局唯一。
            maxsize     协议包的最大长度。安全限制，抵抗攻击。
            prior       协议优先级。仅在生成 c++ 版本时有意义。
            timeout     RPC请求超时配置。
            />
    
        <provider       定义协议组。
            name        协议组名字。
            pvid        协议组编号。必须全局唯一。必须在 application.pvids 范围内。
            >
            <protocol/> 定义协议。
            <rpc/>      定义RPC协议。
        </provider>
    
        <state          定义状态。包含引用的Protocol,Rpc,Provider。
            name        状态名字。
            timeout     状态超时配置
            >
            <protocol
                ref     引用的协议名字。如果协议定义在其他名字空间中，需要使用全名。
                />
            <rpc
                ref     引用的RPC协议名字。如果RPC协议定义在其他名字空间中，需要使用全名。
                />
            <provider
                ref     引用的协议组名字。如果协议组定义在其他名字空间中，需要使用全名。
                />
        </state>
    
        <service        定义服务。rpcgen以服务为单位进行生成。
            name        服务名字以及名字空间。rpcgen会把所有文件都生成到此目录下。
            >
            <manager      定义连接管理器。参考 share/io/protocol.h
                name      管理器名字。
                type      管理器类型。client OR server OR provider
                initstate 初始状态名字。引入相应状态。
                port      socket端口。用于生成初始配置。
                bind      建立连接时，注册对应的Provider。只有在type为provider时有效。
                >
                <state
                    ref   引用状态名字。如果状态定义在其他名字空间中，需要使用全名。
                    />
            </manager>
        </service>
    </application>

    * 定义在Provider外的Protocol,Rpc，主要用于兼容以前定义的协议（如 au,gacd），新协议建议定义到Provider中。
    * 与旧版rpcgen的最大不同在于，service,manager,state的关系。
      旧版rpcgen中，service负责引入state，manager.initstate 设置初始配置。
      新版rpcgen中，manager负责引入state，manager.initstate 也具有引入功能。

      不管哪种引入方式，对于生成的c++来说，所有manager都能共享引入的所有state。
      对于生成的java来说，manager只支持它引入的state中包含的所有协议。


三、配置更改和重新生成限制
    当修改名字空间，会在新的名字空间下会生成，不会删除旧空间下的文件。
    当修改对象名字时，会生成新的文件，不会删除旧文件。

    部分文件只生成一次，和这些文件相关的配置以后即使修改了，以后也不会自动更新到代码中。

    1 会重新生成的文件：
        c++:  rpcgen.cpp,rpcgen.hpp,protocols.hpp,rpcgen/
        java: beans/, XXX.xio.xml的Coder部分总是会被覆盖。

	2 除了第一次生成，更改可能无效的配置。

	                    c++                         java
    <application
            shareHome   忽略更改                    未使用
            >    
        <service>
            <manager    
                type      忽略更改                  忽略更改
                initstate 忽略更改                  未使用
                port      忽略更改                  忽略更改
                bind      忽略更改                  支持更新，重新生成时，会把新配置更新到对应的XXX.xio.xml中。
                >
            </manager>
        </service>
    </application>

四、基本类型和编程语言的对应关系

  名字     c++            java
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

五、变量默认值

1.整数类型初始化
默认值原来只是原样生成出来，没有经过任何检查和处理。这样默认值实际上可以使用其他地方定义的常量。
可以使用常量名字，当常量名字包含名字空间时，由于java,c++分隔符不一样会有问题。rpcgen自动转换。

2.文本类型初始化，
这个类型初始化无法使用常量，没有规则可以区分它们。

3.浮点类型初始化
由于’.’是java的名字空间分隔符，也是浮点数字的分隔符。Rpcgen处理顺序如下。
如果默认值是否合法的浮点常数表达格式，就当作常数生成，否则当作常量生成（生成c++时，会把’.’转换为’::’）。

如：
  <variable name="f1" type="float" default="b.b"/> 名字
  <variable name="f2" type="float" default="0.1f"/> 常数
  <variable name="text" type="string" default="abc"/> 字符串初始化

六、协议的有效性验证
  发送或者接收协议时，对协议进行有效性验证。

  定义
      定义在variable中，如：
      <variable ... validator="value=[min,max];capacity=max;key=[min,max]"/>

  值域验证
      定义验证值的范围，如：value=[min,max]。是否包含min或者max使用"(),[]"来区分。
      如下例子：
      value=[min,max] 等价于 min <= value && value <= max。
      value=(min,max] 等价于 min <  value && value <= max。

      开区间定义
      value=[min,) 等价于 min <= value。开区间的结束符号没有意义，建议用")"。
      value=(,) 相当于不验证。开区间的开始结束符号没有意义，建议用"()"。

      其他：
      定义值域时，分割符"()[],"都是必须的，不能省略。-- 考虑了一下，觉得这些符号最好都不提供默认值。
      为了灵活值的填写，生成代码时不验证值的类型范围及有效性，所以要注意数值的写法，如整数值大于int范围，要在数值结尾加"L"。
      同时，也支持C++及java同时支持的数值写法，如：0x1000，1.23e-5f，1.0/7。语法错误只能在编译时发现。
      key也是值域定义的一种，仅用于定义map的key的值域。其他含义和value一样。
      只有基本的数值类型支持值域验证定义。见下面的表格。

  容量验证
      capacity=max。定义变长类型的最大容量。一般是容器，表示容器可包含的最大元素个数。

  类型支持的有效性验证

      名字     validator
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

      * 是否支持要看这些容器包含的类型是否支持值域验证。

  使用说明
      验证只用于协议(protocol)，不支持RPC。只在协议发送(marshal之前)和协议接收(unmarshal之后)时进行验证。
      代码生成时给每个 bean 均固定生成 _validator_() 函数。
      rpcgen 有 -validateMarshal 和 -validateUnmarshal 两个参数，
      分别表示在 marshal 之前和 unmarshal 之后插入 _validator_() 的调用判断和验证失败的处理，默认不启用。
      一般来说，服务器发送的协议不验证 marshal，客户端发送和接收都验证。

      验证失败的处理目前是：java版抛出 java.lang.VerifyError("validator failed") 异常；C++版抛出 Marshal::Exception() 异常。
      可以根据需要在 rpcgen.visitor.MarshalJava.make(...) 及 rpcgen.visitor.MarshalCxx.make(...) 中修改。

  优化效率（尚未实现）
      如果 _validator_() 为空，省略掉调用。这是由于验证容器时，需要验证所有元素，应该有较明显的效果。
      实现提示：需要实现 hasValidator()，需要递归判断内部包含的bean。编译器可能会完成这个优化，第一版先不实现。

  protocol.maxsize（尚未实现）
      当变长类型定义了 capacity，那么整个协议的最大大小也可估计了。
      当估计出来的大小和原来的maxsize定义相差太大时，打印警告提示。
      考虑到兼容性，不直接使用估计出来的大小代替原来的maxsize定义，
      必须通过显式的设置选项才会替换。
      * 备注 * 这个功能第一版不实现。
