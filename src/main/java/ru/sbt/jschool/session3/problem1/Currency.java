package ru.sbt.jschool.session3.problem1;

/**
 */
public enum Currency {
    RUR {
        @Override public float to(float amount, Currency from) {
            switch (from) {
                case EUR:
                    return amount * RUR_TO_EUR;
                case USD:
                    return amount * RUR_TO_USD;
            }

            throw new RuntimeException("Unknown currency " + from);
        }
    },
    USD {
        @Override public float to(float amount, Currency from) {
            switch (from) {
                case RUR:
                    return amount / RUR_TO_USD;
                case EUR:
                    return (amount / RUR_TO_EUR) * RUR_TO_USD;
            }

            throw new RuntimeException("Unknown currency " + from);
        }
    },
    EUR {
        @Override public float to(float amount, Currency from) {
            switch (from) {
                case RUR:
                    return amount / RUR_TO_EUR;
                case USD:
                    return (amount * RUR_TO_USD) / RUR_TO_EUR;
            }

            throw new RuntimeException("Unknown currency " + from);
        }
    };

    public static final int RUR_TO_USD = 57;

    public static final int RUR_TO_EUR = 70;

    public abstract float to(float amount, Currency from);
}
