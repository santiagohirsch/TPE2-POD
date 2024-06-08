package ar.edu.itba.pod.tpe2.models;

import java.io.Serializable;

public record Pair<A, B>(A left, B right) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!left.equals(pair.left)) return false;
        return right.equals(pair.right);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pair: (");
        sb.append(left);
        sb.append(", ").append(right);
        sb.append(')');
        return sb.toString();
    }
}
