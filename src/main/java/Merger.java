public class Merger extends Thread {
    private final int[] first;
    private final int[] second;
    private final int[] res;

    public Merger(int[] first, int[] second) {
        this.first = first;
        this.second = second;
        this.res = new int[first.length + second.length];
    }

    @Override
    public void run() {
        merge();
    }

    public int[] getMergedArray() {
        return res;
    }

    private void merge() {
        int firstInd = 0; // Указатель на элемент в первом подмассиве
        int secondInd = 0; // Указатель на элемент в правом подмассиве
        int resInd = 0; // Указатель на место в результирующем массиве

        while (firstInd < first.length && secondInd < second.length) {
            // Элементы из двух подмассивов сравниваются и
            // в результирующий массив помещается меньший из них
            if (first[firstInd] <= second[secondInd]) {
                res[resInd++] = first[firstInd++];
            } else {
                res[resInd++] = second[secondInd++];
            }
        } // Данная проверка выполняется, пока не закончится один из подмассивов

        // Если в оставшемся подмассиве еще есть непройденные элементы,
        // они по порядку добавляются в результирующий массив
        if (firstInd == first.length) {
            while (secondInd < second.length) {
                res[resInd++] = second[secondInd++];
            }
        } else if (secondInd == second.length) {
            while (firstInd < first.length) {
                res[resInd++] = first[firstInd++];
            }
        }
    }
}
