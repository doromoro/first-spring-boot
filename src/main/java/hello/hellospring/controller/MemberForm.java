package hello.hellospring.controller;

public class MemberForm {
    // post를 하면 input의 name속성이랑 일치하는 "name"을 보고 스프링이 값을 넣어준다.
    // spring 이 setName()을 호출해서 넣어주는 것임.
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
