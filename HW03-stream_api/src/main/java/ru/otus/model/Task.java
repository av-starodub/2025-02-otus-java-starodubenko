package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@NonNull
@AllArgsConstructor
public class Task {
    private final long id;
    private @Setter String title;
    private @Setter StatusType status;
}
