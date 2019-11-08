package com.appengine.task.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tool")
public class ToolController {

    @PostMapping(value = "/addComma")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    public String get(@RequestParam String res) {
        return addComma(res);
    }

    public String addComma(String args) {
        if (StringUtils.isEmpty(args)) {
            return "";
        }
        String result = "";
        char[] line;
        line = args.toCharArray();
        for (int i = 0; i < line.length; i++) {
            if (line[i] == 32) {
                result += ",";
            } else {
                result += line[i];
            }
        }
        return result;
    }

}
