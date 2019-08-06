package ink.o.w.o.util;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午9:59
 */
@FunctionalInterface
public interface TriFunction<U, T, S, R> {

    /**
     * Applies this function to the given arguments.
     * @param <U>
     * @param <T>
     * @param <S>
     * @return the function result
     */
    R apply(T t, U u, S s);
}
