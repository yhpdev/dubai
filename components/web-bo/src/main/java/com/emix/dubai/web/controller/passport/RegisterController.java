package com.emix.dubai.web.controller.passport;

import com.emix.core.shiro.ShiroConstant;
import com.emix.core.utils.StringUtil;
import com.emix.dubai.business.entity.system.User;
import com.emix.dubai.business.pojo.ApplicationProperties;
import com.emix.dubai.business.service.system.UserService;
import com.emix.dubai.business.service.common.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author nikog
 */
@Controller
@RequestMapping(value = "/passport/register")
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping(method = RequestMethod.GET)
    public String registerForm() {
        return "passport/registerForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@Valid User user, RedirectAttributes redirectAttributes) {
        userService.registerUser(user);
        notificationService.sendRegisterNotification(user, applicationProperties);
        redirectAttributes.addFlashAttribute("user", user);
        return "passport/registerResult";
    }

    @RequestMapping(value = "validateLoginName")
    @ResponseBody
    public String validateLoginName(@RequestParam("loginName") String loginName) {
        if (userService.findUserByLoginName(loginName) == null) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "validateCaptcha")
    @ResponseBody
    public String validateCaptcha(@RequestParam("captcha") String captcha, HttpServletRequest request) {
        String captchaInSession = (String) request.getSession().getAttribute(ShiroConstant.CAPTCHA_SESSION_KEY);
        if (!StringUtil.isEmpty(captcha) && captcha.equalsIgnoreCase(captchaInSession)) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "validateEmail")
    @ResponseBody
    public String validateEmail(@RequestParam("email") String email) {
        if (userService.findUserByEmail(email) == null) {
            return "true";
        } else {
            return "false";
        }
    }

}
