package flyps;

import java.util.ArrayList;
import java.util.Collection;
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

    private T value;
    private Collection<TriConsumer<Signal<T>, T, T>> connections;

    private Signal(T value) {

        this.value = value;
        this.connections = new ArrayList<>();
    }

    public T value() {

        return this.value;
    }


    public static <T> Signal<T> of(T value) {

        return new Signal<>(value);
    }


    public void reset(T value) {

        if (this.value.equals(value)) {
            return;
        }

        var prev = this.value;
        this.value = value;
        this.connections.forEach(connection -> connection.accept(this, prev, this.value));
    }


    public void update(UnaryOperator<T> update) {

        reset(update.apply(value));
    }


    public Runnable connect(Runnable connect) {

        TriConsumer<Signal<T>, T, T> connection = (s, n, p) -> connect.run();
        this.connections.add(connection);

        return () -> this.connections.remove(connection);
    }


    public Runnable connect(TriConsumer<Signal<T>, T, T> triggerFn) {

        this.connections.add(triggerFn);

        return () -> this.connections.remove(triggerFn);
    }
}
