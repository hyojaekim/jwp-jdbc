package slipp.controller;

import nextstep.mvc.JspView;
import nextstep.mvc.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.dto.UserCreatedDto;
import slipp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService = new UserService();

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserCreatedDto userCreatedDto = new UserCreatedDto(
            req.getParameter("userId"),
            req.getParameter("password"),
            req.getParameter("name"),
            req.getParameter("email"));
        logger.debug("User : {}", userCreatedDto);

        userService.save(userCreatedDto);
        return redirect("/");
    }

    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(
            JspView.DEFAULT_REDIRECT_PREFIX + path));
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return redirect("/users/loginForm");
        }

        ModelAndView mav = new ModelAndView(new JspView("/user/list.jsp"));
        mav.addObject("users", userService.findAll());
        return mav;
    }
}
