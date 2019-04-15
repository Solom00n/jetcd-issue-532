# jetcd-issue-532
Simple project to reproduce jetcd issue [#532](https://github.com/etcd-io/jetcd/issues/532)

### Step 0 - build the project and create the docker image

> **Note:** you don't need to follow this step. A ready to use docker image with the same content of this repository can be found at docker hub:
> - **image tag**: solom0n89/jetcd-issue-532:latest
> - **docker hub link**: [jetcd-issue-532](https://cloud.docker.com/repository/docker/solom0n89/jetcd-issue-532)
>
> To skip this step run: `docker pull solom0n89/jetcd-issue-532:latest`
 
From the root directory of this repository, simply run the command:

```bash
docker-compose build
```
This will build the project and create a docker image with name *solom0n89/jetcd-issue-532* (see *docker-compose.yml* and *simple-watch/Dockerfile*).

### Step 1 - reproduce jetcd issue #532
1. Run the etcd cluster and the simple-watch app with `docker-compose up -d`
2. To see the simple-watch app logs run `docker-compose logs -f simple-watch`
3. Add a keyvalue pair in etcd to test the simple-watch app. Open a second terminal and run:

    ```bash
    docker-compose exec etcd0 sh
    / # etcdctl put key value0
    ```
4. Take a look at the simple-watch log. You should see something like *KEY: key - VALUE: value0*

5. Now stop the etcd cluster with `docker-compose stop etcd0 etcd1 etcd2` and wait for a while (my tests don't take more than five minutes). **Note:** immediately restarting the etcd cluster will **NOT** raise the issue.

6. After a while you should see this exception:

    ```bash
    simple-watch_1  | 2019-04-14 22:28:19.899 ERROR 1 --- [ault-executor-3] io.grpc.internal.SerializingExecutor     : Exception while executing runnable io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed@206f44cf
    simple-watch_1  |
    simple-watch_1  | java.lang.NullPointerException: null
    simple-watch_1  |       at io.etcd.jetcd.WatchImpl$WatcherImpl.onError(WatchImpl.java:250) ~[jetcd-core-0.3.0.jar!/:na]
    simple-watch_1  |       at io.grpc.stub.ClientCalls$StreamObserverToCallListenerAdapter.onClose(ClientCalls.java:434) ~[grpc-stub-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.etcd.jetcd.ClientConnectionManager$AuthTokenInterceptor$1$1.onClose(ClientConnectionManager.java:302) ~[jetcd-core-0.3.0.jar!/:na]
    simple-watch_1  |       at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.CensusStatsModule$StatsClientInterceptor$1$1.onClose(CensusStatsModule.java:694) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.CensusTracingModule$TracingClientInterceptor$1$1.onClose(CensusTracingModule.java:397) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:459) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.ClientCallImpl.access$300(ClientCallImpl.java:63) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl.close(ClientCallImpl.java:546) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl.access$600(ClientCallImpl.java:467) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:584) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:123) ~[grpc-core-1.17.1.jar!/:1.17.1]
    simple-watch_1  |       at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_191]
    simple-watch_1  |       at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_191]
    simple-watch_1  |       at java.lang.Thread.run(Thread.java:748) [na:1.8.0_191]
    ```
At this point, the simple-watch app will not receive any updates from etcd, similar to what was discussed in the issue [#524](https://github.com/etcd-io/jetcd/issues/524). To check this:
1. Restart the etcd cluster with `docker-compose start etcd0 etcd1 etcd2`
2. Add a new key-value pair in etcd running:

    ```bash
    docker-compose exec etcd0 sh
    / # etcdctl put key value1

### Step 2 - shutdown
To shutdown the etcd cluster and the simple-watch app run `docker-compose down`
