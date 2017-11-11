package moe.yukisora.flowlayout;

import java.util.ArrayList;
import java.util.List;

class Shapes {
    private Shape shape;
    private List<Integer> x;
    private List<Integer> y;
    private List<Integer> w;
    private List<Integer> h;

    Shapes() {
        shape = new Shape();
        x = new ArrayList<>();
        y = new ArrayList<>();
        w = new ArrayList<>();
        h = new ArrayList<>();
    }

    Shape get(int index) {
        shape.set(x.get(index), y.get(index), w.get(index), h.get(index));

        return shape;
    }

    void set(int index, int x, int y, int w, int h) {
        this.x.add(index, x);
        this.y.add(index, y);
        this.w.add(index, w);
        this.h.add(index, h);
    }
}
