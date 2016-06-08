package com.cloud.dc.node.remotewin;

import java.io.*;

/**
 * Created by root on 2015/8/4.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        InputStream aaa =  Test.class.getClassLoader().getResourceAsStream("MLP5.0.zip");
        FileOutputStream Fout = new FileOutputStream("C:\\MLP5.0.zip");
        BufferedOutputStream out = new BufferedOutputStream(Fout);
             BufferedInputStream in = new BufferedInputStream(aaa);
            int  len;
            byte []bytes = new byte[2048];
            while((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            in.close();
            out.close();
        }

}
