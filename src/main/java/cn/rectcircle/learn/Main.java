package cn.rectcircle.learn;

import cn.rectcircle.learn.state.StateMachine;
import cn.rectcircle.learn.state.StateMachineFactory;
import cn.rectcircle.learn.use.MyThreadContext;
import cn.rectcircle.learn.use.MyThreadEvent;
import cn.rectcircle.learn.use.MyThreadEventType;
import cn.rectcircle.learn.use.MyThreadState;

/**
 * @author rectcircle
 */
public class Main {

    @SuppressWarnings("PMD.MethodTooLongRule")
    public static void main(String[] args) {
        // 模拟线程状态转换
        // https://juejin.im/post/5d8313b6518825313a7bba1e

        var stateMachineFactory = new StateMachineFactory<MyThreadContext, MyThreadState, MyThreadEventType, MyThreadEvent>();
        // NEW -> RUNNABLE
        stateMachineFactory.addTransition(MyThreadState.NEW, MyThreadState.RUNNABLE, MyThreadEventType.CallStart, (o, e) -> {
            System.out.println("NEW -> RUNNABLE by event: " + e);
            return MyThreadState.RUNNABLE;
        });
        // RUNNABLE -> RUNNING
        stateMachineFactory.addTransition(MyThreadState.RUNNABLE, MyThreadState.RUNNING, MyThreadEventType.ObtainCPU, (o, e) -> {
            System.out.println("RUNNABLE -> RUNNING by event: " + e);
            return MyThreadState.RUNNING;
        });
        // RUNNING -> BLOCKED | WAITING | TIMED_WAITING | TERMINATED
        stateMachineFactory.addTransition(MyThreadState.RUNNING, MyThreadState.BLOCKED, MyThreadEventType.WaitingLock, (o, e) -> {
            System.out.println("RUNNING -> BLOCKED by event: " + e);
            return MyThreadState.BLOCKED;
        });
        stateMachineFactory.addTransition(MyThreadState.RUNNING, MyThreadState.WAITING, MyThreadEventType.CallObjectWaitOrWaitIO, (o, e) -> {
            System.out.println("RUNNING -> WAITING by event: " + e);
            return MyThreadState.WAITING;
        });
        stateMachineFactory.addTransition(MyThreadState.RUNNING, MyThreadState.TIMED_WAITING, MyThreadEventType.CallWithTimeout, (o, e) -> {
            System.out.println("RUNNING -> TIMED_WAITING by event: " + e);
            return MyThreadState.TIMED_WAITING;
        });
        stateMachineFactory.addTransition(MyThreadState.RUNNING, MyThreadState.TERMINATED, MyThreadEventType.RunReturn, (o, e) -> {
            System.out.println("RUNNING -> TERMINATED by event: " + e);
            return MyThreadState.TERMINATED;
        });
        // BLOCKED -> RUNNABLE | TERMINATED
        stateMachineFactory.addTransition(MyThreadState.BLOCKED, MyThreadState.RUNNABLE, MyThreadEventType.ObtainLock, (o, e) -> {
            System.out.println("BLOCKED -> RUNNABLE by event: " + e);
            return MyThreadState.RUNNABLE;
        });
        stateMachineFactory.addTransition(MyThreadState.BLOCKED, MyThreadState.TERMINATED, MyThreadEventType.CallInterrupt, (o, e) -> {
            System.out.println("BLOCKED -> TERMINATED by event: " + e);
            return MyThreadState.TERMINATED;
        });
        // WAITING -> RUNNABLE | TERMINATED
        stateMachineFactory.addTransition(MyThreadState.WAITING, MyThreadState.RUNNABLE, MyThreadEventType.CallObjectNotifyOrIOReady, (o, e) -> {
            System.out.println("WAITING -> RUNNABLE by event: " + e);
            return MyThreadState.RUNNABLE;
        });
        stateMachineFactory.addTransition(MyThreadState.WAITING, MyThreadState.TERMINATED, MyThreadEventType.CallInterrupt, (o, e) -> {
            System.out.println("WAITING -> TERMINATED by event: " + e);
            return MyThreadState.TERMINATED;
        });
        // TIMED_WAITING -> RUNNABLE | TERMINATED
        stateMachineFactory.addTransition(MyThreadState.TIMED_WAITING, MyThreadState.RUNNABLE, MyThreadEventType.TimeoutOrReturn, (o, e) -> {
            System.out.println("TIMED_WAITING -> RUNNABLE by event: " + e);
            return MyThreadState.RUNNABLE;
        });
        stateMachineFactory.addTransition(MyThreadState.TIMED_WAITING, MyThreadState.TERMINATED, MyThreadEventType.CallInterrupt, (o, e) -> {
            System.out.println("TIMED_WAITING -> TERMINATED by event: " + e);
            return MyThreadState.TERMINATED;
        });

        StateMachine<MyThreadState, MyThreadEventType, MyThreadEvent> sm = stateMachineFactory.make(new MyThreadContext(), MyThreadState.NEW);

        sm.doTransition(MyThreadEventType.CallStart, new MyThreadEvent(MyThreadEventType.CallStart));
        sm.doTransition(MyThreadEventType.ObtainCPU, new MyThreadEvent(MyThreadEventType.ObtainCPU));
        sm.doTransition(MyThreadEventType.WaitingLock, new MyThreadEvent(MyThreadEventType.WaitingLock));
        sm.doTransition(MyThreadEventType.ObtainLock, new MyThreadEvent(MyThreadEventType.ObtainLock));
        sm.doTransition(MyThreadEventType.ObtainCPU, new MyThreadEvent(MyThreadEventType.ObtainCPU));
        sm.doTransition(MyThreadEventType.CallObjectWaitOrWaitIO, new MyThreadEvent(MyThreadEventType.CallObjectWaitOrWaitIO));
        sm.doTransition(MyThreadEventType.CallObjectNotifyOrIOReady, new MyThreadEvent(MyThreadEventType.CallObjectNotifyOrIOReady));
        sm.doTransition(MyThreadEventType.ObtainCPU, new MyThreadEvent(MyThreadEventType.ObtainCPU));
        sm.doTransition(MyThreadEventType.CallWithTimeout, new MyThreadEvent(MyThreadEventType.CallWithTimeout));
        sm.doTransition(MyThreadEventType.TimeoutOrReturn, new MyThreadEvent(MyThreadEventType.TimeoutOrReturn));
        sm.doTransition(MyThreadEventType.ObtainCPU, new MyThreadEvent(MyThreadEventType.ObtainCPU));
        sm.doTransition(MyThreadEventType.RunReturn, new MyThreadEvent(MyThreadEventType.RunReturn));

    }
}