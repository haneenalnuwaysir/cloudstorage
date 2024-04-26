package org.springframework.boot.cloudstorage.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.boot.cloudstorage.model.File;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    File getFile(String fileName);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllFiles(Integer userId);

    @Insert("INSERT INTO FILES (fileId, filename, contenttype, filesize, userid, filedata) VALUES(#{fileId}, #{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteFile(Integer fileId);
}
