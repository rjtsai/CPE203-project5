public class Node {
    private Point pos;
    private int g;
    private int h;
    private int f;
    private Node prev;

    public Node(Point pos, int g, int h, int f, Node prev) {
        this.pos = pos;
        this.g = g;
        this.h = h;
        this.f =f;
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "Node(" +
                "pos:" + pos +
                ", g:" + g +
                ", h:" + h +
                ", f:" + f +
                ", prev:" + prev +
                ')';
    }

    public int getG() { return g; }
    //public int getH() { return h; } not used
    public int getF() { return f; }
    public Point getPos() { return pos; }
    public Node getPrev() { return prev; }
}