package cn.euphonium.codereviewsystemserver.service.impl;

import cn.euphonium.codereviewsystemserver.entity.CodeMsg;
import cn.euphonium.codereviewsystemserver.entity.ConstInfo;
import cn.euphonium.codereviewsystemserver.service.FileService;
import cn.euphonium.codereviewsystemserver.utils.CodeUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Override
    public CodeMsg codeFormat(CodeMsg codeMsg) {
        //todo
        String code = codeMsg.getCode();
        CodeMsg res = new CodeMsg();
        try {
            //write to file
            BufferedWriter out = new BufferedWriter(new FileWriter("code_format.c"));
            out.write(code);
            out.close();

            StringBuilder sb = new StringBuilder();
//            sb.append("eupho");
            Process process = Runtime.getRuntime().exec("gcc -std=c99 code_format.c");
            InputStream inputStream = process.getErrorStream();
            byte[] b = new byte[8192];
            for (int n; (n = inputStream.read(b)) != -1; ) {
                sb.append(new String(b, 0, n));
            }

            inputStream.close();
            process.destroy();

            String compileResult = sb.toString();
//            System.out.println(compileResult);

            //regex match
            String[] compileResultLines = compileResult.split("\n");
            String regex = ".*error.*";
            for (String line : compileResultLines) {
                if (line.matches(regex)) {
                    res.setStatus(ConstInfo.C_COMPILE_ERROR);
                    break;
                }
            }
            //do code format
            if (res.getStatus() == 0) {
                String originalCode = codeMsg.getCode();
//                System.out.println(originalCode);
                res.setCode(CodeUtils.codeFormat(originalCode));
            }

            //write back to file
            String codeFormatted = res.getCode();
            BufferedWriter out2 = new BufferedWriter(new FileWriter("code_format.c"));
            out2.write(codeFormatted);
            out2.close();

        }catch (IOException e) {
            e.getStackTrace();
        }
        return res;
    }

    @Override
    public ResponseEntity<Object> download() {
        File file = new File("code_format.c");
        if (file.isFile()) {
//            System.out.println("get file");
            return downloadFile(file);
        } else {
//            System.out.println("no file exist");
            return null;
        }
    }

    public ResponseEntity<Object> downloadFile(File file) {
        String fileName = file.getName();
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment;filename=\"%s", fileName));
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
        return responseEntity;
    }
}
