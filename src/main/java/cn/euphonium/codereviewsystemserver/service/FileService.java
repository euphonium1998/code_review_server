package cn.euphonium.codereviewsystemserver.service;

import cn.euphonium.codereviewsystemserver.entity.CodeMsg;
import cn.euphonium.codereviewsystemserver.entity.PDF;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {
    String codeReview(String code) throws IOException;
    CodeMsg codeFormat(CodeMsg codeMsg);
    ResponseEntity<Object> download();
    PDF generatePDF(String code) throws IOException;
}
