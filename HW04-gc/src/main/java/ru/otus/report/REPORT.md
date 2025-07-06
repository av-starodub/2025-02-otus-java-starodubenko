### Запускаем как есть

| Xms(Mb)  | Xmx(Mb)  |      Spend time      |
|:--------:|:--------:|:--------------------:|
|   256    |   256    |     OutOfMemory      |
|   2048   |   2048   |   msec:3899, sec:3   |
|   1024   |   1024   |   msec:4372, sec:4   |
|   1536   |   1536   |   msec:3935, sec:3   |
|   1264   |   1264   |   msec:4412, sec:4   |
| **1792** | **1792** | **msec:3398, sec:3** |
|   1644   |   1644   |   msec:3875, sec:3   |

##### оптимальные: -Xms1792m -Xmx1792m

___

### Оптимизируем

#### 1. В классах Summator и Data меняем c ***Integer*** на ***int*** типы полей и возвращаемых значений геттеров .

| Xms(Mb) | Xmx(Mb) |      Spend time      |
|:-------:|:-------:|:--------------------:|
|   256   |   256   |   msec:1815, sec:1   |
|   512   |   512   |   msec:1041, sec:1   |
|  1024   |  1024   |   msec:1037, sec:1   |
|   768   |   768   |   msec:1092, sec:1   |
| **384** | **384** | **msec:1041, sec:1** |
|   320   |   320   |   msec:1384, sec:1   |

##### оптимальные: Xms384m -Xmx384m

#### 2. В классе Summator избавляемся от ArrayList

| Xms(Mb) | Xmx(Mb) |         Result         |
|:-------:|:-------:|:----------------------:|
|   128   |   128   |    msec:478, sec:0     |
| **64**  | **64**  |  **msec:468, sec:0**   |
|   32    |   32    |    msec:465, sec:0     |
|   16    |   16    |    msec:467, sec:0     |
|    8    |    8    |    msec:464, sec:0     |
|    4    |    4    |    msec:467, sec:0     |
|    2    |    2    |    msec:471, sec:0     |
|    1    |    1    | Too small maximum heap |
|    1    |    2    |    msec:468, sec:0     |

##### минимально возможные значения: -Xms1m -Xmx2m

Однако с параметром -Xmx2m в логе видим, что GC делает 8 сборок.

```log
[2022-08-13T17:33:05.074+0300][0.005s][info][gc] Using G1
[2022-08-13T17:33:05.074+0300][0.005s][debug][gc] ConcGCThreads: 2 offset 16
[2022-08-13T17:33:05.074+0300][0.005s][debug][gc] ParallelGCThreads: 8
[2022-08-13T17:33:05.074+0300][0.005s][debug][gc] Initialize mark stack with 4096 chunks, maximum 524288
[2022-08-13T17:33:05.110+0300][0.041s][info ][gc] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 3M->1M(8M) 0.506ms
[2022-08-13T17:33:05.111+0300][0.042s][info ][gc] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 2M->1M(8M) 0.398ms
[2022-08-13T17:33:05.112+0300][0.042s][info ][gc] GC(2) Pause Young (Normal) (G1 Evacuation Pause) 2M->1M(8M) 0.051ms
[2022-08-13T17:33:05.147+0300][0.077s][info ][gc] GC(3) Pause Young (Normal) (G1 Evacuation Pause) 3M->1M(8M) 0.115ms
[2022-08-13T17:33:05.148+0300][0.079s][info ][gc] GC(4) Pause Young (Normal) (G1 Evacuation Pause) 3M->1M(8M) 0.251ms
[2022-08-13T17:33:05.164+0300][0.094s][info ][gc] GC(5) Pause Young (Normal) (G1 Evacuation Pause) 3M->1M(8M) 0.105ms
[2022-08-13T17:33:05.165+0300][0.095s][info ][gc] GC(6) Pause Young (Normal) (G1 Evacuation Pause) 3M->1M(8M) 0.057ms
[2022-08-13T17:33:05.166+0300][0.097s][info ][gc] GC(7) Pause Young (Normal) (G1 Evacuation Pause) 4M->1M(8M) 0.047ms
```

А с параметром -Xmx64m в логе видим, что это первое значение, когда GC не делает ни одной сборки.

```log
[2022-08-13T17:25:38.197+0300][0.004s][info][gc] Using G1
[2022-08-13T17:25:38.197+0300][0.004s][debug][gc] ConcGCThreads: 2 offset 16
[2022-08-13T17:25:38.197+0300][0.004s][debug][gc] ParallelGCThreads: 8
[2022-08-13T17:25:38.197+0300][0.004s][debug][gc] Initialize mark stack with 4096 chunks, maximum 524288
```

Следовательно значение **-Xmx64m** является оптимальным для этого приложения, поскольку дальнейшее увеличение<br>
не сокращает время работы, а уменьшение увеличивает используемые ресурсы.
___
