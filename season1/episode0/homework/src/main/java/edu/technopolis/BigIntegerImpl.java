package edu.technopolis;

import java.util.Arrays;

/**
 * Created by Nusrat on 10/4/2017.
 */
//Поразрядная реализация
public class BigIntegerImpl extends BigInteger {
    private long[] number = new long[2];
    private int rank = 0;

    BigIntegerImpl(long init) {
        number[number.length - 1] = init;
        rank = 1;
    }

    BigIntegerImpl(BigIntegerImpl init) {
        number = Arrays.copyOf(init.getNumberMassive(), init.size());
        rank = init.getRank();
    }


    public void add(BigIntegerImpl addition) throws Exception {
        if (addition.getNumberMassive().length > this.number.length) {
            this.number = grewd(this.number, addition.getNumberMassive().length);
        } else if (addition.getNumberMassive().length < this.number.length) {
            addition.setNumberMassive(grewd(addition.getNumberMassive(), number.length));
        }
        for (int i = number.length - 1; i >= 0; i--) {
            String second = String.valueOf(addition.getNumberMassive()[i]);
            char[] secondChars = second.toCharArray();
            for (int j = secondChars.length - 1; j >= 0; j--) {
                int secondDig = Character.getNumericValue(secondChars[j]);
                addDigit(secondDig, secondChars.length - 1 - j, i);
            }
        }
    }


    public void substract(BigIntegerImpl addition) throws Exception {
        if (addition.getNumberMassive().length > this.number.length) {
            this.number = grewd(this.number, addition.getNumberMassive().length);
        } else if (addition.getNumberMassive().length < this.number.length) {
            addition.setNumberMassive(grewd(addition.getNumberMassive(), number.length));
        }
        for (int i = number.length - 1; i >= 0; i--) {
            String second = String.valueOf(addition.getNumberMassive()[i]);
            char[] secondChars = second.toCharArray();
            for (int j = secondChars.length - 1; j >= 0; j--) {
                int secondDig = Character.getNumericValue(secondChars[j]);
                subDigit(secondDig, secondChars.length - 1 - j, i);
            }
        }
    }

    private void subDigit(int modulo, int pos, int rank) throws Exception {
        if (pos > 16) {
            throw new Exception("wrong digit pos");
        }

        int curDigit;
        String num = Long.toString(number[rank]);
        if (num.length() - 1 < pos) {
            curDigit = 0;
        } else {
            curDigit = Character.getNumericValue(num.charAt(num.length() - 1 - pos));
        }

        if (curDigit - modulo >= 0) {
            number[rank] -= (long) (modulo * Math.pow(10, pos));
        } else {//915-20   895
            if (number[rank] >= (long) (modulo * Math.pow(10, pos))) {
                number[rank] -= (long) (modulo * Math.pow(10, pos));
            } else {
                number[rank] += (long) (10 * Math.pow(10, pos) - (modulo * Math.pow(10, pos)));

                if (pos == 16) {
                    rank--;
                    if (this.rank < number.length - rank) {
                        this.rank = number.length - rank;
                    }
                    addDigit(1, 0, rank);
                } else {
                    addDigit(1, pos + 1, rank);
                }
            }
        }
    }


    public void addDigit(int modulo, int pos, int rank) throws Exception {

        if (pos > 16) {
            throw new Exception("wrong digit pos");
        }

        int curDigit;
        String num = Long.toString(number[rank]);
        if (num.length() - 1 < pos) {
            curDigit = 0;
        } else {
            curDigit = Character.getNumericValue(num.charAt(num.length() - 1 - pos));
        }

        if (curDigit + modulo < 10) {
            number[rank] += (long) (modulo * Math.pow(10, pos));
        } else {
            number[rank] += (long) (((curDigit + modulo) % 10) * Math.pow(10, pos) - curDigit * Math.pow(10, pos));

            if (pos == 16) {
                if (rank == 0) {
                    this.number = grewd(this.number, this.getNumberMassive().length * 2);
                    rank = number.length / 2;
                }
                rank--;
                if (this.rank < number.length - rank) {
                    this.rank = number.length - rank;
                }
                addDigit(1, 0, rank);

            } else {
                addDigit(1, pos + 1, rank);
            }
        }
    }

    private long[] grewd(long[] number, int newSize) {
        long[] prevNumber = number;
        number = new long[newSize];
        System.arraycopy(prevNumber, 0, number, prevNumber.length, number.length - prevNumber.length);
        return number;
    }


    public void multiply(BigIntegerImpl mult) throws Exception {
        BigIntegerImpl multTo = new BigIntegerImpl(mult);
        while (!multTo.toString().equals("0")) {
            multTo.add(new BigIntegerImpl(-1));

        }
    }

    @Override
    public int size() {
        return number.length;
    }

    @Override
    public long[] getNumberMassive() {
        return number;
    }

    @Override
    public void setNumberMassive(long[] numberMassive) {
        this.number = numberMassive;
    }

    @Override
    public int getRank() {
        return rank;
    }

}
