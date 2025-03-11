package com.ducnh.excellentPdf.model.api.converters;

import com.ducnh.excellentPdf.model.api.PDFFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HTMLToPdfRequest extends PDFFile {
	@Schema(
			description = "Zoom level for displaying the website. Default is '1'.",
			defaultValue = "1")
	private float zoom;
}
