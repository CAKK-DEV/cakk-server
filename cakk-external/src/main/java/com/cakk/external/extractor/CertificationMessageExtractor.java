package com.cakk.external.extractor;

import com.cakk.external.vo.CertificationMessage;

public interface CertificationMessageExtractor<T> {
	T extract(CertificationMessage certificationMessage);
}

