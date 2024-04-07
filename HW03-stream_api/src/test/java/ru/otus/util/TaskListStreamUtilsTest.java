package ru.otus.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Task;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.model.StatusType.OPEN;
import static ru.otus.model.StatusType.PROCESS;
import static ru.otus.model.StatusType.CLOSE;

public class TaskListStreamUtilsTest {
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
        var openTasks = TaskListStreamUtils.getAllByStatus(tasks, OPEN);
        assertThat(openTasks).containsExactlyInAnyOrder(task1, task4);
    }

    @Test
    @DisplayName("checkExists: should return false if the task exists")
    void checkExists() {
        assertThat(TaskListStreamUtils.notExists(tasks, 4)).isFalse();
        assertThat(TaskListStreamUtils.notExists(tasks, 0)).isTrue();
    }

}
