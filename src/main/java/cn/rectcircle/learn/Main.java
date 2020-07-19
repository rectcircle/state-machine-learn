package cn.rectcircle.learn;

import java.util.Set;

import cn.rectcircle.learn.state.StateMachine;
import cn.rectcircle.learn.state.StateMachineFactory;
import cn.rectcircle.learn.use.MyThreadContext;
import cn.rectcircle.learn.use.MyThreadEvent;
import cn.rectcircle.learn.use.MyThreadState;

/**
 * @author rectcircle
 */
public class Main {

    @SuppressWarnings("PMD.MethodTooLongRule")
    public static void main(String[] args) {
        // 模拟线程状态转换
        // https://juejin.im/post/5d8313b6518825313a7bba1e

        var stateMachineFactory = new StateMachineFactory<MyThreadContext, MyThreadState, MyThreadEvent>();
        // NEW -> RUNNABLE
        stateMachineFactory.addTransition(MyThreadState.NEW, MyThreadState.RUNNABLE, (o, e) -> {
            if (e == MyThreadEvent.CallStart) {
                System.out.println("NEW -> RUNNABLE by event: " + e);
                return MyThreadState.RUNNABLE;
            }
            return MyThreadState.TERMINATED;
        });
        // RUNNABLE -> RUNNING
        stateMachineFactory.addTransition(MyThreadState.RUNNABLE, MyThreadState.RUNNING, (o, e) -> {
            if (e == MyThreadEvent.ObtainCPU) {
                System.out.println("RUNNABLE -> RUNNING by event: " + e);
                return MyThreadState.RUNNING;
            }
            return MyThreadState.TERMINATED;
        });
        // RUNNING -> BLOCKED | WAITING | TIMED_WAITING | TERMINATED
        stateMachineFactory.addTransition(MyThreadState.RUNNING, Set.of(MyThreadState.BLOCKED, MyThreadState.WAITING,
                MyThreadState.TIMED_WAITING, MyThreadState.TERMINATED), (o, e) -> {
                    MyThreadState postState = null;
                    if (e == MyThreadEvent.WaitingLock) {
                        postState = MyThreadState.BLOCKED;
                    } else if (e == MyThreadEvent.CallObjectWaitOrWaitIO) {
                        postState = MyThreadState.WAITING;
                    } else if (e == MyThreadEvent.CallWithTimeout) {
                        postState = MyThreadState.TIMED_WAITING;
                    } else if (e == MyThreadEvent.RunReturn) {
                        postState = MyThreadState.TERMINATED;
                    }
                    if (postState != null) {
                        System.out.println("RUNNING -> " + postState + " by event: " + e);
                        return postState;
                    }
                    throw new RuntimeException("RUNNING not supported event: " + e);
                });
        // BLOCKED -> RUNNABLE | TERMINATED
        stateMachineFactory.addTransition(MyThreadState.BLOCKED,
                Set.of(MyThreadState.RUNNABLE, MyThreadState.TERMINATED), (o, e) -> {
                    MyThreadState postState = null;
                    if (e == MyThreadEvent.ObtainLock) {
                        postState = MyThreadState.RUNNABLE;
                    } else if (e == MyThreadEvent.CallInterrupt) {
                        postState = MyThreadState.TERMINATED;
                    }
                    if (postState != null) {
                        System.out.println("BLOCKED -> " + postState + " by event: " + e);
                        return postState;
                    }
                    throw new RuntimeException("BLOCKED not supported event: " + e);
                });
        // WAITING -> RUNNABLE | TERMINATED
        stateMachineFactory.addTransition(MyThreadState.WAITING,
                Set.of(MyThreadState.RUNNABLE, MyThreadState.TERMINATED), (o, e) -> {
                    MyThreadState postState = null;
                    if (e == MyThreadEvent.CallObjectNotifyOrIOReady) {
                        postState = MyThreadState.RUNNABLE;
                    } else if (e == MyThreadEvent.CallInterrupt) {
                        postState = MyThreadState.TERMINATED;
                    }
                    if (postState != null) {
                        System.out.println("WAITING -> " + postState + " by event: " + e);
                        return postState;
                    }
                    throw new RuntimeException("WAITING not supported event: " + e);
                });
        // TIMED_WAITING -> RUNNABLE | TERMINATED
        stateMachineFactory.addTransition(MyThreadState.TIMED_WAITING,
                Set.of(MyThreadState.RUNNABLE, MyThreadState.TERMINATED), (o, e) -> {
                    MyThreadState postState = null;
                    if (e == MyThreadEvent.TimeoutOrReturn) {
                        postState = MyThreadState.RUNNABLE;
                    } else if (e == MyThreadEvent.CallInterrupt) {
                        postState = MyThreadState.TERMINATED;
                    }
                    if (postState != null) {
                        System.out.println("TIMED_WAITING -> " + postState + " by event: " + e);
                        return postState;
                    }
                    throw new RuntimeException("TIMED_WAITING not supported event: " + e);
                });

        StateMachine<MyThreadState, MyThreadEvent> sm = stateMachineFactory.make(new MyThreadContext(), MyThreadState.NEW);

        sm.doTransition(MyThreadEvent.CallStart);
        sm.doTransition(MyThreadEvent.ObtainCPU);
        sm.doTransition(MyThreadEvent.WaitingLock);
        sm.doTransition(MyThreadEvent.ObtainLock);
        sm.doTransition(MyThreadEvent.ObtainCPU);
        sm.doTransition(MyThreadEvent.CallObjectWaitOrWaitIO);
        sm.doTransition(MyThreadEvent.CallObjectNotifyOrIOReady);
        sm.doTransition(MyThreadEvent.ObtainCPU);
        sm.doTransition(MyThreadEvent.CallWithTimeout);
        sm.doTransition(MyThreadEvent.TimeoutOrReturn);
        sm.doTransition(MyThreadEvent.ObtainCPU);
        sm.doTransition(MyThreadEvent.RunReturn);

    }
}