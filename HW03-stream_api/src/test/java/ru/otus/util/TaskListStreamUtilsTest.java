package ru.otus.util;

import org.junit.jupiter.api.BeforeEach;
import ru.otus.model.Task;

import java.util.List;

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
}
