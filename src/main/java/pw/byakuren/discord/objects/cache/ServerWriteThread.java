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

    void stop() {
        System.out.printf("Stopping write thread %s...\n", t.getName());
        run = false;
        t.interrupt();
        System.out.printf("Write thread %s stopped.\n", t.getName());
    }

    void restart() {
        stop();
        start();
    }

    private int writeAll() {
        int t = 0;
        for (CacheObject o: objects) {
            t+=o.write();
        }
        return t;
    }

    public void writeAllAndQuit() {
        for (CacheObject o: objects) {
            o.write();
        }
        stop();
    }

    private Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(WAIT_TIME); //wait 3 minutes
                } catch (InterruptedException e) {
                    //intended interrupt
                }
                if (!run) return;
                writeAll();
            }
        };
    }
}
