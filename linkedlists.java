/**
 * A linked list is a collection with a dynamic length
 * Search happens in O(n) time, most operations take O(1) time.
 */
interface LList {
    public int size();
    public void add(LList.Node node);
    public void remove(int index);
    public LList.Node get(int index);
    public int indexOf(LList.Node node);
    public void clear();
}

class LListBuilder {
    LList buildLargeLList(int size);
}