package com.atypon.finalproject.database;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileManger {
    void exportDataBaseSchema(HttpServletResponse response);

    void updateDataBaseFile();

    void importDataAndClearExisting(MultipartFile file);

}
