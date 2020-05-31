package dev.dewy.dqs.taribone.task.api;

import dev.dewy.dqs.DQS;

public interface Task
{
    TaskExecutor toExecutor(DQS dqs);
}
