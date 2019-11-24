package flyps;

import java.util.function.UnaryOperator;


public class Signal {

    private String value;

    private Signal(String value) {

        this.value = value;
    }

    public static Signal of(String value) {

        return new Signal(value);
    }


    public String value() {

        return this.value;
    }


    public void reset(String value) {

        this.value = value;
    }


    public void update(UnaryOperator<String> update) {

        this.value = update.apply(value);
    }
}
