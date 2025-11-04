package com.klw.behavioral.memento;

public class EditorMementoDemo {
    public static void main(String[] args) {
        Editor editor = new Editor();
        History history = new History(3);

        // 初始状态保存
        history.push(editor.save());

        editor.type("Hello");
        history.push(editor.save());

        editor.type(", world");
        history.push(editor.save());

        editor.type("! This is a long addition"); // 4th change - history capped at 3
        history.push(editor.save());

        System.out.println("现在的内容: " + editor.getContent()); // 全文

        // undo 一次 -> 回到上一个快照
        Memento m1 = history.undo();
        if (m1 != null) editor.restore(m1);
        System.out.println("undo 1 后内容: " + editor.getContent());

        // 再 undo
        Memento m2 = history.undo();
        if (m2 != null) editor.restore(m2);
        System.out.println("undo 2 后内容: " + editor.getContent());

        // redo
        Memento m3 = history.redo();
        if (m3 != null) editor.restore(m3);
        System.out.println("redo 后内容: " + editor.getContent());

        // 尝试再 undo（如果没有更多历史，则不改变）
        Memento m4 = history.undo();
        if (m4 != null) editor.restore(m4);
        System.out.println("最终内容: " + editor.getContent());
    }
}
