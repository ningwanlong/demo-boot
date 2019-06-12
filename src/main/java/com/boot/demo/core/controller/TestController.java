package com.boot.demo.core.controller;

import com.boot.demo.tools.ResultModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/excludeurl")
public class TestController extends BaseController {



    @RequestMapping("/test")
    @ResponseBody
    public String test(
            @RequestParam(defaultValue = "1")int ptype
    ) throws Exception {
        rm = new ResultModel();
        for (int i = 0; i < 500; i++) {
            logger.info("info日志....");
            logger.error("error日志....");
            logger.debug("debug日志....");
            System.out.println("123");
        }

        return toJson();
    }

}
