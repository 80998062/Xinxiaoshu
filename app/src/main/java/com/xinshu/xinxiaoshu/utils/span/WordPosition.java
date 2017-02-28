package com.xinshu.xinxiaoshu.utils.span;

class WordPosition {
    int start;
    int end;

    WordPosition(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "WordPosition{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}