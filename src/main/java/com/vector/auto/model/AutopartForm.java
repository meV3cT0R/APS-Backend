package com.vector.auto.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AutopartForm {
    private String name;


    private Map<String,String> specs;

    private double price;
    private String category;

    private List<String> images;
}
