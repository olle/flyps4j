package flyps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;


public class Signal {

    private String value;
    private Collection<TriConsumer<Signal, String, String>> connections;

    private Signal(String value) {

        this.value = value;
        this.connections = new ArrayList<>();
    }

    public static Signal of(String value) {

        return new Signal(value);
    }


    public String value() {

        return this.value;
    }


    public void reset(String value) {

        if (this.value.equals(value)) {
            return;
        }

        var prev = this.value;
        this.value = value;

        this.connections.forEach(connection -> connection.accept(this, prev, this.value));
    }


    public void update(UnaryOperator<String> update) {

        reset(update.apply(value));
    }


    public void connect(Runnable connect) {

        this.connections.add((s, n, p) -> connect.run());
    }


    public void connect(TriConsumer<Signal, String, String> triggerFn) {

        this.connections.add(triggerFn);
    }
}
