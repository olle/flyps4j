package flyps;

import flyps.Tuples.Double;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


/**
 * A SignalFn is a signal that computes its state by running `fn`. It keeps track of and connects to all referenced
 * input signals during the function call. If the state of any of the connected input signals changes, the state of
 * SignalFn gets re-computed (which means re-running `fn`). The state held by the SignalFn is the return value of `fn`
 * and can be preset using `state`. Like with signals, outputs can be connected. Whenever the state of a SignalFn
 * changes, all connected outputs will be triggered.
 *
 * @param  <T>
 */
public class SignalFn<T> {

    private boolean dirty = true;

    private Supplier<T> fn;

    private Context context;

    private SignalFn(Supplier<T> fn) {

        this.fn = fn;
    }

    public static <T> SignalFn<T> of(Supplier<T> fn) {

        return new SignalFn<>(fn);
    }


    public T value() {

        return fn.get();
    }


    protected void run() {

        Double<Context, T> res = trackInputs(fn);
        this.dirty = false;

        var trackedInputs = Optional.ofNullable(res.first().inputs).orElse(new ArrayList<>());
    }


    private synchronized Tuples.Double<Context, T> trackInputs(Supplier<T> fn) {

        var previous = this.context;

        this.context = new Context();

        var res = Tuples.Double.of(this.context, fn.get());

        this.context = previous;

        return res;
    }


    public Collection<?> inputs() {

        if (this.dirty) {
            this.run();
        }

        return null;
    }

    static class Context {

        public List inputs;
    }
}
