package com.flyright.flyright.controller;

import com.flyright.flyright.model.Role;
import com.flyright.flyright.model.User;
import com.flyright.flyright.model.viewmodels.RegisterUserModel;
import com.flyright.flyright.repository.RoleRepository;
import com.flyright.flyright.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    final
    PasswordEncoder passwordEncoder;
    final UserRepository userRepository;
    final
    RoleRepository roleRepository;
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/dashboards/admin")
    public String dashboard(Model model) {
        return "dashboard/dashboard";
    }

    @GetMapping("/users/feedback")
    public String contact(Model model) {
        return "user/contact";
    }

    @GetMapping("/users/create")
    public String create(Model model){
        return "user/sign_up";
    }
    @PostMapping(value = "/users/register")
    public String register(Model model, RedirectAttributes redirectAttributes, RegisterUserModel registerUserModel){
        //String regex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8,20}$";
        //Pattern p = Pattern.compile(regex);
        // Matcher m = p.matcher(registerUserModel.getPassword());
        if(!registerUserModel.getPassword().equals(registerUserModel.getConfirmPassword())){
            redirectAttributes.addAttribute("error","Password does not match ");
        } else if( userRepository.existsByUsername(registerUserModel.getUserName())){
            redirectAttributes.addAttribute("error","User with same id already exist ");
        }
        else if( registerUserModel.getPassword().isBlank()||registerUserModel.getPassword().isEmpty()){
            redirectAttributes.addAttribute("error","Password can not be empty or blank ");
        }

        //       else if( !m.matches()){
//            redirectAttributes.addAttribute("error","Password is not strong enough");
//        }
        else{
            User u = new User();
            u.setUsername(registerUserModel.getUserName());
            u.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
            Optional<Role> optionalRole= roleRepository.findByName("USER");
            if(optionalRole.isPresent()) {
                Role role =optionalRole.get();
                List<Role> roleList = new ArrayList<>();
                roleList.add(role);
                u.setRoles(roleList);
            }
            userRepository.save(u);
            //redirectAttributes.addAttribute("error","");
            return "redirect:/login";
        }
        return "redirect:/users/create";
    }

    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    public String users(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    @RequestMapping(value = "/users/userRole/{id}", method = RequestMethod.GET)
    public String showAddRoleForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", userRepository.findById(id).get());
        return "user/addRole";
    }

    @RequestMapping(value = "/users/addNewRole", method = RequestMethod.POST)
    public String updateRole(Model model, @RequestParam long id, @RequestParam String name) {

        User u= userRepository.findById(id).get();
        Role role = roleRepository.findByName(name).get();

        List<Role> roleList = u.getRoles();
        roleList.add(role);
        u.setRoles(roleList);


        userRepository.save(u);

        return "redirect:/users/list";
    }
}
