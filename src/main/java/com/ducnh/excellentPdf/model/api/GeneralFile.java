package com.ducnh.excellentPdf.model.api;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class GeneralFile {
	
	@Schema(description = "The input file")
	public MultipartFile fileInput;
}
