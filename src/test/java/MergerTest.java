import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class MergerTest {

    @Test
    public void testSortingForTwoThreads() throws InterruptedException {
        // Генерация исходного массива
        int[] arr = new int[20];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println("Array before sorting: " + Arrays.toString(arr));

        int[] exp = Arrays.copyOf(arr, arr.length);
        Arrays.sort(exp); // Ожидаемый вид отсортированного массива
        System.out.println("Expected result: " + Arrays.toString(exp));

        // Разделение исходного массива на два подмассива
        int threadPool = 2;
        int[] subArray1 = new int[arr.length / threadPool];
        int[] subArray2 = new int[arr.length - arr.length / threadPool];
        System.arraycopy(arr, 0, subArray1, 0, arr.length / threadPool);
        System.arraycopy(
                arr, arr.length / threadPool, subArray2, 0, arr.length - arr.length / threadPool
        );

        // Сортировка двух подмассивов
        QuickSort sorter1 = new QuickSort(subArray1);
        QuickSort sorter2 = new QuickSort(subArray2);
        sorter1.start(); // Начало работы первого потока
        sorter2.start(); // Начало работы второго потока
        sorter1.join();
        sorter2.join(); // Основной поток не продолжит работу, пока не закончат первый и второй потоки

        // Процесс слияния подмассивов аналогичен процессу сортировки, за исключением того,
        // что используется один поток, а не два
        Merger merger = new Merger(sorter1.getSortedArr(), sorter2.getSortedArr());
        merger.start();
        merger.join();
        System.arraycopy(merger.getMergedArray(), 0, arr,0, arr.length);
        System.out.println("Array after sorting: " + Arrays.toString(arr));

        Assert.assertArrayEquals(exp, arr);
    }
}
