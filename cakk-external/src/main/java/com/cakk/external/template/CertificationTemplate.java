package com.cakk.external.template;

import com.cakk.external.api.CertificationApiExecutor;
import com.cakk.external.api.CertificationMessageExtractor;
import com.cakk.external.vo.CertificationMessage;

public class CertificationTemplate {
	private CertificationApiExecutor certificationApiExecutor;
	private CertificationMessageExtractor certificationMessageExtractor;
	

	public CertificationTemplate(
		CertificationApiExecutor certificationApiExecutor,
		CertificationMessageExtractor certificationMessageExtractor) {
		this.certificationApiExecutor = certificationApiExecutor;
		this.certificationMessageExtractor = certificationMessageExtractor;
	}

	public <T> void sendMessageForCertification(CertificationMessage certificationMessage, Class<T> messageType) {
		this.sendMessageForCertification(certificationMessage, certificationMessageExtractor,
			certificationApiExecutor, messageType);
	}

	public <T> void sendMessageForCertification(
		CertificationMessage certificationMessage,
		CertificationMessageExtractor certificationMessageExtractor,
		CertificationApiExecutor certificationApiExecutor,
		Class<T> messageType) {
		T extractMessage = certificationMessageExtractor.extract(certificationMessage, messageType);
		certificationApiExecutor.send(extractMessage, messageType);
	}
}
