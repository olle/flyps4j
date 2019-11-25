package flyps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;


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
