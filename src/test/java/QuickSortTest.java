import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class QuickSortTest {
    private QuickSort sorter;

    @Test
    public void testSortingAlgorithm() {
        int arr1[] = {2, -3, 6, 8, 0, -1, -8, 2, 7, -9};
        int exp1[] = {-9, -8, -3, -1, 0, 2, 2, 6, 7, 8};
        sorter = new QuickSort(arr1);

        System.out.println("Массив до сортировки: " + Arrays.toString(arr1));
        System.out.println("Ожидаемый результат: " + Arrays.toString(exp1));
        sorter.start();
        int res1[] = sorter.getSortedArr();
        System.out.println("Массив после сортировки: " + Arrays.toString(res1) + "\n");

        int[] arr2 = new int[10];
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            arr2[i] = random.nextInt(20);
        }
        sorter = new QuickSort(arr2);

        System.out.println("Массив до сортировки: " + Arrays.toString(arr2));
        Arrays.sort(arr2);
        System.out.println("Ожидаемый результат: " + Arrays.toString(arr2));
        sorter.start();
        int res2[] = sorter.getSortedArr();
        System.out.println("Массив после сортировки: " + Arrays.toString(res2));

        Assert.assertArrayEquals(exp1, res1);
        Assert.assertArrayEquals(arr2, res2);
    }
}