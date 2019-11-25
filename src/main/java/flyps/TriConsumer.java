package flyps;

import java.util.function.Consumer;


/**
 * Represents an operation that accepts three input arguments and returns no result. This is the triple-arity
 * specialization of {@link Consumer}. Unlike most other functional interfaces, {@code TriConsumer} is expected to
 * operate via side-effects.
 *
 * <p>This is a functional interface whose functional method is {@link #accept(Object, Object, Object)}.</p>
 *
 * @param  <S1>  the type of the first argument to the operation
 * @param  <S2>  the type of the second argument to the operation
 * @param  <S3>  the type of the third argument to the operation
 *
 * @author  Olle Törnström - toernstroem@synyx.de
 */
@FunctionalInterface
public interface TriConsumer<S1, S2, S3> {

    void accept(S1 s1, S2 s2, S3 s3);
}
