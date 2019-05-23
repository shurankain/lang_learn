package com.shuran.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shuran.model.FourWordsDto;
import com.shuran.service.FourWordsService;

@Controller
public class FourWordsController {

    private FourWordsService fourWordsService;

    @Autowired
    public FourWordsController(FourWordsService fourWordsService) {
        this.fourWordsService = fourWordsService;
    }

    private FourWordsDto currentWords;


    @GetMapping("/fourWords")
    public String wordForm(Model model) {
        FourWordsDto fourWordsDto = fourWordsService.getFourWords();
        model.addAttribute("wordsDto", fourWordsDto);
        currentWords = fourWordsDto;
        return "fourWords";
    }

    @PostMapping("/fourWords/check")
    public String checkForm(@ModelAttribute("outDto") FourWordsDto outDto,
                            Model model) {
        String info;
        if(outDto.getSelectedValue().equals(currentWords.getAnswer())){
            info = "Correct!";
        }else {
            info = "Wrong!" + currentWords.getQuestion() + " = " + currentWords.getAnswer();
        }

        model.addAttribute("wordsDto", currentWords);
        model.addAttribute("info", info);
        return "fourWords";
    }
}
