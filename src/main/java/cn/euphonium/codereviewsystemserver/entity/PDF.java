package cn.euphonium.codereviewsystemserver.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PDF extends Msg {
    String originalCode = "";
    String compileRes = "";
    String staticAnalysisRes = "";
    String codeFormatRes = "";

    public PDF(int status) {
        this.status = status;
    }
}
