package flyps;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Signal")
public class SignalTest {

    @Test
    @DisplayName("returns its current value")
    public void ensureReturnsCurrentValue() {

        var s = Signal.of("foo");
        assertThat(s.value()).isEqualTo("foo");
    }


    @Test
    @DisplayName("resets its value")
    void ensureResetsValue() throws Exception {

        var s = Signal.of("foo");
        s.reset("bar");
        assertThat(s.value()).isEqualTo("bar");
    }


    @Test
    @DisplayName("updates its value")
    void ensureUpdateItsValue() throws Exception {

        var s = Signal.of("foo");
        s.update(state -> state + "bar");
        assertThat(s.value()).isEqualTo("foobar");
    }


    @Test
    @DisplayName("triggers connected outputs for new values")
    void ensureTriggersConnectedOutletsForNewValues() throws Exception {

        var updates = new AtomicInteger(0);
        var s = Signal.of("foo");
        s.connect(() -> updates.incrementAndGet());
        s.reset("bar");

        assertThat(updates.get()).isEqualTo(1);
    }


    @Test
    @DisplayName("ignores outputs for equal values")
    void ensureIgnoresOutputForEqualValues() throws Exception {

        var updates = new AtomicInteger(0);
        var s = Signal.of("foo");
        s.connect(() -> updates.incrementAndGet());
        s.reset("foo");

        assertThat(updates.get()).isEqualTo(0);
    }


    @Test
    @DisplayName("passes information when triggering connected outputs")
    void ensurePassesInformationWhenTriggeringConnectedOutputs() throws Exception {

        List<Tuples.Triple<Signal<String>, String, String>> updates = new ArrayList<>();

        TriConsumer<Signal<String>, String, String> triggerFn = (signal, prev, next) ->
                updates.add(Tuples.Triple.<Signal<String>, String, String>of(signal, prev, next));

        var s = Signal.of("foo");
        s.connect(triggerFn);
        s.reset("bar");

        assertThat(updates).hasSize(1);

        assertThat(updates.get(0).first()).isEqualTo(s);
        assertThat(updates.get(0).second()).isEqualTo("foo");
        assertThat(updates.get(0).third()).isEqualTo("bar");
    }


    @Test
    @DisplayName("disconnects a connected output")
    void ensureDisconnectsConnectedOutput() throws Exception {

        var r1 = new AtomicInteger(0);
        var r2 = new AtomicInteger(0);
        var s = Signal.of("foo");
        s.connect(() -> r1.incrementAndGet());

        Runnable disconnector = s.connect(() -> r2.incrementAndGet());
        s.reset("bar");
        disconnector.run();
        s.reset("baz");

        assertThat(r1.get()).isEqualTo(2);
        assertThat(r2.get()).isEqualTo(1);
    }
}
