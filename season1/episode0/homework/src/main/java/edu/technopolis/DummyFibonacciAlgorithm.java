package edu.technopolis;

import java.math.BigDecimal;

public class DummyFibonacciAlgorithm implements FibonacciAlgorithm {
    @Override
    public static String evaluate(int index) throws Exception  {
        String st="";
        if (index<=0){
            throw new Exception ("Invalid index");
        } else {
            return fibIter(index, index / 64 + 1).toString();
        }
    }

    private static BigNumber fibIter(long n, int count){
        if (n==0){
            return new BigNumber(count, 0L);
        } else if (n==1 || n==2) {
            return new BigNumber(count, 1L);
        } else{
            BigNumber n1 = new BigNumber(count, 1L);
            BigNumber n2 = new BigNumber(count, 1L);
            for (long i=3; i<=n; i+=2){
                n1 = BigNumber.Summ(n1,n2);
                n2 = BigNumber.Summ(n1,n2);
            }
            if (n % 2==0){
                return n2;
            } else {
                return n1;
            }
        }
    }
}

class BigNumber{
    public int c;
    public long[] longs;

    public BigNumber(int count,long d){
        longs = new long[count];
        c = count;
        for (int x=0; x<count; x++){
            longs[x] = 0;
        }
        longs[count-1] = d;
    }

    public String toString(){
        BigDecimal d = new BigDecimal(0);
        d = d.add(new java.math.BigDecimal((longs[0])>>>1));
        d = d.add(new java.math.BigDecimal((longs[0])>>>1));
        d = d.add(new java.math.BigDecimal(longs[0]&1));
        for (int x=1; x<c; x++){
            d = d.multiply(new java.math.BigDecimal(1L<<32));
            d = d.multiply(new java.math.BigDecimal(1L<<32));
            d = d.add(new java.math.BigDecimal((longs[x])>>>1));
            d = d.add(new java.math.BigDecimal((longs[x])>>>1));
            d = d.add(new java.math.BigDecimal(longs[x]&1));
        }
        return d.toString();
    }

    public static boolean Bigger(long l1, long l2){
        return Long.compare(l1^0x8000000000000000L, l2^0x8000000000000000L)>0;
    }

    public static BigNumber Summ(BigNumber n1, BigNumber n2){
        BigNumber n = new BigNumber(n1.c, 0L);
        n.longs[n.c-1] = (n1.longs[n.c-1] + n2.longs[n.c-1]);
        for (int a=(n.c-1); a >0; a--){
            n.longs[a-1] = (n1.longs[a-1] + n2.longs[a-1]);
            if (Bigger(n1.longs[a], n.longs[a]) || Bigger(n2.longs[a], n.longs[a])) {
                n.longs[a - 1]++;
            }
        }
        return n;
    }
}
