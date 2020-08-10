package dev.dewy.dqs.taribone.task.executor;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.taribone.task.api.TaskExecutor;
import dev.dewy.dqs.taribone.task.api.TaskStatus;

public abstract class AbstractTaskExecutor implements TaskExecutor
{
    protected final DQS dqs;
    protected final String shortName;

    protected TaskStatus lastStatus;

    public AbstractTaskExecutor(DQS dqs)
    {
        this.dqs = dqs;
        this.shortName = getClass().getSimpleName();
    }

    @Override
    public String getShortName()
    {
        return shortName;
    }

    @Override
    public TaskStatus tick()
    {
        return this.lastStatus = onTick();
    }

    @Override
    public void stop()
    {
        onStop();
    }

    @Override
    public TaskStatus getStatus()
    {
        if (lastStatus == null)
        {
            return TaskStatus.forInProgress();
        } else
        {
            return lastStatus;
        }
    }

    protected abstract TaskStatus onTick();

    protected abstract void onStop();
}
