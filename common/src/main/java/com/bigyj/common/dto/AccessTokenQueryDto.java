package com.bigyj.common.dto;

import lombok.Data;

@Data
public class AccessTokenQueryDto {
	private String secret ;
	private String clientId ;
}
