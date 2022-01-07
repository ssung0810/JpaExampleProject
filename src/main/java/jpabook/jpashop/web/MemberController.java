package jpabook.jpashop.web;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createFrom(Model model) {
        model.addAttribute("memberForm", new MemberDto());

        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String createMember(@Valid MemberDto memberDto,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberDto.getCity(), memberDto.getStreet(), memberDto.getZipcode());

        Member member = new Member();
        member.setName(memberDto.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();

        model.addAttribute("members", members);

        return "members/memberList";
    }
}
