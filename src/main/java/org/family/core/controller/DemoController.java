package org.family.core.controller;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {


    @GetMapping(value = "/testGet" , produces = "application/json")
    @ResponseBody
    public Map getTestGet() {

        HashMap<Object, Object> hashMap = Maps.newHashMap();
        hashMap.put("message", "succeed");
        return hashMap;
    }

    @PostMapping(value = "/testPost", produces = "application/json")
    @ResponseBody
    public Map getTestPost(@RequestBody Map bo) {
        HashMap<Object, Object> hashMap = Maps.newHashMap();
        hashMap.put("message", "succeed");
        hashMap.put("requestBody", bo);
        hashMap.put("query", "parameterMap");
        return hashMap;
    }

}
