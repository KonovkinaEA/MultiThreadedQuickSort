public class QuickSort extends Thread {
    private final int[] sortableArr;

    public QuickSort(int[] sortableArr) {
        this.sortableArr = sortableArr;
    }

    @Override
    public void run() {
        sort(sortableArr, 0, sortableArr.length);
    }

    public int[] getSortedArr() {
        return sortableArr;
    }

    private static void sort(int[] arr, int begin, int end) {
        if (begin >= end - 1) return; // Пустой массив и массив из одного элемента сортировать не нужно

        int i = partition(arr, begin, end);
        sort(arr, begin, i); // Сортировка для элементов слева от последнего опорного элемента
        sort(arr, i + 1, end); // Аналогично для элементов справа
    }

    // Все элементы массива переупорядочиваются вокруг опорного элемента -
    // те, что имеют меньшее значение, помещаются перед ним,
    // а все элементы, которые равны или больше опорного элемента, - после него
    private static int partition(int[] arr, int begin, int end) {
        int pivot = arr[begin]; // В качестве опорного элемента был выбран первый элемент массива
        int ind = begin;

        for (int i = begin + 1; i < end; i++) {
            if (arr[i] < pivot) swap(arr, ++ind, i);
        }
        swap(arr, ind, begin);

        return ind;
    }

    private static void swap(int[] arr, int left, int right) {
        int tmp = arr[left];
        arr[left] = arr[right];
        arr[right] = tmp;
    }
}
