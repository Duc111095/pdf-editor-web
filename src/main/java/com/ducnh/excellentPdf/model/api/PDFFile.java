package com.ducnh.excellentPdf.model.api;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PDFFile {
	@Schema(description = "The input PDf file")
	private MultipartFile fileInput;

}
