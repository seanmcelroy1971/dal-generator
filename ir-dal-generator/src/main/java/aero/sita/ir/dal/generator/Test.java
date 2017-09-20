package aero.sita.ir.dal.generator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;


public class Test {

    // ===========================================
    // Public Members
    // ===========================================

    // ===========================================
    // Private Members
    // ===========================================

    // ===========================================
    // Static initialisers
    // ===========================================

    // ===========================================
    // Constructors
    // ===========================================

    public Test() {
    }

    // ===========================================
    // Public Methods
    // ===========================================
    /**
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test();
        test.run();
    }

    public void run() {

        int numRegistrationsToProcess = 5;
        Timer timer = new Timer();

        try {
            for(int regIndex = 0; regIndex < numRegistrationsToProcess; regIndex++) {
                /**
                 * Create a CountDownLatch to allow us to wait for
                 * each task to finsh before we process the next registration.
                 */
                CountDownLatch doneSignal = new CountDownLatch(1);

                /**
                 * Create the task to process the registration
                 */
                Worker tickTockTask = new Worker(doneSignal, regIndex);

                /**
                 * Schedule the task, with a delay of 1001 milliseconds
                 */
                timer.schedule(tickTockTask, 1001);

                /**
                 * Wait for task to complete to finish
                 */
                doneSignal.await();
            }
        } catch (InterruptedException e1) {
        } finally {
            timer.cancel();
        }

        System.out.println("Yeehaw");

    }

    // ===========================================
    // Protected Methods
    // ===========================================

    // ===========================================
    // Private Methods
    // ===========================================

    public class Worker extends TimerTask {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CountDownLatch doneSignal;
        int index = 0;

        public Worker(CountDownLatch doneSignal, int index) {
            this.doneSignal = doneSignal;
            this.index = index;
        }

        @Override
        public void run() {
            System.out.println(format.format(new Date()) + " Processing registration " + index);
            doneSignal.countDown();
        }

    }
}
