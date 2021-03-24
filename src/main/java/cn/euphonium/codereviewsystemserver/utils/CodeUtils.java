package cn.euphonium.codereviewsystemserver.utils;

import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeUtils {

    private static final String annotationReplacement = "@annotation";

    private static List<String> annotationList = new ArrayList<>();

    private static String indentSizeToSpace(int indentSize) {
        String fourSpace = "    ";
        StringBuilder res = new StringBuilder();
        while (indentSize-- > 0) {
            res.append(fourSpace);
        }
        return res.toString();
    }

    public static String quotesReplacement(char flag, List<String> quotes, String originalLine) {
        Pattern quotation;
        if (flag == '\"') {
            quotation = Pattern.compile("\".*?\"");
        } else {
            quotation = Pattern.compile("\'.*?\'");
        }
        Matcher m = quotation.matcher(originalLine);
        while (m.find()) {
            String sub = originalLine.substring(m.start(), m.end());
            quotes.add(sub);
        }
        originalLine = originalLine.replaceAll(quotation.pattern(), Character.toString(flag));
        return originalLine;
    }

    public static String quotesRecovery(char flag, List<String> quotes, String originalLine) {
        Pattern quotation = Pattern.compile(Character.toString(flag));
        for (String quote : quotes) {
            originalLine = originalLine.replaceFirst(quotation.pattern(), quote);
        }
        return originalLine;
    }

    private static String tabToSpace(String originalCode) {
        String fourSpace = "    ";
        return originalCode.replaceAll("\t", fourSpace);
    }

    //not consider about while/for/if/...
    //function param can't write two line
    private static String formatMain(String originalCode) {
        int indentSize = 0;
//        StringBuilder sb = new StringBuilder();
        String[] lines = originalCode.split("\r\n|\r|\n");
        for (int i = 0; i < lines.length; i++) {
            Pattern pattern = Pattern.compile("^\\s*");
            lines[i] = lines[i].replaceAll(pattern.pattern(), indentSizeToSpace(indentSize));

            //quotation
            List<String> doubleQuoteInfo = new ArrayList<>();
            List<String> singleQuoteInfo = new ArrayList<>();
            lines[i] = quotesReplacement('\"', doubleQuoteInfo, lines[i]);
            lines[i] = quotesReplacement('\'', singleQuoteInfo, lines[i]);

            //comma process
            StringBuilder sb = new StringBuilder();
            Pattern comma = Pattern.compile(",");
            Matcher matchComma = comma.matcher(lines[i]);

            while (matchComma.find()) {
                /*
                //todo
                逗号的前后处理
                前面紧贴，后面空一个空格
                用上方的sb拼接，不要动matchComma，因为他在循环里面
                 */
//                char nextCh = lines[i].charAt(matchComma.end());
//                if (Character.toString(nextCh).matches("\\s")) {
//                    String beforeComma = lines[i].substring(ma)
//                    String afterComma = lines[i].substring(matchComma.end());
//                } else {
//
//                }
            }

            for (int j = 0; j < lines[i].length(); j++) {
                if (lines[i].charAt(j) == '{') {
                    indentSize++;
                } else if (lines[i].charAt(j) == '}') {
                    indentSize--;
                    if (lines[i].matches("^\\s*}")) {
                        lines[i] = lines[i].replaceAll(pattern.pattern(), indentSizeToSpace(indentSize));
                    }
                    /*
                    in default will not happen
                    if (indentSize < 0) {

                    }
                     */
                }
            }
            //quotation recovery
            lines[i] = quotesRecovery('\'', singleQuoteInfo, lines[i]);
            lines[i] = quotesRecovery('\"', doubleQuoteInfo, lines[i]);
        }
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    /*
    annotation like "//" "/* " will be replaced by "@annotation"
    not good, but can work, has bug.
     */
    private static String annotationProcess(String originalCode) {
        Pattern pattern = Pattern.compile("((//.*)|(/\\*(.|\r\n|\r|\n)*\\*/))(\r\n|\n|\r)");
        Matcher matcher = pattern.matcher(originalCode);
        while (matcher.find()) {
            String sub = originalCode.substring(matcher.start(), matcher.end() - 1);
            annotationList.add(sub);
        }
        return originalCode.replaceAll(pattern.pattern(), annotationReplacement+"\n");

    }

    private static String annotationRecovery(String originalCode) {
        String code = originalCode;
        for (String annotation : annotationList) {
            code = code.replaceFirst(annotationReplacement, annotation);
//            System.out.println(code);
        }
        return code;
    }

    public static String codeFormat(String originalCode) {

        //step1. tab -> 4 space
        String codeFormatted = CodeUtils.tabToSpace(originalCode);

        //step2. annotation process
        codeFormatted = CodeUtils.annotationProcess(codeFormatted);

        //step3. format main
        codeFormatted = CodeUtils.formatMain(codeFormatted);

        //step final annotation recovery
        return CodeUtils.annotationRecovery(codeFormatted);
    }
}
