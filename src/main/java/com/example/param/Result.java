package com.example.param;

import java.io.Serializable;
import lombok.Data;

/**
 * TODO 添加类的描述
 *
 * @author gqq
 * @version 1.0, 2021/9/10 16:36
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 3481088251938503560L;

    /**
     * 文件名
     */
    private String resultFileName;

    /**
     * 文件
     */
    private String fileType;
}
