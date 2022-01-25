import java.util.*;


public class Main {

    public static void main(String argv[]) {
        Main m = new Main();
        try {
            m.run();
        } catch (Exception e) {
            m.log(e.toString());
        }
    }

    private void run() {
        log("Hello World!");
        
        int[] a = {6, 3, 7, 4, 22, 63, 16, 93, 124, 8, 1, 2};
        MaxHeap h = new MaxHeap(a);
    }

    private void log(String s) {
        System.out.println(s);
    }
}


// maintains the largest element at array[0]
// not necessarily sorted.
public class MaxHeap {

    int[] array;
    
    public MaxHeap(int[] array) {
        this.array = array;
        
        System.out.println("Before: " + toString());
        build_heap();
        System.out.println("After : " + toString());
        printTreeNode(0, "");
    }
    
    // 0 -> 1, 2
    // 1 -> 3, 4
    // 2 -> 5, 6
    private int parent(int i) {
        return (int)((i - 1) / 2);
    }
    
    private int left_child(int i) {
        return (i + 1) * 2 - 1;
    }
    
    private int right_child(int i) {
        return (i + 1) * 2;
    }
    
    private void swap(int i, int j) {
        int s = array[i];
        array[i] = array[j];
        array[j] = s;
    }
    
    private void verify_children_of(int i) {
        if (i >= array.length)
            return;
        
        System.out.println("Verifying children of " + nodeString(i));
        
        
        int l = left_child(i);
        int r = right_child(i);
        int largest_index = i;
        
        if (l < array.length) {
            if (array[l] > array[i]) {
                largest_index = l;
            }
        }
        if (r < array.length) {
            if (array[r] > array[largest_index]) {
                largest_index = r;
            }
        }
        if (largest_index != i) {
            System.out.println("Swapping array[" + i + "]=" + array[i] + " and array[" + largest_index + "]=" + array[largest_index]);
            swap(largest_index, i);
            verify_children_of(largest_index);
        }
    }
    
    // runs in O(n) time complexity
    public void build_heap() {
        // the second half is only leaves without further children
        for (int i = (array.length - 1) / 2; i >= 0; i--) {
            verify_children_of(i);
            System.out.println("");
        }
    }
    
    public String toString() {
        return Arrays.toString(array);
    }
    
    public String nodeString(int i) {
        String s = "array[" + i + "]=" + array[i];
        
        int l = left_child(i);
        if (l < array.length) {
            s += " L[" + l + "]=" + array[l];
        }
        
        int r = right_child(i);
        if (r < array.length) {
            s += " R[" + r + "]=" + array[r];
        }
        
        return s;
    }
    
    public void printTreeNode(int i, String prefix) {
        System.out.println(prefix + "[" + i + "]=" + array[i]);
        
        int l = left_child(i);
        if (l < array.length) {
            printTreeNode(l, prefix + " + . . ");
        }
        
        int r = right_child(i);
        if (r < array.length) {
            printTreeNode(r, prefix + " + . . ");
        }
    }
}

