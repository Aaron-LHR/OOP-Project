package src.UI;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class FontAttrib {
    public static final int GENERAL = 0; // 常规
    public static final int BOLD = 1; // 粗体
    public static final int ITALIC = 2; // 斜体
    public static final int BOLD_ITALIC = 3; // 粗斜体
    // public static final FontAttrib defaultAttr = new FontAttrib();  // 默认格式

    public SimpleAttributeSet attrSet = null; // 属性集
    public String text, name; // 要输入的文本和字体名称
    public int style, size; // 样式和字号
    public Color color, backColor; // 文字颜色和背景颜色

    public FontAttrib() {}

    public FontAttrib(String msg, String name, int style, int size, Color color, Color backCol) {
        setText(msg);
        setName(name);
        setStyle(style);
        setSize(size);
        setColor(color);
        setBackColor(backCol);
    }

    public SimpleAttributeSet getAttrSet() {
        attrSet = new SimpleAttributeSet();

        if (name != null) StyleConstants.setFontFamily(attrSet, name);

        if (style == FontAttrib.GENERAL) {
            StyleConstants.setBold(attrSet, false);
            StyleConstants.setItalic(attrSet, false);
        }
        else if (style == FontAttrib.BOLD) {
            StyleConstants.setBold(attrSet, true);
            StyleConstants.setItalic(attrSet, false);
        }
        else if (style == FontAttrib.ITALIC) {
            StyleConstants.setBold(attrSet, false);
            StyleConstants.setItalic(attrSet, true);
        }
        else if (style == FontAttrib.BOLD_ITALIC) {
            StyleConstants.setBold(attrSet, true);
            StyleConstants.setItalic(attrSet, true);
        }

        StyleConstants.setFontSize(attrSet, size);

        if (color != null) {
            StyleConstants.setForeground(attrSet, color);
        }
        if (backColor != null) {
            StyleConstants.setBackground(attrSet, backColor);
        }
        return attrSet;
    }

    public void setAttrSet(SimpleAttributeSet attrSet) {
        this.attrSet = attrSet;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBackColor() {
        return backColor;
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
