package com.cakk.domain.entity.user;

import java.time.LocalTime;

import org.springframework.context.ApplicationEventPublisher;

import com.cakk.domain.dto.param.user.CertificationParam;
import com.cakk.domain.entity.audit.AuditEntity;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.event.EventMapper;
import com.cakk.domain.event.user.CertificationEvent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "business_information")
public class BusinessInformation extends AuditEntity {

	@Id
	@Column(name = "shop_id")
	private Long id;

	@Column(name = "operation_day", nullable = false, length = 7)
	private String operationDay;

	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;

	@Column(name = "business_number", length = 20)
	private String businessNumber;

	@OneToOne
	@MapsId
	@JoinColumn(name = "shop_id")
	private CakeShop cakeShop;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@Builder
	public BusinessInformation(
		CakeShop cakeShop,
		String operationDay,
		LocalTime startTime,
		LocalTime endTime,
		String businessNumber,
		User user
	) {
		this.id = cakeShop.getId();
		this.cakeShop = cakeShop;
		this.operationDay = operationDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.businessNumber = businessNumber;
		this.user = user;
	}

	public void requestCertificationToApp(CertificationParam param, ApplicationEventPublisher publisher) {
		CertificationEvent certificationEvent;

		if (isExistMyCakeShop()) {
			certificationEvent = EventMapper.supplyCertificationInfoWithCakeShopInfo(param, cakeShop);
		} else {
			certificationEvent = EventMapper.supplyCertificationInfo(param);
		}
		publisher.publishEvent(certificationEvent);
	}

	public void promotedByShopKeeper(User shopKeeper) {
		user = shopKeeper;
	}

	private boolean isExistMyCakeShop() {
		return cakeShop != null;
	}
}
