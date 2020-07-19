package cn.rectcircle.learn.use;

/**
 * @author rectcircle
 */
public enum MyThreadEvent {
    /** 调用了 Start 方法 */
    CallStart,
    /** 获得CPU */
    ObtainCPU,
    /** 调用了 Runnable.run 方法返回 */
    RunReturn,
    /** 等待锁 */
    WaitingLock,
    /** 获得锁 */
    ObtainLock,
    /** Object.wait 或者 等待 IO */
    CallObjectWaitOrWaitIO,
    /** Object.notify 或者 IO 就绪 */
    CallObjectNotifyOrIOReady,
    /** 调用带有超时状态的方法 */
    CallWithTimeout,
    /** 带有超时状态的方法超时或者返回 */
    TimeoutOrReturn,
    /** 调用中断命令 */
    CallInterrupt,
    ;
}