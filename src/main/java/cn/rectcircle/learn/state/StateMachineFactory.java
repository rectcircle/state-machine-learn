package cn.rectcircle.learn.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * FSM 工厂
 * 
 * @param <O> O operand 操作数类型
 * @param <S> S 状态机状态枚举类型
 * @param <E> E 事件类型
 * @author
 */
public final class StateMachineFactory<O, S extends Enum<S>, E> {

    /**
     * 通过当前状态和事件类型获取用户注册的四元组
     */
    private final Map<S, Transition<O, E, S>> transitionMachineTable = new HashMap<>();

    public StateMachineFactory<O, S, E> addTransition(S preState, S postState,
                                                      Transition<O, E, S> hook) {
        transitionMachineTable.put(preState, (o, e) -> {
            S r = hook.transition(o, e);
            if (Objects.equals(r, postState)) {
                return postState;
            }
            throw new RuntimeException("Invalid event: " + e + " at " + preState);
        });
        return this;
    }

    public StateMachineFactory<O, S, E> addTransition(S preState, Set<S> postStates,
                                                      Transition<O, E, S> hook) {
        transitionMachineTable.put(preState, (o, e) -> {
            S r = hook.transition(o, e);
            if (postStates.contains(r)) {
                return r;
            }
            throw new RuntimeException("Invalid event: " + e + " at " + preState);
        });
        return this;
    }

    public StateMachine<S, E> make(O operand, S initialState) {
        return new InternalStateMachine(operand, initialState);
    }

    /**
     * 真正的状态机，一个状态机维护一个对象 operand 和该对象的状态
     * 状态转换规则使用 {@link StateMachineFactory} 中的 {@link #transitionMachineTable}
     */
    private class InternalStateMachine
            implements StateMachine<S, E> {
        private final O operand;
        private S currentState;

        InternalStateMachine(O operand, S initialState) {
            this.operand = operand;
            this.currentState = initialState;
        }

        @Override
        public synchronized S getCurrentState() {
            return currentState;
        }

        @Override
        public synchronized S doTransition(E event) {
            var transition = transitionMachineTable.get(currentState);
            if (transition != null) {
                currentState = transition.transition(operand, event);
                return currentState;
            }
            throw new RuntimeException("Invalid event: " + event + " at " + currentState);
        }
    }
}
