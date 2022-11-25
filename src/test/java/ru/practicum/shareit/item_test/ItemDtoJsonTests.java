package ru.practicum.shareit.item_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemResult;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemDtoJsonTests {
    @Autowired
    JacksonTester<ItemResult> json;

    @Test
    void testItemResult() throws Exception {
        ItemResult item = new ItemResult(
                1L,
                "item",
                "desc",
                true,
                null,
                null,
                new ArrayList<>(),
                null
        );

        JsonContent<ItemResult> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }
}
