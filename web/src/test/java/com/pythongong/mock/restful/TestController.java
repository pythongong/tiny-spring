package com.pythongong.mock.restful;

import com.pythongong.annotation.GetMapping;
import com.pythongong.annotation.PathVariable;
import com.pythongong.annotation.PostMapping;
import com.pythongong.annotation.RequestBody;
import com.pythongong.annotation.RequestParam;
import com.pythongong.annotation.ResponseBody;
import com.pythongong.annotation.RestController;
import com.pythongong.mock.jdbc.TestUser;

@RestController
public class TestController {
    @GetMapping("/test")
    @ResponseBody
    public String handleGet() {
        return "GET Response";
    }

    @PostMapping("/test")
    @ResponseBody
    public String handlePost() {
        return "POST Response";
    }

    @GetMapping("/json")
    @ResponseBody
    public TestResponse handleJson() {
        return new TestResponse("Test");
    }

    @ResponseBody
    public String getUser(@PathVariable("id") String id) {
        return "User:" + id;
    }

    @ResponseBody
    public String searchUsers(@RequestParam("name") String name) {
        return "Search:" + name;
    }

    @ResponseBody
    public String createUser(@RequestBody TestUser user) {
        return "Created:" + user.getName();
    }

    @ResponseBody
    public String invalidParam(@RequestParam("") String invalid) {
        return invalid;
    }
}
