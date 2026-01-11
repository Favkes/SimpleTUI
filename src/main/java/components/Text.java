package components;

public class Text extends Widget {
    public String content;
    public FormatTuple format;

    public Text(Widget parent,
                int y,
                int x,
                String content,
                FormatTuple format) {
        super(parent, y, x, 1, content.length());
        this.content = content;
        this.format = format;
        parent.children.add(this);
    }
    public Text(Widget parent,
                int y,
                int x,
                String content) {
        this(parent, y, x, content, new FormatTuple("", ""));
    }
    public Text(Widget parent,
                String content) {
        this(parent, 0, 0, content);
    }
    public Text(Widget parent,
                int y,
                int x) {
        this(parent, y, x, "");
    }
    public Text(Widget parent) {
        this(parent, "");
    }

    public void updateContentLength() {
        width = content.length();
    }

    @Override
    public void printInfo() {
        System.out.printf("< Text widget class object instance: %s %d %d %d %d %s >\n", content, y, x, height, width, texture);    }
}
