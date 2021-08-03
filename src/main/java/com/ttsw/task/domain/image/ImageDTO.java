package com.ttsw.task.domain.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageDTO {
    private Long id;
    private String name;
    private String type;
    private byte[] picByte;
}
