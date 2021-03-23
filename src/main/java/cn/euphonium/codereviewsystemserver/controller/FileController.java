package cn.euphonium.codereviewsystemserver.controller;

import cn.euphonium.codereviewsystemserver.entity.CodeMsg;
import cn.euphonium.codereviewsystemserver.entity.ConstInfo;
import cn.euphonium.codereviewsystemserver.entity.FileContent;
import cn.euphonium.codereviewsystemserver.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.SocketException;

@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload")
    public FileContent upload(MultipartFile file) throws SocketException, IOException {
        FileContent fileContent = new FileContent();
        if (file == null) {
            fileContent.setStatus(ConstInfo.FILE_ERROR);
            return fileContent;
        }
        String content = new String(file.getBytes(),"UTF-8");
        // 获取文件名
        String fileName = file.getOriginalFilename();
//        // 在file文件夹中创建名为fileName的文件
//        OutputStreamWriter op = new OutputStreamWriter(new FileOutputStream("./file/" + fileName), "UTF-8");
//        // 获取文件输入流
//        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
//        char[] bytes = new char[2];
//        StringBuilder sb = new StringBuilder();
//        // 如果这里的bytes不是数组，则每次只会读取一个字节，例如test会变成 t   e     s    t
//        while (inputStreamReader.read(bytes) != -1) {
//            op.write(bytes);
//            sb.append(Arrays.toString(bytes));
//        }
//        // 关闭输出流
//        op.close();
//        // 关闭输入流
//        inputStreamReader.close();
        fileContent.setName(fileName);
        fileContent.setContent(content);
        return fileContent;
    }


    @RequestMapping(value = "/codeReview", method = RequestMethod.POST)
    public String codeReview(@RequestBody CodeMsg codeMsg) throws IOException {

        return fileService.codeReview(codeMsg.getCode());
    }

    @RequestMapping(value = "/codeFormat", method = RequestMethod.POST)
    public CodeMsg codeFormat(@RequestBody CodeMsg codeMsg) {

        return fileService.codeFormat(codeMsg);
    }
}
