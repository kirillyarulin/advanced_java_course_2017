package edu.technopolis.advancedjava;

public class Deadlock {
    private static final Object FIRST_LOCK = new Object();
    private static final Object SECOND_LOCK = new Object();
    private static final Object ANOTHER_LOCK = new Object();

    private static int state = 0;

    public static void main(String[] args) throws Exception {
        Thread ft = new Thread(Deadlock::first);
        Thread st = new Thread(Deadlock::second);
        ft.start();
        st.start();
        ft.join();
        st.join();
        //never going to reach this point
    }

    private static void first() {
        synchronized(FIRST_LOCK) {
            synchronized (ANOTHER_LOCK) {
                state++;
                while(state < 2) {
                    try {
                        ANOTHER_LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ANOTHER_LOCK.notifyAll();
            }
            synchronized(SECOND_LOCK) {
                //unreachable point
            }
        }
    }

    private static void second() {
        synchronized(SECOND_LOCK) {
            synchronized (ANOTHER_LOCK) {
                state++;
                while(state < 2) {
                    try {
                        ANOTHER_LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ANOTHER_LOCK.notifyAll();
            }
            synchronized(FIRST_LOCK) {
                //unreachable point
            }
        }

    }

}