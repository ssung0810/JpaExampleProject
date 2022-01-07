package jpabook.jpashop.web.items;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.web.dto.BookRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/items/new")
    public void itemSave(BookRequestDto requestDto,
                         HttpServletResponse response) throws IOException {
        Book book = Book.createBook(
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getStockQuantity(),
                requestDto.getAuthor(),
                requestDto.getIsbn());

        itemService.saveItem(book);

        response.sendRedirect("/items");
    }

    @PostMapping("/items/{itemId}/edit")
    public void updateItem(@ModelAttribute("from") BookRequestDto requestDto,
                             HttpServletResponse response) throws IOException {
        Book book = new Book();

        book.setId(requestDto.getId());
        book.setName(requestDto.getName());
        book.setPrice(requestDto.getPrice());
        book.setStockQuantity(requestDto.getStockQuantity());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());

        itemService.saveItem(book);

        response.sendRedirect("/items");
    }
}
