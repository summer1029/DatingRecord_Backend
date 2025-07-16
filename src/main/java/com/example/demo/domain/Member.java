package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@ToString(exclude = "friendships")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nm;
	
	@Column(unique = true)
	private String userEmail;
	
	private String password;
	
	// 나의 친구 목록(친구 관계 엔티티 리스트)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL ,orphanRemoval = true)
    @JsonIgnore
    private Set<Friendship> friendships = new HashSet<>();

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
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    Member member = (Member) o;
	    return Objects.equals(id, member.id);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
	}
}
