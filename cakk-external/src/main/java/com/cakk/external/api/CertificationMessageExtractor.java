package com.cakk.external.api;

import com.cakk.external.vo.CertificationMessage;

public interface CertificationMessageExtractor<T> {
	T extract(CertificationMessage certificationMessage);
}

