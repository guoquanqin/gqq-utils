package com.example.enums;

/**
 * 请求类型
 *
 * @author gqq
 * @version 1.0, 2020/7/17 15:58
 */
public enum EnumContentType {
    /**
     * 请求类型
     */
    AVI("avi","video/x-msvideo"),
    MOV("mov","video/quicktime"),
    RMVB("rmvb","application/vnd.rn-realmedia-vbr"),
    RM("rm","application/vnd.rn-realmedia"),
    FLV("flv","video/x-flv"),
    MP4("mp4","video/mp4"),
    M3GP("3gp","video/3gpp"),
    WAV("wav","audio/wav"),
    MP3("mp3","audio/mpeg"),
    FLAC("flac","audio/x-flac"),
    AAC("aac","audio/aac"),
    JPG("jpg","image/jpeg"),
    PNG("png","image/png"),
    GIF("gif","image/gif"),
    BMP("bmp","image/bmp"),
    TXT("txt","text/plain"),
    DOC("doc","application/msword"),
    XLS("xls","application/vnd.ms-excel"),
    XLSX("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPT("ppt","application/vnd.ms-powerpoint"),
    PPTX("pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    PDF("pdf","application/pdf"),
    RAR("rar","application/octet-stream"),
    EXE("exe","application/octet-stream"),
    ZIP("zip","application/octet-stream"),
//    ZIP("zip","application/zip"),

    ;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 类型
     */
    private String contentType;

    EnumContentType(String suffix, String contentType) {
        this.suffix = suffix;
        this.contentType = contentType;
    }

    public static EnumContentType getInstance(String suffix) {
        for (EnumContentType entity : EnumContentType.values()) {
            if (entity.getSuffix().equals(suffix)) {
                return entity;
            }
        }
        return null;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
