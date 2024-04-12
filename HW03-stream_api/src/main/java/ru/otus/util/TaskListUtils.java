package ru.otus.util;

import ru.otus.model.StatusType;
import ru.otus.model.Task;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

public final class TaskListUtils {
    private TaskListUtils() {
    }

    public static List<Task> getAllByStatus(List<Task> tasks, StatusType status) {
        Objects.requireNonNull(status, "status must not be null");
        return tasks.stream()
                .filter(task -> status.equals(task.getStatus()))
                .toList();
    }

    public static boolean notExists(List<Task> tasks, long id) {
        return tasks.stream()
                .noneMatch(task -> id == task.getId());
    }

    public static long countByStatus(List<Task> tasks, StatusType status) {
        Objects.requireNonNull(status, "status must not be null");
        return tasks.stream()
                .filter(task -> status.equals(task.getStatus()))
                .count();
    }

    /**
     * @param tasks        list
     * @param sortingOrder StatusType in the order required for sorting
     *                     missing types are placed at the end of the list.
     *                     If sortingOrder is empty or NULL,
     *                     the default order will be returned: OPEN, PROCESS, CLOSE.
     * @return a list of tasks sorted in the given order
     */
    public static List<Task> getAllSortedByStatus(List<Task> tasks, StatusType... sortingOrder) {
        var statusPriorities = buildStatusPriorities(sortingOrder);
        return tasks.stream()
                .sorted(Comparator.comparingInt(
                        (Task task) -> {
                            var priorityStatus = statusPriorities.get(task.getStatus());
                            return isNull(priorityStatus) ? 1 : priorityStatus;
                        }).thenComparing(Task::getId)
                )
                .toList();
    }

    private static EnumMap<StatusType, Integer> buildStatusPriorities(StatusType[] sortingOrder) {
        var statusPriorities = new EnumMap<StatusType, Integer>(StatusType.class);
        var statusTypes = isNull(sortingOrder) || sortingOrder.length == 0
                ? StatusType.values()
                : sortingOrder;
        for (var idx = 0; idx < statusTypes.length; idx++) {
            statusPriorities.put(statusTypes[idx], idx);
        }
        return statusPriorities;
    }
}
