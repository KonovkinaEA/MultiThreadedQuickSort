import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class QuickSortTest {
    @Test
    public void testSortingAlgorithm() {
        // Генерация исходного массива
        int[] arr = new int[10];
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            arr[i] = random.nextInt(20);
        }
        QuickSort sorter = new QuickSort(arr);

        System.out.println("Array before sorting: " + Arrays.toString(arr));
        Arrays.sort(arr); // Ожидаемый вид отсортированного массива
        System.out.println("Expected result: " + Arrays.toString(arr));
        sorter.start(); // Сортировка
        int[] res = sorter.getSortedArr(); // Отсортированный массив
        System.out.println("Array after sorting: " + Arrays.toString(res));

        Assert.assertArrayEquals(arr, res);
    }
}