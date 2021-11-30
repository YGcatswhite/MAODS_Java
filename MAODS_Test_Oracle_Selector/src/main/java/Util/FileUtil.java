package Util;

import java.io.*;

/**
 * 对文件移动和删除的工具类
 * @author 姬筠刚 191250055
 */
public class FileUtil {
    /**
     * 将一个路径下的所有文件拷贝到新的路径
     * @param oldPath 被拷贝的路径
     * @param newPath 要靠背到的路径
     * @return 无
     */
    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        //文件名称列表
        String[] filePath = file.list();
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }
        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + file.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath  + file.separator  + filePath[i], newPath  + file.separator + filePath[i]);
            }

            if (new File(oldPath  + file.separator + filePath[i]).isFile()) {
                copyFile(oldPath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }

        }
    }
    /**
     * 将一个路径下的一个文件文件拷贝到新的路径下
     * @param oldPath 被拷贝的路径
     * @param newPath 要靠背到的路径
     * @return 无
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        if(!oldFile.exists()){
            System.out.println(oldPath+" not exits!");
        }
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);;

        byte[] buffer=new byte[2097152];
        int readByte = 0;
        while((readByte = in.read(buffer)) != -1){
            out.write(buffer, 0, readByte);
        }
        in.close();
        out.close();
    }
    /**
     * 向src文件添加header
     * @param header 追加的头部字符串，用于加所属包名称，如：package [包名称]
     * @param srcPath 追加文件地址地址
     */
    public static void appendFileHeader(byte[] header,String srcPath) throws Exception{
        RandomAccessFile src = new RandomAccessFile(srcPath, "rw");
        int srcLength = (int)src.length() ;
        byte[] buff = new byte[srcLength];
        src.read(buff , 0, srcLength);
        src.seek(0);
        src.write(header);
        src.seek(header.length);
        src.write(buff);
        src.close();
    }
    /**
     * 将src文件清空并输入"//这就是一个空文件，在程序运行过程中才被写入"
     * @param srcPath 文件地址
     */
    public static void cleanFile(String srcPath){
        File file = new File(srcPath);
        // 如果文件不存在就创建这个文件
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write("//这就是一个空文件，在程序运行过程中才被写入");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将src文件初始化，本项目是对ObjectExecutorEntry.java初始化,为了应对变更，我宣布这个方法没用了==
     * @param srcPath 文件地址
     */
    public static void initFile(String srcPath){
        File log = new File(srcPath);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(log);
            fileWriter.write("//这就是一个空文件，在程序运行过程中才被写入\n" +
                    "package ObjectExecutor;\n" +
                    "\n" +
                    "public class ObjectExecutorEntry {\n" +
                    "}");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 向src文件添加内容
     * @param content
     * @param srcPath
     */
    public static void writeToFile(String content,String srcPath){
        try {
            File file = new File(srcPath);
            // 如果文件不存在就创建这个文件
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
