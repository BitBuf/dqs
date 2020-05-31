package dev.dewy.dqs.taribone.ticker;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.taribone.task.api.TaskStatus;
import dev.dewy.dqs.utils.Constants;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A runnable ticker for executing bot tasks for bots. The BotTicker
 * automatically executes the bot's current task.
 */
public class TariboneTicker implements Runnable
{
    private final DQS dqs;
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Creates a new BotTicker.
     *
     * @param dqs the bot.
     */
    public TariboneTicker(DQS dqs)
    {
        this.dqs = dqs;
    }

    /**
     * Starts the BotTicker. The ticker starts in a new thread, which can be
     * stopped with {@link #stop()}.
     */
    public void start()
    {
        if (!running.compareAndSet(false, true))
        {
            throw new IllegalStateException("Already ticking");
        }

        Thread t = new Thread(this);

        t.setName("Taribone Ticker");
        t.start();
    }

    /**
     * Stops the BotTicker.
     */
    public void stop()
    {
        running.set(false);
    }

    @Override
    public void run()
    {
        TariboneScheduler scheduler = new TariboneScheduler(50); // 50ms per tick
        scheduler.start();

        while (running.get())
        {
            if (dqs.getTaskExecutor() != null
                    && dqs.getTaskExecutor().getStatus().getType() == TaskStatus.StatusType.IN_PROGRESS)
            {
                TaskStatus status = dqs.getTaskExecutor().tick();
                if (status.getType() == TaskStatus.StatusType.FAILURE)
                {

                    if (status.getThrowable() != null)
                    {
                        Constants.TARIBONE_LOG.warn("Task Failure: " + status.getMessage(), status.getThrowable());
                    } else
                    {
                        Constants.TARIBONE_LOG.warn("Task Failure: " + status.getMessage());
                    }

                    dqs.setTaskExecutor(null);
                }
            }
            scheduler.sleepTick();
        }
    }

}
