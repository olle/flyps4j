package flyps;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;


/**
 * A signal is a container for state information that changes over time.
 *
 * <p>Signals can depend on other signals (inputs). By creating signals and putting them together you build a circuit
 * of signals. State changes will be propagated through the signal circuit starting from the signal where the state
 * change happened. The state change might force dependent signals to also change their state which then leads to state
 * change propagation to their dependent signals in the circuit and so on. The propagation stops as soon as there are
 * no more signals reacting to state changes.</p>
 *
 * <p>A Signal is a container used to store state information. A Signal can be made to change state by calling
 * {@link #reset(Object)} or {@link #update(UnaryOperator)}. Outputs can be connected to signals. Whenever the state of
 * a Signal changes, all connected outputs will be triggered.</p>
 *
 * @param  <T>  the type of the signal value
 */
public class Signal<T> {

    private T state;

    private Set<TriConsumer<Signal<T>, T, T>> outputs;

    private Signal(T value) {

        this.state = value;

        this.outputs = new HashSet<>();
    }

    public T value() {

        return this.state;
    }


    public static <T> Signal<T> of(T state) {

        return new Signal<>(state);
    }


    public void reset(T next) {

        var prev = this.state;
        this.state = next;

        if (!prev.equals(next)) {
            this.outputs.forEach(connection -> connection.accept(this, prev, next));
        }
    }


    public void update(UnaryOperator<T> update) {

        reset(update.apply(state));
    }


    public void update(BiFunction<T, Map<String, Object>, T> update, Map<String, Object> args) {

        reset(update.apply(state, args));
    }


    public Runnable connect(Runnable connect) {

        TriConsumer<Signal<T>, T, T> connection = (s, p, n) -> connect.run();

        this.outputs.add(connection);

        return () -> this.outputs.remove(connection);
    }


    public Runnable connect(TriConsumer<Signal<T>, T, T> triggerFn) {

        this.outputs.add(triggerFn);

        return () -> this.outputs.remove(triggerFn);
    }
}
