package br.com.acrossatto.pocvalidation.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false, of = "requestId")
@Table(name = "TB_REQUISICAO")
public class Request implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REQUEST_ID", length = 12, nullable = false)
	private Long requestId;

	@Column(name = "ACCOUNT_ID", precision = 4, nullable = false)
	private Long accountId;

	@Column(name = "CHANNEL_ID", length = 40, nullable = false)
	private String channelId;

	@Column(name = "CPF", length = 14, nullable = false)
	private String cpf;

	@Column(name = "DT_CADASTRO", nullable = false)
	private LocalDateTime createdDate;

	@Column(name = "DT_ATUALIZACAO", nullable = true)
	private LocalDateTime updatedDate;
	
	@PrePersist
	public void prePersist() {
		this.createdDate = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedDate = LocalDateTime.now();
	}

}