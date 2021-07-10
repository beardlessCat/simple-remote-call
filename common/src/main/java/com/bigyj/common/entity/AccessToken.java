package com.bigyj.common.entity;

import lombok.Data;

@Data
public class AccessToken {
	private String accessToken ;
	private long tokenAvailableTime;
}
