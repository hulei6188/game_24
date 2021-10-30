package com.boot.web.hulei;

public enum Pocket {

    POINT_A(1), POINT_2(2), POINT_3(3), POINT_4(4), POINT_5(5), POINT_6(6),
    POINT_7(7), POINT_8(8), POINT_9(9), POINT_10(10), POINT_J(11), POINT_Q(12), POINT_K(13);
    private final int point;

    private Pocket(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return Integer.toString(point);
    }
}
