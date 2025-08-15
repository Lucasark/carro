package com.intro.sandbox.controller;

import com.intro.sandbox.entity.RegisterEntity;
import com.intro.sandbox.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final RegisterRepository repository;

    @GetMapping("/home")
    public String home(@RequestParam(value = "img", required = false)Boolean img, Model model) {
        log.info("Home endpoint accessed");
        List<RegisterEntity> entities = repository.findByRemoverEquals(Boolean.FALSE);
        entities.sort((a, b) -> {
            boolean novoA = Boolean.TRUE.equals(a.getNovo());
            boolean novoB = Boolean.TRUE.equals(b.getNovo());
            return Boolean.compare(novoB, novoA);
        });
        model.addAttribute("anuncios", entities);
        model.addAttribute("img", img);
        return "home";
    }


    @PostMapping("/favoritar/{id}")
    public String favoritar(@PathVariable String id) {
        repository.updateFavorito(id, true);
        return "redirect:/home";
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable String id) {
        repository.updateRemovido(id, true);
        return "redirect:/home";
    }
}
