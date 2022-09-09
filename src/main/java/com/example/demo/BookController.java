package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class BookController {
	@Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/index")
    public String index(Model model) {
        String sql = "SELECT * FROM books";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        model.addAttribute("bookList", list);
        return "books/index";
    }
    
    @GetMapping("/form")
    public String form(@ModelAttribute BookForm bookForm) {
    	return "books/form";
    }
    
    //新規入力データの保存
    @PostMapping("/index")
    public String create(BookForm bookForm) {
    	String sql = "INSERT INTO books(title, body) VALUES(?, ?);";
    	jdbcTemplate.update(sql, bookForm.getTitle(), bookForm.getBody());
    	return "redirect:/index";
    }
    
    //詳細画面
    @GetMapping("/books/{id}")
    public String show(@ModelAttribute BookForm bookForm, @PathVariable int id) {
    	String sql = "SELECT * FROM books where id = " + id;
    	Map<String, Object> map = jdbcTemplate.queryForMap(sql);
    	bookForm.setId((int)map.get("id"));
    	bookForm.setTitle((String)map.get("title"));
    	bookForm.setBody((String)map.get("body"));
    	return "books/show";
    	
    }
    
    @GetMapping("/edit/{id}")
    public String edit(@ModelAttribute BookForm bookForm, @PathVariable int id) {
    	String sql = "SELECT * FROM books where id = " + id;
    	Map<String, Object> map = jdbcTemplate.queryForMap(sql);
    	bookForm.setId((int)map.get("id"));
    	bookForm.setTitle((String)map.get("title"));
    	bookForm.setBody((String)map.get("body"));
    	return "books/edit";
    }
    
    //データを更新
    @PostMapping("/edit/{id}")
    public String update(BookForm bookForm, @PathVariable int id) {
    	String sql = "UPDATE books SET title = ?, body = ? where id = " + id;
    	jdbcTemplate.update(sql, bookForm.getTitle(), bookForm.getBody());
    	return "redirect:/books/{id}";
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
    	String sql = "DELETE from books where id = " + id;
    	jdbcTemplate.update(sql);
    	return "redirect:/index";
    }
}
