package edu.technopolis;

/**
 * Created by Nusrat on 10/19/2017.
 */


public abstract class BigInteger {

    abstract int size();

    abstract long[] getNumberMassive();

    abstract void setNumberMassive(long[] numberMassive);

    abstract int getRank();

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("");
        out.append(String.valueOf(getNumberMassive()[getNumberMassive().length - getRank()]));
        for (int i = getNumberMassive().length - getRank() + 1; i < getNumberMassive().length; i++) {
            int size = (int) Math.ceil(Math.log10(getNumberMassive()[i]));
            for (int j = 0; j < 17 - size; j++) {
                out.append("0");
            }
            out.append(Long.valueOf(getNumberMassive()[i]));
        }
        return out.toString();
    }
}
