package cn.euphonium.codereviewsystemserver.utils;

import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeUtils {

    public static String opRegex = "";
    public static final String ANNOTATION_REPLACEMENT = "@annotation";
    public static final String[] BINARY_OPERATOR = {"<<", ">>", "&&", "\\|\\|", "\\+", "-", "\\*", "/", "%", "=", "==", "!=", "<", ">", "<=", ">=", "\\+=", "\\*=", "/=", "-="};

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String op : BINARY_OPERATOR) {
            sb.append(op).append("|");
        }
        sb.deleteCharAt(sb.length() - 1).append(")");
        opRegex = sb.toString();
    }

    public static String indentSizeToSpace(int indentSize) {
        String fourSpace = "    ";
        StringBuilder res = new StringBuilder();
        while (indentSize-- > 0) {
            res.append(fourSpace);
        }
        return res.toString();
    }

    public static String[] forContentSplit(String content) {
        String[] res = new String[3];
        for (int i = 0; i < 3; ++i) {
            res[i] = "";
        }
        Pattern pattern = Pattern.compile(";");
        Matcher matcher = pattern.matcher(content);
        int last = -1, idx = 0;
        while (matcher.find()) {
            res[idx++] = content.substring(last + 1, matcher.start());
            last = matcher.start();
        }
        res[idx] = content.substring(last + 1);
        return res;
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
//            System.out.println(sub);
            quotes.add(sub);
        }
        originalLine = originalLine.replaceAll(quotation.pattern(), Character.toString(flag));
        return originalLine;
    }

    public static String quotesRecovery(char flag, List<String> quotes, String originalLine) {
        Pattern quotation = Pattern.compile(Character.toString(flag));
        for (String quote : quotes) {
//            System.out.println(originalLine);
//            System.out.println(quote);
//            System.out.println(quotation.pattern());
            originalLine = originalLine.replaceFirst(quotation.pattern(), Matcher.quoteReplacement(quote));
//            System.out.println(originalLine);
        }
        return originalLine;
    }

    public static String tabToSpace(String originalCode) {
        String fourSpace = "    ";
        return originalCode.replaceAll("\t", fourSpace);
    }

    //not consider about while/for/if/...
    //function param can't write two line
    public static String formatMain(String originalCode) {
        int indentSize = 0;
//        StringBuilder sb = new StringBuilder();
        String[] lines = originalCode.split("\r\n|\r|\n");
        for (int i = 0; i < lines.length; i++) {
            Pattern indentPattern = Pattern.compile("^\\s*");

            //quotation
            List<String> doubleQuoteInfo = new ArrayList<>();
            List<String> singleQuoteInfo = new ArrayList<>();
            lines[i] = quotesReplacement('\"', doubleQuoteInfo, lines[i]);
            lines[i] = quotesReplacement('\'', singleQuoteInfo, lines[i]);

            //bracket process
            lines[i] = bracketProcess(lines[i]);

            //semicolon process
            lines[i] = semicolonProcess(lines[i]);

            //comma process
            StringBuilder sb = new StringBuilder();
            Pattern comma = Pattern.compile(",");
            Matcher matchComma = comma.matcher(lines[i]);
            int lastCommaIdx = -1;

            while (matchComma.find()) {
                String subBeforeLastComma = lines[i].substring(lastCommaIdx + 1, matchComma.start());
//                System.out.println(subBeforeLastComma);
                //process right
                subBeforeLastComma = subBeforeLastComma.replaceAll("^\\s*", " ");
                // process left
                subBeforeLastComma = subBeforeLastComma.replaceAll("\\s+$", "");
                sb.append(subBeforeLastComma).append(",");
                lastCommaIdx = matchComma.start();
            }
            String subAfterLastComma = lines[i].substring(lastCommaIdx + 1);
            sb.append(subAfterLastComma.replaceAll("^\\s*", " "));
            lines[i] = sb.toString();
//            System.out.println(lines[i]);

            //operator process
            lines[i] = operatorProcess(lines[i]);

            //space process
            lines[i] = spaceProcess(lines[i]);

            //indent process
            lines[i] = lines[i].replaceAll(indentPattern.pattern(), indentSizeToSpace(indentSize));

            //judge next line indent
            for (int j = 0; j < lines[i].length(); j++) {
                if (lines[i].charAt(j) == '{') {
                    indentSize++;
                } else if (lines[i].charAt(j) == '}') {
                    indentSize--;
                    if (lines[i].matches("^\\s*}")) {
                        lines[i] = lines[i].replaceAll(indentPattern.pattern(), indentSizeToSpace(indentSize));
                    }
                    /*
                    in default will not happen
                    if (indentSize < 0) {

                    }
                     */
                }
            }
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
    public static String annotationProcess(String originalCode, List<String> annotationList) {
        Pattern pattern = Pattern.compile("((//.*)|(/\\*(.|\r\n|\r|\n)*\\*/))(\r\n|\n|\r)");
        Matcher matcher = pattern.matcher(originalCode);
        while (matcher.find()) {
            String sub = originalCode.substring(matcher.start(), matcher.end() - 1);
            annotationList.add(sub);
        }
        return originalCode.replaceAll(pattern.pattern(), ANNOTATION_REPLACEMENT+"\n");

    }

    public static String annotationRecovery(String originalCode, List<String> annotationList) {
        String code = originalCode;
        for (String annotation : annotationList) {
            code = code.replaceFirst(ANNOTATION_REPLACEMENT, annotation);
//            System.out.println(code);
        }
        return code;
    }

    public static String semicolonProcess(String originalCodeLine) {
        Pattern semicolonEnd = Pattern.compile("(.*)(;\\s*(@annotation)?)$");
        Pattern forPattern = Pattern.compile("(.*for\\s*\\()(.*)(\\).*)");

        String code = originalCodeLine;
        Matcher matcherFor = forPattern.matcher(code);
//        System.out.println("befor for end line");
//        System.out.println(code);

        if (matcherFor.matches()) {
            String bracketLeft = matcherFor.group(1);
            String bracketIn = matcherFor.group(2);
            String bracketRight = matcherFor.group(3);
            String[] ins = forContentSplit(bracketIn);
            for (int idx = 0; idx < ins.length; ++idx) {
//                System.out.println(bracketIn);
//                System.out.println(idx + ":" + ins[idx]);
                ins[idx] = ins[idx].replaceAll("^\\s*", " ");
                ins[idx] = ins[idx].replaceAll("\\s*$", "");
            }
            //first " int i = 1" -> "int i = 1"
            if (ins[0].length() > 0) {
                ins[0] = ins[0].substring(1);
            }
            StringBuilder stringBuilderFor = new StringBuilder();
            stringBuilderFor.append(bracketLeft);
            bracketIn = String.join(";", ins);
            stringBuilderFor.append(bracketIn);
            stringBuilderFor.append(bracketRight);
            code = stringBuilderFor.toString();
//            System.out.println(code);
        }
//        System.out.println("for process");
//        System.out.println(code);

        //for(;;); will match successfully
        Matcher matcherSemicolonEnd = semicolonEnd.matcher(code);
        if (matcherSemicolonEnd.matches()) {
            String beforeSemicolon = matcherSemicolonEnd.group(1);
            String afterSemicolon = matcherSemicolonEnd.group(2);
            beforeSemicolon = beforeSemicolon.replaceAll("\\s*$", "");
            code = beforeSemicolon + afterSemicolon;
//            System.out.println(code);
        }

        return code;
    }

    //recursion to deal with (())
    public static String bracketProcess(String originalLine) {
        String code = originalLine;
        Pattern bracket = Pattern.compile("(^.*?)\\((.*)\\)(.*?$)");
        Matcher matcherBracket = bracket.matcher(code);

        if (matcherBracket.matches()) {
            String bracketLeft = matcherBracket.group(1);
            String bracketIn = matcherBracket.group(2);
            String bracketRight = matcherBracket.group(3);
//            System.out.println(bracketLeft);
//            System.out.println(bracketIn);
//            System.out.println(bracketRight);

            //process left
            Pattern keywordWithBracket = Pattern.compile("(^.*)(for|if|while|switch)\\s*");
            Matcher matcherKeyword = keywordWithBracket.matcher(bracketLeft);
            if (matcherKeyword.matches()) {
                bracketLeft = matcherKeyword.group(1) + matcherKeyword.group(2) + " ";
            } else {
                bracketLeft = bracketLeft.replaceAll("\\s*$", "");
            }

            //process code in bracket
            bracketIn = bracketProcess(bracketIn);
            code = bracketLeft + "(" + bracketIn + ")" + bracketRight;
        }
        return code;
    }

    public static String spaceProcess(String originalLine) {
        return originalLine.replaceAll("\\s+", " ");
    }

    public static String operatorProcess(String originalLine) {
//        System.out.println(originalLine);
        if (originalLine.matches("\\s*#include<.*>\\s*")) {
            return originalLine;
        }
        Pattern binaryOperatorPattern = Pattern.compile("(\\s*(\\d|\\w)+\\s*" + opRegex + ")+" + "\\s*(\\d|\\w)+\\s*");
//        System.out.println(binaryOperatorPattern.pattern());

        Matcher matcherOp = binaryOperatorPattern.matcher(originalLine);
        int last = 0;
        StringBuilder sb = new StringBuilder();
        while (matcherOp.find()) {
            String opCodeDetail = originalLine.substring(matcherOp.start(), matcherOp.end());
            sb.append(originalLine, last, matcherOp.start());
//            System.out.println(opCodeDetail);
            opCodeDetail = operatorProcessDetail(opCodeDetail);
//            System.out.println(opCodeDetail);
            sb.append(opCodeDetail);
            last = matcherOp.end();

//            String sub = code.substring(matcherOp.start() + matcherOp.group(1).length() + matcherOp.group(2).length());
//            System.out.println(sub);
//            System.out.println(matcherOp.group(1));
//            System.out.println(matcherOp.group(2));
//            System.out.println(matcherOp.group(3));

        }
        sb.append(originalLine.substring(last));
        String code = sb.toString();

        return code;
    }

    private static String operatorProcessDetail(String code) {
        String[] vars = code.split(opRegex);
//        System.out.println(Arrays.toString(vars));
        Pattern patternOp = Pattern.compile(opRegex);
        Matcher matcherOp = patternOp.matcher(code);
        int idx = 0;
        StringBuilder sb = new StringBuilder();
        while (matcherOp.find()) {
            sb.append(" ").append(vars[idx++]).append(" ");
            sb.append(code.substring(matcherOp.start(), matcherOp.end()));
//            System.out.println(sub);
        }
        sb.append(" ").append(vars[idx]).append(" ");

        return sb.toString();
    }

    public static String codeFormat(String originalCode) {

        if (originalCode == null || originalCode.equals("")) {
            return "";
        }

        List<String> annotationList = new ArrayList<>();

        //step1. tab -> 4 space
        String codeFormatted = CodeUtils.tabToSpace(originalCode);

        //step2. annotation process
        codeFormatted = CodeUtils.annotationProcess(codeFormatted, annotationList);

        //step3. format main
        codeFormatted = CodeUtils.formatMain(codeFormatted);

//        System.out.println("\n\nresult: ");
//        System.out.println(CodeUtils.annotationRecovery(codeFormatted, annotationList));
        //step final annotation recovery
        return CodeUtils.annotationRecovery(codeFormatted, annotationList);
    }
}
