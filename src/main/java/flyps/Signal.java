package flyps;

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
}
