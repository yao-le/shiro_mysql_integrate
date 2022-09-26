package com.example.shiro_mysql_integrate.api;

import com.example.shiro_mysql_integrate.dao.UserDao;
import com.example.shiro_mysql_integrate.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/users")
    @ResponseBody
    @RequiresPermissions("user:query")
    public List<User> getUser() {
        return (List<User>) userDao.findAll();
    }

    @PostMapping("/login")
    public String login() {
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated()) {
            return "redirect:/index";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }
}
