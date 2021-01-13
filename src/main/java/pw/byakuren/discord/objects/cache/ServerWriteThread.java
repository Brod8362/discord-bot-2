package pw.byakuren.discord.objects.cache;

public class ServerWriteThread {

    private static final long WAIT_TIME = 180000;

    private CacheObject[] objects;
    private Thread t;

    private boolean run;

    public ServerWriteThread(long id, CacheObject[] objects) {
        this.objects = objects;
        t = new Thread(getRunnable(), String.format("ServerWriteThread[%d]", id));
        start();
    }

    boolean isRunning() {
        return t.isAlive();
    }

    void start() {
        run = true;
        System.out.printf("Starting write thread %s\n", t.getName());
        t.start();
    }

    void stop(long start) {
        System.out.printf("Stopping write thread %s...\n", t.getName());
        run = false;
        t.interrupt();
        System.out.printf("Write thread %s stopped. [took %dms]\n", t.getName(), System.currentTimeMillis()-start);
    }

    void disableRun() {
        run=false;
    }

    void restart() {
        stop(System.currentTimeMillis());
        start();
    }

    private int writeAll() {
        int t = 0;
        for (CacheObject o: objects) {
            t+=o.write();
        }
        return t;
    }

    void writeAllAndQuit() {
        long time = System.currentTimeMillis();
        System.out.printf("Writing data for %s...\n", t.getName());
        run=false;
        writeAll();
        stop(time);
    }

    private Runnable getRunnable() {
        return () -> {
            try {
                Thread.sleep(WAIT_TIME); //wait 3 minutes
            } catch (InterruptedException e) {
                //intended interrupt
            }
            if (!run) return;
            writeAll();
        };
    }
}
