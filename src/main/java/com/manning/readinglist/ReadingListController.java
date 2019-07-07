package com.manning.readinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/readingList")
public class ReadingListController {

    private ReadingListRepository readingListRepository;

    @Autowired
    public ReadingListController(ReadingListRepository readingListRepository) {
        this.readingListRepository = readingListRepository;

    }

    /**
     * 处理/{reader}上的Http GET请求，根据路径里指定的读者，从仓库获取Book列表
     * 将这个列表塞入模型，用的键是books
     * 最后返回readingList作为呈现模型的试图逻辑名称
     * @param reader
     * @param model
     * @return
     */
    @RequestMapping(value = "/{reader}", method = RequestMethod.GET)
    public String readersBooks(@PathVariable("reader") String reader, Model model) {
        List<Book> readingList = readingListRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("books", readingList);
        }
        return "readingList";
    }

    /**
     * 处理/{reader}上的Http POST请求，将请求正文里的数据绑定到一个Book对象上
     * 该方法把Book对象的reader属性设置为读者的姓名，随后通过仓库的save()方法保存修改后的Book对象
     * 最后重定向到/{reader}（控制器中的另一个发方法会处理该请求）
     * @param reader
     * @param book
     * @return
     */
    @RequestMapping(value = "/{reader}", method = RequestMethod.POST)
    public String addToReadingList(@PathVariable("reader") String reader, Book book) {
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:/readingList/{reader}";
    }
}
