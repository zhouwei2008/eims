// Place your Spring DSL code here
beans = {
    channel(EDU.oswego.cs.dl.util.concurrent.BoundedBuffer,10){
    }
    threadPool(EDU.oswego.cs.dl.util.concurrent.PooledExecutor,ref('channel'),30){
        minimumPoolSize=10
        keepAliveTime=-1
    }
}
