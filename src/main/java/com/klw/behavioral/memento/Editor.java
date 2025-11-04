package com.klw.behavioral.memento;

/**
 * Originator：编辑器
 */
public class Editor {
    private String content = "";

    // 私有具体备忘录，只能被 Editor 访问其 state 字段 -> 保持不透明
    private static class EditorMemento implements Memento {
        private final String state;

        private EditorMemento(String s) {
            this.state = s;
        }
    }

    public void type(String more) {
        if (more == null) return;
        content += more;
    }

    public String getContent() {
        return content;
    }

    // 保存当前状态为 Memento（外部只看到 Memento 接口）
    public Memento save() {
        return new EditorMemento(content);
    }

    // 恢复：只接受自己私有实现的 Memento
    public void restore(Memento m) {
        if (!(m instanceof EditorMemento)) {
            throw new IllegalArgumentException("Unsupported memento");
        }
        this.content = ((EditorMemento) m).state;
    }
}
