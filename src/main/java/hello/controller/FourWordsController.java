package hello.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hello.Word;
import hello.service.FourWordsService;

@Controller
public class FourWordsController {

    private FourWordsService fourWordsService;

    @Autowired
    public FourWordsController(FourWordsService fourWordsService) {
        this.fourWordsService = fourWordsService;
    }

    @GetMapping("/fourWords")
    public String wordForm(Model model) {
        model.addAttribute("word", new Word()); //Add existing words

        List<Word> wordList = fourWordsService.getFourWords();
        if(wordList.size() == 4){
            model.addAttribute("question", wordList.get(0).getForeign());
            model.addAttribute("answer", wordList.get(0).getTranslation());
            model.addAttribute("wrong1", wordList.get(1).getTranslation());
            model.addAttribute("wrong2", wordList.get(2).getTranslation());
            model.addAttribute("wrong3", wordList.get(3).getTranslation());
        }
        return "fourWords";
    }
}
