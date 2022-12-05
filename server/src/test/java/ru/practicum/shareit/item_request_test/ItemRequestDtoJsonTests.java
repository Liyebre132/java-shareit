package ru.practicum.shareit.item_request_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestResult;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestDtoJsonTests {
    @Autowired
    JacksonTester<ItemRequestResult> json;

    @Test
    void testItemRequestResult() throws Exception {
        ItemRequestResult itemRequestDto = new ItemRequestResult(
                1L,
                "desc",
                LocalDateTime.now(),
                new ArrayList<>()
        );

        JsonContent<ItemRequestResult> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("desc");
    }
}
