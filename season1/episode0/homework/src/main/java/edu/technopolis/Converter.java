package edu.technopolis;

class Converter {
    static String convertTo10(long[] binaryNum){
        long[] number = new long[binaryNum.length];
        int j = 0;
        for(int i = 0; i < binaryNum.length*64; i++){
            if(i != 0 && ((i-63)&63) == 0)  //аналог (i-63)%64 == 0
                continue;
            setBit(number, getBit(binaryNum, i), j);
            j++;
        }

        long[] temp = new long[(int)(Math.ceil(number.length*1.25f))]; //число в двоично десятичной системе

        //прибавляем 3 по всем тетрадам
        AddToAllTetrads(temp,true);  //+3

        long allThree = 0b00110011_00110011_00110011_00110011_00110011_00110011_00110011_00110011L;
        long mask = 1L << 63;
        for(int k = number.length - 1; k >= 0; k--) {
            for (int i = 0; i < 64; i++) {
                long num = number[k] & mask;
                number[k] <<= 1L;

                for(int n = 0; n < temp.length; n++) {
                    if(temp[n] == allThree && num == 0 )
                        break;
                    long firstBit = temp[n] & mask;
                    temp[n] <<= 1;
                    if (num != 0) {
                        temp[n] |= 1L; //последний бит 1
                    }
                    Correction(temp, n, firstBit);
                    num = firstBit;
                }
            }
        }
        //отнимаем 3 по всем тетрадам
        AddToAllTetrads(temp,false);  //-3

        //преобразование из 2/10 в 10
        return ConvertToString(temp);
    }

    private static long getBit(long[] number, int i){
        int arrayNumber = i/64;
        if(arrayNumber > number.length - 1)
            return 0;
        return ((number[arrayNumber] >> (i%64)) & 1L);
    }
    private static void setBit(long[] number, long value, int i){
        int arrayNumber = i/64;
        try {
            if(value == 0)
                number[arrayNumber] = number[arrayNumber] & ~(1L << (i%64));
            else
                number[arrayNumber] = number[arrayNumber] | (1L << (i%64));
        } catch (ArrayIndexOutOfBoundsException e){
            return;
        }

    }
    private static void AddToAllTetrads(long[] temp, boolean sign){
        long correction = 0b00110011_00110011_00110011_00110011_00110011_00110011_00110011_00110011L;
        for(int k = 0; k < temp.length; k++) {
            if(sign)
                temp[k] += correction;
            else
                temp[k] -= correction;
        }

    }
    private static void Correction(long[] temp, int n, long firstBit){
        long mask = 0b00010001_00010001_00010001_00010001_00010001_00010001_00010001_00010000L;
        long needForCorrection = temp[n] & mask;
        boolean isOne = true;
        for(int i = 0; i < 2; i++){
            //long correction = 0b00011001_10011001_10011001_10011001_10011001_10011001_10011001_10011001L;
            long correction = mask;
            correction = correction & needForCorrection;
            correction = correction | (correction >> 1);
            correction >>>= 3;

            if(isOne)
                temp[n] += correction;
            else
                temp[n] -= correction;
            isOne = !isOne;
            needForCorrection = ~needForCorrection;
        }

        if (firstBit != 0)
            temp[n] += 3L << (15 * 4);
        else
            temp[n] -= 3L << (15 * 4);
    }
    private static String ConvertToString(long[] temp){
        StringBuilder strNum = new StringBuilder();

        long mask = 0b00001111;
        L: for(int k = 0; k < temp.length; k++) {
            for (int i = 0; i < 16; i++) {
                long digit = temp[k] & mask;
                strNum.append(digit);
                temp[k] >>>= 4;
                if(isZero(temp))
                    break L;
            }
        }
        strNum = strNum.reverse();
        return strNum.toString();
    }
    private static boolean isZero(long[] temp){
        for(long i: temp) {
            if (i < 0)
                i = ~i;
            if (i != 0)
                return false;
        }
        return true;
    }
}

