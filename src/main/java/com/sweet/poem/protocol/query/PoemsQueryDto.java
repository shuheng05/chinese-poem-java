package com.sweet.poem.protocol.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemsQueryDto {
    private Integer start;
    private Integer count;
    private String type;
    private String author;
    private String dynasty;
    private String form;
}
