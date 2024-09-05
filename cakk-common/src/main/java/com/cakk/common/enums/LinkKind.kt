package com.cakk.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LinkKind {

	WEB("web"), KAKAOTALK("kakaotalk"), INSTAGRAM("instagram");

	private final String value;
}
