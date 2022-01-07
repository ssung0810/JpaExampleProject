package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = getMember();
        Item item = getItem("JPA따라잡기", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);

        // then
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 개수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 시 주문한 개수만큼 수량이 줄어들어야 한다.", 8, item.getStockQuantity());
        assertEquals("주문 가격은 개수 * 수량이다.", 10000*orderCount, getOrder.getTotalPrice());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고를초과하여주문() throws Exception {
        // given
        Member member = getMember();
        Item item = getItem("JPA Study", 10000, 10);

        int orderCount = 11;

        // when
        orderService.order(member.getId(), item.getId(), orderCount);

        // then
        fail("재고 수량 부족으로 예외가 발생하여야 한다.");
    }

    @Test
    public void 상품취소() throws Exception {
        // given
        Member member = getMember();
        Item item = getItem("JPA Study", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancel(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("취소시 상품의 재고가 돌아와야한다.", 10, item.getStockQuantity());
        assertEquals("취소시 상품의 상태코드는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
    }

    private Item getItem(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        em.persist(item);
        return item;
    }

    private Member getMember() {
        Member member = new Member();
        member.setName("tester");
        member.setAddress(new Address("울산", "북구", "112"));
        em.persist(member);
        return member;
    }
}