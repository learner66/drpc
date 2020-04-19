# drpc
一个rpc框架实现:rpc即是远程服务调用，客户端调用远程服务端的服务（方法）

整个client的流程是：

**1**. RPCClient得到一个服务的代理对象
    即是CalcService服务通过RPCClient的getProxy方法得到一个代理对象，该代理对象包含了序列化和反序列化所需要的类，以及选择器等。

    Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)
    loader:用哪个类加载器去加载代理对象
    interfaces:动态代理类需要实现的接口
    handler，动态代理方法在执行时，会调用h里面的invoke方法去执行
**2**. 在getProxy中：

    (T)Proxy.newProxyInstance(this.getClass().getClassLoader(),
                 new Class[]{clazz},
                 new RemoteInvoker(clazz,encoder,decoder,selector));
    clazz指的是CalcService的Class，
    handler指的是RemoteInvoker，RemoteInvoker中的invoke方法用来执行调用逻辑
**3**. 在RemoteInvoker中：

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         Request request = new Request();
         request.setService(ServiceDescriptor.from(clazz,method));
         request.setParameters(args);
         Response resp = invokeRemote(request);
         if(resp==null||resp.getCode()!=0){
             throw new IllegalStateException("fail to invoke remote: "+resp);
         }
         return resp.getData();
     }
       
**3.1**. 生成一个新的Request，包括服务描述ServiceDescriptor和参数parameters
**3.2**. 设置Request中的服务实例和参数
**3.3**. 调用远程方法
**3.4**. 返回Resp的数据

**4**. 调用add方法时会调用RemoteInvoker中的invoke方法
    proxy指的就是返回的代理对象CalcService
    method指的add方法
    args指的是add中的参数a和b
    request通过ServiceDescriptor来生成Service对象

**5**. ServiceDescriptor方法中:
    ServiceDescriptor有clazz,method,returnType,parameterType属性
    
    public static ServiceDescriptor from(Class clazz, Method method){
       ServiceDescriptor sdp = new ServiceDescriptor();
       //设置类
       sdp.setClazz(clazz.getName());
       //设置方法
       sdp.setMethod(method.getName());
       // 设置返回类型
       sdp.setReturnType(method.getReturnType().getName());
       //得到所有参数类型
       Class[] parameterClasses = method.getParameterTypes();
       String[] parameterTypes = new String[parameterClasses.length];
       for(int i = 0;i<parameterClasses.length;i++){
          parameterTypes[i] = parameterClasses[i].getName();
       }
       //设置参数类型
       sdp.setParameterTypes(parameterTypes);
       return sdp;
    }
    
    clazz.getName指的是CalcService方法，method.getName()指的是add,
    method.getReturnType().getName()指的是int类型，
    parameterClasses首先得到所有参数的类型，然后再得到参数的类型名，即Integer和Integer.
    返回该ServiceDescriptor，该对象中包括了服务（方法）所在的类，方法名，方法的返回类型，方法的参数类型.

**6**. 在生成ServiceDescriptor后，request设置了ServiceDescriptor以及parameters的实际值，
       即是request实际上包括了执行的方法描述和实参，然后调用invokeRemote(request)。

**7**. 在invokeRemote(request)方法中:

    private Response invokeRemote(Request request) {
         Response resp = null;
         TransportClient client = null;
         try{
             // 随机选择一个连接来处理该请求
             client = selector.select();
             // 将请求进行序列化
             byte[] outBytes = encoder.encode(request);
             // 请求写入到连接中
             InputStream receive = client.write(new ByteArrayInputStream(outBytes));
             byte[] inBytes = IOUtils.readFully(receive,
                     receive.available());
             // 将接受到的数据进行反序列化
             resp = decoder.decode(inBytes,Response.class);
         } catch (IOException e) {
             //log.warn(e.getMessage(),e);
             resp = new Response();
             resp.setCode(1);
             resp.setMessage("RpcClient got error: "+ e.getClass() + " : "+ e.getMessage());
         } finally {
             if(client!=null) {
                 selector.release(client);
             }
         }
         return resp;
     }
     
**7.1**. 在invokeRemote方法中首先选择一个选择器，并且将request请求序列化，然后将序列化后的内容写入到连接中。
**7.2**. 探究selector.select()方法，首先init方法为每个服务端建立多个连接，select便是选择其中一个连接端进行数据传递。
        
    public class RandomTransportSelector implements TransportSelector
         /**
          * 已经连接好的client
          */
         private List<TransportClient> clients;
         //利用CopyWriteArrayList保存服务
         public RandomTransportSelector(){
             this.clients = new CopyOnWriteArrayList<>();
         }
         /**
          * 为每个服务端建立多个连接
          * @param peers 可以连接的server端点信息
          * @param count client与server建立多少个连接
          * @param clazz client实现class
          */
         @Override
         public void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz) {
             count = Math.max(count,1);
             // 服务端，本地只有一个服务端
             for(Peer peer:peers){
                 //连接的个数
                 for(int i =0;i<count;i++){
                     //生成一个客户端实例
                     TransportClient client = ReflectionUtils.newInstance(clazz);
                     //客户端和服务端连接
                     client.connect(peer);
                     //保存已经连接好的客户端
                     clients.add(client);
                 }
                 //log.info("connect server: {}",peer);
             }
         }
     
         /**
          * 从已经连接好的客户端中，随机选择一个
          * @return
          */
         @Override
         public synchronized  TransportClient select() {
             int i = new Random().nextInt(clients.size());
             return clients.remove(i);
         }
        
    }
**7.3**. client传递数据并返回结果，使用IOUtils.readFully将结果内容读取出来并且反序列化。

整个server的流程是:
**1**. 服务注册，server启动

    server.register(CalcService.class,new CalcServiceImpl());
    server.start();
**2**. server.register是将CalcService注册到server中

    //服务注册
     public <T> void register(Class<T> interfaceClass,T bean){
         serviceManager.register(interfaceClass,bean);
     }