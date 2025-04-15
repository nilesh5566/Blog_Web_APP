package org.nilesh.blogproject.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email")
    @NotEmpty(message = "Eamil Missing")
    private String email;
    @NotEmpty(message = "Password Missing")
    private String password;
    @NotEmpty(message = "First Name Missing")
    private String firstname;
    @NotEmpty(message = "Last Name Missing")
    private String lastname;
   
//     private String gender;
//    @Min(value = 18)
//    @Max(value = 99)
//     private int age;
//     @DateTimeFormat(pattern = "yyyy-MM-dd")
//     @NotNull(message = "DOB Missing")
//     private LocalDate date_of_birth;
//     private String photo;
  
    private String role;

    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    private String password_reset_token;

    private LocalDateTime password_reset_expiry;
    private String passwordResetOtp;
    private LocalDateTime passwordResetOtpExpiry;
    @ManyToMany(fetch = FetchType.EAGER)  // Changed to EAGER for authorities
    @JoinTable(
        name = "account_authority",
        joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();  // Fixed typo (Authority) and renamed to conventional name



}