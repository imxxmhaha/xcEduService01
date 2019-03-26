package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author xxm
 * @create 2019-03-25 21:56
 */
@Service
public class FileSystemService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private TrackerClient trackerClient;

    @Autowired
    private FileSystemRepository fileSystemRepository;
    /**
     * 文件上传
     *
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String filetag,
                                   String businesskey,
                                   String metadata) {
        if(null == multipartFile){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }

        // 第一步:将文件上传到fastDFS中,得到一个文件id
        String fileId = fdfs_upload(multipartFile);
        if(StringUtils.isEmpty(fileId)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }

        // 第二步,将文件id及其他文件信息存储到mongodb中
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());
        if(!StringUtils.isEmpty(metadata)){
            try {
                Map map = JSON.parseObject(metadata, Map.class);
                fileSystem.setMetadata(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fileSystemRepository.save(fileSystem);
        //return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
        return UploadFileResult.success(fileSystem);
    }

    //上传文件到fastDFS中
    private String fdfs_upload(MultipartFile multipartFile) {
        InputStream inputStream = null;
        String fileId = null;
        try {
            inputStream = multipartFile.getInputStream();
            //得到文件的原始名称
            String originalFilename = multipartFile.getOriginalFilename();
            long size = multipartFile.getSize();
            //得到文件的扩展名
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            StorePath storePath = storageClient.uploadFile(inputStream, size, ext, null);
            fileId = storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileId;
    }
}
