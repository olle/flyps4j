package flyps;

/**
 * Utility collection of tuple types of different dimensions.
 */
public final class Tuples {

    private Tuples() {

        // Hidden.
    }

    public static class Single<A> {

        private final A first;

        protected Single(A first) {

            this.first = first;
        }

        public A first() {

            return first;
        }


        public static <S1> Single<S1> of(S1 first) {

            return new Single<>(first);
        }
    }

    public static class Double<A, B> extends Single<A> {

        private final B second;

        protected Double(A first, B second) {

            super(first);
            this.second = second;
        }

        public B second() {

            return second;
        }


        public static <S1, S2> Double<S1, S2> of(S1 first, S2 second) {

            return new Double<>(first, second);
        }
    }

    public static class Triple<A, B, C> extends Double<A, B> {

        private final C third;

        private Triple(A first, B second, C third) {

            super(first, second);
            this.third = third;
        }

        public C third() {

            return third;
        }


        public static <S1, S2, S3> Triple<S1, S2, S3> of(S1 first, S2 second, S3 third) {

            return new Triple<>(first, second, third);
        }
    }
}
