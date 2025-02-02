package com.shuran.controller;

import com.shuran.model.Word;
import com.shuran.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/word")
    public String wordForm(Model model) {
        model.addAttribute("word", new Word());
        return "word";
    }

    @PostMapping("/word")
    public String wordSubmit(@ModelAttribute Word word, Model model) {
        model.addAttribute("word", new Word());
        model.addAttribute("info", wordService.processWordSaving(word));
        return "word";
    }

}