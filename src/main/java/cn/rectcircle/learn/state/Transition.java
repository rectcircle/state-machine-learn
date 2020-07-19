package cn.rectcircle.learn.state;

/**
 * Hook for Transition. Post state is decided by Transition hook. Post state
 * must be one of the valid post states registered in StateMachine.
 * 状态转换的转换器
 * 
 * @author
 */
@FunctionalInterface
public interface Transition<O, E, S extends Enum<S>> {
    /**
     * Transition hook.
     * @param operand the entity attached to the FSM, whose internal
     *                state may change.
     * @param event   causal event
     * @return the postState. Post state must be one of the
     *             valid post states registered in StateMachine.
     */
    S transition(O operand, E event);
}
