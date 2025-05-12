package com.example.melopix.Melopix_backend.VO;

import lombok.Data;
import java.util.List;

@Data
public class PhototagApiVO {
    private String title;
    private String description;
    private List<String> keywords;
}