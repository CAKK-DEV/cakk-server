package com.cakk.external.template;

import com.cakk.external.api.CertificationApiExecutor;
import com.cakk.external.api.CertificationMessageExtractor;
import com.cakk.external.vo.CertificationMessage;

public class CertificationTemplate {

	private final CertificationApiExecutor certificationApiExecutor;
	private final CertificationMessageExtractor certificationMessageExtractor;

	public CertificationTemplate(
		CertificationApiExecutor certificationApiExecutor,
		CertificationMessageExtractor certificationMessageExtractor) {
		this.certificationApiExecutor = certificationApiExecutor;
		this.certificationMessageExtractor = certificationMessageExtractor;
	}

	public void sendMessageForCertification(CertificationMessage certificationMessage) {
		this.sendMessageForCertification(certificationMessage, certificationMessageExtractor, certificationApiExecutor);
	}

	public <T> void sendMessageForCertification(
		CertificationMessage certificationMessage,
		CertificationMessageExtractor certificationMessageExtractor,
		CertificationApiExecutor certificationApiExecutor) {
		T extractMessage = (T)certificationMessageExtractor.extract(certificationMessage);
		certificationApiExecutor.send(extractMessage);
	}
}
