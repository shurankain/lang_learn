package com.shuran.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shuran.model.ExerciseType;
import com.shuran.model.FourWordsDto;
import com.shuran.service.FourWordsService;

@Controller
public class FourWordsController {

    public static final String WORDS_DTO = "wordsDto";
    private FourWordsService fourWordsService;

    @Autowired
    public FourWordsController(FourWordsService fourWordsService) {
        this.fourWordsService = fourWordsService;
    }

    private FourWordsDto currentWords;


    @GetMapping("/fourWords/FT")
    public String wordFormFT(Model model) {
        FourWordsDto fourWordsDto = fourWordsService.getFourWords(ExerciseType.FT);
        model.addAttribute(WORDS_DTO, fourWordsDto);
        currentWords = fourWordsDto;
        return "fourWordsFT";
    }

    @GetMapping("/fourWords/TF")
    public String wordFormTF(Model model) {
        FourWordsDto fourWordsDto = fourWordsService.getFourWords(ExerciseType.TF);
        model.addAttribute(WORDS_DTO, fourWordsDto);
        currentWords = fourWordsDto;
        return "fourWordsTF";
    }

    @GetMapping("/fourWords/checkFT")
    public String checkFormFT(@RequestParam("option") Integer selectedOpt, Model model) {
        String info;
        if (currentWords.getOptions().get(selectedOpt).equals(currentWords.getTranslation())) {
            return wordFormFT(model);
        } else {
            info = "Wrong! " + currentWords.getForeign() + " = " + currentWords.getTranslation();
        }

        model.addAttribute(WORDS_DTO, currentWords);
        model.addAttribute("info", info);
        return "fourWordsFT";
    }

    @GetMapping("/fourWords/checkTF")
    public String checkFormTF(@RequestParam("option") Integer selectedOpt, Model model) {
        String info;
        if (currentWords.getOptions().get(selectedOpt).equals(currentWords.getForeign())) {
            return wordFormTF(model);
        } else {
            info = "Wrong! " + currentWords.getTranslation() + " = " + currentWords.getForeign();
        }

        model.addAttribute(WORDS_DTO, currentWords);
        model.addAttribute("info", info);
        return "fourWordsTF";
    }
}
