package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * spring을 실행할 때 스프링 컨테이너와 스프링 통이 생기는데,
 * 이 어노테이션을 사용하면 이 갹체를 컨테이너 안에 넣어두고 스프링이 관리한다.
 * 이를 스프링 컨테이너에서 스프링 빈을 관리한다 라고 표현한다.
 *
 * 컨트롤러는 spring config로 관리할 수가 없다. 어차피 스프링 컨테이너에서 관리하는 녀석이라
 * 따로 빠질 수가 없음. 어쩔 수 없이 컴포넌트 스캔을 하게 된다.
 */
@Controller
public class MemberController {

    /**
     * 여기서 MemberService 객체를 새로 생성하게 된다면 문제가 생김
     * 다른 컨트롤러에서도 이 서비스를 사용할 수 있을 텐데 그럼 그때마다 객체 생성?
     * 하나만 생성해두고 그냥 돌려 쓰는 것이 좋다.
     * 그럼 스프링 컨테이너에 등록만 해두면 된다. 그럼 부가적인 효과도 많이 얻을 수 있다.
     */
    private final MemberService memberService;

    /**
     * 위에서 Controller가 붙었기 때문에 spring container에서 이미 관리를 하게 된다.
     * 그 때 생성을 하고 컨테이너에 넣어서 관리하니 생성자를 호출하게 되고
     * 생성자에 autowired가 있다면 스프링이 "스프링 컨테이너에 있는" 멤버 서비스를 연결시켜준다.
     * 여기서 이 어노테이션만 있으면 문제가 autowired가 스프링 빈으로 등록되어있지 않아서 오류가 생긴다.
     * 스프링이 멤버 서비스를 알 수 있는 방법이 없는 것이다.
     * @Service를 MemberService에 추가해주면 이를 인식할 수 있게 되고 autowired가 동작하게 된다.
     * repository도 똑같이 적용
     * controller에서 요청을 받고 service에서 비즈니스 로직을 수행하고, repository에서 데이터를 저장하고 관리하고
     * 이는 굉장히 정형화된 패턴이다.
     *
     * 스프링 컨테이너에서 이 빈들을 연결해주는 것이(autowired) 의존관계 주입, DI이다.
     */
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // rest controller가 아니여서 그런지 아마 어디로 보내주고 model.addAtribute 이런거 해줘야 하나보다.
    // 내가 만든 코드
//    @GetMapping("/members")
//    public List<Member> memberList() {
//        return memberService.findMembers();
//    }
//
//    @PostMapping("/members/join")
//    public Long memberJoin(@RequestBody String name) {
//        Member member = new Member();
//        member.setName(name);
//        return memberService.join(member);
//        // 예외처리가 있어서 한번 확인해야 하는데 안해도 되려나?
//    }

    // html파일로 보내주는 역할 (url로 지금 보내는 코드가 home에 있음 -> get)
    @GetMapping("/members/join")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/join")
    public String create(MemberForm form){
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
