package uk.ac.manchester.elegant.verification.service.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServiceThreadPoolExecutor extends ThreadPoolExecutor {

    public ServiceThreadPoolExecutor(int corePoolSize,
                                     int maximumPoolSize,
                                     long keepAliveTime,
                                     TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        assert (r instanceof VerificationTask);
        ((VerificationTask)r).setStatus(VerificationTask.Status.ONGOING);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        assert (r instanceof VerificationTask);
        ((VerificationTask)r).setStatus(VerificationTask.Status.COMPLETED);
    }

}
