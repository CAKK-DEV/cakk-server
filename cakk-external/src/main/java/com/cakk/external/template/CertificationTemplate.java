package com.cakk.external.template;

import lombok.RequiredArgsConstructor;

import com.cakk.external.api.CertificationApiExecutor;
import com.cakk.external.api.CertificationMessageExtractor;
import com.cakk.external.vo.CertificationMessage;

@RequiredArgsConstructor
public class CertificationTemplate {

	private final CertificationApiExecutor certificationApiExecutor;
	private final CertificationMessageExtractor certificationMessageExtractor;

	public void sendMessageForCertification(CertificationMessage certificationMessage) {
		this.sendMessageForCertification(certificationMessage, certificationMessageExtractor, certificationApiExecutor);
	}

	public <T> void sendMessageForCertification(
		CertificationMessage certificationMessage,
		CertificationMessageExtractor certificationMessageExtractor,
		CertificationApiExecutor certificationApiExecutor
	) {
		T extractMessage = (T)certificationMessageExtractor.extract(certificationMessage);
		certificationApiExecutor.send(extractMessage);
	}
}

