package practice.cafekiosk.spring.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import practice.cafekiosk.spring.api.service.order.OrderService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderApiController.class)
class OrderApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001", "002", "003"));

        // when & then
        mockMvc.perform(post("/api/orders/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("주문을 생성할 때 상품번호는 1개 이상이어야 한다.")
    @Test
    void createProductWithoutProductNumber() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest(List.of());

        // when & then
        mockMvc.perform(post("/api/orders/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품번호 리스트는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}