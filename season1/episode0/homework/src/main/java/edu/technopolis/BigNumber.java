package edu.technopolis;

import java.util.Arrays;

public class BigNumber {
    private long[] number;

    BigNumber(){
        number = new long[4];
    }
    private BigNumber(BigNumber copy, int initialSize){
        number = new long[initialSize];
        System.arraycopy(copy.number, 0, number, 0, copy.number.length);
    }

    void setValue(long value){
        number[0] = value;
        for(int i = 1; i < number.length; i++)
            number[i] = 0;
    }

    BigNumber add(BigNumber term){
        if(number.length < term.number.length){
            return term.add(this);
        }
        int size = number.length;
        if(number[size-1] > 0) {
            size++;
        }

        BigNumber result = new BigNumber(this, size);
        byte remain = 0;
        int i;
        for(i = 0; i<term.number.length; i++){
            result.number[i] += term.number[i] + remain;
            if(result.number[i] < 0){
                remain = 1;
                result.number[i] -= Long.MIN_VALUE;
            } else {
                remain = 0;
            }
        }
        if(remain != 0) {
            result.number[i + 1] += remain;
        }
        return result;
    }

    @Override
    public String toString() {
        return Converter.convertTo10(number);
    }
}
