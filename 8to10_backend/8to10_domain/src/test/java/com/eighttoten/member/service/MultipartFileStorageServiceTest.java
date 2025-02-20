package com.eighttoten.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.exception.BadRequestException;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@DisplayName("파일 저장 서비스 테스트")
class MultipartFileStorageServiceTest {

    @Autowired
    MultipartFileStorageService multipartFileStorageService;

    @Test
    @DisplayName("ContentType이 image/ 로 시작하지 않으면 실패한다.")
    void saveImageFile_not_image() {
        //given
        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "testFile.jpg",
                "notimage/jpg",
                "Test image content".getBytes());

        //when,then
        assertThatThrownBy(() -> multipartFileStorageService.saveImageFile(imageFile))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("originalFilename이 없으면 실패한다.")
    void save_image_file_original_filename_null() {
        //given
        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                null,
                "image/jpg",
                "Test image content".getBytes());

        //when,then
        assertThatThrownBy(() -> multipartFileStorageService.saveImageFile(imageFile))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("이미지 파일을 저장한다")
    void save_image_file() throws IOException {
        //given
        String tempDir = System.getProperty("java.io.tmpdir") + "/testImageDir/";
        ReflectionTestUtils.setField(multipartFileStorageService, "imageDirectoryPath", tempDir);

        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "testFile.jpg",
                "image/jpg",
                "Test image content".getBytes());

        //when
        String imagePath = multipartFileStorageService.saveImageFile(imageFile);

        //then
        File savedFile = new File(imagePath);
        assertThat(savedFile.exists()).isTrue();

        if (savedFile.exists()) {
            savedFile.delete();
        }
    }
}