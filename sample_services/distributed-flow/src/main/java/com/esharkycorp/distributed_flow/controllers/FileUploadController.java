package com.esharkycorp.distributed_flow.controllers;

import com.esharkycorp.distributed_flow.models.IngestionInfo;
import com.esharkycorp.distributed_flow.models.TranslationInfo;
import com.esharkycorp.distributed_flow.responses.IngestionResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractMessageQueue;
import com.hilabs.cdi.common.queuebuilder.dto.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.esharkycorp.distributed_flow.Constants.BASE_FOLDER_PATH;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    AbstractMessageQueue queue;

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadFileEndpoint(@RequestParam("file")MultipartFile file) throws IOException {
        Metadata metadata = new Metadata();
        metadata.setLastService("ORIGIN");
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            return new ResponseEntity<>("No File Uploaded", HttpStatus.BAD_REQUEST);
        }
        Path folderPath = Paths.get(BASE_FOLDER_PATH + '/' + metadata.getTraceId().toString());
        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            return new ResponseEntity<>("Couldn't Create Folder",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Path filePath = folderPath.resolve(file.getOriginalFilename());
            file.transferTo(filePath);
        } catch (IOException e) {
            return new ResponseEntity<>("File Upload Failed",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        TranslationInfo resp = TranslationInfo.builder().build();
        resp.setMetadata(metadata);
        queue.produce("file-translation", resp);
        return new ResponseEntity<>("File Uploaded Successfully",HttpStatus.OK);
    }
}
