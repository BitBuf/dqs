package dev.dewy.dqs.taribone.task.api;

public interface TaskExecutor
{
    String getShortName();

    TaskStatus getStatus();

    TaskStatus tick();

    void stop();
}
