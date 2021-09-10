package com.example.util;

import com.example.enums.EnumContentType;
import com.example.param.Result;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件处理类
 *
 * @author gqq
 * @version 1.0, 2021/9/10 16:14
 */
@Slf4j
public class FileUtil {

    private static final Integer BUFFER_SIZE = 1024;

    /**
     * 调用文件路径下载
     *
     * @param urlString   : 文件路径
     * @param fileName    : 带后缀文件名
     * @param response    : 响应体
     * @param directory   : 文件保存地址
     * @param contentType : 内容类型
     * @return
     * @author gqq 2020/7/18 - 16:04
     **/
    public static String fileDownload(String urlString, String fileName, HttpServletResponse response, String directory, String contentType) {
        String resultFileName = directory + File.separator + fileName;
        File dir = new File(directory);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (!result) {
                log.info("文件夹创建失败");
                return resultFileName;
            }
        }
        try {
            response.setContentType(contentType);
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            //超时响应时间为10秒
            con.setConnectTimeout(10 * 1000);
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 输入流
            inputStream = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            outputStream = new FileOutputStream(resultFileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                outputStream.write(bs, 0, len);
            }
        } catch (Exception e) {
            log.error("下载失败");
        }
        return resultFileName;
    }

    /**
     * 获取二进制字节码
     * @param fileUrl 文件网络路径
     * @return
     */
    public static byte[] getByte(String fileUrl) {
        InputStream inputStream = null;
        try {
            // 构造URL
            URL url = new URL(fileUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            //超时响应时间为10秒
            con.setConnectTimeout(10 * 1000);
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 输入流
            inputStream = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (Exception e) {
            log.error("转化失败");
        } finally {
            try {
                // 完毕，关闭所有链接
                inputStream.close();
            } catch (Exception e2) {
                log.error("转化失败");
            }
        }
        return null;
    }

    /**
     * 多结果文件下载
     *
     * @param list     : 文件集合
     * @param response : 响应体
     * @param dri      : 保存路径
     * @return
     * @author 2020/7/20 - 11:02
     **/
    public static String zipDownloadResult(List<Result> list, HttpServletResponse response, String dri) {
        // 要打包的文件列表
        List<File> fileList = Lists.newArrayList();
        // 文件名
        StringBuffer finallfileName = new StringBuffer();
        ZipOutputStream zos = null;
        OutputStream out = null;
        File srcFile = null;
        try {
            // 下载文件
            for (Result result : list) {
                String fileName = "";
                if (StringUtils.isBlank(result.getResultFileName())) {
                    Date time = new Date();
                    fileName = time.getTime() + "\\." + result.getFileType();
                } else {
                    fileName = result.getResultFileName();
                }
                String contentType = EnumContentType.getInstance(result.getFileType()).getContentType();
                // doc
                docDownload("测试", fileName, response, dri);
                String resultFileName = dri + File.separator + fileName;
                srcFile = new File(resultFileName);
                fileList.add(srcFile);
            }
            long start = System.currentTimeMillis();
            String now = DateUtil.convert(new Date(), DateUtil.FORMAT5);
            response.setContentType("application/x-zip-compressed");
            response.setHeader("Content-disposition", "attachment;filename=" + now + ".zip");
            finallfileName.append(now).append(".zip");
            log.info("文件名称：{}", now);

            out = response.getOutputStream();
            zos = new ZipOutputStream(out);

            for (File file : fileList) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(file.getName()));
                int len;
                FileInputStream in = new FileInputStream(file);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            log.info("压缩完成，耗时：{}", System.currentTimeMillis() - start);
            out.flush();
        } catch (IOException e) {
            log.info(e.getMessage());
            log.error("下载失败");
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    log.info(e.getMessage());
                    log.error("下载失败");
                }
            }
            if (out != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    log.info(e.getMessage());
                    log.error("下载失败");
                }
            }
        }
        return finallfileName.toString();
    }


    /**
     * doc文件下载
     *
     * @param content
     * @param fileName
     * @param response
     * @param dri
     * @return
     */
    public static String docDownload(String content, String fileName, HttpServletResponse response, String dri) {
        String resultFileName = dri + File.separator + fileName;
        try {
            response.setContentType("application/msword;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            FileOutputStream outputStream = new FileOutputStream(resultFileName);
            outputStream.write(content.getBytes("UTF-8"));
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * txt文件下载
     *
     * @param content
     * @param fileName
     * @param response
     * @param dri
     * @return
     */
    public static String txtDownload(String content, String fileName, HttpServletResponse response, String dri) {
        String resultFileName = dri + File.separator + fileName;
        try {
            response.setContentType("text/plain;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            FileOutputStream outputStream = new FileOutputStream(resultFileName);
            outputStream.write(content.getBytes("UTF-8"));
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

}
