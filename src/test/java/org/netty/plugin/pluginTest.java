package org.netty.plugin;

/**
 * Created by XiuYin.Cui on 2018/9/4.
 *
 * IDEA 中一些好用的插件
 *
 */

// Rainbow Brackets : 彩虹颜色的括号

public class pluginTest {

    // GSONFormat : Json 文本直接导成 Java 对象
    /**
     * a : Hello
     * b : World
     */

    private String a;
    private String b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    // VisualVM Launcher : 某个对象占用了多大的内存

    public static void main(String[] args) {
        String str = "Hello, World";
        System.out.println(str);

    }

    // Translation : 翻译插件

}
