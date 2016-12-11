package de.zebrajaeger.androidpanohead2.util;

/**
 * Created by lars on 18.09.2016.
 */
public class FinalInt {
    private Integer value;

    public FinalInt(Integer value) {
        this.value = value;
    }

    public FinalInt() {
        this.value = null;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value==null?"null":Integer.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FinalInt finalInt = (FinalInt) o;

        return value.equals(finalInt.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
