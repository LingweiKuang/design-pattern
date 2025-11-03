package com.klw.structural.decorator;

// --- 演示 main，展示链的顺序敏感性 ---
public class DecoratorTextDemo {
    public static void main(String[] args) {
        String raw = "Hello <b>world</b>, this is teh sample text with a typo: recieve and funciton.";

        // 基础文本
        Text base = new PlainText(raw);

        // 链 1：SpellCheck -> HtmlEscape -> Encryption
        Text chain1 = new EncryptionDecorator(
                new HtmlEscapeDecorator(
                        new SpellCheckDecorator(base)));

        String out1 = chain1.process();
        System.out.println("Chain1 (SpellCheck -> HtmlEscape -> Encryption):");
        System.out.println(out1);
        System.out.println("Decrypted Chain1:");
        System.out.println(EncryptionDecorator.decrypt(out1));
        System.out.println("--------------------");

        // 链 2：Encryption -> Compression -> HtmlEscape (注意：将导致 HTML 转义作用在已加密/压缩后的不可读数据上)
        Text chain2 = new HtmlEscapeDecorator(
                new CompressionDecorator(
                        new EncryptionDecorator(base)));

        String out2 = chain2.process();
        System.out.println("Chain2 (Encryption -> Compression -> HtmlEscape):");
        System.out.println(out2);
        System.out.println("--------------------");

        // 链 3：Compression -> Encryption (通常推荐：先压缩再加密)
        Text chain3 = new EncryptionDecorator(new CompressionDecorator(base));
        String out3 = chain3.process();
        System.out.println("Chain3 (Compression -> Encryption):");
        System.out.println(out3);
        System.out.println("Decompress+Decrypt Chain3:");
        // 先解密再解压
        String decrypted = EncryptionDecorator.decrypt(out3);
        System.out.println(CompressionDecorator.decompress(decrypted));
    }
}
