package com.example.demo.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@ToString
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
    @Builder.Default
    private Boolean isDelete = false;
    
    private String color;
	
	  @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "member_id")
	    private Member member;
	
	
	  @Column(name = "created_at", updatable = false)
		private LocalDateTime createdAt;

		@Column(name = "updated_at")
		private LocalDateTime updatedAt;

		@PrePersist
		protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
		    }

		@PreUpdate
		protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
		    }
}
