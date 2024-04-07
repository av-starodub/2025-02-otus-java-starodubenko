package ru.otus.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Task;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.model.StatusType.CLOSE;
import static ru.otus.model.StatusType.OPEN;
import static ru.otus.model.StatusType.PROCESS;
import static ru.otus.util.TaskListUtils.countByStatus;
import static ru.otus.util.TaskListUtils.getAllByStatus;
import static ru.otus.util.TaskListUtils.getAllSortedByStatus;
import static ru.otus.util.TaskListUtils.notExists;

@DisplayName("TaskListUtilsTest")
class TaskListUtilsTest {
    private List<Task> tasks;
    private final Task task1 = new Task(1, "one", OPEN);
    private final Task task2 = new Task(2, "two", PROCESS);
    private final Task task3 = new Task(3, "three", CLOSE);
    private final Task task4 = new Task(4, "four", OPEN);
    private final Task task5 = new Task(5, "five", PROCESS);
    private final Task task6 = new Task(6, "six", CLOSE);

    @BeforeEach
    void init() {
        tasks = List.of(task1, task2, task3, task4, task5, task6);
    }

    @Test
    @DisplayName("checkGetAllByStatus: should return a list of tasks by selected status")
    void checkGetAllByStatus() {
        var openTasks = getAllByStatus(tasks, OPEN);
        assertThat(openTasks).containsExactlyInAnyOrder(task1, task4);
    }

    @Test
    @DisplayName("checkNotExists: should return false if the task exists")
    void checkNotExists() {
        assertThat(notExists(tasks, 4)).isFalse();
        assertThat(notExists(tasks, 0)).isTrue();
    }

    @Test
    @DisplayName("checkCountByStatus: should return the number of tasks by selected status")
    void checkCountByStatus() {
        assertThat(countByStatus(tasks, OPEN)).isEqualTo(2);
    }


    @Test
    @DisplayName("checkGetAllSortedByStatus: should return a list of tasks sorted in the given order")
    void checkGetAllSortedByStatus() {
        var DefaultOrderTasks = getAllSortedByStatus(tasks);
        assertThat(DefaultOrderTasks).containsSequence(task1, task4, task2, task5, task3, task6);

        var CloseProcessOpenOrderTasks = getAllSortedByStatus(tasks, CLOSE, PROCESS, OPEN);
        assertThat(CloseProcessOpenOrderTasks).containsSequence(task3, task6, task2, task5, task1, task4);

        var ProcessOpenCloseOrderTasks = getAllSortedByStatus(tasks, PROCESS, OPEN, CLOSE);
        assertThat(ProcessOpenCloseOrderTasks).containsSequence(task2, task5, task1, task4, task3, task6);

        var opensFirstOrderTasks = getAllSortedByStatus(tasks, OPEN);
        assertThat(opensFirstOrderTasks.get(0)).isEqualTo(task1);
        assertThat(opensFirstOrderTasks.get(1)).isEqualTo(task4);
        assertThat(opensFirstOrderTasks).contains(task2, task3, task5, task6);
    }
}
