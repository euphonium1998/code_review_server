package cn.euphonium.codereviewsystemserver.entity;

import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
public class Question {
    int id;
    String name;
    String account;
    String accountName;
    String questionDescription;
    String inputDescription;
    String outputDescription;
    String sampleInput;
    String sampleOutput;
    List<Sample> samples;
}
