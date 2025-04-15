// package org.nilesh.blogproject.Controller;

// import org.nilesh.blogproject.models.Account;
// import org.nilesh.blogproject.services.AccountService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;

// @Controller
// public class AccountController {

//     @Autowired
//     private AccountService accountService;

//     @GetMapping("/register")
//     public String register(Model model) {
//         model.addAttribute("account", new Account());
//         return "register";  
//     }

//     @PostMapping("/register")
//     public String registerUser(@ModelAttribute Account account) {
//         accountService.save(account);
//         return "redirect:/";
//     }

//     @GetMapping("/login")
//     public String loginPage() {
//         return "login";
//     }

//     @GetMapping("/profile")
//     public String profile() {
//         return "profile";
//     }
//     @GetMapping("/test")
//     public String test() {
//         return "test";
//     }
    
// }

package org.nilesh.blogproject.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.nilesh.blogproject.models.Account;
import org.nilesh.blogproject.services.AccountService;
import org.nilesh.blogproject.services.EmailService;
import org.nilesh.blogproject.utils.email.emaildetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;


@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;
 @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;



    @Value("${password.token.reset.timeout.minutes:10}")
    private int passwordTokenTimeout;
   
  

    // Show registration form
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new Account());
        return "register";  
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute Account account) {
        accountService.save(account);
        return "redirect:/login?success";  // Redirect to login page with success message
    }

    // Show login form
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/forgot-me")
    public String showForgotPasswordPage() {
        return "forgot-password"; // This should match the Thymeleaf HTML template name
    }

    @PostMapping("/forgot-me")
public String handleForgotPassword(@RequestParam("email") String email) {
    // Logic to send reset link to email
    System.out.println("Forgot password request for: " + email);
    return "redirect:/login"; // or a success page
}


    // Profile page (protected)
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated")
    public String profile(Model model, Principal principal) {
        String authUser="email";
        if(principal !=null){
            authUser=principal.getName();
        }
        Optional<Account> optionalAccount=accountService.findByEmailIgnoreCase(authUser); 
        if(optionalAccount.isPresent()){
            Account account=optionalAccount.get();
            model.addAttribute("account", account);
            return "profile";
        }else{
            return "redirect:/?error";
        }
        
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated")
    public String post_profile(@Valid @ModelAttribute Account account,BindingResult bindingResult,Principal principal) {
        if(bindingResult.hasErrors()){
           return "profile"; 
        }
        String authUser="email";
        if(principal !=null){
            authUser=principal.getName();
        }
        Optional<Account> optionalAccount=accountService.findByEmailIgnoreCase(authUser); 
       if(optionalAccount.isPresent()){
        Account account_id=accountService.fingById(account.getId()).get();
        account_id.setFirstname(account.getFirstname());
        account_id.setLastname(account.getLastname());
        account_id.setPassword(account.getPassword());
        accountService.save(account_id);
        return "redirect:/";
       
       }else{
        return "redirect:/?error";
       }
        
       
    }
   
   


    // Test page
    @GetMapping("/test")
    public String test() {
        return "test";
    }

//     // @GetMapping("/reset-password-form")
// public String showResetForm(@RequestParam("email") String email, Model model) {
//     model.addAttribute("email", email);
//     return "reset-password-form"; // Update the form
// }

// @PostMapping("/reset-password-form")
// public String handleResetPassword(@RequestParam("email") String email,
//                                   @RequestParam("password") String newPassword,
//                                   RedirectAttributes attributes) {
//     Optional<Account> optionalAccount = accountService.findByEmailIgnoreCase(email);

//     if (optionalAccount.isPresent()) {
//         Account account = optionalAccount.get();
//         account.setPassword(newPassword); // 🔐 Should be encoded
//         account.setPasswordResetOtp(null);
//         account.setPasswordResetOtpExpiry(null);
//         accountService.save(account);
//         attributes.addFlashAttribute("message", "Password reset successful!");
//         return "redirect:/login";
//     }

//     attributes.addFlashAttribute("error", "Account not found.");
//     return "redirect:/forgot-me";
// }

    

// // === SEND OTP ===
// @PostMapping("/reset_password")
// public String sendResetOtp(@RequestParam("email") String email, RedirectAttributes attributes) {
//     Optional<Account> optionalAccount = accountService.findByEmailIgnoreCase(email);

//     if (optionalAccount.isPresent()) {
//         Account account = optionalAccount.get();

//         // Generate OTP
//         String otp = String.valueOf(new Random().nextInt(899999) + 100000); // 6-digit OTP
//         account.setPasswordResetOtp(otp);
//         account.setPasswordResetOtpExpiry(LocalDateTime.now().plusMinutes(10));
//         accountService.save(account);

//         // Send email logic (replace with your email service)
//         System.out.println("Sending OTP to email: " + email + " => " + otp);

//         attributes.addFlashAttribute("email", email);
//         return "redirect:/verify-otp?email=" + email;
//     }

//     attributes.addFlashAttribute("error", "Email not found");
//     return "redirect:/forgot-me";
// }
// @GetMapping("/verify-otp")
// public String showVerifyOtpPage(@RequestParam("email") String email, Model model) {
//     model.addAttribute("email", email);
//     return "verify-otp"; // Create this HTML
// }

// @PostMapping("/verify-otp")
// public String verifyOtp(@RequestParam("email") String email,
//                         @RequestParam("otp") String otp,
//                         RedirectAttributes attributes) {
//     Optional<Account> optionalAccount = accountService.findByEmailIgnoreCase(email);

//     if (optionalAccount.isPresent()) {
//         Account account = optionalAccount.get();
//         if (account.getPasswordResetOtp().equals(otp) &&
//             account.getPasswordResetOtpExpiry().isAfter(LocalDateTime.now())) {
            
//             return "redirect:/reset-password-form?email=" + email;
//         } else {
//             attributes.addFlashAttribute("error", "Invalid or expired OTP.");
//             return "redirect:/verify-otp?email=" + email;
//         }
//     }

//     attributes.addFlashAttribute("error", "Account not found.");
//     return "redirect:/forgot-me";
// }


@PostMapping("/reset_password")
public String sendResetOtp(@RequestParam("email") String email, RedirectAttributes attributes) {
    Optional<Account> optionalAccount = accountService.findByEmailIgnoreCase(email);

    if (optionalAccount.isPresent()) {
        Account account = optionalAccount.get();

        // Generate 6-digit OTP
        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        account.setPasswordResetOtp(otp);
        account.setPasswordResetOtpExpiry(LocalDateTime.now().plusMinutes(10));
        accountService.save(account);
      String reset_message="This is  the reset password  OTP :"+otp;
      emaildetails Emaildetails=new emaildetails(account.getEmail(),reset_message,"Reset OTP is :");
      if(emailService.sendSimpleEmail(Emaildetails)== false){
        attributes.addFlashAttribute("message", "Erroe whie sending Email Try Again");
        return "redirect:/forgot-password";
      }
        // Replace with actual email service
        System.out.println("Sending OTP to email: " + email + " => " + otp);

        return "redirect:/verify-otp?email=" + email;
    }

    attributes.addFlashAttribute("error", "Email not found");
    return "redirect:/forgot-me";
}

@GetMapping("/verify-otp")
public String showVerifyOtpPage(@RequestParam("email") String email, Model model) {
    model.addAttribute("email", email);
    return "verify-otp";
}

@PostMapping("/verify-otp")
public String verifyOtp(@RequestParam("email") String email,
                        @RequestParam("otp") String otp,
                        RedirectAttributes attributes) {
    Optional<Account> optionalAccount = accountService.findByEmailIgnoreCase(email);

    if (optionalAccount.isPresent()) {
        Account account = optionalAccount.get();
        if (account.getPasswordResetOtp().equals(otp) &&
            account.getPasswordResetOtpExpiry().isAfter(LocalDateTime.now())) {

            return "redirect:/reset-password-form?email=" + email;
        } else {
            attributes.addFlashAttribute("error", "Invalid or expired OTP.");
            return "redirect:/verify-otp?email=" + email;
        }
    }

    attributes.addFlashAttribute("error", "Account not found.");
    return "redirect:/forgot-me";
}

@GetMapping("/reset-password-form")
public String showResetForm(@RequestParam("email") String email, Model model) {
    model.addAttribute("email", email);
    return "reset-password-form";
}

@PostMapping("/reset-password-form")
public String handleResetPassword(@RequestParam("email") String email,
                                  @RequestParam("password") String newPassword,
                                  RedirectAttributes attributes) {
    Optional<Account> optionalAccount = accountService.findByEmailIgnoreCase(email);

    if (optionalAccount.isPresent()) {
        Account account = optionalAccount.get();
        account.setPassword(newPassword); // 🔐 Use encoder in real apps
        account.setPasswordResetOtp(null);
        account.setPasswordResetOtpExpiry(null);
        accountService.save(account);
        attributes.addFlashAttribute("message", "Password reset successful!");
        return "redirect:/login";
    }

    attributes.addFlashAttribute("error", "Account not found.");
    return "redirect:/forgot-me";
}

}



    

    

