package ru.otus.util;

import ru.otus.model.StatusType;
import ru.otus.model.Task;

import java.util.List;

public final class TaskListStreamUtils {
    private TaskListStreamUtils() {
    }

    public static List<Task> getAllByStatus(List<Task> tasks, StatusType status) {
        return tasks.stream()
                .filter(task -> status.equals(task.getStatus()))
                .toList();
    }

    public static boolean notExists(List<Task> tasks, long id) {
        return tasks.stream()
                .noneMatch(task -> id == task.getId());
    }
}
