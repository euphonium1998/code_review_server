package cn.euphonium.codereviewsystemserver.service.impl;

import cn.euphonium.codereviewsystemserver.service.FileService;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String codeReview(String code) throws IOException {

//        File file = new File("file" + File.separator + "test.c");
//        if (file.exists()) {
//            boolean isDelete = file.delete();
//            if (!isDelete) {
//                return "delete file error";
//            }
//        }
//        boolean isCreate = file.createNewFile();
//        if (!isCreate) {
//            return "create file error";
//        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("file" + File.separator + "test.c"));
            out.write(code);
            out.close();

            StringBuilder sb = new StringBuilder();
//            sb.append("eupho");
            Process process = Runtime.getRuntime().exec("splint ./file/test.c");
            InputStream inputStream = process.getInputStream();
            byte[] b = new byte[8192];
            for (int n; (n = inputStream.read(b)) != -1;) {
                sb.append(new String(b, 0, n));
            }

            inputStream.close();
            process.destroy();
            return sb.toString();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return "error";
    }
}
