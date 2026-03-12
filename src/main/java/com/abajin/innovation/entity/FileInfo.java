package com.abajin.innovation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    private String fileName;

    private Boolean directoryFlag;

    private String etag;

}
