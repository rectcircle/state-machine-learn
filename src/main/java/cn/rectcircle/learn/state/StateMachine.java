
package cn.rectcircle.learn.state;

/**
 * FSM 状态机接口
 * 
 * @param <S> 状态枚举类型
 * @param <E> 事件类型
 * @author
 */
public interface StateMachine<S extends Enum<S>, E> {

    /**
     * 获取状态机的当前状态
     * @return
     */
    S getCurrentState();

    /**
     * 做一次状态转换
     * @param event
     * @return
     */
    S doTransition(E event);
}
