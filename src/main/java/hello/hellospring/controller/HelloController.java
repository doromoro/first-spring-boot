package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data","hello");

        // templates의 hello.html을 찾아서 렌더링을 하게 된다.
        return "hello";
    }

    @GetMapping("hello-mvc")
    // 이따구로 하니까 request param이 없으면 에러 페이지가 나와버리네?
    public String helloMVC(@RequestParam(value = "name", required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "hello-tempalte";
    }

    @GetMapping("hello-string")
    @ResponseBody
    // 이 경우 required는 원래 디폴트가 true임.
    public String helloString(@RequestParam("name") String nam){
        // 이 경우 데이터를 그대로 내려주게 된다. 왜 이런 차이점이 발생할까? 아마 responsebody 어노테이션 때문일 것이다.
        return "hello " + nam;
    }

    @GetMapping("hello-api")
    @ResponseBody
    // 이렇게 Hello라는 객체를 반환하게 되면 스프링 부트는 현재 디폴트로 json 형식으로 객체를 반환해준다.
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    // java문법 중 하나로 클래스 안에서 객체를 선언할 때 사용하는 방식임 찾아봐야겠지?
    static class Hello {
        private String name;

        // cmd + n 을 이용해서 getter, setter 보일러 플레이트 코드 만들었음
        // 이 getter, setter를 자바 빈 규약이라고 함. (자바 빈 표준 형식)
        // private 요소에 접근하기 위한 getter, setter를 쓰는 것이다.
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
