package com.file.upload;

import com.file.upload.controllers.FileUploadController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_no_file_uploaded() throws Exception {

        MockMultipartFile file1 = new MockMultipartFile("file", "",
                "application/sql", this.getClass().getResourceAsStream(""));


        mockMvc.perform(MockMvcRequestBuilders.
                        multipart("/file/upload/")
                        .file(file1)
                        .queryParam("email", "bundy@mail.bg"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").
                        value("bundy@mail.bg has uploaded 0 files"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void test_single_file_uploaded_ok() throws Exception {

        MockMultipartFile file1 = new MockMultipartFile("file", "sql-update-table.sql",
                "application/sql", this.getClass().getResourceAsStream("/sql-update-table.sql"));


        mockMvc.perform(MockMvcRequestBuilders.
                        multipart("/file/upload/")
                        .file(file1).queryParam("email", "bundy@mail.bg"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").
                        value("bundy@mail.bg has uploaded 1 file"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void test_files_uploaded_ok() throws Exception {

        MockMultipartFile file1 = new MockMultipartFile("file", "sql-update-table.sql",
                "application/sql", this.getClass().getResourceAsStream("/sql-update-table.sql"));

        MockMultipartFile file2 = new MockMultipartFile("file", "miracle.txt", "text/plain",
                this.getClass().getResourceAsStream("/miracle.txt"));

        MockMultipartFile file3 = new MockMultipartFile("file", "Static Data", "text/plain",
                "test data".getBytes());


        mockMvc.perform(MockMvcRequestBuilders.
                        multipart("/file/upload/")
                        .file(file2).file(file3)
                        .file(file1).queryParam("email", "bundy@mail.bg"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").
                        value("bundy@mail.bg has uploaded 3 files"))
                .andDo(MockMvcResultHandlers.print());
    }

}
