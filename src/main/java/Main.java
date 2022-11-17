import java.util.Arrays;
import java.util.Random;

public class Main {
    private static final int[] arraySizes = {10_000, 100_000, 1000_000, 10_000_000, 100_000_000};

    public static void main(String[] args) throws InterruptedException {
        // Оценка работы алгоритма в зависимости от размера массива и количества потоков
        for (int arrSize : arraySizes) {
            long startTime;
            long elapsedTime;

            System.out.println("\nArrays size: " + arrSize);
            int[] testingArray = createArray(arrSize); // Генерация исходного массива со случайными значениями

            for (int threadPoolSize = 2; threadPoolSize <= 16; threadPoolSize *= 2) {

                startTime = System.currentTimeMillis(); // Начало отсчета времени

                // Размеры подмассивов определяются в зависимости от размера исходного массива и количества потоков
                int subSize = arrSize / threadPoolSize;
                int[][] subArrays = new int[threadPoolSize - 1][subSize];
                int[] lastSubArray = new int[arrSize - (threadPoolSize - 1) * subSize];
                QuickSort[] threads = new QuickSort[threadPoolSize]; // Выделение памяти под массив потоков

                // Части исходного массива копируются в созданные подмассивы
                for (int subArrIndex = 0; subArrIndex < threadPoolSize - 1; subArrIndex++) {
                    System.arraycopy(
                            testingArray,
                            subSize * subArrIndex,
                            subArrays[subArrIndex],
                            0,
                            subSize
                    );
                    // Запуск потоков-сортировщиков для каждого из подмассивов
                    threads[subArrIndex] = new QuickSort(subArrays[subArrIndex]);
                    threads[subArrIndex].start();
                }
                System.arraycopy(
                        testingArray,
                        subSize * (threadPoolSize - 1),
                        lastSubArray,
                        0,
                        arrSize - (threadPoolSize - 1) * subSize
                );
                threads[threadPoolSize - 1] = new QuickSort(lastSubArray);
                threads[threadPoolSize - 1].start();

                // Для каждого потока вызывается метод join(),
                // чтобы основной поток не перешёл к этапу слияния подмассивов раньше времени
                for (int i = 0; i < threadPoolSize; i++) {
                    threads[i].join();
                }

                // Получение отсортированных подмассивов
                for (int i = 0; i < threadPoolSize - 1; i++) {
                    System.arraycopy(
                            threads[i].getSortedArr(),
                            0,
                            subArrays[i],
                            0,
                            subSize
                    );
                }
                System.arraycopy(
                        threads[threadPoolSize - 1].getSortedArr(),
                        0,
                        lastSubArray,
                        0,
                        arrSize - (threadPoolSize - 1) * subSize
                );

                // Отсортированные подмассивы в несколько этапов несколькими сливающимися потоками
                // соединяются в один массив
                int lastSubSize = subSize;
                int numberOfArrays = threadPoolSize;

                while (numberOfArrays > 1) {
                    int currNumberOfArrays = numberOfArrays / 2;
                    Merger[] mergers = new Merger[currNumberOfArrays]; // Выделение памяти под массив потоков

                    // Запуск потоков для слияния пар подмассивов
                    for (int i = 0; i < currNumberOfArrays - 1; i++) {
                        mergers[i] = new Merger(subArrays[2 * i], subArrays[2 * i + 1]);
                        mergers[i].start();
                    }
                    mergers[currNumberOfArrays - 1] =
                            new Merger(subArrays[(currNumberOfArrays - 1) * 2], lastSubArray);
                    mergers[currNumberOfArrays - 1].start();

                    // Для каждого потока вызывается метод join(),
                    // чтобы основной поток не перешёл к этапу записи новых подмассивов раньше времени
                    for (int i = 0; i < currNumberOfArrays; i++) {
                        mergers[i].join();
                    }

                    // Записываем новые подмассивы, полученные на этапе слияния
                    int currSubSize = 2 * lastSubSize;
                    subArrays = Arrays.copyOf(subArrays, currNumberOfArrays);
                    for (int i = 0; i < currNumberOfArrays - 1; i++) {
                        subArrays[i] = Arrays.copyOf(subArrays[i], currSubSize);
                    }
                    lastSubArray = Arrays.copyOf(
                            lastSubArray, arrSize - currSubSize * (currNumberOfArrays - 1)
                    );

                    for (int i = 0; i < currNumberOfArrays - 1; i++) {
                        System.arraycopy(
                                mergers[i].getMergedArray(),
                                0,
                                subArrays[i],
                                0,
                                currSubSize);
                    }
                    System.arraycopy(
                            mergers[currNumberOfArrays - 1].getMergedArray(),
                            0,
                            lastSubArray,
                            0,
                            arrSize - currSubSize * (currNumberOfArrays - 1));

                    lastSubSize = currSubSize;
                    numberOfArrays = currNumberOfArrays;
                }

                elapsedTime = System.currentTimeMillis() - startTime; // Конец подсчёта времени
                System.out.println("Sorting time for " + arrSize +
                        " elements with " + threadPoolSize + " threads is " + (double) elapsedTime / 1000);
            }

            // Однопоточная сортировка
            startTime = System.currentTimeMillis();

            QuickSort oneThreadSort = new QuickSort(testingArray);
            oneThreadSort.start();
            oneThreadSort.join();

            elapsedTime = System.currentTimeMillis() - startTime;

            System.out.println("Sorting time for " + arrSize +
                    " elements with " + 1 + " thread is " + (double) elapsedTime / 1000);
        }
    }

    private static int[] createArray(int size) {
        int[] res = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            res[i] = random.nextInt(size);
        }
        return res;
    }
}
