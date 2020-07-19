package cn.rectcircle.learn.use;

/**
 * @author rectcircle
 */
public enum MyThreadState {
    /** 尚未启动的线程的线程状态。 */
    NEW,
    /** 可运行线程的线程状态，等待CPU调度。 */
    RUNNABLE,
    /** Java没有，为了方便演示添加 */
    RUNNING,
    /** 可运行线程的线程状态，等待CPU调度。等待锁或者IO */
    BLOCKED,
    /** 线程阻塞等待监视器锁定的线程状态。{@link Object#wait() Object.wait}. */
    WAITING,
    /** 线程阻塞等待监视器锁定的线程状态。
     * <ul>
     *   <li>{@link #sleep Thread.sleep}</li>
     *   <li>{@link Object#wait(long) Object.wait} with timeout</li>
     *   <li>{@link #join(long) Thread.join} with timeout</li>
     *   <li>{@link LockSupport#parkNanos LockSupport.parkNanos}</li>
     *   <li>{@link LockSupport#parkUntil LockSupport.parkUntil}</li>
     * </ul>
     */
    TIMED_WAITING,
    /** 终止线程的线程状态。线程正常完成执行或者出现异常。 */
    TERMINATED;
}