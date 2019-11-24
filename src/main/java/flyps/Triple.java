package flyps;

public class Triple<A, B, C> {

    private final A first;
    private final B second;
    private final C third;

    private Triple(A s1, B s2, C s3) {

        this.first = s1;
        this.second = s2;
        this.third = s3;
    }

    public A first() {

        return first;
    }


    public B second() {

        return second;
    }


    public C third() {

        return third;
    }


    static <S1, S2, S3> Triple<S1, S2, S3> of(S1 s1, S2 s2, S3 s3) {

        return new Triple<>(s1, s2, s3);
    }
}
